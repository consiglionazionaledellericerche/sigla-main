package it.cnr.contab.compensi00.ejb;

import javax.ejb.Remote;

@Remote
public interface ScaglioneComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
boolean isUltimoIntervallo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.tabrif.bulk.ScaglioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaScaglione(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
