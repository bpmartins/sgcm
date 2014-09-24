package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.Aluno;
import model.Instrumento;
import model.Turma;
import model.Criterio;
import model.relatorios.TurmaVO;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import util.ConexaoPostGree;

@Repository
@Transactional(rollbackFor={Exception.class})
public class TurmaDAO extends AbstractDAO{
	

	public List<Turma> listar(List<Criterio> criterios) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Turma> turmas = new ArrayList<Turma>();
		Turma turma = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		StringBuffer sql = new StringBuffer();

		try {
			sql.append("SELECT id, descricao, data_inicio,data_fim, CASE WHEN situacao = 'A' THEN 'Ativo' WHEN situacao = 'I' THEN 'Inativo' END AS situacao FROM turma");
			if(!criterios.isEmpty()){
				if(criterios.get(0).getCampo().equalsIgnoreCase("Codigo") && null != criterios.get(0).getValor() && !criterios.get(0).getValor().equalsIgnoreCase("")){
					sql.append(" WHERE situacao ILIKE 'A' AND id = ?");
				}else if (criterios.get(0).getCampo().equalsIgnoreCase("Descricao")){
					String[] valor = criterios.get(0).getValor().split(",");
					sql.append(" WHERE situacao ILIKE 'A' AND descricao ILIKE '%" + valor[0] +"%'");
				}else if (criterios.get(0).getCampo().equalsIgnoreCase("tFrequencia")){
					sql.append(" ");
				}else{
					sql.append(" WHERE situacao ILIKE 'A'");
				}
			}else{
				sql.append(" WHERE situacao ILIKE 'A'");
			}

			ps = con.prepareStatement(sql.toString());

			if(!criterios.isEmpty()){
				if(criterios.get(0).getCampo().equalsIgnoreCase("Codigo") && null != criterios.get(0).getValor() && !criterios.get(0).getValor().equals("")){
					ps.setLong(1,Long.valueOf(criterios.get(0).getValor()));
				} else if(criterios.get(0).getCampo().equalsIgnoreCase("descricao") && null != criterios.get(0).getValor() && !criterios.get(0).getValor().equals("")){
					ps.setString(1,criterios.get(0).getValor().trim().replace(",", "").trim());
				}
			}
			rs = ps.executeQuery();

			while(rs.next()){
				turma = new Turma();
				turma.setId(rs.getLong("id"));
				turma.setDescricao(rs.getString("descricao"));
				turma.setDataInicio(rs.getDate("data_inicio"));
				turma.setDataFim(rs.getDate("data_fim"));
				turma.setSituacao(rs.getString("situacao"));
				
				turmas.add(turma);


			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
			closeConnection( con );
		}

		return turmas;
	}
	
