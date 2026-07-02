package com.gswrapper.modelo.wrapper;

/**
 * 
 * Wrapper3270.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */
public interface Wrapper3270 {
	
	public boolean conectar(String ip, int puerto);
	public void desconectar();
	public boolean reiniciar();
	public boolean login(String usuario, String password, String idPantallaEsperada);
	public boolean login(String usuario, String password, String idPantallaEsperada, int numeroEnter);
	public void ejecutarMandato(Mandato mandato);
	public Cursor getCursor();
	public void escribir(String valor);
	public void escribir(String valor, int posicionCursor);
	public void escribir(String valor, boolean enter);
	public void escribir(String valor, int posicionCursor, boolean enter);
	public void escribir(String valor, int fila, int columna);
	public void escribir(String valor, int fila, int columna, int dimension);
	public void escribir(String valor, int fila, int columna, int dimension, boolean enter);
	public void desbloquearTeclado();
	public void borrar();
	public void borrar(int fila, int columna, int dimension);
	public void borrar(int dimension);
	public void moverCursor(int fila, int columna);
	public boolean esperarTexto(String textoEsperado, int segundosTimeOut);
	public void desplazarCursor();
	public void desplazarCursor(int posiciones);
	public String leerPantalla();
	public String leerValor(int fila, int columna, int dimension);
	public boolean validarPantalla(String key);
	public void limpiarPantalla();
	public void enter();
	public void enter(int repeticiones);
}
