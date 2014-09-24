package action;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import dao.AlunoDAO;
import dao.EmprestimoDAO;
import dao.EventoDAO;
import dao.ProfessorDAO;
import dao.TurmaDAO;

import model.Criterio;
import model.Turma;
import model.relatorios.AlunoVO;
import model.relatorios.EmprestimoVO;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class RelatorioAction extends ActionSupport {
	
	@Autowired
	ProfessorDAO daoProf = new ProfessorDAO();
	@Autowired
	TurmaDAO daoTurma = new TurmaDAO();
	@Autowired
	EmprestimoDAO daoEmprestimo = new EmprestimoDAO();
	@Autowired
	EventoDAO daoEvento = new EventoDAO();
	@Autowired
	AlunoDAO daoAluno = new AlunoDAO();
	private Criterio criterio;
	private Long id;
	private List<Turma> turmas;
	public String formListar() {
		return SUCCESS;
	}
	
	
	public String gerar(){
		try {
				String caminhoRelatorio = "";
				String nomeRelatorio = "relatorioSGCM";
				if(null != this.getId()){
					criterio = new Criterio();
					criterio.setCampo("tFrequencia");
				}
				
				if(criterio.getCampo().equalsIgnoreCase("professores")){
					caminhoRelatorio = ServletActionContext.getServletContext().getRealPath("/relatorios/professores.jasper");
					nomeRelatorio = "professores";
				}else if(criterio.getCampo().equalsIgnoreCase("aMatriculados")){
					caminhoRelatorio = ServletActionContext.getServletContext().getRealPath("/relatorios/alunos_matriculados.jasper");
					nomeRelatorio = "alunos_matriculados";
				}else if(criterio.getCampo().equalsIgnoreCase("iEmprestados")){
					caminhoRelatorio = ServletActionContext.getServletContext().getRealPath("/relatorios/instrumentos_emprestados.jasper");
					nomeRelatorio = "instrumentos_emprestados";
				}else if(criterio.getCampo().equalsIgnoreCase("eExternos")){
					caminhoRelatorio = ServletActionContext.getServletContext().getRealPath("/relatorios/eventos_externos.jasper");
					nomeRelatorio = "eventos_externos";
				}else if(criterio.getCampo().equalsIgnoreCase("eTermo")){
					caminhoRelatorio = ServletActionContext.getServletContext().getRealPath("/relatorios/termo_emprestimo.jasper");
					nomeRelatorio = "termo_emprestimo";
				}else if(criterio.getCampo().equalsIgnoreCase("tFrequencia")){
					if(null == this.getId() || this.getId().intValue() == 0 ){
						List<Criterio> criterios = new ArrayList<Criterio>();
						criterios.add(criterio);
						turmas = daoTurma.listar(criterios);
						return SUCCESS;
					}else{
						caminhoRelatorio = ServletActionContext.getServletContext().getRealPath("/relatorios/frequencia_lancada.jasper");
						nomeRelatorio = "frequencias_lancadas";
					}
				}else if(criterio.getCampo().equalsIgnoreCase("aHistorico")){
					caminhoRelatorio = ServletActionContext.getServletContext().getRealPath("/relatorios/historico_aluno.jasper");
					nomeRelatorio = "historico_aluno";
				}else if(criterio.getCampo().equalsIgnoreCase("aTMatricula")){
					caminhoRelatorio = ServletActionContext.getServletContext().getRealPath("/relatorios/regulamento.jasper");
					nomeRelatorio = "regulamento";
				}else{
				
					addActionMessage("Relatório ainda não implementado nessa versão do sistema.");	
					return SUCCESS;
				}
				JasperPrint jasperPrint = this.getJasperRelatorio(ServletActionContext.getRequest(),  
						caminhoRelatorio,
						nomeRelatorio,
						 criterio);
				
				ByteArrayOutputStream output = new ByteArrayOutputStream();   
		        JasperExportManager.exportReportToPdfStream(jasperPrint, output);   
		        ServletActionContext.getResponse().setContentType("application/pdf");   
		        ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=\" " + nomeRelatorio + ".pdf\" ");   
		        ServletActionContext.getResponse().setContentLength(output.size());   

				ServletOutputStream servletOutputStream = ServletActionContext.getResponse().getOutputStream();
		        servletOutputStream.write(output.toByteArray(), 0, output.size());   
		        try {
					servletOutputStream.flush();   
					servletOutputStream.close();
				} catch (Exception e) {
				}
		        addActionMessage("Relatório Gerado com Sucesso! Favor selecionar o destino do arquivo.");
		} catch (Exception e) {
			if(e.getMessage().contains("Código do emprestimo inválido.")){
				addActionError("Código do emprestimo inválido.");
			}else if(e.getMessage().contains("Código do aluno inválido.")){
				addActionError("Código do aluno inválido.");
			}else{
				addActionError("Erro ao gerar o relatório entre em contato com o administrador.");
			}
			
			
		} 
		return SUCCESS;
	}
	
	
	/**
	 *  Retorna o  JasperPrint para gerar a carta.
	 * @param request
	 * @param urlLogo
	 * @param urlLogoComplementar
	 * @param caminhoJasper
	 * @param nomeArquivo
	 * @param subsidioVO
	 * @return
	 * @throws Exception 
	 * @throws IntegrationException
	 * @throws LexException
	 */
	public JasperPrint getJasperRelatorio(HttpServletRequest request, String caminhoJasper, String nomeArquivo,Criterio criteria) throws Exception{
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		JRBeanCollectionDataSource ds;
		try {
			ds = new JRBeanCollectionDataSource(this.getDadosRelatorio(criteria));
		} catch (SQLException e) {
			throw new Exception(e);
		}

		
		JasperReport jasper = (JasperReport) JRLoader.loadObject(caminhoJasper);
				
		return JasperFillManager.fillReport(jasper, parametros, ds);
	}
	
	/**
	 * 
	 * Metodo getDadosRelatorio
	 * @param subsidioVO
	 * @param usuarioLogado
	 * @param session
	 * @param request
	 * @return DocumentoVO
	 * @throws SQLException 
	 * @throws LexException 
	 * @throws IntegrationException 
	 */
	@SuppressWarnings("unchecked")
	private <T extends Object> List<T> getDadosRelatorio(Criterio criteria) throws SQLException {
		
		if(criteria.getCampo().equalsIgnoreCase("professores")){
			return (List<T>) daoProf.listarRelatorio();
		}else if(criterio.getCampo().equalsIgnoreCase("aMatriculados")){
			return (List<T>) daoTurma.listarRelatorio();
		}else if(criterio.getCampo().equalsIgnoreCase("iEmprestados")){
			return (List<T>) daoEmprestimo.listarRelatorio(criteria);
		}else if(criterio.getCampo().equalsIgnoreCase("eExternos")){
			return (List<T>) daoEvento.listarRelatorio();
		}else if(criterio.getCampo().equalsIgnoreCase("eTermo")){
			
			List<EmprestimoVO> emprestimos = daoEmprestimo.listarRelatorio(criteria);
			if(emprestimos.isEmpty()){
				throw new SQLException("Código do emprestimo inválido.");
			}
			
			return (List<T>) emprestimos;
		}else if(criterio.getCampo().equalsIgnoreCase("tFrequencia")){
			criteria.setValor(this.getId().toString());
			return (List<T>) daoTurma.listarRelatorioFrequencia(criteria);
		}else if(criterio.getCampo().equalsIgnoreCase("aHistorico")){
			List<AlunoVO> alunos = daoAluno.listarRelatorio(criteria);
			if(alunos.isEmpty()){
				throw new SQLException("Código do aluno inválido.");
			}
			return (List<T>) alunos;
		}else if(criterio.getCampo().equalsIgnoreCase("aTMatricula")){
			List<AlunoVO> alunos = daoAluno.listarRelatorio(criteria);
			if(alunos.isEmpty()){
				throw new SQLException("Código do aluno inválido.");
			}
			alunos.get(0).setTexto(this.getTexto());
			return (List<T>) alunos;
		}
		
		return null;	
	}

	private String getTexto() {
		return new StringBuffer()
		.append("O curso/oficina de ensino musical do CCACCM tem características próprias com metodologia dinâmica e destina \na oferecer ao aluno noções e aprendizagem de teoria musical e prática de instrumentos gratuitamente conforme \nregulamento instituido por este centro.\n\n")
		.append("O curso/oficina segue regras e padrões determinados por iniciativa própria dentro de parâmetros que trazem \ndesenvolvimento rápido e lógico para aprendizado precoce de conhecimento teórico e prático instrumental.\n\n")
		.append("O curso será dividido em três etapa, sendo a primeira iniciante a segunda intermediária e a terceira avançada. \n\n")
		.append("A primeira etapa tem como objetivo os conhecimentos básicos de teoria musical e introdução do aluno no âmbito \nda leitura musical e estimular o aprendizado através de iniciação com instrumentos(flauta). \n\n")
		.append("A epata inicial tem duração de 6(seis) meses.\n\n")
		.append("A etapa inicial terá 2(duas) avaliaçãoes de conhecimento (avaliação moderada) sendo a primeira com 3(três) meses \nde iniciação do curso e a segunda com 6(seis) meses, comfinalidade de avaliação do conhecimento e aptidão do aluno a \npara determinação de instrumento a ser executado futuramente (fase intermediária).\n\n")
		.append("A segunda epata tem como objetivo, intensificar o conhecimento musical teórico e introdução ao insino de execução \nde instrumentos profissionais bem como suas intensificações.\n\n")
		.append("A etapa intermediária terá duração de 6(seis) meses.\n\n")
		.append("A etapa intermediária terá avaliações esporádicas conforme determinação do professor e datas pré agendadas. \n")
		.append("A terceira etapa tem como objetivo situar o aluno em conhecimento amplo de teoria musical e participação ativa em \nbandas, conjuntos e fanfarras. \n\n")
		.append("A etapa avançada terá duração de 6(seis) meses e será a conclusão do curso/oficina.\n\n")
		.append("A frequência e primordial em todas as etapas do curso/oficina, com objetivo do aluno obter maior aproveitamento \npossível fica estabelecido à tolerância de 1 falta mês justificada e assinada pelo responsável, não sendo acumulativa. \n\n")
		.append("A disciplina e concentração são ferramentas essenciais no aprendizado musical, ficam determinadas através desde regulamento \nas faltas disciplinares como leves,médias e graves e suas respectivas punições como advertência verbal ou escrita, suspenção \npor tempo determinado e desligamento do curso/oficina.\n\n")
		.append("O aluno tem comprometimento com o CCACCM em participar ativamente nos eventos elaborados pela diretoria de eventos cujas datas \nserão pré determinadas e autorizadas pelo responsável, a não participação nos referidos eventos ou aprensentação deverão ser \njustificados por escrito e assinado pelo responsável como antecedência à diretoria musical, os eventos e apresentações servirão \ncomo pontuação em todas as etapas.\n\n")
		.append("O aluno tera que obter pontuação mínima de 50% de aproveitamento em cada matéria (teórico/prático) para a conclusão do da oficina \nou progressão de etapas.\n\n")
		.append("A frequência e disciplina são pontos chave para um bom resultado e obtenção de uma boa pontuação, seja perseverante e conquiste \ntodos os seus objetivos,seja bem vindo ao curso/oficina Crescendo com Música.\n\n")
		.append("\n\n\n")
		.toString();
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


}
