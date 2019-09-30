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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 28/09/2006
 */
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.persistency.Keyed;
public class Stipendi_cofi_obb_scad_dettBase extends Stipendi_cofi_obb_scad_dettKey implements Keyed {
//    IM_TOTALE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_totale;
 
	public Stipendi_cofi_obb_scad_dettBase() {
		super();
	}
	public Stipendi_cofi_obb_scad_dettBase(java.lang.Integer esercizio, java.lang.Integer mese, java.lang.String tipo_flusso, java.lang.String cd_cds_obbligazione, java.lang.Integer esercizio_obbligazione, java.lang.Long pg_obbligazione, java.lang.Integer esercizio_ori_obbligazione) {
		super(esercizio, mese, tipo_flusso, cd_cds_obbligazione, esercizio_obbligazione, pg_obbligazione, esercizio_ori_obbligazione);
	}
	public java.math.BigDecimal getIm_totale() {
		return im_totale;
	}
	public void setIm_totale(java.math.BigDecimal im_totale)  {
		this.im_totale=im_totale;
	}
}