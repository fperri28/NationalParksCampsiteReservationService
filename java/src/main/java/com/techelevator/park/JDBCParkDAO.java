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

		return newPark;
	}
	
	@Override
	public Park getParkByName(String aParkName) {
		Park aPark = null;
		
		String aParkNameSearch = "%" + aParkName + "%";
		String sqlSearchParksByName = 	"SELECT * " + 
										"FROM park " + 
										"WHERE name " + 
										"ILIKE ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchParksByName, aParkNameSearch);
		
		while(results.next()) {	
			aPark = mapRowToPark(results);
		}
		return aPark;
	}

	@Override
	public List<Park> getParksByName(String aParkName) {
		List<Park> theParksSearch = new ArrayList<Park>();
		
		String aParkNameSearch = "%" + aParkName + "%";
		String sqlSearchParksByName = 	"SELECT * " + 
										"FROM park " + 
										"WHERE name " + 
										"ILIKE ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchParksByName, aParkNameSearch);
		
		while(results.next()) {	
			Park aPark = mapRowToPark(results);
			theParksSearch.add(aPark);
		}
		return theParksSearch;
	}

	@Override
	public Park getParkById(int aParkId) {

		Park thePark = null;
		String sqlSearchParksById = 	"SELECT * " + 
										"FROM park " + 
										"WHERE park_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchParksById, aParkId);		
		while(results.next()) {	
			thePark = mapRowToPark(results);
		}
		return thePark;
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
	public void changeParkData(Park aPark) {

		String sqlUpdateDeptName = 	"UPDATE park " + 
									"SET name = ? " +
									"WHERE park_id = ? ";
		long deptId = aPark.getPark_id();
		String updateName = aPark.getName();
		jdbcTemplate.update(sqlUpdateDeptName, updateName, deptId);	
	}

	@Override
	public void deleteParkById(String aParkId) {
		String sqlQuery = 	"DELETE FROM park " +
							"WHERE park_id = ? ";
		jdbcTemplate.update(sqlQuery, aParkId);
		
	}
	
//	-------------------------------	HELPER METHODS	---------------------------------
	
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
