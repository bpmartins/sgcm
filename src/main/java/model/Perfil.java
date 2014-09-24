package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Perfil implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String descricao;
	private String situacao;
	private List<Usuario> users;
	
	public Perfil() {
		super();
		setUsers(new ArrayList<Usuario>());
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setUsers(List<Usuario> users) {
		this.users = users;
	}
	public List<Usuario> getUsers() {
		return users;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public String getSituacao() {
		return situacao;
	}
}
