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
* Date 30/08/2005
*/
package it.cnr.contab.docamm00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Monito_cococoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer id_report;
	private java.lang.String chiave;
	private java.lang.Integer sequenza;
	private java.lang.String tipo;
	
	public Monito_cococoKey() {
		super();
	}
	public Monito_cococoKey(java.lang.Integer id_report,java.lang.String chiave,java.lang.String tipo,java.lang.Integer sequenza) {
		super();
		this.id_report = id_report;
		this.chiave = chiave;
		this.sequenza = sequenza;
		this.tipo = tipo;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Monito_cococoKey)) return false;
		Monito_cococoKey k = (Monito_cococoKey) o;
		if(!compareKey(getId_report(),k.getId_report())) return false;
		if(!compareKey(getChiave(),k.getChiave())) return false;
		if(!compareKey(getTipo(),k.getTipo())) return false;
		if(!compareKey(getSequenza(),k.getSequenza())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getId_report())+
			calculateKeyHashCode(getChiave())+
			calculateKeyHashCode(getTipo())+
			calculateKeyHashCode(getSequenza());
	}
	/**
	 * @return
	 */
	public java.lang.String getChiave() {
		return chiave;
	}

	/**
	 * @return
	 */
	public java.lang.Integer getId_report() {
		return id_report;
	}

	/**
	 * @return
	 */
	public java.lang.Integer getSequenza() {
		return sequenza;
	}

	/**
	 * @return
	 */
	public java.lang.String getTipo() {
		return tipo;
	}

	/**
	 * @param string
	 */
	public void setChiave(java.lang.String string) {
		chiave = string;
	}

	/**
	 * @param integer
	 */
	public void setId_report(java.lang.Integer integer) {
		id_report = integer;
	}

	/**
	 * @param integer
	 */
	public void setSequenza(java.lang.Integer integer) {
		sequenza = integer;
	}

	/**
	 * @param string
	 */
	public void setTipo(java.lang.String string) {
		tipo = string;
	}

}