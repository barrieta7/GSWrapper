package com.gswrapper.control;

import java.lang.reflect.*;
import java.util.*;

import org.apache.log4j.*;

import com.gswrapper.excepciones.*;
import com.gswrapper.modelo.acciones.*;
import com.gswrapper.modelo.anotaciones.*;
import com.gswrapper.modelo.dao.*;
import com.gswrapper.modelo.vista.*;
import com.gswrapper.modelo.wrapper.*;
import com.gswrapper.util.*;

/**
 * 
 * AccionBase.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.5v
 * @date 2019-07-02
 *
 */
public abstract class AccionBase<E,T> implements Accion<E,T>{
	
	private Stack<Pantalla> pantallasAbiertas = new Stack<>();
	private Pantalla pantallaPrincipal;
	private GSAWrapper3270 wrapper;
	
	private Dao<Pantalla> pantallaDao = PantallaDao.getInstance();
	
	private static final String CAMPO_NO_ENCONTRADO = "No se ha encontrado el campo con nombre %s";
	private static final String PANTALLA_NO_ENCONTRADA = "No se ha encontrado la pantalla con nombre %s";
	private static final String PANTALLA_NO_APILABLE = "No se ha podido apilar la pantalla con nombre %s";
	private static final String PANTALLA_NO_IDENTIFICADA = "No se ha podido identificar la pantalla actual";
	private static final String PANTALLA_NO_ABIERTA = "No se ha podido abrir la pantalla %s";
	
	private static final Logger LOGGER = Logger.getLogger(WrapperX3270.class);
	
	public AccionBase(GSAWrapper3270 wrapper, Pantalla pantalla) {
		
		this.wrapper = wrapper;
		pantallaPrincipal = pantalla;
		pantallasAbiertas.push(pantalla);
	}
	
	/**
	 * 
	 * Lee el identificador con los parámetros de la pantalla actual
	 * 
	 * @return lee el identificador de la UI en la que se encuentre
	 */
	protected String getIdPantallaActual() {
		
		return AccionUtil.getInstance().leerIdPantalla(wrapper, getPantallaActual());
	}
	
	/**
	 * 
	 * 
	 * @return Devuelve el nombre de la pantalla actual
	 */
	protected String getNombrePantallaActual() {
		
		return getPantallaActual().getNombre();
	}
	
	/**
	 * 
	 * Abre la pantalla si existe y es posible
	 * 
	 * @param nombrePantalla
	 * @return true si ha conseguido abrir la pantalla
	 */
	protected boolean abrir(String nombrePantalla) {
		
		Pantalla nuevaPantalla = pantallaDao.get(nombrePantalla);
		
		boolean abierta = false;
		
		if(AccionUtil.getInstance().puedeAbrirse(getPantallaActual(), nuevaPantalla)) {
			
			wrapper.abrirPantalla(nuevaPantalla);
			
			abierta = actualizarPantallasAbiertas(nuevaPantalla);
		}
		
		LogUtil.info(LOGGER, String.format(PANTALLA_NO_ABIERTA, nombrePantalla), !abierta);
		
		return abierta;
	}
	
	/**
	 * 
	 * Vuelve a la pantalla anterior
	 * 
	 * @return true si ha conseguido llegar
	 */
	protected boolean cerrar() {
		
		return cerrar(getPantallaActual().getPantallaSalida());
	}
	
	/**
	 * 
	 * Cierra la pantalla actual e intenta llegar hasta la indicada
	 * 
	 * @param nombrePantallaEsperada
	 * @return Devuelve true si ha conseguido llegar a la pantalla indicada
	 */
	protected boolean cerrar(String nombrePantallaEsperada) {
		
		Pantalla pantallaEsperada = pantallaDao.get(nombrePantallaEsperada);
		
		cerrarPantallas(pantallaEsperada);

		return actualizarPantallasAbiertas(pantallaEsperada);
	}
	
	/**
	 * 
	 * Pulsa enter y se sitúa automáticamente en una de las pantallas anteriores
	 * 
	 * @return Devuelve true si consigue llegar a una de las pantallas anteriores
	 */
	protected boolean confirmar() {
		
		wrapper.enter();
				
		boolean pantallaEncontrada = identificarPantallaActual();
		
		LogUtil.warn(LOGGER, PANTALLA_NO_IDENTIFICADA, !pantallaEncontrada);
		
		return pantallaEncontrada;
	}
	
	/**
	 * 
	 * pulsa enter y se sitúa automáticamente en la pantalla esperada
	 * 
	 * @param nombrePantallaEsperada
	 * @return  Devuelve true si consigue llegar a la pantalla esperada
	 */
	protected boolean confirmar(String nombrePantallaEsperada) {
		
		wrapper.enter();
		
		Pantalla pantallaEsperada = pantallaDao.get(nombrePantallaEsperada);
		
		return actualizarPantallasAbiertas(pantallaEsperada);
	}
	
