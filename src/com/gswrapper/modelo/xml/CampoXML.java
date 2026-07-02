package com.gswrapper.modelo.xml;

import javax.xml.bind.annotation.*;

/**
 * 
 * CampoXML.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
@XmlRootElement(name = "campo")
@XmlAccessorType(XmlAccessType.FIELD)
public class CampoXML {

	@XmlAttribute
	private String nombre;
	
	@XmlAttribute
	private int fila;
	
	@XmlAttribute
	private int columna;
	
	@XmlAttribute
	private int dimension;
	
	
	public CampoXML() {
		super();
	}

	
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
