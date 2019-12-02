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

package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;

public class Stipendi_cofi_coriKey extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// MESE DECIMAL(2,0) NOT NULL (PK)
	private java.lang.Integer mese;
	
	// CD_CONTRIBUTO_RITENUTA VARCHAR2(10) NOT NULL (PK)
	private String cd_contributo_ritenuta;

	// TI_ENTE_PERCIPIENTE VARCHAR2(1) NOT NULL (PK)
	private String ti_ente_percipiente;
	 
public Stipendi_cofi_coriKey() {
	super();
}
public Stipendi_cofi_coriKey(java.lang.Integer esercizio,
                             java.lang.Integer mese,
                             String cd_contributo_ritenuta,
                             String ti_ente_percipiente) {
	super();
	this.esercizio = esercizio;
	this.mese = mese;
	this.cd_contributo_ritenuta = cd_contributo_ritenuta;
	this.ti_ente_percipiente = ti_ente_percipiente;
}

public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Stipendi_cofi_coriKey)) return false;
	Stipendi_cofi_coriKey k = (Stipendi_cofi_coriKey)o;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getMese(),k.getMese())) return false;
	if(!compareKey(getCd_contributo_ritenuta(),k.getCd_contributo_ritenuta())) return false;
	if(!compareKey(getTi_ente_percipiente(),k.getTi_ente_percipiente())) return false;
	return true;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo mese
 */
public java.lang.Integer getMese() {
	return mese;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getMese())+
		calculateKeyHashCode(getCd_contributo_ritenuta())+
		calculateKeyHashCode(getTi_ente_percipiente());
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo mese
 */
public void setMese(java.lang.Integer mese) {
	this.mese = mese;
}
	/**
	 * @return
	 */
	public String getCd_contributo_ritenuta() {
		return cd_contributo_ritenuta;
	}

	/**
	 * @return
	 */
	public String getTi_ente_percipiente() {
		return ti_ente_percipiente;
	}

	/**
	 * @param string
	 */
	public void setCd_contributo_ritenuta(String string) {
		cd_contributo_ritenuta = string;
	}

	/**
	 * @param string
	 */
	public void setTi_ente_percipiente(String string) {
		ti_ente_percipiente = string;
	}

}
