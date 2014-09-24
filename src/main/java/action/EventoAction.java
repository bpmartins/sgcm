package action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Aluno;
import model.Criterio;
import model.Evento;
import model.Filtro;
import model.Instrumento;
import model.Professor;
import model.Usuario;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

import dao.AlunoDAO;
import dao.EventoDAO;
import dao.InstrumentoDAO;
import dao.ProfessorDAO;
import dao.UsuarioDAO;

public class EventoAction extends ActionSupport{
	

	private static final long serialVersionUID = 1L;
	@Autowired
	EventoDAO dao = new EventoDAO();
	private List<Evento> eventos;
	private List<Filtro> camposFiltro = new ArrayList<Filtro>();
	private Evento evento;
	private Criterio criterio;
	private Long id;
	private List<Professor> professores;
	private List<String> alunos;
	private String[] alunosSelecionados;
	private boolean isAlteracao =  false;
	
	@Override
	public String execute() throws Exception {
		this.convertArrayToList(evento);
		if (null == evento.getId()) {
			try {
				dao.persistir(evento);
				addActionMessage("Evento cadastrado com sucesso.");
			} catch (SQLException e) {
				addActionError("Erro ao cadastrar evento.");
				return SUCCESS;
			}
		} else {
			try {
				dao.alterar(evento);
				addActionMessage("Evento alterado com sucesso.");
			} catch (SQLException e) {
				addActionError("Erro ao alterar evento.");
				return SUCCESS;
			}
		}

		return SUCCESS;
	}
	
	
	public String formAlterar() {
		try {
			evento = dao.consultarEvento(getId());
			this.convertListToArray(evento);
			professores = new ProfessorDAO().listar(new ArrayList<Criterio>());
			isAlteracao = true;
		} catch (SQLException e) {
			addActionError("Erro ao carregar o professor.");
			return SUCCESS;
		}
		return "cadastro";
	}

	
	protected void convertArrayToList(Evento evento){
		Aluno aluno = new Aluno();
		for(String AlunostoAux : alunosSelecionados){
			String[] aux = AlunostoAux.split("-");
			aluno = new Aluno();
			aluno.setId(Long.valueOf(aux[0].trim()));
			//instrumento.setDescricao(aux[1].trim());
			evento.getAlunos().add(aluno);
		}
	}

	protected void convertListToArray(Evento evento){
		int index = 0;
		alunosSelecionados = new String[evento.getAlunos().size()];

		for(Aluno aluno : evento.getAlunos()){
			alunosSelecionados[index] = aluno.getId() + " - " + aluno.getNome();
			index++;
		}
	}

	public String formListar() {
		return SUCCESS;
	}
	
	public String listar() {
		List<Criterio> criterios = new ArrayList<Criterio>();
		criterios.add(criterio);
		try {
			eventos = dao.listar(criterios);
		} catch (Exception e) {
			addActionError("Ocorreu um erro ao tentar executar a consulta!");
		}
		if(null != eventos && eventos.isEmpty()){
			addActionMessage("Nenhum registro foi encontrado.");
		}
		return SUCCESS;
	}
	
	public String deletar() {
		evento = new Evento();
		evento.setId(getId());
		
		if (dao.remover(evento)) {
			addActionMessage("Evento inativado com sucesso.");
			return SUCCESS;
		} else {
				addActionError("Erro ao inativar o evento.");
			return SUCCESS;
		}
	}
	
	public String cancelar() {
		evento = null;
		return "cancelar";
	}
	
	public String cadastrar() {
		try {
			professores = new ProfessorDAO().listar(new ArrayList<Criterio>());
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		}
		alunos = null;
		alunosSelecionados = null;
		return "cadastro";
	}

	public EventoDAO getDao() {
		return dao;
	}

	public void setDao(EventoDAO dao) {
		this.dao = dao;
	}

	public List<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}

	public List<Filtro> getCamposFiltro() {
		return camposFiltro;
	}

	public void setCamposFiltro(List<Filtro> camposFiltro) {
		this.camposFiltro = camposFiltro;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
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

	public List<String> getAlunos() {
		alunos = new AlunoDAO().carregarAlunoEvnt();
		if(isAlteracao){
				for(int i=0;i<alunosSelecionados.length;i++){
					if(alunos.contains(alunosSelecionados[i])){
						alunos.remove(alunosSelecionados[i]);
					}
				}
		}
		return alunos;
	}

	public void setAlunos(List<String> alunos) {
		this.alunos = alunos;
	}

	public String[] getAlunosSelecionados() {
		return alunosSelecionados;
	}

	public void setAlunosSelecionados(String[] alunosSelecionados) {
		this.alunosSelecionados = alunosSelecionados;
	}
	
}
