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

package it.cnr.contab.docamm00.fatturapa.bulk;

public enum StatoDocumentoEleEnum {
	INIZIALE("SF01"),
	AGGIORNATO("SF02"),
	COMPLETO("SF02"),
	REGISTRATO("SF03"),
	RIFIUTATO("SF04"),
	//PEC_INVIATA("SF05"),
	DA_STORNARE("SF06"),
	RIFIUTATA_CON_PEC("SF07"),
	//PEC_NON_INVIATA("SF08"),
	STORNATO("SF09");

	private final String statoSDI;

	private StatoDocumentoEleEnum(String statoSDI) {
		this.statoSDI = statoSDI;
	}

	public String statoSDI() {
		return statoSDI;
	}

	public String label() {
		return this.name().replace("_", " ");
	}

	public static StatoDocumentoEleEnum fromStatoSDI(String v) {
		for (StatoDocumentoEleEnum c : StatoDocumentoEleEnum.values()) {
			if (c.statoSDI.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}	
	
}