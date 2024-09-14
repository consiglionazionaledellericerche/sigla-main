package it.cnr.contab.incarichi00.bp;

import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;

import java.util.Optional;

public class CRUDAmministraIncarichiProceduraBP extends CRUDIncarichiProceduraBP {
    public CRUDAmministraIncarichiProceduraBP() {
    }

    public CRUDAmministraIncarichiProceduraBP(String function) {
        super(function);
    }

    public CRUDAmministraIncarichiProceduraBP(String function, OggettoBulk bulk) {
        super(function, bulk);
    }

    @Override
    public boolean isInputReadonly() {
        return Boolean.FALSE;
    }

    @Override
    public boolean isROIncaricoRichiesta() {
        return Boolean.FALSE;
    }

    @Override
    public boolean isROContraente() {
        return Boolean.FALSE;
    }

    @Override
    public boolean isInputReadonlyFieldName(String fieldName) {
        return Boolean.FALSE;
    }

    @Override
    protected void basicEdit(ActionContext actioncontext, OggettoBulk oggettobulk, boolean flag) throws BusinessProcessException {
        super.basicEdit(actioncontext, oggettobulk, flag);
        Optional.ofNullable(getModel())
            .filter(Incarichi_proceduraBulk.class::isInstance)
            .map(Incarichi_proceduraBulk.class::cast)
            .ifPresent(incarichiProceduraBulk -> incarichiProceduraBulk.setAmministra(Boolean.TRUE));
        getAndClearMessage();
    }

    @Override
    protected boolean isAmministra() {
        return Boolean.TRUE;
    }

}
