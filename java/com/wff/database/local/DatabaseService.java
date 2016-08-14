package com.wff.database.local;

import java.util.List;

import com.wff.dto.ModelDto;
import com.wff.model.AbstractModel;

public interface DatabaseService {
	public <M extends AbstractModel> boolean insert(M model);

	public <M extends AbstractModel> boolean update(M model);

	public <M extends AbstractModel> boolean delete(M model);

	public <M extends AbstractModel> List<ModelDto<M>> select(M model);

}
