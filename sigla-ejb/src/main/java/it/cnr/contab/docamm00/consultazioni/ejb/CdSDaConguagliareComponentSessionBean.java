package it.cnr.contab.docamm00.consultazioni.ejb;
import java.util.List;

import it.cnr.contab.docamm00.consultazioni.bulk.V_terzi_da_conguagliareBulk;
import it.cnr.contab.docamm00.consultazioni.comp.CdSDaConguagliareComponent;
import it.cnr.contab.docamm00.consultazioni.comp.MonitoCococoComponent;

import javax.annotation.PostConstruct;
import javax.ejb.*;

/**
 * Bean implementation class for Enterprise Bean: CNRDOCAMM00_EJB_MonitoCococoComponentSession
 */
@Stateless(name="CNRDOCAMM00_EJB_CdSDaConguagliareComponentSession")
public class CdSDaConguagliareComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements CdSDaConguagliareComponentSession  {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.docamm00.consultazioni.comp.CdSDaConguagliareComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
		return new CdSDaConguagliareComponentSessionBean();
	}
	public List<V_terzi_da_conguagliareBulk> findTerzi(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.CdsBulk param1) throws it.cnr.jada.comp.ComponentException{
		pre_component_invocation(param0,componentObj);
		try {
			List<V_terzi_da_conguagliareBulk> result = ((CdSDaConguagliareComponent)componentObj).findTerzi(param0,param1);
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
