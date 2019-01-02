package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.ejb.DistintaCassiereComponentSession;
import it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.Button;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * <!-- @TODO: da completare -->
 */

public class RicercaMandatoReversaleBP extends it.cnr.jada.util.action.SimpleCRUDBP implements it.cnr.jada.util.action.SelectionListener {
    public RicercaMandatoReversaleBP() {
        super();
    }

    public RicercaMandatoReversaleBP(String function) {
        super(function);
    }

    /**
     * clearSelection method comment.
     */
    public void clearSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

        try {

            ((DistintaCassiereComponentSession) createComponentSession()).annullaModificaDettagliDistinta(
                    context.getUserContext(),
                    (Distinta_cassiereBulk) ((BulkBP) getParent()).getModel());
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        }
    }

    /**
     * Metodo utilizzato per creare una toolbar applicativa personalizzata.
     *
     * @return toolbar La nuova toolbar creata
     */
    protected it.cnr.jada.util.jsp.Button[] createToolbar() {
        Button[] toolbar = new Button[3];
        int i = 0;
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.search");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.startSearch");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.freeSearch");
        return toolbar;
    }

    /**
     * deselectAll method comment.
     */
    public void deselectAll(it.cnr.jada.action.ActionContext context) {
    }

    public it.cnr.jada.util.RemoteIterator find(ActionContext context, it.cnr.jada.persistency.sql.CompoundFindClause clauses, OggettoBulk model) throws it.cnr.jada.action.BusinessProcessException {
        try {
            CRUDDistintaCassiereBP bpp = (CRUDDistintaCassiereBP) this.getParent();
            Distinta_cassiereBulk bulk = (Distinta_cassiereBulk) bpp.getModel();
            bulk.setFl_flusso(bpp.isFlusso());
            return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ((DistintaCassiereComponentSession) createComponentSession()).cercaMandatiEReversali(context.getUserContext(), clauses, (V_mandato_reversaleBulk) model, bulk));
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * getSelection method comment.
     */
    public java.util.BitSet getSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet currentSelection) {
        //for (int i = 0;i < bulks.length;i++) {
        //if (Boolean.TRUE.equals(((Cdr_ass_tipo_laBulk)bulks[i]).getFl_associato()))
        //currentSelection.set(i);
        //}
        return currentSelection;
    }

    /**
     * initializeSelection method comment.
     */
    public void initializeSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

        try {
            ((DistintaCassiereComponentSession) createComponentSession()).inizializzaDettagliDistintaPerModifica(
                    context.getUserContext(),
                    (Distinta_cassiereBulk) ((BulkBP) getParent()).getModel());
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        }
    }

    public boolean isFreeSearchButtonHidden() {

        return true;
    }

    /**
     * Inzializza il ricevente nello stato di SEARCH.
     *
     * @param context Il contesto dell'azione
     */
    public void reset(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
/////??????????????		rollbackUserTransaction();
            setModel(context, createEmptyModelForSearch(context));
            setStatus(SEARCH);
            setDirty(false);
            resetChildren(context);
        } catch (Throwable e) {
            throw new it.cnr.jada.action.BusinessProcessException(e);
        }
    }

    /**
     * selectAll method comment.
     */
    public void selectAll(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
        try {
            ((DistintaCassiereComponentSession) createComponentSession()).associaTuttiDocContabili(
                    context.getUserContext(),
                    (Distinta_cassiereBulk) ((BulkBP) getParent()).getModel(), (V_mandato_reversaleBulk) getModel());
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        }

    }

    /**
     * setSelection method comment.
     */
    public java.util.BitSet setSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet oldSelection, java.util.BitSet newSelection) throws it.cnr.jada.action.BusinessProcessException {

        // Mario - purtroppo sono costretto ad ottenere una referenza al bp padre del RicercaMandatoReversaleBP
        // supponiamo (senza controllo ulteriore) che questo bp sia figlio sempre di CRUDDistintaCassiereBP
        CRUDDistintaCassiereBP bpp = (CRUDDistintaCassiereBP) this.getParent();

        java.util.BitSet oldSel = new java.util.BitSet();
        java.util.BitSet newSel = new java.util.BitSet();
        oldSel.or(oldSelection);
        newSel.or(newSelection);

        try {
            // per i mandati CORI/IVA l'aggiunta è gia avvenuta in RicercaMandatoReversaleAction.doCerca
            if (bpp.isInserisciMandatiVersamentoCori(context)) {

                for (int i = 0; i < bulks.length; i++) {
                    V_mandato_reversaleBulk docContabile = (V_mandato_reversaleBulk) bulks[i];
                    if (docContabile.getVersamento_cori().booleanValue()) {
                        oldSel.clear(i);
                        newSel.clear(i);
                    }
                }

            }

            ((DistintaCassiereComponentSession) createComponentSession()).modificaDettagliDistinta(
                    context.getUserContext(),
                    (Distinta_cassiereBulk) ((BulkBP) getParent()).getModel(),
                    bulks,
                    oldSel,
                    newSel);


            if (bpp.isInserisciMandatiVersamentoCori(context)) {

                // Mario - purtroppo sono costretto ad ottenere una referenza al bp del selezionatore
                Enumeration childs = this.getChildren();
                SelezionatoreListaBP nbp = (SelezionatoreListaBP) childs.nextElement();

                // Mario - controlliamo che non siano stati deselezionati dall'utente
                // i mandati di versamento CORI/IVA accentrati selezionati da me in automatico,
                it.cnr.jada.util.action.Selection models = nbp.getSelection();
                V_mandato_reversaleBulk man;
                // numero di elementi presenti nell'ultima pagina
                int nres = nbp.getPageSize() - (nbp.getPageCount() * nbp.getPageSize() - nbp.getElementsCount());
                for (int i = 0; i < nbp.getPageSize() && ((i < nres) || (nbp.getCurrentPage() < nbp.getPageCount() - 1)); i++) {
                    man = (V_mandato_reversaleBulk) nbp.getElementAt(context, nbp.getCurrentPage() * nbp.getPageSize() + i);
                    if (man.getVersamento_cori().booleanValue())
                        if (!models.isSelected(nbp.getCurrentPage() * nbp.getPageSize() + i)) {
                            throw new it.cnr.jada.action.MessageToUser("Non è possibile deselezionare i mandati di versamento CORI/IVA accentrati!");
                        }
                }

            }

            return newSelection;

        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        }
    }

    public void inserisciMandatiCORI(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet oldSelection, java.util.BitSet newSelection) throws it.cnr.jada.action.BusinessProcessException {

        try {
            ((DistintaCassiereComponentSession) createComponentSession()).modificaDettagliDistinta(
                    context.getUserContext(),
                    (Distinta_cassiereBulk) ((BulkBP) getParent()).getModel(),
                    bulks,
                    oldSelection,
                    newSelection);
        } catch (it.cnr.jada.comp.ComponentException e) {
            throw handleException(e);
        } catch (java.rmi.RemoteException e) {
            throw handleException(e);
        }
    }

    public void inserisciMandatiCORI(ActionContext context, RicercaMandatoReversaleBP bp, CRUDDistintaCassiereBP bpp, SelezionatoreListaBP nbp)
            throws BusinessProcessException {
        // MARIO - inserimento automatico dei mandati di versamento cori/iva accentrati
        if (bpp.isInserisciMandatiVersamentoCori(context)) {
            // individuiamo e selezioniamo automaticamente i mandati di versamento cori/iva accentrati
            it.cnr.jada.util.action.Selection models = nbp.getSelection();
            V_mandato_reversaleBulk man;
            for (int i = 0; i < nbp.getElementsCount(); i++) {
                man = (V_mandato_reversaleBulk) nbp.getElementAt(context, i);
                if (man.getVersamento_cori().booleanValue())
                    models.setSelected(i);
            }
            nbp.setSelection(models);
            // inseriamo i mandati
            List lman = nbp.getSelectedElements(context);
            if (!lman.isEmpty()) {
                OggettoBulk[] mandati = new OggettoBulk[lman.size()];
                BitSet oldbit = new BitSet(lman.size());
                BitSet newbit = new BitSet(lman.size());
                Iterator it = lman.iterator();
                for (int i = 0; it.hasNext(); i++) {
                    mandati[i] = (OggettoBulk) it.next();
                    oldbit.clear(i);
                    newbit.set(i);
                }
                bp.inserisciMandatiCORI(context, mandati, oldbit, newbit);
            }
        }
    }
}