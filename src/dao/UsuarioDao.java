package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import modelo.Usuario;

public class UsuarioDao {
	
	private static final Logger logger = Logger.getLogger(UsuarioDao.class.getName());
	private static final UsuarioDao instance = new UsuarioDao();
	
	private UsuarioDao() {
    }

    public static UsuarioDao getInstance() {
        return instance;
    }
	
	public boolean addUser(Usuario user) throws SQLException {
		Conexion con=new Conexion();
		Connection conexion = null;
        boolean exito = false;
        PreparedStatement insert = null;
        try {
        	conexion = con.getConnection();
            insert = conexion.prepareStatement("INSERT INTO " + "Usuarios VALUES (?,?,?,?,?)");
            insert.setString(1, user.getMail());
            insert.setString(2, user.getNombre());
            insert.setString(3, user.getDir());
            insert.setString(4, user.getPass());
            insert.setObject(5, user.getPermisos(), java.sql.Types.CHAR, 1);

            int filasAfectadas = insert.executeUpdate();
            if (filasAfectadas == 1) {
                exito = true;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error insertando usuario", ex);
        } finally {
        	
        	con.desconectar(conexion, insert);
            
        }
        return exito;
    }
	
	public boolean delUser(String mail) throws SQLException {
		Conexion con=new Conexion();
		Connection conexion = null;
        PreparedStatement delete = null;
        boolean exito = false;
        
        try {
        	conexion = con.getConnection();
            delete = conexion.prepareStatement("DELETE FROM " + "Usuarios WHERE Email=?");
            delete.setString(1, mail);

            int filasAfectadas = delete.executeUpdate();
            if (filasAfectadas == 1) {
                exito = true;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error borrando usuario", ex.getMessage());
        } finally {
           
        	con.desconectar(conexion, delete);
        }
        return exito;
    }
	
	public Usuario getUser(String mail) throws SQLException {
		Conexion con=new Conexion();
		Connection conexion = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        Usuario user = null;

        try {
        	conexion = con.getConnection();
            select = conexion.prepareStatement("SELECT* FROM " + "Usuarios WHERE Email=?");
            select.setString(1, mail);
            rs = select.executeQuery();
            while (rs.next()) {
                user = new Usuario(rs.getString("Nombre"), rs.getString("Direccion"), rs.getString("Email"),
                        rs.getString("Pass"), rs.getString("Permisos").charAt(0));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo usuario", ex);
            user = null;
        } finally {
        	con.desconectar(conexion, select);
        	con.desconectarResultSet(rs);
        }
        return user;
    }
	
	public boolean updateUser(String mail, Usuario user) throws SQLException {
		Conexion con=new Conexion();
		Connection conexion = null;
        PreparedStatement update = null;
        PreparedStatement updateComentarios = null;
        boolean exito = false;
        try {
        	conexion = con.getConnection();
            update = conexion.prepareStatement("UPDATE " + "Usuarios SET Nombre=?, Direccion=?, Pass=?, Permisos=? WHERE Email=?");
            update.setString(1, user.getNombre());
            update.setString(2, user.getDir());
            update.setString(3, user.getPass());
            update.setObject(4, user.getPermisos(), java.sql.Types.CHAR, 1);
            update.setString(5, mail);

            int filasAfectadas = update.executeUpdate();
            if (filasAfectadas == 1) {
                updateComentarios = conexion.prepareStatement("UPDATE " + "Comentarios SET Nombre=? WHERE Email=?");
                updateComentarios.setString(1, user.getNombre());
                updateComentarios.setString(2, mail);
                updateComentarios.executeUpdate();
                exito = true;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error editando usuario o su nombre en los comentarios", ex.getMessage());
        } finally {
        	con.desconectar(conexion,update,updateComentarios);
        }
        return exito;
    }
	
	public Map<String, Usuario> getUsers() throws SQLException {
		Conexion con=new Conexion();
		Connection conexion = null;
        Map<String, Usuario> usuarios = new HashMap<String, Usuario>();
        PreparedStatement select = null;
        ResultSet rs = null;
        try {
        	
        	conexion = con.getConnection();
            select = conexion.prepareStatement("SELECT* FROM " + "Usuarios");
            rs = select.executeQuery();
            while (rs.next()) {
                Usuario user = new Usuario(rs.getString("Nombre"), rs.getString("Direccion"),
                        rs.getString("Email"), rs.getString("Pass"), rs.getString("Permisos").charAt(0));
                usuarios.put(user.getMail(), user);
            }
            if (usuarios.size() <= 0) {
                usuarios = null;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo los usuarios", ex);
            usuarios = null;
        } finally {
        	con.desconectar(conexion, select);
        	con.desconectarResultSet(rs);
        }
        return usuarios;
    }

}
