package edu.upc.gessi.rptool.data.mediators;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import edu.upc.gessi.rptool.config.Control;
import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import edu.upc.gessi.rptool.domain.CostFunction;
import edu.upc.gessi.rptool.domain.CustomId;
import edu.upc.gessi.rptool.domain.ExternalObject;
import edu.upc.gessi.rptool.domain.PatternObjectDependency;
import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.domain.metrics.DomainMetric;
import edu.upc.gessi.rptool.domain.metrics.DomainMetricValue;
import edu.upc.gessi.rptool.domain.metrics.FloatMetric;
import edu.upc.gessi.rptool.domain.metrics.IntegerMetric;
import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.domain.metrics.MetricObject;
import edu.upc.gessi.rptool.domain.metrics.SetMetric;
import edu.upc.gessi.rptool.domain.metrics.SimpleMetric;
import edu.upc.gessi.rptool.domain.metrics.StringMetric;
import edu.upc.gessi.rptool.domain.metrics.TimePointMetric;
import edu.upc.gessi.rptool.domain.patternelements.Dependency;
import edu.upc.gessi.rptool.domain.patternelements.ExtendedPart;
import edu.upc.gessi.rptool.domain.patternelements.FixedPart;
import edu.upc.gessi.rptool.domain.patternelements.Keyword;
import edu.upc.gessi.rptool.domain.patternelements.Parameter;
import edu.upc.gessi.rptool.domain.patternelements.PatternElement;
import edu.upc.gessi.rptool.domain.patternelements.PatternItem;
import edu.upc.gessi.rptool.domain.patternelements.PatternObject;
import edu.upc.gessi.rptool.domain.patternelements.RequirementForm;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;
import edu.upc.gessi.rptool.domain.schema.ClassificationObject;
import edu.upc.gessi.rptool.domain.schema.ClassificationSchema;
import edu.upc.gessi.rptool.domain.schema.Classifier;

/**
 * This class contains all generic operations necessaries to create, initialize,
 * manage sessions and transactions.
 * 
 * @author Awais Iqbal
 *
 */
public class MediatorConnection {

	private MediatorConnection() {
		//utility class
	}

    /**
     * This attribute corresponds to the session used to connect with database
     */
    private static SessionFactory sessionFactory = null;

    /**
     * This atribute show the Path where the database is connected.<br/>
     * Example: jdbc:derby:C:\Users\AWAISI~1\AppData\Local\Temp\pabreDatabase
     */
    private static String connectionURL = "";

	public static String getConnectionURL() {
		return connectionURL;
	}

	/**
     * This atribute shows if the connection is maded in a Embedded Derby
     */
    private static boolean isDerbyEmbedded = false;

	public static boolean isIsDerbyEmbedded() {
		return isDerbyEmbedded;
	}

	private static final Logger logger = Logger.getLogger(MediatorConnection.class.getName());

    /**
     * This method check if the sessionFactory is initialized, if is not initialized
     * use the method {@link MediatorConnection#initialize()}
     */
    protected static void checkSessionFactory() {
	if (MediatorConnection.sessionFactory == null) {
	    MediatorConnection.initialize();
	}
    }

    /**
     * This method return the current sessionFactory of Hibernate
     * 
     * @return SessionFactory of Hibernate
     */
    public static SessionFactory getSessionFactory() {
	return sessionFactory;
    }

    /**
     * Reinitialize the database and get a session to connect with it. This method
     * is useful to create from zero a new database with the configuration in the
     * "schema.cfg.xml" file. NOTE: the execution of this method clear all data
     * saved in the database (if it exists).
     */
    public static void createSchema() {
	logger.debug("Creating schema...");
	Configuration cfg = new Configuration();
	cfg.configure("config/hibernate.cfg.xml");
	cfg.setProperty("hibernate.hbm2ddl.auto", "create");
	MediatorConnection.buildSession(cfg);
    }

    /**
     * This method initialize a session to connect with database. The configuration
     * in this case is in the "hibernate.cfg.xml" file.
     */
    public static void initialize() {
	logger.debug("Initializing schema...");
	if (sessionFactory == null) {
	    Configuration cfg = new Configuration();
	    cfg.configure("config/hibernate.cfg.xml");
	    MediatorConnection.buildSession(cfg);
	}
    }

