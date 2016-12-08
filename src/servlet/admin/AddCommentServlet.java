package servlet.admin;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.Usuario;
import control.Tools;
import dao.ComentarioDao;
import dao.UsuarioDao;

/**
 * Servlet implementation class AddCommentServlet
 */
@WebServlet("/AddCommentServlet")
public class AddCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCommentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return "Servlet para añadir comentarios a un producto"; 
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
        String back = (String) request.getSession().getAttribute("backTOURL");
        request.getSession().removeAttribute("backTOURL");
        if (validateForm(request) == true){
            try{
                String codProd = request.getParameter("prod");
                String comentario = request.getParameter("comentario");
                //Tools.validateHTML(comentario);                
                Usuario user = UsuarioDao.getInstance().getUser((String) request.getSession().getAttribute("usuario"));
                
                boolean ok = ComentarioDao.getInstance().newComment(user, codProd, Tools.generaUUID(), 
                        Tools.getDate(), comentario);
                if (!ok){
                    request.setAttribute("resultados", "Error en la operación");
                    Tools.anadirMensaje(request, "Ha ocurrido un error en el transcurso de la operación");
                    request.getRequestDispatcher("/OnlineShop/shop/viewprod.jsp?" + back).forward(request, response);
                }else{
                    response.sendRedirect("/OnlineShop/shop/viewprod.jsp?" + back);
                }
            }catch (SQLException ex){                
                request.setAttribute("resultados", "HTML no válido");
                Tools.anadirMensaje(request, ex.getMessage());
                request.getRequestDispatcher("/OnlineShop/shop/viewprod.jsp?" + back).forward(request, response);
            }
        }else{
            request.setAttribute("resultados", "Formulario enviado incorrecto");
            Tools.anadirMensaje(request, "El formulario que ha enviado no es correcto");
            request.getRequestDispatcher("/OnlineShop/shop/viewprod.jsp?" + back).forward(request, response);
        }
    }
	
	protected boolean validateForm (HttpServletRequest request){
        if (request.getParameterMap().size() >= 3 && request.getParameter("comentario") != null 
                && request.getParameter("send") != null && request.getParameter("prod") != null){            
            return Tools.validateUUID(request.getParameter("prod"));
        }else{
            return false;
        }
    }

}
