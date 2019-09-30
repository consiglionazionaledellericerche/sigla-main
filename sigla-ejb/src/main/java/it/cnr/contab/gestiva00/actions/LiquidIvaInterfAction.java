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

import java.rmi.RemoteException;
import java.sql.*;

import javax.ejb.RemoveException;

import it.cnr.contab.gestiva00.ejb.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.contab.gestiva00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.OptionBP;

public class LiquidIvaInterfAction extends it.cnr.jada.util.action.CRUDAction{
public LiquidIvaInterfAction() {
	super();
}

/**
 * Gestisce il cambiamento del mese impostando le relative dati inizio e fine
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doOnMeseChange(ActionContext context) {
	it.cnr.jada.util.action.SimpleCRUDBP bp= (it.cnr.jada.util.action.SimpleCRUDBP) context.getBusinessProcess();
    try {
        bp.fillModel(context); 
	    setDataDaA(context, (Liquid_iva_interfBulk) bp.getModel());
        return doCerca(context);
	} catch (Exception e) {
		return handleException(context,e);
	}        
}
public Forward doCerca(ActionContext context) throws RemoteException, InstantiationException, RemoveException{
	it.cnr.contab.gestiva00.bp.LiquidIvaInterfBP bp= (it.cnr.contab.gestiva00.bp.LiquidIvaInterfBP) context.getBusinessProcess();
	try {
		bp.fillModel(context); 
	    if (!bp.controllaQuery(context,(Liquid_iva_interfBulk) bp.getModel()))
	    {
			return openConfirm(context,"Non esistono dati per il periodo selezionato. Il sistema li caricherà automaticamente. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfirmCerca");
	    }
		super.doCerca(context);
	    return context.findDefaultForward();
	} catch (Exception e) {
			return handleException(context,e); 
	} 		  
}

public Forward doConfirmCerca(ActionContext context,int option) throws java.rmi.RemoteException {
	it.cnr.contab.gestiva00.bp.LiquidIvaInterfBP bp= (it.cnr.contab.gestiva00.bp.LiquidIvaInterfBP) context.getBusinessProcess();
	try
	{
		bp.fillModel(context);
		if (option == OptionBP.YES_BUTTON) 
		{
			bp.inserisci(context,(Liquid_iva_interfBulk) bp.getModel()); 
			return super.doCerca(context);
		}
		else
		{
			bp.resetForSearch(context);
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
protected it.cnr.jada.action.Forward setDataDaA(
	ActionContext context,
	Liquid_iva_interfBulk interfBulk) {

	try {
		int esercizio = (((it.cnr.contab.utenze00.bp.CNRUserContext)context.getUserContext()).getEsercizio()).intValue();
		int meseIndex = ((Integer)interfBulk.getMesi_int().get(interfBulk.getMese())).intValue();
		java.util.GregorianCalendar gc = getGregorianCalendar();
		gc.set(java.util.Calendar.DAY_OF_MONTH, 1);
		gc.set(java.util.Calendar.YEAR, esercizio);

		gc.set(java.util.Calendar.MONTH, meseIndex-1);
		interfBulk.setDt_inizio(new Timestamp(gc.getTime().getTime()));
		gc.set(java.util.Calendar.MONTH, meseIndex);
		gc.add(java.util.Calendar.DAY_OF_MONTH, -1);
		interfBulk.setDt_fine(new Timestamp(gc.getTime().getTime()));

	} catch (Exception e) {
		return handleException(context, e);
	}
	return context.findDefaultForward();
}
protected java.util.GregorianCalendar getGregorianCalendar() {

	java.util.GregorianCalendar gc = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
	
	gc.set(java.util.Calendar.HOUR, 0);
	gc.set(java.util.Calendar.MINUTE, 0);
	gc.set(java.util.Calendar.SECOND, 0);
	gc.set(java.util.Calendar.MILLISECOND, 0);
	gc.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
	
	return gc;
}
}
