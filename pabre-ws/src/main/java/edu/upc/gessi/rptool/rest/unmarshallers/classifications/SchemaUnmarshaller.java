package edu.upc.gessi.rptool.rest.unmarshallers.classifications;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import javax.ws.rs.NotFoundException;

import org.hibernate.HibernateException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.data.IdToDomainObject;
import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.domain.schema.ClassificationSchema;
import edu.upc.gessi.rptool.domain.schema.Classifier;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.RedundancyException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;

public class SchemaUnmarshaller {
    private long id;
    private Set<Long> sources;
    private Set<ClassifierUnmarshaller> rootClassifiers;

    protected ClassificationSchema cs;
    protected String name;
    protected String description;
    protected String comments;

    @JsonCreator
    public SchemaUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "name", required = true) String name,
	    @JsonProperty(value = "description", required = true) String description,
	    @JsonProperty(value = "comments", required = true) String comments,
	    @JsonProperty(value = "sources", required = false) Set<Long> sourcesIds,
	    @JsonProperty(value = "rootClassifiers", required = true) Set<ClassifierUnmarshaller> rootClassifiersIds)
	    throws IntegrityException {
	cs = new ClassificationSchema();
	cs.setId(id);
	this.id = id;
	this.name = name;
	this.description = description;
	this.comments = comments;
	this.sources = sourcesIds;
	this.rootClassifiers = rootClassifiersIds;
	setSchemaFields();

    }

    public SchemaUnmarshaller(long id, String name, String description, String comments) {
	cs = new ClassificationSchema();
	cs.setId(id);
	this.id = id;
	this.name = name;
	this.description = description;
	this.comments = comments;
    }

    /**
     * Set the basic values of the instace
     * 
     * @throws IntegrityException
     */
    protected void setSchemaFields() throws IntegrityException {
	cs.setId(id);
	cs.setName(name);
	cs.setDescription(description);
	cs.setComments(comments);
    }

    /**
     * Unmarshall the schema setting all the values
     * 
     * @throws HibernateException
     * @throws SemanticallyIncorrectException
     * @throws IntegrityException
     * @throws RedundancyException
     */
    private void unmarshalSchema()
	    throws HibernateException, SemanticallyIncorrectException, IntegrityException, RedundancyException {
	setSources();
	unmarshalRoots();
    }

    /**
     * Method to set the schema sources
     * 
     * @throws SemanticallyIncorrectException
     */
    protected void setSources() throws SemanticallyIncorrectException {
	if (sources != null) {
	    try {
		Set<Source> s = IdToDomainObject.getSources(this.sources);
		cs.clearAndSetSources(s);
	    } catch (NotFoundException e) {
		throw new SemanticallyIncorrectException("invalid source id");
	    }
	}
    }

    /**
     * This method is used to set the root classifiers of this schema
     * 
     * @throws SemanticallyIncorrectException
     * @throws IntegrityException
     * @throws RedundancyException
     */
    protected void unmarshalRoots() throws SemanticallyIncorrectException, IntegrityException, RedundancyException {
	Set<ClassifierUnmarshaller> s = this.rootClassifiers;
	buildAndAddRootClassifiers(s);
    }

    /**
     * Method used to check if the variable "pos" is correctly indicated or not, in
     * case that is correctly indicated add that collection to the classifier as
     * root clessiers
     * 
     * @param s
     *            Collection of classifiers to add
     * @throws SemanticallyIncorrectException
     * @throws IntegrityException
     * @throws RedundancyException
     */
    protected void buildAndAddRootClassifiers(Collection<ClassifierUnmarshaller> s)
	    throws SemanticallyIncorrectException, IntegrityException, RedundancyException {
	boolean[] positions = new boolean[s.size()]; // initialized a false

	for (ClassifierUnmarshaller tmp : s) {
	    int pos = tmp.getPos();
	    if (pos < 0 || pos >= s.size()) {
		throw new SemanticallyIncorrectException("Incorrect pos value");
	    } else {
		positions[pos] = true;
	    }
	    Classifier c = tmp.build(true);
	    c.setParentSchema(cs);
	    cs.addRootClassifier(c);// Build and add the root inside de schema
	    c.setTypeToRoot();
	}
	for (boolean b : positions) {
	    if (!b) throw new SemanticallyIncorrectException("Incorrect pos value");
	}
    }

    /**
     * Method used to check if all the root classifiers unmarshallers have the field
     * ID assigned and is not equals to 0
     * 
     * @return True if all the classifiers unmarshallers has the field id != 0
     */
    public boolean checkAllItemsContainsID() {
	boolean b = id != 0;
	for (ClassifierUnmarshaller classifierUnmarshaller : rootClassifiers) {
	    if (!classifierUnmarshaller.checkAllItemsContainsID()) {
		b = false;
	    }
	}
	return b;
    }

    /**
     * Build the schema and returns the instace of that schema
     * 
     * @return Instance of {@link ClassificationSchema} with all the information of
     *         the unmarshaller
     * @throws HibernateException
     * @throws IntegrityException
     * @throws RedundancyException
     * @throws SemanticallyIncorrectException
     */
    public ClassificationSchema build()
	    throws HibernateException, IntegrityException, RedundancyException, SemanticallyIncorrectException {
	unmarshalSchema();
	return cs;
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public Set<Long> getSources() {
	return sources;
    }

    public void setSources(Set<Long> sources) {
	this.sources = sources;
    }

    public Set<ClassifierUnmarshaller> getRootClassifiers() {
	return rootClassifiers;
    }

    public void setRootClassifiers(Set<ClassifierUnmarshaller> rootClassifiers) {
	this.rootClassifiers = rootClassifiers;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getComments() {
	return comments;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

}
