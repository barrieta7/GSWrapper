package com.gswrapper.modelo.vista;

import com.gswrapper.modelo.wrapper.*;

/**
 * 
 * ComandoPantalla.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public class ComandoPantalla {
	
	private Comando comando;
	private Object[] argumentos;
	
	public ComandoPantalla(Comando comando, Object ... argumentos) {
		
		this.comando = comando;
		this.argumentos = argumentos;
	}

	public Comando getComando() {
		return comando;
	}

	public Object[] getArgumentos() {
		return argumentos;
	}
}
