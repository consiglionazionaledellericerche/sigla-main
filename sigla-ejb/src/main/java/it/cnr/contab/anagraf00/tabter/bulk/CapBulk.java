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

package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.action.*;

/**
 * Gestione dei dati relativi alla tabella Tipo_rapporto
 */

public class CapBulk extends CapBase {

	private ComuneBulk comune;
/**
 * 
 */
public CapBulk() {}
public CapBulk(java.lang.String cd_cap,java.lang.Long pg_comune) {
	super(cd_cap,pg_comune);
}
/**
 * Insert the method's description here.
 * Creation date: (30/10/2002 11.52.30)
 * @return it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk
 */
public ComuneBulk getComune() {
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
	setComune(new ComuneBulk());
	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (30/10/2002 11.52.30)
 * @return it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk
 */
public boolean isROComune() {
	return getComune()==null || getComune().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (30/10/2002 11.52.30)
 * @param newComune it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk
 */
public void setComune(ComuneBulk newComune) {
	comune = newComune;
}
public void setPg_comune(java.lang.Long pg_comune) {
	this.getComune().setPg_comune(pg_comune);
}
/**
 * Oltre alla normale validate da un avviso di errore se la nazione è nulla.
 *
 * @exeption it.cnr.jada.bulk.ValidationException
*/
public void validate() throws ValidationException {

	super.validate();
	if (getPg_comune()==null)
		throw new ValidationException("Il campo COMUNE non può essere vuoto");
	if (getCd_cap()==null)
		throw new ValidationException("Il campo CAP non può essere vuoto");
}
}
