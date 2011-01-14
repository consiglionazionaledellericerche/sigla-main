/*
 * Created on Nov 25, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg01.action;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.pdg01.bp.CRUDPdgModuloSpeseGestBP;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_spese_gestBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.OptionBP;


/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDPdgModuloSpeseGestAction extends CRUDAction {

	public CRUDPdgModuloSpeseGestAction() {
		super();
	}
	public it.cnr.jada.action.Forward doAddToCRUDMain_DettagliGestionali(it.cnr.jada.action.ActionContext context) {
		try 
		{
			CRUDPdgModuloSpeseGestBP bp = ((CRUDPdgModuloSpeseGestBP)getBusinessProcess( context ));
			Pdg_modulo_spese_gestBulk pdg = new Pdg_modulo_spese_gestBulk();
			pdg.setOrigine(Pdg_modulo_spese_gestBulk.OR_PREVISIONE);
			pdg.setCategoria_dettaglio(Pdg_modulo_spese_gestBulk.CAT_DIRETTA);
			pdg.setFl_sola_lettura(new Boolean(false));
			bp.getCrudDettagliGestionali().add(context, pdg);

			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	/**
	 * Cancellazione concatenata dell'Elemento_voce e della GAE alla cancellazione del Cdr assegnatario.
	 * Settaggio a zero degli importi a causa della perdita di informazione del tipo natura FES o FIN 
	 * legata alla linea di attività 
	 *
	 * @param context	Contesto in utilizzo.
	 * @param bulk		Non usato.
	 *
	 * @return	it.cnr.jada.action.Forward	La pagina da visualizzare.
	 */
	public it.cnr.jada.action.Forward doBlankSearchFind_cdr_assegnatario(it.cnr.jada.action.ActionContext context, Pdg_modulo_spese_gestBulk bulk) {
		bulk.setCdr_assegnatario(new CdrBulk());
		bulk.setLinea_attivita(null);
		bulk.setElemento_voce(null);
		bulk.setIm_spese_gest_accentrata_int(Utility.ZERO);
		bulk.setIm_spese_gest_accentrata_est(Utility.ZERO);
		bulk.setIm_spese_gest_decentrata_int(Utility.ZERO);
		bulk.setIm_spese_gest_decentrata_est(Utility.ZERO);
		return context.findDefaultForward();
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
	public it.cnr.jada.action.Forward doBlankSearchFind_linea_attivita(it.cnr.jada.action.ActionContext context, Pdg_modulo_spese_gestBulk bulk) {
		bulk.setLinea_attivita(new WorkpackageBulk());
		bulk.setElemento_voce(null);
		bulk.setIm_spese_gest_accentrata_int(Utility.ZERO);
		bulk.setIm_spese_gest_accentrata_est(Utility.ZERO);
		bulk.setIm_spese_gest_decentrata_int(Utility.ZERO);
		bulk.setIm_spese_gest_decentrata_est(Utility.ZERO);
		return context.findDefaultForward();
	}

	public Forward doConfermaEliminaDettagliGestionali(ActionContext context, int option) {
		try	{
			CRUDPdgModuloSpeseGestBP bp = ((CRUDPdgModuloSpeseGestBP)getBusinessProcess( context ));
			if (option == OptionBP.YES_BUTTON){ 
				fillModel(context);
				doRemoveAllFromCRUD(context, "main.DettagliGestionali");
			}			
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}	
	public Forward doEliminaDettagliGestionali(ActionContext actioncontext) throws BusinessProcessException {
		CRUDPdgModuloSpeseGestBP bp = ((CRUDPdgModuloSpeseGestBP)getBusinessProcess( actioncontext ));
		return openConfirm(actioncontext,"Tutti i dettagli di spesa relativi al modulo e " + bp.getLabelDesctool_classificazione() + " verranno cancellati definitivamente. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfermaEliminaDettagliGestionali");		
	}
}