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

package it.cnr.contab.preventvar00.action;
import java.rmi.RemoteException;
import java.util.Iterator;

import it.cnr.contab.prevent00.bulk.*;
import it.cnr.contab.preventvar00.bulk.*;
import it.cnr.contab.preventvar00.bp.*;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.*;

/**
 * Action di gestione delle variazioni di bilancio preventivo
 */

public class CRUDVarBilancioAction extends it.cnr.jada.util.action.CRUDAction {
public CRUDVarBilancioAction() {
	super();
}

/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di cancellazione dal controller "dettagliCRUDController"
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doAddToCRUDMain_dettagliCRUDController(ActionContext context) {

	try{

		CRUDVarBilancioBP bp = (CRUDVarBilancioBP)getBusinessProcess(context);
		if (((Var_bilancioBulk)bp.getModel()).getTi_variazione()==null)
			throw new it.cnr.jada.action.MessageToUser("Selezionare il Tipo Variazione");

		getController(context,"main.dettagliCRUDController").add(context);
		return context.findDefaultForward();

	}catch(Throwable ex){
		return handleException(context, ex);
	}

}

public Forward doBlankSearchFind_voce(ActionContext context, Var_bilancio_detBulk dett) {

	if (dett!=null){
		V_assestato_voceBulk voce = new V_assestato_voceBulk();
		dett.setVoceFSaldi(voce);
		dett.setTipoGestione(dett.getVarBilancio().getTipoGestione());
	}
	return context.findDefaultForward();
}

/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di ricerca del searchtool "find_voce"
 *
 * @param context	L'ActionContext della richiesta
 * @param varDett	L'OggettoBulk padre del searchtool
 * @param voce	L'OggettoBulk selezionato dall'utente
 * @return Il Forward alla pagina di risposta
 */
public Forward doBringBackSearchFind_voce(ActionContext context, Var_bilancio_detBulk varDett, V_assestato_voceBulk voce) {

	try {

		if (voce!=null){
			CRUDVarBilancioBP bp = (CRUDVarBilancioBP)getBusinessProcess(context);
			Var_bilancioBulk varBilancio = (Var_bilancioBulk)bp.getModel();

			if(varBilancio.hasVoceDuplicata(varDett, voce)){
				varDett.setVoceFSaldi(new V_assestato_voceBulk());
				varDett.setTipoGestione(varBilancio.getTipoGestione());
				setErrorMessage(context,"Esiste già un dettaglio con la voce selezionata!");
			}else{
				varDett.setVoceFSaldi(voce);
			}
		}
		return context.findDefaultForward();

	}catch(Throwable ex){
		return handleException(context, ex);
	}
}

/** metodo da inserire per cercare le variazioni mie
  doSearchFind_pdg_variazione
 /
/**
 * Gestione della richiesta di salvataggio di una variazione come definitiva
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doOnTiVariazioneChange(ActionContext context) {

	try {

		CRUDVarBilancioBP bp = (CRUDVarBilancioBP)getBusinessProcess(context);
		Var_bilancioBulk varBil = (Var_bilancioBulk)bp.getModel();
		String oldTipoVar = varBil.getTi_variazione();
		fillModel(context);
		
		if (!varBil.verificaTipoVariazione()) {
			varBil.setTi_variazione(oldTipoVar);
			setMessage(context, FormBP.WARNING_MESSAGE,"Non è possibile modificare il tipo variazione!\nIncompatibilità con i dettagli.");
		}else{
			Var_bilancio_detBulk varBilDett = (Var_bilancio_detBulk)bp.getDettagliCRUDController().getModel();
			if (varBilDett!=null && varBilDett.getCd_voce()==null)
				varBilDett.setTipoGestione(varBil.getTipoGestione());
		}


		return context.findDefaultForward();

	}catch(Throwable ex){
		return handleException(context, ex);
	}

}
public Forward doOnEsercizioResChange(ActionContext context) {

	try {
		fillModel(context);
		CRUDVarBilancioBP bp = (CRUDVarBilancioBP)getBusinessProcess(context);
		Var_bilancioBulk varBil = (Var_bilancioBulk)bp.getModel();
		if(varBil.isNew()){
			varBil.setVar_stanz_res(new Var_stanz_resBulk());
		}
		for (Iterator i=varBil.getDettagli().iterator();i.hasNext();){
			Var_bilancio_detBulk det=(Var_bilancio_detBulk) i.next();
			V_assestato_voceBulk old=det.getVoceFSaldi();
			det.setVoceFSaldi(new V_assestato_voceBulk(old.getEsercizio(), varBil.getEsercizio_res().getEsercizio(), old.getCd_centro_responsabilita(), old.getTi_appartenenza(), old.getTi_gestione(), old.getCd_elemento_voce()));
			det.getVoceFSaldi().setCd_voce(old.getCd_voce());
			bp.completeSearchTool(context, det, det.getBulkInfo().getFieldProperty("find_voce"));
		}
		return context.findDefaultForward();
	}catch(Throwable ex){
		return handleException(context, ex);
	}

}

/**
 * Gestione della richiesta di salvataggio di una variazione come definitiva
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doSalvaDefinitivo(ActionContext context) {

	try {

//		throw new it.cnr.jada.action.MessageToUser("Funzione non ancora implementata!!");

		fillModel(context);
		CRUDVarBilancioBP bp = (CRUDVarBilancioBP)getBusinessProcess(context);
		bp.salvaDefinitivo(context);
		setMessage(context,  it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Operazione eseguita con successo");

		return context.findDefaultForward();

	}catch(Throwable ex){
		return handleException(context, ex);
	}

}
public Forward doElimina(ActionContext actioncontext) throws RemoteException {
    try
    {
        fillModel(actioncontext);
        CRUDBP crudbp = getBusinessProcess(actioncontext);
        if(!crudbp.isEditing())
        {
            crudbp.setMessage("Non \350 possibile cancellare in questo momento");
        } else
        {
            crudbp.delete(actioncontext);
        }
        return actioncontext.findDefaultForward();
    }
    catch(Throwable throwable)
    {
        return handleException(actioncontext, throwable);
    }
}
}