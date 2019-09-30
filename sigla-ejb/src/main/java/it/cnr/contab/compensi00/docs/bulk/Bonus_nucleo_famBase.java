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
 * Date 19/02/2009
 */
package it.cnr.contab.compensi00.docs.bulk;
import it.cnr.jada.persistency.Keyed;
public class Bonus_nucleo_famBase extends Bonus_nucleo_famKey implements Keyed {
//    TIPO_COMPONENTE_NUCLEO CHAR(1) NOT NULL
	private java.lang.String tipo_componente_nucleo;
 
//    IM_REDDITO_COMPONENTE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_reddito_componente;
 
	private Boolean fl_handicap;
	public Bonus_nucleo_famBase() {
		super();
	}
	public Bonus_nucleo_famBase(java.lang.Integer esercizio, java.lang.Long pg_bonus, java.lang.String cf_componente_nucleo) {
		super(esercizio, pg_bonus, cf_componente_nucleo);
	}
	public java.lang.String getTipo_componente_nucleo() {
		return tipo_componente_nucleo;
	}
	public void setTipo_componente_nucleo(java.lang.String tipo_componente_nucleo)  {
		this.tipo_componente_nucleo=tipo_componente_nucleo;
	}
	public java.math.BigDecimal getIm_reddito_componente() {
		return im_reddito_componente;
	}
	public void setIm_reddito_componente(java.math.BigDecimal im_reddito_componente)  {
		this.im_reddito_componente=im_reddito_componente;
	}
	public Boolean getFl_handicap() {
		return fl_handicap;
	}
	public void setFl_handicap(Boolean fl_handicap) {
		this.fl_handicap = fl_handicap;
	}
}