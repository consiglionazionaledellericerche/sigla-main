package it.cnr.contab.gestiva00.actions;

import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.doccont00.comp.DateServices;
import it.cnr.contab.gestiva00.ejb.*;
import java.util.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.contab.gestiva00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.BulkBP;

/**
 * Registri riepilogativi
 */

public class RiepilogativiIvaCentroDefinitivoAction  extends RiepilogativiDefinitiviIvaAction {
public RiepilogativiIvaCentroDefinitivoAction() {
	super();
}
public it.cnr.jada.action.Forward doSettaInteroAnno(ActionContext context) {

	RiepilogativiIvaCentroDefinitivoBP bp= (RiepilogativiIvaCentroDefinitivoBP) context.getBusinessProcess();
	try {
		bp.fillModel(context);

	} catch (Exception e) {

	}
	return setDateInteroAnno(context, (Riepilogativi_iva_centro_definitivoVBulk) bp.getModel());
}
protected it.cnr.jada.action.Forward setDateInteroAnno(
	ActionContext context,
	Riepilogativi_iva_centro_definitivoVBulk stampaBulk) {
		int esercizio = stampaBulk.getEsercizio().intValue();
		if (stampaBulk.getIntero_anno().booleanValue())
		{
		stampaBulk.setData_da(DateServices.getFirstDayOfYear(esercizio));
		stampaBulk.setData_a(DateServices.getLastDayOfYear(esercizio));
		}
		else
		{
		stampaBulk.setData_da(null);
		stampaBulk.setData_a(null);
		}
		stampaBulk.setMese(null);
	return context.findDefaultForward();
}
public it.cnr.jada.action.Forward doOnMeseChange(ActionContext context) {
	RiepilogativiIvaCentroDefinitivoBP bp= (RiepilogativiIvaCentroDefinitivoBP) context.getBusinessProcess();
	Riepilogativi_iva_centro_definitivoVBulk stampaBulk = (Riepilogativi_iva_centro_definitivoVBulk) bp.getModel();
	stampaBulk.setIntero_anno(new Boolean(false));
	return super.doOnMeseChange(context);
}

}
