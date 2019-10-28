package com.techelevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.campground.Campground;
import com.techelevator.campground.JDBCCampgroundDAO;
import com.techelevator.park.JDBCParkDAO;
import com.techelevator.reservation.JDBCReservationDAO;
import com.techelevator.reservation.Reservation;
import com.techelevator.site.JDBCSiteDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DAOIntegrationTest {

	//private static final String TEST_RES = "XYZ";
	private static SingleConnectionDataSource dataSource;
	private JDBCParkDAO 		parkDAO;
	private JDBCCampgroundDAO 	campDAO;
	private JDBCReservationDAO 	resDAO;
	private JDBCSiteDAO 		siteDAO;
	

	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);  
	}

	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}
	
	@Before
	public void setup() {
		
		parkDAO = new JDBCParkDAO(dataSource);
		campDAO = new JDBCCampgroundDAO(dataSource);
		resDAO = new JDBCReservationDAO(dataSource);
		siteDAO = new JDBCSiteDAO(dataSource);	
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void add_new_reservations_test() {
		Reservation testRes = resDAO.addReservations("Beach", LocalDate.of(2019, 11, 20), LocalDate.of(2019, 11, 22), 1);
		Assert.assertNotEquals(null, testRes);
		Assert.assertEquals("Oops", "Beach", testRes.getName());
	}
	
	@Test
	public void get_all_reservations_returns_list_test() {
		List<Reservation> testList = resDAO.getAllReservations();
		
		Assert.assertNotEquals(null, testList);
	}
	
	@Test
	public void get_reservations_by_date_test() {
		Reservation testRes = resDAO.addReservations("Beach", LocalDate.of(2019, 11, 20), LocalDate.of(2019, 11, 22), 1);
		List<Reservation> testList = resDAO.getReservationsByDate(1, LocalDate.of(2019, 11, 20), LocalDate.of(2019, 11, 22));
		Assert.assertNotEquals(false, testList.contains(testRes));
	}
	
	@Test
	public void get_reservations_for_next_30_days_test() {
		Reservation testRes = resDAO.addReservations("Beach", LocalDate.of(2019, 11, 20), LocalDate.of(2019, 11, 22), 1);
		List<Reservation> testList = resDAO.getReservationsForNext30(LocalDate.of(2019, 11, 20), LocalDate.of(2019, 11, 22), 1);
		Assert.assertNotEquals(false, testList.contains(testRes));
	}
	

	@Test
	public void get_reservation_by_id_test() {
		Reservation testRes = resDAO.addReservations("Beach", LocalDate.of(2019, 11, 20), LocalDate.of(2019, 11, 22), 1);
		testRes.getReservation_id();
		Reservation test = resDAO.getReservationById(testRes.getReservation_id());
		
		Assert.assertEquals("Beach", test.getName());
	}
	
	@Test
	public void get_reservation_by_name_test() {
		Reservation testRes = resDAO.addReservations("Beach", LocalDate.of(2019, 11, 18), LocalDate.of(2019, 11, 19), 1);
		testRes.getReservation_id();
		List<Reservation> test = resDAO.getReservationByName("Beach");
		
		Assert.assertNotEquals(true, test.isEmpty());
	}
	
	
	
	@Test
	public void get_campground_by_id_test() {
		Campground aCamp = new Campground();
		aCamp.setCampground_id(1);	
		aCamp.setName("Lorain");
		Campground theCampround = campDAO.getCampgroundById(1);
		
		Assert.assertNotEquals(false, theCampround.getCampground_id());
		
		
	}
}
