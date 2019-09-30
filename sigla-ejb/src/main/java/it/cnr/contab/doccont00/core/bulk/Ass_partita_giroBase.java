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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_partita_giroBase extends Ass_partita_giroKey implements Keyed {
	// CD_VOCE_CLG VARCHAR(50)
	private java.lang.String cd_voce_clg;

	// TI_APPARTENENZA_CLG CHAR(1)
	private java.lang.String ti_appartenenza_clg;

	// TI_GESTIONE_CLG CHAR(1)
	private java.lang.String ti_gestione_clg;

public Ass_partita_giroBase() {
	super();
}
public Ass_partita_giroBase(java.lang.String cd_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_voce,esercizio,ti_appartenenza,ti_gestione);
}
/* 
 * Getter dell'attributo cd_voce_clg
 */
public java.lang.String getCd_voce_clg() {
	return cd_voce_clg;
}
/* 
 * Getter dell'attributo ti_appartenenza_clg
 */
public java.lang.String getTi_appartenenza_clg() {
	return ti_appartenenza_clg;
}
/* 
 * Getter dell'attributo ti_gestione_clg
 */
public java.lang.String getTi_gestione_clg() {
	return ti_gestione_clg;
}
/* 
 * Setter dell'attributo cd_voce_clg
 */
public void setCd_voce_clg(java.lang.String cd_voce_clg) {
	this.cd_voce_clg = cd_voce_clg;
}
/* 
 * Setter dell'attributo ti_appartenenza_clg
 */
public void setTi_appartenenza_clg(java.lang.String ti_appartenenza_clg) {
	this.ti_appartenenza_clg = ti_appartenenza_clg;
}
/* 
 * Setter dell'attributo ti_gestione_clg
 */
public void setTi_gestione_clg(java.lang.String ti_gestione_clg) {
	this.ti_gestione_clg = ti_gestione_clg;
}
}
