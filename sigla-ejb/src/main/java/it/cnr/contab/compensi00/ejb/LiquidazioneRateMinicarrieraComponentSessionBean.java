package it.cnr.contab.compensi00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.compensi00.comp.LiquidazioneRateMinicarrieraComponent;
@Stateless(name="CNRCOMPENSI00_EJB_LiquidazioneRateMinicarrieraComponentSession")
public class LiquidazioneRateMinicarrieraComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements LiquidazioneRateMinicarrieraComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.compensi00.comp.LiquidazioneRateMinicarrieraComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new LiquidazioneRateMinicarrieraComponentSessionBean();
}
public it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk findVoceF(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.Liquidazione_rate_minicarrieraBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk result = ((LiquidazioneRateMinicarrieraComponent)componentObj).findVoceF(param0,param1);
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
public void liquidaRate(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.Liquidazione_rate_minicarrieraBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		((LiquidazioneRateMinicarrieraComponent)componentObj).liquidaRate(param0,param1,param2);
		component_invocation_succes(param0,componentObj);
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
