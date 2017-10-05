package it.cnr.contab.docamm00.actions;

import it.cnr.contab.docamm00.bp.ContabilizzaOrdineBP;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.*;

import java.rmi.RemoteException;
import java.util.Optional;

public class ContabilizzaOrdineAction extends SelezionatoreListaAction {

    public ContabilizzaOrdineAction() {
    }

    public Forward doCancellaFiltro(ActionContext context) {
        ContabilizzaOrdineBP bp = (ContabilizzaOrdineBP) context.getBusinessProcess();
        bp.setCondizioneCorrente(null);
        try {
            bp.setIterator(context,bp.search(context, null, new EvasioneOrdineRigaBulk()));
        } catch (RemoteException|BusinessProcessException e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }
    public Forward doRicercaLibera(ActionContext context) {
        try {
            ContabilizzaOrdineBP bp = (ContabilizzaOrdineBP) context.getBusinessProcess();
            RicercaLiberaBP ricercaLiberaBP = (RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
            ricercaLiberaBP.setSearchProvider(bp);
            ricercaLiberaBP.setFreeSearchSet("fattura_passiva");
            ricercaLiberaBP.setShowSearchResult(false);
            ricercaLiberaBP.setCanPerformSearchWithoutClauses(true);
            ricercaLiberaBP.setPrototype( new EvasioneOrdineRigaBulk());
            Optional.ofNullable(bp.getCondizioneCorrente())
                    .filter(condizioneComplessaBulk -> !condizioneComplessaBulk.children.isEmpty())
                    .ifPresent(condizioneRicercaBulk -> {
                        ricercaLiberaBP.setCondizioneRadice(condizioneRicercaBulk);
                        ricercaLiberaBP.setCondizioneCorrente(
                                (CondizioneRicercaBulk)condizioneRicercaBulk.children.stream()
                                        .filter(CondizioneSempliceBulk.class::isInstance)
                                        .map(CondizioneSempliceBulk.class::cast)
                                        .reduce((first, second) -> second)
                                        .orElse(null)
                        );
                        ricercaLiberaBP.setRadice(condizioneRicercaBulk);
                    });
            context.addHookForward("searchResult",this,"doRigheSelezionate");
            return context.addBusinessProcess(ricercaLiberaBP);
        } catch(Throwable e) {
            return handleException(context,e);
        }
    }

    public Forward doRigheSelezionate(ActionContext context) {
        try {
            ContabilizzaOrdineBP bp = (ContabilizzaOrdineBP)context.getBusinessProcess();
            HookForward hook = (HookForward)context.getCaller();
            Optional.ofNullable(hook.getParameter("remoteIterator"))
                    .filter(RemoteIterator.class::isInstance)
                    .map(RemoteIterator.class::cast)
                    .ifPresent(ri -> {
                        try {
                            bp.setIterator(context,ri);
                        } catch (RemoteException|BusinessProcessException e) {
                            throw new DetailedRuntimeException(e);
                        }
                    });
            Optional.ofNullable(hook.getParameter("condizioneRadice"))
                    .filter(CondizioneComplessaBulk.class::isInstance)
                    .map(CondizioneComplessaBulk.class::cast)
                    .ifPresent(condizioneRicercaBulk -> {
                            bp.setCondizioneCorrente(condizioneRicercaBulk);
                    });
            bp.setDirty(true);
            return context.findDefaultForward();
        } catch(Throwable e) {
            return handleException(context,e);
        }
    }

}
