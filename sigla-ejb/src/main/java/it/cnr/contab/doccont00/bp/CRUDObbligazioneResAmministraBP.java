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

package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;

import java.util.Optional;

public class CRUDObbligazioneResAmministraBP extends CRUDObbligazioneResBP {
	private static final long serialVersionUID = 1L;

	public CRUDObbligazioneResAmministraBP() {
		super();
	}

	public CRUDObbligazioneResAmministraBP(String function) {
		super(function);
	}

	@Override
	public boolean isStatoVisibile() {
		return true;
	}
	
	@Override
	public boolean isROStato() {
		return false;
	}

	@Override
	public void basicEdit(ActionContext context, OggettoBulk bulk, boolean doInitializeForEdit) throws BusinessProcessException {
		super.basicEdit(context, bulk, doInitializeForEdit);
		Optional.ofNullable(getModel())
				.filter(ObbligazioneBulk.class::isInstance)
				.map(ObbligazioneBulk.class::cast)
				.ifPresent(obbligazioneBulk -> obbligazioneBulk.setAmministra(Boolean.TRUE));
	}
}
