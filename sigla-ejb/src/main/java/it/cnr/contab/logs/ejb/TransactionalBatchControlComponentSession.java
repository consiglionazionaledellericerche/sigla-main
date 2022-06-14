/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.logs.ejb;

import it.cnr.contab.logs.bulk.Batch_controlBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.TransactionalSessionImpl;

import javax.ejb.EJBException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

// Referenced classes of package it.cnr.contab.logs.ejb:
//            BatchControlComponentSession

public class TransactionalBatchControlComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession
    implements BatchControlComponentSession
{

    public TransactionalBatchControlComponentSession()
    {
    }

    public Batch_controlBulk attivaBatch(UserContext usercontext, Batch_controlBulk batch_controlbulk)
        throws RemoteException, ComponentException
    {
        try
        {
            return (Batch_controlBulk)invoke("attivaBatch", new Object[] {
                usercontext, batch_controlbulk
            });
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            try
            {
                throw invocationtargetexception.getTargetException();
            }
            catch(ComponentException componentexception)
            {
                throw componentexception;
            }
            catch(Throwable throwable)
            {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }
    public RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk)
        throws RemoteException, ComponentException
    {
        try
        {
            return (RemoteIterator)invoke("cerca", new Object[] {
                usercontext, compoundfindclause, oggettobulk
            });
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            try
            {
                throw invocationtargetexception.getTargetException();
            }
            catch(ComponentException componentexception)
            {
                throw componentexception;
            }
            catch(Throwable throwable)
            {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
        throws RemoteException, ComponentException
    {
        try
        {
            return (RemoteIterator)invoke("cerca", new Object[] {
                usercontext, compoundfindclause, oggettobulk, oggettobulk1, s
            });
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            try
            {
                throw invocationtargetexception.getTargetException();
            }
            catch(ComponentException componentexception)
            {
                throw componentexception;
            }
            catch(Throwable throwable)
            {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws RemoteException, ComponentException
    {
        try
        {
            return (OggettoBulk)invoke("creaConBulk", new Object[] {
                usercontext, oggettobulk
            });
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            try
            {
                throw invocationtargetexception.getTargetException();
            }
            catch(ComponentException componentexception)
            {
                throw componentexception;
            }
            catch(Throwable throwable)
            {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public OggettoBulk[] creaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[])
        throws RemoteException, ComponentException
    {
        try
        {
            return (OggettoBulk[])invoke("creaConBulk", new Object[] {
                usercontext, aoggettobulk
            });
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            try
            {
                throw invocationtargetexception.getTargetException();
            }
            catch(ComponentException componentexception)
            {
                throw componentexception;
            }
            catch(Throwable throwable)
            {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public Batch_controlBulk disattivaBatch(UserContext usercontext, Batch_controlBulk batch_controlbulk)
        throws RemoteException, ComponentException
    {
        try
        {
            return (Batch_controlBulk)invoke("disattivaBatch", new Object[] {
                usercontext, batch_controlbulk
            });
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            try
            {
                throw invocationtargetexception.getTargetException();
            }
            catch(ComponentException componentexception)
            {
                throw componentexception;
            }
            catch(Throwable throwable)
            {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws RemoteException, ComponentException
    {
        try
        {
            invoke("eliminaConBulk", new Object[] {
                usercontext, oggettobulk
            });
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            try
            {
                throw invocationtargetexception.getTargetException();
            }
            catch(ComponentException componentexception)
            {
                throw componentexception;
            }
            catch(Throwable throwable)
            {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public void eliminaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[])
        throws RemoteException, ComponentException
    {
        try
        {
            invoke("eliminaConBulk", new Object[] {
                usercontext, aoggettobulk
            });
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            try
            {
                throw invocationtargetexception.getTargetException();
            }
            catch(ComponentException componentexception)
            {
                throw componentexception;
            }
            catch(Throwable throwable)
            {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk)
        throws RemoteException, ComponentException
    {
        try
        {
            return (OggettoBulk)invoke("inizializzaBulkPerInserimento", new Object[] {
                usercontext, oggettobulk
            });
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            try
            {
                throw invocationtargetexception.getTargetException();
            }
            catch(ComponentException componentexception)
            {
                throw componentexception;
            }
            catch(Throwable throwable)
            {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk)
        throws RemoteException, ComponentException
    {
        try
        {
            return (OggettoBulk)invoke("inizializzaBulkPerModifica", new Object[] {
                usercontext, oggettobulk
            });
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            try
            {
                throw invocationtargetexception.getTargetException();
            }
            catch(ComponentException componentexception)
            {
                throw componentexception;
            }
            catch(Throwable throwable)
            {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public OggettoBulk[] inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk aoggettobulk[])
        throws RemoteException, ComponentException
    {
        try
        {
            return (OggettoBulk[])invoke("inizializzaBulkPerModifica", new Object[] {
                usercontext, aoggettobulk
            });
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            try
            {
                throw invocationtargetexception.getTargetException();
            }
            catch(ComponentException componentexception)
            {
                throw componentexception;
            }
            catch(Throwable throwable)
            {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public OggettoBulk inizializzaBulkPerRicerca(UserContext usercontext, OggettoBulk oggettobulk)
        throws RemoteException, ComponentException
    {
        try
        {
            return (OggettoBulk)invoke("inizializzaBulkPerRicerca", new Object[] {
                usercontext, oggettobulk
            });
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            try
            {
                throw invocationtargetexception.getTargetException();
            }
            catch(ComponentException componentexception)
            {
                throw componentexception;
            }
            catch(Throwable throwable)
            {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext usercontext, OggettoBulk oggettobulk)
        throws RemoteException, ComponentException
    {
        try
        {
            return (OggettoBulk)invoke("inizializzaBulkPerRicercaLibera", new Object[] {
                usercontext, oggettobulk
            });
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            try
            {
                throw invocationtargetexception.getTargetException();
            }
            catch(ComponentException componentexception)
            {
                throw componentexception;
            }
            catch(Throwable throwable)
            {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public Batch_controlBulk inizializzaParametri(UserContext usercontext, Batch_controlBulk batch_controlbulk)
        throws RemoteException, ComponentException
    {
        try
        {
            return (Batch_controlBulk)invoke("inizializzaParametri", new Object[] {
                usercontext, batch_controlbulk
            });
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            try
            {
                throw invocationtargetexception.getTargetException();
            }
            catch(ComponentException componentexception)
            {
                throw componentexception;
            }
            catch(Throwable throwable)
            {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public RemoteIterator listaBatch_control_jobs(UserContext usercontext)
        throws RemoteException, ComponentException
    {
        try
        {
            return (RemoteIterator)invoke("listaBatch_control_jobs", new Object[] {
                usercontext
            });
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            try
            {
                throw invocationtargetexception.getTargetException();
            }
            catch(ComponentException componentexception)
            {
                throw componentexception;
            }
            catch(Throwable throwable)
            {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws RemoteException, ComponentException
    {
        try
        {
            return (OggettoBulk)invoke("modificaConBulk", new Object[] {
                usercontext, oggettobulk
            });
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            try
            {
                throw invocationtargetexception.getTargetException();
            }
            catch(ComponentException componentexception)
            {
                throw componentexception;
            }
            catch(Throwable throwable)
            {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public OggettoBulk[] modificaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[])
        throws RemoteException, ComponentException
    {
        try
        {
            return (OggettoBulk[])invoke("modificaConBulk", new Object[] {
                usercontext, aoggettobulk
            });
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            try
            {
                throw invocationtargetexception.getTargetException();
            }
            catch(ComponentException componentexception)
            {
                throw componentexception;
            }
            catch(Throwable throwable)
            {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

    public OggettoBulk creaConBulkRequiresNew(UserContext usercontext, OggettoBulk oggettoBulk)
            throws RemoteException, ComponentException
    {
        try
        {
            return (OggettoBulk)invoke("creaBulkRequiresNew", new Object[] {
                    usercontext, oggettoBulk
            });
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(InvocationTargetException invocationtargetexception)
        {
            try
            {
                throw invocationtargetexception.getTargetException();
            }
            catch(ComponentException componentexception)
            {
                throw componentexception;
            }
            catch(Throwable throwable)
            {
                throw new RemoteException("Uncaugth exception", throwable);
            }
        }
    }

}