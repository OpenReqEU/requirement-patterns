package edu.upc.gessi.rptool.exceptions;

/*
 * This class represents the exception that we launch 
 * when an ERROR IN THE DATA INTEGRITY may be produced 
 */
public class IntegrityException extends RPToolException {

    /*
     * ATTRIBUTES
     */

    private static final long serialVersionUID = 1L;
    private String message = null;

    /*
     * CREATORS
     */

    public IntegrityException(String msg) {
	message = "";
	message = msg;
    }

    /*
     * GET'S AND SET'S METHODS
     */

    public String getMsg() {
	return message;
    }

    public void setMsg(String message) {
	this.message = message;
    }

    @Override
    public String getMessage() {
	return this.message;
    }

}
