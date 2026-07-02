package com.gswrapper.control;

import java.io.*;
import java.util.*;

import org.apache.log4j.*;

import com.gswrapper.excepciones.*;
import com.gswrapper.modelo.acciones.*;
import com.gswrapper.modelo.dao.*;
import com.gswrapper.modelo.vista.*;
import com.gswrapper.modelo.wrapper.*;
import com.gswrapper.modelo.xml.*;
import com.gswrapper.util.*;

/**
 * 
 * GSAWrapperX3270.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.4v
 * @date 2019-07-19
 *
 */
public class GSAWrapperX3270 extends WrapperX3270 implements GSAWrapper3270 {

	private static final Logger LOGGER = Logger.getLogger(GSAWrapperX3270.class);
	
	private static final int MAX_INTENTOS_REINICIAR = 10;
	
	private static final int MILISEGUNDOS_ESPERA = 6000;
	
	private static final String LOG_CARGAR_PANTALLAS = "Cargando pantallas de %s";
	private static final String LOG_ABRIR_PANTALLA = "Abriendo pantalla %s";
	private static final String LOG_CERRAR_PANTALLA = "Cerrando pantalla %s";
	private static final String LOG_EJECUTAR_ACCION = "Ejecutando accion %s";
	
	private static final String ERROR_PANTALLA = "Pantalla %s no encontrada";
	private static final String ERROR_ACCION = "La acción %s no existe o parámetros incorrectos";
	private static final String ERROR_CONEXION = "Intentando recuperar la conexión, intento %d";

	private Stack<String> accionesEnEjecucion = new Stack<>();
	
	private Dao<Pantalla> pantallaDao = PantallaDao.getInstance();

	
	public GSAWrapperX3270(String pathEmulador) throws IOException {
		
		super(pathEmulador);
	}

	@Override
	public void cargarPantallasXML(String pathXML) throws ClassNotFoundException {
		
		LogUtil.info(LOGGER, String.format(LOG_CARGAR_PANTALLAS, pathXML));
		
 		MenuXML pantallas = LectorXML.getInstance().leerXML(pathXML, MenuXML.class);
		
		if(pantallas != null) cargarPantallas(pantallas);
	}
	
	@Override
	public void abrirPantalla(String nombrePantalla) throws IllegalScreenException {
		
		abrirPantalla(nombrePantalla, false);
	}

	@Override
	public void abrirPantalla(String nombrePantalla, boolean abrirPantallasAnteriores) throws IllegalScreenException {
		
		LogUtil.info(LOGGER, String.format(LOG_ABRIR_PANTALLA, nombrePantalla));
		
		Pantalla pantalla = pantallaDao.get(nombrePantalla);
		
		if(!Condition.empty(pantalla)) {
			
			if(abrirPantallasAnteriores) {
				
				abrirPantalla(AccionUtil.getInstance().getPantallaAcceso(pantalla), true);
			}
			
			abrirPantalla(pantalla);
			
			validarPantalla(pantalla);
		}
	}
	
	@Override
	public void abrirPantalla(Pantalla pantalla) {
		
		if(!AccionUtil.getInstance().esPantallaEsperada(this, pantalla)) {
			
			ejecutarComandosPantalla(pantalla.getComandosAcceso());	
		}
	}
	
	@Override
	public void cerrarPantalla(String nombrePantalla) throws IllegalScreenException {
		
		LogUtil.info(LOGGER,String.format(LOG_CERRAR_PANTALLA, nombrePantalla));

		Pantalla pantalla = pantallaDao.get(nombrePantalla);
		
		if(!Condition.empty(pantalla)) ejecutarComandosPantalla(pantalla.getComandosSalida());
	}
	
	@Override
	public void cerrarPantalla(Pantalla pantalla) {
		
		if(!Condition.empty(pantalla)) ejecutarComandosPantalla(pantalla.getComandosSalida());
	}
	
	@Override
	public Object ejecutarAccion(String nombreAccion, Object parametros) throws IllegalScreenException, IllegalActionExcepcion {
		
		return ejecutarAccion(nombreAccion, parametros, null);
	}
	
	@Override
	public synchronized Object ejecutarAccion(String nombreAccion, Object parametros, String pantallaEsperada) throws IllegalScreenException, IllegalActionExcepcion {
		
		LogUtil.info(LOGGER,String.format(LOG_EJECUTAR_ACCION, nombreAccion));
	
		AccionManager accionManager = AccionManager.getInstance();
		
		validarAccion(nombreAccion, parametros);
		
		Pantalla pantallaAccion = accionManager.getPantallaAccion(nombreAccion);
		Pantalla pantallaFinal = getPantallaFinal(pantallaEsperada, pantallaAccion.getPantallaSalida());
		
		Object resultado = null;
		
		accionesEnEjecucion.push(nombreAccion);
		
		try {
			
			abrirPantalla(pantallaAccion);
			
			validarPantalla(pantallaAccion);
			
			ResultadoAccion resultadoAccion = getResultadoAccion(nombreAccion, parametros, pantallaAccion);
				
			cerrarPantallasAbiertas(resultadoAccion.getPantallasAbiertas());
			
			resultado = resultadoAccion.getResultado();
				
			validarPantalla(pantallaFinal);
			
		} catch (NoConnectionExcepcion e) {
				
			LogUtil.warn(LOGGER, e.getMessage());
			
			reiniciarAccion(e, pantallaAccion);
			
			resultado = ejecutarAccion(nombreAccion, parametros, pantallaEsperada);
			
		} finally {
			
			accionesEnEjecucion.pop();
		}
		
		return resultado;
	}
	
