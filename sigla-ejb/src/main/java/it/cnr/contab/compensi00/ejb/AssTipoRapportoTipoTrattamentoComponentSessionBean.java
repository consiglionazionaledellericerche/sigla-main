package it.cnr.contab.compensi00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;

import it.cnr.jada.ejb.CRUDComponentSessionBean;
@Stateless(name="CNRCOMPENSI00_EJB_AssTipoRapportoTipoTrattamentoComponentSession")
public class AssTipoRapportoTipoTrattamentoComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements AssTipoRapportoTipoTrattamentoComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
		componentObj = new it.cnr.contab.compensi00.comp.AssTipoRapportoTipoTrattamentoComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}

	public static CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new AssTipoRapportoTipoTrattamentoComponentSessionBean();
	}
}