	/**
	 * 
	 * Devuelve el contenido del campo declarado con @GSCampo
	 * 
	 * @param nombreCampo
	 * @return contenido del campo
	 */
	protected String getCampo(String nombreCampo) {
				
		return getCampo(String.class, nombreCampo, null);
	}
	
	/**
	 * 
	 * Devuelve el contenido del campo declarado con @GSCampo 
	 * 
	 * @param nombreCampo
	 * @return contenido del campo
	 */
	protected <C> C getCampo(Class <C> claseCampo, String nombreCampo, C valorPorDefecto) {
		
		C valor = valorPorDefecto;
		
		Campo campo = getPantallaActual().getCampo(nombreCampo);
		
		if(!Condition.empty(campo)) {
				
			String valorString = wrapper.leerValor(campo.getFila(), campo.getColumna(), campo.getDimension());
				
			valor = ClassUtil.parseString(claseCampo, valorString, valorPorDefecto);
		}
		
		LogUtil.info(LOGGER, String.format(CAMPO_NO_ENCONTRADO, nombreCampo), Condition.empty(campo));
		
		return valor;
	}
	
	/**
	 * 
	 * Setea el valor de un campo
	 * 
	 * @param nombreCampo
	 * @param valor
	 */
	protected void setCampo(String nombreCampo, Object valor) {
		
		Campo campo = getPantallaActual().getCampo(nombreCampo);
		
		if(!Condition.empty(campo)) {
			
			wrapper.escribir(
					String.valueOf(valor), campo.getFila(), campo.getColumna(), campo.getDimension());
		}
		
		LogUtil.info(LOGGER, String.format(CAMPO_NO_ENCONTRADO, nombreCampo), Condition.empty(campo));
	}
	

	/**
	 * 
	 *  Devuelve un objeto rellenado con los campos de la clase, no el uso de primitivos
	 * 
	 * @param <O>
	 * @param O
	 * @return Objeto rellenado
	 */
	protected <O> O getObject(Class <O> O) {
		
		O instancia = ClassUtil.newInstance(O);
			
		GSACampo anotacion;
		
		for (Field field : ClassUtil.getFields(O)) {
			
			anotacion = ClassUtil.getFieldAnotation(GSACampo.class, field);
			
			if(anotacion != null) {
				
				Object valor = getCampo(field.getType(), anotacion.value(), null);
				
				Method metodoSet = ClassUtil.getSetMethod(O, field);
				
				ClassUtil.invoke(instancia, metodoSet, valor);
			}
		}
			
		return instancia;
	}

	
	/**
	 * 
	 * Setea el objeto en la UI
	 * 
	 * @param instancia
	 */
	protected void setObject(Object instancia) {
		
		GSACampo anotacion;
		
		for (Field field : ClassUtil.getFields(instancia.getClass())) {
			
			anotacion = ClassUtil.getFieldAnotation(GSACampo.class, field);
			
			if(anotacion != null) {
				
				Method metodoGet = ClassUtil.getGetMethod(instancia.getClass(), field);
				
				Object valor = ClassUtil.invoke(instancia, metodoGet);
				
				setCampo(anotacion.value(), valor);
			}
		}
	}
	
	
	/**
	 * 
	 * actualiza la pila adelante si puede o atrás, solo actualiza la pila, no abre nada
	 * 
	 * @param nombrePantalla
	 * @return
	 */
	protected boolean actualizarPantalla(String nombrePantalla) {
		
		return actualizarPantallasAbiertas(pantallaDao.get(nombrePantalla));
	}
	

	/**
	 * 
	 * devuelve true o false si estamos en la pantalla que le hemos pasado, si es true puede estar adelante o atrás, hay que actualizar la pila
	 * 
	 * @param nombrePantalla
	 * @return devuelve true o false si estamos en la pantalla que le hemos pasado
	 */
	protected boolean esPantalaActual(String nombrePantalla) {
		
		Pantalla pantallaEsperada = pantallaDao.get(nombrePantalla);
		
		boolean esPantalla = AccionUtil.getInstance().esPantallaEsperada(wrapper, pantallaEsperada);
		
		LogUtil.warn(LOGGER, String.format(PANTALLA_NO_ENCONTRADA, nombrePantalla), !Condition.empty(pantallaEsperada));
		
		return esPantalla;
	}
	
	
	/**
	 * 
	 * 
	 * 
	 * @return true si el id de la pantalla actual no es el que hay en este momento
	 */
	protected boolean esOtraPantala() {
		
		return !AccionUtil.getInstance().esPantallaEsperada(wrapper, getPantallaActual());
	}
	
