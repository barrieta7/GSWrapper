package com.gswrapper.modelo.acciones;

import java.util.*;

import com.gswrapper.modelo.vista.*;

/**
 * 
 * Accion.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public interface Accion<E,T> {
	
	public E ejecutar(T parametros);
	public Stack<Pantalla> getPantallasAbiertas();
}
