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

public class Saldo_coanBase extends Saldo_coanKey implements Keyed {
	// TOT_AVERE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal tot_avere;

	// TOT_DARE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal tot_dare;

public Saldo_coanBase() {
	super();
}
public Saldo_coanBase(java.lang.String cd_cds,java.lang.String cd_centro_responsabilita,java.lang.String cd_linea_attivita,java.lang.String cd_unita_organizzativa,java.lang.String cd_voce_ep,java.lang.Integer esercizio,java.lang.String ti_istituz_commerc) {
	super(cd_cds,cd_centro_responsabilita,cd_linea_attivita,cd_unita_organizzativa,cd_voce_ep,esercizio,ti_istituz_commerc);
}
/* 
 * Getter dell'attributo tot_avere
 */
public java.math.BigDecimal getTot_avere() {
	return tot_avere;
}
/* 
 * Getter dell'attributo tot_dare
 */
public java.math.BigDecimal getTot_dare() {
	return tot_dare;
}
/* 
 * Setter dell'attributo tot_avere
 */
public void setTot_avere(java.math.BigDecimal tot_avere) {
	this.tot_avere = tot_avere;
}
/* 
 * Setter dell'attributo tot_dare
 */
public void setTot_dare(java.math.BigDecimal tot_dare) {
	this.tot_dare = tot_dare;
}
}
