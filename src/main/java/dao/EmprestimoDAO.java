package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.Emprestimo;
import model.Criterio;
import model.relatorios.EmprestimoVO;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import util.ConexaoPostGree;

@Repository
@Transactional(rollbackFor={Exception.class})
public class EmprestimoDAO extends AbstractDAO{
	

	public List<Emprestimo> listar(List<Criterio> criterios) throws SQLException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Emprestimo> emprestimos = new ArrayList<Emprestimo>();
		Emprestimo emprestimo = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		StringBuffer sql = new StringBuffer();

		try {
			sql.append("SELECT e.id, i.descricao, aluno.nome, e.data_inicio, e.data_fim, CASE WHEN e.ind_baixa = 'N' THEN 'Não' WHEN e.ind_baixa = 'S' THEN 'Sim' END AS ind_baixa FROM emprestimo e INNER JOIN instrumento i ON e.id_instrumento = i.id INNER JOIN pessoa aluno ON e.id_aluno = aluno.id");
			if(!criterios.isEmpty()){
				if(criterios.get(0).getCampo().equalsIgnoreCase("Aluno") && null != criterios.get(0).getValor() && !criterios.get(0).getValor().equalsIgnoreCase("")){
					String[] valor = criterios.get(0).getValor().split(",");
					sql.append(" WHERE e.ind_baixa ILIKE 'N'AND aluno.nome ILIKE '%" + valor[0] + "%'");
				}else if (criterios.get(0).getCampo().equalsIgnoreCase("Instrumento")){
					String[] valor = criterios.get(0).getValor().split(",");
					sql.append(" WHERE e.ind_baixa ILIKE 'N' AND i.descricao ILIKE '%" + valor[0] +"%'");
				}else{
					sql.append(" WHERE e.ind_baixa ILIKE 'N'");
				}
			}

			ps = con.prepareStatement(sql.toString());
			rs = ps.executeQuery();

			while(rs.next()){
				emprestimo = new Emprestimo();
				emprestimo.setId(rs.getLong("id"));
				emprestimo.getInstrumento().setDescricao(rs.getString("descricao"));
				emprestimo.getAluno().setNome(rs.getString("nome"));
				emprestimo.setDataInicio(rs.getDate("data_inicio"));
				emprestimo.setDataFim(rs.getDate("data_fim"));
				emprestimo.setIndBaixa(rs.getString("ind_baixa"));
				
				emprestimos.add(emprestimo);


			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			throw new SQLException(e);
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
			closeConnection( con );
		}

		return emprestimos;
	}
	
	public void persistir(Emprestimo i) throws SQLException {
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		Connection con = ConexaoPostGree.getConPostGree();

		try{

				ps = con.prepareStatement("INSERT INTO emprestimo(id, id_aluno, id_instrumento, data_inicio, data_fim, observacoes, ind_baixa) "
						+ "VALUES (NEXTVAL('sequence_emprestimo'), ?, ?, ?, ?, ?, 'N')");
				ps.setLong(1, i.getAluno().getId());
				ps.setLong(2, i.getInstrumento().getId());
				ps.setDate(3, i.getDataInicio());
				ps.setDate(4, i.getDataFim());
				ps.setString(5, i.getObservacoes());
				ps.executeUpdate();
				
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			removerRegistrosErro(i.getId());
			throw new SQLException(e.getMessage());
		} finally {
			closeStatement( ps );
			closeStatement(ps2);
			closeConnection( con );
		}
		
	}
	
	public void alterar(Emprestimo i) throws SQLException {

		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();

		try {
			ps = con.prepareStatement("UPDATE emprestimo SET id_aluno=?,id_instrumento = ?,data_inicio=?, data_fim=? WHERE id=?");
			ps.setLong(1, i.getAluno().getId());
			ps.setLong(2, i.getInstrumento().getId());
			ps.setDate(3, i.getDataInicio());
			ps.setDate(4, i.getDataFim());
			ps.setLong(5,i.getId());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			removerRegistrosErro(i.getId());
			throw new SQLException(e.getMessage());
		} finally {
			closeStatement( ps );
			closeConnection( con );
		}

	}

