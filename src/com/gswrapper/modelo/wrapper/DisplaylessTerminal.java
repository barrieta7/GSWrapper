package com.gswrapper.modelo.wrapper;
/**
 * DisplaylessTerminal.java
 * 
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 20/06/2018
 */

import java.io.*;
import java.nio.charset.*;

import com.gswrapper.excepciones.*;

/**
 * 
 * DisplaylessTerminal.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public interface DisplaylessTerminal {
	
	public void abrir(String path) throws IOException;
	public void abrir(String path, Charset codificacion) throws IOException;
	public void cerrar();
	public void reiniciar();
	public String leer() throws TerminalTimeOutException;
	public void escribir(Comando comando, Object [] argumentos);
	public Conexion getConexion();
	public void setConexion(Conexion conexion);
}
