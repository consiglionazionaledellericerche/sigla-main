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

package it.cnr.contab.config00.pdcfin.bulk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class V_voce_f_partita_giroBulk extends Voce_fBulk {
	protected String ds_titolo_capitolo;
	/**
	 * V_voce_f_partita_giroBulk constructor comment.
	 */
	public V_voce_f_partita_giroBulk() {
		super();
	}
	/**
	 * V_voce_f_partita_giroBulk constructor comment.
	 * @param cd_voce java.lang.String
	 * @param esercizio java.lang.Integer
	 * @param ti_appartenenza java.lang.String
	 * @param ti_gestione java.lang.String
	 */
	public V_voce_f_partita_giroBulk(String cd_voce, Integer esercizio, String ti_appartenenza, String ti_gestione) {
		super(cd_voce, esercizio, ti_appartenenza, ti_gestione);
	}
	/**
	 * @return java.lang.String
	 */
	public java.lang.String getDs_titolo_capitolo() {
		return ds_titolo_capitolo;
	}
	/**
	 * @param newDs_titolo_capitolo java.lang.String
	 */
	public void setDs_titolo_capitolo(java.lang.String newDs_titolo_capitolo) {
		ds_titolo_capitolo = newDs_titolo_capitolo;
	}
}
