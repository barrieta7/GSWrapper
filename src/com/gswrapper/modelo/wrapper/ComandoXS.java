package com.gswrapper.modelo.wrapper;
import java.util.*;

import org.apache.log4j.*;

/**
 * 
 * ComandoXS.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public enum ComandoXS implements Comando { 
	CONECTAR("Open %s:%d", 2),
	DESCONECTAR("Close"), 
	LEER("Ascii"),
	LEER_CAMPO("Ascii(%d,%d,%d)", 3), 
	ENTER("Enter"), 
	PRIMER_INPUT("Home"), 
	SIGUIENTE_INPUT("Tab"), 
	ANTERIOR_INPUT("BackTab"), 
	BORRAR_INPUT_ACTUAL("EraseEOF"), 
	BORRAR_INPUTS("EraseInput"),
	BORRAR("Delete"),
	DESPLAZAR_CURSOR_IZQUIERDA("Left"),
	DESPLAZAR_CURSOR_DERECHA("Rigth"),
	MOVER_CURSOR("MoveCursor(%d,%d)", 2),
	EJECUTAR_MANDATO("PF (%s)", 1), 
	ESCRIBIR("String (\"%s\")", 1),
	INFO_CURSOR("Query(Cursor)"),
	ESPERAR_TEXTO("Expect(\"%s\",%d)",2),
	DESBLOQUEAR_TECLADO("Key(0x12)"),
	ESPERAR_DESBLOQUEO_TERMINAL("Wait(Unlock)");
	
	private static final Logger LOGGER = Logger.getLogger(ComandoXS.class);
	
	private static final String ERROR_COMANDO = 
			"No coincide el número de parámetros del comando";
	
	private static final String ERROR_ARGUMENTOS = 
			"No coincide el tipo de argumento con el especificado en el comando";

	private String comando;
	private int numParametros;
	
	ComandoXS(String comando) {
		this(comando,0);
	}

	ComandoXS(String comando, int numParametros) {
		this.comando = comando;
		this.numParametros = numParametros;
	}

	@Override
	public String ejecutar(Object [] argumentos) {
		
		int numArgumentos = argumentos == null ? 0 : argumentos.length;
		
		if(numParametros == numArgumentos) {
			
			try {
				
				return String.format(comando, argumentos);	
				
			}catch (IllegalFormatException e) {
				
				LOGGER.error(ERROR_ARGUMENTOS,e);
			}
			
		}else {
			
			LOGGER.error(ERROR_COMANDO + comando + " " + argumentos);
		}
		
		return "";
	}
	
}
