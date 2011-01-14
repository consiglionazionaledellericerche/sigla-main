package it.cnr.contab.compensi00.ejb;

import javax.ejb.Remote;

@Remote
public interface TipoTrattamentoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
java.util.List caricaIntervalli(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isUltimoIntervallo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