    /**
     * This method build a new session with the configuration received as parameter.
     * 
     * @param cfg
     *            The configuration which we want to get session.
     */
    static void buildSession(Configuration cfg) {
	logger.debug("building session");
	MediatorConnection.checkEmbeddedRoute(cfg);
	MediatorConnection.addClasses(cfg);
	sessionFactory = cfg.buildSessionFactory();
	MediatorConnection.setupGenerator();
    }

    /**
     * Given a Hibernate configuration check if the driver is embedded in that case
     * checks if contains a route inside the {@code connection.url}, in case where
     * is null then adds the current directory where the software is executed. The
     * {@code connection.directory} cannot end with'/'.
     * 
     * @param cfg
     *            Configuration file to check
     */
    static void checkEmbeddedRoute(Configuration cfg) {
	logger.debug("checking if the database is embedded");
	String driver = cfg.getProperty("hibernate.connection.driver_class");
	if (driver != null && driver.contains("EmbeddedDriver") && driver.contains("derby")) {
	    isDerbyEmbedded = true;
	    logger.debug("database is embedded derby");
	    String dir = cfg.getProperty("connection.directory");
	    File f;

	    // SET DB name
	    String databaseName = "pabreDatabase";
	    String indicatedDatabaseName = cfg.getProperty("connection.database.name");
	    if (indicatedDatabaseName != null && !indicatedDatabaseName.equals("")) {
		databaseName = indicatedDatabaseName;
	    }

	    if (dir != null && !dir.equals("")) {
		f = new File(dir);
	    } else {
		logger.debug("No directory indicated, using the current execution directory, watchout the permissions");
		f = new File(".");
		logger.debug("Using directory: " + f.getAbsolutePath());
	    }

	    boolean canBeCreated = MediatorConnection.checkFileCanbeCreated(f);

	    if (!f.canRead() || !f.canWrite() || !canBeCreated) {
		logger.error("Indicated directory: " + f.getAbsolutePath() + " permissions:\nWRITE: " + f.canWrite()
			+ "\nREAD: " + f.canRead() + "\nCREATE FILE: " + canBeCreated + "\nUsing a TMP directory");
		f = MediatorConnection.getTempFilePath();
		f = new File(f.getAbsolutePath());
		logger.error("using temp directory: " + f.getAbsolutePath());
	    }
	    connectionURL = "jdbc:derby:" + f.getAbsolutePath() + File.separator + databaseName;
	    String url = connectionURL + ";create=true";
	    logger.debug("setting property: hibernate.connection.url= " + url);
	    logger.debug("Connectiong to database: " + url);
	    cfg.setProperty("hibernate.connection.url", url);
	}
    }

    /**
     * Method used to check if the given directory has permission to create a File
     * 
     * @param f
     *            File to check
     * @return True if can be created otherwise, False
     */
    static boolean checkFileCanbeCreated(File f) {
    	String path = f.getAbsolutePath() + File.separator + "tmpFile";
	File newFile = new File(path);
	boolean created = newFile.mkdir();
	if (created) {
		try {
			Files.delete(Paths.get(path));
		} catch (IOException e) {
			created = false;
		}
	}
	return created;
    }

    protected static File getTempFilePath() {
	String tempFilePath = null;
	try {
	    File temp = File.createTempFile("temp-file-name", ".tmp");
	    String absolutePath = temp.getAbsolutePath();
	    tempFilePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
	    Files.delete(Paths.get(absolutePath));// Delete temporary file
	} catch (IOException e) {
		Control.getInstance().showErrorMessage(e.getMessage());
	}
	return new File(tempFilePath);
    }

    /**
     * This method return the current session related the current {@link Thread}. If
     * the current session is null, the method initialize it with the method
     * {@link initialize}.
     * 
     * @return The current session with the database
     */
    public static Session getCurrentSession() {
	checkSessionFactory();
	return sessionFactory.getCurrentSession();
    }

    /**
     * This method returns the current opened session. In the case where current
     * session is null, the method it will initialize it with the method
     * {@link initialize}. NOTE: Using this method you have to commit and close the
     * session manually!
     * 
     * @return Current opened session with the database.
     */
    public static Session openNewSession() {
	checkSessionFactory();
	return sessionFactory.openSession();
    }

    /**
     * Get the session bind with this session and begin a transaction in it.
     * 
     * @return The new session getting with a transaction started in them.
     */
    public static Session beginTransaction() {
	Session session = null;
	try {
	    session = getCurrentSession();
	    session.beginTransaction();
	    if (!FlushMode.isManualFlushMode(session.getFlushMode())) {
		session.setFlushMode(FlushMode.MANUAL);
	    }
	} catch (HibernateException e) {
	    MediatorConnection.checkSessionAndRollback(e, session);
	}
	return session;
    }

