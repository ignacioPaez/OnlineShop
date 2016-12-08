package dao;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {

	private static final Logger logger = Logger.getLogger(Conexion.class
			.getName());
	static String login = "root";
	static String password = "abc";
	static String url = "jdbc:mysql://localhost:3306/shop";
	Connection conn = null;

	private static Conexion instancia;

	public Conexion getInstancia() {
		if (instancia == null) {
			instancia = new Conexion();
		}
		return instancia;
	}

	
	/** Constructor de DbConnection */
	public Conexion() {
		try {
			// obtenemos el driver de para mysql
			Class.forName("com.mysql.jdbc.Driver");
			// obtenemos la conexión
			conn = DriverManager.getConnection(url, login, password);

			if (conn != null) {
				System.out.println("Conexión a base de datos OK");
			}
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, "Error al iniciar la base de datos", ex.getMessage());
		} catch (ClassNotFoundException ex) {
			logger.log(Level.SEVERE, "Error en base de datos: clase no encontrada", ex.getMessage());
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Error al iniciar la base de datos: excepcion", ex.getMessage());
		}
	}

	/** Permite retornar la conexión */
	public Connection getConnection() {
		try {
			if (conn == null) {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				conn = DriverManager.getConnection(url, login, password);
			}

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;
	}

	public void desconectar(Connection conexion, Statement... statements) {
		try {
			conexion.close();
		} catch (SQLException ex) {
			logger.log(Level.SEVERE,
					"Error al cerrar una conexión a la base de datos", ex);
		} finally {
			for (Statement statement : statements) {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException ex) {
						logger.log(Level.SEVERE,
								"Error al cerrar un statement", ex);
					}
				}
			}
		}
	}

	public void desconectarResultSet(ResultSet... results) {
		for (ResultSet rs : results) {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					logger.log(Level.SEVERE, "Error al cerrar un resultset", ex);
				}
			}
		}
	}

}
