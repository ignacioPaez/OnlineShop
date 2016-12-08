package servlet.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.Producto;
import control.Tools;
import dao.ProductoDao;

/**
 * Servlet implementation class EditProductServlet
 */

@WebServlet("/EditProductServlet")
@MultipartConfig
public class EditProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditProductServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return "Servlet encargado de la edición de productos"; 
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
        if (validateForm(request) == true) {
            String codigo = Tools.getcontentPartText(request.getPart("codigo"));
			String nombre = Tools.getcontentPartText(request.getPart("name"));
			double precio = Double.parseDouble(Tools.getcontentPartText(request.getPart("price")));
			int nStock = Integer.parseInt(Tools.getcontentPartText(request.getPart("stock")));
			String descripcion = Tools.getcontentPartText(request.getPart("desc"));
			String detalles = Tools.getContentTextArea(request.getPart("detail"));
			//Tools.validateHTML(detalles);
			String rutaImagen = request.getServletContext().getRealPath("/images/" + codigo);
			///-----Tratar imagenes
			if (request.getPart("conserv") == null && Tools.existeElFichero(rutaImagen)) {
			    Tools.borrarImagenDeProdructoDelSistemaDeFicheros(rutaImagen);
			    if (request.getPart("foto").getSize() <= 0) {
			        Tools.anadirMensaje(request, "ADVERTENCIA: Se ha decidido no conservar la imagen anterior y no se ha seleccionado otra");

			    }
			}
			if (request.getPart("foto").getSize() > 0 && !Tools.recuperarYGuardarImagenFormulario(request, response, codigo)) {
			    return;
			}
			///-----Fin tratado de imagenes

			Producto prod = new Producto(codigo, nombre, precio, nStock, descripcion, detalles);
			boolean ok = ProductoDao.getInstance().updateProduct(prod.getCodigo(), prod);
			request.setAttribute("resultados", "Resultados de la operación");
			if (ok == true) {
			    Tools.anadirMensaje(request, "El producto se ha editado correctamente");
			} else {
			    Tools.anadirMensaje(request, "Ha ocurrido un error al modificar el producto. El producto no ha sido encontrado");
			}
			request.getRequestDispatcher("/admin/administration/products_administration.jsp").forward(request, response);
        }
    }

	
	 private boolean validateForm(HttpServletRequest request) throws IOException, ServletException {
	        if (request.getParts().size() >= 7 && request.getPart("codigo") != null && request.getPart("name") != null
	                && request.getPart("price") != null && request.getPart("stock") != null
	                && request.getPart("desc") != null && request.getPart("detail") != null
	                && request.getPart("sendProd") != null) {
	            return Tools.validateUUID(Tools.getcontentPartText(request.getPart("codigo")));
	        }
	        return false;
	    }

}
