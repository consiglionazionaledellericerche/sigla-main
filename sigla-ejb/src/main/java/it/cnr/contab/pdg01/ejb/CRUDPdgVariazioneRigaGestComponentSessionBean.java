package it.cnr.contab.pdg01.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.*;
/**
 * Bean implementation class for Enterprise Bean: CNRPDG01_EJB_CRUDPdgVariazioneRigaGestComponentSession
 */
@Stateless(name="CNRPDG01_EJB_CRUDPdgVariazioneRigaGestComponentSession")
public class CRUDPdgVariazioneRigaGestComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean  implements CRUDPdgVariazioneRigaGestComponentSession{
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.pdg01.comp.CRUDPdgVariazioneRigaGestComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
	return new CRUDPdgVariazioneRigaGestComponentSessionBean();
}
}

