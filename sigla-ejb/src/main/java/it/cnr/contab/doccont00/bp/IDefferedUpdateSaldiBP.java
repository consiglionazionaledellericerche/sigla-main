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

/**
 * <!-- @TODO: da completare -->
 */

public interface IDefferedUpdateSaldiBP {
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'defferedUpdateSaldiBulk'
 *
 * @return Il valore della proprietà 'defferedUpdateSaldiBulk'
 */
public it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi getDefferedUpdateSaldiBulk();
/**
 * Insert the method's description here.
 * Creation date: (5/22/2002 10:15:21 AM)
 */
public IDefferedUpdateSaldiBP getDefferedUpdateSaldiParentBP();
}
