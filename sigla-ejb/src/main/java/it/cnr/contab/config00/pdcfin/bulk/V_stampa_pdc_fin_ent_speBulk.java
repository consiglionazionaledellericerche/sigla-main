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

package it.cnr.contab.config00.pdcfin.bulk;

import java.util.Dictionary;

import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;


public class V_stampa_pdc_fin_ent_speBulk extends it.cnr.jada.bulk.OggettoBulk {
	private java.lang.Integer esercizio;
	
	private java.lang.String ti_gestione;
	public final static String TI_GESTIONE_ENTRATE = "E" ;
	public final static String TI_GESTIONE_SPESE   = "S" ;
	
	private final static java.util.Dictionary ti_gestioneKeys;
	static {
		ti_gestioneKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_gestioneKeys.put(TI_GESTIONE_SPESE, "Spese");
		ti_gestioneKeys.put(TI_GESTIONE_ENTRATE, "Entrate");
	}	
/**
 * V_stampa_pdc_fin_ent_speBulk constructor comment.
 */
public V_stampa_pdc_fin_ent_speBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (19/12/2002 11.03.03)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}

/**
 * Insert the method's description here.
 * Creation date: (19/12/2002 11.03.03)
 * @param newEsercizio java.lang.Integer
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}

public java.lang.String getTi_gestione () {
	return ti_gestione;
}
public void setTi_gestione(java.lang.String ti_gestione)  {
	this.ti_gestione=ti_gestione;
}

public Dictionary getTi_gestioneKeys() {
	return ti_gestioneKeys;
}

}
