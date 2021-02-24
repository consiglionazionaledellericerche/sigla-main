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

import it.cnr.contab.config00.bp.CRUDLimiteSpesaClassBP;
import it.cnr.contab.config00.pdcfin.bulk.LimiteSpesaClassBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.CRUDAction;

import java.math.BigDecimal;
import java.util.Optional;

public class CRUDLimiteSpesaClassAction extends CRUDAction {

public Forward doOnImLimiteAssestatoChange( ActionContext context) {
	try{
		CRUDLimiteSpesaClassBP bp = (CRUDLimiteSpesaClassBP)context.getBusinessProcess();
		V_classificazione_vociBulk model=(V_classificazione_vociBulk)bp.getModel();
		LimiteSpesaClassBulk riga = (LimiteSpesaClassBulk) bp.getDettagli().getModel();
		BigDecimal oldImp = riga.getIm_limite_assestato();
		fillModel(context);
		if (model.getIm_limite_assestato()==null){
			riga.setIm_limite_assestato(BigDecimal.ZERO);
			throw new ApplicationException("Valorizzare prima l'importo limite in testata.");
		}

		if (riga!=null) {
			if (model.getImLimiteAssestatoRipartito().compareTo(model.getIm_limite_assestato()) > 0) {
				BigDecimal impMax = model.getIm_limite_assestato().subtract(model.getImLimiteAssestatoRipartito().subtract(riga.getIm_limite_assestato()));
				riga.setIm_limite_assestato(oldImp);
				throw new ApplicationException("L'importo ripartito non può essere superiore al limite stabilito per la classificazione! Il valore massimo inseribile è "
				+new it.cnr.contab.util.EuroFormat().format(impMax));
			}

			if (riga.getIm_limite_assestato().compareTo(Optional.ofNullable(riga.getImAssestatoAssunto()).orElse(BigDecimal.ZERO)) < 0) {
				riga.setIm_limite_assestato(oldImp);
				throw new ApplicationException("L'importo limite non può essere inferiore alla quota già stanziata per il Cds.");
			}
		}

		return context.findDefaultForward();
	}catch (Throwable t) {
	    return handleException(context, t);

	}	
}
public Forward doAddToCRUDMain_Dettagli(ActionContext context) {
	try {
		fillModel(context);
		CRUDLimiteSpesaClassBP bp = (CRUDLimiteSpesaClassBP)context.getBusinessProcess();
		V_classificazione_vociBulk model=(V_classificazione_vociBulk)bp.getModel();
		if (!Optional.ofNullable(model).map(V_classificazione_vociBulk::getId_classificazione).isPresent() ||
			!Optional.ofNullable(model).map(V_classificazione_vociBulk::getIm_limite_assestato).isPresent())
			throw new ApplicationException("Completare prima i dati in testata.");
		else
			getController(context,"main.Dettagli").add(context);
		return context.findDefaultForward();			
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
