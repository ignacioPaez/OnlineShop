package servlet.admin;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.security.validator.ValidatorException;
import control.Tools;
import dao.UsuarioDao;
import modelo.Usuario;

/**
 * Servlet implementation class EditUserServlet
 */
@WebServlet("/EditUserServlet")
public class EditUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return "Servlet para edición de los datos de un cliente (usado por los clientes)"; 
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
		 processRequest(request, response);
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (validateForm(request)) {
            try {
                
            	Usuario user = UsuarioDao.getInstance().getUser((String) request.getSession().getAttribute("usuario"));
            	//Usuario user = UsuarioDao.getInstance().getUser(request.getParameter("mail"));
            	//request.getSession().setAttribute("usermail", request.getParameter("mail"));
            	String nombre = request.getParameter("name");
                String dir = request.getParameter("dir");                
                Usuario newUser = new Usuario (nombre, dir, user.getMail(), user.getPass(), user.getPermisos());
                boolean ok = UsuarioDao.getInstance().updateUser(user.getMail(), newUser);
                if (ok){
                    request.setAttribute("resultados", "Resultados de la operación");
                    Tools.anadirMensaje(request, "Los datos han sido modificados correctamente");
                }else{
                    request.setAttribute("resultados", "Resultados de la operación");
                    Tools.anadirMensaje(request, "Ha ocurrido un error modificando el usuario");
                }

            } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
                request.getRequestDispatcher("/admin/preferences.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("resultados", "Formulario incorrecto");
            Tools.anadirMensaje(request, "El formulario enviado no es correcto");
            request.getRequestDispatcher("/admin/preferences.jsp").forward(request, response);
        }
    }

    protected boolean validateForm(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 3 && request.getParameter("name") != null && 
                request.getParameter("dir") != null  && request.getParameter("changeData") != null) {
            return true;
        } else {
            return false; //return false no funciona
        }
    }

}
