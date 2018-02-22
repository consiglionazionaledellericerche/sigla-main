/*
 * Created on Nov 9, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg01.consultazioni.action;

import it.cnr.contab.pdg01.consultazioni.bulk.V_cons_pdgp_pdgg_speBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;

import java.util.Optional;

/**
 * @author rpagano
 * <p>
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsPdgpPdggSpeAction extends ConsultazioniAction {
    public Forward doCaricaGestionale(ActionContext context) {
        try {
            fillModel(context);

            ConsultazioniBP bp = (ConsultazioniBP) context.getBusinessProcess();
            V_cons_pdgp_pdgg_speBulk consPdgBulk = (V_cons_pdgp_pdgg_speBulk) bp.getModel();

            if (consPdgBulk == null) {
                setErrorMessage(context, "Attenzione: è necessario selezionare un dettaglio!");
                return context.findDefaultForward();
            }

            BulkBP nbp = (BulkBP) context.getUserInfo().createBusinessProcess(
                    context,
                    "CRUDPdgModuloSpeseGestBP",
                    new Object[]{
                            bp.isEditable() ? "M" : "V",
                            consPdgBulk
                    }
            );

            context.addHookForward("close", this, "doBringBackCaricaGestionale");

            return context.addBusinessProcess(nbp);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    /**
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doBringBackCaricaGestionale(ActionContext context) throws BusinessProcessException {
        try {
            Optional.ofNullable(context.getBusinessProcess())
                    .filter(ConsultazioniBP.class::isInstance)
                    .map(ConsultazioniBP.class::cast)
                    .ifPresent(consultazioniBP -> {
                        try {
                            consultazioniBP.refresh(context);
                            consultazioniBP.setModel(context, null);
                        } catch (BusinessProcessException e) {
                            throw new DetailedRuntimeException(e);
                        }
                    });
            return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doFiltraFiles(ActionContext context) {
        try {
            ConsultazioniBP bp = (ConsultazioniBP) context.getBusinessProcess();
            bp.setModel(context, null);
            return super.doFiltraFiles(context);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }

    public Forward doCancellaFiltro(ActionContext context) {
        try {
            ConsultazioniBP bp = (ConsultazioniBP) context.getBusinessProcess();
            bp.setModel(context, null);
            return super.doCancellaFiltro(context);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }
}