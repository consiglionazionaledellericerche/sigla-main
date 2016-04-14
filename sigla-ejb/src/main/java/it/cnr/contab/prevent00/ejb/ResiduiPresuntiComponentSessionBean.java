package it.cnr.contab.prevent00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.*;
@Stateless(name="CNRPREVENT00_EJB_ResiduiPresuntiComponentSession")
public class ResiduiPresuntiComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ResiduiPresuntiComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.prevent00.comp.ResiduiPresuntiComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
	return new ResiduiPresuntiComponentSessionBean();
}
}
