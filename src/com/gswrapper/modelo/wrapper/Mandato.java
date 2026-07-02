package com.gswrapper.modelo.wrapper;

/**
 * 
 * Mandato.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public enum Mandato implements Comando {
	
	F1("PF(1)"), F2("PF(2)"), F3("PF(3)"), F4("PF(4)"), F5("PF(5)"), F6("PF(6)"),
	F7("PF(7)"), F8("PF(8)"), F9("PF(9)"), F10("PF(10)"), F11("PF(11)"), F12("PF(12)"),
	F13("PF(3)"), F14("PF(14)"), F15("PF(15)"), F16("PF(16)"), F17("PF(17)"), F18("PF(18)"),
	F19("PF(19)"), F20("PF(20)"), F21("PF(21)"), F22("PF(22)"), F23("PF(23)"), F24("PF(24)");
	
	private String mandato;
	
	Mandato(String mandato) {
	    this.mandato = mandato;
	}

	@Override
	public String ejecutar(Object[] argumentos) {
		return mandato;
	}
	
}
