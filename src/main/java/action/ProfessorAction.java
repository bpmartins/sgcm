package action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import model.Criterio;
import model.Filtro;
import model.Instrumento;
import model.Perfil;
import model.Professor;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;


import dao.InstrumentoDAO;
import dao.PerfilDAO;
import dao.ProfessorDAO;

@Component
public class ProfessorAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	ProfessorDAO dao = new ProfessorDAO();
	private List<Filtro> camposFiltro = new ArrayList<Filtro>();
	private Professor professor = new Professor();
	private Criterio criterio;
	private Long id;
	private List<Professor> professores;
	private List<String> instrumentos;
	private String[] instrumentosSelecionados;
	private boolean isAlteracao =  false;

	@Override
	public String execute() throws Exception {
		this.convertArrayToList(professor);
		if (null == professor.getId()) {
			try {
				dao.persistir(professor);
				addActionMessage("Professor cadastrado com sucesso.");
			} catch (SQLException e) {
				addActionError("Erro ao cadastrar professor.");
				return SUCCESS;
			}
		} else {
			try {
				dao.alterar(professor);
				addActionMessage("Professor alterado com sucesso.");
			} catch (SQLException e) {
				addActionError("Erro ao alterar professor.");
				return SUCCESS;
			}
		}

		return SUCCESS;
	}

	public String deletar() {
		professor = new Professor();
		professor.setId(getId());

		if (dao.inativar(professor)) {
			addActionMessage("Professor Inativado com sucesso.");
			return SUCCESS;
		} else {
			addActionError("Erro ao inativar professor.");
			return SUCCESS;
		}
	}

	public String formAlterar() {
		try {
			professor = dao.consultarProfessor(getId());
			this.convertListToArray(professor);
			isAlteracao = true;
		} catch (SQLException e) {
			addActionError("Erro ao carregar o professor.");
			return SUCCESS;
		}
		return "cadastro";
	}

	public String cadastrar() {
		instrumentos = null;
		instrumentosSelecionados = null;
		return "cadastro";
	}

	public String cancelar() {
		return "cancelar";
	}

	public String formListar() {
		return SUCCESS;
	}

	protected void convertArrayToList(Professor prof){
		Instrumento instrumento = new Instrumento();
		for(String instrumentoAux : instrumentosSelecionados){
			String[] aux = instrumentoAux.split("-");
			instrumento = new Instrumento();
			instrumento.setId(Long.valueOf(aux[0].trim()));
			//instrumento.setDescricao(aux[1].trim());
			prof.getInstrumentos().add(instrumento);
		}
	}

	protected void convertListToArray(Professor prof){
		int index = 0;
		instrumentosSelecionados = new String[prof.getInstrumentos().size()];

		for(Instrumento instrumento : prof.getInstrumentos()){
			instrumentosSelecionados[index] = instrumento.getId() + " - " + instrumento.getDescricao();
			index++;
		}
	}

	public String listar() {
		List<Criterio> criterios = new ArrayList<Criterio>();
		criterios.add(criterio);
		try {
			professores = dao.listar(criterios);
		} catch (Exception e) {
			addActionError("Ocorreu um erro ao tentar executar a consulta!");
		}
		if (null != professores && professores.isEmpty()) {
			addActionMessage("Nenhum registro foi encontrado.");
		}
		return SUCCESS;
	}

	public List<Filtro> getCamposFiltro() {
		camposFiltro.add(new Filtro("matricula", "Matricula"));
		camposFiltro.add(new Filtro("nome", "Nome"));
		return camposFiltro;
	}

	public void setCamposFiltro(List<Filtro> camposFiltro) {
		this.camposFiltro = camposFiltro;
	}
	public Professor getProfessor() {
		return professor;
	}
	public void setProfessor(Professor professor) {
		this.professor = professor;
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
	public List<Professor> getProfessores() {
		return professores;
	}
	public void setProfessores(List<Professor> professores) {
		this.professores = professores;
	}

	public List<String> getInstrumentos() {
		instrumentos = new InstrumentoDAO().carregarInstrumentosProf();
		if(isAlteracao){
				for(int i=0;i<instrumentosSelecionados.length;i++){
					if(instrumentos.contains(instrumentosSelecionados[i])){
						instrumentos.remove(instrumentosSelecionados[i]);
					}
				}
		}

		return instrumentos;
	}

	public void setInstrumentos(List<String> instrumentos) {
		this.instrumentos = instrumentos;
	}

	public String[] getInstrumentosSelecionados() {
		return instrumentosSelecionados;
	}

	public void setInstrumentosSelecionados(String[] instrumentosSelecionados) {
		this.instrumentosSelecionados = instrumentosSelecionados;
	}

}
