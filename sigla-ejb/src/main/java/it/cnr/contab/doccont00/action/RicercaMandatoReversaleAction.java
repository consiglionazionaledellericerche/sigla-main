package it.cnr.contab.doccont00.action;


import it.cnr.contab.doccont00.bp.CRUDDistintaCassiereBP;
import it.cnr.contab.doccont00.bp.RicercaMandatoReversaleBP;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.ObjectReplacer;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.SelectionListener;
import it.cnr.jada.util.action.SelezionatoreListaBP;

import java.rmi.RemoteException;

public class RicercaMandatoReversaleAction extends it.cnr.jada.util.action.CRUDAction {
    public RicercaMandatoReversaleAction() {
        super();
    }

    /**
     * Gestisce un HookForward di ritorno da un risultato di una ricerca (SelezionatoreBP)
     *
     * @param context <code>ActionContext</code> in uso.
     * @return <code>Forward</code>
     */
    public Forward doAnnullaRiportaSelezione(ActionContext context) {
        try {
            context.closeBusinessProcess();
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce una richiesta di ricerca.
     * <p>
     * L'implementazione di default utilizza il metodo astratto <code>read</code>
     * di <code>CRUDBusinessProcess</code>.
     * Se la ricerca fornisce più di un risultato viene creato un
     * nuovo <code>SelezionatoreListaBP</code> per la selezione di un elemento.
     * Al business process viene anche chiesto l'elenco delle colonne da
     * visualizzare.
     */
    public Forward doCerca(ActionContext context) throws java.rmi.RemoteException, InstantiationException, javax.ejb.RemoveException {
        String setCol = null;
        try {
            fillModel(context);
            RicercaMandatoReversaleBP bp = (RicercaMandatoReversaleBP) getBusinessProcess(context);
            OggettoBulk model = bp.getModel();
            it.cnr.jada.util.RemoteIterator ri = bp.find(context, null, model);
            if (ri == null || ri.countElements() == 0) {
                it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
                bp.setMessage("Nessun mandato/reversale da inserire in distinta.");
                return context.findDefaultForward();
            } else {

                // Mario - purtroppo sono costretto ad ottenere una referenza al bp padre del RicercaMandatoReversaleBP
                // supponiamo (senza controllo ulteriore) che questo bp sia figlio sempre di CRUDDistintaCassiereBP
                CRUDDistintaCassiereBP bpp = (CRUDDistintaCassiereBP) bp.getParent();

                bp.setModel(context, model);

                if (bpp != null && bpp.isUoDistintaTuttaSac(context) && (Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(), it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context)).getFl_tesoreria_unica().booleanValue()))
                    setCol = "elencoConUoFirmati";
                else if (bpp != null && bpp.isUoDistintaTuttaSac(context))
                    setCol = "elencoConUo";

                SelezionatoreListaBP nbp = selectMandatiInDistinta(context,
                        ri,
                        it.cnr.jada.bulk.BulkInfo.getBulkInfo(V_mandato_reversaleBulk.class),
                        setCol, "doRiportaSelezione", null, bp);
                nbp.setMultiSelection(true);

                inserisciMandatiCORI(context, bp, bpp, nbp);
                context.addHookForward("annulla_seleziona", this, "doAnnullaRiportaSelezione");

                return context.findDefaultForward();
            }
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * Gestisce un HookForward di ritorno da un risultato di una ricerca (SelezionatoreBP)
     *
     * @param context <code>ActionContext</code> in uso.
     * @return <code>Forward</code>
     */
    public Forward doRiportaSelezione(ActionContext context) {

        try {
            BusinessProcess bp = this.getBusinessProcess(context);
            HookForward caller = (HookForward) context.getCaller();
            context.closeBusinessProcess();
            HookForward forward = (HookForward) context.findForward("bringback");
            return forward;
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public SelezionatoreListaBP selectMandatiInDistinta(ActionContext actioncontext, RemoteIterator remoteiterator, BulkInfo bulkinfo, String s, String s1, ObjectReplacer objectreplacer, SelectionListener selectionlistener)
            throws BusinessProcessException {
        try {
            SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP) actioncontext.createBusinessProcess("SelezionatoreMandatiInDistintaBP");
            selezionatorelistabp.setObjectReplacer(objectreplacer);
            selezionatorelistabp.setSelectionListener(actioncontext, selectionlistener);
            selezionatorelistabp.setIterator(actioncontext, remoteiterator);
            selezionatorelistabp.setBulkInfo(bulkinfo);
            selezionatorelistabp.setColumns(bulkinfo.getColumnFieldPropertyDictionary(s));
            actioncontext.addHookForward("seleziona", this, s1);
            HookForward _tmp = (HookForward) actioncontext.findForward("seleziona");
            actioncontext.addBusinessProcess(selezionatorelistabp);
            return selezionatorelistabp;
        } catch (RemoteException remoteexception) {
            throw new BusinessProcessException(remoteexception);
        }
    }

    public void inserisciMandatiCORI(ActionContext context, RicercaMandatoReversaleBP bp, CRUDDistintaCassiereBP bpp, SelezionatoreListaBP nbp)
            throws BusinessProcessException {
        bp.inserisciMandatiCORI(context, bp, bpp, nbp);
    }

}
