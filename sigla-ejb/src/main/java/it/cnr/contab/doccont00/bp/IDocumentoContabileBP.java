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

package it.cnr.contab.doccont00.bp;

public interface IDocumentoContabileBP {
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @param setSavePoint	
 * @return 
 * @throws BusinessProcessException	
 */
public abstract it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession getVirtualSession (	it.cnr.jada.action.ActionContext context,	boolean setSavePoint) throws it.cnr.jada.action.BusinessProcessException;
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'deleting'
 *
 * @return Il valore della proprietà 'deleting'
 */
public boolean isDeleting();
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'deleting'
 *
 * @param newDeleting	Il valore da assegnare a 'deleting'
 */
public void setDeleting(boolean newDeleting);
}
