/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.logs.ejb;

import it.cnr.contab.logs.bulk.Batch_controlBulk;
import it.cnr.contab.logs.comp.BatchControlComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.*;
import it.cnr.jada.ejb.CRUDComponentSessionBean;
import it.cnr.jada.util.RemoteIterator;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import java.rmi.RemoteException;

@Stateless(name="BLOGS_EJB_BatchControlComponentSession")
public class BatchControlComponentSessionBean extends CRUDComponentSessionBean implements BatchControlComponentSession{
	@PostConstruct
	public void ejbCreate() {
        componentObj = new BatchControlComponent();
    }
	@Remove
	public void ejbRemove() throws EJBException {
		componentObj.release();
	}
	
	public static CRUDComponentSessionBean newInstance() throws EJBException {
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

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public OggettoBulk creaConBulkRequiresNew(UserContext param0, OggettoBulk  param1)
            throws ComponentException, EJBException
    {
        pre_component_invocation(param0, componentObj);
        try
        {
            OggettoBulk result = ((BatchControlComponent)componentObj).creaConBulkRequiresNew(param0, param1);
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

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public OggettoBulk modificaConBulkRequiresNew(UserContext param0, OggettoBulk  param1)
            throws ComponentException, EJBException
    {
        pre_component_invocation(param0, componentObj);
        try
        {
            OggettoBulk result = ((BatchControlComponent)componentObj).modificaConBulk(param0, param1);
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