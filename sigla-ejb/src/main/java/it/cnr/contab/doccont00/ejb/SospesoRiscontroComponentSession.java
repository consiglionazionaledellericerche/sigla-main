package it.cnr.contab.doccont00.ejb;

import javax.ejb.Remote;

@Remote
public interface SospesoRiscontroComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession {
void cambiaStato(it.cnr.jada.UserContext param0,java.util.Collection param1,java.lang.String param2,java.lang.String param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
