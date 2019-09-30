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

package it.cnr.contab.missioni00.bp;

import java.util.Vector;
import it.cnr.contab.missioni00.docs.bulk.Missione_dettaglioBulk;

/**
 * Insert the type's description here.
 * Creation date: (07/02/2002 14.08.27)
 * @author: Paola sala
 */
public class CRUDMissione_spesaController extends it.cnr.jada.util.action.SimpleDetailCRUDController 
{
	private boolean editingSpesa = false;			
/**
 * CRUDMissione_dettagliController constructor comment.
 * @param name java.lang.String
 * @param modelClass java.lang.Class
 * @param listPropertyName java.lang.String
 * @param parent it.cnr.jada.util.action.FormController
 */
public CRUDMissione_spesaController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
	super(name, modelClass, listPropertyName, parent);
}
public int addDetail(it.cnr.jada.bulk.OggettoBulk model) throws it.cnr.jada.action.BusinessProcessException 
{
	Missione_dettaglioBulk spesa = (Missione_dettaglioBulk) model;
	
	// Inizio modalita' inserimento dettaglio di spesa
	editingSpesa = true;
	
	return (super.addDetail(spesa));
}
//
//	Se la spesa ha una valuta uguale a quella di default disabilito il cambio
//
public boolean isCambioReadOnly() 
{
	if(!isEditingSpesa())
		return true;

	Missione_dettaglioBulk aSpesa = (Missione_dettaglioBulk)getModel();
	
	if(	aSpesa!=null &&
		aSpesa.getCd_divisa_spesa() != null &&
		aSpesa.getCd_divisa_spesa().compareTo(it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_EURO)==0)
		return true;
		
	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (14/03/2002 15.03.31)
 * @return boolean
 */
public boolean isEditingSpesa() {
	return editingSpesa;
}
/**
 * Insert the method's description here.
 * Creation date: (14/03/2002 15.03.31)
 * @param newEditingSpesa boolean
 */
public void setEditingSpesa(boolean newEditingSpesa) {
	editingSpesa = newEditingSpesa;
}
}
