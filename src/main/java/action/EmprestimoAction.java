package action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import model.Emprestimo;
import model.Instrumento;
import model.Aluno;
import model.Criterio;
import model.Filtro;

import com.opensymphony.xwork2.ActionSupport;

import dao.InstrumentoDAO;
import dao.AlunoDAO;
import dao.EmprestimoDAO;

@Component
public class EmprestimoAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	@Autowired
	EmprestimoDAO dao = new EmprestimoDAO();
	private List<Filtro> camposFiltro = new ArrayList<Filtro>();
	private Emprestimo emprestimo = new Emprestimo();
	private Criterio criterio;
	private Long id;
	private List<Emprestimo> emprestimos;
	private List<Instrumento> instrumentos;
	private List<Aluno> alunos;
	private boolean isAlteracao =  false;
	
	
	@Override
	@SkipValidation
	public String execute() throws Exception {
		if (null == emprestimo.getId()) {
			try {
				dao.persistir(emprestimo);
				addActionMessage("Empréstimo cadastrado com sucesso.");
			} catch (SQLException e) {
				addActionError("Erro ao cadastrar empréstimo.");
				return SUCCESS;
			}
		} else {
			try {
				dao.alterar(emprestimo);
				addActionMessage("Empréstimo alterada com sucesso.");
			} catch (SQLException e) {
				addActionError("Erro ao alterar empréstimo.");
				return SUCCESS;
			}
		}

		return SUCCESS;
	}
	
	public String baixar() throws Exception {
		if (null == this.getId()) {
			addActionMessage("Não foi selecionado nenhum empréstimo para dar baixa.");
		} else {
			try {
				emprestimo.setId(this.getId());
				dao.baixar(emprestimo);
				addActionMessage("Baixa do empréstimo realizada com sucesso.");
			} catch (SQLException e) {
				addActionError("Erro ao baixar o empréstimo.");
				return SUCCESS;
			}
		}

		return SUCCESS;
	}

	public String formAlterar() {
		try {
			InstrumentoDAO daoInstrumento = new InstrumentoDAO();
			AlunoDAO daoAluno = new AlunoDAO();
			emprestimo = dao.consultarEmprestimo(getId());
			if(emprestimo.getIndBaixa().equalsIgnoreCase("Sim")){
				addActionError("Este Empréstimos já foi baixado e não pode ser alterado.");
				return SUCCESS;
			}
			instrumentos =  daoInstrumento.listarParaEmprestimo();
			alunos =  daoAluno.listarParaEmprestimo();
			instrumentos.add(emprestimo.getInstrumento());
			alunos.add(emprestimo.getAluno());
			isAlteracao = true;
		} catch (SQLException e) {
			addActionError("Erro ao carregar a empréstimo.");
			return SUCCESS;
		}
		return "cadastro";
	}

	public String cadastrar() {
		InstrumentoDAO daoInstrumento = new InstrumentoDAO();
		AlunoDAO daoAluno = new AlunoDAO();
		try {
			instrumentos =  daoInstrumento.listarParaEmprestimo();
			alunos =  daoAluno.listarParaEmprestimo();
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
			emprestimos = dao.listar(criterios);
		} catch (Exception e) {
			addActionError("Ocorreu um erro ao tentar executar a consulta!");
		}
		if(null != emprestimos && emprestimos.isEmpty()){
			addActionMessage("Nenhum registro foi encontrado.");
		}
		return SUCCESS;
	}
	
	
	public List<Filtro> getCamposFiltro() {
		camposFiltro.add(new Filtro("aluno", "Aluno"));
		camposFiltro.add(new Filtro("instrumento", "Instrumento"));
		return camposFiltro;
	}

	public void setCamposFiltro(List<Filtro> camposFiltro) {
		this.camposFiltro = camposFiltro;
	}

	public Emprestimo getEmprestimo() {
		return emprestimo;
	}

	public void setEmprestimo(Emprestimo emprestimo) {
		this.emprestimo = emprestimo;
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

	public List<Emprestimo> getEmprestimos() {
		return emprestimos;
	}

	public void setEmprestimos(List<Emprestimo> emprestimos) {
		this.emprestimos = emprestimos;
	}

	public List<Instrumento> getInstrumentos() {
		return instrumentos;
	}

	public void setInstrumentos(List<Instrumento> instrumentos) {
		this.instrumentos = instrumentos;
	}
	
	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}

}
