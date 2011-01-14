package it.cnr.contab.config00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRCONFIG00_EJB_PDCEconPatrComponentSession")
public class PDCEconPatrComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements PDCEconPatrComponentSession{
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.config00.comp.PDCEconPatrComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new PDCEconPatrComponentSessionBean();
}
}
