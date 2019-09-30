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

package it.cnr.contab.preventvar00.bp;
import it.cnr.contab.preventvar00.tabrif.bulk.*;

/**
 * Business Process di gestione delle causali di variazione di bilancio preventivo
 */

public class CRUDCausaleVarBilancioBP extends it.cnr.jada.util.action.SimpleCRUDBP {
public CRUDCausaleVarBilancioBP() {
	super();
}

public CRUDCausaleVarBilancioBP(String function) {
	super(function);
}

public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
	
	super.basicEdit(context, bulk, doInitializeForEdit);

	if (getStatus()!=VIEW){
		Causale_var_bilancioBulk causale = (Causale_var_bilancioBulk)bulk;
		if ( causale != null && causale.getTi_causale().equals(Causale_var_bilancioBulk.SISTEMA)) {
			setStatus(VIEW);
			setMessage("Causale non modificabile.");
		}
	}
}
}