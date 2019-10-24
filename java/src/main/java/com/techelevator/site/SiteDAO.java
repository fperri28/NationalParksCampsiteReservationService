package com.techelevator.site;

import java.util.List;

public interface SiteDAO {

// 	C.R.U.D (Create (Insert) - Read (Select) - Update - Delete)
	
	//	Create Method - Add a row to the table
	public Site addLocation(Site newSite);						//	Insert a row into the site table from a site Object

	// 	Read Method(s) - Select a row(s) from the table
	public Site getSiteById(String aSiteId);					//	Select a sites by it's ID (Primary Key)
	
	public List<Site> getAllSites();							//	Return all the Sites
	
	public List<Site> getSiteByCampground(String campground);	//	Return all sites in a site
	
	//	Update Method - Update (returns nothing)
	public void changeSiteData(Site aSite);						//	Update a Site in the table using values in the location object

	//	Delete Method - Delete (returns nothing)
	public void deleteSiteById(String aSiteId);					//	Delete a Site by it's ID (Primary Key)
	
	public void deleteSiteByCampground(String campground);		//	Delete all rows for a Site
	
	
}
