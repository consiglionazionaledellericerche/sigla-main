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

package it.cnr.contab.config00.action;

/**
 * Action che gestisce il cambiamento di stato dell'esercizio contabile.
 */
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.*;

public class CRUDEsercizioAction extends CRUDAction {
/**
 * Metodo che permette di cambiare lo stato dell'esercizio contabile.
 * @param  context <code>ActionContext</code> in uso.
 * @return Forward Oggetto restituito dal metodo <code>handleException</code>,
 *				   che gestisce le principali eccezioni.
 */
public Forward doApriPdG( ActionContext context)
{
	try 
	{
		boolean modified = fillModel(context);
		((it.cnr.contab.config00.bp.CRUDConfigEsercizioBP) getBusinessProcess(context)).apriPdg(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}

	
}
/**
 * Metodo che permette di cambiare lo stato dell'esercizio contabile.
 * @param  context <code>ActionContext</code> in uso.
 * @return Forward Oggetto restituito dal metodo <code>handleException</code>,
 *				   che gestisce le principali eccezioni.
 */
public Forward doCambiaStato( ActionContext context)
{
	try 
	{
		boolean modified = fillModel(context);
		((it.cnr.contab.config00.bp.CRUDConfigEsercizioBP) getBusinessProcess(context)).cambiaStato(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}

	
}
}
