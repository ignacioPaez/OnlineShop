package control;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import dao.RequestDao;

/**
 * Servlet Filter implementation class LogFilter
 */
@WebFilter("/LogFilter")
public class LogFilter implements Filter {

    /**
     * Default constructor. 
     */
    public LogFilter() {
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
		response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        HttpServletRequest modRequest = (HttpServletRequest) request;                
        String fechaHora = Tools.getDate();        
        String requestedURL = modRequest.getRequestURL().toString();
        String remoteAddr = modRequest.getRemoteAddr();
        String remoteHost = modRequest.getRemoteHost();
        String method = modRequest.getMethod();
        String param = getParamString(modRequest);
        String userAgent = modRequest.getHeader("user-agent");        
        boolean ok = RequestDao.getInstance().saveRequest(fechaHora, requestedURL, remoteAddr, remoteHost,
                method, param, userAgent);
        if (ok == false){
            Logger.getLogger(LogFilter.class.getName()).log(Level.INFO, "No se ha guardado el log de petición en la BD");
        }        
        chain.doFilter(request, response);        
	}
	
	private String getParamString (HttpServletRequest request){
        StringBuilder param = new StringBuilder("");
        
        Map <String, String []> parametros = request.getParameterMap();
        Iterator <String> iterador = parametros.keySet().iterator();        
        while (iterador.hasNext()){
            String key = iterador.next();
            if (key.toUpperCase().contains("PASS") == true){
                continue;
            }
            String  [] value = parametros.get(key);    
            for (int i = 0; i < value.length; i++){
                param.append("&");
                param.append(key);
                param.append("=");
                param.append(value[i]);
            }  
        }
        if (param.toString().equals("")){
            return null;
        }else{
            return param.toString();
        }
    }

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
