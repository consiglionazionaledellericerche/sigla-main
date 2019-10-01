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


import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.pdg00.bp.CostiDipendenteBP;
import it.cnr.contab.pdg00.cdip.bulk.Ass_cdp_laBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_cdp_uoBulk;
import it.cnr.contab.pdg00.cdip.bulk.Costi_dipendenteVBulk;
import it.cnr.contab.pdg00.cdip.bulk.Costo_del_dipendenteBulk;
import it.cnr.contab.pdg00.cdip.bulk.V_cdp_matricolaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.action.SelectionIterator;

import java.math.BigDecimal;

/**
 * Action per la gestione di CostiDipendenteBP
 */
public class CostiDipendenteAction extends it.cnr.jada.util.action.BulkAction {
public CostiDipendenteAction() {
	super();
}

/**
 * Gestisce la creazione di un nuovo costo scaricato per la matricola
 * selezionata. Aggiunge una nuova riga ai dettagli dei costi scaricati
 * e fa partire la selezione del CDR su cui scaricare.
 * La selezione del CDR viene gestita <code>doBringBackSearchCdr</code>
 */ 
public it.cnr.jada.action.Forward doAddToCRUDMain_costiDipendenti_costiScaricati(it.cnr.jada.action.ActionContext context) {
	try {
		CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
		Costi_dipendenteVBulk cdp = (Costi_dipendenteVBulk)bp.getModel();
		it.cnr.jada.util.RemoteIterator i = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,bp.createComponentSession().listaCdr(context.getUserContext(),cdp.getUnita_organizzativa_filter().getCd_unita_organizzativa(),bp.getMese()));
		if (i.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, i);
			return context.findDefaultForward();
		}
		if (i.countElements() == 1) {
			CdrBulk cdr = (CdrBulk)i.nextElement();
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, i);
			return doSelezionaCdrPerScarico(context,cdr);
		}
		return select(context,i,it.cnr.jada.bulk.BulkInfo.getBulkInfo(CdrBulk.class),null,"doSelezionaCdrPerScarico");
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce la creazione di un nuovo costo scaricato verso altra UO 
 * per la matricola selezionata. 
 * Aggiunge una nuova riga ai dettagli dei costi scaricati verso altra UO
 * e fa partire la selezione dell'UO su cui scaricare.
 * La selezione del UO viene gestita <code>doBringBackSearchUo</code>
 */ 
public it.cnr.jada.action.Forward doAddToCRUDMain_costiDipendenti_costiScaricatiAltraUO(it.cnr.jada.action.ActionContext context) {
	return doSearch(context, "main.unita_organizzativa_scarico");
}
/**
 * Ricalcola i giorni del dettaglio di scarico selezionato.
 * @param i l'anno per cui effettuare il ricalcolo (1,2,3)
 */
public it.cnr.jada.action.Forward doCalcolaGiorni_la(it.cnr.jada.action.ActionContext context,int i) {
	try {
		fillModel(context);
		CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
		Ass_cdp_laBulk ass_bp_la = (Ass_cdp_laBulk)bp.getCostiScaricati().getModel();
		if (ass_bp_la.getPrc_la(i) == null)
			ass_bp_la.setPrc_la(i,java.math.BigDecimal.valueOf(0));
		ass_bp_la.calcolaGiorni_la(i,((V_cdp_matricolaBulk)bp.getCostiDipendenti().getModel()).getGiorni_lavorativi(i));
		validaSommaPrc(context);
		return context.findDefaultForward();
	} catch(it.cnr.jada.bulk.ValidationException e) {
		return handleException(context,e);
	} catch(it.cnr.jada.bulk.FillException e) {
		return handleException(context,e);
	}
}

/**
 * Ricalcola i giorni del dettaglio di scarico verso altra UO selezionato.
 * @param i l'anno per cui effettuare il ricalcolo (1,2,3)
 */
public it.cnr.jada.action.Forward doCalcolaGiorni_uo(it.cnr.jada.action.ActionContext context,int i) {
	try {
		fillModel(context);
		CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
		Ass_cdp_uoBulk ass_bp_uo = (Ass_cdp_uoBulk)bp.getCostiScaricatiAltraUO().getModel();
		if (ass_bp_uo.getPrc_uo(i) == null)
			ass_bp_uo.setPrc_uo(i,java.math.BigDecimal.valueOf(0));
		ass_bp_uo.calcolaGiorni_uo(i,((V_cdp_matricolaBulk)bp.getCostiDipendenti().getModel()).getGiorni_lavorativi(i));
		validaSommaPrc(context);
		return context.findDefaultForward();
	} catch(it.cnr.jada.bulk.ValidationException e) {
		return handleException(context,e);
	} catch(it.cnr.jada.bulk.FillException e) {
		return handleException(context,e);
	}
}

