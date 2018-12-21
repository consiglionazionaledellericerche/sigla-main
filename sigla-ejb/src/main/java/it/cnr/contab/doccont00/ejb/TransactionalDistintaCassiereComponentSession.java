package it.cnr.contab.doccont00.ejb;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk;
import it.cnr.contab.doccont00.intcass.bulk.ExtCassiereCdsBulk;
import it.cnr.contab.doccont00.intcass.bulk.Ext_cassiere00_logsBulk;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

import java.rmi.RemoteException;
import java.util.List;

public class TransactionalDistintaCassiereComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements DistintaCassiereComponentSession {
    public void annullaModificaDettagliDistinta(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("annullaModificaDettagliDistinta", new Object[]{
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

    public void associaTuttiDocContabili(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk param1, it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("associaTuttiDocContabili", new Object[]{
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

    public it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk calcolaTotali(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk) invoke("calcolaTotali", new Object[]{
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

    public it.cnr.contab.doccont00.intcass.bulk.V_ext_cassiere00Bulk caricaLogs(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.intcass.bulk.V_ext_cassiere00Bulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.doccont00.intcass.bulk.V_ext_cassiere00Bulk) invoke("caricaLogs", new Object[]{
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

    public it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1, java.lang.Class param2, it.cnr.jada.bulk.OggettoBulk param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
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

    public it.cnr.jada.util.RemoteIterator cercaFile_Cassiere(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("cercaFile_Cassiere", new Object[]{
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

    public it.cnr.jada.util.RemoteIterator cercaMandatiEReversali(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1, it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk param2, it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk param3) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("cercaMandatiEReversali", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3});
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

    public it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1, it.cnr.jada.bulk.OggettoBulk param2, java.lang.String param3) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("creaConBulk", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3});
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

    public void eliminaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1, java.lang.String param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("eliminaConBulk", new Object[]{
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

    public void eliminaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk[] param1, it.cnr.jada.bulk.OggettoBulk param2, java.lang.String param3) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("eliminaConBulk", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3});
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

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1, it.cnr.jada.bulk.OggettoBulk param2, java.lang.String param3) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("inizializzaBulkPerInserimento", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3});
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

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1, it.cnr.jada.bulk.OggettoBulk param2, java.lang.String param3) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("inizializzaBulkPerModifica", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3});
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

    public void inizializzaDettagliDistintaPerModifica(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("inizializzaDettagliDistintaPerModifica", new Object[]{
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

    public void inviaDistinte(it.cnr.jada.UserContext param0, java.util.Collection param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("inviaDistinte", new Object[]{
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

    public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1, it.cnr.jada.bulk.OggettoBulk param2, java.lang.String param3) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("modificaConBulk", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3});
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

    public void modificaDettagliDistinta(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk param1, it.cnr.jada.bulk.OggettoBulk[] param2, java.util.BitSet param3, java.util.BitSet param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("modificaDettagliDistinta", new Object[]{
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

    public it.cnr.contab.doccont00.intcass.bulk.V_ext_cassiere00Bulk processaFile(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.intcass.bulk.V_ext_cassiere00Bulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.doccont00.intcass.bulk.V_ext_cassiere00Bulk) invoke("processaFile", new Object[]{
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

    public it.cnr.contab.config00.bulk.Parametri_cnrBulk parametriCnr(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException {
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

    public it.cnr.jada.util.RemoteIterator selectFileScarti(it.cnr.jada.UserContext param0, Ext_cassiere00_logsBulk param1) throws it.cnr.jada.comp.ComponentException, RemoteException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("selectFileScarti", new Object[]{
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

    public void caricaFile(it.cnr.jada.UserContext param0, java.io.File param1) throws it.cnr.jada.comp.ComponentException, RemoteException {
        try {
            invoke("caricaFile", new Object[]{
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

    public ExtCassiereCdsBulk recuperaCodiciCdsCassiere(UserContext param0,
                                                        Distinta_cassiereBulk param1) throws ComponentException,
            PersistencyException, RemoteException {
        try {
            return (ExtCassiereCdsBulk) invoke("recuperaCodiciCdsCassiere", new Object[]{
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

    public BancaBulk recuperaIbanUo(UserContext param0,
                                    Unita_organizzativaBulk param1) throws ComponentException,
            PersistencyException, RemoteException {
        try {
            return (BancaBulk) invoke("recuperaIbanUo", new Object[]{
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

    public List dettagliDistinta(UserContext param0,
                                 Distinta_cassiereBulk param1, String param2)
            throws PersistencyException, ComponentException, RemoteException {
        try {
            return (List) invoke("dettagliDistinta", new Object[]{
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

    public Distinta_cassiereBulk inviaDistinta(UserContext param0,
                                               Distinta_cassiereBulk param1) throws ComponentException,
            RemoteException {
        try {
            return (Distinta_cassiereBulk) invoke("inviaDistinta", new Object[]{
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

    public List<V_mandato_reversaleBulk> findMandatiCollegati(UserContext param0,
                                                              V_mandato_reversaleBulk param1)
            throws ComponentException, RemoteException {
        try {
            return (List) invoke("findMandatiCollegati", new Object[]{
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

    public List<V_mandato_reversaleBulk> findReversaliCollegate(UserContext param0,
                                                                V_mandato_reversaleBulk param1)
            throws ComponentException, RemoteException {
        try {
            return (List) invoke("findReversaliCollegate", new Object[]{
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

    public List findDocumentiFlusso(UserContext param0,
                                    V_mandato_reversaleBulk param1)
            throws ComponentException, RemoteException {
        try {
            return (List) invoke("findDocumentiFlusso", new Object[]{
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

    public List findDocumentiFlussoClass(UserContext param0,
                                         V_mandato_reversaleBulk param1)
            throws ComponentException, RemoteException {
        try {
            return (List) invoke("findDocumentiFlussoClass", new Object[]{
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

    public List findDocumentiFlussoSospeso(UserContext param0,
                                           V_mandato_reversaleBulk param1)
            throws ComponentException, RemoteException {
        try {
            return (List) invoke("findDocumentiFlussoSospeso", new Object[]{
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

    public List findReversali(UserContext param0,
                              V_mandato_reversaleBulk param1)
            throws ComponentException, RemoteException {
        try {
            return (List) invoke("findReversali", new Object[]{
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

    public List findDocumentiFlussoClassReversali(UserContext param0,
                                                  V_mandato_reversaleBulk param1)
            throws ComponentException, RemoteException {
        try {
            return (List) invoke("findDocumentiFlussoClassReversali", new Object[]{
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

    public void unlockMessaggiSIOPEPlus(UserContext param0)
            throws ComponentException, RemoteException {
        try {
            invoke("unlockMessaggiSIOPEPlus", new Object[]{param0});
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

    public Configurazione_cnrBulk lockMessaggiSIOPEPlus(UserContext param0)
            throws ComponentException, RemoteException {
        try {
            return (Configurazione_cnrBulk) invoke("Configurazione_cnrBulk", new Object[]{
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
}
