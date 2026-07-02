package com.gswrapper.modelo.xml;

import java.util.*;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.*;

/**
 * 
 * PantallaXML.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
@XmlRootElement(name = "pantalla")
@XmlAccessorType(XmlAccessType.FIELD)
public class PantallaXML {
	
	@XmlAttribute
	private String nombre;
	
	@XmlJavaTypeAdapter(ListaSeperadaPorComasAdapter.class)
	@XmlAttribute
	private List<String> pantallasAcceso;
	
	@XmlAttribute
	private String pantallaSalida;
	
	@XmlJavaTypeAdapter(ListaSeperadaPorComasAdapter.class)
	@XmlAttribute
	private List<String> acceder;
	
	@XmlJavaTypeAdapter(ListaSeperadaPorComasAdapter.class)
	@XmlAttribute
	private List<String> salir;
	
	@XmlElement(name="identificador")
	private IdXML identificador;
	
	@XmlElementWrapper(name="campos")
    @XmlElement(name="campo")
	private List<CampoXML> campos;
	
	@XmlElementWrapper(name="acciones")
    @XmlElement(name="accion")
	private List<AccionXML> acciones;
	
	public PantallaXML() {
		super();
		acceder = new ArrayList<>();
		salir = new ArrayList<>();
		campos = new ArrayList<>();
		acciones = new ArrayList<>();
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public List<String> getAcceder() {
		return acceder;
	}

	public void setAcceder(List<String> acceder) {
		this.acceder = acceder;
	}

	public List<String> getSalir() {
		return salir;
	}

	public void setSalir(List<String> salir) {
		this.salir = salir;
	}

	public List<CampoXML> getCampos() {
		return campos;
	}

	public void setCampos(List<CampoXML> campos) {
		this.campos = campos;
	}

	public List<AccionXML> getAcciones() {
		return acciones;
	}

	public void setAcciones(List<AccionXML> acciones) {
		this.acciones = acciones;
	}

	public IdXML getIdentificador() {
		return identificador;
	}

	public void setIdentificador(IdXML identificador) {
		this.identificador = identificador;
	}

	public String getPantallaSalida() {
		return pantallaSalida;
	}

	public void setPantallaSalida(String pantallaSalida) {
		this.pantallaSalida = pantallaSalida;
	}

	public List<String> getPantallasAcceso() {
		return pantallasAcceso;
	}

	public void setPantallasAcceso(List<String> pantallasAcceso) {
		this.pantallasAcceso = pantallasAcceso;
	}
}
