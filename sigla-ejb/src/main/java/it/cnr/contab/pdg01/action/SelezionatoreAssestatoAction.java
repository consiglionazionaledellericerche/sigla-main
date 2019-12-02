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
 * Created on May 05, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg01.action;

import java.math.BigDecimal;

import it.cnr.contab.prevent00.bulk.V_assestatoBulk;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.pdg01.bp.SelezionatoreAssestatoBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.ConsultazioniAction;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SelezionatoreAssestatoAction extends ConsultazioniAction {
	/**
	 * 
	 */
	public SelezionatoreAssestatoAction() {
		super();
	}
    public Forward doAssegnaImporto(ActionContext actioncontext){
		SelezionatoreAssestatoBP bp = (SelezionatoreAssestatoBP)actioncontext.getBusinessProcess();
		try {
			V_assestatoBulk saldo = (V_assestatoBulk)bp.getModel();
			BigDecimal impOld = saldo.getImp_da_assegnare();
			bp.fillModels(actioncontext);
			if (bp.getBulkCaller() instanceof Var_stanz_resBulk) {
				if (((Var_stanz_resBulk)bp.getBulkCaller()).getTipologia().equalsIgnoreCase(Var_stanz_resBulk.TIPOLOGIA_ECO) &&
					 Utility.nvl(saldo.getImp_da_assegnare()).compareTo(Utility.ZERO) > 0) {
					bp.setMessage("L'importo delle righe di variazione deve essere negativo per le Economie.");
					saldo.setImp_da_assegnare(impOld);
					return actioncontext.findDefaultForward();
			    }
			}
			bp.getSelection().addToSelection(bp.getSelection().getFocus());
			bp.setObjectReplacer(bp.getAssestatoReplacer());
			bp.getAssestatoReplacer().put(saldo,saldo);
			bp.allineaImportiePercentuali(actioncontext);
		} catch (FillException e) {
			bp.setMessage(e.getMessage());
		} catch (BusinessProcessException e) {
			return handleException(actioncontext, e);
		}
		return actioncontext.findDefaultForward();
    }
	
	public Forward doConferma(ActionContext actioncontext) {
		try
		{
			SelezionatoreAssestatoBP bp = (SelezionatoreAssestatoBP)actioncontext.getBusinessProcess();
			bp.saveSelection(actioncontext);
			if(bp.getSelection().isEmpty() && bp.getSelection().getFocus() < 0)
			{
				bp.setMessage(2, "E' necessario selezionare almeno un elemento");
				return actioncontext.findDefaultForward();
			} else
			{
				return basicDoBringBack(actioncontext);
			}
		}
		catch(Throwable throwable)
		{
			return handleException(actioncontext, throwable);
		}
	}
    
	public Forward doModalitaInserimentoImporti(ActionContext actioncontext){
		SelezionatoreAssestatoBP bp = (SelezionatoreAssestatoBP)actioncontext.getBusinessProcess();
		try {
			fillModel( actioncontext );
			bp.saveSelection(actioncontext);
			bp.impostaModalitaMappa(actioncontext, bp.MODALITA_INSERIMENTO_IMPORTI);
		} catch (BusinessProcessException e) {
			bp.setMessage(e.getMessage());
		} catch (FillException e) {
			bp.setMessage(e.getMessage());
		}
		return actioncontext.findDefaultForward();
    }
	
    public Forward doModalitaInserimentoPercentuali(ActionContext actioncontext){
		SelezionatoreAssestatoBP bp = (SelezionatoreAssestatoBP)actioncontext.getBusinessProcess();
		try {
			fillModel( actioncontext );
			bp.saveSelection(actioncontext);
			bp.impostaModalitaMappa(actioncontext, bp.MODALITA_INSERIMENTO_PERCENTUALI);
		} catch (BusinessProcessException e) {
			bp.setMessage(e.getMessage());
		} catch (FillException e) {
			bp.setMessage(e.getMessage());
		}
		return actioncontext.findDefaultForward();
    }
   
	public Forward doAssegnaPercentuale(ActionContext actioncontext){
		SelezionatoreAssestatoBP bp = (SelezionatoreAssestatoBP)actioncontext.getBusinessProcess();
		OggettoBulk saldo = (OggettoBulk)bp.getModel();

		try {
			bp.fillModels(actioncontext);
			bp.getSelection().addToSelection(bp.getSelection().getFocus());
			bp.setObjectReplacer(bp.getAssestatoReplacer());
			bp.getAssestatoReplacer().put(saldo,saldo);
			bp.allineaImportiePercentuali(actioncontext);
		} catch (FillException e) {
			bp.setMessage(e.getMessage());
		} catch (BusinessProcessException e) {
			bp.setMessage(e.getMessage());
		}
		return actioncontext.findDefaultForward();
    }

	public Forward doAggiornaImportiPercentuali(ActionContext actioncontext) {
		SelezionatoreAssestatoBP bp = (SelezionatoreAssestatoBP)actioncontext.getBusinessProcess();
		try {
			fillModel( actioncontext );
			bp.saveSelection(actioncontext);
			bp.fillModels( actioncontext );
			bp.allineaSelezioneeObjectReplacer(actioncontext);
		} catch (BusinessProcessException e) {
			bp.setMessage(e.getMessage());
		} catch (FillException e) {
			bp.setMessage(e.getMessage());
		}
		return actioncontext.findDefaultForward();
	}

	public Forward doAssegnaPercentualiUguali(ActionContext actioncontext) {
		SelezionatoreAssestatoBP bp = (SelezionatoreAssestatoBP)actioncontext.getBusinessProcess();
		try {
			fillModel( actioncontext );
			bp.saveSelection(actioncontext);
			bp.fillModels( actioncontext );
			bp.assegnaPercentualiUguali(actioncontext);
		} catch (BusinessProcessException e) {
			bp.setMessage(e.getMessage());
		} catch (FillException e) {
			bp.setMessage(e.getMessage());
		}
		return actioncontext.findDefaultForward();
	}

	public Forward doSort(ActionContext actioncontext, String s, String s1) throws BusinessProcessException {
		if (s1.equals("importo_disponibile_netto"))
			return super.doSort(actioncontext, s, "importo_disponibile");
		return super.doSort(actioncontext, s, s1);
	}
}
