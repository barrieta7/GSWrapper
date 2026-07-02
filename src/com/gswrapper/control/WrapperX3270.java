package com.gswrapper.control;
import java.io.*;

import org.apache.log4j.*;

import com.gswrapper.excepciones.*;
import com.gswrapper.modelo.wrapper.*;
import com.gswrapper.util.*;

/**
 * 
 * WrapperX3270.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.2v
 * @date 2019-07-19
 *
 */
public class WrapperX3270 implements Wrapper3270 {
	
	private static final Logger LOGGER = Logger.getLogger(WrapperX3270.class);
	
	private static final String MENSAJE_NUEVA_CONEXION = "Se ha establecido una nueva conexión a %s : %d";
	private static final String MENSAJE_NUEVO_USUARIO = "El usuario %s se ha conectado";
	private static final String MENSAJE_COMANDO = "Se esta ejecutando %s";
	private static final String MENSAJE_DESCONEXION = "Se ha perdido la conexión con el servidor";
	
	private DisplaylessTerminal terminal;
	private Usuario usuarioTerminal;
	private String idPantallaPrincipal;
	
	public WrapperX3270(String pathEmulador) throws IOException {
		
		terminal = new DisplaylessTerminalXS();
		
		terminal.abrir(pathEmulador);
	}

	@Override
	public boolean conectar(String ip, int puerto) {
		
		boolean conectado = false;
		
		if(!terminal.getConexion().estaConectado()) {
			
			conectado = ejecutarComando(ComandoXS.CONECTAR, ip, puerto).estaConectado();
			
			if(conectado) {
				
				terminal.setConexion(new Conexion(ip, puerto));
				
				LogUtil.info(LOGGER, String.format(MENSAJE_NUEVA_CONEXION, ip, puerto));
					
			} else {
				
				terminal.reiniciar();
			}
		}
		
		return conectado;
	}

	@Override
	public void desconectar() {
		
		terminal.getConexion().desconectar();
		
		ejecutarComando(ComandoXS.DESCONECTAR);
		
		terminal.cerrar();
	}
	
	@Override
	public boolean reiniciar() {
		
		if(terminal.getConexion().estaConectado()) {
			
			terminal.getConexion().desconectar();
			
			ejecutarComando(ComandoXS.DESCONECTAR);
		}
		
		terminal.reiniciar();
		
		boolean conectado = conectar(terminal.getConexion().getIp(), terminal.getConexion().getPuerto());
		
		boolean logueado = login(usuarioTerminal.getId(), usuarioTerminal.getPassword(), idPantallaPrincipal, 2);
		
		return conectado && logueado;
	}

	@Override
	public boolean login(String usuario, String password, String idPantallaEsperada) {
			
		return login(usuario, password, idPantallaEsperada, 1);
	}
	
	@Override
	public boolean login(String usuario, String password, String idPantallaEsperada, int numeroEnter) {
		
		escribir(usuario);
		
		desplazarCursor();
		
		escribir(password);
		
		enter(numeroEnter);
		
		boolean logueado = validarPantalla(idPantallaEsperada);
		
		if(logueado) {
			
			LogUtil.info(LOGGER, String.format(MENSAJE_NUEVO_USUARIO, usuario));
			
			usuarioTerminal = new Usuario(usuario, password);
			
			idPantallaPrincipal = idPantallaEsperada;
			
		} else {
			
			if(validarPantalla("interactivo")) {
				
				escribir("90");
				
				enter(4);
				
				login(usuario, password, idPantallaEsperada, numeroEnter);
			}
		}
		
		return logueado;
	}

	@Override
	public void ejecutarMandato(Mandato mandato) {
		
		ejecutarComando(mandato);
	}
	
	@Override
	public Cursor getCursor() {
		
		String separador = " ";
		
		RespuestaXS respuesta = ejecutarComando(ComandoXS.INFO_CURSOR);
		
		int fila = Integer.parseInt(respuesta.getPantallaTerminal().split(separador)[0]);
		
		int columna = Integer.parseInt(respuesta.getPantallaTerminal().split(separador)[1]);
		
		return new Cursor(fila, columna);
	}
	
	@Override
	public void escribir(String valor) {
		
		escribir(valor, 0, false);
	}

	@Override
	public void escribir(String valor, int posicion) {
		
		escribir(valor, posicion, false);
	}

	@Override
	public void escribir(String valor, boolean enter) {
		
		escribir(valor, 0, enter);
	}

