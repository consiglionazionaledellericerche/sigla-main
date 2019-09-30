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

package it.cnr.contab.doccont00.action;

import java.util.*;
import it.cnr.contab.doccont00.intcass.bulk.*;
import it.cnr.contab.doccont00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;

public class ListaDistinteEmesseAction extends it.cnr.jada.util.action.SelezionatoreListaAction {
public ListaDistinteEmesseAction() {
	super();
}
/**
 * Metodo utilizzato per gestire l'invio di una distinta.
 */
public Forward doInviaDistinta(ActionContext context) {

	try {

		ListaDistinteEmesseBP bp = (ListaDistinteEmesseBP)context.getBusinessProcess();
		bp.setSelection( context );		
		Collection distinte_emesse = (Collection)bp.getSelectedElements(context);
		bp.inviaDistinta( context, distinte_emesse );
		bp.setMessage("Invio effettuato");
		return context.findDefaultForward();

	} catch(Exception e) {
		return handleException(context,e);
	}
}
public Forward doSelection(ActionContext context,String name) {
	try 
	{
//		fillModel(context);
		AbstractSelezionatoreBP bp = (AbstractSelezionatoreBP)context.getBusinessProcess();
		bp.setFocus(context);
		bp.setSelection(context);
		return context.findDefaultForward();
	}
	catch(Exception e) 
	{
		return handleException(context,e);
	}
}
}
