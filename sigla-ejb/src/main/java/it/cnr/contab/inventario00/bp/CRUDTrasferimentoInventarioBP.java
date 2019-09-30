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

package it.cnr.contab.inventario00.bp;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario00.docs.bulk.Trasferimento_inventarioBulk;
import it.cnr.contab.inventario01.bp.CRUDScaricoInventarioBP;
import it.cnr.contab.inventario01.ejb.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
/**
 * Insert the type's description here.
 * Creation date: (28/01/2003 15.30.10)
 * @author: Gennaro Borriello
 */
public class CRUDTrasferimentoInventarioBP extends CRUDScaricoInventarioBP {

	
	// Indica se l'operazione di trasferimento è verso un altro Inventario, (extraInv),
	//	o all'interno dello stesso Inventario, (intraInv).
	private final String EXTRA_INVENTARIO = "EXTRA";
	private final String INTRA_INVENTARIO = "INTRA";
	
	private boolean trasferimentoExtraInv;
	private boolean trasferimentoIntraInv;
/**
 * CRUDTrasferimentoInventarioBP constructor comment.
 */
public CRUDTrasferimentoInventarioBP() {
	this("Tr");
}
/**
 * CRUDTrasferimentoInventarioBP constructor comment.
 * @param function java.lang.String
 */
public CRUDTrasferimentoInventarioBP(String function) {
	super(function+"Tr");
	setTab("tab","tabTrasferimentoInventarioTestata");
}
/** 
 * Trasferimento intra-inventario - Ricerca nuovo bene padre
 *	Controlla che il bene selezionato non sia già stato indicato come bene da trasferire
 * 
 * @param context la <code>ActionContext</code> che ha generato la richiesta.
 * @param nuovo_bene_padre il <code>Inventario_beniBulk</code> nuovo bene padre
**/
public void checkNuovoBenePadreAlreadySelected(ActionContext context, Inventario_beniBulk nuovo_bene_padre) throws BusinessProcessException {


	try {
		((BuonoCaricoScaricoComponentSession)createComponentSession()).checkNuovoBenePadreAlreadySelected(
			context.getUserContext(),
			(Trasferimento_inventarioBulk)getModel(),
			nuovo_bene_padre);
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch (BusinessProcessException e){
		throw handleException(e);
	}	
}
/**
 * Imposta come attivi i tab di default.
 *
 * @param context <code>ActionContext</code>
 */
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	String type = config.getInitParameter("TIPO_TRASFERIMENTO");

	Trasferimento_inventarioBulk trasf = (Trasferimento_inventarioBulk)getModel();

	if (type != null && type.equals(EXTRA_INVENTARIO)){		
		setTrasferimentoExtraInv(true);
		//trasf.setTrasferimentoExtraInv(true);
	} else if (type != null && type.equals(INTRA_INVENTARIO)){
		setTrasferimentoIntraInv(true);
		//trasf.setTrasferimentoIntraInv(true);
	}
		
	super.init(config,context);
	resetTabs();
}
public OggettoBulk initializeModelForInsert(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {

	if (bulk instanceof Trasferimento_inventarioBulk){
		Trasferimento_inventarioBulk buonoT = (Trasferimento_inventarioBulk)bulk;

		if (bulk != null){
			if (isTrasferimentoExtraInv()){
				buonoT.setTrasferimentoExtraInv(true);
			} else if (isTrasferimentoIntraInv()){
				buonoT.setTrasferimentoIntraInv(true);
			}
		}

		return super.initializeModelForInsert(context, buonoT);
	}

	return super.initializeModelForInsert(context, bulk);
}
/**
 *	Disabilito il bottone di ricerca libera.
 */

public boolean isFreeSearchButtonHidden() {
	
	return true;
}
/**
 *	Disabilito il bottone di ricerca.
 */
public boolean isSearchButtonHidden() {
	return true;
}
/**
 * Disabilito il pulsante Salva, nel caso che ci si trovi in Modifica di un 
 *	Buono di Scarico.
 * 
 * @return <code>boolean</code> lo stato del pulsante.
**/
public boolean isSaveButtonEnabled() {
	return isInserting();
}
/**
 * 
 * @return <code>boolean</code> lo stato del pulsante.
 */
public boolean isDeleteButtonHidden() {
	
	return true;
}

/**
 * Insert the method's description here.
 * Creation date: (29/07/2004 15.15.32)
 * @return boolean
 */
public boolean isTrasferimentoExtraInv() {
	return trasferimentoExtraInv;
}
/**
 * Insert the method's description here.
 * Creation date: (29/07/2004 15.15.32)
 * @return boolean
 */
public boolean isTrasferimentoIntraInv() {
	return trasferimentoIntraInv;
}
/**
 * Imposta come attivi i tab di default.
 *
 * @param context <code>ActionContext</code>
 */

public void resetTabs() {
	setTab("tab","tabTrasferimentoInventarioTestata");
}
/**
 * Imposta la Selezione fatta sul Controller dei Beni che si stanno scaricando.
 *	Invoca:
 *		- <code>BuonoScaricoComponent.modificaBeniScaricatiPerAssocia</code>, se il Buono di Scarico è generato da una Fattura Attiva;
 *		- <code>BuonoScaricoComponent.modificaBeniScaricati</code>, negli altri casi.
 *
 * @param context la <code>ActionContext</code> che ha generato la richiesta
 * @param beni <code>OggettoBulk[]</code> i beni selezionati dall'utente.
 * @param oldSelection la <code>BitSet</code> selezione precedente.
 * @param newSelection la <code>BitSet</code> selezione attuale.
 *
 * @return currentSelection la <code>BitSet</code> selezione attuale.
**/
public java.util.BitSet setSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet oldSelection, java.util.BitSet newSelection) throws it.cnr.jada.action.BusinessProcessException {
	try {
		// Sono nel trasferimento
		
		((BuonoCaricoScaricoComponentSession)createComponentSession()).modificaBeniScaricati(
			context.getUserContext(),
			(Trasferimento_inventarioBulk)getModel(),
			bulks,
			oldSelection,
			newSelection);
		
		return newSelection;
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (29/07/2004 15.15.32)
 * @param newTrasferimentoExtraInv boolean
 */
public void setTrasferimentoExtraInv(boolean newTrasferimentoExtraInv) {
	trasferimentoExtraInv = newTrasferimentoExtraInv;
}
/**
 * Insert the method's description here.
 * Creation date: (29/07/2004 15.15.32)
 * @param newTrasferimentoIntraInv boolean
 */
public void setTrasferimentoIntraInv(boolean newTrasferimentoIntraInv) {
	trasferimentoIntraInv = newTrasferimentoIntraInv;
}
public String getLabelData_registrazione(){
	return "Data Trasferimento";
}
}
