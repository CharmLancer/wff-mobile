package com.wff.database.model;

public enum FieldName {
	INSTANCE("\"INSTANCE\""),ROWID("\"ROWID\""),COLUMN("\"COLUMN\""),TABLE("\"TABLE\""),VALUE("\"VALUE\""),USER_LABEL("\"USER_LABEL\"");

	private String value;
	
	FieldName(final String value){
		this.value=value;
	}
	
	public String getValue(){
		return value;
	}
}
