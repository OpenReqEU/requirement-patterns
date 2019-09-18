package edu.upc.gessi.rptool.domain.schema;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import edu.upc.gessi.rptool.exceptions.IntegrityException;

@Entity
@Table(name = "CLASSIFICATION_SCHEMA")
public class ClassificationSchema extends ClassificationObject {

    static final Logger logger = Logger.getLogger(ClassificationSchema.class);

    /*
     * ATTRIBUTES
     */

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "parentSchema", orphanRemoval = true, fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    private Set<Classifier> rootClassifiers;

    /*
     * CREATORS
     */

    public ClassificationSchema() {
	rootClassifiers = new HashSet<>();
    }

    /*
     * GET'S AND SET'S METHODS
     */

    public String getName() {
	return name;
    }

    public void setName(String newname) throws IntegrityException {
	// The name of the ClassificationObject can't be an empty value
	// If new name is null or it has a length equal to 0 (an empty string),
	// we launch an integrity exception
	if (newname == null || newname.trim().length() == 0)
	    throw new IntegrityException("You have to introduce a name");
	name = newname;
    }

    public Set<Classifier> getRootClassifiers() {
	return rootClassifiers;
    }

    public void setRootClassifiers(Set<Classifier> s) {
	rootClassifiers = s;
    }

    /**
     * The root classifier received as param is added to the root classifiers of
     * this Schema
     * 
     * @param root
     *            The root classifier that we will add to this Schema
     * @throws IntegrityException
     *             We launch an integrity exception when this Schema already
     *             contains another root classifier with the same name
     */
    public void addRootClassifier(Classifier root) throws IntegrityException {
	if (checkRepeatedNameRootClassifier(root.getName())) {
	    rootClassifiers.add(root);
	} else {
	    throw new IntegrityException(
		    "You cannot add this root classifier: the schema already contains a root classifier with this name.");
	}
    }

    /**
     * The root classifier received as param is removed of this Schema
     * 
     * @param child
     *            The root classifier that we will remove to this Schema
     */
    public void removeRootClassifier(Classifier root) throws IntegrityException {

	if (rootClassifiers.size() == 1)
	    throw new IntegrityException(
		    "You cannot delete this classifier: the schema must have at least one root classifier.");
	rootClassifiers.remove(root);
    }

    public void removeRootClassifierNoCheck(Classifier root) {
	rootClassifiers.remove(root);
    }

    /**
     * Method used to know if this Schema already contains a Root Classifier with
     * the name n.
     * 
     * @param n
     *            The name that we want to test
     * @return - True, if any other root classifier of this Schema have the name n -
     *         False, in another case
     */
    private boolean checkRepeatedNameRootClassifier(String n) {
	Iterator<Classifier> i = rootClassifiers.iterator();
	while (i.hasNext()) {
	    Classifier r = i.next();
	    if (r.getName().equals(n)) {
		return false;
	    }
	}
	return true;
    }

    /**
     * Method used to create a new ClassificationSchema with the same state (same
     * value for his attributes) than original.
     * 
     * @return a new ClassificationSchema with the same value for his attributes
     *         than original
     * @throws Exception
     */
    public ClassificationSchema copy() throws Exception {
	ClassificationSchema newClassifSchema = new ClassificationSchema();

	try {
	    // Set attributes of the new schema equal than copied schema
	    newClassifSchema.setName(this.getName());
	    newClassifSchema.setDescription(this.getDescription());
	    newClassifSchema.setComments(this.getComments());

	    // copying sources
	    newClassifSchema.setSources(new HashSet<>(getSources()));

	    // Copying the root classifiers
	    for (Classifier c : getRootClassifiers()) {
		newClassifSchema.addRootClassifier(c.copy());
	    }

	    return newClassifSchema;
	} catch (IntegrityException e) {
	    String msg = "Error trying to copy a ClassificationSchema";
	    logger.error(msg);
	    throw e;
	}
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ClassificationSchema other = (ClassificationSchema) obj;
	if (name == null) {
	    if (other.name != null) return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }

}