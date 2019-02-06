package edu.upc.gessi.rptool.rest.unmarshallers.keywords;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.patternelements.Keyword;

public class KeywordUnmarshaller {
    private long id;
    private String name;

    @JsonCreator
    public KeywordUnmarshaller(@JsonProperty(value = "id", required = false) long id,
	    @JsonProperty(value = "name", required = true) String name) {
	this.id = id;
	this.name = name;
    }

    public Keyword build() {
	return new Keyword(id, name);
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

}
