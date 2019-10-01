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

import it.cnr.contab.anagraf00.tabter.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.action.*;

/**
 * Gestione dei dati relativi alla tabella Abicab
 */

public class AbicabBulk extends AbicabBase {

	private ComuneBulk comune;
	private java.util.Collection capsComune;
/**
 * 
 */
public AbicabBulk() {}
public AbicabBulk(java.lang.String abi,java.lang.String cab) {
	super(abi,cab);
}
/**
 * Insert the method's description here.
 * Creation date: (13/11/2002 12.16.59)
 * @return java.util.Collection
 */
public java.util.Collection getCapsComune() {
	return capsComune;
}
	/**
	 * Insert the method's description here.
	 * Creation date: (24/08/2001 11.10.33)
	 * @return it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk
	 */
	public it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk getComune() {
		return comune;
	}

public java.lang.Long getPg_comune() {
	it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune = this.getComune();
	if (comune == null)
		return null;
	return comune.getPg_comune();
}
public OggettoBulk initialize(CRUDBP bp, ActionContext context) {

	super.initialize(bp, context);
	resetComune();
	return this;
}
public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {

	super.initializeForInsert(bp, context);
	resetFlags();
	return this;
}
public boolean isCancellatoLogicamente() {

	if (getFl_cancellato() != null)
		return getFl_cancellato().booleanValue();

	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (30/10/2002 11.52.30)
 * @return it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk
 */
public boolean isROComune() {
	return getComune()==null || getComune().getCrudStatus()==NORMAL;
}
public void resetCaps() {
	setCapsComune(new java.util.Vector());
}
public void resetComune() {
	setComune(new ComuneBulk());
	setCapsComune(null);
}
private void resetFlags() {
	setFl_cancellato(Boolean.FALSE);
}
/**
 * Insert the method's description here.
 * Creation date: (13/11/2002 12.16.59)
 * @param newCapsComune java.util.Collection
 */
public void setCapsComune(java.util.Collection newCapsComune) {
	capsComune = newCapsComune;
}
	/**
	 * Insert the method's description here.
	 * Creation date: (24/08/2001 11.10.33)
	 * @param newComune it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk
	 */
	public void setComune(it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk newComune) {
		comune = newComune;
	}

public void setPg_comune(java.lang.Long pg_comune) {
	this.getComune().setPg_comune(pg_comune);
}
/**
 *
 *
*/
public void validate() throws ValidationException {

	super.validate();
	if (getAbi()==null)
		throw new ValidationException("Il campo ABI non può essere vuoto");
	if (getCab()==null)
		throw new ValidationException("Il campo CAB non può essere vuoto");
	if(getDs_abicab()==null)
		throw new ValidationException("Il campo DESCRIZIONE non può essere vuoto");
}
}
