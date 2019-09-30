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

package it.cnr.contab.doccont00.singconto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_stm_paramin_sing_contoBulk extends V_stm_paramin_sing_contoBase {

public V_stm_paramin_sing_contoBulk() {
	super();

	setTipo("A");
}
public V_stm_paramin_sing_contoBulk(V_voce_f_sing_contoBulk voceF) {
	
	this();
	
	completeFrom(voceF);
}
private void completeFrom(V_voce_f_sing_contoBulk voceF) {

	if (voceF == null) return;

	setCd_cds(voceF.getCd_cds());
	setEsercizio(new java.math.BigDecimal(voceF.getEsercizio().toString()));
	setTi_appartenenza(voceF.getTi_appartenenza());
	setTi_gestione(voceF.getTi_gestione());
	setCd_voce(voceF.getCd_voce());
	setTi_competenza_residuo(voceF.getTi_competenza_residuo());

	//voceF.getCd_cds_proprio());
	//voceF.getDs_voce());
	//voceF.getCd_natura());
	//voceF.getCd_funzione());
	//voceF.getCd_parte());
	//voceF.getCd_categoria());
	//setvoceF.getCd_unita_organizzativa());
	//voceF.getCd_proprio_voce());
	//voceF.getCd_elemento_voce());
	//voceF.getDs_elemento_voce());
	//voceF.getFl_partita_giro());
	//voceF.getFl_voce_sac());
}
}
