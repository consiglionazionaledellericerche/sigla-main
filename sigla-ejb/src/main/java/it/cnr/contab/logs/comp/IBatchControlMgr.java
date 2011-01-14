package it.cnr.contab.logs.comp;

import it.cnr.contab.logs.bulk.Batch_controlBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.util.RemoteIterator;

public interface IBatchControlMgr
    extends ICRUDMgr
{

    public abstract Batch_controlBulk attivaBatch(UserContext usercontext, Batch_controlBulk batch_controlbulk)
        throws ComponentException;

    public abstract OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException;

    public abstract Batch_controlBulk disattivaBatch(UserContext usercontext, Batch_controlBulk batch_controlbulk)
        throws ComponentException;

    public abstract void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException;

    public abstract Batch_controlBulk inizializzaParametri(UserContext usercontext, Batch_controlBulk batch_controlbulk)
        throws ComponentException;

    public abstract RemoteIterator listaBatch_control_jobs(UserContext usercontext)
        throws ComponentException;
}