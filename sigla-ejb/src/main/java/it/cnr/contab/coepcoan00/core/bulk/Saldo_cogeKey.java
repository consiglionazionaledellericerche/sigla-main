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

package it.cnr.contab.coepcoan00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Saldo_cogeKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// CD_VOCE_EP VARCHAR(45) NOT NULL (PK)
	private java.lang.String cd_voce_ep;

	// CD_TERZO DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer cd_terzo;

	// TI_ISTITUZ_COMMERC CHAR(1) NOT NULL (PK)
	private java.lang.String ti_istituz_commerc;

public Saldo_cogeKey() {
	super();
}
public Saldo_cogeKey(java.lang.String cd_cds,java.lang.Integer cd_terzo,java.lang.String cd_voce_ep,java.lang.Integer esercizio,java.lang.String ti_istituz_commerc) {
	this.cd_cds = cd_cds;
	this.cd_terzo = cd_terzo;
	this.cd_voce_ep = cd_voce_ep;
	this.esercizio = esercizio;
	this.ti_istituz_commerc = ti_istituz_commerc;
}
public Saldo_cogeKey(java.lang.String cd_cds,java.lang.Integer cd_terzo,java.lang.String cd_unita_organizzativa,java.lang.String cd_voce_ep,java.lang.Integer esercizio,java.lang.String ti_istituz_commerc) {
	super();
	this.cd_cds = cd_cds;
	this.cd_terzo = cd_terzo;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
	this.cd_voce_ep = cd_voce_ep;
	this.esercizio = esercizio;
	this.ti_istituz_commerc = ti_istituz_commerc;	
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Saldo_cogeKey)) return false;
	Saldo_cogeKey k = (Saldo_cogeKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_terzo(),k.getCd_terzo())) return false;
	if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
	if(!compareKey(getCd_voce_ep(),k.getCd_voce_ep())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getTi_istituz_commerc(),k.getTi_istituz_commerc())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo cd_voce_ep
 */
public java.lang.String getCd_voce_ep() {
	return cd_voce_ep;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_terzo())+
		calculateKeyHashCode(getCd_unita_organizzativa())+
		calculateKeyHashCode(getCd_voce_ep())+
		calculateKeyHashCode(getEsercizio())+
	    calculateKeyHashCode(getTi_istituz_commerc());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo cd_voce_ep
 */
public void setCd_voce_ep(java.lang.String cd_voce_ep) {
	this.cd_voce_ep = cd_voce_ep;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
	/**
	 * @return
	 */
	public java.lang.String getTi_istituz_commerc() {
		return ti_istituz_commerc;
	}

	/**
	 * @param string
	 */
	public void setTi_istituz_commerc(java.lang.String string) {
		ti_istituz_commerc = string;
	}

}
