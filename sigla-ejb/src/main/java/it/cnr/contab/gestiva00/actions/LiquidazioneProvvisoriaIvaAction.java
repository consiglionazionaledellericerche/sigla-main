/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.gestiva00.actions;

import it.cnr.contab.gestiva00.bp.LiquidazioneIvaBP;
import it.cnr.contab.gestiva00.bp.LiquidazioneProvvisoriaIvaBP;
import it.cnr.contab.gestiva00.core.bulk.IPrintable;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_provvisoria_ivaVBulk;
import it.cnr.contab.gestiva00.core.bulk.Stampa_registri_ivaVBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.MTUWrapper;

/**
 * liquidazione iva
 */

public class LiquidazioneProvvisoriaIvaAction  extends StampaAction{
public LiquidazioneProvvisoriaIvaAction() {
	super();
}
/**
 * gestisce la creazione dei registri iva
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
protected Forward basicDoCerca(
	ActionContext context)
	throws Throwable {

	LiquidazioneIvaBP bp= (LiquidazioneIvaBP) context.getBusinessProcess();
	if (bp.isLiquidato())
		throw new it.cnr.jada.comp.ApplicationException("Premere su 'reset dati' prima di proseguire");
	Stampa_registri_ivaVBulk bulk = (Stampa_registri_ivaVBulk)bp.getModel();

	if (bp.isBulkPrintable())
		((IPrintable)bulk).setId_report(null);
	bulk.setRistampa(false);
       	
	MTUWrapper wrapper = manageStampa(context, bulk);

	((LiquidazioneIvaBP) bp).setLiquidato(true);
	Liquidazione_provvisoria_ivaVBulk stampaBulk= (Liquidazione_provvisoria_ivaVBulk) wrapper.getBulk();
	stampaBulk.aggiornaTotali();
	bp.setModel(context, stampaBulk);

	return doStampa(context, (IPrintable)bp.getModel(), wrapper);
}
public Forward doOnTipoChange(ActionContext context) {

    try {
        fillModel(context);
        //LiquidazioneProvvisoriaIvaBP bp = (LiquidazioneProvvisoriaIvaBP) context.getBusinessProcess();
        //Liquidazione_provvisoria_ivaVBulk liquidazione = (Liquidazione_provvisoria_ivaVBulk) bp.getModel();
		//liquidazione.setTipi_sezionali(
			//bp.createComponentSession().selectTipi_sezionaliByClause(
												//context.getUserContext(),
												//liquidazione,
												//new it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk(),
												//null));

		//bp.aggiornaProspetti(context, liquidazione);
		//aggiornaRegistriStampati(context);
		
        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
/**
 * gestisce un'operazione di reset per la liquidazione provvisoria
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doReset(ActionContext context) {

    try {
        LiquidazioneProvvisoriaIvaBP bp= (LiquidazioneProvvisoriaIvaBP) context.getBusinessProcess();

        bp.setModel(context, bp.createNewBulk(context));

        return context.findDefaultForward();
    } catch (Throwable e) {
        return handleException(context, e);
    }
}
}
