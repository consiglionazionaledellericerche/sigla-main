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

package it.cnr.contab.ordmag.ordini.action;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoRigaBulk;
import it.cnr.contab.ordmag.ordini.bp.CRUDEvasioneOrdineBP;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.SelezionatoreListaBP;

public class CRUDEvasioneOrdineAction extends it.cnr.jada.util.action.CRUDAction {

	public CRUDEvasioneOrdineAction() {
	        super();
    }

	public Forward doBlankSearchFindUnitaOperativaOrd(ActionContext context, EvasioneOrdineBulk evasioneOrdine) {
		evasioneOrdine.setMagazzinoAbilitato(null);
		evasioneOrdine.setNumerazioneMag(null);
		evasioneOrdine.setUnitaOperativaAbilitata(new UnitaOperativaOrdBulk());
		return context.findDefaultForward();
	}

	public Forward doBringBackSearchFindUnitaOperativaOrd(ActionContext context, EvasioneOrdineBulk evasioneOrdine, UnitaOperativaOrdBulk unitaOperativaOrd) {
		try {
			fillModel(context);
			if (unitaOperativaOrd!=null) {
				CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)getBusinessProcess(context);
				bp.initializeUnitaOperativaOrd(context, evasioneOrdine, unitaOperativaOrd);
				bp.setDirty(true);
			}
			return context.findDefaultForward();
		}catch (Exception ex) {
			return handleException(context, ex);
		}
	}

	public Forward doBlankSearchFindMagazzino(ActionContext context, EvasioneOrdineBulk evasioneOrdine) {
		evasioneOrdine.setMagazzinoAbilitato(new MagazzinoBulk());
		evasioneOrdine.setNumerazioneMag(null);
		return context.findDefaultForward();
	}

	public Forward doBringBackSearchFindMagazzino(ActionContext context, EvasioneOrdineBulk evasioneOrdine, MagazzinoBulk magazzino) {
		try {
			fillModel(context);
			if (magazzino!=null) {
				CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)getBusinessProcess(context);
				bp.initializeMagazzino(context, evasioneOrdine, magazzino);
				bp.setDirty(true);
			}
			return context.findDefaultForward();
		}catch (Exception ex) {
			return handleException(context, ex);
		}
	}

	public Forward doCercaConsegneDaEvadere(ActionContext context) 
	{
		try 
		{
			CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)getBusinessProcess(context);
			fillModel( context );
			bp.getConsegne().getSelection().clear();
			bp.cercaConsegne(context);
			return doToggleCriteriRicerca(context);
		} 
		catch(Exception e) {return handleException(context,e);}
	}

	@Override
	public Forward doSalva(ActionContext actioncontext) throws RemoteException {
		try
		{
			fillModel(actioncontext);
			List<BollaScaricoMagBulk> listaBolleScarico = gestioneSalvataggio(actioncontext);
			CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)actioncontext.getBusinessProcess();
			if (!listaBolleScarico.isEmpty()){
				SelezionatoreListaBP nbp = (SelezionatoreListaBP)actioncontext.createBusinessProcess("BolleScaricoGenerate");
				nbp.setMultiSelection(false);
	
				RemoteIterator iterator = Utility.createMovimentiMagComponentSession().preparaQueryBolleScaricoDaVisualizzare(actioncontext.getUserContext(), listaBolleScarico);
				
				nbp.setIterator(actioncontext,iterator);
				BulkInfo bulkInfo = BulkInfo.getBulkInfo(BollaScaricoMagBulk.class);
				nbp.setBulkInfo(bulkInfo);
	
				String columnsetName = bp.getColumnSetForBollaScarico();
				if (columnsetName != null)
					nbp.setColumns(bulkInfo.getColumnFieldPropertyDictionary(columnsetName));
				return actioncontext.addBusinessProcess(nbp);
			} else 
				bp.setMessage("Operazione Effettuata");
			return actioncontext.findDefaultForward();
		}
		catch(ValidationException validationexception)
		{
			getBusinessProcess(actioncontext).setErrorMessage(validationexception.getMessage());
		}
		catch(Exception throwable)
		{
			return handleException(actioncontext, throwable);
		}
		return actioncontext.findDefaultForward();
	}
	
	private List<BollaScaricoMagBulk> gestioneSalvataggio(ActionContext actioncontext) throws ValidationException, ApplicationException,  BusinessProcessException {
		CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)actioncontext.getBusinessProcess();
		it.cnr.jada.util.action.Selection selection = bp.getConsegne().getSelection();
		EvasioneOrdineBulk bulk = (EvasioneOrdineBulk) bp.getModel();
		java.util.List consegne = bp.getConsegne().getDetails();
		bulk.setRigheConsegnaSelezionate(new BulkList<>());
		for (it.cnr.jada.util.action.SelectionIterator i = selection.iterator();i.hasNext();) {
			OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk)consegne.get(i.nextIndex());
			bulk.getRigheConsegnaSelezionate().add(consegna);
		}
		if (bulk.getRigheConsegnaSelezionate() == null || bulk.getRigheConsegnaSelezionate().isEmpty()){
			throw new it.cnr.jada.comp.ApplicationException("Selezionare almeno una consegna da evadere!");
		} else {
			List<BollaScaricoMagBulk> listaBolleScarico = bp.evadiConsegne(actioncontext);
			bulk.setRigheConsegnaDaEvadereColl(new BulkList<>());
			return listaBolleScarico;
		}
	}
	
	public Forward doSelectConsegneDaEvadere(ActionContext context) {
	
		CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)getBusinessProcess(context);
	    try {
	        bp.getConsegne().setSelection(context);
	    } catch (Exception e) {
			return handleException(context, e);
	    }

		OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk)bp.getConsegne().getModel();
	    if (bp.getConsegne().getSelection().isSelected(bp.getConsegne().getSelection().getFocus())) {
			if (!Optional.ofNullable(consegna.getUnitaMisuraEvasa())
						 .map(UnitaMisuraBulk::getCdUnitaMisura)
						 .isPresent()) {
				consegna.setUnitaMisuraEvasa(consegna.getOrdineAcqRiga().getBeneServizio().getUnitaMisura());
				consegna.setCoefConvEvasa(BigDecimal.ONE);
				consegna.setQuantitaEvasa(consegna.getQuantita());
				bp.setDettConsegneCollapse(Boolean.TRUE);
			}
	    } else {
			Optional.ofNullable(consegna.getUnitaMisuraEvasa())
					.ifPresent(um->{
						consegna.setUnitaMisuraEvasa(null);
						consegna.setCoefConvEvasa(null);
						consegna.setQuantitaEvasa(null);
						bp.setDettConsegneCollapse(Boolean.FALSE);
					});
	    }
		return context.findDefaultForward();
	}

	public Forward doBringBackSearchFindUnitaMisuraEvasa(ActionContext context, OrdineAcqConsegnaBulk consegna, UnitaMisuraBulk unitaMisura) {
		try {
			fillModel(context);
			if (unitaMisura!=null) {
				CRUDEvasioneOrdineBP bp = (CRUDEvasioneOrdineBP)getBusinessProcess(context);
				bp.inizializeUnitaMisuraEvasa(context, consegna, unitaMisura);
				bp.setDirty(true);
			}
			return context.findDefaultForward();
		}catch (Exception ex) {
			return handleException(context, ex);
		}
	}
	
	public Forward doBlanckSearchFindUnitaMisuraEvasa(ActionContext context, ScaricoMagazzinoRigaBulk scaricoRiga) {
		scaricoRiga.setCoefConv(null);
		return context.findDefaultForward();
	}

    public Forward doToggleCriteriRicerca(ActionContext context) {
    	CRUDEvasioneOrdineBP bp = Optional.ofNullable(getBusinessProcess(context))
                .filter(CRUDEvasioneOrdineBP.class::isInstance)
                .map(CRUDEvasioneOrdineBP.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("Business Process non valido"));
        bp.setCriteriRicercaCollapse(!bp.isCriteriRicercaCollapse());
        return context.findDefaultForward();
    }

    public Forward doToggleDettConsegne(ActionContext context) {
    	CRUDEvasioneOrdineBP bp = Optional.ofNullable(getBusinessProcess(context))
                .filter(CRUDEvasioneOrdineBP.class::isInstance)
                .map(CRUDEvasioneOrdineBP.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("Business Process non valido"));
        bp.setDettConsegneCollapse(!bp.isDettConsegneCollapse());
        return context.findDefaultForward();
    }
}
