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

package it.cnr.contab.doccont00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_consegnaBulk extends Tipo_consegnaBase {

/**
 * Tipo_consegnaBulk constructor comment.
 */
public Tipo_consegnaBulk() {
	super();
}
/**
 * Tipo_consegnaBulk constructor comment.
 * @param cd_tipo_consegna Il codice del tipo di consegna
 */
public Tipo_consegnaBulk(java.lang.String cd_tipo_consegna) {
	super(cd_tipo_consegna);
}
}
