package com.gswrapper.modelo.xml;

import javax.xml.bind.annotation.*;
/**
 * 
 * AccionXML.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */

@XmlRootElement(name = "accion")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccionXML {
	
	@XmlAttribute
	private String nombre;
	
	@XmlAttribute
	private String clase;
	
	public AccionXML() {
		super();
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getClase() {
		return clase;
	}
	
	public void setClase(String clase) {
		this.clase = clase;
	}
}
