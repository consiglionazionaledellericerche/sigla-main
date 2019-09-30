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

package it.cnr.contab.inventario00.comp;

import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.esercizio.bulk.*;
import it.cnr.contab.inventario00.docs.bulk.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.CallableStatement;

import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;

/**
 * Insert the type's description here.
 * Creation date: (03/12/2001 14.30.39)
 * @author: Roberto Fantino
 */
public class InventarioApChComponent
	extends it.cnr.jada.comp.CRUDComponent
	implements ICRUDMgr,IInventarioApChMgr,Cloneable,Serializable{

	// Flag che indica sei il Bulk corrente corrisponde allo stato attuale
	//private Boolean isAttuale;
/**
 * InventarioApChComponent constructor comment.
 */
public InventarioApChComponent() {
	super();
}

/** 
  *  Controlla sul DB l'esistenza di almeno un bene Inventariato
  *    per l'inventario selezionato.
  *  In caso affermativo viene disabilitata la modifica del 
  *   Numero Bene Iniziale
  *    
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param invApCh <code>Inventario_ap_chBulk</code> l'Inventario di riferimento
**/
private void abiltaNumeroBeneIniziale(UserContext aUC, Inventario_ap_chBulk invApCh) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException{
			
	Long max = ((Inventario_beniHome)getHome(aUC,Inventario_beniBulk.class)).getMaxNr_Inventario(invApCh.getPg_inventario());
	
	//invApCh.setInventarioRO(max !=null);
	
	if (max.intValue() == 0)
		max = invApCh.getInventario().getNr_inventario_iniziale();
	invApCh.setInventarioRO(max.intValue()!=0);
	
	//invApCh.getInventario().setNr_inventario_iniziale(max);
}
/** 
  *   Aggiorna l'Inventario sul DB in seguito ad una richiesta di creazione o modifica dello 
  *	stato Aperto/Chiuso.
  *  
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param inv <code>Id_inventarioBulk</code> l'Inventario da aggiornare
**/
private void aggiornaInventario(UserContext userContext, Id_inventarioBulk inv)
	throws	ComponentException,
			it.cnr.jada.persistency.PersistencyException {

	Id_inventarioHome invHome = (Id_inventarioHome)getHome(userContext,Id_inventarioBulk.class);
	inv.setToBeUpdated();
	invHome.update(inv, userContext);
}
/**
  *  Controlla validità Consegnatario
  *    PreCondition:
  *      Si sta tentando di salvare un Inventario senza aver specificato una data di inizio 
  *		validità per il Consegnatario
  *    PostCondition:
  *		Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare l'Inventario
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param invCons il <code>Inventario_consegnatarioBulk</code> Consegnatario dell'Inventario
**/
private void checkConsegnatario(it.cnr.jada.UserContext userContext, Inventario_consegnatarioBulk invCons) throws ComponentException {

	// Controllo su Data Inizio Validità. Campo Obbligatorio
	if (invCons.getDt_inizio_validita()==null){
		throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: la data Inizio Validità del Consegnatario è un dato obbligatorio.");
	}
}
/** 
  *  checkDataApertura - Data non specificata
  *    PreCondition:
  *      Si sta tentando di salvare un Inventario di cui non è stata indicata la data di Apertura.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare l'Inventario
  *
  *  checkDataApertura - Data non in esercizio corrente
  *    PreCondition:
  *      Si sta tentando di salvare un Inventario la cui data di apertura non è nell'esercizio corrente
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare l'Inventario
  *
  *  checkDataApertura - Data non valida su data apertura
  *    PreCondition:
  *      La data indicata è antecedente all'ultima data di APERTURA registrata sul DB, per l'Inventario indicato.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare l'Inventario
  *
  *  checkDataApertura - Data non valida su data chiusura
  *    PreCondition:
  *      La data indicata è antecedente all'ultima data di CHIUSURA registrata sul DB, per l'Inventario indicato
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare l'Inventario
  *
  *  checkDataApertura - Data non valida su data sistema
  *    PreCondition:
  *      La data indicata è posteriore alla data di sistema.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare l'Inventario
  *
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessuna delle precondition verificata
  *    PostCondition:
  *      Consente di proseguire le operazioni
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param invApCh <code>Inventario_ap_chBulk</code> l'Inventario di riferimento
**/
private void checkDataApertura(it.cnr.jada.UserContext aUC, Inventario_ap_chBulk invApCh) 
	throws	ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
	
	// Data Apertura --> campo nullo
	if (invApCh.getDt_apertura() == null)
		throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: indicare una data Apertura");

	// Data Apertura nell'esercizio corrente
	java.util.GregorianCalendar gc = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
	gc.setTime(invApCh.getDt_apertura());
	if (gc.get(java.util.GregorianCalendar.YEAR)!=invApCh.getEsercizio().intValue())
		throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: la data Apertura deve essere all'interno dell'esercizio corrente");
	
	Inventario_ap_chHome invApChHome = (Inventario_ap_chHome)getHome(aUC,Inventario_ap_chBulk.class);
	Inventario_ap_chBulk tmp = invApChHome.findLastAperturaChiusuraObjFor(invApCh.getInventario(),invApCh.getEsercizio());

	if (tmp!=null){
		// Data Apertura > Ultima Data Apertura effettuata 
		if (!invApCh.getStato().equals(Inventario_ap_chBulk.OPEN) && !invApCh.getDt_apertura().after(tmp.getDt_apertura())){
			throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: la data Apertura deve essere superiore all'ultima data Apertura dell'Inventario");
		}
		// Data Apertura >= Ultima Data Chiusura effettuata
		if ( (tmp.getDt_chiusura()!=null)  && (!tmp.getDt_chiusura().equals(EsercizioHome.DATA_INFINITO)) &&  (invApCh.getDt_apertura().before(tmp.getDt_chiusura())) )
			throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: la data Apertura deve essere superiore all'ultima data Chiusura dell'Inventario");

		// Data Apertura > Data odierna
		java.sql.Timestamp dataOdierna = getHome(aUC,Inventario_ap_chBulk.class).getServerTimestamp();
		if (invApCh.getDt_apertura().after(dataOdierna))
			throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: la data Aperura non può essere superiore alla data odierna.");
	}

}
/** 
  *  checkDataChiusura - Data non specificata
  *    PreCondition:
  *      Si sta tentando di salvare un Inventario di cui non è stata indicata la data di Chiusura.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare l'Inventario
  *
  *  checkDataChiusura - Data in esercizio
  *    PreCondition:
  *      Si sta tentando di salvare un Inventario la cui data di Chiusura non è nell'esercizio corrente
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare l'Inventario
  *
  *  checkDataChiusura - Data non valida su apertura
  *    PreCondition:
  *      La data indicata è antecedente alla data di APERTURA per l'Inventario indicato.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare l'Inventario
  *
  *  checkDataChiusura - Data non valida su data sistema
  *    PreCondition:
  *      La data indicata è posteriore alla data di sistema.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare l'Inventario
  *
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessuna delle precondition verificata
  *    PostCondition:
  *      Consente di proseguire le operazioni 
  *  
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param invApCh <code>Inventario_ap_chBulk</code> l'Inventario di riferimento
**/
private void checkDataChiusura(UserContext userContext, Inventario_ap_chBulk invApCh) throws ComponentException, it.cnr.jada.comp.CRUDValidationException, it.cnr.jada.persistency.PersistencyException {

	if(invApCh.getDt_chiusura() != null && (!invApCh.getDt_chiusura().equals(EsercizioHome.DATA_INFINITO)) ){

		// Data Chiusura nell'esercizio corrente
		java.util.GregorianCalendar gc = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
		gc.setTime(invApCh.getDt_chiusura());
		if (gc.get(java.util.GregorianCalendar.YEAR)!=invApCh.getEsercizio().intValue())
			throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: la data Chiusura deve essere all'interno dell'esercizio corrente.");

		// Data Chiusura >= Data Apertura
		if (invApCh.getDt_chiusura().before(invApCh.getDt_apertura()))
			throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: la data Chiusura deve essere superiore alla data apertura.");

		// Data Chiusura <= Data Odierna
		java.sql.Timestamp dataOdierna = getHome(userContext,Inventario_ap_chBulk.class).getServerTimestamp();
		if (invApCh.getDt_chiusura().after(dataOdierna))
			throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: la data Chiusura non può essere superiore alla data odierna.");
	}
	else if (invApCh.isOpen() && (invApCh.getDt_chiusura() == null || (invApCh.getDt_chiusura().equals(EsercizioHome.DATA_INFINITO))) )
		throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: indicare una data di Chiusura");
}
/**
  *  Controlla validità Nr Bene Iniziale - Valore non specificato
  *    PreCondition:
  *      Si sta tentando di salvare un Inventario in cui non si è specificato un numero di 
  *		partenza per la numerazione dei Beni.
  *    PostCondition:
  *		Un messaggio di errore viene visualizzato all'utente per segnalare di indicare un valore
  *
  *  Controlla validità Nr Bene Iniziale - Valore non valido
  *    PreCondition:
  *      Si sta tentando di salvare un Inventario in cui si è specificato un numero di 
  *		partenza per la numerazione dei Beni inferiore a 0.
  *    PostCondition:
  *		Un messaggio di errore viene visualizzato all'utente per segnalare di modificare il valore indicato
  *
  *  Controlli superati
  *    PreCondition:
  *      Nessuna delle precondition verificata
  *    PostCondition:
  *      Consente di proseguire le operazioni
  *
  * @param inventario_ApCh <code>Inventario_ap_chBulk</code> l'Inventario di riferimento
**/
private void checkNumeroBeneIniziale(Inventario_ap_chBulk inventario_ApCh) throws it.cnr.jada.comp.CRUDValidationException{

	Id_inventarioBulk inventario = inventario_ApCh.getInventario();
	
	// Il Nr. Iniziale del Bene Inventariato non è stato specificato
	if (inventario.getNr_inventario_iniziale() == null)
		throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: specificare un valore per il numero iniziale del Bene.");
		
	// Il Nr. Iniziale del Bene Inventariato deve essere >= 0
	if (inventario.getNr_inventario_iniziale().intValue() <= 0)
		throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: il numero iniziale del Bene deve essere maggiore di 0");
}
/**
  *  checkStatoInventario
  *    PreCondition:
  *      Si sta tentando di aprire un Inventario che si trova già nello stato "A", per l'esercizio corrente.
  *    PostCondition:
  *		Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di salvare l'Inventario
  *
  *  Controlli superati
  *    PreCondition:
  *      Nessuna delle precondition verificata
  *    PostCondition:
  *      Consente di proseguire le operazioni
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param invApCh il <code>Inventario_ap_chBulk</code> l'Inventario 
**/
private void checkStatoInventario(it.cnr.jada.UserContext aUC, Inventario_ap_chBulk invApCh) 
	throws	ComponentException, it.cnr.jada.comp.CRUDValidationException,it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {

	Inventario_ap_chHome invApChHome = (Inventario_ap_chHome)getHome(aUC,Inventario_ap_chBulk.class);
  	if (invApChHome.isAperto(invApCh,invApCh.getEsercizio()))
	  throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: l'Inventario è già aperto.\n E' necessario chiudere l'inventario prima di riaprirlo.");
}
/** 
  *  Errore nella validazione inventario.
  *    PreCondition:
  *      Le modifiche apportate allo stato dell'Inventario non hanno superato la validazione, (metodo validaSuInserimento).
  *    PostCondition:
  *      Non  viene consentita la registrazione dell'inventario.
  *
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessun errore rilevato
  *    PostCondition:
  *      Viene consentito il salvataggio.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Bulk da creare
  *
  * @return bulk l'oggetto <code>OggettoBulk</code> creato
**/  
public OggettoBulk creaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException{

	return creaConBulk(aUC, bulk, null);
}
//^^@@
/**
  *  ******** DA RISCRIVERE *****************

  *   Validazione documento.
  *	PreCondition:
  *		Viene richiesta la creazione di un documento passivo e lo stesso non ha superato il metodo 'validaFattura'.
  *	PostCondition:
  *		Non  viene consentita la registrazione della fattura.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta la creazione di un documento passivo e lo stesso ha superato il metodo 'validaFattura'.
  *	PostCondition:
  *		Viene consentita la registrazione del documento.
 */
