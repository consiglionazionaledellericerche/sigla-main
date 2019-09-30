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

package it.cnr.contab.pdg00.bulk;

/**
 * Insert the type's description here.
 * Creation date: (27/08/2004 16.10.22)
 * @author: Gennaro Borriello
 */
public class Stampa_vpg_stato_patrim_riclassVBulk extends Stampa_vpg_bilancio_riclassVBulk {


	// ATTIVITA_PASSIVITA
	private java.lang.String ti_att_pass;

	public static final String TIPO_ATTIVITA = "SPATT";
	public static final String TIPO_PASSIVITA= "SPPAS";
	
	public final static java.util.Dictionary ti_att_passKeys;
	static 
	{
		ti_att_passKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_att_passKeys.put(TIPO_ATTIVITA,  "Attività");
		ti_att_passKeys.put(TIPO_PASSIVITA, "Passività");
	};	
/**
 * Stampa_vpg_stato_patrim_riclassVBulk constructor comment.
 */
public Stampa_vpg_stato_patrim_riclassVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (27/08/2004 17.31.40)
 * @return java.lang.String
 */
public java.lang.String getTi_att_pass() {
	return ti_att_pass;
}
/**
 * Insert the method's description here.
 * Creation date: (27/08/2004 17.31.40)
 * @return java.util.Dictionary
 */
public final static java.util.Dictionary getTi_att_passKeys() {
	return ti_att_passKeys;
}
/**
 * Insert the method's description here.
 * Creation date: (27/08/2004 17.31.40)
 * @param newTi_att_pass java.lang.String
 */
public void setTi_att_pass(java.lang.String newTi_att_pass) {
	ti_att_pass = newTi_att_pass;
}
}
