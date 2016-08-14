package test;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wff.dto.ModelDto;
import com.wff.exception.ApplicationServiceException;
import com.wff.exception.DatabaseFieldValueException;
import com.wff.site.model.User;
import com.wff.site.services.UserService;

import test.config.BaseTest;

@Ignore
public class UserServiceImplTest extends BaseTest {

	@Autowired
	UserService userService;

	@Test
	public void test() throws DatabaseFieldValueException, ApplicationServiceException {
		User user = new User();
		user.userName.setFieldValue("admin");
		user.userPassword.setFieldValue("-");
		// Test Insert
		userService.insert(user);
		// Test select all
		List<ModelDto<User>> users = userService.getUser(user.userName, user.userPassword);
		for (ModelDto<User> result : users) {
			if (result.contains(user.userName.getFieldName())) {
				Assert.assertTrue(result.get(user.userName.getFieldName()).equalsIgnoreCase("admin"));
			}
			if (result.contains(user.userPassword.getFieldName())) {
				Assert.assertTrue(result.get(user.userPassword.getFieldName()).equalsIgnoreCase("-"));
			}
		}
		// Test select specific
		Assert.assertTrue(userService.checkUser("admin", "-"));
		Assert.assertFalse(userService.checkUser("admin", "a"));
	}

}
