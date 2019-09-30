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

public class Nomenclatura_combinataBase extends Nomenclatura_combinataKey implements Keyed {
	// DS_NOMENCLATURA_COMBINATA VARCHAR(300) NOT NULL
	private java.lang.String ds_nomenclatura_combinata;

	private java.lang.Long livello;

	private java.lang.Integer esercizio;
	
	private java.lang.String cd_nomenclatura_combinata;
	
	private java.lang.Integer esercizio_inizio;
	
	private java.lang.Integer esercizio_fine;
	
	private java.lang.String unita_supplementari;

	
public Nomenclatura_combinataBase() {
	super();
}
public Nomenclatura_combinataBase(java.lang.Long id_nomenclatura_combinata) {
	super(id_nomenclatura_combinata);
}
public java.lang.String getDs_nomenclatura_combinata() {
	return ds_nomenclatura_combinata;
}
public void setDs_nomenclatura_combinata(java.lang.String ds_nomenclatura_combinata) {
	this.ds_nomenclatura_combinata = ds_nomenclatura_combinata;
}
public java.lang.Long getLivello() {
	return livello;
}
public void setLivello(java.lang.Long livello) {
	this.livello = livello;
}
public java.lang.Integer getEsercizio() {
	return esercizio;
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
public java.lang.String getCd_nomenclatura_combinata() {
	return cd_nomenclatura_combinata;
}
public void setCd_nomenclatura_combinata(
		java.lang.String cd_nomenclatura_combinata) {
	this.cd_nomenclatura_combinata = cd_nomenclatura_combinata;
}
public java.lang.Integer getEsercizio_inizio() {
	return esercizio_inizio;
}
public void setEsercizio_inizio(java.lang.Integer esercizio_inizio) {
	this.esercizio_inizio = esercizio_inizio;
}
public java.lang.Integer getEsercizio_fine() {
	return esercizio_fine;
}
public void setEsercizio_fine(java.lang.Integer esercizio_fine) {
	this.esercizio_fine = esercizio_fine;
}
public java.lang.String getUnita_supplementari() {
	return unita_supplementari;
}
public void setUnita_supplementari(java.lang.String unita_supplementari) {
	this.unita_supplementari = unita_supplementari;
}
}