/**
 * Ricalcola la percentuale del dettaglio di scarico selezionato.
 * @param i l'anno per cui effettuare il ricalcolo (1,2,3)
 */
public it.cnr.jada.action.Forward doCalcolaPrc_la(it.cnr.jada.action.ActionContext context,int i) {
	try {
		fillModel(context);
		CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
		Ass_cdp_laBulk ass_bp_la = (Ass_cdp_laBulk)bp.getCostiScaricati().getModel();
		if (ass_bp_la.getGiorni_la(i) == null)
			ass_bp_la.setGiorni_la(i,java.math.BigDecimal.valueOf(0));
		ass_bp_la.calcolaPrc_la(i,((V_cdp_matricolaBulk)bp.getCostiDipendenti().getModel()).getGiorni_lavorativi(i));
		validaSommaPrc(context);
		return context.findDefaultForward();
	} catch(it.cnr.jada.bulk.FillException e) {
		return handleException(context,e);
	} catch(it.cnr.jada.bulk.ValidationException e) {
		return handleException(context,e);
	}
}

/**
 * Ricalcola la percentuale del dettaglio di scarico verso altra UO selezionato.
 * @param i l'anno per cui effettuare il ricalcolo (1,2,3)
 */
public it.cnr.jada.action.Forward doCalcolaPrc_uo(it.cnr.jada.action.ActionContext context,int i) {
	try {
		fillModel(context);
		CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
		Ass_cdp_uoBulk ass_bp_uo = (Ass_cdp_uoBulk)bp.getCostiScaricatiAltraUO().getModel();
		if (ass_bp_uo.getGiorni_uo(i) == null)
			ass_bp_uo.setGiorni_uo(i,java.math.BigDecimal.valueOf(0));
		ass_bp_uo.calcolaPrc_uo(i,((V_cdp_matricolaBulk)bp.getCostiDipendenti().getModel()).getGiorni_lavorativi(i));
		validaSommaPrc(context);
		return context.findDefaultForward();
	} catch(it.cnr.jada.bulk.FillException e) {
		return handleException(context,e);
	} catch(it.cnr.jada.bulk.ValidationException e) {
		return handleException(context,e);
	}
}

/**
 * Gestisce la richiesta di cambio di stato di un dettaglio caricato da altra UO.
 * Invoca la componente per salvare lo stato del dettaglio
 */
