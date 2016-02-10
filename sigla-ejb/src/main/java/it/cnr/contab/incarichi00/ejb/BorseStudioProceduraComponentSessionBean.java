package it.cnr.contab.incarichi00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

/**
 * Bean implementation class for Enterprise Bean: CNRINCARICHI00_EJB_BorseStudioProceduraComponentSession
 */
@Stateless(name="CNRINCARICHI00_EJB_BorseStudioProceduraComponentSession")
public class BorseStudioProceduraComponentSessionBean extends IncarichiProceduraComponentSessionBean implements BorseStudioProceduraComponentSession {
	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new it.cnr.contab.incarichi00.comp.BorseStudioProceduraComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new BorseStudioProceduraComponentSessionBean();
	}
}
