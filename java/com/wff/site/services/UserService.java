package com.wff.site.services;

import java.util.List;

import com.wff.database.model.DatabaseField;
import com.wff.dto.ModelDto;
import com.wff.exception.DatabaseFieldValueException;
import com.wff.site.model.User;

public interface UserService extends AbstractCRUDService {

	public boolean checkUser(String userName, String userPassword) throws DatabaseFieldValueException;

	List<ModelDto<User>> getUser(DatabaseField... dbField);
}
