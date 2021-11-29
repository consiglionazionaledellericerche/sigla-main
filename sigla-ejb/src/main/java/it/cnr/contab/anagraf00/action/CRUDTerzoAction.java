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

import it.cnr.contab.anagraf00.bp.CRUDTerzoBP;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.Termini_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.ejb.TerzoComponentSession;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.rmi.RemoteException;

/**
 * Adatta e implementa la {@link it.cnr.jada.util.action.CRUDAction } per le
 * funzionalità supplementari necessarie al crud dell'anagrafina.
 */
public class CRUDTerzoAction extends it.cnr.jada.util.action.CRUDAction {

	// public final String UTENTE_MIGRAZIONE = "$$$$$MIGRAZIONE$$$$$";

	/**
	 * Costruttore standard di CRUDAnagraficaAction.
	 * 
	 */

	public CRUDTerzoAction() {
		super();
	}

	public Forward doAddToCRUDMain_Modalita_pagamento(ActionContext context) {
		try {
			CRUDTerzoBP bp = (CRUDTerzoBP) getBusinessProcess(context);
			if (bp.getCrudBanche().getModel() != null)
				bp.validaRiga(context, bp.getCrudBanche().getModel());
			RemoteIterator ri = it.cnr.jada.util.ejb.EJBCommonServices
					.openRemoteIterator(context, ((TerzoComponentSession) bp
							.createComponentSession())
							.cercaModalita_pagamento_disponibiliByClause(
									context.getUserContext(),
									(TerzoBulk) bp.getModel()));
			int count = ri.countElements();
			if (count == 0) {
				bp.setMessage("Nessuna modalità di pagamento trovata");
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(
						context, ri);
			} else {
				SelezionatoreListaBP selezionatore = select(context, ri,
						BulkInfo.getBulkInfo(Rif_modalita_pagamentoBulk.class),
						null, "doBringBackModalita_pagamento");
				selezionatore.setMultiSelection(true);
				it.cnr.jada.bulk.BulkInfo bulkInfo = it.cnr.jada.bulk.BulkInfo.getBulkInfo(Rif_modalita_pagamentoBulk.class);
				selezionatore.setFormField(bp.getFormField("cd_terzo"));
				selezionatore.setBulkInfo(bulkInfo);
				return selezionatore;
			}
			return context.findDefaultForward();
		} catch (Throwable e) {
			return handleException(context, e);
		}
	}

	public Forward doAddToCRUDMain_Modalita_pagamento_Banche(
			ActionContext context) {
		try {
			CRUDTerzoBP bp = (CRUDTerzoBP) getBusinessProcess(context);
			TerzoBulk terzo = (TerzoBulk) bp.getModel();
			Modalita_pagamentoBulk modalita = (Modalita_pagamentoBulk) bp
					.getCrudModalita_pagamento().getModel();
			boolean modalitaPerCessione = modalita.isPerCessione();
			boolean modalitaAnnullata = modalita.isAnnullata();

			if (modalitaAnnullata)
				throw new it.cnr.jada.comp.ApplicationException(
						"Attenzione: non è possibile aggiungere nuovi dettagli poichè la modalità di pagamento prescelta è stata Annullata.");

			TerzoComponentSession terzoComponent = (TerzoComponentSession) bp
					.createComponentSession();

			if (modalitaPerCessione) {

				RemoteIterator iterator = terzoComponent
						.cercaBanchePerTerzoCessionario(
								context.getUserContext(), modalita);
				iterator = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(
						context, iterator);
				if (iterator.countElements() == 0) {
					it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(
							context, iterator);
					throw new it.cnr.jada.comp.ApplicationException(
							"Attenzione: il Terzo Delegato indicato non ha Banche utilizzabili.");
				}
				SelezionatoreListaBP banche_selezionate = select(context,
						iterator, BulkInfo.getBulkInfo(BancaBulk.class),
						modalita.getRif_modalita_pagamento().getTiPagamentoColumnSet(),
						"doBringBackBanchePerCessionario");
				banche_selezionate.setMultiSelection(true);
				return banche_selezionate;

			} else {
				getController(context, "main.Modalita_pagamento.Banche").add(
						context);
				((BancaBulk) getController(context,
						"main.Modalita_pagamento.Banche").getModel())
						.setNazioniIban(terzoComponent.findNazioniIban(
								context.getUserContext(),
								((BancaBulk) getController(context,
										"main.Modalita_pagamento.Banche")
										.getModel())));
				return context.findDefaultForward();
			}

		} catch (Throwable e) {
			return handleException(context, e);
		}
	}

