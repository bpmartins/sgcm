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
				
					addActionMessage("Relat�rio ainda n�o implementado nessa vers�o do sistema.");	
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
		        addActionMessage("Relat�rio Gerado com Sucesso! Favor selecionar o destino do arquivo.");
		} catch (Exception e) {
			if(e.getMessage().contains("C�digo do emprestimo inv�lido.")){
				addActionError("C�digo do emprestimo inv�lido.");
			}else if(e.getMessage().contains("C�digo do aluno inv�lido.")){
				addActionError("C�digo do aluno inv�lido.");
			}else{
				addActionError("Erro ao gerar o relat�rio entre em contato com o administrador.");
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
				throw new SQLException("C�digo do emprestimo inv�lido.");
			}
			
			return (List<T>) emprestimos;
		}else if(criterio.getCampo().equalsIgnoreCase("tFrequencia")){
			criteria.setValor(this.getId().toString());
			return (List<T>) daoTurma.listarRelatorioFrequencia(criteria);
		}else if(criterio.getCampo().equalsIgnoreCase("aHistorico")){
			List<AlunoVO> alunos = daoAluno.listarRelatorio(criteria);
			if(alunos.isEmpty()){
				throw new SQLException("C�digo do aluno inv�lido.");
			}
			return (List<T>) alunos;
		}else if(criterio.getCampo().equalsIgnoreCase("aTMatricula")){
			List<AlunoVO> alunos = daoAluno.listarRelatorio(criteria);
			if(alunos.isEmpty()){
				throw new SQLException("C�digo do aluno inv�lido.");
			}
			alunos.get(0).setTexto(this.getTexto());
			return (List<T>) alunos;
		}
		
		return null;	
	}

	private String getTexto() {
		return new StringBuffer()
		.append("O curso/oficina de ensino musical do CCACCM tem caracter�sticas pr�prias com metodologia din�mica e destina \na oferecer ao aluno no��es e aprendizagem de teoria musical e pr�tica de instrumentos gratuitamente conforme \nregulamento instituido por este centro.\n\n")
		.append("O curso/oficina segue regras e padr�es determinados por iniciativa pr�pria dentro de par�metros que trazem \ndesenvolvimento r�pido e l�gico para aprendizado precoce de conhecimento te�rico e pr�tico instrumental.\n\n")
		.append("O curso ser� dividido em tr�s etapa, sendo a primeira iniciante a segunda intermedi�ria e a terceira avan�ada. \n\n")
		.append("A primeira etapa tem como objetivo os conhecimentos b�sicos de teoria musical e introdu��o do aluno no �mbito \nda leitura musical e estimular o aprendizado atrav�s de inicia��o com instrumentos(flauta). \n\n")
		.append("A epata inicial tem dura��o de 6(seis) meses.\n\n")
		.append("A etapa inicial ter� 2(duas) avalia��oes de conhecimento (avalia��o moderada) sendo a primeira com 3(tr�s) meses \nde inicia��o do curso e a segunda com 6(seis) meses, comfinalidade de avalia��o do conhecimento e aptid�o do aluno a \npara determina��o de instrumento a ser executado futuramente (fase intermedi�ria).\n\n")
		.append("A segunda epata tem como objetivo, intensificar o conhecimento musical te�rico e introdu��o ao insino de execu��o \nde instrumentos profissionais bem como suas intensifica��es.\n\n")
		.append("A etapa intermedi�ria ter� dura��o de 6(seis) meses.\n\n")
		.append("A etapa intermedi�ria ter� avalia��es espor�dicas conforme determina��o do professor e datas pr� agendadas. \n")
		.append("A terceira etapa tem como objetivo situar o aluno em conhecimento amplo de teoria musical e participa��o ativa em \nbandas, conjuntos e fanfarras. \n\n")
		.append("A etapa avan�ada ter� dura��o de 6(seis) meses e ser� a conclus�o do curso/oficina.\n\n")
		.append("A frequ�ncia e primordial em todas as etapas do curso/oficina, com objetivo do aluno obter maior aproveitamento \nposs�vel fica estabelecido � toler�ncia de 1 falta m�s justificada e assinada pelo respons�vel, n�o sendo acumulativa. \n\n")
		.append("A disciplina e concentra��o s�o ferramentas essenciais no aprendizado musical, ficam determinadas atrav�s desde regulamento \nas faltas disciplinares como leves,m�dias e graves e suas respectivas puni��es como advert�ncia verbal ou escrita, suspen��o \npor tempo determinado e desligamento do curso/oficina.\n\n")
		.append("O aluno tem comprometimento com o CCACCM em participar ativamente nos eventos elaborados pela diretoria de eventos cujas datas \nser�o pr� determinadas e autorizadas pelo respons�vel, a n�o participa��o nos referidos eventos ou aprensenta��o dever�o ser \njustificados por escrito e assinado pelo respons�vel como anteced�ncia � diretoria musical, os eventos e apresenta��es servir�o \ncomo pontua��o em todas as etapas.\n\n")
		.append("O aluno tera que obter pontua��o m�nima de 50% de aproveitamento em cada mat�ria (te�rico/pr�tico) para a conclus�o do da oficina \nou progress�o de etapas.\n\n")
		.append("A frequ�ncia e disciplina s�o pontos chave para um bom resultado e obten��o de uma boa pontua��o, seja perseverante e conquiste \ntodos os seus objetivos,seja bem vindo ao curso/oficina Crescendo com M�sica.\n\n")
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
