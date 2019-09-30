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
 * Created on Feb 27, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.varstanz00.action;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;

import it.cnr.contab.prevent00.bulk.V_assestato_residuoBulk;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bp.AssestatoResiduoReplacer;
import it.cnr.contab.varstanz00.bp.SelezionatoreAssestatoResiduoBP;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.bulk.ValidationParseException;
import it.cnr.jada.util.action.ConsultazioniAction;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.action.SelezionatoreListaAction;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SelezionatoreAssestatoResiduoAction extends ConsultazioniAction {

	/**
	 * 
	 */
	public SelezionatoreAssestatoResiduoAction() {
		super();
	}
    public Forward doAssegnaImporto(ActionContext actioncontext){
		SelezionatoreAssestatoResiduoBP selezionatore = (SelezionatoreAssestatoResiduoBP)actioncontext.getBusinessProcess();
		try {
			V_assestato_residuoBulk saldo = (V_assestato_residuoBulk)selezionatore.getModel();
			BigDecimal impOld = saldo.getImp_da_assegnare();
			selezionatore.fillModels(actioncontext);
			if (selezionatore.getVar_stanz_res().getTipologia().equalsIgnoreCase(Var_stanz_resBulk.TIPOLOGIA_ECO) &&
			    Utility.nvl(saldo.getImp_da_assegnare()).compareTo(Utility.ZERO) > 0){
					selezionatore.setMessage("L'importo delle righe di variazione deve essere negativo per le Economie.");
					saldo.setImp_da_assegnare(impOld);
					return actioncontext.findDefaultForward();
			    }
			selezionatore.getSelection().addToSelection(selezionatore.getSelection().getFocus());
			if (selezionatore.getAssestatoReplacer() == null){
				selezionatore.setAssestatoReplacer(new AssestatoResiduoReplacer());
				selezionatore.setObjectReplacer(selezionatore.getAssestatoReplacer());
			}
			selezionatore.getAssestatoReplacer().put(saldo,saldo);
		} catch (FillException e) {
			selezionatore.setMessage(e.getMessage());
		}
		return actioncontext.findDefaultForward();
    }
	public Forward doConferma(ActionContext actioncontext) {
		try{
			SelezionatoreAssestatoResiduoBP selezionatore = (SelezionatoreAssestatoResiduoBP)actioncontext.getBusinessProcess();
			for (Iterator elementiSel = selezionatore.getSelectedElements(actioncontext).iterator();elementiSel.hasNext();){
				V_assestato_residuoBulk saldo = (V_assestato_residuoBulk)elementiSel.next();
				if (saldo.getImp_da_assegnare() != null)
				  selezionatore.aggiungiDettaglioVariazione(actioncontext,saldo);
			}
			selezionatore.setAssestatoReplacer(null);
			selezionatore.setObjectReplacer(null);
			selezionatore.refresh(actioncontext);
			selezionatore.setMessage("Operazione effettuata.");
			return actioncontext.findDefaultForward();
		}catch(BusinessProcessException e){
			return handleException(actioncontext, e);
		}
	}
}
