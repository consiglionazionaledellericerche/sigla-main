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
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Ass_incarico_attivitaKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String cd_tipo_incarico;
	private java.lang.String cd_tipo_attivita;
	private java.lang.String tipo_natura;
	public Ass_incarico_attivitaKey() {
		super();
	}
	public Ass_incarico_attivitaKey(java.lang.Integer esercizio, java.lang.String cd_tipo_incarico, java.lang.String cd_tipo_attivita, java.lang.String tipo_natura) {
		super();
		this.esercizio=esercizio;
		this.cd_tipo_incarico=cd_tipo_incarico;
		this.cd_tipo_attivita=cd_tipo_attivita;
		this.tipo_natura=tipo_natura;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Ass_incarico_attivitaKey)) return false;
		Ass_incarico_attivitaKey k = (Ass_incarico_attivitaKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCd_tipo_incarico(), k.getCd_tipo_incarico())) return false;
		if (!compareKey(getCd_tipo_attivita(), k.getCd_tipo_attivita())) return false;
		if (!compareKey(getTipo_natura(), k.getTipo_natura())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCd_tipo_incarico());
		i = i + calculateKeyHashCode(getCd_tipo_attivita());
		i = i + calculateKeyHashCode(getTipo_natura());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setCd_tipo_incarico(java.lang.String cd_tipo_incarico)  {
		this.cd_tipo_incarico=cd_tipo_incarico;
	}
	public java.lang.String getCd_tipo_incarico() {
		return cd_tipo_incarico;
	}
	public void setCd_tipo_attivita(java.lang.String cd_tipo_attivita)  {
		this.cd_tipo_attivita=cd_tipo_attivita;
	}
	public java.lang.String getCd_tipo_attivita() {
		return cd_tipo_attivita;
	}
	public void setTipo_natura(java.lang.String tipo_natura)  {
		this.tipo_natura=tipo_natura;
	}
	public java.lang.String getTipo_natura() {
		return tipo_natura;
	}
}