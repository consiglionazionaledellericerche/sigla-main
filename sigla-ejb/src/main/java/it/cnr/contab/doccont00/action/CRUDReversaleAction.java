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

import it.cnr.contab.docamm00.actions.EconomicaAction;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_accertamentiVBulk;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk;
import it.cnr.contab.doccont00.bp.CRUDMandatoBP;
import it.cnr.contab.doccont00.bp.CRUDReversaleBP;
import it.cnr.contab.doccont00.bp.ListaSospesiBP;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Azione che gestisce le richieste relative alla Gestione Documenti Contabili
 * (Reversale)
 */
public class CRUDReversaleAction extends EconomicaAction {
    public CRUDReversaleAction() {
        super();
    }

    /**
     * Gestisce il caricamento dei documenti passivi
     */
    //
// Gestisce la selezione del bottone "Nuova Scadenza"
//
    public Forward doAddToCRUDMain_SospesiSelezionati(ActionContext context) {

        try {
            CRUDReversaleBP bp = (CRUDReversaleBP) context.getBusinessProcess();
            it.cnr.jada.util.RemoteIterator ri = bp.cercaSospesi(context);
            ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
            if (ri == null || ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                bp.setMessage("La ricerca non ha fornito alcun risultato.");
                return context.findDefaultForward();
/*		}
		else if (ri.countElements() == 1) {
			OggettoBulk bulk = (OggettoBulk)ri.nextElement();
			if (ri instanceof javax.ejb.EJBObject)
				((javax.ejb.EJBObject)ri).remove();
			bp.setMessage("La ricerca ha fornito un solo risultato.");
			bp.edit(context,bulk);
			return context.findDefaultForward();*/
            } else {
                //		bp.setModel(context,filtro);
                BulkInfo bulkInfo = BulkInfo.getBulkInfo(SospesoBulk.class);
                ListaSospesiBP nbp = (ListaSospesiBP) context.createBusinessProcess("ListaSospesiBP");
                nbp.setColumns(bulkInfo.getColumnFieldPropertyDictionary("SospesiReversale"));
                nbp.setIterator(context, ri);
                nbp.setMultiSelection(true);
//			nbp.setBulkInfo(bulkInfo);
                context.addHookForward("seleziona", this, "doRiportaSelezioneSospesi");
                return context.addBusinessProcess(nbp);
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
	/*
	try 
	{
		CRUDAbstractMandatoBP bp = (CRUDAbstractMandatoBP)getBusinessProcess(context);
		fillModel( context );		
		bp.caricaSospesi(context);
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
	*/
    }

    /**
     * Metodo utilizzato per gestire la conferma dei documenti attivi
     * disponibili selezionati.
     */

    public Forward doAggiungiDocAttivi(ActionContext context) {
        try {
            CRUDReversaleBP bp = (CRUDReversaleBP) getBusinessProcess(context);
            fillModel(context);
            bp.aggiungiDocAttivi(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce l'annullamento del terzo
     */
    public Forward doBlankSearchFind_doc_attivi(ActionContext context, OggettoBulk reversale) {
        try {
            ((ReversaleIBulk) reversale).setDocAttiviColl(new java.util.ArrayList());
            ((ReversaleIBulk) reversale).getFind_doc_attivi().setTerzoAnag(new it.cnr.contab.anagraf00.core.bulk.V_all_anagrafico_terzoBulk());
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce la selezione di un tipo bollo
     */
    public Forward doCambiaTipoBollo(ActionContext context) {
        try {
            fillModel(context);
            CRUDReversaleBP bp = (CRUDReversaleBP) getBusinessProcess(context);
            ReversaleBulk reversale = (ReversaleBulk) bp.getModel();
            reversale.getReversale_terzo().setToBeUpdated();
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce la selezione dell'unità organizzativa
     */
    public Forward doCambiaUnitaOrganizzativa(ActionContext context) {
        try {
            fillModel(context);
            SimpleCRUDBP bp = (SimpleCRUDBP) getBusinessProcess(context);
            ReversaleIBulk reversale = (ReversaleIBulk) bp.getModel();
            reversale.setDocAttiviColl(new java.util.ArrayList());
//		reversale.setSospesiColl( new java.util.ArrayList() );
            reversale.setCd_cds(reversale.getUnita_organizzativa().getCd_unita_padre());
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce il caricamento dei documenti attivi
     */
    public Forward doCercaDocAttivi(ActionContext context) {
        try {
            CRUDReversaleBP bp = (CRUDReversaleBP) getBusinessProcess(context);
            fillModel(context);
            ReversaleIBulk reversale = (ReversaleIBulk) bp.getModel();
            if (reversale.getFind_doc_attivi().getCd_terzo() == null &&
                    reversale.getFind_doc_attivi().getCd_precedente() == null &&
                    reversale.getFind_doc_attivi().getCognome() == null &&
                    reversale.getFind_doc_attivi().getRagione_sociale() == null &&
                    reversale.getFind_doc_attivi().getNome() == null &&
                    reversale.getFind_doc_attivi().getPartita_iva() == null &&
                    reversale.getFind_doc_attivi().getCodice_fiscale() == null
            )
                throw new it.cnr.jada.comp.ApplicationException("Attenzione! Deve essere specificato almeno un campo dell'anagrafica.");

            if (reversale.getFind_doc_attivi().getTerzoAnag().getCrudStatus() == bp.getModel().UNDEFINED) {
                //doSearchFind_doc_attiviInAutomatico( context );
                it.cnr.jada.util.RemoteIterator ri = bp.find(context, null, reversale.getFind_doc_attivi().getTerzoAnag(), reversale, "find_doc_attivi.terzoAnag");
                if (ri == null || ri.countElements() == 0) {
                    it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                    bp.setMessage("Il terzo non e' presente nell'anagrafico.");
                    return context.findDefaultForward();
                } else if (ri.countElements() == 1) {
                    FormField field = getFormField(context, "main.find_doc_attivi");
                    doBringBackSearchResult(context, field, (OggettoBulk) ri.nextElement());
                    it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                } else {
                    it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                    bp.setMessage("Esite piu' di un terzo che soddisfa i criteri di ricerca.");
                    return context.findDefaultForward();
                }
            }
            bp.cercaDocAttivi(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce un comando di cancellazione.
     */
    public Forward doConfermaElimina(ActionContext context, int choice) throws java.rmi.RemoteException {
        try {
            fillModel(context);
            if (choice == OptionBP.YES_BUTTON) {
                CRUDBP bp = getBusinessProcess(context);
                CRUDReversaleBP bpr = (CRUDReversaleBP) context.getBusinessProcess();
                ReversaleIBulk rev = (ReversaleIBulk) bp.getModel();
                if (bpr.isAnnullabileEnte(context, rev))
                    return openConfirm(context, "All'annullamento della reversale seguirà la riemissione?", OptionBP.CONFIRM_YES_NO, "doConfermaRiemissione");

                bp.delete(context);
                bp.setMessage("Annullamento effettuato");
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doConfermaRiemissione(ActionContext context, int choice) throws java.rmi.RemoteException {
        try {
            fillModel(context);
            CRUDBP bp = getBusinessProcess(context);
            CRUDReversaleBP bpr = (CRUDReversaleBP) context.getBusinessProcess();
            if (choice == OptionBP.YES_BUTTON) {
                bpr.deleteRiemissione(context);
                bp.setMessage("Annullamento effettuato");
            } else {
                bp.delete(context);
                bp.setMessage("Annullamento effettuato");
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce un comando di cancellazione.
     */
    public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {
        try {
            fillModel(context);

            CRUDBP bp = getBusinessProcess(context);
            if (!bp.isDeleteButtonEnabled()) {
                bp.setMessage("Non è possibile cancellare in questo momento");
            } else {
                ReversaleIBulk reversale = (ReversaleIBulk) bp.getModel();
                if (reversale.isDipendenteDaAltroDocContabile())
                    bp.setMessage("Non è possibile annullare la reversale perchè e' stata originata da un altro doc. contabile");
                else if (reversale.getDoc_contabili_collColl().size() > 0)
                    return openConfirm(context, "All'annullamento della reversale anche i documenti contabili collegati verranno annullati. Vuoi continuare?", OptionBP.CONFIRM_YES_NO, "doConfermaElimina");
                else
                    return doConfermaElimina(context, OptionBP.YES_BUTTON);
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce la selezione dei sospesi
     */
    public Forward doRiportaSelezioneSospesi(ActionContext context) {

        try {
            CRUDReversaleBP bp = (CRUDReversaleBP) context.getBusinessProcess();
            bp.aggiungiSospesi(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }

    }

    /**
     * Metodo utilizzato per ricercare i documenti attivi in automatico.
     */
    public Forward doSearchFind_doc_attiviInAutomatico(ActionContext context) {
        try {
            CRUDReversaleBP bp = (CRUDReversaleBP) getBusinessProcess(context);
            ReversaleIBulk reversale = (ReversaleIBulk) bp.getModel();
            it.cnr.jada.util.RemoteIterator ri = bp.find(context, null, reversale.getFind_doc_attivi().getTerzoAnag(), reversale, "find_doc_attivi.terzoAnag");
            if (ri == null || ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                bp.setMessage("Il terzo non e' presente nell'anagrafico.");
                return context.findDefaultForward();
            } else if (ri.countElements() == 1) {
                FormField field = getFormField(context, "main.find_doc_attivi");
                doBringBackSearchResult(context, field, (OggettoBulk) ri.nextElement());
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                return context.findDefaultForward();
            } else {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                bp.setMessage("Esite piu' di un terzo che soddisfa i criteri di ricerca.");
                return context.findDefaultForward();
            }
        } catch (Exception e) {
            return handleException(context, e);
        }

    }

    /**
     * Associa un codice SIOPE (Reversale_siopeBulk), ad una riga di reversale (Reversale_rigaBulk).
     *
     * @param context {@link ActionContext } in uso.
     * @return Forward
     */

    public Forward doAggiungiCodiceSiope(ActionContext context) {
        try {
            fillModel(context);
            CRUDReversaleBP bp = (CRUDReversaleBP) context.getBusinessProcess();
            bp.getCodiciSiopeCollegabili().remove(context);
            bp.getCodiciSiopeCollegati().reset(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return super.handleException(context, e);
        }
    }

    /**
     * Rimuove i codici SIOPE selezionati.
     *
     * @param context {@link ActionContext } in uso.
     * @return Forward
     */

    public Forward doRimuoviCodiceSiope(ActionContext context) {
        try {
            fillModel(context);
            CRUDReversaleBP bp = (CRUDReversaleBP) context.getBusinessProcess();
            bp.getCodiciSiopeCollegati().remove(context);
            bp.getCodiciSiopeCollegabili().reset(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return super.handleException(context, e);
        }
    }

    public Forward doSalva(ActionContext context) throws RemoteException {
        try {
            fillModel(context);
            CRUDReversaleBP bp = (CRUDReversaleBP) context.getBusinessProcess();
            ReversaleBulk reversale = (ReversaleBulk) bp.getModel();

            if (bp.isSiope_attiva() && reversale.isRequiredSiope() && !reversale.isSiopeTotalmenteAssociato()) {
                if (bp.isSiopeBloccante(context)) {
                    if (!reversale.isSiopeTotalmenteAssociato()) {
                        boolean salvaConAlert = true;
                        reversale:
                        for (Iterator i = reversale.getReversale_rigaColl().iterator(); i.hasNext(); ) {
                            Reversale_rigaBulk riga = (Reversale_rigaBulk) i.next();
                            if (!riga.getTipoAssociazioneSiope().equals(Reversale_rigaBulk.SIOPE_TOTALMENTE_ASSOCIATO) &&
                                    (!riga.getReversale_siopeColl().isEmpty() || !riga.getCodici_siopeColl().isEmpty() ||
                                            riga.getEsercizio_accertamento().compareTo(riga.getEsercizio_ori_accertamento()) == 0)) {
                                salvaConAlert = false;
                                break reversale;
                            }
                        }
                        if (salvaConAlert) {
                            return openConfirm(context, "Attenzione! Alcune o tutte le righe reversali non risultano associate completamente a codici SIOPE.\n" +
                                    "Comunicare all'Ufficio competente l'associazione del corretto codice.", OptionBP.OK_BUTTON, "doConfirmSalva");
                        }
                    }
                    bp.setMessage("Attenzione! Alcune o tutte le righe reversali non risultano associate completamente a codici SIOPE. Impossibile continuare");
                    return doSelezionaRigaSiopeDaCompletare(context);
                }
                return openConfirm(context, "Attenzione! Alcune o tutte le righe della reversale non risultano associate completamente a codici SIOPE. Vuoi continuare?", OptionBP.CONFIRM_YES_NO, "doConfirmSalvaCup");
            }

            return doConfirmSalvaCup(context, OptionBP.YES_BUTTON);
        } catch (Throwable e) {
            return super.handleException(context, e);
        }
    }

    public Forward doConfirmSalva(ActionContext actioncontext, int option) {
        try {
            if (option == OptionBP.YES_BUTTON || option == OptionBP.OK_BUTTON) {
                return super.doSalva(actioncontext);
            }
            return doSelezionaRigaSiopeDaCompletare(actioncontext);
        } catch (Throwable e) {
            return handleException(actioncontext, e);
        }
    }

    public Forward doTab(ActionContext actioncontext, String s, String s1) {
        try {
            fillModel(actioncontext);
            Forward forward = super.doTab(actioncontext, s, s1);
            if (s.equals("tab") && s1.equals("tabDettaglioReversale")) {
                CRUDReversaleBP bp = (CRUDReversaleBP) actioncontext.getBusinessProcess();
                CRUDController crudcontroller = getController(actioncontext, "main.DocumentiAttiviSelezionati");
                ReversaleBulk reversale = (ReversaleBulk) bp.getModel();
                if (reversale != null &&
                        crudcontroller != null &&
                        crudcontroller.getElements().hasMoreElements() &&
                        crudcontroller.getSelection().isEmpty() &&
                        crudcontroller.getSelection().getFocus() == -1) {
                    crudcontroller.getSelection().setFocus(0);
                    return doSelection(actioncontext, "main.DocumentiAttiviSelezionati");
                }
            }
            return forward;
        } catch (Exception exception) {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doSelezionaRigaSiopeDaCompletare(ActionContext actioncontext) {
        try {
            fillModel(actioncontext);
            CRUDReversaleBP bp = (CRUDReversaleBP) actioncontext.getBusinessProcess();
            super.doTab(actioncontext, "tab", "tabDettaglioReversale");
            bp.selezionaRigaSiopeDaCompletare(actioncontext);
            return actioncontext.findDefaultForward();
        } catch (Exception exception) {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doSelezionaSiopeDaCompletare(ActionContext actioncontext) throws RemoteException {
        try {
            fillModel(actioncontext);
            CRUDReversaleBP crudbp = (CRUDReversaleBP) getBusinessProcess(actioncontext);
            crudbp.resetForSearch(actioncontext);
            OggettoBulk oggettobulk = crudbp.getModel();

            CompoundFindClause clauses = new CompoundFindClause();
            clauses.addClause("AND", "fl_associazione_siope_completa", SQLBuilder.EQUALS, false);
            clauses.addClause("AND", "stato_trasmissione", SQLBuilder.EQUALS, ReversaleBulk.STATO_TRASMISSIONE_NON_INSERITO);
            clauses.addClause("AND", "stato", SQLBuilder.NOT_EQUALS, ReversaleBulk.STATO_REVERSALE_ANNULLATO);
            clauses.addClause("AND", "ti_reversale", SQLBuilder.NOT_EQUALS, ReversaleBulk.TIPO_REGOLARIZZAZIONE);

            RemoteIterator remoteiterator = crudbp.find(actioncontext, clauses, oggettobulk);
            if (remoteiterator == null || remoteiterator.countElements() == 0) {
                EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
                crudbp.setMessage("La ricerca non ha fornito alcun risultato.");
                return actioncontext.findDefaultForward();
            }
            if (remoteiterator.countElements() == 1) {
                OggettoBulk oggettobulk1 = (OggettoBulk) remoteiterator.nextElement();
                ((ReversaleBulk) oggettobulk1).setSiopeDaCompletare(true);
                EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
                crudbp.setMessage(FormBP.INFO_MESSAGE, "La ricerca ha fornito un solo risultato.");
                return doRiportaSelezioneSiopeDaCompletare(actioncontext, oggettobulk1);
            } else {
                crudbp.setModel(actioncontext, oggettobulk);
                SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP) actioncontext.createBusinessProcess("Selezionatore");
                selezionatorelistabp.setIterator(actioncontext, remoteiterator);
                selezionatorelistabp.setBulkInfo(crudbp.getSearchBulkInfo());
                selezionatorelistabp.setColumns(getBusinessProcess(actioncontext).getSearchResultColumns());
                actioncontext.addHookForward("seleziona", this, "doRiportaSelezioneSiopeDaCompletare");
                return actioncontext.addBusinessProcess(selezionatorelistabp);
            }
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doRiportaSelezioneSiopeDaCompletare(ActionContext actioncontext) throws RemoteException {
        try {
            Forward forward = super.doRiportaSelezione(actioncontext);
            CRUDReversaleBP crudbp = (CRUDReversaleBP) getBusinessProcess(actioncontext);
            if (!crudbp.isSearching() && crudbp.getModel() != null && ((ReversaleBulk) crudbp.getModel()).getPg_reversale() != null) {
                crudbp.impostaSiopeDaCompletare(actioncontext);
                return forward;
            }
            return doNuovo(actioncontext);
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    private Forward doRiportaSelezioneSiopeDaCompletare(ActionContext actioncontext, OggettoBulk bulk) throws RemoteException {
        try {
            Forward forward = super.doRiportaSelezione(actioncontext, bulk);
            CRUDReversaleBP crudbp = (CRUDReversaleBP) getBusinessProcess(actioncontext);
            if (!crudbp.isSearching() && crudbp.getModel() != null && ((ReversaleBulk) crudbp.getModel()).getPg_reversale() != null) {
                crudbp.impostaSiopeDaCompletare(actioncontext);
                return forward;
            }
            return doNuovo(actioncontext);
        } catch (Throwable throwable) {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doConfirmSalvaCup(ActionContext actioncontext, int option) {
        try {
            if (option == OptionBP.YES_BUTTON) {
                CRUDReversaleBP bp = (CRUDReversaleBP) actioncontext.getBusinessProcess();
                ReversaleBulk reversale = (ReversaleBulk) bp.getModel();
                // reversale.isRequiredSiope() controlla che non sia una reversale di regolarizzazione
                if (bp.isCup_attivo() && reversale.isRequiredSiope()) {
                    boolean trovato = false;
                    if (reversale instanceof ReversaleIBulk) {
                        bp.getCupCollegati().validate(actioncontext);
                        for (Iterator i = reversale.getReversale_rigaColl().iterator(); i.hasNext() && !trovato; ) {
                            Reversale_rigaBulk riga = (Reversale_rigaBulk) i.next();
                            if (riga.getReversaleCupColl().isEmpty() || riga.getTipoAssociazioneCup().compareTo(Mandato_rigaBulk.SIOPE_TOTALMENTE_ASSOCIATO) != 0)
                                trovato = true;
                        }
                        if (trovato)
                            return openConfirm(actioncontext, "Attenzione! Alcune o tutte le righe della reversale non risultano associate completamente al CUP. Vuoi continuare?", OptionBP.CONFIRM_YES_NO, "doConfirmSalva");
                    }
                }
                if (!reversale.isAnnullato()) {
                    if (bp.isSiope_cup_attivo() && reversale.isRequiredSiope()) {
                        boolean trovato = false;
                        if (reversale instanceof ReversaleIBulk) {
                            bp.getSiopeCupCollegati().validate(actioncontext);
                            for (Iterator i = reversale.getReversale_rigaColl().iterator(); i.hasNext() && !trovato; ) {
                                Reversale_rigaBulk riga = (Reversale_rigaBulk) i.next();
                                for (Iterator j = riga.getReversale_siopeColl().iterator(); j.hasNext() && !trovato; ) {
                                    Reversale_siopeBulk rigaSiope = (Reversale_siopeBulk) j.next();

                                    if (rigaSiope.getReversaleSiopeCupColl().isEmpty() || rigaSiope.getTipoAssociazioneCup().compareTo(Mandato_rigaBulk.SIOPE_TOTALMENTE_ASSOCIATO) != 0)
                                        trovato = true;
                                }
                                if (trovato)
                                    return openConfirm(actioncontext, "Attenzione! Alcune o tutte le righe siope non risultano associate completamente al CUP. Vuoi continuare?", OptionBP.CONFIRM_YES_NO, "doConfirmSalva");
                            }
                        }
                    }
                }
                return doConfirmSalva(actioncontext, OptionBP.YES_BUTTON);
            }
            return doConfirmSalva(actioncontext, OptionBP.NO_BUTTON);
        } catch (Throwable e) {
            return handleException(actioncontext, e);
        }
    }

    public Forward doCambiaStatoDaVariare(ActionContext actioncontext) {
        try {
            fillModel(actioncontext);
            CRUDReversaleBP bp = (CRUDReversaleBP) actioncontext.getBusinessProcess();
            bp.impostaReversaleDaVariare(actioncontext);
            return actioncontext.findDefaultForward();
        } catch (Exception exception) {
            return handleException(actioncontext, exception);
        }
    }

    public Forward doSalvaVariazioneSostituzione(ActionContext actioncontext) throws RemoteException {
        try {
            CRUDReversaleBP bp = Optional.ofNullable(getBusinessProcess(actioncontext))
                    .filter(CRUDReversaleBP.class::isInstance)
                    .map(CRUDReversaleBP.class::cast)
                    .orElseThrow(() -> new DetailedRuntimeException("CRUDReversaleBP non trovato!"));
            return doSalva(actioncontext);
        } catch (Exception exception) {
            return handleException(actioncontext, exception);
        }
    }


    /**
     * Gestisce la ricerca degli Accertamenti
     *
     *
     * @param actionContext	L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doRicercaAccertamento(ActionContext actionContext) {
        CRUDReversaleBP bp = Optional.ofNullable(getBusinessProcess(actionContext))
                .filter(CRUDReversaleBP.class::isInstance)
                .map(CRUDReversaleBP.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("CRUDReversaleBP non trovato!"));
        try {
            fillModel(actionContext);
            List<Reversale_rigaBulk> models = bp.getDocumentiAttiviSelezionati().getSelectedModels(actionContext);
            Reversale_rigaBulk reversaleRigaBulk =
                    Optional.ofNullable(models)
                            .orElse(Collections.emptyList())
                            .stream()
                            .reduce((a, b) -> {
                                throw new IllegalStateException();
                            })
                            .get();
            return basicDoRicercaAccertamento(actionContext, reversaleRigaBulk);
        } catch (IllegalStateException | NoSuchElementException _ex) {
            bp.setErrorMessage("Per procedere, selezionare un unico dettaglio su cui cambiare l'accertamento!");
            return actionContext.findDefaultForward();
        } catch (Throwable e) {
            return handleException(actionContext, e);
        }
    }

    private Forward basicDoRicercaAccertamento(ActionContext actionContext, Reversale_rigaBulk reversaleRigaBulk) {
        try {
            //imposta il filtro per la ricerca
            Filtro_ricerca_accertamentiVBulk filtro = new Filtro_ricerca_accertamentiVBulk();
            filtro.setCliente(reversaleRigaBulk.getReversale().getTerzo());
            filtro.setIm_importo(reversaleRigaBulk.getIm_reversale_riga());
            filtro.setCd_unita_organizzativa(reversaleRigaBulk.getCd_uo_doc_amm());
            filtro.setCd_uo_origine(reversaleRigaBulk.getReversale().getCd_uo_origine());
            filtro.setFl_importo(Boolean.TRUE);
            filtro.setData_scadenziario(null);
            filtro.setFl_data_scadenziario(Boolean.FALSE);

            //richiama il filtro RicercaAccertamentiBP
            it.cnr.jada.util.action.BulkBP robp= (it.cnr.jada.util.action.BulkBP) actionContext.getUserInfo()
                    .createBusinessProcess(actionContext, "RicercaAccertamentiBP", new Object[]{"MRSWTh"});
            robp.setModel(actionContext, filtro);
            //imposta il bringback su doContabilizzaAccertamenti
            actionContext.addHookForward("bringback", this, "doContabilizzaAccertamenti");
            return actionContext.addBusinessProcess(robp);
        } catch (Throwable e) {
            return handleException(actionContext, e);
        }
    }

    public Forward doContabilizzaAccertamenti(ActionContext context) throws BusinessProcessException, ApplicationException {
        HookForward caller = (HookForward) context.getCaller();
        CRUDReversaleBP bp = Optional.ofNullable(getBusinessProcess(context))
                .filter(CRUDReversaleBP.class::isInstance)
                .map(CRUDReversaleBP.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("CRUDReversaleBP non trovato!"));
        Accertamento_scadenzarioBulk scadenza = Optional.ofNullable(caller.getParameter("accertamentoSelezionato"))
                .filter(Accertamento_scadenzarioBulk.class::isInstance)
                .map(Accertamento_scadenzarioBulk.class::cast)
                .orElseThrow(() -> new ApplicationException("Selezionare un accertamento da associare!"));
        try {
            List<Reversale_rigaBulk> models = bp.getDocumentiAttiviSelezionati().getSelectedModels(context);
            Reversale_rigaBulk reversaleRigaBulk =
                    Optional.ofNullable(models)
                            .orElse(Collections.emptyList())
                            .stream()
                            .reduce((a, b) -> {
                                throw new IllegalStateException();
                            })
                            .get();
            bp.cambiaAccertamentoScadenzario(context, reversaleRigaBulk, scadenza);
        } catch (Throwable e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }
}
