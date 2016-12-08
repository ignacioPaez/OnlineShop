package control;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import modelo.Usuario;
import dao.UsuarioDao;

/**
 * Servlet Filter implementation class AdministrationFilter
 */
@WebFilter("/AdministrationFilter")
public class AdministrationFilter implements Filter {

    /**
     * Default constructor. 
     */
    public AdministrationFilter() {
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
		HttpSession sesion = ((HttpServletRequest)request).getSession();
        Usuario user = null;
		try {
			user = UsuarioDao.getInstance().getUser((String)sesion.getAttribute("usuario"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (user.getPermisos() == 'a'){
            chain.doFilter(request, response);
        }else{
            ((HttpServletResponse)response).sendRedirect("/admin/index.jsp");
        }
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
