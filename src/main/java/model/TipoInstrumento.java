package model;

public enum TipoInstrumento {
	
	CORDAS(1,"Cordas"),
	PERCURSSAO(2,"Percussão"),
	SOPRO(3,"Sopro"),
	TECLAS(4,"Teclas"),
	ELETRICOS(5,"Eléctricos");
	
	
	private Integer codigo;
	private String descricao;
	
	
	private TipoInstrumento(Integer codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	

	public static TipoInstrumento valueOf(Integer codigo){
		switch (codigo){
			case 1 : return CORDAS;
			case 2 : return PERCURSSAO;
			case 3 : return SOPRO;
			case 4 : return TECLAS;
			case 5 : return ELETRICOS;
			default : return null;
		}
	}


	public Integer getCodigo() {
		return codigo;
	}


	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
