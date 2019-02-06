package edu.upc.gessi.rptool.rest.dtos.importexport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import edu.upc.gessi.rptool.domain.patternelements.Keyword;

@JsonInclude(Include.NON_NULL)
public class KeywordExportDTO {
    private long id;
    private String name;

    public KeywordExportDTO(Keyword keyword) {
	this.id = keyword.getId();
	this.name = keyword.getName();
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public void setName(String n) {
	name = n;
    }

    public String getName() {
	return name;
    }
}