package it.cnr.contab.bollo00.ejb;

import java.sql.Timestamp;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.bollo00.comp.TipoAttoBolloComponent;
import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloBulk;

/**
 * Bean implementation class for Enterprise Bean: CNRBOLLO00_EJB_TipoAttoBolloComponentSession
 */
@Stateless(name="CNRBOLLO00_EJB_TipoAttoBolloComponentSession")
public class TipoAttoBolloComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements it.cnr.contab.bollo00.ejb.TipoAttoBolloComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.bollo00.comp.TipoAttoBolloComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new TipoAttoBolloComponentSessionBean();
	}

    public Tipo_atto_bolloBulk getTipoAttoBollo(it.cnr.jada.UserContext param0, Timestamp data, java.lang.String codiceTipoAttoBollo) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
        	Tipo_atto_bolloBulk result = ((TipoAttoBolloComponent)componentObj).getTipoAttoBollo(param0, data, codiceTipoAttoBollo);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }


}
