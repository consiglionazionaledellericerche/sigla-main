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

import java.rmi.RemoteException;

import it.cnr.contab.anagraf00.bp.CRUDAbiCabBP;
import it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.compensi00.bp.EstrazioneINPSBP;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.inventario00.bp.CRUDUbicazioneBP;
import it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.contab.utenze00.bp.*;
import it.cnr.contab.utente00.nav.ejb.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.FormField;
import it.cnr.jada.util.action.SelezionatoreListaAlberoBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.ejb.EJBCommonServices;

import javax.servlet.http.HttpSessionBindingEvent;

/**
 * Action di gestione della selezione di unità organizzative di scrivania
 */

public class SelezionaCdsAction extends it.cnr.jada.util.action.BulkAction {
public SelezionaCdsAction() {
	super();
}
public Forward basicDoBringBack(ActionContext context) throws BusinessProcessException {
	try {
		SelezionaCdsBP scdsbp = (SelezionaCdsBP)context.getBusinessProcess();
		GestioneUtenteBP bp = (GestioneUtenteBP)context.getBusinessProcess("/GestioneUtenteBP");
		it.cnr.contab.config00.sto.bulk.CdrBulk cdr = null;//(it.cnr.contab.config00.sto.bulk.CdrBulk)scdrbp.getFocusedElement();
		if (cdr != null)
			((CNRUserInfo) context.getUserInfo()).setCdr(cdr);
		context.closeBusinessProcess();
		bp.cercaUnitaOrganizzative(context);
		return context.findForward("desktop");
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doSelezionaEsercizio(ActionContext context) {
	try {
		SelezionaCdsBP bp = (SelezionaCdsBP)context.getBusinessProcess();
		bp.getUserInfo().fillFromActionContext(context,null,it.cnr.jada.util.action.FormController.EDIT,bp.getFieldValidationMap());
		if (!bp.getUserInfo().getUtente().isUtenteComune()) {
			context.setUserContext(new CNRUserContext(
				bp.getUserInfo().getUtente().getCd_utente(),
				context.getSessionId(),
				bp.getUserInfo().getEsercizio(),
				null,
				null,
				null));
			return context.findForward("desktop");
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doSelezionaCds(ActionContext context) {

    try {
        fillModel(context);
        SelezionaCdsBP bp = (SelezionaCdsBP) context.getBusinessProcess();
		GestioneUtenteBP bpu = (GestioneUtenteBP)context.getBusinessProcess("/GestioneUtenteBP");

        SelezionaCdsBulk scds = (SelezionaCdsBulk)bp.getModel();
        Integer esercizio = bp.getUserInfo().getEsercizio();
        CdsBulk cds = scds.getCds();
        Unita_organizzativaBulk uo = scds.getUo();
        CdrBulk cdr = scds.getCdr();

		if (uo != null && uo.getCd_unita_organizzativa()!=null) {
			bp.completeSearchTools(context,bp.getController());
			doSearch(context,"main.find_uo");
		}
		else if (cds != null && cds.getCd_proprio_unita()!=null) {
			bp.completeSearchTools(context,bp.getController());
			doSearch(context,"main.find_cds");
		}
		else {
			bp.completeSearchTools(context,bp.getController());
		}

        scds = (SelezionaCdsBulk)bp.getModel();
        cds = scds.getCds();
        uo = scds.getUo();
        cdr = scds.getCdr();

        if (cds==null||cds.getCd_unita_organizzativa()==null)
			throw new MessageToUser("Il CdS deve essere valorizzato!",bp.ERROR_MESSAGE);
        if (uo==null||uo.getCd_unita_organizzativa()==null)
			throw new MessageToUser("L'unità Organizzativa deve essere valorizzata!",bp.ERROR_MESSAGE);
        if (cdr==null||cdr.getCd_centro_responsabilita()==null)
			throw new MessageToUser("Il CdR deve essere valorizzato!",bp.ERROR_MESSAGE);
        	
        bp.validaSelezionaCds(context, esercizio);
        
        SelezionatoreUnitaOrganizzativaBP bpsuo = (SelezionatoreUnitaOrganizzativaBP) bpu.cercaUnitaOrganizzative(context);
        bpsuo.selezionaUO(context, esercizio, uo, cdr);
		return context.findForward("desktop");
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
public static GestioneLoginComponentSession getGestioneLoginComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
	return (GestioneLoginComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession",GestioneLoginComponentSession.class);
}
protected Forward selectFromSearchResult(ActionContext actioncontext, FormField formfield, BulkInfo bulkinfo, RemoteIterator remoteiterator, String s)
{
    try
    {
        BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
        
		SelezionaCdsBulk scds = (SelezionaCdsBulk)bulkbp.getModel();
		

        remoteiterator = EJBCommonServices.openRemoteIterator(actioncontext, remoteiterator);
        if(remoteiterator == null || remoteiterator.countElements() == 0)
        {
            EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
            bulkbp.setMessage("La ricerca non ha fornito alcun risultato.");
            return actioncontext.findDefaultForward();
        }
        if(remoteiterator.countElements() == 1)
        {
            doBringBackSearchResult(actioncontext, formfield, (OggettoBulk)remoteiterator.nextElement());
            EJBCommonServices.closeRemoteIterator(actioncontext,remoteiterator);
            return actioncontext.findDefaultForward();
        } else
        {
            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP)actioncontext.createBusinessProcess("Selezionatore");
            selezionatorelistabp.setIterator(actioncontext, remoteiterator);
            selezionatorelistabp.setBulkInfo(bulkinfo);
            if (bulkinfo.getBulkClass().equals(CdsBulk.class))
            	selezionatorelistabp.setColumns(it.cnr.jada.bulk.BulkInfo.getBulkInfo(it.cnr.contab.config00.sto.bulk.CdsBulk.class).getColumnFieldPropertyDictionary("scrivania"));
            else if (bulkinfo.getBulkClass().equals(Unita_organizzativaBulk.class))
            	selezionatorelistabp.setColumns(it.cnr.jada.bulk.BulkInfo.getBulkInfo(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class).getColumnFieldPropertyDictionary("scrivania"));
            else if (bulkinfo.getBulkClass().equals(CdrBulk.class))
            	selezionatorelistabp.setColumns(it.cnr.jada.bulk.BulkInfo.getBulkInfo(it.cnr.contab.config00.sto.bulk.CdrBulk.class).getColumnFieldPropertyDictionary("scrivania"));
            else
            	selezionatorelistabp.setColumns(bulkinfo.getColumnFieldPropertyDictionary(s));
            actioncontext.addHookForward("seleziona", this, "doBringBackSearchResult");
            HookForward hookforward = (HookForward)actioncontext.findForward("seleziona");
            hookforward.addParameter("field", formfield);
            return actioncontext.addBusinessProcess(selezionatorelistabp);
        }
    }
    catch(Exception exception)
    {
        return handleException(actioncontext, exception);
    }
}
public Forward doBringBackSearchResult(ActionContext actioncontext) throws RemoteException {
	return super.doBringBackSearchResult(actioncontext);
}
public Forward doBringBackSearchFind_cds(ActionContext context, SelezionaCdsBulk scds, CdsBulk cds) {
	try {
		if (cds!=null){
			SelezionaCdsBP bp = (SelezionaCdsBP)context.getBusinessProcess();
	
			scds.setCds(cds);
			bp.findCds(context);
		}
		return context.findDefaultForward();
	}catch(BusinessProcessException ex){
		return handleException(context, ex);
	}
}
public Forward doBringBackSearchFind_uo(ActionContext context, SelezionaCdsBulk scds, Unita_organizzativaBulk uo) {
	try {
		if (uo!=null){
			SelezionaCdsBP bp = (SelezionaCdsBP)context.getBusinessProcess();
	
			scds.setUo(uo);
			bp.findUo(context);
		}
		return context.findDefaultForward();
	}catch(BusinessProcessException ex){
		return handleException(context, ex);
	}
}
}
