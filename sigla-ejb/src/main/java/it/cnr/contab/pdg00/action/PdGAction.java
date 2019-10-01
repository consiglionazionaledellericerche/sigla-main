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

package it.cnr.contab.pdg00.action;

import it.cnr.contab.prevent00.bulk.*;
import it.cnr.contab.pdg00.bp.*;
import it.cnr.contab.pdg00.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

public class PdGAction extends it.cnr.jada.util.action.BulkAction {
public PdGAction() {
		super();
	}
/**
 * Gestione annullamento dello scarico CDP su PDG
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doAnnullaScaricaDip(it.cnr.jada.action.ActionContext context) {
		try {
			fillModel(context);
			it.cnr.contab.pdg00.bp.PdGPreventivoBP bp = (it.cnr.contab.pdg00.bp.PdGPreventivoBP)context.getBusinessProcess();
			bp.setModel(context,
				bp.createPdGPreventivoComponentSession().annullaCDPSuPdg(
					context.getUserContext(),
					(it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk)bp.getModel()
				)
			);
			bp.setMessage("Operazione completata");
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestione apertura della consultazione di Entrata
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public it.cnr.jada.action.Forward doConsPDGEntrata(it.cnr.jada.action.ActionContext context) {
			try {
				fillModel(context);
				it.cnr.jada.util.action.ConsultazioniBP bp = (it.cnr.jada.util.action.ConsultazioniBP)context.createBusinessProcess("ConsPDGAggregatoEntrataBP");
				bp.setModel(context,((BulkBP)context.getBusinessProcess()).getModel());
				bp.openIterator(context);
				return context.addBusinessProcess(bp);
			} catch(Throwable e) {
				return handleException(context,e);
			}
	}
	/**
	 * Gestione apertura della consultazione di Spesa
	 *
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public it.cnr.jada.action.Forward doConsPDGSpesa(it.cnr.jada.action.ActionContext context) {
			try {
				fillModel(context);
				it.cnr.jada.util.action.ConsultazioniBP bp = (it.cnr.jada.util.action.ConsultazioniBP)context.createBusinessProcess("ConsPDGAggregatoSpesaBP");
				bp.setModel(context,((BulkBP)context.getBusinessProcess()).getModel());
				bp.openIterator(context);
				return context.addBusinessProcess(bp);
			} catch(Throwable e) {
				return handleException(context,e);
			}
	}			
/**
 * Gestione apertura del pannello di controllo delle stampe del DPG
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doApriStampe(it.cnr.jada.action.ActionContext context) {
		try {
			fillModel(context);
			it.cnr.contab.pdg00.bp.PdGStampePreventivoBP bp = (it.cnr.contab.pdg00.bp.PdGStampePreventivoBP)context.createBusinessProcess("PdGStampePreventivoBP");
			bp.setModel(context,((BulkBP)context.getBusinessProcess()).getModel());
			return context.addBusinessProcess(bp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di ricerca del searchtool "centro_responsabilita"
 *
 * @param context	L'ActionContext della richiesta
 * @param pdg	L'OggettoBulk padre del searchtool
 * @param cdr	L'OggettoBulk selezionato dall'utente
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doBringBackSearchCentro_responsabilita(it.cnr.jada.action.ActionContext context,it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk pdg, it.cnr.contab.config00.sto.bulk.CdrBulk cdr) {
 try {
  it.cnr.contab.pdg00.bp.PdGPreventivoBP bp = (it.cnr.contab.pdg00.bp.PdGPreventivoBP)context.getBusinessProcess();
			bp.setModel(context,
				bp.createPdGPreventivoComponentSession().caricaPdg(
					context.getUserContext(),
					cdr
				)
			);
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
}
/**
 * Controllo navigazione a gestione dei Costi caricati da altra UO
 * 
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doCostiCaricati(it.cnr.jada.action.ActionContext context) {
		try {
			it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk pdg = (it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk)((BulkBP)context.getBusinessProcess()).getModel();

			it.cnr.jada.util.action.CRUDListaBP nbp = (it.cnr.jada.util.action.CRUDListaBP)context.getUserInfo().createBusinessProcess(
							context,
							"CRUDCostiCaricatiBP",
							new Object[] {
								"M",
								pdg.getCentro_responsabilita()
							}
						);

			return context.addBusinessProcess(nbp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
/**
 * Controllo navigazione a gestione visualizzazione dei costi/ricavi scaricati ad altra UO
 *
 * @param context	L'ActionContext della richiesta
 * @param bpName	
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doCostiScaricati(it.cnr.jada.action.ActionContext context, String bpName) {
		try {
			it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk pdg = (it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk)((BulkBP)context.getBusinessProcess()).getModel();

			it.cnr.jada.util.action.CRUDListaBP nbp = (it.cnr.jada.util.action.CRUDListaBP)context.getUserInfo().createBusinessProcess(
							context,
							bpName,
							new Object[] {
								"M",
								pdg.getCentro_responsabilita()
							}
						);

			return context.addBusinessProcess(nbp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
/**
 * Controllo navigazione a gestione dei costi scaricati verso altro CDR
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doCostiScaricatiEtr(it.cnr.jada.action.ActionContext context) {
		return doCostiScaricati(context, "CRUDCostiScaricatiEtrBP");
	}
/**
 * Controllo navigazione a gestione dei costi scaricati verso altra UO
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doCostiScaricatiSpe(it.cnr.jada.action.ActionContext context) {
		return doCostiScaricati(context, "CRUDCostiScaricatiSpeBP");
	}
/**
 * Gestione eliminazione dei dettagli di piano di gestione per linea di attività
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doDelDetByLA(it.cnr.jada.action.ActionContext context) {
		try {
			fillModel(context);
			it.cnr.contab.pdg00.bp.PdGPreventivoBP bp = (it.cnr.contab.pdg00.bp.PdGPreventivoBP)context.getBusinessProcess();
			bp.setModel(context,
				bp.createPdGPreventivoComponentSession().delDetByLA(
					context.getUserContext(),
					(it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk)bp.getModel()
				)
			);
			bp.setMessage("Operazione completata, si ricorda che eventuali dettagli di scarico verso altro cdr/uo non possono essere eliminati");
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
/**
 * Gestione dettagli di entrata del PDG
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doDettagliEtrPdG(it.cnr.jada.action.ActionContext context) {
		return doDettagliPdG(context, "CRUDEtrDetPdGBP");
	}
/**
 * Gestione dettagli di PDG
 *
 * @param context	L'ActionContext della richiesta
 * @param bpName	Nome del sotto-bp da creare
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doDettagliPdG(it.cnr.jada.action.ActionContext context, String bpName) {
		try {
			PdGPreventivoBP bp = (PdGPreventivoBP)context.getBusinessProcess();
			Pdg_preventivoBulk pdg = (Pdg_preventivoBulk)bp.getModel();

			boolean pdgModificabile = bp.createPdGPreventivoComponentSession().isDettagliPdGModificabili(context.getUserContext(), pdg);

			BulkBP nbp = (BulkBP)context.getUserInfo().createBusinessProcess(
							context,
							bpName,
							new Object[] {
								bp.isEditable() && pdgModificabile ? "M" : "V",
								pdg.getCentro_responsabilita()
							}
						);

			if (!pdgModificabile)
			  if(pdg.getStato().equals(Pdg_preventivoBulk.ST_F_CHIUSO_DFNT))
			    nbp.setMessage(nbp.WARNING_MESSAGE,"Lo stato del pdg è in chiusura definitiva, utilizzare la funzione di variazioni al PDG per apportare modifiche.");
			  else
				nbp.setMessage(nbp.WARNING_MESSAGE,"Lo stato del pdg o il livello dell'utente non permette la modifica dei dettagli.");

			return context.addBusinessProcess(nbp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
/**
 * Gestione dettagli di spesa del PDG
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doDettagliSpePdG(it.cnr.jada.action.ActionContext context) {
		return doDettagliPdG(context, "CRUDSpeDetPdGBP");
	}
/**
 * Gestione navigazione a controllo di eliminazione di dettagli del PDG per linee di attività
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doEliminazionePerLineaAttivita(it.cnr.jada.action.ActionContext context) {
		try {
			it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk pdg = (it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk)((BulkBP)context.getBusinessProcess()).getModel();

			BulkBP nbp = (BulkBP)context.getUserInfo().createBusinessProcess(
							context,
							"EliminazionePerLineaAttivitaBP",
							new Object[] {
								"M"
							}
						);

			nbp.setModel(context, ((BulkBP)context.getBusinessProcess()).getModel() );
			return context.addBusinessProcess(nbp);

		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
/**
 * Gestione navigazione a contradditorio su entrate figurative caricate da altro CDR
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doEntrateFigurative(it.cnr.jada.action.ActionContext context) {
		try {
			it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk pdg = (it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk)((BulkBP)context.getBusinessProcess()).getModel();

			it.cnr.jada.util.action.CRUDListaBP nbp = (it.cnr.jada.util.action.CRUDListaBP)context.getUserInfo().createBusinessProcess(
							context,
							"CRUDEntrateFigurativeBP",
							new Object[] {
								"M",
								pdg.getCentro_responsabilita()
							}
						);

			return context.addBusinessProcess(nbp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
/**
 * Gestisce una richiesta di ricerca libera su un searchtool
 */
public it.cnr.jada.action.Forward doFreeSearch(it.cnr.jada.action.ActionContext context,String fieldName) {
	try {
		PdGPreventivoBP bp = (PdGPreventivoBP)context.getBusinessProcess();
		bp.fillModel(context);
		it.cnr.jada.util.action.FormField field = getFormField(context,fieldName);
		try {
				it.cnr.jada.bulk.OggettoBulk model = field.getModel();
				it.cnr.jada.util.action.RicercaLiberaBP fsbp = (it.cnr.jada.util.action.RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
				fsbp.setFindbp((it.cnr.jada.util.action.FindBP)bp);
				fsbp.setPrototype((it.cnr.jada.bulk.OggettoBulk)field.getField().getPropertyType().newInstance(),model,field.getField().getProperty());
				context.addHookForward("seleziona",this,"doBringBackSearchResult");
				it.cnr.jada.action.HookForward hook = (it.cnr.jada.action.HookForward)context.findForward("seleziona");
				hook.addParameter("field",field);
				return context.addBusinessProcess(fsbp);
			} catch(Exception ex) {
				return handleException(context,ex);
			}
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Gestione modifica dello stato del piano di Gestione
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doModificaStato(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	PdGPreventivoBP bp = (PdGPreventivoBP)context.getBusinessProcess();

	try {
		fillModel(context);		

		bp.setDirty(false);

		bp.setModel(
			context,
			bp.createPdGPreventivoComponentSession().modificaStatoPdG(
				context.getUserContext(),
				(Pdg_preventivoBulk)bp.getModel()));

		return context.findDefaultForward();
	} catch(it.cnr.contab.pdg00.comp.DiscrepanzeAggregatoException e) {
		OptionBP option = openConfirm(context,e.getMessage(),OptionBP.CONFIRM_YES_NO,"doConfirmListaDiscrepanze");
		option.addAttribute("discrepanze", e.getLista());
		return context.findDefaultForward();
	} catch(it.cnr.jada.comp.ApplicationException e) {
		bp.caricaPdg(context,((Pdg_preventivoBulk)bp.getModel()).getCentro_responsabilita());
		return handleException(context,e);
	} catch(Throwable e) {	      
		return handleException(context,e);
	}
}
/**
 * Gestione del ribaltamento dei costi sulle aree di ricerca
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doRibaltamento(it.cnr.jada.action.ActionContext context) {
		try {
			fillModel(context);
			it.cnr.contab.pdg00.bp.PdGPreventivoBP bp = (it.cnr.contab.pdg00.bp.PdGPreventivoBP)context.getBusinessProcess();
			bp.setModel(context,
				bp.createPdGPreventivoComponentSession().ribaltaCostiPdGArea(
					context.getUserContext(),
					(it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk)bp.getModel()
				)
			);
			bp.setMessage("Operazione completata");
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
/**
 * Gestione scarico dei CDP su PDG
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doScaricaDipendenti(it.cnr.jada.action.ActionContext context) {
		try {
			fillModel(context);
			it.cnr.contab.pdg00.bp.PdGPreventivoBP bp = (it.cnr.contab.pdg00.bp.PdGPreventivoBP)context.getBusinessProcess();
			bp.setModel(context,
				bp.createPdGPreventivoComponentSession().scaricaCDPSuPdg(
					context.getUserContext(),
					(it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk)bp.getModel()
				)
			);
			bp.setMessage("Operazione completata");
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
}
