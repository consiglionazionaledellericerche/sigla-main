package it.cnr.contab.compensi00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRCOMPENSI00_EJB_DetrazioniLavoroComponentSession")
public class DetrazioniLavoroComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements DetrazioniLavoroComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.compensi00.comp.DetrazioniLavoroComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new DetrazioniLavoroComponentSessionBean();
}
}
