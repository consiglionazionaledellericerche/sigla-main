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
 * Date 12/09/2016
 */
package it.cnr.contab.docamm00.consultazioni.bulk;
public class VFatturaPassivaRigaBrevettiBulk extends VFatturaPassivaRigaBrevettiBase {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_FATTURA_PASSIVA_RIGA_BREVETTI
	 **/
	public VFatturaPassivaRigaBrevettiBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_FATTURA_PASSIVA_RIGA_BREVETTI
	 **/
	public VFatturaPassivaRigaBrevettiBulk(String cd_cds,String cd_unita_organizzativa,Integer esercizio, Long pg_fattura_attiva,Long progressivo_riga) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura_attiva,progressivo_riga);
	}
}