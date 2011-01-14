package it.cnr.contab.logs.comp;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;

public interface ITestataLogMgr
{

    public abstract OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException;

    public abstract OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException;

    public abstract OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException;
}