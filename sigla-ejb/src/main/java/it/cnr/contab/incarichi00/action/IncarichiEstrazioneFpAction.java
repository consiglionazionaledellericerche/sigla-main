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

package it.cnr.contab.incarichi00.action;

import it.cnr.contab.incarichi00.bp.CRUDIncarichiEstrazioneFpBP;
import it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_archivio_xml_fpBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.ConsultazioniBP;

import java.util.List;

public class IncarichiEstrazioneFpAction extends it.cnr.jada.util.action.CRUDAction{
	public IncarichiEstrazioneFpAction() {
		super();
	}

	public Forward doGeneraXML(ActionContext context) {
		try {
			fillModel(context);
			CRUDIncarichiEstrazioneFpBP bp = (CRUDIncarichiEstrazioneFpBP)context.getBusinessProcess();
			bp.generaXML(context);

			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public Forward doSelezionaIncarichidaEstrarre(ActionContext context) {
		try {
			fillModel(context);
			CRUDIncarichiEstrazioneFpBP bp = (CRUDIncarichiEstrazioneFpBP)context.getBusinessProcess();
			Incarichi_archivio_xml_fpBulk archivioXmlFP = (Incarichi_archivio_xml_fpBulk)bp.getModel();

			if (archivioXmlFP==null || archivioXmlFP.getCrudStatus()==CRUDBP.SEARCH){
				bp.setMessage("Non è stato selezionato alcun periodo per effettuare l'estrazione.");
				return context.findDefaultForward();
			} else if (archivioXmlFP.getEsercizio()==null){
				bp.setMessage("Non è stato selezionato l'anno per effettuare l'estrazione.");
				return context.findDefaultForward();
			} else if (archivioXmlFP.getDt_calcolo()==null){
				bp.setMessage("Non è stato selezionato il tipo di data da utilizzare per effettuare l'estrazione.");
				return context.findDefaultForward();
			} 

			ConsultazioniBP newBp = bp.getConsIncarichiEstrazioneFpBP(context);
			newBp.openIterator(context);
			context.addHookForward("generaXML",this,"doBringBackSelezionaIncarichidaEstrarre");			
			return context.addBusinessProcess(newBp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	@SuppressWarnings("unchecked")
	public Forward doBringBackSelezionaIncarichidaEstrarre(ActionContext context) {
		try {
			CRUDIncarichiEstrazioneFpBP bp = (CRUDIncarichiEstrazioneFpBP)context.getBusinessProcess();
			HookForward hook = (HookForward)context.getCaller();
			List arraylist = (List)hook.getParameter("selectedElements");
			if (((Incarichi_archivio_xml_fpBulk)bp.getModel()).isFl_crea_file_perla())
				bp.generaXMLPerla(context, arraylist);
			else
				bp.generaXML(context, arraylist);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public Forward doClearSelection(ActionContext context) {
		try {
			fillModel(context);
			CRUDIncarichiEstrazioneFpBP bp = (CRUDIncarichiEstrazioneFpBP)context.getBusinessProcess();
			if (bp.getModel() instanceof Incarichi_archivio_xml_fpBulk)
				bp.clearSelection(context, (Incarichi_archivio_xml_fpBulk)bp.getModel());

			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
}
