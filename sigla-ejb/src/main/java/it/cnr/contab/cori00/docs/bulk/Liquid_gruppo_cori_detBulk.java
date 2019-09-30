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

public class Liquid_gruppo_cori_detBulk extends Liquid_gruppo_cori_detBase {


public Liquid_gruppo_cori_detBulk() {
	super();
}
public Liquid_gruppo_cori_detBulk(java.lang.String cd_cds,java.lang.String cd_contributo_ritenuta,java.lang.String cd_gruppo_cr,java.lang.String cd_regione,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_compenso,java.lang.Long pg_comune,java.lang.Integer pg_liquidazione,java.lang.String ti_ente_percipiente) {
	super(cd_cds,cd_contributo_ritenuta,cd_gruppo_cr,cd_regione,cd_unita_organizzativa,esercizio,pg_compenso,pg_comune,pg_liquidazione,ti_ente_percipiente);
}
}
