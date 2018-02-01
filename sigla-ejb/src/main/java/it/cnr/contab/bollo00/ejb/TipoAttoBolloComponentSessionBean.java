package it.cnr.contab.bollo00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

/**
 * Bean implementation class for Enterprise Bean: CNRBOLLO00_EJB_TipoAttoBolloComponentSession
 */
@Stateless(name="CNRBOLLO00_EJB_TipoAttoBolloComponentSession")
public class TipoAttoBolloComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements it.cnr.contab.bollo00.ejb.TipoAttoBolloComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.bollo00.comp.TipoAttoBolloComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new TipoAttoBolloComponentSessionBean();
	}
}
