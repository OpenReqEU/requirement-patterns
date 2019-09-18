package edu.upc.gessi.rptool.rest.dtos.importexport.metrics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.upc.gessi.rptool.domain.Source;
import edu.upc.gessi.rptool.domain.metrics.Metric;
import edu.upc.gessi.rptool.domain.metrics.SetMetric;
import edu.upc.gessi.rptool.domain.metrics.TimePointMetric;
import edu.upc.gessi.rptool.domain.metrics.Type;

@JsonInclude(Include.NON_NULL)
public class GenericMetricExportDTO {

    @JsonProperty(value = "id")
    private Long id;
    @JsonIgnore
    protected Type type;
    protected String name;
    protected String description;
    protected String comments;
    @JsonProperty(value = "sourcesByIdentifier")
    protected List<String> sources;

    // DATE
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date dat2;

    @JsonProperty(value = "date")
    protected String dateInString;

    // SET
    @JsonProperty(value = "idSimple")
    protected Long idSimple;

    public GenericMetricExportDTO(Metric m) {
	this.name = m.getName();

	this.description = m.getDescription();
	if (this.description == null)
	    this.description = "";

	this.comments = m.getComments();
	if (this.comments == null)
	    this.comments = "";

	if (m.getSources() != null && !m.getSources().isEmpty()) { // Create sources only if the metric has any
	    this.sources = new ArrayList<>(); // Create a new List of sources with only they identifier
	    Set<Source> sourcesAux = m.getSources();
	    for (Source source : sourcesAux) {
		this.sources.add(source.getIdentifier());
	    }
	}
	this.id = m.getId();

	switch (m.getType()) {
	case SET:
	    type = Type.SET;
	    SetMetric sm = (SetMetric) m;
	    this.id = m.getId();
	    this.idSimple = sm.getSimple().getId();

	    break;
	case TIME:
	    this.type = Type.TIME;

	    TimePointMetric tpm = (TimePointMetric) m;
	    dateInString = tpm.getDateInString();

	    break;
	default:
	    break;

	}
    }

    public Type getType() {
	return type;
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

    public List<String> getSources() {
	return sources;
    }

    public void setSources(List<String> sources) {
	this.sources = sources;
    }

    public Long getIdSimple() {
	return idSimple;
    }

    public void setIdSimple(Long idSimple) {
	this.idSimple = idSimple;
    }

    public void setType(Type type) {
	this.type = type;
    }

    public String getDateInString() {
	return dateInString;
    }

    public void setDateInString(String dateInString) {
	this.dateInString = dateInString;
    }

}
