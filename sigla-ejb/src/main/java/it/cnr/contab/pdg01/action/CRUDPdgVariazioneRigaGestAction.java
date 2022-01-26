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

/*
 * Created on Nov 25, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg01.action;

import java.util.Iterator;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.pdg01.bp.CRUDPdgVariazioneRigaGestBP;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_entrata_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk;
import it.cnr.contab.progettiric00.bp.RimodulaProgettiRicercaBP;
import it.cnr.contab.progettiric00.bp.TestataProgettiRicercaBP;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.action.FormField;
import it.cnr.jada.util.action.OptionBP;


/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDPdgVariazioneRigaGestAction extends it.cnr.jada.util.action.CRUDAction {

	public CRUDPdgVariazioneRigaGestAction() {
		super();
	}

	public it.cnr.jada.action.Forward doAddToCRUDMain_RigheVariazioneEtrGest(it.cnr.jada.action.ActionContext context) {
		try 
		{
			CRUDPdgVariazioneRigaGestBP bp = ((CRUDPdgVariazioneRigaGestBP)getBusinessProcess( context ));
			Pdg_variazione_riga_entrata_gestBulk riga = new Pdg_variazione_riga_entrata_gestBulk();
			bp.getRigheVariazioneEtrGest().add(context, riga);

			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	public it.cnr.jada.action.Forward doAddToCRUDMain_RigheVariazioneSpeGest(it.cnr.jada.action.ActionContext context) {
		try 
		{
			CRUDPdgVariazioneRigaGestBP bp = ((CRUDPdgVariazioneRigaGestBP)getBusinessProcess( context ));
			Pdg_variazione_riga_gestBulk riga = bp.preparaNuovaRiga(context);
			bp.getRigheVariazioneSpeGest().add(context, riga);

			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	/**
	 * Cancellazione concatenata dell'Elemento_voce alla cancellazione del Cdr assegnatario
	 * Settaggio a zero degli importi a causa della perdita di informazione del tipo natura FES o FIN 
	 * legata alla linea di attività 
	 *
	 * @param context	Contesto in utilizzo.
	 * @param bulk		Non usato.
	 *
	 * @return	it.cnr.jada.action.Forward	La pagina da visualizzare.
	 */
	public it.cnr.jada.action.Forward doBlankSearchSearchtool_progetto(it.cnr.jada.action.ActionContext context, Pdg_variazione_riga_gestBulk bulk) {
		CRUDPdgVariazioneRigaGestBP bp = ((CRUDPdgVariazioneRigaGestBP)getBusinessProcess( context ));
		bulk.setProgetto(new ProgettoBulk());
		bulk.setLinea_attivita(new WorkpackageBulk());
		bulk.getLinea_attivita().setProgetto(new ProgettoBulk());
		gestioneVariazioneApprovata(context, bulk, bp);
		bulk.setIm_entrata(Utility.ZERO);
		return context.findDefaultForward();
	}

	public it.cnr.jada.action.Forward doBlankSearchSearchtool_progetto_liv2(it.cnr.jada.action.ActionContext context, Pdg_variazione_riga_gestBulk bulk) {
		return doBlankSearchSearchtool_progetto(context, bulk);
	}

	public it.cnr.jada.action.Forward doBlankSearchFind_linea_attivita(it.cnr.jada.action.ActionContext context, Pdg_variazione_riga_gestBulk bulk) {
		CRUDPdgVariazioneRigaGestBP bp = ((CRUDPdgVariazioneRigaGestBP)getBusinessProcess( context ));
		bulk.setLinea_attivita(new WorkpackageBulk());
		//Reinserito il codice in quanto la scelta della voce dipende anche dalla natura della GAE
		//Quindi se viene annullata la GAE deve essere annullata anchhe la Voce
		gestioneVariazioneApprovata(context, bulk, bp);
		bulk.setIm_entrata(Utility.ZERO);
		return context.findDefaultForward();
	}
	
	public it.cnr.jada.action.Forward doBlankSearchFind_linea_attivita_liv2(it.cnr.jada.action.ActionContext context, Pdg_variazione_riga_gestBulk bulk) {
		return doBlankSearchFind_linea_attivita(context, bulk);
	}

	private void gestioneVariazioneApprovata(
			it.cnr.jada.action.ActionContext context,
			Pdg_variazione_riga_gestBulk bulk, CRUDPdgVariazioneRigaGestBP bp) {
		if (!bp.isVariazioneApprovata(context)){
			bulk.setElemento_voce(null);
			bulk.setIm_spese_gest_accentrata_est(Utility.ZERO);
			bulk.setIm_spese_gest_decentrata_est(Utility.ZERO);
			bulk.setIm_spese_gest_accentrata_int(Utility.ZERO);
			bulk.setIm_spese_gest_decentrata_int(Utility.ZERO);
		} else {
			bulk.setProgetto(new ProgettoBulk());
			bulk.getLinea_attivita().setProgetto(new ProgettoBulk());
		}
	}

	public it.cnr.jada.action.Forward doBlankSearchFind_elemento_voce(it.cnr.jada.action.ActionContext context, Pdg_variazione_riga_gestBulk bulk) {
		CRUDPdgVariazioneRigaGestBP bp = ((CRUDPdgVariazioneRigaGestBP)getBusinessProcess( context ));
		bulk.setElemento_voce(new Elemento_voceBulk());
		bulk.setIm_entrata(Utility.ZERO);
		bulk.setIm_spese_gest_accentrata_est(Utility.ZERO);
		bulk.setIm_spese_gest_decentrata_est(Utility.ZERO);
		bulk.setIm_spese_gest_accentrata_int(Utility.ZERO);
		bulk.setIm_spese_gest_decentrata_int(Utility.ZERO);
		bulk.setArea(null);
		return context.findDefaultForward();
	}

	public Forward doConfermaEliminaRigheVariazione(ActionContext context, int option) {
		try	{
			CRUDPdgVariazioneRigaGestBP bp = ((CRUDPdgVariazioneRigaGestBP)getBusinessProcess( context ));
			if (option == OptionBP.YES_BUTTON){ 
				fillModel(context);
				if (bp.isGestioneSpesa())
					doRemoveAllFromCRUD(context, "main.RigheVariazioneSpeGest");
				else
					doRemoveAllFromCRUD(context, "main.RigheVariazioneEtrGest");
			}			
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}	

	public Forward doEliminaRigheVariazione(ActionContext actioncontext) throws BusinessProcessException {
		CRUDPdgVariazioneRigaGestBP bp = ((CRUDPdgVariazioneRigaGestBP)getBusinessProcess( actioncontext ));
		return openConfirm(actioncontext,"Tutti i dettagli della variazione verranno cancellati definitivamente. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfermaEliminaRigheVariazione");		
	}
	public Forward doApponiVistoDipartimento(ActionContext context) {
		try {
			fillModel(context);
			CRUDPdgVariazioneRigaGestBP bp = ((CRUDPdgVariazioneRigaGestBP)getBusinessProcess( context ));
			if(bp.getRigheVariazioneGestionale().getSelection().isEmpty())
			{
				bp.setMessage(2, "E' necessario selezionare almeno un elemento");
				return context.findDefaultForward();
			} else
			{
				for (Iterator s = bp.getRigheVariazioneGestionale().getSelectedModels(context).iterator();s.hasNext();)
					bp.apponiVistoDipartimento(context, (Pdg_variazione_riga_gestBulk)s.next());
			}					
			bp.edit(context, bp.getModel());
			setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");
			return context.findDefaultForward();
		}catch(java.lang.ClassCastException ex){
			return handleException(context, ex);
		}catch(Throwable ex){
			return handleException(context, ex);
		}			
	}
	public it.cnr.jada.action.Forward doBringBackSearchFind_linea_attivita(it.cnr.jada.action.ActionContext context, Pdg_variazione_riga_gestBulk bulk, WorkpackageBulk linea_di_attivita) {
		try {
			fillModel(context);
			CRUDPdgVariazioneRigaGestBP bp = (CRUDPdgVariazioneRigaGestBP)getBusinessProcess(context);
			bulk.setLinea_attivita(linea_di_attivita);
			bp.valorizzaProgettoLineaAttivita(context,bulk);
			return context.findDefaultForward();
		}catch(Throwable ex){
			return handleException(context, ex);
		}
	}
	public it.cnr.jada.action.Forward doBringBackSearchFind_linea_attivita_liv2(it.cnr.jada.action.ActionContext context, Pdg_variazione_riga_gestBulk bulk, WorkpackageBulk linea_di_attivita) {
		return doBringBackSearchFind_linea_attivita(context, bulk, linea_di_attivita);
	}
	public Forward doCRUDCrea_linea_attivita(ActionContext actioncontext) throws FillException, BusinessProcessException {
		CRUDPdgVariazioneRigaGestBP bulkbp = (CRUDPdgVariazioneRigaGestBP)actioncontext.getBusinessProcess();

		FormField formfield;
		if (bulkbp.isGestioneSpesa()) 
			formfield = getFormField(actioncontext, "main.RigheVariazioneSpeGest.crea_linea_attivita");
		else
			formfield = getFormField(actioncontext, "main.RigheVariazioneEtrGest.crea_linea_attivita");
		
		try {
            CRUDBP crudbp = (CRUDBP)actioncontext.getUserInfo().createBusinessProcess(actioncontext, formfield.getField().getCRUDBusinessProcessName(), new Object[] {
                    bulkbp.isEditable() ? "MR" : "R", 
                    bulkbp.isGestioneSpesa()?bulkbp.getRigheVariazioneSpeGest().getModel():bulkbp.getRigheVariazioneEtrGest().getModel(), 
                    ((Ass_pdg_variazione_cdrBulk)bulkbp.getModel()).getCentro_responsabilita()
                });

            actioncontext.addHookForward("bringback", this, "doBringBackCRUD");
			HookForward hookforward = (HookForward)actioncontext.findForward("bringback");
			hookforward.addParameter("field", formfield);
			crudbp.setBringBack(true);
			return actioncontext.addBusinessProcess(crudbp);
		} catch(Throwable e) {
			return handleException(actioncontext,e);
		}
	}
	public it.cnr.jada.action.Forward doBringBackCRUDCrea_linea_attivita(it.cnr.jada.action.ActionContext context, Pdg_variazione_riga_gestBulk bulk, WorkpackageBulk linea_di_attivita) {
		return doBringBackSearchFind_linea_attivita(context, bulk, linea_di_attivita);
	}

	public Forward doCRUDFind_linea_attivita(ActionContext actioncontext) throws FillException, BusinessProcessException {
		return doCRUDCrea_linea_attivita(actioncontext);
	}
	public it.cnr.jada.action.Forward doBringBackCRUDFind_linea_attivita(it.cnr.jada.action.ActionContext context, Pdg_variazione_riga_gestBulk bulk, WorkpackageBulk linea_di_attivita) {
		return doBringBackCRUDCrea_linea_attivita(context,bulk,linea_di_attivita);
	}
}