package com.gswrapper.excepciones;

/**
 * 
 * IllegalScreenException.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public class IllegalScreenException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public IllegalScreenException(String mensajeError) {
		
		super(mensajeError);
	}
	
}
