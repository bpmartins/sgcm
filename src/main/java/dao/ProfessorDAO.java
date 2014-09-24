package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.Criterio;
import model.Instrumento;
import model.Professor;
import model.relatorios.ProfessorVO;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import util.ConexaoPostGree;

@Repository
@Transactional(rollbackFor={Exception.class})
public class ProfessorDAO extends PessoaDAO{


	public List<Professor> listar(List<Criterio> criterios) throws SQLException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Professor> professores = new ArrayList<Professor>();
		Professor professor = null;
		Connection con = ConexaoPostGree.getConPostGree();
		StringBuffer sql = new StringBuffer();

		try {

			sql.append("SELECT id, nome, matricula, CASE WHEN situacao = 'A' THEN 'Ativo' WHEN situacao = 'I' THEN 'Inativo' END AS situacao"
					+ " FROM pessoa p INNER JOIN professor po ON p.id = po.id_pessoa");
			if(!criterios.isEmpty()){
				if(criterios.get(0).getCampo().equalsIgnoreCase("matricula") && null != criterios.get(0).getValor() && !criterios.get(0).getValor().equalsIgnoreCase("")){
					sql.append(" WHERE situacao ILIKE 'A' AND matricula LIKE '%" + criterios.get(0).getValor() +"%'");
				}else if (criterios.get(0).getCampo().equalsIgnoreCase("nome")){
					sql.append(" WHERE situacao ILIKE 'A' AND nome ILIKE '%" + criterios.get(0).getValor() +"%'");
				}else{
					sql.append(" WHERE situacao ILIKE 'A'");
				}
			}else{
				sql.append(" WHERE situacao ILIKE 'A'");
			}
			ps = con.prepareStatement(sql.toString());

			rs = ps.executeQuery();

			while(rs.next()){
				professor = new Professor();
				professor.setId(rs.getLong("id"));
				professor.setNome(rs.getString("nome"));
				professor.setMatricula(rs.getString("matricula"));
				professor.setSituacao(rs.getString("situacao"));
				professores.add(professor);
			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
			closeConnection( con );
		}	
		return professores;
	}

	public void persistir(Professor p) throws SQLException {
		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();

		try {

			p.setId(this.getId());
			ps = con.prepareStatement("INSERT INTO pessoa(id, nome, telefone, data_nascimento, situacao, "
					+ "logradouro, numero, bairro, cep, complemento) "
					+ "VALUES (?, ?, ?, ?,'A', ?, ?, ?, ?, ?)");

			ps.setLong(1,p.getId());
			ps.setString(2, p.getNome());
			ps.setString(3, p.getTelefone());
			ps.setDate(4, p.getDataNascimento());
			ps.setString(5, p.getLogradouro());
			ps.setString(6, p.getNumero());
			ps.setString(7, p.getBairro());
			ps.setString(8, p.getCep().trim());
			ps.setString(9, p.getComplemento());
			ps.executeUpdate();

			ps = con.prepareStatement("INSERT INTO professor(id_pessoa, matricula) " + "VALUES (?, ?)");
			ps.setLong(1, p.getId());
			ps.setString(2, p.getMatricula());
			ps.executeUpdate();

			ps = con.prepareStatement("INSERT INTO instrumento_professor(id_instrumento, id_professor)VALUES (?, ?)");
			for(Instrumento i : p.getInstrumentos()){
				ps.setLong(1, i.getId());
				ps.setLong(2, p.getId());
				ps.addBatch();
			}

			ps.executeBatch();
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			removerRegistrosErro(p.getId());
			throw new SQLException(e.getMessage());
		} finally {
			closeStatement( ps );
			closeConnection( con );
		}
	}

