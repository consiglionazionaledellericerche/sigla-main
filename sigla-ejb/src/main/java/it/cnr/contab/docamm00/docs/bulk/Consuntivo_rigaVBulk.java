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

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Consuntivo_rigaVBulk extends it.cnr.jada.bulk.OggettoBulk {

	protected IDocumentoAmministrativoBulk documentoAmministrativo;
	protected it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk voce_iva;

	protected java.math.BigDecimal totale_imponibile = new java.math.BigDecimal(0);
	protected java.math.BigDecimal totale_prezzo = new java.math.BigDecimal(0);
	protected java.math.BigDecimal totale_iva = new java.math.BigDecimal(0);
/**
 * Consuntivo_rigaVBulk constructor comment.
 */
private Consuntivo_rigaVBulk() {
	super();
}
/**
 * Consuntivo_rigaVBulk constructor comment.
 */
public Consuntivo_rigaVBulk(IDocumentoAmministrativoRigaBulk rigaDocAmm) {
	super();

	init(rigaDocAmm);
}
public boolean equalsByPrimaryKey(Object bulk) {

	if (bulk == null) return false;
	if (!(bulk instanceof Consuntivo_rigaVBulk)) return false;
	Consuntivo_rigaVBulk obj = (Consuntivo_rigaVBulk)bulk;
	if (((OggettoBulk)getDocumentoAmministrativo()).equalsByPrimaryKey(obj.getDocumentoAmministrativo()) &&
		getVoce_iva().equalsByPrimaryKey(obj.getVoce_iva()))
		return true;
	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (12/14/2001 2:49:15 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
 */
public IDocumentoAmministrativoBulk getDocumentoAmministrativo() {
	return documentoAmministrativo;
}
/**
 * Insert the method's description here.
 * Creation date: (9/13/2001 12:42:53 PM)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getTotale_imponibile() {
	return totale_imponibile;
}
/**
 * Insert the method's description here.
 * Creation date: (9/13/2001 12:42:53 PM)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getTotale_iva() {
	return totale_iva;
}
/**
 * Insert the method's description here.
 * Creation date: (9/13/2001 12:42:53 PM)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getTotale_prezzo() {
	return totale_prezzo;
}
/**
 * Insert the method's description here.
 * Creation date: (9/25/2001 12:28:19 PM)
 * @return it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk
 */
public it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk getVoce_iva() {
	return voce_iva;
}
/**
 * Insert the method's description here.
 * Creation date: (9/11/2001 6:40:07 PM)
 * @return java.lang.String
 */
public void init(IDocumentoAmministrativoRigaBulk rigaDocAmm) {

	setDocumentoAmministrativo(rigaDocAmm.getFather());
	setVoce_iva(rigaDocAmm.getVoce_iva());
}
/**
 * Insert the method's description here.
 * Creation date: (12/14/2001 2:49:15 PM)
 * @param newDocumentoAmministrativo it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
 */
public void setDocumentoAmministrativo(IDocumentoAmministrativoBulk newDocumentoAmministrativo) {
	documentoAmministrativo = newDocumentoAmministrativo;
}
/**
 * Insert the method's description here.
 * Creation date: (9/13/2001 12:42:53 PM)
 * @param newTotale_imponibile java.math.BigDecimal
 */
public void setTotale_imponibile(java.math.BigDecimal newTotale_imponibile) {
	totale_imponibile = newTotale_imponibile;
}
/**
 * Insert the method's description here.
 * Creation date: (9/13/2001 12:42:53 PM)
 * @param newTotale_iva java.math.BigDecimal
 */
public void setTotale_iva(java.math.BigDecimal newTotale_iva) {
	totale_iva = newTotale_iva;
}
/**
 * Insert the method's description here.
 * Creation date: (9/13/2001 12:42:53 PM)
 * @param newTotale_prezzo java.math.BigDecimal
 */
public void setTotale_prezzo(java.math.BigDecimal newTotale_prezzo) {
	totale_prezzo = newTotale_prezzo;
}
/**
 * Insert the method's description here.
 * Creation date: (9/25/2001 12:28:19 PM)
 * @param newVoce_iva it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk
 */
public void setVoce_iva(it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk newVoce_iva) {
	voce_iva = newVoce_iva;
}
}
