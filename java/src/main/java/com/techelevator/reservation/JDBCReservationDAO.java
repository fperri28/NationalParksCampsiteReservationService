package com.techelevator.reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.park.Park;


public class JDBCReservationDAO implements ReservationDAO{

	private JdbcTemplate jdbcTemplate;
	
	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
/*
 * 
 * SELECT *
FROM reservation
INNER JOIN site ON reservation.site_id = site.site_id
WHERE from_date  BETWEEN '2019-01-01' AND '2019-12-30'
AND campground_id = 2
GROUP BY site.campground_id, reservation.reservation_id, site.site_id 
; 
 * 
 * 
 * 
 */
	
	
	
	@Override
	public Reservation addReservations(String name, LocalDate fromDate, LocalDate toDate) {
		Reservation newRes = new Reservation();
		
		String sqlAddNewRes = 	"INSERT INTO park " + 
								"(reservation_id, site_id, name, from_date, to_date, create_date) " + 
								"VALUES (?, ?, ?, ?, ?, ?)";
		
		newRes.setReservation_id(getNextReservationId()); 
		newRes.setName(name);
		newRes.setFrom_date(fromDate);
		newRes.setTo_date(toDate);
		newRes.setCreate_date(LocalDate.now());
		
		jdbcTemplate.update(sqlAddNewRes, 
							newRes.getReservation_id(), 
							newRes.getSite_id(),
							newRes.getName(), 
							newRes.getFrom_date(), 
							newRes.getTo_date(),
							newRes.getCreate_date()
											);

		return newRes;
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
	public List<Reservation> getReservationsByDate(int siteId, LocalDate fromDate, LocalDate toDate) {
		List<Reservation> reservationbySite = new ArrayList<Reservation>();
		
		String sqlListAllEmpQuery = 	"  SELECT * " +
										" FROM reservation " +
										" INNER JOIN site ON reservation.site_id = site.site_id " +
										" WHERE from_date  BETWEEN ? AND ? " +
										" AND to_date  BETWEEN ? AND ? " +
										" AND campground_id = ? " +
										" GROUP BY site.campground_id, reservation.reservation_id, site.site_id ";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListAllEmpQuery, fromDate, toDate, fromDate, toDate, siteId);

		while(results.next()) {	
			Reservation aReservation = mapRowToReservation(results);
			reservationbySite.add(aReservation);
		}
		
		return reservationbySite;
	}

	@Override
	public List<Reservation> getReservationsBySite(int siteId, int campId) {
		List<Reservation> reservationbySite = new ArrayList<Reservation>();
		
		String sqlListAllEmpQuery = 	"SELECT * " + 
										"FROM reservation " + 
										"WHERE site_id = ? AND " +
										"campground_id = ? "
										;
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListAllEmpQuery, siteId);

		while(results.next()) {	
			Reservation aReservation = mapRowToReservation(results);
			reservationbySite.add(aReservation);
		}
		
		return reservationbySite;
	}
	
	@Override
	public Reservation getReservationById(int aReservationId) {
		Reservation theReservation = null;
		String sqlSearchReservationsById = 	"SELECT * " + 
											"FROM reservation " + 
											"WHERE reservation_id = ? ";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchReservationsById, aReservationId);		
		while(results.next()) {	
			theReservation = mapRowToReservation(results);
		}
		return theReservation;
	}

	@Override
	public List<Reservation> getResByMaxOcc(int occ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateReservation(int resID, LocalDate fromDate, LocalDate toDate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delReservation(int resID) {
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
