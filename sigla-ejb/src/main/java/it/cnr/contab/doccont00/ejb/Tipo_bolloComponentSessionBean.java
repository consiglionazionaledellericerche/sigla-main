package it.cnr.contab.doccont00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRDOCCONT00_EJB_Tipo_bolloComponentSession")
public class Tipo_bolloComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements Tipo_bolloComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.doccont00.comp.Tipo_bolloComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new Tipo_bolloComponentSessionBean();
}
}
