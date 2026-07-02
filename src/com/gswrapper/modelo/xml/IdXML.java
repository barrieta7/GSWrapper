package com.gswrapper.modelo.xml;

import javax.xml.bind.annotation.*;
/**
 * 
 * IdXML.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */

@XmlRootElement(name = "id")
@XmlAccessorType(XmlAccessType.FIELD)
public class IdXML {
	
	@XmlAttribute
	private String clave;
	
	@XmlAttribute
	private int fila; 

	@XmlAttribute
	private int columna;
	
	@XmlAttribute
	private int dimension;
	
	public IdXML() {
		super();
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
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
