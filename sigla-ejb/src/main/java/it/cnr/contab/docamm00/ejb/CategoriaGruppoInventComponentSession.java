package it.cnr.contab.docamm00.ejb;

import javax.ejb.Remote;

@Remote
public interface CategoriaGruppoInventComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk completaElementoVoceOf(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk findElementoVoce(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk findVoce_f(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator getChildren(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk getParent(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isLeaf(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
