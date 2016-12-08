package servlet.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import control.Tools;
import dao.ComentarioDao;

/**
 * Servlet implementation class DeleteCommentServlet
 */
@WebServlet("/DeleteCommentServlet")
public class DeleteCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteCommentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return "Servlet para el borrado de comentarios";
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
        String back = (String) request.getSession().getAttribute("backTOURL");
        request.getSession().removeAttribute("backTOURL");
        if (validateForm(request) == false){
            response.sendError(404);
            return;
        }
        String codigoComentario = request.getParameter("cod");
        boolean ok = ComentarioDao.getInstance().deleteComment(codigoComentario);
        if (ok == true){
            request.setAttribute("resultados", "Operación completada");
            Tools.anadirMensaje(request, "El comentario se ha borrado correctamente");
        }else{
            request.setAttribute("resultados", "Operación fallida");
            Tools.anadirMensaje(request, "Ha ocurrido un error borrando el comentario, disculpe las molestias");
        }
        request.getRequestDispatcher("/shop/viewprod.jsp?" + back).forward(request, response); 
    }
    
    protected boolean validateForm (HttpServletRequest request){
        if (request.getParameterMap().size() >= 1 && request.getParameter("cod") != null){
            return Tools.validateUUID(request.getParameter("cod"));
        }
        return false;
    }

}