	/**
	 * Assucia una termine di pagamento, tipo {@link Termini_pagamentoBulk }, a
	 * un terzo, tipo {@link TerzoBulk }.
	 * 
	 * @param context
	 *            {@link ActionContext } in uso.
	 * 
	 * @return Forward
	 */

	public Forward doAggiungiTermini_pagamento(ActionContext context) {
		try {
			fillModel(context);
			CRUDTerzoBP bp = (CRUDTerzoBP) context.getBusinessProcess();
			for (java.util.Iterator i = bp
					.getCrudTermini_pagamento_disponibili().iterator(); i
					.hasNext();)
				((OggettoBulk) i.next()).setUser(context.getUserContext()
						.getUser());
			bp.getCrudTermini_pagamento_disponibili().remove(context);
			bp.getCrudTermini_pagamento().reset(context);
			return context.findDefaultForward();
		} catch (Throwable e) {
			return super.handleException(context, e);
		}
	}

	public Forward doBlankSearchFind_terzo_delegato(ActionContext context,
			Modalita_pagamentoBulk modalita_pagamento) {
		try {
			CRUDTerzoBP bp = (CRUDTerzoBP) getBusinessProcess(context);
			// r.p. controllo disabilitato per permettere la gestione di più
			// delegati attivi contemporaneamente
			// if (bp.getCrudBanche().getElements() != null &&
			// bp.getCrudBanche().countDetails() >0){
			// throw new
			// it.cnr.jada.comp.ApplicationException("Attenzione: cancellare prima tutte le Banche.");
			// }

			modalita_pagamento.setTerzo_delegato(new TerzoBulk());

			return context.findDefaultForward();
		} catch (Throwable e) {
			return handleException(context, e);
		}
	}

	public Forward doBringBackBanchePerCessionario(ActionContext context) {
		try {
			CRUDTerzoBP bp = (CRUDTerzoBP) getBusinessProcess(context);
			TerzoComponentSession terzoComponent = (TerzoComponentSession) bp
					.createComponentSession();
			TerzoBulk terzo = (TerzoBulk) bp.getModel();
			Modalita_pagamentoBulk modalita_pagamento = (Modalita_pagamentoBulk) bp
					.getCrudModalita_pagamento().getModel();
			HookForward caller = (HookForward) context.getCaller();
			java.util.List banche = (java.util.List) caller
					.getParameter("selectedElements");
			if (banche != null) {
				for (java.util.Iterator i = banche.iterator(); i.hasNext();) {
					BancaBulk banca_selezionata = (BancaBulk) i.next();
					BancaBulk nuova_banca = (BancaBulk) banca_selezionata
							.clone();

					nuova_banca.setTerzo(terzo);
					nuova_banca.setTerzo_delegato(modalita_pagamento
							.getTerzo_delegato());
					nuova_banca.setPg_banca_delegato(nuova_banca.getPg_banca());
					nuova_banca.setOrigine(BancaBulk.ORIGINE_ON_LINE);
					nuova_banca.setFl_cc_cds(new Boolean(false));

					// nuova_banca.setPg_banca(null);
					nuova_banca.setUser(terzo.getUser());
					nuova_banca.setCrudStatus(OggettoBulk.TO_BE_CREATED);

					nuova_banca.setNazioniIban(terzoComponent.findNazioniIban(
							context.getUserContext(), nuova_banca));

					java.util.List coll = (java.util.List) terzo
							.getBanchePerTipoPagamento().get(
									nuova_banca.getChiave());
					if (coll == null)
						bp.getCrudBanche().add(context, nuova_banca);
					else if (!existBancaInCollection(coll, banca_selezionata))
						bp.getCrudBanche().add(context, nuova_banca);
				}
				bp.getCrudBanche().reset(context);
			}
			return context.findDefaultForward();
		} catch (Throwable e) {
			return handleException(context, e);
		}
	}

