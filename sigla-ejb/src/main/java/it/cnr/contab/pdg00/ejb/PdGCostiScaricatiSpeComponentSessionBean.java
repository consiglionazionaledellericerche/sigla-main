package it.cnr.contab.pdg00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRPDG00_EJB_PdGCostiScaricatiSpeComponentSession")
public class PdGCostiScaricatiSpeComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements PdGCostiScaricatiSpeComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.pdg00.comp.PdGCostiScaricatiSpeComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new PdGCostiScaricatiSpeComponentSessionBean();
}
}
