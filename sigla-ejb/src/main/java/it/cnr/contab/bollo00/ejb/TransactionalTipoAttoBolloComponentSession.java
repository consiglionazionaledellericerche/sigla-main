package it.cnr.contab.bollo00.ejb;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloBulk;

public class TransactionalTipoAttoBolloComponentSession  extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements TipoAttoBolloComponentSession {
    public Tipo_atto_bolloBulk getTipoAttoBollo(it.cnr.jada.UserContext param0, Timestamp data, java.lang.String codiceTipoAttoBollo) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (Tipo_atto_bolloBulk) invoke("getTipoAttoBollo", new Object[]{
                    param0,
                    data,
                    codiceTipoAttoBollo});
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
