package it.cnr.contab.anagraf00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRANAGRAF00_EJB_ComuneComponentSession")
public class ComuneComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ComuneComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.anagraf00.comp.ComuneComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new ComuneComponentSessionBean();
}
}
