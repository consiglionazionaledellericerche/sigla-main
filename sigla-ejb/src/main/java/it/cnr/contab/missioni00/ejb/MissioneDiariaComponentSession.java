package it.cnr.contab.missioni00.ejb;

import javax.ejb.Remote;

@Remote
public interface MissioneDiariaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.missioni00.tabrif.bulk.Missione_diariaBulk gestioneNazione(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.tabrif.bulk.Missione_diariaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
