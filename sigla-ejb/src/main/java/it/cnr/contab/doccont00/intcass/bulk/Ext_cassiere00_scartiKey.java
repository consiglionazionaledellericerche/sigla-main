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
 * Date 18/01/2008
 */
package it.cnr.contab.doccont00.intcass.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Ext_cassiere00_scartiKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String nome_file;
	private java.lang.Long pg_esecuzione;
	private java.lang.Long pg_rec;
	public Ext_cassiere00_scartiKey() {
		super();
	}
	public Ext_cassiere00_scartiKey(java.lang.Integer esercizio, java.lang.String nome_file, java.lang.Long pg_esecuzione, java.lang.Long pg_rec) {
		super();
		this.esercizio=esercizio;
		this.nome_file=nome_file;
		this.pg_esecuzione=pg_esecuzione;
		this.pg_rec=pg_rec;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Ext_cassiere00_scartiKey)) return false;
		Ext_cassiere00_scartiKey k = (Ext_cassiere00_scartiKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getNome_file(), k.getNome_file())) return false;
		if (!compareKey(getPg_esecuzione(), k.getPg_esecuzione())) return false;
		if (!compareKey(getPg_rec(), k.getPg_rec())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getNome_file());
		i = i + calculateKeyHashCode(getPg_esecuzione());
		i = i + calculateKeyHashCode(getPg_rec());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setNome_file(java.lang.String nome_file)  {
		this.nome_file=nome_file;
	}
	public java.lang.String getNome_file() {
		return nome_file;
	}
	public void setPg_esecuzione(java.lang.Long pg_esecuzione)  {
		this.pg_esecuzione=pg_esecuzione;
	}
	public java.lang.Long getPg_esecuzione() {
		return pg_esecuzione;
	}
	public void setPg_rec(java.lang.Long pg_rec)  {
		this.pg_rec=pg_rec;
	}
	public java.lang.Long getPg_rec() {
		return pg_rec;
	}
}