package action;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.Aluno;
import model.Evento;
import model.Professor;
import model.Turma;
import model.Criterio;
import model.Filtro;

import com.opensymphony.xwork2.ActionSupport;

import dao.AlunoDAO;
import dao.ProfessorDAO;
import dao.TurmaDAO;

@Component
public class TurmaAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	@Autowired
	TurmaDAO dao = new TurmaDAO();
	private List<Filtro> camposFiltro = new ArrayList<Filtro>();
	private Turma turma = new Turma();
	private Criterio criterio;
	private Long id;
	private List<Turma> turmas;
	private List<Professor> professores;
	private List<String> alunos;
	private List<Aluno> alunosFrequencia;
	private String[] alunosSelecionados;
	private Date dataFrequencia;
	private boolean isAlteracao =  false;

	@Override
	public String execute() throws Exception {
		if (null == turma.getId()) {
			try {
				dao.persistir(turma);
				addActionMessage("Turma cadastrada com sucesso.");
			} catch (SQLException e) {
				addActionError("Erro ao cadastrar turma.");
				return SUCCESS;
			}
		} else {
			try {
				dao.alterar(turma);
				addActionMessage("Turma alterada com sucesso.");
			} catch (SQLException e) {
				addActionError("Erro ao alterar turma.");
				return SUCCESS;
			}
		}

		return SUCCESS;
	}
	
	public String deletar() {
		turma = new Turma();
		turma.setId(getId());

		if (dao.inativar(turma)) {
			addActionMessage("Turma Inativada com sucesso.");
			return SUCCESS;
		} else {
			addActionError("Erro ao inativar turma.");
			return SUCCESS;
		}
	}
	
	public String matricular() {
		this.convertArrayToList(turma);
		try {
			dao.matricular(turma);
			addActionMessage("Alunos Matrículados com sucesso.");
		} catch (SQLException e) {
			addActionError("Erro ao matricular os alunos.");
		}
		return SUCCESS;
	}
	
	public String formMatricular() {
		try {
			turma = dao.consultarTurma(getId());
			this.convertListToArray(turma);
			isAlteracao = true;
		} catch (SQLException e) {
			addActionError("Erro ao carregar o módulo de matricula.");
			return SUCCESS;
		}
		return "matricula";
	}
	
	public String frequencia() {
		if(dao.verifFrenqLancada(dataFrequencia, this.getId())){
			addActionError("Frenquência para a data já foi lançada.");
		}else{
			try {
				dao.lancarFrequencia(this.getId(), dataFrequencia, alunosSelecionados);
				addActionMessage("Frequência lançada com sucesso.");
			} catch (Exception e) {
				addActionError("Erro ao lançar Frequência");
				return SUCCESS;
			}
		}
		alunosFrequencia = new AlunoDAO().carregarAlunoFrequencia(this.getId());
		dataFrequencia = null;
		return "frequencia";
	}
	
	public String formFrequencia() {
		alunosFrequencia = new AlunoDAO().carregarAlunoFrequencia(this.getId());
		return "frequencia";
	}

	public String formAlterar() {
		try {
			ProfessorDAO daoProf = new ProfessorDAO();
			turma = dao.consultarTurma(getId());
			professores =  daoProf.listar(new ArrayList<Criterio>());
			isAlteracao = true;
		} catch (SQLException e) {
			addActionError("Erro ao carregar a turma.");
			return SUCCESS;
		}
		return "cadastro";
	}

	public String cadastrar() {
		ProfessorDAO daoProf = new ProfessorDAO();
		try {
			professores =  daoProf.listar(new ArrayList<Criterio>());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "cadastro";
	}

	public String cancelar(){
		return "cancelar";
	}
		
	public String formListar() {
		return SUCCESS;
	}
	
	public String listar() {
		List<Criterio> criterios = new ArrayList<Criterio>();
		criterios.add(criterio);
		try {
			turmas = dao.listar(criterios);
		} catch (Exception e) {
			addActionError("Ocorreu um erro ao tentar executar a consulta!");
		}
		if(null != turmas && turmas.isEmpty()){
			addActionMessage("Nenhum registro foi encontrado.");
		}
		return SUCCESS;
	}
	
	
	protected void convertArrayToList(Turma turna){
		Aluno aluno = new Aluno();
		for(String AlunostoAux : alunosSelecionados){
			String[] aux = AlunostoAux.split("-");
			aluno = new Aluno();
			aluno.setId(Long.valueOf(aux[0].trim()));
			//instrumento.setDescricao(aux[1].trim());
			turna.getAlunos().add(aluno);
		}
	}

	protected void convertListToArray(Turma turma){
		int index = 0;
		alunosSelecionados = new String[turma.getAlunos().size()];

		for(Aluno aluno : turma.getAlunos()){
			alunosSelecionados[index] = aluno.getId() + " - " + aluno.getNome();
			index++;
		}
	}
	
	
	public List<Filtro> getCamposFiltro() {
		camposFiltro.add(new Filtro("descricao", "Descrição"));
		return camposFiltro;
	}

	public void setCamposFiltro(List<Filtro> camposFiltro) {
		this.camposFiltro = camposFiltro;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
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

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
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

	public boolean isAlteracao() {
		return isAlteracao;
	}

	public void setAlteracao(boolean isAlteracao) {
		this.isAlteracao = isAlteracao;
	}

	public void setAlunosFrequencia(List<Aluno> alunosFrequencia) {
		this.alunosFrequencia = alunosFrequencia;
	}

	public List<Aluno> getAlunosFrequencia() {
		return alunosFrequencia;
	}

	public void setDataFrequencia(Date dataFrequencia) {
		this.dataFrequencia = dataFrequencia;
	}

	public Date getDataFrequencia() {
		return dataFrequencia;
	}

}
