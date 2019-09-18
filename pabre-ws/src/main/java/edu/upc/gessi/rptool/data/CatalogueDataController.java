package edu.upc.gessi.rptool.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.uima.UIMAException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.fasterxml.jackson.core.JsonParseException;

import edu.upc.gessi.rptool.data.mediators.MediatorConnection;
import edu.upc.gessi.rptool.data.mediators.MediatorGeneric;
import edu.upc.gessi.rptool.domain.CostFunction;
import edu.upc.gessi.rptool.domain.PatternObjectDependency;
import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.domain.metrics.SetMetric;
import edu.upc.gessi.rptool.domain.metrics.SimpleMetric;
import edu.upc.gessi.rptool.domain.patternelements.Keyword;
import edu.upc.gessi.rptool.domain.patternelements.Parameter;
import edu.upc.gessi.rptool.domain.patternelements.PatternItem;
import edu.upc.gessi.rptool.domain.patternelements.PatternObject;
import edu.upc.gessi.rptool.domain.patternelements.RequirementForm;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;
import edu.upc.gessi.rptool.domain.schema.ClassificationSchema;
import edu.upc.gessi.rptool.exceptions.IntegrityException;
import edu.upc.gessi.rptool.exceptions.RedundancyException;
import edu.upc.gessi.rptool.exceptions.ValueException;
import edu.upc.gessi.rptool.rest.dtos.importexport.DependenciesExportDTO;
import edu.upc.gessi.rptool.rest.dtos.importexport.ExportDTO;
import edu.upc.gessi.rptool.rest.dtos.importexport.ImportDTO;
import edu.upc.gessi.rptool.rest.dtos.importexport.KeywordExportDTO;
import edu.upc.gessi.rptool.rest.dtos.importexport.SourceExportDTO;
import edu.upc.gessi.rptool.rest.dtos.importexport.classifications.SchemaExportDTO;
import edu.upc.gessi.rptool.rest.dtos.importexport.metrics.MetricExportDTO;
import edu.upc.gessi.rptool.rest.dtos.importexport.patternelements.RequirementPatternExportDTO;
import edu.upc.gessi.rptool.rest.exceptions.MissingCreatorPropertyException;
import edu.upc.gessi.rptool.rest.exceptions.SemanticallyIncorrectException;
import edu.upc.gessi.rptool.rest.unmarshallers.patterns.ImportUnmarshaller;

/**
 * This class contains the controller for specific operations necessaries for
 * the {@link Keyword}, {@link Source}, {@link Parameter}, {@link PatternItem} ,
 * {@link CostFunction} and {@link PatternObjectDependency}.
 */
@SuppressWarnings("unchecked")
public final class CatalogueDataController {

    private static final Logger logger = Logger.getLogger(CatalogueDataController.class.getName());

    /**
     * Constructor has to be private so no one be able to create any instance
     */
    private CatalogueDataController() {
    }

    /**
     * Imports a complete catalog into the system
     * 
     * @param iu
     *            unmarshaller to be imported
     * @return lists of all the imported objects with they name and id assigned
     * @throws SemanticallyIncorrectException
     * @throws MissingCreatorPropertyException
     * @throws IntegrityException
     * @throws ValueException
     * @throws HibernateException
     * @throws RedundancyException
     * @throws IOException
     * @throws JsonParseException
     * @throws UIMAException
     */
    public static ImportDTO importCatalogue(ImportUnmarshaller iu)
	    throws SemanticallyIncorrectException, MissingCreatorPropertyException, IntegrityException, ValueException,
	    RedundancyException, JsonParseException, IOException, UIMAException {

	iu.buildAndSaveSources();
	saveSources(iu.getSourcesOBJ());

	iu.buildKeywords();
	saveKeywords(iu.getKeywordsOBJ());

	iu.buildAndSaveMetrics();

	iu.buildPatterns();
	logger.debug("Pattern builded");
	savePatterns(iu.getPatternsOBJ());

	iu.buildAndSaveSchemas();

	iu.buildAndSaveDependencies();

	ImportDTO importDTO = null;
	importDTO = new ImportDTO(iu.getSourcesOBJ(), iu.getKeywordsOBJ(), iu.getMetricsOBJ(), iu.getPatternsOBJ(),
		iu.getSchemasOBJ(), iu.getDependenciesOBJ());
	return importDTO;

    }

    /**
     * Given a list of patterns to save in the system
     * 
     * @param patternsOBJ
     *            list of patterns
     * @throws IntegrityException
     * @throws UIMAException
     */
    private static void savePatterns(List<RequirementPattern> patternsOBJ) throws IntegrityException, UIMAException {
	logger.debug("Starting to save the patterns");
	for (int i = 0; i < patternsOBJ.size(); i++) {
	    PatternDataController.saveNewPattern(patternsOBJ.get(i)); // Call the controller to save the requirement
								      // pattern
	}
	MediatorConnection.flush();
    }

