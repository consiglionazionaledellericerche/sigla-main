package it.cnr.contab.ordmag.ejb;

import javax.ejb.Remote;

import it.cnr.contab.ordmag.anag00.NumerazioneMagBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdBulk;

@Remote
public interface NumeratoriOrdMagComponentSession  extends it.cnr.jada.ejb.CRUDComponentSession {
java.lang.Long getNextPG(it.cnr.jada.UserContext param0,NumerazioneMagBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.Integer getNextPG(it.cnr.jada.UserContext param0,NumerazioneOrdBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
