package it.cnr.contab.coepcoan00.bp;

import it.cnr.contab.coepcoan00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;
/**
 * Business Process che gestisce le attività di CRUD per l'oggetto Scrittura_partitadoppiaBulk
 */

public class CRUDScritturaPDoppiaBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private final SimpleDetailCRUDController movimentiDare = new SimpleDetailCRUDController("MovimentiDare",it.cnr.contab.coepcoan00.core.bulk.Movimento_cogeBulk.class,"movimentiDareColl",this);
	private final SimpleDetailCRUDController movimentiAvere = new SimpleDetailCRUDController("MovimentiAvere",it.cnr.contab.coepcoan00.core.bulk.Movimento_cogeBulk.class,"movimentiAvereColl",this);	

public CRUDScritturaPDoppiaBP() {
	super();
	setTab("tab", "tabScrittura");
}

public CRUDScritturaPDoppiaBP(String function) {
	super(function);
	setTab("tab", "tabScrittura");	
}
/**
 * restituisce il Controller che gestisce la lista dei movimenti avere
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getMovimentiAvere() {
	return movimentiAvere;
}
/**
 * restituisce il Controller che gestisce la lista dei movimenti dare
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getMovimentiDare() {
	return movimentiDare;
}
/* Stabilisce quando il bottone Elimina della Gestione Scrittura Partita Doppia deve essere
   abilitato */
public boolean isDeleteButtonEnabled() {
	return super.isDeleteButtonEnabled() &&
			Scrittura_partita_doppiaBulk.ORIGINE_CAUSALE.equals(((Scrittura_partita_doppiaBulk)getModel()).getOrigine_scrittura());
}
/* Stabilisce quando il bottone Salva della Gestione Scrittura Partita Doppia deve essere
   abilitato */
public boolean isSaveButtonEnabled() {
	return isEditable() && isInserting() && 
	Scrittura_partita_doppiaBulk.ORIGINE_CAUSALE.equals(((Scrittura_partita_doppiaBulk)getModel()).getOrigine_scrittura());
}
/* Metodo per riportare il fuoco sul tab iniziale */
protected void resetTabs(ActionContext context) {
	setTab( "tab", "tabScrittura");
}
}
