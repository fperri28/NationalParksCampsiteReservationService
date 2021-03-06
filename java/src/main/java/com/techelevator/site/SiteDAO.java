package com.techelevator.site;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.reservation.Reservation;

public interface SiteDAO {

// 	C.R.U.D (Create (Insert) - Read (Select) - Update - Delete)
	
	//	Create Method - Add a row to the table
	public Site addLocation(Site newSite);						//	Insert a row into the site table from a site Object

	// 	Read Method(s) - Select a row(s) from the table
	public Site getSiteById(String aSiteId);					//	Select a sites by it's ID (Primary Key)
	
	public List<Site> getAllSites();							//	Return all the Sites
	
	public List<Site> getSiteByCampground(int campgroundId);	//	Return all sites in a site

	public List<Site> getCampgroundBySite(int siteId);
	
	public List<Site> getAllCampgroundsByParkId(int parkId);
	
	public List<Site> getAvailableCampgroundResByPark(int parkId, LocalDate fromDate, LocalDate toDate, String fromMonth, String toMonth);
	
	public Site getDailyFeeByCampgroundId(int campgroundId, int siteId);
	
	//return all available reservations by site
	public List<Site> getAvailableResBySite(int campId, int parkId, LocalDate fromDate, LocalDate toDate, String fromMonth, String toMonth);
			
	public String stringTrueFalseSwitch(boolean trueFalseStatement);
	
	public String stringRV(Integer rvLength);
	
	//	Update Method - Update (returns nothing)
	public void changeSiteData(Site aSite);						//	Update a Site in the table using values in the location object

	//	Delete Method - Delete (returns nothing)
	public void deleteSiteById(String aSiteId);					//	Delete a Site by it's ID (Primary Key)
	
	public void deleteSiteByCampground(String campground);		//	Delete all rows for a Site
	
	
}
