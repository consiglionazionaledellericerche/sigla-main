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

package it.cnr.contab.doccont00.intcass.bulk;

import java.math.BigDecimal;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

public class V_mandato_reversale_scad_voceBulk extends V_mandato_reversale_scad_voceBase {
	public V_mandato_reversale_scad_voceBulk() {
		super();
	}
	public V_mandato_reversale_scad_voceBulk(Integer esercizio, Integer esercizio_originale, String ti_documento, String cd_cds, String cd_voce, String cd_centro_responsabilita, String cd_linea_attivita, Long pg_documento) {
		super(esercizio, esercizio_originale, ti_documento, cd_cds, cd_voce, cd_centro_responsabilita, cd_linea_attivita, pg_documento);
	}

}
