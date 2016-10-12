/*
 * Copyright (C), 2002-2015, 苏宁易购电子商务有限公司
 * FileName: Tool.java
 * Author:   张超(14080608)
 * Date:     2015-8-18 上午10:25:19
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.fred.code.tool;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

import com.fred.code.domain.CodeDatabase;
import com.fred.code.framework.util.ZipUtil;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 张超(14080608)
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class Tool {

	/**
	 * 代码存放路径
	 */
	private static final String CODE_PATH = "/usr/local/nginx/html/code/";

	private static final String DATABASE_TYPE_DB2 = "DB2";

	private static final String DB2_DRIVER_CLASS_NAME = "com.ibm.db2.jcc.DB2Driver";

	private static final String MYSQL_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

	private static final String CURRENT_SCHEMA_FLAG = "currentSchema=";

	private static final String PACKAGE_PREFIX = "com/mdm/";

	private static final String ALL_FLAG = "all";

	public static void main(String[] args) {
		try {
			CodeDatabase dbConfig = new CodeDatabase();
			dbConfig.setType(DATABASE_TYPE_DB2);
			dbConfig.setUrl("jdbc:db2://10.27.87.99:60004/wsdb:currentSchema=WS;");
			dbConfig.setUsername("wsuser");
			dbConfig.setPassword("wsuser");
			dbConfig.setProjectName("ws");
			generate(dbConfig, new String[] { "MOCK_RESPONSE", "CODE_DATABASE", "MOCK_RULE" });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Connection getConnection(CodeDatabase dbConfig) throws Exception {
		Class.forName(DATABASE_TYPE_DB2.equals(dbConfig.getType()) ? DB2_DRIVER_CLASS_NAME : MYSQL_DRIVER_CLASS_NAME);
		return DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
	}

	/**
	 * load all tables
	 *
	 * @param dbConfig
	 * @return
	 * @throws Exception
	 */
	public static List<String> loadTables(CodeDatabase dbConfig) throws Exception {
		Connection con = getConnection(dbConfig);
		List<String> list = new ArrayList<String>();
		// MySQL
		if (!DATABASE_TYPE_DB2.equals(dbConfig.getType())) {
			ResultSet rs = con.getMetaData().getTables(con.getCatalog(), dbConfig.getUsername(), null,
					new String[] { "TABLE" });
			while (rs.next()) {
				list.add(rs.getString("TABLE_NAME"));
			}
			rs.close();
			con.close();
			return list;
		}
		// DB2
		String schema = dbConfig.getUsername().toUpperCase();
		if (dbConfig.getUrl().contains(CURRENT_SCHEMA_FLAG)) {
			int startIndex = dbConfig.getUrl().indexOf(CURRENT_SCHEMA_FLAG) + CURRENT_SCHEMA_FLAG.length();
			int endIndex = dbConfig.getUrl().indexOf(";");
			schema = dbConfig.getUrl().substring(startIndex, endIndex);
		}
		ResultSet rs = con.getMetaData().getTables(con.getCatalog(), schema, null, new String[] { "TABLE" });
		while (rs.next()) {
			list.add(rs.getString("TABLE_NAME"));
		}
		rs.close();
		con.close();
		return list;
	}

	public static String generate(CodeDatabase dbConfig, String tableNameStr) throws Exception {
		if (ALL_FLAG.equals(tableNameStr)) {
			List<String> list = loadTables(dbConfig);
			return generate(dbConfig, list.toArray(new String[list.size()]));
		}
		return generate(dbConfig, StringUtils.split(tableNameStr, ","));
	}

	public static String generate(CodeDatabase dbConfig, String[] tableNames) throws Exception {
		Connection con = getConnection(dbConfig);
		String tempFolder = RandomStringUtils.randomAlphanumeric(16) + "/";
		for (String tableName : tableNames) {
			// CapBank
			String className = getDomainName(tableName);
			// capbank
			String packageName = StringUtils.lowerCase(className);
			// CapBankDmo
			String dmoName = className;
			List<Column> columns = getColumn(con, dbConfig.getType(), tableName);
			List<Column> keyFieldList = getKeyFieldList(columns);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("tableName", tableName);
			parameters.put("projectName", dbConfig.getProjectName());
			parameters.put("packageName", packageName);
			parameters.put("className", className);
			parameters.put("dmoName", dmoName);
			parameters.put("list", columns);
			parameters.put("keyFieldList", keyFieldList);
			parameters.put("count", keyFieldList.size());
			generateSqlFile(parameters, tempFolder);
			generateDmoFile(parameters, dbConfig, tempFolder);
			generateDaoFile(parameters, dbConfig, tempFolder);
			// generateDaoImplFile(parameters, dbConfig, tempFolder);
//			generateServiceFile(parameters, dbConfig, tempFolder);
			generateServiceImplFile(parameters, dbConfig, tempFolder);
		}
		con.close();
		// 打包成zip
		String destFileName = jointDestFileName(dbConfig.getProjectName(), tableNames);
		ZipUtil.pack(CODE_PATH + destFileName, CODE_PATH + tempFolder);
		// 删除原文件
		FileUtils.deleteQuietly(new File(CODE_PATH + tempFolder));
		return destFileName;
	}

	private static String jointDestFileName(String projectName, String[] tableNames) {
		StringBuilder builder = new StringBuilder(projectName);
		if (tableNames.length == 1) {
			builder.append(".").append(tableNames[0]);
		}
		builder.append(".zip");
		return builder.toString();
	}

	/**
	 * generate sqlMapper.xml
	 *
	 * @param parameters
	 * @throws Exception
	 */
	public static void generateSqlFile(Map<String, Object> parameters, String tempFolder) throws Exception {
		File templateFile = ResourceUtils.getFile("classpath:template/sql.vm");
		File file = createFile(tempFolder, "", parameters.get("className"), "Mapper.xml");
		VelocityUtil.render(templateFile, parameters, file);
	}

	/**
	 * generate dmo.java
	 *
	 * @param parameters
	 * @throws Exception
	 */
	public static void generateDmoFile(Map<String, Object> parameters, CodeDatabase dbConfig, String tempFolder)
			throws Exception {
		File templateFile = ResourceUtils.getFile("classpath:template/dmo.vm");
		String packagePath = createPackagePath("/pojo/");
		File file = createFile(tempFolder, packagePath, parameters.get("className"), ".java");
		VelocityUtil.render(templateFile, parameters, file);
	}

	/**
	 * generate dao.java
	 *
	 * @param parameters
	 * @throws Exception
	 */
	public static void generateDaoFile(Map<String, Object> parameters, CodeDatabase dbConfig, String tempFolder)
			throws Exception {
		File templateFile = ResourceUtils.getFile("classpath:template/dao.vm");
		String packagePath = createPackagePath("/dao/write/", parameters.get("packageName"));
		File file = createFile(tempFolder, packagePath, parameters.get("className"), "WriteMapper.java");
		VelocityUtil.render(templateFile, parameters, file);
	}

	/**
	 * generate daoImpl.java
	 *
	 * @param parameters
	 * @throws Exception
	 */
	public static void generateDaoImplFile(Map<String, Object> parameters, CodeDatabase dbConfig, String tempFolder)
			throws Exception {
		File templateFile = ResourceUtils.getFile("classpath:template/dao-impl.vm");
		String packagePath = createPackagePath("/dao/impl/", parameters.get("packageName"));
		File file = createFile(tempFolder, packagePath, parameters.get("className"), "DaoImpl.java");
		VelocityUtil.render(templateFile, parameters, file);
	}

	/**
	 * generate service.java
	 *
	 * @param parameters
	 * @throws Exception
	 */
	public static void generateServiceFile(Map<String, Object> parameters, CodeDatabase dbConfig, String tempFolder)
			throws Exception {
		File templateFile = ResourceUtils.getFile("classpath:template/service.vm");
		String packagePath = createPackagePath("/service/", parameters.get("packageName"));
		File file = createFile(tempFolder, packagePath, parameters.get("className"), "Service.java");
		VelocityUtil.render(templateFile, parameters, file);
	}

	/**
	 * generate serviceImpl.java
	 *
	 * @param parameters
	 * @throws Exception
	 */
	public static void generateServiceImplFile(Map<String, Object> parameters, CodeDatabase dbConfig, String tempFolder)
			throws Exception {
		File templateFile = ResourceUtils.getFile("classpath:template/service_impl.vm");
		String packagePath = createPackagePath("/service/", parameters.get("packageName"));
		File file = createFile(tempFolder, packagePath, parameters.get("className"), "Service.java");
		VelocityUtil.render(templateFile, parameters, file);
	}

	private static String createPackagePath(Object... paths) {
		StringBuilder builder = new StringBuilder(PACKAGE_PREFIX);
		for (Object object : paths) {
			builder.append(object);
		}
		return builder.append("/").toString();
	}

	private static File createFile(String tempFolder, String packagePath, Object className, String suffix) {
		File file = new File(CODE_PATH + tempFolder + packagePath + className + suffix);
		file.getParentFile().mkdirs();
		return file;
	}

	private static List<Column> getKeyFieldList(List<Column> columns) {
		List<Column> keyField = new ArrayList<Column>();
		for (Column column : columns) {
			if (column.getPrimaryKey()) {
				keyField.add(column);
			}
		}
		return keyField;
	}

	private static List<Column> getColumn(Connection con, String type, String tableName) throws Exception {
		List<String> primaryKeys = new ArrayList<String>();
		DatabaseMetaData dataBaseMetaData = con.getMetaData();
		ResultSet result = dataBaseMetaData.getPrimaryKeys(null, null, tableName);
		while (result.next()) {
			primaryKeys.add(result.getString("COLUMN_NAME"));
		}
		result.close();
		List<Column> columns = new ArrayList<Column>();
		PreparedStatement prep = con.prepareStatement(getSql(type, tableName));
		ResultSetMetaData metadata = prep.executeQuery().getMetaData();
		for (int i = 1, count = metadata.getColumnCount(); i <= count; i++) {
			Column column = new Column();
			column.setColumnName(metadata.getColumnName(i));
			column.setProperty(getFieldName(metadata.getColumnName(i)));
			column.setDataType(DataTypeUtil.getJavaDataType(metadata.getColumnType(i)));
			column.setPrimaryKey(primaryKeys.contains(metadata.getColumnName(i)));
			column.setJavaType(DataTypeUtil.getJavaObjDataType(metadata.getColumnType(i)));
			column.setCapitalizeProperty(StringUtils.capitalize(column.getProperty()));
			columns.add(column);
		}
		prep.close();
		return columns;
	}

	public static String getFieldName(String n) {
		if (n.indexOf("_") < 0) {
			return StringUtils.uncapitalize(n);
		}
		String[] words = StringUtils.split(n, "_");
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.lowerCase(words[0]));
		for (int i = 1; i < words.length; i++) {
			sb.append(StringUtils.capitalize(StringUtils.lowerCase(words[i])));
		}
		return sb.toString();
	}

	public static String getDomainName(String tableName) {
		if (tableName.indexOf("_") < 0) {
			return tableName;
		}
		String[] words = StringUtils.split(tableName, "_");
		StringBuilder sb = new StringBuilder();
		for (String word : words) {
			sb.append(StringUtils.capitalize(StringUtils.lowerCase(word)));
		}
		return sb.toString();
	}

	public static String getSql(String type, String tableName) {
		StringBuilder builder = new StringBuilder("select * from ");
		builder.append(tableName);
		if (DATABASE_TYPE_DB2.equals(type)) {
			builder.append(" fetch first 1 rows only");
		} else {
			builder.append(" limit 1");
		}
		return builder.toString();
	}

}
