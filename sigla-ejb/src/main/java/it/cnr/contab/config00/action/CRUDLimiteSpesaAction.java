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

package it.cnr.contab.config00.action;

import java.math.BigDecimal;

import it.cnr.contab.config00.bp.CRUDLimiteSpesaBP;
import it.cnr.contab.config00.pdcfin.bulk.LimiteSpesaBulk;
import it.cnr.contab.config00.pdcfin.bulk.LimiteSpesaDetBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.ejb.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.*;

public class CRUDLimiteSpesaAction extends CRUDAction {

public Forward doOnImportoLimiteChange( ActionContext context)
{
	try{
		CRUDLimiteSpesaBP bp = (CRUDLimiteSpesaBP)context.getBusinessProcess();
		LimiteSpesaBulk model=(LimiteSpesaBulk)bp.getModel();
		LimiteSpesaDetBulk riga = (LimiteSpesaDetBulk) bp.getDettagli().getModel();
		java.math.BigDecimal oldImp = riga.getImporto_limite();
		fillModel(context);
		if (model.getImporto_limite()==null){
			riga.setImporto_limite(BigDecimal.ZERO);
			throw new ApplicationException("Valorizzare prima l'importo limite in testata.");
		}
		if (riga!=null) 
			if ((riga.getImporto_limite().subtract(riga.getImpegni_assunti())).compareTo(BigDecimal.ZERO)<0){
				 riga.setImporto_limite(oldImp);
				 throw new ApplicationException("L'importo limite non può essere inferiore agli impegni assunti per il Cds.");
			}
			else{
				if(model.getImporto_limite().compareTo(model.getImporto_assegnato().add(riga.getImporto_limite()).subtract(oldImp))<0){
					riga.setImporto_limite(oldImp);
					throw new ApplicationException("L'importo limite della voce risulta già completamente assegnato.");
				}
		    	else	
		    		model.setImporto_assegnato(model.getImporto_assegnato().add(riga.getImporto_limite()).subtract(oldImp));
			}
		return context.findDefaultForward();
	}catch (Throwable t) {
	    return handleException(context, t);

	}	
}
public Forward doAddToCRUDMain_Dettagli(ActionContext context) {
	try {
		fillModel(context);
		CRUDLimiteSpesaBP bp = (CRUDLimiteSpesaBP)context.getBusinessProcess();
		LimiteSpesaBulk model=(LimiteSpesaBulk)bp.getModel();
		if(model.getCd_elemento_voce()==null || model.getFonte()==null||model.getImporto_limite()==null)  
			throw new ApplicationException("Completare prima i dati in testata.");
		else
			getController(context,"main.Dettagli").add(context);
		return context.findDefaultForward();			
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
