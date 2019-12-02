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
 * Date 17/03/2009
 */
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Cnr_estrazione_coriKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_cds;
	private java.lang.Integer esercizio;
	private java.lang.String cd_unita_organizzativa;
	private java.lang.Integer pg_liquidazione;
	private java.lang.Integer matricola;
	private java.lang.String codice_fiscale;
	private java.lang.String ti_pagamento;
	private java.lang.Integer esercizio_compenso;
	private java.lang.String cd_imponibile;
	private java.lang.String ti_ente_percipiente;
	private java.lang.String cd_contributo_ritenuta;
	public Cnr_estrazione_coriKey() {
		super();
	}
	public Cnr_estrazione_coriKey(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.String cd_unita_organizzativa, java.lang.Integer pg_liquidazione, java.lang.Integer matricola, java.lang.String codice_fiscale, java.lang.String ti_pagamento, java.lang.Integer esercizio_compenso, java.lang.String cd_imponibile, java.lang.String ti_ente_percipiente, java.lang.String cd_contributo_ritenuta) {
		super();
		this.cd_cds=cd_cds;
		this.esercizio=esercizio;
		this.cd_unita_organizzativa=cd_unita_organizzativa;
		this.pg_liquidazione=pg_liquidazione;
		this.matricola=matricola;
		this.codice_fiscale=codice_fiscale;
		this.ti_pagamento=ti_pagamento;
		this.esercizio_compenso=esercizio_compenso;
		this.cd_imponibile=cd_imponibile;
		this.ti_ente_percipiente=ti_ente_percipiente;
		this.cd_contributo_ritenuta=cd_contributo_ritenuta;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Cnr_estrazione_coriKey)) return false;
		Cnr_estrazione_coriKey k = (Cnr_estrazione_coriKey) o;
		if (!compareKey(getCd_cds(), k.getCd_cds())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCd_unita_organizzativa(), k.getCd_unita_organizzativa())) return false;
		if (!compareKey(getPg_liquidazione(), k.getPg_liquidazione())) return false;
		if (!compareKey(getMatricola(), k.getMatricola())) return false;
		if (!compareKey(getCodice_fiscale(), k.getCodice_fiscale())) return false;
		if (!compareKey(getTi_pagamento(), k.getTi_pagamento())) return false;
		if (!compareKey(getEsercizio_compenso(), k.getEsercizio_compenso())) return false;
		if (!compareKey(getCd_imponibile(), k.getCd_imponibile())) return false;
		if (!compareKey(getTi_ente_percipiente(), k.getTi_ente_percipiente())) return false;
		if (!compareKey(getCd_contributo_ritenuta(), k.getCd_contributo_ritenuta())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_cds());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCd_unita_organizzativa());
		i = i + calculateKeyHashCode(getPg_liquidazione());
		i = i + calculateKeyHashCode(getMatricola());
		i = i + calculateKeyHashCode(getCodice_fiscale());
		i = i + calculateKeyHashCode(getTi_pagamento());
		i = i + calculateKeyHashCode(getEsercizio_compenso());
		i = i + calculateKeyHashCode(getCd_imponibile());
		i = i + calculateKeyHashCode(getTi_ente_percipiente());
		i = i + calculateKeyHashCode(getCd_contributo_ritenuta());
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
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}
	public void setPg_liquidazione(java.lang.Integer pg_liquidazione)  {
		this.pg_liquidazione=pg_liquidazione;
	}
	public java.lang.Integer getPg_liquidazione() {
		return pg_liquidazione;
	}
	public void setMatricola(java.lang.Integer matricola)  {
		this.matricola=matricola;
	}
	public java.lang.Integer getMatricola() {
		return matricola;
	}
	public void setCodice_fiscale(java.lang.String codice_fiscale)  {
		this.codice_fiscale=codice_fiscale;
	}
	public java.lang.String getCodice_fiscale() {
		return codice_fiscale;
	}
	public void setTi_pagamento(java.lang.String ti_pagamento)  {
		this.ti_pagamento=ti_pagamento;
	}
	public java.lang.String getTi_pagamento() {
		return ti_pagamento;
	}
	public void setEsercizio_compenso(java.lang.Integer esercizio_compenso)  {
		this.esercizio_compenso=esercizio_compenso;
	}
	public java.lang.Integer getEsercizio_compenso() {
		return esercizio_compenso;
	}
	public void setCd_imponibile(java.lang.String cd_imponibile)  {
		this.cd_imponibile=cd_imponibile;
	}
	public java.lang.String getCd_imponibile() {
		return cd_imponibile;
	}
	public void setTi_ente_percipiente(java.lang.String ti_ente_percipiente)  {
		this.ti_ente_percipiente=ti_ente_percipiente;
	}
	public java.lang.String getTi_ente_percipiente() {
		return ti_ente_percipiente;
	}
	public void setCd_contributo_ritenuta(java.lang.String cd_contributo_ritenuta)  {
		this.cd_contributo_ritenuta=cd_contributo_ritenuta;
	}
	public java.lang.String getCd_contributo_ritenuta() {
		return cd_contributo_ritenuta;
	}
}