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

package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Gestione dei dati relativi alla tabella Rif_termini_pagamento
 */

public class Rif_termini_pagamentoBulk extends Rif_termini_pagamentoBase {

/**
 * 
 */
public Rif_termini_pagamentoBulk() {}
public Rif_termini_pagamentoBulk(java.lang.String cd_termini_pag) {
	super(cd_termini_pag);
}
public String getCd_ds_termini_pagamento() {

	String result = "";
	result += ((getCd_termini_pag() == null) ? "n.n." : getCd_termini_pag()) + " - ";
	result += ((getDs_termini_pag() == null) ? "n.n." : getDs_termini_pag());
	return result;
}
}
