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

import it.cnr.jada.bulk.*;

/**
 * Insert the type's description here.
 * Creation date: (02/12/2003 12.57.12)
 * @author: Roberto Peli
 */
public abstract class Quadri_liq_annualeVBulk extends Liquidazione_iva_annualeVBulk {

	private BulkList elencoSecondario = new BulkList();

	private java.math.BigDecimal im_tot_iva = null;
	private java.math.BigDecimal im_totale = null;
public int addToElencoSecondario(Vp_liquid_iva_annualeBulk aLiqAnnuale) {	

	getElencoSecondario().add(aLiqAnnuale);
	return getElenco().size()-1;
}
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 13.09.07)
 * @return it.cnr.jada.bulk.BulkList
 */
public it.cnr.jada.bulk.BulkList getElencoSecondario() {
	return elencoSecondario;
}
/**
 * Insert the method's description here.
 * Creation date: (03/12/2003 11.22.46)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getIm_tot_iva() {
	return im_tot_iva;
}
/**
 * Insert the method's description here.
 * Creation date: (03/12/2003 11.22.46)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getIm_totale() {
	return im_totale;
}
public Vp_liquid_iva_annualeBulk removeFromElencoSecondario( int indiceDiLinea ) {

	return (Vp_liquid_iva_annualeBulk)getElencoSecondario().remove(indiceDiLinea);
}
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 13.09.07)
 * @param newElencoSecondario it.cnr.jada.bulk.BulkList
 */
public void setElencoSecondario(it.cnr.jada.bulk.BulkList newElencoSecondario) {
	elencoSecondario = newElencoSecondario;
}
/**
 * Insert the method's description here.
 * Creation date: (03/12/2003 11.22.46)
 * @param newIm_tot_iva java.math.BigDecimal
 */
public void setIm_tot_iva(java.math.BigDecimal newIm_tot_iva) {
	im_tot_iva = newIm_tot_iva;
}
/**
 * Insert the method's description here.
 * Creation date: (03/12/2003 11.22.46)
 * @param newIm_totale java.math.BigDecimal
 */
public void setIm_totale(java.math.BigDecimal newIm_totale) {
	im_totale = newIm_totale;
}
}
