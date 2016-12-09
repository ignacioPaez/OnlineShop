package servlet.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.Usuario;
import control.Tools;
import dao.UsuarioDao;

/**
 * Servlet implementation class ChangePassServlet
 */
@WebServlet("/ChangePassServlet")
@MultipartConfig
public class ChangePassServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangePassServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return "Servlet para cambiar contraseña del cliente (usado por los clientes)"; 
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
                String prevPass = request.getParameter("prevPass");
                prevPass = Tools.generateMD5Signature(prevPass + prevPass.toLowerCase());
                String newPass = request.getParameter("newPass");
                String repeatPass = request.getParameter("repeatPass");
                Usuario user = UsuarioDao.getInstance().getUser((String)request.getSession().getAttribute("usuario"));
                if (user != null) {
                    if (prevPass.equals(user.getPass()) == false) {
                        Tools.anadirMensaje(request, "La contraseña que ha introducido no es correcta");
                    } else if (newPass.equals(repeatPass) == false) {
                        Tools.anadirMensaje(request, "La contraseña no coincide con la repetición");
                    } else {
                        String huellaPass = Tools.generateMD5Signature(newPass + newPass.toLowerCase());
                        Usuario newUser = new Usuario (user.getNombre(), user.getDir(), user.getMail(), huellaPass, user.getPermisos());
                        boolean ok = UsuarioDao.getInstance().updateUser(user.getMail(), newUser);
                        if (ok == true){
                            Tools.anadirMensaje(request, "La contraseña ha sido cambiada con éxito");
                        }else{
                            Tools.anadirMensaje(request, "Ha ocurrido un error cambiando la contraseña");
                        }
                    }
                } else {
                    request.setAttribute("errorSesion", "");
                    request.setAttribute("resultados", "No se encontro el usuario de la sesion");
                    Tools.anadirMensaje(request, "No se ha encontrado el usuario activo y se ha cerrado la sesion");
                    request.getRequestDispatcher("/logout").forward(request, response);
                }
            } catch ( SQLException ex) {
                request.setAttribute("resultados", "Datos de formulario inválidos");
                Tools.anadirMensaje(request, ex.getMessage());
                request.getRequestDispatcher("/admin/preferences.jsp").forward(request, response);
            }
        }
        request.setAttribute("resultados", "Resultados de la operación");
        request.getRequestDispatcher("/admin/preferences.jsp").forward(request, response);
    }

    protected boolean validateForm(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        if (parameters.size() == 4 && parameters.containsKey("prevPass")
                && parameters.containsKey("newPass") && parameters.containsKey("repeatPass") && parameters.containsKey("changePass")) {
            return true;
        } else {
            Tools.anadirMensaje(request, "El formulario enviado no tiene la forma correta");
            return false;
        }
    }


}
