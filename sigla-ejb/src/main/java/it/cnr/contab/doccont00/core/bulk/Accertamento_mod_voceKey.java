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
 * Date 27/09/2006
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Accertamento_mod_voceKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_cds;
	private java.lang.Integer esercizio;
	private java.lang.Long pg_modifica;
	private java.lang.String ti_appartenenza;
	private java.lang.String ti_gestione;
	private java.lang.String cd_voce;
	private java.lang.String cd_centro_responsabilita;
	private java.lang.String cd_linea_attivita;
	public Accertamento_mod_voceKey() {
		super();
	}
	public Accertamento_mod_voceKey(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_modifica, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_voce, java.lang.String cd_centro_responsabilita, java.lang.String cd_linea_attivita) {
		super();
		this.cd_cds=cd_cds;
		this.esercizio=esercizio;
		this.pg_modifica=pg_modifica;
		this.ti_appartenenza=ti_appartenenza;
		this.ti_gestione=ti_gestione;
		this.cd_voce=cd_voce;
		this.cd_centro_responsabilita=cd_centro_responsabilita;
		this.cd_linea_attivita=cd_linea_attivita;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Accertamento_mod_voceKey)) return false;
		Accertamento_mod_voceKey k = (Accertamento_mod_voceKey) o;
		if (!compareKey(getCd_cds(), k.getCd_cds())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPg_modifica(), k.getPg_modifica())) return false;
		if (!compareKey(getTi_appartenenza(), k.getTi_appartenenza())) return false;
		if (!compareKey(getTi_gestione(), k.getTi_gestione())) return false;
		if (!compareKey(getCd_voce(), k.getCd_voce())) return false;
		if (!compareKey(getCd_centro_responsabilita(), k.getCd_centro_responsabilita())) return false;
		if (!compareKey(getCd_linea_attivita(), k.getCd_linea_attivita())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_cds());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPg_modifica());
		i = i + calculateKeyHashCode(getTi_appartenenza());
		i = i + calculateKeyHashCode(getTi_gestione());
		i = i + calculateKeyHashCode(getCd_voce());
		i = i + calculateKeyHashCode(getCd_centro_responsabilita());
		i = i + calculateKeyHashCode(getCd_linea_attivita());
		return i;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setPg_modifica(java.lang.Long pg_modifica)  {
		this.pg_modifica=pg_modifica;
	}
	public java.lang.Long getPg_modifica() {
		return pg_modifica;
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
	public void setCd_voce(java.lang.String cd_voce)  {
		this.cd_voce=cd_voce;
	}
	public java.lang.String getCd_voce() {
		return cd_voce;
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		this.cd_centro_responsabilita=cd_centro_responsabilita;
	}
	public java.lang.String getCd_centro_responsabilita() {
		return cd_centro_responsabilita;
	}
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita)  {
		this.cd_linea_attivita=cd_linea_attivita;
	}
	public java.lang.String getCd_linea_attivita() {
		return cd_linea_attivita;
	}
}