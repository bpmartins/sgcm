package action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import model.Criterio;
import model.Filtro;
import model.GenericItem;
import model.Instrumento;
import model.TipoInstrumento;

import com.opensymphony.xwork2.ActionSupport;

import dao.InstrumentoDAO;

public class InstrumentoAction extends ActionSupport{
	
	
	private static final long serialVersionUID = 1L;
	@Autowired
	InstrumentoDAO dao = new InstrumentoDAO();
	private List<GenericItem> tiposInstrumentos = new ArrayList<GenericItem>();
	private Integer tipoInstrumento;
	private List<Instrumento> instrumentos;
	private List<Filtro> camposFiltro = new ArrayList<Filtro>();
	private Instrumento instrumento;
	private Criterio criterio;
	private Long id;

	@Override
	public String execute() throws Exception{
		
		if(null != instrumento){
			instrumento.setTipoInstrumento(TipoInstrumento.valueOf(this.getTipoInstrumento()));
		}
		
			if(null == instrumento.getId()){
				try {
					dao.persistir(instrumento);
					addActionMessage("Instrumento cadastrado com sucesso.");
				} catch (SQLException e) {
					addActionError("Erro ao cadastrar o instrumento.");
					return SUCCESS;
				}
			}else{
				try {
					dao.alterar(instrumento);
					addActionMessage("Instrumento alterado com sucesso.");
				} catch (SQLException e) {
					addActionError("Erro ao alterar o instrumento.");
					return SUCCESS;
				}
			}
			
			return SUCCESS;
	}
	
	public String deletar() {
		instrumento = new Instrumento();
		instrumento.setId(getId());
		if (dao.inativar(instrumento)) {
			addActionMessage("Instrumento Inativado com sucesso.");
			return SUCCESS;
		} else {
				addActionError("Erro ao inativar o instrumento.");
			return SUCCESS;
		}
	}
	
	public String formAlterar() {
		    try {
		    	instrumento = dao.consultarInstrumento(getId());
		    	this.setTipoInstrumento(instrumento.getTipoInstrumento().getCodigo());
			} catch (SQLException e) {
				addActionError("Erro ao consultar o Instrumento.");
				return SUCCESS;
			}
			return "cadastro";
	}
	
	public String cadastrar() {
		return "cadastro";
	}

	
	public String cancelar() {
		instrumento = null;
		return "cancelar";
	}


	public String formListar() {
			return SUCCESS;
	}
	
	public String listar() {
		List<Criterio> criterios = new ArrayList<Criterio>();
		criterios.add(criterio);
		try {
			instrumentos = dao.listar(criterios);
		} catch (Exception e) {
			addActionError("Ocorreu um erro ao tentar executar a consulta!");
		}
		if(null != instrumentos && instrumentos.isEmpty()){
			addActionMessage("Nenhum registro foi encontrado.");
		}
		return SUCCESS;
	}

	public InstrumentoDAO getDao() {
		return dao;
	}

	public void setDao(InstrumentoDAO dao) {
		this.dao = dao;
	}

	public List<Instrumento> getInstrumentos() {
		return instrumentos;
	}

	public void setInstrumentos(List<Instrumento> instrumentos) {
		this.instrumentos = instrumentos;
	}

	public List<Filtro> getCamposFiltro() {
		camposFiltro.add(new Filtro("codigo", "Código"));
		camposFiltro.add(new Filtro("descricao", "Descrição"));
		camposFiltro.add(new Filtro("tipo", "Tipo"));
		return camposFiltro;
	}

	public void setCamposFiltro(List<Filtro> camposFiltro) {
		this.camposFiltro = camposFiltro;
	}

	public Instrumento getInstrumento() {
		return instrumento;
	}

	public void setInstrumento(Instrumento instrumento) {
		this.instrumento = instrumento;
	}

	public Criterio getCriterio() {
		return criterio;
	}

	public void setCriterio(Criterio criterio) {
		this.criterio = criterio;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTiposInstrumentos(List<GenericItem> tiposInstrumentos) {
		this.tiposInstrumentos = tiposInstrumentos;
	}

	public List<GenericItem> getTiposInstrumentos() {
		tiposInstrumentos.add(new GenericItem(TipoInstrumento.CORDAS.getCodigo().toString(), TipoInstrumento.CORDAS.getDescricao()));
		tiposInstrumentos.add(new GenericItem(TipoInstrumento.ELETRICOS.getCodigo().toString(), TipoInstrumento.ELETRICOS.getDescricao()));
		tiposInstrumentos.add(new GenericItem(TipoInstrumento.PERCURSSAO.getCodigo().toString(), TipoInstrumento.PERCURSSAO.getDescricao()));
		tiposInstrumentos.add(new GenericItem(TipoInstrumento.SOPRO.getCodigo().toString(), TipoInstrumento.SOPRO.getDescricao()));
		tiposInstrumentos.add(new GenericItem(TipoInstrumento.TECLAS.getCodigo().toString(), TipoInstrumento.TECLAS.getDescricao()));
		return tiposInstrumentos;
	}

	public void setTipoInstrumento(Integer tipoInstrumento) {
		this.tipoInstrumento = tipoInstrumento;
	}

	public Integer getTipoInstrumento() {
		return tipoInstrumento;
	}

}
