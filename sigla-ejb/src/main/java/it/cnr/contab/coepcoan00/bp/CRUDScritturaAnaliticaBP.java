package it.cnr.contab.coepcoan00.bp;

import it.cnr.contab.coepcoan00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;
/**
 * Business Process che gestisce le attività di CRUD per l'entita' Scrittura Analitica
 */
public class CRUDScritturaAnaliticaBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private final SimpleDetailCRUDController movimenti = new SimpleDetailCRUDController("Movimenti",it.cnr.contab.coepcoan00.core.bulk.Movimento_coanBulk.class,"movimentiColl",this);
/**
 * CRUDScritturaAnaliticaBP constructor comment.
 */
public CRUDScritturaAnaliticaBP() {
	super();
	setTab("tab", "tabScritturaAnalitica");
}
/**
 * CRUDScritturaAnaliticaBP constructor comment.
 * @param function java.lang.String
 */
public CRUDScritturaAnaliticaBP(String function) {
	super(function);
	setTab("tab", "tabScritturaAnalitica");	
}
/**
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getMovimenti() {
	return movimenti;
}
/* Stabilisce quando il bottone Elimina della Gestione Scrittura Analitica deve essere
   abilitato */
public boolean isDeleteButtonEnabled() {
	return super.isDeleteButtonEnabled() &&
			Scrittura_analiticaBulk.ORIGINE_CAUSALE.equals(((Scrittura_analiticaBulk)getModel()).getOrigine_scrittura());
}
/* Stabilisce quando il bottone Salva della Gestione Scrittura Analitica deve essere
   abilitato */
public boolean isSaveButtonEnabled() {
	return isEditable() && isInserting() && 
	Scrittura_analiticaBulk.ORIGINE_CAUSALE.equals(((Scrittura_analiticaBulk)getModel()).getOrigine_scrittura());
}
/* Metodo per riportare il fuoco sul tab iniziale */
protected void resetTabs(ActionContext context) {
	setTab( "tab", "tabScritturaAnalitica");
}
}
