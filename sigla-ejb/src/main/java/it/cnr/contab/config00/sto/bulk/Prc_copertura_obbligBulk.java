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

package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Prc_copertura_obbligBulk extends Prc_copertura_obbligBase 
{
	protected CdsBulk cds;

public Prc_copertura_obbligBulk() {
	super();
}
public Prc_copertura_obbligBulk(java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio) {
	super(cd_unita_organizzativa,esercizio);
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cds'
 *
 * @return Il valore della proprietà 'cds'
 */
public CdsBulk getCds() {
	return cds;
}
/**
 * Metodo per la gestione del campo <code>esercizio</code>.
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	return this;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'cds'
 *
 * @param newCds	Il valore da assegnare a 'cds'
 */
public void setCds(CdsBulk newCds) {
	cds = newCds;
}
}
