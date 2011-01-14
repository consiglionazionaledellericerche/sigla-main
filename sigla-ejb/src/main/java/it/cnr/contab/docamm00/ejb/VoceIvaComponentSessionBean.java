package it.cnr.contab.docamm00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.docamm00.comp.VoceIvaComponent;
@Stateless(name="CNRDOCAMM00_EJB_VoceIvaComponentSession")
public class VoceIvaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements VoceIvaComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new it.cnr.contab.docamm00.comp.VoceIvaComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new VoceIvaComponentSessionBean();
}
public it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk caricaVoceIvaDefault(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk result = ((VoceIvaComponent)componentObj).caricaVoceIvaDefault(param0);
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
public boolean validaVoceIva(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk param1) throws javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		boolean result = ((VoceIvaComponent)componentObj).validaVoceIva(param0,param1);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public java.util.List findListaVoceIVAWS(it.cnr.jada.UserContext param0,String param1,String param2,String param3) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		java.util.List result = ((VoceIvaComponent)componentObj).findListaVoceIVAWS(param0,param1,param2,param3);
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
