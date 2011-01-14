package it.cnr.contab.config00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

/**
 * Bean implementation class for Enterprise Bean: CNRCONFIG00_EJB_ClassificazioneComponentSession
 */
@Stateless(name="CNRCONFIG00_EJB_ClassificazioneComponentSession")
public class ClassificazioneComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ClassificazioneComponentSession{
	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new it.cnr.contab.config00.comp.ClassificazioneComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new ClassificazioneComponentSessionBean();
	}
}
