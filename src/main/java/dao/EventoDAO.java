package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.Aluno;
import model.Criterio;
import model.Evento;
import model.Instrumento;
import model.Professor;
import model.Usuario;
import model.relatorios.EventoVO;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import util.ConexaoPostGree;

@Repository
@Transactional(rollbackFor={Exception.class})
public class EventoDAO extends AbstractDAO{

	
public List<Evento> listar(List<Criterio> criterios) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Evento> eventos = new ArrayList<Evento>();
		Evento evento = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		StringBuffer sql = new StringBuffer();
		
		try {
			sql.append("SELECT e.id, p.nome AS professor, e.nome, e.local, e.data, e.hora FROM evento AS e INNER JOIN pessoa AS p ON p.id = e.id_professor");
			if(!criterios.isEmpty()){
				if(criterios.get(0).getCampo().equalsIgnoreCase("Codigo") && null != criterios.get(0).getValor() && !criterios.get(0).getValor().equalsIgnoreCase("")){
					sql.append(" WHERE e.id = ?");
				}else if (criterios.get(0).getCampo().equalsIgnoreCase("nome")){
					sql.append(" WHERE e.nome ILIKE '%" + criterios.get(0).getValor() +"%'");
				}
			}
			
			sql.append(" ORDER BY e.id");
			
			ps = con.prepareStatement(sql.toString());
			
			if(!criterios.isEmpty()){
				if(criterios.get(0).getCampo().equalsIgnoreCase("Codigo") && null != criterios.get(0).getValor() && !criterios.get(0).getValor().equals("")){
					ps.setLong(1,Long.valueOf(criterios.get(0).getValor()));
				}
			}
			rs = ps.executeQuery();
			
			while(rs.next()){			
					evento = new Evento();
					evento.setId(rs.getLong("id"));
					evento.setNome(rs.getString("nome"));
					evento.setLocal(rs.getString("local"));
					evento.setHora(rs.getString("hora"));
					evento.setData(rs.getDate("data"));
					evento.getProfessor().setNome(rs.getString("professor"));
					//user.getPerfil().setId(rs.getLong("id_perfil"));
					//user.getPerfil().setDescricao(rs.getString("descricao"));
					eventos.add(evento);
			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
            closeConnection( con );
        }
		
		
		return eventos;

	}

	public void alterar(Evento e) throws SQLException {
		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();
	
		try {	
	
			ps = con.prepareStatement("UPDATE evento SET id_professor=?, nome=?, local=?, data=?, hora=? WHERE id=?");
	
			ps.setLong(1, e.getProfessor().getId());
			ps.setString(2, e.getNome());
			ps.setString(3, e.getLocal());
			ps.setDate(4, e.getData());
			ps.setString(5, e.getHora());
			ps.setLong(6,e.getId());
			ps.executeUpdate();
			
			ps = con.prepareStatement("DELETE FROM evento_aluno WHERE id_evento = " + e.getId());
			ps.executeUpdate();
	
			ps = con.prepareStatement("INSERT INTO evento_aluno(id_aluno, id_evento)VALUES (?, ?)");
			for(Aluno a : e.getAlunos()){
				ps.setLong(1, a.getId());
				ps.setLong(2, e.getId());
				ps.addBatch();
			}
	
			ps.executeBatch();
		} catch (SQLException ex) {
			Logger.getRootLogger().error(ex.getMessage());
			removerRegistrosErro(e.getId());
			throw new SQLException(ex.getMessage());
		} finally {
			closeStatement( ps );
			closeConnection( con );
		}
	}

