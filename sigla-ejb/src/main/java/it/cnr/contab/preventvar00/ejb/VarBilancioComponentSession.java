package it.cnr.contab.preventvar00.ejb;

import java.rmi.RemoteException;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;

import javax.ejb.Remote;

@Remote
public interface VarBilancioComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession {
it.cnr.contab.preventvar00.bulk.Var_bilancioBulk salvaDefinitivo(it.cnr.jada.UserContext param0,it.cnr.contab.preventvar00.bulk.Var_bilancioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.preventvar00.bulk.Var_bilancioBulk creaVariazioneBilancioDiRegolarizzazione(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.preventvar00.bulk.Var_bilancioBulk eliminaVariazione(UserContext param0, OggettoBulk param1) throws it.cnr.jada.comp.ComponentException , it.cnr.jada.persistency.PersistencyException, RemoteException;
}
