package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import control.Tools;
import modelo.Producto;

public class ProductoDao {
	
	private static final ProductoDao instance = new ProductoDao();
	private static final Logger logger = Logger.getLogger(ProductoDao.class.getName());
	
	private ProductoDao() {
    }

    public static ProductoDao getInstance() {
        return instance;
    }
	
	public boolean addProduct(Producto prod) {
		Conexion con=new Conexion();
		Connection conexion = null;
        PreparedStatement insert = null;
        boolean exito = false;
        try {
            conexion = con.getConnection();
        	insert = conexion.prepareStatement("INSERT INTO " + "Productos VALUES (?,?,?,?,?,?)");
            insert.setString(1, prod.getCodigo());
            insert.setString(2, prod.getNombre());
            insert.setDouble(3, prod.getPrecio());
            insert.setInt(4, prod.getStock());
            insert.setString(5, prod.getDesc());
            insert.setString(6, prod.getDetalles());

            int filasAfectadas = insert.executeUpdate();
            if (filasAfectadas == 1) {
                exito = true;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error insertando producto", ex);
        } finally {
            con.desconectar(conexion,insert);
        }
        return exito;
    }
	
	public boolean delProduct(String codigo) {
		Conexion con=new Conexion();
		Connection conexion = null;		
        PreparedStatement delete = null;
        PreparedStatement deleteComments = null;
        boolean exito = false;
        try {
        	conexion = con.getConnection();
            delete = conexion.prepareStatement("DELETE FROM " + "Productos WHERE Codigo=?");
            delete.setString(1, codigo);
            deleteComments = conexion.prepareStatement("DELETE FROM " + "Comentarios WHERE CodigoProducto=?");
            deleteComments.setString(1, codigo);

            int filasAfectadas = delete.executeUpdate();
            if (filasAfectadas == 1) {
                deleteComments.executeUpdate();
                exito = true;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error borrando producto o borrando sus comentarios asignados", ex);
        } finally {
        	 con.desconectar(conexion,delete, deleteComments);
        }
        return exito;
    }
	
	public Producto getProduct(String codigo) {
		Conexion con=new Conexion();
		Connection conexion = null;		
        PreparedStatement select = null;
        ResultSet rs = null;
        Producto prod = null;
        try {
        	conexion = con.getConnection();
            select = conexion.prepareStatement("SELECT* FROM " + "Productos WHERE Codigo=?");
            select.setString(1, codigo);
            rs = select.executeQuery();
            while (rs.next()) {
                prod = new Producto(rs.getString("Codigo"), rs.getString("Nombre"), rs.getDouble("Precio"),
                        rs.getInt("Stock"), rs.getString("Descripcion"), rs.getString("Detalles"));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo producto", ex);
            prod = null;
        } finally {
        	con.desconectar(conexion,select);
        	con.desconectarResultSet(rs);
        }
        return prod;
    }

	public boolean updateProduct(String codigo, Producto prod) {
		Conexion con=new Conexion();
		Connection conexion = null;
        PreparedStatement update = null;
        boolean exito = false;
        try {
        	conexion = con.getConnection();
            update = conexion.prepareStatement("UPDATE " +
                    "Productos SET Nombre=?, Precio=?, Stock=?, Descripcion=?, Detalles=? WHERE Codigo=?");
            update.setString(1, prod.getNombre());
            update.setDouble(2, prod.getPrecio());
            update.setInt(3, prod.getStock());
            update.setObject(4, prod.getDesc());
            update.setString(5, prod.getDetalles());
            update.setString(6, codigo);

            int filasAfectadas = update.executeUpdate();
            if (filasAfectadas == 1) {
                exito = true;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error actualizando producto", ex);
        } finally {
        	con.desconectar(conexion,update);
        }
        return exito;
    }
	
	public boolean updateProductIfAvailable(Map<String, Integer> carro, javax.servlet.http.HttpServletRequest request,
            Map<Producto, Integer> listado) {
		Conexion con=new Conexion();
		Connection conexion = null;
        PreparedStatement select = null;
        PreparedStatement update = null;
        ResultSet rs = null;
        boolean exito = false;
        try {
        	conexion = con.getConnection();
            conexion.setAutoCommit(false);
            select = conexion.prepareStatement("SELECT* FROM " + "Productos WHERE Codigo=?");
            update = conexion.prepareStatement("UPDATE " + "Productos SET Stock=? WHERE Codigo=?");
            String codigoProd;
            int filasAfectadas = 0;

            Iterator<String> iterador = carro.keySet().iterator();
            while (iterador.hasNext()) {
                codigoProd = iterador.next();
                select.setString(1, codigoProd);
                rs = select.executeQuery();
                if (rs.next() == false) {
                    Tools.anadirMensaje(request, "No existe el producto con codigo: " + codigoProd +
                            "(producto eliminado de la cesta)");
                    iterador.remove();
                    conexion.rollback();
                } else {
                    Producto prod = new Producto(rs.getString("Codigo"), rs.getString("Nombre"), rs.getDouble("Precio"),
                            rs.getInt("Stock"), rs.getString("Descripcion"), rs.getString("Detalles"));
                    select.clearParameters();
                    if (carro.get(codigoProd) > prod.getStock()) {
                        Tools.anadirMensaje(request, "No hay unidades suficientes de: " + prod.getNombre() +
                                "(producto eliminado de la cesta)");
                        iterador.remove();
                        conexion.rollback();
                    } else {
                        update.setInt(1, prod.getStock() - carro.get(codigoProd));
                        update.setString(2, codigoProd);
                        filasAfectadas = update.executeUpdate();
                        if (filasAfectadas != 1) {
                            Tools.anadirMensaje(request, "Ocurrio un error en el catalogo");
                            conexion.rollback();
                        }
                        update.clearParameters();
                        listado.put(prod, carro.get(codigoProd));
                    }
                }
            }
            conexion.commit();
            exito = true;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error actualizando unidades de productos en compra", ex);
            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                logger.log(Level.SEVERE, "Error haciendo rolback de la transacción que ha dado error en la actualización de unidades por compra", ex1);
            }
        } finally {
        	con.desconectar(conexion,select, update);
            con.desconectarResultSet(rs);
        }
        return exito;
    }
	
	public Map<String, Producto> getProducts() {
		Conexion con=new Conexion();
		Connection conexion = null;
		Map<String, Producto> productos = new HashMap<String, Producto>();
        PreparedStatement select = null;
        ResultSet rs = null;
        try {
        	conexion = con.getConnection();
            select = conexion.prepareStatement("SELECT* FROM " + "Productos");
            rs = select.executeQuery();
            while (rs.next()) {
                Producto prod = new Producto(rs.getString("Codigo"), rs.getString("Nombre"),
                        rs.getDouble("Precio"), rs.getInt("Stock"), rs.getString("Descripcion"), rs.getString("Detalles"));
                productos.put(prod.getCodigo(), prod);
            }
            if (productos.size() <= 0) {
                productos = null;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error obteniendo los productos", ex);
            productos = null;
        } finally {
        	con.desconectar(conexion,select);
            con.desconectarResultSet(rs);
        }
        return productos;
    }
	
	public Map<String, Producto> searchProd(String campo, String term) {
		Conexion con=new Conexion();
		Connection conexion = null;
		Map<String, Producto> productos = new HashMap<String, Producto>();
        PreparedStatement select = null;
        ResultSet rs = null;
        try {
        	conexion = con.getConnection();
            select = conexion.prepareStatement("SELECT* FROM " + "Productos WHERE " + campo + " LIKE ?");
            select.setString(1, "%" + term + "%");
            rs = select.executeQuery();
            while (rs.next()) {
                Producto prod = new Producto(rs.getString("Codigo"), rs.getString("Nombre"),
                        rs.getDouble("Precio"), rs.getInt("Stock"), rs.getString("Descripcion"), rs.getString("Detalles"));
                productos.put(prod.getCodigo(), prod);
            }
            if (productos.size() <= 0) {
                productos = null;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error buscando producto", ex);
            productos = null;
        } finally {
        	con.desconectar(conexion,select);
            con.desconectarResultSet(rs);
        }
        return productos;
    }

}
