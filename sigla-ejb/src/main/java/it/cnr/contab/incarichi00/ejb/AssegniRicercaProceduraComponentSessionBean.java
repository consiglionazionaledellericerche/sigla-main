package it.cnr.contab.incarichi00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

/**
 * Bean implementation class for Enterprise Bean: CNRINCARICHI00_EJB_AssegniRicercaProceduraComponentSession
 */
@Stateless(name="CNRINCARICHI00_EJB_AssegniRicercaProceduraComponentSession")
public class AssegniRicercaProceduraComponentSessionBean extends IncarichiProceduraComponentSessionBean implements AssegniRicercaProceduraComponentSession {
	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new it.cnr.contab.incarichi00.comp.AssegniRicercaProceduraComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new AssegniRicercaProceduraComponentSessionBean();
	}
}
