package test;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.wff.dto.ModelDto;
import com.wff.exception.ApplicationServiceException;
import com.wff.exception.DatabaseFieldValueException;
import com.wff.site.model.Station;
import com.wff.site.services.StationService;

import junit.framework.Assert;
import test.config.BaseTest;

@Ignore
public class StationServiceImplTest extends BaseTest {
	Logger LOGGER = LoggerFactory.getLogger(StationServiceImplTest.class);
	@Autowired
	StationService stationService;

	public List<ModelDto<Station>> testSelectStation(Station station)
			throws DatabaseFieldValueException, ApplicationServiceException {
		List<ModelDto<Station>> stations = stationService.selectAll(station);
		for (ModelDto<Station> s : stations) {
			LOGGER.info("Station {}", s.toString());
		}
		return stations;
	}

	@Test
	public void testUpdateStation() throws DatabaseFieldValueException, ApplicationServiceException {
		Station station = new Station();
		station.stationLatitude.setFieldValue(100.10);
		station.stationLongitude.setFieldValue(100.10);
		station.stationName.setFieldValue("Station 30");
		// Delete
		station.addQueryFields(station.stationName.setFieldValue("Station 30"));
		stationService.delete(station);
		// Insert a station
		testInsertStation(station);
		// Select
		station.addQueryFields(station.stationName.setFieldValue("Station 30"));
		List<ModelDto<Station>> stations = stationService.selectBy(station);
		Assert.assertTrue(stations.size() == 1);
		// Update a station
		station.clearModelValues();
		station.addQueryFields(station.stationName.setFieldValue("Station 30"));
		station.stationName.setFieldValue("Station 30 New");
		stations = stationService.update(station);
		for (ModelDto<Station> s : stations) {
			if (s.contains(station.stationName.getFieldName())) {
				Assert.assertTrue(s.get(station.stationName.getFieldName()).toString()
						.equalsIgnoreCase(station.stationName.getFieldValue()));
			}
		}
		Assert.assertEquals(1, stationService.selectBy(station).size());
		stationService.delete(station);
		Assert.assertEquals(0, stationService.selectBy(station).size());
	}

	public void testInsertStation(Station station) throws DatabaseFieldValueException, ApplicationServiceException {

		List<ModelDto<Station>> stations = stationService.insert(station);
		for (ModelDto<Station> s : stations) {
			LOGGER.info("Station {}", s.toString());
		}
		Assert.assertFalse(stations.isEmpty());
	}

}
