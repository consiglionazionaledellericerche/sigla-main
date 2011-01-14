package it.cnr.contab.inventario00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRINVENTARIO00_EJB_TipoCaricoScaricoComponentSession")
public class TipoCaricoScaricoComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements TipoCaricoScaricoComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.inventario00.comp.TipoCaricoScaricoComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new TipoCaricoScaricoComponentSessionBean();
}
}
