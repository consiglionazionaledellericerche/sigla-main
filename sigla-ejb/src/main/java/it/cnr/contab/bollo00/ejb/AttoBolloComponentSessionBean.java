package it.cnr.contab.bollo00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

/**
 * Bean implementation class for Enterprise Bean: CNRBOLLO00_EJB_AttoBolloComponentSession
 */
@Stateless(name="CNRBOLLO00_EJB_AttoBolloComponentSession")
public class AttoBolloComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements it.cnr.contab.bollo00.ejb.AttoBolloComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.bollo00.comp.AttoBolloComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new AttoBolloComponentSessionBean();
	}
}
