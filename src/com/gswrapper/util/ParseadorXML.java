package com.gswrapper.util;

import java.util.*;
import java.util.regex.*;

import org.apache.log4j.*;

import com.gswrapper.modelo.acciones.*;
import com.gswrapper.modelo.vista.*;
import com.gswrapper.modelo.wrapper.*;
import com.gswrapper.modelo.xml.*;
/**
 * 
 * ParseadorXML.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */

public class ParseadorXML {
	
	private static final String MANDATO_PATTERN = "^[f|F][0-9]{1,2}";
	private static final String SEPARADOR_COMANDO = ":";
	private static final String SEPARADOR_ARGUMENTOS = ";";

	private static ParseadorXML instance;
	
	private static final Logger LOGGER = Logger.getLogger(ParseadorXML.class);
	
	public static synchronized ParseadorXML getInstance() {
		
		if(instance == null) instance = new ParseadorXML();
		
		return instance;
	}
	
	public Pantalla pasearPantallaXML(PantallaXML pantallaXML) {
		
		Pantalla pantalla = crearPantalla(pantallaXML);
		
		cargarCampos(pantalla, pantallaXML.getCampos());
		cargarComandos(pantalla.getComandosAcceso(), pantallaXML.getAcceder());
		cargarComandos(pantalla.getComandosSalida(), pantallaXML.getSalir());
		
		return pantalla;
	}
	
	
	public MetaAccion parsearAccion(AccionXML accionXML, String pantalla) throws ClassNotFoundException {
		
		return new MetaAccion(accionXML.getNombre(), accionXML.getClase(), pantalla);
	}
	
	
	private Pantalla crearPantalla (PantallaXML pantallaXML) {
		
		Pantalla pantalla = new Pantalla();
		
		pantalla.setIdentificador(getId(pantallaXML.getIdentificador()));
		pantalla.setNombre(pantallaXML.getNombre());
		pantalla.setPantallasAcceso(pantallaXML.getPantallasAcceso());
		pantalla.setPantallaSalida(pantallaXML.getPantallaSalida());
	
		return pantalla;
	}
	
	
	private Id getId(IdXML id) {
		
		return new Id(id.getClave(), id.getFila(), id.getColumna(), id.getDimension());
	}
	
	
	private void cargarCampos(Pantalla pantalla, List<CampoXML> campos) {

		for (CampoXML campoXML : campos) {
			
			pantalla.addCampo(campoXML.getNombre(), crearCampo(campoXML));
		}
	}
	
	
	private Campo crearCampo(CampoXML campoXML) {
		
		Campo campo = new Campo();
		
		campo.setNombre(campoXML.getNombre());
		campo.setFila(campoXML.getFila());
		campo.setColumna(campoXML.getColumna());
		campo.setDimension(campoXML.getDimension());
		
		return campo;
	}
	
	
	private void cargarComandos(List<ComandoPantalla> comandos, List<String> comandosXML) {
		
		for (String comandoXML : comandosXML) {

			Comando comando = getComando(leerComando(comandoXML.toUpperCase()));
			
			if(comando != null) {
				
				comandos.add(new ComandoPantalla(comando, leerArgumentos(comandoXML)));
			}
		}
	}
	
	
	private String leerComando(String comandoXML) {
		
		return comandoXML.split(SEPARADOR_COMANDO)[0];
	}
	
	
	private Object [] leerArgumentos(String comandoXML) {
		
		if(tieneArgumentos(comandoXML)) {
			
			return comandoXML.split(SEPARADOR_COMANDO)[1].split(SEPARADOR_ARGUMENTOS);
		}
		
		return new Object [] {};
	}
	
	
	private boolean tieneArgumentos(String comandoXML) {
		
		return comandoXML.split(SEPARADOR_COMANDO).length > 1;
	}
	
	
	private Comando getComando(String nombreComando) {
		
		try {
			
			if(esMandato(nombreComando)) {
				
				return  Mandato.valueOf(nombreComando);
				
			}else {
				
				return ComandoXS.valueOf(nombreComando);
			}
			
		}catch (IllegalArgumentException e) {
			
			LOGGER.error("Error al intentar buscar el comando " + nombreComando);
			
			return null;
		}
		
	}

	
	private boolean esMandato(String nombreComando) {
		
		Pattern p = Pattern.compile(MANDATO_PATTERN);
		Matcher m = p.matcher(nombreComando);
		
		return m.find();
	}
}
