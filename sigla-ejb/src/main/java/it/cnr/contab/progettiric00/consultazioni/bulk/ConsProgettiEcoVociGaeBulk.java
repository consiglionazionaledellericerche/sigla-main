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

package it.cnr.contab.progettiric00.consultazioni.bulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

import java.math.BigDecimal;

public class ConsProgettiEcoVociGaeBulk extends OggettoBulk implements Persistent {
	public ConsProgettiEcoVociGaeBulk() {
		super();
	}
	//	ESERCIZIO DECIMAL(4,0)
	private Integer esercizio_piano;

	private String  cd_centro_responsabilita;

	protected it.cnr.contab.progettiric00.core.bulk.ProgettoBulk findProgetto;

	private String  ds_cdr;

	private Integer pg_progetto;

	private String  cd_progetto;
		
	private String  ds_progetto;

	private java.math.BigDecimal assestato_fin;
		
	private java.math.BigDecimal assestato_cofin;
	
	private java.math.BigDecimal impacc_fin;
	
	private java.math.BigDecimal impacc_cofin;

	public String getCd_centro_responsabilita() {
		return cd_centro_responsabilita;
	}

	public void setCd_centro_responsabilita(String cd_centro_responsabilita) {
		this.cd_centro_responsabilita = cd_centro_responsabilita;
	}

	public Integer getEsercizio_piano() {
		return esercizio_piano;
	}

	public void setEsercizio_piano(Integer esercizio_piano) {
		this.esercizio_piano = esercizio_piano;
	}

	public String getDs_cdr() {
		return ds_cdr;
	}

	public void setDs_cdr(String ds_cdr) {
		this.ds_cdr = ds_cdr;
	}

	public Integer getPg_progetto() {
		return pg_progetto;
	}

	public void setPg_progetto(Integer pg_progetto) {
		this.pg_progetto = pg_progetto;
	}

	public String getCd_progetto() {
		return cd_progetto;
	}

	public void setCd_progetto(String cd_progetto) {
		this.cd_progetto = cd_progetto;
	}

	public String getDs_progetto() {
		return ds_progetto;
	}

	public void setDs_progetto(String ds_progetto) {
		this.ds_progetto = ds_progetto;
	}

	public BigDecimal getAssestato_fin() {
		return assestato_fin;
	}

	public void setAssestato_fin(BigDecimal assestato_fin) {
		this.assestato_fin = assestato_fin;
	}

	public BigDecimal getAssestato_cofin() {
		return assestato_cofin;
	}

	public void setAssestato_cofin(BigDecimal assestato_cofin) {
		this.assestato_cofin = assestato_cofin;
	}

	public BigDecimal getImpacc_fin() {
		return impacc_fin;
	}

	public void setImpacc_fin(BigDecimal impacc_fin) {
		this.impacc_fin = impacc_fin;
	}

	public BigDecimal getImpacc_cofin() {
		return impacc_cofin;
	}

	public void setImpacc_cofin(BigDecimal impacc_cofin) {
		this.impacc_cofin = impacc_cofin;
	}

	public ProgettoBulk getFindProgetto() {
		return findProgetto;
	}

	public void setProgettoForPrint(ProgettoBulk findProgetto) {
		this.findProgetto = findProgetto;
		if (getFindProgetto() != null){
			setPg_progetto(getFindProgetto().getPg_progetto());
		}
	}
}