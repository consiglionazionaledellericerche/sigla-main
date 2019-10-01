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

package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Prc_copertura_obbligBase extends Prc_copertura_obbligKey implements Keyed {
	// PRC_COPERTURA_OBBLIG_2 DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal prc_copertura_obblig_2;

	// PRC_COPERTURA_OBBLIG_3 DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal prc_copertura_obblig_3;

public Prc_copertura_obbligBase() {
	super();
}
public Prc_copertura_obbligBase(java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio) {
	super(cd_unita_organizzativa,esercizio);
}
/* 
 * Getter dell'attributo prc_copertura_obblig_2
 */
public java.math.BigDecimal getPrc_copertura_obblig_2() {
	return prc_copertura_obblig_2;
}
/* 
 * Getter dell'attributo prc_copertura_obblig_3
 */
public java.math.BigDecimal getPrc_copertura_obblig_3() {
	return prc_copertura_obblig_3;
}
/* 
 * Setter dell'attributo prc_copertura_obblig_2
 */
public void setPrc_copertura_obblig_2(java.math.BigDecimal prc_copertura_obblig_2) {
	this.prc_copertura_obblig_2 = prc_copertura_obblig_2;
}
/* 
 * Setter dell'attributo prc_copertura_obblig_3
 */
public void setPrc_copertura_obblig_3(java.math.BigDecimal prc_copertura_obblig_3) {
	this.prc_copertura_obblig_3 = prc_copertura_obblig_3;
}
}
