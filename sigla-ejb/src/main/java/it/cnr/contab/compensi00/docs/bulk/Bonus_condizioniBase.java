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
public class Bonus_condizioniBase extends Bonus_condizioniKey implements Keyed {
//    DS_CONDIZIONE VARCHAR(100) NOT NULL
	private java.lang.String ds_condizione;
 
//    NUMERO_COMPONENTI DECIMAL(3,0)
	private java.lang.Integer numero_componenti;
 
//    IM_REDDITO_LIMITE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_reddito_limite;
 
//    IM_BONUS DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_bonus;
 
	public Bonus_condizioniBase() {
		super();
	}
	public Bonus_condizioniBase(java.lang.Integer esercizio, java.lang.String cd_condizione) {
		super(esercizio, cd_condizione);
	}
	public java.lang.String getDs_condizione() {
		return ds_condizione;
	}
	public void setDs_condizione(java.lang.String ds_condizione)  {
		this.ds_condizione=ds_condizione;
	}
	public java.lang.Integer getNumero_componenti() {
		return numero_componenti;
	}
	public void setNumero_componenti(java.lang.Integer numero_componenti)  {
		this.numero_componenti=numero_componenti;
	}
	public java.math.BigDecimal getIm_reddito_limite() {
		return im_reddito_limite;
	}
	public void setIm_reddito_limite(java.math.BigDecimal im_reddito_limite)  {
		this.im_reddito_limite=im_reddito_limite;
	}
	public java.math.BigDecimal getIm_bonus() {
		return im_bonus;
	}
	public void setIm_bonus(java.math.BigDecimal im_bonus)  {
		this.im_bonus=im_bonus;
	}
}