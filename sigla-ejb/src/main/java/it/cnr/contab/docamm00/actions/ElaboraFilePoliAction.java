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

package it.cnr.contab.docamm00.actions;

import it.cnr.contab.docamm00.bp.ElaboraFilePoliBP;
import it.cnr.contab.docamm00.docs.bulk.VSpesometroBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;

public class ElaboraFilePoliAction extends it.cnr.jada.util.action.CRUDAction {
public ElaboraFilePoliAction() {
	super();
}
public Forward doBringBack(ActionContext context) {
	return context.findDefaultForward();
}

public it.cnr.jada.action.Forward doOnMeseChange(ActionContext context) {
	it.cnr.contab.docamm00.bp.ElaboraFilePoliBP bp= (it.cnr.contab.docamm00.bp.ElaboraFilePoliBP) context.getBusinessProcess();
    try {
    	bp.fillModel(context); 
        bp.setFile(null);
    	VSpesometroBulk dett = (VSpesometroBulk)bp.getModel();
    	if(dett.isComunicazionePoliv()){
    		dett.setMese(null);
    		throw new ApplicationException("In caso di comunicazione annuale, il mese non deve essere indicato!");
    	}
        return context.findDefaultForward();
	} catch (Exception e) {
		return handleException(context,e);
	}        
}
public it.cnr.jada.action.Forward doOnFlBlacklistChange(ActionContext context) {
	it.cnr.contab.docamm00.bp.ElaboraFilePoliBP bp= (it.cnr.contab.docamm00.bp.ElaboraFilePoliBP) context.getBusinessProcess();
    try {
        bp.fillModel(context); 
        bp.setFile(null);
        VSpesometroBulk dett = (VSpesometroBulk)bp.getModel();
        dett.setComunicazionePoliv(false);
        return context.findDefaultForward();
	} catch (Exception e) {
		return handleException(context,e);
	}        
}
public it.cnr.jada.action.Forward doOnComunicazionePolivChange(ActionContext context) {
	it.cnr.contab.docamm00.bp.ElaboraFilePoliBP bp= (it.cnr.contab.docamm00.bp.ElaboraFilePoliBP) context.getBusinessProcess();
    try {
        bp.fillModel(context);
        bp.setFile(null);
        VSpesometroBulk dett = (VSpesometroBulk)bp.getModel();
        dett.setFlBlacklist(false);
        dett.setMese(null);
        return context.findDefaultForward();
	} catch (Exception e) {
		return handleException(context,e);
	}        
}

public Forward doElaboraFile(ActionContext context) throws ComponentException, PersistencyException, IntrospectionException {	

	try{
		fillModel(context);
		ElaboraFilePoliBP bp = (ElaboraFilePoliBP) context.getBusinessProcess();
		VSpesometroBulk dett = (VSpesometroBulk)bp.getModel();
		if(!dett.isFlBlacklist() && !dett.isComunicazionePoliv())
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Selezionare il tipo di elaborazione."));
		// Controllo spostato nel Bp perchè dipende dalla configurazione nell'anno se l'estrazione è annuale o mensile
//		if (dett.isFlBlacklist() && dett.getMese() == null )
//			return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare il Mese"));
		try {
				bp.doElaboraFile(context,dett);
		} catch (Exception e) { 
			return handleException(context, e);
		}
		bp.setMessage("Elaborazione completata.");
		return context.findDefaultForward();
	} catch (it.cnr.jada.bulk.FillException e){
		return handleException(context, e);
	}
}

}