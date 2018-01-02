package it.cnr.contab.config00.ejb;

import javax.ejb.Remote;

@Remote
public interface Configurazione_cnrComponentSession extends it.cnr.jada.ejb.GenericComponentSession {
    it.cnr.contab.config00.bulk.Configurazione_cnrBulk getConfigurazione(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.sql.Timestamp getDt01(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.sql.Timestamp getDt01(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.sql.Timestamp getDt02(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.sql.Timestamp getDt02(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.math.BigDecimal getIm01(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.math.BigDecimal getIm01(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.math.BigDecimal getIm02(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.math.BigDecimal getIm02(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getVal01(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getVal01(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getVal02(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getVal02(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getVal03(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getVal03(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getVal04(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2, java.lang.String param3, java.lang.String param4) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.String getVal04(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.lang.Boolean isAttivoOrdini(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;
}
