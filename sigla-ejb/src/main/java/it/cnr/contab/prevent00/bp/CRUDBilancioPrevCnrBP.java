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
import it.cnr.contab.prevent00.ejb.*;

/**
 * Business Process di gestione del del bilancio preventivo CNR
 */

public class CRUDBilancioPrevCnrBP extends it.cnr.jada.util.action.BulkBP { 
public CRUDBilancioPrevCnrBP() {
	super();
}

public CRUDBilancioPrevCnrBP(String function) {
	super(function);
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
	return (BilancioPreventivoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPREVENT00_EJB_BilancioPreventivoComponentSession", BilancioPreventivoComponentSession.class);
}

public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext param1, it.cnr.jada.persistency.sql.CompoundFindClause param2, it.cnr.jada.bulk.OggettoBulk param3, it.cnr.jada.bulk.OggettoBulk param4, java.lang.String param5) {
	return null;
}

protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	super.init(config,context);
		
	Bilancio_preventivoBulk bilancioPrev = new Bilancio_preventivoBulk();
		
	// Valorizzo Esercizio di scrivania
	bilancioPrev.setEsercizio( it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context) );

	//Valorizzo Tipo appartenenza = CNR		
	bilancioPrev.setTi_appartenenza( "C" );

	it.cnr.jada.UserContext userContext = context.getUserContext();
	try 
	{
		// Valorizzo Cds ENTE		
		BilancioPreventivoComponentSession aComponent =	createBilancioPreventivoComponentSession();
		it.cnr.contab.config00.sto.bulk.EnteBulk cds = (it.cnr.contab.config00.sto.bulk.EnteBulk)aComponent.cercaCdsEnte(userContext, bilancioPrev);
		bilancioPrev.setCd_cds(cds.getCd_unita_organizzativa());
		
		// Lettura da tabella "Bilancio_preventivo" del bilancio da visualizzare
		Bilancio_preventivoBulk bilancioP = (Bilancio_preventivoBulk) aComponent.caricaBilancioPreventivo(userContext, bilancioPrev);
		if(bilancioP == null)
		{
 		    throw new it.cnr.jada.comp.ApplicationException("Non esiste il Bilancio Preventivo CNR con Esercizio '" + bilancioPrev.getEsercizio() + "' e Cds '" + bilancioPrev.getCd_cds() + "' !");
		}
		else
		{
			setModel(context,bilancioP);
		}			
	} 
	catch(Exception e) 
	{
		throw handleException(e);
	}
}

/**
 * Gestione della produzione del bilancio preventivo del CDS 
 *
 * @param context	L'ActionContext della richiesta
 * @throws BusinessProcessException	
 */
public void predisponeBilancioPreventivoCNR(it.cnr.jada.action.ActionContext context)  throws it.cnr.jada.action.BusinessProcessException 
{
	it.cnr.jada.UserContext userContext = context.getUserContext();
	Bilancio_preventivoBulk bilancioPrev = (Bilancio_preventivoBulk) getModel();
	try
	{	
		setModel(context,createBilancioPreventivoComponentSession().predisponeBilancioPreventivoCNR(userContext, bilancioPrev));
	}
	catch(javax.ejb.EJBException e) 
	{
		throw new it.cnr.jada.action.BusinessProcessException(e);
	} 
	catch(java.rmi.RemoteException e) 
	{
		throw new it.cnr.jada.action.BusinessProcessException(e);
	} 
	catch(it.cnr.jada.comp.ComponentException e) 
	{
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}	

	return ;
}
}