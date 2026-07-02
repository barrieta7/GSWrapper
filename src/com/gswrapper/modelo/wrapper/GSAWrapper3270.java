package com.gswrapper.modelo.wrapper;

import com.gswrapper.excepciones.IllegalActionExcepcion;
import com.gswrapper.excepciones.IllegalScreenException;
import com.gswrapper.modelo.vista.Pantalla;

/**
 * 
 * GSAWrapper3270.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public interface GSAWrapper3270 extends Wrapper3270 {

	public void cargarPantallasXML(String pathXML) throws ClassNotFoundException;
	public void abrirPantalla(String nombrePantalla) throws IllegalScreenException;
	public void abrirPantalla(String nombrePantalla, boolean abrirPantallasAnteriores) throws IllegalScreenException;
	public void abrirPantalla(Pantalla pantalla);
	public void cerrarPantalla(String nombrePantalla) throws IllegalScreenException;
	public void cerrarPantalla(Pantalla pantalla);
	public Object ejecutarAccion(String nombreAccion, Object parametros) throws IllegalScreenException, IllegalActionExcepcion;
	public Object ejecutarAccion(String nombreAccion, Object parametros, String pantallaEsperada) throws IllegalScreenException, IllegalActionExcepcion;
}
