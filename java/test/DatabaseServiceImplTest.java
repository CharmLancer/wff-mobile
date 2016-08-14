package test;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.wff.database.local.DatabaseService;
import com.wff.database.model.DatabaseField;
import com.wff.database.model.DatabaseTable;
import com.wff.dto.ModelDto;
import com.wff.exception.DatabaseFieldValueException;
import com.wff.site.model.Schema;
import com.wff.site.model.User;

import test.config.BaseTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
public class DatabaseServiceImplTest extends BaseTest {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	DatabaseService databaseService;

	@Test
	public void testAInsert() throws DatabaseFieldValueException {
		User user = new User();
		user.build(user.userName.setFieldValue("Guest01"), user.userPassword.setFieldValue("-"));
		// Inserting a user
		databaseService.insert(user);
		// List<ModelDto<User>> users = databaseService.select(user);
		// Assert.assertEquals(1, users.size());
	}

	@Test
	public void testBUpdate() throws DatabaseFieldValueException {
		User user = new User();
		// 1. Query to get the instance key assigned to model.
		user.addQueryFields(
				new DatabaseField(Schema.User.userName, DatabaseTable.usersTable()).setFieldValue("Guest01"));
		List<ModelDto<User>> users = databaseService.select(user);
		// 2. Update the model field value to update.
		for (ModelDto<User> u : users) {
			user.clearModelValues();
			if (u.get(user.userName.getFieldName()) != null) {
				user.userName.setFieldValue("Guest 01 New");
			}
			if (u.get(user.userPassword.getFieldName()) != null) {
				user.userPassword.setFieldValue("abc");
			}
			databaseService.update(user);
		}
	}

	@Test
	public void testCDelete() throws DatabaseFieldValueException {
		User user = new User();
		// 1. Query to get the instance key assigned to model.
		user.addQueryFields(
				new DatabaseField(Schema.User.userName, DatabaseTable.usersTable()).setFieldValue("Guest01 New"));
		List<ModelDto<User>> users = databaseService.select(user);
		// 2. Update the model field value to update.
		// for (User u : users) {
		// databaseService.delete(u);
		// }
	}

}
