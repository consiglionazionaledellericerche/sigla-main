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
 * Date 25/03/2008
 */
package it.cnr.contab.pdg00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class V_stm_paramin_sit_sint_gaeKey extends OggettoBulk implements KeyedPersistent {
	
//  ID_REPORT DECIMAL(22,0) NOT NULL
	private java.math.BigDecimal id_report;

//  CDR VARCHAR(200)
	private java.lang.String cdr;
 
//    GAE VARCHAR(200)
	private java.lang.String gae;

	public V_stm_paramin_sit_sint_gaeKey() {
		super();
	}

	public java.lang.String getCdr() {
		return cdr;
	}

	public void setCdr(java.lang.String cdr) {
		this.cdr = cdr;
	}

	public java.lang.String getGae() {
		return gae;
	}

	public void setGae(java.lang.String gae) {
		this.gae = gae;
	}

	public java.math.BigDecimal getId_report() {
		return id_report;
	}

	public void setId_report(java.math.BigDecimal id_report) {
		this.id_report = id_report;
	}
	
	/*public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof V_stm_paramin_sit_sint_gaeKey)) return false;
		V_stm_paramin_sit_sint_gaeKey k = (V_stm_paramin_sit_sint_gaeKey) o;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		return i;
	}*/
}