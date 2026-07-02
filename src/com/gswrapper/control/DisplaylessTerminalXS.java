package com.gswrapper.control;

/**
 * DisplaylessTerminalXS.java
 * 
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 20/06/2018
 */

import java.io.*;
import java.nio.charset.*;

import org.apache.log4j.*;

import com.gswrapper.excepciones.*;
import com.gswrapper.modelo.wrapper.*;
import com.gswrapper.util.*;

public class DisplaylessTerminalXS implements DisplaylessTerminal {
	
	private static final Logger LOGGER = Logger.getLogger(DisplaylessTerminalXS.class);

	private Process process;
	private BufferedReader lectura;
	private PrintWriter escritura;

	private Conexion conexion;
	
	private String pathTerminal;
	
	private static final int TIME_OUT = 20000;
	private static final int TIEMPO_ESPERA = 50;
	
	private static final String ERROR_TIMEOUT = "Timeout supertado";
	private static final String ERROR_REINICIAR = "Error al reiniciar";
	private static final String ERROR_CERRAR = "Error al cerrar el canal de lectura";

	@Override
	public void abrir(String path) throws IOException {

		abrir(path, null);
	}
	
	@Override
	public void abrir(String path, Charset codificacion) throws IOException {
		
		if(codificacion == null) codificacion = Charset.forName(ConstantesXSTerminal.DEFAULT_CHARSET);
		
		pathTerminal = path + String.format(ConstantesXSTerminal.ARGUMENTO_CHARSET, codificacion.name());
		
		process = Runtime.getRuntime().exec(path);
		lectura = new BufferedReader(new InputStreamReader(process.getInputStream(), codificacion));
		escritura = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));
		if(conexion == null) conexion = new Conexion();
	}
	

	@Override
	public void cerrar() {
		
		if (process != null) {
			
			try {
				
				lectura.close();
				
			} catch (IOException e) {
				
				LogUtil.error(LOGGER, ERROR_CERRAR, e);
			}
			
			escritura.close();
			process.destroy();
		}
		
		conexion.desconectar();
	}
	
	@Override
	public void reiniciar() {

		try {
			
			cerrar();
			
			abrir(pathTerminal);
			
		} catch (IOException e) {
		
			LogUtil.error(LOGGER, ERROR_REINICIAR, e);
		}
	}

	@Override
	public String leer() throws TerminalTimeOutException {

		StringBuilder respuesta = new StringBuilder();

		if (lectura != null) {
			
			try {
				
				esperarRespuesta();

				String linea;
				
				while (lectura.ready() && (linea = lectura.readLine()) != null) {
					
					respuesta.append(linea + ConstantesXSTerminal.SALTO_DE_LINEA);
				}

			} catch (IOException e) {
				
				return "";
			}
		}

		return respuesta.toString();
	}

	@Override
	public void escribir(Comando comando, Object [] argumentos) {
		
		if (escritura != null) {
			
			escritura.println(comando.ejecutar(argumentos));
			
			escritura.flush();
		}
	}
	
	@Override
	public Conexion getConexion() {
		
		return conexion;
	}

	@Override
	public void setConexion(Conexion conexion) {
		
		this.conexion = conexion;
	}
	
	private void esperarRespuesta() throws IOException, TerminalTimeOutException {

		int intentos = 0;

		while (!lectura.ready()) {

			try {
				
				Thread.sleep(TIEMPO_ESPERA);
				
			} catch (InterruptedException e) {
				
				Thread.currentThread().interrupt();
				
				throw new TerminalTimeOutException(ERROR_TIMEOUT);
			}

			intentos++;

			if (intentos * TIEMPO_ESPERA > TIME_OUT) {
				
				throw new TerminalTimeOutException(ERROR_TIMEOUT);
			}
				
		}
	}
}
