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

package it.cnr.contab.logs.bp;

import it.cnr.contab.logs.ejb.BatchControlComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.Table;

public class BatchControlBP extends SelezionatoreListaBP
{

    public BatchControlBP()
    {
        super.table.setMultiSelection(true);
    }

    public BatchControlBP(String s)
    {
        super(s);
        super.table.setMultiSelection(true);
    }

    public BatchControlComponentSession createComponentSession()
        throws BusinessProcessException
    {
        return (BatchControlComponentSession)super.createComponentSession("BLOGS_EJB_BatchControlComponentSession", it.cnr.contab.logs.ejb.BatchControlComponentSession.class);
    }

    public Button[] createToolbar()
    {
        Button abutton[] = new Button[5];
        int i = 0;
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.new");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.delete");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.showLog");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.stopBatch");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.startBatch");
        return abutton;
    }

    protected void init(it.cnr.jada.action.Config config, ActionContext actioncontext)
        throws BusinessProcessException
    {
        try
        {
            super.init(config, actioncontext);
            setBulkInfo(BulkInfo.getBulkInfo(it.cnr.contab.logs.bulk.V_batch_control_jobsBulk.class));
            refresh(actioncontext);
        }
        catch(Throwable throwable)
        {
            throw handleException(throwable);
        }
    }

    public void refresh(ActionContext actioncontext)
        throws BusinessProcessException
    {
        try
        {
            setIterator(actioncontext, createComponentSession().listaBatch_control_jobs(actioncontext.getUserContext()));
        }
        catch(Throwable throwable)
        {
            throw handleException(throwable);
        }
    }
}