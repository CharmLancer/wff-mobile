package com.wff.database.model;

public abstract class DatabaseTable {
	private static final String USER_TABLE_NAME = "\"USERS\"";
	private static final String BIG_TABLE_NAME = "\"BIGTABLES\"";
	private static final String TABLE1 = "\"Table1\"";
	private static final String TABLE2 = "\"Table2\"";
	private static final String TABLE3 = "\"Table3\"";
	private static final String VALUE = "\"VALUE\"";

	public static String usersTable() {
		return USER_TABLE_NAME;
	}

	public static String bigTable() {
		return BIG_TABLE_NAME;
	}

	public static String t1() {
		return TABLE1;
	}

	public static String t2() {
		return TABLE2;
	}

	public static String t3() {
		return TABLE3;
	}

	public static String value() {
		return VALUE;
	}
}
