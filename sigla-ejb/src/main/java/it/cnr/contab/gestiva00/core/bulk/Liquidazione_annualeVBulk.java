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

import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.bulk.BulkList;

/**
 * Insert the type's description here.
 * Creation date: (02/12/2003 12.57.12)
 * @author: Roberto Peli
 */
public class Liquidazione_annualeVBulk extends Liquidazione_iva_annualeVBulk {
	private java.math.BigDecimal importoTotale = null;
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 13.00.09)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getImportoTotale() {
	return importoTotale;
}
public java.lang.String getReportName() {

	return "/gestiva/gestiva/liquid_iva_annuale.jasper";
}
public java.util.Vector getReportParameters() {

	java.util.Vector params = new java.util.Vector();
	if (getId_report() != null) {
		params.add(getId_report().toString());
	}
	return params;
}
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 13.00.09)
 * @param newImportoTotale java.math.BigDecimal
 */
public void setImportoTotale(java.math.BigDecimal newImportoTotale) {
	importoTotale = newImportoTotale;
}
public BulkList getPrintSpoolerParam() {
	BulkList printSpoolerParam = new BulkList();
	Print_spooler_paramBulk param;
	param = new Print_spooler_paramBulk();
	param.setNomeParam("id");
	param.setValoreParam(getId_report().toString());
	param.setParamType("java.lang.Long");
	printSpoolerParam.add(param);
	return printSpoolerParam;
}
}
