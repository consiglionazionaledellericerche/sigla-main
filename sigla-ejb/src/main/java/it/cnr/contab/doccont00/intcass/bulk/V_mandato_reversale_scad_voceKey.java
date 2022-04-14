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

package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;

public class V_mandato_reversale_scad_voceKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30)
	private String cd_cds;
	private String cd_linea_attivita;
	private String cd_centro_responsabilita;
	private String cd_voce;

	// CD_TIPO_DOCUMENTO_CONT VARCHAR(10)
	private String ti_documento;

	// ESERCIZIO DECIMAL(4,0)
	private Integer esercizio;
	private Integer esercizio_originale;

	// PG_DOCUMENTO_CONT DECIMAL(10,0)
	private Long pg_documento;

	public V_mandato_reversale_scad_voceKey() {
		super();
	}
	public V_mandato_reversale_scad_voceKey(Integer esercizio, Integer esercizio_originale, String ti_documento, String cd_cds, String cd_voce, String cd_centro_responsabilita, String cd_linea_attivita, Long pg_documento) {
		this.esercizio_originale = esercizio_originale;
		this.esercizio = esercizio;
		this.ti_documento = ti_documento;
		this.cd_cds = cd_cds ;
		this.cd_voce = cd_voce ;
		this.cd_centro_responsabilita = cd_centro_responsabilita ;
		this.cd_linea_attivita = cd_linea_attivita ;
		this.pg_documento = pg_documento;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (29/04/2002 15.51.30)
	 * @return java.lang.String
	 */
	public String getCd_cds() {
		return cd_cds;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (29/04/2002 15.51.30)
	 * @return java.lang.String
	 */
	/**
	 * Insert the method's description here.
	 * Creation date: (29/04/2002 15.51.30)
	 * @return java.lang.Integer
	 */
	public Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (29/04/2002 15.51.30)
	 * @return java.lang.Long
	 */
	/**
	 * Insert the method's description here.
	 * Creation date: (29/04/2002 15.51.30)
	 * @param newCd_cds java.lang.String
	 */
	public void setCd_cds(String newCd_cds) {
		cd_cds = newCd_cds;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (29/04/2002 15.51.30)
	 * @param newCd_tipo_documento_cont java.lang.String
	 */
	/**
	 * Insert the method's description here.
	 * Creation date: (29/04/2002 15.51.30)
	 * @param newEsercizio java.lang.Integer
	 */
	public void setEsercizio(Integer newEsercizio) {
		esercizio = newEsercizio;
	}

	public String getCd_linea_attivita() {
		return cd_linea_attivita;
	}

	public void setCd_linea_attivita(String cd_linea_attivita) {
		this.cd_linea_attivita = cd_linea_attivita;
	}

	public String getCd_centro_responsabilita() {
		return cd_centro_responsabilita;
	}

	public void setCd_centro_responsabilita(String cd_centro_responsabilita) {
		this.cd_centro_responsabilita = cd_centro_responsabilita;
	}

	public String getCd_voce() {
		return cd_voce;
	}

	public void setCd_voce(String cd_voce) {
		this.cd_voce = cd_voce;
	}

	public String getTi_documento() {
		return ti_documento;
	}

	public void setTi_documento(String ti_documento) {
		this.ti_documento = ti_documento;
	}

	public Integer getEsercizio_originale() {
		return esercizio_originale;
	}

	public void setEsercizio_originale(Integer esercizio_originale) {
		this.esercizio_originale = esercizio_originale;
	}

	public Long getPg_documento() {
		return pg_documento;
	}

	public void setPg_documento(Long pg_documento) {
		this.pg_documento = pg_documento;
	}



	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof V_mandato_reversale_scad_voceKey)) return false;
		V_mandato_reversale_scad_voceKey k = (V_mandato_reversale_scad_voceKey)o;
		if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		if(!compareKey(getTi_documento(),k.getTi_documento())) return false;
		if(!compareKey(getPg_documento(),k.getPg_documento())) return false;
		if(!compareKey(getEsercizio_originale(),k.getEsercizio_originale())) return false;
		if(!compareKey(getCd_voce(),k.getCd_voce())) return false;
		if(!compareKey(getCd_linea_attivita(),k.getCd_linea_attivita())) return false;
		if(!compareKey(getCd_centro_responsabilita(),k.getCd_centro_responsabilita())) return false;
		return true;
	}

	public int primaryKeyHashCode() {
		return
				calculateKeyHashCode(getCd_cds())+
						calculateKeyHashCode(getEsercizio())+
						calculateKeyHashCode(getTi_documento())+
						calculateKeyHashCode(getPg_documento())+
						calculateKeyHashCode(getCd_voce())+
						calculateKeyHashCode(getCd_centro_responsabilita())+
						calculateKeyHashCode(getCd_linea_attivita())+
						calculateKeyHashCode(getEsercizio_originale());
	}
	@Override
	public boolean equals(Object obj) {
		return equalsByPrimaryKey(obj);
	}
}