package it.cnr.contab.pdg00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRPDG00_EJB_PdGEntrateFigurativeComponentSession")
public class PdGEntrateFigurativeComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements PdGEntrateFigurativeComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.pdg00.comp.PdGEntrateFigurativeComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new PdGEntrateFigurativeComponentSessionBean();
}
}
