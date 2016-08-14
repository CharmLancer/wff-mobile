package com.wff.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wff.database.model.DatabaseField;
import com.wff.database.model.DatabaseFieldProperty;
import com.wff.database.model.DatabaseTable;
import com.wff.database.model.FieldName;
import com.wff.exception.DatabaseFieldValueException;

public abstract class AbstractModel implements RowMapper {
	protected StringBuilder sql = new StringBuilder();
	protected StringBuilder select = new StringBuilder();
	protected StringBuilder from = new StringBuilder();
	protected StringBuilder where = new StringBuilder();

	@JsonIgnore
	Logger LOGGER = LoggerFactory.getLogger(AbstractModel.class);

	@JsonIgnore
	protected HashMap<Integer, DatabaseField> modelFields = new HashMap<>();

	@JsonIgnore
	protected HashMap<Integer, DatabaseField> queryFields = new HashMap<>();

	@JsonIgnore
	public void setModelFields(HashMap<Integer, DatabaseField> modelFields) {
		this.modelFields = modelFields;
	}

	@JsonIgnore
	public void setQueryFields(HashMap<Integer, DatabaseField> queryFields) {
		this.queryFields = queryFields;
	}

	protected void clear() {
		sql.setLength(0);
		select.setLength(0);
		from.setLength(0);
		where.setLength(0);
	}

	public DatabaseField createDatabaseField(DatabaseFieldProperty dbFieldProperty, final String... rootTable) {
		return new DatabaseField(dbFieldProperty, rootTable);
	}

	public void clearModelValues() {
		for (DatabaseField modelField : modelFields.values()) {
			modelField.resetValue();
		}
	}

	public void clearQueryValues() {
		for (DatabaseField queryField : queryFields.values()) {
			queryField.resetValue();
		}
	}

	public DatabaseField getInstance() {
		for (DatabaseField dbField : modelFields.values()) {
			if (dbField.getFieldName().equalsIgnoreCase(FieldName.INSTANCE.getValue())) {
				return dbField;
			}
		}
		return null;
	}

	public boolean where(List<DatabaseField> queryFields) {
		int count = 0;
		for (DatabaseField modelField : modelFields.values()) {
			for (DatabaseField queryField : queryFields) {
				if (modelField.getFieldName().equalsIgnoreCase(queryField.getFieldName())
						&& modelField.getFieldValue().equalsIgnoreCase(queryField.getFieldValue())) {
					count++;
				}
			}
		}
		return count == queryFields.size();
	}

	@JsonIgnore
	public ArrayList<DatabaseField> getModelFieldsWithValue() {
		ArrayList<DatabaseField> modelFieldsWithValue = new ArrayList<>();
		for (DatabaseField modelField : modelFields.values()) {
			if (modelField.getFieldValue() != null) {
				modelFieldsWithValue.add(modelField);
			}
		}
		return modelFieldsWithValue;
	}

	public HashMap<Integer, DatabaseField> getModelFields() {
		return modelFields;
	}

	public void setModelFields(ArrayList<DatabaseField> modelFields) {
		for (DatabaseField modelField : modelFields) {
			addModelFields(modelField);
		}
	}

	public void setQueryFields(ArrayList<DatabaseField> queryFields) {
		for (DatabaseField queryField : queryFields) {
			addField(this.queryFields, queryField);
		}
	}

	private void addField(HashMap<Integer, DatabaseField> fields, DatabaseField dbField) {
		int fieldIndex = isFieldFound(fields, dbField);
		if (fieldIndex > -1) {
			fields.remove(fieldIndex);
		} else {
			fieldIndex = fields.size();
		}
		dbField.setIndex(fieldIndex);
		fields.put(fieldIndex, dbField);
	}

	public int isFieldFound(HashMap<Integer, DatabaseField> fields, DatabaseField queryField) {
		Iterator<Integer> fieldsIterator = fields.keySet().iterator();
		while (fieldsIterator.hasNext()) {
			Integer dbFieldIndex = fieldsIterator.next();
			DatabaseField dbField = fields.get(dbFieldIndex);
			if (dbField.getFieldName().equalsIgnoreCase(queryField.getFieldName())) {
				return dbFieldIndex;
			}
		}
		return -1;
	}

