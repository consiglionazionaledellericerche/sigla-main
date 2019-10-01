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

package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.*;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 11:10:31 AM)
 * @author: Ardire Alfonso
 */
public class Riepilogativi_iva_centro_definitivoVBulk extends Riepilogativi_iva_centroVBulk {
	private Boolean intero_anno;
	public Boolean getIntero_anno() {
			return intero_anno;
		}
	public void setIntero_anno(Boolean interoAnno) {
		intero_anno = interoAnno;
	}
/**
 * Filtro_ricerca_obbligazioniVBulk constructor comment.
 */
public Riepilogativi_iva_centro_definitivoVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @param newFl_fornitore java.lang.Boolean
 */
public it.cnr.jada.bulk.OggettoBulk initializeForSearch(
	it.cnr.jada.util.action.BulkBP bp,
	it.cnr.jada.action.ActionContext context) {

	Riepilogativi_iva_centroVBulk bulk = (Riepilogativi_iva_centroVBulk)super.initializeForSearch(bp, context);
	
	bulk.setTipo_report(DEFINITIVO);
	
	return bulk;
}
/**
 * Insert the method's description here.
 * Creation date: (2/24/2003 10:35:44 AM)
 * @return java.lang.Integer
 */
public boolean isPageNumberRequired() {
	return true;
}
public void validate() throws ValidationException {
	// TODO Auto-generated method stub
	
	if (getTipo_sezionale() == null && !isStarSezionali())
		throw new ValidationException("Selezionare un tipo sezionale");
	if (getMese() == null && getData_da() == null && getData_a() == null && !isRistampa())
		throw new ValidationException("Selezionare un mese o l'intero anno");
	if (getPageNumber() == null)
		throw new ValidationException("Specificare il numero di pagina da cui iniziare la stampa!");
	if (getPageNumber().intValue() < 1)
		throw new ValidationException("Il numero di pagina da cui iniziare la stampa deve essere maggiore o uguale a 1!");
}
}
