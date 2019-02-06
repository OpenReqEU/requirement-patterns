package edu.upc.gessi.rptool.domain.patternelements;

import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * This class represents a fixed part, 
 * a subclass of Pattern Item without any 
 * more attributes.
 */
@Entity
@Table(name = "FIXED_PATTERN")
public class FixedPart extends PatternItem {

}