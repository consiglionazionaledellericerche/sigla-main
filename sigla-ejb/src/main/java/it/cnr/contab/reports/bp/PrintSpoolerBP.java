package it.cnr.contab.reports.bp;

import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Insert the type's description here.
 * Creation date: (11/04/2002 17:28:04)
 *
 * @author: CNRADM
 */
public class PrintSpoolerBP extends it.cnr.jada.util.action.SelezionatoreListaBP {

    /**
     * SpoolerStatusBP constructor comment.
     */
    public PrintSpoolerBP() {
        super();
        table.setMultiSelection(true);
        setBulkInfo(BulkInfo.getBulkInfo(Print_spoolerBulk.class));
    }

    public it.cnr.contab.reports.ejb.OfflineReportComponentSession createComponentSession() throws BusinessProcessException {
        return
                (it.cnr.contab.reports.ejb.OfflineReportComponentSession) createComponentSession(
                        "BREPORTS_EJB_OfflineReportComponentSession",
                        it.cnr.contab.reports.ejb.OfflineReportComponentSession.class);
    }

    public it.cnr.jada.util.jsp.Button[] createToolbar() {
        Button[] toolbar = new Button[4];
        int i = 0;
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.refresh");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.print");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.delete");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.close");
        return toolbar;
    }

    protected void init(Config config, ActionContext context) throws BusinessProcessException {
        super.init(config, context);
        Print_spoolerBulk print_spooler = new Print_spoolerBulk();
        print_spooler.setTiVisibilita(Print_spoolerBulk.TI_VISIBILITA_UTENTE);
        setModel(context, print_spooler);
        refresh(context);
    }

    public boolean isPrintButtonEnabled() {
        return
                getFocusedElement() != null &&
                        ((Print_spoolerBulk) getFocusedElement()).isEseguita();
    }

    public void refresh(ActionContext context) throws BusinessProcessException {
        try {
            setIterator(context, createComponentSession().queryJobs(
                    context.getUserContext(),
                    ((Print_spoolerBulk) getModel()).getTiVisibilita()));
        } catch (Throwable e) {
            throw new BusinessProcessException(e);
        }
    }

    public void writeToolbar(javax.servlet.jsp.PageContext pageContext) throws java.io.IOException, javax.servlet.ServletException {
        Button[] toolbar = getToolbar();
        Print_spoolerBulk print_spool = (Print_spoolerBulk) getFocusedElement();
        if (print_spool != null && Print_spoolerBulk.STATO_ESEGUITA.equalsIgnoreCase(print_spool.getStato()))
            toolbar[1].setHref("doPrint('" + JSPUtils.getAppRoot((HttpServletRequest) pageContext.getRequest()) + "offline_report/" + print_spool.getNomeFile() + "?pg=" + print_spool.getPgStampa().longValue() + "')");
        else
            toolbar[1].setHref(null);
        writeToolbar(pageContext.getOut(), toolbar);
    }

    public String getFileName() {
        Print_spoolerBulk print_spool = (Print_spoolerBulk) getFocusedElement();
        if (print_spool != null)
            return print_spool.getNomeFile();
        else
            return (null);
    }

    public String getDownloadFile(javax.servlet.jsp.PageContext pageContext) {
        Print_spoolerBulk print_spool = (Print_spoolerBulk) getFocusedElement();
        if (print_spool != null && Print_spoolerBulk.STATO_ESEGUITA.equalsIgnoreCase(print_spool.getStato()))
            return JSPUtils.buildAbsoluteUrl(
                    pageContext,
                    null,
                    "offline_report/" + print_spool.getNomeFile());
        else
            return (null);
    }

    public String getPgStampa() {
        Print_spoolerBulk print_spool = (Print_spoolerBulk) getFocusedElement();
        if (print_spool != null)
            return "" + print_spool.getPgStampa().longValue();
        else
            return null;
    }

    public boolean isEMailEnabled() {
        return Optional.ofNullable(getFocusedElement())
                .filter(Print_spoolerBulk.class::isInstance)
                .map(Print_spoolerBulk.class::cast)
                .flatMap(print_spoolerBulk -> Optional.ofNullable(print_spoolerBulk.getFlEmail()))
                .orElse(Boolean.FALSE);
    }

}
