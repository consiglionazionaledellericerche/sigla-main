package it.cnr.contab.config00.ejb;
import java.rmi.RemoteException;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

import javax.ejb.EJBException;
import javax.ejb.Remote;

@Remote
public interface Linea_attivitaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.config00.latt.bulk.WorkpackageBulk creaLineaAttivitaCSSAC(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.CdrBulk param1,it.cnr.contab.config00.latt.bulk.WorkpackageBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.latt.bulk.WorkpackageBulk creaLineaAttivitaSAUO(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.CdrBulk param1,it.cnr.contab.config00.latt.bulk.WorkpackageBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.latt.bulk.WorkpackageBulk creaLineaAttivitaSAUOP(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.latt.bulk.WorkpackageBulk inizializzaNaturaPerInsieme(it.cnr.jada.UserContext param0,it.cnr.contab.config00.latt.bulk.WorkpackageBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.latt.bulk.WorkpackageBulk inizializzaNature(it.cnr.jada.UserContext param0,it.cnr.contab.config00.latt.bulk.WorkpackageBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListaGAEWS(it.cnr.jada.UserContext userContext,String cdr,String tipo,String query,String dominio,String tipoRicerca)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListaGAEWS(it.cnr.jada.UserContext userContext,String cdr,String tipo,String query,String dominio,String tipoRicerca,String tipoFiltro)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
WorkpackageBulk completaOggetto(UserContext userContext,WorkpackageBulk linea)throws ComponentException, PersistencyException,java.rmi.RemoteException;
java.util.List findListaGAEFEWS(it.cnr.jada.UserContext userContext,String cdr,Integer modulo)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
