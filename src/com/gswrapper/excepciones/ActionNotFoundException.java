package com.gswrapper.excepciones;

/**
 * 
 * ActionNotFoundException.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 */
public class ActionNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public ActionNotFoundException(String mensajeError) {
		
		super(mensajeError);
	}

}
