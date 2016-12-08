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
 * Servlet implementation class AddProductServlet
 */
@WebServlet("/AddProductServlet")
@MultipartConfig
public class AddProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddProductServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		 return "Servlet para añadir productos al catálogo"; 
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
	
        if (validateForm(request)) {
            try {
                String nombre = Tools.getcontentPartText(request.getPart("name"));
                double precio = Double.parseDouble(Tools.getcontentPartText(request.getPart("price")));
                int nStock = Integer.parseInt(Tools.getcontentPartText(request.getPart("stock")));
                String descripcion = Tools.getcontentPartText(request.getPart("desc"));
                String detalles = Tools.getContentTextArea(request.getPart("detail"));
            	
            	/*String nombre = request.getParameter("name");
                double precio = Double.parseDouble(request.getParameter("price"));
                int nStock = Integer.parseInt(request.getParameter("stock"));
                String descripcion = request.getParameter("desc");
                String detalles = request.getParameter("detail");*/
                String codigo = Tools.generaUUID();
                
                ////----Guardar Imagen si hay, si hay error guardando se aborta y notifica
                if (request.getPart("foto").getSize() > 0 && !Tools.recuperarYGuardarImagenFormulario(request, response, codigo)) {
                    return;
                }
                ///-----Fin tratado de imagen

                Producto prod = new Producto(codigo, nombre, precio, nStock, descripcion, detalles);
                boolean ok = ProductoDao.getInstance().addProduct(prod);
                request.setAttribute("resultados", "Resultados de la operación");
                if (ok) {
                    Tools.anadirMensaje(request, "El producto se ha añadido correctamente");
                } else {
                    Tools.anadirMensaje(request, "Ha ocurrido un error al añadir el producto. El producto está duplicado. Inténtelo de nuevo");
                }
                request.getRequestDispatcher("/admin/administration/products_administration.jsp").forward(request, response);
            } catch (IOException ex) {
                request.setAttribute("resultados", "Intrusión detectada");
                Tools.anadirMensaje(request, ex.getLocalizedMessage());
                request.getRequestDispatcher("/admin/administration/addproduct.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("resultados", "Formulario no válido");
            Tools.anadirMensaje(request, "El formulario recibido no tiene los campos esperados");
            request.getRequestDispatcher("/admin/administration/addproduct.jsp").forward(request, response);
        }
	}
	
	protected boolean validateForm(HttpServletRequest request) throws IOException, ServletException {
        if (request.getParts().size() >= 6 && request.getPart("name") != null && request.getPart("price") != null
                && request.getPart("stock") != null && request.getPart("desc") != null
                && request.getPart("detail") != null && request.getPart("sendProd") != null) {
            return true;
        }
        return false;
    }

}
