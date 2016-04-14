package it.cnr.contab.compensi00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRCOMPENSI00_EJB_DetrazioniFamiliariComponentSession")
public class DetrazioniFamiliariComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements DetrazioniFamiliariComponentSession{
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.compensi00.comp.DetrazioniFamiliariComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new DetrazioniFamiliariComponentSessionBean();
}
}
