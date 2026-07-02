package com.gswrapper.control;

import java.lang.reflect.*;

import org.apache.log4j.*;

import com.gswrapper.excepciones.IllegalActionExcepcion;
import com.gswrapper.excepciones.NoConnectionExcepcion;
import com.gswrapper.modelo.acciones.*;
import com.gswrapper.modelo.dao.*;
import com.gswrapper.modelo.vista.*;
import com.gswrapper.modelo.wrapper.*;
import com.gswrapper.util.*;

/**
 * 
 * AccionManager.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.3v
 * @date 2019-07-19
 *
 */

public class AccionManager {
	
	private static final Logger LOGGER = Logger.getLogger(AccionManager.class);
	
	private static final String METODO_ACCION = "ejecutar";
	private static final String MENSAJE_ACCION = "Ejecutando la accion %s";
	private static final String ERROR_ACCION = "Error al ejecutar la accion %s";
	
	private static AccionManager instance;
	
	private Dao<MetaAccion> accionDao = AccionDao.getInstance();
	
	public static synchronized AccionManager getInstance() {
		
		if(instance == null) instance = new AccionManager();
		
		return instance;
	}
	
	
	public boolean isAccionValida(String nombreAccion, Object parametros) {
		
		return existeAccion(nombreAccion) && parametrosValidos(accionDao.get(nombreAccion), parametros);
	}
	
	public Pantalla getPantallaAccion(String nombreAccion) {
		
		Pantalla pantalla = null;
		
		if(existeAccion(nombreAccion)) {
			
			Dao<Pantalla> pantallaDao = PantallaDao.getInstance();
			
			pantalla = pantallaDao.get(accionDao.get(nombreAccion).getPantalla());
		}
		
		return pantalla;
	}
	
	public ResultadoAccion ejecutarAccion(String nombreAccion, Object parametros, Object ... parametrosConstructor) throws IllegalActionExcepcion {		
		
		LogUtil.info(LOGGER, String.format(MENSAJE_ACCION, nombreAccion));
			
		MetaAccion metaAccion = accionDao.get(nombreAccion);
				
		Class<? extends AccionBase<?, ?>> claseAccion = metaAccion.getClase();

		Constructor<? extends AccionBase<?, ?>> constructor = getConstructor(claseAccion);
		
		AccionBase<?, ?> accion = newInstance(constructor, parametrosConstructor);
		
		Method metodoEjecutar = ClassUtil.getMethod(claseAccion, METODO_ACCION, metaAccion.getClaseEntrada());
		
		ResultadoAccion resultadoAccion = new ResultadoAccion();
		
		try {
			
			resultadoAccion.setResultado(metodoEjecutar.invoke(accion, parametros));
		
		} catch (Exception e) {
			
			if(e.getCause() != null && e.getCause().getClass().equals(NoConnectionExcepcion.class)) throw (NoConnectionExcepcion)e.getCause();
			
			LogUtil.error(LOGGER, String.format(ERROR_ACCION, nombreAccion), e);
			
			resultadoAccion.setResultado(null);
			
		} finally {
			
			resultadoAccion.setPantallasAbiertas(accion.getPantallasAbiertas());
		}
		
		return resultadoAccion;
	}
	
	private Constructor<? extends AccionBase<?, ?>> getConstructor(Class<? extends AccionBase<?, ?>> claseAccion) {
		
		return ClassUtil.getConstructor(claseAccion, GSAWrapper3270.class, Pantalla.class);
	}
	
	private AccionBase<?, ?> newInstance(Constructor<? extends AccionBase<?, ?>> constructor, Object ... parametros) throws IllegalActionExcepcion {
		
		AccionBase<?, ?> instancia = null;
		
		try {
			
			instancia = constructor.newInstance(parametros);
			
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
		
			LOGGER.error(String.format(ClassUtil.ERROR_INSTANCIA, constructor.getName()), e);
			
			throw new IllegalActionExcepcion(String.format(ClassUtil.ERROR_INSTANCIA, constructor.getName()));
		}
		
		return instancia;
	}
	
	private boolean existeAccion(String nombreAccion) {
		
		return accionDao.exists(nombreAccion);
	}
	
	private boolean parametrosValidos(MetaAccion accion, Object parametros) {
		
		boolean parametrosValidos = true;
		
		if(!Condition.empty(parametros)) {
			
			parametrosValidos = parametros.getClass().equals(accion.getClaseEntrada());
		}
		
		return parametrosValidos;		
	}
	
}
