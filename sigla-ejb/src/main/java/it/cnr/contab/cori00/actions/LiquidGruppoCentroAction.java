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

/*
 * Created on Jan 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.cori00.actions;

import it.cnr.contab.cori00.bp.LiquidGruppoCentroBP;
import it.cnr.contab.cori00.docs.bulk.Liquid_gruppo_centroBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.OptionBP;

/**
 * @author Matilde D'Urso
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LiquidGruppoCentroAction extends ConsultazioniAction {
	
	public Forward doCloseForm(ActionContext context)
		throws BusinessProcessException
		{
		try
		{
            return doConfirmCloseForm(context, 4);
		}
		catch(Throwable throwable)
		{
			return handleException(context, throwable);
		}
	}
	public Forward doRibalta(ActionContext context) {
		try{
   			  return openConfirm(context,"Sei sicuro di voler ribaltare la riga selezionata?",OptionBP.CONFIRM_YES_NO,"doConfermaRibalta");
			} catch (BusinessProcessException e1) {
				return handleException(context, e1);
			}
	}
	public Forward doConfermaRibalta(ActionContext context,int option) {
		if (option == OptionBP.YES_BUTTON) 	
		{
		try{
			fillModel(context);
			LiquidGruppoCentroBP bp = (LiquidGruppoCentroBP) context.getBusinessProcess();
			if (bp.getFocusedElement() == null)
			{
				bp.setMessage("Selezionare la riga da ribaltare.");
				return context.findDefaultForward();	
			}	
			Liquid_gruppo_centroBulk liquid = (Liquid_gruppo_centroBulk)bp.getModel();
			if (!liquid.getEsercizio().equals(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext())- 1)
				||
				liquid.getDa_esercizio_precedente().equals("Y"))
			{
				bp.setMessage("Non è possibile ribaltare la riga selezionata.");
				return context.findDefaultForward();	
			}
			if (liquid.getStato().equals(liquid.STATO_RIBALTATO))
			{
				bp.setMessage("La riga selezionata risulta già ribaltata.");
				return context.findDefaultForward();	
			}
			try {
					bp.ribalta(context,liquid);
			} catch (BusinessProcessException e) {
				return handleException(context, e);
			}
			bp.setMessage("Operazione completata.");
			
			return context.findDefaultForward();
			} catch (it.cnr.jada.bulk.FillException e){
				return handleException(context, e);
			}
		}
		return context.findDefaultForward();
	}
	public Forward doCambiaStato(ActionContext context) {
		try{
   			  return openConfirm(context,"Sei sicuro di voler cambiare lo stato della riga selezionata?",OptionBP.CONFIRM_YES_NO,"doConfermaCambiaStato");
			} catch (BusinessProcessException e1) {
				return handleException(context, e1);
			}
	}
	public Forward doConfermaCambiaStato(ActionContext context,int option) {
		if (option == OptionBP.YES_BUTTON) 	
		{
			try {
			fillModel(context);
			LiquidGruppoCentroBP bp = (LiquidGruppoCentroBP) context.getBusinessProcess();
			if (bp.getFocusedElement() == null)
			{
				bp.setMessage("Selezionare una riga.");
				return context.findDefaultForward();	
			}	
			Liquid_gruppo_centroBulk liquid = (Liquid_gruppo_centroBulk)bp.getModel();
			if (!liquid.getEsercizio().equals(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext())))
			{
				bp.setMessage("E' possibile cambiare lo Stato solo per le righe dell''esercizio in corso.");
				return context.findDefaultForward();	
			}
			if (!liquid.getStato().equals(liquid.STATO_INIZIALE)
				&&
				!liquid.getStato().equals(liquid.STATO_SOSPESO))
			{
				bp.setMessage("E' possibile cambiare lo Stato solo per le righe con stato Iniziale o Sospeso.");
				return context.findDefaultForward();	
			}
			try {
				//verifico se già esiste una riga per lo stesso gruppo con stato iniziale
				if (bp.esisteRiga(context,liquid) && liquid.getStato().equals(liquid.STATO_SOSPESO))
				{
				  bp.setMessage("Esiste già una riga con stato Iniziale per lo stesso gruppo.");
				  return context.findDefaultForward();	
				}	
				bp.cambiaStato(context,liquid);
			} catch (BusinessProcessException e) {
				return handleException(context, e);
			}
			bp.setMessage("Operazione completata.");
			return context.findDefaultForward();
		} catch (it.cnr.jada.bulk.FillException e){
			return handleException(context, e);
		}
	}
		return context.findDefaultForward();
	}	
}