package com.gswrapper.modelo.vista;

/**
 * 
 * Campo.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public class Campo {

	private String nombre;
	
	private int fila;
	private int columna;
	private int dimension;
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getFila() {
		return fila;
	}
	
	public void setFila(int fila) {
		this.fila = fila;
	}
	
	public int getColumna() {
		return columna;
	}
	
	public void setColumna(int columna) {
		this.columna = columna;
	}
	
	public int getDimension() {
		return dimension;
	}
	
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}
}
