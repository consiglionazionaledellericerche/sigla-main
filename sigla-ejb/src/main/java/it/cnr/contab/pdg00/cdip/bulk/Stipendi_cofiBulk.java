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

public class Stipendi_cofiBulk extends Stipendi_cofiBase {
	private static final java.util.Dictionary statoKeys = new it.cnr.jada.util.OrderedHashtable();
	public static final String STATO_LIQUIDATO = "P";
	public static final String STATO_NON_LIQUIDATO = "I";

	static {
		statoKeys.put(STATO_LIQUIDATO,"Liquidato");
		statoKeys.put(STATO_NON_LIQUIDATO,"Non liquidato");
	}
	private static final java.util.Dictionary meseKeys = new it.cnr.jada.util.OrderedHashtable();
	public static final int GENNAIO = 1;
	public static final int FEBBRAIO = 2;
	public static final int MARZO = 3;
	public static final int APRILE = 4;
	public static final int MAGGIO = 5;
	public static final int GIUGNO = 6;
	public static final int LUGLIO = 7;
	public static final int AGOSTO = 8;
	public static final int SETTEMBRE = 9;
	public static final int OTTOBRE = 10;
	public static final int NOVEMBRE = 11;
	public static final int DICEMBRE = 12;

	static {
		meseKeys.put(new Integer(GENNAIO),"Gennaio");
		meseKeys.put(new Integer(FEBBRAIO),"Febbraio");
		meseKeys.put(new Integer(MARZO),"Marzo");
		meseKeys.put(new Integer(APRILE),"Aprile");
		meseKeys.put(new Integer(MAGGIO),"Maggio");
		meseKeys.put(new Integer(GIUGNO),"Giugno");
		meseKeys.put(new Integer(LUGLIO),"Luglio");
		meseKeys.put(new Integer(AGOSTO),"Agosto");
		meseKeys.put(new Integer(SETTEMBRE),"Settembre");
		meseKeys.put(new Integer(OTTOBRE),"Ottobre");
		meseKeys.put(new Integer(NOVEMBRE),"Novembre");
		meseKeys.put(new Integer(DICEMBRE),"Dicembre");
	}	
public Stipendi_cofiBulk() {
	super();
}
public Stipendi_cofiBulk(java.lang.Integer esercizio,java.lang.Integer mese) {
	super(esercizio,mese);
}
/**
 * Insert the method's description here.
 * Creation date: (21/10/2002 12:34:48)
 * @return java.util.Dictionary
 */
public final java.util.Dictionary getStatoKeys() {
	return statoKeys;
}
	/**
	 * @return
	 */
	public static java.util.Dictionary getMeseKeys() {
		return meseKeys;
	}

	public boolean isLiquidato() {
		return Stipendi_cofiBulk.STATO_LIQUIDATO.equals(this.getStato());
	}

	public boolean isNonLiquidato() {
		return Stipendi_cofiBulk.STATO_NON_LIQUIDATO.equals(this.getStato());
	}
}
