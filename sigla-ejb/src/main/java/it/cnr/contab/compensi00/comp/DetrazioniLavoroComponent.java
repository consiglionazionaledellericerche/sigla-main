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

package it.cnr.contab.compensi00.comp;

import java.io.Serializable;
import java.rmi.RemoteException;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;

public class DetrazioniLavoroComponent extends it.cnr.jada.comp.CRUDComponent implements IDetrazioniLavoroMgr,Cloneable,Serializable{
/**
 * DetrazioniLavoroComponentSession constructor comment.
 */
public DetrazioniLavoroComponent() {
	super();
}
/** 
  *  Tutti i controlli di validazione del periodo di inizio/fine validita' del nuovo record
  *  superati
  *    PreCondition:
  *      la tabella contiene altri record con stessa chiave di quello che sto inserendo
  *		 e la data di inizio validità del nuovo record e' successiva a quella 
  *		 del record (con stessa chiave) piu' recente in tabella (cioe' che ha data fine 
  *		 validita = infinito)
  *    PostCondition:
  *      Consente l'inserimento del Detrazioni
  
  *  Riscontrata condizione di errore.
  *    PreCondition:
  *      Si è verificato un errore.
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione, si è verificato un errore".
**/
public OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException{

	Detrazioni_lavoroBulk detraz = (Detrazioni_lavoroBulk)bulk;
	validaDetrazione(userContext, detraz);

    return super.creaConBulk(userContext, detraz);
}
/** 
  *  Tutti i controlli per la cancellazione del record sono stati superati
  *    	PreCondition:
  *		   	Deve esistere un record con chiave uguale a quella del record da cancellare e con data fine validita 
  *		   	uguale a data di inizio validita del record da cancellare meno un giorno
  *    PostCondition:
  *        	Cancello il record ed aggiorno il record di periodo precendente a quello cancvellato 
  *		   	mettendo la sua data di fine ad infinito (31/12/2200)
  *
  *  Riscontrata condizione di errore.
  *    PreCondition:
  *        	la tabella non contiene un altro record con stessa chiave di quello che sto cancellando
  *			e con data fine validita uguale alla data inizio validita del record da cancellare meno un giorno
  *    PostCondition:
  *        	Viene inviato il messaggio "Attenzione, deve esistere almeno un periodo".
**/
public void eliminaConBulk (UserContext userContext,OggettoBulk bulk) throws ComponentException {

	Detrazioni_lavoroBulk detr = (Detrazioni_lavoroBulk)bulk;
	if (!isCancellabile(userContext,detr))
		throw new it.cnr.jada.comp.ApplicationException("Attenzione, deve esistere almeno un periodo");
	
	super.eliminaConBulk(userContext, detr);
}
private boolean isCancellabile(UserContext aUC,OggettoBulk bulk) throws ComponentException {
	
	try {

		int rc = -1;
		boolean cancellabile = true;

		Detrazioni_lavoroBulk detrazioni_lavoro = (Detrazioni_lavoroBulk)bulk;
		Detrazioni_lavoroHome detrazioni_lavoroHome = (Detrazioni_lavoroHome)getHome(aUC,detrazioni_lavoro);
		
		it.cnr.jada.persistency.sql.SQLBuilder sql = detrazioni_lavoroHome.createSQLBuilder();
		sql.addSQLClause("AND","TI_LAVORO",sql.EQUALS,detrazioni_lavoro.getTi_lavoro());	
		sql.addSQLClause("AND","IM_INFERIORE",sql.EQUALS,detrazioni_lavoro.getIm_inferiore());		   
		rc = sql.executeCountQuery(getHomeCache(aUC).getConnection());
	
		if (rc == 1)
			cancellabile = false;
		else{
			if (!detrazioni_lavoro.getDt_fine_validita().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO) && (detrazioni_lavoro.isToBeUpdated() || detrazioni_lavoro.isToBeDeleted()))
				throw new it.cnr.jada.comp.ApplicationException("Attenzione, l'unico intervallo che è possibile cancellare/modificare è l'ultimo");	  
			else{

				sql.addSQLClause("AND","DT_FINE_VALIDITA",sql.EQUALS,it.cnr.contab.compensi00.docs.bulk.CompensoBulk.decrementaData(detrazioni_lavoro.getDt_inizio_validita()));

				Detrazioni_lavoroBulk penultimoRimborso = (Detrazioni_lavoroBulk)detrazioni_lavoroHome.fetchAll(detrazioni_lavoroHome.createBroker(sql)).get(0);

				penultimoRimborso.setDt_fine_validita(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO);
				updateBulk(aUC, penultimoRimborso);
             }
       }

       return cancellabile;

	} catch (Throwable e){
	   throw handleException(e);
	}
}
/** 
  *  Tutti i controlli del periodo di inizio/fine validita del nuovo Detrazioni superati.
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      Viene consentito il salvataggio del nuovo Detrazioni
  *  Periodo di Inizio/Fine validita del nuovo Detrazioni non valido.
  *    PreCondition:
  *      Si è verificato un errore non salvo.
  *    PostCondition:
  *      Viene inviato il messaggio : "Attenzione sovrapposizione con intervalli di validità preesistenti"
**/
private void validaDetrazione(UserContext aUC,Detrazioni_lavoroBulk detraz) throws ComponentException{

	try{

		detraz.validaImporti();

		Detrazioni_lavoroHome home = (Detrazioni_lavoroHome)getHome(aUC,Detrazioni_lavoroBulk.class);
		if (!home.checkValidita(aUC, detraz))
			throw new it.cnr.jada.comp.ApplicationException("Attenzione sovrapposizione con intervalli di validità preesistenti");

//		if (!home.checkIntervallo(detraz))
//			throw new it.cnr.jada.comp.ApplicationException("L'importo inferiore inserito non è compatibile con un intervallo precedentemente inserito");

	}catch(it.cnr.jada.bulk.ValidationException ex){
		throw new it.cnr.jada.comp.ApplicationException(ex);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
}
