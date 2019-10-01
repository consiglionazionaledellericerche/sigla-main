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
* Date 14/09/2005
*/
package it.cnr.contab.prevent00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Pdg_piano_ripartoBase extends Pdg_piano_ripartoKey implements Keyed {
//    STATO CHAR(1) NOT NULL
	private java.lang.String stato;
 
//    IM_TOT_SPESE_ACC DECIMAL(15,2)
	private java.math.BigDecimal im_tot_spese_acc;
 
	public Pdg_piano_ripartoBase() {
		super();
	}
	public Pdg_piano_ripartoBase(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer id_classificazione) {
		super(esercizio, cd_centro_responsabilita, id_classificazione);
	}
	public java.lang.String getStato () {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	public java.math.BigDecimal getIm_tot_spese_acc () {
		return im_tot_spese_acc;
	}
	public void setIm_tot_spese_acc(java.math.BigDecimal im_tot_spese_acc)  {
		this.im_tot_spese_acc=im_tot_spese_acc;
	}
}