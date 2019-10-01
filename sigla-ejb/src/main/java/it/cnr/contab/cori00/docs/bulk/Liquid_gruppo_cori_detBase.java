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

package it.cnr.contab.cori00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Liquid_gruppo_cori_detBase extends Liquid_gruppo_cori_detKey implements Keyed {
	// ESERCIZIO_CONTRIBUTO_RITENUTA DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_contributo_ritenuta;

public Liquid_gruppo_cori_detBase() {
	super();
}
public Liquid_gruppo_cori_detBase(java.lang.String cd_cds,java.lang.String cd_contributo_ritenuta,java.lang.String cd_gruppo_cr,java.lang.String cd_regione,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_compenso,java.lang.Long pg_comune,java.lang.Integer pg_liquidazione,java.lang.String ti_ente_percipiente) {
	super(cd_cds,cd_contributo_ritenuta,cd_gruppo_cr,cd_regione,cd_unita_organizzativa,esercizio,pg_compenso,pg_comune,pg_liquidazione,ti_ente_percipiente);
}
public Liquid_gruppo_cori_detBase(java.lang.String cd_cds,java.lang.String cd_cds_origine,java.lang.String cd_contributo_ritenuta,java.lang.String cd_gruppo_cr,java.lang.String cd_regione,java.lang.String cd_unita_organizzativa,java.lang.String cd_uo_origine,java.lang.Integer esercizio,java.lang.Long pg_compenso,java.lang.Long pg_comune,java.lang.Integer pg_liquidazione,java.lang.Integer pg_liquidazione_origine,java.lang.String ti_ente_percipiente) {
	super(cd_cds,cd_cds_origine,cd_contributo_ritenuta,cd_gruppo_cr,cd_regione,cd_unita_organizzativa,cd_uo_origine,esercizio,pg_compenso,pg_comune,pg_liquidazione,pg_liquidazione_origine,ti_ente_percipiente);
}
/* 
 * Getter dell'attributo esercizio_contributo_ritenuta
 */
public java.lang.Integer getEsercizio_contributo_ritenuta() {
	return esercizio_contributo_ritenuta;
}
/* 
 * Setter dell'attributo esercizio_contributo_ritenuta
 */
public void setEsercizio_contributo_ritenuta(java.lang.Integer esercizio_contributo_ritenuta) {
	this.esercizio_contributo_ritenuta = esercizio_contributo_ritenuta;
}
}
