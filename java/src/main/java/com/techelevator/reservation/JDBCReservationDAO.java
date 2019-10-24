package com.techelevator.reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;


public class JDBCReservationDAO implements ReservationDAO{

	private JdbcTemplate jdbcTemplate;
	
	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	
	@Override
	public Reservation addReservations(Reservation newReservation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reservation> getAllReservations() {
		List<Reservation> allReservations = new ArrayList<Reservation>();
		
		String sqlListAllReservationsQuery = 	"SELECT * " + 
												"FROM reservation " + 
												"ORDER BY name ASC";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListAllReservationsQuery);
		
		while(results.next()) {	
			Reservation aReservation = mapRowToReservation(results);
			allReservations.add(aReservation);
		}
		return allReservations;
	}

	@Override
	public List<Reservation> getReservationsByDate(LocalDate fromDate, LocalDate toDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reservation> getReservationsBySite(Long SiteId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Reservation getReservationById(int aReservationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reservation> getResByMaxOcc(int occ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateReservation(Long resID, LocalDate fromDate, LocalDate toDate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delReservation(Long resID) {
		// TODO Auto-generated method stub
		return false;
	}

//	-------------------------------	HELPER METHODS	---------------------------------

	private int getNextReservationId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_reservation_id')");
		if (nextIdResult.next()) {
			return nextIdResult.getInt(1);
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new reservation");
		}
	}
	
	private Reservation mapRowToReservation(SqlRowSet results) {
		Reservation theReservation = new Reservation(); 
		
		theReservation.setReservation_id(results.getInt("reservation_id"));
		theReservation.setSite_id(results.getInt("site_id"));		
		theReservation.setName(results.getString("name"));
		theReservation.setFrom_date(results.getDate("from_date").toLocalDate());
		theReservation.setTo_date(results.getDate("to_date").toLocalDate());
		theReservation.setCreate_date(results.getDate("create_date").toLocalDate());
		
		return theReservation;
	}

}
