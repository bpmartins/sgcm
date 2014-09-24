package model;

import java.util.ArrayList;
import java.util.List;

public class Professor extends Pessoa {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String matricula;
	private List<Instrumento> instrumentos;
	private List<Evento> eventos;
	private List<Turma> turmas;
	
	
	public Professor() {
		super();
		instrumentos = new ArrayList<Instrumento>();
		eventos = new ArrayList<Evento>();
		turmas = new ArrayList<Turma>();
	}
	public List<Instrumento> getInstrumentos() {
		return instrumentos;
	}
	public void setInstrumentos(List<Instrumento> instrumentos) {
		this.instrumentos = instrumentos;
	}
	public List<Evento> getEventos() {
		return eventos;
	}
	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}
	public List<Turma> getTurmas() {
		return turmas;
	}
	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public String getMatricula() {
		return matricula;
	}
	
	

}
