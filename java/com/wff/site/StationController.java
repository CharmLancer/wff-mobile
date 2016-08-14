package com.wff.site;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wff.dto.ModelDto;
import com.wff.exception.ApplicationServiceException;
import com.wff.exception.DatabaseFieldValueException;
import com.wff.site.model.Station;
import com.wff.site.services.StationService;

@Controller
@RequestMapping("/station")
public class StationController {
	@Autowired
	StationService stationService;

	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {
			"stationName", "stationLatitude", "stationLongitude" })
	public Station save(@RequestParam("stationName") String stationName,
			@RequestParam("stationLatitude") String stationLatitude,
			@RequestParam("stationLongitude") String stationLongitude)
					throws DatabaseFieldValueException, ApplicationServiceException {
		Station station = new Station();
		station.stationName.setFieldValue(stationName);
		station.stationLatitude.setFieldValue(stationLatitude);
		station.stationLongitude.setFieldValue(stationLongitude);
		station.addQueryFields(station.stationName);
		stationService.insert(station);
		return station;
	}

	@ResponseBody
	@RequestMapping(value = { "/getAll" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ModelDto<Station>> getAll() throws DatabaseFieldValueException, ApplicationServiceException {
		return stationService.selectAll(new Station());
	}

}
