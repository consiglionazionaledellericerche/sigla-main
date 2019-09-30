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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 14/03/2016
 */
package it.cnr.contab.doccont00.consultazioni.bulk;
public class CnrIfacDettObbligazioniBulk extends CnrIfacDettObbligazioniBase {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CNR_IFAC_DETT_OBBLIGAZIONI
	 **/
	public CnrIfacDettObbligazioniBulk(java.lang.String cdCds,java.lang.String cdCentroResponsabilita,java.lang.String cdLineaAttivita,java.lang.String cdVoce,java.lang.Integer esercizio,java.lang.Integer esercizioOriginale,java.lang.Long pgObbligazione,java.lang.Long pgObbligazioneScadenzario,java.lang.String tiAppartenenza,java.lang.String tiGestione,java.lang.Long pgDocamm) {
		super(cdCds, cdCentroResponsabilita, cdLineaAttivita, cdVoce, esercizio, esercizioOriginale, pgObbligazione, pgObbligazioneScadenzario, tiAppartenenza, tiGestione,pgDocamm);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CNR_IFAC_DETT_OBBLIGAZIONI
	 **/
	public CnrIfacDettObbligazioniBulk() {
		super();
	}
}