    /**
     * Saves all the keywords given by parameter
     * 
     * @param keywordsOBJ
     *            list of keywords
     */
    private static void saveKeywords(List<Keyword> keywordsOBJ) {
	for (int i = 0; i < keywordsOBJ.size(); i++) {
	    MediatorGeneric.save(keywordsOBJ.get(i));
	}
	MediatorConnection.flush();
    }

    /**
     * Saves all the sources given by parameter
     * 
     * @param sourcesOBJ
     *            List of sources
     */
    private static void saveSources(List<Source> sourcesOBJ) {
	for (int i = 0; i < sourcesOBJ.size(); i++) {
	    MediatorGeneric.save(sourcesOBJ.get(i));
	}
	MediatorConnection.flush();
    }

    /**
     * Exports all the objects saved in the database, ready to be imported again
     * 
     * @return Object with all the fields to be exported
     * @throws SemanticallyIncorrectException
     */
    public static ExportDTO exportCatalogue() throws SemanticallyIncorrectException {
	ExportDTO exportDTO = new ExportDTO();

	List<Source> listSources = MediatorGeneric.list(Source.class);
	List<SourceExportDTO> listSourceDTO = new ArrayList<>();
	for (int i = 0; i < listSources.size(); i++) {
	    listSourceDTO.add(new SourceExportDTO(listSources.get(i)));
	}
	exportDTO.setSources(listSourceDTO);

	List<Keyword> listKeywords = MediatorGeneric.list(Keyword.class);
	List<KeywordExportDTO> listKeywordsDTO = new ArrayList<>();
	for (int i = 0; i < listKeywords.size(); i++) {
	    listKeywordsDTO.add(new KeywordExportDTO(listKeywords.get(i)));
	}
	exportDTO.setKeywords(listKeywordsDTO);

	List<Metric> listMetrics = MediatorGeneric.list(Metric.class);
	List<MetricExportDTO> listMetricDTO = new ArrayList<>();
	for (int i = 0; i < listMetrics.size(); i++) {
	    listMetricDTO.add(new MetricExportDTO(listMetrics.get(i)));
	}
	exportDTO.setMetrics(listMetricDTO);

	List<RequirementPattern> listPatterns = MediatorGeneric.list(RequirementPattern.class);
	List<RequirementPatternExportDTO> listPatternDTO = new ArrayList<>();
	for (int i = 0; i < listPatterns.size(); i++) {
	    listPatternDTO.add(new RequirementPatternExportDTO(listPatterns.get(i)));
	}
	exportDTO.setPatterns(listPatternDTO);

	List<ClassificationSchema> listSchemas = MediatorGeneric.list(ClassificationSchema.class);
	List<SchemaExportDTO> listSchemasDTO = new ArrayList<>();
	for (int i = 0; i < listSchemas.size(); i++) {
	    listSchemasDTO.add(new SchemaExportDTO(listSchemas.get(i)));
	}
	exportDTO.setSchemas(listSchemasDTO);

	List<PatternObject> listPatternObjects = MediatorGeneric.list(PatternObject.class);
	List<DependenciesExportDTO> listDependenciesDTO = new ArrayList<>();
	for (int i = 0; i < listPatternObjects.size(); i++) {
	    PatternObject po = listPatternObjects.get(i);
	    if (!po.getDependencies().isEmpty()) {
		DependenciesExportDTO deDTO = new DependenciesExportDTO(po.getId(), po.getDependencies());
		listDependenciesDTO.add(deDTO);
	    }

	}
	exportDTO.setDependencies(listDependenciesDTO);

	return exportDTO;
    }

    /**
     * Given a String, lemmatize that string and search all the lemmas inside the
     * {@link RequirementPattern} name or description, {@link Keyword} name,
     * {@link RequirementForm} name.
     * 
     * @param words
     *            String with the words to search separated by words
     * @return List of {@link RequirementPattern} who contain any words as
     *         substring.
     * @throws UIMAException
     */
    public static Collection<RequirementPattern> searchPatternWithWords(String words) throws UIMAException {
	// Lemmatize the entry string
	String lemmatized = NLPController.lemmatize(words.toLowerCase());
	logger.info("Searching lemmas: " + lemmatized);
	// Split the lemmas to a list
	ArrayList<String> l = new ArrayList<>(Arrays.asList(lemmatized.split("\\s+")));
	// return the pattern with that words
	return searchPatternWithWordsInPatternVersions(l);
    }

