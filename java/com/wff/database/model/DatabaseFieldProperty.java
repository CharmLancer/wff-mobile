package com.wff.database.model;

public class DatabaseFieldProperty {
	public String fieldName;
	public FieldType fieldType;
	public String tableName;

	public DatabaseFieldProperty(String fieldName, FieldType fieldType, String tableName) {
		super();
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.tableName = tableName;
	}

}
