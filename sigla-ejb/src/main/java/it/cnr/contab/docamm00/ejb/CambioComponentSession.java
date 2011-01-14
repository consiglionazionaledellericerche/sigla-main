package it.cnr.contab.docamm00.ejb;

import javax.ejb.Remote;

@Remote
public interface CambioComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
boolean validaCambio(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.CambioBulk param1) throws java.rmi.RemoteException;
}
