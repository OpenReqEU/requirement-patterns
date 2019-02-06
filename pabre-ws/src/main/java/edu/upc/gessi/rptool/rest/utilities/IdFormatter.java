package edu.upc.gessi.rptool.rest.utilities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdFormatter {
    public IdFormatter(long l) {
	id = l;
    }

    @JsonProperty("id")
    private long id;

}
