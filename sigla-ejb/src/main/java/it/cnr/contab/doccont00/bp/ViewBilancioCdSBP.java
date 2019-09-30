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

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;

/**
 * Business process che gestisce attività relative al bilancio del Cds.
 */

public class ViewBilancioCdSBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private String tipoGestione; //puo valere E per entrate o S per spese
	private String cd_cds; 
	private final SimpleDetailCRUDController vociBilancio = new SimpleDetailCRUDController("VociBilancio",V_sit_bil_cds_cnrBulk.class, "vociBilancioColl",this);	
public ViewBilancioCdSBP() {}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param function	La funzione con cui è stato creato il BusinessProcess
 * @param cd_cds	
 */
public ViewBilancioCdSBP(String function, String cd_cds ) {
	super(function);
	setCd_cds( cd_cds);}
/**
 * Metodo utilizzato per creare una toolbar applicativa personalizzata.
 * @return null In questo caso la toolbar è vuota
 */
protected it.cnr.jada.util.jsp.Button[] createToolbar() 
{
	return null;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'cd_cds'
 *
 * @return Il valore della proprietà 'cd_cds'
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'tipoGestione'
 *
 * @return Il valore della proprietà 'tipoGestione'
 */
public java.lang.String getTipoGestione() {
	return tipoGestione;
}
/**
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getVociBilancio() {
	return vociBilancio;
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		
		setTipoGestione(config.getInitParameter("tipoGestione"));
	
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
	super.init(config,context);
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'cd_cds'
 *
 * @param newCd_cds	Il valore da assegnare a 'cd_cds'
 */
public void setCd_cds(java.lang.String newCd_cds) {
	cd_cds = newCd_cds;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'tipoGestione'
 *
 * @param newTipoGestione	Il valore da assegnare a 'tipoGestione'
 */
public void setTipoGestione(java.lang.String newTipoGestione) {
	tipoGestione = newTipoGestione;
}
}
