package it.cnr.contab.bilaterali00.action;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.bilaterali00.bp.CRUDBltVisiteBP;
import it.cnr.contab.bilaterali00.bulk.Blt_accordiBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_autorizzatiBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_progettiBulk;
import it.cnr.contab.bilaterali00.bulk.Blt_visiteBulk;
import it.cnr.contab.bilaterali00.ejb.BltVisiteComponentSession;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.doccont00.bp.CRUDObbligazioneBP;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.incarichi00.bp.CRUDIncarichiProceduraBP;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.util.EuroFormat;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.OptionBP;

import java.math.BigDecimal;
import java.rmi.RemoteException;

public class BltVisiteAction extends it.cnr.jada.util.action.CRUDAction{
	public BltVisiteAction() {
		super();
	}

	public Forward doOnAnnoVisitaChange(ActionContext context) {
		try {
			fillModel(context);
			CRUDBltVisiteBP bp = (CRUDBltVisiteBP)getBusinessProcess(context);
	
			Blt_visiteBulk visita = (Blt_visiteBulk)bp.getModel();
			visita.getBltAutorizzatiDett().getBltAutorizzati().setBltProgetti(new Blt_progettiBulk());
			visita.getBltAutorizzatiDett().getBltAutorizzati().setTerzo(null);
			return context.findDefaultForward();
	
		}catch (Throwable ex) {
			return handleException(context, ex);
		}
	}

	public Forward doBlankSearchFindAccordo(ActionContext context, Blt_visiteBulk visita) {
		if (visita!=null){
			visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().setBltAccordo(new Blt_accordiBulk());
			visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().setCd_progetto(null);
			visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().setDs_progetto_ita(null);
			visita.getBltAutorizzatiDett().getBltAutorizzati().setTerzo(null);
		}
		return context.findDefaultForward();
	}
	
	public Forward doBringBackSearchFindAccordo(ActionContext context, Blt_visiteBulk visita, Blt_accordiBulk accordo) {
		try {
			fillModel(context);
			CRUDBltVisiteBP bp = (CRUDBltVisiteBP)getBusinessProcess(context);
			if (accordo != null){
				visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().setBltAccordo(accordo);
				visita.setFlPagamentoEnte(visita.getFlPagamentoEnte());
				bp.setDirty(true);
			}	
			return context.findDefaultForward();
		}catch (Throwable ex) {
			return handleException(context, ex);
		}
	}

	public Forward doBlankSearchFindProgetto(ActionContext context, Blt_visiteBulk visita) {
		if (visita!=null){
			Blt_accordiBulk accordo = visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo();
			visita.getBltAutorizzatiDett().getBltAutorizzati().setBltProgetti(new Blt_progettiBulk());
			visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().setBltAccordo(accordo);
			visita.getBltAutorizzatiDett().getBltAutorizzati().setTerzo(null);
		}
		return context.findDefaultForward();
	}

	public Forward doBlankSearchFindTerzo(ActionContext context, Blt_visiteBulk visita) {
		if (visita!=null){
			Blt_autorizzatiBulk autorizzato = new Blt_autorizzatiBulk();
			autorizzato.setBltProgetti(visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti());
			visita.getBltAutorizzatiDett().setBltAutorizzati(autorizzato);
		}
		return context.findDefaultForward();
	}

	public Forward doBringBackSearchFindTerzo(ActionContext context, Blt_visiteBulk visita, TerzoBulk terzo) {
		try {
			fillModel(context);
			CRUDBltVisiteBP bp = (CRUDBltVisiteBP)getBusinessProcess(context);
			
			visita.getBltAutorizzatiDett().getBltAutorizzati().setTerzo(terzo);
			bp.caricaBltAutorizzati(context);
			return context.findDefaultForward();
	
		}catch (Throwable ex) {
			return handleException(context, ex);
		}
	}

