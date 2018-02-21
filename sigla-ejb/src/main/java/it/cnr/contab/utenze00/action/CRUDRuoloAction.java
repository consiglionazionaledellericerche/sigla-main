package it.cnr.contab.utenze00.action;

/**
 * Action che gestisce l'interfaccia di Gestione Ruoli: in particolare gestisce l'aggiunta e la rimozione di una
 * accesso ad un Ruolo
 */

import it.cnr.contab.utenze00.bp.CRUDRuoloBP;
import it.cnr.contab.utenze00.bp.CRUDTipoRuoloBP;
import it.cnr.contab.utenze00.bulk.RuoloBulk;
import it.cnr.contab.utenze00.bulk.Ruolo_accessoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.FormField;

public class CRUDRuoloAction extends CRUDAction {
    /**
     * CRUDRuoloAction constructor comment.
     */
    public CRUDRuoloAction() {
        super();
    }

    /**
     * Aggiunge un nuovo accesso alla lista di Accessi associati ad un Ruolo
     * @param context contesto dell'azione
     * @return it.cnr.jada.action.Forward forward successivo
     */


    public Forward doAggiungiAccesso(ActionContext context) throws FillException {
        CRUDRuoloBP bp = (CRUDRuoloBP) context.getBusinessProcess();
        fillModel(context);
        RuoloBulk ruolo = (RuoloBulk) bp.getModel();
        int[] indexes = bp.getCrudAccessi_disponibili().getSelectedRows(context);

        java.util.Arrays.sort(indexes);

        for (int i = indexes.length - 1; i >= 0; i--) {
            Ruolo_accessoBulk ra = ruolo.addToRuolo_accessi(indexes[i]);
            ra.setToBeCreated();
            ra.setUser(context.getUserInfo().getUserid());
        }
        bp.getCrudAccessi_disponibili().getSelection().clearSelection();
        return context.findDefaultForward();
    }

    /**
     * Rimuove un accesso dalla lista di Accessi associati ad un Ruolo
     * @param context contesto dell'azione
     * @return it.cnr.jada.action.Forward forward successivo
     */
    public Forward doRimuoviAccesso(ActionContext context) throws FillException, BusinessProcessException {
        CRUDRuoloBP bp = (CRUDRuoloBP) context.getBusinessProcess();
        fillModel(context);
        RuoloBulk ruolo = (RuoloBulk) bp.getModel();
        int indexes[] = bp.getCrudAccessi().getSelectedRows(context);

        java.util.Arrays.sort(indexes);

        for (int i = indexes.length - 1; i >= 0; i--) {
            Ruolo_accessoBulk ra = ruolo.removeFromRuolo_accessi(indexes[i]);
            ra.setToBeDeleted();
        }
        bp.getCrudAccessi().reset(context);
        return context.findDefaultForward();
    }

    public Forward doCRUDTipo_ruolo(ActionContext context) throws FillException, BusinessProcessException {
        CRUDRuoloBP bp = (CRUDRuoloBP) context.getBusinessProcess();
        RuoloBulk ruolo = (RuoloBulk) bp.getModel();
        FormField formfield = getFormField(context, "main.tipo_ruolo");
        try {
            fillModel(context);
            if (bp.isDirty()) {
                setErrorMessage(context, "Attenzione: è necessario salvare le modifiche effettuate!");
                return context.findDefaultForward();
            }
            CRUDTipoRuoloBP nbp = (CRUDTipoRuoloBP) context.createBusinessProcess("CRUDTipoRuoloBP",
                    new Object[]{
                            bp.isEditable() ? "M" : "V"
                    }
            );
            context.addHookForward("bringback", this, "doBringBackCRUD");
            HookForward hookforward = (HookForward) context.findForward("bringback");
            hookforward.addParameter("field", formfield);
            nbp.setBringBack(true);
            return context.addBusinessProcess(nbp);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }
}
