package com.gswrapper.modelo.acciones;

import java.util.*;

import com.gswrapper.modelo.vista.*;

/**
 * 
 * ResultadoAccion.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public class ResultadoAccion {
	
	private Object resultado;
	private Stack<Pantalla> pantallasAbiertas = new Stack<>();
	
	
	public Object getResultado() {
		return resultado;
	}
	
	public void setResultado(Object resultado) {
		this.resultado = resultado;
	}
	
	public Stack<Pantalla> getPantallasAbiertas() {
		return pantallasAbiertas;
	}
	
	public void setPantallasAbiertas(Stack<Pantalla> pantallasAbiertas) {
		this.pantallasAbiertas = pantallasAbiertas;
	}
}
