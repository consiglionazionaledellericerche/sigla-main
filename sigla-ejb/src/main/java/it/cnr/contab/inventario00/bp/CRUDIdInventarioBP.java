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

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.jada.util.action.*;

/**
 * Insert the type's description here.
 * Creation date: (16/11/2001 13.10.51)
 * @author: Roberto Fantino
 */
public class CRUDIdInventarioBP extends SimpleCRUDBP {
	private final SimpleDetailCRUDController uo  = new SimpleDetailCRUDController("uo",Unita_organizzativaBulk.class,"uo",this);
	private final SimpleDetailCRUDController uoDisponibili = new SimpleDetailCRUDController("uoDisponibili",Unita_organizzativaBulk.class,"uoDisponibili",this);
/**
 * CRUDIdInventarioBP constructor comment.
 */
public CRUDIdInventarioBP() {
	super();
}
/**
 * CRUDIdInventarioBP constructor comment.
 * @param function java.lang.String
 */
public CRUDIdInventarioBP(String function) {
	super(function);
}

public final SimpleDetailCRUDController getUo() {
	return uo;
}

public final it.cnr.jada.util.action.SimpleDetailCRUDController getUoDisponibili() {
	return uoDisponibili;
}
/**
 * Abilita il pulsante di ASSOCIA solo se lo stato del processo è EDIT.
 *
 * @return <code>boolean</code> lo stato del pulsante
 */
public boolean isBottoneAssociaEnabled() {

	// Abilito il bottone "ASSOCIA" se sono in modifica
	if(this.isEditable()){
		return (true);
	}
	return (false);	// disabilito
}
/**
 * Abilita il pulsante di "IMPOSTA UO RESPONSABILE"
 *
 * @return <code>boolean</code> lo stato del pulsante
 */
public boolean isBottoneImpostaUoEnabled() {

	if(this.isEditable() && ((Id_inventarioBulk)this.getModel()).getUo() != null &&
		((Id_inventarioBulk)this.getModel()).getUo().size()!=0){
			return (true); //abilitato
	}
	return (false);	// disabilitato
}
/**
 * Abilita il pulsante di RIMUOVI solo se lo stato del processo è EDIT.
 *
 * @return <code>boolean</code> lo stato del pulsante
 */
public boolean isBottoneRimuoviEnabled() {

	// Abilito il bottone se sono in modifica
	if(this.isEditable() && ((Id_inventarioBulk)this.getModel()).getUo() != null &&
		((Id_inventarioBulk)this.getModel()).getUo().size()!=0){
			return (true);
	}
	return (false);	// disabilito
}
/**
 * Disabilita il pulsante di "Elimina" del Form
 *
 * @return <code>boolean</code> FALSE
 */
public boolean isDeleteButtonEnabled() {
	return false;
}
}
