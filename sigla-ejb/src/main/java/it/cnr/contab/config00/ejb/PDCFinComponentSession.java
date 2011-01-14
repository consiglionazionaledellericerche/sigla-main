package it.cnr.contab.config00.ejb;

import javax.ejb.Remote;

@Remote
public interface PDCFinComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession  {
it.cnr.jada.util.OrderedHashtable loadTipologieCdsKeys(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String getVoceCnr(it.cnr.jada.UserContext param0, it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk param1, it.cnr.contab.config00.latt.bulk.WorkpackageBulk param2)  throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListaVociWS(it.cnr.jada.UserContext userContext,String uo,String tipo,String query,String dominio,String tipoRicerca)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListaVociWS(it.cnr.jada.UserContext userContext,String uo,String tipo,String query,String dominio,String tipoRicerca,String tipoFiltro)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
