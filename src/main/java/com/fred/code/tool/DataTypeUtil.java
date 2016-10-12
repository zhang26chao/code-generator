package com.fred.code.tool;

import java.sql.Types;

public class DataTypeUtil {

    private static int groupDataType(int dataType) {
        switch (dataType) {
        	case java.sql.Types.BIT:
        		return ColumnType.BOOLEAN; 
            case java.sql.Types.CHAR:
            case java.sql.Types.VARCHAR:
            case java.sql.Types.LONGVARCHAR:
                return ColumnType.STRING;
            case java.sql.Types.TINYINT:
                return ColumnType.BYTE;
            case java.sql.Types.INTEGER:
            case java.sql.Types.SMALLINT:
            case java.sql.Types.BOOLEAN:
                return ColumnType.INT;
            case java.sql.Types.BIGINT:
                return ColumnType.LONG;
            case java.sql.Types.FLOAT:
            case Types.REAL:
                return ColumnType.FLOAT;
            case java.sql.Types.DOUBLE:
                return ColumnType.DOUBLE;
            case java.sql.Types.DECIMAL:
            case java.sql.Types.NUMERIC:
                return ColumnType.BIG_DECIMAL;
            case java.sql.Types.DATE:
            case java.sql.Types.TIME:
            case java.sql.Types.TIMESTAMP:
                return ColumnType.DATE;
            case java.sql.Types.BLOB:
            case java.sql.Types.BINARY:
            case java.sql.Types.LONGVARBINARY:
            case java.sql.Types.CLOB:
            case java.sql.Types.VARBINARY:
                return ColumnType.BLOB;
            default:
                throw new IllegalArgumentException("found unkown column type " + dataType);
        }
    }

    public static String getJavaObjDataType(int dbtype) {
        dbtype = groupDataType(dbtype);
        switch (dbtype) {
            case ColumnType.STRING:
                return "String";
            case ColumnType.INT:
                return "Integer";
            case ColumnType.BYTE:
                return "Byte";
            case ColumnType.LONG:
                return "Long";
            case ColumnType.FLOAT:
                return "Float";
            case ColumnType.DOUBLE:
                return "double";
            case ColumnType.DATE:
                return "Date";
            case ColumnType.BLOB:
                return "byte[]";
            case ColumnType.BIG_DECIMAL:
                return "BigDecimal";
            case ColumnType.BOOLEAN:
                return "Boolean";    
            default:
                return "";
        }
    }

    public static String getJavaDataType(int dbtype) {
        dbtype = groupDataType(dbtype);
        switch (dbtype) {
            case ColumnType.STRING:
                return "String";
            case ColumnType.INT:
                return "int";
            case ColumnType.BYTE:
                return "byte";
            case ColumnType.LONG:
                return "long";
            case ColumnType.FLOAT:
                return "float";
            case ColumnType.DOUBLE:
                return "double";
            case ColumnType.DATE:
                return "Date";
            case ColumnType.BLOB:
                return "byte[]";
            case ColumnType.BIG_DECIMAL:
                return "BigDecimal";
            case ColumnType.BOOLEAN:
                return "boolean";    
            default:
                return "";
        }
    }
}
