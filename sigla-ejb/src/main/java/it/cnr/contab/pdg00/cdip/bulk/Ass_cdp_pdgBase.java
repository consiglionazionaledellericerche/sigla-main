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

package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_cdp_pdgBase extends Ass_cdp_pdgKey implements Keyed {

public Ass_cdp_pdgBase() {
	super();
}
public Ass_cdp_pdgBase(java.lang.String cd_centro_responsabilita,java.lang.String cd_elemento_voce,java.lang.String cd_linea_attivita,java.lang.Integer esercizio,java.lang.String id_matricola,java.lang.Long pg_spesa,java.lang.String ti_appartenenza,java.lang.String ti_gestione,java.lang.String ti_prev_cons) {
	super(cd_centro_responsabilita,cd_elemento_voce,cd_linea_attivita,esercizio,id_matricola,pg_spesa,ti_appartenenza,ti_gestione,ti_prev_cons);
}
}
