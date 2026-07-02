package com.gswrapper.modelo.dao;

import java.util.*;
import java.util.concurrent.locks.*;

import com.gswrapper.modelo.acciones.*;

/**
 * 
 * AccionDao.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-09-19
 *
 */
public class AccionDao implements Dao<MetaAccion>{
	
	private static AccionDao instance;
	
	private ReentrantLock lock = new ReentrantLock();
	
	private Map<String, MetaAccion> acciones = new HashMap<String, MetaAccion>();
	
	public static synchronized AccionDao getInstance() {
		
		if(instance == null) instance = new AccionDao();
		
		return instance;
	}
	
	@Override
	public void add(String nombre, MetaAccion accion) {
		
		lock.lock();
		
		if(!acciones.containsKey(nombre)) acciones.put(nombre, accion);
		
		lock.unlock();
	}

	@Override
	public MetaAccion get(String nombre) {
		
		lock.lock();
		
		MetaAccion accion =  acciones.get(nombre);

		lock.unlock();
		
		return accion;
	}
	
	@Override
	public boolean exists(String nombre) {
		
		lock.lock();
		
		boolean existe = acciones.containsKey(nombre);
		
		lock.unlock();
		
		return existe;
	}

	@Override
	public void remove(String nombre) {
		
		lock.lock();
		
		acciones.remove(nombre);
		
		lock.unlock();
	}

	@Override
	public void removeAll() {
		
		lock.lock();
		
		acciones.clear();
		
		lock.unlock();
	}
}
