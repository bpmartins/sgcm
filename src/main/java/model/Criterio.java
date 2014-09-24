package model;

import java.io.Serializable;

public class Criterio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String campo;
	private String operador;
	private String valor;
	
	public String getCampo() {
		return campo;
	}
	public void setCampo(String campo) {
		this.campo = campo;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public void setOperador(String operador) {
		this.operador = operador;
	}
	public String getOperador() {
		return operador;
	}

}
