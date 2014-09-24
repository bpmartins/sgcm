package action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;

import model.Aluno;
import model.Criterio;
import model.Filtro;

import com.opensymphony.xwork2.ActionSupport;

import dao.AlunoDAO;

public class AlunoAction extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	AlunoDAO dao = new AlunoDAO();
	private List<Filtro> camposFiltro = new ArrayList<Filtro>();
	private Aluno aluno;
	private Criterio criterio;
	private Long id;
	private List<Aluno> alunos;
	private boolean isAlteracao =  false;
	
	
	@Override
	@SkipValidation	
	public String execute() throws Exception {
		if (null == aluno.getId()) {
			try {
				dao.persistir(aluno);
				addActionMessage("Aluno cadastrado com sucesso.");
			} catch (SQLException e) {
				addActionError("Erro ao cadastrar aluno.");
				return SUCCESS;
			}
		} else {
			try {
				dao.alterar(aluno);
				addActionMessage("Aluno alterado com sucesso.");
			} catch (SQLException e) {
				addActionError("Erro ao alterar aluno.");
				return SUCCESS;
			}
		}

		return SUCCESS;
	}
	
	public String deletar() {
		aluno = new Aluno();
		aluno.setId(getId());
		
		if (dao.inativar(aluno)) {
			addActionMessage("Aluno Inativado com sucesso.");
			return SUCCESS;
		} else {
				addActionError("Erro ao inativar o aluno.");
			return SUCCESS;
		}
	}
	
	public String formAlterar() {
		try {
			aluno = dao.consultarAluno(getId());
			isAlteracao = true;
		} catch (SQLException e) {
			addActionError("Erro ao consultar o aluno.");
			return SUCCESS;
		}
		return "cadastro";
	}
	
	public String cadastrar() {
		return "cadastro";
	}

	public String cancelar() {
		aluno = null;
		return "cancelar";
	}
	
	public String formListar() {
		return SUCCESS;
	}
	
	public String listar() {
		List<Criterio> criterios = new ArrayList<Criterio>();
		criterios.add(criterio);
		try {
			alunos = dao.listar(criterios);
		} catch (Exception e) {
			addActionError("Ocorreu um erro ao tentar executar a consulta!");
		}
		if(null != alunos && alunos.isEmpty()){
			addActionMessage("Nenhum registro foi encontrado.");
		}
		return SUCCESS;
	}

	public List<Filtro> getCamposFiltro() {
		//camposFiltro.add(new Filtro("matricula", "Matrícula"));
		camposFiltro.add(new Filtro("nome", "Nome"));
		return camposFiltro;
	}

	public void setCamposFiltro(List<Filtro> camposFiltro) {
		this.camposFiltro = camposFiltro;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
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

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}

}
