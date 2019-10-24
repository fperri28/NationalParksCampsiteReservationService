package com.techelevator.park;

import java.util.List;

public interface ParkDAO {

// 	C.R.U.D (Create (Insert) - Read (Select) - Update - Delete)
	
	//	Create Method - Add a row to the table
	public Park addLocation(Park newPark);						//	Insert a row into the Park table from a Park Object

	// 	Read Method(s) - Select a row(s) from the table
	public Park getParkById(int aParkId);						//	Select a Park by it's ID (Primary Key)
	
	public List<Park> getAllParks();							//	Return all the Park
	
	//	Update Method - Update (returns nothing)
	public void changeParkData(Park aPark);						//	Update a Park in the table using values in the location object

	//	Delete Method - Delete (returns nothing)
	public void deleteParkById(String aParkId);					//	Delete a Park by it's ID (Primary Key)
	
}
