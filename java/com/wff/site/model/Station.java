package com.wff.site.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wff.database.model.DatabaseField;
import com.wff.model.AbstractModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Station extends AbstractModel {

	// Variable to store field value
	@JsonProperty("stationName")
	public DatabaseField stationName;
	@JsonProperty("stationLatitude")
	public DatabaseField stationLatitude;
	@JsonProperty("stationLongitude")
	public DatabaseField stationLongitude;

	public Station() {
		stationName = new DatabaseField(Schema.Station.stationName);
		stationLatitude = new DatabaseField(Schema.Station.stationLatitude);
		stationLongitude = new DatabaseField(Schema.Station.stationLongitude);
		super.addModelFields(stationName);
		super.addModelFields(stationLatitude);
		super.addModelFields(stationLongitude);
	}

	@JsonProperty("stationName")
	public DatabaseField getStationName() {
		return stationName;
	}

	@JsonProperty("stationName")
	public void setStationName(DatabaseField stationName) {
		this.stationName = stationName;
	}

	@JsonProperty("stationLatitude")
	public DatabaseField getStationLatitude() {
		return stationLatitude;
	}

	@JsonProperty("stationLatitude")
	public void setStationLatitude(DatabaseField stationLatitude) {
		this.stationLatitude = stationLatitude;
	}

	@JsonProperty("stationLongitude")
	public DatabaseField getStationLongitude() {
		return stationLongitude;
	}

	@JsonProperty("stationLongitude")
	public void setStationLongitude(DatabaseField stationLongitude) {
		this.stationLongitude = stationLongitude;
	}

}
