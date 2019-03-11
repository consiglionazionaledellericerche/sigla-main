package it.cnr.contab.progettiric00.bp;

import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;

public class AmministraTestataProgettiRicercaBP extends TestataProgettiRicercaBP {
    public AmministraTestataProgettiRicercaBP() {
    }

    public AmministraTestataProgettiRicercaBP(String function) {
        super(function);
    }

    @Override
    protected boolean isROProgettoPianoEconomico(ProgettoBulk progettoBulk) {
        return Boolean.FALSE;
    }

    @Override
    public void basicEdit(ActionContext actioncontext, OggettoBulk oggettobulk, boolean flag) throws BusinessProcessException {
        super.basicEdit(actioncontext, oggettobulk, flag);
        this.setStatus(EDIT);
    }
}
