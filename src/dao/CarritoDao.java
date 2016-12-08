package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import modelo.Carrito;
import modelo.Producto;

public class CarritoDao {
	
	private static final Logger logger = Logger.getLogger(CarritoDao.class.getName());
	private static final CarritoDao instance = new CarritoDao();
	
	private CarritoDao() {
    }

    public static CarritoDao getInstance() {
        return instance;
    }
	
	 public boolean saveCart(Carrito cart, boolean completado, String date, String formPago) {
		 	Conexion con=new Conexion();
			Connection conexion = null;
	        PreparedStatement insertHistorial = null;
	        PreparedStatement insertCarrito = null;
	        boolean exito = false;
	        try {
	        	conexion = con.getConnection();
	            conexion.setAutoCommit(false);
	            insertHistorial = conexion.prepareStatement("INSERT INTO " + "HistorialCarritos VALUES (?,?,?,?,?,?)");
	            insertHistorial.setString(1, cart.getUser());
	            insertHistorial.setString(2, cart.getCodigo());
	            insertHistorial.setString(3, date);
	            insertHistorial.setDouble(4, cart.getPrecio());
	            insertHistorial.setString(5, formPago);
	            insertHistorial.setBoolean(6, completado);

	            int filasAfectadas = insertHistorial.executeUpdate();
	            if (filasAfectadas != 1) {
	                conexion.rollback();
	            } else {
	                insertCarrito = conexion.prepareStatement("INSERT INTO " + "Carritos VALUES (?,?,?,?,?)");
	                Iterator<String> iteradorProductos = cart.getArticulos().keySet().iterator();
	                while (iteradorProductos.hasNext()) {
	                    String key = iteradorProductos.next();
	                    Producto prod = ProductoDao.getInstance().getProduct(key);
	                    int cantidad = cart.getArticulos().get(key);
	                    insertCarrito.setString(1, cart.getCodigo());
	                    insertCarrito.setString(2, prod.getCodigo());
	                    insertCarrito.setString(3, prod.getNombre());
	                    insertCarrito.setDouble(4, prod.getPrecio());
	                    insertCarrito.setInt(5, cantidad);
	                    filasAfectadas = insertCarrito.executeUpdate();
	                    if (filasAfectadas != 1) {
	                        conexion.rollback();
	                        break;
	                    }
	                    insertCarrito.clearParameters();
	                }
	                conexion.commit();
	                exito = true;
	            }
	        } catch (SQLException ex) {
	            logger.log(Level.SEVERE, "Error añadiendo carrito al registro", ex);
	            try {
	                conexion.rollback();
	            } catch (SQLException ex1) {
	                logger.log(Level.SEVERE, "Error haciendo rollback de la transacción para insertar carrito en el registro", ex1);
	            }
	        } finally {
	        	con.desconectar(conexion,insertCarrito, insertHistorial);
	        }
	        return exito;
	    }
	 
	 public Carrito requestLastIncompleteCart(String mail) {
		 Conexion con=new Conexion();
			Connection conexion = null;
	        PreparedStatement selectCarro = null;
	        PreparedStatement selectProductos = null;
	        ResultSet consultaDatosCarro = null;
	        ResultSet rs = null;
	        Carrito carro = null;
	        try {
	        	conexion = con.getConnection();
	            conexion.setAutoCommit(false);
	            ArrayList<String> carrosIncompletos = this.requestIncompleteCarts(mail);
	            if (carrosIncompletos != null) {
	                selectCarro = conexion.prepareStatement("SELECT * FROM " + "HistorialCarritos WHERE CodigoCarrito=?");
	                selectCarro.setString(1, carrosIncompletos.get(0));
	                consultaDatosCarro = selectCarro.executeQuery();

	                while (consultaDatosCarro.next()) {
	                    carro = new Carrito(consultaDatosCarro.getString("CodigoCarrito"),
	                            consultaDatosCarro.getString("Email"), consultaDatosCarro.getDouble("Precio"));
	                }
	                if (carro == null) {
	                    conexion.rollback();
	                } else {
	                    Map<String, Integer> productosCarro = new HashMap<String, Integer>();
	                    selectProductos = conexion.prepareStatement("SELECT CodigoProducto, Cantidad FROM "
	                            + ".Carritos WHERE CodigoCarrito=?");
	                    selectProductos.setString(1, carrosIncompletos.get(0));
	                    rs = selectProductos.executeQuery();
	                    while (rs.next()) {
	                        productosCarro.put(rs.getString("CodigoProducto"), rs.getInt("Cantidad"));
	                    }
	                    conexion.commit();
	                    carro.setArticulos(productosCarro);
	                }
	            } else {
	                conexion.rollback();
	            }
	        } catch (SQLException ex) {
	            logger.log(Level.SEVERE, "Error obteniendo el ultimo carrito incompleto del usuario", ex);
	            carro = null;
	            try {
	                conexion.rollback();
	            } catch (SQLException ex1) {
	                logger.log(Level.SEVERE, "Error haciendo rollback de la transacción para obtener ultimo carro incompleto del usuario", ex1);
	            }
	        } finally {
	        	con.desconectar(conexion,selectCarro, selectProductos);
	            con.desconectarResultSet(rs);
	        }
	        return carro;
	        
	        
	    }
	 
