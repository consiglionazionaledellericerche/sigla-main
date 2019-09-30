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

public class Ass_ev_funz_tipocdsBase extends Ass_ev_funz_tipocdsKey implements Keyed {

public Ass_ev_funz_tipocdsBase() {
	super();
}
public Ass_ev_funz_tipocdsBase(java.lang.String cd_conto,java.lang.String cd_funzione,java.lang.String cd_tipo_unita,java.lang.Integer esercizio) {
	super(cd_conto,cd_funzione,cd_tipo_unita,esercizio);
}
}
