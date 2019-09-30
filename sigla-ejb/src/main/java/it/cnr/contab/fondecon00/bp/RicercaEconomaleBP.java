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

package it.cnr.contab.fondecon00.bp;

import it.cnr.contab.fondecon00.core.bulk.*;
import it.cnr.contab.fondecon00.ejb.FondoEconomaleComponentSession;

public class RicercaEconomaleBP extends it.cnr.jada.util.action.SelezionatoreListaBP {

	private Fondo_economaleBulk lastFondoSelected = null;
	public RicercaEconomaleBP() {
		super();
	}

	public RicercaEconomaleBP(String function) {
		super(function);
		setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Fondo_economaleBulk.class));
		setBulkInfo(getBulkInfo());
	}

	public FondoEconomaleComponentSession createComponentSession() throws it.cnr.jada.action.BusinessProcessException {
		try {
			return (FondoEconomaleComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRFONDECON00_EJB_FondoEconomaleComponentSession",FondoEconomaleComponentSession.class);
		} catch(Throwable e) {
			throw handleException(e);
		}
	}

/**
 * Insert the method's description here.
 * Creation date: (4/19/2002 11:55:08 AM)
 * @return it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk
 */
public it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk getLastFondoSelected() {
	return lastFondoSelected;
}
	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		super.init(config,context);
		try {
			setIterator(
				context,
				createComponentSession().cercaFondi(context.getUserContext())
			);
		} catch(Throwable e) {
			throw new it.cnr.jada.action.BusinessProcessException(e);
		}
	}
/**
 * Insert the method's description here.
 * Creation date: (4/19/2002 11:55:08 AM)
 * @param newLastFondoSelected it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk
 */
public void setLastFondoSelected(it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk newLastFondoSelected) {
	lastFondoSelected = newLastFondoSelected;
}
}