	public void addModelFields(DatabaseField modelField) {
		addField(modelFields, modelField);
	}

	public void addQueryFields(DatabaseField queryField) {
		addField(queryFields, queryField);
	}

	public AbstractModel build(DatabaseField... fields) {
		this.queryFields.clear();
		this.modelFields.clear();
		// Assign new value
		for (DatabaseField field : fields) {
			addModelFields(field);
		}
		return this;
	};

	public HashMap<Integer, DatabaseField> getQueryFields() {
		return queryFields;
	}

	public void prepareFromClause(StringBuilder select, StringBuilder from, int index) {
		DatabaseField field = modelFields.get(index);
		boolean end = index == modelFields.size() - 1;
		boolean start = index == 0;
		select.append(field.getFieldName() + "." + FieldName.INSTANCE.getValue());
		select.append(",");
		select.append(field.getFieldName() + "." + FieldName.VALUE.getValue() + " " + field.getFieldName());
		// From clause
		from.append("(");
		from.append("SELECT ");
		from.append(FieldName.INSTANCE.getValue() + "," + FieldName.VALUE.getValue());
		from.append(" FROM " + field.getRootTable());
		from.append(" WHERE " + FieldName.TABLE.getValue() + "='" + field.getTableName() + "'");
		from.append(" AND " + FieldName.COLUMN.getValue() + "='" + field.getFieldName() + "'");
		for (DatabaseField dbField : queryFields.values()) {
			if (dbField.contains(field)) {
				if (dbField.getFieldValue() != null && !dbField.getFieldValue().isEmpty()) {
					from.append(" AND " + FieldName.VALUE.getValue() + "='" + dbField.getFieldValue() + "'");
				}
				break;
			}
		}
		from.append(")");
		from.append(field.getFieldName());
		if (!end) {
			select.append(", ");
			from.append(",");
		} else {
			select.append(" ");
			from.append(" ");
		}
		LOGGER.info("From clause " + from.toString());
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (DatabaseField field : modelFields.values()) {
			sb.append(field.toString());
			sb.append("\n");
		}
		return sb.toString();
	};

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

		for (DatabaseField field : modelFields.values()) {
			if (rs.getString(field.getFieldName()) != null) {
				try {
					field.setFieldValue(rs.getString(field.getFieldName()).toString(), field.getTableName());
				} catch (DatabaseFieldValueException e) {
					throw new SQLException("Unexpected exception");
				}
			} else {
				throw new SQLException("Field name was not found");
			}
			if (rs.getString("INSTANCE") != null) {
				field.setInstance(rs.getString("INSTANCE"));
			}

		}
		LOGGER.warn("Values after map row");
		for (DatabaseField field : modelFields.values()) {
			LOGGER.warn(field.toString());
		}

		return this;
	}

	public String select() {
		clear();
		// Select From clause
		for (int i = 0; i < modelFields.size(); i++) {
			prepareFromClause(select, from, i);
		}
		// Where clause
		DatabaseField firstField = modelFields.get(0);
		for (int i = 1; i < modelFields.size(); i++) {
			if (i == 1) {
				where.append(firstField.getFieldName() + "." + FieldName.INSTANCE.getValue() + "="
						+ modelFields.get(i).getFieldName() + "." + FieldName.INSTANCE.getValue());
			} else {
				where.append(" and " + firstField.getFieldName() + "." + FieldName.INSTANCE.getValue() + "="
						+ modelFields.get(i).getFieldName() + "." + FieldName.INSTANCE.getValue());
			}

		}
		// Filtering
		for (DatabaseField dbField : queryFields.values()) {
			where.append(" and " + dbField.getFieldName() + "." + DatabaseTable.value() + "='" + dbField.getFieldValue()
					+ "'");
		}
		// Final Statement
		sql.append("SELECT " + select.toString() + " FROM " + from.toString()
				+ (where.length() > 0 ? " WHERE " + where.toString() : ""));
		LOGGER.info(sql.toString());
		return sql.toString();
	};
}
