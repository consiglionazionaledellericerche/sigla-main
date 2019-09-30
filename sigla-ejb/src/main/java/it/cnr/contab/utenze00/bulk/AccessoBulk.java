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

package it.cnr.contab.utenze00.bulk;

/**
 * Definisce "cosa" un utente può fare all'interno di tutte le funzionalità della Gestione Contabile
 *	
 */

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class AccessoBulk extends AccessoBase {
	public static final String TIPO_RISERVATO_CNR 	= "C";
	public static final String TIPO_PUBBLICO 		= "D";
	public static final String TIPO_SUPERUTENTE 	= "S";
	public static final String TIPO_AMMIN_UTENZE 	= "A";	
//	public static final String CD_MOD_AMMIN_UTENZE = "CFGUTENZECOREUADMINM";
//	public static final String CD_VIS_AMMIN_UTENZE = "CFGUTENZECOREUADMINV";
//	public static final String CD_MOD_UT_COMUNE = "CFGUTENZECOREUCOMUNV";
//	public static final String CD_VIS_UT_COMUNE = "CFGUTENZECOREUCOMUNM";
//	public static final String CD_MOD_ACC_RUO 	= "CFGUTENZECOREACCRUOV";
//	public static final String CD_VIS_ACC_RUO 	= "CFGUTENZECOREACCRUOM";	
public AccessoBulk() {
	super();
}
public AccessoBulk(java.lang.String cd_accesso) {
	super(cd_accesso);
	this.setCd_accesso(cd_accesso);
}
}
