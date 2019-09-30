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

package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Natura_transazioneBase extends Natura_transazioneKey implements Keyed {
	// DS_NATURA_TRANSAZIONE VARCHAR(300) NOT NULL
	private java.lang.String ds_natura_transazione;
    
	private java.lang.Integer esercizio;
	
	private java.lang.String cd_natura_transazione;
	
	private java.lang.String cd_oper_triangolare;
public Natura_transazioneBase() {
	super();
}
public Natura_transazioneBase(java.lang.Long id_natura_transazione) {
	super(id_natura_transazione);
}
/* 
 * Getter dell'attributo ds_natura_transazione
 */
public java.lang.String getDs_natura_transazione() {
	return ds_natura_transazione;
}
/* 
 * Setter dell'attributo ds_natura_transazione
 */
public void setDs_natura_transazione(java.lang.String ds_natura_transazione) {
	this.ds_natura_transazione = ds_natura_transazione;
}
public java.lang.Integer getEsercizio() {
	return esercizio;
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
public java.lang.String getCd_natura_transazione() {
	return cd_natura_transazione;
}
public void setCd_natura_transazione(java.lang.String cd_natura_transazione) {
	this.cd_natura_transazione = cd_natura_transazione;
}
public java.lang.String getCd_oper_triangolare() {
	return cd_oper_triangolare;
}
public void setCd_oper_triangolare(java.lang.String cd_oper_triangolare) {
	this.cd_oper_triangolare = cd_oper_triangolare;
}
}
