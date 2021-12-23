package it.cnr.contab.ordmag.magazzino.action;

import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.ordmag.magazzino.bulk.StampaBollaScaricoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.Stampa_consumiBulk;
import it.cnr.contab.reports.action.ParametricPrintAction;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.FillException;

public class StampaBollaScaricoMagAction extends ParametricPrintAction {

    public Forward doOnDataDaChange(ActionContext actioncontext) throws FillException {
        ParametricPrintBP bp= (ParametricPrintBP) actioncontext.getBusinessProcess();
        StampaBollaScaricoMagBulk model=(StampaBollaScaricoMagBulk)bp.getModel();
        fillModel(actioncontext);
        try {
            if(model.getaData() == null) {
                model.setaData(model.getDaData());
            }
            return actioncontext.findDefaultForward();
        } catch (Throwable e) {
            return handleException(actioncontext, e);
        }
    }


    public Forward doOnPgBollaDaChange(ActionContext actioncontext) throws FillException {
        ParametricPrintBP bp= (ParametricPrintBP) actioncontext.getBusinessProcess();
        StampaBollaScaricoMagBulk model=(StampaBollaScaricoMagBulk)bp.getModel();
        fillModel(actioncontext);
        try {
            if(model.getaNumBolla() == null) {
                model.setaNumBolla(model.getDaNumBolla());
            }
            return actioncontext.findDefaultForward();
        } catch (Throwable e) {
            return handleException(actioncontext, e);
        }
    }

    public Forward doBringBackSearchFindDaBeneServizio(ActionContext context, StampaBollaScaricoMagBulk stampaBolla, Bene_servizioBulk bene) {
        stampaBolla.setDaBeneServizio(bene);
        if(stampaBolla.getaBeneServizio() == null || stampaBolla.getaBeneServizio().getCd_bene_servizio() == null) {
            stampaBolla.setaBeneServizio(bene);
        }
        return context.findDefaultForward();
    }
}
