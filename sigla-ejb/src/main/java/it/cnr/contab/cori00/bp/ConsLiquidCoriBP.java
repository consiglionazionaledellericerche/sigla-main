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

package it.cnr.contab.cori00.bp;

import it.cnr.contab.cori00.ejb.Liquid_coriComponentSession;
import it.cnr.contab.cori00.views.bulk.ParSelConsLiqCoriBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;

import java.rmi.RemoteException;


public class ConsLiquidCoriBP extends BulkBP
{
	public Button[] createToolbar() {
		Button[] toolbar = new Button[1];
		int i = 0;
		toolbar[i++] = new Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"buttons.ricerca");
		return toolbar;
	}

	protected void init(Config config,ActionContext context) throws BusinessProcessException {

		Liquid_coriComponentSession cs = (Liquid_coriComponentSession)createComponentSession(context);
		ParSelConsLiqCoriBulk bulk = new ParSelConsLiqCoriBulk();
		try {
			bulk = cs.initializeConsultazioneCori(context.getUserContext(), bulk);
		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}

		setModel(context,bulk);
		super.init(config,context);
	}

	public RemoteIterator find(ActionContext actioncontext, CompoundFindClause clauses, OggettoBulk bulk, OggettoBulk context, String property) throws BusinessProcessException {
		try {
			CRUDComponentSession cs = createComponentSession(actioncontext);
			if (cs == null) return null;
			return EJBCommonServices.openRemoteIterator(
					actioncontext,
					cs.cerca(
							actioncontext.getUserContext(),
							clauses,
							bulk,
							getModel(),
							property));
		} catch (it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch (java.rmi.RemoteException e) {
			throw handleException(e);
		}
	}

	public void controlloSelezioneValorizzata(ActionContext context, ParSelConsLiqCoriBulk bulk) throws ValidationException{
		if (bulk.getPgInizio()== null && bulk.getPgFine()== null && bulk.getaLiquidazione() == null && bulk.getDaLiquidazione() == null)
			throw new ValidationException("Valorizzare un parametro di selezione");
		if ((bulk.getPgInizio()!= null && bulk.getPgFine()== null ) || (bulk.getPgInizio() == null && bulk.getPgFine() != null ))
			throw new ValidationException("Valorizzare il numero iniziale ed il numero finale del mandato");
		if ((bulk.getDaLiquidazione()!= null && bulk.getDaLiquidazione().getPg_liquidazione() != null && (bulk.getaLiquidazione()== null || bulk.getaLiquidazione().getPg_liquidazione() == null)) ||
				((bulk.getDaLiquidazione() == null || bulk.getDaLiquidazione().getPg_liquidazione() == null)  && bulk.getaLiquidazione() != null && bulk.getaLiquidazione().getPg_liquidazione() != null))
			throw new ValidationException("Valorizzare il numero iniziale ed il numero finale della liquidazione");
	}
	public RemoteIterator ricercaCori(ActionContext actioncontext) throws BusinessProcessException {
		try {
			Liquid_coriComponentSession cs = (Liquid_coriComponentSession)createComponentSession(actioncontext);
			if (cs == null) return null;
			ParSelConsLiqCoriBulk parSelConsLiqCoriBulk = (ParSelConsLiqCoriBulk)getModel();
			return cs.ricercaCori(actioncontext.getUserContext(), parSelConsLiqCoriBulk);
		} catch (it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch (java.rmi.RemoteException e) {
			throw handleException(e);
		}

	}

	public it.cnr.jada.ejb.CRUDComponentSession createComponentSession(ActionContext actionContext) throws BusinessProcessException {

		return (Liquid_coriComponentSession) createComponentSession(
				"CNRCORI00_EJB_Liquid_coriComponentSession",
				Liquid_coriComponentSession.class);

	}
}
