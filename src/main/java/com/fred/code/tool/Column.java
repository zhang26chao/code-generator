package com.fred.code.tool;

public class Column {

	private String columnName;
	
	private String property;

	private String dataType;
	
	private String comment;

	private boolean primaryKey;
	
	private String javaType;
	
	private String capitalizeProperty;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public boolean getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean isPrimaryKey) {
		this.primaryKey = isPrimaryKey;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getCapitalizeProperty() {
		return capitalizeProperty;
	}

	public void setCapitalizeProperty(String capitalizeProperty) {
		this.capitalizeProperty = capitalizeProperty;
	}
}
