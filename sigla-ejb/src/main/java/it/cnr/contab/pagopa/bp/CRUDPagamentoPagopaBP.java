package it.cnr.contab.pagopa.bp;

import it.cnr.contab.pagopa.bulk.PagamentoPagopaBulk;
import it.cnr.contab.pagopa.bulk.PendenzaPagopaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.SimpleCRUDBP;

public class CRUDPagamentoPagopaBP extends SimpleCRUDBP {
    private PendenzaPagopaBulk pendenza;

    public CRUDPagamentoPagopaBP() {
        super();
    }

    public CRUDPagamentoPagopaBP(String function) {
        super(function);
    }

    public CRUDPagamentoPagopaBP(String function, PendenzaPagopaBulk pendenza)
            throws BusinessProcessException {
        this(function);
        this.pendenza = pendenza;
    }

    public PendenzaPagopaBulk getPendenza() {
        return pendenza;
    }

    public void setPendenza(PendenzaPagopaBulk pendenza) {
        this.pendenza = pendenza;
    }

    protected void initialize(ActionContext context)
            throws BusinessProcessException {
        setEditable(false);
        try {
                if (getPendenza() == null)
                    resetForSearch(context);
                else
                    reset(context);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }
    public OggettoBulk initializeModelForFreeSearch(ActionContext context,
                                                    OggettoBulk bulk) throws BusinessProcessException {
        PagamentoPagopaBulk pagamentoPagopaBulk = (PagamentoPagopaBulk) super.initializeModelForFreeSearch(
                context, bulk);
        pagamentoPagopaBulk.setPendenzaPagopa(getPendenza());
        return pagamentoPagopaBulk;
    }

    public OggettoBulk initializeModelForSearch(ActionContext context,
                                                OggettoBulk bulk) throws BusinessProcessException {
        PagamentoPagopaBulk pagamentoPagopaBulk = (PagamentoPagopaBulk)  super.initializeModelForSearch(context,
                bulk);
        pagamentoPagopaBulk.setPendenzaPagopa(getPendenza());
        return pagamentoPagopaBulk;
    }
}
