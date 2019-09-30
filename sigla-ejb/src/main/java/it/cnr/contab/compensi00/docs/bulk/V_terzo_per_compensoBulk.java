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

package it.cnr.contab.compensi00.docs.bulk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(value=Include.NON_NULL)
public class V_terzo_per_compensoBulk extends V_terzo_per_compensoBase {

	public static final int TUTTO_BENE = 0;
	public static final int TERZO_ASSENTE = 1;
	public static final int TERZO_NON_VALIDO = 2;
	public static final int TERZO_SENZA_MOD_PAG = 3;
	public static final int TIPO_RAPP_ASSENTE = 4;
	public static final int TIPO_RAPP_NON_VALIDO = 5;
	public static final int TIPO_TRATT_ASSENTE = 6;
	public static final int TIPO_TRATT_NON_VALIDO = 7;

	private it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk anagrafico;
	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo;
	private it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipoRapporto;
	public V_terzo_per_compensoBulk() {
		super();
	}
	public V_terzo_per_compensoBulk(java.lang.Integer cd_terzo, String ti_dipendente_altro) {
		super(cd_terzo, ti_dipendente_altro);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (25/02/2002 12.34.00)
	 * @return it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
	 */
	public it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk getAnagrafico() {
		return anagrafico;
	}
	public java.lang.String getCd_terzo_precedente() {
		if (getTerzo() != null)
			return getTerzo().getCd_precedente();
		return super.getCd_terzo_precedente();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (25/02/2002 12.34.31)
	 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
	 */
	public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo() {
		return terzo;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (26/02/2002 14.42.18)
	 * @return it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
	 */
	public it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk getTipoRapporto() {
		return tipoRapporto;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (25/02/2002 12.34.00)
	 * @param newAnagrafico it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
	 */
	public void setAnagrafico(it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk newAnagrafico) {
		anagrafico = newAnagrafico;
	}
	public void setCd_terzo_precedente(java.lang.String newCd_terzo_precedente) {

		if (getTerzo() != null)
			getTerzo().setCd_precedente(newCd_terzo_precedente);
		else
			super.setCd_terzo_precedente(newCd_terzo_precedente);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (25/02/2002 12.34.31)
	 * @param newTerzo it.cnr.contab.anagraf00.core.bulk.TerzoBulk
	 */
	public void setTerzo(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newTerzo) {
		terzo = newTerzo;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (26/02/2002 14.42.18)
	 * @param newTipoRapporto it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
	 */
	public void setTipoRapporto(it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk newTipoRapporto) {
		tipoRapporto = newTipoRapporto;
	}
}
