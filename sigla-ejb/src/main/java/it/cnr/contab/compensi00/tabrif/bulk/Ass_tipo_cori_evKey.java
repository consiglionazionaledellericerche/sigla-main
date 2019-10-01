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
 * Date 11/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Ass_tipo_cori_evKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String ti_appartenenza;
	private java.lang.String ti_gestione;
	private java.lang.String ti_ente_percepiente;
	private java.lang.String cd_contributo_ritenuta;
	public Ass_tipo_cori_evKey() {
		super();
	}
	public Ass_tipo_cori_evKey(java.lang.Integer esercizio, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String ti_ente_percepiente, java.lang.String cd_contributo_ritenuta) {
		super();
		this.esercizio=esercizio;
		this.ti_appartenenza=ti_appartenenza;
		this.ti_gestione=ti_gestione;
		this.ti_ente_percepiente=ti_ente_percepiente;
		this.cd_contributo_ritenuta=cd_contributo_ritenuta;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Ass_tipo_cori_evKey)) return false;
		Ass_tipo_cori_evKey k = (Ass_tipo_cori_evKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getTi_appartenenza(), k.getTi_appartenenza())) return false;
		if (!compareKey(getTi_gestione(), k.getTi_gestione())) return false;
		if (!compareKey(getTi_ente_percepiente(), k.getTi_ente_percepiente())) return false;
		if (!compareKey(getCd_contributo_ritenuta(), k.getCd_contributo_ritenuta())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getTi_appartenenza());
		i = i + calculateKeyHashCode(getTi_gestione());
		i = i + calculateKeyHashCode(getTi_ente_percepiente());
		i = i + calculateKeyHashCode(getCd_contributo_ritenuta());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setTi_appartenenza(java.lang.String ti_appartenenza)  {
		this.ti_appartenenza=ti_appartenenza;
	}
	public java.lang.String getTi_appartenenza() {
		return ti_appartenenza;
	}
	public void setTi_gestione(java.lang.String ti_gestione)  {
		this.ti_gestione=ti_gestione;
	}
	public java.lang.String getTi_gestione() {
		return ti_gestione;
	}
	public void setTi_ente_percepiente(java.lang.String ti_ente_percepiente)  {
		this.ti_ente_percepiente=ti_ente_percepiente;
	}
	public java.lang.String getTi_ente_percepiente() {
		return ti_ente_percepiente;
	}
	public void setCd_contributo_ritenuta(java.lang.String cd_contributo_ritenuta)  {
		this.cd_contributo_ritenuta=cd_contributo_ritenuta;
	}
	public java.lang.String getCd_contributo_ritenuta() {
		return cd_contributo_ritenuta;
	}
}