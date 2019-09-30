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

package it.cnr.contab.logs.action;

import it.cnr.contab.logs.bp.CRUDBatchControlBP;
import it.cnr.contab.logs.bulk.Batch_controlBulk;
import it.cnr.contab.logs.bulk.Batch_proceduraBulk;
import it.cnr.contab.logs.ejb.BatchControlComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.*;

import java.rmi.RemoteException;

public class CRUDBatchControlAction extends CRUDAction
{

    public CRUDBatchControlAction()
    {
    }

    public Forward doBlankSearchProcedura(ActionContext actioncontext, Batch_controlBulk batch_controlbulk)
        throws RemoteException
    {
        try
        {
            CRUDBatchControlBP crudbatchcontrolbp = (CRUDBatchControlBP)actioncontext.getBusinessProcess();
            batch_controlbulk.setProcedura(new Batch_proceduraBulk());
            crudbatchcontrolbp.setModel(actioncontext, ((BatchControlComponentSession)crudbatchcontrolbp.createComponentSession()).inizializzaParametri(actioncontext.getUserContext(), (Batch_controlBulk)crudbatchcontrolbp.getModel()));
            return actioncontext.findDefaultForward();
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doBringBackSearchProcedura(ActionContext actioncontext, Batch_controlBulk batch_controlbulk, Batch_proceduraBulk batch_procedurabulk)
        throws RemoteException
    {
        try
        {
            CRUDBatchControlBP crudbatchcontrolbp = (CRUDBatchControlBP)actioncontext.getBusinessProcess();
            batch_controlbulk.setProcedura(batch_procedurabulk);
            crudbatchcontrolbp.setModel(actioncontext, ((BatchControlComponentSession)crudbatchcontrolbp.createComponentSession()).inizializzaParametri(actioncontext.getUserContext(), (Batch_controlBulk)crudbatchcontrolbp.getModel()));
            return actioncontext.findDefaultForward();
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doSalva(ActionContext actioncontext)
        throws RemoteException
    {
        try
        {
            Object obj = super.doSalva(actioncontext);
            if(getBusinessProcess(actioncontext).isEditing())
            {
                obj = actioncontext.closeBusinessProcess();
                Forward forward = actioncontext.findForward("close");
                if(forward != null)
                    obj = forward;
            }
            return ((Forward) (obj));
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }
}