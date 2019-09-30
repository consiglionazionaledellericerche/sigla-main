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

package it.cnr.contab.fondecon00.views.bulk;

import it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Vsx_reintegro_fondoBulk extends Vsx_reintegro_fondoBase {

public Vsx_reintegro_fondoBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (5/16/2002 4:37:43 PM)
 * @param spesa it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk
 */
public void completeFrom(Fondo_spesaBulk spesa) {

	if (spesa == null) return;
	
	setCd_cds(spesa.getCd_cds());
	setCd_codice_fondo(spesa.getCd_codice_fondo());
	setCd_uo(spesa.getCd_unita_organizzativa());
	setEsercizio(spesa.getEsercizio());
	setPg_fondo_spesa(spesa.getPg_fondo_spesa());
	setProc_name("CNRCTB130.vsx_reintegraSpeseFondo");
	setUser(spesa.getUser());
}
}
