package edu.upc.gessi.rptool.listeners;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.License;

/**
 * Servlet implementation class SwaggerServlet
 */
public class SwaggerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // API Description
    private static final String API_VERSION = "1.0.0";
    private static final String TITLE = "The PABRE-WS API";
    private static final String DESCRIPTION = "Pabre is a project from GESSI research group of Universitat Politècnica de Catalunya (UPC).<br/>"
	    + " <h3>Important information:</h3>" + "<ul>"
	    + "<li><strong>id</strong>: Every object has they own <strong>id</strong> to retrieve information from the catalogue.</li>"
	    + "<li><strong>id</strong>: <em>0</em> and <em>1</em> are reserved from the system.</li>"
	    + "<li><strong>name</strong>: The <strong>name</strong> of any object is unique at the same level, the childs of any classifiers have a unique name, bu the same name can be repeated at diferent level.</li>"
	    + "<li><strong>Updates</strong>: When using the PUT method, remember to not include all the fields, because that way they will be update, just but the fields to be updated not all.</li>"
	    + "<li><strong>Unmarshallers</strong>: Unmarshaller governs the process of deserializing JSON data into newly created Java content trees.</li>"
	    + "<li><strong>DTO</strong>: Data Transfer Object is a object that serialize Java object to JSON.</li>"
	    + "<li><strong>Date</strong>: The format used for date is: \"<strong>YYYY-MM-DD hh:mm:ss</strong>\" .</li>"
	    + "<li><strong>Errors</strong>: All the error messages has the same format \"[<strong>ExceptionType</strong>]: Exception message\".</li>"
	    + "<li><strong>ExceptionType</strong> can be:<ul>" + "<li>ContraintViolationException</li>"
	    + "<li>IntegrityException</li>" + "<li>JsonMappingException</li>" + "<li>JsonProcessingException</li>"
	    + "<li>MissingCreatorPropertyException</li>" + "<li>NotFoundException</li>"
	    + "<li>PropertyValueException</li>" + "<li>SemanticallyIncorrectException</li>"
	    + "<li>TransactionException</li>" + "<li>UnknownExpcetion</li>" + "<li>UnrecognizedPropertyException</li>"
	    + "<li>ValueException</li>" + "</ul></li>" + "</ul>";

    private static final String BASEPATH = "requirement-patterns/pabre-ws/api";

    // Package to search for resources
    private static final String RESOURCE_PACKAGE = "edu.upc.gessi.rptool.rest";

    // LICENSE
    private static final String LICENSE_NAME = "EPL-v1.0";
    private static final String LICENSE_URL = "https://www.eclipse.org/legal/epl-v10.html";

    // Contact information
    private static final String CONTACT_NAME = "Awais Iqbal Begum";
    private static final String CONTACT_URL = "https://www.upc.edu/gessi/PABRE/";
    private static final String CONTACT_EMAIL = "iqbal@essi.upc.edu";

    @Override
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	// License
	License l = new License();
	l.setName(LICENSE_NAME);
	l.setUrl(LICENSE_URL);

	// Contact
	Contact c = new Contact();
	c.setName(CONTACT_NAME);
	c.setEmail(CONTACT_EMAIL);
	c.setUrl(CONTACT_URL);

	Info info = new Info();
	info.setVersion(API_VERSION);
	info.setTitle(TITLE);
	info.setDescription(DESCRIPTION);
	info.setLicense(l);
	info.setContact(c);

	BeanConfig beanConfig = new BeanConfig();
	beanConfig.setSchemes(new String[] { "http" });
	beanConfig.setBasePath(BASEPATH);
	beanConfig.setResourcePackage(RESOURCE_PACKAGE);
	beanConfig.setScan(false);
	beanConfig.setPrettyPrint(true);
	beanConfig.setInfo(info);

    }

}
