package it.cnr.contab.compensi00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;

import it.cnr.jada.ejb.CRUDComponentSessionBean;
@Stateless(name="CNRCOMPENSI00_EJB_AssTipoCORIEvComponentSession")
public class AssTipoCORIEvComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements  AssTipoCORIEvComponentSession{
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.compensi00.comp.AssTipoCORIEvComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}
	
	public static CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new AssTipoCORIEvComponentSessionBean();
	}
}
