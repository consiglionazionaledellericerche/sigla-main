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

import java.util.GregorianCalendar;

import it.cnr.contab.docamm00.bp.ElaboraFileSpesometroBP;
import it.cnr.contab.docamm00.docs.bulk.VSpesometroNewBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;

public class ElaboraFileSpesometroAction extends it.cnr.jada.util.action.CRUDAction {
public ElaboraFileSpesometroAction() {
	super();
}
public Forward doBringBack(ActionContext context) {
	return context.findDefaultForward();
}

public Forward doElaboraFile(ActionContext context) throws ComponentException, PersistencyException, IntrospectionException {	
	try {
	 	fillModel(context); 
		ElaboraFileSpesometroBP bp = (ElaboraFileSpesometroBP) context.getBusinessProcess();
		bp.setFile(null);
		VSpesometroNewBulk dett = (VSpesometroNewBulk)bp.getModel();
		if(dett.getNome_file()==null|| dett.getNome_file().length()!=22){
			throw new ApplicationException("Indicare un nome di file valido!");
		}
		
		if(dett.getDa_data()==null ||dett.getA_data()==null){
			throw new ApplicationException("Indicare il periodo di estrazione!");
    	}
		if(dett.getA_data().before(dett.getDa_data()))
			throw new ApplicationException("Periodo di estrazione non valido!");
		
		GregorianCalendar dataDa = new GregorianCalendar();
		dataDa.setTime(new java.util.Date( dett.getDa_data().getTime()));
		int anno= dataDa.get(GregorianCalendar.YEAR);
		dataDa.setTime(new java.util.Date( dett.getA_data().getTime()));
		int anno_a= dataDa.get(GregorianCalendar.YEAR);
		
		if(CNRUserContext.getEsercizio(context.getUserContext()).intValue() !=anno || CNRUserContext.getEsercizio(context.getUserContext()).intValue() !=anno_a)
			throw new ApplicationException("Periodo di estrazione non coerente con esercizio di scrivania!");			
		if(dett.getTipo()==null)
			throw new ApplicationException("Indicare il tipo!");
		
		try {
				bp.doElaboraFile(context,dett);
		} catch (Exception e) { 
			return handleException(context, e);
		}
		bp.setMessage("Elaborazione completata.");
		return context.findDefaultForward();
	} catch (Exception e) {
		return handleException(context,e);
	}  
	
}

}