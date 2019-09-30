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

package it.cnr.contab.pdg00.action;

import it.cnr.contab.pdg00.bp.ElaboraStralcioMensileStipendiBP;
import it.cnr.contab.pdg00.cdip.bulk.V_cnr_estrazione_coriBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * Insert the type's description here.
 * Creation date: (03/03/2009 12.04.09)
 * @author: Matilde D'Urso
 */
public class ElaboraStralcioMensileStipendiAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CaricaFileCassiereAction constructor comment.
 */
public ElaboraStralcioMensileStipendiAction() {
	super();
}
public Forward doBringBack(ActionContext context) {
	return context.findDefaultForward();
}

public Forward doVisualizzaDettagli(ActionContext context) {	

	ElaboraStralcioMensileStipendiBP bp = (ElaboraStralcioMensileStipendiBP)context.getBusinessProcess();
	
	try{
		V_cnr_estrazione_coriBulk v = (V_cnr_estrazione_coriBulk)bp.getModel();
		V_cnr_estrazione_coriBulk dett =(V_cnr_estrazione_coriBulk)bp.getV_cnr_estrazione_cori().getModel();

		if (v.getV_cnr_estrazione_cori().isEmpty()){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: non esistono dettagli da visualizzare");
		}
		if (dett == null){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare la riga per la quale si vogliono visualizzare i dettagli");
		}

		CompoundFindClause clause = new CompoundFindClause();
		clause.addClause("AND","esercizio",SQLBuilder.EQUALS,dett.getEsercizio());
		clause.addClause("AND","mese",SQLBuilder.EQUALS,dett.getMese());
		
		it.cnr.jada.util.action.ConsultazioniBP nbp = (it.cnr.jada.util.action.ConsultazioniBP)context.createBusinessProcess("DettagliStralcioMensileStipendiBP");
		
		nbp.addToBaseclause(clause);
		nbp.openIterator(context);
		
		context.addHookForward("close",this,"doDefault");

		return context.addBusinessProcess(nbp);
	}
	catch (Throwable e){		
		return handleException(context,e);
	}

}
/**
  * Gestisce un Eccezione di chiave duplicata.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
protected Forward handleApplicationPersistencyException(ActionContext context, it.cnr.jada.persistency.sql.ApplicationPersistencyException e) {

	it.cnr.jada.comp.ApplicationException mess = new it.cnr.jada.comp.ApplicationException(e.getMessage());

	return handleException(context, mess);
}
/**
  * Gestisce un Eccezione di chiave duplicata.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
protected Forward handleDuplicateKeyException(ActionContext context, it.cnr.jada.persistency.sql.DuplicateKeyException e) {

	it.cnr.jada.comp.ApplicationException mess = new it.cnr.jada.comp.ApplicationException("Si sta tentando di creare un oggetto già esistente in archivio.");

	return handleException(context, mess);
}
public it.cnr.jada.action.Forward doOnMeseChange(ActionContext context) {
	it.cnr.contab.pdg00.bp.ElaboraStralcioMensileStipendiBP bp= (it.cnr.contab.pdg00.bp.ElaboraStralcioMensileStipendiBP) context.getBusinessProcess();
    try {
        bp.fillModel(context); 
	    return context.findDefaultForward();
	} catch (Exception e) {
		return handleException(context,e);
	}        
}
public Forward doElaboraStralcioMensile(ActionContext context) throws ApplicationException {	

	try{
		fillModel(context);
		ElaboraStralcioMensileStipendiBP bp = (ElaboraStralcioMensileStipendiBP) context.getBusinessProcess();
		V_cnr_estrazione_coriBulk dati = (V_cnr_estrazione_coriBulk)bp.getModel();

		if (dati.getMese() == null )
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare il Mese"));
		try {
			if (!dati.getV_cnr_estrazione_cori().isEmpty() && !bp.isMeseValido(context,dati))
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: il Mese da elaborare deve essere successivo a quelli già elaborati"));

				bp.doElaboraStralcioMensile(context,dati);
				bp.refresh(context);
				
				if (bp.esisteStralcioNegativo(context, dati))
					bp.setMessage("Attenzione! Elaborazione completata ma esistono dettagli negativi");
				else
					bp.setMessage("Elaborazione completata.");
				
		} catch (BusinessProcessException e) {
			return handleException(context, e);
		}
		return context.findDefaultForward();
	} catch (it.cnr.jada.bulk.FillException e){
		return handleException(context, e);
	}
}
/*
public Forward doReset(ActionContext context) {
	try{
		fillModel(context);
		ElaboraFileStipendiBP bp = (ElaboraFileStipendiBP) context.getBusinessProcess();
		V_stipendi_cofi_dettBulk dett = (V_stipendi_cofi_dettBulk)bp.getModel();

		if (dett.getMese() == null )
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare il Mese"));
		if (dett.getV_stipendi_cofi_dett().isEmpty())
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Operazione non consentita: il mese selezionato non è stato ancora caricato"));
		if (dett.getBatch_log_riga().isEmpty())
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Operazione non consentita: il mese selezionato non è stato ancora elaborato"));
		try {
				bp.doReset(context,dett);
				bp.refresh(context);
		} catch (BusinessProcessException e) {
			return handleException(context, e);
		}
		bp.setMessage("Elaborazione Annullata.");
		return context.findDefaultForward();
	} catch (it.cnr.jada.bulk.FillException e){
		return handleException(context, e);
	}
}
*/
}