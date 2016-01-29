package it.cnr.contab.doccont00.action;

import java.util.List;

import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.doccont00.bp.AbstractFirmaDigitaleDocContBP;
import it.cnr.contab.doccont00.bp.AllegatiDocContBP;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.ConsultazioniAction;

/**
 * 
 * @author mspasiano
 * @date 30-11-2015
 * 
 */
public class FirmaDigitaleDocContAction extends ConsultazioniAction {
	private static final long serialVersionUID = 1L;

	public FirmaDigitaleDocContAction() {
		super();
	}
	
	public Forward doCancellaFiltro(ActionContext context) {
		AbstractFirmaDigitaleDocContBP bp = (AbstractFirmaDigitaleDocContBP)context.getBusinessProcess();
		bp.setFindclause(null);
		return doRefresh(context);
	}
	
	public Forward doCambiaVisibilita(ActionContext context) {
		AbstractFirmaDigitaleDocContBP bp = (AbstractFirmaDigitaleDocContBP)context.getBusinessProcess();
		OggettoBulk bulk = bp.getModel();
		StatoTrasmissione statoTrasmissioneBulk = ((StatoTrasmissione)bulk);
		try {
			fillModel(context);
			String statoTrasmissione = statoTrasmissioneBulk.getStato_trasmissione();
			bulk = (OggettoBulk)bp.getBulkInfo().getBulkClass().newInstance();
			statoTrasmissioneBulk = ((StatoTrasmissione)bulk);
			statoTrasmissioneBulk.setStato_trasmissione(statoTrasmissione);
			bp.setModel(context, bulk);
			bp.setColumnSet(context, statoTrasmissioneBulk.getStato_trasmissione());
			bp.openIterator(context);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	
	public Forward doRefresh(ActionContext context) {
		AbstractFirmaDigitaleDocContBP bp = (AbstractFirmaDigitaleDocContBP)context.getBusinessProcess();
		OggettoBulk bulk = bp.getModel();
		StatoTrasmissione statoTrasmissioneBulk = ((StatoTrasmissione)bulk);
		try {
			fillModel(context);
			String statoTrasmissione = statoTrasmissioneBulk.getStato_trasmissione();
			bulk = (OggettoBulk)bp.getBulkInfo().getBulkClass().newInstance();
			statoTrasmissioneBulk = ((StatoTrasmissione)bulk);
			statoTrasmissioneBulk.setStato_trasmissione(statoTrasmissione);
			bp.setModel(context, bulk);
			bp.openIterator(context);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	
	public Forward doPredisponiPerLaFirma(ActionContext context) {
		AbstractFirmaDigitaleDocContBP bp = (AbstractFirmaDigitaleDocContBP)context.getBusinessProcess();
		OggettoBulk bulk = bp.getModel();
		StatoTrasmissione statoTrasmissioneBulk = ((StatoTrasmissione)bulk);
		try {
			fillModel(context);
			String statoTrasmissione = statoTrasmissioneBulk.getStato_trasmissione();
			bulk = (OggettoBulk)bp.getBulkInfo().getBulkClass().newInstance();
			statoTrasmissioneBulk = ((StatoTrasmissione)bulk);
			statoTrasmissioneBulk.setStato_trasmissione(statoTrasmissione);
			bp.setModel(context, bulk);
			try {
				bp.predisponiPerLaFirma(context);							
			} finally {
				bp.openIterator(context);				
			}
			bp.openIterator(context);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	
	public Forward doEliminaPredisposizione(ActionContext context) {
		AbstractFirmaDigitaleDocContBP bp = (AbstractFirmaDigitaleDocContBP)context.getBusinessProcess();
		OggettoBulk bulk = bp.getModel();
		StatoTrasmissione statoTrasmissioneBulk = ((StatoTrasmissione)bulk);
		try {
			fillModel(context);
			String statoTrasmissione = statoTrasmissioneBulk.getStato_trasmissione();
			bulk = (OggettoBulk)bp.getBulkInfo().getBulkClass().newInstance();
			statoTrasmissioneBulk = ((StatoTrasmissione)bulk);
			statoTrasmissioneBulk.setStato_trasmissione(statoTrasmissione);
			bp.setModel(context, bulk);
			bp.eliminaPredisposizione(context);
			bp.openIterator(context);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	public Forward doDetail(ActionContext context) {
		AbstractFirmaDigitaleDocContBP bp = (AbstractFirmaDigitaleDocContBP)context.getBusinessProcess();
		OggettoBulk bulk = bp.getModel();
		StatoTrasmissione statoTrasmissioneBulk = ((StatoTrasmissione)bulk);
		try {
			fillModel(context);
			String statoTrasmissione = statoTrasmissioneBulk.getStato_trasmissione();
			bulk = (OggettoBulk)bp.getBulkInfo().getBulkClass().newInstance();
			statoTrasmissioneBulk = ((StatoTrasmissione)bulk);
			statoTrasmissioneBulk.setStato_trasmissione(statoTrasmissione);
			bp.setModel(context, bulk);
			AllegatiDocContBP allegatiDocContBP = (AllegatiDocContBP) context.createBusinessProcess("AllegatiDocContBP", new Object[] {"M"});
			StatoTrasmissione  selectedStatoTrasmissione  = (StatoTrasmissione) bp.getSelectedElements(context).get(0);
			if (selectedStatoTrasmissione.getCd_tipo_documento_cont().equalsIgnoreCase(Numerazione_doc_contBulk.TIPO_MAN)){
				allegatiDocContBP.setAllegatiFormName("mandato");				
				List<Rif_modalita_pagamentoBulk> result = Utility.createMandatoComponentSession().findModPagObbligatorieAssociateAlMandato(context.getUserContext(), 
						(V_mandato_reversaleBulk) selectedStatoTrasmissione);
				for (Rif_modalita_pagamentoBulk rif_modalita_pagamentoBulk : result) {
					allegatiDocContBP.addToRifModalitaPagamento(rif_modalita_pagamentoBulk.getCd_modalita_pag(), rif_modalita_pagamentoBulk.getDs_modalita_pag());
				}				
			} else {
				allegatiDocContBP.setAllegatiFormName("altro");
			}
			allegatiDocContBP.setModel(context, allegatiDocContBP.initializeModelForEdit(context, (OggettoBulk) selectedStatoTrasmissione));
			allegatiDocContBP.setStatus(AllegatiDocContBP.EDIT);
			context.addHookForward("close",this,"doRefresh");
			return context.addBusinessProcess(allegatiDocContBP);
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	
	public Forward doSign(ActionContext context) {
		try {
			BulkBP firmaOTPBP = (BulkBP) context.createBusinessProcess("FirmaOTPBP");
			firmaOTPBP.setModel(context, new FirmaOTPBulk());
			context.addHookForward("firmaOTP",this,"doBackSign");			
			return context.addBusinessProcess(firmaOTPBP);
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	
	public Forward doBackSign(ActionContext context) {
		AbstractFirmaDigitaleDocContBP bp = (AbstractFirmaDigitaleDocContBP)context.getBusinessProcess();
		OggettoBulk bulk = bp.getModel();
		StatoTrasmissione statoTrasmissioneBulk = ((StatoTrasmissione)bulk);
		HookForward caller = (HookForward)context.getCaller();
		FirmaOTPBulk firmaOTPBulk = (FirmaOTPBulk) caller.getParameter("firmaOTP");
		try {
			fillModel(context);
			String statoTrasmissione = statoTrasmissioneBulk.getStato_trasmissione();
			bulk = (OggettoBulk)bp.getBulkInfo().getBulkClass().newInstance();
			statoTrasmissioneBulk = ((StatoTrasmissione)bulk);
			statoTrasmissioneBulk.setStato_trasmissione(statoTrasmissione);
			bp.setModel(context, bulk);
			try {
				bp.sign(context, firmaOTPBulk);
			} finally {
				bp.openIterator(context);				
			}
		} catch(Exception e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}
}