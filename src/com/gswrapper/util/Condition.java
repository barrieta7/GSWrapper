package com.gswrapper.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 
 * Condition.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public class Condition {

	public static boolean empty(Object object) {
		
		return object == null;
	}
	
	public static boolean empty(String string) {
		
		return string == null || string.trim().equals("");
	}
	
	public static boolean empty(BigDecimal bigDecimal) {
		
		return bigDecimal ==  null || bigDecimal.compareTo(BigDecimal.ZERO) == 0;
	}
	
	public static boolean empty(Integer integer) {
		
		return integer == null || integer == 0;
	}
	
	public static boolean empty(Long numero) {
		
		return numero == null || numero == 0L;
	}
	
	public static boolean empty(Map<?,?> map) {
		
		return map == null || map.isEmpty();
	}
	
	public static boolean empty(List<?> list) {
		
		return list == null || list.isEmpty();
	}
	
	public static <V> V eval(boolean condition,V positive,V negative) {
		
		return (condition ? positive : negative);
	}
	
	public static <V> V evalNotEmpty(V positive,V negative) {
		
		return (!empty(positive) ? positive : negative);
	}
	
	public static <V> V evalNotEmpty(V positive) {
		
		return evalNotEmpty(positive, null);
	}
}
