package it.cnr.contab.compensi00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRCOMPENSI00_EJB_TipologiaRischioComponentSession")
public class TipologiaRischioComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements TipologiaRischioComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.compensi00.comp.TipologiaRischioComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new TipologiaRischioComponentSessionBean();
}
}
