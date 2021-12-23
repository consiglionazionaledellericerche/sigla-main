package it.cnr.contab.ordmag.magazzino.action;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.magazzino.bulk.StampaBollaScaricoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.StampaPartitarioMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.Stampa_consumiBulk;
import it.cnr.contab.reports.action.ParametricPrintAction;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.DateUtils;

import java.util.Date;
import java.util.Optional;

public class StampaPartitarioMagAction extends ParametricPrintAction {

    private void checkDateRange(Date da, Date a,String property) throws ValidationException {
        if (Optional.ofNullable(da).isPresent() && Optional.ofNullable(a).isPresent()) {
            if (da.compareTo(a) > 0) {
                throw new ValidationException("Intervallo di " + property + " non corretto, la data Da non può essere maggiore della data A");
            }
        }
    }
    public Forward doOnDataInizioCompotenzaChange(ActionContext actioncontext) throws FillException {
        ParametricPrintBP bp= (ParametricPrintBP) actioncontext.getBusinessProcess();
        StampaPartitarioMagBulk model=(StampaPartitarioMagBulk)bp.getModel();
        fillModel(actioncontext);
        try {

            if(model.getDaDataCompetenza() != null && model.getaDataCompetenza() == null) {
                model.setaDataCompetenza(model.getDaDataCompetenza());
            }
            return actioncontext.findDefaultForward();
        } catch (Throwable e) {
            return handleException(actioncontext, e);
        }
    }
    public Forward doOnDataFineCompotenzaChange(ActionContext actioncontext) throws FillException {
        ParametricPrintBP bp= (ParametricPrintBP) actioncontext.getBusinessProcess();
        StampaPartitarioMagBulk model=(StampaPartitarioMagBulk)bp.getModel();
        fillModel(actioncontext);

        try {
            checkDateRange(model.getDaDataCompetenza(),model.getaDataCompetenza(),"Data Compotenza");
            return actioncontext.findDefaultForward();
        } catch (Throwable e) {
            return handleException(actioncontext, e);
        }
    }


    public Forward doOnDataInizioMovimentoChange(ActionContext actioncontext) throws FillException {
        ParametricPrintBP bp= (ParametricPrintBP) actioncontext.getBusinessProcess();
        StampaPartitarioMagBulk model=(StampaPartitarioMagBulk)bp.getModel();
        fillModel(actioncontext);
        try {

            if(model.getDaDataMovimento() != null && model.getaDataMovimento() == null) {
                model.setaDataMovimento(model.getDaDataMovimento());
            }
            return actioncontext.findDefaultForward();
        } catch (Throwable e) {
            return handleException(actioncontext, e);
        }
    }
    public Forward doOnDataFineMovimentoChange(ActionContext actioncontext) throws FillException {
        ParametricPrintBP bp= (ParametricPrintBP) actioncontext.getBusinessProcess();
        StampaPartitarioMagBulk model=(StampaPartitarioMagBulk)bp.getModel();
        fillModel(actioncontext);

        try {
            checkDateRange(model.getDaDataMovimento(),model.getaDataMovimento(),"Data Movimento");
            return actioncontext.findDefaultForward();
        } catch (Throwable e) {
            return handleException(actioncontext, e);
        }
    }

    public Forward doBringBackSearchFindDaBeneServizio(ActionContext context, StampaPartitarioMagBulk stampaPartitarioMagBulk, Bene_servizioBulk bene) {
        stampaPartitarioMagBulk.setDaBeneServizio(bene);
        if ( !(Optional.ofNullable(stampaPartitarioMagBulk.getaBeneServizio()).filter(t->t.getCd_bene_servizio()!=null ).isPresent())){
            stampaPartitarioMagBulk.setaBeneServizio(bene);
        }
        return context.findDefaultForward();
    }
    public Forward doBringBackSearchFindDaFornitore(ActionContext context, StampaPartitarioMagBulk stampaPartitarioMagBulk, TerzoBulk fornitore) {
        stampaPartitarioMagBulk.setDaFornitore(fornitore);
        if ( !(Optional.ofNullable(stampaPartitarioMagBulk.getaFornitore()).filter(t->t.getCd_terzo()!=null ).isPresent())){
            stampaPartitarioMagBulk.setaFornitore(fornitore);
        }
        return context.findDefaultForward();
    }


}
