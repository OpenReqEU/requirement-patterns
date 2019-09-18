package edu.upc.gessi.rptool.rest.dtos.patternelements;

import java.net.URI;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;

public class ReducedPatternObjectDTO {
    protected Long id;

    @InjectLink(value = "patternObjects/${instance.id}/dependencies", style = Style.ABSOLUTE)
    private URI dependenciesUri;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public URI getDependenciesUri() {
	return dependenciesUri;
    }

    public void setDependenciesUri(URI uri) {
	this.dependenciesUri = uri;
    }

    public ReducedPatternObjectDTO(Long identifier) {
	id = identifier;
    }
}
