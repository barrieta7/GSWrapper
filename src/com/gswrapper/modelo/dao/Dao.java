package com.gswrapper.modelo.dao;

/**
 * 
 * Dao.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-09-19
 *
 */
public interface Dao <E> {
	
	public void add(String key, E Object);
	
	public E get(String key);
	
	public boolean exists(String key);
	
	public void remove(String key);
	
	public void removeAll();
}
