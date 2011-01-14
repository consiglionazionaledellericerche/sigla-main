package it.cnr.contab.pdg01.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.*;
/**
 * Bean implementation class for Enterprise Bean: CNRPDG01_EJB_CRUDPdgModuloEntrateGestComponentSession
 */
@Stateless(name="CNRPDG01_EJB_CRUDPdgModuloEntrateGestComponentSession")
public class CRUDPdgModuloEntrateGestComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements CRUDPdgModuloEntrateGestComponentSession{
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.pdg01.comp.CRUDPdgModuloEntrateGestComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
	return new CRUDPdgModuloEntrateGestComponentSessionBean();
}
}

