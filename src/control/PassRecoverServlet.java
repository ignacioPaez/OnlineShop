package control;

import java.io.IOException;
import java.sql.SQLException;

import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.apache.commons.lang.RandomStringUtils;

import modelo.Usuario;
import dao.UsuarioDao;

/**
 * Servlet implementation class PassRecoverServlet
 */
@WebServlet("/PassRecoverServlet")
public class PassRecoverServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PassRecoverServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return "Servlet para la restauración de contraseñas perdidas"; 
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
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (validateParam(request) == true) {
            try {
                String email = request.getParameter("email");
                Usuario user = UsuarioDao.getInstance().getUser(email);

                if (user == null) {
                    request.setAttribute("resultados", "Usuario no encontrado");
                    Tools.anadirMensaje(request, "Ha intentado recuperar la contraseña de un usuario que no existe");
                } else {
                    String newPass = RandomStringUtils.randomAlphanumeric(19);
                    String newPassHash = Tools.generateMD5Signature(newPass + newPass.toLowerCase());
                    Usuario newUser = new Usuario(user.getNombre(), user.getDir(), user.getMail(),
                            newPassHash, user.getPermisos());
                    
                    boolean cambioPass = UsuarioDao.getInstance().updateUser(user.getMail(), newUser);
                    if (cambioPass == true){
                        boolean mail = enviarMail(request, user, newPass);
                        if (mail == false){
                            request.setAttribute("resultados", "Ocurrió un error");
                            Tools.anadirMensaje(request, "Se intentó enviar su contraseña por email pero no fue posible, inténtelo de nuevo, disculpe las molestias");
                        }
                    }else{
                        request.setAttribute("resultados", "Ha ocurrido un error");
                        Tools.anadirMensaje(request, "Error en el proceso de recuperación de la contraseña, inténtelo de nuevo");
                    }
                }
            } catch (SQLException ex) {
                request.setAttribute("resultados", "Error en la validación");
                Tools.anadirMensaje(request, ex.getMessage());
            } finally {
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("resultados", "Formulario no valido");
            Tools.anadirMensaje(request, "El formulario recibido no es correcto");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    protected boolean enviarMail(HttpServletRequest request, Usuario user, String newPass) {
        EnviarMail mailConfig = (EnviarMail) request.getServletContext().getAttribute("EmailSend");
        Session mailSession = mailConfig.startSession((Authenticator) request.getServletContext().getAttribute("autorizacionMail"));
        String contenido = Tools.leerArchivoClassPath("/control/plantillaRecuperarPass.html");
        contenido = contenido.replace("&NAME", user.getNombre());
        contenido = contenido.replace("&EMAIL", user.getMail());
        contenido = contenido.replace("&PASS", newPass);
        MimeMessage mensaje = mailConfig.newMail("Recuperación de contraseña tienda online", user.getMail(), contenido, mailSession);
        if (mensaje == null) {
            request.setAttribute("resultados", "Error enviando mensaje");
            Tools.anadirMensaje(request, "No se pudo enviar su email, disculpe las molestias");
            return false;
        } else {
            boolean ok = mailConfig.sendEmail(mensaje, mailSession);

            if (ok == true) {
                request.setAttribute("resultados", "Mensaje enviado correctamente");
                Tools.anadirMensaje(request, "Se ha enviado a su dirección de correo una nueva contraseña");
                return true;
            } else {
                request.setAttribute("resultados", "Error enviando mensaje");
                Tools.anadirMensaje(request, "No se pudo enviar su email, disculpe las molestias");
                return false;
            }
        }
    }

    protected boolean validateParam(HttpServletRequest request) {
        if (request.getParameterMap().size() >= 1 && request.getParameter("email") != null) {
            return true;
        }
        return false;
    }

}
