package com.gswrapper.util;

import java.lang.annotation.*;
import java.lang.reflect.*;

import org.apache.commons.lang3.text.*;
import org.apache.log4j.*;
/**
 * 
 * ClassUtil.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */

public class ClassUtil {
	
	private static final Logger LOGGER = Logger.getLogger(ClassUtil.class);
	
	public static final String ERROR_INSTANCIA = "No se ha podido crean una instancia de la clase %s";
	public static final String ERROR_CONSTRUCTOR = "No se ha encontrado el constructor de la clase %s";
	public static final String ERROR_PARSEAR_CAMPO = "No se ha podido parsear el campo %s al tipo %s";
	public static final String ERROR_RECUPERAR_METODO = "No se ha podido recuperar el metodo %s";
	public static final String ERROR_EJECUTAR_METODO = "No se ha podido ejecutar el metodo %s";
	
	
	public static Class<?> classForName(String nombreClase){
		
		try {
			
			return Class.forName(nombreClase);
			
		} catch (ClassNotFoundException e) {
			
			return null;
		}
	}
	
	public static Field[] getFields(Class<?> clase) {
		
		return clase.getDeclaredFields();
	}
	
	public static <A extends Annotation> A getFieldAnotation(Class<A> annotationClass, Field field) {
		
		return field.getAnnotation(annotationClass);
	}
	
	public static <I> I newInstance(Class<I> instanceClass) {
		
		I instance = null;
		
		try {
			
			instance = instanceClass.newInstance();
			
		} catch (InstantiationException | IllegalAccessException e) {
			
			LOGGER.error(String.format(ERROR_INSTANCIA, instanceClass.getName()), e);
		}
		
		return instance;
	}
	
	public static <I> Constructor<I> getConstructor(Class<I> constructorClass, Class<?> ... constructorClasses) {
		
		Constructor<I> constructor = null;
		
		try {
			
			constructor = constructorClass.getDeclaredConstructor(constructorClasses);
			
		} catch (NoSuchMethodException | SecurityException e) {
			
			LOGGER.error(String.format(ERROR_CONSTRUCTOR, constructorClass.getName()), e);
		}
		
		return constructor;
	}
	
	public static <I> I parseString(Class<I> classToParse, String value, I defaultValue) {
		
		I finalValue = defaultValue;
		
		try {
			
			finalValue  = classToParse.getDeclaredConstructor(String.class).newInstance(value);
			
		} catch (Exception e) {
			
			LOGGER.error(String.format(ERROR_PARSEAR_CAMPO, value, classToParse.getName()));
		}
		
		return finalValue;
	}
	
	public static <I> Method getMethod(Class<I> classMethod, String methodName, Class<?> ... classParameters) {
		
		Method method = null;
		
		try {
			
			method = classMethod.getMethod(methodName, classParameters);
				
		} catch (NoSuchMethodException | SecurityException e) {
				
			LOGGER.error(String.format(ERROR_RECUPERAR_METODO, methodName), e);
		}
			
		return method;
	}
	
	public static <I> Object invoke(I instance, Method method, Object ... arguments) {
		
		Object result = null;
		
		try {
				result = method.invoke(instance, arguments);
				
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				
			LOGGER.error(String.format(ERROR_EJECUTAR_METODO, method.getName()), e);
		}
			
		return result;
	}
	
	private static final String GET_METHOD = "get%s";
	
	public static <O> Method getGetMethod(Class<O> classMehod, Field field) {
		
		return getMehod(classMehod, field, GET_METHOD);
	}
	
	private static final String SET_METHOD = "set%s";
	
	public static <O> Method getSetMethod(Class<O> classMehod, Field field) {
		
		return getMehod(classMehod, field, SET_METHOD);
	}
	
	private static <O> Method getMehod(Class<O> classMehod, Field field, String methodType) {
		
		String methodName = String.format(methodType, WordUtils.capitalize(field.getName()));
		
		return getMethod(classMehod, methodName, field.getType());
	}
}
