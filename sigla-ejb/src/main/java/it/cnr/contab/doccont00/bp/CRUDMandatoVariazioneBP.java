package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.util.enumeration.StatoVariazioneSostituzione;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;

import java.rmi.RemoteException;
import java.util.Optional;

public class CRUDMandatoVariazioneBP extends CRUDMandatoBP {
    public CRUDMandatoVariazioneBP() {
        super("Tr");
    }

    public CRUDMandatoVariazioneBP(String function) {
        super(function + "Tr");
    }

    public void setDaVariare(ActionContext actionContext) throws it.cnr.jada.action.BusinessProcessException {
        final MandatoBulk mandatoBulk = Optional.ofNullable(getModel())
                .filter(MandatoBulk.class::isInstance)
                .map(MandatoBulk.class::cast)
                .orElseThrow(() -> new BusinessProcessException("Mandato non trovato!"));

        mandatoBulk.setStatoVarSos(StatoVariazioneSostituzione.DA_VARIARE.value());
        mandatoBulk.setToBeUpdated();
        try {
            final OggettoBulk oggettoBulk = createComponentSession().modificaConBulk(actionContext.getUserContext(), mandatoBulk);
            commitUserTransaction();
            basicEdit(actionContext, oggettoBulk, true);
        } catch (ComponentException | RemoteException e) {
            throw handleException(e);
        }
    }
}
