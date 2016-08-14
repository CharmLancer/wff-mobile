package com.wff.site.services;

import java.util.List;

import com.wff.dto.ModelDto;
import com.wff.exception.ApplicationServiceException;
import com.wff.model.AbstractModel;

public interface AbstractCRUDService {
	public <M extends AbstractModel> List<ModelDto<M>> insert(M model) throws ApplicationServiceException;

	public <M extends AbstractModel> List<ModelDto<M>> update(M model) throws ApplicationServiceException;

	public <M extends AbstractModel> List<ModelDto<M>> delete(M model) throws ApplicationServiceException;

	public <M extends AbstractModel> List<ModelDto<M>> selectBy(M model) throws ApplicationServiceException;

	public <M extends AbstractModel> List<ModelDto<M>> selectAll(M model) throws ApplicationServiceException;
}
