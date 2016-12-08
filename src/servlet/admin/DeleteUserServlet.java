package servlet.admin;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;

import modelo.Usuario;
import control.Tools;
import dao.AdminDao;
import dao.CarritoDao;
import dao.UsuarioDao;

/**
 * Servlet implementation class DeleteUserServlet
 */
@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return "Servlet para el borrado de un cliente";
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
            try {
                String email = request.getParameter("user");
                request.setAttribute("resultados", "Resultados de la operación");
                Usuario user = UsuarioDao.getInstance().getUser(email);
                if (user != null) {
                    if (AdminDao.getInstance().anyAdmin() == 1 && user.getPermisos() == 'a') {
                        Tools.anadirMensaje(request, "El usuario elegido es el único administrador que queda, no se puede borrar");                        
                    } else {
                        boolean ok = UsuarioDao.getInstance().delUser(email);
                        CarritoDao.getInstance().deleteImcompleteCartsClient(email);
                        if (ok == true) {
                            Tools.anadirMensaje(request, "El usuario se ha borrado correctamente");
                            //Lo que he añadido para que administrador que se borra a si mismo salga
                            if (email.equals((String)request.getSession().getAttribute("usuario")) == true){
                                response.sendRedirect("/logout");
                                return;
                            }
                        }else{
                            Tools.anadirMensaje(request, "Ha ocurrido un error borrando el usuario");
                        }
                    }
                } else {
                    Tools.anadirMensaje(request, "El usuario seleccionado no se ha encontrado, imposible borrar");
                }
            } catch (SQLException ex) {
                Tools.anadirMensaje(request, ex.getLocalizedMessage());
            }finally{
                request.getRequestDispatcher("/admin/administration/user_administration.jsp").forward(request, response);
            }
        } else {
            response.sendError(404);
        }
    }
	
	 protected boolean validar(HttpServletRequest request) {
	        if (request.getParameterMap().size() >= 1 && request.getParameter("user") != null) {
	            return true;
	        } else {
	            return false;
	        }
	    }


}
