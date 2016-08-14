package com.wff.database.local;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.wff.database.model.DatabaseField;
import com.wff.dto.ModelDto;
import com.wff.exception.DatabaseFieldValueException;
import com.wff.model.AbstractModel;

@Service
public class DatabaseServiceImpl implements DatabaseService {
	Logger LOGGER = LoggerFactory.getLogger(DatabaseServiceImpl.class);

	@Autowired
	BasicDataSource dataSource;

	protected JdbcTemplate jdbcTemplate;

	@Override
	public <M extends AbstractModel> boolean insert(M model) {
		try (Connection con = dataSource.getConnection()) {
			LOGGER.info("INSERTING MODEL INTO DATABASE");
			jdbcTemplate = new JdbcTemplate(dataSource);
			String sqls[] = new String[model.getModelFields().size()];
			String rowId = UUID.randomUUID().toString();
			Iterator<DatabaseField> modelFieldsIterator = model.getModelFields().values().iterator();
			int i = 0;
			while (modelFieldsIterator.hasNext()) {
				DatabaseField modelField = modelFieldsIterator.next();
				sqls[i] = modelField.insert((rowId));
				LOGGER.warn(sqls[i]);
				i++;
			}
			jdbcTemplate.batchUpdate(sqls);
			return true;
		} catch (SQLException e) {
			LOGGER.error("Failed to insert.");
		}
		return false;
	}

	@Override
	public <M extends AbstractModel> boolean update(M model) {
		try (Connection con = dataSource.getConnection()) {
			jdbcTemplate = new JdbcTemplate(dataSource);
			ArrayList<DatabaseField> modelFieldsWithValueToUpdate = model.getModelFieldsWithValue();
			String sqls[] = new String[modelFieldsWithValueToUpdate.size()];
			// // First get the instance to update model fields
			// List<M> queriedModels = select(model);
			// if (!queriedModels.isEmpty()) {
			// for (M queriedModel : queriedModels) {
			// LOGGER.info(queriedModel.toString());
			// for (int i = 0; i < modelFieldsWithValueToUpdate.size(); i++) {
			// sqls[i] = modelFieldsWithValueToUpdate.get(i).update();
			// LOGGER.warn(sqls[i]);
			// }
			// }
			// jdbcTemplate.batchUpdate(sqls);
			// }
			for (int i = 0; i < modelFieldsWithValueToUpdate.size(); i++) {
				sqls[i] = modelFieldsWithValueToUpdate.get(i).update();
				LOGGER.warn(sqls[i]);
			}
			jdbcTemplate.batchUpdate(sqls);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Failed to update.");
		}
		return false;
	}

	@Override
	public <M extends AbstractModel> boolean delete(M model) {
		try (Connection con = dataSource.getConnection()) {
			jdbcTemplate = new JdbcTemplate(dataSource);
			String sqls[] = new String[model.getModelFields().size()];
			// First get the instance to update model fields
			List<ModelDto<M>> queriedModels = select(model);
			StringBuilder sb = new StringBuilder();
			for (ModelDto<M> queriedModel : queriedModels) {
				LOGGER.info(queriedModel.toString());
				Iterator<HashMap> modelFieldsIterator = queriedModel.getValues().keySet().iterator();
				int i = 0;
				while (modelFieldsIterator.hasNext()) {
					HashMap fields = modelFieldsIterator.next();
					if (fields.containsKey("INSTANCE")) {
						if (sb.length() > 0) {
							sb.append("," + fields.get("INSTANCE"));
						} else {
							sb.append(fields.get("INSTANCE"));
						}
					}
				}
			}
			//
			if (sb.length() > 0) {

				LOGGER.trace("Delete From {} where INSTANCE in {}", model.getInstance().getTableName(), sb.toString());
				jdbcTemplate.batchUpdate("Delete from 'BIGTABLES' where INSTANCE IN " + sb.toString());
			}

			return true;
		} catch (Exception e) {
			LOGGER.error("Failed to update.");
		}
		return false;
	}

	@Override
	public <M extends AbstractModel> List<ModelDto<M>> select(M model) {

		try (Connection con = dataSource.getConnection()) {
			jdbcTemplate = new JdbcTemplate(dataSource);
			String sqls[] = new String[model.getQueryFields().size()];

			LOGGER.info(model.select());
			// jdbcTemplate.query(sqls[0], model.getFields().get(0));
			// List<M> models = jdbcTemplate.query(model.select(), model);
			List<ModelDto<M>> models = Lists.newArrayList();
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(model.select());
			for (Map row : rows) {
				model.clearModelValues();
				for (DatabaseField field : model.getModelFields().values()) {
					if (row.get(field.getFieldName()) != null) {
						try {
							field.setFieldValue(row.get(field.getFieldName()).toString(), field.getTableName());
						} catch (DatabaseFieldValueException e) {
							throw new SQLException("Unexpected exception");
						}
					} else {
						throw new SQLException("Field name was not found");
					}
					if (row.get("INSTANCE") != null) {
						field.setInstance(row.get("INSTANCE").toString());
					}

				}
				ModelDto<M> dto = new ModelDto<M>(model);
				models.add(dto);
			}

			return models;
		} catch (SQLException e) {
			LOGGER.error("Failed to select.");
		}
		return new ArrayList<>();
	}

}
