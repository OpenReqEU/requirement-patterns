package edu.upc.gessi.rptool.data.mediators;

import java.io.Serializable;

import edu.upc.gessi.rptool.config.Control;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import edu.upc.gessi.rptool.domain.CustomId;
import edu.upc.gessi.rptool.domain.Identificable;

/**
 * This class manage the generation of the IDs
 * 
 * @author Awais Iqbal
 *
 */
public class IdGenerator implements IdentifierGenerator {

    /**
     * Logger attribute
     */
    private static final Logger logger = Logger.getLogger(IdGenerator.class.getName());

    /**
     * Holder to save the last used ID
     */
    private static CustomId idHolder;

    /**
     * String to indicate in exception, cause of exception
     */
    public static final String IDEXCEPTIONSTRING = "ID FAILED";

    public IdGenerator() {
    	//utility class
    }

    public static synchronized long getCurrentID() {
	return idHolder.getNextIDToUse();
    }

    public static synchronized void setCurrentID(long id) {
	idHolder.setNextIDToUse(id);
    }

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
	logger.debug("Generating id for class: " + object.getClass().getSimpleName());
	Serializable s = null;
	if (object instanceof Identificable) {
	    logger.debug("Object implements Identificable");
	    Identificable i = (Identificable) object;
	    if (i.getId() == 0) {
		synchronized (this) {
		    s = generateNewUnusedID();
		    logger.debug("Object dosen't have assigned ID, assigning: " + s);
		    MediatorGeneric.update(idHolder);
		}
	    } else {
		logger.debug("Object already have ID");
		if (i.getId() == 1) {
		    throw new HibernateException("[" + IDEXCEPTIONSTRING + "]ID 1 is reserved for the system.");
		} else if (MediatorGeneric.checkIfExists(i.getId())) {
		    throw new HibernateException(
			    "[" + IDEXCEPTIONSTRING + "]ID: " + i.getId() + " is already used, try another one");
		}
		return i.getId();
	    }
	} else {
		Control.getInstance().showErrorMessage("ID Generation failed due to object doesn't implements Identificable");
	}
	return s;
    }

    /**
     * Generate a new ID which is not used
     * 
     * @return Next ID to use
     */
    private Serializable generateNewUnusedID() {
	long s = idHolder.useNewID();
	boolean idAlreadyUsed = MediatorGeneric.checkIfExists(s);
	while (idAlreadyUsed) {
	    s = idHolder.useNewID();
	    idAlreadyUsed = MediatorGeneric.checkIfExists(s);
	}
	return s;
    }

    public static CustomId getIdHolder() {
	return idHolder;
    }

    public static void setIdHolder(CustomId idHolder) {
	IdGenerator.idHolder = idHolder;
    }

}
