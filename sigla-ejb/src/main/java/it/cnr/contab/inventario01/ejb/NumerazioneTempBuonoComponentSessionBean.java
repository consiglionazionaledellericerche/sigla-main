package it.cnr.contab.inventario01.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario01.comp.NumerazioneTempBuonoComponent;
@Stateless(name="CNRINVENTARIO01_EJB_NumerazioneTempBuonoComponentSession")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class NumerazioneTempBuonoComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements NumerazioneTempBuonoComponentSession {
@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
	componentObj = new NumerazioneTempBuonoComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new NumerazioneTempBuonoComponentSessionBean();
}
public java.lang.Long getNextTempPG(it.cnr.jada.UserContext param0,Buono_carico_scaricoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		java.lang.Long result = ((NumerazioneTempBuonoComponent)componentObj).getNextTempPG(param0,param1);
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
