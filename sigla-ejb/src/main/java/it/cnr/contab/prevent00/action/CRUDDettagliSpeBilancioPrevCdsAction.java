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

package it.cnr.contab.prevent00.action;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.prevent00.bp.*;
import it.cnr.contab.prevent00.bulk.*;
import it.cnr.jada.action.*;

/**
 * Action di gestione dettagli di spesa del bilancio preventivo CDS
 */

public class CRUDDettagliSpeBilancioPrevCdsAction extends it.cnr.jada.util.action.CRUDAction {
public CRUDDettagliSpeBilancioPrevCdsAction() {
	super();
}

/**
 * Gestione della cancellazione del dettaglio di spesa CDS
 *
 * @param context	L'ActionContext della richiesta
 */

public Forward doElimina(ActionContext context) throws java.rmi.RemoteException
{
	CRUDDettagliSpeBilancioPrevCdsBP bp = (CRUDDettagliSpeBilancioPrevCdsBP)getBusinessProcess(context);
	Voce_f_saldi_cmpBulk dettaglio = (Voce_f_saldi_cmpBulk) bp.getModel();
	
	if(dettaglio.getFl_sola_lettura().booleanValue())
	{
		setMessage(context,0, "Dettaglio non eliminabile !");		
	}
	else
	{	
		try
		{		
			super.doElimina(context);
		}			
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}	
	return context.findDefaultForward();
}
}