/*
 * Created on Jan 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.consultazioni.action;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.consultazioni.bp.ConsObbligazioniBP;
import it.cnr.contab.consultazioni.bp.ConsWorkpackageBP;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.SelezionatoreListaAction;

import java.util.Iterator;
import java.util.Optional;

/**
 * @author mincarnato
 * <p>
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsWorkpackageAction extends SelezionatoreListaAction {

    public Forward doCloseForm(ActionContext context) throws BusinessProcessException {
        try {
            ConsWorkpackageBP bp = (ConsWorkpackageBP) context.getBusinessProcess();
            if (bp.getName().equalsIgnoreCase("StampaSituazioneSinteticaDispGAEBP") ||
                    bp.getName().equalsIgnoreCase("StampaSituazioneSinteticaRendGAEBP")) {
                bp.resetIdReport(context);
                return super.doCloseForm(context);
            }
            return super.doCloseForm(context);
        } catch (Throwable throwable) {
            return handleException(context, throwable);
        }
    }

    public Forward doChiudiRicerca(ActionContext context) {
        try {
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doWorkpackageSelezionati(ActionContext context) {

        try {
            ConsWorkpackageBP bp = (ConsWorkpackageBP) context.getBusinessProcess();
            HookForward hook = (HookForward) context.getCaller();
            it.cnr.jada.util.RemoteIterator ri = (it.cnr.jada.util.RemoteIterator) hook.getParameter("remoteIterator");
            bp.setIterator(context, ri);
            bp.setDirty(true);
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doObbligazioni(ActionContext context) {
        try {
            ConsWorkpackageBP bp = (ConsWorkpackageBP) context.getBusinessProcess();
            bp.setSelection(context);
            java.util.List wp = bp.getSelectedElements(context);

            if (wp.isEmpty()) {
                bp.setMessage("Non è stata selezionata nessuna riga.");
                return context.findDefaultForward();
            }

            if (wp == null)
                return context.findDefaultForward();

            CompoundFindClause clauses = new CompoundFindClause();
            long selectElements = bp.getSelection().size();
            if ((bp.getElementsCount() != selectElements || bp.getCondizioneCorrente() != null)) {
                for (Iterator i = wp.iterator(); i.hasNext(); ) {
                    WorkpackageBulk wpb = (WorkpackageBulk) i.next();
                    clauses.addClause("OR", "cd_linea_attivita", SQLBuilder.EQUALS, wpb.getCd_linea_attivita());
                    clauses.addClause("AND", "cd_centro_responsabilita", SQLBuilder.EQUALS, wpb.getCd_centro_responsabilita());
                }
            }
            CompoundFindClause findclause = Optional.ofNullable(bp.getCondizioneCorrente())
                        .map(condizioneComplessaBulk -> condizioneComplessaBulk.buildFindClauses(true))
                        .orElse(null);
            if (findclause == null)
                findclause = new CompoundFindClause();
            findclause.addChild(clauses);

            ConsObbligazioniBP ricercaLiberaBP = (ConsObbligazioniBP) context.createBusinessProcess("ConsObbligazioniBP");
            ricercaLiberaBP.addToBaseclause(findclause);
            ricercaLiberaBP.openIterator(context);

            context.addHookForward("close", this, "doDefault");
            return context.addBusinessProcess(ricercaLiberaBP);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }


    public Forward doPrint(ActionContext context) {
        ConsWorkpackageBP bp = (ConsWorkpackageBP) context.getBusinessProcess();
        try {
            if (bp.getName().equalsIgnoreCase("StampaSituazioneSinteticaDispGAEBP")) {
                bp.setSelection(context);

                java.util.List wp = bp.getSelectedElements(context);
                if (wp.isEmpty()) {
                    bp.setMessage("Non è stata selezionata nessuna riga.");
                    return context.findDefaultForward();
                }

                bp.selectedGae(context, wp);
                OfflineReportPrintBP printbp = (OfflineReportPrintBP) context.createBusinessProcess("OfflineReportPrintBP");

                printbp.setId_report_generico(bp.getPg_stampa());
                printbp.setReportName("/cnrpreventivo/pdg/situazione_sintetica_disp_GAE.jasper");
                Print_spooler_paramBulk param;
                param = new Print_spooler_paramBulk();
                param.setNomeParam("Idrpt");
                param.setValoreParam(bp.getPg_stampa().toString());
                param.setParamType("java.lang.Long");
                printbp.addToPrintSpoolerParam(param);

                param = new Print_spooler_paramBulk();
                param.setNomeParam("Esercizio");
                param.setValoreParam(CNRUserContext.getEsercizio(context.getUserContext()).toString());
                param.setParamType("java.lang.Integer");
                printbp.addToPrintSpoolerParam(param);

                context.addHookForward("close", this, "doAnnullaStampa");
                return context.addBusinessProcess(printbp);
            }
            if (bp.getName().equalsIgnoreCase("StampaSituazioneSinteticaRendGAEBP")) {
                bp.setSelection(context);

                java.util.List wp = bp.getSelectedElements(context);
                if (wp.isEmpty()) {
                    bp.setMessage("Non è stata selezionata nessuna riga.");
                    return context.findDefaultForward();
                }

                bp.selectedGae(context, wp);
                OfflineReportPrintBP printbp = (OfflineReportPrintBP) context.createBusinessProcess("OfflineReportPrintBP");

                printbp.setId_report_generico(bp.getPg_stampa());
                printbp.setReportName("/cnrpreventivo/pdg/situazione_sintetica_rendicontazione_GAE.jasper");
                Print_spooler_paramBulk param;
                param = new Print_spooler_paramBulk();
                param.setNomeParam("Idrpt");
                param.setValoreParam(bp.getPg_stampa().toString());
                param.setParamType("java.lang.Long");
                printbp.addToPrintSpoolerParam(param);

                param = new Print_spooler_paramBulk();
                param.setNomeParam("Esercizio");
                param.setValoreParam(CNRUserContext.getEsercizio(context.getUserContext()).toString());
                param.setParamType("java.lang.Integer");
                printbp.addToPrintSpoolerParam(param);

                context.addHookForward("close", this, "doAnnullaStampa");
                return context.addBusinessProcess(printbp);
            } else
                return super.doPrint(context);

        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doAnnullaStampa(ActionContext context) {
        try {
            ConsWorkpackageBP bp = (ConsWorkpackageBP) context.getBusinessProcess();
            bp.resetIdReport(context);
            bp.clearSelection(context);

            return context.findDefaultForward();

        } catch (BusinessProcessException e) {
            return handleException(context, e);
        }
    }

    @Override
    public Forward basicDoBringBack(ActionContext actioncontext) throws BusinessProcessException {
        return actioncontext.findDefaultForward();
    }

}
