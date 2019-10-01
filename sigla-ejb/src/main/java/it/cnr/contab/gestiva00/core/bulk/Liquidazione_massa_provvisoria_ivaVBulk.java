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

package it.cnr.contab.gestiva00.core.bulk;

import java.math.BigDecimal;

/**
 * Insert the type's description here.
 * Creation date: (2/24/2003 10:54:31 AM)
 * @author: Roberto Peli
 */
public class Liquidazione_massa_provvisoria_ivaVBulk extends Liquidazione_provvisoria_ivaVBulk {

    public static BigDecimal REPORTID = new BigDecimal(-1000);

/**
 * Liquidazione_massa_ivaVBulk constructor comment.
 */
public Liquidazione_massa_provvisoria_ivaVBulk() {
	super();
}
public it.cnr.jada.bulk.OggettoBulk initializeForSearch(
	it.cnr.jada.util.action.BulkBP bp,
	it.cnr.jada.action.ActionContext context) {

	Liquidazione_massa_provvisoria_ivaVBulk bulk = (Liquidazione_massa_provvisoria_ivaVBulk)super.initializeForSearch(bp, context);
	
	bulk.setTipo_stampa(TIPO_STAMPA_LIQUIDAZIONE_MASSA_PRV);
	bulk.setId_report(REPORTID);
	return bulk;
}
public boolean isDBManagementRequired() {
	return false;
}
public boolean isRistampabile() {
	return false;
}
}
