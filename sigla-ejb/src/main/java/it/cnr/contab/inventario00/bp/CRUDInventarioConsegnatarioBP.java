package it.cnr.contab.inventario00.bp;

/**
 * Un BusinessProcess controller che permette di effettuare operazioni di CRUD su istanze di 
 *	Inventario_consegnatarioBulk per la gestione dell'assegnazione del Terzo Consegnatario
 *	dell'Inventario.
**/
import it.cnr.contab.inventario00.tabrif.bulk.Inventario_consegnatarioBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;

public class CRUDInventarioConsegnatarioBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	
/**
 * CRUDInventarioConsegnatarioBP constructor comment.
 */
public CRUDInventarioConsegnatarioBP() {
	super();
}
/**
 * CRUDInventarioConsegnatarioBP constructor comment.
 * @param function java.lang.String
 */
public CRUDInventarioConsegnatarioBP(String function) {
	super(function);
}
/**
 * Nasconde il pulsante di "Elimina".
 *	Il pulsante è nascosto poichè non è possibile cancellare una riga dalla tabella dei Consegnatari.
 *
 * @return <code>boolean</code>
**/ 
public boolean isDeleteButtonHidden() {

	Inventario_consegnatarioBulk inv_cons = (Inventario_consegnatarioBulk)getModel();

	return !(isEditing() && inv_cons.isLast());
}
/**
 * Abilita il pulsante di "Salva".
 *	Il pulsante è abilitato solo se si sta creando una nuova riga nella tabella dei Consegnatari.
 *	Restituisce TRUE se il BusinessProcess è <code>isInserting</code>
 *
 * @return <code>boolean</code>
**/ 
public boolean isSaveButtonEnabled() {

	return isInserting();
}
}
