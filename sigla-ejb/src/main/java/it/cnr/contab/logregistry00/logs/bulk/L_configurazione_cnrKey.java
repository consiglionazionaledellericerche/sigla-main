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
* Date 22/09/2005
*/
package it.cnr.contab.logregistry00.logs.bulk;
import it.cnr.contab.logregistry00.core.bulk.OggettoLogBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class L_configurazione_cnrKey extends OggettoLogBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String cd_unita_funzionale;
	private java.lang.String cd_chiave_primaria;
	private java.lang.String cd_chiave_secondaria;
	public L_configurazione_cnrKey() {
		super();
	}
	public L_configurazione_cnrKey(java.math.BigDecimal pg_storico_, java.lang.Integer esercizio, java.lang.String cd_unita_funzionale, java.lang.String cd_chiave_primaria, java.lang.String cd_chiave_secondaria) {
		super();
		this.pg_storico_=pg_storico_;
		this.esercizio=esercizio;
		this.cd_unita_funzionale=cd_unita_funzionale;
		this.cd_chiave_primaria=cd_chiave_primaria;
		this.cd_chiave_secondaria=cd_chiave_secondaria;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof L_configurazione_cnrKey)) return false;
		L_configurazione_cnrKey k = (L_configurazione_cnrKey) o;
		if (!compareKey(getPg_storico_(), k.getPg_storico_())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCd_unita_funzionale(), k.getCd_unita_funzionale())) return false;
		if (!compareKey(getCd_chiave_primaria(), k.getCd_chiave_primaria())) return false;
		if (!compareKey(getCd_chiave_secondaria(), k.getCd_chiave_secondaria())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getPg_storico_());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCd_unita_funzionale());
		i = i + calculateKeyHashCode(getCd_chiave_primaria());
		i = i + calculateKeyHashCode(getCd_chiave_secondaria());
		return i;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setCd_unita_funzionale(java.lang.String cd_unita_funzionale)  {
		this.cd_unita_funzionale=cd_unita_funzionale;
	}
	public java.lang.String getCd_unita_funzionale () {
		return cd_unita_funzionale;
	}
	public void setCd_chiave_primaria(java.lang.String cd_chiave_primaria)  {
		this.cd_chiave_primaria=cd_chiave_primaria;
	}
	public java.lang.String getCd_chiave_primaria () {
		return cd_chiave_primaria;
	}
	public void setCd_chiave_secondaria(java.lang.String cd_chiave_secondaria)  {
		this.cd_chiave_secondaria=cd_chiave_secondaria;
	}
	public java.lang.String getCd_chiave_secondaria () {
		return cd_chiave_secondaria;
	}
}