package edu.upc.gessi.rptool.rest.dtos;

import java.net.URI;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.patternelements.Keyword;

@JsonInclude(Include.NON_NULL)
public class KeywordDTO {

    @InjectLink(value = "keywords/${instance.id}", style = Style.ABSOLUTE)
    private URI uri;
    private long id;
    private String name;

    public KeywordDTO(Keyword k) {
	this.id = k.getId();
	this.name = k.getName();
    }

    public void setId(long id) {
	this.id = id;
    }

    public long getId() {
	return id;
    }

    public void setUri(URI u) {
	uri = u;
    }

    public URI getUri() {
	return uri;
    }

    public void setName(String n) {
	name = n;
    }

    public String getName() {
	return name;
    }
}