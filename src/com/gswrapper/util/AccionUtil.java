package com.gswrapper.util;

import org.apache.log4j.*;

import com.gswrapper.modelo.dao.*;
import com.gswrapper.modelo.vista.*;
import com.gswrapper.modelo.wrapper.*;

/**
 * 
 * AccionUtil.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public class AccionUtil {
	
	private static final Logger LOGGER = Logger.getLogger(AccionUtil.class);
	
	private static final int MAX_INTENTOS = 2;
	
	private static AccionUtil instance;
	
	public static synchronized AccionUtil getInstance() {
		
		if(instance == null) instance = new AccionUtil();
		
		return instance;
	}
	
	public boolean esPantallaEsperada(GSAWrapper3270 wrapper, String nombrePantalla) {
		
		return esPantallaEsperada(wrapper, PantallaDao.getInstance().get(nombrePantalla));
	}
	
	public boolean esPantallaEsperada(GSAWrapper3270 wrapper, Pantalla pantallaEsperada) {
		
		return esPantallaEsperada(wrapper, pantallaEsperada, MAX_INTENTOS);
	}
	
	private static final String MENSAJE_PANTALLA_NO_ESPERADA = 
			"Pantalla no esperada los identificadores %s y %s no coinciden";
	
	public boolean esPantallaEsperada(GSAWrapper3270 wrapper, Pantalla pantallaEsperada, int intentos) {
		
		boolean esPantallaEsperada = false;
		
		if(!Condition.empty(pantallaEsperada)) {
		
			String idPantalla = pantallaEsperada.getIdentificador().getClave();
			
			wrapper.esperarTexto(idPantalla, 1);
			
			String idLeido = leerIdPantalla(wrapper, pantallaEsperada);
			
			esPantallaEsperada = idPantalla.equalsIgnoreCase(idLeido);
			
			intentos = intentos - 1;
			
			if(!esPantallaEsperada && intentos > 0) {
				
				esPantallaEsperada = esPantallaEsperada(wrapper, pantallaEsperada, intentos);	
			}
			
			LogUtil.warn(LOGGER, String.format(MENSAJE_PANTALLA_NO_ESPERADA, idPantalla, idLeido), !esPantallaEsperada);
		}
		
		return esPantallaEsperada;
	}
	
	public String leerIdPantalla(GSAWrapper3270 wrapper, Pantalla pantalla) {	
		
		Id identificador = pantalla.getIdentificador();
		
		return wrapper.leerValor(identificador.getFila(), identificador.getColumna(), identificador.getDimension()).trim();
	}
	
	public boolean puedeAbrirse(Pantalla pantallaActual, Pantalla nuevaPantalla) {
		
		boolean puedeAbrirse = false;
		
		if(!Condition.empty(nuevaPantalla) && !pantallaActual.equals(nuevaPantalla)) {
			
			puedeAbrirse = nuevaPantalla.getPantallasAcceso().contains(pantallaActual.getNombre());
		}
		
		return puedeAbrirse;		
	}
	
	public String getPantallaAcceso(Pantalla pantalla) {
		
		String pantallaAcceso = null;
		
		if(pantalla.getPantallasAcceso() != null && pantalla.getPantallasAcceso().size() == 1) {
			
			pantallaAcceso = pantalla.getPantallasAcceso().get(0);
		}
		
		return pantallaAcceso;
	}

}
