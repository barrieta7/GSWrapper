package com.gswrapper.excepciones;

/**
 * 
 * TerminalTimeOutException.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public class TerminalTimeOutException extends Exception {

	private static final long serialVersionUID = 1L;

	public TerminalTimeOutException(String mensajeError) {
		
			super(mensajeError);
	 }
}
