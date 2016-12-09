package servlet.producto;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.security.validator.ValidatorException;
import modelo.Carrito;
import modelo.Producto;
import control.Tools;
import dao.ProductoDao;

/**
 * Servlet implementation class AddCarritoServlet
 */
@WebServlet("/AddCarritoServlet")
public class AddCarritoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCarritoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return "Servlet encargado de añadir un producto al carrito";
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (validateForm(request) == false) {
            response.sendError(404);
            return;
        } else {
            int cantidadNueva = Integer.parseInt(request.getParameter("cant"));
			String cod = request.getParameter("prod");
			if (ProductoDao.getInstance().getProduct(cod) != null) {
			    Producto prod = ProductoDao.getInstance().getProduct(cod);
			    Carrito carro = (Carrito) request.getSession().getAttribute("carrito");
			    Integer cantidadActual = 0;
			    if (carro != null) {
			        cantidadActual = carro.getArticulos().get(cod);
			        if (cantidadActual == null) {
			            cantidadActual = 0;
			        }
			    }
			    if ((cantidadNueva + cantidadActual) > prod.getStock()) {
			        request.setAttribute("resultados", "No hay suficiente Stock");
			        Tools.anadirMensaje(request, "No hay stock suficiente del producto seleccionado");
			        request.getRequestDispatcher("/shop/products.jsp").forward(request, response);
			        return;
			    }
			    if (carro == null) {
			        carro = new Carrito(Tools.generaUUID(), (String)request.getSession().getAttribute("usuario"));
			        carro.addProduct(request.getParameter("prod"), cantidadNueva, prod.getPrecio());
			        request.getSession().setAttribute("carrito", carro);
			    } else {
			        ((Carrito) request.getSession().getAttribute("carrito")).addProduct(cod, cantidadNueva, prod.getPrecio());
			    }
			} else {
			    request.setAttribute("resultados", "Producto no disponible");
			    Tools.anadirMensaje(request, "El producto elegido no existe");
			}
        }
        request.getRequestDispatcher("/shop/products.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (validateForm(request) == false) {
            response.sendError(404);
            return;
        } else {
            int cantidadNueva = Integer.parseInt(request.getParameter("cant"));
			String cod = request.getParameter("prod");
			if (ProductoDao.getInstance().getProduct(cod) != null) {
			    Producto prod = ProductoDao.getInstance().getProduct(cod);
			    Carrito carro = (Carrito) request.getSession().getAttribute("carrito");
			    Integer cantidadActual = 0;
			    if (carro != null) {
			        cantidadActual = carro.getArticulos().get(cod);
			        if (cantidadActual == null) {
			            cantidadActual = 0;
			        }
			    }
			    if ((cantidadNueva + cantidadActual) > prod.getStock()) {
			        request.setAttribute("resultados", "No hay suficiente Stock");
			        Tools.anadirMensaje(request, "No hay stock suficiente del producto seleccionado");
			        request.getRequestDispatcher("/shop/products.jsp").forward(request, response);
			        return;
			    }
			    if (carro == null) {
			        carro = new Carrito(Tools.generaUUID(), (String)request.getSession().getAttribute("usuario"));
			        carro.addProduct(request.getParameter("prod"), cantidadNueva, prod.getPrecio());
			        request.getSession().setAttribute("carrito", carro);
			    } else {
			        ((Carrito) request.getSession().getAttribute("carrito")).addProduct(cod, cantidadNueva, prod.getPrecio());
			    }
			} else {
			    request.setAttribute("resultados", "Producto no disponible");
			    Tools.anadirMensaje(request, "El producto elegido no existe");
			}
        }
        request.getRequestDispatcher("/shop/products.jsp").forward(request, response);
	
	}
	
	protected boolean validateForm(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 2 && request.getParameter("prod") != null && request.getParameter("cant") != null) {
            return Tools.validateUUID(request.getParameter("prod"));
        } else {
            return false;
        }
    }

}