	public void baixar(Emprestimo i) throws SQLException {
		
		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();

		try {
			ps = con.prepareStatement("UPDATE emprestimo SET ind_baixa = 'S' WHERE id=?");
			ps.setLong(1, i.getId());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			removerRegistrosErro(i.getId());
			throw new SQLException(e.getMessage());
		} finally {
			closeStatement( ps );
			closeConnection( con );
		}
	}
	
	public Emprestimo consultarEmprestimo(Long id) throws SQLException {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Emprestimo emprestimo = new Emprestimo();

		String query = "SELECT i.descricao, aluno.nome, e.id_instrumento, e.id_aluno, e.data_inicio, e.data_fim, e.observacoes, CASE WHEN e.ind_baixa = 'N' THEN 'Não' WHEN e.ind_baixa = 'S' THEN 'Sim' END AS ind_baixa FROM emprestimo e INNER JOIN instrumento i ON e.id_instrumento = i.id INNER JOIN pessoa aluno ON e.id_aluno = aluno.id"
				+ " WHERE e.id = ?";

		try {
			connection = ConexaoPostGree.getConPostGree();
			ps = connection.prepareStatement( query );
			ps.setLong(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				emprestimo.setId(id);
				emprestimo.getInstrumento().setId(rs.getLong("id_instrumento"));
				emprestimo.getAluno().setId(rs.getLong("id_aluno"));
				emprestimo.getInstrumento().setDescricao(rs.getString("descricao"));	
				emprestimo.getAluno().setNome(rs.getString("nome"));
				emprestimo.setDataInicio(rs.getDate("data_inicio"));
				emprestimo.setDataFim(rs.getDate("data_fim"));
				emprestimo.setObservacoes(rs.getString("observacoes"));
				emprestimo.setIndBaixa(rs.getString("ind_baixa"));
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
			closeConnection( connection );
		}
		return emprestimo;
	}
	
	protected void removerRegistrosErro(Long id) throws SQLException{

		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();

		try {
			ps = con.prepareStatement("DELETE FROM emprestimo WHERE id = " + id);
			ps.executeUpdate();	
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			throw new SQLException(e);
		} finally {
			closeStatement( ps );
			closeConnection( con );
		}

	}
	
	
	public List<EmprestimoVO> listarRelatorio(Criterio criterio) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<EmprestimoVO> emprestimos = new ArrayList<EmprestimoVO>();
		EmprestimoVO emprestimo = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		StringBuffer sql = new StringBuffer();

		try {
			sql.append("SELECT e.id, i.descricao,e.id_aluno, e.id_instrumento, aluno.nome,e.observacoes, e.data_inicio, e.data_fim, CASE WHEN e.ind_baixa = 'N' THEN 'Não' WHEN e.ind_baixa = 'S' THEN 'Sim' END AS ind_baixa FROM emprestimo e INNER JOIN instrumento i ON e.id_instrumento = i.id INNER JOIN pessoa aluno ON e.id_aluno = aluno.id");
			
			if (!criterio.getValor().equalsIgnoreCase("")){
				sql.append(" WHERE i.id = " + criterio.getValor());
			}else{
				sql.append(" WHERE e.ind_baixa ILIKE 'N'");
			}

			ps = con.prepareStatement(sql.toString());
			rs = ps.executeQuery();

			while(rs.next()){
				emprestimo = new EmprestimoVO();
				emprestimo.setId(rs.getString("id"));
				emprestimo.setCodInstrumento(rs.getString("id_instrumento"));
				emprestimo.setInstrumento(rs.getString("descricao"));
				emprestimo.setCodAluno(rs.getString("id_aluno"));
				emprestimo.setAluno(rs.getString("nome"));
				emprestimo.setDataInicio(new SimpleDateFormat("dd/MM/yyyy").format( rs.getDate("data_inicio")));
				emprestimo.setDataFim(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("data_fim")));	
				emprestimo.setObservacoes(rs.getString("observacoes"));
				emprestimos.add(emprestimo);
			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
			closeConnection( con );
		}
		return emprestimos;
	}
}
