package it.cnr.contab.utenze00.ejb;

import it.cnr.contab.utenze00.comp.AssBpAccessoComponent;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name = "CNRUTENZE00_EJB_AssBpAccessoComponentSession")
public class AssBpAccessoComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements AssBpAccessoComponentSession {
    public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
        return new AssBpAccessoComponentSessionBean();
    }

    @PostConstruct
    public void ejbCreate() {
        componentObj = new AssBpAccessoComponent();
    }

    public java.util.List findAccessoByBP(it.cnr.jada.UserContext param0, String param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.util.List result = ((AssBpAccessoComponent) componentObj).findAccessoByBP(param0, param1);
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

    public it.cnr.contab.utenze00.bulk.AssBpAccessoBulk finAssBpAccesso(it.cnr.jada.UserContext param0, String param1, String param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.utenze00.bulk.AssBpAccessoBulk result = ((AssBpAccessoComponent) componentObj).finAssBpAccesso(param0, param1, param2);
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
