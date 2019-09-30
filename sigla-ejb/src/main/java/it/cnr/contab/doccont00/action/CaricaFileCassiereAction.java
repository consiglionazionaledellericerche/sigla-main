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

import it.cnr.contab.config00.ejb.EsercizioComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.doccont00.bp.DettagliFileCassiereBP;
import it.cnr.contab.doccont00.intcass.bulk.Ext_cassiere00Bulk;
import it.cnr.contab.doccont00.intcass.bulk.Ext_cassiere00_scartiBulk;
import it.cnr.contab.doccont00.intcass.bulk.V_ext_cassiere00Bulk;
import it.cnr.contab.doccont00.bp.CaricaFileCassiereBP;
import it.cnr.contab.doccont00.ejb.DistintaCassiereComponentSession;
import it.cnr.contab.preventvar00.consultazioni.bp.ConsAssCompPerDataDettagliBP;
import it.cnr.contab.preventvar00.consultazioni.bulk.V_cons_var_pdggBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.blobs.bulk.Bframe_blobBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.RicercaLiberaBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.upload.UploadedFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.StringTokenizer;

/**
 * Insert the type's description here.
 * Creation date: (10/04/2003 12.04.09)
 * @author: Gennaro Borriello
 */
public class CaricaFileCassiereAction extends it.cnr.jada.util.action.SelezionatoreListaAction {
/**
 * CaricaFileCassiereAction constructor comment.
 */
public CaricaFileCassiereAction() {
	super();
}
public Forward doBringBack(ActionContext context) {
	return context.findDefaultForward();
}
/**
  * Richiamato per la procedura di upload del nuovo File Cassiere.
  *	
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
public Forward doCaricaFile(ActionContext context) {

	it.cnr.jada.action.HttpActionContext httpContext = (it.cnr.jada.action.HttpActionContext)context;
	
	UploadedFile file =httpContext.getMultipartParameter("fileCassiere");
	try {
		if (file == null || file.getName().equals(""))
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare un File da caricare.");

		CaricaFileCassiereBP bp = (CaricaFileCassiereBP)httpContext.getBusinessProcess();
		((it.cnr.contab.doccont00.ejb.DistintaCassiereComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_DistintaCassiereComponentSession",it.cnr.contab.doccont00.ejb.DistintaCassiereComponentSession.class)).caricaFile(context.getUserContext(),file.getFile());
		bp.setMessage("Operazione Completata!");
		bp.refresh(context);
		return context.findDefaultForward();
	}catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
  * Permette di filtrare i Files da visualizzare.
  *
  Richiama il metodo processaFile(UserContext, V_ext_cassiere00Bulk) della Componente,
	che invocherà la stored procedure <code>CNRCTB750.processaInterfaccia()</code>.
  
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doFiltraFiles(ActionContext context) {
	try {
		CaricaFileCassiereBP bp = (CaricaFileCassiereBP)context.getBusinessProcess();

		RicercaLiberaBP ricercaLiberaBP = (RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
		ricercaLiberaBP.setPrototype(new V_ext_cassiere00Bulk());
		context.addHookForward("filter",this,"doRiportaFiltraFiles");
		context.addHookForward("close",this,"doDefault");
		
		return context.addBusinessProcess(ricercaLiberaBP);

	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
  * Permette di elaborare il File selezionato dall'utente.
  *	Richiama il metodo processaFile(UserContext, V_ext_cassiere00Bulk) della Componente,
  *	che invocherà la stored procedure <code>CNRCTB750.processaInterfaccia()</code>.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doProcessaFile(ActionContext context) {
	try {
		fillModel(context);
		CaricaFileCassiereBP bp = (CaricaFileCassiereBP)context.getBusinessProcess();
		V_ext_cassiere00Bulk selected = (V_ext_cassiere00Bulk)bp.getFocusedElement();
		if (selected != null){
			bp.createComponentSession().processaFile(context.getUserContext(), selected);
		} else{
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare un File");
		}
		
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
  * Permette di filtrare i Files da visualizzare.
  *
  Richiama il metodo processaFile(UserContext, V_ext_cassiere00Bulk) della Componente,
	che invocherà la stored procedure <code>CNRCTB750.processaInterfaccia()</code>.
  
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doRiportaFiltraFiles(ActionContext context) {
	try {
		CaricaFileCassiereBP bp = (CaricaFileCassiereBP)context.getBusinessProcess();
		HookForward caller = (HookForward)context.getCaller();
	
		it.cnr.jada.persistency.sql.CompoundFindClause clauses = (it.cnr.jada.persistency.sql.CompoundFindClause)caller.getParameter("filter");
		
		it.cnr.jada.util.RemoteIterator iterator = bp.createComponentSession().cercaFile_Cassiere(context.getUserContext(), clauses);
		
		bp.setIterator(context, iterator);

		bp.setModel(context, null);
		
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}

/**
  * Permette di visualizzare i dettagli relativi al File selezionato dall'utente.
  *	Apre un'altra finestra, (DettagliFileCassiereBP), che ha come Iterator un Iteratore sui record 
  *	presi dalla tabella <code>EXT_CASSIERE00</code>.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doVisualizzaDettagli(ActionContext context) {	

	CaricaFileCassiereBP bp = (CaricaFileCassiereBP)context.getBusinessProcess();
	
	try{
		V_ext_cassiere00Bulk file = (V_ext_cassiere00Bulk)bp.getModel();
		if (file == null){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare un File");
		}
		
		it.cnr.jada.util.RemoteIterator ri = bp.createComponentSession().cerca(context.getUserContext(), null,new Ext_cassiere00Bulk(),  file, "DettagliFileCassiere");
		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
		if (ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: non ci sono dettagli");
		}
		DettagliFileCassiereBP nbp = (DettagliFileCassiereBP)context.createBusinessProcess("DettagliFileCassiereBP");
		nbp.setIterator(context,ri);
		nbp.setMultiSelection(false);
		nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Ext_cassiere00Bulk.class));
		return context.addBusinessProcess(nbp);		
	}
	catch (Throwable e){		
		return handleException(context,e);
	}

}
/**
  * Permette di visualizzare i Logs relativi al File selezionato dall'utente.
  *	Apre un'altra finestra, (CRUDTestataLogBP), che ha come Model l'oggetto 
  *	<code>it.cnr.contab.logs.bulk.Batch_log_tstaBulk</code>.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doVisualizzaLogs(ActionContext context) {	

	
	CaricaFileCassiereBP bp = (CaricaFileCassiereBP)context.getBusinessProcess();
	
	try{
		
		V_ext_cassiere00Bulk selected = (V_ext_cassiere00Bulk)bp.getFocusedElement();
		if (selected == null){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare un File");
		}
		
		it.cnr.contab.doccont00.intcass.bulk.Ext_cassiere00_logsBulk selected_log = (it.cnr.contab.doccont00.intcass.bulk.Ext_cassiere00_logsBulk)bp.getLogs().getModel();
		if (selected_log == null){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare una riga dalla tabella dei Logs");
		}
				
		it.cnr.contab.logs.bp.CRUDTestataLogBP tbp = (it.cnr.contab.logs.bp.CRUDTestataLogBP)context.createBusinessProcess("CRUDTestataLogBP");
		it.cnr.contab.logs.bulk.Batch_log_tstaBulk logBulk = new it.cnr.contab.logs.bulk.Batch_log_tstaBulk(selected_log.getPg_esecuzione());
		tbp.edit(context, logBulk);

		return context.addBusinessProcess(tbp);		
	}
	catch (Throwable e){		
		return handleException(context,e);
	}

}
/**
  * Gestisce un Eccezione di chiave duplicata.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
protected Forward handleApplicationPersistencyException(ActionContext context, it.cnr.jada.persistency.sql.ApplicationPersistencyException e) {

	it.cnr.jada.comp.ApplicationException mess = new it.cnr.jada.comp.ApplicationException(e.getMessage());

	return handleException(context, mess);
}
/**
  * Gestisce un Eccezione di chiave duplicata.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
protected Forward handleDuplicateKeyException(ActionContext context, it.cnr.jada.persistency.sql.DuplicateKeyException e) {

	it.cnr.jada.comp.ApplicationException mess = new it.cnr.jada.comp.ApplicationException("Si sta tentando di creare un oggetto già esistente in archivio.");

	return handleException(context, mess);
}

public Forward doVisualizzaLogScarti(ActionContext context) {	

	
	CaricaFileCassiereBP bp = (CaricaFileCassiereBP)context.getBusinessProcess();
	V_ext_cassiere00Bulk selected = (V_ext_cassiere00Bulk)bp.getFocusedElement();
	
	try{
	if (selected == null){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare un File");
		}
	
	it.cnr.contab.doccont00.intcass.bulk.Ext_cassiere00_logsBulk selected_log = (it.cnr.contab.doccont00.intcass.bulk.Ext_cassiere00_logsBulk)bp.getLogs().getModel();
	if (selected_log == null){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare una riga dalla tabella dei Logs");
	}
	
	
		it.cnr.jada.util.RemoteIterator ri = ((DistintaCassiereComponentSession)bp.createComponentSession()).selectFileScarti(context.getUserContext(),selected_log);
		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
		if (ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: Nessun dato disponibile.");
		}
		
		SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
		nbp.setIterator(context,ri);
		nbp.disableSelection();
		nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Ext_cassiere00_scartiBulk.class));
		nbp.setColumns(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Ext_cassiere00_scartiBulk.class).getColumnFieldPropertyDictionary("BASE"));
		
		HookForward hook = (HookForward)context.findForward("seleziona");		
		return context.addBusinessProcess(nbp);	

	}
	catch (Throwable e){		
		return handleException(context,e);
	}

}

}