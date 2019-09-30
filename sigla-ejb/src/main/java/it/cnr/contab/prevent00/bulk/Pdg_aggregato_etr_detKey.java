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

package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Pdg_aggregato_etr_detKey extends OggettoBulk implements KeyedPersistent {
	// TI_APPARTENENZA CHAR(1) NOT NULL (PK)
	private java.lang.String ti_appartenenza;

	// CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_elemento_voce;

	// TI_AGGREGATO CHAR(1) NOT NULL (PK)
	private java.lang.String ti_aggregato;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_centro_responsabilita;

	// CD_NATURA VARCHAR(1) NOT NULL (PK)
	private java.lang.String cd_natura;

	// TI_GESTIONE CHAR(1) NOT NULL (PK)
	private java.lang.String ti_gestione;

public Pdg_aggregato_etr_detKey() {
	super();
}
public Pdg_aggregato_etr_detKey(java.lang.String cd_centro_responsabilita,java.lang.String cd_elemento_voce,java.lang.String cd_natura,java.lang.Integer esercizio,java.lang.String ti_aggregato,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super();
	this.cd_centro_responsabilita = cd_centro_responsabilita;
	this.cd_elemento_voce = cd_elemento_voce;
	this.cd_natura = cd_natura;
	this.esercizio = esercizio;
	this.ti_aggregato = ti_aggregato;
	this.ti_appartenenza = ti_appartenenza;
	this.ti_gestione = ti_gestione;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Pdg_aggregato_etr_detKey)) return false;
	Pdg_aggregato_etr_detKey k = (Pdg_aggregato_etr_detKey)o;
	if(!compareKey(getCd_centro_responsabilita(),k.getCd_centro_responsabilita())) return false;
	if(!compareKey(getCd_elemento_voce(),k.getCd_elemento_voce())) return false;
	if(!compareKey(getCd_natura(),k.getCd_natura())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getTi_aggregato(),k.getTi_aggregato())) return false;
	if(!compareKey(getTi_appartenenza(),k.getTi_appartenenza())) return false;
	if(!compareKey(getTi_gestione(),k.getTi_gestione())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_centro_responsabilita
 */
public java.lang.String getCd_centro_responsabilita() {
	return cd_centro_responsabilita;
}
/* 
 * Getter dell'attributo cd_elemento_voce
 */
public java.lang.String getCd_elemento_voce() {
	return cd_elemento_voce;
}
/* 
 * Getter dell'attributo cd_natura
 */
public java.lang.String getCd_natura() {
	return cd_natura;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo ti_aggregato
 */
public java.lang.String getTi_aggregato() {
	return ti_aggregato;
}
/* 
 * Getter dell'attributo ti_appartenenza
 */
public java.lang.String getTi_appartenenza() {
	return ti_appartenenza;
}
/* 
 * Getter dell'attributo ti_gestione
 */
public java.lang.String getTi_gestione() {
	return ti_gestione;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_centro_responsabilita())+
		calculateKeyHashCode(getCd_elemento_voce())+
		calculateKeyHashCode(getCd_natura())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getTi_aggregato())+
		calculateKeyHashCode(getTi_appartenenza())+
		calculateKeyHashCode(getTi_gestione());
}
/* 
 * Setter dell'attributo cd_centro_responsabilita
 */
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	this.cd_centro_responsabilita = cd_centro_responsabilita;
}
/* 
 * Setter dell'attributo cd_elemento_voce
 */
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.cd_elemento_voce = cd_elemento_voce;
}
/* 
 * Setter dell'attributo cd_natura
 */
public void setCd_natura(java.lang.String cd_natura) {
	this.cd_natura = cd_natura;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo ti_aggregato
 */
public void setTi_aggregato(java.lang.String ti_aggregato) {
	this.ti_aggregato = ti_aggregato;
}
/* 
 * Setter dell'attributo ti_appartenenza
 */
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.ti_appartenenza = ti_appartenenza;
}
/* 
 * Setter dell'attributo ti_gestione
 */
public void setTi_gestione(java.lang.String ti_gestione) {
	this.ti_gestione = ti_gestione;
}
}
