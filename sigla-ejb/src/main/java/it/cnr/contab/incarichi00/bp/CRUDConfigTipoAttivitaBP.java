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

package it.cnr.contab.incarichi00.bp;

import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;

public class CRUDConfigTipoAttivitaBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private Unita_organizzativaBulk uoSrivania;

	public CRUDConfigTipoAttivitaBP() {
		super();
	}

	public CRUDConfigTipoAttivitaBP(String function) {
		super(function);
	}

	private boolean isEnteCNR = true;

	protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
		super.initialize(actioncontext);
		try {
			Parametri_enteBulk parEnte = Utility.createParametriEnteComponentSession().getParametriEnte(actioncontext.getUserContext());
			setEnteCNR(parEnte.isEnteCNR());
		} catch (Throwable throwable) {
			throw new BusinessProcessException(throwable);
		}
	}

	public void setEnteCNR(boolean isEnteCNR) {
		this.isEnteCNR = isEnteCNR;
	}

	public boolean isEnteCNR() {
		return isEnteCNR;
	}
}

