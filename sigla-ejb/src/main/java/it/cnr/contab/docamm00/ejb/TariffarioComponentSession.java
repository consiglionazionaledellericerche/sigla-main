package it.cnr.contab.docamm00.ejb;

import javax.ejb.Remote;

@Remote
public interface TariffarioComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
java.util.List findListaTariffariWS(it.cnr.jada.UserContext userContext,String uo,String query,String dominio,String tipoRicerca)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
