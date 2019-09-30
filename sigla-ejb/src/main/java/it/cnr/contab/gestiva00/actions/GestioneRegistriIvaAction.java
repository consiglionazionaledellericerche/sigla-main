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

package it.cnr.contab.gestiva00.actions;

import it.cnr.contab.gestiva00.ejb.*;
import java.util.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.contab.gestiva00.bp.*;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.action.BulkBP;

/**
 * Gestione dei registri iva
 */

public class GestioneRegistriIvaAction  extends StampaAction{
public GestioneRegistriIvaAction() {
	super();
}
/**
 * gestisce l'annullamento di un registro
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doAnnulla(ActionContext context) {

    try {
        fillModel(context);
        StampaRegistriIvaBP bp= (StampaRegistriIvaBP) context.getBusinessProcess();
        Gestione_registri_ivaVBulk bulk=(Gestione_registri_ivaVBulk) bp.getModel();
        bulk.setTipo_stampa("GESTIONE");
        bulk.setTipo_report("ANNULLA");        
        bp.setModel(context,bulk);
        it.cnr.jada.bulk.MTUWrapper wrapper=manageStampa(context, bulk);
       	String message = getMessageFrom(wrapper);
		if (message != null)
            bp.setMessage(message);
        else
        	bp.setMessage("Operazione effettuata in modo corretto");

        Stampa_registri_ivaVBulk stampaBulk= (Stampa_registri_ivaVBulk) wrapper.getBulk();
        bp.setModel(context, stampaBulk);
        bp.commitUserTransaction();
        
        return context.findDefaultForward();
    } catch (Throwable e) {
	    try {
		    context.getBusinessProcess().rollbackUserTransaction();
	    } catch (BusinessProcessException ex) {
		    return handleException(context, ex);
	    }
        return handleException(context, e);
    }
}
/**
 * gestisce l'estrazione dei sezionali in base alla uo selezionata
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doBlankSearchUnita_organizzativa(
	ActionContext context,
	Gestione_registri_ivaVBulk registroIVA) {

    try {
	    registroIVA.setUnita_organizzativa(new Unita_organizzativaBulk());
	    registroIVA.setTipi_sezionali(null);
	    registroIVA.setTipo_sezionale(null);
     	
        return context.findDefaultForward();
    } catch (Throwable e) {
        return handleException(context, e);
    }
}
/**
 * gestisce l'estrazione dei sezionali in base alla uo selezionata
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doBringBackSearchUnita_organizzativa(
	ActionContext context,
	Gestione_registri_ivaVBulk registroIVA,
	Unita_organizzativaBulk uoTrovata) {

    try {
	    GestioneRegistriIvaBP bp = (GestioneRegistriIvaBP)context.getBusinessProcess();
	    if (uoTrovata != null) {
		    //CompoundFindClause clauses = new CompoundFindClause();
		    //clauses.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, uoTrovata.getCd_unita_organizzativa());
		 	Gestione_registri_ivaVBulk registroClause = (Gestione_registri_ivaVBulk)registroIVA.clone();
		 	//In questo caso devo estrarre solo i sez della uo selezionata -->
		 	//tolgo clausola su uo dell'ente
		 	registroClause.setUnita_organizzativa(uoTrovata);
	 		registroIVA.setTipi_sezionali(
		 		bp.createComponentSession().selectTipi_sezionaliByClause(
		 					context.getUserContext(),
		 					registroClause,
		 					new it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk(),
		 					null));
	    } else {
		    registroIVA.setTipi_sezionali(null);
	    }
	    
	    registroIVA.setTipo_sezionale(null);
     	registroIVA.setUnita_organizzativa(uoTrovata);
     	
        return context.findDefaultForward();
    } catch (Throwable e) {
        return handleException(context, e);
    }
}
/**
 * Gestisce la conferma di un registro
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doConferma(ActionContext context) {

    try {
        fillModel(context);
        StampaRegistriIvaBP bp = (StampaRegistriIvaBP) context.getBusinessProcess();
        Gestione_registri_ivaVBulk bulk= (Gestione_registri_ivaVBulk) bp.getModel();
        bulk.setTipo_stampa("GESTIONE");
        bulk.setTipo_report("CONFERMA");
        bp.setModel(context, bulk);
        it.cnr.jada.bulk.MTUWrapper wrapper= manageStampa(context, bulk);
       	String message = getMessageFrom(wrapper);
		if (message != null)
            bp.setMessage(message);
        else
            bp.setMessage("Operazione effettuata in modo corretto");

        Stampa_registri_ivaVBulk stampaBulk= (Stampa_registri_ivaVBulk) wrapper.getBulk();
        bp.setModel(context, stampaBulk);
		bp.commitUserTransaction();
		
        return context.findDefaultForward();
    } catch (Throwable e) {
	    try {
		    context.getBusinessProcess().rollbackUserTransaction();
	    } catch (BusinessProcessException ex) {
		    return handleException(context, ex);
	    }
        return handleException(context, e);
    }
}
}
