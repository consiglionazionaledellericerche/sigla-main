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
import com.fasterxml.jackson.annotation.JsonIgnore;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Classificazione_vociKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer id_classificazione;
	public Classificazione_vociKey() {
		super();
	}
	public Classificazione_vociKey(java.lang.Integer id_classificazione) {
		super();
		this.id_classificazione=id_classificazione;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Classificazione_vociKey)) return false;
		Classificazione_vociKey k = (Classificazione_vociKey) o;
		if (!compareKey(getId_classificazione(), k.getId_classificazione())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getId_classificazione());
		return i;
	}
	public void setId_classificazione(java.lang.Integer id_classificazione)  {
		this.id_classificazione=id_classificazione;
	}
	@JsonIgnore
	public void setId_classificazione(java.math.BigDecimal id_classificazione) {
		this.id_classificazione = new java.lang.Integer(id_classificazione.intValue());
	}
	public java.lang.Integer getId_classificazione () {
		return id_classificazione;
	}
}