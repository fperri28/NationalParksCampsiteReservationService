package com.techelevator.site;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.campground.Campground;

public interface SiteDAO {

// 	C.R.U.D (Create (Insert) - Read (Select) - Update - Delete)
	
	//	Create Method - Add a row to the table
	public boolean addLocation(Site newSite);		//	Insert a row into the site table from a site Object

	// 	Read Method(s) - Select a row(s) from the table
	public Site getSiteById(String aSite);	//	Select a campground by it's ID (Primary Key)
	
	public List<Campground> getAllCampgrounds();				//	Return all the campground
	
	public List<Campground> getCampgroundByPark(String park);	//	Return all campground in a park

	public BigDecimal getCampgroundRate(Campground aCampground);//	Return the rate for a campground
	
	
	//TODO Add a read method for getCampgroundByOpenDate?
	
	
	//	Update Method - Update (returns nothing)
	public void changeCampgroundData(Campground aCampground);	//	Update a campground in the table using values in the location object

	//	Delete Method - Delete (returns nothing)
	public void deleteCampgroundById(String aCampgroundId);		//	Delete a campground by it's ID (Primary Key)
	
	public void deleteCampgroundByPark(String park);			//	Delete all rows for a campground
	
	
	
}