	public Forward doStampaDispFinanziarie(ActionContext actioncontext) {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmStampaDispFinanziarie");
            else
                return doConfirmStampaDispFinanziarie(actioncontext, OptionBP.YES_BUTTON);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
	}
    public Forward doConfirmStampaDispFinanziarie(ActionContext actioncontext, int i) {
        try
        {
            if(i == OptionBP.YES_BUTTON)
            {
                CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
                bulkbp.edit(actioncontext, bulkbp.getModel());
    			it.cnr.jada.action.BusinessProcess businessprocess = actioncontext.createBusinessProcess("OfflineReportPrintBP");
                bulkbp.initializeStampaDispFinanziariePrintBP((OfflineReportPrintBP)businessprocess);
                actioncontext.addHookForward("default",this,"doBringBackStampaDocumentiCandidatura");
                return actioncontext.addBusinessProcess(businessprocess);
            } else
            {
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
    }
	
	public Forward doStampaAccettDispFinanziarie(ActionContext actioncontext) {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmStampaAccettDispFinanziarie");
            else
                return doConfirmStampaAccettDispFinanziarie(actioncontext, OptionBP.YES_BUTTON);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
	}
    public Forward doConfirmStampaAccettDispFinanziarie(ActionContext actioncontext, int i) {
        try
        {
            if(i == OptionBP.YES_BUTTON)
            {
                CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
                bulkbp.edit(actioncontext, bulkbp.getModel());
                it.cnr.jada.action.BusinessProcess businessprocess = actioncontext.createBusinessProcess("OfflineReportPrintBP");
                bulkbp.initializeStampaAccettDispFinanziariePrintBP((OfflineReportPrintBP)businessprocess);
                actioncontext.addHookForward("default",this,"doBringBackStampaDocumentiCandidatura");
                return actioncontext.addBusinessProcess(businessprocess);
            } else
            {
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
    }

	public Forward doStampaDocumentiCandidatura(ActionContext actioncontext) {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmStampaDocumentiCandidatura");
            else
                return doConfirmStampaDocumentiCandidatura(actioncontext, OptionBP.YES_BUTTON);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
	}
    public Forward doConfirmStampaDocumentiCandidatura(ActionContext actioncontext, int i) {
        try
        {
            if(i == OptionBP.YES_BUTTON)
            {
                CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
                bulkbp.edit(actioncontext, bulkbp.getModel());
                it.cnr.jada.action.BusinessProcess businessprocess = actioncontext.createBusinessProcess("OfflineReportPrintBP");
                bulkbp.initializeStampaDocumentiCandidaturaPrintBP((OfflineReportPrintBP)businessprocess);
                actioncontext.addHookForward("default",this,"doBringBackStampaDocumentiCandidatura");
                return actioncontext.addBusinessProcess(businessprocess);
            } else
            {
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
    }
	public Forward doStampaTrasmissioneCandidatura(ActionContext actioncontext) {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmStampaTrasmissioneCandidatura");
            else
                return doConfirmStampaTrasmissioneCandidatura(actioncontext, OptionBP.YES_BUTTON);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
	}
    public Forward doConfirmStampaTrasmissioneCandidatura(ActionContext actioncontext, int i) {
        try
        {
            if(i == OptionBP.YES_BUTTON)
            {
                CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
                bulkbp.edit(actioncontext, bulkbp.getModel());
                it.cnr.jada.action.BusinessProcess businessprocess = actioncontext.createBusinessProcess("OfflineReportPrintBP");
                bulkbp.initializeStampaTrasmissioneCandidaturaPrintBP((OfflineReportPrintBP)businessprocess);
                actioncontext.addHookForward("default",this,"doBringBackStampaDocumentiCandidatura");
                return actioncontext.addBusinessProcess(businessprocess);
            } else
            {
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
    }
	public Forward doStampaAutorizzazionePartenza(ActionContext actioncontext) {
        try
        {
        	CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmStampaAutorizzazionePartenza");
            else
                return doConfirmStampaAutorizzazionePartenza(actioncontext, OptionBP.YES_BUTTON);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
	}
    public Forward doConfirmStampaAutorizzazionePartenza(ActionContext actioncontext, int i) {
        try
        {
            if(i == OptionBP.YES_BUTTON)
            {
                CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
                bulkbp.edit(actioncontext, bulkbp.getModel());
                bulkbp.validaLancioStampaAutorizzazionePartenza();
                it.cnr.jada.action.BusinessProcess businessprocess = actioncontext.createBusinessProcess("OfflineReportPrintBP");
                bulkbp.initializeStampaAutorizzazionePartenzaPrintBP((OfflineReportPrintBP)businessprocess);
                actioncontext.addHookForward("default",this,"doBringBackStampaDocumentiCandidatura");
                return actioncontext.addBusinessProcess(businessprocess);
            } else
            {
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }
    public Forward doBringBackStampaDocumentiCandidatura(ActionContext actioncontext) throws RemoteException {
        try{
	    	CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
	    	bulkbp.edit(actioncontext, bulkbp.getModel());
//	    	bulkbp.setModel(actioncontext, bulkbp.initializeModelForEdit(actioncontext, bulkbp.getModel()));
	    	return super.doDefault(actioncontext);
       } catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
    }
    /**
     * Ricerca un'obbligazione valida da associare al doc amm
     * richeide la validità delle selezioni effettuate
     */
    public Forward doCreaObbligazione(ActionContext context) {
    	try {
    		CRUDBltVisiteBP bp = (CRUDBltVisiteBP)getBusinessProcess(context);
    		fillModel(context);

    		bp.validaCreazioneObbligazione(context);
    		context.addHookForward("bringback",this,"doBringBackCreaObbligazione");
    		CRUDObbligazioneBP obbligazioneBP = bp.createBPCreazioneObbligazione(context);
    		return context.addBusinessProcess(obbligazioneBP);
    	} catch(Throwable e) {
    		return handleException(context,e);
    	}
    }
    public Forward doBringBackCreaObbligazione(ActionContext context) {
    	try {
	    	HookForward caller = (HookForward)context.getCaller();
	    	Obbligazione_scadenzarioBulk obblig = (Obbligazione_scadenzarioBulk)caller.getParameter("bringback");
	    	CRUDBltVisiteBP bp = (CRUDBltVisiteBP)context.getBusinessProcess();
	    	if (obblig != null) {
	    		try {
	    			bp.validaObbligazione(context, obblig);
	    		} catch (Exception e) {
	    			throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
	    		}
	    		Blt_visiteBulk visita = (Blt_visiteBulk)bp.getModel();
	    		visita.setObbligazioneScadenzario(obblig);
	    		bp.setDirty(true);
	    		if (!"tabObbligazione".equals(bp.getTab("tab")))
	    			bp.setTab("tab", "tabObbligazione");
	    	}
	    	return context.findDefaultForward();
    	} catch(Throwable e) {
    		return handleException(context,e);
    	}
    }
	public Forward doStampaProvvedimentoObbligazione(ActionContext actioncontext) {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmStampaProvvedimentoObbligazione");
            else
                return doConfirmStampaProvvedimentoObbligazione(actioncontext, OptionBP.YES_BUTTON);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
	}
    public Forward doConfirmStampaProvvedimentoObbligazione(ActionContext actioncontext, int i) {
        try
        {
            if(i == OptionBP.YES_BUTTON)
            {
                CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
                bulkbp.edit(actioncontext, bulkbp.getModel());
                it.cnr.jada.action.BusinessProcess businessprocess = actioncontext.createBusinessProcess("OfflineReportPrintBP");
                bulkbp.initializeStampaProvvObbligazionePrintBP((OfflineReportPrintBP)businessprocess);
                actioncontext.addHookForward("default",this,"doBringBackStampaDocumentiCandidatura");
                return actioncontext.addBusinessProcess(businessprocess);
            } else
            {
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
    }

    protected BancaBulk getBancaDefaultForCdsFrom(ActionContext context, Blt_visiteBulk visita, java.util.Collection banche) {
   		if (banche == null || banche.isEmpty())
   			return null;
   		BancaBulk primaBanca = (BancaBulk) new java.util.Vector(banche).firstElement();
		return primaBanca;
    }

    public Forward doOnModalitaPagamentoChange(ActionContext context) {
    	try {
    		fillModel(context);
    		CRUDBltVisiteBP bp= (CRUDBltVisiteBP) getBusinessProcess(context);
    		BltVisiteComponentSession component= (BltVisiteComponentSession)bp.createComponentSession();
    		Blt_visiteBulk visita = (Blt_visiteBulk) bp.getModel();

    		//visualizza la prima banca della lista
    		if (visita.getModalitaPagamento() != null) {
    			java.util.Collection coll= component.findListaBanche(context.getUserContext(), visita);
    			visita.setBanca(getBancaDefaultForCdsFrom(context, visita, coll));
    			visita.setCessionario(component.findCessionario(context.getUserContext(), visita));
    		} else
    			visita.setBanca(null);

			if (visita.getModalitaPagamento()==null || !visita.getModalitaPagamento().getTi_pagamento().equals(Rif_modalita_pagamentoBulk.IBAN)) {
				visita.setNumProtAttestatoSogg(null);
				visita.setDtProtAttestatoSogg(null);
				visita.setDtIniVisitaEffettiva(null);
				visita.setDtFinVisitaEffettiva(null);
			}

    		bp.setModel(context, visita);
    	} catch (Throwable t) {
    		return handleException(context, t);
    	}
    	
    	return context.findDefaultForward();
    }

    public Forward doOnModalitaPagamentoAnticipoChange(ActionContext context) {
    	try {
    		fillModel(context);
    		CRUDBltVisiteBP bp= (CRUDBltVisiteBP) getBusinessProcess(context);
    		BltVisiteComponentSession component= (BltVisiteComponentSession)bp.createComponentSession();
    		Blt_visiteBulk visita = (Blt_visiteBulk) bp.getModel();

    		//visualizza la prima banca della lista
    		if (visita.getModalitaPagamentoAnticipo() != null) {
    			java.util.Collection coll= component.findListaBancheAnticipo(context.getUserContext(), visita);
    			visita.setBancaAnticipo(getBancaDefaultForCdsFrom(context, visita, coll));
    			visita.setCessionarioAnticipo(component.findCessionarioAnticipo(context.getUserContext(), visita));
    		} else
    			visita.setBancaAnticipo(null);

    		bp.setModel(context, visita);
    	} catch (Throwable t) {
    		return handleException(context, t);
    	}
    	
    	return context.findDefaultForward();
    }

    public Forward doStampaPagamentoAnticipo(ActionContext actioncontext) {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openConfirm(actioncontext, "L'operazione richiesta comporta il salvataggio delle modifiche effettuate. Vuoi continuare?", OptionBP.CONFIRM_YES_NO, "doConfirmStampaPagamentoAnticipo");
            else
                return doConfirmStampaPagamentoAnticipo(actioncontext, OptionBP.YES_BUTTON);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
	}
    public Forward doConfirmStampaPagamentoAnticipo(ActionContext actioncontext, int i) {
        try
        {
            if(i == OptionBP.YES_BUTTON)
            {
                CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
                if(bulkbp.isDirty())
                	bulkbp.save(actioncontext);
                bulkbp.edit(actioncontext, bulkbp.getModel());
                bulkbp.validaLancioStampaPagamentoAnticipo();
                it.cnr.jada.action.BusinessProcess businessprocess = actioncontext.createBusinessProcess("OfflineReportPrintBP");
                bulkbp.initializeStampaPagamentoAnticipoPrintBP((OfflineReportPrintBP)businessprocess);
                actioncontext.addHookForward("default",this,"doBringBackStampaDocumentiCandidatura");
                return actioncontext.addBusinessProcess(businessprocess);
            } else
            {
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doStampaPagamentoSaldo(ActionContext actioncontext) {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openConfirm(actioncontext, "L'operazione richiesta comporta il salvataggio delle modifiche effettuate. Vuoi continuare?", OptionBP.CONFIRM_YES_NO, "doConfirmStampaPagamentoSaldo");
            else
                return doConfirmStampaPagamentoSaldo(actioncontext, OptionBP.YES_BUTTON);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
	}
    public Forward doConfirmStampaPagamentoSaldo(ActionContext actioncontext, int i) {
        try
        {
            if(i == OptionBP.YES_BUTTON)
            {
                CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
                if(bulkbp.isDirty())
                	bulkbp.save(actioncontext);
                bulkbp.edit(actioncontext, bulkbp.getModel());
                bulkbp.validaLancioStampaPagamentoSaldo();
                it.cnr.jada.action.BusinessProcess businessprocess = actioncontext.createBusinessProcess("OfflineReportPrintBP");
                bulkbp.initializeStampaPagamentoSaldoPrintBP((OfflineReportPrintBP)businessprocess);
                actioncontext.addHookForward("default",this,"doBringBackStampaDocumentiCandidatura");
                return actioncontext.addBusinessProcess(businessprocess);
            } else
            {
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

	public Forward doStampaAnnullaProvvedimentoObbligazione(ActionContext actioncontext) {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmStampaAnnullaProvvedimentoObbligazione");
            else
                return doConfirmStampaAnnullaProvvedimentoObbligazione(actioncontext, OptionBP.YES_BUTTON);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
	}
    public Forward doConfirmStampaAnnullaProvvedimentoObbligazione(ActionContext actioncontext, int i) {
        try
        {
            if(i == OptionBP.YES_BUTTON)
            {
                CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
                bulkbp.edit(actioncontext, bulkbp.getModel());
                it.cnr.jada.action.BusinessProcess businessprocess = actioncontext.createBusinessProcess("OfflineReportPrintBP");
                bulkbp.initializeStampaAnnullaProvvObbligazionePrintBP((OfflineReportPrintBP)businessprocess);
                actioncontext.addHookForward("default",this,"doBringBackStampaDocumentiCandidatura");
                return actioncontext.addBusinessProcess(businessprocess);
            } else
            {
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
    }

	public Forward doAnnullaVisita(ActionContext actioncontext) {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmAnnullaVisita");
            else
                return doConfirmAnnullaVisita(actioncontext, OptionBP.YES_BUTTON);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
	}
	public Forward doConfirmAnnullaVisita(ActionContext actioncontext,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
                CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
                OggettoBulk model = bulkbp.getModel();
                bulkbp.reset(actioncontext);
                bulkbp.edit(actioncontext, model);

                Blt_visiteBulk visita = (Blt_visiteBulk) bulkbp.getModel();
    	        if (visita.getNumProtRimbSpese()!=null || visita.getDtProtRimbSpese()!=null ||
    	        	visita.getImRimbSpese()!=null || visita.getPgBanca()!=null) 
    	        	return openConfirm(actioncontext, "Attenzione! Saranno cancellati i dati del rimborso spese! Confermi l'annullamento della visita?", OptionBP.CONFIRM_YES_NO, "doConfirmNewAnnullaVisita");
            	return openConfirm(actioncontext, "Attenzione! Confermi l'annullamento della visita?", OptionBP.CONFIRM_YES_NO, "doConfirmNewAnnullaVisita");
			}
			return actioncontext.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(actioncontext,e);
		}
	}
	public Forward doConfirmNewAnnullaVisita(ActionContext context,int option) {
		try 
		{
			if ( option == OptionBP.YES_BUTTON) 
			{
				CRUDBltVisiteBP bp = (CRUDBltVisiteBP)getBusinessProcess(context);
				bp.annullaVisita(context);
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	public Forward doStampaModelloAccettazioneVisita(ActionContext actioncontext) {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmStampaModelloAccettazioneVisita");
            else
                return doConfirmStampaModelloAccettazioneVisita(actioncontext, OptionBP.YES_BUTTON);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
	}
    public Forward doConfirmStampaModelloAccettazioneVisita(ActionContext actioncontext, int i) {
        try
        {
            if(i == OptionBP.YES_BUTTON)
            {
                CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
                bulkbp.edit(actioncontext, bulkbp.getModel());
                it.cnr.jada.action.BusinessProcess businessprocess = actioncontext.createBusinessProcess("OfflineReportPrintBP");
                bulkbp.initializeStampaModelloAccettazioneVisitaPrintBP((OfflineReportPrintBP)businessprocess);
                actioncontext.addHookForward("default",this,"doBringBackStampaDocumentiCandidatura");
                return actioncontext.addBusinessProcess(businessprocess);
            } else
            {
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
    }
	public Forward doStampaModelloAttestatoSoggiorno(ActionContext actioncontext) {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmStampaModelloAttestatoSoggiorno");
            else
                return doConfirmStampaModelloAttestatoSoggiorno(actioncontext, OptionBP.YES_BUTTON);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
	}
    public Forward doConfirmStampaModelloAttestatoSoggiorno(ActionContext actioncontext, int i) {
        try
        {
            if(i == OptionBP.YES_BUTTON)
            {
                CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
                bulkbp.edit(actioncontext, bulkbp.getModel());
                it.cnr.jada.action.BusinessProcess businessprocess = actioncontext.createBusinessProcess("OfflineReportPrintBP");
                bulkbp.initializeStampaModelloAttestatoSoggiornoPrintBP((OfflineReportPrintBP)businessprocess);
                actioncontext.addHookForward("default",this,"doBringBackStampaDocumentiCandidatura");
                return actioncontext.addBusinessProcess(businessprocess);
            } else
            {
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
    }
	public Forward doStampaAttribuzioneIncarico(ActionContext actioncontext) {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmStampaAttribuzioneIncarico");
            else
                return doConfirmStampaAttribuzioneIncarico(actioncontext, OptionBP.YES_BUTTON);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
	}
    public Forward doConfirmStampaAttribuzioneIncarico(ActionContext actioncontext, int i) {
        try
        {
            if(i == OptionBP.YES_BUTTON)
            {
                CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
                bulkbp.edit(actioncontext, bulkbp.getModel());
                it.cnr.jada.action.BusinessProcess businessprocess = actioncontext.createBusinessProcess("OfflineReportPrintBP");
                bulkbp.initializeStampaAttribuzioneIncaricoPrintBP((OfflineReportPrintBP)businessprocess);
                actioncontext.addHookForward("default",this,"doBringBackStampaDocumentiCandidatura");
                return actioncontext.addBusinessProcess(businessprocess);
            } else
            {
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
    }
	public Forward doStampaModelloContratto(ActionContext actioncontext) {
        try
        {
        	CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmStampaModelloContratto");
            else
                return doConfirmStampaModelloContratto(actioncontext, OptionBP.YES_BUTTON);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
	}
    public Forward doConfirmStampaModelloContratto(ActionContext actioncontext, int i) {
        try
        {
            if(i == OptionBP.YES_BUTTON)
            {
                CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
                bulkbp.edit(actioncontext, bulkbp.getModel());
                it.cnr.jada.action.BusinessProcess businessprocess = actioncontext.createBusinessProcess("OfflineReportPrintBP");
                bulkbp.initializeStampaModelloContrattoPrintBP((OfflineReportPrintBP)businessprocess);
                actioncontext.addHookForward("default",this,"doBringBackStampaDocumentiCandidatura");
                return actioncontext.addBusinessProcess(businessprocess);
            } else
            {
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }
	public Forward doStampaNotaAddebito(ActionContext actioncontext) {
        try
        {
        	CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmStampaNotaAddebito");
            else
                return doConfirmStampaNotaAddebito(actioncontext, OptionBP.YES_BUTTON);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
	}
    public Forward doConfirmStampaNotaAddebito(ActionContext actioncontext, int i) {
        try
        {
            if(i == OptionBP.YES_BUTTON)
            {
                CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
                bulkbp.edit(actioncontext, bulkbp.getModel());
                it.cnr.jada.action.BusinessProcess businessprocess = actioncontext.createBusinessProcess("OfflineReportPrintBP");
                bulkbp.initializeStampaNotaAddebitoPrintBP((OfflineReportPrintBP)businessprocess);
                actioncontext.addHookForward("default",this,"doBringBackStampaDocumentiCandidatura");
                return actioncontext.addBusinessProcess(businessprocess);
            } else
            {
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }
    public Forward doCreaIncarico(ActionContext actioncontext) {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmCreaIncarico");
            else
                return doConfirmCreaIncarico(actioncontext, OptionBP.YES_BUTTON);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }
    public Forward doConfirmCreaIncarico(ActionContext actioncontext, int i) {
        try
        {
            if(i == OptionBP.YES_BUTTON)
            {
                CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
                bulkbp.edit(actioncontext, bulkbp.getModel());
                bulkbp.validaLancioCreazioneIncarico();
                CRUDIncarichiProceduraBP incaricoProceduraBP = bulkbp.createBPCreazioneIncarico(actioncontext);
        		incaricoProceduraBP.setBringBack(true);
                actioncontext.addHookForward("bringback",this,"doBringBackCreaIncarico");
                return actioncontext.addBusinessProcess(incaricoProceduraBP);
            } else
            {
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }
    public Forward doApriIncarico(ActionContext context) {
    	try {
    		CRUDBltVisiteBP bp = (CRUDBltVisiteBP)getBusinessProcess(context);
    		fillModel(context);

    		Incarichi_repertorioBulk incarico = ((Blt_visiteBulk)bp.getModel()).getIncaricoRepertorio();

    		if (incarico==null){
    			bp.setMessage("La visita non risulta collegata ad alcun incarico.");
    			return context.findDefaultForward();
    		}
    		else if (incarico.getIncarichi_procedura()==null) {
    			bp.setMessage("L'incarico collegato alla visita non risulta appartenere ad alcuna procedura di conferimento incarichi.");
    			return context.findDefaultForward();
    		}

    		CRUDBP newBP = (CRUDBP)context.getUserInfo().createBusinessProcess(
    				context,
    				"CRUDIncarichiProceduraBP",
    				new Object[] {
    					"MRSW",
    					bp.getModel()
    				}
    			);

    		newBP.edit(context, incarico.getIncarichi_procedura());
    		newBP.setBringBack(true);
    		context.addHookForward("default",this,"doBringBackStampaDocumentiCandidatura");

    		return context.addBusinessProcess(newBP);
    	} catch(Throwable e) {
    		return handleException(context,e);
    	}
    }
    public Forward doBringBackCreaIncarico(ActionContext context) {
		try {
	    	HookForward caller = (HookForward)context.getCaller();
	    	Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)caller.getParameter("bringback");
	    	CRUDBltVisiteBP bp = (CRUDBltVisiteBP)context.getBusinessProcess();
	    	if (procedura != null) {
	    		Blt_visiteBulk visita = (Blt_visiteBulk)bp.getModel();
	    		visita.setIncaricoRepertorio((Incarichi_repertorioBulk)procedura.getIncarichi_repertorioColl().get(0));
				bp.save(context);
	    		if (!"tabIncarico".equals(bp.getTab("tab")))
	    			bp.setTab("tab", "tabObbligazione");
	    	}
	    	return context.findDefaultForward();
		} catch (BusinessProcessException e) {
    		return handleException(context,e);
		} catch (ValidationException e) {
    		return handleException(context,e);
		}
    }

	public Forward doOnImRimbSpeseAntChange(ActionContext context) {
		try {
			fillModel(context);
			CRUDBltVisiteBP bp = (CRUDBltVisiteBP)getBusinessProcess(context);
	
			Blt_visiteBulk visita = (Blt_visiteBulk)bp.getModel();
			if (visita.getImRimbSpeseAnt().compareTo(BigDecimal.ZERO)==0)
				throw new ValidationException( "Inserire un importo positivo.");
			else if (visita.getImRimbSpeseAnt().compareTo(visita.getImRimbPrevisto())==1)
				throw new ValidationException( "Non è possibile inserire un importo superiore al rimborso totale previsto di "+new EuroFormat().format(visita.getImRimbPrevisto()));
			else if (visita.getImRimbSpeseAnt().compareTo(visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getImporto_max_anticipo())==1)
				throw new ValidationException( "Non è possibile inserire un importo superiore all'importo massimo previsto per un anticipo ("+new EuroFormat().format(visita.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getImporto_max_anticipo())+")");

			visita.setImRimbSpese(visita.getImRimbPrevisto().subtract(visita.getImRimbSpeseAnt()));

			return context.findDefaultForward();
		}catch (Throwable ex) {
			return handleException(context, ex);
		}
	}
	public Forward doStampaModuloRimborsoSpese(ActionContext actioncontext) {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmStampaModuloRimborsoSpese");
            else
                return doConfirmStampaModuloRimborsoSpese(actioncontext, OptionBP.YES_BUTTON);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
	}
    public Forward doConfirmStampaModuloRimborsoSpese(ActionContext actioncontext, int i) {
        try
        {
            if(i == OptionBP.YES_BUTTON)
            {
                CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
                bulkbp.edit(actioncontext, bulkbp.getModel());
                it.cnr.jada.action.BusinessProcess businessprocess = actioncontext.createBusinessProcess("OfflineReportPrintBP");
                bulkbp.initializeStampaModuloRimborsoSpesePrintBP((OfflineReportPrintBP)businessprocess);
                actioncontext.addHookForward("default",this,"doBringBackStampaDocumentiCandidatura");
                return actioncontext.addBusinessProcess(businessprocess);
            } else
            {
                return actioncontext.findDefaultForward();
            }
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
    }

	public Forward doFasePrecedente(ActionContext actioncontext) {
        try
        {
            BulkBP bulkbp = (BulkBP)actioncontext.getBusinessProcess();
            fillModel(actioncontext);
            if(bulkbp.isDirty())
                return openContinuePrompt(actioncontext, "doConfirmFasePrecedente");
            else
                return doConfirmFasePrecedente(actioncontext, OptionBP.YES_BUTTON);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
	}
    public Forward doConfirmFasePrecedente(ActionContext actioncontext, int i) {
        try
        {
            if(i == OptionBP.YES_BUTTON)
            {
                CRUDBltVisiteBP bulkbp = (CRUDBltVisiteBP)actioncontext.getBusinessProcess();
                bulkbp.setModel(actioncontext, bulkbp.returnToFasePrecedente(actioncontext, (Blt_visiteBulk)bulkbp.getModel()));
            }
            return actioncontext.findDefaultForward();
        }
        catch(BusinessProcessException businessprocessexception)
        {
            return handleException(actioncontext, businessprocessexception);
        }
    }
 
    /**
     * Ricerca le banche valide
     */
    public Forward doSearchListabanche(ActionContext context) {
    	Blt_visiteBulk visita = (Blt_visiteBulk)getBusinessProcess(context).getModel();
   		return search(context, getFormField(context, "main.listabanche"), visita.getModalitaPagamento().getTiPagamentoColumnSet());
    }

    public Forward doSearchListabancheAnt(ActionContext context) {
    	Blt_visiteBulk visita = (Blt_visiteBulk)getBusinessProcess(context).getModel();
   		return search(context, getFormField(context, "main.listabancheAnt"), visita.getModalitaPagamentoAnticipo().getTiPagamentoColumnSet());
    }
}