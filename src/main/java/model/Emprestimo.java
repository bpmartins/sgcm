package model;

import java.io.Serializable;
import java.sql.Date;

public class Emprestimo implements Serializable{
	
	private Long id;
	private Date dataInicio;
	private Date dataFim;
	private String observacoes;
	private String indBaixa;
	private Instrumento instrumento;
	private Aluno aluno;
	
	
	public Emprestimo() {
		super();
		instrumento = new Instrumento();
		aluno = new Aluno();		
	}
	
	public Instrumento getInstrumento() {
		return instrumento;
	}

	public void setInstrumento(Instrumento instrumento) {
		this.instrumento = instrumento;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getObservacoes() {
		return observacoes;
	}
	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
	public String getIndBaixa() {
		return indBaixa;
	}
	public void setIndBaixa(String indBaixa) {
		this.indBaixa = indBaixa;
	}
	
	
	
}
