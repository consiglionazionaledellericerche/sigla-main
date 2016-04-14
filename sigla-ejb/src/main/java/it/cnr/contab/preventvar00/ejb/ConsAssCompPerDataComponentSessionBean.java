package it.cnr.contab.preventvar00.ejb;

import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.preventvar00.comp.ConsAssCompPerDataComponent;
import it.cnr.contab.preventvar00.consultazioni.bulk.V_cons_ass_comp_per_dataBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;


@Stateless(name="CNRPREVENTVAR00_EJB_ConsAssCompPerDataComponentSession")
public class ConsAssCompPerDataComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ConsAssCompPerDataComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new ConsAssCompPerDataComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new ConsAssCompPerDataComponentSessionBean();
	}

public it.cnr.jada.util.RemoteIterator findVariazioniDettaglio(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.String param2, it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4, OggettoBulk param5) 
		throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.jada.util.RemoteIterator result = ((ConsAssCompPerDataComponent)componentObj).findVariazioniDettaglio(param0,param1,param2,param3,param4,param5);
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

public it.cnr.jada.util.RemoteIterator findVariazioni(UserContext param0, OggettoBulk param1) 
		throws  ComponentException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.jada.util.RemoteIterator result = ((ConsAssCompPerDataComponent)componentObj).findVariazioni(param0,param1);
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
