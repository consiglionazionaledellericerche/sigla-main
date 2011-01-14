package it.cnr.contab.config00.ejb;

import javax.ejb.Remote;

@Remote
public interface CDRComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
boolean isCdrEnte(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isEnte(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.sto.bulk.CdrBulk cdrFromUserContext(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.sto.bulk.CdrBulk getCdrEnte(it.cnr.jada.UserContext userContext)throws it.cnr.jada.comp.ComponentException;
java.util.List findListaCDRWS(it.cnr.jada.UserContext userContext,String uo,String query,String dominio,String tipoRicerca)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
