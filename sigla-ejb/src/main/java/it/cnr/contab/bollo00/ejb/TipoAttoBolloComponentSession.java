package it.cnr.contab.bollo00.ejb;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import javax.ejb.Remote;

import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloBulk;

/**
 * Remote interface for Enterprise Bean: CNRBOLLO00_EJB_TipoAttoBolloComponentSession
 */
@Remote
public interface TipoAttoBolloComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
    public Tipo_atto_bolloBulk getTipoAttoBollo(it.cnr.jada.UserContext param0, Timestamp data, java.lang.String codiceTipoAttoBollo) throws RemoteException, it.cnr.jada.comp.ComponentException;
}
