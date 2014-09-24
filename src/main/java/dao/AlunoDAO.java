package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.Aluno;
import model.Criterio;
import model.relatorios.AlunoVO;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import util.ConexaoPostGree;

@Repository
@Transactional(rollbackFor={Exception.class})
public class AlunoDAO extends PessoaDAO{
	

	public List<Aluno> listar(List<Criterio> criterios) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Aluno> alunos = new ArrayList<Aluno>();
		Aluno aluno = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		StringBuffer sql = new StringBuffer();
		
		try {
			sql.append("SELECT p.id, nome, data_nascimento, CASE WHEN situacao = 'A' THEN 'Ativo' "
					+ "WHEN situacao = 'I' THEN 'Inativo' END AS situacao FROM pessoa p INNER JOIN aluno a ON a.id_pessoa = p.id");
			if(!criterios.isEmpty()){
				if(criterios.get(0).getCampo().equalsIgnoreCase("nome") && null != criterios.get(0).getValor() && !criterios.get(0).getValor().equalsIgnoreCase("")){
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
				aluno = new Aluno();
				aluno.setId(rs.getLong("id"));
				aluno.setNome(rs.getString("nome"));
				aluno.setSituacao(rs.getString("situacao"));
				aluno.setDataNascimento(rs.getDate("data_nascimento"));
				
				alunos.add(aluno);		

			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
            closeConnection( con );
        }
		
		
		return alunos;

	}
	
	public void persistir(Aluno i) throws SQLException {
		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();
		
		ResultSet rs = null;
		String query = "SELECT nextval('sequence_pessoa')";
		
		try {
			ps = con.prepareStatement( query );
			rs = ps.executeQuery();
			
			while(rs.next()){
				i.setId(rs.getLong("nextval"));
			}
			
			ps = con.prepareStatement("INSERT INTO pessoa(id, nome, telefone, data_nascimento, situacao, logradouro, numero, bairro, cep, complemento) "
					+ "VALUES (?, ?, ?, ?,'A', ?, ?, ?, ?, ?)");
			ps.setLong(1,i.getId() );
			ps.setString(2, i.getNome());
			ps.setString(3, i.getTelefone());
			ps.setDate(4, i.getDataNascimento());
			ps.setString(5, i.getLogradouro());
			ps.setString(6, i.getNumero());
			ps.setString(7, i.getBairro());
			ps.setString(8, i.getCep().trim());
			ps.setString(9, i.getComplemento());
			ps.executeUpdate();
			
			ps = con.prepareStatement("INSERT INTO aluno(id_pessoa, nome_pai, nome_mae, sexo, nome_deficiencia, nome_medicamento, "
					+ "experiencia_musical, nome_escola, tempo_experiencia, instrum_aprendido, preferencia, sair_sozinho) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setLong(1, i.getId());
			ps.setString(2, i.getNomePai());
			ps.setString(3, i.getNomeMae());
			ps.setString(4, i.getSexo());
			ps.setString(5, i.getNomeDeficiencia());
			ps.setString(6, i.getNomeMedicamento());
			ps.setString(7, i.getExperienciaMusical());
			ps.setString(8, i.getNomeEscola());
			if(null != i.getTempoExperiencia() ){
				ps.setInt(9, i.getTempoExperiencia());
			}else{
				ps.setNull(9, Types.BIGINT);
			}
			
			ps.setString(10, i.getInstrumentoAprendido());
			ps.setString(11, i.getPreferencia());
			ps.setBoolean(12, i.getSairSozinho());
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
	
	public boolean inativar(Aluno i) {
		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();
		try{
			ps = con.prepareStatement("UPDATE pessoa SET situacao ='I' WHERE id = ?");
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
	
	public void alterar(Aluno i) throws SQLException {

		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();

		try {
			ps = con.prepareStatement("UPDATE pessoa SET nome=?, telefone=?, data_nascimento=?, logradouro=?, numero=?, bairro=?, cep=?, complemento=? WHERE id=?");
			ps.setString(1, i.getNome());
			ps.setString(2, i.getTelefone());
			ps.setDate(3, i.getDataNascimento());
			ps.setString(4, i.getLogradouro());
			ps.setString(5, i.getNumero());
			ps.setString(6, i.getBairro());
			ps.setString(7, i.getCep().trim());
			ps.setString(8, i.getComplemento());
			ps.setLong(9,i.getId() );
			ps.executeUpdate();

			ps = con.prepareStatement("UPDATE aluno SET nome_pai = ?, nome_mae = ?, sexo = ?, nome_deficiencia = ?, nome_medicamento = ?, "
					+ "experiencia_musical = ?, nome_escola = ?, tempo_experiencia = ?, instrum_aprendido = ?, preferencia =?, sair_sozinho = ?"
					+ " WHERE id_pessoa = ?");
			ps.setString(1, i.getNomePai());
			ps.setString(2, i.getNomeMae());
			ps.setString(3, i.getSexo());
			ps.setString(4, i.getNomeDeficiencia());
			ps.setString(5, i.getNomeMedicamento());
			ps.setString(6, i.getExperienciaMusical());
			ps.setString(7, i.getNomeEscola());
			if(null != i.getTempoExperiencia() ){
				ps.setInt(8, i.getTempoExperiencia());
			}else{
				ps.setNull(9, Types.BIGINT);
			}
			ps.setString(9, i.getInstrumentoAprendido());
			ps.setString(10, i.getPreferencia());
			ps.setBoolean(11, i.getSairSozinho());
			ps.setLong(12, i.getId());
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

	public Aluno consultarAluno(Long id) throws SQLException {

		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Aluno aluno = new Aluno();

		String query = "SELECT pe.id, nome, telefone, situacao, data_nascimento, logradouro, numero, bairro, cep, complemento, "
				+ "nome_pai, nome_mae, sexo, nome_deficiencia, nome_medicamento, experiencia_musical, nome_escola, "
				+ "tempo_experiencia, instrum_aprendido, preferencia, sair_sozinho"
				+ " FROM pessoa pe INNER JOIN aluno a ON pe.id = a.id_pessoa WHERE pe.id = ?";

		try {
			connection = ConexaoPostGree.getConPostGree();
			ps = connection.prepareStatement( query );
			ps.setLong(1, id);
			rs = ps.executeQuery();
			if(rs.next()){
				aluno.setId(id);
				aluno.setNome(rs.getString("nome"));	
				aluno.setDataNascimento(rs.getDate("data_nascimento"));
				aluno.setTelefone(rs.getString("telefone"));
				aluno.setLogradouro(rs.getString("logradouro"));
				aluno.setSituacao(rs.getString("situacao"));
				aluno.setNumero(rs.getString("numero"));
				aluno.setBairro(rs.getString("bairro"));
				aluno.setCep(rs.getString("cep"));
				aluno.setComplemento(rs.getString("complemento"));
				
				aluno.setNomePai(rs.getString("nome_pai"));
				aluno.setNomeMae(rs.getString("nome_mae"));
				aluno.setSexo(rs.getString("sexo"));
				aluno.setNomeDeficiencia(rs.getString("nome_deficiencia"));
				aluno.setNomeMedicamento(rs.getString("nome_medicamento"));
				aluno.setExperienciaMusical(rs.getString("experiencia_musical"));
				aluno.setNomeEscola(rs.getString("nome_escola"));
				aluno.setTempoExperiencia(rs.getInt("tempo_experiencia"));
				aluno.setInstrumentoAprendido(rs.getString("instrum_aprendido"));
				aluno.setPreferencia(rs.getString("preferencia"));
				aluno.setSairSozinho(rs.getBoolean("sair_sozinho"));
			}

		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
			closeConnection( connection );
		}
		return aluno;
	}
	
	protected void removerRegistrosErro(Long id){

		PreparedStatement ps = null;
		Connection con = ConexaoPostGree.getConPostGree();

		try {
			ps = con.prepareStatement("DELETE FROM pessoa WHERE id = " + id);
			ps.executeUpdate();	
			ps = con.prepareStatement("DELETE FROM aluno WHERE id_pessoa = " + id);
			ps.executeUpdate();	
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeStatement( ps );
			closeConnection( con );
		}

	}
	
   public List<Aluno> listarParaEmprestimo() throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Aluno> alunos = new ArrayList<Aluno>();
		Aluno aluno = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		StringBuffer sql = new StringBuffer();
		
		try {
			sql.append("SELECT p.id, p.nome FROM pessoa p INNER JOIN aluno a ON a.id_pessoa = p.id WHERE p.id NOT IN (SELECT e.id_aluno FROM emprestimo AS e WHERE e.ind_baixa = 'N' ) ");
			sql.append(" AND p.	situacao ILIKE 'A'");
			ps = con.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			
			while(rs.next()){
				aluno = new Aluno();
				aluno.setId(rs.getLong("id"));
				aluno.setNome(rs.getString("nome"));
				alunos.add(aluno);		
			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
            closeConnection( con );
        }
		
		
		return alunos;

	}
   
   public List<String> carregarAlunoEvnt() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> alunos = new ArrayList<String>();
		Connection con = ConexaoPostGree.getConPostGree();	
		StringBuffer sql = new StringBuffer();

		try {
			sql.append("SELECT p.id, p.nome FROM pessoa p INNER JOIN aluno a ON a.id_pessoa = p.id WHERE situacao <> 'I'");
			ps = con.prepareStatement(sql.toString());
			rs = ps.executeQuery();

			while(rs.next()){
				alunos.add(rs.getLong("id") + " - " + rs.getString("nome"));
			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
			closeConnection( con );
		}
		return alunos;
	}
   
   public List<Aluno> carregarAlunoFrequencia(Long idTurma) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Aluno> alunos = new ArrayList<Aluno>();
		Connection con = ConexaoPostGree.getConPostGree();	
		StringBuffer sql = new StringBuffer();

		try {
			sql.append("SELECT p.id, p.nome FROM pessoa p INNER JOIN matricula m ON m.id_aluno = p.id WHERE id_turma = ?");
			ps = con.prepareStatement(sql.toString());
			ps.setLong(1, idTurma);
			rs = ps.executeQuery();
			
			Aluno a ;
			while(rs.next()){
				a =  new Aluno();
				a.setId(rs.getLong("id"));
				a.setNome(rs.getString("nome"));
				alunos.add(a);
			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
			closeConnection( con );
		}
		return alunos;
	}
	
   
   public List<AlunoVO> listarRelatorio(Criterio criterio) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AlunoVO> alunos = new ArrayList<AlunoVO>();
		AlunoVO aluno = null;
		Connection con = ConexaoPostGree.getConPostGree();	
		StringBuffer sql = new StringBuffer();
		
		try {
			sql.append("SELECT p.id, nome, data_nascimento, CASE WHEN situacao = 'A' THEN 'Ativo' "
					+ " WHEN situacao = 'I' THEN 'Inativo' END AS situacao FROM pessoa p INNER JOIN aluno a ON a.id_pessoa = p.id" +
					  " WHERE p.id = ?");
			ps = con.prepareStatement(sql.toString());
			ps.setLong(1, Long.valueOf(criterio.getValor()));
			rs = ps.executeQuery();
			
			while(rs.next()){
				aluno = new AlunoVO();
				aluno.setAluno(rs.getString("nome"));
			}
			
			 if(null == aluno){
				 return alunos;
			 }
			
			sql = new StringBuffer();
			sql.append("SELECT t.id, descricao  FROM matricula AS m INNER JOIN  turma AS t ON id_turma = t.id WHERE m.id_aluno = ?");
			ps = con.prepareStatement(sql.toString());
			ps.setLong(1, Long.valueOf(criterio.getValor()));
			rs = ps.executeQuery();
			
	
			while(rs.next()){
					if(null == aluno.getTurmas()){
						aluno.setTurmas("");
					}
					aluno.setTurmas(aluno.getTurmas() + rs.getString("id") + " - " +  rs.getString("descricao") + "\n" );
			}
			
			sql = new StringBuffer();
			sql.append("SELECT i.descricao, data_inicio, data_fim, CASE WHEN e.ind_baixa = 'N' THEN 'Ativo' WHEN e.ind_baixa = 'S' THEN 'Baixado' END AS ind_baixa FROM emprestimo AS e INNER JOIN instrumento AS i ON e.id_instrumento = i.id WHERE id_aluno = ?");
			ps = con.prepareStatement(sql.toString());
			ps.setLong(1, Long.valueOf(criterio.getValor()));
			rs = ps.executeQuery();
				
		
		   while(rs.next()){
				if(null == aluno.getEmprestimos()){
					 aluno.setEmprestimos("");
				 }
				     aluno.setEmprestimos(aluno.getEmprestimos() + rs.getString("descricao") + " - " +  new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("data_inicio")) + " - " +  new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("data_fim")) + " - " +  rs.getString("ind_baixa") + "\n" );
		   }		

		    sql = new StringBuffer();
			sql.append("SELECT nome FROM evento_aluno INNER JOIN evento ON id = id_evento WHERE id_aluno = ?");
			ps = con.prepareStatement(sql.toString());
			ps.setLong(1, Long.valueOf(criterio.getValor()));
			rs = ps.executeQuery();
				
		
		   while(rs.next()){
				if(null == aluno.getEventos()){
					 aluno.setEventos("");
				 }
				     aluno.setEventos(aluno.getEventos() + rs.getString("nome")  + "\n" );
		   }	

		  
		alunos.add(aluno);
				
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
			throw new SQLException(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
           closeConnection( con );
       }
		
		
		return alunos;

	}
	
}
