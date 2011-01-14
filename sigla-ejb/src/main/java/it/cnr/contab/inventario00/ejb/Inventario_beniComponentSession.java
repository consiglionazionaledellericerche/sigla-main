package it.cnr.contab.inventario00.ejb;

import javax.ejb.Remote;

@Remote
public interface Inventario_beniComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession {
it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk caricaInventario(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
java.util.Collection findTipiAmmortamento(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
java.lang.String getLocalTransactionID(it.cnr.jada.UserContext param0,boolean param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator selectBeniAccessoriFor(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator selectBuonoFor(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator selectFatturaFor(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
Boolean isContab(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
