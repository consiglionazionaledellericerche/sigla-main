package it.cnr.contab.inventario00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.inventario00.comp.IdInventarioComponent;
@Stateless(name="CNRINVENTARIO00_EJB_IdInventarioComponentSession")
public class IdInventarioComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements IdInventarioComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.inventario00.comp.IdInventarioComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new IdInventarioComponentSessionBean();
}
public it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk findInventarioFor(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,boolean param3) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk result = ((IdInventarioComponent)componentObj).findInventarioFor(param0,param1,param2,param3);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public boolean isAperto(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk param1,java.lang.Integer param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		boolean result = ((IdInventarioComponent)componentObj).isAperto(param0,param1,param2);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
}
