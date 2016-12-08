package servlet.usuario;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    	    throws ServletException, IOException {
    	        if (request.getSession().getAttribute("auth") != null && (Boolean)request.getSession().getAttribute("auth") == true){
    	            request.getSession().invalidate();
    	            if (request.getAttribute("errorSesion") != null){
    	                request.getRequestDispatcher("/OnlineShop/login.jsp").forward(request, response);
    	                return;
    	            }
    	            request.getRequestDispatcher("/WEB-INF/admin/logout.jsp").forward(request, response);
    	        }else{
    	            response.sendRedirect("/OnlineShop/index.jsp");
    	        }
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
	
	public String getServletInfo(){
        return "Servlet encargado del cierre de sesi�n de usuarios";
    }

}
