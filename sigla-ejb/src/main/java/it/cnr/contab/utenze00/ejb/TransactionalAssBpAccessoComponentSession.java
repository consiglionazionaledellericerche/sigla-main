package it.cnr.contab.utenze00.ejb;

import java.rmi.RemoteException;
import java.util.List;

public class TransactionalAssBpAccessoComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements AssBpAccessoComponentSession {
    public java.util.List findAccessoByBP(it.cnr.jada.UserContext param0, String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (List) invoke("findAccessoByBP", new Object[]{
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

    public it.cnr.contab.utenze00.bulk.AssBpAccessoBulk finAssBpAccesso(it.cnr.jada.UserContext param0, String param1, String param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.utenze00.bulk.AssBpAccessoBulk) invoke("finAssBpAccesso", new Object[]{
                    param0,
                    param1,
                    param2
            });
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
