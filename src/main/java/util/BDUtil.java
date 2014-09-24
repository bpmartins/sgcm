package util;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Classe com utilitáros para banco de dados
 * 
 * @author Marcos Miguel
 *
 */
public class BDUtil {

	/**
	 * Busca próximo id do generator criado no banco de dados para inserções que 
	 * precisam de antecipadamente ter o id antes da persitencia da tabela pai e filhas.
	 * 
	 * @return
	 */
	public static long getProximoID() {
		ResultSet tabela;
		Statement state;
		long proxID = 0;

		try {
			String sql = " select nextval('sequence_geral') as proxid ";

			state = ConexaoPostGree.getConPostGree().createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			
			tabela = state.executeQuery(sql);

			tabela.next();
			
			proxID = tabela.getLong("proxid");

			tabela.close();
			state.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proxID;
	}
}


