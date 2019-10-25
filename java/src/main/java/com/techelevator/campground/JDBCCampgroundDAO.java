package com.techelevator.campground;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;


public class JDBCCampgroundDAO implements CampgroundDAO{

	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public boolean addCampground(Campground newCampground) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Campground getCampgroundById(int aCampgroundId) {

		Campground theCampground = null;
		String sqlSearchCampgroundsById = 	"SELECT * " + 
											"FROM campground " + 
											"WHERE campground_id = ?";
		
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
	public List<Campground> getCampgroundByPark(int parkId) {
		List<Campground> campByPark = new ArrayList<Campground>();
		
		String sqlListAllCampgroundsQuery = "SELECT * "+
				   							"FROM Campground " +
				   							"WHERE park_id = ? " +
				   							"ORDER BY name ASC";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListAllCampgroundsQuery, parkId);

		while(results.next()) {	
			Campground aCampground = mapRowToCampground(results);
			campByPark.add(aCampground);
		}
		
		return campByPark;
	}

	@Override
	public BigDecimal getCampgroundRate(int campgroundId) {
		List<Campground> campByPark = new ArrayList<Campground>();
		BigDecimal dailyRate = null;
		String sqlListAllCampgroundsQuery = "SELECT * "+
				   							"FROM campground " +
				   							"WHERE campground_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListAllCampgroundsQuery, campgroundId);

		while(results.next()) {	
			Campground dailyRates = mapRowToCampground(results);
			campByPark.add(dailyRates);
		}
		for(int i = 0; i < campByPark.size(); i++) {
			dailyRate = campByPark.get(i).getDaily_fee(); 
			
		}
		return dailyRate;
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
	
	@Override
	public List<Campground> getOpen(Campground campgroundId) {
		List<Campground>openForBusiness = new ArrayList<Campground>();
		String sqlListOpenMonthQuery = " SELECT open_from_mm, open_to_mm " +
										" FROM campground " +
										" WHERE campground_id = ? ";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListOpenMonthQuery, campgroundId);
		
		while(results.next()) {	
			Campground aCampground = mapRowToCampground(results);
			openForBusiness.add(aCampground);
		}
		return openForBusiness;
	}

	
	public String stringMonth(String month) {
		
		switch(month) {
		case "01":
			return "January";
		case "02":
			return "February";
		case "03":
			return "March";
		case "04":
			return "April";
		case "05":
			return "May";
		case "06":
			return "June";
		case "07":
			return "July";
		case "08":
			return "August";
		case "09":
			return "September";
		case "10":
			return "October";
		case "11":
			return "November";
		case "12":
			return "December";
		default:
			return "Invalid Month";
			
		}
	}
	
//	-------------------------------	HELPER METHODS	------------------------------
	
	
	

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
		
		theCampground.setCampground_id(results.getInt("campground_id"));
		theCampground.setPark_id(results.getInt("park_id"));		
		theCampground.setName(results.getString("name"));
		theCampground.setOpen_from_mm(results.getString("open_from_mm"));
		theCampground.setOpen_to_mm(results.getString("open_to_mm"));
		theCampground.setDaily_fee(results.getBigDecimal("daily_fee"));
		
		return theCampground;
	}
	
}
