package model.relatorios;

import java.io.Serializable;

public class TurmaVO  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String turma;
	private String professor;
	private String alunos;
	private String frequencias;
	
	
	public String getTurma() {
		return turma;
	}
	public void setTurma(String turma) {
		this.turma = turma;
	}
	public String getProfessor() {
		return professor;
	}
	public void setProfessor(String professor) {
		this.professor = professor;
	}
	public String getAlunos() {
		return alunos;
	}
	public void setAlunos(String alunos) {
		this.alunos = alunos;
	}
	public String getFrequencias() {
		return frequencias;
	}
	public void setFrequencias(String frequencias) {
		this.frequencias = frequencias;
	}

}
