package it.cnr.contab.inventario00.ejb;

import javax.ejb.Remote;

@Remote
public interface IdInventarioComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk findInventarioFor(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,boolean param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isAperto(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk param1,java.lang.Integer param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
