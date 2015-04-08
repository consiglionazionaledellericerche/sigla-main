package it.cnr.contab.docamm00.actions;

import it.cnr.contab.anagraf00.bp.CRUDTerzoBP;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.bp.CRUDFatturaPassivaBP;
import it.cnr.contab.docamm00.bp.CRUDFatturaPassivaElettronicaBP;
import it.cnr.contab.docamm00.bp.CRUDNotaDiCreditoBP;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.ejb.FatturaElettronicaPassivaComponentSession;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.action.MessageToUser;
import it.cnr.jada.action.StaticForward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.FormField;
import it.cnr.jada.util.action.SelezionatoreListaAction;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.gov.fatturapa.sdi.fatturapa.v1.SoggettoEmittenteType;

import java.rmi.RemoteException;
import java.util.Iterator;

import javax.ejb.RemoveException;

public class CRUDFatturaPassivaElettronicaAction extends CRUDAction {
	private static final long serialVersionUID = 1L;
	
	@Override
	public Forward doCerca(ActionContext actioncontext) throws RemoteException,
			InstantiationException, RemoveException {
		try {

			CRUDFatturaPassivaElettronicaBP fatturaPassivaElettronicaBP = (CRUDFatturaPassivaElettronicaBP) actioncontext.getBusinessProcess();
			DocumentoEleTestataBulk bulk = (DocumentoEleTestataBulk) fatturaPassivaElettronicaBP.getModel();
			fillModel(actioncontext);
	        RemoteIterator remoteiterator = fatturaPassivaElettronicaBP.find(actioncontext, null, bulk);
	        if(remoteiterator == null || remoteiterator.countElements() == 0)
	        {
	            EJBCommonServices.closeRemoteIterator(remoteiterator);
	            fatturaPassivaElettronicaBP.setMessage("La ricerca non ha fornito alcun risultato.");
	            return actioncontext.findDefaultForward();
	        }
	        if(remoteiterator.countElements() == 1)
	        {
	            OggettoBulk oggettobulk1 = (OggettoBulk)remoteiterator.nextElement();
	            EJBCommonServices.closeRemoteIterator(remoteiterator);
	            fatturaPassivaElettronicaBP.setMessage("La ricerca ha fornito un solo risultato.");
	            return doRiportaSelezione(actioncontext, oggettobulk1);
	        } else
	        {
	        	fatturaPassivaElettronicaBP.setModel(actioncontext, bulk);
	            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP)actioncontext.createBusinessProcess("Selezionatore");
	            selezionatorelistabp.setModel(actioncontext, bulk);
	            selezionatorelistabp.setDefaultAction("SelezionaFatturaPassivaElettronicaAction");
				StaticForward staticForward = new StaticForward();
				staticForward.setName("default");
				staticForward.setPath("/docamm00/selezionatore_fatt_ele.jsp");
				selezionatorelistabp.getMapping().addForward(staticForward);

	            selezionatorelistabp.setIterator(actioncontext, remoteiterator);
	            selezionatorelistabp.setBulkInfo(fatturaPassivaElettronicaBP.getSearchBulkInfo());
	            selezionatorelistabp.setColumns(getBusinessProcess(actioncontext).getSearchResultColumns());
	            actioncontext.addHookForward("seleziona", this, "doRiportaSelezione");
	            return actioncontext.addBusinessProcess(selezionatorelistabp);
	        }
		} catch (Exception e) {
			return handleException(actioncontext, e);
		}	
	}
		
	public static class SelezionatoreFatturaPassivaElettronicaAction extends SelezionatoreListaAction {
		private static final long serialVersionUID = 1L;

		public SelezionatoreFatturaPassivaElettronicaAction() {
			super();
		}

		public Forward doCambiaVisibilita(ActionContext actioncontext)
				throws RemoteException {
			SelezionatoreListaBP bp = (SelezionatoreListaBP)actioncontext.getBusinessProcess();
			DocumentoEleTestataBulk bulk = (DocumentoEleTestataBulk)bp.getModel();
			try {
				fillModel(actioncontext);
				String statoDocumento = bulk.getStatoDocumento();
				if (statoDocumento.equalsIgnoreCase(DocumentoEleTestataBulk.STATO_DOCUMENTO_TUTTI))
					bulk.setStatoDocumento(null);				
				EJBCommonServices.closeRemoteIterator(bp.detachIterator());
				bp.setIterator(actioncontext, ((FatturaElettronicaPassivaComponentSession)
						bp.createComponentSession("CNRDOCAMM00_EJB_FatturaElettronicaPassivaComponentSession", FatturaElettronicaPassivaComponentSession.class)).
						cerca(actioncontext.getUserContext(), null, bulk));
				bp.refresh(actioncontext);
				bulk.setStatoDocumento(statoDocumento);
				return actioncontext.findDefaultForward();
			} catch(Throwable e) {
				bulk.setStatoDocumento(null);
				return handleException(actioncontext,e);
			}
		}
	}
	public Forward doBringBackCRUDPrestatore(ActionContext context, DocumentoEleTestataBulk bulk, TerzoBulk terzo) {
		if (terzo != null) {
			if (terzo.getAnagrafico().getPartita_iva() == null)
				throw new MessageToUser("Terzo non valido! Partita IVA obbligatoria!");						
			if (terzo.getAnagrafico().getCodice_fiscale() == null)
				throw new MessageToUser("Terzo non valido! Codice Fiscale obbligatorio!");
			if (bulk.getDocumentoEleTrasmissione().getPrestatoreCodicefiscale() != null) {
				if (!bulk.getDocumentoEleTrasmissione().getPrestatoreCodicefiscale().equals(terzo.getAnagrafico().getCodice_fiscale()))
					throw new MessageToUser("Terzo non valido! Codice Fiscale non congruente!");				
			}
			if (bulk.getDocumentoEleTrasmissione().getPrestatoreCodice() != null) {
				if (!bulk.getDocumentoEleTrasmissione().getPrestatoreCodice().equals(terzo.getAnagrafico().getPartita_iva()))
					throw new MessageToUser("Terzo non valido! Partita IVA non congruente!");		
			}			
			bulk.getDocumentoEleTrasmissione().setPrestatore(terzo);
			bulk.getDocumentoEleTrasmissione().setPrestatoreAnag(terzo.getAnagrafico());			
		}
		return context.findDefaultForward();
	}
	
	@SuppressWarnings("unchecked")
	public Forward doBringBackCRUDModalitaPagamento(ActionContext context, DocumentoEleTestataBulk bulk, TerzoBulk terzo) {
		if (terzo != null) {			
			if (!terzo.equalsByPrimaryKey(bulk.getDocumentoEleTrasmissione().getPrestatore()))
				throw new MessageToUser("Il Terzo selezionato non è valido!");
			Modalita_pagamentoBulk modalitaPagamento = null;
			for (Iterator<Modalita_pagamentoBulk> iterator = terzo.getModalita_pagamento().iterator(); iterator.hasNext();) {
				Modalita_pagamentoBulk modPag = iterator.next();
				if (modPag.getRif_modalita_pagamento().getTipoPagamentoSdi() != null && 
						modPag.getRif_modalita_pagamento().getTipoPagamentoSdi().equals(bulk.getBeneficiarioModPag()))
					modalitaPagamento = modPag;
			}
			if (modalitaPagamento == null )
				throw new MessageToUser("La Modalità di pagamento non è valida!");
			bulk.setModalitaPagamento(modalitaPagamento);		
		}
		return context.findDefaultForward();
	}	
	public Forward doCRUDModalitaPagamento(ActionContext context) throws FillException, BusinessProcessException {
		CRUDFatturaPassivaElettronicaBP fatturaPassivaElettronicaBP = (CRUDFatturaPassivaElettronicaBP) context.getBusinessProcess();
		DocumentoEleTestataBulk bulk = (DocumentoEleTestataBulk) fatturaPassivaElettronicaBP.getModel();
		
		AnagraficoBulk anagrafico = null;
		TerzoBulk terzo = null;
    	if (bulk.getDocumentoEleTrasmissione().getSoggettoEmittente() != null && 
    			bulk.getDocumentoEleTrasmissione().getSoggettoEmittente().equals(SoggettoEmittenteType.TZ.value())) {
    		if (bulk.getDocumentoEleTrasmissione().getIntermediarioCdTerzo() != null){
    			anagrafico = bulk.getDocumentoEleTrasmissione().getIntermediarioAnag();
    			terzo = bulk.getDocumentoEleTrasmissione().getIntermediario();
    		} else if (bulk.getDocumentoEleTrasmissione().getRappresentanteCdTerzo() != null) {
    			anagrafico = bulk.getDocumentoEleTrasmissione().getRappresentanteAnag();
    			terzo = bulk.getDocumentoEleTrasmissione().getRappresentante();
    		} else {
        		anagrafico = bulk.getDocumentoEleTrasmissione().getPrestatoreAnag();
        		terzo = bulk.getDocumentoEleTrasmissione().getPrestatore();    			
    		}
    	} else {
    		anagrafico = bulk.getDocumentoEleTrasmissione().getPrestatoreAnag();
    		terzo = bulk.getDocumentoEleTrasmissione().getPrestatore();
    	}
				
		FormField formfield = getFormField(context, "main.modalitaPagamento");
		try {
			CRUDTerzoBP nbp = (CRUDTerzoBP)context.createBusinessProcess("CRUDTerzoBP",
							new Object[] {"M", anagrafico}
						);
			nbp.basicEdit(context, terzo, true);
			nbp.setTab("tab", "tabModalitaPagamento");
			context.addHookForward("bringback", this, "doBringBackCRUD");
			HookForward hookforward = (HookForward)context.findForward("bringback");
			hookforward.addParameter("field", formfield);
			nbp.setBringBack(true);
			return context.addBusinessProcess(nbp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}	

	public Forward doConfirmRifiutaFattura(ActionContext context, it.cnr.jada.util.action.OptionBP option) throws BusinessProcessException {
		CRUDFatturaPassivaElettronicaBP fatturaPassivaElettronicaBP = (CRUDFatturaPassivaElettronicaBP) context.getBusinessProcess();
		DocumentoEleTestataBulk bulk = (DocumentoEleTestataBulk) fatturaPassivaElettronicaBP.getModel();
		try {
			if (option.getOption() == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
				Object motivoRifiuto = ((HttpActionContext)context).getRequest().getParameter("main.motivoRifiuto");
				if (motivoRifiuto != null) {
					bulk.setMotivoRifiuto(String.valueOf(motivoRifiuto));
					fatturaPassivaElettronicaBP.setModel(context, bulk);
					fatturaPassivaElettronicaBP.rifiutaFattura(context);					
				} else {
					fatturaPassivaElettronicaBP.setMessage("Il Motivo del rifiuto è obbligatorio!");
				}				
			}			
		} catch (Exception e) {
			return handleException(context,e);
		}		
		return context.findDefaultForward();
	}
	
	public Forward doRifiutaFattura(ActionContext context) throws FillException, BusinessProcessException {
		try {
			String message = "Inserire il motivo di rifiuto della fattura:";
			message += "<textarea name=\"main.motivoRifiuto\" class=\"FormInput\" "+
					"cols=\"60\" rows=\"5\" onfocus=\"focused(this)\" onclick=\"cancelBubble(event)\"></textarea>";
			openConfirm( context, message, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doConfirmRifiutaFattura");
			return context.findDefaultForward();			
		} catch (Exception e) {
			return handleException(context,e);
		}
	}
	public Forward doCompilaFattura(ActionContext context) throws FillException, BusinessProcessException {
		try {
			CRUDFatturaPassivaElettronicaBP fatturaPassivaElettronicaBP = (CRUDFatturaPassivaElettronicaBP) context.getBusinessProcess();
			DocumentoEleTestataBulk bulk = (DocumentoEleTestataBulk) fatturaPassivaElettronicaBP.getModel();
			if (bulk.getImportoDocumento() == null) {
				fatturaPassivaElettronicaBP.setMessage("Il totale del documento non è valorizzato, il documento deve essere rifiutato!");
			} else {
				String message = "La compilazione della Fattura e il suo successivo salvataggio, ";
				message += "comporta l'accettazione del documento elettronico.<br>Si desidera procedere?";
				openConfirm( context, message, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doConfirmCompilaFattura");				
			}
			return context.findDefaultForward();			
		} catch (Exception e) {
			return handleException(context,e);
		}
	}
	
    public Forward doBringBackCompilaFattura(ActionContext context) throws RemoteException {
        try{
			CRUDFatturaPassivaElettronicaBP fatturaPassivaElettronicaBP = (CRUDFatturaPassivaElettronicaBP) context.getBusinessProcess();
			fatturaPassivaElettronicaBP.edit(context, (OggettoBulk) fatturaPassivaElettronicaBP.createComponentSession().findByPrimaryKey(context.getUserContext(), fatturaPassivaElettronicaBP.getModel()));
	    	return super.doDefault(context);
       } catch(BusinessProcessException businessprocessexception){
    	   return handleException(context, businessprocessexception);
       } catch (ComponentException e) {
    	   return handleException(context, e);
       }
    }
	
    public Forward doVisualizzaFattura(ActionContext context) throws FillException, BusinessProcessException {
		CRUDFatturaPassivaElettronicaBP fatturaPassivaElettronicaBP = (CRUDFatturaPassivaElettronicaBP) context.getBusinessProcess();
		DocumentoEleTestataBulk bulk = (DocumentoEleTestataBulk) fatturaPassivaElettronicaBP.getModel();
		try {
			CRUDFatturaPassivaBP nbp = (CRUDFatturaPassivaBP)context.createBusinessProcess(bulk.getBusinessProcessFattura(),
							new Object[] {"M"}
						);
			nbp = (CRUDFatturaPassivaBP) context.addBusinessProcess(nbp);
			nbp.edit(context, ((FatturaElettronicaPassivaComponentSession)fatturaPassivaElettronicaBP.createComponentSession()).
					cercaFatturaPassiva(context.getUserContext(), bulk));
			return nbp;
		} catch(Throwable e) {
			return handleException(context,e);
		}		    	
    }
    
	public Forward doConfirmCompilaFattura(ActionContext context, it.cnr.jada.util.action.OptionBP option) throws FillException, BusinessProcessException {
		if (option.getOption() == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
			CRUDFatturaPassivaElettronicaBP fatturaPassivaElettronicaBP = (CRUDFatturaPassivaElettronicaBP) context.getBusinessProcess();
			DocumentoEleTestataBulk bulk = (DocumentoEleTestataBulk) fatturaPassivaElettronicaBP.getModel();
			CRUDFatturaPassivaAction action = new CRUDFatturaPassivaAction();
			try {
				CRUDFatturaPassivaBP nbp = (CRUDFatturaPassivaBP)context.createBusinessProcess("CRUDFatturaPassivaBP",
								new Object[] {"M"}
							);
				String mode = it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().
						validaBPPerUtente(context.getUserContext(),((CNRUserInfo)context.getUserInfo()).getUtente(),
								((CNRUserInfo)context.getUserInfo()).getUtente().isUtenteComune() ? 
										((CNRUserInfo)context.getUserInfo()).getUnita_organizzativa().getCd_unita_organizzativa() : 
											"*","CRUDFatturaPassivaBP");
				if (mode == null) 
					throw new it.cnr.jada.action.MessageToUser("Accesso non consentito alla mappa di creazione delle fatture. Impossibile continuare.");
				
				context.addHookForward("default",this,"doBringBackCompilaFattura");
				nbp = (CRUDFatturaPassivaBP) context.addBusinessProcess(nbp);
				if (bulk.getTipoDocumentoSIGLA().equalsIgnoreCase(Fattura_passivaBulk.TIPO_NOTA_DI_CREDITO)) {
					Fattura_passivaBulk fatturaPassivaBulk = ((FatturaElettronicaPassivaComponentSession)fatturaPassivaElettronicaBP.createComponentSession()).
						cercaFatturaPassivaForNota(context.getUserContext(), bulk);
					nbp.edit(context, fatturaPassivaBulk);
					CRUDNotaDiCreditoBP notaBp = (CRUDNotaDiCreditoBP)action.doGeneraNotaDiCredito(context);
					notaBp.setModel(context, fatturaPassivaElettronicaBP.completaFatturaPassiva(context, (Fattura_passivaBulk) notaBp.getModel(), notaBp));
				} else {
					Fattura_passivaBulk fatturaPassivaBulk = (Fattura_passivaBulk) nbp.getModel();
					nbp.setModel(context, fatturaPassivaElettronicaBP.completaFatturaPassiva(context, fatturaPassivaBulk, nbp));					
				}
				return nbp;
			} catch(Throwable e) {
				return handleException(context,e);
			}		
		}
		return context.findDefaultForward();
	}	
}