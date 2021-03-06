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
	
	
	@Override
	public Reservation addReservations(String name, LocalDate fromDate, LocalDate toDate, int siteId) {
		Reservation newRes = new Reservation();
		
		String sqlAddNewRes = 	"INSERT INTO reservation " + 
								"(reservation_id, site_id, name, from_date, to_date, create_date) " + 
								"VALUES (?, ?, ?, ?, ?, ?)";
		
		newRes.setReservation_id(getNextReservationId());
		newRes.setName(name);
		newRes.setFrom_date(fromDate);
		newRes.setTo_date(toDate);
		newRes.setCreate_date(LocalDate.now());
		newRes.setSite_id(siteId);
		
		jdbcTemplate.update(sqlAddNewRes, 
							newRes.getReservation_id(), 
							newRes.getSite_id(),
							newRes.getName(), 
							newRes.getFrom_date(), 
							newRes.getTo_date(),
							newRes.getCreate_date());

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
		
		String sqlListAllResQuery = 	"  SELECT * " +
										" FROM reservation " +
										" INNER JOIN site ON reservation.site_id = site.site_id " +
										" WHERE from_date  BETWEEN ? AND ? " +
										" AND to_date  BETWEEN ? AND ? " +
										" AND campground_id = ? " +
										" GROUP BY site.campground_id, reservation.reservation_id, site.site_id ";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListAllResQuery, fromDate, toDate, fromDate, toDate, siteId);

		while(results.next()) {	
			Reservation aReservation = mapRowToReservation(results);
			reservationbySite.add(aReservation);
		}
		
		return reservationbySite;
	}

	@Override
	public List<Reservation> getReservationsForNext30(LocalDate fromDate, LocalDate toDate, int parkId) {
		List<Reservation> reservationForNext30 = new ArrayList<Reservation>();
		
		String sqlListAllEResFor30DaysQuery =	"SELECT * " + 
												"FROM reservation " + 
												"INNER JOIN site ON reservation.site_id = site.site_id " + 
												"INNER JOIN campground ON site.campground_id = campground.campground_id " + 
												"INNER JOIN park ON campground.park_id = park.park_id " + 
												"WHERE from_date BETWEEN ? AND ? " + 
												"AND park.park_id = ? " + 
												"ORDER BY from_date ASC ";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListAllEResFor30DaysQuery, fromDate, toDate, parkId);

		while(results.next()) {	
			Reservation aReservation = mapRowToReservation(results);
			reservationForNext30.add(aReservation);
		}
		
		return reservationForNext30;
	}
	
	
	@Override
	public List<Reservation> getReservationsBySite(int siteId, int campId) {
		// TODO Auto-generated method stub
		return null;
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
	public List<Reservation> getReservationByName(String name) {
		List<Reservation> theReservation = new ArrayList<Reservation>();
		
		String searchTermName = "%" + name + "%";
		String sqlListReservation = "SELECT * " +
									"FROM reservation " +
									"WHERE name " +
									"ILIKE ? ";
		
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlListReservation, searchTermName);
		
		while(results.next()) {
			theReservation.add(mapRowToReservation(results));
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
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('reservation_reservation_id_seq')");
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
