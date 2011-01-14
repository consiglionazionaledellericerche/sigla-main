package it.cnr.contab.progettiric00.ejb;

import javax.ejb.Remote;

@Remote
public interface ProgettoRicercaModuloComponentSession extends it.cnr.contab.progettiric00.ejb.ProgettoRicercaComponentSession {
	it.cnr.jada.util.RemoteIterator getChildren(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.util.RemoteIterator getChildrenWorkpackage(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.bulk.OggettoBulk getParent(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	boolean isLeaf(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.progettiric00.core.bulk.ProgettoBulk cercaWorkpackages(it.cnr.jada.UserContext param0,it.cnr.contab.progettiric00.core.bulk.ProgettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.util.RemoteIterator cercaModuliForWorkpackage(it.cnr.jada.UserContext usercontext,it.cnr.jada.persistency.sql.CompoundFindClause compoundfindclause,it.cnr.jada.bulk.OggettoBulk oggettobulk) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
    it.cnr.contab.config00.sto.bulk.DipartimentoBulk getDipartimentoModulo(it.cnr.jada.UserContext param0,it.cnr.contab.progettiric00.core.bulk.ProgettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
