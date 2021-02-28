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

package it.cnr.contab.cori00.views.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

import java.util.Objects;

public class VConsLiqCoriKey extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private Integer esercizio_compenso;

	// PG_COMPENSO DECIMAL(10,0) NOT NULL (PK)
	private Long pg_compenso;

	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private String uo_compenso;

	// PG_LIQUIDAZIONE DECIMAL(8,0) NOT NULL (PK)
	private Integer pg_liquidazione;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private Integer esercizio_liquidazione;

	// CD_UO_ORIGINE VARCHAR(30) NOT NULL (PK)
	private String cd_uo_liquidazione;

	// PG_COMUNE DECIMAL(10,0) NOT NULL (PK)
	private Long pg_comune;

	// CD_CONTRIBUTO_RITENUTA VARCHAR(10) NOT NULL (PK)
	private String cd_contributo_ritenuta;

	// CD_REGIONE VARCHAR(10) NOT NULL (PK)
	private String cd_regione;

	// TI_ENTE_PERCIPIENTE CHAR(1) NOT NULL (PK)
	private String ti_ente_percipiente;

	// CD_GRUPPO_CR VARCHAR(10) NOT NULL (PK)
	private String cd_gruppo_cr;

public VConsLiqCoriKey() {
	super();
}
public VConsLiqCoriKey(String uo_compenso, String cd_contributo_ritenuta, String cd_gruppo_cr, String cd_regione, String cd_uo_liquidazione, Integer esercizio_compenso, Integer esercizio_liquidazione, Long pg_compenso, Long pg_comune, Integer pg_liquidazione, String ti_ente_percipiente) {
	super();
	this.uo_compenso = uo_compenso;
	this.cd_contributo_ritenuta = cd_contributo_ritenuta;
	this.cd_gruppo_cr = cd_gruppo_cr;
	this.cd_regione = cd_regione;
	this.cd_uo_liquidazione = cd_uo_liquidazione;
	this.esercizio_liquidazione = esercizio_liquidazione;
	this.esercizio_compenso = esercizio_compenso;
	this.pg_compenso = pg_compenso;
	this.pg_comune = pg_comune;
	this.pg_liquidazione = pg_liquidazione;
	this.ti_ente_percipiente = ti_ente_percipiente;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof VConsLiqCoriKey)) return false;
	VConsLiqCoriKey k = (VConsLiqCoriKey)o;
	if(!compareKey(getUo_compenso(),k.getUo_compenso())) return false;
	if(!compareKey(getEsercizio_compenso(),k.getEsercizio_compenso())) return false;
	if(!compareKey(getCd_contributo_ritenuta(),k.getCd_contributo_ritenuta())) return false;
	if(!compareKey(getCd_gruppo_cr(),k.getCd_gruppo_cr())) return false;
	if(!compareKey(getCd_regione(),k.getCd_regione())) return false;
	if(!compareKey(getCd_uo_liquidazione(),k.getCd_uo_liquidazione())) return false;
	if(!compareKey(getEsercizio_liquidazione(),k.getEsercizio_liquidazione())) return false;
	if(!compareKey(getPg_compenso(),k.getPg_compenso())) return false;
	if(!compareKey(getPg_comune(),k.getPg_comune())) return false;
	if(!compareKey(getPg_liquidazione(),k.getPg_liquidazione())) return false;
	if(!compareKey(getTi_ente_percipiente(),k.getTi_ente_percipiente())) return false;
	return true;
}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		VConsLiqCoriKey that = (VConsLiqCoriKey) o;
		return Objects.equals(esercizio_compenso, that.esercizio_compenso) && Objects.equals(pg_compenso, that.pg_compenso) && Objects.equals(uo_compenso, that.uo_compenso) && Objects.equals(pg_liquidazione, that.pg_liquidazione) && Objects.equals(esercizio_liquidazione, that.esercizio_liquidazione) && Objects.equals(cd_uo_liquidazione, that.cd_uo_liquidazione) && Objects.equals(pg_comune, that.pg_comune) && Objects.equals(cd_contributo_ritenuta, that.cd_contributo_ritenuta) && Objects.equals(cd_regione, that.cd_regione) && Objects.equals(ti_ente_percipiente, that.ti_ente_percipiente) && Objects.equals(cd_gruppo_cr, that.cd_gruppo_cr);
	}

	@Override
	public int hashCode() {
		return Objects.hash(esercizio_compenso, pg_compenso, uo_compenso, pg_liquidazione, esercizio_liquidazione, cd_uo_liquidazione, pg_comune, cd_contributo_ritenuta, cd_regione, ti_ente_percipiente, cd_gruppo_cr);
	}

	public Integer getEsercizio_compenso() {
		return esercizio_compenso;
	}

	public void setEsercizio_compenso(Integer esercizio_compenso) {
		this.esercizio_compenso = esercizio_compenso;
	}

	public Long getPg_compenso() {
		return pg_compenso;
	}

	public void setPg_compenso(Long pg_compenso) {
		this.pg_compenso = pg_compenso;
	}

	public String getUo_compenso() {
		return uo_compenso;
	}

	public void setUo_compenso(String uo_compenso) {
		this.uo_compenso = uo_compenso;
	}

	public Integer getPg_liquidazione() {
		return pg_liquidazione;
	}

	public void setPg_liquidazione(Integer pg_liquidazione) {
		this.pg_liquidazione = pg_liquidazione;
	}

	public Integer getEsercizio_liquidazione() {
		return esercizio_liquidazione;
	}

	public void setEsercizio_liquidazione(Integer esercizio_liquidazione) {
		this.esercizio_liquidazione = esercizio_liquidazione;
	}

	public String getCd_uo_liquidazione() {
		return cd_uo_liquidazione;
	}

	public void setCd_uo_liquidazione(String cd_uo_liquidazione) {
		this.cd_uo_liquidazione = cd_uo_liquidazione;
	}

	public Long getPg_comune() {
		return pg_comune;
	}

	public void setPg_comune(Long pg_comune) {
		this.pg_comune = pg_comune;
	}

	public String getCd_contributo_ritenuta() {
		return cd_contributo_ritenuta;
	}

	public void setCd_contributo_ritenuta(String cd_contributo_ritenuta) {
		this.cd_contributo_ritenuta = cd_contributo_ritenuta;
	}

	public String getCd_regione() {
		return cd_regione;
	}

	public void setCd_regione(String cd_regione) {
		this.cd_regione = cd_regione;
	}

	public String getTi_ente_percipiente() {
		return ti_ente_percipiente;
	}

	public void setTi_ente_percipiente(String ti_ente_percipiente) {
		this.ti_ente_percipiente = ti_ente_percipiente;
	}

	public String getCd_gruppo_cr() {
		return cd_gruppo_cr;
	}

	public void setCd_gruppo_cr(String cd_gruppo_cr) {
		this.cd_gruppo_cr = cd_gruppo_cr;
	}
}
