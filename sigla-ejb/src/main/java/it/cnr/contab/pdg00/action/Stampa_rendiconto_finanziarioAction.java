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

package it.cnr.contab.pdg00.action;

import java.rmi.RemoteException;

import it.cnr.contab.pdg00.cdip.bulk.Stampa_rendiconto_finanziarioVBulk;
import it.cnr.contab.reports.action.ParametricPrintAction;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;

public class Stampa_rendiconto_finanziarioAction extends ParametricPrintAction {
	@Override
	public Forward doDefault(ActionContext context) throws RemoteException {
		try{
			fillModel(context);
			ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
			Stampa_rendiconto_finanziarioVBulk stampa = (Stampa_rendiconto_finanziarioVBulk)bp.getModel();
			if (stampa.isDecisionale()){
				stampa.setDipartimento(null);
				stampa.setCds(null);
			}
			return super.doDefault(context);
		}catch(it.cnr.jada.bulk.FillException ex){
			return handleException(context, ex);
		}
	}
	public Forward doSeleziona(ActionContext context) {
		try{
			fillModel(context);
			ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
			Stampa_rendiconto_finanziarioVBulk stampa = (Stampa_rendiconto_finanziarioVBulk)bp.getModel();
			stampa.selezionaRagruppamenti();
			return context.findDefaultForward();
		}catch(it.cnr.jada.bulk.FillException ex){
			return handleException(context, ex);
		}
	}
	/**
	 * Stampa del dell'aggregato PDG parte entrate
	 */
	public it.cnr.jada.action.Forward doPrint(it.cnr.jada.action.ActionContext context) {
		try{
			fillModel(context);
			ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
			Stampa_rendiconto_finanziarioVBulk stampa = (Stampa_rendiconto_finanziarioVBulk)bp.getModel();		
			if (stampa.getTi_etr_spe().equals(Stampa_rendiconto_finanziarioVBulk.TIPO_ENTRATA)){
				if (stampa.getTipo_stampa().equalsIgnoreCase(Stampa_rendiconto_finanziarioVBulk.GESTIONALE))
				  bp.setReportName("/preventivo/preventivo/Rendiconto_finanziario_gestionale_Parte_Entrate.jasper");
				else if (stampa.getTipo_stampa().equalsIgnoreCase(Stampa_rendiconto_finanziarioVBulk.DECISIONALE))  
				  bp.setReportName("/preventivo/preventivo/Rendiconto_finanziario_decisionale_Parte_Entrate.jasper");
			}else if (stampa.getTi_etr_spe().equals(Stampa_rendiconto_finanziarioVBulk.TIPO_SPESA)){
				if (stampa.getTipo_stampa().equalsIgnoreCase(Stampa_rendiconto_finanziarioVBulk.GESTIONALE))
					  bp.setReportName("/preventivo/preventivo/Rendiconto_finanziario_gestionale_Parte_Spese.jasper");
				else if (stampa.getTipo_stampa().equalsIgnoreCase(Stampa_rendiconto_finanziarioVBulk.DECISIONALE))  
					  bp.setReportName("/preventivo/preventivo/Rendiconto_finanziario_decisionale_Parte_Spese.jasper");
			}
		return super.doPrint(context);
		}catch(it.cnr.jada.bulk.FillException ex){
			return handleException(context, ex);
		}
		
	}		

}
