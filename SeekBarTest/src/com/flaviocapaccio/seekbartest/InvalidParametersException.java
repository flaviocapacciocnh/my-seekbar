package com.flaviocapaccio.seekbartest;

public class InvalidParametersException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public InvalidParametersException() {
		message = "Parametri non validi! Controlla min, max e step!";
	}
	
	public InvalidParametersException(String m){
		message = m;
	}
	
	public String getMessage(){
		return message;
	}
}
