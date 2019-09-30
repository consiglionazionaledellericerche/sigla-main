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

package it.cnr.contab.reports.action;

import it.cnr.contab.reports.bp.PrintSpoolerBP;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.util.Locale;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Insert the type's description here.
 * Creation date: (11/04/2002 17:28:27)
 * @author: CNRADM
 */
public class  PrintSpoolerAction extends it.cnr.jada.util.action.SelezionatoreListaAction {
	/**
	 * SpoolerStatusAction constructor comment.
	 */
	public PrintSpoolerAction() {
		super();
	}
	public Forward doCambiaVisibilita(ActionContext context) {
		PrintSpoolerBP bp = (PrintSpoolerBP)context.getBusinessProcess();
		Print_spoolerBulk print_spooler = (Print_spoolerBulk)bp.getModel();
		String ti_visibilita = print_spooler.getTiVisibilita();
		try {
			fillModel(context);
			bp.refresh(context);
			return context.findDefaultForward();
		} catch(Throwable e) {
			print_spooler.setTiVisibilita(ti_visibilita);
			return handleException(context,e);
		}
	}
	public Forward doDelete(ActionContext context) {
		try {
			PrintSpoolerBP bp = (PrintSpoolerBP)context.getBusinessProcess();
			bp.setSelection(context);
			Print_spoolerBulk[] array = null;
			if (!bp.getSelection().isEmpty()) {
				array = new Print_spoolerBulk[bp.getSelection().size()];
				int j = 0;
				for (it.cnr.jada.util.action.SelectionIterator i = bp.getSelection().iterator();i.hasNext();)
					array[j++] = (Print_spoolerBulk)bp.getElementAt(context,i.nextIndex());
			} else if (bp.getFocusedElement() != null) {
				array = new Print_spoolerBulk[1];
				array[0] = (Print_spoolerBulk)bp.getFocusedElement();
			}
			if (array != null){
				bp.createComponentSession().deleteJobs(context.getUserContext(),array);
				for (int i = 0;i < array.length;i++) {
					try {
						Print_spoolerBulk print = array[i];
						if (print.getServer() == null || print.getNomeFile() == null)
							continue;
						StringBuffer reportServerURL = new StringBuffer(print.getServer());
						HttpClient httpclient = HttpClientBuilder.create().build();
						reportServerURL.append("/").append(print.getUtcr());
						reportServerURL.append("/").append(print.getNomeFile());
						HttpDelete method = new HttpDelete(reportServerURL.toString());
						method.setHeader("Accept-Language", Locale.getDefault().toString());
						httpclient.execute(method);
					} catch(java.io.IOException e) {
						// Non posso fare molto... le stampe sono già state cancellate dalla
						// tabella della coda!!
					}
				}
			} else{
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare almeno una riga.");
			}
			bp.refresh(context);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doRefresh(ActionContext context) {
		try {
			PrintSpoolerBP bp = (PrintSpoolerBP)context.getBusinessProcess();
			bp.refresh(context);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	public Forward doSelection(ActionContext context,String name) {
		try {
			PrintSpoolerBP bp = (PrintSpoolerBP)context.getBusinessProcess();
			bp.setFocus(context);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
}