package servlet.producto;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import control.Tools;
import modelo.Carrito;
import modelo.Producto;
import dao.ProductoDao;

/**
 * Servlet implementation class EditAmountServlet
 */
@WebServlet("/EditAmountServlet")
public class EditAmountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditAmountServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return "Servlet para la edición de cantidad de un producto en la cesta";
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (validateForm(request) == true) {
            if (request.getSession().getAttribute("carrito") == null) {
                request.setAttribute("resultados", "Carrito no encontrado");
                Tools.anadirMensaje(request, "La sesión no contiene carrito de compra, es posible que haya caducado la sesión");
                request.getRequestDispatcher("/shop/products.jsp").forward(request, response);
                return;
            }
            int cantidad = Integer.parseInt(request.getParameter("cant"));
			String cod = request.getParameter("prod");
			
			Producto prod = ProductoDao.getInstance().getProduct(cod);
			if (prod != null) {
			    //Se comprueba si se han pedido mas unidades de las que hay
			    if (cantidad > prod.getStock()){
			        request.setAttribute("resultados", "Stock insuficiente");
			        Tools.anadirMensaje(request, "No tenemos disponibles las unidades que nos solicita");
			        request.getRequestDispatcher("/shop/cart.jsp").forward(request, response);
			        return;
			    }
			    
			    boolean ok = ((Carrito) request.getSession().getAttribute("carrito")).editCant(cod, cantidad, prod.getPrecio());
			    if (ok == false) {
			        request.setAttribute("resultados", "Produco no encontrado en la cesta");
			        Tools.anadirMensaje(request, "El producto que ha seleccionado para modificar su cantidad no se encuentra en el carrito");
			    }
			    request.getRequestDispatcher("/shop/cart.jsp").forward(request, response);
			} else {
			    request.setAttribute("resultados", "Producto no disponible");
			    Tools.anadirMensaje(request, "El producto seleccionado no se ha encontrado en la tienda");
			    request.getRequestDispatcher("/shop/cart.jsp").forward(request, response);
			}
        } else {
            response.sendError(404);
        }
    }
	
	protected boolean validateForm(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 2 && request.getParameter("prod") != null && request.getParameter("cant") != null) {
            return Tools.validateUUID(request.getParameter("prod"));
        } else {
            return false;
        }
    }

}
