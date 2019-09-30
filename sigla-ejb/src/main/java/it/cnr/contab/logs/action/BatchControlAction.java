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

import it.cnr.contab.logs.bp.*;
import it.cnr.contab.logs.bulk.Batch_controlBulk;
import it.cnr.contab.logs.bulk.Batch_log_tstaBulk;
import it.cnr.contab.logs.ejb.BatchControlComponentSession;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.util.Iterator;

public class BatchControlAction extends SelezionatoreListaAction
{

    public BatchControlAction()
    {
    }

    public Forward doConfirmShowLog(ActionContext actioncontext)
    {
        HookForward hookforward = (HookForward)actioncontext.getCaller();
        return doConfirmShowLog(actioncontext, (Batch_log_tstaBulk)hookforward.getParameter("focusedElement"));
    }

    public Forward doConfirmShowLog(ActionContext actioncontext, Batch_log_tstaBulk batch_log_tstabulk)
    {
        try
        {
            if(batch_log_tstabulk == null)
            {
                return actioncontext.findDefaultForward();
            } else
            {
                CRUDTestataLogBP crudtestatalogbp = (CRUDTestataLogBP)actioncontext.createBusinessProcess("CRUDTestataLogBP");
                crudtestatalogbp.edit(actioncontext, batch_log_tstabulk);
                return actioncontext.addBusinessProcess(crudtestatalogbp);
            }
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doElimina(ActionContext actioncontext)
    {
        try
        {
            BatchControlBP batchcontrolbp = (BatchControlBP)actioncontext.getBusinessProcess();
            for(Iterator iterator = batchcontrolbp.iterator(); iterator.hasNext(); ((BatchControlComponentSession)batchcontrolbp.createComponentSession()).eliminaConBulk(actioncontext.getUserContext(), (Batch_controlBulk)iterator.next()));
            batchcontrolbp.refresh(actioncontext);
            return actioncontext.findDefaultForward();
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doNuovo(ActionContext actioncontext)
    {
        try
        {
            CRUDBatchControlBP crudbatchcontrolbp = (CRUDBatchControlBP)actioncontext.createBusinessProcess("CRUDBatchControlBP", new Object[] {
                "M"
            });
            actioncontext.addHookForward("close", this, "doRefresh");
            return actioncontext.addBusinessProcess(crudbatchcontrolbp);
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doRefresh(ActionContext actioncontext)
    {
        try
        {
            BatchControlBP batchcontrolbp = (BatchControlBP)actioncontext.getBusinessProcess();
            batchcontrolbp.refresh(actioncontext);
            return actioncontext.findDefaultForward();
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doSelection(ActionContext actioncontext, String s)
        throws BusinessProcessException
    {
        SelezionatoreListaBP selezionatorelistabp = (SelezionatoreListaBP)actioncontext.getBusinessProcess();
        selezionatorelistabp.setSelection(actioncontext);
        selezionatorelistabp.setFocus(actioncontext);
        return actioncontext.findDefaultForward();
    }

    public Forward doShowLog(ActionContext actioncontext)
    {
        try
        {
            BatchControlBP batchcontrolbp = (BatchControlBP)actioncontext.getBusinessProcess();
            Batch_controlBulk batch_controlbulk = (Batch_controlBulk)batchcontrolbp.getFocusedElement();
            if(batchcontrolbp.getFocusedElement() == null)
                return actioncontext.findDefaultForward();
            RemoteIterator remoteiterator = batchcontrolbp.createComponentSession().cerca(actioncontext.getUserContext(), null, new Batch_log_tstaBulk(), batch_controlbulk, "batch_log_tsta");
            remoteiterator = EJBCommonServices.openRemoteIterator(actioncontext, remoteiterator);
            if(remoteiterator.countElements() == 0)
            {
                EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
                batchcontrolbp.setMessage("Nessun log trovato");
                return actioncontext.findDefaultForward();
            }
            if(remoteiterator.countElements() == 1)
            {
                Batch_log_tstaBulk batch_log_tstabulk = (Batch_log_tstaBulk)remoteiterator.nextElement();
                EJBCommonServices.closeRemoteIterator(actioncontext, remoteiterator);
                return doConfirmShowLog(actioncontext, batch_log_tstabulk);
            } else
            {
                return select(actioncontext, remoteiterator, BulkInfo.getBulkInfo(it.cnr.contab.logs.bulk.Batch_log_tstaBulk.class), null, "doConfirmShowLog");
            }
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doStartBatch(ActionContext actioncontext)
    {
        try
        {
            BatchControlBP batchcontrolbp = (BatchControlBP)actioncontext.getBusinessProcess();
            for(Iterator iterator = batchcontrolbp.iterator(); iterator.hasNext(); ((BatchControlComponentSession)batchcontrolbp.createComponentSession()).attivaBatch(actioncontext.getUserContext(), (Batch_controlBulk)iterator.next()));
            batchcontrolbp.refresh(actioncontext);
            return actioncontext.findDefaultForward();
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }

    public Forward doStopBatch(ActionContext actioncontext)
    {
        try
        {
            BatchControlBP batchcontrolbp = (BatchControlBP)actioncontext.getBusinessProcess();
            for(Iterator iterator = batchcontrolbp.iterator(); iterator.hasNext(); ((BatchControlComponentSession)batchcontrolbp.createComponentSession()).disattivaBatch(actioncontext.getUserContext(), (Batch_controlBulk)iterator.next()));
            batchcontrolbp.refresh(actioncontext);
            return actioncontext.findDefaultForward();
        }
        catch(Throwable throwable)
        {
            return handleException(actioncontext, throwable);
        }
    }
}