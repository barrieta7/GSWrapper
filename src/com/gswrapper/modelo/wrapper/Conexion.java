package com.gswrapper.modelo.wrapper;

/**
 * 
 * Conexion.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public class Conexion {
	
	private String ip;
	private int puerto;
	
	private boolean conectado;
	
	public Conexion() {
		
		this.conectado = false;
	}
	
	public Conexion(String ip, int puerto) {
		
		this.ip = ip;
		this.puerto = puerto;
		this.conectado = true;
	}
	

	public String getIp() {
		return ip;
	}

	public int getPuerto() {
		return puerto;
	}
	
	public boolean estaConectado() {
		return conectado;
	}
	
	public void conectar() {
		this.conectado = true;
	}
	
	public void desconectar() {
		this.conectado = false;
	}
}
