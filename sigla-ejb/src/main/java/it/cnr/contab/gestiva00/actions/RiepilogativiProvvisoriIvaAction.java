package it.cnr.contab.gestiva00.actions;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.gestiva00.ejb.*;
import java.util.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.contab.gestiva00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.BulkBP;

/**
 * Registri riepilogativi
 */

public class RiepilogativiProvvisoriIvaAction  extends StampaAction{
public RiepilogativiProvvisoriIvaAction() {
	super();
}

/**
 * Gestisce il cambiamento del sezionale
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */

public Forward doOnSezionaliFlagsChange(ActionContext context) {

    try {
        StampaRegistriIvaBP bp = (StampaRegistriIvaBP) context.getBusinessProcess();

        Riepilogativi_ivaVBulk riepilogativo = (Riepilogativi_ivaVBulk) bp.getModel();
        fillModel(context);
        if (riepilogativo.getSezionaliFlag() == null || riepilogativo.getSezionaliFlag().equals(riepilogativo.SEZIONALI_FLAGS_SEZ)) {
            //riepilogativo.setSezionaliFlag("");
            Tipo_sezionaleBulk sezionale = new Tipo_sezionaleBulk();
        } else {
            Tipo_sezionaleBulk sezionale = new Tipo_sezionaleBulk();
            sezionale.setTi_acquisti_vendite(riepilogativo.getSezionaliFlag());
            riepilogativo.setTipo_sezionale(sezionale);
        }
        //bp.setModel(context, riepilogativo);
        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}

/*
 * Gestisce il cambiamento del tipo del sezionale
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */

public Forward doOnTipoChange(ActionContext context) {

    try {
        fillModel(context);
        StampaRegistriIvaBP bp = (StampaRegistriIvaBP) context.getBusinessProcess();

        Riepilogativi_ivaVBulk riepilogativo = (Riepilogativi_ivaVBulk) bp.getModel();
	
        if (riepilogativo.SEZIONALI_SAN_MARINO_SENZA_IVA.equalsIgnoreCase(riepilogativo.getTipoSezionaleFlag()) &&
	        riepilogativo.SEZIONALI_FLAGS_VEN.equalsIgnoreCase(riepilogativo.getSezionaliFlag()))
			riepilogativo.setSezionaliFlag(riepilogativo.SEZIONALI_FLAGS_ACQ);

		riepilogativo.setTipi_sezionali(
			bp.createComponentSession().selectTipi_sezionaliByClause(
												context.getUserContext(),
												riepilogativo,
												new it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk(),
												null));

        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
}