	public Forward doBringBackModalita_pagamento(ActionContext context) {
		try {
			CRUDTerzoBP bp = (CRUDTerzoBP) getBusinessProcess(context);
			TerzoBulk terzo = (TerzoBulk) bp.getModel();
			HookForward caller = (HookForward) context.getCaller();
			java.util.Collection coll = (java.util.Collection) caller
					.getParameter("selectedElements");
			if (coll != null) {
				for (java.util.Iterator i = coll.iterator(); i.hasNext();) {
					Modalita_pagamentoBulk modalita_pagamento = new Modalita_pagamentoBulk();
					modalita_pagamento
							.setRif_modalita_pagamento((Rif_modalita_pagamentoBulk) i
									.next());
					modalita_pagamento.setTerzo(terzo);
					modalita_pagamento.setUser(context.getUserContext()
							.getUser());
					modalita_pagamento.setToBeCreated();

					if (!terzo.getModalita_pagamento().containsByPrimaryKey(modalita_pagamento))
						terzo.addToModalita_pagamento(modalita_pagamento);
					if (modalita_pagamento.isPerCessione())
						modalita_pagamento.setTerzo_delegato(new TerzoBulk());
				}
				bp.getCrudModalita_pagamento().reset(context);
			}
			return context.findDefaultForward();
		} catch (Throwable e) {
			return handleException(context, e);
		}
	}

	/**
	 * Assegnamento del un comune comune alla sede di un terzo.
	 * 
	 * @param context
	 *            {@link ActionContext } in uso.
	 * @param terzo
	 *            {@link TerzoBulk }
	 * @param comune
	 *            {@link ComuneBulk } da assegnare.
	 * 
	 * @return Forward
	 * 
	 * @exception RemoteException
	 */
	public Forward doBringBackSearchFind_comune_sede(ActionContext context,
			TerzoBulk terzo, ComuneBulk comune) throws java.rmi.RemoteException {

		CRUDTerzoBP bp = (CRUDTerzoBP) getBusinessProcess(context);

		if (comune != null) {
			terzo.setComune_sede(comune);

			if (comune.getPg_comune() != null
					&& !("".equals(comune.getPg_comune()))) {
				try {
					terzo = ((TerzoComponentSession) bp
							.createComponentSession()).setComune_sede(
							context.getUserContext(), terzo, comune);
					getBusinessProcess(context).setModel(context, terzo);
				} catch (Throwable e) {
					return handleException(context, e);
				}
			}
		}

		return context.findDefaultForward();
	}

	public Forward doBringBackSearchFind_unita_organizzativa(
			ActionContext context, TerzoBulk trz, Unita_organizzativaBulk uo) {
		if (trz.getDenominazione_sede() == null && uo != null)
			trz.setDenominazione_sede(uo.getDs_unita_organizzativa());
		trz.setUnita_organizzativa(uo);

		return context.findDefaultForward();
	}

	/**
	 * Gestisce una eccezione di chiave duplicata
	 */
	public Forward doConfirmHandleExCodiceFiscale(ActionContext context,
			it.cnr.jada.util.action.OptionBP option) {
		try {
			it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP bp = (it.cnr.contab.anagraf00.bp.CRUDAnagraficaBP) getBusinessProcess(context);
			if (option.getOption() == it.cnr.jada.util.action.OptionBP.NO_BUTTON) {
				((AnagraficoBulk) bp.getModel())
						.setFl_codice_fiscale_forzato(Boolean.TRUE);
				return context.findDefaultForward();
			}
			AnagraficoBulk ana = (AnagraficoBulk) option
					.getAttribute("anagrafica");
			ana.setCodice_fiscale((String) option.getAttribute("nuovoCodice"));
			bp.setModel(context, ana);
			return context.findDefaultForward();
		} catch (BusinessProcessException e) {
			return handleException(context, e);
		}
	}

	/**
	 * Metodo invocato dal metodo doSalva, gestisce la risposta dell'utente.
	 * 
	 * @param context
	 *            la <code>ActionContext</code> che ha generato la richiesta
	 * @param optionBP
	 *            <code>OptionBP</code> l'oggetto che contiene le informazioni
	 *            relative alla risposta dell'utente
	 * 
	 * @return Forward
	 */

	public Forward doConfirmSalva(ActionContext context, OptionBP optionBP)
			throws it.cnr.jada.comp.ApplicationException {

		try {

			CRUDTerzoBP bp = (CRUDTerzoBP) getBusinessProcess(context);
			if (optionBP.getOption() == OptionBP.YES_BUTTON) {
				return super.doSalva(context);
			}
			return context.findDefaultForward();

		} catch (Throwable e) {
			return handleException(context, e);
		}
	}

