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

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.TransactionalSessionImpl;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

// Referenced classes of package it.cnr.contab.logs.ejb:
//            TestataLogComponentSession

public class TransactionalTestataLogComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession
    implements TestataLogComponentSession
{

    public TransactionalTestataLogComponentSession()
    {
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

    public RemoteIterator cerca(UserContext usercontext, CompoundFindClause compoundfindclause, Class class1, OggettoBulk oggettobulk, String s)
        throws RemoteException, ComponentException
    {
        try
        {
            return (RemoteIterator)invoke("cerca", new Object[] {
                usercontext, compoundfindclause, class1, oggettobulk, s
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

    public OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
        throws RemoteException, ComponentException
    {
        try
        {
            return (OggettoBulk)invoke("creaConBulk", new Object[] {
                usercontext, oggettobulk, oggettobulk1, s
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

    public void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk, String s)
        throws RemoteException, ComponentException
    {
        try
        {
            invoke("eliminaConBulk", new Object[] {
                usercontext, oggettobulk, s
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

    public void eliminaConBulk(UserContext usercontext, OggettoBulk aoggettobulk[], OggettoBulk oggettobulk, String s)
        throws RemoteException, ComponentException
    {
        try
        {
            invoke("eliminaConBulk", new Object[] {
                usercontext, aoggettobulk, oggettobulk, s
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

    public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
        throws RemoteException, ComponentException
    {
        try
        {
            return (OggettoBulk)invoke("inizializzaBulkPerInserimento", new Object[] {
                usercontext, oggettobulk, oggettobulk1, s
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

    public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
        throws RemoteException, ComponentException
    {
        try
        {
            return (OggettoBulk)invoke("inizializzaBulkPerModifica", new Object[] {
                usercontext, oggettobulk, oggettobulk1, s
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

    public OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk, OggettoBulk oggettobulk1, String s)
        throws RemoteException, ComponentException
    {
        try
        {
            return (OggettoBulk)invoke("modificaConBulk", new Object[] {
                usercontext, oggettobulk, oggettobulk1, s
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
}
