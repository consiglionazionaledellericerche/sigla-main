package it.cnr.contab.config00.ejb;
import javax.ejb.Remote;
@Remote
public interface Unita_organizzativaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.jada.bulk.OggettoBulk findUOByCodice(it.cnr.jada.UserContext param0,String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk getUoEnte(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String getIndirizzoUnitaOrganizzativa(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param1,boolean param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListaUOWS(it.cnr.jada.UserContext userContext,String query,String dominio,String tipoRicerca, String cds)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