	/**
	 * Gestisce un comando di cancellazione modificato rispetto al default.
	 * 
	 * @see it.cnr.jada.util.action.CRUDAction#doElimina
	 */
	public Forward doElimina(ActionContext context)
			throws java.rmi.RemoteException {

		try {
			fillModel(context);

			it.cnr.jada.util.action.CRUDBP bp = getBusinessProcess(context);
			if (!bp.isEditing()) {
				bp.setMessage("Non è possibile cancellare in questo momento");
			} else {
				bp.delete(context);
				try {
					bp.edit(context, bp.getModel());
				} catch (Throwable e) {
				}
				if (((TerzoBulk) bp.getModel()).getDt_fine_rapporto() != null) {
					bp.setMessage("Data di fine rapporto impostata");
				} else {
					bp.reset(context);
					bp.setMessage("Cancellazione effettuata");
				}
			}
			return context.findDefaultForward();
		} catch (Throwable e) {
			return handleException(context, e);
		}
	}

	/**
	 * Gestisce un comando di cancellazione di una Modalità di Pagamento
	 * 
	 * @see it.cnr.jada.util.action.CRUDAction#doElimina
	 */
	public Forward doRemoveFromCRUDMain_Modalita_pagamento(ActionContext context)
			throws ValidationException, BusinessProcessException {

		CRUDTerzoBP bp = (CRUDTerzoBP) getBusinessProcess(context);
		Modalita_pagamentoBulk mod_pag = (Modalita_pagamentoBulk) bp
				.getCrudModalita_pagamento().getModel();
		java.util.List selected_mod = bp.getCrudModalita_pagamento()
				.getSelectedModels(context);
		TerzoBulk terzo = (TerzoBulk) bp.getModel();
		boolean more_mod_pag = false;

		try {
			// Sono state selezionate più Modalità di Pagamento: controlla che
			// non ve ne siano create da Migrazione.
			if (selected_mod != null && selected_mod.size() > 0) {
				for (java.util.Iterator i = selected_mod.iterator(); i
						.hasNext();) {
					Modalita_pagamentoBulk sel_modalita = (Modalita_pagamentoBulk) i
							.next();
					if (sel_modalita.getUtcr() != null
							&& sel_modalita.getUtcr().compareTo(
									"$$$$$MIGRAZIONE$$$$$") == 0) {
						throw new it.cnr.jada.comp.ApplicationException(
								"Attenzione: la  Modalità di Pagamento "
										+ sel_modalita.getCd_modalita_pag()
										+ " non è cancellabile\npoichè creata da una operazione di migrazione.");
					}
				}
			} else if (mod_pag != null) {

				// Controlla che la mOdalità di Pagamento NON sia stata creata
				// da Migrazione
				String ti_pagamento = mod_pag.getRif_modalita_pagamento()
						.getTi_pagamento();
				if (mod_pag.getUtcr() != null
						&& mod_pag.getUtcr().compareTo("$$$$$MIGRAZIONE$$$$$") == 0) {
					throw new it.cnr.jada.comp.ApplicationException(
							"Attenzione: la  Modalità di Pagamento "
									+ mod_pag.getCd_modalita_pag()
									+ " non è cancellabile\npoichè creata da una operazione di migrazione.");
				}

				// Verifica l'esistenza di un'altra Modalità di Pagamento che
				// abbia lo stesso ti_pagamento
				for (java.util.Iterator i = terzo.getModalita_pagamento()
						.iterator(); i.hasNext();) {
					Modalita_pagamentoBulk m = (Modalita_pagamentoBulk) i
							.next();
					if (m != mod_pag
							&& ti_pagamento.equals(m
									.getRif_modalita_pagamento()
									.getTi_pagamento())) {
						more_mod_pag = true;
					}
				}

				/*
				 * NON esistono altre Mod. di Pagamento dello stesso tipo di
				 * quella che si sta tentando di cancellare: il sistema
				 * controlla che, tra le banche collegate alla Mod. Pag. da
				 * cancellare NON ve ne siano che sono state create da procedure
				 * di MIGRAZIONE, (ORIGINE == 'S'): in tal caso, l'operazione
				 * sarà bloccata.
				 */
				if (!more_mod_pag && verifyBanche_StipendiFor(terzo, mod_pag)) {
					throw new it.cnr.jada.comp.ApplicationException(
							"Attenzione: questa Modalità di Pagamento non è cancellabile poichè una o più banche ad essa associate sono state create da una operazione di migrazione.");
				}
			}

			getController(context, "main.Modalita_pagamento").remove(context);

		} catch (it.cnr.jada.comp.ApplicationException e) {
			return handleException(context, e);
		}

		return context.findDefaultForward();
	}

