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
 * Created on Nov 9, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg01.consultazioni.action;

import it.cnr.contab.pdg01.consultazioni.bp.ConsPdgpPdggEtrBP;
import it.cnr.contab.pdg01.consultazioni.bulk.V_cons_pdgp_pdgg_etrBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.ConsultazioniBP;

import java.util.Optional;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsPdgpPdggEtrAction extends ConsultazioniAction {
	public Forward doCaricaGestionale(ActionContext context) {
		try {
			fillModel(context);

			ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
			V_cons_pdgp_pdgg_etrBulk consPdgBulk = (V_cons_pdgp_pdgg_etrBulk)bp.getModel();

			if (consPdgBulk==null) {
				setErrorMessage(context,"Attenzione: è necessario selezionare un dettaglio!");
				return context.findDefaultForward();
			}
			
			BulkBP nbp = (BulkBP)context.getUserInfo().createBusinessProcess(
							context,
							"CRUDPdgModuloEntrateGestBP",
							new Object[] {
								bp.isEditable() ? "M" : "V",
								consPdgBulk
							}
						);

			context.addHookForward("close",this,"doBringBackCaricaGestionale");

			return context.addBusinessProcess(nbp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	/**
	 * @param context	L'ActionContext della richiesta
	 * @return Il Forward alla pagina di risposta
	 */
	public Forward doBringBackCaricaGestionale(ActionContext context) throws BusinessProcessException
	{
		try{
			Optional.ofNullable(context.getBusinessProcess())
					.filter(ConsultazioniBP.class::isInstance)
					.map(ConsultazioniBP.class::cast)
					.ifPresent(consultazioniBP -> {
                        try {
                            consultazioniBP.refresh(context);
                            consultazioniBP.setModel(context,null);
                        } catch (BusinessProcessException e) {
                            throw new DetailedRuntimeException(e);
                        }
                    });
			return context.findDefaultForward();
		} catch(Throwable e) {
		  return handleException(context,e);
		}
	}	
	public Forward doFiltraFiles(ActionContext context) {
		try{
			ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
			bp.setModel(context,null);
			return super.doFiltraFiles(context);
		} catch(Throwable e) {
		  return handleException(context,e);
		}
	}
	public Forward doCancellaFiltro(ActionContext context) {
		try{
			ConsultazioniBP bp = (ConsultazioniBP)context.getBusinessProcess();
			bp.setModel(context,null);
			return super.doCancellaFiltro(context);
		} catch(Throwable e) {
		  return handleException(context,e);
		}
	}
}