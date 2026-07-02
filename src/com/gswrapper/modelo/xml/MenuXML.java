package com.gswrapper.modelo.xml;

import java.util.*;

import javax.xml.bind.annotation.*;

/**
 * 
 * MenuXML.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
@XmlRootElement(name = "menu")
@XmlAccessorType(XmlAccessType.FIELD)
public class MenuXML {
	
	@XmlElement(name="pantalla")
	private List<PantallaXML> pantallas;
	
	public MenuXML() {
		super();
	}

	public List<PantallaXML> getPantallas() {
		return pantallas;
	}

	public void setPantallas(List<PantallaXML> pantallas) {
		this.pantallas = pantallas;
	}
	
	
	@Override
	public String toString() {
		return pantallas.size() + "";
	}
}
