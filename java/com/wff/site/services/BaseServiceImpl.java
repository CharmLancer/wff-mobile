package com.wff.site.services;

import com.wff.exception.ApplicationServiceException;

public class BaseServiceImpl {
	public void throwException(String label, String cause) throws ApplicationServiceException {
		throw new ApplicationServiceException(label, cause);
	}
}