    /**
     * End the transaction in the session received as parameter.
     * 
     * @param session
     *            The session in which we want to end the transaction.
     */
    public static void endTransaction(Session session) {
	try {
	    session.flush();
	    session.getTransaction().commit();
	} catch (HibernateException e) {
	    MediatorConnection.checkSessionAndRollback(e, session);
	}
    }

    /**
     * End the transaction of the current session.
     */
    public static void endCurrentSessionTransaction() {
	endTransaction(getCurrentSession());
    }

    /**
     * Setup the custom ID generator, creates the entry to the database to save the
     * has ID: 1 and the first ID to use for the client is ID=2
     */
    public static void setupGenerator() {
	Session s = MediatorConnection.beginTransaction();
	// Obtain the instace of the ID holder/generator
	long id = 1;
	CustomId idHolder = (CustomId) s.createCriteria(CustomId.class).add(Restrictions.idEq(id)).uniqueResult();
	if (idHolder == null) {
	    idHolder = new CustomId();
	    idHolder.setId(1);
	    idHolder.setNextIDToUse(2);
	    s.save(idHolder);
	}
	IdGenerator.setIdHolder(idHolder);
	MediatorConnection.endTransaction(s);

    }

    /**
     * This method check if the session if ok and rollback, throwing the exception
     * 
     * @param exp
     *            Exception to throw
     * @param session
     *            Session to check
     */
    protected static void checkSessionAndRollback(HibernateException exp, Session session) {
	if (session != null && session.getTransaction() != null) {
	    session.getTransaction().rollback();
	}
	throw exp;
    }

    /**
     * Force this session to flush. Must be called at the end of a unit of work,
     * before committing the transaction and closing the session (depending on
     * {@link #setFlushMode flush-mode}, {@link Transaction#commit()} calls this
     * method).
     * <p/>
     * <i>Flushing</i> is the process of synchronizing the underlying persistent
     * store with persistable state held in memory.
     *
     * @throws HibernateException
     *             Indicates problems flushing the session or talking to the
     *             database.
     */
    public static void flush() {
	getCurrentSession().flush();
    }

    public static void addClasses(Configuration cfg) {
	cfg.addPackage("edu.upc.gessi.rptool.domain");// Add the information about the custom id generator
	// Add main package
	cfg.addAnnotatedClass(CostFunction.class);
	cfg.addAnnotatedClass(CustomId.class);
	cfg.addAnnotatedClass(ExternalObject.class);
	cfg.addAnnotatedClass(PatternObjectDependency.class);
	cfg.addAnnotatedClass(Source.class);

	// Add Metrics package
	cfg.addAnnotatedClass(DomainMetric.class);
	cfg.addAnnotatedClass(DomainMetricValue.class);
	cfg.addAnnotatedClass(FloatMetric.class);
	cfg.addAnnotatedClass(IntegerMetric.class);
	cfg.addAnnotatedClass(Metric.class);
	cfg.addAnnotatedClass(MetricObject.class);
	cfg.addAnnotatedClass(SetMetric.class);
	cfg.addAnnotatedClass(SimpleMetric.class);
	cfg.addAnnotatedClass(StringMetric.class);
	cfg.addAnnotatedClass(TimePointMetric.class);

	// Add patternElements package
	cfg.addAnnotatedClass(Dependency.class);
	cfg.addAnnotatedClass(ExtendedPart.class);
	cfg.addAnnotatedClass(FixedPart.class);
	cfg.addAnnotatedClass(Keyword.class);
	cfg.addAnnotatedClass(Parameter.class);
	cfg.addAnnotatedClass(PatternElement.class);
	cfg.addAnnotatedClass(PatternItem.class);
	cfg.addAnnotatedClass(PatternObject.class);
	cfg.addAnnotatedClass(RequirementForm.class);
	cfg.addAnnotatedClass(RequirementPattern.class);
	cfg.addAnnotatedClass(RequirementPatternVersion.class);

	// Add schema package
	cfg.addAnnotatedClass(ClassificationObject.class);
	cfg.addAnnotatedClass(ClassificationSchema.class);
	cfg.addAnnotatedClass(Classifier.class);

    }

}
