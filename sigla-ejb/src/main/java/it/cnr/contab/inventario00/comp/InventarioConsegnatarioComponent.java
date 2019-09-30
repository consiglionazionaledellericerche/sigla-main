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
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

/**
 * Insert the type's description here.
 * Creation date: (04/12/2001 15.25.02)
 * @author: Roberto Fantino
 */
public class InventarioConsegnatarioComponent
	extends it.cnr.jada.comp.CRUDComponent
	implements ICRUDMgr,IInventarioConsegnatarioMgr,Cloneable,Serializable{
/**
 * InventarioConsegnatarioComponent constructor comment.
 */
public InventarioConsegnatarioComponent() {
	super();
}
/**
  *  Controlla la data di fine validità
  *    PreCondition:
  *      La data specificata di fine validità è superiore alla data di inizio validità
  *    PostCondition:
  *		 Un messaggio di errore viene visualizzato all'utente per indicare che la data di fine validità
  *		deve essere posteriore a quella di inizio.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param invC <code>Inventario_consegnatarioBulk</code> l'OggettoBulk che contiene le 
  *	  informazioni relative all'Inventario ed al suo Consegnatario
**/
private void checkDataFineValidita(UserContext aUC, Inventario_consegnatarioBulk invC)
	throws	ComponentException{

	if (invC.getDt_fine_validita() != null){
		if (invC.getDt_inizio_validita().after(invC.getDt_fine_validita()))
			throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: la data di fine validità deve essere superiore alla data di inizio validita");
	}
}
/**
  *  Controlla la data di fine validità - non specificata
  *    PreCondition:
  *      La data di inizio validità non è stata specificata
  *    PostCondition:
  *		 Un messaggio di errore viene visualizzato all'utente per indicare che la data di 
  *		inizio validità è obbligatoria
  *
  *  Controlla la data di fine validità - data non valida
  *    PreCondition:
  *      Il periodo specificato si accavalla con uno già esistente. La data di inizio validità 
  *		specificata, è antecedente alla max data di INIZIO validità registrata su DB.
  *    PostCondition:
  *		 Un messaggio di errore viene visualizzato all'utente per indicare che la data di 
  *		inizio validità deve essere superiore all'ultima data inizio validita. 
  *
  *  Controlla la data di fine validità - data non valida
  *    PreCondition:
  *      Il periodo specificato si accavalla con uno già esistente. La data di inizio validità 
  *		specificata, è antecedente alla max data di FINE validità registrata su DB.
  *    PostCondition:
  *		 Un messaggio di errore viene visualizzato all'utente per indicare che la data di 
  *		inizio validità deve essere superiore all'ultima data fine validita.
  *  
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param invC <code>Inventario_consegnatarioBulk</code> l'OggettoBulk che contiene le 
  *	  informazioni relative all'Inventario ed al suo Consegnatario
**/
private void checkDataInizioValidita(it.cnr.jada.UserContext aUC, Inventario_consegnatarioBulk invC) 
	throws	ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {

	// Data Inizio Validita == null
	if (invC.getDt_inizio_validita() == null)
		throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: la data inizio validita è un campo obbligatorio");

	// Data Inizio Validita < Max Data Inizio Validita
	Inventario_consegnatarioHome invCHome = (Inventario_consegnatarioHome)getHome(aUC,Inventario_consegnatarioBulk.class);
	java.sql.Timestamp maxDt = (java.sql.Timestamp)invCHome.findMax(invC, "dt_inizio_validita") ;
	if ( (maxDt != null) && (!invC.getDt_inizio_validita().after(maxDt)) )
		throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: la data inizio validita deve essere superiore all'ultima data inizio validita");

	// Data Inizio Validita < Ultima Data Fine Validita
	Inventario_consegnatarioBulk tmp = new Inventario_consegnatarioBulk();
	tmp.setInventario(invC.getInventario());
	tmp.setDt_inizio_validita(maxDt);
	maxDt = (java.sql.Timestamp)invCHome.findMax(tmp, "dt_fine_validita") ;
	
	if ( (maxDt != null) && (maxDt.compareTo(EsercizioHome.DATA_INFINITO)!=0) && (invC.getDt_inizio_validita().before(maxDt)) )
		throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: la data inizio validita deve essere superiore all'ultima data fine validita");
}
/** 
  *  Errore nella validazione del Consegnatario.
  *    PreCondition:
  *      I dati specificati dall'utente per la creazione del onsegnatario non hanno passato 
  *		i controlli di validazione, (metodo validaConsegntario).
  *    PostCondition:
  *      Non  viene consentito il salvataggio dei dati.
  *
  *  Errore nella validazione della Data di Inizio validità
  *    PreCondition:
  *      La data di inizio validità specificata non ha superato i controlli di validità, (metodo checkDataInizioValidita).
  *    PostCondition:
  *      Non  viene consentito il salvataggio dei dati.
  *
  *  Errore nella validazione della Data di Fine validità
  *    PreCondition:
  *      La data di fine validità specificata non ha superato i controlli di validità, (metodo checkDataFineValidita).
  *    PostCondition:
  *      Non  viene consentito il salvataggio dei dati.
  *   
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di un Consegnatario per l'Inventario 
  *		associato alla UO di scrivania.
  *    PostCondition:
  *      Viene consentito il salvataggio.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Bulk da creare
  *
  * @return bulk l'oggetto <code>OggettoBulk</code> creato
**/  
public OggettoBulk creaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException{

	try{
		Inventario_consegnatarioBulk invC = (Inventario_consegnatarioBulk)bulk;

		validaConsegntario(aUC,invC);
		checkDataInizioValidita(aUC, invC);
		checkDataFineValidita(aUC,invC);		
		
		return super.creaConBulk(aUC, invC);
		
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(bulk, ex);
	}catch(it.cnr.jada.persistency.IntrospectionException ex){
		throw handleException(bulk, ex);
	}
}
/** 
  *  Elimina l'ultimo Consegnatario.
  *    PreCondition:
  *      E' stata generata la richiesta di cancellare fisicamente l'ultimo Consegnatario 
  *		dell'Inventario. L'operazione di cancellazione dal DB del Consegnatario, infatti, 
  *		è possibile solo sull'ultimo Consegnatario specificato, (quello attuale), ossia 
  *		su quel Consegnatario che ha DT_FINE_VALIDITA' = NULL.
  *    PostCondition:
  *		Il sistema cancella il Consegnatario specificato. Viene impostato a NULL il valore
  *		DT_FINE_VALIDITA del Consegnatario "precedente", in modo da indicarlo come Consegnatario
  *		attuale.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.  
  * @param bulk <code>OggettoBulk</code> il Bulk da eliminare.
**/ 
public void eliminaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException {
try{
	super.eliminaConBulk(aUC, bulk);
	Inventario_consegnatarioBulk inv_consegnatario = (Inventario_consegnatarioBulk)bulk;
	Inventario_consegnatarioHome home =(Inventario_consegnatarioHome)getHome(aUC,Inventario_consegnatarioBulk.class);
	Inventario_consegnatarioBulk inv_con=new Inventario_consegnatarioBulk();
	inv_con.setInventario(new Id_inventarioBulk(inv_consegnatario.getPg_inventario()));
	SQLBuilder sql = home.createSQLBuilder();
	sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,inv_consegnatario.getPg_inventario());
	sql.addSQLClause("AND","DT_FINE_VALIDITA",sql.EQUALS,home.findMax(inv_con,"dt_fine_validita"));
	List consegnatari = home.fetchAll(sql);
	for (Iterator i=consegnatari.iterator();i.hasNext();){
		Inventario_consegnatarioBulk consegnatario=(Inventario_consegnatarioBulk )i.next();
		consegnatario.setDt_fine_validita(null);
		consegnatario.setToBeUpdated();
		super.modificaConBulk(aUC,consegnatario);
	}
 }catch(it.cnr.jada.persistency.PersistencyException ex){
	throw handleException(bulk, ex);
 }
}
/** 
  *  Cerca l'Inventario di cui la Uo di scrivania è responsabile
  *    PreCondition:
  *      La Uo di scrivania non è responsabile di alcun Inventario, (metodo loadInventarioResp).
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
  *
  *  Inizializzazione di una istanza di Inventario_consegnatarioBulk
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Inventario_consegnatarioBulk.
  *    PostCondition:
  *      Viene impostata e proposta la data odierna come data di inizio validità.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
  * @param bulk <code>OggettoBulk</code> il bulk che deve essere inizializzato.
  *
  * @return bulk <code>OggettoBulk</code> il bulk inizializzato.
**/
public OggettoBulk inizializzaBulkPerInserimento (UserContext aUC,OggettoBulk bulk) throws ComponentException{

	try {
		Inventario_consegnatarioBulk invC = (Inventario_consegnatarioBulk)super.inizializzaBulkPerInserimento(aUC,bulk);

		// carico l'Inventario di cui la U.O. di scrivania è responsabile
		loadInventarioResp(aUC,invC);
		invC.setDt_inizio_validita(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
		invC.setConsegnatario(new it.cnr.contab.anagraf00.core.bulk.TerzoBulk());
		invC.setDelegato(new it.cnr.contab.anagraf00.core.bulk.TerzoBulk());
		return invC;
		
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(bulk,e);
	} catch(it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(bulk,e);
	} catch(javax.ejb.EJBException e) {
		throw handleException(bulk,e);
	}
}
/**    
  *  Inizializzazione di una istanza di Inventario_consegnatarioBulk per modifica
  *    PreCondition:
  *      E' stata richiesta l'inizializzazione di una istanza di Inventario_consegnatarioBulk per modifica.
  *    PostCondition:
  *      L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato
  *		per l'operazione modifica.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
  * @param bulk <code>OggettoBulk</code> l'Inventario che deve essere inizializzato.
  *
  * @return bulk <code>OggettoBulk</code> l'Inventario inizializzato.
**/
public OggettoBulk inizializzaBulkPerModifica (UserContext aUC,OggettoBulk bulk) throws ComponentException{

	try{
		Inventario_consegnatarioBulk invC = (Inventario_consegnatarioBulk)super.inizializzaBulkPerInserimento(aUC,bulk);
		getHomeCache(aUC).fetchAll(aUC);
		return invC;
		
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(bulk,e);
	}

}
/** 
  *  Cerca l'Inventario di cui la Uo di scrivania è responsabile
  *    PreCondition:
  *      La Uo di scrivania non è responsabile di alcun Inventario, (metodo loadInventarioResp).
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
  *
  *  Inizializzazione di una istanza di Inventario_consegnatarioBulk per Ricerca
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Inventario_consegnatarioBulk per Ricerca.
  *    PostCondition:
  *      L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato
  *		per l'operazione di ricerca.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'Inventario che deve essere istanziato
  *
  * @return <code>OggettoBulk</code> l'Inventario inizializzato
**/
public OggettoBulk inizializzaBulkPerRicerca(UserContext aUC,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	try {

		Inventario_consegnatarioBulk invC = (Inventario_consegnatarioBulk)super.inizializzaBulkPerRicerca(aUC,bulk);

		// carico l'Inventario di cui la U.O. di scrivania è responsabile
		loadInventarioResp(aUC,invC);

		getHomeCache(aUC).fetchAll(aUC);
		return invC;
		
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(bulk,e);
	} catch(it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(bulk,e);
	}
}
/** 
  *  Cerca l'Inventario di cui la Uo di scrivania è responsabile
  *    PreCondition:
  *      La Uo di scrivania non è responsabile di alcun Inventario, (metodo loadInventarioResp).
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
  *
  *  Inizializzazione di una istanza di Inventario_consegnatarioBulk per Ricerca
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Inventario_consegnatarioBulk per Ricerca.
  *    PostCondition:
  *      L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato
  *		per l'operazione di ricerca.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'Inventario che deve essere istanziato
  *
  * @return <code>OggettoBulk</code> l'Inventario inizializzato
**/
public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext aUC,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	try {

		Inventario_consegnatarioBulk invC = (Inventario_consegnatarioBulk)super.inizializzaBulkPerRicercaLibera(aUC,bulk);

		// carico l'Inventario di cui la U.O. di scrivania è responsabile
		loadInventarioResp(aUC,invC);

		getHomeCache(aUC).fetchAll(aUC);
		return invC;
		
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(bulk,e);
	} catch(it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(bulk,e);
	}
}
/**
  *  Carica l'inventario di cui la U.O. di scrivania è responsabile.
  *    PreCondition:
  *      La UO di scrivania non è responsabile di alcun Inventario.
  *    PostCondition:
  *		Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità di continuare nelle operazioni.
  *
  *  Carica l'Inventario di competenza
  *    PreCondition:
  *      E' stato richiesto di caricare l'Inventario di cui la UO di scrivania è responsabile.
  *    PostCondition:
  *      Viene caricato l'Inventario di competenza. Questo viene utilizzato per impostare l'Inventario di riferimento.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param invC <code>Inventario_consegnatarioBulk</code> l'OggettoBulk che contiene le 
  *	  informazioni relative all'Inventario ed al suo Consegnatario
**/
private void loadInventarioResp(UserContext userContext, Inventario_consegnatarioBulk invC) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.IntrospectionException, it.cnr.jada.persistency.PersistencyException{

	Id_inventarioHome invHome = (Id_inventarioHome)getHome(userContext,Id_inventarioBulk.class);
	Id_inventarioBulk invResp = invHome.findInventarioRespFor(userContext);
	if (invResp == null)
		throw new it.cnr.jada.comp.ApplicationException("L'Unità organizzativa selezionata non è responsabile di alcun inventario");

	invC.setInventario(invResp);
}
/** 
  *  Errore - data fine validità
  *    PreCondition:
  *      Le modifiche apportate alla data di fine validità per il Consegnatario non superano 
  *		i controlli di validazione.
  *    PostCondition:
  *      Viene visualizzato un messaggio con la spiegazione dell'errore.
  *  
  *  Modifica Consegnatario
  *    PreCondition:
  *      E' stata generata la richiesta di modificare i dati del Consegnatario dell'Inventario.
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

	Inventario_consegnatarioBulk invC = (Inventario_consegnatarioBulk)bulk;

	checkDataFineValidita(aUC,invC);
	setDataFineValidita(invC);

	return super.modificaConBulk(aUC,invC);

}
/** 
  *  Ricerca di un Consegnatario
  *    PreCondition:
  *      E' stata generata la richiesta di ricercare un Consegnatario nello storico dell'Inventario.
  *    PostCondition:
  *		E' stato creato il SQLBuilder con le clausole implicite (presenti nell'istanza di Inventario_consegnatarioBulk),
  *		ed è stata aggiunta la clausola che l'Inventario sia quello associato alla UO di scrivania.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione
  * @param invC <code>Inventario_consegnatarioBulk</code> l'OggettoBulk che contiene le 
  *	  informazioni relative all'Inventario ed al suo Consegnatario
  *
  * @return sql <code>SQLBuilder</code> Risultato della selezione.
**/
protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {

	Inventario_consegnatarioBulk invcons = (Inventario_consegnatarioBulk)bulk;
	
	
	SQLBuilder sql = (SQLBuilder)super.select(userContext,clauses, bulk);
	sql.addSQLClause("AND", "PG_INVENTARIO", sql.EQUALS, invcons.getInventario().getPg_inventario());
	sql.addOrderBy("DT_FINE_VALIDITA");
	
	return sql;
	
}
/**
  *  Ricerca di un Terzo Consegnatario per l'Inventario
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca di un Terzo Consegnatario per l'Inventario.
  *    PostCondition:
  *		Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
  *		clausole che il Terzo sia ancora valido, ossia abbia Data di fine rapporto e Data 
  *		di cancellazione non valorizzate.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param inv_consegnatario il <code>Inventario_consegnatarioBulk</code> l'oggetto che contiene le 
  *	  informazioni relative all'Inventario ed al suo Consegnatario
  * @param delegato il <code>TerzoBulk</code> TerzoBulk modello
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione
  *
  * @return sql <code>SQLBuilder</code> Risultato della selezione.
**/
public SQLBuilder selectConsegnatarioByClause(
		UserContext userContext, 
		Inventario_consegnatarioBulk inv_consegnatario, 
		it.cnr.contab.anagraf00.core.bulk.TerzoBulk delegato, 
		CompoundFindClause clauses) throws ComponentException {
			
		
		SQLBuilder sql = getHome(userContext, it.cnr.contab.anagraf00.core.bulk.TerzoBulk.class,"V_TERZO_CF_PI").createSQLBuilder();
		sql.addClause( clauses );

		sql.addSQLClause("AND", "DT_FINE_RAPPORTO", sql.ISNULL, null);
		sql.addSQLClause("AND", "DT_CANC", sql.ISNULL, null);
		sql.addOrderBy("CD_TERZO");
		
		return sql;		
}
/**
  *  Ricerca di un Terzo Delegato per l'Inventario
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca di un Terzo Delegato per l'Inventario.
  *    PostCondition:
  *		Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
  *		clausole che il Terzo sia ancora valido, ossia abbia Data di fine rapporto e Data 
  *		di cancellazione non valorizzate.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param inv_consegnatario il <code>Inventario_consegnatarioBulk</code> l'oggetto che contiene le 
  *	  informazioni relative all'Inventario ed al suo Consegnatario
  * @param delegato il <code>TerzoBulk</code> TerzoBulk modello
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione
  *
  * @return sql <code>SQLBuilder</code> Risultato della selezione.
**/
public SQLBuilder selectDelegatoByClause(UserContext userContext, Inventario_consegnatarioBulk inv_consegnatario, it.cnr.contab.anagraf00.core.bulk.TerzoBulk delegato, CompoundFindClause clauses) 
		throws ComponentException
{
		
		SQLBuilder sql = getHome(userContext, it.cnr.contab.anagraf00.core.bulk.TerzoBulk.class,"V_TERZO_CF_PI").createSQLBuilder();
		sql.addClause( clauses );

		sql.addSQLClause("AND", "DT_FINE_RAPPORTO", sql.ISNULL, null);
		sql.addSQLClause("AND", "DT_CANC", sql.ISNULL, null);
		sql.addOrderBy("CD_TERZO");
		
		return sql;		
}
/** 
  *	Imposta la data di fine validità del Consegnatario. Se la data non è stata specificata, 
  *	 viene impostata come DATA_INFINITO.
  *
  * @param inv_consegnatario il <code>Inventario_consegnatarioBulk</code> l'oggetto che contiene le 
  *	  informazioni relative all'Inventario ed al suo Consegnatario
**/
private void setDataFineValidita(Inventario_consegnatarioBulk invC) {
					
	if(invC.getDt_fine_validita()==null)
		invC.setDt_fine_validita(EsercizioHome.DATA_INFINITO);
}
/** 
  *  valida Consegnatario - Consegnatario non specificato
  *    PreCondition:
  *      Non è stato specificato alcun Terzo Consegnatario.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare la necessità di 
  *		specificare un Terzo.
  *
  *  valida Consegnatario - Data inizio validità
  *    PreCondition:
  *      Non è stata specificata la data inizio validità, oppure la data specificata non è valida.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per indicare l'errore.
  *
  *  tutti i controlli superati
  *    PreCondition:
  *      Nessuna altra precondition soddisfatta
  *    PostCondition:
  *      viene consentito il salvataggio
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param inv_consegnatario il <code>Inventario_consegnatarioBulk</code> l'oggetto che contiene le 
  *	  informazioni relative all'Inventario ed al suo Consegnatario
**/
private void validaConsegntario (UserContext aUC,Inventario_consegnatarioBulk consegnatario) throws ComponentException{
	
	try{
		if (consegnatario.getConsegnatario()==null || consegnatario.getCd_consegnatario()==null){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: specificare un Consegnatario");
		}

		if (consegnatario.getDt_inizio_validita()==null){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: specificare una Data di inizio Validità");
		}
		Inventario_consegnatarioHome consH = (Inventario_consegnatarioHome)getHome(aUC,Inventario_consegnatarioBulk.class);
		consH.checkInserimentoSuccessivo(consegnatario); 
	}
	catch (Exception e)
	{
		throw new ComponentException(e);	
	}   	
}
}
