package servlet.usuario;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sun.security.validator.ValidatorException;
import modelo.Carrito;
import modelo.Usuario;
import control.Tools;
import dao.CarritoDao;
import dao.UsuarioDao;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return "Servlet para la autentificación de usuarios";
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
        if (validateForm(request) == false) {
            request.setAttribute("resultados", "Error iniciando sesion");
            Tools.anadirMensaje(request, "El formulario enviado no es correcto");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else if (request.getSession().getAttribute("intentosLogin") != null && (Integer) request.getSession().getAttribute("intentosLogin") >= 5) {
            request.setAttribute("resultados", "Innicio de sesión bloqueado");
            Tools.anadirMensaje(request, "Se han superado el número de intentos de inicio de sesión, se ha bloqueado el incio de sesión");
            Tools.anadirMensaje(request, "Deberá esperar unos minutos para volver a intentarlo");
            this.starTimer(request.getSession());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            try {
                String email = request.getParameter("email");
                String password = request.getParameter("pass");

                Usuario user = UsuarioDao.getInstance().getUser(email);
                if (user != null) {
                    //Inicio correcto
                    if (Tools.generateMD5Signature(password
                            + password.toLowerCase()).equals(user.getPass()) == true) {
                        request.getSession().setAttribute("auth", true);
                        request.getSession().setAttribute("usuario", user.getMail());

                        Carrito carro = CarritoDao.getInstance().requestLastIncompleteCart(user.getMail());
                        if (carro != null) {
                            request.getSession().setAttribute("carrito", carro);
                        }
                        if (request.getSession().getAttribute("requestedPage") != null) {
                            String redirect = (String) request.getSession().getAttribute("requestedPage");
                            request.getSession().removeAttribute("requestedPage");
                            response.sendRedirect(redirect);
                        } else {
                            response.sendRedirect("./index.jsp");
                        }
                        request.getSession().removeAttribute("intentosLogin");
                        return;
                    //Contraseña incorrecta
                    } else {
                        Tools.anadirMensaje(request, "La contraseña introducida es incorrecta");
                        Tools.anadirMensaje(request, "Haga click <a href=\"/OnlineShop/PassRecoverServlet?email="
                                + user.getMail() + "\" >aquí</a> si olvidó la contraseña y desea recuperarla");
                    }
                } else {
                    Tools.anadirMensaje(request, "No se ha encontrado ningún usuario con los datos especificados");
                }
                this.incrementarIntentos(request.getSession());
                request.setAttribute("resultados", "Error inciando sesion");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } catch (IOException ex) {
                request.setAttribute("resultados", "Intrusión detectada");
                Tools.anadirMensaje(request, ex.getLocalizedMessage());
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } catch (SQLException ex) {
                request.setAttribute("resultados", "Datos de formulario no válidos");
                Tools.anadirMensaje(request, ex.getLocalizedMessage());
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        }
    }

    protected boolean validateForm(HttpServletRequest request) {
        Map<String, String[]> param = request.getParameterMap();
        if (param.size() == 3 && param.containsKey("email") && param.containsKey("pass")
                && param.containsKey("login")) {
            return true;
        } else {
            Tools.anadirMensaje(request, "El formulario enviado no tiene el formato correcto");
            return false;
        }
    }

    protected void starTimer(final HttpSession sesion) {
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                sesion.invalidate();
            }
        };

        Timer timer = new Timer();
        //10 minutos ---> 600.000 milisegundos
        timer.schedule(timerTask, 600000);
    }

    protected void incrementarIntentos(HttpSession sesion) {
        if (sesion.getAttribute("intentosLogin") == null) {
            sesion.setAttribute("intentosLogin", 1);
        } else {
            sesion.setAttribute("intentosLogin", (Integer) sesion.getAttribute("intentosLogin") + 1);
        }
    }

}
