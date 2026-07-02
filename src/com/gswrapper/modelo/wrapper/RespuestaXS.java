package com.gswrapper.modelo.wrapper;
import org.apache.log4j.*;

import com.gswrapper.util.*;

/**
 * 
 * RespuestaXS.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public class RespuestaXS {
	
	private static final Logger LOGGER = Logger.getLogger(RespuestaXS.class);
	
	private static final int INDICE_CONEXION = 3;
	
	private String pantallaTerminal = "";
	private String[] flagsEstado;
	private boolean errorEjecucion;
	
	public RespuestaXS() {
		
		errorEjecucion = true;
	}
	
	public RespuestaXS(String respuesta) {
		
		parsearPantalla(respuesta);
		parsearFlags(respuesta);
		parsearErrores(respuesta);
		
		mostrarInformacionRespuesta();
	}
	
	private void parsearPantalla(String respuesta) {
		
		if(respuesta.contains(ConstantesXSTerminal.CABECERA_PANTALLA)) {
			
			String flags = respuesta.replaceAll(ConstantesXSTerminal.CABECERA_PANTALLA + ".*"+ ConstantesXSTerminal.SALTO_DE_LINEA , "");
			
			pantallaTerminal = respuesta.substring(0, respuesta.indexOf(flags))
					.replaceAll(ConstantesXSTerminal.CABECERA_PANTALLA, "").trim();
		}
	}
	
	private void parsearFlags(String respuesta) {
	
		String flags = respuesta.replaceAll(ConstantesXSTerminal.CABECERA_PANTALLA + ".*"+ ConstantesXSTerminal.SALTO_DE_LINEA , "")
				.replaceAll(ConstantesXSTerminal.SALTO_DE_LINEA + ".*[a-z]", "").trim();
		
		flagsEstado = flags.split(ConstantesXSTerminal.SEPARADOR_FLAGS);
	}
	
	private void parsearErrores(String respuesta) {
		
		String errores = respuesta.replaceAll(ConstantesXSTerminal.CABECERA_PANTALLA + ".*"+ ConstantesXSTerminal.SALTO_DE_LINEA , "")
				.replaceAll("^[a-z|A-Z].*"+ ConstantesXSTerminal.SALTO_DE_LINEA, "").trim();

		errorEjecucion = !errores.equals(ConstantesXSTerminal.OK);
	}
	
	public String getPantallaTerminal() {
		
		return pantallaTerminal;
	}

	public String[] getFlagsEstado() {
		
		return flagsEstado;
	}
	
	public boolean errorEjecucion() {
		
		return errorEjecucion;
	}
	
	public boolean estaConectado() {
		
		boolean conectado = false;
		
		if(flagsEstado != null && flagsEstado.length >= INDICE_CONEXION) {
			
			conectado = flagsEstado[INDICE_CONEXION].contains(ConstantesXSTerminal.TAG_CONECTADO);
		}
		
		return conectado;
	}
	
	private void mostrarInformacionRespuesta() {
		
		LogUtil.info(LOGGER, pantallaTerminal);
	}
	
}
