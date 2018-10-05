package it.cnr.contab.config00.bp;

import it.cnr.contab.config00.ejb.EsercizioComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ComponentException;

import java.rmi.RemoteException;

/**
 * Business process che mantiene lo stato di un processo CRUD.
 * E' utilizzato in combinazione con la <code>CRUDEsercizioAction</code>.
 */
public class CRUDConfigEsercizioBP extends it.cnr.jada.util.action.SimpleCRUDBP {
    /**
     * Primo costruttore della classe <code>CRUDConfigEsercizioBP</code>.
     */
    public CRUDConfigEsercizioBP() {
        super();
    }

    /**
     * Secondo costruttore della classe <code>CRUDConfigEsercizioBP</code>.
     *
     * @param String function
     */
    public CRUDConfigEsercizioBP(String function) {
        super(function);
    }

    /**
     * Gestione comando di apertura dei Piani di Gestione
     *
     * @param context L'ActionContext della richiesta
     * @throws BusinessProcessException
     */
    public void apriPdg(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            EsercizioBulk esercizio = ((it.cnr.contab.config00.ejb.EsercizioComponentSession) createComponentSession()).apriPianoDiGestione(context.getUserContext(), (EsercizioBulk) getModel());
            setModel(context, esercizio);
            setMessage("Piano di Gestione aperto in modo corretto.");
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * Metodo che permette di cambiare lo stato dell'esercizio contabile, in
     * particolari condizioni e rispettando criteri predefiniti.
     *
     * @param context contesto dell'Action che e' stata richiesta
     */
    public void cambiaStato(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            EsercizioBulk esercizio = ((it.cnr.contab.config00.ejb.EsercizioComponentSession) createComponentSession()).cambiaStatoConBulk(context.getUserContext(), (EsercizioBulk) getModel());
            setModel(context, esercizio);
            setMessage("Stato aggiornato in modo corretto.");
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
     * Restituisce il valore della proprietà 'apriPdGButtonEnabled'
     * Il bottone di apertura dei pdg è abilitato solo se l'esercizio è in stato (INIZIALE/PDG_APERTO/STATO_APERTO)
     * e lo stato di apertura/chiusura è impostato e il modello è editabile e si è in editing
     *
     * @return Il valore della proprietà 'apriPdGButtonEnabled'
     */
    public boolean isApriPdGButtonEnabled() {
        if (isEditing() && isEditable() &&
                ((EsercizioBulk) getModel()).getSt_apertura_chiusura() != null &&
                (((EsercizioBulk) getModel()).getSt_apertura_chiusura().equals(EsercizioBulk.STATO_INIZIALE) ||
                        ((EsercizioBulk) getModel()).getSt_apertura_chiusura().equals(EsercizioBulk.STATO_PDG_APERTO) ||
                        ((EsercizioBulk) getModel()).getSt_apertura_chiusura().equals(EsercizioBulk.STATO_APERTO)))
            return true;
        return false;

    }

    /**
     * Ritorna false se il pusante di apertura dei PDG deve essere nascosto
     */
    public boolean isApriPdGButtonHidden() {
        //if (((EsercizioBulk) getModel()).getCd_cds().equals( it.cnr.contab.config00.sto.bulk.EnteBulk.CODICE_ENTE))
        return false;
    }

    /**
     * Restituisce il valore della proprietà 'cambiaStatoButtonEnabled'
     * Il bottone di modifica stato dell'esercizio è abilitato solo se l'esercizio non è in stato (INIZIALE/CHIUSO)
     * e lo stato di apertura/chiusura è impostato e il modello è editabile e si è in editing
     *
     * @return Il valore della proprietà 'cambiaStatoButtonEnabled'
     */
    public boolean isCambiaStatoButtonEnabled() {
        if (isEditing() && isEditable() &&
                ((EsercizioBulk) getModel()).getSt_apertura_chiusura() != null &&
                !((EsercizioBulk) getModel()).getSt_apertura_chiusura().equals(EsercizioBulk.STATO_INIZIALE)

            // &&! ((EsercizioBulk) getModel()).getSt_apertura_chiusura().equals( EsercizioBulk.STATO_CHIUSO_DEF)
        )
            return true;
        return false;

    }

    protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
        super.initialize(actioncontext);
        EsercizioComponentSession sessione = (EsercizioComponentSession) createComponentSession();
        try {
            EsercizioBulk esercizioBulk = sessione.getEsercizio(actioncontext.getUserContext());
            if (esercizioBulk != null)
                this.setModel(actioncontext, esercizioBulk);
            this.setStatus(it.cnr.jada.util.action.FormController.EDIT);
        } catch (DetailedRuntimeException e) {
            throw handleException(e);
        } catch (ComponentException e) {
            throw handleException(e);
        } catch (RemoteException e) {
            throw handleException(e);
        }
    }

    public boolean isSearchButtonHidden() {
        return true;
    }

    public boolean isFreeSearchButtonHidden() {
        return true;
    }

    public boolean isNewButtonHidden() {
        return true;
    }
}
