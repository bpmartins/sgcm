package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Criterio;
import model.Perfil;
import model.Usuario;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import util.ConexaoPostGree;

@Repository
@Transactional(rollbackFor={Exception.class})
public class PerfilDAO extends AbstractDAO{
	
	public List<Perfil> listarParaCombos() {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Perfil> perfis = new ArrayList<Perfil>();
		Perfil perfil = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		
		try {
			ps = con.prepareStatement("SELECT id, descricao, situacao FROM perfil WHERE situacao <> 'I'");
			rs = ps.executeQuery();
			
			while(rs.next()){
				perfil = new Perfil();
				perfil.setId(rs.getLong("id"));
				perfil.setDescricao(rs.getString("descricao"));
				perfil.setSituacao(rs.getString("situacao"));
				perfis.add(perfil);
			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
            closeConnection( con );
        }	
		return perfis;
	}
	

	public List<Perfil> listar(List<Criterio> criterios) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Perfil> perfis = new ArrayList<Perfil>();
		Perfil perfil = null;
		Connection con = ConexaoPostGree.getConPostGree();
		StringBuffer sql = new StringBuffer();
		
		try {
			
			sql.append("SELECT id, descricao, CASE WHEN situacao = 'A' THEN 'Ativo' WHEN situacao = 'I' THEN 'Inativo' END AS situacao FROM perfil");
			if(!criterios.isEmpty()){
				if(criterios.get(0).getCampo().equalsIgnoreCase("Codigo") && null != criterios.get(0).getValor() && !criterios.get(0).getValor().equalsIgnoreCase("")){
					sql.append(" WHERE situacao ILIKE 'A' AND id = ?");
				}else if (criterios.get(0).getCampo().equalsIgnoreCase("Descricao")){
					sql.append(" WHERE situacao ILIKE 'A' AND descricao ILIKE '%" + criterios.get(0).getValor() +"%'");
				}else{
					sql.append(" WHERE situacao ILIKE 'A'");
				}
			}else{
				sql.append(" WHERE situacao ILIKE 'A'");
			}
			ps = con.prepareStatement(sql.toString());
			
			if(!criterios.isEmpty()){
				if(criterios.get(0).getCampo().equalsIgnoreCase("Codigo") && null != criterios.get(0).getValor() && !criterios.get(0).getValor().equalsIgnoreCase("")){
					ps.setLong(1,Long.valueOf(criterios.get(0).getValor()));
				}
			}
			
			rs = ps.executeQuery();
			
			while(rs.next()){
				perfil = new Perfil();
				perfil.setId(rs.getLong("id"));
				perfil.setDescricao(rs.getString("descricao"));
				perfil.setSituacao(rs.getString("situacao"));
				perfis.add(perfil);
			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
            closeConnection( con );
        }	
		return perfis;
	}
	
	public void persistir(Perfil p) throws SQLException {
		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		try {
			ps = con.prepareStatement("INSERT INTO perfil(id, descricao, situacao) VALUES (NEXTVAL('sequence_perfil'),?,'A')");
			ps.setString(1, p.getDescricao());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			closeStatement( ps );
            closeConnection( con );
        }
		
	}
	
	public boolean inativar(Perfil p) {

		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		try {
			ps = con.prepareStatement("UPDATE perfil SET situacao = 'I'  WHERE id = ?");
			ps.setLong(1, p.getId());
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
	
	public void alterar(Perfil p) throws SQLException {

		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		try {
			ps = con.prepareStatement("UPDATE perfil SET descricao=? WHERE id=?");
			ps.setString(1, p.getDescricao());
			ps.setLong(2, p.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			closeStatement( ps );
            closeConnection( con );
        }

	}
	
	public Perfil consultarPerfil(Long id) throws SQLException {
		
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Perfil profile = new Perfil();
	        
		String query = "SELECT id, descricao, situacao FROM perfil WHERE id = ? ";
	        
		try {
			connection = ConexaoPostGree.getConPostGree();
			ps = connection.prepareStatement( query );
			ps.setLong(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				profile.setId(rs.getLong("id"));
				profile.setDescricao(rs.getString("descricao"));
				profile.setSituacao(rs.getString("situacao"));
				return profile;
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
