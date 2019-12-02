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
* Created by Generator 1.0
* Date 23/06/2006
*/
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class Obbligazione_mod_voceBase extends Obbligazione_mod_voceKey implements Keyed {
//    IM_MODIFICA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_modifica;
 
	public Obbligazione_mod_voceBase() {
		super();
	}
	public Obbligazione_mod_voceBase(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_modifica, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_voce, java.lang.String cd_centro_responsabilita, java.lang.String cd_linea_attivita) {
		super(cd_cds, esercizio, pg_modifica, ti_appartenenza, ti_gestione, cd_voce, cd_centro_responsabilita, cd_linea_attivita);
	}
	public java.math.BigDecimal getIm_modifica () {
		return im_modifica;
	}
	public void setIm_modifica(java.math.BigDecimal im_modifica)  {
		this.im_modifica=im_modifica;
	}
}