/*
 * Created on Sep 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.action;

import java.util.Optional;

import it.cnr.contab.config00.pdcfin.bulk.LimiteSpesaBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.prevent01.bp.CRUDDettagliModuloCostiBP;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_costiBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.contab.prevent01.ejb.PdgModuloCostiComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.OptionBP;
/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDDettagliModuloCostiAction extends CRUDAction {

	public CRUDDettagliModuloCostiAction() {
		super();
	}
	public Forward doConfermaEliminamodulo(ActionContext context, int option)
	{
		try
		{
			CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)context.getBusinessProcess();		
			if (option == OptionBP.YES_BUTTON) 
				super.doElimina(context);		
			
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	
	}	
	public Forward doEliminamodulo(ActionContext actioncontext) throws BusinessProcessException {
		CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)actioncontext.getBusinessProcess();
		if (bp.isCostiDipendenteRipartiti(actioncontext)){
			bp.setErrorMessage("Eliminazione non possibile, in quanto sono stati scaricati i costi del dipendente!");
			return actioncontext.findDefaultForward();
		}		  
		return openConfirm(actioncontext,"Tutti i dettagli di spesa relativi al modulo verranno cancellati definitivamente. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfermaEliminamodulo");		
	}
	public Forward doEliminaProgetto(ActionContext actioncontext) throws BusinessProcessException {
		CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)actioncontext.getBusinessProcess();
		if (bp.isCostiDipendenteRipartiti(actioncontext)){
			bp.setErrorMessage("Eliminazione non possibile, in quanto sono stati scaricati i costi del dipendente!");
			return actioncontext.findDefaultForward();
		}		  
		return openConfirm(actioncontext,"Tutti i dettagli di spesa relativi al progetto verranno cancellati definitivamente. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfermaEliminamodulo");		
	}
	public Forward doOnIm_cf_amm_altroChange(ActionContext context) {

		try{	
			CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)getBusinessProcess(context);
			Pdg_modulo_costiBulk pdg_modulo_costi = (Pdg_modulo_costiBulk)bp.getModel();
					
			java.math.BigDecimal oldImp = pdg_modulo_costi.getIm_cf_amm_altro();

			try {
				fillModel(context);
				if(pdg_modulo_costi.getIm_cf_amm_altro() == null)
                   pdg_modulo_costi.setIm_cf_amm_altro(Utility.ZERO);
				return context.findDefaultForward();
			} catch(it.cnr.jada.bulk.FillException e) {
				pdg_modulo_costi.setIm_cf_amm_altro(oldImp);
				bp.setModel(context,pdg_modulo_costi);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
	public Forward doOnIm_cf_amm_attrezzChange(ActionContext context) {

		try{	
			CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)getBusinessProcess(context);
			Pdg_modulo_costiBulk pdg_modulo_costi = (Pdg_modulo_costiBulk)bp.getModel();
					
			java.math.BigDecimal oldImp = pdg_modulo_costi.getIm_cf_amm_attrezz();

			try {
				fillModel(context);
				if(pdg_modulo_costi.getIm_cf_amm_attrezz() == null)
				   pdg_modulo_costi.setIm_cf_amm_attrezz(Utility.ZERO);
				
				return context.findDefaultForward();
			} catch(it.cnr.jada.bulk.FillException e) {
				pdg_modulo_costi.setIm_cf_amm_attrezz(oldImp);
				bp.setModel(context,pdg_modulo_costi);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
	public Forward doOnIm_cf_amm_immobiliChange(ActionContext context) {

		try{	
			CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)getBusinessProcess(context);
			Pdg_modulo_costiBulk pdg_modulo_costi = (Pdg_modulo_costiBulk)bp.getModel();
					
			java.math.BigDecimal oldImp = pdg_modulo_costi.getIm_cf_amm_immobili();

			try {
				fillModel(context);
				if(pdg_modulo_costi.getIm_cf_amm_immobili() == null)
				   pdg_modulo_costi.setIm_cf_amm_immobili(Utility.ZERO);				
				return context.findDefaultForward();
			} catch(it.cnr.jada.bulk.FillException e) {
				pdg_modulo_costi.setIm_cf_amm_immobili(oldImp);
				bp.setModel(context,pdg_modulo_costi);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
	public Forward doOnIm_cf_tfrChange(ActionContext context) {

		try{	
			CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)getBusinessProcess(context);
			Pdg_modulo_costiBulk pdg_modulo_costi = (Pdg_modulo_costiBulk)bp.getModel();
					
			java.math.BigDecimal oldImp = pdg_modulo_costi.getIm_cf_tfr();

			try {
				fillModel(context);
				if(pdg_modulo_costi.getIm_cf_tfr() == null)
				   pdg_modulo_costi.setIm_cf_tfr(Utility.ZERO);								
				return context.findDefaultForward();
			} catch(it.cnr.jada.bulk.FillException e) {
				pdg_modulo_costi.setIm_cf_tfr(oldImp);
				bp.setModel(context,pdg_modulo_costi);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
	public Forward doOnIm_costi_generaliChange(ActionContext context) {

		try{	
			CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)getBusinessProcess(context);
			Pdg_modulo_costiBulk pdg_modulo_costi = (Pdg_modulo_costiBulk)bp.getModel();
					
			java.math.BigDecimal oldImp = pdg_modulo_costi.getIm_costi_generali();

			try {
				fillModel(context);
				if(pdg_modulo_costi.getIm_costi_generali() == null)
				   pdg_modulo_costi.setIm_costi_generali(Utility.ZERO);												
				return context.findDefaultForward();
			} catch(it.cnr.jada.bulk.FillException e) {
				pdg_modulo_costi.setIm_costi_generali(oldImp);
				bp.setModel(context,pdg_modulo_costi);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
	public Forward doOnRis_pres_es_prec_tit_iiChange(ActionContext context) {

		try{	
			CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)getBusinessProcess(context);
			Pdg_modulo_costiBulk pdg_modulo_costi = (Pdg_modulo_costiBulk)bp.getModel();
					
			java.math.BigDecimal oldImp = pdg_modulo_costi.getRis_pres_es_prec_tit_ii();

			try {
				fillModel(context);
				if(pdg_modulo_costi.getRis_pres_es_prec_tit_ii() == null)
				   pdg_modulo_costi.setRis_pres_es_prec_tit_ii(Utility.ZERO);								
				
				return context.findDefaultForward();
			} catch(it.cnr.jada.bulk.FillException e) {
				pdg_modulo_costi.setRis_pres_es_prec_tit_ii(oldImp);
				bp.setModel(context,pdg_modulo_costi);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
	public Forward doOnRis_pres_es_prec_tit_iChange(ActionContext context) {

		try{	
			CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)getBusinessProcess(context);
			Pdg_modulo_costiBulk pdg_modulo_costi = (Pdg_modulo_costiBulk)bp.getModel();
					
			java.math.BigDecimal oldImp = pdg_modulo_costi.getRis_pres_es_prec_tit_i();

			try {
				fillModel(context);
				if(pdg_modulo_costi.getRis_pres_es_prec_tit_i() == null)
				   pdg_modulo_costi.setRis_pres_es_prec_tit_i(Utility.ZERO);								
				
				return context.findDefaultForward();
			} catch(it.cnr.jada.bulk.FillException e) {
				pdg_modulo_costi.setRis_pres_es_prec_tit_i(oldImp);
				bp.setModel(context,pdg_modulo_costi);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
	public Forward doOnRis_es_prec_tit_iiChange(ActionContext context) {

		try{	
			CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)getBusinessProcess(context);
			Pdg_modulo_costiBulk pdg_modulo_costi = (Pdg_modulo_costiBulk)bp.getModel();
					
			java.math.BigDecimal oldImp = pdg_modulo_costi.getRis_es_prec_tit_ii();

			try {
				fillModel(context);
				if(pdg_modulo_costi.getRis_es_prec_tit_ii() == null)
				   pdg_modulo_costi.setRis_es_prec_tit_ii(Utility.ZERO);								
				
				return context.findDefaultForward();
			} catch(it.cnr.jada.bulk.FillException e) {
				pdg_modulo_costi.setRis_es_prec_tit_ii(oldImp);
				bp.setModel(context,pdg_modulo_costi);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
	public Forward doOnRis_es_prec_tit_iChange(ActionContext context) {

		try{	
			CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)getBusinessProcess(context);
			Pdg_modulo_costiBulk pdg_modulo_costi = (Pdg_modulo_costiBulk)bp.getModel();
					
			java.math.BigDecimal oldImp = pdg_modulo_costi.getRis_es_prec_tit_i();

			try {
				fillModel(context);
				if(pdg_modulo_costi.getRis_es_prec_tit_i() == null)
				   pdg_modulo_costi.setRis_es_prec_tit_i(Utility.ZERO);								
				
				return context.findDefaultForward();
			} catch(it.cnr.jada.bulk.FillException e) {
				pdg_modulo_costi.setRis_es_prec_tit_i(oldImp);
				bp.setModel(context,pdg_modulo_costi);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
	public Forward doOnIm_spese_gest_decentrata_intChange(ActionContext context) {
		try{	
			CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)getBusinessProcess(context);
			Pdg_modulo_speseBulk pdg_modulo_spese = (Pdg_modulo_speseBulk)bp.getCrudDettagliSpese().getModel();
					
			java.math.BigDecimal oldImp = pdg_modulo_spese.getIm_spese_gest_decentrata_int();

			try {
				fillModel(context);
				if(pdg_modulo_spese.getIm_spese_gest_decentrata_int() == null)
				   pdg_modulo_spese.setIm_spese_gest_decentrata_int(Utility.ZERO);								
				
				return context.findDefaultForward();
			} catch(it.cnr.jada.bulk.FillException e) {
				pdg_modulo_spese.setIm_spese_gest_decentrata_int(oldImp);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
	public Forward doOnIm_spese_gest_decentrata_estChange(ActionContext context) {
		try{	
			CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)getBusinessProcess(context);
			Pdg_modulo_speseBulk pdg_modulo_spese = (Pdg_modulo_speseBulk)bp.getCrudDettagliSpese().getModel();
					
			java.math.BigDecimal oldImp = pdg_modulo_spese.getIm_spese_gest_decentrata_est();

			try {
				fillModel(context);
				if(pdg_modulo_spese.getIm_spese_gest_decentrata_est() == null)
				   pdg_modulo_spese.setIm_spese_gest_decentrata_est(Utility.ZERO);								
				
				return context.findDefaultForward();
			} catch(it.cnr.jada.bulk.FillException e) {
				pdg_modulo_spese.setIm_spese_gest_decentrata_est(oldImp);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
	public Forward doOnIm_spese_gest_accentrata_intChange(ActionContext context) {
		try{	
			CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)getBusinessProcess(context);
			Pdg_modulo_speseBulk pdg_modulo_spese = (Pdg_modulo_speseBulk)bp.getCrudDettagliSpese().getModel();
					
			java.math.BigDecimal oldImp = pdg_modulo_spese.getIm_spese_gest_accentrata_int();

			try {
				fillModel(context);
				if(pdg_modulo_spese.getIm_spese_gest_accentrata_int() == null)
				   pdg_modulo_spese.setIm_spese_gest_accentrata_int(Utility.ZERO);								
				
				return context.findDefaultForward();
			} catch(it.cnr.jada.bulk.FillException e) {
				pdg_modulo_spese.setIm_spese_gest_accentrata_int(oldImp);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
	public Forward doOnIm_spese_gest_accentrata_estChange(ActionContext context) {
		try{	
			CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)getBusinessProcess(context);
			Pdg_modulo_speseBulk pdg_modulo_spese = (Pdg_modulo_speseBulk)bp.getCrudDettagliSpese().getModel();
					
			java.math.BigDecimal oldImp = pdg_modulo_spese.getIm_spese_gest_accentrata_est();

			try {
				fillModel(context);
				if(pdg_modulo_spese.getIm_spese_gest_accentrata_est() == null)
				   pdg_modulo_spese.setIm_spese_gest_accentrata_est(Utility.ZERO);								
				
				return context.findDefaultForward();
			} catch(it.cnr.jada.bulk.FillException e) {
				pdg_modulo_spese.setIm_spese_gest_accentrata_est(oldImp);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
	public Forward doOnIm_spese_a2Change(ActionContext context) {
		try{	
			CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)getBusinessProcess(context);
			Pdg_modulo_speseBulk pdg_modulo_spese = (Pdg_modulo_speseBulk)bp.getCrudDettagliSpese().getModel();
					
			java.math.BigDecimal oldImp = pdg_modulo_spese.getIm_spese_a2();

			try {
				fillModel(context);
				if(pdg_modulo_spese.getIm_spese_a2() == null)
				   pdg_modulo_spese.setIm_spese_a2(Utility.ZERO);								
				
				return context.findDefaultForward();
			} catch(it.cnr.jada.bulk.FillException e) {
				pdg_modulo_spese.setIm_spese_a2(oldImp);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
	public Forward doOnIm_spese_a3Change(ActionContext context) {
		try{	
			CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)getBusinessProcess(context);
			Pdg_modulo_speseBulk pdg_modulo_spese = (Pdg_modulo_speseBulk)bp.getCrudDettagliSpese().getModel();
					
			java.math.BigDecimal oldImp = pdg_modulo_spese.getIm_spese_a3();

			try {
				fillModel(context);
				if(pdg_modulo_spese.getIm_spese_a3() == null)
				   pdg_modulo_spese.setIm_spese_a3(Utility.ZERO);								
				
				return context.findDefaultForward();
			} catch(it.cnr.jada.bulk.FillException e) {
				pdg_modulo_spese.setIm_spese_a3(oldImp);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
	public it.cnr.jada.action.Forward doBringBackSearchClassificazione(ActionContext context, Pdg_modulo_speseBulk pdg_modulo_spese, V_classificazione_vociBulk classificazione) throws java.rmi.RemoteException {
		try{
			innerDoBringBackSearchClassificazione(context, pdg_modulo_spese, classificazione, false);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}

	public it.cnr.jada.action.Forward doBringBackSearchClassificazione_codlast(ActionContext context, Pdg_modulo_speseBulk pdg_modulo_spese, V_classificazione_vociBulk classificazione) throws java.rmi.RemoteException {
		try{
			innerDoBringBackSearchClassificazione(context, pdg_modulo_spese, classificazione, true);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}

	private void innerDoBringBackSearchClassificazione(ActionContext context, Pdg_modulo_speseBulk pdg_modulo_spese, V_classificazione_vociBulk classificazione, boolean initVoceEconomica) throws Throwable {
		fillModel(context);	
		CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)getBusinessProcess(context);
		if (classificazione != null){
			if (initVoceEconomica) {
		    	if (Optional.ofNullable(pdg_modulo_spese.getPdg_modulo_costi())
		    				.flatMap(el->Optional.ofNullable(el.getPdg_modulo()))
		    				.flatMap(el->Optional.ofNullable(el.getProgetto()))
		    				.flatMap(el->Optional.ofNullable(el.getOtherField()))
		    				.flatMap(el->Optional.ofNullable(el.getTipoFinanziamento()))
		    				.flatMap(el->Optional.ofNullable(el.getFlPianoEcoFin()))
		    				.orElseThrow(()->new RuntimeException("Errore in fase di ricerca. Aprire una segnalazione HelpDesk"))
		    				.booleanValue()) {
		    		bp.inizializzaVoceEconomica(context, pdg_modulo_spese, classificazione);
		    	}
			}
			pdg_modulo_spese.setClassificazione(classificazione);
			if(classificazione.getFl_mastrino().booleanValue()){
				pdg_modulo_spese.setLimiteInt(((PdgModuloCostiComponentSession)bp.createComponentSession()).soggettaLimite(context.getUserContext(),pdg_modulo_spese,LimiteSpesaBulk.FONTE_INTERNA));
				pdg_modulo_spese.setLimiteEst(((PdgModuloCostiComponentSession)bp.createComponentSession()).soggettaLimite(context.getUserContext(),pdg_modulo_spese,LimiteSpesaBulk.FONTE_ESTERNA));
				pdg_modulo_spese.setPrevAnnoSucObb(classificazione.getFl_prev_obb_anno_suc());
				if(pdg_modulo_spese.isPrevAnnoSucObb())
					pdg_modulo_spese.setIm_spese_a2(null);
				else
					if (pdg_modulo_spese.getIm_spese_a2()==null)
						pdg_modulo_spese.setIm_spese_a2(Utility.ZERO);
			}
			bp.setModel(context,((PdgModuloCostiComponentSession)bp.createComponentSession()).calcolaPrevisioneAssestataRowByRow(context.getUserContext(),(Pdg_modulo_costiBulk)bp.getModel() ,pdg_modulo_spese,new Integer(CNRUserContext.getEsercizio(context.getUserContext()).intValue() - 1)));
		}
	}

	public Forward doSalva(ActionContext context) throws java.rmi.RemoteException {

		CRUDDettagliModuloCostiBP bp= (CRUDDettagliModuloCostiBP) getBusinessProcess(context);
		if (bp.isContrSpeseAggiornabile()&&bp.getStatus()==bp.VIEW) {
			bp.setStatus(bp.EDIT);
		}
		return super.doSalva(context);

	}
	public Forward doBlankSearchClassificazione(ActionContext context, Pdg_modulo_speseBulk pdg_modulo_spese) throws java.rmi.RemoteException {
	 	pdg_modulo_spese.setLimiteEst(false);
		pdg_modulo_spese.setLimiteInt(false);
		pdg_modulo_spese.setClassificazione(new V_classificazione_vociBulk());
		return context.findDefaultForward();
	}
	public Forward doBlankSearchClassificazione_codlast(ActionContext context, Pdg_modulo_speseBulk pdg_modulo_spese) throws java.rmi.RemoteException {
	 	pdg_modulo_spese.setLimiteEst(false);
		pdg_modulo_spese.setLimiteInt(false);
		pdg_modulo_spese.setClassificazione(new V_classificazione_vociBulk());
		return context.findDefaultForward();
	}
	public it.cnr.jada.action.Forward doBringBackSearchArea(ActionContext context, Pdg_modulo_speseBulk pdg_modulo_spese, CdsBulk cds) throws java.rmi.RemoteException {
		try{
			fillModel(context);	
			CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)getBusinessProcess(context);
			if (cds != null)
				pdg_modulo_spese.setArea(cds);
			fillModel(context);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
	public it.cnr.jada.action.Forward doBlankSearchArea(ActionContext context, Pdg_modulo_speseBulk pdg_modulo_spese, CdsBulk cds) throws java.rmi.RemoteException {
		try{
			fillModel(context);	 
			CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)getBusinessProcess(context);
			pdg_modulo_spese.setArea(new CdsBulk());
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
}