	/**
	 * Gestisce un comando di cancellazione di una Banca
	 * 
	 * @see it.cnr.jada.util.action.CRUDAction#doElimina
	 */
	public Forward doRemoveFromCRUDMain_Modalita_pagamento_Banche(
			ActionContext context) throws ValidationException,
			BusinessProcessException {

		CRUDTerzoBP bp = (CRUDTerzoBP) getBusinessProcess(context);
		try {
			// Sono state selezionate + banche da cancellare: controlla che tra
			// queste NON ci siano banche da MIGRAZIONE
			if (bp.getCrudBanche().getSelection(context).size() > 1) {
				java.util.List selected_banche = bp.getCrudBanche()
						.getSelectedModels(context);
				if (selected_banche != null && selected_banche.size() > 0) {
					for (java.util.Iterator i = selected_banche.iterator(); i
							.hasNext();) {
						BancaBulk banca = (BancaBulk) i.next();
						if (banca.isOrigineStipendi()) {
							throw new it.cnr.jada.comp.ApplicationException(
									"Attenzione: la banca "
											+ banca.getIntestazione()
											+ " non è cancellabile,\nin quanto creata da una operazione di migrazione.");
						}
					}
				}
			} else {
				BancaBulk banca = (BancaBulk) bp.getCrudBanche().getModel();
				if (banca != null && banca.isOrigineStipendi()) {
					throw new it.cnr.jada.comp.ApplicationException(
							"Attenzione: la banca selezionata non è cancellabile,\nin quanto creata da una operazione di migrazione.");
				}
			}
			getController(context, "main.Modalita_pagamento.Banche").remove(
					context);

		} catch (it.cnr.jada.comp.ApplicationException e) {
			return handleException(context, e);
		}

		return context.findDefaultForward();
	}

	/**
	 * Rimove i termini di pagamento selezionati.
	 * 
	 * @param context
	 *            {@link ActionContext } in uso.
	 * 
	 * @return Forward
	 */

	public Forward doRimuoviTermini_pagamento(ActionContext context) {
		try {
			fillModel(context);
			CRUDTerzoBP bp = (CRUDTerzoBP) context.getBusinessProcess();
			for (java.util.Iterator i = bp.getCrudTermini_pagamento()
					.iterator(); i.hasNext();)
				((OggettoBulk) i.next()).setUser(context.getUserContext()
						.getUser());
			bp.getCrudTermini_pagamento().remove(context);
			bp.getCrudTermini_pagamento_disponibili().reset(context);
			return context.findDefaultForward();
		} catch (Throwable e) {
			return super.handleException(context, e);
		}
	}

	/**
	 * Metodo reimplementato per controllare le modalità di pagamento.
	 * Controlla, per ogni modalità di pagamento indicata, se sono state
	 * speficicati i dati relativi alla banca di riferimento. Se una modalità
	 * risulta sprovvista dei dati necessari, viene visualizzato un messaggio
	 * che indica che quella modalità non verrà salvata e chiede se si vuole
	 * continuare. La risposta dell'Utente viene gestita dal metodo
	 * doConfirmSalva(ActionContext).
	 * 
	 * @param context
	 *            la <code>ActionContext</code> che ha generato la richiesta
	 * 
	 * @return Forward
	 */

