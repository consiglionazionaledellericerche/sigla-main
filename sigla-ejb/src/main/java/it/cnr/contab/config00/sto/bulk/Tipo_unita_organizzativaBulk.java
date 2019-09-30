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
import it.cnr.jada.util.*;

public class Tipo_unita_organizzativaBulk extends Tipo_unita_organizzativaBase {



public Tipo_unita_organizzativaBulk() {
	super();
}
public Tipo_unita_organizzativaBulk(java.lang.String cd_tipo_unita) {
	super(cd_tipo_unita);
}
/**
 * Esegue la validazione formale dei campi di input
 */

public void validate() throws ValidationException {
	if ( getCd_tipo_unita() == null || getCd_tipo_unita().equals(""))
		throw new ValidationException( "Il campo CODICE è obbligatorio." );
	if ( getDs_tipo_unita() == null || getDs_tipo_unita().equals(""))
		throw new ValidationException( "Il campo DESCRIZIONE è obbligatorio." );		
}
}
