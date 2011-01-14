package it.cnr.contab.messaggio00.ejb;

import javax.ejb.Remote;

@Remote
public interface CRUDMessaggioComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
void setMessaggioVisionato(it.cnr.jada.UserContext param0,it.cnr.contab.messaggio00.bulk.MessaggioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isMessaggioVisionato(it.cnr.jada.UserContext param0,it.cnr.contab.messaggio00.bulk.MessaggioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
