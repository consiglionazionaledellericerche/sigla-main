package it.cnr.contab.compensi00.ejb;

import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;

import it.cnr.contab.compensi00.comp.AssGruppoCRUOComponent;
import it.cnr.contab.compensi00.tabrif.bulk.Gruppo_cr_uoBulk;
import it.cnr.jada.ejb.CRUDComponentSessionBean;
@Stateless(name="CNRCOMPENSI00_EJB_AssGruppoCRUOComponentSession")
public class AssGruppoCRUOComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements AssGruppoCRUOComponentSession {
	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new it.cnr.contab.compensi00.comp.AssGruppoCRUOComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}
	
	public static CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new AssGruppoCRUOComponentSessionBean();
	}

}