	@Override
	public void escribir(String valor, int posicion, boolean enter) {
		
		desplazarCursor(posicion);
		
		ejecutarComando(ComandoXS.ESCRIBIR, valor);
		
		enter(Condition.eval(enter, 1, 0));
	}
	
	@Override
	public void escribir(String valor, int fila, int columna) {
		
		moverCursor(fila, columna);
		
		ejecutarComando(ComandoXS.ESCRIBIR, valor);
	}

	@Override
	public void escribir(String valor, int fila, int columna, int dimension) {
		
		escribir(valor, fila, columna, dimension, false);
	}

	@Override
	public void escribir(String valor, int fila, int columna, int dimension, boolean enter) {
		
		moverCursor(fila, columna);
		
		if(valor.length() > dimension) valor = valor.substring(0, dimension);
		
		ejecutarComando(ComandoXS.ESCRIBIR, valor);
		
		enter(Condition.eval(enter, 1, 0));
	}
	
	@Override
	public void desbloquearTeclado() {
		
		ejecutarComando(ComandoXS.DESBLOQUEAR_TECLADO);
	}
	
	@Override
	public void borrar() {
		
		borrar(1);
	}
	
	@Override
	public void borrar(int fila, int columna, int dimension) {
		
		moverCursor(fila, columna + dimension);
		
		borrar(dimension);
	}

	@Override
	public void borrar(int dimension) {
		
		for (int i = 0; i < dimension; i++) {
			
			ejecutarComando(ComandoXS.DESPLAZAR_CURSOR_IZQUIERDA);
			
			ejecutarComando(ComandoXS.BORRAR);
		}
	}

	@Override
	public void moverCursor(int fila, int columna) {
		
		ejecutarComando(ComandoXS.MOVER_CURSOR, fila, columna);
	}
	
	@Override
	public boolean esperarTexto(String textoEsperado, int segundosTimeOut) {
		
		RespuestaXS respuesta = ejecutarComando(ComandoXS.ESPERAR_TEXTO, textoEsperado, segundosTimeOut);

		return respuesta.errorEjecucion();
	}
	
	@Override
	public void desplazarCursor() {
		
		ejecutarComando(ComandoXS.SIGUIENTE_INPUT);
	}
		
	@Override
	public void desplazarCursor(int posicion) {
		
		if(posicion > 0) {
			
			ejecutarComando(ComandoXS.PRIMER_INPUT);
			
			for (int i = 0; i < posicion; i++) {
				
				desplazarCursor();
			}
		}
	}

	@Override
	public String leerPantalla() {
		
		return ejecutarComando(ComandoXS.LEER).getPantallaTerminal();
	}
	
	@Override
	public String leerValor(int fila, int columna, int dimension) {
		
		return ejecutarComando(ComandoXS.LEER_CAMPO, fila, columna, dimension).getPantallaTerminal().trim();
	}

	@Override
	public boolean validarPantalla(String key) {
	
		esperarTexto(key, 1);
		
		return leerPantalla().contains(key);
	}
	
	@Override
	public void limpiarPantalla() {
		
		ejecutarComando(ComandoXS.BORRAR_INPUTS);
		
		ejecutarComando(ComandoXS.PRIMER_INPUT);
	}
	
	@Override
	public void enter() {
		
		enter(1);
	}

	@Override
	public void enter(int repeticiones) {
		
		for (int i = 0; i < repeticiones; i++) {
			
			ejecutarComando(ComandoXS.ENTER);
		}
	}

	protected RespuestaXS ejecutarComando(Comando comando, Object ... argumentos) {
		
		LogUtil.info(LOGGER, String.format(MENSAJE_COMANDO, comando.ejecutar(argumentos)));
		
		terminal.escribir(comando, argumentos);
		
		return getRespuestaTerminal();	
	}
	
	
	private RespuestaXS getRespuestaTerminal() {
		
		RespuestaXS respuestaTerminal = null;
		
		try {
			
			respuestaTerminal = new RespuestaXS(terminal.leer());
			
			if(terminal.getConexion().estaConectado() && !respuestaTerminal.estaConectado()) {
				
				LogUtil.warn(LOGGER, MENSAJE_DESCONEXION);
				
				terminal.getConexion().desconectar();
				
				throw new NoConnectionExcepcion(MENSAJE_DESCONEXION);
			}
			
		} catch (TerminalTimeOutException e) {
			
			LogUtil.warn(LOGGER, e.getMessage());
			
			respuestaTerminal = new RespuestaXS();
		}
		
		return respuestaTerminal;
	}
	
}
