package it.cnr.contab.doccont00.consultazioni.action;

import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleAllegatiBulk;
import it.cnr.contab.doccont00.consultazioni.bp.ConsControlliPCCBP;
import it.cnr.contab.doccont00.consultazioni.bulk.ControlliPCCParams;
import it.cnr.contab.doccont00.consultazioni.bulk.VControlliPCCBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.jada.action.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.SelezionatoreListaAction;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.config.StoragePropertyNames;

import javax.servlet.ServletException;
import java.io.*;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

public class ConsControlliPCCAction extends SelezionatoreListaAction {

    public Forward doEstraiCSV(ActionContext context) throws RemoteException {
        ConsControlliPCCBP bp = (ConsControlliPCCBP) context.getBusinessProcess();
        try {
            fillModel(context);
            bp.setSelection(context);
            List<VControlliPCCBulk> selectedElements = bp.getSelectedElements(context);
            if (selectedElements == null || selectedElements.isEmpty())
                throw new ApplicationException("Selezionare almeno una riga!");
            final CNRUserInfo userInfo = (CNRUserInfo) context.getUserInfo();
            final String codiceFiscale = Optional.ofNullable(userInfo.getUtente())
                    .flatMap(utenteBulk -> Optional.ofNullable(utenteBulk.getCodiceFiscaleLDAP()))
                    .orElse(null);
            ControlliPCCParams controlliPCCParams = new ControlliPCCParams();
            controlliPCCParams.setCodiceFiscale(codiceFiscale);
            BulkBP controlliPCCParamsBP = (BulkBP) context.createBusinessProcess("ControlliPCCParamsBP", new Object[]{});
            controlliPCCParamsBP.setModel(context, controlliPCCParams);
            context.addHookForward("model", this, "doConfirmEstraiCSV");
            return context.addBusinessProcess(controlliPCCParamsBP);
        } catch (Exception e) {
            return handleException(context, e);
        }
    }

    public Forward doConfirmEstraiCSV(ActionContext context) throws BusinessProcessException {
        ConsControlliPCCBP bp = (ConsControlliPCCBP) context.getBusinessProcess();
        HookForward caller = (HookForward) context.getCaller();
        ControlliPCCParams controlliPCCParams = (ControlliPCCParams) caller.getParameter("model");
        List<VControlliPCCBulk> vControlliPCCBulks = bp.getSelectedElements(context);
        bp.elaboraCSV(controlliPCCParams, vControlliPCCBulks);
        bp.clearSelection(context);
        return doVisualizzaAllegatiCSV(context);
    }

    public Forward doVisualizzaAllegatiCSV(ActionContext context) throws BusinessProcessException {
        final BusinessProcess allegatiPCCBP = context.createBusinessProcess("AllegatiPCCBP", new Object[]{"M"});
        return context.addBusinessProcess(allegatiPCCBP);
    }
}