	public Forward doSalva(ActionContext context) {
		try {
			fillModel(context);
			CRUDTerzoBP bp = (CRUDTerzoBP) context.getBusinessProcess();
			TerzoBulk terzo = (TerzoBulk) bp.getModel();
			boolean existBanca = true;
			for (java.util.Iterator i = terzo.getModalita_pagamento()
					.iterator(); i.hasNext();) {
				Modalita_pagamentoBulk modalita_pagamento = (Modalita_pagamentoBulk) i
						.next();
				if (modalita_pagamento.isPerCessione()) {
					if (modalita_pagamento.getCd_terzo_delegato() == null)
						throw new it.cnr.jada.comp.ApplicationException(
								"Non sono state specificati i dati relativi ai riferimenti al cessionario.");
					existBanca = false;
					for (java.util.Iterator it = terzo.getBanche(
							modalita_pagamento).iterator(); it.hasNext();) {
						BancaBulk banca = (BancaBulk) it.next();
						if (modalita_pagamento.getCd_terzo_delegato() != null
								&& banca.getCd_terzo_delegato() != null
								&& banca.getCd_terzo_delegato().compareTo(
										modalita_pagamento
												.getCd_terzo_delegato()) == 0)
							existBanca = true;
					}
				}
				if (!existBanca)
					throw new it.cnr.jada.comp.ApplicationException(
							"Non sono state specificati i dati relativi ai riferimenti di pagamento per il cessionario "
									+ modalita_pagamento.getCd_terzo_delegato()
									+ ".");
				if (terzo.getBanche(modalita_pagamento).isEmpty()) {
					OptionBP optionBP = openConfirm(
							context,
							"Attenzione: la modalità di pagamento <b><u>"
									+ modalita_pagamento.getCd_modalita_pag()
									+ " </u></b>non verrà salvata, poichè non sono state specificati i dati relativi ai riferimenti di pagamento.Continuare?",
							OptionBP.CONFIRM_YES_NO, "doConfirmSalva");
					return optionBP;
				}				
			}
			if(terzo.getAnagrafico()!=null && terzo.getAnagrafico().isStrutturaCNR() && terzo.getCodiceUnivocoPcc()==null){
				bp.setMessage("Attenzione, l'anagrafica è censita nell'indice delle "+
						"pubbliche amministrazioni, richiedere tramite helpdesk l'inserimento del codice Pcc relativo al terzo che si sta tentando di modificare/creare." +
							"Salvataggio eseguito in modo corretto.");					
			}
				
			return super.doSalva(context);
		} catch (Throwable e) {
			return super.handleException(context, e);
		}
	}

	/**
	 * Insert the method's description here. Creation date: (11/11/2002
	 * 11.29.43)
	 * 
	 * @return boolean
	 * @param addBanca
	 *            it.cnr.contab.anagraf00.core.bulk.BancaBulk
	 */
	private boolean existBancaInCollection(java.util.List bancheColl,
			BancaBulk addBanca) {

		for (java.util.Iterator i = bancheColl.iterator(); i.hasNext();) {
			BancaBulk existBanca = (BancaBulk) i.next();

			if ((existBanca.getPg_banca_delegato().compareTo(
					addBanca.getPg_banca()) == 0)
					&& (existBanca.getCd_terzo_delegato().compareTo(
							addBanca.getTerzo().getCd_terzo()) == 0)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Insert the method's description here. Creation date: (11/11/2002
	 * 11.29.43)
	 * 
	 * @return boolean
	 * @param terzo
	 *            it.cnr.contab.anagraf00.core.bulk.BancaBulk
	 */
	private boolean verifyBanche_StipendiFor(TerzoBulk terzo,
			Modalita_pagamentoBulk mod_pag) {

		for (java.util.Iterator i = terzo.getBanche().iterator(); i.hasNext();) {
			BancaBulk banca = (BancaBulk) i.next();
			if (mod_pag.getChiavePerBanca().equals(banca.getChiave())
					&& banca.isOrigineStipendi()) {
				return true;
			}
		}

		return false;
	}

	public Forward doOnChangeNazioneIban(ActionContext context) {
		try {
			fillModel(context);
			CRUDTerzoBP bp = (CRUDTerzoBP) context.getBusinessProcess();
			BancaBulk banca = (BancaBulk) bp.getCrudBanche().getModel();
			banca.setCodice_iban_parte1(null);
			banca.setCodice_iban_parte2(null);
			banca.setCodice_iban_parte3(null);
			banca.setCodice_iban_parte4(null);
			banca.setCodice_iban_parte5(null);
			banca.setCodice_iban_parte6(null);
			banca.allineaIbanDaContoIT();
			return context.findDefaultForward();
		} catch (java.lang.ClassCastException ex) {
			return context.findDefaultForward();
		} catch (Throwable ex) {
			return handleException(context, ex);
		}
	}
}
