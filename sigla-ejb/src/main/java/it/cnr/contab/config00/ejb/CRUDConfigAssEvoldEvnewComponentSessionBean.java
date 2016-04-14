package it.cnr.contab.config00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRCONFIG00_EJB_CRUDConfigAssEvoldEvnewComponentSession")
public class CRUDConfigAssEvoldEvnewComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements CRUDConfigAssEvoldEvnewComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.config00.comp.CRUDConfigAssEvoldEvnewComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new CRUDConfigAssEvoldEvnewComponentSessionBean();
	}
}
