package model.relatorios;

import java.io.Serializable;

public class AlunoVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String aluno;
	private String turmas;
	private String emprestimos;
	private String eventos;
	private String texto;
	
	public String getAluno() {
		return aluno;
	}
	public void setAluno(String aluno) {
		this.aluno = aluno;
	}
	public String getTurmas() {
		return turmas;
	}
	public void setTurmas(String turmas) {
		this.turmas = turmas;
	}
	public String getEmprestimos() {
		return emprestimos;
	}
	public void setEmprestimos(String emprestimos) {
		this.emprestimos = emprestimos;
	}
	public String getEventos() {
		return eventos;
	}
	public void setEventos(String eventos) {
		this.eventos = eventos;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}

}
