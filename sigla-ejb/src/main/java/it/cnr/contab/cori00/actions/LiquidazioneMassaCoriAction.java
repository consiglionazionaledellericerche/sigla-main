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

package it.cnr.contab.cori00.actions;

import it.cnr.contab.cori00.bp.LiquidazioneMassaCoriBP;
import it.cnr.contab.cori00.docs.bulk.Liquidazione_massa_coriBulk;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;

import java.sql.*;

public class LiquidazioneMassaCoriAction extends it.cnr.jada.util.action.BulkAction{
public LiquidazioneMassaCoriAction() {
	super();
}
public Forward doEseguiLiquidMassaCori(ActionContext context) throws ApplicationException {	

	try{
		fillModel(context);
		LiquidazioneMassaCoriBP bp = (LiquidazioneMassaCoriBP) context.getBusinessProcess();
		Liquidazione_massa_coriBulk liquidMassa = (Liquidazione_massa_coriBulk)bp.getModel();
		if (bp.isLiquidato())
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Premere su 'Reset Dati' prima di proseguire"));

		if (liquidMassa.getData_da() == null)
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare la data di Inizio Periodo"));

		if (liquidMassa.getData_a() == null)
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare la data di Fine Periodo"));

		if (liquidMassa.getData_da().after(liquidMassa.getData_a()))
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: la data di Fine Periodo deve essere maggiore della data di Inizio Periodo"));
		
		try {
				bp.doEseguiLiquidMassaCori(context,liquidMassa);
				bp.setIsLiquidato(true);
		} catch (BusinessProcessException e) {
			return handleException(context, e);
		}
		bp.setMessage("Operazione completata. Verificare il Risultato dell'Elaborazione.");
		return context.findDefaultForward();
	} catch (it.cnr.jada.bulk.FillException e){
		return handleException(context, e);
	}
}
public Forward doReset(ActionContext context) {

	try {
		LiquidazioneMassaCoriBP bp= (LiquidazioneMassaCoriBP) context.getBusinessProcess();
		bp.doReset(context);

		return context.findDefaultForward();
	} catch (Throwable e) {
		return handleException(context, e);
	}
}
public Forward doCercaBatch(ActionContext context) {
	try {
		fillModel(context);
		LiquidazioneMassaCoriBP bp = (LiquidazioneMassaCoriBP) context.getBusinessProcess();
		Liquidazione_massa_coriBulk liquidMassa = (Liquidazione_massa_coriBulk)bp.getModel();
	
		if (liquidMassa.getPg_exec() == null)
		return handleException(context, new it.cnr.jada.bulk.ValidationException("Inserire il Progressivo dell'Elaborazione prima di effettuare la Ricerca"));

		try {
			bp.doCercaBatch(context,liquidMassa);
			bp.setIsLiquidato(true);
		} catch (Throwable e) {
		   	return handleException(context, e);
		}
	} catch (FillException e1) {
		return handleException(context, e1);
	}
	return context.findDefaultForward();
}
}