    /**
     * Given list of words, search that words as substring of any field like:
     * {@link RequirementPattern} name or description, {@link Keyword} name,
     * {@link RequirementForm} name.
     * 
     * @param list
     *            List of String to search
     * @return List of {@link RequirementPattern} who contain any words as
     *         substring.
     */
    public static Collection<RequirementPattern> searchPatternWithWords(List<String> list) {
	Session session = MediatorConnection.getCurrentSession();
	if (list == null || list.isEmpty()) {
	    return MediatorGeneric.list(RequirementPattern.class);
	}
	String from1 = " from RequirementPattern as p "; // Table of patterns
	String from2 = " left join p.versions as v "; // Table of versions
	String from3 = " left join v.forms as f "; // Table of forms
	String cond1 = "  v.versionDate = (select max(v2.versionDate) from p.versions v2) "; // Last version
	StringBuilder patternConditions = new StringBuilder();
	StringBuilder keywordSubquery = new StringBuilder("select k from v.keywords k where ");
	StringBuilder formSubquery = new StringBuilder();

	for (int i = 0; i < list.size(); i++) {
	    // Add pattern conditions
	    patternConditions.append(" lower(p.name) LIKE concat('%',:param").append(i).append(",'%') ");
	    patternConditions.append(" or lower(p.description) LIKE concat('%',:param").append(i).append(",'%') ");

	    // Add keyword conditions
	    keywordSubquery.append(" lower(k.name) LIKE concat('%',:param").append(i).append(",'%') ");

	    // Add form conditions
	    formSubquery.append(" lower(f.name) LIKE concat('%',:param").append(i).append(",'%') ");

	    // Add OR if there are more conditions
	    if (i != list.size() - 1) { // Add OR while is not the last
		patternConditions.append(" or ");
		keywordSubquery.append(" or ");
		formSubquery.append(" or ");
	    }
	}

	// Create the query
	Query query = session.createQuery("select p " + from1 + from2 + from3 + " where " + cond1 + " and ("
		+ patternConditions + " or exists (" + keywordSubquery + ") or " + formSubquery + "   )");

	// Set comparing name parameter
	for (int i = 0; i < list.size(); i++) {
	    query.setParameter("param" + i, list.get(i));
	}
	logger.debug(query.toString());
	List<RequirementPattern> patterns = query.list();
	return new HashSet<>(patterns);
    }

    /**
     * Given list of words, search that words as substring of the
     * {@link RequirementPatternVersion#getLemmatizedVersion()}
     * 
     * @param list
     *            List of String to search
     * @return List of {@link RequirementPattern} who contain any words as
     *         substring.
     */
    public static Collection<RequirementPattern> searchPatternWithWordsInPatternVersions(List<String> list) {
	Session session = MediatorConnection.getCurrentSession();
	if (list == null || list.isEmpty()) {
	    return MediatorGeneric.list(RequirementPattern.class);
	}
	String from1 = " from RequirementPattern as p "; // Table of patterns
	String from2 = " left join p.versions as v "; // Table of versions
	// String cond1 = " v.versionDate = (select max(v2.versionDate) from p.versions
	// v2) "; // Last version
	StringBuilder patternConditions = new StringBuilder();

	for (int i = 0; i < list.size(); i++) {
	    // Add pattern conditions
	    patternConditions.append(" lower(v.lemmatizedVersion) LIKE concat('%',:param").append(i).append(",'%') ");
	    // Add OR if there are more conditions
	    if (i != list.size() - 1) { // Add OR while is not the last
		patternConditions.append(" or ");
	    }
	}

	// Create the query
	Query query = session.createQuery("select p " + from1 + from2 + " where (" + patternConditions + " )");

	// Set comparing name parameter
	for (int i = 0; i < list.size(); i++) {
	    query.setParameter("param" + i, list.get(i));
	}
	logger.debug(query.toString());
	List<RequirementPattern> patterns = query.list();
	return new HashSet<>(patterns);
    }

    /**
     * This method delete all the schemas, patterns, metrics, keywords and sources.
     * After this call all the database should be clean without any record (except
     * the IDHolder)
     * 
     * @throws HibernateException
     * @throws IntegrityException
     * @throws SemanticallyIncorrectException
     */
    public static void clearDatabase() throws HibernateException, IntegrityException, SemanticallyIncorrectException {
	// Schemas
	List<ClassificationSchema> l = MediatorGeneric.list(ClassificationSchema.class);
	for (ClassificationSchema classificationSchema : l) {
	    SchemaDataController.delete(classificationSchema);
	}

	// Patterns
	List<RequirementPattern> lrp = MediatorGeneric.list(RequirementPattern.class);
	for (RequirementPattern requirementPattern : lrp) {
	    PatternDataController.deletePattern(requirementPattern);
	}

	// Metrics
	List<Metric> lm = MediatorGeneric.list(SetMetric.class);// Delete Set first
	for (Metric metric : lm) {
	    MetricDataController.delete(metric);
	}

	lm = MediatorGeneric.list(SimpleMetric.class);// Delete Simple metrics
	for (Metric metric : lm) {
	    MetricDataController.delete(metric);
	}

	// Keywords
	List<Keyword> lk = MediatorGeneric.list(Keyword.class);
	for (Keyword keyword : lk) {
	    GenericDataController.delete(keyword);
	}

	// Sources
	List<Source> ls = MediatorGeneric.list(Source.class);
	for (Source source : ls) {
	    GenericDataController.delete(source);
	}

	MediatorConnection.flush();
    }

    /**
     * @see MediatorGeneric#checkIfExists(long)
     */
    public static boolean checkIfExists(long id) {
	return MediatorGeneric.checkIfExists(id);
    }

}
