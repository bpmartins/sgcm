/**
 * 
 */
package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import model.Criterio;
import model.Usuario;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import util.ConexaoPostGree;


/**
 * Classe dao da entidade Usuario
 * @author Bruno
 * 
 */
@Repository
@Transactional(rollbackFor={Exception.class})
public class UsuarioDAO extends AbstractDAO{

	
	public void persistir(Usuario u) throws SQLException {
		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		try {
			ps = con.prepareStatement("INSERT INTO usuario( id, id_perfil, login, senha, id_pessoa, situacao) VALUES (NEXTVAL('sequence_usuario'),?,?,?,?,'A')");
			ps.setLong(1, u.getPerfil().getId());
			ps.setString(2,u.getLogin());
			ps.setString(3, u.getSenha());
			if(null != u.getPessoa().getId() && u.getPessoa().getId().intValue() != -1){
				ps.setLong(4, u.getPessoa().getId());
			}else{
				ps.setNull(4,Types.BIGINT );
			}
			
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			closeStatement( ps );
            closeConnection( con );
        }
		
	}

	public boolean inativar(Usuario u) {

		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		try {
			ps = con.prepareStatement("UPDATE usuario SET situacao = 'I'  WHERE id = ?");
			ps.setLong(1, u.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			return false;
		} finally {
			closeStatement( ps );
            closeConnection( con );
        }
		return true;

	}
	
	public void alterar(Usuario u) throws SQLException {

		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		try {
			ps = con.prepareStatement("UPDATE usuario SET id_perfil=?, login=?, senha=?, id_pessoa=? WHERE id=?");
			ps.setLong(1, u.getPerfil().getId());
			ps.setString(2, u.getLogin());
			ps.setString(3, u.getSenha());
			if(null != u.getPessoa().getId() && u.getPessoa().getId().intValue() != -1){
				ps.setLong(4, u.getPessoa().getId());
			}else{
				ps.setNull(4,Types.BIGINT );
			}
			ps.setLong(5, u.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			closeStatement( ps );
            closeConnection( con );
        }

	}


	public List<Usuario> listar(List<Criterio> criterios) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Usuario> users = new ArrayList<Usuario>();
		Usuario user = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		StringBuffer sql = new StringBuffer();
		
		try {
			sql.append("SELECT u.id, u.id_perfil, u.login, u.senha, CASE WHEN u.situacao = 'A' THEN 'Ativo' WHEN u.situacao = 'I' THEN 'Inativo' END AS situacao, p.descricao FROM usuario AS u INNER JOIN perfil AS p ON u.id_perfil = p.id");
			if(!criterios.isEmpty()){
				if(criterios.get(0).getCampo().equalsIgnoreCase("Codigo") && null != criterios.get(0).getValor() && !criterios.get(0).getValor().equalsIgnoreCase("")){
					sql.append(" WHERE p.situacao ILIKE 'A' AND p.id = ?");
				}else if (criterios.get(0).getCampo().equalsIgnoreCase("Login")){
					sql.append(" WHERE p.situacao ILIKE 'A' AND login LIKE '%" + criterios.get(0).getValor() +"%'");
				}else{
					sql.append(" WHERE p.situacao ILIKE 'A'");
				}
			}else{
				sql.append(" WHERE p.situacao ILIKE 'A'");
			}
			ps = con.prepareStatement(sql.toString());
			
			if(!criterios.isEmpty()){
				if(criterios.get(0).getCampo().equalsIgnoreCase("Codigo") && null != criterios.get(0).getValor() && !criterios.get(0).getValor().equals("")){
					ps.setLong(1,Long.valueOf(criterios.get(0).getValor()));
				}
			}
			rs = ps.executeQuery();
			
			while(rs.next()){			
				if(rs.getLong("id") != 1){
					user = new Usuario();
					user.setId(rs.getLong("id"));
					user.setLogin(rs.getString("login"));
					user.setSenha(rs.getString("senha"));
					user.setSituacao(rs.getString("situacao"));
					user.getPerfil().setId(rs.getLong("id_perfil"));
					user.getPerfil().setDescricao(rs.getString("descricao"));
					users.add(user);
				}

			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
            closeConnection( con );
        }
		
		
		return users;

	}
		
	public Usuario consultarUsuario(Long id) throws SQLException {
			
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Usuario user = new Usuario();
	        
		String query = "SELECT id, id_perfil, id_pessoa, login, senha, situacao FROM usuario WHERE id = ? ";
	        
		try {
			connection = ConexaoPostGree.getConPostGree();
			ps = connection.prepareStatement( query );
			ps.setLong(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				user.setId(rs.getLong("id"));
				user.setLogin(rs.getString("login"));
				user.setSenha(rs.getString("senha"));
				user.setSituacao(rs.getString("situacao"));
				user.getPerfil().setId(rs.getLong("id_perfil"));
				user.getPessoa().setId(rs.getLong("id_pessoa"));
				return user;
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
            closeConnection( connection );
        }
	return null;
	}

	public Usuario logarUsuario(String login,String senha) throws SQLException {
		
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Usuario user = new Usuario();
	        
		String query = "SELECT id, id_perfil, login, senha, situacao FROM usuario WHERE login = ? AND senha = ?";
	        
		try {
			connection = ConexaoPostGree.getConPostGree();
			ps = connection.prepareStatement( query );
			ps.setString(1, login);
			ps.setString(2, senha);
			rs = ps.executeQuery();
			if(rs.next()){
				user.setId(rs.getLong("id"));
				user.setLogin(rs.getString("login"));
				user.setSenha(rs.getString("senha"));
				user.setSituacao(rs.getString("situacao"));
				user.getPerfil().setId(rs.getLong("id_perfil"));
				return user;
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
            closeConnection( connection );
        }
	return null;
	}

}
