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

import it.cnr.jada.bulk.BulkList;

/**
 * Insert the type's description here.
 * Creation date: (12/6/2002 11:15:55 AM)
 * @author: CNRADM
 */
public interface IPrintable {
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 11:16:45 AM)
 */
java.math.BigDecimal getId_report();
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 11:16:45 AM)
 */
String getReportName();
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 11:16:45 AM)
 */
java.util.Vector getReportParameters();
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 11:16:45 AM)
 */
boolean isRistampa();
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 11:16:45 AM)
 */
boolean isRistampabile();
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 11:16:45 AM)
 */
void setId_report(java.math.BigDecimal new_id_report);

BulkList getPrintSpoolerParam();
}
