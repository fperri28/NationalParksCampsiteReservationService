package com.techelevator.campground;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.park.Park;

public class JDBCCampgroundDAO implements CampgroundDAO{

	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public boolean addLocation(Campground newCampground) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Campground getCampgroundById(String aCampgroundId) {

		Campground theCampground = null;
		String sqlSearchCampgroundsById = 	"SELECT * " + 
											"FROM Campground " + 
											"WHERE Campground_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchCampgroundsById, aCampgroundId);		
		while(results.next()) {	
			theCampground = mapRowToCampground(results);
		}
		return theCampground;
	}

	@Override
	public List<Campground> getAllCampgrounds() {
		List<Campground> allCampgrounds = new ArrayList<Campground>();
		
		String sqlListAllCampgroundsQuery = 	"SELECT * " + 
												"FROM Campground " + 
												"ORDER BY name ASC";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListAllCampgroundsQuery);
		
		while(results.next()) {	
			Campground aCampground = mapRowToCampground(results);
			allCampgrounds.add(aCampground);
		}
		return allCampgrounds;
	}

	@Override
	public List<Campground> getCampgroundByPark(String park) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getCampgroundRate(Campground aCampground) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeCampgroundData(Campground aCampground) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCampgroundById(String aCampgroundId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCampgroundByPark(String park) {
		// TODO Auto-generated method stub
		
	}

	private int getNextCampgroundId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_campground_id')");
		if (nextIdResult.next()) {
			return nextIdResult.getInt(1);
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new campground");
		}
	}
	
	private Campground mapRowToCampground(SqlRowSet results) {
		Campground theCampground = new Campground(); 
		
		theCampground.setCampground_id(results.getInt("Campground_id"));
		theCampground.setPark_id(results.getInt("Park_id"));		
		theCampground.setName(results.getString("name"));
		theCampground.setOpen_from_mm(results.getString("open_from_mm"));
		theCampground.setOpen_to_mm(results.getString("open_to_mm"));
		theCampground.setDaily_fee(results.getBigDecimal("daily_fee"));
		
		return theCampground;
	}
	
}
