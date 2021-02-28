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

package it.cnr.contab.anagraf00.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import it.cnr.contab.anagraf00.ejb.*;
import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.bp.*;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.compensi00.bp.CRUDCompensoBP;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.docamm00.bp.CRUDFatturaAttivaBP;
import it.cnr.contab.doccont00.comp.DateServices;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.ejb.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.*;

/**
 * Adatta e implementa la {@link it.cnr.jada.util.action.CRUDAction } per le funzionalità supplementari
 * necessarie al crud dell'anagrafina.
 */
public class CRUDAnagraficaAction extends it.cnr.jada.util.action.CRUDAction {

	
	/**
	 * Costruttore standard di CRUDAnagraficaAction.
	 *
	 */

	public CRUDAnagraficaAction() {
		super();
	}
	/**
	 * Assegnamento di un comune fiscale ad un anagrafico.
	 *
	 * @param context {@link ActionContext } in uso.
	 * @param anag {@link AnagraficoBulk }
	 * @param comune {@link ComuneBulk } da assegnare.
	 *
	 * @throws RemoteException
	 *
	 * @return Forward
	 */

	public Forward doBringBackSearchFind_comune_fiscale(ActionContext context,
														it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk anag,
														it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune)
															throws java.rmi.RemoteException {

		it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP anagBP = (it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP)getBusinessProcess(context);

		if (comune != null) {
			if( comune.getPg_comune() != null && !("".equals(comune.getPg_comune())) ) {
				try {
					anagBP.setModel(context,
						((it.cnr.contab.anagraf00.ejb.AnagraficoComponentSession)
							anagBP.createComponentSession()
						).setComune_fiscale(context.getUserContext(),anag, comune)
					);
				} catch(Throwable e) {
					return handleException(context,e);
				}
			}
		}

		return context.findDefaultForward();
	}
	public Forward doBringBackSearchFind_unita_organizzativa(ActionContext context,
														it.cnr.contab.anagraf00.core.bulk.TerzoBulk trz,
														it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo)
															throws java.rmi.RemoteException
	{
		if(trz.getDenominazione_sede() == null && uo != null)
			trz.setDenominazione_sede( uo.getDs_unita_organizzativa() );
		trz.setUnita_organizzativa( uo );

		return context.findDefaultForward();
	}
public Forward doBringBackTerzo(ActionContext context) {
	try {
		HookForward hook = (HookForward)context.getCaller();
		return riporta(context,(OggettoBulk)hook.getParameter("bringback"));
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
	/**
	 * Presiede al cambiamento dell'istanza Ti_entita e gestisce le valorizzazioni automatiche
	 * di Ti_entita_fisica, Ti_entita_giuridica e Ti_italiano_estero nel caso siano null.
	 *
	 * @param context {@link ActionContext } in uso.
	 *
	 * @return Forward
	 */

	public Forward doCambiaTi_entita(ActionContext context) {
		try {
			String oldTi_entita = ((it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP)context.getBusinessProcess()).getAnagrafico().getTi_entita(); 
			super.fillModel(context);
			it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk anagraficoBulk =
				((it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP)context.getBusinessProcess()).getAnagrafico();

			if (!anagraficoBulk.getAssociatiStudio().isEmpty() && !oldTi_entita.equals(anagraficoBulk.getTi_entita())){
				((it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP)context.getBusinessProcess()).setMessage("Attenzione, risultano associati terzi allo \"Studio associato\". Modifica non consentita!");
				anagraficoBulk.setTi_entita(oldTi_entita);
				return context.findDefaultForward();
			}

			if(anagraficoBulk.isPersonaFisica()) {
				if(anagraficoBulk.getTi_entita_fisica() == null) {
					anagraficoBulk.setTi_entita_fisica(anagraficoBulk.ALTRO);
				}
				anagraficoBulk.setFl_fatturazione_differita(Boolean.FALSE);
				anagraficoBulk.setFl_studio_associato(Boolean.FALSE);
				if(anagraficoBulk.DITTA_INDIVID.equals( anagraficoBulk.getTi_entita_fisica() )) {
					anagraficoBulk.setFl_soggetto_iva(Boolean.TRUE);
				}
			}
			else if(anagraficoBulk.isPersonaGiuridica()) {
				if(anagraficoBulk.getTi_entita_giuridica() == null)
					anagraficoBulk.setTi_entita_giuridica(anagraficoBulk.ALTRO);
				if(anagraficoBulk.ENTE_PUBBLICO.equals( anagraficoBulk.getTi_entita_giuridica() )) {
					/* <B>Rich. 661</B>
					 *	Il fl_esigibilità_differita, per gli enti pubblici DEVE essere di default == FALSE.
					 *
					 * Creation date: (29/01/2004)
				     * Author: Borriello Gennaro
					*/
					//anagraficoBulk.setFl_fatturazione_differita(Boolean.TRUE);
					anagraficoBulk.setFl_fatturazione_differita(Boolean.FALSE);
				} else {
					anagraficoBulk.setFl_fatturazione_differita(Boolean.FALSE);
				}
				if(it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk.ITALIA.equals( anagraficoBulk.getTi_italiano_estero() )) {
					anagraficoBulk.setFl_soggetto_iva(Boolean.TRUE);
				}
			}
			if(anagraficoBulk.getTi_italiano_estero() == null) {
				anagraficoBulk.setTi_italiano_estero(it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk.ITALIA);
			}

			return context.findDefaultForward();
		} catch(FillException e) {
			return handleException(context,e);
		}
	}
	/**
	 * Presiede al cambiamento dell'istanza Ti_italiano_estero e gestisce la valorizzazione automatica
	 * Ti_italiano_estero = italiana e setta Codice_fiscale, Cap_comune_fiscale e Caps_comune a null.
	 *
	 * @param context {@link ActionContext } in uso.
	 *
	 * @return Forward
	 */

	public Forward doCambiaTi_italiano_estero(ActionContext context) {
		try {
			super.fillModel(context);
			it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk anagraficoBulk =
				((it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP)context.getBusinessProcess()).getAnagrafico();

			if(anagraficoBulk.getTi_italiano_estero() == null) {
				anagraficoBulk.setTi_italiano_estero(it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk.ITALIA);
			}

			anagraficoBulk.setComune_fiscale(null);
			anagraficoBulk.setCap_comune_fiscale(null);
			anagraficoBulk.setCaps_comune(null);

			return context.findDefaultForward();
		} catch(FillException e) {
			return handleException(context,e);
		}
	}
	/**
	 * Attiva la ricerca per il comune fiscale.
	 *
	 * @param context {@link ActionContext } in uso.
	 *
	 * @return Forward
	 */

	public Forward doCercaComuneFiscale(ActionContext context) {
		try {
			fillModel(context);
			it.cnr.jada.util.action.RicercaLiberaBP bp = (it.cnr.jada.util.action.RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
			bp.setPrototype(new it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk());
			bp.setPrototype(getBusinessProcess(context).getModel());
			context.addHookForward("seleziona",this,"doRiportaSelezioneComuneFiscale");
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Attiva la ricerca per il comune di nascita.
	 *
	 * @param context {@link ActionContext } in uso.
	 *
	 * @return Forward
	 */

	public Forward doCercaComuneNascita(ActionContext context) {
		try {
			fillModel(context);
			it.cnr.jada.util.action.RicercaLiberaBP bp = (it.cnr.jada.util.action.RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
			bp.setPrototype(new it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk());
			bp.setPrototype(getBusinessProcess(context).getModel());
			context.addHookForward("seleziona",this,"doRiportaSelezioneComuneNascita");
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Attiva la ricerca per un ante correalto.
	 *
	 * @param context {@link ActionContext } in uso.
	 *
	 * @return Forward
	 */

	public Forward doCercaEnte(ActionContext context) {
		try {
			fillModel(context);
			it.cnr.jada.util.action.RicercaLiberaBP bp = (it.cnr.jada.util.action.RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
			bp.setPrototype(new it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk());
			bp.setPrototype(getBusinessProcess(context).getModel());
			context.addHookForward("seleziona",this,"doRiportaSelezioneEnte");
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Attiva la ricerca per la nazione di una nazionalità.
	 *
	 * @param context {@link ActionContext } in uso.
	 *
	 * @return Forward
	 */

	public Forward doCercaNazioneNazionalita(ActionContext context) {
		try {
			fillModel(context);
			it.cnr.jada.util.action.RicercaLiberaBP bp = (it.cnr.jada.util.action.RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
			bp.setPrototype(new it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk());
			bp.setPrototype(getBusinessProcess(context).getModel());
			context.addHookForward("seleziona",this,"doRiportaSelezioneNazioneNazionalita");
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Attiva la ricerca per un tipo di rapporto.
	 *
	 * @param context {@link ActionContext } in uso.
	 *
	 * @return Forward
	 */

	public Forward doCercaTipoRapporto(ActionContext context) {
		try {
			fillModel(context);
			it.cnr.jada.util.action.RicercaLiberaBP bp = (it.cnr.jada.util.action.RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
			bp.setPrototype(new it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk());
			bp.setPrototype(getBusinessProcess(context).getModel());
			context.addHookForward("seleziona",this,"doRiportaSelezioneTipoRapporto");
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
public Forward doConfermaTerzi(ActionContext context,int option) {
	try {
		if (option == OptionBP.YES_BUTTON) {
			CRUDAnagraficaBP bp = (CRUDAnagraficaBP)getBusinessProcess(context);
			String function = bp.isEditable() ? "M" : "V";
			if (bp.isBringBack())
				function += "R";
			CRUDTerzoBP terzobp = (CRUDTerzoBP)context.createBusinessProcess("CRUDTerzoBP",new Object[] { function, bp.getModel() });
			//TerzoBulk terzo = ((AnagraficoComponentSession)bp.createComponentSession()).getDefaultTerzo(context.getUserContext(),bp.getAnagrafico());
			//if (terzo != null && !terzo.isTerzo_speciale())
				//terzobp.edit(context,terzo);
			if (bp.isBringBack())
				context.addHookForward("bringback",this,"doBringBackTerzo");
			Forward forward = context.addBusinessProcess(terzobp);
			terzobp.resetForSearch(context);
			return doCerca(context);
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
	/**
	 * Gestisce una eccezione di chiave duplicata
	 */
	public Forward doConfirmHandleExCodiceFiscale(ActionContext context,it.cnr.jada.util.action.OptionBP option) {
		try {
			it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP bp = (it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP)getBusinessProcess(context);
			if (option.getOption() == it.cnr.jada.util.action.OptionBP.NO_BUTTON) {
				((AnagraficoBulk)bp.getModel()).setFl_codice_fiscale_forzato(Boolean.TRUE);
				return context.findDefaultForward();
			}	
			AnagraficoBulk ana = (AnagraficoBulk)option.getAttribute("anagrafica");
			ana.setCodice_fiscale((String)option.getAttribute("nuovoCodice"));
			bp.setModel(context,ana);
			return context.findDefaultForward();
		} catch(BusinessProcessException e) {
			return handleException(context,e);
		}
	}

	/**
	 * Gestisce un comando di cancellazione modificato rispetto al default.
	 *
	 * @see it.cnr.jada.util.action.CRUDAction#doElimina
	 */
	public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {

		try {
			fillModel(context);

			it.cnr.jada.util.action.CRUDBP bp = getBusinessProcess(context);
			if (!bp.isEditing()) {
				bp.setMessage("Non è possibile cancellare in questo momento");
			} else {
				bp.delete(context);
				try {
					bp.edit(context,bp.getModel());
				} catch(Throwable e) {}
				if(((AnagraficoBulk)bp.getModel()).getDt_fine_rapporto() != null) {
					bp.setMessage("Data di fine rapporto impostata");
				} else {
					bp.reset(context);
					bp.setMessage("Cancellazione effettuata");
				}
			}
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	/**
	 * Effettua l'assegnamento del campo selezionato nella ricerca.
	 *
	 * @param context {@link ActionContext } in uso.
	 *
	 * @return {@link Forward }
	 *
	 * @throws RemoteException
	 *
	 * @see #doCercaComuneFiscale
	 */

	public Forward doRiportaSelezioneComuneFiscale(ActionContext context)  throws java.rmi.RemoteException {
		HookForward caller = (HookForward)context.getCaller();
		it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune = (it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk)caller.getParameter("selezione");
		if (comune != null) {
			((it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk)getBusinessProcess(context).getModel()
			).setComune_fiscale(comune);

			if( comune.getPg_comune() != null && !(comune.getPg_comune()).equals("") ) {
				try {
					getBusinessProcess(context).setModel(context,
					((it.cnr.contab.anagraf00.ejb.AnagraficoComponentSession)
						((it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP)getBusinessProcess(context)
						).createComponentSession()
					).setComune_fiscale(context.getUserContext(),(it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk)getBusinessProcess(context).getModel(), comune));
				} catch(BusinessProcessException bpe) {
					return handleException(context, bpe);
				} catch(it.cnr.jada.comp.ComponentException ce) {
					return handleException(context,ce);
				}
			}
			
		}

		return context.findDefaultForward();
	}
	/**
	 * Effettua l'assegnamento del campo selezionato nella ricerca.
	 *
	 * @param context {@link ActionContext } in uso.
	 *
	 * @return Forward
	 *
	 * @exception RemoteException
	 *
	 * @see #doCercaComuneNascita
	 */

	public Forward doRiportaSelezioneComuneNascita(ActionContext context)  throws java.rmi.RemoteException {
		HookForward caller = (HookForward)context.getCaller();
		it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune = (it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk)caller.getParameter("selezione");
		if (comune != null)
			((it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk)getBusinessProcess(context).getModel()).setComune_nascita(comune);
		return context.findDefaultForward();
	}
	/**
	 * Effettua l'assegnamento del campo selezionato nella ricerca.
	 *
	 * @param context {@link ActionContext } in uso.
	 *
	 * @return Forward
	 *
	 * @throws RemoteException
	 *
	 * @see #doCercaEnte
	 */

	public Forward doRiportaSelezioneEnte(ActionContext context)  throws java.rmi.RemoteException {
		HookForward caller = (HookForward)context.getCaller();
		it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk anag = (it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk)caller.getParameter("selezione");
		if (anag != null)
			((it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk)getBusinessProcess(context).getModel()).setCd_ente_appartenenza(anag.getCd_ente_appartenenza());
		return context.findDefaultForward();
	}
	/**
	 * Effettua l'assegnamento del campo selezionato nella ricerca.
	 *
	 * @param context {@link ActionContext } in uso.
	 *
	 * @return Forward
	 *
	 * @throws RemoteException
	 *
	 * @see #doCercaNazioneNazionalita
	 */

	public Forward doRiportaSelezioneNazioneNazionalita(ActionContext context)  throws java.rmi.RemoteException {
		HookForward caller = (HookForward)context.getCaller();
		it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk nazione = (it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk)caller.getParameter("selezione");
		if (nazione != null)
			((it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk)getBusinessProcess(context).getModel()).setNazionalita(nazione);
		return context.findDefaultForward();
	}
	/**
	 * Effettua l'assegnamento del campo selezionato nella ricerca.
	 *
	 * @param context {@link ActionContext } in uso.
	 *
	 * @return Forward
	 *
	 * @throws RemoteException
	 *
	 * @see #doCercaTipoRapporto
	 */

	public Forward doRiportaSelezioneTipoRapporto(ActionContext context)  throws java.rmi.RemoteException {
		HookForward caller = (HookForward)context.getCaller();
		it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipoRapporto = (it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk)caller.getParameter("selezione");
		it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP anagBP = (it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP)getBusinessProcess(context);

		if (tipoRapporto != null) {
			((it.cnr.contab.anagraf00.core.bulk.RapportoBulk)anagBP.getCrudRapporti().getModel()
			).setCd_tipo_rapporto(tipoRapporto.getCd_tipo_rapporto());
		}

		return context.findDefaultForward();
	}
public Forward doTerzi(ActionContext context) {
	try {
		CRUDBP bp = getBusinessProcess(context);
		fillModel(context);
		if (bp.isDirty())
			return openContinuePrompt(context,"doConfermaTerzi");
		return doConfermaTerzi(context,OptionBP.YES_BUTTON);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
	/**
	 * Metodo utilizzato per gestire dell'eccezione generata dall'inserimento di un
	 * anagrafica già esistente.
	 *
	 * @param context {@link ActionContext } in uso.
	 * @param ex Eccezione da gestire.
	 *
	 * @return Forward
	 *
	 * @throws RemoteException
	 *
	 * @see #
	 */

	public Forward handleException(ActionContext context, Throwable ex) {
		try {
			throw ex;
		} catch(it.cnr.contab.anagraf00.util.ExCodiceFiscale e) {

			CRUDAnagraficaBP bp   = (CRUDAnagraficaBP)getBusinessProcess(context);
			try {
				String newCF = ((AnagraficoComponentSession)bp.createComponentSession()).calcolaCodiceFiscale(context.getUserContext(), (AnagraficoBulk)bp.getModel());
				if(newCF!=null && bp.getModel()!= null && ((AnagraficoBulk)bp.getModel()).getCodice_fiscale()!=null){
					String msg = e.getMessage();
					msg += "\nIl codice fiscale esatto potrebbe essere \""
							+ newCF + "\".\nUtilizzare il Codice Fiscale calcolato?";
	
					it.cnr.jada.util.action.OptionBP option = openConfirm( context, msg, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doConfirmHandleExCodiceFiscale");
					option.addAttribute("anagrafica", (AnagraficoBulk)bp.getModel());
					option.addAttribute("nuovoCodice", newCF);
					return option;
				} else{
					bp.setErrorMessage(e.getMessage());
					return context.findDefaultForward();
				}
			} catch(Throwable twb) {
				bp.setErrorMessage(e.getMessage());
				return context.findDefaultForward();
			}

		} catch(Throwable e) {
			return super.handleException(context,e);
		}
	}
public Forward doOnIm_pagamentoChange(ActionContext context) {

	try{	
		it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP bp = (it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP)getBusinessProcess(context);
        AnagraficoBulk anagrafico = bp.getAnagrafico();		
		Pagamento_esternoBulk pagamento_esterno = (Pagamento_esternoBulk)bp.getCrudPagamenti_esterni().getModel();
		java.math.BigDecimal oldImp = pagamento_esterno.getIm_pagamento();

		try {
			fillModel(context);
			return context.findDefaultForward();
		} catch(it.cnr.jada.bulk.FillException e) {
			pagamento_esterno.setIm_pagamento(oldImp);
			bp.setModel(context,anagrafico);
			throw e;
		}
	} catch(Throwable e) {
		return handleException(context, e);
	}
}
public Forward doOnDt_pagamentoChange(ActionContext context) {

	try{	
		it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP bp = (it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP)getBusinessProcess(context);
		AnagraficoBulk anagrafico = bp.getAnagrafico();		
		Pagamento_esternoBulk pagamento_esterno = (Pagamento_esternoBulk)bp.getCrudPagamenti_esterni().getModel();
		java.sql.Timestamp oldData = pagamento_esterno.getDt_pagamento();
		try {
			fillModel(context);
			if (!bp.isSearching())
			  try{
				pagamento_esterno.validateDate();
			  }catch(ValidationException ex){
				pagamento_esterno.setDt_pagamento(oldData);
				bp.setModel(context,anagrafico);
				throw ex;			  	
			  }			
			return context.findDefaultForward();
		} catch(it.cnr.jada.bulk.FillException e) {
			pagamento_esterno.setDt_pagamento(oldData);
			bp.setModel(context,anagrafico);
			throw e;
		}
	} catch(Throwable e) {
		return handleException(context, e);
	}
}
public Forward doOnIm_speseChange(ActionContext context) {

	try{	
		it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP bp = (it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP)getBusinessProcess(context);
		AnagraficoBulk anagrafico = bp.getAnagrafico();		
		Pagamento_esternoBulk pagamento_esterno = (Pagamento_esternoBulk)bp.getCrudPagamenti_esterni().getModel();
		java.math.BigDecimal oldImp = pagamento_esterno.getIm_spese();

		try {
			fillModel(context);
			return context.findDefaultForward();
		} catch(it.cnr.jada.bulk.FillException e) {
			pagamento_esterno.setIm_spese(oldImp);
			bp.setModel(context,anagrafico);
			throw e;
		}
	} catch(Throwable e) {
		return handleException(context, e);
	}
}	
/**
 * Gestisce un comando "riporta".
 */
protected Forward riporta(ActionContext context,OggettoBulk model) {

	if (model != null && model instanceof TerzoBulk) {
		CRUDBP bp = (CRUDBP)context.getBusinessProcess();
		TerzoBulk tb = (TerzoBulk)model;
		
		if(bp.getParent() != null && bp.getParent() instanceof it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP) {
			it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk docAmm = ((it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP)bp.getParent()).getDocumentoAmministrativoCorrente();
			if (docAmm instanceof it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk) {
				it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk fp = (it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk)docAmm;
				if (tb.getAnagrafico() == null)
					throw new MessageToUser("Il terzo selezionato non è valido!",bp.ERROR_MESSAGE);
				if (tb.getAnagrafico().getTi_italiano_estero()!=null && tb.getAnagrafico().getTi_italiano_estero().equals(NazioneBulk.ITALIA) && ((tb.getAnagrafico().getPartita_iva()==null  && !tb.getAnagrafico().getFl_non_obblig_p_iva()) ||tb.getAnagrafico().getCodice_fiscale()==null))
					throw new MessageToUser("Il terzo selezionato non è valido!",bp.ERROR_MESSAGE);
			
				if (tb.getAnagrafico().DIVERSI.equalsIgnoreCase(tb.getAnagrafico().getTi_entita()))
					throw new MessageToUser("Il terzo selezionato non è valido per la " + fp.getDescrizioneEntita() + " perché è di tipo \"Diversi\"!", bp.ERROR_MESSAGE);
				//NB: per le fatture passive il debitore è un creditore
				if (tb.DEBITORE.equalsIgnoreCase(tb.getTi_terzo()))
					throw new MessageToUser("Il terzo selezionato non è valido per la " + fp.getDescrizioneEntita() + " perché è un debitore!", bp.ERROR_MESSAGE);
				if (tb.getDt_fine_rapporto() != null && fp.getDt_fattura_fornitore() != null && 
					fp.getDt_fattura_fornitore().after(tb.getDt_fine_rapporto()) && 
					!tb.getDt_fine_rapporto().equals(fp.getDt_fattura_fornitore()))
					throw new MessageToUser("Il rapporto con il terzo selezionato è terminato precedentemente alla data della " + fp.getDescrizioneEntita() + "! Selezione non valida.", bp.ERROR_MESSAGE);
				
				String cond = fp.getSupplierNationType();
				if (!cond.equalsIgnoreCase(tb.getAnagrafico().getTi_italiano_estero()))
					throw new MessageToUser("E' necessario selezionare un terzo con nazionalità compatibile con quella del documento amministrativo.", bp.ERROR_MESSAGE);
			}
			if (docAmm instanceof it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk) {
				//NB: per le fatture att il creditore è un debitore
				if (tb.getAnagrafico().getTi_italiano_estero()!=null && tb.getAnagrafico().getTi_italiano_estero().equals(NazioneBulk.ITALIA) && ((tb.getAnagrafico().getPartita_iva()==null  && !tb.getAnagrafico().getFl_non_obblig_p_iva()) ||tb.getAnagrafico().getCodice_fiscale()==null))
					throw new MessageToUser("Il terzo selezionato non è valido!",bp.ERROR_MESSAGE);
				
				it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk fa = (it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk)docAmm;
				String cond = fa.getSupplierNationType();
				if (!cond.equalsIgnoreCase(tb.getAnagrafico().getTi_italiano_estero()))
					throw new MessageToUser("E' necessario selezionare un terzo con nazionalità compatibile con quella del documento amministrativo.", bp.ERROR_MESSAGE);
				
				if (tb.CREDITORE.equalsIgnoreCase(tb.getTi_terzo()))
					throw new MessageToUser("Il terzo selezionato non è valido per il documento attivo perché è un creditore!", bp.ERROR_MESSAGE);
			}
		}
		
		if (tb.getDt_fine_rapporto() != null) {
			java.util.Calendar dataFineRapporto = it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk.getDateCalendar(tb.getDt_fine_rapporto());
			if (bp.getParent() != null && bp.getParent() instanceof it.cnr.contab.fondecon00.bp.FondoEconomaleBP) {
				java.util.Calendar today = it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk.getDateCalendar(null);
				if (dataFineRapporto.before(today) && 
					!dataFineRapporto.equals(today))
					throw new MessageToUser("Il rapporto con il terzo selezionato è terminato precedentemente alla data odierna! Selezione non valida.", bp.ERROR_MESSAGE);
			}
			if (bp.getParent() != null && bp.getParent() instanceof it.cnr.contab.fondecon00.bp.FondoSpesaBP) {
				it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk spesa = (it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk)((it.cnr.contab.fondecon00.bp.FondoSpesaBP)bp.getParent()).getModel();
				java.util.Calendar dataSpesa = spesa.getDateCalendar(spesa.getDt_spesa());
				if (dataSpesa.after(dataFineRapporto) && 
					!dataSpesa.equals(dataFineRapporto))
					throw new MessageToUser("Il rapporto con il terzo selezionato è terminato precedentemente alla data della spesa! Selezione non valida.", bp.ERROR_MESSAGE);
			}
		}
	}	
	return super.riporta(context,model);
}
public Forward doCambiaFl_cervellone(ActionContext context) {
	try {
		super.fillModel(context);
		it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk anagraficoBulk =
			((it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP)context.getBusinessProcess()).getAnagrafico();
		if (anagraficoBulk.isFl_cervellone())
		{
			anagraficoBulk.setFl_cervellone(Boolean.TRUE);
		}    
		else
		{	
			anagraficoBulk.setFl_cervellone(Boolean.FALSE);
			anagraficoBulk.setDt_inizio_res_italia(null);
			anagraficoBulk.setDt_fine_res_italia(null);
			anagraficoBulk.setAnno_inizio_res_fis(null);
			anagraficoBulk.setAnno_fine_agevolazioni(null);
		}
		return context.findDefaultForward();
	} catch(FillException e) {
		return handleException(context,e);
	}
}

public Forward doCambiaDateRes(ActionContext context) {
	Integer numMinGiorni=null;
	Integer numMaxAnni=null;
	Long numGiorniRes;
	Long numGiorniResAnnoSuc;
	Long numGiorniResUltimoAnno;
	try{	
		CRUDAnagraficaBP bp = (CRUDAnagraficaBP)getBusinessProcess(context);
		AnagraficoBulk anagrafico = (AnagraficoBulk)bp.getModel();
		java.sql.Timestamp oldDtIniResIta = anagrafico.getDt_inizio_res_italia();
		java.sql.Timestamp oldDtFinResIta = anagrafico.getDt_fine_res_italia();
		
		try {
			fillModel(context);
			if (anagrafico.getDt_inizio_res_italia() != null && 
					anagrafico.getDt_fine_res_italia() != null	&&
					anagrafico.getDt_inizio_res_italia().after(anagrafico.getDt_fine_res_italia()))
			{
				anagrafico.setDt_inizio_res_italia(oldDtIniResIta);
				anagrafico.setDt_fine_res_italia(oldDtFinResIta);
				throw new MessageToUser("La Data di Inizio residenza in Italia non può essere successiva alla data di Fine residenza in Italia.", bp.ERROR_MESSAGE);
			}			
			if (bp.isSearching())
				return context.findDefaultForward();

			GregorianCalendar data_inizio_agevolazioni = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar data_fine_agevolazioni = (GregorianCalendar) GregorianCalendar.getInstance();
			
			GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
			GregorianCalendar data_a = (GregorianCalendar) GregorianCalendar.getInstance();
			
 			if( anagrafico.getDt_inizio_res_italia() != null) {
 				Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
 				if ( sess.getIm01(context.getUserContext(), new Integer(0), null, "COSTANTI", "NUMERO_LIMITE_GG_RESIDENZA_FISCALE") == null ||
 					 sess.getIm01(context.getUserContext(), new Integer(0), null, "COSTANTI", "NUM_MAX_ANNI_AGEVOLAZIONI_RIENTRO_CERVELLI") == null ||
 					 sess.getDt01(context.getUserContext(), new Integer(0), null, "COSTANTI", "PRIMO_ANNO_AGEVOLAZIONI_RIENTRO_CERVELLI") == null ||
 					 sess.getDt01(context.getUserContext(), new Integer(0), null, "COSTANTI", "ULTIMO_ANNO_AGEVOLAZIONI_RIENTRO_CERVELLI") == null)
 				{
 					anagrafico.setDt_inizio_res_italia(null);
 					anagrafico.setDt_fine_res_italia(null);
 					anagrafico.setAnno_fine_agevolazioni(null);
					anagrafico.setAnno_inizio_res_fis(null);
 					throw new ApplicationException("Configurazione CNR: non sono stati impostati i valori per la gestione dei 'Cervelli'");
 				}
 					
 				numMinGiorni = sess.getIm01(context.getUserContext(), new Integer(0), null, "COSTANTI", "NUMERO_LIMITE_GG_RESIDENZA_FISCALE").intValue();
 				numMaxAnni = sess.getIm01(context.getUserContext(), new Integer(0), null, "COSTANTI", "NUM_MAX_ANNI_AGEVOLAZIONI_RIENTRO_CERVELLI").intValue();
 				data_inizio_agevolazioni.setTime(sess.getDt01(context.getUserContext(), new Integer(0), null, "COSTANTI", "PRIMO_ANNO_AGEVOLAZIONI_RIENTRO_CERVELLI"));
 				data_fine_agevolazioni.setTime(sess.getDt01(context.getUserContext(), new Integer(0), null, "COSTANTI", "ULTIMO_ANNO_AGEVOLAZIONI_RIENTRO_CERVELLI"));
 				
				data_da.setTime(anagrafico.getDt_inizio_res_italia());
				if (anagrafico.getDt_fine_res_italia() != null)
					data_a.setTime(anagrafico.getDt_fine_res_italia());
				
				if (data_da.compareTo(data_inizio_agevolazioni) < 0 ||
					data_da.compareTo(data_fine_agevolazioni) > 0 )	
				{
					anagrafico.setAnno_inizio_res_fis(null);
				}
				else
				{
					//calcolo il numero di giorni residui nell'anno
					if( anagrafico.getDt_fine_res_italia() != null && (new Long(data_a.get(java.util.GregorianCalendar.YEAR)).equals(new Long(data_da.get(java.util.GregorianCalendar.YEAR))) ))
					{
						numGiorniRes = DateUtils.daysBetweenDates(anagrafico.getDt_inizio_res_italia(),anagrafico.getDt_fine_res_italia()) + 1;
					}
					else
					{
						numGiorniRes = DateUtils.daysBetweenDates(anagrafico.getDt_inizio_res_italia(),DateServices.getLastDayOfYear(data_da.get(java.util.GregorianCalendar.YEAR))) + 1;	
					}
				}
 			}	
			else
			{
				anagrafico.setAnno_inizio_res_fis(null);
			}
 						
 			if (anagrafico.getAnno_inizio_res_fis()!=null)
 				anagrafico.setAnno_fine_agevolazioni(null);
		} catch(it.cnr.jada.bulk.FillException e) {
			anagrafico.setDt_inizio_res_italia(oldDtIniResIta);
			anagrafico.setDt_fine_res_italia(oldDtFinResIta);
			throw e;
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context, e);
	}
	}
public Forward doOnTi_personaChange(ActionContext context) {
	try{
		it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP bp = (it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP)getBusinessProcess(context);
		Carico_familiare_anagBulk carichi_fam = (Carico_familiare_anagBulk)bp.getCrudCarichi_familiari_anag().getModel();
		fillModel(context);
		if(carichi_fam.isConiuge())
			carichi_fam.setPrc_carico(new java.math.BigDecimal(100));
		else
			carichi_fam.setPrc_carico(null);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context, e);
	}
}
public Forward doClickFlagFigliosenza(ActionContext context){

	it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP bp = (it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP)getBusinessProcess(context);
	Carico_familiare_anagBulk carichi_fam = (Carico_familiare_anagBulk)bp.getCrudCarichi_familiari_anag().getModel();
try{	
	fillModel(context);
	if(carichi_fam.getDt_ini_validita()==null || carichi_fam.getDt_fin_validita()==null)
		throw new ApplicationException("Attenzione, valorizzare prima la data di inizio e fine validità");
	if (carichi_fam.getFl_primo_figlio_manca_con()!=null && carichi_fam.getFl_primo_figlio_manca_con().booleanValue())
		bp.checkConiugeAlreadyExistFor(context, (AnagraficoBulk)bp.getModel(),carichi_fam);
	return context.findDefaultForward();
} catch(Throwable e) {
	carichi_fam.setFl_primo_figlio_manca_con(Boolean.FALSE);
	return handleException(context, e);
}
}
public Forward doElenco(ActionContext context) {
	try {
		CRUDBP bp = getBusinessProcess(context);
		fillModel(context);
		if (bp.isDirty())
			return openContinuePrompt(context,"doConfermaElenco");
		return doConfermaElenco(context,OptionBP.YES_BUTTON);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doConfermaElenco(ActionContext context,int option) {
	try {
		if (option == OptionBP.YES_BUTTON) {
			it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP bp = (it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP)getBusinessProcess(context);
			bp.Estrazione(context);
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doOnDt_fin_validitaChange(ActionContext context)  {
	try{
		it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP bp = (it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP)getBusinessProcess(context);
		AnagraficoBulk anagrafico = bp.getAnagrafico();	
		java.util.GregorianCalendar data_da = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
		java.util.GregorianCalendar data_a = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
		Carico_familiare_anagBulk carico = (Carico_familiare_anagBulk)bp.getCrudCarichi_familiari_anag().getModel();
		java.sql.Timestamp oldData = carico.getDt_fin_validita();
		java.sql.Timestamp maxDataCompensi = bp.findMaxDataCompValida(context.getUserContext(), anagrafico);
		try {
			fillModel(context);
			if(carico.getDt_fin_validita()==null)
				throw new ValidationException("E' necessario inserire la data di fine validità.");
			data_da.setTime(carico.getDt_ini_validita());
			data_a.setTime(carico.getDt_fin_validita());
//			if (data_da.get(java.util.GregorianCalendar.YEAR)!=data_a.get(java.util.GregorianCalendar.YEAR)){
//				carico.setDt_fin_validita(oldData);
//				throw new ValidationException("La data di inizio e fine validità devono appartenere allo stesso esercizio.");
//			}
				
			if (!bp.isSearching())
			  try{
				  if ((oldData==null || carico.getDt_fin_validita().before(oldData))&& anagrafico.isUtilizzata_detrazioni()&&
					  carico.getDt_fin_validita().before(maxDataCompensi))
					  throw new ValidationException("Carico familiare utilizzato nel calcolo delle detrazioni. E' possibile inserire solo una data successiva al "+ new SimpleDateFormat("dd/MM/yyyy").format(maxDataCompensi));
			  }catch(ValidationException ex){
				  carico.setDt_fin_validita(oldData);
				  bp.setModel(context,anagrafico);
				  throw ex;			  	
			  }			
			return context.findDefaultForward();
		} catch(it.cnr.jada.bulk.FillException e) {
			carico.setDt_fin_validita(oldData);
			bp.setModel(context,anagrafico);
			throw e;
		}
	} catch(Throwable e) {
		return handleException(context, e);
	}
}
public Forward doCambiaFl_abilita_diaria_miss_est(ActionContext context) {
	try {
		super.fillModel(context);
		it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk anagraficoBulk =
			((it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP)context.getBusinessProcess()).getAnagrafico();
		if (anagraficoBulk.isFl_abilita_diaria_miss_est())
		{
			anagraficoBulk.setFl_abilita_diaria_miss_est(Boolean.TRUE);
		}    
		else
		{	
			anagraficoBulk.setFl_abilita_diaria_miss_est(Boolean.FALSE);
			anagraficoBulk.setDt_inizio_diaria_miss_est(null);
			anagraficoBulk.setDt_fine_diaria_miss_est(null);

		}
		return context.findDefaultForward();
	} catch(FillException e) {
		return handleException(context,e);
	}
}

public Forward doCambiaDateDiariaMissEst(ActionContext context) {

	try{	
		CRUDAnagraficaBP bp = (CRUDAnagraficaBP)getBusinessProcess(context);
		AnagraficoBulk anagrafico = (AnagraficoBulk)bp.getModel();
		java.sql.Timestamp oldDtIniDiaria = anagrafico.getDt_inizio_diaria_miss_est();
		java.sql.Timestamp oldDtFinDiaria = anagrafico.getDt_fine_diaria_miss_est();
		
		try {
			fillModel(context);
			if (anagrafico.getDt_inizio_diaria_miss_est() != null && 
					anagrafico.getDt_fine_diaria_miss_est() != null	&&
					anagrafico.getDt_inizio_diaria_miss_est().after(anagrafico.getDt_fine_diaria_miss_est()))
			{
				anagrafico.setDt_inizio_diaria_miss_est(oldDtIniDiaria);
				anagrafico.setDt_fine_diaria_miss_est(oldDtFinDiaria);
				throw new MessageToUser("La Data di Inizio autorizzazione non può essere successiva alla data di Fine autorizzazione.", bp.ERROR_MESSAGE);
			}			
			if (bp.isSearching())
				return context.findDefaultForward();

		} catch(it.cnr.jada.bulk.FillException e) {
			anagrafico.setDt_inizio_diaria_miss_est(oldDtIniDiaria);
			anagrafico.setDt_fine_diaria_miss_est(oldDtFinDiaria);
			throw e;
		}
	
		return context.findDefaultForward();
	
	} catch(Throwable e) {
		return handleException(context, e);
	}
	
	}

	public Forward doSalva(ActionContext actioncontext) throws java.rmi.RemoteException {
		CRUDAnagraficaBP bp = (CRUDAnagraficaBP)getBusinessProcess(actioncontext);
		try {
			fillModel(actioncontext);

			bp.save(actioncontext);
			postSalvataggio(actioncontext);
			return actioncontext.findDefaultForward();
		} catch (ValidationException validationexception) {
			getBusinessProcess(actioncontext).setErrorMessage(validationexception.getMessage());
		} catch (Throwable throwable) {
			return handleException(actioncontext, throwable);
		}
		return actioncontext.findDefaultForward();
	}

	protected void postSalvataggio(ActionContext context) throws BusinessProcessException {

		CRUDAnagraficaBP bp = (CRUDAnagraficaBP) getBusinessProcess(context);
		AnagraficoBulk anag = (AnagraficoBulk) bp.getModel();
		if (!anag.isDipendente() && !anag.getRapporti().isEmpty()){
			bp.aggiornaDatiAce(context, anag);
		}
	}

}
