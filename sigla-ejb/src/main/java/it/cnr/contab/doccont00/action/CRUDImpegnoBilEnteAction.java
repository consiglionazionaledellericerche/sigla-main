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

package it.cnr.contab.doccont00.action;

import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.doccont00.bp.CRUDImpegnoBilEnteBP;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
/**
 * Azione che gestisce le richieste relative alla Gestione dell'
 * Impegno da bilancio Ente.
 */
public class CRUDImpegnoBilEnteAction extends CRUDAbstractObbligazioneAction {
public CRUDImpegnoBilEnteAction() {
	super();
}
public Forward doBlankSearchFind_creditore(ActionContext context,ObbligazioneBulk obbligazione) 
{
	try 
	{
		obbligazione.setCreditore(new TerzoBulk());
		obbligazione.getCreditore().setAnagrafico( new AnagraficoBulk());
		
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce la validazione di nuovo terzo creato
 	 * @param context <code>ActionContext</code> in uso.
	 * @param obbligazione Oggetto di tipo <code>ObbligazioneBulk</code> 
	 * @param terzo Oggetto di tipo <code>TerzoBulk</code> che rappresenta il nuovo terzo creato
	 *
	 * @return <code>Forward</code>
 */
public Forward doBringBackCRUDCrea_creditore(ActionContext context, ObbligazioneBulk obbligazione, TerzoBulk terzo) 
{
	try 
	{
		if (terzo != null )
		{
			obbligazione.validateTerzo( terzo);
			obbligazione.setCreditore( terzo );
		}	
		return context.findDefaultForward();
	}
	catch(ValidationException e) 
	{
		getBusinessProcess(context).setErrorMessage(e.getMessage());
		return context.findDefaultForward();
	}		
	
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce la selezione di un'unità organizzativa
 *
 */
public Forward doCambiaUnitaOrganizzativa(ActionContext context) 
{
	try 
	{ 	
		fillModel( context );
		SimpleCRUDBP bp = (SimpleCRUDBP)getBusinessProcess(context);
		ImpegnoBulk imp = (ImpegnoBulk)bp.getModel();
		imp.setVoce_f( new Voce_fBulk() );
		imp.setElemento_voce( new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk() );
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce un comando di cancellazione.
 */
public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {
	try {
		fillModel(context);

		CRUDBP bp = getBusinessProcess(context);
		if (!bp.isEditing()) {
			bp.setMessage("Non è possibile cancellare in questo momento");
		} else {
			bp.delete(context);
			bp.setMessage("Annullamento effettuato");
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doBringBackSearchFind_voce(ActionContext context, ImpegnoBulk bulk, Voce_fBulk voce) {

	CRUDImpegnoBilEnteBP bp = (CRUDImpegnoBilEnteBP) getBusinessProcess(context);
	try 
	{ 	
		if (voce!=null){
			bulk.setVoce_f(voce);
			bp.caricaElementoVoce(context, voce);
		}
	}
	catch(Throwable e) {return handleException(context,e);}

	return context.findDefaultForward();
}
}
