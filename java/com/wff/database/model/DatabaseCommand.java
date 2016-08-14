package com.wff.database.model;

public interface DatabaseCommand {
	public String insert(String instance);

	public String update();

	public String select();

	public String delete();
}
