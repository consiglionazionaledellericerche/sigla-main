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

/**
 * Insert the type's description here.
 * Creation date: (3/26/2002 11:53:38 AM)
 * @author: Roberto Peli
 */
public interface IGenericSearchDocAmmBP {
/**
 * Insert the method's description here.
 * Creation date: (3/26/2002 11:56:27 AM)
 * @return java.lang.String
 */
String getColumnsetForGenericSearch();
/**
 * Insert the method's description here.
 * Creation date: (3/26/2002 11:56:27 AM)
 * @return java.lang.String
 */
String getPropertyForGenericSearch();
/**
 * Insert the method's description here.
 * Creation date: (3/26/2002 11:56:27 AM)
 * @return java.lang.String
 */
it.cnr.jada.ejb.CRUDComponentSession initializeModelForGenericSearch(
	it.cnr.jada.util.action.BulkBP bp,
	it.cnr.jada.action.ActionContext context)
	throws it.cnr.jada.action.BusinessProcessException;
}
