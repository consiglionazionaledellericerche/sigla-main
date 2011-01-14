package it.cnr.contab.inventario00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRINVENTARIO00_EJB_InventarioConsegnatarioComponentSession")
public class InventarioConsegnatarioComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements InventarioConsegnatarioComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.inventario00.comp.InventarioConsegnatarioComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new InventarioConsegnatarioComponentSessionBean();
}
}
