package com.gswrapper.modelo.vista;

import java.util.*;

/**
 * 
 * Pantalla.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public class Pantalla {

	private String nombre, pantallaSalida;
	
	private List<String> pantallasAcceso;
	
	private Id identificador;

	private Map<String, Campo> campos;

	private List<ComandoPantalla> comandosAcceso, comandosSalida;
	
	
	public Pantalla() {
		
		campos = new HashMap<>();
		pantallasAcceso = new ArrayList<>();
		comandosAcceso = new ArrayList<>();
		comandosSalida = new ArrayList<>();
	}
	
	public void addCampo(String nombre, Campo campo) {
		
		if(!campos.containsKey(nombre)) campos.put(nombre, campo);
	}
	
	public void addComandoAcceso(ComandoPantalla comando) {
		comandosAcceso.add(comando);
	}
	
	public void addComandoSalida(ComandoPantalla comando) {
		comandosSalida.add(comando);
	}
	
	public Campo getCampo(String nombre) {
		return campos.get(nombre);
	}
	
	public List<ComandoPantalla> getComandosAcceso() {
		return comandosAcceso;
	}

	public List<ComandoPantalla> getComandosSalida() {
		return comandosSalida;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<String> getPantallasAcceso() {
		return pantallasAcceso;
	}

	public void setPantallasAcceso(List<String> pantallasAcceso) {
		this.pantallasAcceso = pantallasAcceso;
	}

	public String getPantallaSalida() {
		return pantallaSalida;
	}

	public void setPantallaSalida(String pantallaSalida) {
		this.pantallaSalida = pantallaSalida;
	}

	public Id getIdentificador() {
		return identificador;
	}

	public void setIdentificador(Id identificador) {
		this.identificador = identificador;
	}

	@Override
	public boolean equals(Object obj) {
        
		if (this == obj) return true;
        
        if (!(obj instanceof Pantalla)) return false;
        
        Pantalla tmp = (Pantalla) obj;
        
        return (nombre == tmp.nombre) && (identificador.getClave() == tmp.getIdentificador().getClave());
    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identificador == null) ? 0 : identificador.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}
}
