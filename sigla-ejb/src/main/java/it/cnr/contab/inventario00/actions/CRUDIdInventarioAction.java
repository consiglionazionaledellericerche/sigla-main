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

import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.contab.inventario00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

/**
 * Insert the type's description here.
 * Creation date: (16/11/2001 13.09.00)
 * @author: Roberto Fantino
 */
public class CRUDIdInventarioAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDIdInventarioAction constructor comment.
 */
public CRUDIdInventarioAction() {
	super();
}
/**
  *   Gestisce l'associazione di una UO all'Inventario su cui si sta lavorando.
  *	Il metodo elimina la UO selezionata dall'utente dalla collezione di UO disponibili e lo 
  *	aggiunge alla collezione delle UO associate.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
public Forward doAssociaUo(ActionContext context) {

	try {
		CRUDIdInventarioBP bp = (CRUDIdInventarioBP)context.getBusinessProcess();
		Id_inventarioBulk inv = (Id_inventarioBulk)bp.getModel();

		fillModel(context);

		for (SelectionIterator i = bp.getUoDisponibili().getSelection().reverseIterator();i.hasNext();) {
			Ass_inventario_uoBulk assInvUo = (Ass_inventario_uoBulk)inv.getAss_inventario_uoDisponibili().remove(i.nextIndex());
			assInvUo.setToBeCreated();
			assInvUo.setUser(context.getUserContext().getUser());
			inv.getAss_inventario_uo().add(assInvUo);
			bp.setDirty(true);
		}
		bp.getUoDisponibili().getSelection().clear();

		return context.findDefaultForward();
	} catch(it.cnr.jada.bulk.FillException e) {
		return handleException(context,e);
	}
}
/**
  *  L'utente ha indicato una UO tra quelle associate, come la responsabile dell'Inventario.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
public Forward doImpostaUoResponsabile(ActionContext context) {
	try {

		CRUDIdInventarioBP bp = (CRUDIdInventarioBP)context.getBusinessProcess();
		Id_inventarioBulk inv = (Id_inventarioBulk)bp.getModel();

		fillModel(context);

		// Non è stata indicata nessuna UO
		if (bp.getUo().getSelection().size() != 1) {
			this.setErrorMessage(context, "Selezionare una unità organizzativa!");
		}else{
			Ass_inventario_uoBulk oldResp = inv.getAssInvUoResp();
			if (oldResp != null){
				oldResp.setFl_responsabile(new Boolean(false));
				oldResp.setToBeUpdated();
			}

			SelectionIterator i = bp.getUo().getSelection().iterator();
			Ass_inventario_uoBulk assInvUoResp = (Ass_inventario_uoBulk)inv.getAss_inventario_uo().get(i.nextIndex());

			inv.setAssInvUoResp(assInvUoResp);
			assInvUoResp.setFl_responsabile(new Boolean(true));
			assInvUoResp.setToBeUpdated();
		}

		bp.setDirty(true);
		bp.getUo().getSelection().clearSelection();
		bp.getUoDisponibili().getSelection().clearSelection();

		return context.findDefaultForward();
	} catch(it.cnr.jada.bulk.FillException e) {
		return handleException(context,e);
	}
}
/**
  *   Gestisce la disassociazione di una UO dall'Inventario su cui si sta lavorando.
  *	Il metodo elimina la UO selezionata dall'utente dalla collezione di UO associate e lo 
  *	aggiunge alla collezione delle UO disponibili.
  *	Se la UO era stata indicata come responsabile dell'Inventario, si provvede a cancellarla
  *	da tale ruolo: in tal modo, all'atto del salvataggio, il sistema non commetterà errori
  *	nel controllare che sia stata specificata una UO responsabile.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
public Forward doRimuoviUo(ActionContext context) {

	try {
		CRUDIdInventarioBP bp = (CRUDIdInventarioBP)context.getBusinessProcess();
		Id_inventarioBulk inv = (Id_inventarioBulk)bp.getModel();

		fillModel(context);

		for (SelectionIterator i = bp.getUo().getSelection().reverseIterator();i.hasNext();) {
			Ass_inventario_uoBulk assInvUo = (Ass_inventario_uoBulk)inv.getAss_inventario_uo().remove(i.nextIndex());
			// La Uo che si sta disassociando era stata indicata come la responsabile dell'Inventario
			if (assInvUo.getFl_responsabile().booleanValue()){
				inv.setAssInvUoResp(null);
				assInvUo.setFl_responsabile(new Boolean(false));
			}
			assInvUo.setToBeDeleted();
			inv.getAss_inventario_uoDisponibili().add(assInvUo);
			bp.setDirty(true);
		}
		bp.getUo().getSelection().clear();

		return context.findDefaultForward();
	} catch(it.cnr.jada.bulk.FillException e) {
		return handleException(context,e);
	}
}
}
