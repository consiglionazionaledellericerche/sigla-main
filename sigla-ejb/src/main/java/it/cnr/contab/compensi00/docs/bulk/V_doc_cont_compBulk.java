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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_doc_cont_compBulk extends V_doc_cont_compBase {

	private CompensoBulk compenso;
	public final static java.lang.String TIPO_DOC_CONT_REVERSALE = "R";
	public final static java.lang.String TIPO_DOC_CONT_MANDATO = "M";
	public final static it.cnr.jada.util.OrderedHashtable TIPO_DOC_CONT;
	private it.cnr.contab.doccont00.core.bulk.IManRevBulk manRev;

	static {
		TIPO_DOC_CONT = new it.cnr.jada.util.OrderedHashtable();
		TIPO_DOC_CONT.put(TIPO_DOC_CONT_MANDATO,"Mandato");
		TIPO_DOC_CONT.put(TIPO_DOC_CONT_REVERSALE,"Reversale");
	}
public V_doc_cont_compBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (10/10/2002 10.48.02)
 * @return it.cnr.contab.compensi00.docs.bulk.CompensoBulk
 */
public CompensoBulk getCompenso() {
	return compenso;
}
/**
 * Insert the method's description here.
 * Creation date: (10/10/2002 11.29.53)
 * @return it.cnr.contab.doccont00.core.bulk.IManRevBulk
 */
public it.cnr.contab.doccont00.core.bulk.IManRevBulk getManRev() {
	return manRev;
}
/**
 * Insert the method's description here.
 * Creation date: (10/10/2002 11.29.53)
 * @return it.cnr.contab.doccont00.core.bulk.IManRevBulk
 */
public String getTipoDocCont() {
	return (String)TIPO_DOC_CONT.get(getTipo_doc_cont());
}
/**
 * Insert the method's description here.
 * Creation date: (23/10/2002 12.55.12)
 * @return boolean
 */
public boolean isDocumentoPrincipale() {
	return "Y".equals(getPrincipale());
}
/**
 * Insert the method's description here.
 * Creation date: (10/10/2002 10.48.02)
 * @param newCompenso it.cnr.contab.compensi00.docs.bulk.CompensoBulk
 */
public void setCompenso(CompensoBulk newCompenso) {
	compenso = newCompenso;
}
/**
 * Insert the method's description here.
 * Creation date: (10/10/2002 11.29.53)
 * @param newManRev it.cnr.contab.doccont00.core.bulk.IManRevBulk
 */
public void setManRev(it.cnr.contab.doccont00.core.bulk.IManRevBulk newManRev) {
	manRev = newManRev;
}

	public boolean isTipoDocMandato() {
		return TIPO_DOC_CONT_MANDATO.equals(this.getTipo_doc_cont());
	}
	public boolean isTipoDocReversale() {
		return TIPO_DOC_CONT_REVERSALE.equals(this.getTipo_doc_cont());
	}
}
