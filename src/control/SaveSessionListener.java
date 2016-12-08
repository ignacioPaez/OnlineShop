package control;

import java.sql.SQLException;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import modelo.Carrito;
import modelo.Usuario;
import dao.CarritoDao;
import dao.UsuarioDao;

/**
 * Application Lifecycle Listener implementation class SaveSessionListener
 *
 */
@WebListener
public class SaveSessionListener implements HttpSessionListener {

    /**
     * Default constructor. 
     */
    public SaveSessionListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent se)  { 
         // TODO Auto-generated method stub
    	HttpSession session = se.getSession();
        if (session.getAttribute("auth") != null && (Boolean)session.getAttribute("auth") == true &&
                session.getAttribute("usuario") != null){
            Usuario user = null;
			try {
				user = UsuarioDao.getInstance().getUser((String)session.getAttribute("usuario"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            Carrito cart = (Carrito) session.getAttribute("carrito");
            if (user != null && cart != null && cart.getLenght() > 0){
                CarritoDao.getInstance().deleteImcompleteCartsClient(user.getMail());
                cart.setUser(user.getMail());
                CarritoDao.getInstance().saveCart(cart, false, Tools.getDate(), null);
            }
        }
    }
	
}
