package it.cnr.contab.missioni00.ejb;

import javax.ejb.Remote;

@Remote
public interface MissioneTipoPastoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_pastoBulk gestioneNazione(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_pastoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
