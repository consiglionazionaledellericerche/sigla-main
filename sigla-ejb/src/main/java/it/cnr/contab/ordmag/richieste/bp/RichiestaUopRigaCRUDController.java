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

package it.cnr.contab.ordmag.richieste.bp;
import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import it.cnr.contab.docamm00.ejb.*;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.SimpleCRUDBP;

/**
 * Riga del documento generico passivo
 */

public class RichiestaUopRigaCRUDController extends it.cnr.jada.util.action.SimpleDetailCRUDController {
public RichiestaUopRigaCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
	super(name, modelClass, listPropertyName, parent);
}

public boolean isGrowable() {
	
	RichiestaUopBulk doc = (RichiestaUopBulk)getParentModel();
	return	super.isGrowable() && !((it.cnr.jada.util.action.CRUDBP)getParentController()).isSearching() 
//		&&	!doc.STATO_PAGATO.equalsIgnoreCase(doc.getStato_cofi()) && 
//		(!doc.isDoc1210Associato() || (doc.getLettera_pagamento_estero()!=null && doc.getLettera_pagamento_estero().getStato_trasmissione().compareTo(MandatoBulk.STATO_TRASMISSIONE_TRASMESSO)==0))
				;
}

public boolean isShrinkable() {
	
	RichiestaUopBulk doc = (RichiestaUopBulk)getParentModel();
	return	super.isShrinkable() && !((it.cnr.jada.util.action.CRUDBP)getParentController()).isSearching() 
//			&&
//			!doc.STATO_PAGATO.equalsIgnoreCase(doc.getStato_cofi()) &&
//			(!doc.isDoc1210Associato() || (doc.getLettera_pagamento_estero()!=null && doc.getLettera_pagamento_estero().getStato_trasmissione().compareTo(MandatoBulk.STATO_TRASMISSIONE_TRASMESSO)==0))
			;
}

public void validate(ActionContext context,OggettoBulk model) throws ValidationException {
//	if (context.getCurrentCommand().equals("doContabilizzaObbligazioni"))
//		return;
//	try {
//		if ((Documento_generico_rigaBulk)model!=null && (((Documento_generico_rigaBulk)model).getTerzo()==null || ((Documento_generico_rigaBulk)model).getTerzo().getAnagrafico()==null))
//			throw new ValidationException("Il campo anagrafica e' obbligatorio");
//		if ((Documento_generico_rigaBulk)model!=null && ((Documento_generico_rigaBulk)model).getDs_riga()==null)
//			throw new ValidationException("Inserire una descrizione");
//		if ((Documento_generico_rigaBulk)model!=null && (((Documento_generico_rigaBulk)model).getIm_riga()==null || ((Documento_generico_rigaBulk)model).getIm_riga().compareTo(new java.math.BigDecimal(0))==0))
//			throw new ValidationException("Inserire un importo positivo");
//		if ((Documento_generico_rigaBulk)model!=null && ((Documento_generico_rigaBulk)model).getBanca()==null)
//			throw new ValidationException("Inserire dei riferimenti bancari corretti");
//		((DocumentoGenericoComponentSession)(((SimpleCRUDBP)getParentController()).createComponentSession())).validaRiga(context.getUserContext(), (Documento_generico_rigaBulk)model);
//	} catch (ValidationException e) {
//		throw e;
//	} catch (it.cnr.jada.comp.ApplicationException e) {
//		throw new ValidationException(e.getMessage());
//	} catch (Throwable e) {
//		throw new it.cnr.jada.DetailedRuntimeException(e);
//	}
}

public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {
//	try {
//		Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)detail;
//		if (riga.getTi_associato_manrev() != null && riga.ASSOCIATO_A_MANDATO.equalsIgnoreCase(riga.getTi_associato_manrev()))
//			throw new ValidationException("Impossibile eliminare il dettaglio \"" + 
//											((riga.getDs_riga() != null) ?
//												riga.getDs_riga() :
//												String.valueOf(riga.getProgressivo_riga().longValue())) + 
//											"\" perchè associato a mandato.");
//		((DocumentoGenericoComponentSession)(((SimpleCRUDBP)getParentController()).createComponentSession())).eliminaRiga(context.getUserContext(), (Documento_generico_rigaBulk)detail);
//	} catch (it.cnr.jada.comp.ApplicationException e) {
//		throw new ValidationException(e.getMessage());
//	} catch (ValidationException e) {
//		throw e;
//	} catch (Throwable e) {
//		throw new it.cnr.jada.DetailedRuntimeException(e);
//	}
}

@Override
	public void writeFormInput(JspWriter jspwriter, String s, String s1,
			boolean flag, String s2, String s3) throws IOException {
	
	RichiestaUopBulk doc=null;
	if(((it.cnr.jada.util.action.CRUDBP)getParentController()) instanceof CRUDRichiestaUopBP ){
	if(((it.cnr.jada.util.action.CRUDBP)getParentController()).getModel()!=null)
		doc = (RichiestaUopBulk)((it.cnr.jada.util.action.CRUDBP)getParentController()).getModel();
//		if (doc!=null &&
//			doc.isRiportataInScrivania()&&
//			!doc.isPagata()&& 
//			isInputReadonly()&& 
//			s1.equals("modalita_pagamento")){ 
//				getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, flag, s2, "onChange=\"submitForm('doOnModalitaPagamentoChange')\"", getInputPrefix(), getStatus(), getFieldValidationMap());
//		}else
//			if (doc!=null &&
//			doc.isRiportataInScrivania()&&
//			!doc.isPagata()&& 
//			isInputReadonly()&& 
//			s1.equals("listabanche")){ 
//				getBulkInfo().writeFormInput(jspwriter, getModel(), s, s1, flag,
//						s2,"" ,
//						getInputPrefix(), getStatus(), getFieldValidationMap());
//			}
//		else
			super.writeFormInput(jspwriter,s,s1,flag,s2,s3);
	}
	}
}