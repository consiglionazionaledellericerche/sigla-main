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

package it.cnr.contab.chiusura00.bp;

import it.cnr.contab.chiusura00.ejb.*;
import it.cnr.contab.chiusura00.bulk.*;
import it.cnr.contab.doccont00.intcass.bulk.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.ejb.*;
import it.cnr.jada.util.*;

/**
 * BP che gestisce il riporto avanti massivo di documenti contabili all'esercizio successivo
 * con cambio di capitolo finanziario (voce_f o elemento_voce)
 */

public class RiportoEvolutoEsSuccessivoBP extends RiportoEsSuccessivoBP {

	
	
public RiportoEvolutoEsSuccessivoBP() {
	super("Tn");
}
public RiportoEvolutoEsSuccessivoBP(String function) {
	super(function+"Tn");
}

protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	super.init(config,context);
	if (!isRibaltato()) {
		throw handleException( new ApplicationException("Non è possibile utilizzare questa funzione perchè non è stato effettuato il ribaltamento complessivo dei documenti contabili per il CDS "+CNRUserContext.getCd_cds(context.getUserContext())));
	}
}

/**
 * Ricerca di documenti contabili idonei al riporto avanti
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @param model	
 * @return 
 * @throws BusinessProcessException	
 */

public it.cnr.jada.util.RemoteIterator cercaDocDaRiportare(ActionContext context,V_obb_acc_xxxBulk model) throws it.cnr.jada.action.BusinessProcessException 
{
	try 
	{
		((V_obb_acc_xxxBulk)model).validatePerRiportaEvoluto();
		completeSearchTools(context,this);		
		RicercaDocContComponentSession sessione = (RicercaDocContComponentSession) createComponentSession();
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,sessione.cercaPerRiportaAvantiEvoluto(context.getUserContext(),model));
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * richiama la stored procedure che effettua il riporto massivo sui documenti
 * selezionati dall'utente con cambio di elemento_voce o di voce_f
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @throws BusinessProcessException	
 */

public void confermaRiporto(ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	try 
	{
		RicercaDocContComponentSession sessione = (RicercaDocContComponentSession) createComponentSession();
		sessione.callRiportoNextEsDocContVoce(context.getUserContext(), ((V_obb_acc_xxxBulk)getModel()).getPg_call());
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * gestisce la selezione massiva di tutti i doc.contabili visualizzati all'utente
 */
public void selectAll(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try 
	{
		((RicercaDocContComponentSession)createComponentSession()).selectAllPerRiportaAvantiEvoluto(
						context.getUserContext(),(V_obb_acc_xxxBulk)getModel());
	} catch(Exception e) {
		throw handleException(e);
	} 
	
}
/**
 * inserisce/elimina dalla tabella VSX_CHIUSURA le chiavi dei doc.contabili
 * selezionati/deselezionati dall'utente
 */
public java.util.BitSet setSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet oldSelection, java.util.BitSet newSelection) throws it.cnr.jada.action.BusinessProcessException {

	try 
	{
		((RicercaDocContComponentSession)createComponentSession()).setSelectionPerRiportaAvantiEvoluto(
			context.getUserContext(),
			(V_obb_acc_xxxBulk)getModel(),
			bulks,
			oldSelection,
			newSelection);
		return newSelection;
	} catch(Exception e) {
		throw handleException(e);
	} 
}
}
