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
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;
/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 11:10:31 AM)
 * @author: Ardire Alfonso
 */
public class Gestione_registri_ivaVBulk 
	extends Stampa_registri_ivaVBulk {

	private Unita_organizzativaBulk unita_organizzativa;
	private java.math.BigDecimal id_report = null;
/**
 * Filtro_ricerca_obbligazioniVBulk constructor comment.
 */
public Gestione_registri_ivaVBulk() {
	super();
}
public java.lang.String getCd_unita_organizzativa() {
	Unita_organizzativaBulk uo = this.getUnita_organizzativa();
	if (uo == null)
		return super.getCd_unita_organizzativa();
	return uo.getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (2/24/2003 10:26:22 AM)
 * @return java.lang.Integer
 */
public java.lang.Integer getPageNumber() {
	return new Integer(1);
}
/**
 * Insert the method's description here.
 * Creation date: (12/10/2002 2:31:34 PM)
 * @author: Alfonso Ardire
 * @param newTipo_report java.lang.String
 */
public java.lang.String getTipo_documento_stampato() {
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (12/10/2002 2:31:34 PM)
 * @author: Alfonso Ardire
 * @param newTipo_report java.lang.String
 */
public java.lang.String getTipo_report_stampato() {
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (17/07/2002 16.24.24)
 * @author: Alfonso Ardire
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
	return unita_organizzativa;
}
/**
 * Insert the method's description here.
 * Creation date: (10/16/2001 11:18:42 AM)
 * @param newFl_fornitore java.lang.Boolean
 */
public OggettoBulk initializeForSearch(
	it.cnr.jada.util.action.BulkBP bp,
	it.cnr.jada.action.ActionContext context) {

	Gestione_registri_ivaVBulk bulk = (Gestione_registri_ivaVBulk)super.initializeForSearch(bp, context);
		
	bulk.setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	//tipo_sezionale = new Tipo_sezionaleBulk();	
	//it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = null;
	//setCd_unita_organizzativa(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa());
	//unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
	bulk.setUnita_organizzativa(new Unita_organizzativaBulk());
	//setCd_cds(unita_organizzativa.getUnita_padre().getCd_unita_organizzativa());
	//setTipo_report(DEFINITIVO);
	//setTipo_stampa(TIPO_STAMPA_RIEPILOGATIVI);
	
	return bulk;
}
/**
 * Insert the method's description here.
 * Creation date: (2/24/2003 10:26:22 AM)
 * @return java.lang.Integer
 */
public boolean isPageNumberRequired() {
	return false;
}
public boolean isROuo() {

	return getUnita_organizzativa() != null &&
			getUnita_organizzativa().getCrudStatus() == OggettoBulk.NORMAL;
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	if (getUnita_organizzativa() == null)
		super.setCd_unita_organizzativa(cd_unita_organizzativa);
	else
		getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
}
/**
 * Insert the method's description here.
 * Creation date: (2/24/2003 10:26:22 AM)
 * @param newPageNumber java.lang.Integer
 */
public void setPageNumber(java.lang.Integer newPageNumber) {}
/**
 * Insert the method's description here.
 * Creation date: (17/07/2002 16.24.24)
 * @author: Alfonso Ardire
 * @param newUnita_organizzativa it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUnita_organizzativa(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_organizzativa) {
	unita_organizzativa = newUnita_organizzativa;
}
public void validate() throws ValidationException {


	if (getUnita_organizzativa()==null || getUnita_organizzativa().getCd_unita_organizzativa()==null)
		throw new ValidationException("Inserire una unità organizzativa");
	 
    super.validate();

}
}
