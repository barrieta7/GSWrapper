package com.gswrapper.modelo.dao;

import java.util.*;
import java.util.concurrent.locks.*;

import com.gswrapper.modelo.vista.*;

/**
 * 
 * PantallaDao.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-09-19
 *
 */
public class PantallaDao implements Dao<Pantalla>{
	
	private static PantallaDao instance;
	
	private ReentrantLock lock = new ReentrantLock();
	
	private Map<String, Pantalla> pantallas = new HashMap<String, Pantalla>();
	
	public static synchronized PantallaDao getInstance() {
		
		if(instance == null) instance = new PantallaDao();
		
		return instance;
	}
	
	@Override
	public void add(String nombre, Pantalla pantalla) {
		
		lock.lock();
		
		if(!pantallas.containsKey(nombre)) pantallas.put(nombre, pantalla);
		
		lock.unlock();
	}

	@Override
	public Pantalla get(String nombre) {

		lock.lock();
		
		Pantalla pantalla = pantallas.get(nombre);
		
		lock.unlock();
		
		return pantalla;
	}
	
	@Override
	public boolean exists(String nombre) {

		lock.lock();
		
		boolean existe = pantallas.containsKey(nombre);
		
		lock.unlock();
		
		return existe;
	}

	@Override
	public void remove(String nombre) {
		
		lock.lock();
		
		pantallas.remove(nombre);
		
		lock.unlock();
	}

	@Override
	public void removeAll() {
		
		lock.lock();
		
		pantallas.clear();
		
		lock.unlock();
	}
}
