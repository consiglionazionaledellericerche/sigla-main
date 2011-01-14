package it.cnr.contab.config00.ejb;

import javax.ejb.Remote;

@Remote
public interface EsercizioComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.config00.esercizio.bulk.EsercizioBulk apriPianoDiGestione(it.cnr.jada.UserContext param0,it.cnr.contab.config00.esercizio.bulk.EsercizioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.esercizio.bulk.EsercizioBulk cambiaStatoConBulk(it.cnr.jada.UserContext param0,it.cnr.contab.config00.esercizio.bulk.EsercizioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isEsercizioChiuso(it.cnr.jada.UserContext userContext)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
