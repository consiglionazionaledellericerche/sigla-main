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

package it.cnr.contab.config00.ejb;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import java.rmi.RemoteException;
import java.util.List;

public class TransactionalUnita_organizzativaComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements Unita_organizzativaComponentSession {
    public it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1, it.cnr.jada.bulk.OggettoBulk param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("cerca", new Object[]{
                    param0,
                    param1,
                    param2});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1, it.cnr.jada.bulk.OggettoBulk param2, it.cnr.jada.bulk.OggettoBulk param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("cerca", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("creaConBulk", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk[] creaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk[]) invoke("creaConBulk", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void eliminaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("eliminaConBulk", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void eliminaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("eliminaConBulk", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("inizializzaBulkPerInserimento", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("inizializzaBulkPerModifica", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk[] inizializzaBulkPerModifica(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk[]) invoke("inizializzaBulkPerModifica", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("inizializzaBulkPerRicerca", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("inizializzaBulkPerRicercaLibera", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("modificaConBulk", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk[] modificaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk[]) invoke("modificaConBulk", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk findUOByCodice(it.cnr.jada.UserContext param0, String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("findUOByCodice", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk getUoEnte(it.cnr.jada.UserContext param0) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("getUoEnte", new Object[]{
                    param0});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.lang.String getIndirizzoUnitaOrganizzativa(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param1, boolean param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getIndirizzoUnitaOrganizzativa", new Object[]{
                    param0,
                    param1,
                    param2});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public List findListaUOWS(UserContext userContext, String query,
                              String dominio, String tipoRicerca, String cds) throws ComponentException,
            RemoteException {
        try {
            return (List) invoke("findListaUOWS", new Object[]{
                    userContext,
                    query,
                    dominio,
                    tipoRicerca,
                    cds});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public List<CdsBulk> findListaCds(UserContext userContext, int esercizio) throws ComponentException,
            RemoteException {
        try {
            return (List<CdsBulk>) invoke("findListaCds", new Object[]{
                    userContext,
                    esercizio});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }
}
