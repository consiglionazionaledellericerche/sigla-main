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

/**
 * Insert the type's description here.
 * Creation date: (05/12/2001 12.02.34)
 * @author: CNRADM
 */
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

public class DetrazioniFamiliariComponent extends it.cnr.jada.comp.CRUDComponent implements IDetrazioniFamiliariMgr,Cloneable,Serializable{
/**
 * DetrazioniFamiliariComponent constructor comment.
 */
public DetrazioniFamiliariComponent() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (05/12/2001 12.03.36)
 */
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

	Detrazioni_familiariBulk detr = (Detrazioni_familiariBulk)bulk;
	if (!isCancellabile(userContext,detr))
		throw new it.cnr.jada.comp.ApplicationException("Attenzione, deve esistere almeno un periodo");
	
	super.eliminaConBulk(userContext, detr);
}
private boolean isCancellabile(UserContext aUC,OggettoBulk bulk) throws ComponentException {
	
	try {

		int rc = -1;
		boolean cancellabile = true;

		Detrazioni_familiariBulk detr = (Detrazioni_familiariBulk)bulk;
		Detrazioni_familiariHome detrHome = (Detrazioni_familiariHome)getHome(aUC,detr);
		
		it.cnr.jada.persistency.sql.SQLBuilder sql = detrHome.createSQLBuilder();
		sql.addSQLClause("AND","TI_PERSONA",sql.EQUALS,detr.getTi_persona());
		sql.addSQLClause("AND","IM_INFERIORE",sql.EQUALS,detr.getIm_inferiore());
		sql.addSQLClause("AND","NUMERO",sql.EQUALS,detr.getNumero());
		rc = sql.executeCountQuery(getHomeCache(aUC).getConnection());
	
		if (rc == 1)
			cancellabile = false;
		else{
			if (!detr.getDt_fine_validita().equals(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO) && (detr.isToBeUpdated() || detr.isToBeDeleted()))
				throw new it.cnr.jada.comp.ApplicationException("Attenzione, l'unico intervallo che è possibile cancellare/modificare è l'ultimo");	  
			else{
				sql.addSQLClause("AND","DT_FINE_VALIDITA",sql.EQUALS,it.cnr.contab.compensi00.docs.bulk.CompensoBulk.decrementaData(detr.getDt_inizio_validita()));

				Detrazioni_lavoroBulk penultimoRimborso = (Detrazioni_lavoroBulk)detrHome.fetchAll(detrHome.createBroker(sql)).get(0);

				penultimoRimborso.setDt_fine_validita(it.cnr.contab.config00.esercizio.bulk.EsercizioHome.DATA_INFINITO);
				updateBulk(aUC, penultimoRimborso);
             }
       }

       return cancellabile;

	} catch (Throwable e){
	   throw handleException(e);
	}
}
public void validaCreaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException {

	checkSQLConstraints(userContext,bulk,true,false);
	super.validaCreaConBulk(userContext, bulk);

	validaDetrazione(userContext, (Detrazioni_familiariBulk)bulk);
	validaDate(userContext, (Detrazioni_familiariBulk)bulk);
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
private void validaDate(UserContext userContext,Detrazioni_familiariBulk detraz) throws ComponentException{

	try{

		Detrazioni_familiariHome home = (Detrazioni_familiariHome)getHome(userContext,Detrazioni_familiariBulk.class);
		if (!home.checkValidita(userContext,detraz))
				throw new it.cnr.jada.comp.ApplicationException("Attenzione sovrapposizione con intervalli di validità preesistenti");

	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
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
private void validaDetrazione(UserContext userContext,Detrazioni_familiariBulk detraz) throws ComponentException{

	try{

		if (detraz.getNumero()==null)
			throw new it.cnr.jada.comp.ApplicationException("Il campo Numero non può essere vuoto");
		if (detraz.getNumero()!=null && detraz.getNumero().intValue()==0)
			throw new it.cnr.jada.comp.ApplicationException("Il campo Numero deve essere maggiore di zero");

		detraz.validaImporti();

	}catch(it.cnr.jada.bulk.ValidationException ex){
		throw new it.cnr.jada.comp.ApplicationException(ex);
	}
}
public void validaModificaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException {

	checkSQLConstraints(userContext,bulk,true,false);
	super.validaModificaConBulk(userContext, bulk);

	validaDetrazione(userContext, (Detrazioni_familiariBulk)bulk);
}
}
