package action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import model.Criterio;
import model.Filtro;
import model.Perfil;
import model.Pessoa;
import model.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import dao.PerfilDAO;
import dao.PessoaDAO;
import dao.UsuarioDAO;

/**
 * Classe controladora que faz  com que a camada de visualização posso usar os recursos presentes do modelo.
 * @author Bruno Martins
 *
 */
@Component
public class UsuarioAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	@Autowired
	UsuarioDAO dao = new UsuarioDAO();
	private List<Usuario> usuarios;
	private List<Filtro> camposFiltro = new ArrayList<Filtro>();
	private Usuario usuario;
	private Criterio criterio;
	private Long id;
	private  List<Perfil> perfis = new ArrayList<Perfil>();
	private  List<Pessoa> pessoas = new ArrayList<Pessoa>();

	@Override
	public String execute() throws Exception{
		
			if(null == usuario.getId()){
				try {
					dao.persistir(usuario);
					addActionMessage("Usuário cadastrado com sucesso.");
				} catch (SQLException e) {
					addActionError("Erro ao cadastrar usuário.");
					return SUCCESS;
				}
			}else{		
				try {
					dao.alterar(usuario);
					addActionMessage("Usuário alterado com sucesso.");
				} catch (SQLException e) {
					addActionError("Erro ao alterar usuário.");
					return SUCCESS;
				}
			}
			
			return SUCCESS;


	}
	

	public List<Usuario> getListUsuarios() throws SQLException {
		return null;
	}


	public String deletar() {
		usuario = new Usuario();
		usuario.setId(getId());
		
		if(usuario.getId().intValue() == 1){
			addActionMessage("Este Usuário não pode ser Inativado.");
			return SUCCESS;
		}
		
		if (dao.inativar(usuario)) {
			addActionMessage("Usuário Inativado com sucesso.");
			return SUCCESS;
		} else {
				addActionError("Erro ao inativar usuário.");
			return SUCCESS;
		}
	}
	
	public String formAlterar() {
		    try {
		    	perfis = new PerfilDAO().listarParaCombos();
		    	pessoas = new PessoaDAO().listarParaCombos();
				if(null != usuario && null != usuario.getId() && usuario.getId().intValue() == 1 || getId() == 1){
					addActionMessage("Este Usuário não pode ser modificado.");
					return SUCCESS;
				}
				usuario = dao.consultarUsuario(getId());
			} catch (SQLException e) {
				addActionError("Erro ao consultar o usuário.");
				return SUCCESS;
			}
			return "cadastro";
	}

	
	public String cancelar() {
		usuario = null;
		return "cancelar";
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String logar() {
		
	    Map session = ActionContext.getContext().getSession();
	    
		try {
			usuario = dao.logarUsuario(usuario.getLogin(), usuario.getSenha());
		} catch (SQLException e) {
			addActionError("Falha de Login");
			session.put("logged-in","false");
			session.put("usuario", null);
			return "login";
		}
			
		if(null == usuario){
			addActionError("Usuário Inválido!");
			session.put("logged-in","false");
			session.put("usuario", null);
			return "login";
		}
		
		if(usuario.getSituacao().equalsIgnoreCase("I")){
			addActionError("Usuário Inválido!");
			session.put("logged-in","false");
			session.put("usuario", null);
			return "login";
		}
		
		session.put("logged-in","true");
		session.put("usuario", usuario);
	
		return "index";
	}
	
	public String logOut() {
		
	    Map session = ActionContext.getContext().getSession();
		session.put("logged-in","false");
		session.put("usuario", null);
	
		return "login";
	}


	public String formListar() {
			return SUCCESS;
	}
	
	public String listar() {
		List<Criterio> criterios = new ArrayList<Criterio>();
		criterios.add(criterio);
		try {
			usuarios = dao.listar(criterios);
		} catch (Exception e) {
			addActionError("Ocorreu um erro ao tentar executar a consulta!");
		}
		if(null != usuarios && usuarios.isEmpty()){
			addActionMessage("Nenhum registro foi encontrado.");
		}
		return SUCCESS;
	}


	public String cadastrar() {
		perfis = new PerfilDAO().listarParaCombos();
		pessoas = new PessoaDAO().listarParaCombos();
		return "cadastro";
	}

	
	public String pesquisar() {
		return "pesquisar";
	}

	/**
	 * @return the usuarios
	 */
	
	public List<Usuario> getUsuarios() {
		if (usuarios == null)
			usuarios = new ArrayList<Usuario>();
		return usuarios;
	}

	/**
	 * @param usuarios
	 *            the usuarios to set
	 */

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	/**
	 * @return the usuario
	 */

	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario
	 *            the usuario to set
	 */

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the id
	 */

	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */

	public void setId(Long id) {
		this.id = id;
	}


	public void setCriterio(Criterio criterio) {
		this.criterio = criterio;
	}


	public Criterio getCriterio() {
		return criterio;
	}


	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}


	public List<Perfil> getPerfis() {
		return perfis;
	}


	public void setCamposFiltro(List<Filtro> camposFiltro) {
		this.camposFiltro = camposFiltro;
	}


	public List<Filtro> getCamposFiltro() {
		camposFiltro.add(new Filtro("codigo", "Código"));
		camposFiltro.add(new Filtro("login", "Login"));
		return camposFiltro;
	}
	

	public List<Pessoa> getPessoas() {
		return pessoas;
	}


	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

}
