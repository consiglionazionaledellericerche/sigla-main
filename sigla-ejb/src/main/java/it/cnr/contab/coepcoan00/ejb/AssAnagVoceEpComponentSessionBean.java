package it.cnr.contab.coepcoan00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;

@Stateless(name="CNRCOEPCOAN00_EJB_AssAnagVoceEpComponentSession")
public class AssAnagVoceEpComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements AssAnagVoceEpComponentSession{
	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new it.cnr.contab.coepcoan00.comp.AssAnagVoceEpComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}
	
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new AssAnagVoceEpComponentSessionBean();
	}
}
