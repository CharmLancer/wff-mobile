package com.wff.database.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.wff.exception.DatabaseFieldValueException;

public class DatabaseField extends DatabaseTable implements DatabaseCommand, ResultSetExtractor {
	Logger LOGGER = LoggerFactory.getLogger(DatabaseField.class);
	int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	String instance;
	String fieldName;
	String tableName;
	String rootTable = DatabaseTable.bigTable();
	FieldType fieldType;
	String fieldValue;
	private static final String INSTANCE = "\"INSTANCE\"";
	private static final String COLUMN = "\"COLUMN\"";
	private static final String VALUE = "\"VALUE\"";
	private static final String TABLE = "\"TABLE\"";
	private static final String USER_LABEL = "\"USER_LABEL\"";

	public DatabaseField(DatabaseField dbField) {
		this.instance = dbField.instance;

		this.fieldName = dbField.fieldName;
		this.tableName = dbField.tableName;
		this.rootTable = dbField.rootTable;
		this.fieldType = dbField.fieldType;
		this.fieldValue = dbField.fieldValue;

	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public String getRootTable() {
		return rootTable;
	}

	public DatabaseField(final DatabaseFieldProperty dbFieldProperty, final String... rootTable) {
		super();
		this.fieldName = dbFieldProperty.fieldName;
		this.fieldType = dbFieldProperty.fieldType;
		this.tableName = dbFieldProperty.tableName;
		if (rootTable != null && rootTable.length > 0) {
			this.rootTable = rootTable[0];
		}
	}

	public DatabaseField(final String fieldName, final FieldType fieldType, final String tableName,
			final String... rootTable) {
		super();
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.tableName = tableName;
		if (rootTable != null && rootTable.length > 0) {
			this.rootTable = rootTable[0];
		}
	}

	public DatabaseField setFieldValue(Object fieldValue, final String... tableName)
			throws DatabaseFieldValueException {
		if (tableName != null && tableName.length == 1)
			this.tableName = tableName[0];
		switch (this.fieldType) {
		case BOOLEAN:
			if (fieldValue instanceof Boolean) {
				this.fieldValue = Boolean.toString((boolean) fieldValue);
			} else {
				throwDatabaseFieldValueException();
			}
			break;
		case DATE:
			if (fieldValue instanceof Date) {
				this.fieldValue = ((Date) fieldValue).toString();
			} else {
				throwDatabaseFieldValueException();
			}
			break;
		case DECIMAL:
			if (fieldValue instanceof Double) {
				this.fieldValue = Double.toString(((double) fieldValue));
			} else if (fieldValue instanceof Integer) {
				this.fieldValue = Integer.toString((int) fieldValue);
			} else if (fieldValue instanceof String) {
				this.fieldValue = (String) fieldValue;
			} else {
				throwDatabaseFieldValueException();
			}
			break;
		case NUMERIC:
			if (fieldValue instanceof Integer) {
				this.fieldValue = Integer.toString(((int) fieldValue));
			} else {
				throwDatabaseFieldValueException();
			}
			break;
		case STRING:
			this.fieldValue = (String) fieldValue;
			break;
		default:
			this.fieldValue = (String) fieldValue;
		}
		return this;
	}

	private void throwDatabaseFieldValueException() throws DatabaseFieldValueException {
		throw new DatabaseFieldValueException();
	}

	public Logger getLOGGER() {
		return LOGGER;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getTableName() {
		return tableName;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	@Override
	public String insert(final String instance) {
		String sql = "INSERT INTO " + rootTable
				+ "(\"ROWID\", \"INSTANCE\", \"COLUMN\", \"TABLE\", \"VALUE\", \"USER_LABEL\") " + "VALUES "
				+ "(DEFAULT, '" + instance + "', '" + this.fieldName + "', '" + this.tableName + "', '"
				+ this.fieldValue + "', 'USER_ADMIN')";
		return sql;
	}

	@Override
	public String update() {
		String sql = "UPDATE " + rootTable + " SET " + VALUE + "='" + this.fieldValue + "' WHERE " + INSTANCE + "='"
				+ this.instance + "' AND " + COLUMN + "='" + this.fieldName + "' AND " + TABLE + "='" + this.tableName
				+ "'";
		return sql;
	}

	@Override
	public String select() {
		String sql = "SELECT * FROM " + rootTable + " WHERE \"TABLE\" ='" + this.tableName + "'";
		return sql;
	}

	@Override
	public DatabaseField extractData(ResultSet rs) throws SQLException, DataAccessException {
		if (rs.getString("COLUMN").equalsIgnoreCase(this.fieldName)) {
			this.fieldValue = rs.getString("VALUE");
			this.instance = rs.getString("INSTANCE");
			LOGGER.info("(" + this.instance + ")" + this.fieldName + " = " + this.fieldValue);
		}
		return this;
	}

	@Override
	public String toString() {
		return "DatabaseField [instance=" + instance + ", fieldName=" + fieldName + ", tableName=" + tableName
				+ ", fieldType=" + fieldType + ", fieldValue=" + fieldValue + "]";

	}

	public void clear() {
		this.instance = null;
		this.fieldValue = null;
	}

	public void resetValue() {
		this.fieldValue = null;
	}

	public boolean contains(DatabaseField dbField) {
		return (this.fieldName.equalsIgnoreCase(dbField.fieldName) && this.fieldType == dbField.fieldType
				&& this.tableName.equalsIgnoreCase(dbField.tableName));
	}

	@Override
	public String delete() {
		String sql = "DELETE FROM " + rootTable + " WHERE " + INSTANCE + "='" + this.instance + "' AND " + COLUMN + "='"
				+ this.fieldName + "' AND " + TABLE + "='" + this.tableName + "'";
		return sql;
	}

}
