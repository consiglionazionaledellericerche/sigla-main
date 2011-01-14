package it.cnr.contab.config00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRCONFIG00_EJB_Insieme_laComponentSession")
public class Insieme_laComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements Insieme_laComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.config00.comp.Insieme_laComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new Insieme_laComponentSessionBean();
}
}
