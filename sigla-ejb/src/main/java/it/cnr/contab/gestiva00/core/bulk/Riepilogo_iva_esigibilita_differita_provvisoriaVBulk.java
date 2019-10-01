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
import it.cnr.jada.bulk.ValidationException;

/**
 * Insert the type's description here.
 * Creation date: (2/25/2003 9:29:42 AM)
 * @author: Roberto Peli
 */
public class Riepilogo_iva_esigibilita_differita_provvisoriaVBulk extends Riepilogo_iva_esigibilita_differitaVBulk {
/**
 * Insert the method's description here.
 * Creation date: (2/25/2003 9:45:47 AM)
 */
private Boolean intero_anno;
public Riepilogo_iva_esigibilita_differita_provvisoriaVBulk() {}
public it.cnr.jada.bulk.OggettoBulk initializeForSearch(
	it.cnr.jada.util.action.BulkBP bp,
	it.cnr.jada.action.ActionContext context) {

	Riepilogo_iva_esigibilita_differita_provvisoriaVBulk bulk = (Riepilogo_iva_esigibilita_differita_provvisoriaVBulk)super.initializeForSearch(bp, context);
	setSezionaliFlag(null);
	bulk.setTipo_report(PROVVISORIO);
	
	return bulk;
}
public boolean isPageNumberRequired() {
	return false;
}
public boolean isRistampabile() {
	return false;
}
/**
 * @return
 */
public Boolean getIntero_anno() {
	return intero_anno;
}

/**
 * @param string
 */
public void setIntero_anno(Boolean s) {
	intero_anno = s;
}
public void validate() throws ValidationException {

//	super.validate();

	if (getMese() == null && getData_da() == null && getData_a() == null)
		throw new ValidationException("Selezionare un mese o l'intero anno");
	if(getSezionaliFlag()==null)
		throw new ValidationException("Selezionare tipo sezionale");
}
public BulkList getPrintSpoolerParam() {
	BulkList printSpoolerParam = new BulkList();
	Print_spooler_paramBulk param;
	param = new Print_spooler_paramBulk();
	param.setNomeParam("Id_report");
	param.setValoreParam(getId_report().toString());
	param.setParamType("java.lang.Integer");
	printSpoolerParam.add(param);
	
	param = new Print_spooler_paramBulk();
	param.setNomeParam("Pagina");
	param.setValoreParam(getPageNumber().toString());
	param.setParamType("java.lang.String");
	printSpoolerParam.add(param);

	return printSpoolerParam;

}
}
