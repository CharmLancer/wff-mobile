package com.wff.database.model;

public enum FieldType {
	NUMERIC("int"),DECIMAL("Decimal"),BOOLEAN("Boolean"),STRING("String"),DATE("Date");

	private String value;
	
	FieldType(final String value){
		this.value=value;
	}
	
	public String getValue(){
		return value;
	}
}
