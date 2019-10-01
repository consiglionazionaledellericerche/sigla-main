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
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Ass_tipo_cori_voce_epKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String cd_contributo_ritenuta;
	private java.lang.String ti_ente_percepiente;
	private java.lang.String sezione;
	public Ass_tipo_cori_voce_epKey() {
		super();
	}
	public Ass_tipo_cori_voce_epKey(java.lang.Integer esercizio, java.lang.String cd_contributo_ritenuta, java.lang.String ti_ente_percepiente, java.lang.String sezione) {
		super();
		this.esercizio=esercizio;
		this.cd_contributo_ritenuta=cd_contributo_ritenuta;
		this.ti_ente_percepiente=ti_ente_percepiente;
		this.sezione=sezione;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Ass_tipo_cori_voce_epKey)) return false;
		Ass_tipo_cori_voce_epKey k = (Ass_tipo_cori_voce_epKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCd_contributo_ritenuta(), k.getCd_contributo_ritenuta())) return false;
		if (!compareKey(getTi_ente_percepiente(), k.getTi_ente_percepiente())) return false;
		if (!compareKey(getSezione(), k.getSezione())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCd_contributo_ritenuta());
		i = i + calculateKeyHashCode(getTi_ente_percepiente());
		i = i + calculateKeyHashCode(getSezione());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setCd_contributo_ritenuta(java.lang.String cd_contributo_ritenuta)  {
		this.cd_contributo_ritenuta=cd_contributo_ritenuta;
	}
	public java.lang.String getCd_contributo_ritenuta() {
		return cd_contributo_ritenuta;
	}
	public void setTi_ente_percepiente(java.lang.String ti_ente_percepiente)  {
		this.ti_ente_percepiente=ti_ente_percepiente;
	}
	public java.lang.String getTi_ente_percepiente() {
		return ti_ente_percepiente;
	}
	public void setSezione(java.lang.String sezione)  {
		this.sezione=sezione;
	}
	public java.lang.String getSezione() {
		return sezione;
	}
}