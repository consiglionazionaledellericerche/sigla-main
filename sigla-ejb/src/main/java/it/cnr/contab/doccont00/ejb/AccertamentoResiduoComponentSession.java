package it.cnr.contab.doccont00.ejb;

import javax.ejb.Remote;

@Remote
public interface AccertamentoResiduoComponentSession extends AccertamentoComponentSession {
	String controllaDettagliScadenzaAccertamento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1,it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	void cancellaAccertamentoModTemporanea(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.Accertamento_modificaBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
}
