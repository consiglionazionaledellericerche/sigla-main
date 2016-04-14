package it.cnr.contab.missioni00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name="CNRMISSIONI00_EJB_MissioneTariffarioAutoComponentSession")
public class MissioneTariffarioAutoComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements MissioneTariffarioAutoComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.missioni00.comp.MissioneTariffarioAutoComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new MissioneTariffarioAutoComponentSessionBean();
}
}