	/**
	 * 
	 * Ejecuta una acción declarada en el fichero xml
	 * 
	 * @param accion
	 * @param parametros
	 * @return
	 * @throws IllegalScreenException
	 * @throws IllegalActionExcepcion
	 */
	protected Object ejecutarAccion(String accion, Object parametros) throws IllegalScreenException, IllegalActionExcepcion {
		
		return ejecutarAccion(accion, parametros, getPantallaActual().getNombre());
	}
	
	/**
	 * 
	 * Ejecuta una acción declarada en el fichero xml
	 * 
	 * @param accion
	 * @param parametros
	 * @param pantallaEsperada
	 * @return
	 * @throws IllegalScreenException
	 * @throws IllegalActionExcepcion
	 */
	protected Object ejecutarAccion(String accion, Object parametros, String pantallaEsperada) throws IllegalScreenException, IllegalActionExcepcion {
		
		return wrapper.ejecutarAccion(accion, parametros, pantallaEsperada);
	}
	
	
	/**
	 * 
	 * Borra todos los campos de la pantalla
	 *
	 */
	protected void limpiarPantalla() {
		
		wrapper.limpiarPantalla();
	}
	
	protected Wrapper3270 getWrapper() {
		
		return wrapper;
	}

	private Pantalla getPantallaActual() {
		
		return pantallasAbiertas.lastElement();
	}
	
	private boolean actualizarPantallasAbiertas(Pantalla pantalla) {
		
		boolean pantallasActualizadas = false;
		
		if(AccionUtil.getInstance().esPantallaEsperada(wrapper, pantalla)) {
			
			if(!pantallasAbiertas.contains(pantalla)) {
				
				pantallasActualizadas = apilarPantalla(pantalla);
				
			} else {
				
				pantallasActualizadas = true;
				
				desapilarPantallasPosteriores(pantalla);
			}
		}
		
		LogUtil.info(LOGGER, pantallasAbiertas.toString());
		
		return pantallasActualizadas;
	}
	
	
	private boolean apilarPantalla(Pantalla pantalla) {
		
		Pantalla ultimaReferenciaPila = getUltimaReferenciaPila(pantalla);
		
		boolean sePuedeApilar = !Condition.empty(ultimaReferenciaPila);
		
		if(sePuedeApilar) {
			
			desapilarPantallasPosteriores(ultimaReferenciaPila);
			
			apilarPantallasAnteriores(pantalla.getPantallaSalida());
			
			pantallasAbiertas.push(pantalla);
		}
		
		LogUtil.warn(LOGGER, String.format(PANTALLA_NO_APILABLE, pantalla.getNombre()), !sePuedeApilar);
		
		return sePuedeApilar;
	}
	
	
	private Pantalla getUltimaReferenciaPila(Pantalla pantalla) {
		
		Pantalla ultimaReferencia = null;
		
		Pantalla pantallaSalida = pantallaDao.get(pantalla.getPantallaSalida());
		
		if(!Condition.empty(pantallaSalida)) {
			
			boolean sePuedeApilar = pantallasAbiertas.contains(pantallaSalida);
			
			ultimaReferencia = sePuedeApilar ? pantallaSalida : getUltimaReferenciaPila(pantallaSalida);
		}
		
		return ultimaReferencia;
	}
	
	private void apilarPantallasAnteriores(String nombrePantallaSalida) {
		
		Pantalla pantallaSalida = pantallaDao.get(nombrePantallaSalida);
		
		int pantallasIntroducidas = 0;
		
		Pantalla pantallaActual = getPantallaActual();
		
		while(!pantallaActual.equals(pantallaSalida)) {
			
			pantallasAbiertas.add(pantallasAbiertas.size() - pantallasIntroducidas, pantallaSalida);
			
			pantallasIntroducidas++;
			
			pantallaSalida = pantallaDao.get(pantallaSalida.getPantallaSalida());
		}
	}
	
	
	private void desapilarPantallasPosteriores(Pantalla pantalla) {
		
		while(!getPantallaActual().equals(pantalla)) {
			
			pantallasAbiertas.pop();
		}
	}
	
	
	private boolean sePuedeCerrar(Pantalla pantallaEsperada) {
		
		return  !getPantallaActual().equals(pantallaPrincipal) &&
				!Condition.empty(pantallaEsperada) && 
				pantallasAbiertas.contains(pantallaEsperada);
	}
	
	
	private void cerrarPantallas(Pantalla pantallaEsperada) {
		
		if(sePuedeCerrar(pantallaEsperada)) {
			
			while(!getPantallaActual().equals(pantallaEsperada)) {
				
				wrapper.cerrarPantalla(pantallasAbiertas.pop());
			}
		}	
	}
	
	
	private boolean identificarPantallaActual() {
		
		for (Pantalla pantalla : pantallasAbiertas) {	
			
			if(actualizarPantallasAbiertas(pantalla)) return true;
		}
		
		return false;
	}
	
	@Override
	public Stack<Pantalla> getPantallasAbiertas() {
		
		return pantallasAbiertas;
	}
	
}
