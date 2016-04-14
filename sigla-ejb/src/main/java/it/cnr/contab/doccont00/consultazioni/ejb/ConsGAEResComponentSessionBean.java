package it.cnr.contab.doccont00.consultazioni.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.doccont00.consultazioni.comp.ConsGAEResComponent;


@Stateless(name="CNRDOCCONT00_EJB_ConsGAEResComponentSession")
public class ConsGAEResComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ConsGAEResComponentSession  {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.doccont00.consultazioni.comp.ConsGAEResComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new ConsGAEResComponentSessionBean();
	}
	public it.cnr.jada.util.RemoteIterator findConsultazioneResEtr(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.String param2, it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((ConsGAEResComponent)componentObj).findConsultazioneResEtr(param0,param1,param2,param3,param4);
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
	public it.cnr.jada.util.RemoteIterator findConsultazioneResSpe(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.String param2, it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((ConsGAEResComponent)componentObj).findConsultazioneResSpe(param0,param1,param2,param3,param4);
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

