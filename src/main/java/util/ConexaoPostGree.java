package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;
/**
 * Classe de Conexao com o Banco de Dados 
 */
public class ConexaoPostGree {

    private static Connection vConPostGree;

    private ConexaoPostGree() {
    }

    public static Connection getConPostGree() {
        try {
            if ((vConPostGree == null) || vConPostGree.isClosed()) {
                Class.forName("org.postgresql.Driver");
                vConPostGree = DriverManager.getConnection("jdbc:postgresql://localhost:5432/SGCM", "postgres", "123456");
            }
        } catch (SQLException e) {
        	Logger.getRootLogger().error(e.getMessage());
        } catch (ClassNotFoundException e) {
        	 System.out.println("Erro ao conectar ao banco de dados : " + e.getMessage());
        }
        return vConPostGree;
    }

    public static void setConPostGree(Connection pConnection) {
        vConPostGree = pConnection;
    }
}
