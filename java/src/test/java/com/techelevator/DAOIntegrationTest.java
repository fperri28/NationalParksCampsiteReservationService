package com.techelevator;

import static org.junit.Assert.fail;

import java.sql.SQLException;

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
import com.techelevator.site.JDBCSiteDAO;

public abstract class DAOIntegrationTest {

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
		
		String sqlInsertReservation = " ";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource); 
		jdbcTemplate.batchUpdate(sqlInsertReservation);
		
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
	public void get_campground_by_id_test() {
		
		Campground theCampround = campDAO.getCampgroundById(1);
		
		
		
		
		fail("Not yet implemented");
		
		
		
		
		
	}
}
