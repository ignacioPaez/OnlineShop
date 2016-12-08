package servlet.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.Comentario;
import control.Tools;
import dao.ComentarioDao;

/**
 * Servlet implementation class EditCommentServlet
 */
@WebServlet("/EditCommentServlet")
public class EditCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditCommentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return "Servlet que permite a los administradores editar comentarios";
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
        String backEdit = (String)request.getSession().getAttribute("backTOEditComment");
        request.getSession().removeAttribute("backTOEditComment");        
        if (validateForm(request) == false){
            request.setAttribute("resultados", "Formulario incorrecto");
            Tools.anadirMensaje(request, "El formulario que ha enviado no es correcto");
            request.getRequestDispatcher("/admin/administration/editcomment.jsp?" + backEdit).forward(request, response);
            return;
        }
        String codComentario = request.getParameter("codComentario");
		String comentario = request.getParameter("comentario");
		//Tools.validateHTML(comentario);
   
		Comentario comentarioActual = ComentarioDao.getInstance().getComment(codComentario);
		Calendar cal = Calendar.getInstance(Tools.getLocale());
		String fecha = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH);
		String hora = cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
		Comentario newComment = new Comentario (codComentario, fecha, hora, comentarioActual.getCodigoProducto(), comentarioActual.getEmail(), comentarioActual.getNombre(), comentario);
		
		boolean ok = ComentarioDao.getInstance().updateComment(codComentario, newComment);
		if (ok == true){
		    request.setAttribute("resultados", "Operación completada");
		    Tools.anadirMensaje(request, "El comentario se ha modificado con éxito");
		    request.getRequestDispatcher("/shop/viewprod.jsp?" + back).forward(request, response);
		}else{
		    request.setAttribute("resultados", "Operación fallida");
		    Tools.anadirMensaje(request, "Ha ocurrido un error editando el comentario");
		    request.getRequestDispatcher("/shop/viewprod.jsp?" + back).forward(request, response);
		}
        
    }
    
    private boolean validateForm (HttpServletRequest request){
        if (request.getParameterMap().size() >= 3 && request.getParameter("codComentario") != null && request.getParameter("comentario") != null
                && request.getParameter("editComment") != null){
            return true;
        }
        return false;
    }

}
