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

/*
 * Created on Apr 26, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.action;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.pdg00.bulk.Stampa_situazione_sintetica_x_progettoBulk;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;

public class StampaSituazioneSinteticaXProgettoAction extends it.cnr.contab.reports.action.ParametricPrintAction {
	public StampaSituazioneSinteticaXProgettoAction() {
		super();
	}

	public Forward doBlankSearchFindUoForPrint(ActionContext context, Stampa_situazione_sintetica_x_progettoBulk stampa){
		ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
		Stampa_situazione_sintetica_x_progettoBulk stampa_gae = ((Stampa_situazione_sintetica_x_progettoBulk)bp.getModel());
		stampa_gae.setUoForPrint(new Unita_organizzativaBulk());
		
		return context.findDefaultForward();
	}
	
	public Forward doBlankSearchFindCdsForPrint(ActionContext context, Stampa_situazione_sintetica_x_progettoBulk stampa){
		ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
		Stampa_situazione_sintetica_x_progettoBulk stampa_gae = ((Stampa_situazione_sintetica_x_progettoBulk)bp.getModel());
		stampa_gae.setCdsForPrint(new CdsBulk());
		stampa_gae.setUoForPrint(new Unita_organizzativaBulk());
		
		return context.findDefaultForward();
	}

	public Forward doBringBackSearchFindGaeForPrint(ActionContext context, Stampa_situazione_sintetica_x_progettoBulk stampa, WorkpackageBulk gae){
		ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
		Stampa_situazione_sintetica_x_progettoBulk stampa_gae = ((Stampa_situazione_sintetica_x_progettoBulk)bp.getModel());

		stampa_gae.setGaeForPrint(gae);
		if (gae!=null) {
			stampa_gae.setProgettoForPrint(gae.getProgetto());
			stampa_gae.setResponsabileGaeForPrint(gae.getResponsabile());
		}
		return context.findDefaultForward();
	}

	public Forward doCheckPrintGae(ActionContext context){
		try {
			fillModel(context);
			ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
			Stampa_situazione_sintetica_x_progettoBulk stampa = ((Stampa_situazione_sintetica_x_progettoBulk)bp.getModel());
			
			if (!stampa.getPrintGae())
				stampa.setPrintSoloGaeAttive(Boolean.FALSE);
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
}
