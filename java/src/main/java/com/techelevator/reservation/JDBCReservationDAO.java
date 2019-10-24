package com.techelevator.reservation;

import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class JDBCReservationDAO implements ReservationDAO{

	private JdbcTemplate jdbcTemplate;
	
	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	
	@Override
	public boolean addReservations(Reservation newReservation) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Reservation> getAllReservations() {
		// TODO Auto-generated method stub
		return null;
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

}
