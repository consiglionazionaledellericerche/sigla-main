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

package it.cnr.contab.devutil00.action;

import it.cnr.contab.devutil00.bp.*;
import it.cnr.contab.devutil00.bulk.*;
/**
 * Insert the type's description here.
 * Creation date: (11/07/2001 15:45:52)
 * @author: CNRADM
 */
public class RequestTraceAction extends it.cnr.jada.util.action.BulkAction {
	private java.lang.String newTraceUser;
	private java.lang.String[] tracingUsers;
/**
 * SQLTraceAction constructor comment.
 */
public RequestTraceAction() {
	super();
}
public it.cnr.jada.action.Forward doAddUser(it.cnr.jada.action.ActionContext context) {
	try {
		RequestTraceBP bp = (RequestTraceBP)context.getBusinessProcess();
		if (newTraceUser != null)
			context.addRequestTracingUser(newTraceUser.toUpperCase());
		bp.refresh(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public it.cnr.jada.action.Forward doAggiorna(it.cnr.jada.action.ActionContext context) {
	try {
		RequestTraceBP bp = (RequestTraceBP)context.getBusinessProcess();
		bp.refresh(context);
		return context.findDefaultForward();
	} catch(it.cnr.jada.action.BusinessProcessException e) {
		return handleException(context,e);
	} 
}
public it.cnr.jada.action.Forward doRemoveUsers(it.cnr.jada.action.ActionContext context) {
	try {
		RequestTraceBP bp = (RequestTraceBP)context.getBusinessProcess();
		if (tracingUsers != null)
			for (int i = 0;i < tracingUsers.length;i++)
				context.removeRequestTracingUser(tracingUsers[i]);
		bp.refresh(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (12/07/2001 13:52:30)
 * @return java.lang.String
 */
public java.lang.String getNewTraceUser() {
	return newTraceUser;
}
/**
 * 
 * @return java.lang.String[]
 */
public java.lang.String[] getTracingUsers() {
	return tracingUsers;
}
/**
 * Insert the method's description here.
 * Creation date: (12/07/2001 13:52:30)
 * @param newNewTraceUser java.lang.String
 */
public void setNewTraceUser(java.lang.String newNewTraceUser) {
	newTraceUser = newNewTraceUser;
}
/**
 * 
 * @param newTracingUsers java.lang.String[]
 */
public void setTracingUsers(java.lang.String[] newTracingUsers) {
	tracingUsers = newTracingUsers;
}
}