public it.cnr.jada.action.Forward doCambiaStatoCostoCaricato(it.cnr.jada.action.ActionContext context) {
	CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
	V_cdp_matricolaBulk cdp = (V_cdp_matricolaBulk)bp.getCostiDipendenti().getModel();
	String statoOld = cdp.getCostoCaricato().getStato();
	try {
		fillModel(context);
		cdp = (V_cdp_matricolaBulk)bp.getCostiDipendenti().getModel();
		if (cdp!=null) {
			if (bp.createComponentSession().isCostiDipendenteDefinitivi(context.getUserContext(), cdp.getMese(), cdp.getCd_unita_organizzativa())) {
				cdp.getCostoCaricato().setStato(statoOld);
				bp.setMessage("Non è possibile modificare lo stato in quanto la U.O. " + cdp.getCd_unita_organizzativa() + " di appartenenza della matricola ha già reso definitiva la sua ripartizione dei costi.");
			} else if (bp.getMese()==0 && bp.createComponentSession().isCostiDipendenteRipartiti(context.getUserContext(), cdp.getCd_unita_organizzativa())) {
				cdp.getCostoCaricato().setStato(statoOld);
				bp.setMessage("Non è possibile modificare lo stato in quanto la U.O. " + cdp.getCd_unita_organizzativa() + " di appartenenza della matricola ha già scaricato i costi del personale sul piano di gestione.");
			} else {
				cdp.setStato_carico(cdp.getCostoCaricato().getStato());
				cdp.getCostoCaricato().setToBeUpdated();
			}
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		cdp.getCostoCaricato().setStato(statoOld);
		return handleException(context,e);
	}
}

/**
 * Controllo della copia della ripartizione dei CDP da una matricola ad un'altra differente
 *
 * @param context	L'ActionContext della richiesta
 * @param option 
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doConfermaCopiaRipartizione(it.cnr.jada.action.ActionContext context,int option) {
	try {
		if (option == OptionBP.YES_BUTTON) {
			CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
			bp.copiaRipartizione(context.getUserContext());
			bp.setModel(context,bp.createComponentSession().salvaCosti_dipendente(context.getUserContext(),	(Costi_dipendenteVBulk)bp.getModel()));
			bp.getCostiDipendenti().getSelection().clear();
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	} 
}

/**
 * Gestisce la richiesta di ripartizione dei residui su una o più matricole.
 * Mostra un elenco delle linee di attività su cui effettuare la ripartizione.
 * La gestione della selezione delle linee di attività viene effettuata da
 * doConfermaSelezioneRipartizioneResidui
 */
public it.cnr.jada.action.Forward doConfermaRipartizioneResidui(it.cnr.jada.action.ActionContext context,int option) {
	try {
		CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
		Costi_dipendenteVBulk costi_dipendente = (Costi_dipendenteVBulk)bp.getModel();
		V_cdp_matricolaBulk cdp = (V_cdp_matricolaBulk)bp.getCostiDipendenti().getModel();
		
		if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
			if (bp.getCostiDipendenti().getSelection().size() == 0 && 
				bp.getCostiDipendenti().getModel() == null)
				throw new it.cnr.jada.action.MessageToUser("E' necessario selezionare almeno una matricola");
			else if (bp.getCostiDipendenti().getSelection().size() != 0 && cdp == null)
				cdp = (V_cdp_matricolaBulk)bp.getCostiDipendenti().getDetails().get(bp.getCostiDipendenti().getSelection().iterator().nextIndex());
			
			int countTi=0, countTd=0, countRap3=0;
			StringBuffer matrRap3=new StringBuffer();
			for (SelectionIterator i = bp.getCostiDipendenti().getSelection().iterator();i.hasNext();) {
				V_cdp_matricolaBulk matricola_dest = (V_cdp_matricolaBulk)bp.getCostiDipendenti().getDetails().get(i.nextIndex());
				if (matricola_dest.getTi_rapporto().equalsIgnoreCase(Costo_del_dipendenteBulk.TI_RAPPORTO_INDETERMINATO))
					countTi++;
				else {
					countTd++;
					if (matricola_dest.getFl_rapporto13()) {
						countRap3++;
						if (matrRap3.length()>0) matrRap3.append(", ");
						matrRap3.append(matricola_dest.getId_matricola());
					}
				}
			}
			
			if (countTi>0 && countTd>0)
				throw new it.cnr.jada.action.MessageToUser("Funzione non disponibile in presenza di selezione contemporanea di dipendenti a tempo deteminato ed indeterminato.");
			else if (countRap3>0)
				throw new it.cnr.jada.action.MessageToUser("Funzione non disponibile per le matricole "+matrRap3+".");
				
			java.util.List linee = bp.createComponentSession().listaLinea_attivitaPerRipartizioneResidui(
				context.getUserContext(),
				cdp == null ? null : cdp.getId_matricola(),
				costi_dipendente.getUnita_organizzativa_filter().getCd_unita_organizzativa(),				
				bp.getMese(),
				cdp == null ? null : cdp.getTi_rapporto(),
				cdp == null ? null : cdp.getFl_rapporto13());
			if (linee != null && linee.size()!=0)  { // Fix del 05/03/2002 Se la lista è vuota non effettua operazioni
				it.cnr.jada.util.action.SelezionatoreListaBP slbp = select(context,new it.cnr.jada.util.ListRemoteIterator(linee),it.cnr.jada.bulk.BulkInfo.getBulkInfo(it.cnr.contab.config00.latt.bulk.WorkpackageBulk.class),null,"doConfermaSelezioneRipartizioneResidui");
				slbp.setMultiSelection(true);
				return slbp;
			}
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	} 
}

/**
 * Gestisce la richiesta di ripartizione dei residui su una o più matricole
 * in seguito alla selezione di una o più linee di attività.
 * Per ogni matricola selezionata invoca <code>ripartizioneResidui</code> 
 * sulla componente. Al primo errore interrompe il processo.
 */
public it.cnr.jada.action.Forward doConfermaSelezioneRipartizioneResidui(it.cnr.jada.action.ActionContext context) {
	try {
		CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();

		it.cnr.jada.action.HookForward caller = (it.cnr.jada.action.HookForward)context.getCaller();
		java.util.List linee = (java.util.List)caller.getParameter("selectedElements");
		bp.ripartizioneResidui(context.getUserContext(), linee);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	} 
}

/**
 * Gestisce la pressione del botton "Ripartizione residui". Se il
 * bp è "dirty" chiede conferma, quindi passa la gestione a
 * <code>doConfermaRipartizioneResidui</code>
 */
public it.cnr.jada.action.Forward doRipartizioneResidui(it.cnr.jada.action.ActionContext context) {
	try {
		CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();

		if (!bp.isRipartizioneCostiModificabile()) {
			if (!bp.isPdgPrevisionaleEnabled()) 
				bp.setMessage("Non è possibile modificare i dati in quanto il PdGP risulta confermato anche solo parzialmente.");
			if (bp.isCostiRipartiti()) 
				bp.setMessage("Non è possibile modificare i dati in quanto risulta essere stato effettuato uno scarico dei Costi Dipendenti.");
			return context.findDefaultForward();
		}

		fillModel(context);
		if (bp.isDirty()) 
			return openContinuePrompt(context,"doConfermaRipartizioneResidui");
		return doConfermaRipartizioneResidui(context,it.cnr.jada.util.action.OptionBP.YES_BUTTON);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}

/**
 * Gestisce il salvataggio dei dati di scarico dei costi di un dipendente.
 */
public it.cnr.jada.action.Forward doSalva(it.cnr.jada.action.ActionContext context) {
	CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
	try {
		fillModel(context);
		bp.getModel().validate();
		bp.setModel(context,bp.createComponentSession().salvaCosti_dipendente(
			context.getUserContext(),
			(Costi_dipendenteVBulk)bp.getModel()));
		bp.setDirty(false);
		if (bp.getCostiDipendenti().getSelection().isEmpty() || 
			(bp.getCostiDipendenti().getSelection().size() == 1 &&
			 bp.getCostiDipendenti().getSelection().isSelected(bp.getCostiDipendenti().getSelection().getFocus())))
			bp.setMessage("Salvataggio eseguito in modo corretto.");
		else
			return openConfirm(context,"Si vuole copiare la ripartizione sulle altre matricole selezionate?",OptionBP.QUESTION_MESSAGE,"doConfermaCopiaRipartizione");
		return context.findDefaultForward();
	} catch(it.cnr.jada.bulk.ValidationException e) {
		bp.setErrorMessage(e.getMessage());
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}

/**
 * Gestisce la selezione di un CDR in seguito alla creazione di un nuovo
 * dettaglio di scarico verso un CDR. Fa partire la selezione di una linea
 * attività, che viene gestita da doBringBackSearchLinea_attivita
 */
public it.cnr.jada.action.Forward doSelezionaCdrPerScarico(it.cnr.jada.action.ActionContext context) {
	it.cnr.jada.action.HookForward caller = (it.cnr.jada.action.HookForward)context.getCaller();
	return doSelezionaCdrPerScarico(context,(CdrBulk)caller.getParameter("focusedElement"));
}

/**
 * Gestisce la selezione di un CDR in seguito alla creazione di un nuovo
 * dettaglio di scarico verso un CDR. Fa partire la selezione di una linea
 * attività, che viene gestita da doBringBackSearchLinea_attivita
 */
private it.cnr.jada.action.Forward doSelezionaCdrPerScarico(it.cnr.jada.action.ActionContext context,CdrBulk cdr) {
	try {
		if (cdr == null) return context.findDefaultForward();
		CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
		V_cdp_matricolaBulk cdp = (V_cdp_matricolaBulk)bp.getCostiDipendenti().getModel();
		it.cnr.jada.util.RemoteIterator i = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,bp.createComponentSession().listaLinea_attivitaPerCdr(context.getUserContext(),cdr,bp.getMese(),cdp.getTi_rapporto(),cdp.getFl_rapporto13()));
		if (i.countElements() == 0) {
			bp.setErrorMessage("Nessuna linea di attività disponibile per il cdr selezionato.");
			return context.findDefaultForward();
		}
		Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(), CNRUserContext.getEsercizio(context.getUserContext())); 
		return select(context,i,it.cnr.jada.bulk.BulkInfo.getBulkInfo(WorkpackageBulk.class),parCnr.getFl_nuovo_pdg()?"prg_liv2":null,"doSelezionaLinea_attivitaPerScarico");
	} catch(Exception e) {
		return handleException(context,e);
	}
}

/**
 * Gestione selezione di una linea di attività per scarico CDP
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doSelezionaLinea_attivitaPerScarico(it.cnr.jada.action.ActionContext context) {
	it.cnr.jada.action.HookForward caller = (it.cnr.jada.action.HookForward)context.getCaller();
	return doSelezionaLinea_attivitaPerScarico(context,(WorkpackageBulk)caller.getParameter("focusedElement"));
}

/**
 * Gestisce la selezione di una linea di attività in seguito alla 
 * creazione di un nuovo dettaglio di scarico verso un CDR. 
 * Imposta la linea di attività scelta nel nuovo dettaglio
 * e i valori di default.
 */
private it.cnr.jada.action.Forward doSelezionaLinea_attivitaPerScarico(it.cnr.jada.action.ActionContext context,WorkpackageBulk linea_attivita) {
	try {
		CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
		V_cdp_matricolaBulk matricola = (V_cdp_matricolaBulk)bp.getCostiDipendenti().getModel();

		if (linea_attivita == null)
			return context.findDefaultForward();

		for (java.util.Iterator i = bp.getCostiScaricati().getDetails().iterator();i.hasNext();) {
			Ass_cdp_laBulk ass_cdp_la = (Ass_cdp_laBulk)i.next();
			if (linea_attivita.getCd_centro_responsabilita().equals(ass_cdp_la.getCd_centro_responsabilita()) && 
				linea_attivita.getCd_linea_attivita().equals(ass_cdp_la.getCd_linea_attivita()))
				throw new it.cnr.jada.action.MessageToUser("CDR e linea di attività già scelti");
		}

		Ass_cdp_laBulk ass_cdp_la = new Ass_cdp_laBulk();
		ass_cdp_la.setEsercizio(matricola.getEsercizio());
		ass_cdp_la.setId_matricola(matricola.getId_matricola());
		ass_cdp_la.setLinea_attivita(linea_attivita);
		ass_cdp_la.setStato(Ass_cdp_laBulk.STATO_NON_SCARICATO);
		ass_cdp_la.setPrc_la_a1(BigDecimal.ZERO);
		ass_cdp_la.setPrc_la_a2(BigDecimal.ZERO);
		ass_cdp_la.setPrc_la_a3(BigDecimal.ZERO);
		ass_cdp_la.setGiorni_la_a1(BigDecimal.ZERO);
		ass_cdp_la.setGiorni_la_a2(BigDecimal.ZERO);
		ass_cdp_la.setGiorni_la_a3(BigDecimal.ZERO);
		ass_cdp_la.setFl_dip_altra_uo(new Boolean(matricola.isProvenienzaCaricato()));

		bp.getCostiScaricati().addDetail(ass_cdp_la);
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}

/**
 * Gestisce la selezione di una linea di attività in seguito alla 
 * creazione di un nuovo dettaglio di scarico verso un CDR. 
 * Imposta la linea di attività scelta nel nuovo dettaglio
 * e i valori di default.
 */
public it.cnr.jada.action.Forward doBringBackSearchUnita_organizzativa_scarico(it.cnr.jada.action.ActionContext context, Costi_dipendenteVBulk cdp, Unita_organizzativaBulk uo) {
	try {
		CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
		V_cdp_matricolaBulk matricola = (V_cdp_matricolaBulk)bp.getCostiDipendenti().getModel();

		if (uo == null)
			return context.findDefaultForward();

		for (java.util.Iterator i = bp.getCostiScaricatiAltraUO().getDetails().iterator();i.hasNext();) {
			Ass_cdp_uoBulk ass_cdp_uo = (Ass_cdp_uoBulk)i.next();
			if (uo.getCd_unita_organizzativa().equals(ass_cdp_uo.getCd_unita_organizzativa()))
				throw new it.cnr.jada.action.MessageToUser("Unità organizzativa già scelta");
		}

		Ass_cdp_uoBulk ass_cdp_uo = new Ass_cdp_uoBulk();
		ass_cdp_uo.setEsercizio(matricola.getEsercizio());
		ass_cdp_uo.setUnita_organizzativa(uo);
		ass_cdp_uo.setStato(Ass_cdp_uoBulk.STATO_INIZIALE);
		ass_cdp_uo.setPrc_uo_a1(BigDecimal.ZERO);
		ass_cdp_uo.setPrc_uo_a2(BigDecimal.ZERO);
		ass_cdp_uo.setPrc_uo_a3(BigDecimal.ZERO);
		ass_cdp_uo.setGiorni_uo_a1(BigDecimal.ZERO);
		ass_cdp_uo.setGiorni_uo_a2(BigDecimal.ZERO);
		ass_cdp_uo.setGiorni_uo_a3(BigDecimal.ZERO);
		ass_cdp_uo.setId_matricola(matricola.getId_matricola());
		ass_cdp_uo.setToBeCreated();
		
		bp.getCostiScaricatiAltraUO().addDetail(ass_cdp_uo);

		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Controlla che la somma delle percentuali degli scarichi (sia su l.att. che 
 * verso altra UO) sia inferiore o uguale a 100%. In caso contrario
 * Visualizza un messaggio di avvertimento.
 */
private void validaSommaPrc(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.bulk.ValidationException {
	CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
	java.math.BigDecimal prc_a1;
	java.math.BigDecimal prc_a2;
	java.math.BigDecimal prc_a3;
	prc_a1 = prc_a2 = prc_a3 = java.math.BigDecimal.valueOf(0);
	for (java.util.Iterator i = bp.getCostiScaricati().getDetails().iterator();i.hasNext();) {
		Ass_cdp_laBulk ass_cdp_la = (Ass_cdp_laBulk)i.next();
		prc_a1 = prc_a1.add(ass_cdp_la.getPrc_la_a1());
		prc_a2 = prc_a2.add(ass_cdp_la.getPrc_la_a2());
		prc_a3 = prc_a3.add(ass_cdp_la.getPrc_la_a3());
	}
	if (bp.getCostiScaricatiAltraUO().getDetails() != null)
		for (java.util.Iterator i = bp.getCostiScaricatiAltraUO().getDetails().iterator();i.hasNext();) {
			Ass_cdp_uoBulk ass_cdp_uo = (Ass_cdp_uoBulk)i.next();
			if (!ass_cdp_uo.isNon_accettato()) {
				prc_a1 = prc_a1.add(ass_cdp_uo.getPrc_uo_a1());
				prc_a2 = prc_a2.add(ass_cdp_uo.getPrc_uo_a2());
				prc_a3 = prc_a3.add(ass_cdp_uo.getPrc_uo_a3());
			}
		}
	java.math.BigDecimal BD_100 = java.math.BigDecimal.valueOf(100);
	StringBuffer msg = new StringBuffer("La somma delle percentuali scaricate per gli anni ");
	String sep = "";
	if (prc_a1.compareTo(BD_100) > 0) {
		msg.append(sep);
		msg.append("1");
		sep = ",";
	}
	if (prc_a2.compareTo(BD_100) > 0) {
		msg.append(sep);
		msg.append("2");
		sep = ",";
	}
	if (prc_a3.compareTo(BD_100) > 0) {
		msg.append(sep);
		msg.append("3");
		sep = ",";
	}
	if (sep.length() > 0) {
		msg.append(" è superiore a 100");
		throw new it.cnr.jada.bulk.ValidationException(msg.toString());
	}
}
public Forward doBringBackSearchUnita_organizzativa_filter(ActionContext context,Costi_dipendenteVBulk costi, Unita_organizzativaBulk uo) {
	try {
   		costi.setUnita_organizzativa_filter(uo);
   		CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
		bp.setModel(context, bp.createComponentSession().caricaCosti_dipendente(context.getUserContext(),uo,bp.getMese()));
		bp.setDirty(false);
		return context.findDefaultForward(); 
	} catch(javax.ejb.EJBException e) {
		throw new it.cnr.jada.action.ActionPerformingError(e);
	} catch(java.rmi.RemoteException e) {
		throw new it.cnr.jada.action.ActionPerformingError(e);
	} catch (DetailedRuntimeException e) {
		throw new it.cnr.jada.action.ActionPerformingError(e);
	} catch (ComponentException e) {
		throw new it.cnr.jada.action.ActionPerformingError(e);
	} catch (BusinessProcessException e) {
		throw new it.cnr.jada.action.ActionPerformingError(e);
	}
}
/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di azzeramento del searchtool "unita_organizzativa_filter"
 *
 * @param context	L'ActionContext della richiesta
 * @param costi_dipendente	L'OggettoBulk padre del searchtool
 * @return Il Forward alla pagina di risposta
 */
public Forward doBlankSearchUnita_organizzativa_filter(ActionContext context,Costi_dipendenteVBulk costi) { 
	try {
		costi.setUnita_organizzativa_filter(new Unita_organizzativaBulk());
		CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
		bp.setModel(context, bp.createComponentSession().caricaCosti_dipendente(context.getUserContext(),null,bp.getMese()));
		return context.findDefaultForward(); 
	} catch(javax.ejb.EJBException e) {
		throw new it.cnr.jada.action.ActionPerformingError(e);
	} catch(java.rmi.RemoteException e) {
		throw new it.cnr.jada.action.ActionPerformingError(e);
	} catch (DetailedRuntimeException e) {
		throw new it.cnr.jada.action.ActionPerformingError(e);
	} catch (ComponentException e) {
		throw new it.cnr.jada.action.ActionPerformingError(e);
	} catch (BusinessProcessException e) {
		throw new it.cnr.jada.action.ActionPerformingError(e);
	}
}
public Forward doSalvaDefinitivo(ActionContext context){
	try 
	{
		fillModel( context );
		CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
		bp.getModel().validate();
		bp.completeSearchTools(context, bp);
        bp.validate(context);
		if (bp.isDirty()) {
			bp.setMessage("Operazione non consentita. Salvare le modifiche effettuate prima di rendere definitiva la ripartizione!");
			return context.findDefaultForward();
		}
		else
			return openConfirm(context, "Attenzione! Dopo il salvataggio definitivo non sarà più possibile modificare i dati inseriti. Si vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmSalvaDefinitivo");
		
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
public Forward doConfirmSalvaDefinitivo(ActionContext context,int option) {
	try 
	{
		if ( option == OptionBP.YES_BUTTON) 
		{
			CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
			bp.getModel().validate();
			bp.setModel(context,bp.createComponentSession().salvaDefinitivoCosti_dipendente(
				context.getUserContext(),
				(Costi_dipendenteVBulk)bp.getModel()));
			bp.setDirty(false);
			bp.setCostiDefinitivi(Boolean.TRUE);
			bp.setMessage("Salvataggio Definitivo eseguito in modo corretto.");
		}
		return context.findDefaultForward();
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
public Forward doAnnullaDefinitivo(ActionContext context){
	try 
	{
		fillModel( context );
		CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
		bp.completeSearchTools(context, bp);
        bp.validate(context);
		return openConfirm(context, "Attenzione! Sarà annullato il salvataggio definitivo. Si vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmAnnullaDefinitivo");
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
public Forward doConfirmAnnullaDefinitivo(ActionContext context,int option) {
	try 
	{
		if ( option == OptionBP.YES_BUTTON) 
		{
			CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
			bp.getModel().validate();
			bp.setModel(context,bp.createComponentSession().annullaDefinitivoCosti_dipendente(
				context.getUserContext(),
				(Costi_dipendenteVBulk)bp.getModel()));
			bp.setDirty(false);
			bp.setCostiDefinitivi(Boolean.FALSE);
			bp.setMessage("Annullamento Salvataggio Definitivo eseguito in modo corretto.");
		}
		return context.findDefaultForward();
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
public it.cnr.jada.action.Forward doCopiaMesePrecedente(it.cnr.jada.action.ActionContext context) {
	try 
	{
		fillModel( context );
		CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
		if (bp.getMese() > 1)
			return openConfirm(context, "Attenzione! Saranno caricati i dati recuperandoli dalla ripartizione del mese precedente. Si vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmCopiaMesePrecedente");
		else
			return openConfirm(context, "Attenzione! Saranno caricati i dati recuperandoli dalla ripartizione del PDGP. Si vuole procedere?", OptionBP.CONFIRM_YES_NO, "doConfirmCopiaMesePrecedente");
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
/**
 * Gestione conferma dello scarico dei CDP
 * 
 * @param context	L'ActionContext della richiesta
 * @param option	
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doConfirmCopiaMesePrecedente(it.cnr.jada.action.ActionContext context,it.cnr.jada.util.action.OptionBP option) {
	try {
		CostiDipendenteBP bp = (CostiDipendenteBP)context.getBusinessProcess();
		if (option.getOption() == it.cnr.jada.util.action.OptionBP.YES_BUTTON)
			bp.copiaMesePrecedente(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	} 
}

}