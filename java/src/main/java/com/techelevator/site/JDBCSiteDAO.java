package com.techelevator.site;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.Campground;
import com.techelevator.reservation.Reservation;

public class JDBCSiteDAO implements SiteDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Site addLocation(Site newSite) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Site getSiteById(String aSiteId) {
		Site theSite = null;
		String sqlSearchSitesById = 	"SELECT * " + 
										"FROM site " + 
										"WHERE site_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchSitesById, aSiteId);		
		while(results.next()) {	
			theSite = mapRowToSite(results);
		}
		return theSite;
	}
	
	@Override
	public List<Site> getAllCampgroundsByParkId(int parkId){
		
		List<Site> allCampsgroundsInAPark = new ArrayList<Site>();
		//  THIS IS AN SQL TO RETURN ONLY A LIST OF UNIQUE CAMPGROUND_ID
		String sqlGetAllCampgroundsFromParkId = "SELECT DISTINCT campground.campground_id " + 
												"FROM site " + 
												"INNER JOIN campground " + 
												"ON campground.campground_id = site.campground_id " + 
												"WHERE park_id = ? "
												;
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampgroundsFromParkId, parkId);
		while(results.next()) {
			Site aCampground = mapRowSite(results);
			allCampsgroundsInAPark.add(aCampground);
		}
		
		return allCampsgroundsInAPark;
	}


	@Override
	public List<Site> getAvailableCampgroundResByPark(int parkId, LocalDate fromDate, LocalDate toDate, String fromMonth, String toMonth){
		////////// THIS IS RETURNING A LIST THAT DOESN'T REPEATS THE FIRST CAMPGROUNDS SITES 5 TIMES OVER AND OVER
		
		List<Site> reservationByAllCampgroundsInAPark = new ArrayList<Site>();
		
		List<Site> totalCamps = getAllCampgroundsByParkId(parkId);
		
		for(int i = 0; i < totalCamps.size(); i++) {

			String sqlListAllResBySiteQuery = 	" SELECT * " +
												" FROM site " +
												" INNER JOIN campground " +
												" ON campground.campground_id = site.campground_id " +
												" WHERE park_id = ? " +
												" AND campground.campground_id = ? " +
												" AND site_id NOT IN (SELECT reservation.site_id " +
												" FROM reservation " +
												" INNER JOIN site ON reservation.site_id = site.site_id " +
												" WHERE from_date BETWEEN ? AND ? " +
												" AND to_date BETWEEN ? AND ? " +
												" GROUP BY site.campground_id, reservation.reservation_id, site.site_id ) " +
												" AND open_from_mm <= ? AND open_to_mm >= ? " +
												" LIMIT 5 ";

			SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListAllResBySiteQuery, parkId, totalCamps.get(i).getCampground_id(), fromDate, toDate, fromDate, toDate, fromMonth, toMonth);

			while(results.next()) {	
				Site aFreeSite = mapRowToSite(results);
				reservationByAllCampgroundsInAPark.add(aFreeSite);
			}
		}
		
		return reservationByAllCampgroundsInAPark;
	}
	
	
	@Override
	public List<Site> getAvailableResBySite(int campId, int parkId, LocalDate fromDate, LocalDate toDate, String fromMonth, String toMonth){
		List<Site> reservationByAvailableSites = new ArrayList<Site>();
		
		String sqlListAllResBySiteQuery = 	" SELECT * " +
											" FROM site " +
											" INNER JOIN campground " +
											" ON campground.campground_id = site.campground_id " +
											" WHERE park_id = ? " +
											" AND campground.campground_id = ? " +
											" AND site_id NOT IN (SELECT reservation.site_id " +
											" FROM reservation " +
											" INNER JOIN site ON reservation.site_id = site.site_id " +
											" WHERE from_date BETWEEN ? AND ? " +
											" AND to_date BETWEEN ? AND ? " +
											" GROUP BY site.campground_id, reservation.reservation_id, site.site_id ) " +
											" AND open_from_mm <= ? AND open_to_mm >= ? " +
											" LIMIT 5 ";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListAllResBySiteQuery, parkId, campId, fromDate, toDate, fromDate, toDate, fromMonth, toMonth);

		while(results.next()) {	
			Site aFreeSite = mapRowToSite(results);
			reservationByAvailableSites.add(aFreeSite);
		}
		
		return reservationByAvailableSites;
	}

	@Override
	public List<Site> getAllSites() {
		List<Site> allSites = new ArrayList<Site>();
		
		String sqlListAllSitesQuery = 	"SELECT * " + 
										"FROM site " + 
										"ORDER BY name ASC";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListAllSitesQuery);
		
		while(results.next()) {	
			Site aSite = mapRowToSite(results);
			allSites.add(aSite);
		}
		return allSites;
	}

	@Override
	public List<Site> getSiteByCampground(int campgroundId) {
		List<Site> siteByCampground = new ArrayList<Site>();
		
		String sqlListAllSitesQuery = "SELECT * "+
				   					  "FROM site " +
				   					  "WHERE campground_id = ? " +
				   					  "LIMIT 5";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListAllSitesQuery, campgroundId);

		while(results.next()) {	
			Site aSite = mapRowToSite(results);
			siteByCampground.add(aSite);
		}
		
		return siteByCampground;
	}
	
	@Override
	public Site getDailyFeeByCampgroundId(int campgroundId, int siteId) {
		Site dailyFeeBySiteId = null;
		String sqlListAllSitesQuery = 	"SELECT daily_fee, site_id " + 
										"FROM campground " + 
										"LEFT JOIN site " + 
										"ON campground.campground_id = site.campground_id " + 
										"WHERE campground.campground_id = ? AND site.site_id = ? " + 
										"ORDER BY site.site_id " 
										;
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListAllSitesQuery, campgroundId, siteId);

		while(results.next()) {	
			dailyFeeBySiteId = mapRowToSite(results);
		}
		
		return dailyFeeBySiteId;
	}
	
	@Override
	public List<Site> getCampgroundBySite(int siteId) {
		List<Site> campgroundBySite = new ArrayList<Site>();
		
		String sqlListAllSitesQuery = "SELECT campground_id "+
				   					  "FROM site " +
				   					  "WHERE site_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListAllSitesQuery, siteId);

		while(results.next()) {	
			Site aSite = mapRowToSite(results);
			campgroundBySite.add(aSite);
		}
		
		return campgroundBySite;
	}
	
	@Override
	public String stringTrueFalseSwitch(boolean trueFalseStatement) {

		int trueOrFalse;
		if(trueFalseStatement == true){
			trueOrFalse = 1;
		}else{
			trueOrFalse = 0;
		}
		switch (trueOrFalse){
		case 1:
			if (trueFalseStatement){
				return "Yes";
			}
			break;
		case 0:
			if (!trueFalseStatement){
				return "No";
			}
			break;
		default:
			return "Unknown";
		}
		return "Unknown";
	}
	
	@Override
	public String stringRV(Integer rvLength) {
		
		if (rvLength == 0) {
			return "N/A";
		} else {
			String stringLength = rvLength.toString();
			return stringLength;
		}
	}

	@Override
	public void changeSiteData(Site aSite) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSiteById(String aSiteId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSiteByCampground(String campground) {
		// TODO Auto-generated method stub
		
	}

//	-------------------------------	HELPER METHODS	------------------------------	
	
	private Site mapRowToSite(SqlRowSet results) {
		Site theSite = new Site(); 
		
		theSite.setSite_id(results.getInt("site_id"));
		theSite.setCampground_id(results.getInt("campground_id"));		
		theSite.setSite_number(results.getInt("site_number"));	
		theSite.setMax_occupancy(results.getInt("max_occupancy"));
		theSite.setAccessible(results.getBoolean("accessible"));
		theSite.setMax_rv_length(results.getInt("max_rv_length"));
		theSite.setUtilities(results.getBoolean("utilities"));
		
		return theSite;
	}
	
	private Site mapRowSite(SqlRowSet results) {
		Site theSite = new Site(); 
		theSite.setCampground_id(results.getInt("campground_id"));		
		return theSite;
	}

	
}
