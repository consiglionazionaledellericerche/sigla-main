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
 * Date 16/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ass_tipo_cori_voce_epBase extends Ass_tipo_cori_voce_epKey implements Keyed {
//    CD_VOCE_EP VARCHAR(45) NOT NULL
	private java.lang.String cd_voce_ep;
 
//    CD_VOCE_EP_CONTR VARCHAR(45) NOT NULL
	private java.lang.String cd_voce_ep_contr;

	//    CD_VOCE_EP_CONTR_COMP VARCHAR(45)
	private java.lang.String cd_voce_ep_contr_comp;

	public Ass_tipo_cori_voce_epBase() {
		super();
	}
	public Ass_tipo_cori_voce_epBase(java.lang.Integer esercizio, java.lang.String cd_contributo_ritenuta, java.lang.String ti_ente_percepiente, java.lang.String sezione) {
		super(esercizio, cd_contributo_ritenuta, ti_ente_percepiente, sezione);
	}
	public java.lang.String getCd_voce_ep() {
		return cd_voce_ep;
	}
	public void setCd_voce_ep(java.lang.String cd_voce_ep)  {
		this.cd_voce_ep=cd_voce_ep;
	}
	public java.lang.String getCd_voce_ep_contr() {
		return cd_voce_ep_contr;
	}
	public void setCd_voce_ep_contr(java.lang.String cd_voce_ep_contr)  {
		this.cd_voce_ep_contr=cd_voce_ep_contr;
	}
}