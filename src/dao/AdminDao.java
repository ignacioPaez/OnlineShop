package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminDao {
	
	private static final Logger logger = Logger.getLogger(AdminDao.class.getName());
	private static final AdminDao instance = new AdminDao();
	
	private AdminDao() {
    }

    public static AdminDao getInstance() {
        return instance;
    }
	
	public int anyAdmin() {
		Conexion con=new Conexion();
		Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        int numAdmin = -1;
        try {
        	conexion = con.getConnection();
            select = conexion.prepareStatement("SELECT COUNT(Permisos) AS num FROM " +
                    "Usuarios WHERE Permisos = 'a'");
            rs = select.executeQuery();
            while (rs.next()) {
                numAdmin = rs.getInt("num");
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo el numero de administradores", ex);
        } finally {
        	con.desconectar(conexion,select);
            con.desconectarResultSet(rs);
        }
        return numAdmin;
    }

}
