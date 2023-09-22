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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/09/2016
 */
package it.cnr.contab.docamm00.docs.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class VDocammElettroniciAttiviKey extends OggettoBulk implements KeyedPersistent {
	private String tipoDocamm;
    private String cd_cds;

    private String cd_unita_organizzativa;

    private Integer esercizio;

    private Long pg_docamm;

	public VDocammElettroniciAttiviKey() {
		super();
	}

	public VDocammElettroniciAttiviKey(String tipoDocamm, String cd_cds, String cd_unita_organizzativa, Integer esercizio, Long pg_docamm) {
		this.tipoDocamm = tipoDocamm;
		this.cd_cds = cd_cds;
		this.cd_unita_organizzativa = cd_unita_organizzativa;
		this.esercizio = esercizio;
		this.pg_docamm = pg_docamm;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof VDocammElettroniciAttiviKey)) return false;
		VDocammElettroniciAttiviKey k = (VDocammElettroniciAttiviKey)o;
		if(!compareKey(getTipoDocamm(),k.getTipoDocamm())) return false;
		if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
		if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		if(!compareKey(getPg_docamm(),k.getPg_docamm())) return false;
		return true;
	}

	public String getTipoDocamm() {
		return tipoDocamm;
	}

	public void setTipoDocamm(String tipoDocamm) {
		this.tipoDocamm = tipoDocamm;
	}

	public String getCd_cds() {
		return cd_cds;
	}

	public void setCd_cds(String cd_cds) {
		this.cd_cds = cd_cds;
	}

	public String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}

	public void setCd_unita_organizzativa(String cd_unita_organizzativa) {
		this.cd_unita_organizzativa = cd_unita_organizzativa;
	}

	public Integer getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(Integer esercizio) {
		this.esercizio = esercizio;
	}

	public Long getPg_docamm() {
	return pg_docamm;
}

	public void setPg_docamm(Long pg_docamm) {
		this.pg_docamm = pg_docamm;
	}

	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getTipoDocamm())+
			calculateKeyHashCode(getCd_cds())+
			calculateKeyHashCode(getCd_unita_organizzativa())+
			calculateKeyHashCode(getEsercizio())+
			calculateKeyHashCode(getPg_docamm());
	}
}