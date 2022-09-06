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

package it.cnr.contab.config00.pdcep.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_ev_voceepBase extends Ass_ev_voceepKey implements Keyed {
	private java.lang.String cd_voce_ep_contr;
	public java.lang.String getCd_voce_ep_contr() {
		return cd_voce_ep_contr;
	}
	public void setCd_voce_ep_contr(java.lang.String cd_voce_ep_contr) {
		this.cd_voce_ep_contr = cd_voce_ep_contr;
	}
	public Ass_ev_voceepBase() {
		super();
	}
	public Ass_ev_voceepBase(java.lang.String cd_elemento_voce,java.lang.String cd_voce_ep,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
		super(cd_elemento_voce,cd_voce_ep,esercizio,ti_appartenenza,ti_gestione);
	}
}
