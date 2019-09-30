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
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.util.*;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 11:10:31 AM)
 * @author: Ardire Alfonso
 */
public class Riepilogativi_definitivi_ivaVBulk extends Riepilogativi_ivaVBulk {

	private Integer pageNumber = null;	
/**
 * Filtro_ricerca_obbligazioniVBulk constructor comment.
 */
public Riepilogativi_definitivi_ivaVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (2/24/2003 10:33:16 AM)
 * @return java.lang.Integer
 */
public java.lang.Integer getPageNumber() {
	return pageNumber;
}
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:26:09 PM)
 */
public java.lang.String getReportName() {
	return "/gestiva/gestiva/registro_riepilogativo_uo.jasper";
}
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:26:09 PM)
 */
public java.util.Vector getReportParameters() {

	java.util.Vector params = new java.util.Vector();
	if (getId_report() != null) {
		params.add(getId_report().toString());
		params.add(getPageNumber().toString());
	}
	return params;

}
/**
 * Insert the method's description here.
 * Creation date: (30/08/2002 13.48.56)
 * @param newSezionaliFlag java.lang.String
 */
public java.util.Dictionary getSezionaliFlags() {

	OrderedHashtable dic = (OrderedHashtable)super.getSezionaliFlags();
	dic.remove(SEZIONALI_FLAGS_SEZ);
	return dic;
}
/**
 * Insert the method's description here.
 * Creation date: (12/10/2002 2:31:34 PM)
 * @author: Alfonso Ardire
 * @param newTipo_report java.lang.String
 */
public java.lang.String getTipo_documento_stampato() {

	return getTipoSezionaleFlag();
}
public String getTipo_report_stampato() {
	
	if (SEZIONALI_FLAGS_ACQ.equalsIgnoreCase(getSezionaliFlag()))
		return "REGISTRO_RIEPILOGATIVO_ACQ";
	else if (SEZIONALI_FLAGS_VEN.equalsIgnoreCase(getSezionaliFlag()))
		return "REGISTRO_RIEPILOGATIVO_VEN";

	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @param newFl_fornitore java.lang.Boolean
 */
public it.cnr.jada.bulk.OggettoBulk initializeForSearch(
	it.cnr.jada.util.action.BulkBP bp,
	it.cnr.jada.action.ActionContext context) {

	Riepilogativi_definitivi_ivaVBulk bulk = (Riepilogativi_definitivi_ivaVBulk)super.initializeForSearch(bp, context);
	
	bulk.setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
	bulk.setCd_unita_organizzativa(unita_organizzativa.getCd_unita_organizzativa());
	
	
	bulk.setCd_cds(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
	bulk.setTipo_report(DEFINITIVO);
	bulk.setTipo_stampa(TIPO_STAMPA_RIEPILOGATIVI);

	bulk.setSezionaliFlag(SEZIONALI_FLAGS_ACQ);
	bulk.setPageNumber(new Integer(1));
	
	return bulk;
}
/**
 * Insert the method's description here.
 * Creation date: (2/24/2003 10:33:16 AM)
 * @return java.lang.Integer
 */
public boolean isPageNumberRequired() {
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (2/24/2003 10:33:16 AM)
 * @param newPageNumber java.lang.Integer
 */
public void setPageNumber(java.lang.Integer newPageNumber) {

	pageNumber = newPageNumber;
}
public BulkList getPrintSpoolerParam() {
	BulkList printSpoolerParam = new BulkList();
	Print_spooler_paramBulk param;
	param = new Print_spooler_paramBulk();
	param.setNomeParam("idReport");
	param.setValoreParam(getId_report().toString());
	param.setParamType("java.lang.Integer");
	printSpoolerParam.add(param);
	
	param = new Print_spooler_paramBulk();
	param.setNomeParam("aPagIniziale");
	param.setValoreParam(getPageNumber().toString());
	param.setParamType("java.lang.String");
	printSpoolerParam.add(param);

	return printSpoolerParam;

}
}
