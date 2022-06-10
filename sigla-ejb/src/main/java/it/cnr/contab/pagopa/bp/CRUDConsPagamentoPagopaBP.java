package it.cnr.contab.pagopa.bp;

import it.cnr.contab.pagopa.bulk.PagamentoPagopaBulk;
import it.cnr.contab.pagopa.bulk.PendenzaPagopaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.action.SimpleCRUDBP;

public class CRUDConsPagamentoPagopaBP extends ConsultazioniBP {
    private PendenzaPagopaBulk pendenza;

    public CRUDConsPagamentoPagopaBP() {
        super();
    }

    public CRUDConsPagamentoPagopaBP(String function) {
        super(function);
    }

    public PendenzaPagopaBulk getPendenza() {
        return pendenza;
    }

    public void setPendenza(PendenzaPagopaBulk pendenza) {
        this.pendenza = pendenza;
    }

    @Override
    public String getMultiSelezione() {
        return "N";
    }
}
