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

public class Liquid_gruppo_coriBulk extends Liquid_gruppo_coriBase {

	private Liquid_coriBulk liquidazione_cori;

public Liquid_gruppo_coriBulk() {
	super();
}
public Liquid_gruppo_coriBulk(java.lang.String cd_cds,java.lang.String cd_gruppo_cr,java.lang.String cd_regione,java.lang.Integer esercizio,java.lang.Long pg_comune,java.lang.Integer pg_liquidazione) {
	super(cd_cds,cd_gruppo_cr,cd_regione,esercizio,pg_comune,pg_liquidazione);
}
/**
 * Insert the method's description here.
 * Creation date: (14/06/2002 15.43.22)
 * @return it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk
 */
public Liquid_coriBulk getLiquidazione_cori() {
	return liquidazione_cori;
}
/**
 * Insert the method's description here.
 * Creation date: (14/06/2002 15.43.22)
 * @param newLiquidazione_cori it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk
 */
public void setLiquidazione_cori(Liquid_coriBulk newLiquidazione_cori) {
	liquidazione_cori = newLiquidazione_cori;
}
}
