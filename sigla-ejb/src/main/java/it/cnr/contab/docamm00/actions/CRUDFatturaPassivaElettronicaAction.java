package it.cnr.contab.docamm00.actions;

import it.cnr.contab.anagraf00.bp.CRUDTerzoBP;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.bp.CRUDFatturaPassivaBP;
import it.cnr.contab.docamm00.bp.CRUDFatturaPassivaElettronicaBP;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.ejb.FatturaElettronicaPassivaComponentSession;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleIvaBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.action.MessageToUser;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.RegimeFiscaleType;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.SoggettoEmittenteType;

import java.math.BigDecimal;
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
	            EJBCommonServices.closeRemoteIterator(actioncontext,remoteiterator);
	            fatturaPassivaElettronicaBP.setMessage("La ricerca non ha fornito alcun risultato.");
	            return actioncontext.findDefaultForward();
	        }
	        if(remoteiterator.countElements() == 1)
	        {
	            OggettoBulk oggettobulk1 = (OggettoBulk)remoteiterator.nextElement();
	            EJBCommonServices.closeRemoteIterator(actioncontext,remoteiterator);
	            fatturaPassivaElettronicaBP.setMessage(FormBP.INFO_MESSAGE,"La ricerca ha fornito un solo risultato.");
	            return doRiportaSelezione(actioncontext, oggettobulk1);
	        } else
	        {
	        	fatturaPassivaElettronicaBP.setModel(actioncontext, bulk);
	            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP)actioncontext.createBusinessProcess("SelezionatoreFatturaElettronicaBP");
	            selezionatorelistabp.setModel(actioncontext, bulk);
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
				EJBCommonServices.closeRemoteIterator(actioncontext,bp.detachIterator());
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

	public Forward doReinviaEsito(ActionContext context) throws FillException, BusinessProcessException {
		CRUDFatturaPassivaElettronicaBP fatturaPassivaElettronicaBP = (CRUDFatturaPassivaElettronicaBP) context.getBusinessProcess();
		DocumentoEleTestataBulk bulk = (DocumentoEleTestataBulk) fatturaPassivaElettronicaBP.getModel();
		try {
			fatturaPassivaElettronicaBP.reinviaEsito(context, bulk);
			fatturaPassivaElettronicaBP.setMessage("Esito inviato correttamente, in attesa di risposta dal Sistema di Interscambio.");
			return context.findDefaultForward();
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
				if (motivoRifiuto != null && String.valueOf(motivoRifiuto).length() > 0) {
					bulk.setMotivoRifiuto(String.valueOf(motivoRifiuto));
					fatturaPassivaElettronicaBP.rifiutaFattura(context, bulk);					
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
			CRUDFatturaPassivaElettronicaBP fatturaPassivaElettronicaBP = (CRUDFatturaPassivaElettronicaBP) context.getBusinessProcess();
			DocumentoEleTestataBulk bulk = (DocumentoEleTestataBulk) fatturaPassivaElettronicaBP.getModel();
			if (bulk.getFlDecorrenzaTermini().equalsIgnoreCase("S")) {
				fatturaPassivaElettronicaBP.setMessage("Ricevuta decorrenza termini - non è possibile effettuare il Rifiuto. Registrare il documento e richiedere nota credito, oppure rifiutare il documento secondo le modalità di invio PEC (Vedere Manuale)!");
			} else {
				String message = "Inserire il motivo di rifiuto della fattura:";
				message += "<textarea maxLength=\"255\" name=\"main.motivoRifiuto\" class=\"FormInput w-100\" "+
						"cols=\"60\" rows=\"5\" onfocus=\"focused(this)\" onclick=\"cancelBubble(event)\"></textarea>";
				openConfirm( context, message, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doConfirmRifiutaFattura");				
			}
			return context.findDefaultForward();			
		} catch (Exception e) {
			return handleException(context,e);
		}
	}
	public Forward doCompilaFattura(ActionContext context) throws FillException, BusinessProcessException {
		try {
			CRUDFatturaPassivaElettronicaBP fatturaPassivaElettronicaBP = (CRUDFatturaPassivaElettronicaBP) context.getBusinessProcess();
			DocumentoEleTestataBulk bulk = (DocumentoEleTestataBulk) fatturaPassivaElettronicaBP.getModel();
			BigDecimal tot_riepilogo=BigDecimal.ZERO;
			boolean hasAccesso = ((it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession")).controllaAccesso(context.getUserContext(), "AMMFATTURDOCSFATPASA");
			if (bulk.getImportoDocumento() == null) {
				fatturaPassivaElettronicaBP.setMessage("Prima di procedere verificare il totale del documento!");
				return context.findDefaultForward();
			} else if (!bulk.getDocEleIVAColl().isEmpty()){
				
				for(Iterator i=bulk.getDocEleIVAColl().iterator();i.hasNext();)
			    {
			      DocumentoEleIvaBulk rigaEle=(DocumentoEleIvaBulk)i.next();
			      // prof
				  if ((bulk.getDocEleTributiColl()!=null && !bulk.getDocEleTributiColl().isEmpty()) 
							||(bulk.getDocumentoEleTrasmissione().getRegimefiscale()!= null && 
							(bulk.getDocumentoEleTrasmissione().getRegimefiscale().equals(RegimeFiscaleType.RF_02.name()) ||
									bulk.getDocumentoEleTrasmissione().getRegimefiscale().equals(RegimeFiscaleType.RF_19.name())))) {
					  if (!bulk.isAttivoSplitPaymentProf() && (rigaEle.getImposta()!=null && rigaEle.getImposta().compareTo(BigDecimal.ZERO)!=0) && 
						   rigaEle.getEsigibilitaIva()!=null && rigaEle.getEsigibilitaIva().compareTo("I")!=0  &&  !hasAccesso ) {
				    	  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
				    	  fatturaPassivaElettronicaBP.setMessage("La tipologia di esigibilità IVA non deve essere di tipo 'Differita' "
				    	  		+ "o 'Split Payment'"
				    	  		+ (fatturaPassivaElettronicaBP.getDataAttivazioneSplitProf()!=null?
				    	  			" per documenti con data emissione antecedente al "+sdf.format(fatturaPassivaElettronicaBP.getDataAttivazioneSplitProf()):"")
				    	  		+(fatturaPassivaElettronicaBP.getDataDisattivazioneSplitProf()!=null? 
				    	  			" o successiva al "+sdf.format(fatturaPassivaElettronicaBP.getDataDisattivazioneSplitProf()):"")
				    	  		+ ". Il documento deve essere rifiutato!");	
				    	  return context.findDefaultForward();			    	  
				      }
				  }else{
					if (!bulk.isAttivoSplitPayment() && rigaEle.getEsigibilitaIva()!=null && rigaEle.getEsigibilitaIva().compareTo("I")!=0) {
						  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
						  fatturaPassivaElettronicaBP.setMessage("La tipologia di esigibilità IVA non deve essere di tipo 'Differita' "
			    	  		+ "o 'Split Payment'"
			    	  		+ (fatturaPassivaElettronicaBP.getDataAttivazioneSplit()!=null?
			    	  			" per documenti con data emissione antecedente al "+sdf.format(fatturaPassivaElettronicaBP.getDataAttivazioneSplit()):"")
			    	  		+(fatturaPassivaElettronicaBP.getDataDisattivazioneSplit()!=null? 
			    	  			" o successiva al "+sdf.format(fatturaPassivaElettronicaBP.getDataDisattivazioneSplit()):"")
			    	  		+ ". Il documento deve essere rifiutato!");	
			    	  return context.findDefaultForward();			    	  
					  }
				  }
			      if (rigaEle.getImponibileImporto()!=null) 
			    	  tot_riepilogo=tot_riepilogo.add(rigaEle.getImponibileImporto());
			      if (rigaEle.getImposta()!=null)
			    	  tot_riepilogo=tot_riepilogo.add(rigaEle.getImposta());
			      
				}
				if(bulk.getImportoDocumento().compareTo(BigDecimal.ZERO)==0 && bulk.getImportoDocumento() .compareTo(tot_riepilogo)!=0) {
					fatturaPassivaElettronicaBP.setMessage("Prima di procedere verificare il totale del documento!");
					return context.findDefaultForward();
				}
			}
			
	      
			if ((bulk.getDocEleTributiColl()!=null && !bulk.getDocEleTributiColl().isEmpty()) 
					||(bulk.getDocumentoEleTrasmissione().getRegimefiscale()!= null && 
					(bulk.getDocumentoEleTrasmissione().getRegimefiscale().equals(RegimeFiscaleType.RF_02.name()) ||
							bulk.getDocumentoEleTrasmissione().getRegimefiscale().equals(RegimeFiscaleType.RF_19.name()))))
					{
					if (bulk.isAttivoSplitPaymentProf()) {
						if (!bulk.isDocumentoSplitPaymentProf() && !Fattura_passivaBulk.TIPO_NOTA_DI_CREDITO.equals(bulk.getTipoDocumentoSIGLA()) &&
								!bulk.getDocumentoEleTrasmissione().getRegimefiscale().equals(RegimeFiscaleType.RF_12.name()) &&	
								!bulk.getDocumentoEleTrasmissione().getRegimefiscale().equals(RegimeFiscaleType.RF_04.name())
								&&   !hasAccesso 
								) {
							java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
							fatturaPassivaElettronicaBP.setMessage("La tipologia di esigibilità IVA deve essere di tipo 'Split Payment'"
					    	  		+ (fatturaPassivaElettronicaBP.getDataAttivazioneSplitProf()!=null?
					    	  			" per documenti con data emissione dal "+sdf.format(fatturaPassivaElettronicaBP.getDataAttivazioneSplitProf()):"") 
					    	  		+ (fatturaPassivaElettronicaBP.getDataDisattivazioneSplitProf()!=null?
					    	  			" per documenti con data emissione al "+sdf.format(fatturaPassivaElettronicaBP.getDataDisattivazioneSplitProf()):"") 
					    	  		+ ". Il documento deve essere rifiutato!");	
							return context.findDefaultForward();
						}
					}
				}
			else{		
					if (bulk.isAttivoSplitPayment()) {
							if (!bulk.isDocumentoSplitPayment() && !Fattura_passivaBulk.TIPO_NOTA_DI_CREDITO.equals(bulk.getTipoDocumentoSIGLA()) &&
								!bulk.getDocumentoEleTrasmissione().getRegimefiscale().equals(RegimeFiscaleType.RF_12.name()) &&	
								!bulk.getDocumentoEleTrasmissione().getRegimefiscale().equals(RegimeFiscaleType.RF_04.name())
								&&   !hasAccesso 
								) {
								java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
								fatturaPassivaElettronicaBP.setMessage("La tipologia di esigibilità IVA deve essere di tipo 'Split Payment'"
										+ (fatturaPassivaElettronicaBP.getDataAttivazioneSplit()!=null?
							    	  			" per documenti con data emissione dal "+sdf.format(fatturaPassivaElettronicaBP.getDataAttivazioneSplit()):"") 
							    	  		+ (fatturaPassivaElettronicaBP.getDataDisattivazioneSplit()!=null?
							    	  			" per documenti con data emissione al "+sdf.format(fatturaPassivaElettronicaBP.getDataDisattivazioneSplit()):"") 
							    	  		+ ". Il documento deve essere rifiutato!");	
								return context.findDefaultForward();
							}
					}				
			}
			String message = "La compilazione della Fattura e il suo successivo salvataggio, ";
			message += "comporta l'accettazione del documento elettronico.<br>Si desidera procedere?";
			return openConfirm( context, message, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doConfirmCompilaFattura");				
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
			CRUDFatturaPassivaBP nbp = null;
			try {
				nbp = (CRUDFatturaPassivaBP)context.createBusinessProcess("CRUDFatturaPassivaBP",
								new Object[] {"M"}
							);
				String mode = it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().
						validaBPPerUtente(context.getUserContext(),((CNRUserInfo)context.getUserInfo()).getUtente(),
								((CNRUserInfo)context.getUserInfo()).getUtente().isUtenteComune() ? 
										((CNRUserInfo)context.getUserInfo()).getUnita_organizzativa().getCd_unita_organizzativa() : 
											"*","CRUDFatturaPassivaBP");
				if (mode == null || mode.equals("V")) 
					throw new it.cnr.jada.action.MessageToUser("Accesso non consentito alla mappa di creazione delle fatture. Impossibile continuare.");
				if (bulk.getTipoDocumentoSIGLA().equalsIgnoreCase(Fattura_passivaBulk.TIPO_NOTA_DI_CREDITO)) {
					if(bulk.getModalitaPagamento()==null)
						throw new it.cnr.jada.action.MessageToUser("Prima di procedere indicare la modalità di pagamento!");
				}
				context.addHookForward("default",this,"doBringBackCompilaFattura");
				nbp = (CRUDFatturaPassivaBP) context.addBusinessProcess(nbp);
				if (bulk.getTipoDocumentoSIGLA().equalsIgnoreCase(Fattura_passivaBulk.TIPO_NOTA_DI_CREDITO) ||
						bulk.getTipoDocumentoSIGLA().equalsIgnoreCase(Fattura_passivaBulk.TIPO_NOTA_DI_DEBITO)) {
					Fattura_passivaBulk fatturaPassivaBulk = ((FatturaElettronicaPassivaComponentSession)fatturaPassivaElettronicaBP.createComponentSession()).
						cercaFatturaPassivaForNota(context.getUserContext(), bulk);
					nbp.edit(context, fatturaPassivaBulk); 
					nbp.setFromFatturaElettronica(Boolean.TRUE);
					CRUDFatturaPassivaBP notaBp = null;
					if (bulk.getTipoDocumentoSIGLA().equalsIgnoreCase(Fattura_passivaBulk.TIPO_NOTA_DI_CREDITO))
						notaBp = (CRUDFatturaPassivaBP)action.doGeneraNotaDiCredito(context);
					else if (bulk.getTipoDocumentoSIGLA().equalsIgnoreCase(Fattura_passivaBulk.TIPO_NOTA_DI_DEBITO))
						notaBp = (CRUDFatturaPassivaBP)action.doGeneraNotaDiDebito(context);
					notaBp.setModel(context, fatturaPassivaElettronicaBP.completaFatturaPassiva(context, (Fattura_passivaBulk) notaBp.getModel(), notaBp, fatturaPassivaBulk));
				} else {
					Fattura_passivaBulk fatturaPassivaBulk = (Fattura_passivaBulk) nbp.getModel();
					nbp.setModel(context, fatturaPassivaElettronicaBP.completaFatturaPassiva(context, fatturaPassivaBulk, nbp, null));					
				}
				return nbp;
			} catch(Throwable e) {
				if (nbp != null){
					context.closeBusinessProcess(nbp);
					if(nbp.getMessage()!=null)
							throw new it.cnr.jada.action.MessageToUser(nbp.getMessage());
				}
				return handleException(context,e);
			}		
		}
		return context.findDefaultForward();
	}	
public Forward doOnCambiaImportoDocumento(ActionContext context) throws ComponentException, RemoteException, BusinessProcessException{
		CRUDFatturaPassivaElettronicaBP bp = (CRUDFatturaPassivaElettronicaBP)getBusinessProcess(context);
		DocumentoEleTestataBulk bulk = (DocumentoEleTestataBulk) bp.getModel();
		DocumentoEleTestataBulk DocumentoEleTestataBulkDB =(DocumentoEleTestataBulk) bp.createComponentSession().findByPrimaryKey(context.getUserContext(),bp.getModel());
		try{
			fillModel(context);	
			if (!bulk.isAbilitato())
				if(DocumentoEleTestataBulkDB.getImportoDocumento()!=null && DocumentoEleTestataBulkDB.getImportoDocumento().compareTo(BigDecimal.ZERO)!=0 && bulk.getImportoDocumento()!=null &&
						DocumentoEleTestataBulkDB.getImportoDocumento().compareTo(bulk.getImportoDocumento())!=0){
						throw new it.cnr.jada.comp.ApplicationException("Importo documento non modificabile se già indicato!");
				}
		}catch (Throwable e){
				bulk.setImportoDocumento(DocumentoEleTestataBulkDB.getImportoDocumento());
				return handleException(context,e);
		}
	return context.findDefaultForward();		
	}
}