package it.cnr.contab.doccont00.ejb;

import javax.ejb.Remote;

@Remote
public interface ObbligazioneResComponentSession extends ObbligazioneComponentSession {
String controllaDettagliScadenzaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void cancellaObbligazioneModTemporanea(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
}
