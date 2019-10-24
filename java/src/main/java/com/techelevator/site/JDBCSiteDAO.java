package com.techelevator.site;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class JDBCSiteDAO implements SiteDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public boolean addLocation(Site newSite) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Site getSiteById(String aSite) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Site> getAllSites() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Site> getSiteByCampground(String campground) {
		// TODO Auto-generated method stub
		return null;
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
	public void deleteSiteBySite(String campground) {
		// TODO Auto-generated method stub
		
	}

}
