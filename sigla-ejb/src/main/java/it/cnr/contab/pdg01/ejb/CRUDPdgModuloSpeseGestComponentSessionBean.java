package it.cnr.contab.pdg01.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.*;
/**
 * Bean implementation class for Enterprise Bean: CNRPDG01_EJB_CRUDPdgModuloSpeseGestComponentSession
 */
@Stateless(name="CNRPDG01_EJB_CRUDPdgModuloSpeseGestComponentSession")
public class CRUDPdgModuloSpeseGestComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements CRUDPdgModuloSpeseGestComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.pdg01.comp.CRUDPdgModuloSpeseGestComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
	return new CRUDPdgModuloSpeseGestComponentSessionBean();
}
}

