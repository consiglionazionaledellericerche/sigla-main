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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_ev_evBase extends Ass_ev_evKey implements Keyed {
	// TI_ELEMENTO_VOCE CHAR(1) NOT NULL
	private java.lang.String ti_elemento_voce;

	// TI_ELEMENTO_VOCE_COLL CHAR(1)
	private java.lang.String ti_elemento_voce_coll;

public Ass_ev_evBase() {
	super();
}
public Ass_ev_evBase(java.lang.String cd_cds,java.lang.String cd_elemento_voce,java.lang.String cd_elemento_voce_coll,java.lang.String cd_natura,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_appartenenza_coll,java.lang.String ti_gestione,java.lang.String ti_gestione_coll) {
	super(cd_cds,cd_elemento_voce,cd_elemento_voce_coll,cd_natura,esercizio,ti_appartenenza,ti_appartenenza_coll,ti_gestione,ti_gestione_coll);
}
/* 
 * Getter dell'attributo ti_elemento_voce
 */
public java.lang.String getTi_elemento_voce() {
	return ti_elemento_voce;
}
/* 
 * Getter dell'attributo ti_elemento_voce_coll
 */
public java.lang.String getTi_elemento_voce_coll() {
	return ti_elemento_voce_coll;
}
/* 
 * Setter dell'attributo ti_elemento_voce
 */
public void setTi_elemento_voce(java.lang.String ti_elemento_voce) {
	this.ti_elemento_voce = ti_elemento_voce;
}
/* 
 * Setter dell'attributo ti_elemento_voce_coll
 */
public void setTi_elemento_voce_coll(java.lang.String ti_elemento_voce_coll) {
	this.ti_elemento_voce_coll = ti_elemento_voce_coll;
}
}
