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

import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;

/**
 * Insert the type's description here.
 * Creation date: (19/12/2002 11.02.47)
 * @author: Simonetta Costa
 */
public class V_stampa_pdc_fin_speseBulk extends it.cnr.jada.bulk.OggettoBulk {
	private java.lang.Integer esercizio;
	private java.lang.String tipoCds;
	private OrderedHashtable tipologieCdsKeys;
/**
 * V_stampa_pdc_fin_speseBulk constructor comment.
 */
public V_stampa_pdc_fin_speseBulk() {
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
 * Creation date: (19/12/2002 11.20.35)
 * @return java.lang.String
 */
public java.lang.String getTipoCds() {
	return tipoCds;
}
/**
 * Insert the method's description here.
 * Creation date: (19/12/2002 11.24.48)
 * @return java.util.Hashtable
 */
public OrderedHashtable getTipologieCdsKeys() {
	return tipologieCdsKeys;
}
/**
 * Insert the method's description here.
 * Creation date: (19/12/2002 11.03.03)
 * @param newEsercizio java.lang.Integer
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (19/12/2002 11.20.35)
 * @param newTipoCds java.lang.String
 */
public void setTipoCds(java.lang.String newTipoCds) {
	tipoCds = newTipoCds;
}
/**
 * Insert the method's description here.
 * Creation date: (19/12/2002 11.24.48)
 * @param newTipologieCdsKeys java.util.Hashtable
 */
public void setTipologieCdsKeys(OrderedHashtable newTipologieCdsKeys) {
	tipologieCdsKeys = newTipologieCdsKeys;
}
public void validate() throws ValidationException{

	if (getEsercizio()==null)
		throw new ValidationException("Il campo ESERCIZIO e' obbligatorio");

	if (getTipoCds()==null)
		throw new ValidationException("Il campo TIPO CDS e' obbligatorio");
}
}
