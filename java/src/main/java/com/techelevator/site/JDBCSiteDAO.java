package com.techelevator.site;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.Campground;

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
	public List<Site> getSiteByCampground(String campground) {
		List<Site> siteByCampground = new ArrayList<Site>();
		
		String sqlListAllSitesQuery = "SELECT * "+
				   					  "FROM site " +
				   					  "WHERE campground_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListAllSitesQuery, campground);

		while(results.next()) {	
			Site aSite = mapRowToSite(results);
			siteByCampground.add(aSite);
		}
		
		return siteByCampground;
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
	
}
