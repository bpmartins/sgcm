package model;

import java.io.Serializable;

public class Filtro implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String value;
	private String descricao;
	
	public Filtro(String value, String descricao) {
		super();
		this.value = value;
		this.descricao = descricao;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
