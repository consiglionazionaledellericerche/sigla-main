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
 * Un BusinessProcess controller che permette di effettuare le operazioni di CRUD su istanze 
 *	di Tipo_carico_scaricoBulk, per la gestione dei Tipi di Movimenti di Carico e Scarico 
 *	dell'Inventario
**/
import it.cnr.contab.inventario00.tabrif.bulk.*; 
public class CRUDTipoCaricoScaricoBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	
public CRUDTipoCaricoScaricoBP() {
	super();
}
public CRUDTipoCaricoScaricoBP(String function) {
	super(function);
}
/**
 * Abilita il pulsante di Elimina.
 *	Il pulsante di elimina viene abilitato solo se il Tipo di Movimento non è stato già cancellato.
 *
 * @return <code>boolean</code>
**/
public boolean isDeleteButtonEnabled() {
	if (!isEditable())
		return false;
		
	return ((Tipo_carico_scaricoBulk)getModel()).isCancellabile();
}
/** 
 * Restituisce TRUE se il Tipo di Movimento è di CARICO.
 *
 * @return <code>boolean</code>
**/
public boolean isMovimentoCarico() {
	
	Tipo_carico_scaricoBulk movimento = (Tipo_carico_scaricoBulk)getModel();
	if (movimento.getTi_documento() == null){
		return false;
	}	
	return movimento.getTi_documento().equals(movimento.TIPO_CARICO);
}
}
