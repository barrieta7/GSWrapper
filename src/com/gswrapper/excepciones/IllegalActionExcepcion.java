package com.gswrapper.excepciones;

/**
 * 
 * IllegalActionExcepcion.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public class IllegalActionExcepcion extends Exception{

	private static final long serialVersionUID = 1L;

	public IllegalActionExcepcion(String mensajeError) {
		
		super(mensajeError);
	}
	
}
