package it.cnr.contab.consultazioni.action;

import it.cnr.contab.consultazioni.bp.ConsObbligazioniBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class RicercaLiberaConsObbligazioniAction extends RicercaLiberaAction {
    @Override
    public Forward doIniziaRicerca(ActionContext actioncontext) {
        try {
            salvaCondizione(actioncontext);
            RicercaLiberaBP ricercaliberabp = (RicercaLiberaBP) actioncontext.getBusinessProcess();
            if (ricercaliberabp.isNuovaCondizione())
                ricercaliberabp.cancellaCondizioneCorrente(actioncontext);
            if (!ricercaliberabp.isCanPerformSearchWithoutClauses() && ricercaliberabp.getCondizioneRadice().contaCondizioneSemplici() == 0) {
                ricercaliberabp.setMessage("E' necessario aggiungere almeno una condizione.");
                return actioncontext.findDefaultForward();
            }
            if (ricercaliberabp.getSearchProvider() == null) {
                actioncontext.closeBusinessProcess();
                HookForward hookforward = (HookForward) actioncontext.findForward("filter");
                hookforward.addParameter("filter", ricercaliberabp.getCondizioneRadice().creaFindClause());
                return hookforward;
            }
            RemoteIterator remoteiterator = EJBCommonServices.openRemoteIterator(actioncontext, ricercaliberabp.getSearchProvider().search(actioncontext, (CompoundFindClause) ricercaliberabp.getCondizioneRadice().creaFindClause(), ricercaliberabp.getPrototype()));
            if (remoteiterator == null || remoteiterator.countElements() == 0) {
                ricercaliberabp.setMessage("La ricerca non ha fornito alcun risultato.");
                EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
                return actioncontext.findDefaultForward();
            }
            ConsObbligazioniBP consObbligazioniBP = (ConsObbligazioniBP)ricercaliberabp.getParent();
            consObbligazioniBP.setIterator(actioncontext, remoteiterator);
            actioncontext.closeBusinessProcess();
            HookForward hookforward1 = (HookForward) actioncontext.findForward("searchResult");
            return hookforward1;
        } catch (Exception exception) {
            return handleException(actioncontext, exception);
        }
    }
    private void salvaCondizione(ActionContext actioncontext)
            throws FillException, ValidationException {
        RicercaLiberaBP ricercaliberabp = (RicercaLiberaBP) actioncontext.getBusinessProcess();
        if (ricercaliberabp.isNuovaCondizione() && ((CondizioneSempliceBulk) ricercaliberabp.getCondizioneCorrente()).getFindFieldProperty() == null)
            return;
        if ((ricercaliberabp.getCondizioneCorrente() instanceof CondizioneSempliceBulk) && ((CondizioneSempliceBulk) ricercaliberabp.getCondizioneCorrente()).getFindFieldProperty() == null)
            throw new ValidationException("E' necessario specificare una condizione");
        CondizioneRicercaBulk condizionericercabulk = ricercaliberabp.getCondizioneCorrente();
        condizionericercabulk.fillFromActionContext(actioncontext, null, 2, ricercaliberabp.getFieldValidationMap());
        if (condizionericercabulk instanceof CondizioneSempliceBulk) {
            Object obj = ((CondizioneSempliceBulk) condizionericercabulk).getFindFieldProperty().getValueFromActionContext(ricercaliberabp.getPrototype(), actioncontext, null, ricercaliberabp.getStatus());
            if (obj != FieldProperty.UNDEFINED_VALUE)
                ((CondizioneSempliceBulk) condizionericercabulk).setValue(obj);
        }
        condizionericercabulk.validate();
        ricercaliberabp.setNuovaCondizione(false);
    }
}
