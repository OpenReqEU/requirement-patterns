package edu.upc.gessi.rptool.data;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;
import static org.apache.uima.fit.util.JCasUtil.select;
import static org.apache.uima.fit.util.JCasUtil.selectCovered;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.testing.factory.TokenBuilder;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.clearnlp.ClearNlpLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.clearnlp.ClearNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.clearnlp.ClearNlpSegmenter;
import edu.upc.gessi.rptool.domain.patternelements.Keyword;
import edu.upc.gessi.rptool.domain.patternelements.RequirementForm;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPattern;
import edu.upc.gessi.rptool.domain.patternelements.RequirementPatternVersion;
import edu.upc.gessi.rptool.exceptions.IntegrityException;

public class NLPController {

    private static final Logger logger = Logger.getLogger(NLPController.class.getName());

    /**
     * Constructor has to be private so no one be able to create any instance
     */
    private NLPController() {
    }

    public static final String SEPARATOR = ". ";
    public static final String LANGUAGE = "en";

    public static String lemmatizePatternLastVersion(RequirementPattern rp) throws Exception {
	return lemmatizeVersion(rp.findLastVersion());
    }

    public static String lemmatizeVersion(RequirementPatternVersion rpv) throws IntegrityException, UIMAException {
	return lemmatizeVersionGivenLanguage(rpv, LANGUAGE);
    }

    public static String lemmatizeVersionGivenLanguage(RequirementPatternVersion rpv, String language)
	    throws IntegrityException, UIMAException {
	if (rpv == null) {
	    throw new IntegrityException("Null Pattern version cannot be lemmatized");
	}
	String original = "";

	// add pattern name
	original += rpv.getRequirementPattern().getName() + SEPARATOR;

	if (!rpv.getRequirementPattern().getDescription().equals("")) {
	    // add pattern description
	    original += rpv.getRequirementPattern().getDescription() + SEPARATOR;
	}
	// add keywords
	if (rpv.getKeywords() != null && !rpv.getKeywords().isEmpty()) {
	    for (Keyword key : rpv.getKeywords()) {
		original += key.getName();
		original += SEPARATOR;
	    }
	}
	// add forms name
	for (RequirementForm form : rpv.getForms()) {
	    original += form.getName();
	    original += SEPARATOR;
	}

	logger.debug("Lemmatized string: " + original);
	return lemmatize(original);
    }

    /**
     * Given a text, lemmatize the given text with the default language
     * 
     * @param text
     *            Text to be lemmatized
     * @return String with all the lemmas separated with spaces
     * @throws UIMAException
     */
    public static String lemmatize(String text) throws UIMAException {
	return lemmatize(text, LANGUAGE);
    }

    /**
     * Given a text and a language, lemmatize the given text, and return a text the
     * lemmatized words.
     * 
     * @param text
     *            Text to be lemmatized
     * @param language
     *            Language of the given text
     * @return Lemmatized text, all the words are separated by a space character at
     *         the end
     * @throws UIMAException
     */
    public static String lemmatize(String text, String language) throws UIMAException {
	String ret = "";

	// Create analysis engines
	AnalysisEngineDescription segmenter = createEngineDescription(ClearNlpSegmenter.class,
		ClearNlpSegmenter.PARAM_LANGUAGE, language);
	AnalysisEngineDescription posTagger = createEngineDescription(ClearNlpPosTagger.class,
		ClearNlpPosTagger.PARAM_LANGUAGE, language);
	AnalysisEngineDescription lemmatizer = createEngineDescription(ClearNlpLemmatizer.class,
		ClearNlpLemmatizer.PARAM_LANGUAGE, language);

	// Create JCas
	JCas jcas = JCasFactory.createJCas();
	jcas.setDocumentText(text);
	jcas.setDocumentLanguage(language);

	runPipeline(jcas, segmenter, posTagger, lemmatizer);

	// generate lemmatized string
	for (Sentence s : select(jcas, Sentence.class)) {
	    List<Lemma> lemmas = selectCovered(Lemma.class, s);
	    for (int i = 0; i < lemmas.size(); i++) {
		ret += lemmas.get(i).getValue();
		ret += " ";
	    }
	}
	return ret;

    }

    /**
     * Given a text, lemmatize the given text, and return a text the lemmatized
     * words.
     * 
     * @param text
     *            Text to be lemmatized
     * @return Lemmatized text, all the words are separated by a dot ('.')
     * @throws UIMAException
     *             Lemmatization exception
     */
    public static String lemmatization(String text) throws UIMAException {

	AnalysisEngineDescription tagger = createEngineDescription(ClearNlpPosTagger.class);
	AnalysisEngineDescription lemma = createEngineDescription(ClearNlpLemmatizer.class);
	AnalysisEngine LemmaEngine = createEngine(createEngineDescription(tagger, lemma));

	JCas jcas = runParser(LemmaEngine, LANGUAGE, text);
	Collection<Lemma> lemmas = JCasUtil.select(jcas, Lemma.class);
	String ret = "";
	String[] terms = text.split(" ");
	int i = 0;
	if (!lemmas.isEmpty()) {
	    for (Lemma l : lemmas) {
		if (!l.getValue().matches("\\d+")) { // if is not a digit
		    if (!ret.equals(""))
			ret = ret + " " + l.getValue();
		    else
			ret = l.getValue();
		} else { // if is a digit
		    if (!ret.equals(""))
			ret = ret + " " + terms[i];
		    else
			ret = terms[i];
		}
		i++;
	    }
	}
	return ret;
    }

    /**
     * Run Parser in with the given engine to process
     * 
     * @param aEngine
     *            Engines to be processed
     * @param aLanguage
     *            Language to be processed
     * @param aText
     *            Text to be processed
     * @return {@link JCas} with the words procced
     * @throws UIMAException
     */
    public static JCas runParser(AnalysisEngine aEngine, String aLanguage, String aText) throws UIMAException {

	JCas jcas = aEngine.newJCas(); // create a new JCas

	jcas.setDocumentLanguage(aLanguage); // Set the language

	TokenBuilder<Token, Sentence> tb = new TokenBuilder<Token, Sentence>(Token.class, Sentence.class);

	tb.buildTokens(jcas, aText); // build

	aEngine.process(jcas);// procces

	return jcas;
    }

}