	private Pantalla getPantallaFinal(String pantallaEsperada, String pantallaSalida) {
		
		String pantallaFinal = Condition.evalNotEmpty(pantallaEsperada, pantallaSalida);
		
		return pantallaDao.get(pantallaFinal);
	}
	
	private void validarAccion(String nombreAccion, Object parametros) throws IllegalActionExcepcion{
		
		AccionManager accionManager = AccionManager.getInstance();
		
		if(!accionManager.isAccionValida(nombreAccion, parametros)) {
			
			throw new IllegalActionExcepcion(String.format(ERROR_ACCION, nombreAccion));
		}
	}
	
	private ResultadoAccion getResultadoAccion(String nombreAccion, Object parametros, Pantalla pantallaAccion) throws IllegalActionExcepcion {
		
		AccionManager accionManager = AccionManager.getInstance();
		
		ResultadoAccion resultadoAccion = accionManager.ejecutarAccion(nombreAccion, parametros, this, pantallaAccion);
		
		return resultadoAccion;
	}
	
	private void cerrarPantallasAbiertas(Stack<Pantalla> pantallasAbiertas) {
		
		while (pantallasAbiertas.size() > 0) {
		
			ejecutarComandosPantalla(pantallasAbiertas.pop().getComandosSalida());
		}
	}
	
	private void reiniciarAccion(NoConnectionExcepcion excepcion, Pantalla pantallaAccion) throws IllegalScreenException {
		
		recuperarAccion(excepcion);
		
		reconectar(MAX_INTENTOS_REINICIAR, excepcion);
		
		String pantallaAcceso = AccionUtil.getInstance().getPantallaAcceso(pantallaAccion);
		
		abrirPantalla(pantallaAcceso, true);
	}
	
	private synchronized void recuperarAccion(NoConnectionExcepcion excepcion) {
		
		if(accionesEnEjecucion.empty() || 
				!accionesEnEjecucion.firstElement().equals(accionesEnEjecucion.lastElement())) {
			
			throw excepcion;
		}
	}
	
	private void reconectar(int intentosRestantes, NoConnectionExcepcion excepcion)  {
		
		LogUtil.warn(LOGGER, String.format(ERROR_CONEXION, intentosRestantes));
		
		if(!reiniciar()) {
			
			try {
				
				Thread.sleep(MILISEGUNDOS_ESPERA);
				
				if(intentosRestantes <= 0) {
						
					throw excepcion;
				}
				
				reconectar(--intentosRestantes  , excepcion);
				
			} catch (InterruptedException e) {
				
				Thread.currentThread().interrupt();
			
				throw excepcion;
			}
		}
	}
	
	private void cargarPantallas(MenuXML pantallas) throws ClassNotFoundException {
		
		ParseadorXML parser = ParseadorXML.getInstance();
		
		limpiarCachePantallas();
		
		for (PantallaXML pantalla : pantallas.getPantallas()) {
			
			pantallaDao.add(pantalla.getNombre(), parser.pasearPantallaXML(pantalla));
			
			cargarAcciones(pantalla);
		}
	}
	
	private void cargarAcciones(PantallaXML pantalla) throws ClassNotFoundException {
		
		Dao<MetaAccion> accionDao = AccionDao.getInstance();
		
		ParseadorXML parser = ParseadorXML.getInstance();
		
		for (AccionXML accion : pantalla.getAcciones()) {
			
			accionDao.add(accion.getNombre(), parser.parsearAccion(accion, pantalla.getNombre()));
		}
	}
	
	private void limpiarCachePantallas() {
		
		pantallaDao.removeAll();
		
		AccionDao.getInstance().removeAll();
	}
	
	private void validarPantalla(Pantalla pantalla) throws IllegalScreenException {
			
		if(!AccionUtil.getInstance().esPantallaEsperada(this, pantalla)) {
			
			throw new IllegalScreenException(String.format(ERROR_PANTALLA, pantalla.getNombre()));
		}	
	}
	
	private void ejecutarComandosPantalla(List<ComandoPantalla> comandosPantalla) {
		
		for (ComandoPantalla comandoPantalla : comandosPantalla) {
			
			ejecutarComando(comandoPantalla.getComando(), comandoPantalla.getArgumentos());
		}
	}
}
