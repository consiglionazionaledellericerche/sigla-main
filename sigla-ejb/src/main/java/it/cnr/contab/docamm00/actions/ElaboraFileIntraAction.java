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

import it.cnr.contab.docamm00.bp.ElaboraFileIntraBP;
import it.cnr.contab.docamm00.docs.bulk.VIntra12Bulk;
import it.cnr.contab.docamm00.docs.bulk.VIntrastatBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;

public class ElaboraFileIntraAction extends it.cnr.jada.util.action.CRUDAction {
public ElaboraFileIntraAction() {
	super();
}
public Forward doBringBack(ActionContext context) {
	return context.findDefaultForward();
}

public it.cnr.jada.action.Forward doOnMeseChange(ActionContext context) {
	it.cnr.contab.docamm00.bp.ElaboraFileIntraBP bp= (it.cnr.contab.docamm00.bp.ElaboraFileIntraBP) context.getBusinessProcess();
    try {
        bp.fillModel(context); 
        bp.setFile(null);
        return context.findDefaultForward();
	} catch (Exception e) {
		return handleException(context,e);
	}        
}
public Forward doElaboraFile(ActionContext context) throws ComponentException, PersistencyException, IntrospectionException {	

	try{
		fillModel(context);
		ElaboraFileIntraBP bp = (ElaboraFileIntraBP) context.getBusinessProcess();
		if ( bp.getModel() instanceof VIntrastatBulk ){
			VIntrastatBulk dett = (VIntrastatBulk)bp.getModel();
	        if (dett.getMese() == null )
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare il Mese"));
			try {
					bp.doElaboraFile(context,dett,Boolean.FALSE);
					
			} catch (Exception e) {
				return handleException(context, e);
			}
		}
		         
                else {
                	VIntra12Bulk     dett = (VIntra12Bulk)bp.getModel();
		if (dett.getMese() == null )
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare il Mese"));
		try {
				bp.doElaboraFile(context,dett);
				
		} catch (Exception e) { 
			return handleException(context, e);
		}
		}
		
		bp.setMessage("Elaborazione completata.");
		return context.findDefaultForward();
	} catch (it.cnr.jada.bulk.FillException e){
		return handleException(context, e);
	}
}
public Forward doElaboraFileInvio(ActionContext context) throws ComponentException, PersistencyException, IntrospectionException {	

	try{
		fillModel(context);
		ElaboraFileIntraBP bp = (ElaboraFileIntraBP) context.getBusinessProcess();
		VIntrastatBulk dett = (VIntrastatBulk)bp.getModel();
	    if (dett.getMese() == null )
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare il Mese"));
		try {
					bp.doElaboraFile(context,dett,Boolean.TRUE);
					
		} catch (Exception e) {
				return handleException(context, e);
		}
		bp.setMessage("Elaborazione completata.");
		return context.findDefaultForward();
	} catch (it.cnr.jada.bulk.FillException e){
		return handleException(context, e);
	}
}
public Forward doConfermaElaborazione(ActionContext context) throws ComponentException, PersistencyException, IntrospectionException {
	try{
		fillModel(context);
		ElaboraFileIntraBP bp = (ElaboraFileIntraBP) context.getBusinessProcess();
		if ( bp.getModel() instanceof VIntrastatBulk ){
			VIntrastatBulk dett = (VIntrastatBulk)bp.getModel();
	        try {
					bp.confermaElaborazione(context,dett);
					
			} catch (Exception e) {
				return handleException(context, e);
			}
			bp.setMessage("Elaborazione confermata.");
			return context.findDefaultForward();
		}
	} catch (Exception e) {
		return handleException(context, e);
	}
	return context.findDefaultForward();
}

}