package servlet.admin;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import control.Tools;
import dao.ProductoDao;

/**
 * Servlet implementation class DeleteProductServlet
 */
@WebServlet("/DeleteProductServlet")
public class DeleteProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteProductServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return "Servlet para borrado de un producto"; 
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
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (validar(request) == true) {
            request.setAttribute("resultados", "Resultados de la operación");
            boolean ok = ProductoDao.getInstance().delProduct(request.getParameter("prod"));

            if (ok == true) {
                Tools.anadirMensaje(request, "El producto ha sido borrado correctamente");
            } else {
                Tools.anadirMensaje(request, "Ha ocurrido un error borrando el producto");
            }
           String rutaDeLaImagen = request.getServletContext().getRealPath("/images/" +
                   request.getParameter("cod"));
            if (Tools.existeElFichero(rutaDeLaImagen)) {
                boolean image = Tools.borrarImagenDeProdructoDelSistemaDeFicheros(rutaDeLaImagen);
                if (image) {
                    Tools.anadirMensaje(request, "Se ha borrado correctamente la imagen del producto");
                } else {
                    Tools.anadirMensaje(request, "Hubo un error borrando la imagen del producto");
                }
            }            
            RequestDispatcher borrado = request.getRequestDispatcher("/admin/administration/products_administration.jsp");
            borrado.forward(request, response);
        } else {
            response.sendError(404);
        }
    }
	
	protected boolean validar(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 1 && request.getParameter("prod") != null) {
            return Tools.validateUUID(request.getParameter("prod"));
        } else {
            return false;
        }
    }

}
