package com.techelevator.campground;

import java.math.BigDecimal;
import java.util.List;

public class JDBCCampgroundDAO implements CampgroundDAO{

	@Override
	public boolean addLocation(Campground newCampground) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Campground getCampgroundById(String aCampground) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Campground> getAllCampgrounds() {
		// TODO Auto-generated method stub
		return null;
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

}