	public void persistir(Turma i) throws SQLException {
		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();
		
		try {
			ps = con.prepareStatement("INSERT INTO turma(id, descricao, data_inicio, data_fim, situacao,id_professor) "
					+ "VALUES (NEXTVAL('sequence_turma'), ?, ?, ?,'A',?)");
			ps.setString(1, i.getDescricao());
			ps.setDate(2, i.getDataInicio());
			ps.setDate(3, i.getDataFim());
			ps.setLong(4, i.getProfessor().getId());
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
	
	
	
	private Long getId() throws SQLException{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT nextval('sequence_matricula')";

		try {
			connection = ConexaoPostGree.getConPostGree();
			ps = connection.prepareStatement( query );
			rs = ps.executeQuery();
			if(rs.next()){
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
		}
		return null;
	}
	
	
	public void matricular(Turma t) throws SQLException {
		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();
		
		try {
			
			ps = con.prepareStatement("DELETE FROM matricula WHERE id_turma = " + t.getId());
			ps.executeUpdate();
			
			ps = con.prepareStatement("INSERT INTO matricula( id, id_aluno, id_turma, data_matricula) "
					+ "VALUES (?, ?, ?, ?)");
			
			for(Aluno a : t.getAlunos()){
				ps.setLong(1, this.getId());
				ps.setLong(2, a.getId());
				ps.setLong(3, t.getId());
				ps.setDate(4, new Date(new java.util.Date().getTime()));
				ps.addBatch();
			}
			ps.executeBatch();		
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			closeStatement( ps );
			closeConnection( con );
		}

	}
	
	public boolean inativar(Turma i) {
		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();
		try{
			ps = con.prepareStatement("UPDATE turma SET situacao ='I' WHERE id = ?");
			ps.setLong(1, i.getId());
			ps.executeUpdate();
		}
		catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			return false;
		} finally {
			closeStatement(ps);
			closeConnection(con);
		}
		return true;
	}
	
	public void alterar(Turma i) throws SQLException {

		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();

		try {
			ps = con.prepareStatement("UPDATE turma SET descricao=?, data_inicio=?, data_fim=?,id_professor=?  WHERE id=?");
			ps.setString(1, i.getDescricao());
			ps.setDate(2, i.getDataFim());
			ps.setDate(3, i.getDataFim());
			ps.setLong(4, i.getProfessor().getId());
			ps.setLong(5,i.getId() );
			ps.executeUpdate();

			ps.executeBatch();
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			removerRegistrosErro(i.getId());
			throw new SQLException(e.getMessage());
		} finally {
			closeStatement( ps );
			closeConnection( con );
		}

	}

	public Turma consultarTurma(Long id) throws SQLException {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Turma turma = new Turma();

		String query = "SELECT id, descricao, data_inicio, data_fim, situacao,id_professor"
				+ " FROM turma WHERE id = ?";

		try {
			connection = ConexaoPostGree.getConPostGree();
			ps = connection.prepareStatement( query );
			ps.setLong(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				turma.setId(id);
				turma.setDescricao(rs.getString("descricao"));	
				turma.setDataInicio(rs.getDate("data_inicio"));
				turma.setDataFim(rs.getDate("data_fim"));
				turma.setSituacao(rs.getString("situacao"));
				turma.getProfessor().setId(rs.getLong("id_professor"));
			}
			
			query = "SELECT p.id , p.nome FROM matricula m INNER JOIN pessoa p ON p.id = m.id_aluno WHERE m.id_turma = ?";
			ps = connection.prepareStatement( query );
			ps.setLong(1, id);
			rs = ps.executeQuery();
			Aluno a;
			while(rs.next()){
				a = new Aluno();
				a.setId(rs.getLong("id"));
				a.setNome(rs.getString("nome"));
				turma.getAlunos().add(a);
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
			closeConnection( connection );
		}
		return turma;
	}
	
	protected void removerRegistrosErro(Long id){

		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();

		try {
			ps = con.prepareStatement("DELETE FROM turma WHERE id = " + id);
			ps.executeUpdate();	
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeStatement( ps );
			closeConnection( con );
		}

	}
	
	
	public boolean verifFrenqLancada(Date data, Long codTurma){
		
		
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT data_ausencia FROM frequencia WHERE data_ausencia = ? AND id_turma = ?";

		try {
			connection = ConexaoPostGree.getConPostGree();
			ps = connection.prepareStatement( query );
			ps.setDate(1, data);
			ps.setLong(2, codTurma);
			rs = ps.executeQuery();
			if(rs.next()){
				return true;
			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeStatement( ps );
			closeConnection( connection );
		}

		return false;
		
	}
	
	public void lancarFrequencia(Long idTurma,Date data , String[] alunos) throws SQLException {
		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();
		
		try {
			ps = con.prepareStatement("INSERT INTO frequencia(id_aluno, id_turma, data_ausencia) VALUES (?, ?, ?)");
			for(int i=0;i<alunos.length;i++){
				ps.setLong(1,Long.valueOf(alunos[i]));
				ps.setLong(2,idTurma);
				ps.setDate(3,data);
				ps.addBatch();
			}
			ps.executeBatch();
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			closeStatement( ps );
			closeConnection( con );
		}

	}
	
	
	public List<TurmaVO> listarRelatorio() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TurmaVO> turmas = new ArrayList<TurmaVO>();
		List<Turma> turmasAux = new ArrayList<Turma>();
		TurmaVO turma = null;
		Turma turmaAux = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		StringBuffer sql = new StringBuffer();

		try {
			
			//
			sql.append("SELECT id,descricao,id_professor FROM turma WHERE situacao ILIKE 'A'");
			ps = con.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				turmaAux = new Turma();
				turmaAux.setId(rs.getLong("id"));
				turmaAux.setDescricao(rs.getString("descricao"));
				turmaAux.getProfessor().setId(rs.getLong("id_professor"));	
				turmasAux.add(turmaAux);
			}
			
			List<Turma> turmasAux2 = new ArrayList<Turma>();
			
			for(Turma t : turmasAux){
				sql =  new StringBuffer();
				sql.append("SELECT nome FROM pessoa WHERE id = ?");
				ps = con.prepareStatement(sql.toString());
				ps.setLong(1, t.getProfessor().getId());
				rs = ps.executeQuery();
				
				while(rs.next()){
					t.getProfessor().setNome(rs.getString("nome"));
				}
				turmasAux2.add(t);
			}
			
			
			for(Turma t : turmasAux2){
				sql =  new StringBuffer();
				sql.append("SELECT p.id,p.nome FROM matricula AS m INNER JOIN pessoa AS p ON p.id = m.id_aluno WHERE m.id_turma = ?");
				ps = con.prepareStatement(sql.toString());
				ps.setLong(1, t.getId());
				rs = ps.executeQuery();
				
				turma = new TurmaVO();
				turma.setTurma(t.getId() + " - " + t.getDescricao());
				turma.setProfessor(t.getProfessor().getId() + " - " + t.getProfessor().getNome());
				while(rs.next()){
					
					if(turma.getAlunos() == null){
						turma.setAlunos(rs.getLong("id") + " - " + rs.getString("nome") + "\n");
					}else{
						turma.setAlunos(turma.getAlunos() + rs.getLong("id") + " - " + rs.getString("nome") + "\n");
					}
				}
				turmas.add(turma);
			}

		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
			closeConnection( con );
		}

		return turmas;
	}
	
	
	
	public List<TurmaVO> listarRelatorioFrequencia(Criterio criterio) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TurmaVO> turmas = new ArrayList<TurmaVO>();
		TurmaVO turma = new TurmaVO();
		Connection con = ConexaoPostGree.getConPostGree();	
		StringBuffer sql = new StringBuffer();

		try {
			
			//
			sql.append("SELECT id,descricao,id_professor FROM turma WHERE id = ?");
			ps = con.prepareStatement(sql.toString());
			ps.setLong(1, Long.valueOf(criterio.getValor()));
			rs = ps.executeQuery();
			
			while(rs.next()){
				turma.setTurma(rs.getString("descricao"));
			}
			
			sql =  new StringBuffer();
			sql.append("SELECT nome , data_ausencia FROM frequencia INNER JOIN pessoa ON id_aluno = id  WHERE id_turma = ?");
			ps = con.prepareStatement(sql.toString());
			ps.setLong(1, Long.valueOf(criterio.getValor()));
			rs = ps.executeQuery();
				
			while(rs.next()){
				if(null == turma.getFrequencias()){
					turma.setFrequencias("");
				}
				turma.setFrequencias(turma.getFrequencias() + rs.getString("nome") + " - " +  new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("data_ausencia")) + "\n" );
			}
			turmas.add(turma);
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
			closeConnection( con );
		}

		return turmas;
	}
}
