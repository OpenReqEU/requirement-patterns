package edu.upc.gessi.rptool.listeners;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

import edu.upc.gessi.rptool.data.mediators.MediatorConnection;

public class ResourceManagerListener implements ServletContextListener {
    static final Logger logger = Logger.getLogger(ResourceManagerListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
	MediatorConnection.initialize();
	MediatorConnection.setupGenerator(); // Setup the generator
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
	SessionFactory sessionFactory = MediatorConnection.getSessionFactory();
	if (sessionFactory != null)
	    sessionFactory.close();// Close session factory to releases resources

	if (MediatorConnection.isIsDerbyEmbedded()) { // Check if the conection is in a Embedded Derby
	    try {
		logger.debug("Calling connection: " + MediatorConnection.getConnectionURL() + ";shutdown=true");
		// Call the Driver to shutdown the Embedded client
		DriverManager.getConnection(MediatorConnection.getConnectionURL() + ";shutdown=true");
	    } catch (SQLException e) {
		logger.error("Derby is shutting down.", e);
	    }
	}
	// This, manually deregisters JDBC driver, which prevents Tomcat from
	// complaining about memory leaks
	Enumeration<Driver> drivers = DriverManager.getDrivers();
	while (drivers.hasMoreElements()) {
	    Driver driver = drivers.nextElement();
	    try {
		DriverManager.deregisterDriver(driver);
		logger.debug("Deregistering jdbc driver: " + driver);
	    } catch (SQLException e) {
		logger.debug("ERROR Deregistering jdbc driver: " + driver);
	    }
	}

    }
}
