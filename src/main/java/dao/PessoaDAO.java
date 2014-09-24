package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Pessoa;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import util.ConexaoPostGree;

@Repository
@Transactional(rollbackFor={Exception.class})
public class PessoaDAO extends AbstractDAO{


	public List<Pessoa> listarParaCombos() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Pessoa> pessoas = new ArrayList<Pessoa>();
		Pessoa pessoa = null;
		Connection con = ConexaoPostGree.getConPostGree();	

		try {
			ps = con.prepareStatement("SELECT id, nome FROM pessoa WHERE situacao <> 'I'");
			rs = ps.executeQuery();

			while(rs.next()){
				pessoa = new Pessoa();
				pessoa.setId(rs.getLong("id"));
				pessoa.setNome(rs.getString("nome"));
				pessoas.add(pessoa);
			}
		} catch (SQLException e) {
			Logger.getRootLogger().error(e.getMessage());
		} finally {
			closeResultSet( rs );
			closeStatement( ps );
			closeConnection( con );
		}	
		return pessoas;
	}

	protected Long getId() throws SQLException{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT nextval('sequence_pessoa')";

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

}
