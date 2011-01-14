package it.cnr.contab.inventario00.ejb;

import javax.ejb.Remote;

@Remote
public interface InventarioApChComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.inventario00.docs.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.inventario00.tabrif.bulk.Inventario_ap_chBulk loadInventarioApChAttuale(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
}
