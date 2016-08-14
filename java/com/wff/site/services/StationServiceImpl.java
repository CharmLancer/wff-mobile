package com.wff.site.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wff.database.local.DatabaseService;
import com.wff.dto.ModelDto;
import com.wff.exception.ApplicationServiceException;
import com.wff.model.AbstractModel;

@Service
public class StationServiceImpl extends BaseServiceImpl implements StationService {
	Logger LOGGER = LoggerFactory.getLogger(StationServiceImpl.class);

	@Autowired
	DatabaseService databaseService;

	@Autowired
	MessageService messageService;

	@Override
	public <M extends AbstractModel> List<ModelDto<M>> insert(M model) throws ApplicationServiceException {
		if (databaseService.select(model).isEmpty()) {
			if (databaseService.insert(model))
				return databaseService.select(model);
		} else {
			List<ModelDto<M>> models = databaseService.select(model);
			for (ModelDto m : models) {
				LOGGER.warn(m.toString());
			}
			messageService.send("The station is already existed in database.");
			// throwException("insert", "The station is already existed in
			// database.");
		}
		return null;
	}

	@Override
	public <M extends AbstractModel> List<ModelDto<M>> update(M model) throws ApplicationServiceException {
		if (databaseService.update(model))
			return databaseService.select(model);
		return null;
	}

	@Override
	public <M extends AbstractModel> List<ModelDto<M>> delete(M model) throws ApplicationServiceException {
		if (databaseService.delete(model))
			return databaseService.select(model);
		return null;
	}

	@Override
	public <M extends AbstractModel> List<ModelDto<M>> selectBy(M model) throws ApplicationServiceException {
		return databaseService.select(model);
	}

	@Override
	public <M extends AbstractModel> List<ModelDto<M>> selectAll(M model) throws ApplicationServiceException {
		return databaseService.select(model);
	}

}
