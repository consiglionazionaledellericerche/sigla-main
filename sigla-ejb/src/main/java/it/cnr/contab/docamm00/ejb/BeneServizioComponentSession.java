package it.cnr.contab.docamm00.ejb;

import javax.ejb.Remote;

@Remote
public interface BeneServizioComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk completaElementoVoceOf(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
