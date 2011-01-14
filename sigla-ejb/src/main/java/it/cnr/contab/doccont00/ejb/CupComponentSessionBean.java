package it.cnr.contab.doccont00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.doccont00.comp.CupComponent;
@Stateless(name="CNRDOCCONT00_EJB_CupComponentSession")
public class CupComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements CupComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.doccont00.comp.CupComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new CupComponentSessionBean();
}

}
