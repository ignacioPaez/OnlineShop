package control;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import modelo.Usuario;
import dao.UsuarioDao;

/**
 * Servlet Filter implementation class AdminFilter
 */
@WebFilter("/AdminFilter")
public class AdminFilter implements Filter {

    /**
     * Default constructor. 
     */
    public AdminFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest requestMod = ((HttpServletRequest) request);
        if (isPermited(requestMod) == false){
            requestMod.getSession().setAttribute("requestedPage", requestMod.getRequestURL().toString());
            RequestDispatcher noPermited = request.getRequestDispatcher("/WEB-INF/paginasError/restricted.jsp");
            noPermited.forward(request, response);
        }else{
            chain.doFilter(request, response);
        }
	}
	
	private boolean isPermited(HttpServletRequest request) {      
        if (request.getSession().getAttribute("auth") == null && request.getSession().getAttribute("usuario") == null) {
            return false;
        } else if ((Boolean)request.getSession().getAttribute("auth")){
            Usuario user = null;
			try {
				user = UsuarioDao.getInstance().getUser((String)request.getSession().getAttribute("usuario"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (user == null){
                return false;
            }else{
                return true;
            }
        }
        return false;
    }

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
