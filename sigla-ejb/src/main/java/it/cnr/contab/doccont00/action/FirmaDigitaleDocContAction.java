package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.bp.AbstractFirmaDigitaleDocContBP;
import it.cnr.contab.doccont00.bp.AllegatiDocContBP;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
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
			bp.predisponiPerLaFirma(context);
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
			allegatiDocContBP.setModel(context, allegatiDocContBP.initializeModelForEdit(context, (OggettoBulk) bp.getSelectedElements(context).get(0)));
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
			bp.sign(context, firmaOTPBulk);			
			bp.openIterator(context);
		} catch(Exception e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}	
	
	public Forward doInvia(ActionContext context) {
		try {
			BulkBP firmaOTPBP = (BulkBP) context.createBusinessProcess("FirmaOTPBP");
			firmaOTPBP.setModel(context, new FirmaOTPBulk());
			context.addHookForward("firmaOTP",this,"doBackInvia");			
			return context.addBusinessProcess(firmaOTPBP);
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	
	public Forward doBackInvia(ActionContext context) {		
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
			bp.invia(context, firmaOTPBulk);			
			bp.openIterator(context);
		} catch(Exception e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}		
}