	 public boolean deleteImcompleteCartsClient(String mailClient) {
		 Conexion con=new Conexion();
			Connection conexion = null;
	        PreparedStatement deleteHistorialCarros = null;
	        PreparedStatement deleteProdCarro = null;
	        boolean exito = false;
	        try {
	        	conexion = con.getConnection();
	            conexion.setAutoCommit(false);
	            ArrayList<String> carrosIncompletos = this.requestIncompleteCarts(mailClient);
	            if (carrosIncompletos != null) {
	                deleteHistorialCarros = conexion.prepareStatement("DELETE FROM "
	                        + "HistorialCarritos WHERE CodigoCarrito=?");
	                deleteProdCarro = conexion.prepareStatement("DELETE FROM "
	                        + "Carritos WHERE CodigoCarrito=?");
	                for (int i = 0; i < carrosIncompletos.size(); i++) {
	                    deleteHistorialCarros.setString(1, carrosIncompletos.get(i));
	                    deleteHistorialCarros.execute();
	                    deleteHistorialCarros.clearParameters();
	                    deleteProdCarro.setString(1, carrosIncompletos.get(i));
	                    deleteProdCarro.execute();
	                    deleteProdCarro.clearParameters();
	                }
	            }
	           conexion.commit();
	            exito = true;
	        } catch (SQLException ex) {
	            logger.log(Level.SEVERE, "Error borrando los carritos incompletos de un usuario", ex);
	            try {
	                conexion.rollback();
	            } catch (SQLException ex1) {
	                logger.log(Level.SEVERE, "Error haciendo rollback de la transacción para borrar carros incompletos de un usuario", ex1);
	            }
	        } finally {
	        	
	        	con.desconectar(conexion,deleteHistorialCarros, deleteProdCarro);
	        }
	        return exito;
	    }
	 
	 public ArrayList<Carrito> requestSalesRecord(String campo, String term) {
		 Conexion con=new Conexion();
			Connection conexion = null;
	        if (campo.equals("1") == true) {
	            campo = "'1'";
	        }
	        PreparedStatement select = null;
	        ResultSet rs = null;
	        ArrayList<Carrito> historial = new ArrayList<Carrito>();
	        try {
	        	conexion = con.getConnection();
	            Calendar cal = Calendar.getInstance(new Locale("es", "ES"));
	            select = conexion.prepareStatement("SELECT* FROM " +
	                    "HistorialCarritos WHERE Completado=true AND " + campo + "=?");
	            select.setString(1, term);
	            rs = select.executeQuery();
	            while (rs.next()) {
	                Carrito carro = new Carrito(rs.getString("CodigoCarrito"), rs.getString("Email"), 
	                        rs.getDouble("Precio"), rs.getDate("FechaHora", cal).toString(),
	                        rs.getTime("FechaHora", cal).toString(), rs.getString("Pago"));
	                historial.add(carro);
	            }

	            if (historial.size() <= 0) {
	                historial = null;
	            }
	        } catch (SQLException ex) {
	            logger.log(Level.SEVERE, "Error obteniendo historial de ventas", ex);
	            historial = null;
	        } finally {
	        	con.desconectar(conexion,select);
	            con.desconectarResultSet(rs);
	        }
	        return historial;
	    }
	 
	 public ArrayList<Producto> getDetailsCartRecord(String codigo) {
		 Conexion con=new Conexion();
			Connection conexion = null;
	        ArrayList<Producto> listado = new ArrayList<Producto>();
	        PreparedStatement select = null;
	        ResultSet rs = null;
	        try {
	        	conexion = con.getConnection();
	            select = conexion.prepareStatement("SELECT* FROM " +  "Carritos WHERE CodigoCarrito=?");
	            select.setString(1, codigo);
	            rs = select.executeQuery();
	            while (rs.next()) {
	                Producto prod = new Producto(rs.getString("CodigoProducto"), rs.getString("Nombre"),
	                        rs.getDouble("Precio"), rs.getInt("Cantidad"));
	                listado.add(prod);
	            }
	            if (listado.size() <= 0) {
	                listado = null;
	            } 
	        } catch (SQLException ex) {
	            logger.log(Level.SEVERE, "Error obteniendo los productos pertenecientes a un carrito", ex);
	            listado = null;
	        } finally {
	        	con.desconectar(conexion,select);
	            con.desconectarResultSet(rs);
	        }
	        return listado;
	    }
	 
	 private ArrayList<String> requestIncompleteCarts(String mail) {
		 Conexion con=new Conexion();
			Connection conexion = null;
	        ArrayList<String> carrosIncompletos = new ArrayList<String>();
	        PreparedStatement selectHistorial = null;
	        ResultSet rs = null;
	        try {
	        	conexion = con.getConnection();
	            selectHistorial = conexion.prepareStatement("SELECT CodigoCarrito FROM " +
	                    "HistorialCarritos WHERE Email=? AND Completado=false");
	            selectHistorial.setString(1, mail);
	            rs = selectHistorial.executeQuery();
	            while (rs.next()) {
	                carrosIncompletos.add(rs.getString("CodigoCarrito"));
	            }
	            if (carrosIncompletos.size() <= 0) {
	                carrosIncompletos = null;
	            } 
	        } catch (SQLException ex) {
	            logger.log(Level.SEVERE, "Error obteniendo los codigos de los ultimos carros incompletos", ex);
	            carrosIncompletos = null;
	        } finally {
	        	con.desconectar(conexion,selectHistorial);
	            con.desconectarResultSet(rs);
	        }
	        return carrosIncompletos;
	    }

}
