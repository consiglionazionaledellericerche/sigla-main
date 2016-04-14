package it.cnr.contab.missioni00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.missioni00.comp.MissioneRimborsoKmComponent;
@Stateless(name="CNRMISSIONI00_EJB_MissioneRimborsoKmComponentSession")
public class MissioneRimborsoKmComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements MissioneRimborsoKmComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.missioni00.comp.MissioneRimborsoKmComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new MissioneRimborsoKmComponentSessionBean();
}
public it.cnr.contab.missioni00.tabrif.bulk.Missione_rimborso_kmBulk gestioneNazione(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.tabrif.bulk.Missione_rimborso_kmBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.contab.missioni00.tabrif.bulk.Missione_rimborso_kmBulk result = ((MissioneRimborsoKmComponent)componentObj).gestioneNazione(param0,param1);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
}
