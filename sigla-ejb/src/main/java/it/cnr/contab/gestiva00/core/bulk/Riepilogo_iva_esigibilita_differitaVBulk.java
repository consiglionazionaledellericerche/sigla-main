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

import it.cnr.contab.gestiva00.bp.RiepilogoIvaEsigibilitaDifferitaBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.util.*;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 11:10:31 AM)
 * @author: Ardire Alfonso
 */
public abstract class Riepilogo_iva_esigibilita_differitaVBulk extends Riepilogativi_ivaVBulk {

	private Integer pageNumber = null;
/**
 * Filtro_ricerca_obbligazioniVBulk constructor comment.
 */
public Riepilogo_iva_esigibilita_differitaVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (2/24/2003 10:47:54 AM)
 * @return java.lang.Integer
 */
public java.lang.Integer getPageNumber() {
	return pageNumber;
}
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:26:20 PM)
 */
public java.lang.String getReportName() {
	return "/gestiva/gestiva/registro_iva_differita.jasper";
}
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:26:20 PM)
 */
public java.util.Vector getReportParameters() {

	java.util.Vector params = new java.util.Vector();
	if (getId_report() != null) {
		params.add(getId_report().toString());
		params.add(getPageNumber().toString());
	}
	return params;
}
public String getTipo_report_stampato() {
	
	return "REGISTRO_ESIGIBILITA_IVA";
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @param newFl_fornitore java.lang.Boolean
 */
public it.cnr.jada.bulk.OggettoBulk initializeForSearch(
	it.cnr.jada.util.action.BulkBP bp,
	it.cnr.jada.action.ActionContext context) {

	Riepilogo_iva_esigibilita_differitaVBulk bulk = (Riepilogo_iva_esigibilita_differitaVBulk)super.initializeForSearch(bp, context);
	
	bulk.setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	//tipo_sezionale = new Tipo_sezionaleBulk();	
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
	bulk.setCd_unita_organizzativa(unita_organizzativa.getCd_unita_organizzativa());
	
	bulk.setCd_cds(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
	bulk.setTipo_report(DEFINITIVO);
	bulk.setTipo_stampa(TIPO_STAMPA_RIEPILOGATIVI_IVA_DIFFERITA);
	bulk.setPageNumber(new Integer(1));
	
	return bulk;
}
/**
 * Insert the method's description here.
 * Creation date: (2/24/2003 10:47:54 AM)
 * @return java.lang.Integer
 */
public boolean isPageNumberRequired() {
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (2/24/2003 10:47:54 AM)
 * @param newPageNumber java.lang.Integer
 */
public void setPageNumber(java.lang.Integer newPageNumber) {

	pageNumber = newPageNumber;
}
public java.util.Dictionary getSezionaliFlags() {
	OrderedHashtable dic = (OrderedHashtable)((OrderedHashtable)SEZIONALI_FLAG_KEYS).clone();
	dic.remove(SEZIONALI_FLAGS_SEZ);
	return dic;
}
}
