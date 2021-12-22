package it.cnr.contab.ordmag.magazzino.action;

import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.gestiva00.actions.StampaAction;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.magazzino.bulk.Stampa_consumiBulk;
import it.cnr.contab.reports.action.ParametricPrintAction;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.FillException;

public class StampaConsumiAction extends ParametricPrintAction {
    public Forward doOnDataInizioMovimentoChange(ActionContext actioncontext) throws FillException {


        ParametricPrintBP bp= (ParametricPrintBP) actioncontext.getBusinessProcess();
        Stampa_consumiBulk model=(Stampa_consumiBulk)bp.getModel();
        fillModel(actioncontext);

        try {
            if(model.getDaDataMovimento() == null ){
                throw new it.cnr.jada.bulk.ValidationException("Selezionare Data Movimento Da");
            }
            if(model.getaDataMovimento() == null) {
                model.setaDataMovimento(model.getDaDataMovimento());
            }
            return actioncontext.findDefaultForward();
        } catch (Throwable e) {
            return handleException(actioncontext, e);
        }
    }
    public Forward doOnDataFineMovimentoChange(ActionContext actioncontext) throws FillException {


        ParametricPrintBP bp= (ParametricPrintBP) actioncontext.getBusinessProcess();
        Stampa_consumiBulk model=(Stampa_consumiBulk)bp.getModel();
        fillModel(actioncontext);

        try {
            if(model.getDaDataMovimento() == null ){
                throw new it.cnr.jada.bulk.ValidationException("Selezionare Data Movimento Da");
            }
            if(model.getaDataMovimento() == null ){
                throw new it.cnr.jada.bulk.ValidationException("Selezionare Data Movimento A");
            }
            if(model.getDaDataMovimento().compareTo(model.getaDataMovimento()) > 0){
                throw new it.cnr.jada.bulk.ValidationException("Intervallo di Data Movimento non corretto, la data Da non può essere maggiore della data A");
            }
            return actioncontext.findDefaultForward();
        } catch (Throwable e) {
            return handleException(actioncontext, e);
        }
    }


    public Forward doBringBackSearchFindDaUnitaOperativa(ActionContext context, Stampa_consumiBulk stampaConsumi, UnitaOperativaOrdBulk uop) {
        stampaConsumi.setDaUnitaOperativa(uop);
        if(stampaConsumi.getaUnitaOperativa() == null || stampaConsumi.getaUnitaOperativa().getCdUnitaOperativa()==null){
            stampaConsumi.setaUnitaOperativa(uop);
        }
        return context.findDefaultForward();
    }
    public Forward doBringBackSearchFindDaCatGrp(ActionContext context, Stampa_consumiBulk stampaConsumi, Categoria_gruppo_inventBulk catGrp) {
        stampaConsumi.setDaCatgrp(catGrp);
        if(stampaConsumi.getaCatgrp() == null || stampaConsumi.getaCatgrp().getCd_categoria_gruppo() == null) {
            stampaConsumi.setaCatgrp(catGrp);
        }
        return context.findDefaultForward();
    }
    public Forward doBringBackSearchFindDaBeneServizio(ActionContext context, Stampa_consumiBulk stampaConsumi, Bene_servizioBulk bene) {
        stampaConsumi.setDaBeneServizio(bene);
        if(stampaConsumi.getaBeneServizio() == null || stampaConsumi.getaBeneServizio().getCd_bene_servizio() == null) {
            stampaConsumi.setaBeneServizio(bene);
        }
        return context.findDefaultForward();
    }
}
