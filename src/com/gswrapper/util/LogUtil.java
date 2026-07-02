package com.gswrapper.util;

import org.apache.log4j.*;
/**
 * 
 * LogUtil.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */

public class LogUtil {
	
	private static boolean debug = true;
	
	public static void setDebugMode(boolean debugMode) {
		
		debug = debugMode;
	}
	
	public static void info(Logger log, String mensaje) {
		
		info(log, mensaje, true);
	}
	
	public static void info(Logger log, String mensaje, boolean mostrarMensaje) {
		
		if(mostrarLog(mostrarMensaje)) log.info(mensaje);
	}
	
	public static void warn(Logger log, String mensaje) {
	
		warn(log, mensaje, true);
	}
	
	public static void warn(Logger log, String mensaje, boolean mostrarMensaje) {
		
		if(mostrarLog(mostrarMensaje)) log.warn(mensaje);
	}
	
	public static void error(Logger log, String mensaje, Throwable error) {
		
		error(log, mensaje, true, error);
	}
	
	public static void error(Logger log, String mensaje, boolean mostrarMensaje, Throwable error) {
		
		if(mostrarLog(mostrarMensaje)) log.error(mensaje, error);
	}
	
	private static boolean mostrarLog(boolean mostrarLog) {
		
		return mostrarLog && debug;
	}
}
