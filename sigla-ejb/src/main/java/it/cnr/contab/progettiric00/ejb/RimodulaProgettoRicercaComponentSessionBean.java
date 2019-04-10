package it.cnr.contab.progettiric00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
@Stateless(name="CNRPROGETTIRIC00_EJB_RimodulaProgettoRicercaComponentSession")
public class RimodulaProgettoRicercaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements RimodulaProgettoRicercaComponentSession{
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.progettiric00.comp.RimodulaProgettoRicercaComponent();
	}

	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
		return new RimodulaProgettoRicercaComponentSessionBean();
	}
}