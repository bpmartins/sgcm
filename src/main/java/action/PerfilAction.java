package action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Criterio;
import model.Filtro;
import model.Perfil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;

import dao.PerfilDAO;

@Component
public class PerfilAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	@Autowired
	PerfilDAO dao = new PerfilDAO();
	private List<Filtro> camposFiltro = new ArrayList<Filtro>();
	private Perfil perfil;
	private Criterio criterio;
	private Long id;
	private List<Perfil> perfis;

	@Override
	public String execute() throws Exception {

		if (null == perfil.getId()) {
			try {
				dao.persistir(perfil);
				addActionMessage("Perfil cadastrado com sucesso.");
			} catch (SQLException e) {
				addActionError("Erro ao cadastrar perfil.");
				return SUCCESS;
			}
		} else {
			try {
				dao.alterar(perfil);
				addActionMessage("Perfil alterado com sucesso.");
			} catch (SQLException e) {
				addActionError("Erro ao alterar perfil.");
				return SUCCESS;
			}
		}

		return SUCCESS;
	}

	public String cancelar() {
		perfil = null;
		return "cancelar";
	}

	public String formListar() {
		return SUCCESS;
	}

	public String listar() {
		List<Criterio> criterios = new ArrayList<Criterio>();
		criterios.add(criterio);
		try {
			perfis = dao.listar(criterios);
		} catch (Exception e) {
			addActionError("Ocorreu um erro ao tentar executar a consulta!");
		}
		if (null != perfis && perfis.isEmpty()) {
			addActionMessage("Nenhum registro foi encontrado.");
		}
		return SUCCESS;
	}
	
	public String deletar() {
		perfil = new Perfil();
		perfil.setId(getId());
		
		if(perfil.getId().intValue() == 1 || perfil.getId().intValue() == 2){
			addActionMessage("Este Perfil não pode ser inativado.");
			return SUCCESS;
		}
		
		if (dao.inativar(perfil)) {
			addActionMessage("Perfil Inativado com sucesso.");
			return SUCCESS;
		} else {
				addActionError("Erro ao inativar perfil.");
			return SUCCESS;
		}
	}
	
	public String formAlterar() {
	    try {
	    	perfis = new PerfilDAO().listarParaCombos();
			perfil = dao.consultarPerfil(getId());
			if(perfil.getId().intValue() == 1 || perfil.getId().intValue() == 2){
				perfis = new ArrayList<Perfil>();
				addActionMessage("Este Perfil não pode ser modificado.");
				return SUCCESS;
			}
		} catch (SQLException e) {
			addActionError("Erro ao consultar o perfil.");
			return SUCCESS;
		}
		return "cadastro";
}

	public String cadastrar() {
		return "cadastro";
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
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

	public List<Perfil> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}

	public void setCamposFiltro(List<Filtro> camposFiltro) {
		this.camposFiltro = camposFiltro;
	}

	public List<Filtro> getCamposFiltro() {
		camposFiltro.add(new Filtro("codigo", "Código"));
		camposFiltro.add(new Filtro("descricao", "Descrição"));
		return camposFiltro;
	}

}
