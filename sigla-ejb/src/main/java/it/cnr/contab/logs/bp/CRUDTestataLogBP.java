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

import it.cnr.contab.logs.bulk.Batch_log_tstaBulk;
import it.cnr.contab.logs.bulk.Batch_log_tstaKey;
import it.cnr.contab.reports.bp.ReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.jsp.Button;

import java.math.BigDecimal;

public class CRUDTestataLogBP extends SimpleCRUDBP
{

    public CRUDTestataLogBP()
    {
        dettagli = new RemoteDetailCRUDController("dettagli", it.cnr.contab.logs.bulk.Batch_log_rigaBulk.class, "dettagli", "BLOGS_EJB_TestataLogComponentSession", this);
        dettagli.setReadonly(true);
    }

    public CRUDTestataLogBP(String s)
    {
        super(s);
        dettagli = new RemoteDetailCRUDController("dettagli", it.cnr.contab.logs.bulk.Batch_log_rigaBulk.class, "dettagli", "BLOGS_EJB_TestataLogComponentSession", this);
        dettagli.setReadonly(true);
    }

    protected Button[] createToolbar()
    {
        Button abutton[] = new Button[5];
        int i = 0;
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.search");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.startSearch");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.freeSearch");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.delete");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.print");
        return abutton;
    }
	public boolean isPrintButtonHidden() 
	{
		return super.isPrintButtonHidden() || isInserting() || isSearching();
	}
    public final RemoteDetailCRUDController getDettagli()
    {
        return dettagli;
    }

    protected void initializePrintBP(AbstractPrintBP abstractprintbp)
    {
        ReportPrintBP reportprintbp = (ReportPrintBP)abstractprintbp;
//        reportprintbp.setReportName("/logs/batchlog.rpt");
//        reportprintbp.setReportParameter(0, "C");
//        reportprintbp.setReportParameter(1, ((Batch_log_tstaBulk)getModel()).getPg_esecuzione().toString());
        
        reportprintbp.setReportName("/logs/batchlog.jasper");
    	Print_spooler_paramBulk param;
    	param = new Print_spooler_paramBulk();
    	param.setNomeParam("Pg_esecuzione");
    	param.setValoreParam(((Batch_log_tstaBulk)getModel()).getPg_esecuzione().toString());
    	param.setParamType("java.lang.Integer");
    	reportprintbp.addToPrintSpoolerParam(param);

    	param = new Print_spooler_paramBulk();
    	param.setNomeParam("Controllo");
    	param.setValoreParam("C");
    	param.setParamType("java.lang.String");
    	reportprintbp.addToPrintSpoolerParam(param);
    }

    public void reset(ActionContext actioncontext)
        throws BusinessProcessException
    {
        resetForSearch(actioncontext);
    }

    private final RemoteDetailCRUDController dettagli;
}