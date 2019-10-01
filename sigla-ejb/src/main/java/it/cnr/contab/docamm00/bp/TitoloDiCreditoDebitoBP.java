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

package it.cnr.contab.docamm00.bp;

import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;

/**
 * Insert the type's description here.
 * Creation date: (5/2/2002 4:37:26 PM)
 * @author: CNRADM
 */
public interface TitoloDiCreditoDebitoBP {
/**
 * Insert the method's description here.
 * Creation date: (5/2/2002 4:37:57 PM)
 */
 
void addToDocumentiContabiliModificati(IScadenzaDocumentoContabileBulk scadenza);
/**
 * Insert the method's description here.
 * Creation date: (5/2/2002 4:37:57 PM)
 */
 
it.cnr.jada.bulk.PrimaryKeyHashMap getDocumentiContabiliModificati();
/**
 * Insert the method's description here.
 * Creation date: (5/2/2002 4:37:57 PM)
 */
 
boolean isDocumentoContabileModificato(IScadenzaDocumentoContabileBulk scadenza);
/**
 * Insert the method's description here.
 * Creation date: (5/2/2002 4:37:57 PM)
 */
 
void removeFromDocumentiContabiliModificati(IScadenzaDocumentoContabileBulk scadenza);
/**
 * Insert the method's description here.
 * Creation date: (5/2/2002 4:37:57 PM)
 */
 
void setDocumentiContabiliModificati(it.cnr.jada.bulk.PrimaryKeyHashMap aMap);
}
