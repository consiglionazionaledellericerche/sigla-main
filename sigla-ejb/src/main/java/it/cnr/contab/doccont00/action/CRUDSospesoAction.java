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

package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.*;
/**
 * Azione che gestisce le richieste relative alla Gestione Sospesi o Riscontri
 */
public class CRUDSospesoAction extends it.cnr.jada.util.action.CRUDAction {
public CRUDSospesoAction() {
	super();
}
/**
 * Gestisce la selezione del campo tipo entrata/spesa
 *
 */
public Forward doCambiaTipoEntrataSpesa(ActionContext context) 
{
	try 
	{ 	
		fillModel( context );
		CRUDSospesoBP bp = (CRUDSospesoBP)getBusinessProcess(context);
		bp.cambiaTipoEntrataSpesa( context );

		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce la selezione del campo tipo sospeso/riscontro
 *
 */
public Forward doCambiaTipoSospesoRiscontro(ActionContext context) 
{
	try 
	{ 	
		fillModel( context );
		CRUDSospesoBP bp = (CRUDSospesoBP)getBusinessProcess(context);
		bp.cambiaStato( context );

		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce il caricamento dei documenti contabili
 *
 */
public Forward doSearchFind_documento_cont(ActionContext context)
{
	try 
	{
		RemoteIterator ri;
		BulkInfo bulkInfo;
		FormField field;
		
		CRUDSospesoBP bp = (CRUDSospesoBP)context.getBusinessProcess();
		SospesoBulk sospeso = (SospesoBulk) bp.getModel();
		ri = bp.find(context,null,sospeso.getV_man_rev(),sospeso,"v_man_rev");	

		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
		if (ri == null || ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
			bp.setMessage("La ricerca non ha fornito alcun risultato.");
			return context.findDefaultForward();
		} 
		/*
		else if (ri.countElements() == 1) 
		{			
			doBringBackSearchResult(context,field,(OggettoBulk)ri.nextElement());
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(ri);
			return context.findDefaultForward();
		} */
		else 
		{
			SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
			nbp.setIterator(context,ri);
			bulkInfo = it.cnr.jada.bulk.BulkInfo.getBulkInfo(it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk.class);
			nbp.setBulkInfo(bulkInfo);
			nbp.setColumns(bulkInfo.getColumnFieldPropertyDictionary());
			context.addHookForward("seleziona",this,"doBringBackSearchResult");
			HookForward hook = (HookForward)context.findForward("seleziona");
			field = getFormField(context,"main.find_documento_cont");
			hook.addParameter("field",field);
			return context.addBusinessProcess(nbp);
		}
	} catch(Exception e) 
	{
		return handleException(context,e);
	}
}
}