//^^@@

public OggettoBulk creaConBulk(UserContext aUC,OggettoBulk bulk,it.cnr.contab.inventario00.docs.bulk.OptionRequestParameter status)
	throws ComponentException {

	try{
		Inventario_ap_chHome invApChHome = (Inventario_ap_chHome)getHome(aUC, Inventario_ap_chBulk.class);
		
		Inventario_ap_chBulk invApCh = (Inventario_ap_chBulk)bulk;
		setEsercizio(aUC, invApCh);

		// Controlli
		//validaSuInserimento(aUC, invApCh);

		if (invApCh.getStato() == null || invApCh.getStato().equalsIgnoreCase(Inventario_ap_chBulk.CLOSE))
			validaSuInserimento(aUC, invApCh);
		else
			validaSuModifica(aUC, invApCh);
			

		// Aggiornamento Inventario & InventarioConsegnatario
		aggiornaInventario(aUC, invApCh.getInventario());
		
		//if (invApCh.getInventarioConsegnatario().getCrudStatus()!=OggettoBulk.UNDEFINED){
			//inserisciInventarioConsegnatario(aUC, invApCh, status);
		//}

		// Controlla se è la prima volta che si apre l'Inventario
		if (invApChHome.findLastAperturaChiusuraObjFor(invApCh.getInventario(), invApCh.getEsercizio()) == null){
			inserisciInventarioConsegnatario(aUC, invApCh, status);
		}

		setStatoApertura(aUC,invApCh);
		setDataChiusura(invApCh);

		return super.creaConBulk(aUC,invApCh);
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(bulk,e);
	} catch(it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(bulk,e);
	}
}
/** 
  *  Cerca l'Inventario di cui la Uo di scrivania è responsabile
  *    PreCondition:
  *      La Uo di scrivania non è responsabile di alcun Inventario, (metodo loadInventario).
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
  *
  *  Inizializzazione di una istanza di Inventario_ap_chBulk
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Inventario_ap_chBulk
  *    PostCondition:
  *      Vengono impostati i parametri di base dell'Inventario come il Consegnatario.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'inventario che deve essere istanziato
  *
  * @return <code>OggettoBulk</code> l'inventario inizializzato
**/
public OggettoBulk inizializzaBulkPerInserimento (UserContext aUC,OggettoBulk bulk) throws ComponentException{

	try {
		Inventario_ap_chBulk invApCh = (Inventario_ap_chBulk)super.inizializzaBulkPerInserimento(aUC,bulk);
		
		getHomeCache(aUC).fetchAll(aUC);
		
		// Carico l'Inventario di cui la U.O. di scrivania è responsabile
		loadInventario(aUC,invApCh);
		
		// Controllo che sia la prima volta che inserisco questo Inventario
		SQLBuilder sql = getHome(aUC,Inventario_ap_chBulk.class).createSQLBuilder();
		sql.addSQLClause("AND", "PG_INVENTARIO", sql.EQUALS, invApCh.getPg_inventario());
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));
		
		if (sql.executeExistsQuery(getConnection(aUC))){
			invApCh = loadInventarioApChAttuale(aUC);			
			abiltaNumeroBeneIniziale(aUC,invApCh);
			invApCh.setIsAttuale(Boolean.TRUE);
			if (invApCh.isClose()){
				Inventario_ap_chBulk old = loadInventarioApChAttuale(aUC);
				invApCh = (Inventario_ap_chBulk)old.clone();
				invApCh.setCrudStatus(it.cnr.jada.bulk.OggettoBulk.TO_BE_CREATED);
				invApCh.setDt_chiusura(null);
				invApCh.setDt_apertura(it.cnr.contab.compensi00.docs.bulk.CompensoBulk.incrementaData(loadUltimaDt_Chiusura(aUC,invApCh)));
				
			}
		}

		// Carico il Consegnatario e il Delegato associati all'inventario di scrivania
		loadInventarioConsegnatario(aUC,invApCh);
		getHomeCache(aUC).fetchAll(aUC);
					
		/* Controllo l'esistenza di Beni Inventariati per abilitare/disabilitare
		  la modifica del Numero Bene Iniziale dell'Inventario. */
		//abiltaNumeroBeneIniziale(aUC, invApCh);		

		invApCh.setIsAttuale(Boolean.TRUE);

		return invApCh;
		
	} catch(java.sql.SQLException e) {
		throw handleException(bulk,e);
	}  catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(bulk,e);
	} catch(it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(bulk,e);
	}
}
/**    
  *  Inizializzazione di una istanza di Inventario_ap_chBulk per modifica
  *    PreCondition:
  *      E' stata richiesta l'inizializzazione di una istanza di Inventario_ap_chBulk per modifica
  *    PostCondition:
  *      Viene caricato il Consegnatario dell'Inventario ed abilita la possibilità di modificare 
  *		il valore di riferimento iniziale per la numerazione dei Beni facenti parte dell'Inventario.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'Inventario che deve essere inizializzato
  *
  * @return bulk <code>OggettoBulk</code> l'Inventario inizializzato
**/
public OggettoBulk inizializzaBulkPerModifica (UserContext aUC,OggettoBulk bulk) throws ComponentException{

	//try {		
		//Inventario_ap_chBulk invApCh = (Inventario_ap_chBulk)bulk;
		//isAttuale = invApCh.getIsAttuale();
		
		//// Carico il Consegnatario e il Delegato associati all'inventario di scrivania
		//loadInventarioConsegnatario(aUC, invApCh);

		//// Controllo l'esistenza di Beni Inventariati per abilitare/disabilitare
		//// la modifica del Numero Bene Iniziale dell'Inventario.
		//abiltaNumeroBeneIniziale(aUC, invApCh);
	
		////if (invApCh.equalsByPrimaryKey((Inventario_ap_chBulk)loadInventarioApChAttuale(aUC))){
			////invApCh.setIsAttuale(new Boolean(true));
			////if (invApCh.isClose()){
				////invApCh.setDt_chiusura(null);
				////invApCh.setDt_apertura(it.cnr.contab.compensi00.docs.bulk.CompensoBulk.incrementaData(loadUltimaDt_Chiusura(aUC,invApCh)));
				
			////}
		////}
		//return invApCh;
		
	//} catch(it.cnr.jada.persistency.PersistencyException e) {
		//throw handleException(bulk,e);
	//} catch(it.cnr.jada.persistency.IntrospectionException e) {
		//throw handleException(bulk,e);
	//}

	try {		
		Inventario_ap_chBulk invApCh = (Inventario_ap_chBulk)bulk;
		
		// Carico il Consegnatario e il Delegato associati all'inventario di scrivania
		loadInventarioConsegnatario(aUC, invApCh);
		getHomeCache(aUC).fetchAll(aUC);

	
		return invApCh;
		
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(bulk,e);
	} catch(it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(bulk,e);
	}

}
/** 
  *  Cerca l'Inventario di cui la Uo di scrivania è responsabile
  *    PreCondition:
  *      La Uo di scrivania non è responsabile di alcun Inventario, (metodo loadInventario).
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
  *
  *  Inizializzazione di una istanza di Inventario_ap_chBulk per Ricerca
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Inventario_ap_chBulk per Ricerca
  *    PostCondition:
  *      L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato
  *			per l'operazione inserimento criteri di ricerca.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'Inventario che deve essere istanziato
  *
  * @return <code>OggettoBulk</code> l'Inventario inizializzato
**/
public OggettoBulk inizializzaBulkPerRicerca(UserContext aUC,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	try {

		Inventario_ap_chBulk invApCh = (Inventario_ap_chBulk)super.inizializzaBulkPerRicerca(aUC,bulk);

		// Carico l'Inventario di cui la U.O. di scrivania è responsabile
		loadInventario(aUC,invApCh);

		getHomeCache(aUC).fetchAll(aUC);
		return invApCh;
		
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(bulk,e);
	} catch(it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(bulk,e);
	}

}
/** 
  *  Cerca l'Inventario di cui la Uo di scrivania è responsabile
  *    PreCondition:
  *      La Uo di scrivania non è responsabile di alcun Inventario, (metodo loadInventario).
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
  *
  *  Inizializzazione di una istanza di Inventario_ap_chBulk per Ricerca
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Inventario_ap_chBulk per Ricerca
  *    PostCondition:
  *      L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato
  *			per l'operazione inserimento criteri di ricerca.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'Inventario che deve essere istanziato
  *
  * @return <code>OggettoBulk</code> l'Inventario inizializzato
**/
public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext aUC,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	return inizializzaBulkPerRicerca( aUC, bulk);

}
/** 
  * In seguito alla richiesta di creazione di una nuova istanza sulla tabella INVENTARIO_AP_CH,
  *	 (si sta aprendo l'Inventario per la prima volta), inserisce una riga nella tabella INVENTARIO_CONSEGNATARIO
  *  
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param invC <code>Inventario_consegnatarioBulk</code> il Consegnatario specificato
**/
private void inserisciInventarioConsegnatario(UserContext userContext, Inventario_ap_chBulk invApCh, it.cnr.contab.inventario00.docs.bulk.OptionRequestParameter status)
	throws	ComponentException,	it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {



	String msg = "Attenzione: la data di inizio validità del Consegnatario non può essere posteriore alla data di apertura dell'Inventario.\nLa data di inizio validità del primo Consegnatario verrà modificata.\nContinuare?";
		
	Inventario_consegnatarioBulk invC = invApCh.getInventarioConsegnatario();
	
	Inventario_consegnatarioHome invCHome = (Inventario_consegnatarioHome)getHome(userContext,Inventario_consegnatarioBulk.class);	

	Inventario_consegnatarioBulk first_consegnatario = invCHome.findFirstInventarioConsegnatarioFor(invApCh.getInventario());

	/* Se NON esistono Consegnatari definiti per questo Inventario, si assegna al Consegnatario 
		di default la data di apertura dell'Inventario stesso. */
	if (first_consegnatario == null){
		invC.setDt_inizio_validita(invApCh.getDt_apertura());
		invC.setToBeCreated();
		invCHome.insert(invC, userContext);
		return;
	}
	// Esiste già un Consegnatario definito per l'Inventario 
	else {
		/* Il primo Consegnatario definito per l'Inventario, 
			ha data di inizio validità posteriore all'apertura dell'Inventario. 
			Lancia un messaggio all'utente che spiega che la data di inizio validità del primo
			Consegnatario verrà modificata */
		if (status == null && first_consegnatario.getDt_inizio_validita().after(invApCh.getDt_apertura())){
			throw new it.cnr.jada.comp.OptionRequestException("onCheckDataConsegnatarioFailed",msg);	
		}
		/* Il primo Consegnatario definito per l'Inventario, 
			ha data di inizio validità posteriore all'apertura dell'Inventario. 
			L'utente ha accettato la modifica. */
		else if (status != null && first_consegnatario.getDt_inizio_validita().after(invApCh.getDt_apertura())){
			Inventario_consegnatarioBulk nuovo_consegnatario = (Inventario_consegnatarioBulk)first_consegnatario.clone();
			nuovo_consegnatario.setDt_inizio_validita(invApCh.getDt_apertura());
			nuovo_consegnatario.setToBeCreated();
			nuovo_consegnatario.setUser(invApCh.getUser());
			//makeBulkPersistent(aUC, nuovo_consegnatario);
			invCHome.insert(nuovo_consegnatario, userContext);
			first_consegnatario.setToBeDeleted();
			invCHome.delete(first_consegnatario, userContext);
			//deleteBulk(aUC, invC);
			
			//invApCh.setInventarioConsegnatario(nuovo_consegnatario);
		}
	}	
	
}
/**
  *  loadInventario - Carica l'inventario di cui la U.O. di scrivania è responsabile.
  *    PreCondition:
  *      La UO di scrivania non è responsabile di alcun Inventario.
  *    PostCondition:
  *		Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di continuare nelle operazioni.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param invApCh <code>Inventario_ap_chBulk</code> l'Inventario modello
**/
private void loadInventario(UserContext aUC,Inventario_ap_chBulk invApCh) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException{

	Id_inventarioHome invHome = (Id_inventarioHome)getHome(aUC,Id_inventarioBulk.class);
	Id_inventarioBulk invResp = invHome.findInventarioRespFor(aUC);
	if (invResp == null)
		throw new it.cnr.jada.comp.ApplicationException("L'Unità Organizzativa selezionata non è responsabile di alcun Inventario");

	invApCh.setInventario(invResp);
}
/** 
  *  Cerca l'Inventario di cui la Uo di scrivania è responsabile
  *    PreCondition:
  *      La Uo di scrivania non è responsabile di alcun Inventario.
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
  *
  *  Carica l'Inventario di competenza
  *    PreCondition:
  *      E' stato richiesto di caricare l'Inventario di cui la UO di scrivania è responsabile.
  *    PostCondition:
  *      Viene caricato l'Inventario di competenza impostando come clausole di ricerca che
  *		la data di apertura sia l'ultima registrata sul DB; questo per essere sicuri che lo stato
  *		dell'Inventario carcicato sia quello attuale.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  *
  * @return invApCH <code>Inventario_ap_chBulk</code> l'Inventario allo stato attuale
**/
public Inventario_ap_chBulk loadInventarioApChAttuale(UserContext aUC) 
	throws it.cnr.jada.comp.ComponentException, 
			it.cnr.jada.persistency.PersistencyException, 
			it.cnr.jada.persistency.IntrospectionException{

	// Carica l'Inventario per la UO di scrivania
	Id_inventarioHome invHome = (Id_inventarioHome)getHome(aUC,Id_inventarioBulk.class);
	Id_inventarioBulk inventario = invHome.findInventarioRespFor(aUC);
	if (inventario == null)
		throw new it.cnr.jada.comp.ApplicationException("L'Unità Organizzativa selezionata non è responsabile di alcun Inventario");

	
	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	String query_per_max = "(SELECT MAX(DT_APERTURA) FROM " + schema + "INVENTARIO_AP_CH " +
				"WHERE PG_INVENTARIO = " + inventario.getPg_inventario() + 
				" AND ESERCIZIO = "+ it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC) + ")";
	
	it.cnr.jada.bulk.BulkHome home = getHome(aUC, Inventario_ap_chBulk.class);
	it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
	
	sql.addSQLClause("AND", "DT_APERTURA = " + query_per_max);
	sql.addSQLClause("AND", "PG_INVENTARIO", sql.EQUALS, inventario.getPg_inventario());
	sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));

	it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
        if (!broker.next())
            return null;
 
	Inventario_ap_chBulk invApCh=(Inventario_ap_chBulk) broker.fetch(Inventario_ap_chBulk.class);
	invApCh.setIsAttuale(new Boolean(true));
	broker.close();
	return invApCh;
}
/** 
  * Carica l'Oggetto Inventario_consegnatario associato all'Inventario
  *  caricato precedentemente --> solo in lettura.
  *
  * Se l'inventario viene aperto per la prima volta, allora viene 
  *  creato e inserito un nuovo record nella tabella Inventario_consegnatario
  *  con Consegnatario = Responsabile della U.O. di scrivania
  *  
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param invApCh <code>Inventario_ap_chBulk</code> l'Inventario
**/
private void loadInventarioConsegnatario(UserContext aUC,Inventario_ap_chBulk invApCh) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException{

	try{
		Inventario_consegnatarioBulk consegnatario = null;
		Inventario_consegnatarioHome consegnatarioHome = (Inventario_consegnatarioHome)getHome(aUC, Inventario_consegnatarioBulk.class);
		SQLBuilder sql = consegnatarioHome.createSQLBuilder();

		sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,invApCh.getPg_inventario());
		sql.addSQLClause("AND","DT_INIZIO_VALIDITA",sql.LESS_EQUALS,it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
		sql.openParenthesis("AND");
		sql.addSQLClause("AND","DT_FINE_VALIDITA",sql.GREATER_EQUALS,it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
		sql.openParenthesis("OR");
		sql.addSQLClause("AND","DT_FINE_VALIDITA",sql.ISNULL,null);
		sql.closeParenthesis();
		sql.closeParenthesis();
	it.cnr.jada.persistency.Broker broker = consegnatarioHome.createBroker(sql);
      
	if (broker.next()){
		consegnatario = (Inventario_consegnatarioBulk) broker.fetch(Inventario_consegnatarioBulk.class);
		broker.close();
	} else {
		consegnatario = new Inventario_consegnatarioBulk();
		consegnatario.setInventario((Id_inventarioBulk)getHome(aUC,Id_inventarioBulk.class).findByPrimaryKey(new Id_inventarioBulk(invApCh.getPg_inventario())));
		if (consegnatario.getInventario().getUoResp()!=null && consegnatario.getInventario().getUoResp().getResponsabile()!=null)
			consegnatario.setConsegnatario(consegnatario.getInventario().getUoResp().getResponsabile());
		consegnatario.setUser(aUC.getUser());	

	}

	invApCh.setInventarioConsegnatario(consegnatario);
	} catch (javax.ejb.EJBException e){
		throw new ComponentException(e);
	}	
}
/**
  * Carica l'ultima Data di Chiusura registrata per l'Inventario di Competenza.
  *  
**/  
private java.sql.Timestamp loadUltimaDt_Chiusura(UserContext aUC, Inventario_ap_chBulk inventario) throws it.cnr.jada.comp.ComponentException{

	
	java.sql.Statement st = null;
	java.sql.Timestamp dt_chiusura = null;
	
	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	String query = "SELECT MAX(DT_CHIUSURA) FROM " + schema + "INVENTARIO_AP_CH " +
				"WHERE PG_INVENTARIO = " + inventario.getPg_inventario() + 
				" AND ESERCIZIO = "+ it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC) +
				" AND STATO = '" + inventario.CLOSE + "'";

	try{				
		st = getConnection(aUC).createStatement();
		java.sql.ResultSet rs = st.executeQuery(query);
		if (rs.next()){
			dt_chiusura = rs.getTimestamp(1);
		}
		return dt_chiusura;		
	} catch (java.sql.SQLException e){
		throw new it.cnr.jada.comp.ComponentException();
	} finally{
		try {
			if (st != null)
				st.close();
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		}
	}	
}
/** 
  *  Errore sulle date indicate
  *    PreCondition:
  *      E' stata richiesta un modifica allo stato dell'inventario ed i controlli effettuati
  *		sulle date di apertura e chiusura non sono stati superati.
  *    PostCondition:
  *      Viene visualizzato un messaggio con la spiegazione dell'errore.
  *
  *  Errore 
  *    PreCondition:
  *       E' stata richiesta un modifica allo stato dell'inventario ed i controlli effettuati
  *		sul valore indicato come riferimento per il primo bene, non sono stati superati.
  *    PostCondition:
  *      Viene visualizzato un messaggio con la spiegazione dell'errore.
  *  
  *  Modifica Bene
  *    PreCondition:
  *      E' stata generata la richiesta di modificare lo stato di un Inventario.
  *		Le modifiche passano le validazioni.
  *    PostCondition:
  *      Viene consentito il salvataggio.
  *  
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Bulk da modificare
  *
  * @return l'oggetto <code>OggettoBulk</code> modificato
**/ 
public OggettoBulk modificaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException {

	try {
		Inventario_ap_chBulk invApCh = (Inventario_ap_chBulk)bulk;
		boolean wasClose = invApCh.isClose();
		validaSuModifica(aUC, invApCh);

		if (!invApCh.isInventarioRO()){
			// Controllo numero bene iniziale
			checkNumeroBeneIniziale(invApCh);

			invApCh.getInventario().setToBeUpdated();
			aggiornaInventario(aUC, invApCh.getInventario());
		}

		setStatoApertura(aUC,invApCh);
		setDataChiusura(invApCh);

		if (/*isAttuale!=null && isAttuale.booleanValue() && */wasClose){
			Inventario_ap_chBulk clone = (Inventario_ap_chBulk)invApCh.clone();
			insertBulk(aUC, clone);
			return clone;
			
		}
		return super.modificaConBulk(aUC,invApCh);
		
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(bulk,e);
	} catch(it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(bulk,e);
	}
}
/** 
  *  Ricerca di un Stato storico
  *    PreCondition:
  *      E' stata generata la richiesta di ricercare una situazione nello storico dell'Inventario
  *		associato alla UO di scrivania.
  *    PostCondition:
  *		E' stato creato il SQLBuilder con le clausole implicite (presenti nell'istanza di Inventario_ap_chBulk),
  *		ed è stata aggiunta la clausola che l'Inventario sia quello associato alla UO di scrivania,
  *		e appartenga all'esercizio atttuale.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione
  * @param bulk <code>OggettoBulk</code> l'Inventario modello
  *
  * @return sql <code>SQLBuilder</code> Risultato della selezione.
**/
protected it.cnr.jada.persistency.sql.Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {

	it.cnr.jada.persistency.sql.SQLBuilder sql = (SQLBuilder)super.select(userContext,clauses,bulk);
	
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	sql.addSQLClause("AND", "PG_INVENTARIO", sql.EQUALS, ((Inventario_ap_chBulk)bulk).getPg_inventario());

	sql.addOrderBy("DT_APERTURA");
	
	return sql;
}
/** 
  *	Imposta la data di chiusura dell'Inventario. Se la data non è stata specificata, 
  *	ci si trova nella situazione di un Inventario in stato "A", quindi la Data di Chiusura
  *	viene impostata come DATA_INFINITO.
  *
  * @param invApCh <code>Inventario_ap_chBulk</code> l'Inventario
**/ 
private void setDataChiusura(Inventario_ap_chBulk invApCh) {
					
	if(invApCh.getDt_chiusura()==null)
		invApCh.setDt_chiusura(EsercizioHome.DATA_INFINITO);
}
/** 
  *	Imposta l'esercizio dell'Inventario pari a quello di scrivania.  
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta  
  * @param invApCh <code>Inventario_ap_chBulk</code> l'Inventario
**/  
private void setEsercizio(UserContext aUC, Inventario_ap_chBulk invApCh) {
					
	invApCh.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));
}
/** 
  *	Imposta lo stato dell'Inventario.
  *
  * @param invApCh <code>Inventario_ap_chBulk</code> l'Inventario
 * @throws ComponentException 
**/  
private void setStatoApertura(UserContext aUC,Inventario_ap_chBulk invApCh) throws ComponentException {
					
	if ( (invApCh.getDt_chiusura() != null) && (!invApCh.getDt_chiusura().equals(EsercizioHome.DATA_INFINITO)) ){
		invApCh.setStato(invApCh.CLOSE);
		//chiamare procedura che lancia il job
		
		//r.p. 13/01/2016 disabilitato creava più problemi che benefici
//		if (invApCh.getStato().equals(invApCh.CLOSE))
//			callAmmortamentoBeni(aUC);
	}
	else
		invApCh.setStato(invApCh.OPEN);
}
/** 
  *  Valida Inventario per INSERIMENTO - Inventario in stato "A"
  *    PreCondition:
  *      E' stato richiesto di impostare lo stato dell'Inventario ad "Aperto",ma lo stato e già "A".
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità 
  *		di proseguire con il salvataggio.
  *
  *  Valida Inventario per INSERIMENTO - data di Apertura non valida.
  *    PreCondition:
  *      La data di apertura specificata dall'utente non è valida.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità 
  *		di proseguire con il salvataggio.
  *
  *  Valida Inventario per INSERIMENTO - valore di riferimento per i Beni non valido
  *    PreCondition:
  *      Il valore da tenere come riferimento per la numerazione dei Beni che andranno 
  *		in Inventario non è valido.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità 
  *		di proseguire con il salvataggio.
  *
  *  Valida Inventario per INSERIMENTO - Consegnatario non valido
  *    PreCondition:
  *      I dati specificati dall'utente, relativi al Consegnatario non sono validi.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità 
  *		di proseguire con il salvataggio.
  *
  *  Controlli superati
  *    PreCondition:
  *      Nessuna delle precondition verificata
  *    PostCondition:
  *      Consente di proseguire le operazioni
  *    
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param invApCh <code>Inventario_beniBulk</code> l'Inventario di riferimento
**/
private void validaSuInserimento(UserContext aUC,Inventario_ap_chBulk invApCh)
	throws	ComponentException,
			it.cnr.jada.persistency.PersistencyException,
			it.cnr.jada.persistency.IntrospectionException{

	// Controllo se Inventario già aperto
	//	--> necessario prima chiuderlo
	checkStatoInventario(aUC, invApCh);
	
	// Controllo Data Apertura
	checkDataApertura(aUC, invApCh);
	
	// Controllo numero bene iniziale
	checkNumeroBeneIniziale(invApCh);
	
	// Controlli sul Consegnatario
	//checkConsegnatario(aUC, invApCh.getInventarioConsegnatario());
}
/** 
  *  Valida Inventario per MODIFICA - data di Apertura non valida.
  *    PreCondition:
  *      La data di apertura specificata dall'utente non è valida.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità 
  *		di proseguire con il salvataggio.
  *
  *  Valida Inventario per MODIFICA - data di Chiusura non valida.
  *    PreCondition:
  *      La data di chiusura specificata dall'utente non è valida.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità 
  *		di proseguire con il salvataggio.
  *
  *  Valida Inventario per MODIFICA - valore di riferimento per i Beni non valido
  *    PreCondition:
  *      Il valore da tenere come riferimento per la numerazione dei Beni che andranno 
  *		in Inventario non è valido.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità 
  *		di proseguire con il salvataggio.
  *    
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param invApCh <code>Inventario_beniBulk</code> l'Inventario di riferimento
**/
private void validaSuModifica (UserContext aUC,Inventario_ap_chBulk invApCh)
	throws	ComponentException,
			it.cnr.jada.persistency.PersistencyException,
			it.cnr.jada.persistency.IntrospectionException {

	// Controllo Data Apertura
	checkDataApertura(aUC, invApCh);
	
	// Controllo su data Chiusura
	checkDataChiusura(aUC, invApCh);
	
	// Controllo numero bene iniziale
	checkNumeroBeneIniziale(invApCh);
}
/**  
 *  Richiama la procedura che provvede ad effettuare il calcolo dell'ammortamento dei beni.
 *    PreCondition:
 *      E' stata generata la richiesta di Calcolo ammortamento dei beni.
 *    PostCondition:
 *      Viene richiamata la procedura di Calcolo ammortamento, (CNRCTB400.Ammortamentobene).
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
 * @param file il <code>Inventario_ap_chBulk</code> inventario  da processare.
**/ 
private void callAmmortamentoBeni(
	UserContext userContext)
	throws  it.cnr.jada.comp.ComponentException {
	LoggableStatement cs = null;
	try	{
		cs = new LoggableStatement(getConnection(userContext), 
			"{ call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
			"CNRCTB400.AmmortamentoBeni(?,?,?)}",false,this.getClass());
		cs.setInt(1,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue());// ESERCIZIO
		cs.setString(2,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));// CDS
		cs.setString(3,it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));	// USER
		cs.executeQuery();
	} catch (Throwable e) {
		throw handleException(e);
	} finally {
		try {
			if (cs != null) cs.close();
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		}
	}
}
}
