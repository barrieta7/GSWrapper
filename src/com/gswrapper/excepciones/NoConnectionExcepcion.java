package com.gswrapper.excepciones;

/**
 * 
 * NoConnectionExcepcion.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-09-19
 *
 */
public class NoConnectionExcepcion extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoConnectionExcepcion(String mensajeError) {
		
		super(mensajeError);
	}
	
}
