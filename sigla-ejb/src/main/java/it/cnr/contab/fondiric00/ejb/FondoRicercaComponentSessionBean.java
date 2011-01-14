package it.cnr.contab.fondiric00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRFONDIRIC00_EJB_FondoRicercaComponentSession")
public class FondoRicercaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements FondoRicercaComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.fondiric00.comp.FondoRicercaComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new FondoRicercaComponentSessionBean();
}
}
