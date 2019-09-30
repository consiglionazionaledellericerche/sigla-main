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

package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Risultato_eliminazioneVBulk
	extends it.cnr.jada.bulk.OggettoBulk
	implements IDefferUpdateSaldi {

	private java.util.Vector documentiAmministrativiScollegati = new java.util.Vector();
	private java.util.Vector documentiContabiliScollegati = new java.util.Vector();

	private PrimaryKeyHashMap deferredSaldi = new PrimaryKeyHashMap();

/**
 * Consuntivo_rigaVBulk constructor comment.
 */
public Risultato_eliminazioneVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (9/11/2001 6:40:07 PM)
 * @return java.lang.String
 */
public void add(IDocumentoAmministrativoBulk docAmm) {

	if (docAmm == null) return;

	for (java.util.Iterator i = docAmm.getChildren().iterator(); i.hasNext();)
		add((IDocumentoAmministrativoRigaBulk)i.next());
}
/**
 * Insert the method's description here.
 * Creation date: (9/11/2001 6:40:07 PM)
 * @return java.lang.String
 */
public void add(IDocumentoAmministrativoRigaBulk riga) {

	if (riga == null) return;

	if (riga.getScadenzaDocumentoContabile() != null) {
		if (riga.isDirectlyLinkedToDC()) {
			IDocumentoContabileBulk docContAssociato = riga.getScadenzaDocumentoContabile().getFather();
			if (!BulkCollections.containsByPrimaryKey(documentiContabiliScollegati, (OggettoBulk)docContAssociato))
				addToDocumentiContabiliScollegati(docContAssociato);
		it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk scadenza = riga.getScadenzaDocumentoContabile();
		} else {
			IDocumentoAmministrativoBulk docAmmAssociato = riga.getAssociatedDetail().getFather();
			if (!BulkCollections.containsByPrimaryKey(documentiAmministrativiScollegati, (OggettoBulk)docAmmAssociato))
				addToDocumentiAmministrativiScollegati(docAmmAssociato);
		}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 10:50:29 AM)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
public void addToDefferredSaldi(
	it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk docCont, 
	java.util.Map values) {

	if (docCont != null) {
		if (deferredSaldi == null)
			deferredSaldi = new PrimaryKeyHashMap();
		if (!deferredSaldi.containsKey(docCont))
			deferredSaldi.put(docCont, values);
		else {
			java.util.Map firstValues = (java.util.Map)deferredSaldi.get(docCont);
			deferredSaldi.remove(docCont);
			deferredSaldi.put(docCont, firstValues);
		}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (11/28/2001 5:01:47 PM)
 * @return java.util.Vector
 */
public int addToDocumentiAmministrativiScollegati(IDocumentoAmministrativoBulk docAmm) {

	documentiAmministrativiScollegati.add(docAmm);
	return documentiAmministrativiScollegati.size()-1;
}
/**
 * Insert the method's description here.
 * Creation date: (11/28/2001 5:01:47 PM)
 * @return java.util.Vector
 */
public int addToDocumentiContabiliScollegati(IDocumentoContabileBulk docCont) {

	documentiContabiliScollegati.add(docCont);
	return documentiContabiliScollegati.size()-1;
}
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 10:50:29 AM)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
public it.cnr.jada.bulk.PrimaryKeyHashMap getDefferredSaldi() {
	return deferredSaldi;
}
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 10:50:29 AM)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
public IDocumentoContabileBulk getDefferredSaldoFor(IDocumentoContabileBulk docCont) {
	
	if (docCont != null && deferredSaldi != null)
		for (java.util.Iterator i = deferredSaldi.keySet().iterator(); i.hasNext();) {
			IDocumentoContabileBulk key = (IDocumentoContabileBulk)i.next();
			if (((OggettoBulk)docCont).equalsByPrimaryKey((OggettoBulk)key))
				return key;
		}
	return null;	
}
/**
 * Insert the method's description here.
 * Creation date: (11/28/2001 5:01:47 PM)
 * @return java.util.Vector
 */
public java.util.Vector getDocumentiAmministrativiScollegati() {
	return documentiAmministrativiScollegati;
}
/**
 * Insert the method's description here.
 * Creation date: (11/28/2001 5:01:47 PM)
 * @return java.util.Vector
 */
public java.util.Vector getDocumentiContabiliScollegati() {
	return documentiContabiliScollegati;
}
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 10:50:29 AM)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
public void removeFromDefferredSaldi(it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk docCont) {

	if (docCont != null && deferredSaldi != null &&
		deferredSaldi.containsKey(docCont))
			deferredSaldi.remove(docCont);
}
/**
 * Insert the method's description here.
 * Creation date: (11/28/2001 5:01:47 PM)
 * @return java.util.Vector
 */
public void removeFromDocumentiAmministrativiScollegati(IDocumentoAmministrativoBulk docAmm) {

	documentiAmministrativiScollegati.remove(docAmm);
}
/**
 * Insert the method's description here.
 * Creation date: (11/28/2001 5:01:47 PM)
 * @return java.util.Vector
 */
public void removeFromDocumentiContabiliScollegati(IDocumentoContabileBulk docCont) {

	documentiContabiliScollegati.remove(docCont);
}
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 10:50:29 AM)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
public void reset() {

	setDocumentiAmministrativiScollegati(new java.util.Vector());
	setDocumentiContabiliScollegati(new java.util.Vector());
	
	resetDefferredSaldi();
}
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 10:50:29 AM)
 * @param docCont it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk
 */
public void resetDefferredSaldi() {
	
	deferredSaldi = null;	
}
/**
 * Insert the method's description here.
 * Creation date: (11/28/2001 5:01:47 PM)
 * @param newFattureScollegate java.util.Vector
 */
public void setDocumentiAmministrativiScollegati(java.util.Vector newDocumentiAmministrativiScollegati) {
	documentiAmministrativiScollegati = newDocumentiAmministrativiScollegati;
}
/**
 * Insert the method's description here.
 * Creation date: (11/28/2001 5:01:47 PM)
 * @param newAccertamentiScollegati java.util.Vector
 */
public void setDocumentiContabiliScollegati(java.util.Vector newDocumentiContabiliScollegati) {
	documentiContabiliScollegati = newDocumentiContabiliScollegati;
}
}
