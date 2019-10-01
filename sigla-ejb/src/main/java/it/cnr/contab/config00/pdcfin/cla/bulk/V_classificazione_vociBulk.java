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
* Date 29/08/2005
*/
package it.cnr.contab.config00.pdcfin.cla.bulk;

import it.cnr.contab.prevent00.bulk.Pdg_piano_ripartoBulk;
import it.cnr.jada.bulk.BulkList;

public class V_classificazione_vociBulk extends Classificazione_vociBulk {
  	private java.lang.String cd_livello_last;
  	private java.lang.String cd_classificazione;
	private java.lang.Integer nr_livello;
	
	public V_classificazione_vociBulk() {
		super();
	}
	
	public java.lang.String getCd_livello_last() {
		return cd_livello_last;
	}
	
	public void setCd_livello_last(java.lang.String cd_livello_last) {
		this.cd_livello_last = cd_livello_last;
	}
	
	public V_classificazione_vociBulk(java.lang.Integer id_classificazione) {
		super(id_classificazione);
	}
	
	public java.lang.String getCd_classificazione() {
		return cd_classificazione;
	}

	public void setCd_classificazione(java.lang.String string) {
		cd_classificazione = string;
	}

	public java.lang.Integer getNr_livello() {
		return nr_livello;
	}

	public void setNr_livello(java.lang.Integer integer) {
		nr_livello = integer;
	}
}