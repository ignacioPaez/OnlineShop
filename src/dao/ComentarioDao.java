package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import modelo.Comentario;
import modelo.Usuario;

public class ComentarioDao {

	private static final Logger logger = Logger.getLogger(ComentarioDao.class
			.getName());
	private static final ComentarioDao instance = new ComentarioDao();
	
	private ComentarioDao() {
    }

    public static ComentarioDao getInstance() {
        return instance;
    }

	public boolean newComment(Usuario user, String codigoProducto,
			String codigoComentario, String fechaHora, String comentario) {
		Conexion con=new Conexion();
		Connection conexion = null;
		PreparedStatement insert = null;
		boolean exito = false;
		try {
			conexion = con.getConnection();
			insert = conexion.prepareStatement(
							"INSERT INTO " + "Comentarios VALUES (?,?,?,?,?,?)");
			insert.setString(1, codigoComentario);
			insert.setString(2, fechaHora);
			insert.setString(3, codigoProducto);
			insert.setString(4, user.getMail());
			insert.setString(5, user.getNombre());
			insert.setString(6, comentario);

			int filasAfectadas = insert.executeUpdate();
			if (filasAfectadas == 1) {
				exito = true;
			}
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, "Error añadiendo nuevo comentario", ex);
		} finally {
			con.desconectar(conexion,insert);
		}
		return exito;
	}

	public boolean deleteComment(String codigoComentario) {
		Conexion con=new Conexion();
		Connection conexion = null;
		PreparedStatement delete = null;
		boolean exito = false;
		try {
			conexion = con.getConnection();
			delete = conexion.prepareStatement(
							"DELETE FROM "
									+ "Comentarios WHERE CodigoComentario=?");
			delete.setString(1, codigoComentario);

			int filasAfectadas = delete.executeUpdate();
			if (filasAfectadas == 1) {
				exito = true;
			}
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, "Error borrando comentario", ex);
		} finally {
			con.desconectar(conexion,delete);
		}
		return exito;
	}

	public boolean updateComment(String codComentario, Comentario comentario) {
		Conexion con=new Conexion();
		Connection conexion = null;
		PreparedStatement update = null;
		boolean exito = false;
		try {
			conexion = con.getConnection();
			update = conexion.prepareStatement(
							"UPDATE "
									+ "Comentarios SET FechaHora=?, CodigoProducto=?, "
									+ "Email=?, Nombre=?, Comentario=? WHERE CodigoComentario=?");
			update.setString(1, comentario.getFechaHora());
			update.setString(2, comentario.getCodigoProducto());
			update.setString(3, comentario.getEmail());
			update.setString(4, comentario.getNombre());
			update.setString(5, comentario.getComentario());
			update.setString(6, comentario.getCodigoComentario());

			int filasAfectadas = update.executeUpdate();
			con.getConnection().close();
			if (filasAfectadas == 1) {
				exito = true;
			}
		} catch (SQLException ex) {
			logger.log(Level.WARNING, "Error modificando comentario", ex);
		} finally {
			con.desconectar(conexion,update);
		}
		return exito;
	}

	public LinkedList<Comentario> getComentarios(String campo, String valor) {
		Conexion con=new Conexion();
		Connection conexion = null;
		LinkedList<Comentario> comentarios = new LinkedList<Comentario>();
		PreparedStatement select = null;
		ResultSet rs = null;
		try {
			conexion = con.getConnection();
			select = conexion.prepareStatement(
							"SELECT * FROM " + "Comentarios WHERE " + campo
									+ "=? ORDER BY FechaHora DESC");
			select.setString(1, valor);
			rs = select.executeQuery();
			while (rs.next()) {
				Comentario comment = new Comentario(
						rs.getString("CodigoComentario"), rs.getDate(
								"FechaHora").toString(), rs
								.getTime("FechaHora").toString(),
						rs.getString("CodigoProducto"), rs.getString("Email"),
						rs.getString("Nombre"), rs.getString("Comentario"));
				comentarios.add(comment);
			}
			if (comentarios.size() <= 0) {
				comentarios = null;
			}
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, "Error obteniendo los comentarios", ex);
			comentarios = null;
		} finally {

			con.desconectar(conexion,select);
			con.desconectarResultSet(rs);
		}
		return comentarios;
	}

	public Comentario getComment(String codComentario) {
		Conexion con=new Conexion();
		Connection conexion = null;
		Comentario comment = null;
		PreparedStatement select = null;
		ResultSet rs = null;
		try {
			conexion = con.getConnection();
			select = conexion.prepareStatement(
							"SELECT * FROM "
									+ "Comentarios WHERE CodigoComentario=?");
			select.setString(1, codComentario);
			rs = select.executeQuery();
			while (rs.next()) {
				comment = new Comentario(rs.getString("CodigoComentario"), rs
						.getDate("FechaHora").toString(), rs.getTime(
						"FechaHora").toString(),
						rs.getString("CodigoProducto"), rs.getString("Email"),
						rs.getString("Nombre"), rs.getString("Comentario"));
			}
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, "Error obteniendo comentario", ex);
		} finally {
			con.desconectar(conexion,select);
			con.desconectarResultSet(rs);
		}
		return comment;
	}

}
