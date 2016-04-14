package it.cnr.contab.pdg00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRPDG00_EJB_PdGCostiScaricatiEtrComponentSession")
public class PdGCostiScaricatiEtrComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements PdGCostiScaricatiEtrComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.pdg00.comp.PdGCostiScaricatiEtrComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new PdGCostiScaricatiEtrComponentSessionBean();
}
}
