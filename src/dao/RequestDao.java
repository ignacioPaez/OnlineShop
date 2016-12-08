package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestDao {
	
	private static final Logger logger = Logger.getLogger(RequestDao.class.getName());
	private static final RequestDao instance = new RequestDao();
	
	private RequestDao() {
    }

    public static RequestDao getInstance() {
        return instance;
    }
	
	public boolean saveRequest(String fechaHora, String requestedURL, String remoteAddr,
            String remoteHost, String method, String param, String userAgent) {
		Conexion con=new Conexion();
		Connection conexion = null;
        PreparedStatement insert = null;
        boolean exito = false;
        try {
        	conexion = con.getConnection();
            insert = conexion.prepareStatement("INSERT INTO " + "Log VALUES (?,?,?,?,?,?,?)");
            insert.setString(1, fechaHora);
            insert.setString(2, requestedURL);
            insert.setString(3, remoteAddr);
            insert.setString(4, remoteHost);
            insert.setString(5, method);
            insert.setString(6, param);
            insert.setString(7, userAgent);

            int filasAfectadas = insert.executeUpdate();
            if (filasAfectadas == 1) {
                exito = true;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error guardando log de petición", ex);
        } finally {
        	con.desconectar(conexion, insert);
        }
        return exito;
    }

}
