package com.gswrapper.excepciones;

/**
 * 
 * IllegalCommandException.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public class IllegalCommandException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public IllegalCommandException(String mensajeError) {
		
		super(mensajeError);
	}
}
