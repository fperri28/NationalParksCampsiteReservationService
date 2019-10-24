package com.techelevator.park;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCParkDAO implements ParkDAO{

	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Park addLocation(Park newPark) {
		String sqlAddNewPark = 	"INSERT INTO park" + 
								"(park_id, name, location, establish_date, area, visitors, description) " + 
								"VALUES (?, ?, ?, ?, ?, ?, ?)";
		newPark.setPark_id(getNextParkId()); 
		
		jdbcTemplate.update(sqlAddNewPark, newPark.getPark_id(), newPark.getName());

		return newPark;
	}

	@Override
	public Park getParkById(String aPark) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Park> getAllParks() {
		List<Park> allParks = new ArrayList<Park>();
		
		String sqlListAllParksQuery = 	"SELECT * " + 
										"FROM park " + 
										"ORDER BY name ASC";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListAllParksQuery);
		
		while(results.next()) {	
			Park aPark = mapRowToPark(results);
			allParks.add(aPark);
		}
		return allParks;
	}


	@Override
	public List<Park> getParkByCampground(String campground) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getParkRate(Park aPark) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeParkData(Park aPark) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteParkById(String aParkId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteParkByCampground(String campground) {
		// TODO Auto-generated method stub
		
	}

	private int getNextParkId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_park_id')");
		if (nextIdResult.next()) {
			return nextIdResult.getInt(1);
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new park");
		}
	}
	
	private Park mapRowToPark(SqlRowSet results) {
		Park thePark = new Park(); 
		
		thePark.setPark_id(results.getInt("park_id"));
		thePark.setName(results.getString("name"));
		thePark.setLocation(results.getString("location"));
		thePark.setEstablish_date(results.getDate("establish_date").toLocalDate());
		thePark.setArea(results.getInt("area"));
		thePark.setVisitors(results.getInt("visitors"));
		thePark.setDescription(results.getString("description"));
		
		return thePark;
	}
	
	
}
