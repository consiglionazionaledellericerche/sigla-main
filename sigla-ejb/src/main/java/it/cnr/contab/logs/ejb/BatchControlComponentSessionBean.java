package it.cnr.contab.logs.ejb;

import it.cnr.contab.logs.bulk.Batch_controlBulk;
import it.cnr.contab.logs.comp.BatchControlComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.*;
import it.cnr.jada.ejb.CRUDComponentSessionBean;
import it.cnr.jada.util.RemoteIterator;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Remove;
import javax.ejb.SessionBean;
import javax.ejb.Stateless;

@Stateless(name="BLOGS_EJB_BatchControlComponentSession")
public class BatchControlComponentSessionBean extends CRUDComponentSessionBean implements BatchControlComponentSession{
	@PostConstruct
	public void ejbCreate() throws javax.ejb.CreateException {
        componentObj = new BatchControlComponent();
    }
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}
	
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
        return new BatchControlComponentSessionBean();
    }

    public Batch_controlBulk attivaBatch(UserContext param0, Batch_controlBulk param1)
        throws ComponentException, EJBException
    {
        pre_component_invocation(param0, componentObj);
        try
        {
            Batch_controlBulk result = ((BatchControlComponent)componentObj).attivaBatch(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        }
        catch(NoRollbackException e)
        {
            component_invocation_succes(param0, componentObj);
            throw e;
        }
        catch(ComponentException e)
        {
            component_invocation_failure(param0, componentObj);
            throw e;
        }
        catch(RuntimeException e)
        {
            throw uncaughtRuntimeException(param0, componentObj, e);
        }
        catch(Error e)
        {
            throw uncaughtError(param0, componentObj, e);
        }
    }

	public Batch_controlBulk disattivaBatch(UserContext param0, Batch_controlBulk param1)
		throws ComponentException, EJBException
    {
        pre_component_invocation(param0, componentObj);
        try
        {
            Batch_controlBulk result = ((BatchControlComponent)componentObj).disattivaBatch(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        }
        catch(NoRollbackException e)
        {
            component_invocation_succes(param0, componentObj);
            throw e;
        }
        catch(ComponentException e)
        {
            component_invocation_failure(param0, componentObj);
            throw e;
        }
        catch(RuntimeException e)
        {
            throw uncaughtRuntimeException(param0, componentObj, e);
        }
        catch(Error e)
        {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public Batch_controlBulk inizializzaParametri(UserContext param0, Batch_controlBulk param1)
    	throws ComponentException, EJBException
    {
        pre_component_invocation(param0, componentObj);
        try
        {
            Batch_controlBulk result = ((BatchControlComponent)componentObj).inizializzaParametri(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        }
        catch(NoRollbackException e)
        {
            component_invocation_succes(param0, componentObj);
            throw e;
        }
        catch(ComponentException e)
        {
            component_invocation_failure(param0, componentObj);
            throw e;
        }
        catch(RuntimeException e)
        {
            throw uncaughtRuntimeException(param0, componentObj, e);
        }
        catch(Error e)
        {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public RemoteIterator listaBatch_control_jobs(UserContext param0)
    	throws ComponentException, EJBException
    {
        pre_component_invocation(param0, componentObj);
        try
        {
            RemoteIterator result = ((BatchControlComponent)componentObj).listaBatch_control_jobs(param0);
            component_invocation_succes(param0, componentObj);
            return result;
        }
        catch(NoRollbackException e)
        {
            component_invocation_succes(param0, componentObj);
            throw e;
        }
        catch(ComponentException e)
        {
            component_invocation_failure(param0, componentObj);
            throw e;
        }
        catch(RuntimeException e)
        {
            throw uncaughtRuntimeException(param0, componentObj, e);
        }
        catch(Error e)
        {
            throw uncaughtError(param0, componentObj, e);
        }
    }
}