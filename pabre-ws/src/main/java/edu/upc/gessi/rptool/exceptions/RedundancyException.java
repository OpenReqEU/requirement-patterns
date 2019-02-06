package edu.upc.gessi.rptool.exceptions;

/*
 * This class represents the exception that we launch 
 * when an error has been produced because a data redundancy
 * (i.e.: 2 elements with the same primary key in one set, etc.) 
 */
public class RedundancyException extends RPToolException {
    private static final long serialVersionUID = 1L;
}
