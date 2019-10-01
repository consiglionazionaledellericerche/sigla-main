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

package it.cnr.contab.missioni00.ejb;

import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.missioni00.docs.bulk.AnticipoBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class TransactionalMissioneComponentSession extends
        it.cnr.jada.ejb.TransactionalCRUDComponentSession implements
        MissioneComponentSession {
    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk cancellaCompensoPhisically(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.missioni00.docs.bulk.MissioneBulk) invoke(
                    "cancellaCompensoPhisically",
                    new Object[]{param0, param1});
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk cancellaDiariaPhisically(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException,
            it.cnr.jada.persistency.PersistencyException {
        try {
            return (it.cnr.contab.missioni00.docs.bulk.MissioneBulk) invoke(
                    "cancellaDiariaPhisically", new Object[]{param0, param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (it.cnr.jada.persistency.PersistencyException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk cancellaRimborsoPhisically(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException,
            it.cnr.jada.persistency.PersistencyException {
        try {
            return (it.cnr.contab.missioni00.docs.bulk.MissioneBulk) invoke(
                    "cancellaRimborsoPhisically", new Object[]{param0, param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (it.cnr.jada.persistency.PersistencyException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk cancellaTappePhisically(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException,
            it.cnr.jada.persistency.PersistencyException {
        try {
            return (it.cnr.contab.missioni00.docs.bulk.MissioneBulk) invoke(
                    "cancellaTappePhisically", new Object[]{param0, param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (it.cnr.jada.persistency.PersistencyException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.util.RemoteIterator cerca(
            it.cnr.jada.UserContext param0,
            it.cnr.jada.persistency.sql.CompoundFindClause param1,
            it.cnr.jada.bulk.OggettoBulk param2) throws RemoteException,
            it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("cerca",
                    new Object[]{param0, param1, param2});
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

    public it.cnr.jada.util.RemoteIterator cerca(
            it.cnr.jada.UserContext param0,
            it.cnr.jada.persistency.sql.CompoundFindClause param1,
            it.cnr.jada.bulk.OggettoBulk param2,
            it.cnr.jada.bulk.OggettoBulk param3, java.lang.String param4)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("cerca",
                    new Object[]{param0, param1, param2, param3, param4});
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

    public it.cnr.jada.util.RemoteIterator cercaObbligazioni(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke(
                    "cercaObbligazioni", new Object[]{param0, param1});
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk completaTerzo(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1,
            it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk param2)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.missioni00.docs.bulk.MissioneBulk) invoke(
                    "completaTerzo", new Object[]{param0, param1, param2});
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

    public it.cnr.jada.bulk.OggettoBulk creaConBulk(
            it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("creaConBulk",
                    new Object[]{param0, param1});
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

    public it.cnr.jada.bulk.OggettoBulk creaConBulk(
            it.cnr.jada.UserContext param0,
            it.cnr.jada.bulk.OggettoBulk param1,
            it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("creaConBulk",
                    new Object[]{param0, param1, param2});
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

    public it.cnr.jada.bulk.OggettoBulk[] creaConBulk(
            it.cnr.jada.UserContext param0,
            it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException,
            it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk[]) invoke("creaConBulk",
                    new Object[]{param0, param1});
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

    public void eliminaConBulk(it.cnr.jada.UserContext param0,
                               it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException,
            it.cnr.jada.comp.ComponentException {
        try {
            invoke("eliminaConBulk", new Object[]{param0, param1});
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

    public void eliminaConBulk(it.cnr.jada.UserContext param0,
                               it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException,
            it.cnr.jada.comp.ComponentException {
        try {
            invoke("eliminaConBulk", new Object[]{param0, param1});
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

    public it.cnr.contab.docamm00.tabrif.bulk.CambioBulk findCambio(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk param1,
            java.sql.Timestamp param2) throws RemoteException,
            it.cnr.jada.comp.ComponentException,
            it.cnr.jada.persistency.PersistencyException {
        try {
            return (it.cnr.contab.docamm00.tabrif.bulk.CambioBulk) invoke(
                    "findCambio", new Object[]{param0, param1, param2});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (it.cnr.jada.persistency.PersistencyException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.util.Collection findInquadramenti(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.util.Collection) invoke("findInquadramenti",
                    new Object[]{param0, param1});
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk findInquadramentiETipiTrattamento(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.missioni00.docs.bulk.MissioneBulk) invoke(
                    "findInquadramentiETipiTrattamento", new Object[]{param0,
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

    public java.util.Collection findListabanche(it.cnr.jada.UserContext param0,
                                                it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException,
            it.cnr.jada.persistency.PersistencyException {
        try {
            return (java.util.Collection) invoke("findListabanche",
                    new Object[]{param0, param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (it.cnr.jada.persistency.PersistencyException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.util.Collection findTipi_rapporto(
            it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.util.Collection) invoke("findTipi_rapporto",
                    new Object[]{param0, param1});
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

    public java.util.Collection findTipi_trattamento(
            it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.util.Collection) invoke("findTipi_trattamento",
                    new Object[]{param0, param1});
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk generaDiaria(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.missioni00.docs.bulk.MissioneBulk) invoke(
                    "generaDiaria", new Object[]{param0, param1});
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk generaRimborso(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.missioni00.docs.bulk.MissioneBulk) invoke(
                    "generaRimborso", new Object[]{param0, param1});
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

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(
            it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke(
                    "inizializzaBulkPerInserimento", new Object[]{param0,
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

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(
            it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke(
                    "inizializzaBulkPerModifica",
                    new Object[]{param0, param1});
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

    public it.cnr.jada.bulk.OggettoBulk[] inizializzaBulkPerModifica(
            it.cnr.jada.UserContext param0,
            it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException,
            it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk[]) invoke(
                    "inizializzaBulkPerModifica",
                    new Object[]{param0, param1});
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

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(
            it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke(
                    "inizializzaBulkPerRicerca",
                    new Object[]{param0, param1});
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

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(
            it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke(
                    "inizializzaBulkPerRicercaLibera", new Object[]{param0,
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

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(
            it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke(
                    "inizializzaBulkPerStampa", new Object[]{param0, param1});
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk inizializzaDivisaCambioPerRimborsoKm(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.Missione_dettaglioBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException,
            it.cnr.jada.persistency.PersistencyException,
            javax.ejb.EJBException, java.rmi.RemoteException,
            it.cnr.jada.bulk.ValidationException {
        try {
            return (it.cnr.contab.missioni00.docs.bulk.MissioneBulk) invoke(
                    "inizializzaDivisaCambioPerRimborsoKm", new Object[]{
                            param0, param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (it.cnr.jada.persistency.PersistencyException ex) {
                throw ex;
            } catch (javax.ejb.EJBException ex) {
                throw ex;
            } catch (java.rmi.RemoteException ex) {
                throw ex;
            } catch (it.cnr.jada.bulk.ValidationException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public boolean isMissioneAnnullata(it.cnr.jada.UserContext param0,
                                       it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return ((Boolean) invoke("isMissioneAnnullata", new Object[]{
                    param0, param1})).booleanValue();
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk loadCompenso(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.missioni00.docs.bulk.MissioneBulk) invoke(
                    "loadCompenso", new Object[]{param0, param1});
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

    public it.cnr.jada.bulk.OggettoBulk modificaConBulk(
            it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("modificaConBulk",
                    new Object[]{param0, param1});
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

    public it.cnr.jada.bulk.OggettoBulk modificaConBulk(
            it.cnr.jada.UserContext param0,
            it.cnr.jada.bulk.OggettoBulk param1,
            it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("modificaConBulk",
                    new Object[]{param0, param1, param2});
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

    public it.cnr.jada.bulk.OggettoBulk[] modificaConBulk(
            it.cnr.jada.UserContext param0,
            it.cnr.jada.bulk.OggettoBulk[] param1) throws RemoteException,
            it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk[]) invoke("modificaConBulk",
                    new Object[]{param0, param1});
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

    public void rollbackToSavePoint(it.cnr.jada.UserContext param0,
                                    java.lang.String param1) throws RemoteException,
            it.cnr.jada.comp.ComponentException {
        try {
            invoke("rollbackToSavePoint", new Object[]{param0, param1});
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk setDivisaCambio(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1,
            it.cnr.contab.missioni00.docs.bulk.Missione_tappaBulk param2)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.missioni00.docs.bulk.MissioneBulk) invoke(
                    "setDivisaCambio", new Object[]{param0, param1, param2});
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk setNazioneDivisaCambioItalia(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1,
            it.cnr.contab.missioni00.docs.bulk.Missione_tappaBulk param2)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.missioni00.docs.bulk.MissioneBulk) invoke(
                    "setNazioneDivisaCambioItalia", new Object[]{param0,
                            param1, param2});
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

    public void setSavePoint(it.cnr.jada.UserContext param0,
                             java.lang.String param1) throws RemoteException,
            it.cnr.jada.comp.ComponentException {
        try {
            invoke("setSavePoint", new Object[]{param0, param1});
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

    public it.cnr.jada.bulk.OggettoBulk stampaConBulk(
            it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("stampaConBulk",
                    new Object[]{param0, param1});
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

    public void updateAnticipo(it.cnr.jada.UserContext param0,
                               it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException,
            it.cnr.jada.persistency.PersistencyException {
        try {
            invoke("updateAnticipo", new Object[]{param0, param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (it.cnr.jada.persistency.PersistencyException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk updateCompenso(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException,
            it.cnr.jada.persistency.PersistencyException {
        try {
            return (it.cnr.contab.missioni00.docs.bulk.MissioneBulk) invoke(
                    "updateCompenso", new Object[]{param0, param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (it.cnr.jada.persistency.PersistencyException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void validaEsercizioDataRegistrazione(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("validaEsercizioDataRegistrazione", new Object[]{param0,
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk validaMassimaliSpesa(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1,
            it.cnr.contab.missioni00.docs.bulk.Missione_dettaglioBulk param2)
            throws RemoteException, it.cnr.jada.comp.ComponentException,
            javax.ejb.EJBException,
            it.cnr.jada.persistency.PersistencyException,
            java.rmi.RemoteException, it.cnr.jada.bulk.ValidationException {
        try {
            return (it.cnr.contab.missioni00.docs.bulk.MissioneBulk) invoke(
                    "validaMassimaliSpesa", new Object[]{param0, param1,
                            param2});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (javax.ejb.EJBException ex) {
                throw ex;
            } catch (it.cnr.jada.persistency.PersistencyException ex) {
                throw ex;
            } catch (java.rmi.RemoteException ex) {
                throw ex;
            } catch (it.cnr.jada.bulk.ValidationException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void validaObbligazione(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param1,
            it.cnr.jada.bulk.OggettoBulk param2) throws RemoteException,
            it.cnr.jada.comp.ComponentException {
        try {
            invoke("validaObbligazione",
                    new Object[]{param0, param1, param2});
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

    public void validaTerzo(it.cnr.jada.UserContext param0,
                            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("validaTerzo", new Object[]{param0, param1});
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

    public int validaTerzo(it.cnr.jada.UserContext param0,
                           it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1,
                           boolean param2) throws RemoteException,
            it.cnr.jada.comp.ComponentException {
        try {
            return ((Integer) invoke("validaTerzo", new Object[]{param0,
                    param1, new Boolean(param2)})).intValue();
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

    public boolean isDiariaEditable(it.cnr.jada.UserContext param0,
                                    it.cnr.contab.missioni00.docs.bulk.Missione_tappaBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return ((Boolean) invoke("isDiariaEditable", new Object[]{param0,
                    param1})).booleanValue();
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

    public boolean isTerzoCervellone(it.cnr.jada.UserContext param0,
                                     it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return ((Boolean) invoke("isTerzoCervellone", new Object[]{
                    param0, param1})).booleanValue();
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

    public List recuperoTipi_pasto(UserContext aUC, Timestamp dataTappa, Long inquadramento, NazioneBulk nazione, String tipoPasto, CompoundFindClause clauses) throws ComponentException, java.rmi.RemoteException, PersistencyException {
        try {
            return (List) invoke("recuperoTipi_pasto", new Object[]{
                    aUC, dataTappa, inquadramento, nazione, tipoPasto, clauses});
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

    public java.util.List recuperoTipiSpesa(UserContext aUC, Timestamp dataInizioTappa, Long nazione, Long inquadramento, Boolean rimborsoAmmissibile, String cdTipoSpesa) throws ComponentException, RemoteException, PersistencyException {
        try {
            return (List) invoke("recuperoTipiSpesa", new Object[]{
                    aUC, dataInizioTappa, nazione, inquadramento, rimborsoAmmissibile, cdTipoSpesa});
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

    public List findListaMissioniSIP(UserContext userContext, String query,
                                     String dominio, String uo, String terzo, String voce, String cdr,
                                     String gae, String tipoRicerca, Timestamp data_inizio,
                                     Timestamp data_fine) throws ComponentException, RemoteException {
        try {
            return (List) invoke("findListaMissioniSIP", new Object[]{
                    userContext, query, dominio, uo, terzo, voce, cdr, gae,
                    tipoRicerca, data_inizio, data_fine});
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

    public it.cnr.contab.config00.bulk.Parametri_cnrBulk parametriCnr(it.cnr.jada.UserContext param0) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.config00.bulk.Parametri_cnrBulk) invoke("parametriCnr", new Object[]{
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

    public void archiviaStampa(UserContext userContext, Date fromDate,
                               Date untilDate, MissioneBulk missioneBulk, Integer... years) throws ComponentException,
            java.rmi.RemoteException {
        try {
            invoke("archiviaStampa", new Object[]{userContext, fromDate,
                    untilDate, missioneBulk, years});
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

    public java.math.BigDecimal calcolaMinutiTappa(it.cnr.jada.UserContext param0,
                                                   it.cnr.contab.missioni00.docs.bulk.Missione_tappaBulk param1) throws RemoteException,
            it.cnr.jada.comp.ComponentException {
        try {
            return ((java.math.BigDecimal) invoke("calcolaMinutiTappa", new Object[]{param0,
                    param1}));
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

    public java.math.BigDecimal recuperoCambio(UserContext param0, String divisa, Timestamp dataInizioMissione) throws RemoteException,
            it.cnr.jada.comp.ComponentException {
        try {
            return ((java.math.BigDecimal) invoke("recuperoCambio", new Object[]{param0,
                    divisa, dataInizioMissione}));
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

    public DivisaBulk recuperoDivisa(it.cnr.jada.UserContext param0, Long nazione, String gruppoInquadramento, Timestamp dataInizioMissione) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (DivisaBulk) invoke("recuperoDivisa", new Object[]{
                    param0, nazione, gruppoInquadramento, dataInizioMissione});
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

    public DivisaBulk getDivisaDefault(it.cnr.jada.UserContext param0) throws RemoteException, it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException {
        try {
            return (DivisaBulk) invoke("getDivisaDefault", new Object[]{
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

    public SQLBuilder selectTipo_pastoByClause(UserContext param0, Timestamp dataTappa, Long inquadramento, NazioneBulk nazione, String tipoSpesa, CompoundFindClause clauses) throws ComponentException, RemoteException, PersistencyException {
        try {
            return ((SQLBuilder) invoke("selectTipo_pastoByClause", new Object[]{param0, dataTappa, inquadramento, nazione, tipoSpesa, clauses}));
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

    public SQLBuilder selectTipo_spesaByClause(UserContext param0, Timestamp dataTappa, Long inquadramento, NazioneBulk nazione, Boolean ammissibileConRimborso, String tipoSpesa, CompoundFindClause clauses) throws ComponentException, RemoteException, PersistencyException {
        try {
            return ((SQLBuilder) invoke("selectTipo_spesaByClause", new Object[]{param0, dataTappa, inquadramento, nazione, ammissibileConRimborso, tipoSpesa, clauses}));
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

    public SQLBuilder selectTipo_autoByClause(UserContext param0, Timestamp dataTappa, NazioneBulk nazione, String tipoAuto, CompoundFindClause clauses) throws ComponentException, java.rmi.RemoteException {
        try {
            return ((SQLBuilder) invoke("selectTipo_autoByClause", new Object[]{param0, dataTappa, nazione, tipoAuto, clauses}));
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

    public Obbligazione_scadenzarioBulk recuperoObbligazioneDaGemis(UserContext aUC, MissioneBulk missione) throws ComponentException, java.rmi.RemoteException {
        try {
            return ((Obbligazione_scadenzarioBulk) invoke("recuperoObbligazioneDaGemis", new Object[]{aUC, missione}));
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

    public AnticipoBulk recuperoAnticipoDaGemis(UserContext aUC, MissioneBulk missione) throws ComponentException, java.rmi.RemoteException {
        try {
            return ((AnticipoBulk) invoke("recuperoAnticipoDaGemis", new Object[]{aUC, missione}));
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

    public MissioneBulk caricaTerzoInModificaMissione(UserContext userContext, MissioneBulk missioneBulk) throws ComponentException,
            java.rmi.RemoteException {
        try {
            return (MissioneBulk)invoke("caricaTerzoInModificaMissione", new Object[] { userContext, missioneBulk});
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
    public void cancellazioneMissioneDaGemis(UserContext userContext, Long idRimborsoMissioneGemis) throws ComponentException,
    java.rmi.RemoteException {
    	try {
    		invoke("cancellazioneMissioneDaGemis", new Object[] { userContext, idRimborsoMissioneGemis});
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
