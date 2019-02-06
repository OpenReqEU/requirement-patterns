package edu.upc.gessi.rptool.rest.dtos;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class NewDependencyDTO {
    long id;
    Set<Long> dependencies;

    public NewDependencyDTO(/* DependencyObject d */) {
	id = 2;
	dependencies = new HashSet<>();
	dependencies.add((long) 5);
	dependencies.add((long) 6);

    }

    public long getId() {
	return id;
    }

    public void setId(long l) {
	id = l;
    }

    public Set<Long> getDependencies() {
	return dependencies;
    }

    public void setDependencies(Set<Long> s) {
	dependencies = s;
    }

}
