package it.cnr.contab.config00.ejb;

import java.rmi.RemoteException;

public class TransactionalConfigurazione_cnrComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements Configurazione_cnrComponentSession {
    public it.cnr.contab.config00.bulk.Configurazione_cnrBulk getConfigurazione(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.config00.bulk.Configurazione_cnrBulk) invoke("getConfigurazione", new Object[]{
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

    public java.sql.Timestamp getDt01(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.sql.Timestamp) invoke("getDt01", new Object[]{
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

    public java.sql.Timestamp getDt01(it.cnr.jada.UserContext param0, java.lang.String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.sql.Timestamp) invoke("getDt01", new Object[]{
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

    public java.sql.Timestamp getDt02(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.sql.Timestamp) invoke("getDt02", new Object[]{
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

    public java.sql.Timestamp getDt02(it.cnr.jada.UserContext param0, java.lang.String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.sql.Timestamp) invoke("getDt02", new Object[]{
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

    public java.math.BigDecimal getIm01(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.math.BigDecimal) invoke("getIm01", new Object[]{
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

    public java.math.BigDecimal getIm01(it.cnr.jada.UserContext param0, java.lang.String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.math.BigDecimal) invoke("getIm01", new Object[]{
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

    public java.math.BigDecimal getIm02(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.math.BigDecimal) invoke("getIm02", new Object[]{
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

    public java.math.BigDecimal getIm02(it.cnr.jada.UserContext param0, java.lang.String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.math.BigDecimal) invoke("getIm02", new Object[]{
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

    public java.lang.String getVal01(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getVal01", new Object[]{
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

    public java.lang.String getVal01(it.cnr.jada.UserContext param0, java.lang.String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getVal01", new Object[]{
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

    public java.lang.String getVal02(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getVal02", new Object[]{
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

    public java.lang.String getVal02(it.cnr.jada.UserContext param0, java.lang.String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getVal02", new Object[]{
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

    public java.lang.String getVal03(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getVal03", new Object[]{
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

    public java.lang.String getVal03(it.cnr.jada.UserContext param0, java.lang.String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getVal03", new Object[]{
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

    public java.lang.String getVal04(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getVal04", new Object[]{
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

    public java.lang.String getVal04(it.cnr.jada.UserContext param0, java.lang.String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.String) invoke("getVal04", new Object[]{
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

    public java.lang.Boolean isAttivoOrdini(it.cnr.jada.UserContext param0) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.lang.Boolean) invoke("isAttivoOrdini", new Object[]{
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
