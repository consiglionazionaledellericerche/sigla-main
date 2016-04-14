package it.cnr.contab.config00.ejb;

import it.cnr.contab.config00.comp.CodiciSiopeComponent;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

/**
 * Bean implementation class for Enterprise Bean: CNRINCARICHI00_EJB_IncarichiProceduraComponentSession
 */
@Stateless(name="CNRCONFIG00_EJB_CODICI_SIOPE_ComponentSession")
public class CodiciSiopeComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements CodiciSiopeComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new CodiciSiopeComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new CodiciSiopeComponentSessionBean();
	}
}
