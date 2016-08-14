package com.wff.site.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.wff.database.model.DatabaseField;
import com.wff.database.model.DatabaseTable;
import com.wff.model.AbstractModel;

@JsonRootName(value = "User")
public class User extends AbstractModel {
	Logger LOGGER = LoggerFactory.getLogger(User.class);

	public DatabaseField userName;
	public DatabaseField userPassword;
	public DatabaseField userEmail;

	public User() {
		userName = new DatabaseField(Schema.User.userName, DatabaseTable.usersTable());
		userPassword = new DatabaseField(Schema.User.userPassword, DatabaseTable.usersTable());
		userEmail = new DatabaseField(Schema.User.userEmail, DatabaseTable.usersTable());
		super.addModelFields(userName);
		super.addModelFields(userPassword);
		super.addModelFields(userEmail);
	}

}
