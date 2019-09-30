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
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_cdp_stato_mensilitaBase extends OggettoBulk implements Persistent {
	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;

	// ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;

	// FL_STATO_SCARICO VARCHAR(1)
	private Boolean fl_stato_scarico;

	// MESE DECIMAL(2,0) NOT NULL
	private java.lang.Integer mese;

	// STATO_CARICO CHAR(1) NOT NULL
	private String stato_carico;

public V_cdp_stato_mensilitaBase() {
	super();
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (27/09/2002 12:57:02)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_stato_scarico() {
	return fl_stato_scarico;
}
/* 
 * Getter dell'attributo mese
 */
public java.lang.Integer getMese() {
	return mese;
}
/**
 * Insert the method's description here.
 * Creation date: (27/09/2002 12:57:02)
 * @return java.lang.String
 */
public java.lang.String getStato_carico() {
	return stato_carico;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (27/09/2002 12:57:02)
 * @param newFl_stato_scarico java.lang.Boolean
 */
public void setFl_stato_scarico(java.lang.Boolean newFl_stato_scarico) {
	fl_stato_scarico = newFl_stato_scarico;
}
/* 
 * Setter dell'attributo mese
 */
public void setMese(java.lang.Integer mese) {
	this.mese = mese;
}
/**
 * Insert the method's description here.
 * Creation date: (27/09/2002 12:57:02)
 * @param newStato_carico java.lang.String
 */
public void setStato_carico(java.lang.String newStato_carico) {
	stato_carico = newStato_carico;
}
}
