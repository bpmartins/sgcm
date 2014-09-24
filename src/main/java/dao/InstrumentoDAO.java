package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Criterio;
import model.Instrumento;
import model.TipoInstrumento;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import util.ConexaoPostGree;

@Repository
@Transactional(rollbackFor={Exception.class})
public class InstrumentoDAO extends AbstractDAO{


	public void persistir(Instrumento i) throws SQLException {
		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		try {
			ps = con.prepareStatement("INSERT INTO instrumento(id, tipo, descricao, situacao) VALUES (NEXTVAL('sequence_instrumento'), ?, ?,'A')");
			ps.setLong(1, i.getTipoInstrumento().getCodigo());
			ps.setString(2,i.getDescricao());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			closeStatement( ps );
			closeConnection( con );
		}

	}

	public boolean inativar(Instrumento i) {

		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		try {
			ps = con.prepareStatement("UPDATE instrumento SET situacao = 'I'  WHERE id = ?");
			ps.setLong(1, i.getId());
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

	public void alterar(Instrumento i) throws SQLException {

		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		try {
			ps = con.prepareStatement("UPDATE instrumento SET  tipo=?, descricao=? WHERE id=?");
			ps.setLong(1, i.getTipoInstrumento().getCodigo());
			ps.setString(2, i.getDescricao());
			ps.setLong(3, i.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			closeStatement( ps );
			closeConnection( con );
		}

	}


	public List<Instrumento> listar(List<Criterio> criterios) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Instrumento> instrumentos = new ArrayList<Instrumento>();
		Instrumento instrumento = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		StringBuffer sql = new StringBuffer();

		try {
			sql.append("SELECT id, tipo, descricao, CASE WHEN situacao = 'A' THEN 'Ativo' WHEN situacao = 'I' THEN 'Inativo' END AS situacao FROM instrumento");
			if(!criterios.isEmpty()){
				if(criterios.get(0).getCampo().equalsIgnoreCase("Codigo") && null != criterios.get(0).getValor() && !criterios.get(0).getValor().equalsIgnoreCase("")){
					sql.append(" WHERE situacao ILIKE 'A' AND id = ?");
				}else if (criterios.get(0).getCampo().equalsIgnoreCase("Descricao")){
					String[] valor = criterios.get(0).getValor().split(",");
					sql.append(" WHERE situacao ILIKE 'A' AND descricao ILIKE '%" + valor[0] +"%'");
				}	else if(criterios.get(0).getCampo().equalsIgnoreCase("tipo") && null != criterios.get(0).getValor() && !criterios.get(0).getValor().equalsIgnoreCase("")){
					sql.append(" WHERE situacao ILIKE 'A' AND tipo = ?");
				}else{
					sql.append(" WHERE situacao ILIKE 'A'");
				}
			}else{
				sql.append(" WHERE situacao ILIKE 'A'");
			}
			ps = con.prepareStatement(sql.toString());

			if(!criterios.isEmpty()){
				if(criterios.get(0).getCampo().equalsIgnoreCase("Codigo") && null != criterios.get(0).getValor() && !criterios.get(0).getValor().equals("")){
					String[] valor = criterios.get(0).getValor().split(",");
					ps.setLong(1,Long.valueOf(valor[0]));
				} else if(criterios.get(0).getCampo().equalsIgnoreCase("tipo") && null != criterios.get(0).getValor() && !criterios.get(0).getValor().equals("")){
					ps.setString(1,criterios.get(0).getValor().trim().replace(",", "").trim());
				}

			}
			rs = ps.executeQuery();

			while(rs.next()){
				instrumento = new Instrumento();
				instrumento.setId(rs.getLong("id"));
				instrumento.setDescricao(rs.getString("descricao"));
				instrumento.setSituacao(rs.getString("situacao"));
				instrumento.setTipoInstrumento(TipoInstrumento.valueOf(rs.getInt("tipo")));
				instrumentos.add(instrumento);


			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
			closeConnection( con );
		}


		return instrumentos;

	}
	
	
	public List<String> carregarInstrumentosProf() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> instrumentos = new ArrayList<String>();
		Connection con = ConexaoPostGree.getConPostGree();	
		StringBuffer sql = new StringBuffer();

		try {
			sql.append("SELECT id, tipo, descricao, situacao FROM instrumento WHERE situacao <> 'I'");
			ps = con.prepareStatement(sql.toString());
			rs = ps.executeQuery();

			while(rs.next()){
				instrumentos.add(rs.getLong("id") + " - " + rs.getString("descricao"));
			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
			closeConnection( con );
		}
		return instrumentos;
	}

	public Instrumento consultarInstrumento(Long id) throws SQLException {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Instrumento instrumento = new Instrumento();

		String query = "SELECT id, tipo, descricao, situacao FROM instrumento WHERE id = ? ";

		try {
			connection = ConexaoPostGree.getConPostGree();
			ps = connection.prepareStatement( query );
			ps.setLong(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				instrumento.setId(rs.getLong("id"));
				instrumento.setDescricao(rs.getString("descricao"));
				instrumento.setSituacao(rs.getString("situacao"));
				instrumento.setTipoInstrumento(TipoInstrumento.valueOf(rs.getInt("tipo")));
				return instrumento;
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
	
	public List<Instrumento> listarParaEmprestimo() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Instrumento> instrumentos = new ArrayList<Instrumento>();
		Instrumento instrumento = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		StringBuffer sql = new StringBuffer();

		try {
			sql.append("SELECT i.id, i.tipo, i.descricao, i.situacao FROM instrumento i WHERE i.id NOT IN (SELECT e.id_instrumento FROM emprestimo AS e WHERE e.ind_baixa = 'N' )");
			sql.append(" AND i.situacao ILIKE 'A'");
			ps = con.prepareStatement(sql.toString());

			rs = ps.executeQuery();

			while(rs.next()){
				instrumento = new Instrumento();
				instrumento.setId(rs.getLong("id"));
				instrumento.setDescricao(rs.getString("descricao"));
				instrumento.setSituacao(rs.getString("situacao"));
				instrumento.setTipoInstrumento(TipoInstrumento.valueOf(rs.getInt("tipo")));
				instrumentos.add(instrumento);
			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
			closeConnection( con );
		}


		return instrumentos;

	}



}
