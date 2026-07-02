package com.gswrapper.modelo.acciones;

import java.lang.reflect.*;

import com.gswrapper.control.*;
import com.gswrapper.util.*;

/**
 * 
 * MetaAccion.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public class MetaAccion {

	private String nombre;
	private Class<? extends AccionBase<?, ?>> clase;
	private String pantalla;

	@SuppressWarnings("unchecked")
	public MetaAccion(String nombre, String clase, String pantalla) throws ClassNotFoundException {
		
		this.nombre = nombre;
		this.pantalla = pantalla;
		this.clase = (Class<? extends AccionBase<?, ?>>) Class.forName(clase);
	}
	
	public Class<?> getClaseEntrada(){
		return getParametrosClase(1);
	}
	
	public Class<?> getClaseSalida(){
		return getParametrosClase(0);
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Class<? extends AccionBase<?, ?>> getClase() {
		return clase;
	}

	public void setClase(Class<? extends AccionBase<?, ?>> clase) {
		this.clase = clase;
	}

	public String getPantalla() {
		return pantalla;
	}

	public void setPantalla(String pantalla) {
		this.pantalla = pantalla;
	}
	
	
	private Class<?> getParametrosClase(int numeroArgumento) {
		
		ParameterizedType parametrosClase = (ParameterizedType) getAccionBase(clase).getGenericSuperclass();

		String nombreClaseParametro = parametrosClase.getActualTypeArguments()[numeroArgumento].getTypeName();
		
		return ClassUtil.classForName(nombreClaseParametro);
	}
	

	private Class<?> getAccionBase(Class<?> clase){
		
		Class<?> accionBase = clase;
		
		if(!clase.getSuperclass().equals(AccionBase.class)) {
			
			accionBase = getAccionBase(clase.getSuperclass());
		}
		
		
		return accionBase;
	}
}
