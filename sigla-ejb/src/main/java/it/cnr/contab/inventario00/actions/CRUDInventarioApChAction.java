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

package it.cnr.contab.inventario00.actions;

/**
 * Insert the type's description here.
 * Creation date: (03/12/2001 12.13.02)
 * @author: CNRADM
 */
import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.contab.inventario00.bp.*;

import it.cnr.contab.inventario00.docs.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;
 
public class CRUDInventarioApChAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDInventarioApChAction constructor comment.
 */
public CRUDInventarioApChAction() {
	super();
}
/**
 * Richiamato nel caso che la data di inizio validità del Consegnatario non sia valida.
 *	Il controllo viene effettuato al momento del salvataggio e verifica che la data di apertura 
 *	dell'Inventario NON sia antecedente alla data dui inizio validità del primo Consegnatario
 *	specificato per l'Inventario.
 */

public Forward doOnCheckDataConsegnatarioFailed(
    ActionContext context,
    int option) {

    if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) {
        CRUDInventarioApChBP bp = (CRUDInventarioApChBP) getBusinessProcess(context);
        try {           
            OptionRequestParameter userConfirmation = new OptionRequestParameter();
            userConfirmation.setCheckDataConsegnatarioRequired(Boolean.FALSE);
            bp.setUserConfirm(userConfirmation);
             
            doSalva(context);
            
            Inventario_ap_chBulk invApCh = (Inventario_ap_chBulk)bp.getModel();
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }
    return context.findDefaultForward();
}
}
