package it.cnr.contab.pdg01.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import it.cnr.contab.pdg01.comp.CRUDPdgVariazioneRigaGestComponent;
/**
 * Bean implementation class for Enterprise Bean: CNRPDG01_EJB_CRUDPdgVariazioneRigaGestComponentSession
 */
@Stateless(name="CNRPDG01_EJB_CRUDPdgVariazioneRigaGestComponentSession")
public class CRUDPdgVariazioneRigaGestComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean  implements CRUDPdgVariazioneRigaGestComponentSession{
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.pdg01.comp.CRUDPdgVariazioneRigaGestComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
	return new CRUDPdgVariazioneRigaGestComponentSessionBean();
}
public it.cnr.contab.progettiric00.core.bulk.ProgettoBulk getProgettoLineaAttivita(it.cnr.jada.UserContext param0,it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.contab.progettiric00.core.bulk.ProgettoBulk result = ((CRUDPdgVariazioneRigaGestComponent)componentObj).getProgettoLineaAttivita(param0,param1);
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
