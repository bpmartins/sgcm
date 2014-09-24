/**
 * 
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author bruno.martins
 *
 */
public class AbstractDAO {

	
	
	
	public void closeResultSet(ResultSet rs){
		try {
			if (null != rs)  rs.close();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void closeStatement(PreparedStatement ps){
		try {
			if (null != ps) ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void closeConnection(Connection connection){
		try {
			if (null != connection) connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
