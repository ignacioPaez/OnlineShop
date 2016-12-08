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
import dao.UsuarioDao;

/**
 * Servlet implementation class EditUserCompleteServlet
 */
@WebServlet("/EditUserCompleteServlet")
public class EditUserCompleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditUserCompleteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return "Servlet para la edición de clientes (usado por el administrador)"; 
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
        if (validateForm(request) == true) {
            try {
                String mail = request.getParameter("mail");
                String nombre = request.getParameter("nombre");
                String dir = request.getParameter("dir");
                char perm = request.getParameter("perm").charAt(0);
                Usuario user = UsuarioDao.getInstance().getUser(mail);
                if (user == null) {
                    request.setAttribute("resultados", "Usuario no encontrado");
                    Tools.anadirMensaje(request, "El usuario que quiere editar no ha sido encontrado");
                } else {
                    Usuario updateUser = new Usuario(nombre, dir, user.getMail(), user.getPass(), perm);
                    if (AdminDao.getInstance().anyAdmin() == 1 && user.getPermisos() == 'a' &&
                            updateUser.getPermisos() == 'c') {
                        request.setAttribute("resultados", "Error editando permisos");
                        Tools.anadirMensaje(request, "Este usuario es el último administrador, no puede cambiar sus permisos");
                    } else {
                        boolean ok = UsuarioDao.getInstance().updateUser(user.getMail(), updateUser);
                        if (ok == true) {
                            request.setAttribute("resultados", "Usuario editado correctamente");
                            Tools.anadirMensaje(request, "El usuario ha sido editado correctamente");
                        } else {
                            request.setAttribute("resultados", "Operación fallida");
                            Tools.anadirMensaje(request, "Ocurrió un error editando el usuario, es posible que el usuario que desea editar no exista");
                        }
                    }
                }
            } catch ( SQLException ex) {
                request.setAttribute("resultados", "Intrusión detectada");
                Tools.anadirMensaje(request, ex.getLocalizedMessage());
            } finally {
                request.getRequestDispatcher("/admin/administration/user_administration.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("resultados", "Formulario incorrecto");
            Tools.anadirMensaje(request, "El formulario recibido no es correcto");
            request.getRequestDispatcher("/admin/administration/user_administration.jsp").forward(request, response);
        }
    }

    protected boolean validateForm(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 5 && request.getParameter("nombre") != null && 
                request.getParameter("dir") != null && request.getParameter("perm") != null &&
                request.getParameter("edit") != null && request.getParameter("mail") != null &&
                Tools.validatePermisos(request.getParameter("perm").charAt(0))) {
            return true;
        } else {
            return false;
        }
    }

}
