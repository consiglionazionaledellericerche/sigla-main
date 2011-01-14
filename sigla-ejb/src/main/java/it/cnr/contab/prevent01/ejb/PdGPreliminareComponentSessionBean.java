package it.cnr.contab.prevent01.ejb;
import it.cnr.contab.prevent01.comp.PdGPreliminareComponent;

import javax.annotation.PostConstruct;
import javax.ejb.*;
@Stateless(name="CNRPREVENT01_EJB_PdGPreliminareComponentSession")
public class PdGPreliminareComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements PdGPreliminareComponentSession{
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.prevent01.comp.PdGPreliminareComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
	return new PdGPreliminareComponentSessionBean();
}
public it.cnr.jada.bulk.OggettoBulk cambiaStatoConBulk(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_esercizioBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.jada.bulk.OggettoBulk result = ((PdGPreliminareComponent)componentObj).cambiaStatoConBulk(param0,param1);
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
public it.cnr.jada.bulk.OggettoBulk riportaStatoPrecedenteConBulk(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_esercizioBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.jada.bulk.OggettoBulk result = ((PdGPreliminareComponent)componentObj).riportaStatoPrecedenteConBulk(param0,param1);
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
