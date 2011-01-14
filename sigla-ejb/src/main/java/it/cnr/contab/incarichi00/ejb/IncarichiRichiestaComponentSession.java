package it.cnr.contab.incarichi00.ejb;

import javax.ejb.Remote;

@Remote
public interface IncarichiRichiestaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.jada.bulk.OggettoBulk pubblicaSulSito(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator findListaIncarichiRichiesta(it.cnr.jada.UserContext param0,String param1,String param2,Integer param3,String param4,String param5,String param6) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator findListaIncarichiCollaborazione(it.cnr.jada.UserContext param0,String param1,String param2,Integer param3,String param4,String param5,String param6) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator findListaIncarichiElenco(it.cnr.jada.UserContext param0,String param1,String param2,Integer param3,String param4,String param5,String param6) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List completaListaIncarichiRichiesta(it.cnr.jada.UserContext param0,java.util.List param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List completaListaIncarichiCollaborazione(it.cnr.jada.UserContext param0,java.util.List param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List completaListaIncarichiElenco(it.cnr.jada.UserContext param0,java.util.List param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
