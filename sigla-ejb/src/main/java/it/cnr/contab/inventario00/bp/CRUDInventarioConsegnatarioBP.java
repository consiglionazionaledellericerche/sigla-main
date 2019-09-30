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
