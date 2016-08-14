package com.wff.dto;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.collect.Maps;
import com.wff.database.model.DatabaseField;
import com.wff.model.AbstractModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "values" })
public class ModelDto<T extends AbstractModel> {
	private static String INSTANCE = "instance";
	private static String FIELD_NAME = "name";
	private static String FIELD_VALUE = "value";
	private static String FIELD_TYPE = "type";

	@JsonProperty("model")
	private HashMap values;

	@JsonProperty("type")
	private String type;

	@JsonCreator
	public ModelDto(T model) {
		values = Maps.newHashMap();
		HashMap fieldValue = Maps.newHashMap();
		for (Integer key : model.getModelFields().keySet()) {
			DatabaseField dbField = model.getModelFields().get(key);
			values.put(dbField.getFieldName(), fieldValue);
			// Set value
			fieldValue.put(INSTANCE, dbField.getInstance());
			fieldValue.put(FIELD_NAME, dbField.getFieldName());
			fieldValue.put(FIELD_TYPE, dbField.getFieldType().getValue());
			fieldValue.put(FIELD_VALUE, dbField.getFieldValue());
		}
		type = model.getClass().getCanonicalName();

	}

	@JsonProperty("model")
	public HashMap getValues() {
		return values;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	public boolean contains(String fieldName) {
		if (values.containsKey(FIELD_NAME) && values.get(FIELD_NAME).toString().equalsIgnoreCase(fieldName)) {
			return true;
		}
		return false;
	}

	public String get(String fieldName) {

		if (values.containsKey(FIELD_NAME) && values.get(FIELD_NAME).toString().equalsIgnoreCase(fieldName)) {
			return (String) values.get(FIELD_VALUE);
		}

		return null;
	}

	@Override
	public String toString() {
		return "ModelDto [values=" + values + "]";
	}

}
