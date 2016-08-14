package com.wff.site.model;

import com.wff.database.model.DatabaseFieldProperty;
import com.wff.database.model.FieldType;

public class Schema {
	public static class Station {
		// Variable to know field property such as name, type, table
		public static DatabaseFieldProperty stationName = new DatabaseFieldProperty("stationName", FieldType.STRING,
				"Station");
		public static DatabaseFieldProperty stationLatitude = new DatabaseFieldProperty("stationLatitude",
				FieldType.DECIMAL, "Station");
		public static DatabaseFieldProperty stationLongitude = new DatabaseFieldProperty("stationLongitude",
				FieldType.DECIMAL, "Station");
	}

	public static class User {
		public static DatabaseFieldProperty userName = new DatabaseFieldProperty("userName", FieldType.STRING, "User");

		public static DatabaseFieldProperty userPassword = new DatabaseFieldProperty("userPassword", FieldType.DECIMAL,
				"User");

		public static DatabaseFieldProperty userEmail = new DatabaseFieldProperty("userEmail", FieldType.STRING,
				"User");

	}

}
