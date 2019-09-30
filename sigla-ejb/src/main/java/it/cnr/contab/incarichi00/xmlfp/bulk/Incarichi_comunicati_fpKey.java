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
 * Date 26/07/2007
 */
package it.cnr.contab.incarichi00.xmlfp.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Incarichi_comunicati_fpKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio_repertorio;
	private java.lang.Long pg_repertorio;
	private java.lang.String tipo_record;
	private java.lang.Long pg_record;	

	public Incarichi_comunicati_fpKey() {
		super();
	}
	public Incarichi_comunicati_fpKey(java.lang.Integer esercizio_repertorio, java.lang.Long pg_repertorio, java.lang.String tipo_record, java.lang.Long pg_record) {
		super();
		this.esercizio_repertorio=esercizio_repertorio;
		this.pg_repertorio=pg_repertorio;
		this.tipo_record=tipo_record;
		this.pg_record=pg_record;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Incarichi_comunicati_fpKey)) return false;
		Incarichi_comunicati_fpKey k = (Incarichi_comunicati_fpKey) o;
		if (!compareKey(getEsercizio_repertorio(), k.getEsercizio_repertorio())) return false;
		if (!compareKey(getPg_repertorio(), k.getPg_repertorio())) return false;
		if (!compareKey(getTipo_record(), k.getTipo_record())) return false;
		if (!compareKey(getPg_record(), k.getPg_record())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio_repertorio());
		i = i + calculateKeyHashCode(getPg_repertorio());
		i = i + calculateKeyHashCode(getTipo_record());
		i = i + calculateKeyHashCode(getPg_record());
		return i;
	}
	public void setEsercizio_repertorio(java.lang.Integer esercizio_repertorio)  {
		this.esercizio_repertorio=esercizio_repertorio;
	}
	public java.lang.Integer getEsercizio_repertorio() {
		return esercizio_repertorio;
	}
	public void setPg_repertorio(java.lang.Long pg_repertorio)  {
		this.pg_repertorio=pg_repertorio;
	}
	public java.lang.Long getPg_repertorio() {
		return pg_repertorio;
	}
	public java.lang.String getTipo_record() {
		return tipo_record;
	}
	public void setTipo_record(java.lang.String tipoRecord) {
		tipo_record = tipoRecord;
	}
	public java.lang.Long getPg_record() {
		return pg_record;
	}
	public void setPg_record(java.lang.Long pgRecord) {
		pg_record = pgRecord;
	}
}