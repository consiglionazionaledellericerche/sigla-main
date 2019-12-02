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

package it.cnr.contab.prevent00.bp;

import it.cnr.contab.prevent00.bulk.*;

/**
 * Business Process di gestione del del bilancio preventivo CDS
 */

public class CRUDResiduiPresuntiBP extends it.cnr.jada.util.action.SimpleCRUDBP{
public CRUDResiduiPresuntiBP() 
{
	super();
}
public CRUDResiduiPresuntiBP(String function) 
{
	super(function);
}
//
//	Se il preventivo dell'ente risulta approvato non e' consentita
//	la modifica
//

public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException 
{
	try
	{
		super.basicEdit(context, bulk, doInitializeForEdit);
		if(isSearching())
			return;
			
		Voce_f_res_presBulk voceResPres = (Voce_f_res_presBulk)getModel();

		it.cnr.contab.prevent00.ejb.BilancioPreventivoComponentSession component = createBilancioPreventivoComponentSession();

		//	Ricerca CDS ENTE
		Bilancio_preventivoBulk bilancioPrev = new Bilancio_preventivoBulk(null, voceResPres.getEsercizio(), voceResPres.getTi_appartenenza());
		it.cnr.contab.config00.sto.bulk.EnteBulk cds = (it.cnr.contab.config00.sto.bulk.EnteBulk)component.cercaCdsEnte(context.getUserContext(), bilancioPrev);
		bilancioPrev.setCd_cds(cds.getCd_unita_organizzativa());

		//	Ricerca preventivo ente
		bilancioPrev = component.caricaBilancioPreventivo(context.getUserContext(), bilancioPrev);		
		if( bilancioPrev!=null &&
			Bilancio_preventivoBulk.STATO_C.equals(bilancioPrev.getStato()) && voceResPres!=null)
		{
			setStatus(VIEW);
			setMessage("Modifica non consentita! Bilancio preventivo approvato.");
		}
	} 
	catch(Throwable e) 
	{
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}	
}
/**
 * Crea il riferimento alla remote interface del session bean CNRPREVENT00_EJB_BilancioPreventivoComponentSession
 * 
 * @return La remote interface
 * @throws EJBException	Se si verifica qualche eccezione applicativa per cui non è possibile effettuare l'operazione
 * @throws RemoteException	Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
 */
public it.cnr.contab.prevent00.ejb.BilancioPreventivoComponentSession createBilancioPreventivoComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException 
{
	return (it.cnr.contab.prevent00.ejb.BilancioPreventivoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPREVENT00_EJB_BilancioPreventivoComponentSession", it.cnr.contab.prevent00.ejb.BilancioPreventivoComponentSession.class);
}
}
