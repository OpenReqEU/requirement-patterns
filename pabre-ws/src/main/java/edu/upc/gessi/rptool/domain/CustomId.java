package edu.upc.gessi.rptool.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CUSTOM_ID_HOLDER")
public class CustomId implements Identificable {

    @Id
    @Column(name = "ID")
    private long id;

    @Column(name = "NEXT_ID_TO_USE", nullable = false)
    private long nextIDToUse;

    public CustomId() {
	super();
    }

    public CustomId(long id, long nextId) {
	super();
	this.id = id;
	this.nextIDToUse = nextId;
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public long getNextIDToUse() {
	return nextIDToUse;
    }

    public void setNextIDToUse(long nextIDToUse) {
	this.nextIDToUse = nextIDToUse;
    }

    public long useNewID() {
	long ret = nextIDToUse;
	nextIDToUse++;
	return ret;
    }

}