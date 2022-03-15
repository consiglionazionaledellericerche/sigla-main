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

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.V_voce_f_partita_giroBulk;
import it.cnr.contab.doccont00.bp.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.prevent00.bulk.Pdg_vincoloBulk;
import it.cnr.contab.prevent00.bulk.V_assestatoBulk;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.BulkCollections;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.StrServ;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.OptionBP;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Collection;

/**
 * Azione che gestisce le richieste relative alla Gestione Documenti Contabili
 * (Accertamento)
 */
public class CRUDAccertamentoAction extends CRUDAbstractAccertamentoAction {
    private WorkpackageBulk vecchiaLA;
    private int indiceVecchiaSelezione;

    public CRUDAccertamentoAction() {
        super();
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward creaLineaAttivita(ActionContext context) {
        try {
            it.cnr.jada.util.action.SimpleCRUDBP lineaAttivitaBP = (it.cnr.jada.util.action.SimpleCRUDBP) context.getUserInfo().createBusinessProcess(context, "CRUDLinea_attivitaBP", new Object[]{"MR"});
            context.addHookForward("bringback", this, "doBringBackLineaAttivita");
            context.addHookForward("close", this, "doBringBackLineaAttivita");
            HookForward hook = (HookForward) context.findForward("bringback");

            return context.addBusinessProcess(lineaAttivitaBP);
        } catch (Exception ex) {
            return handleException(context, ex);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di cancellazione dal controller "scadenzario"
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doAddToCRUDMain_Scadenzario(ActionContext context) {
        try {
            // Visualizzo il Tab della scadenza
            super.doTab(context, "tabScadenzario", "tabScadenza");

            ((CRUDAccertamentoBP) getBusinessProcess(context)).addScadenza(context);
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di azzeramento del searchtool "find_capitolo"
     *
     * @param context      L'ActionContext della richiesta
     * @param accertamento L'OggettoBulk padre del searchtool
     * @return Il Forward alla pagina di risposta
     */
    public Forward doBlankSearchFind_capitolo(ActionContext context, AccertamentoBulk accertamento) {
        try {
            //accertamento.setLinee_attivitaColl(new java.util.Vector());
            accertamento.setLineeAttivitaColl(new java.util.Vector());
            accertamento.setCapitolo(new it.cnr.contab.config00.pdcfin.bulk.V_voce_f_partita_giroBulk());
            accertamento.setCd_elemento_voce(null);

            // Deseleziono l'eventuale selezione della linea di attivita' altrimenti
            // la tiene in memoria
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
            bp.getLineeDiAttivita().reset(context);
            bp.annullaImputazioneFinanziariaCapitoli(context);
            accertamento.setCapitolo(new V_voce_f_partita_giroBulk());

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doBlankSearchFind_elemento_voce(ActionContext context, AccertamentoBulk accertamento) {
        return doBlankSearchFind_capitolo(context, accertamento);
    }

    public Forward doBlankSearchFind_debitore(ActionContext context, AccertamentoBulk accertamento) {
        try {
            accertamento.setDebitore(new TerzoBulk());
            accertamento.getDebitore().setAnagrafico(new AnagraficoBulk());

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce la validazione di nuovo terzo creato
     *
     * @param context      <code>ActionContext</code> in uso.
     * @param accertamento Oggetto di tipo <code>AccertamentoBulk</code>
     * @param terzo        Oggetto di tipo <code>TerzoBulk</code> che rappresenta il nuovo terzo creato
     * @return <code>Forward</code>
     */
    public Forward doBringBackCRUDCrea_debitore(ActionContext context, AccertamentoBulk accertamento, TerzoBulk terzo) {
        try {
            if (terzo != null) {
                accertamento.validateTerzo(terzo);
                accertamento.setDebitore(terzo);
            }
            return context.findDefaultForward();
        } catch (ValidationException e) {
            getBusinessProcess(context).setErrorMessage(e.getMessage());
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce la validazione di nuovo contratto creato
     *
     * @param context      <code>ActionContext</code> in uso.
     * @param accertamento Oggetto di tipo <code>AccertamentoBulk</code>
     * @param contratto    Oggetto di tipo <code>ContrattoBulk</code> che rappresenta il nuovo contratto creato
     * @return <code>Forward</code>
     */
    public Forward doBringBackCRUDFind_contratto(ActionContext context, AccertamentoBulk accertamento, ContrattoBulk contratto) {
        try {
            if (contratto != null) {
                CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);

                bp.validaContratto(context, contratto);
                accertamento.setContratto(contratto);
            }
            return doBringBackSearchFind_contratto(context, accertamento, contratto);
        } catch (it.cnr.jada.action.MessageToUser e) {
            getBusinessProcess(context).setErrorMessage(e.getMessage());
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doBringBackLineaAttivita(ActionContext context) {
        HookForward caller = (HookForward) context.getCaller();
        it.cnr.contab.config00.latt.bulk.WorkpackageBulk nuovaLA =
                (it.cnr.contab.config00.latt.bulk.WorkpackageBulk) caller.getParameter(
                        "bringback");

        if (nuovaLA == null)
            return context.findDefaultForward();

        CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
        AccertamentoBulk accertamento = (AccertamentoBulk) bp.getModel();

        try {
            bp.validaLineaAttivita(context, nuovaLA);
            WorkpackageBulk aLAselezionata = (WorkpackageBulk) bp.getLineeDiAttivita().getModel();

            // Salvo il numero delle linee di attivita prima del nuovo caricamento
            int size = accertamento.getLineeAttivitaColl().size();

            bp.caricaLineeAttivita(context);

            // Se il nuovo numero delle linee di attivita caricate non e' maggiore
            // di quello prima del caricamento significa che la linea di attivita'
            // appena creata non e' eleggibile
            if (accertamento.getLineeAttivitaColl().size() >= size)
                setMessage(context, 0, "GAE riportato correttamente !");
            else
                setMessage(context, 0, "GAE non eleggibile !");

            if (aLAselezionata == null) {
                // Non era stata selezionata alcuna linea di attivita'
                //accertamento.setLinea_attivita(null);
                return context.findDefaultForward();
            }

            // Riseleziono la Linea di attivita' selezionata prima dell'apertura della
            //finestra delle linee di attivita0

            int index = BulkCollections.indexOfByPrimaryKey(bp.getLineeDiAttivita().getDetails(), aLAselezionata);
            if (index < 0)
                setMessage(context, 0, "Il GAE " + aLAselezionata.getCd_linea_attivita() + "' non e' piu' eleggibile !");
            bp.getLineeDiAttivita().setModelIndex(context, index);
            //accertamento.setLinea_attivita((WorkpackageBulk) bp.getLineeDiAttivita().getModel());
		/*
        int index = BulkCollections.indexOfByPrimaryKey(bp.getLineeDiAttivita().getDetails(), nuovaLA );
        if (index < 0)
            setMessage(context, 0, "Il GAE " + aLAselezionata.getCd_linea_attivita() + "' non e' piu' eleggibile !");
        bp.getLineeDiAttivita().setModelIndex(context, index);
        accertamento.setLinea_attivita((Linea_attivitaBulk) bp.getLineeDiAttivita().getModel());
		*/
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di ricerca del searchtool "find_capitolo"
     *
     * @param context      L'ActionContext della richiesta
     * @param accertamento L'OggettoBulk padre del searchtool
     * @param capitolo     L'OggettoBulk selezionato dall'utente
     * @return Il Forward alla pagina di risposta
     */
    public Forward doBringBackSearchFind_capitolo(ActionContext context, AccertamentoBulk accertamento, it.cnr.contab.config00.pdcfin.bulk.V_voce_f_partita_giroBulk capitolo) {
        try {
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);


            if ((capitolo != null) && (capitolo.getCd_voce() != null)) {
                accertamento.setCapitolo(capitolo);
                accertamento.setCd_elemento_voce(capitolo.getCd_titolo_capitolo());
                return doCaricaCentriDiResponsabilita(context);
            }

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackSearchFind_elemento_voce(ActionContext context, AccertamentoBulk accertamento, it.cnr.contab.config00.pdcfin.bulk.V_voce_f_partita_giroBulk capitolo) {
        return doBringBackSearchFind_capitolo(context, accertamento, capitolo);
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doConfermaScadenza(ActionContext context) {
        try {
            fillModel(context);
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);

            bp.confermaScadenza(context);
		
/*		Visualizzo il Tab dei dettagli appena creati / modificati

		int index = bp.getScadenzario().getModelIndex();
		bp.getScadenzario().setModelIndex(-1);		
		bp.resyncChildren();
		super.doTab(context, "tabScadenzario", "tabDettaglioScadenza");
		bp.getScadenzario().setModelIndex(index);
*/
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param context L'ActionContext della richiesta
     * @param option
     * @return Il Forward alla pagina di risposta
     */
    public Forward doConfirmRigenerazioneDettagli(ActionContext context, int option) {
        try {
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
            AccertamentoBulk accertamento = (AccertamentoBulk) bp.getModel();

            if (option == OptionBP.YES_BUTTON) {
                bp.gestisciDettagliScadenzePerCambioLA(context.getUserContext(), context);
            }
            // Annullo la nuova selezione perche' l'utente ha deciso di non rigenerare i
            // dettagli delle scadenze
            if (option == OptionBP.NO_BUTTON) {
                // Ripristino l'oggetto LineAttivita' selezionato in precedenza
                //accertamento.setLinea_attivita(getVecchiaLA());
                // Ripristino la vecchia selezione della LineaAttivita'
                bp.getLineeDiAttivita().setModelIndex(context, getIndiceVecchiaSelezione());
            }

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }

    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doEditaScadenza(ActionContext context) {
        try {
            fillModel(context);
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
            bp.editaScadenza(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }
//
// Gestisce cancellazione logica (storno) dell'accertamento
//

    public Forward doElimina(ActionContext context) {
        try {
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
            bp.eliminaLogicamenteAccertamento(context);

            setMessage(context, 0, "Cancellazione effettuata!");

            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    //
//	Se seleziono il bottone di Ricerca e ho una scadenza in
//	stato di Edit devo prima riabilitare i campi
//
    public Forward doNuovaRicerca(ActionContext context) {
        CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
	/*
	if(bp.isEditingScadenza())
		doUndoScadenza(context);
	*/
        return super.doNuovaRicerca(context);
    }
//
// Se quando sono nella Tab delle scadenze seleziono il bottone nuovo
// visualizzo la prima pagina in modo che ci sia l'obbligo di selezionare
// della Linea di Attivita'
//

    public Forward doNuovo(ActionContext context) {
//	super.doTab(context, "tab", "tabAccertamento");
        return (super.doNuovo(context));
    }

    //
//	Se seleziono il bottone di Ricerca Libera e ho una scadenza in
//	stato di Edit devo prima riabilitare i campi
//
    public Forward doRicercaLibera(ActionContext context) {
        CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
	
	/* 
		if(bp.isEditingScadenza())
			doUndoScadenza(context);
	*/
        return super.doRicercaLibera(context);
    }

    public Forward doSalva(ActionContext context) throws java.rmi.RemoteException {
        try {
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);

            StringBuffer errControllo = new StringBuffer();
            fillModel(context);
            bp.validate(context);

            // in questo caso l'acc. modifica è stata già effettuata
            if (((AccertamentoBulk) bp.getModel()).isCheckDisponibilitaContrattoEseguito())
                return super.doSalva(context);

            if (bp instanceof CRUDAccertamentoResiduoBP) {
                if (bp.modificaAccertamentoResProprie(context, errControllo)) {
                    if (!errControllo.toString().equals("")) {

                        String message = errControllo + "." +
                                "Si vuole creare un movimento di modifica dell'accertamento residuo?";
                        openConfirm(context, message, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doModificaConfermata");

                        return context.findDefaultForward();
                    }
                } else {
                    // altrimenti cancello l'eventuale modifica precedente
                    if (((AccertamentoBulk) bp.getModel()).isAccertamentoResiduo()) {
                        AccertamentoResiduoBulk accertamento = (AccertamentoResiduoBulk) bp.getModel();
                        if (accertamento.getAccertamento_modifica() != null && accertamento.getAccertamento_modifica().getPg_modifica() != null) {
                            ((CRUDAccertamentoResiduoBP) bp).cancellaAccertamentoModTemporanea(context, accertamento.getAccertamento_modifica());
                            accertamento.setAccertamento_modifica(new Accertamento_modificaBulk());
                        }
                    }
                }
            }
        } catch (Exception e) {
            return handleException(context, e);
        }
        return super.doSalva(context);
    }

    //
//	Prima di riportare l'oggetto riordino le scadenze e riseleziono la
//	scadenza da riportare (selezionata prima dell'ordinamento).
//	Metodo implementato per evitare che la query che va a rileggere le scadenze, 
//	che	e' ordinata per data, disallinei la selezione della scadenza da riportare
//
    public Forward doRiporta(ActionContext context) {
        CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
        AccertamentoBulk accertamento = (AccertamentoBulk) bp.getModel();

        if ((accertamento.getAccertamento_scadenzarioColl() == null) ||
                (accertamento.getAccertamento_scadenzarioColl().isEmpty()))
            return doRiportaCondizionato(context);

        Accertamento_scadenzarioBulk scadSelezionata = (Accertamento_scadenzarioBulk) bp.getScadenzario().getModel();

        if (scadSelezionata == null)
            return doRiportaCondizionato(context);

        accertamento.setAccertamento_scadenzarioColl(scadSelezionata.ordinaPerDataScadenza(accertamento.getAccertamento_scadenzarioColl()));

        /* simona 10.5.2002 l'indexOfPrimaryKey non funziona sulle scadenze nuove che non hanno la schiave valorizzata */
        //   int index = BulkCollections.indexOfByPrimaryKey(bp.getScadenzario().getDetails(),scadSelezionata);

        int index = -1;
        java.util.ListIterator e = bp.getScadenzario().getDetails().listIterator();
        while (e.hasNext())
            if (scadSelezionata.getPg_accertamento_scadenzario().longValue() == ((Accertamento_scadenzarioBulk) e.next()).getPg_accertamento_scadenzario().longValue())
                index = e.previousIndex();

        bp.getScadenzario().setModelIndex(context, index);

        return doRiportaCondizionato(context);
    }

    public Forward doRiportaCondizionato(ActionContext context) {
        CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);

        try {

            StringBuffer errControllo = new StringBuffer();
            fillModel(context);
            bp.validate(context);

            // in questo caso l'acc. modifica è stata già effettuata
            if (((AccertamentoBulk) bp.getModel()).isCheckDisponibilitaContrattoEseguito())
                return super.doRiporta(context);

            if (bp.modificaAccertamentoResProprie(context, errControllo)) {
                if (!errControllo.toString().equals("")) {

                    String message = errControllo + "." +
                            "Si vuole creare un movimento di modifica dell'accertamento residuo?";
                    openConfirm(context, message, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doModificaConfermataRiporta");

                    return context.findDefaultForward();
                }
            } else {
                // altrimenti cancello l'eventuale modifica precedente
                if (((AccertamentoBulk) bp.getModel()).isAccertamentoResiduo()) {
                    AccertamentoResiduoBulk accertamento = (AccertamentoResiduoBulk) bp.getModel();
                    if (accertamento.getAccertamento_modifica() != null && accertamento.getAccertamento_modifica().getPg_modifica() != null) {
                        ((CRUDAccertamentoResiduoBP) bp).cancellaAccertamentoModTemporanea(context, accertamento.getAccertamento_modifica());
                        accertamento.setAccertamento_modifica(new Accertamento_modificaBulk());
                    }
                }
            }

        } catch (Exception e1) {
            return handleException(context, e1);
        }
        return super.doRiporta(context);
    }

    /**
     * <!-- @TODO: da completare -->
     * Gestisce una richiesta di selezione dal controller "lineeDiAttivita"
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
/*
public Forward doSelectLineeDiAttivita(ActionContext context) 
{
	try  
	{
		CRUDAccertamentoBP bp = (CRUDAccertamentoBP)getBusinessProcess(context);
		
		if(!bp.isEditable())
			return context.findDefaultForward();

		AccertamentoBulk accertamento = (AccertamentoBulk) bp.getModel();

		// Se l'accertamento e' stato cancellato non ammetto la selezione
		if(accertamento.getDt_cancellazione() != null)
			throw new it.cnr.jada.comp.ApplicationException("Selezione non valida! L'accertamento risulta cancellato.");
			
		// Salvo l'indice e l'oggetto LineAttivita' della precedente selezione
		setIndiceVecchiaSelezione(bp.getLineeDiAttivita().getModelIndex());
		setVecchiaLA((WorkpackageBulk) bp.getLineeDiAttivita().getModel());
		
		// Salvo l'oggetto LineAttivita' appena selezionato		
		bp.getLineeDiAttivita().setSelection(context);			
		accertamento.setLinea_attivita((WorkpackageBulk) bp.getLineeDiAttivita().getModel());
		
		// Non sono ancora state inserite le scadenze
		if (accertamento.getAccertamento_scadenzarioColl().size() == 0 )
			return context.findDefaultForward();
		
		Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) accertamento.getAccertamento_scadenzarioColl().get(0);
		if(scadenza.getAccertamento_scad_voceColl().size() == 0 )
			return context.findDefaultForward();
			
		Accertamento_scad_voceBulk dettaglio = (Accertamento_scad_voceBulk) scadenza.getAccertamento_scad_voceColl().get(0);
		if( dettaglio != null && 
			(!dettaglio.getCd_linea_attivita().equals( accertamento.getLinea_attivita().getCd_linea_attivita()) ||
          !dettaglio.getCd_centro_responsabilita().equals( accertamento.getLinea_attivita().getCd_centro_responsabilita())))
		{
			// Alla selezione di una Linea di attivita' diversa chiedo se l'utente 
			// vuole proseguire. In caso affermativo devo rigenerare i dettagli 
			// delle scadenze gia' esistenti			
			return openConfirm(context,"I dettagli delle scadenze verranno rigenerati. Vuoi continuare?",it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO,"doConfirmRigenerazioneDettagli");
		}
		
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
*/
//
// Se sono in modalita' Edit e tabulo dalla pagina della testata dell'accertamento 
// (prima pagina) devo verificare che la linea di attivita' sia stata selezionata
// e che sia stata valorizzata anche la data di registrazione (serve per i controlli
// sulle date delle scadenze)
//
    public Forward doTab(ActionContext context, String tabName, String pageName) {
        try {
            fillModel(context);
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
            AccertamentoBulk accertamento = (AccertamentoBulk) bp.getModel();

            if (bp.isEditable()) {
                if (bp.getTab(tabName).equalsIgnoreCase("tabAccertamento")) {
                    bp.getModel().validate();
                    if (((AccertamentoBulk) bp.getModel()).getCapitolo().getCrudStatus() == bp.getModel().UNDEFINED)
                        doSearch(context, "main.find_capitolo");
                    if (bp.getMessage() != null) {
                        bp.setMessage("La ricerca del Capitolo non ha fornito alcun risultato.");
                        return context.findDefaultForward();
                    }
                    // MITODO - verificare se è necessario validare la testata
                    //bp.verificaTestataAccertamento( context );

                    // Visualizzo il Tab della scadenza
                    super.doTab(context, "tabScadenzario", "tabScadenza");

                    if (accertamento.getDt_registrazione() == null) {
                        setMessage(context, 0, "Valorizzare la data di registrazione !");
                        return context.findDefaultForward();
                    }
                } else if (bp.getTab(tabName).equalsIgnoreCase("tabImputazioneFin")) {
                    //bp.getModel().validate();
                    if (((AccertamentoBulk) bp.getModel()).getInternalStatus() == AccertamentoBulk.INT_STATO_CDR_CONFERMATI) {
                        OptionBP option = openConfirm(context, "Le linee di attività non sono state confermate. Si intende proseguire?", OptionBP.CONFIRM_YES_NO, "doConfirmTabImputazioneFin");
                        option.addAttribute("tabName", tabName);
                        option.addAttribute("pageName", pageName);
                        return option;
                    }
                }
            }
            Forward frw = super.doTab(context, tabName, pageName);
            if (bp instanceof CRUDAccertamentoResiduoBP) ((CRUDAccertamentoResiduoBP) bp).setStatusAndEditableMap();
            return frw;
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doUndoScadenza(ActionContext context) {
        try {
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
            bp.undoScadenza(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Metodo utilizzato per gestire la conferma dell'inserimento/modifica di una obbligazione che ha sfondato
     * la disponibilità per il contratto
     *
     * @param context <code>ActionContext</code> in uso.
     * @param option  Esito della risposta alla richiesta di sfondamento
     * @return <code>Forward</code>
     * @throws <code>RemoteException</code>
     */

    public Forward doOnCheckDisponibilitaContrattoFailed(ActionContext context, int option) {

        if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) {
            try {
                CRUDBP bp = getBusinessProcess(context);
                ((AccertamentoBulk) bp.getModel()).setCheckDisponibilitaContrattoEseguito(true);
                if (bp.isBringBack())
                    return doConfermaRiporta(context, option);
                else
                    doSalva(context);
                ((AccertamentoBulk) bp.getModel()).setCheckDisponibilitaContrattoEseguito(false);
            } catch (Throwable e) {
                return handleException(context, e);
            }
        }
        return context.findDefaultForward();
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'indiceVecchiaSelezione'
     *
     * @return Il valore della proprietà 'indiceVecchiaSelezione'
     */
    public int getIndiceVecchiaSelezione() {
        return indiceVecchiaSelezione;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'indiceVecchiaSelezione'
     *
     * @param newIndiceVecchiaSelezione Il valore da assegnare a 'indiceVecchiaSelezione'
     */
    public void setIndiceVecchiaSelezione(int newIndiceVecchiaSelezione) {
        indiceVecchiaSelezione = newIndiceVecchiaSelezione;
    }

    /**
     * @return it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
     */
    public it.cnr.contab.config00.latt.bulk.WorkpackageBulk getVecchiaLA() {
        return vecchiaLA;
    }

    /**
     * @param newVecchiaLA it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
     */
    public void setVecchiaLA(it.cnr.contab.config00.latt.bulk.WorkpackageBulk newVecchiaLA) {
        vecchiaLA = newVecchiaLA;
    }

    /**
     * Gestisce un HookForward di ritorno dalla conferma del caricamento Cdr
     *
     * @param context <code>ActionContext</code> in uso.
     * @param option  Esito della risposta alla richiesta di conferma
     * @return <code>Forward</code>
     */
    public Forward doConfirmCaricaCentriDiResponsabilita(ActionContext context, int option) {
        try {
            if (option == OptionBP.YES_BUTTON) {
                CRUDAccertamentoBP bp = (CRUDAccertamentoBP) context.getBusinessProcess();
                bp.caricaCentriDiResponsabilita(context);
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }

    }

    /**
     * Gestisce un HookForward di ritorno dalla conferma del caricamento Cdr
     *
     * @param context <code>ActionContext</code> in uso.
     * @param option  Esito della risposta alla richiesta di conferma
     * @return <code>Forward</code>
     */
    public Forward doConfirmCaricaLineeAttivita(ActionContext context, int option) {
        try {
            if (option == OptionBP.YES_BUTTON) {
                CRUDAccertamentoBP bp = (CRUDAccertamentoBP) context.getBusinessProcess();
                bp.caricaLineeAttivita(context);
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }

    }

    /**
     * Gestisce un HookForward di ritorno dalla conferma del caricamento Cdr
     *
     * @param context <code>ActionContext</code> in uso.
     * @param option  Esito della risposta alla richiesta di conferma
     * @return <code>Forward</code>
     */
    public Forward doConfirmConfermaLineeAttivita(ActionContext context, int option) {
        try {
            if (option == OptionBP.YES_BUTTON) {
                CRUDAccertamentoBP bp = (CRUDAccertamentoBP) context.getBusinessProcess();
                bp.confermaLineeAttivita(context);
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }

    }

    /**
     * Gestisce la conferma dei dati di testata dell'Accertamento
     *
     * @param context <code>ActionContext</code> in uso.
     * @return <code>Forward</code>
     */
    public Forward doCaricaCentriDiResponsabilita(ActionContext context) {
        try {
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
            Collection capitoliIniziali = ((AccertamentoBulk) bp.getModel()).getCapitoliDiEntrataCdsSelezionatiColl();
            fillModel(context);
            if (bp.isDirty() && capitoliIniziali.size() > 0)
                return openConfirm(context, "Attenzione l'imputazione finanziaria corrente verrà persa. Vuoi continuare?", OptionBP.CONFIRM_YES_NO, "doConfirmCaricaCentriDiResponsabilita");
            return doConfirmCaricaCentriDiResponsabilita(context, OptionBP.YES_BUTTON);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce il caricamento delle linee di attivita
     *
     * @param context <code>ActionContext</code> in uso.
     * @return <code>Forward</code>
     */
    public Forward doCaricaLineeAttivita(ActionContext context) {
        try {
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
            Collection cdrIniziali = ((AccertamentoBulk) bp.getModel()).getCdrSelezionatiColl();
            fillModel(context);
            if (bp.isDirty() && cdrIniziali.size() > 0)
                return openConfirm(context, "Attenzione l'imputazione finanziaria corrente verrà persa. Vuoi continuare?", OptionBP.CONFIRM_YES_NO, "doConfirmCaricaLineeAttivita");
            return doConfirmCaricaLineeAttivita(context, OptionBP.YES_BUTTON);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce la conferma delle linee di attivita
     *
     * @param context <code>ActionContext</code> in uso.
     * @return <code>Forward</code>
     */
    public Forward doConfermaLineeAttivita(ActionContext context) {
        try {
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
            fillModel(context);
            AccertamentoBulk accertamento = (AccertamentoBulk) bp.getModel();
            if (bp.isDirty() && accertamento.hasDettagli())
                return openConfirm(context, "Attenzione i dettagli delle scadenze saranno persi. Vuoi continuare?", OptionBP.CONFIRM_YES_NO, "doConfirmConfermaLineeAttivita");
            return doConfirmConfermaLineeAttivita(context, OptionBP.YES_BUTTON);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doConfirmTabImputazioneFin(ActionContext context, OptionBP option) {
        try {
            if (option.getOption() == OptionBP.YES_BUTTON) {
                super.doTab(context, (String) option.getAttribute("tabName"), (String) option.getAttribute("pageName"));
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }

    }

    /**
     * Gestisce il cambio del flag imputazione finanziaria automatica o manuale
     *
     * @param context <code>ActionContext</code> in uso.
     * @return <code>Forward</code>
     */
    public Forward doCambiaFl_calcolo_automatico(ActionContext context) {
        try {
            fillModel(context);
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
            bp.cambiaFl_calcolo_automatico(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce il caricamento delle nuove linee di attività
     *
     * @param context   <code>ActionContext</code> in uso.
     * @return <code>Forward</code>
     */
    public Forward doBringBackCRUDCrea_linea_attivita(ActionContext context) {
        try {
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) context.getBusinessProcess();
            it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk nuovaLatt = (it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk) bp.getNuoveLineeDiAttivita().getModel();
            HookForward caller = (HookForward) context.getCaller();
            it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt = (it.cnr.contab.config00.latt.bulk.WorkpackageBulk) caller.getParameter("bringback");
            ;

            ((CRUDAccertamentoBP) getBusinessProcess(context)).validaNuovaLineaAttivita(context, nuovaLatt, latt);
            return context.findDefaultForward();
        } catch (ValidationException e) {
            getBusinessProcess(context).setErrorMessage(e.getMessage());
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce il caricamento delle nuove linee di attività
     *
     * @param context   <code>ActionContext</code> in uso.
     * @param nuovaLatt Oggetto di tipo <code>Linea_attivitaBulk</code> (istanza doc contabili)
     * @param latt      Oggetto di tipo <code>Linea_attivitaBulk</code>
     * @return <code>Forward</code>
     */
    public Forward doBringBackCRUDCrea_linea_attivita(ActionContext context, it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk nuovaLatt, it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt) {
        try {
            ((CRUDAccertamentoBP) getBusinessProcess(context)).validaNuovaLineaAttivita(context, nuovaLatt, latt);
            return context.findDefaultForward();
        } catch (ValidationException e) {
            getBusinessProcess(context).setErrorMessage(e.getMessage());
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackSearchFind_contratto(ActionContext context, AccertamentoBulk accertamento, ContrattoBulk contratto) {
        try {
            if (contratto != null) {
                if (accertamento.getDebitore() == null || (accertamento.getDebitore() != null && accertamento.getDebitore().getCd_terzo() == null))
                    accertamento.setDebitore(contratto.getFigura_giuridica_esterna());
                if (accertamento.getDs_accertamento() == null)
                    accertamento.setDs_accertamento(contratto.getOggetto());
                accertamento.setContratto(contratto);
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doInsertPgAccertamento(ActionContext context) {
        try {
            fillModel(context);
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
            AccertamentoBulk accertamento = (AccertamentoBulk) bp.getModel();

            /* se trattasi di Accertamento Residuo si antepone al numero dell'accertamento l'anno originale*/
            if (accertamento.isAccertamentoResiduo()) {
                /*N.B.: per favorire l'inerimento degli accertamenti residui a 3 nuovi CDS (110, 111, 112), e tenuto
                 * conto del fatto che la numerazione è univoca per CDS 999 è stato deciso di anteporre al numero
                 * dell'accertamento residuo (subito dopo l'anno) un numeretto identificativo del CDS.
                 * Tale gestione dovrà essere assolutamente eliminata a regime.
                 */
                Integer lungNumacc = new Integer(AccertamentoResiduoBulk.LUNGHEZZA_NUMERO_ACCERTAMENTO);
                Integer firstNumber = new Integer(0);

                if (accertamento.getCd_cds_origine() != null) {
                    if (accertamento.getCd_cds_origine().equals("110") || accertamento.getCd_cds_origine().equals("111") ||
                            accertamento.getCd_cds_origine().equals("112")) {
                        lungNumacc = new Integer(AccertamentoResiduoBulk.LUNGHEZZA_NUMERO_ACCERTAMENTO - 1);
                        if (accertamento.getCd_cds_origine().equals("110"))
                            firstNumber = new Integer(9);
                        if (accertamento.getCd_cds_origine().equals("111"))
                            firstNumber = new Integer(8);
                        if (accertamento.getCd_cds_origine().equals("112"))
                            firstNumber = new Integer(7);
                    }
                }

                if (accertamento.getEsercizio_originale() == null) {
                    accertamento.setPg_accertamento(null);
                    bp.setMessage("Occorre indicare l'anno dell'accertamento prima del numero.");
                } else if (accertamento.getEsercizio_originale().compareTo(accertamento.getEsercizio()) != -1) {
                    accertamento.setPg_accertamento(null);
                    bp.setMessage("L'esercizio dell'accertamento residuo deve essere inferiore al " + accertamento.getEsercizio());
                } else if (accertamento.getPg_accertamento() != null) {
//				if (accertamento.getPg_accertamento().toString().length()>AccertamentoResiduoBulk.LUNGHEZZA_NUMERO_ACCERTAMENTO) {
                    //bp.setMessage("Attenzione! Il numero dell'accertamento residuo deve essere al massimo di " + AccertamentoResiduoBulk.LUNGHEZZA_NUMERO_ACCERTAMENTO + " caratteri.");
                    if (accertamento.getPg_accertamento().toString().length() > lungNumacc.intValue()) {
                        bp.setMessage("Attenzione! Il numero dell'accertamento residuo deve essere al massimo di " + lungNumacc.intValue() + " caratteri.");
                        accertamento.setPg_accertamento(null);
                    } else if (lungNumacc.equals(new Integer(AccertamentoResiduoBulk.LUNGHEZZA_NUMERO_ACCERTAMENTO)))
                        accertamento.setPg_accertamento(new Long(accertamento.getEsercizio_originale() + StrServ.lpad(accertamento.getPg_accertamento().toString(), lungNumacc.intValue(), "0")));
                    else
                        accertamento.setPg_accertamento(new Long(accertamento.getEsercizio_originale() + firstNumber.toString() + StrServ.lpad(accertamento.getPg_accertamento().toString(), lungNumacc.intValue(), "0")));
                }
            }
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doModificaConfermata(ActionContext context, int opt) throws RemoteException {
        try {
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
            AccertamentoBulk accertamento = (AccertamentoBulk) bp.getModel();
            boolean viewMode = bp.isViewing();
            //String status = viewMode ?"V":"M";
            String status = "M";
            CRUDAccertamentoModificaBP newbp = null;
            if (opt == OptionBP.YES_BUTTON) {
                // controlliamo prima che abbia l'accesso al BP
                // per dare un messaggio più preciso
                String mode = it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().validaBPPerUtente(context.getUserContext(), ((CNRUserInfo) context.getUserInfo()).getUtente(), ((CNRUserInfo) context.getUserInfo()).getUtente().isUtenteComune() ? ((CNRUserInfo) context.getUserInfo()).getUnita_organizzativa().getCd_unita_organizzativa() : "*", "CRUDAccertamentoModificaBP");
                if (mode == null)
                    throw new it.cnr.jada.action.MessageToUser("Accesso non consentito alla mappa di creazione delle modifiche\nagli accertamenti residui. Impossibile continuare.");

                newbp = (CRUDAccertamentoModificaBP) context.getUserInfo().createBusinessProcess(context, "CRUDAccertamentoModificaBP", new Object[]{status + "RSWTh", accertamento, CRUDAccertamentoModificaBP.TIPO_ACCESSO_MODIFICA});
                context.addHookForward("bringback", this, "doBringBackAccertamentiModificaWindow");
                //context.addHookForward("close",this,"doBringBackAccertamentiModificaWindow");
                //HookForward hook = (HookForward)context.findForward("bringback");
                return context.addBusinessProcess(newbp);
            }
        } catch (Exception e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }

    public Forward doModificaConfermataRiporta(ActionContext context, int opt) throws RemoteException {
        try {
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
            AccertamentoBulk accertamento = (AccertamentoBulk) bp.getModel();
            boolean viewMode = bp.isViewing();
            //String status = viewMode ?"V":"M";
            String status = "M";
            CRUDAccertamentoModificaBP newbp = null;
            if (opt == OptionBP.YES_BUTTON) {
                // controlliamo prima che abbia l'accesso al BP
                // per dare un messaggio più preciso
                String mode = it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().validaBPPerUtente(context.getUserContext(), ((CNRUserInfo) context.getUserInfo()).getUtente(), ((CNRUserInfo) context.getUserInfo()).getUtente().isUtenteComune() ? ((CNRUserInfo) context.getUserInfo()).getUnita_organizzativa().getCd_unita_organizzativa() : "*", "CRUDAccertamentoModificaBP");
                if (mode == null)
                    throw new it.cnr.jada.action.MessageToUser("Accesso non consentito alla mappa di creazione delle modifiche\nagli impegni residui. Impossibile continuare.");

                newbp = (CRUDAccertamentoModificaBP) context.getUserInfo().createBusinessProcess(context, "CRUDAccertamentoModificaBP", new Object[]{status + "RSWTh", accertamento, CRUDAccertamentoModificaBP.TIPO_ACCESSO_MODIFICA});
                context.addHookForward("bringback", this, "doBringBackAccertamentiModificaRiportaWindow");
                //context.addHookForward("close",this,"doBringBackAccertamentiModificaWindow");
                //HookForward hook = (HookForward)context.findForward("bringback");
                return context.addBusinessProcess(newbp);
            }
        } catch (Exception e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }

    public Forward doBringBackAccertamentiModificaWindow(ActionContext context) {

        try {
            CRUDAccertamentoResiduoBP bp = (CRUDAccertamentoResiduoBP) getBusinessProcess(context);
            AccertamentoResiduoBulk accertamento = (AccertamentoResiduoBulk) bp.getModel();
            HookForward caller = (HookForward) context.getCaller();
            Accertamento_modificaBulk obbMod = (Accertamento_modificaBulk) caller.getParameter("bringback");
            if (obbMod != null) {
                // cancello l'eventuale modifica temporanea precedente inserita
                if (accertamento.getAccertamento_modifica() != null && accertamento.getAccertamento_modifica().isTemporaneo())
                    bp.cancellaAccertamentoModTemporanea(context, accertamento.getAccertamento_modifica());
                if (obbMod.getVariazione() == null && !((it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context)).getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE) == 0))
                    throw new it.cnr.jada.action.MessageToUser("La variazione allo stanziamento residuo non è stata inserita. Impossibile continuare.");
                accertamento.setAccertamento_modifica(obbMod);
                //se provengo da BP che si occupa dell'aggiornamento dei saldi aggiorno
                if (IDefferedUpdateSaldiBP.class.isAssignableFrom(bp.getParent().getClass()))
                    accertamento.setSaldiDaAggiornare(false);
                else
                    accertamento.setSaldiDaAggiornare(true);
                return super.doSalva(context);
            }

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
        return context.findDefaultForward();
    }

    public Forward doBringBackAccertamentiModificaRiportaWindow(ActionContext context) {

        try {
            CRUDAccertamentoResiduoBP bp = (CRUDAccertamentoResiduoBP) getBusinessProcess(context);
            AccertamentoResiduoBulk accertamento = (AccertamentoResiduoBulk) bp.getModel();
            HookForward caller = (HookForward) context.getCaller();
            Accertamento_modificaBulk obbMod = (Accertamento_modificaBulk) caller.getParameter("bringback");
            if (obbMod != null) {
                // cancello l'eventuale modifica temporanea precedente inserita
                if (accertamento.getAccertamento_modifica() != null && accertamento.getAccertamento_modifica().isTemporaneo())
                    bp.cancellaAccertamentoModTemporanea(context, accertamento.getAccertamento_modifica());
                if (obbMod.getVariazione() == null && !((it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context)).getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE) == 0))
                    throw new it.cnr.jada.action.MessageToUser("La variazione allo stanziamento residuo non è stata inserita. Impossibile continuare.");
                accertamento.setAccertamento_modifica(obbMod);
                //se provengo da BP che si occupa dell'aggiornamento dei saldi aggiorno
                if (IDefferedUpdateSaldiBP.class.isAssignableFrom(bp.getParent().getClass()))
                    accertamento.setSaldiDaAggiornare(false);
                else
                    accertamento.setSaldiDaAggiornare(true);
                return super.doRiporta(context);
            }

        } catch (Throwable ex) {
            return handleException(context, ex);
        }
        return context.findDefaultForward();
    }

    public Forward doApriModificheAccertamenti(ActionContext context) {
        try {
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
            AccertamentoBulk accertamento = (AccertamentoBulk) bp.getModel();

            CRUDAccertamentoModificaBP newbp = null;
            newbp = (CRUDAccertamentoModificaBP) context.getUserInfo().createBusinessProcess(context, "CRUDAccertamentoModificaBP", new Object[]{"V", accertamento, CRUDAccertamentoModificaBP.TIPO_ACCESSO_VISUALIZZAZIONE});
            context.addBusinessProcess(newbp);
            return doCerca(context);

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doAnnullaScadenza(ActionContext context) {
        try {
            fillModel(context);
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) getBusinessProcess(context);
            bp.annullaScadenza(context);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doBringBackSearchFindAssestato(ActionContext context, Pdg_vincoloBulk pdgVincolo, V_assestatoBulk assestato) {
        try {
            fillModel(context);
            CRUDAccertamentoResiduoBP bp = (CRUDAccertamentoResiduoBP) getBusinessProcess(context);
            if (assestato != null) {
                pdgVincolo.setAssestatoRisorseCopertura(assestato);
                pdgVincolo.setEsercizio_res(assestato.getEsercizio_res());
                pdgVincolo.setLineaAttivita(new WorkpackageBulk(assestato.getCd_centro_responsabilita(), assestato.getCd_linea_attivita()));
                doSearch(context, "main.Vincoli.find_linea_attivita");
                pdgVincolo.setElementoVoce(new Elemento_voceBulk(assestato.getCd_elemento_voce(), assestato.getEsercizio(), assestato.getTi_appartenenza(), assestato.getTi_gestione()));
                doSearch(context, "main.Vincoli.find_elemento_voce");
                bp.setDirty(true);
            }
            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    public Forward doBlankSearchFindAssestato(ActionContext context, Pdg_vincoloBulk pdgVincolo) {
        try {
            fillModel(context);
            CRUDAccertamentoResiduoBP bp = (CRUDAccertamentoResiduoBP) getBusinessProcess(context);
            pdgVincolo.setAssestatoRisorseCopertura(null);
            pdgVincolo.setEsercizio_res(null);
            pdgVincolo.setLineaAttivita(null);
            pdgVincolo.setElementoVoce(null);
            pdgVincolo.setIm_vincolo(BigDecimal.ZERO);
            bp.setDirty(true);
            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    public Forward doOnChangeStato(ActionContext context) {
        try {
            CRUDAccertamentoBP bp = (CRUDAccertamentoBP) context.getBusinessProcess();
            AccertamentoResiduoBulk accertamento = (AccertamentoResiduoBulk) bp.getModel();
            String oldStato = accertamento.getStato();

            fillModel(context);

            if (accertamento.isStatoInesigibile() || accertamento.isStatoParzialmenteInesigibile()) {
                if (accertamento.getIm_quota_inesigibile() == null)
                    accertamento.setIm_quota_inesigibile(BigDecimal.ZERO);
                if (accertamento.isStatoInesigibile())
                    accertamento.setIm_quota_inesigibile(accertamento.getImportoNonIncassato());
            } else if (accertamento.getPdgVincoliColl().size() > 0 || accertamento.getAccertamentoVincoliPerentiColl().size() > 0)
                if (bp instanceof CRUDAccertamentoResiduoAmministraBP)
                    bp.setMessage("Attenzione! Esistono vincoli associati all'accertamento non coerenti con il suo nuovo stato che saranno azzerati all'atto del salvataggio.");
                else {
                    accertamento.setStato(oldStato);
                    bp.setMessage("Operazione non possibile! Esistono vincoli associati all'accertamento. Eliminare i vincoli e rieffettuare l'operazione.");
                }
            else
                accertamento.setIm_quota_inesigibile(null);

            return context.findDefaultForward();
        } catch (java.lang.ClassCastException ex) {
            return context.findDefaultForward();
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }
    public Forward doCopiaAccertamento(ActionContext context) 
    {
    	try {
    		fillModel( context );
    		CRUDAccertamentoBP bp = (CRUDAccertamentoBP) context.getBusinessProcess();
            
		    bp.getModel().validate();
            if (((AccertamentoBulk) bp.getModel()).getCapitolo().getCrudStatus() != bp.getModel().NORMAL)
                doSearch(context, "main.find_capitolo");
            if (bp.getMessage() != null) {
                bp.setMessage("La ricerca del Capitolo non ha fornito alcun risultato.");
                return context.findDefaultForward();
            }
        	if(bp.isDirty())
           		return openContinuePrompt(context, "doConfirmCopiaAccertamento");
           	else
           		return doConfirmCopiaAccertamento(context, 4);
            
        } catch(Throwable e) {
    		return handleException(context,e);
    	}
    }
    public Forward doConfirmCopiaAccertamento(ActionContext context, int option) 
    {
    	try {
    		if (option == OptionBP.YES_BUTTON) {
    			CRUDAccertamentoBP bp = (CRUDAccertamentoBP)getBusinessProcess(context);
    			bp.copiaAccertamento( context );
    		}
    		return context.findDefaultForward();
    	} catch(Throwable e) {
    		return handleException(context,e);
    	}
    }

    public Forward doOnAnnoAccertamentoPluriennaleChange(ActionContext actioncontext) {
        try {


            CRUDAccertamentoBP  bp = (CRUDAccertamentoBP ) getBusinessProcess(actioncontext);
            AccertamentoBulk model=(AccertamentoBulk)bp.getModel();
            Accertamento_pluriennaleBulk riga = (Accertamento_pluriennaleBulk) bp.getCrudAccertamento_pluriennale().getModel();

            Integer annoCorrente = model.getEsercizio();

            fillModel(actioncontext);

            if(riga.getAnno().compareTo(annoCorrente) <= 0){
                throw new ApplicationException("L'anno di Accertamento Pluriennale deve essere successivo all'anno corrente");
            }
            return actioncontext.findDefaultForward();
        } catch (Throwable e) {
            return handleException(actioncontext, e);
        }
    }

}
