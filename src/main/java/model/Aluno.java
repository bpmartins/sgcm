package model;

public class Aluno extends Pessoa{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String nomePai;
	private String nomeMae;
	private String sexo;
	private String nomeDeficiencia;
	private String nomeMedicamento;
	private String experienciaMusical;
	private String nomeEscola;
	private Integer tempoExperiencia; //tempo informado em número de meses. EX: 1 ano e 6 meses = 18 meses
	private String instrumentoAprendido;
	private String preferencia; //instrumento que o aluno deseja aprender
	private Boolean sairSozinho;


	public String getNomePai() {
		return nomePai;
	}
	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}
	public String getNomeMae() {
		return nomeMae;
	}
	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getNomeDeficiencia() {
		return nomeDeficiencia;
	}
	public void setNomeDeficiencia(String nomeDeficiencia) {
		this.nomeDeficiencia = nomeDeficiencia;
	}
	public String getNomeMedicamento() {
		return nomeMedicamento;
	}
	public void setNomeMedicamento(String nomeMedicamento) {
		this.nomeMedicamento = nomeMedicamento;
	}
	public String getExperienciaMusical() {
		return experienciaMusical;
	}
	public void setExperienciaMusical(String experienciaMusical) {
		this.experienciaMusical = experienciaMusical;
	}
	public String getNomeEscola() {
		return nomeEscola;
	}
	public void setNomeEscola(String nomeEscola) {
		this.nomeEscola = nomeEscola;
	}
	public Integer getTempoExperiencia() {
		return tempoExperiencia;
	}
	public void setTempoExperiencia(Integer tempoExperiencia) {
		this.tempoExperiencia = tempoExperiencia;
	}
	public String getInstrumentoAprendido() {
		return instrumentoAprendido;
	}
	public void setInstrumentoAprendido(String instrumentoAprendido) {
		this.instrumentoAprendido = instrumentoAprendido;
	}
	public String getPreferencia() {
		return preferencia;
	}
	public void setPreferencia(String preferencia) {
		this.preferencia = preferencia;
	}
	public Boolean getSairSozinho() {
		return sairSozinho;
	}
	public void setSairSozinho(Boolean sairSozinho) {
		this.sairSozinho = sairSozinho;
	}

}
