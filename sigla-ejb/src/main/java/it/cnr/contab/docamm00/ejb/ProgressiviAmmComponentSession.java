package it.cnr.contab.docamm00.ejb;

import javax.ejb.Remote;

@Remote
public interface ProgressiviAmmComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
java.lang.Long getNextPG(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
