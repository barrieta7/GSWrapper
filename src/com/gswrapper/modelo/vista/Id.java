package com.gswrapper.modelo.vista;

/**
 * 
 * Id.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public class Id {

	private String clave;
	private int fila; 
	private int columna;
	private int dimension;
	
	public Id(String clave, int fila, int columna, int dimension) {
		
		this.clave = clave;
		this.fila = fila;
		this.columna = columna;
		this.dimension = dimension;
	}
	
	
	public String getClave() {
		return clave;
	}

	public int getFila() {
		return fila;
	}

	public int getColumna() {
		return columna;
	}

	public int getDimension() {
		return dimension;
	}
}
