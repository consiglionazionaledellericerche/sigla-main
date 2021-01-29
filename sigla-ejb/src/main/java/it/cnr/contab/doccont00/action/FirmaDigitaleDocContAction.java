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

import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.doccont00.bp.AbstractFirmaDigitaleDocContBP;
import it.cnr.contab.doccont00.bp.AllegatiDocContBP;
import it.cnr.contab.doccont00.bp.AllegatiMultipliDocContBP;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.SelezionatoreListaAction;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author mspasiano
 * @date 30-11-2015
 * 
 */
public class FirmaDigitaleDocContAction extends SelezionatoreListaAction {
	private static final long serialVersionUID = 1L;

	public FirmaDigitaleDocContAction() {
		super();
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
				bp.setSelection(context);
				bp.predisponiPerLaFirma(context);							
			} finally {
				bp.openIterator(context);				
			}
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
			try {
				bp.setSelection(context);
				bp.eliminaPredisposizione(context);
			} finally {
				bp.openIterator(context);				
			}			
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	public Forward doAttachDocuments(ActionContext context) {
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
			bp.setSelection(context);
			List<StatoTrasmissione> selectedElements = bp.getSelectedElements(context);
			if (selectedElements == null || selectedElements.isEmpty())
				throw new ApplicationException("Selezionare almeno un elemento!");
			AllegatiMultipliDocContBP allegatiMultipliDocContBP =
					(AllegatiMultipliDocContBP) context.createBusinessProcess("AllegatiMultipliDocContBP", new Object[] {"M", selectedElements});

			return context.addBusinessProcess(allegatiMultipliDocContBP);
		} catch(Exception e) {
			return handleException(context,e);
		}

	}

	@SuppressWarnings("unchecked")
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
			bp.setSelection(context);
			List<StatoTrasmissione> selectedElements = bp.getSelectedElements(context);
			if (selectedElements == null || selectedElements.isEmpty() || selectedElements.size() > 1)
				throw new ApplicationException("Selezionare solo un elemento!");

			StatoTrasmissione  selectedStatoTrasmissione  = (StatoTrasmissione) bp.getSelectedElements(context).get(0);
			AllegatiDocContBP allegatiDocContBP;
			if (selectedStatoTrasmissione.getCd_tipo_documento_cont().equalsIgnoreCase(Numerazione_doc_contBulk.TIPO_MAN)){
				allegatiDocContBP = (AllegatiDocContBP) context.createBusinessProcess("AllegatiMandatoBP", new Object[] {"M"});
				allegatiDocContBP.setAllegatiFormName("mandato");				
				List<Rif_modalita_pagamentoBulk> result = Utility.createMandatoComponentSession().findModPagObbligatorieAssociateAlMandato(context.getUserContext(), 
						(V_mandato_reversaleBulk) selectedStatoTrasmissione);
				for (Rif_modalita_pagamentoBulk rif_modalita_pagamentoBulk : result) {
					allegatiDocContBP.addToRifModalitaPagamento(rif_modalita_pagamentoBulk.getCd_modalita_pag(), rif_modalita_pagamentoBulk.getDs_modalita_pag());
				}
			} else {
				allegatiDocContBP = (AllegatiDocContBP) context.createBusinessProcess("AllegatiDocContBP", new Object[] {"M"});
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
	
	@SuppressWarnings("unchecked")
	public Forward doSign(ActionContext context) {
		AbstractFirmaDigitaleDocContBP bp = (AbstractFirmaDigitaleDocContBP)context.getBusinessProcess();
		try {
			fillModel(context);
			bp.setSelection(context);
			List<StatoTrasmissione> selectedElements = bp.getSelectedElements(context);
			if (selectedElements == null || selectedElements.isEmpty())
				throw new ApplicationException("Selezionare almeno un elemento!");

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
				bp.setSelection(context);
				bp.sign(context, firmaOTPBulk);
			} finally {
				bp.openIterator(context);				
			}
		} catch(Exception e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}

	@Override
	public Forward basicDoBringBack(ActionContext actioncontext) throws BusinessProcessException {
		return actioncontext.findDefaultForward();
	}

	public Forward doRicercaLibera(ActionContext context) {
		AbstractFirmaDigitaleDocContBP bp = (AbstractFirmaDigitaleDocContBP)context.getBusinessProcess();
		try {
			bp.setModel(context, Optional.ofNullable(bp.getModel())
					.filter(StatoTrasmissione.class::isInstance)
					.map(StatoTrasmissione.class::cast)
					.map(statoTrasmissione -> {
						statoTrasmissione.setStato_trasmissione(StatoTrasmissione.ALL);
						return statoTrasmissione;
					}).map(OggettoBulk.class::cast).orElse(null));
            bp.setColumnSet(context, StatoTrasmissione.ALL);
		} catch (BusinessProcessException e) {
			return handleException(context, e);
		}
		return super.doRicercaLibera(context);
	}
}