	public void persistir(Evento e) throws SQLException {
		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();
	
		try {
			
			ResultSet rs = null;

			String query = "SELECT nextval('sequence_evento')";

			try {
				con = ConexaoPostGree.getConPostGree();
				ps = con.prepareStatement( query );
				rs = ps.executeQuery();
				if(rs.next()){
					e.setId( rs.getLong(1));
				}
			} catch (SQLException ex) {
				throw new SQLException(ex);
			} finally {
				closeResultSet( rs );
				closeStatement( ps );
			}
			
	
			ps = con.prepareStatement("INSERT INTO evento(id, id_professor, nome, local, data, hora) VALUES (?, ?, ?, ?, ?, ?)");
	
			ps.setLong(1,e.getId());
			ps.setLong(2, e.getProfessor().getId());
			ps.setString(3, e.getNome());
			ps.setString(4, e.getLocal());
			ps.setDate(5, e.getData());
			ps.setString(6, e.getHora());
			ps.executeUpdate();
	
			ps = con.prepareStatement("INSERT INTO evento_aluno(id_aluno, id_evento)VALUES (?, ?)");
			for(Aluno a : e.getAlunos()){
				ps.setLong(1, a.getId());
				ps.setLong(2, e.getId());
				ps.addBatch();
			}
	
			ps.executeBatch();
		} catch (SQLException ex) {
			Logger.getRootLogger().error(ex.getMessage());
			removerRegistrosErro(e.getId());
			throw new SQLException(ex.getMessage());
		} finally {
			closeStatement( ps );
			closeConnection( con );
		}
	}
	
	
	public Evento consultarEvento(Long id) throws SQLException {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Evento evento = new Evento();

		String query = "SELECT id, id_professor, nome, local, data, hora FROM evento WHERE id = ?";

		try {
			connection = ConexaoPostGree.getConPostGree();
			ps = connection.prepareStatement( query );
			ps.setLong(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				evento.setId(id);
				evento.setNome(rs.getString("nome"));
				evento.getProfessor().setId(rs.getLong("id_professor"));
				evento.setLocal(rs.getString("local"));
				evento.setData(rs.getDate("data"));
				evento.setHora(rs.getString("hora"));

			}

			ps = connection.prepareStatement( "SELECT id, nome FROM pessoa p INNER JOIN evento_aluno ap ON p.id = ap.id_aluno AND ap.id_evento =  ?" );
			ps.setLong(1, id);
			rs = ps.executeQuery();
			Aluno a ;
			List<Aluno> aux = new ArrayList<Aluno>();
			while(rs.next()){
				a = new Aluno();
				a.setId(rs.getLong("id"));
				a.setNome(rs.getString("nome"));
				aux.add(a);
			}
			
		evento.getAlunos().addAll(aux);	

		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
			closeConnection( connection );
		}
		return evento;
	}

	public boolean remover(Evento evento) {
	
		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		try {
			ps = con.prepareStatement("DELETE FROM evento_aluno WHERE id_evento = ?");
			ps.setLong(1, evento.getId());
			ps.executeUpdate();
			
			ps = con.prepareStatement("DELETE FROM evento WHERE id = ?");
			ps.setLong(1, evento.getId());
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
	
	protected void removerRegistrosErro(Long id){

		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();

		try {
			ps = con.prepareStatement("DELETE FROM evento WHERE id = " + id);
			ps.executeUpdate();	
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeStatement( ps );
			closeConnection( con );
		}

	}
	
	
public List<EventoVO> listarRelatorio() {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<EventoVO> eventos = new ArrayList<EventoVO>();
		EventoVO evento = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		StringBuffer sql = new StringBuffer();
		
		try {
			sql.append("SELECT e.id, p.nome AS professor, e.nome, e.local, e.data, e.hora FROM evento AS e INNER JOIN pessoa AS p ON p.id = e.id_professor");
			sql.append(" ORDER BY e.id");
			
			ps = con.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){			
					evento = new EventoVO();
					evento.setId(rs.getString("id"));
					evento.setNome(rs.getString("nome"));
					evento.setLocal(rs.getString("local"));
					evento.setData(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("data")));
					evento.setResponsavel(rs.getString("professor"));
					eventos.add(evento);
			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
            closeConnection( con );
        }
		
		
		return eventos;

	}
}
