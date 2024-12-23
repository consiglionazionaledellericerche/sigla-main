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

import java.math.BigDecimal;

import it.cnr.contab.gestiva00.bp.LiquidazioneMassaIvaBP;
import it.cnr.contab.gestiva00.core.bulk.IPrintable;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_massa_ivaVBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_massa_provvisoria_ivaVBulk;
import it.cnr.contab.gestiva00.core.bulk.Stampa_registri_ivaVBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.MTUWrapper;

/**
 * Insert the type's description here.
 * Creation date: (2/24/2003 11:34:52 AM)
 * @author: CNRADM
 */
public class LiquidazioneMassaIvaAction extends LiquidazioneDefinitivaIvaAction {
/**
 * LiquidazioneMassaIvaAction constructor comment.
 */
public LiquidazioneMassaIvaAction() {
	super();
}
protected void aggiornaRegistriStampati(ActionContext context)
	throws Throwable {

	//Does nothing by default
}
protected Forward basicDoCerca(
		ActionContext context)
		throws Throwable {
		String message="Si è scelto di effettuare la Liquidazione Massiva per tutte le UO."
					+ "Si desidera continuare?";
	    return openConfirm(context,message,it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO,"doConfermaCerca");
}
public Forward doConfermaCerca(ActionContext context,int option) {

	try {
		if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
			return basicDoCercaConfermato(context);
		}
	} catch (Throwable t) {
		return handleException(context, t);
	}
	return context.findDefaultForward();
}
protected Forward basicDoCercaConfermato(
		ActionContext context)
		throws Throwable {

		LiquidazioneMassaIvaBP bp= (LiquidazioneMassaIvaBP) context.getBusinessProcess();
		Stampa_registri_ivaVBulk bulk = (Stampa_registri_ivaVBulk)bp.getModel();
		bulk.setEsercizio(CNRUserContext.getEsercizio(context.getUserContext()));

		bulk.setRistampa(false);
	       	
		try {
			MTUWrapper wrapper = manageStampa(context, bulk);

			Liquidazione_massa_ivaVBulk stampaBulk= (Liquidazione_massa_ivaVBulk) wrapper.getBulk();
			stampaBulk.setEsercizio(CNRUserContext.getEsercizio(context.getUserContext()));
			bp.setModel(context, stampaBulk);

			String message = getMessageFrom(wrapper);
			bp.commitUserTransaction();
			if (message != null)
				bp.setMessage(message);

			bp.resetForSearch(context);
			
		} catch (Throwable t) {
			bp.rollbackUserTransaction();
			return handleException(context, t);
		}

		return context.findDefaultForward();
	}

protected Forward basicDoRistampa(ActionContext context) 
	throws Throwable {

	LiquidazioneMassaIvaBP bp= (LiquidazioneMassaIvaBP) context.getBusinessProcess();
	bp.setMessage(
				it.cnr.jada.util.action.OptionBP.ERROR_MESSAGE, 
				"Questo tipo di liquidazione non è ristampabile");
	return context.findDefaultForward();
}
public Forward doOnTipoChange(ActionContext context) {

    try {
        fillModel(context);
		aggiornaRegistriStampati(context);
        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
public it.cnr.jada.action.Forward doLiquidazioneMassivaProvvisoria(ActionContext context) {
    try {
        fillModel(context);
        return openConfirm(context,"Desideri effettuare la liquidazione provvisoria di tutte le UO per il calcolo dell'iva da versare?",it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO,"doConfermaLiquidazioneMassivaProvvisoria");
    } catch (Throwable e) {
        return handleException(context, e);
    }
}
public Forward doConfermaLiquidazioneMassivaProvvisoria(ActionContext context,int option) {
	try {
		if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
			LiquidazioneMassaIvaBP bp= (LiquidazioneMassaIvaBP) context.getBusinessProcess();
			MTUWrapper wrapper = makeLiquidazioneMassivaProvvisoria(context);

			String message = getMessageFrom(wrapper);
			bp.commitUserTransaction();
			if (message != null)
				bp.setMessage(message);
			return context.findDefaultForward();
		}
	} catch (Throwable t) {
		return handleException(context, t);
	}
	return context.findDefaultForward();
}
private MTUWrapper makeLiquidazioneMassivaProvvisoria(ActionContext context) throws Throwable{
	LiquidazioneMassaIvaBP bp= (LiquidazioneMassaIvaBP) context.getBusinessProcess();
	Stampa_registri_ivaVBulk model = (Stampa_registri_ivaVBulk)bp.getModel();
	
	Liquidazione_massa_provvisoria_ivaVBulk modelProvv = new Liquidazione_massa_provvisoria_ivaVBulk();
	modelProvv.initializeForSearch(bp, context);
	modelProvv.setData_da(model.getData_da());
	modelProvv.setData_a(model.getData_a());
	modelProvv.setMese(model.getMese());
	modelProvv.setUser(model.getUser());
	modelProvv.setRistampa(false);
		   	
	return manageStampa(context, modelProvv);
}
}
