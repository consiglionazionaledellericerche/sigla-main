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
