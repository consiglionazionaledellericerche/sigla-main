package it.cnr.contab.missioni00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.missioni00.comp.MissioneDiariaComponent;
@Stateless(name="CNRMISSIONI00_EJB_MissioneQuotaRimborsoComponentSession")
public class MissioneQuotaRimborsoComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements MissioneQuotaRimborsoComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.missioni00.comp.MissioneQuotaRimborsoComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new MissioneQuotaRimborsoComponentSessionBean();
}

}