	public boolean inativar(Professor p) {

		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		try {
			ps = con.prepareStatement("UPDATE pessoa SET situacao = 'I'  WHERE id = ?");
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

	public void alterar(Professor p) throws SQLException {

		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();


		try {
			ps = con.prepareStatement("UPDATE pessoa SET nome=?, telefone=?, data_nascimento=?, logradouro=?, numero=?, bairro=?, cep=?, complemento=? WHERE id=?");

			ps.setString(1, p.getNome());
			ps.setString(2, p.getTelefone());
			ps.setDate(3, p.getDataNascimento());
			ps.setString(4, p.getLogradouro());
			ps.setString(5, p.getNumero());
			ps.setString(6, p.getBairro());
			ps.setString(7, p.getCep().trim());
			ps.setString(8, p.getComplemento());
			ps.setLong(9,p.getId() );
			ps.executeUpdate();

			ps = con.prepareStatement("UPDATE professor SET matricula=? WHERE id_pessoa=?");
			ps.setString(1, p.getMatricula());
			ps.setLong(2, p.getId());
			ps.executeUpdate();

			ps = con.prepareStatement("DELETE FROM instrumento_professor WHERE id_professor = " + p.getId());
			ps.executeUpdate();

			ps = con.prepareStatement("INSERT INTO instrumento_professor(id_instrumento, id_professor) VALUES (?, ?)");
			for(Instrumento i : p.getInstrumentos()){
				ps.setLong(1, i.getId());
				ps.setLong(2, p.getId());
				ps.addBatch();
			}

			ps.executeBatch();
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			removerRegistrosErro(p.getId());
			throw new SQLException(e.getMessage());
		} finally {
			closeStatement( ps );
			closeConnection( con );
		}

	}

	public Professor consultarProfessor(Long id) throws SQLException {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Professor professor = new Professor();

		String query = "SELECT pe.id, nome, telefone, situacao, data_nascimento, logradouro, numero, bairro, cep, complemento, matricula "
				+ "FROM pessoa pe INNER JOIN professor po ON pe.id = po.id_pessoa WHERE pe.id = ?";

		try {
			connection = ConexaoPostGree.getConPostGree();
			ps = connection.prepareStatement( query );
			ps.setLong(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				professor.setId(id);
				professor.setNome(rs.getString("nome"));
				professor.setDataNascimento(rs.getDate("data_nascimento"));
				professor.setTelefone(rs.getString("telefone"));
				professor.setLogradouro(rs.getString("logradouro"));
				professor.setSituacao(rs.getString("situacao"));
				professor.setNumero(rs.getString("numero"));
				professor.setBairro(rs.getString("bairro"));
				professor.setCep(rs.getString("cep"));
				professor.setComplemento(rs.getString("complemento"));
				professor.setMatricula(rs.getString("matricula"));
			}

			ps = connection.prepareStatement( "SELECT id, descricao FROM instrumento i INNER JOIN instrumento_professor ip ON i.id = ip.id_instrumento AND ip.id_professor =  ?" );
			ps.setLong(1, id);
			rs = ps.executeQuery();
			Instrumento i ;
			List<Instrumento> aux = new ArrayList<Instrumento>();
			while(rs.next()){
				i = new Instrumento();
				i.setId(rs.getLong("id"));
				i.setDescricao(rs.getString("descricao"));
				aux.add(i);
			}
			
		professor.getInstrumentos().addAll(aux);	

		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
			closeConnection( connection );
		}
		return professor;
	}

	protected void removerRegistrosErro(Long id){

		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();

		try {
			ps = con.prepareStatement("DELETE FROM pessoa WHERE id = " + id);
			ps.executeUpdate();	
			ps = con.prepareStatement("DELETE FROM professor WHERE id_pessoa = " + id);
			ps.executeUpdate();	
			ps = con.prepareStatement("DELETE FROM instrumento_professor WHERE id_professor = " + id);
			ps.executeUpdate();	
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeStatement( ps );
			closeConnection( con );
		}

	}
	
	
	public List<ProfessorVO> listarRelatorio() throws SQLException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ProfessorVO> professores = new ArrayList<ProfessorVO>();
		ProfessorVO professor = null;
		Connection con = ConexaoPostGree.getConPostGree();
		StringBuffer sql = new StringBuffer();

		try {

			sql.append("SELECT id, nome, matricula,telefone,data_nascimento, CASE WHEN situacao = 'A' THEN 'Ativo' WHEN situacao = 'I' THEN 'Inativo' END AS situacao"
					+ " FROM pessoa p INNER JOIN professor po ON p.id = po.id_pessoa");
			ps = con.prepareStatement(sql.toString());

			rs = ps.executeQuery();

			while(rs.next()){
				professor = new ProfessorVO();
				professor.setId(rs.getString("id"));
				professor.setNome(rs.getString("nome"));
				professor.setTelefone(rs.getString("telefone"));
				professor.setSituacao(rs.getString("situacao"));
				professor.setDataNascimento(new SimpleDateFormat("dd/mm/yyyy").format(rs.getDate("data_nascimento")));
				professores.add(professor);
			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
			closeConnection( con );
		}	
		return professores;
	}


}
