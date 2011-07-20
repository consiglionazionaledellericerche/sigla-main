/*
 * Created on Jul 21, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.action;

import java.io.File;
import java.io.FileOutputStream;

import it.cnr.contab.pdg00.bp.PdGVariazioneBP;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazione_archivioBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazione_archivioHome;
import it.cnr.contab.pdg00.cdip.bulk.V_assestato_modulo_var_pdgBulk;
import it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession;
import it.cnr.contab.util.ExcelFile;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.blobs.bp.ExcelBlobBP;
import it.cnr.jada.blobs.ejb.BlobComponentSession;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.SelezionatoreListaBP;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PdGConsultazioneAssestatoAction extends ConsultazioniAction {
	public it.cnr.jada.action.Forward doArchive(ActionContext actioncontext) throws it.cnr.jada.action.BusinessProcessException {
		try{
			SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP)actioncontext.getBusinessProcess();
			PdGVariazioneBP pdgvariazionebp = (PdGVariazioneBP)selezionatorelistabp.getParent();
			Pdg_variazioneBulk pdg_variazione = (Pdg_variazioneBulk)pdgvariazionebp.getModel();

			String tipoConsultazione = null;
			if (actioncontext.getBusinessProcess().getName().equals("ConsultazioneAssestatoEntrateModuloPdgBP")) 
			    tipoConsultazione = Pdg_variazione_archivioBulk.SITUAZIONE_ASSESTATO_ENTRATA;
			else if (actioncontext.getBusinessProcess().getName().equals("ConsultazioneAssestatoRicaviModuloPdgBP"))
				tipoConsultazione = Pdg_variazione_archivioBulk.SITUAZIONE_ASSESTATO_RICAVI;
			else if (actioncontext.getBusinessProcess().getName().equals("ConsultazioneAssestatoSpeseModuloPdgBP"))
				tipoConsultazione = Pdg_variazione_archivioBulk.SITUAZIONE_ASSESTATO_SPESA;
			else if (actioncontext.getBusinessProcess().getName().equals("ConsultazioneAssestatoCostiModuloPdgBP"))
				tipoConsultazione = Pdg_variazione_archivioBulk.SITUAZIONE_ASSESTATO_COSTI;
			else
				tipoConsultazione = Pdg_variazione_archivioBulk.SITUAZIONE_ASSESTATO_ENTRATA;
 
			String longDescription = "Selezione";
			try{
				longDescription = selezionatorelistabp.getBulkInfo().getLongDescription();
				if (longDescription.length() > 30)
				  longDescription = longDescription.substring(0,30); 
			}
			catch(java.lang.NullPointerException e){
			}		    
 
			File file= File.createTempFile(longDescription,".xls");  

		    RemoteIterator remoteiterator = selezionatorelistabp.detachIterator();
			it.cnr.jada.util.ejb.HttpEJBCleaner.unregister(actioncontext,remoteiterator);

			((ExcelFile)file).caricaFile(longDescription,selezionatorelistabp.getColumns(),remoteiterator, actioncontext.getUserContext().getUser());

			PdGVariazioniComponentSession pdGVariazioniComponentSession = (PdGVariazioniComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG00_EJB_PdGVariazioniComponentSession",PdGVariazioniComponentSession.class);
			pdGVariazioniComponentSession.archiviaConsultazioneExcel(actioncontext.getUserContext(), pdg_variazione, tipoConsultazione, file);

			//file.delete();

			actioncontext.closeBusinessProcess();
			
			pdg_variazione = (Pdg_variazioneBulk)pdGVariazioniComponentSession.inizializzaBulkPerModifica(actioncontext.getUserContext(), pdg_variazione);
			pdgvariazionebp.edit(actioncontext, pdg_variazione);
			pdgvariazionebp.setDirty(false);
			pdgvariazionebp.setMessage("Archiviazione effettuata con successo.");

			return actioncontext.findDefaultForward();
		}
		catch(Throwable throwable){
			return handleException(actioncontext,throwable);
		} 
	}
}
