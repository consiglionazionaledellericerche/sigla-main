package it.cnr.contab.utenze00.action;

import java.io.Serializable;

import javax.servlet.http.*;

import it.cnr.jada.UserContext;
import it.cnr.jada.action.HttpActionContext;

public class UnregisterUser implements Serializable{ //HttpSessionBindingListener {
/**
 * UnregisterUser constructor comment.
 */
/*	private UserContext httpContext;
public UnregisterUser() {
}
public static void registerUnregisterUser(HttpActionContext context) {
	//context.getSession().setAttribute("UnregisterUserContext",new UnregisterUser());
	UnregisterUser user = new UnregisterUser();
	user.httpContext = context.getUserContext();
	context.getSession().setAttribute("UnregisterUserContext",user);
}
public void valueBound(HttpSessionBindingEvent event) {
}
public void valueUnbound(HttpSessionBindingEvent event) {
	try {
		//GestioneUtenteAction.getComponentSession().unregisterUser(HttpActionContext.getUserContext(event.getSession()));
		GestioneUtenteAction.getComponentSession().unregisterUser(httpContext);
	} catch(javax.ejb.EJBException e) {
	} catch(java.rmi.RemoteException e) {
	} catch(it.cnr.jada.comp.ComponentException e) {
	}
}*/
}
