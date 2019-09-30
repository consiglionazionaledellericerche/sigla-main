/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
