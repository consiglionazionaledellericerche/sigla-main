package it.cnr.contab.docamm00.actions;

import it.cnr.contab.docamm00.bp.AllegatiMultipliFatturaPassivaBP;
import it.cnr.contab.docamm00.bp.SelezionatoreFattureElettronicheLiquidazioneBP;
import it.cnr.contab.docamm00.bp.SelezionatoreFattureLiquidazioneSospesaBP;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.AllegatoFatturaBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.SelezionatoreListaAction;

import java.util.List;

public class SelezionatoreFattureElettronicheLiquidazioneAction extends SelezionatoreListaAction {
    public Forward doProvvedimentoLiquidazione(ActionContext context) {
        SelezionatoreFattureElettronicheLiquidazioneBP bp = (SelezionatoreFattureElettronicheLiquidazioneBP) context.getBusinessProcess();
        try {
            fillModel(context);
            bp.setSelection(context);
            List<DocumentoEleTestataBulk> selectedElements = bp.getSelectedElements(context);
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
    public Forward doRefresh(ActionContext actioncontext) throws BusinessProcessException {
        SelezionatoreFattureElettronicheLiquidazioneBP bp = (SelezionatoreFattureElettronicheLiquidazioneBP)actioncontext.getBusinessProcess();
        bp.refresh(actioncontext);
        return actioncontext.findDefaultForward();
    }
}
