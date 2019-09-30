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
 * Date 14/06/2010
 */
package it.cnr.contab.docamm00.intrastat.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class FatturaAttivaIntraSBulk extends FatturaAttivaIntraSBase {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: FATTURA_ATTIVA_INTRA_S
	 **/
	public FatturaAttivaIntraSBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: FATTURA_ATTIVA_INTRA_S
	 **/
	public FatturaAttivaIntraSBulk(java.lang.String cdCds, java.lang.String cdUnitaOrganizzativa, java.lang.Integer esercizio, java.lang.Long pgFatturaAttiva, java.lang.Long pgRigaIntra, java.lang.Long pgStorico) {
		super(cdCds, cdUnitaOrganizzativa, esercizio, pgFatturaAttiva, pgRigaIntra, pgStorico);
	}
}