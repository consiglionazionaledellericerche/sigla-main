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

public class Liquidazione_iva_variazioniBulk extends Liquidazione_iva_variazioniBase {
	private static final long serialVersionUID = 1L;

	public Liquidazione_iva_variazioniBulk() {
		super();
	}
	
	public Liquidazione_iva_variazioniBulk(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.String cd_unita_organizzativa,java.lang.String tipo_liquidazione,java.sql.Timestamp dt_inizio,java.sql.Timestamp dt_fine,Long pg_dettaglio) {
		super(cd_cds,esercizio,cd_unita_organizzativa,tipo_liquidazione,dt_inizio,dt_fine,pg_dettaglio);
	}
}