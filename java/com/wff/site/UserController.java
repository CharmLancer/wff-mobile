package com.wff.site;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wff.dto.ModelDto;
import com.wff.exception.ApplicationServiceException;
import com.wff.exception.DatabaseFieldValueException;
import com.wff.site.model.User;
import com.wff.site.services.MessageService;
import com.wff.site.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;

	@Autowired
	MessageService messageService;

	@ResponseBody
	@RequestMapping("/hello")
	public String helloWorld() {
		return "Hello, World!";
	}

	@ResponseBody
	@RequestMapping(value = "/login", params = { "name", "password" })
	public String login(@RequestParam("name") String userName, @RequestParam("password") String userPassword)
			throws DatabaseFieldValueException {
		boolean valid = userService.checkUser(userName, userPassword);
		messageService.send("User is " + (valid ? " valid " : " not valid"));
		return Boolean.toString(valid);
	}

	@ResponseBody
	@RequestMapping(value = "/register", params = { "name", "password", "email" })
	public String register(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("password") String password) throws DatabaseFieldValueException, ApplicationServiceException {
		User user = new User();
		user.userName.setFieldValue(name);
		user.userPassword.setFieldValue(password);
		user.userEmail.setFieldValue(email);
		userService.insert(user);
		return Boolean.toString(Boolean.TRUE);
	}

	@ResponseBody
	@RequestMapping(value = { "/getAll" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ModelDto<User>> getAll() throws DatabaseFieldValueException, ApplicationServiceException {
		return userService.selectAll(new User());
	}
}
