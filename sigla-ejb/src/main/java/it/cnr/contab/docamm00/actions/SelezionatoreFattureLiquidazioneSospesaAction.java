package it.cnr.contab.docamm00.actions;

import it.cnr.contab.docamm00.bp.AllegatiMultipliFatturaPassivaBP;
import it.cnr.contab.docamm00.bp.SelezionatoreFattureLiquidazioneSospesaBP;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.AllegatoFatturaBulk;
import it.cnr.contab.doccont00.bp.AbstractFirmaDigitaleDocContBP;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.SelezionatoreListaAction;

import java.util.List;

public class SelezionatoreFattureLiquidazioneSospesaAction extends SelezionatoreListaAction {
    public Forward doProvvedimentoLiquidazione(ActionContext context) {
        SelezionatoreFattureLiquidazioneSospesaBP bp = (SelezionatoreFattureLiquidazioneSospesaBP)context.getBusinessProcess();
        try {
            fillModel(context);
            bp.setSelection(context);
            List<Fattura_passiva_IBulk> selectedElements = bp.getSelectedElements(context);
            if (selectedElements == null || selectedElements.isEmpty())
                throw new ApplicationException("Selezionare almeno una fattura!");
            AllegatiMultipliFatturaPassivaBP allegatiMultipliFatturaPassivaBP =
                    (AllegatiMultipliFatturaPassivaBP) context.createBusinessProcess(
                            "AllegatiMultipliFatturaPassivaBP",
                            new Object[] {"M", selectedElements, AllegatoFatturaBulk.P_SIGLA_FATTURE_ATTACHMENT_LIQUIDAZIONE}
                    );
            context.addHookForward("close", this, "doRefresh");
            return context.addBusinessProcess(allegatiMultipliFatturaPassivaBP);
        } catch(Exception e) {
            return handleException(context,e);
        }
    }
    public Forward doCambiaVisibilita(ActionContext actioncontext) throws BusinessProcessException {
        SelezionatoreFattureLiquidazioneSospesaBP bp = (SelezionatoreFattureLiquidazioneSospesaBP)actioncontext.getBusinessProcess();
        OggettoBulk bulk = bp.getModel();
        Fattura_passiva_IBulk fatturaPassivaIBulk = ((Fattura_passiva_IBulk)bulk);
        try {
            fillModel(actioncontext);
            String statoLiquidazione = fatturaPassivaIBulk.getStato_liquidazione();
            bulk = (OggettoBulk)bp.getBulkInfo().getBulkClass().newInstance();
            fatturaPassivaIBulk = ((Fattura_passiva_IBulk)bulk);
            fatturaPassivaIBulk.setStato_liquidazione(statoLiquidazione);
            bp.setModel(actioncontext, bulk);
            bp.openIterator(actioncontext);
            return actioncontext.findDefaultForward();
        } catch(Throwable e) {
            return handleException(actioncontext,e);
        }
    }


    public Forward doRefresh(ActionContext actioncontext) throws BusinessProcessException {
        SelezionatoreFattureLiquidazioneSospesaBP bp = (SelezionatoreFattureLiquidazioneSospesaBP)actioncontext.getBusinessProcess();
        bp.refresh(actioncontext);
        return actioncontext.findDefaultForward();
    }
}
