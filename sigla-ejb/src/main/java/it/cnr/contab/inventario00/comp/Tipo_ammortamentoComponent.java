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
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;

import java.io.Serializable;
import java.rmi.RemoteException;

public class Tipo_ammortamentoComponent extends it.cnr.jada.comp.CRUDDetailComponent implements ITipo_ammortamentoMgr,ICRUDMgr,Cloneable,Serializable
{

  
public  Tipo_ammortamentoComponent()
{

	/*Default constructor*/

}
/** 
  *  Annullamento
  *    PreCondition:
  *      E' stata generata la richiesta di annullare tutte le operazioni fatte sul Tipo Ammortamento.
  *    PostCondition:
  *      Viene effettuata un rollback fino al punto (SavePoint) impostato in precedenza, 
  *		(metodo inizializzaGruppiPerModifica)
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
**/
public void annullaModificaGruppi(UserContext userContext) throws it.cnr.jada.comp.ComponentException{
	try {
		rollbackToSavepoint(userContext,"ASS_TIPO_AMM_CAT_GRUP_INV_APG");
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	}
}
/** 
  * Associa tutte le Categorie Gruppo disponibili.
  *    PreCondition:
  *      Si sta tentando di creare un nuovo Tipo Ammortamento. E' stata generata la richiesta 
  *		di associare tutte Categorie Gruppo disponibili.
  *    PostCondition:
  *      Vengono riportati sulla tabella ASS_TIPO_AMM_CAT_GRUP_INV_APG tutte le Categorie Gruppo disponibili.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> tipo ammortamento.
  * @param gruppiSelezionati la <code>List</code> lista dei Gruppi Selezionati.
**/
public void associaTuttiGruppi(UserContext userContext,Tipo_ammortamentoBulk tipo_ammortamento, java.util.List gruppiSelezionati) throws ComponentException{

	// BLOCCA TUTTI I RECORD SELEZIONATI
	lockGruppiSelezionati(userContext, tipo_ammortamento);

	
	// SE E' IN FASE DI MODIFICA
	if (!tipo_ammortamento.isToBeCreated()){
		associaTuttiGruppiPerModifica(userContext, tipo_ammortamento);
	}
	else { // SE E' IN FASE DI CREAZIONE		
		String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
		LoggableStatement ps = null;

		String query = "INSERT INTO " + schema + "ASS_TIPO_AMM_CAT_GRUP_INV_APG ( " +	
		              "CD_TIPO_AMMORTAMENTO, " +
					  "TI_AMMORTAMENTO, " +
					  "CD_CATEGORIA_GRUPPO, " +
					  "ESERCIZIO_COMPETENZA, " +
					  "LOCAL_TRANSACTION_ID ) SELECT " + 
					  "1, 'O', CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO, ?, ? " +
					  " FROM " + schema + "CATEGORIA_GRUPPO_INVENT, " + 
					  schema + "ASS_TIPO_AMM_CAT_GRUP_INV, " +
					  schema + "ASS_TIPO_AMM_CAT_GRUP_INV_APG WHERE " +
					  "( CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO = ASS_TIPO_AMM_CAT_GRUP_INV.CD_CATEGORIA_GRUPPO(+) ) AND " + 
					  "( CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO = ASS_TIPO_AMM_CAT_GRUP_INV_APG.CD_CATEGORIA_GRUPPO(+) ) AND " +
					  /* Riga aggiunta in correttiva*/
					  "( CATEGORIA_GRUPPO_INVENT.LIVELLO > '0' ) AND" +
					  /* End riga aggiunta */
					  "( ASS_TIPO_AMM_CAT_GRUP_INV_APG.LOCAL_TRANSACTION_ID(+) = ? ) AND " + 
					  "( ASS_TIPO_AMM_CAT_GRUP_INV.CD_CATEGORIA_GRUPPO IS NULL ) AND " + 
					  "( ASS_TIPO_AMM_CAT_GRUP_INV_APG.CD_CATEGORIA_GRUPPO IS NULL ) AND " + 
					  "( CATEGORIA_GRUPPO_INVENT.FL_GESTIONE_INVENTARIO = 'Y' ) AND " + 
					  "( CATEGORIA_GRUPPO_INVENT.FL_AMMORTAMENTO = 'Y' ) AND" + 
					  "( ASS_TIPO_AMM_CAT_GRUP_INV.DT_CANCELLAZIONE(+) IS NULL ) AND" + 
					  "( ASS_TIPO_AMM_CAT_GRUP_INV_APG.DT_CANCELLAZIONE(+) IS NULL)";
			
		try {	

			ps = new LoggableStatement(getConnection(userContext),query,true,this.getClass());
			ps.setInt(1, tipo_ammortamento.getEsercizio_competenza().intValue()); // ESERCIZIO_COMPETENZA
			ps.setString(2, tipo_ammortamento.getLocal_transactionID()); // LOCAL_TRANSACTION_ID
			ps.setString(3, tipo_ammortamento.getLocal_transactionID()); // LOCAL_TRANSACTION_ID
			
			ps.execute();
			try{ps.close();}catch( java.sql.SQLException e ){};
		} catch(java.sql.SQLException e) {
			throw handleException(e);
		} finally {
				if (ps != null)
					try{ps.close();}catch( java.sql.SQLException e ){};
		}
	} // End else
}
/** 
  * Associa tutte le Categorie Gruppo disponibili per modifica.
  *    PreCondition:
  *      Si sta tentando di modificare un Tipo Ammortamento. E' stata generata la richiesta 
  *		di associare tutte Categorie Gruppo disponibili.
  *    PostCondition:
  *      Vengono riportati sulla tabella ASS_TIPO_AMM_CAT_GRUP_INV_APG tutte le Categorie Gruppo disponibili.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> tipo ammortamento.
**/
private void associaTuttiGruppiPerModifica(UserContext userContext,Tipo_ammortamentoBulk tipo_ammortamento) throws ComponentException{
	
	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
		
	String sub_query = "(" + 	
			"SELECT A.CD_CATEGORIA_GRUPPO " + 
				"FROM " + schema + "ASS_TIPO_AMM_CAT_GRUP_INV A " + 
				"WHERE " + "A.CD_TIPO_AMMORTAMENTO != '" + tipo_ammortamento.getCd_tipo_ammortamento()  + "' " +   
				"AND  A.DT_CANCELLAZIONE IS NULL " + 
				"AND A.ESERCIZIO_COMPETENZA = " + tipo_ammortamento.getEsercizio_competenza() +   
		" UNION ALL " + 
			"SELECT A.CD_CATEGORIA_GRUPPO " + 
				"FROM " + schema + "ASS_TIPO_AMM_CAT_GRUP_INV A " + 
				"WHERE  EXISTS (SELECT 1 " +
					  		 	"FROM " + schema + "ASS_TIPO_AMM_CAT_GRUP_INV_APG " +
					  		 	"WHERE CD_TIPO_AMMORTAMENTO = A.CD_TIPO_AMMORTAMENTO " +
								"AND TI_AMMORTAMENTO	   = A.TI_AMMORTAMENTO " +
								"AND CD_CATEGORIA_GRUPPO  = A.CD_CATEGORIA_GRUPPO " +
								"AND ESERCIZIO_COMPETENZA = A.ESERCIZIO_COMPETENZA " +
								"AND DT_CANCELLAZIONE IS NULL) " +
				"AND A.CD_TIPO_AMMORTAMENTO = '" + tipo_ammortamento.getCd_tipo_ammortamento()  + "' " +
				"AND  A.DT_CANCELLAZIONE IS NULL " +  
				"AND  A.ESERCIZIO_COMPETENZA = " + tipo_ammortamento.getEsercizio_competenza() +
		" UNION ALL " +
		    "SELECT APG.CD_CATEGORIA_GRUPPO " + 
			    "FROM " + schema + "ASS_TIPO_AMM_CAT_GRUP_INV_APG APG " +
			    "WHERE  APG.LOCAL_TRANSACTION_ID = '" + tipo_ammortamento.getLocal_transactionID() + "' " + 
			    "AND APG.CD_TIPO_AMMORTAMENTO = '" + tipo_ammortamento.getCd_tipo_ammortamento()  + "' " +
			    "AND APG.DT_CANCELLAZIONE IS NULL " + 
			") SCARTO ";
			
	LoggableStatement ps = null;		
	
	String query = "INSERT INTO " + schema + "ASS_TIPO_AMM_CAT_GRUP_INV_APG ( " +	
	              "CD_TIPO_AMMORTAMENTO, " +
				  "TI_AMMORTAMENTO, " +
				  "CD_CATEGORIA_GRUPPO, " +
				  "ESERCIZIO_COMPETENZA, " +
				  "LOCAL_TRANSACTION_ID ) SELECT " + 
				  "?, 'O', CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO, ?, ? " +
				  " FROM " + schema + "CATEGORIA_GRUPPO_INVENT, " +
				  sub_query +
				  "WHERE CATEGORIA_GRUPPO_INVENT.FL_GESTIONE_INVENTARIO = 'Y' " +
				  "AND CATEGORIA_GRUPPO_INVENT.FL_AMMORTAMENTO = 'Y' " +
				  "AND CATEGORIA_GRUPPO_INVENT.LIVELLO >0" + 
				  "AND CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO = SCARTO.CD_CATEGORIA_GRUPPO (+) " + 
				  "AND SCARTO.CD_CATEGORIA_GRUPPO IS NULL";
				  
	try {	

		ps = new LoggableStatement(getConnection(userContext),query,true,this.getClass());
		ps.setString(1, tipo_ammortamento.getCd_tipo_ammortamento()); // CD_TIPO_AMMORTAMENTO
		ps.setInt(2, tipo_ammortamento.getEsercizio_competenza().intValue()); // ESERCIZIO_COMPETENZA
		ps.setString(3, tipo_ammortamento.getLocal_transactionID()); // LOCAL_TRANSACTION_ID
		
		ps.execute();
		try{ps.close();}catch( java.sql.SQLException e ){};
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	} finally {
			if (ps != null)
				try{ps.close();}catch( java.sql.SQLException e ){};
	}
}
/** 
  *  Cerca tutte le Categorie Gruppo disponibili
  *    PreCondition:
  *      E' stata generata la richiesta di cercare tutte le Categorie Gruppo che rispondono alle caratteristiche 
  *		per essere associati al Tipo Ammortamento.
  *		I gruppi disponibli sono rispondono alle seguenti caratteristiche:
  *		  	- siano soggetti a gestione Inventario, (FL_GESTIONE_INVENTARIO = 'Y');
  *			- siano soggetti ad ammortamento, (FL_AMMORTAMENTO = 'Y');
  *			- siano di livello maggiore di 0, (ossia siano Gruppi e non Categorie);
  *			- NON siano già associati ad altri Tipi Ammortamento;
  *			- NON siano stati cacellati logicamente.
  *    PostCondition:
  *     Viene costruito e restituito l'Iteratore sui gruppi disponibili.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
  * @param tipo_ammortamento <code>Tipo_ammortamentoBulk</code> il Tipo Ammortamento.
  *
  * @return l'Iteratore <code>RemoteIterator</code> sui gruppi trovati.
**/
public it.cnr.jada.util.RemoteIterator cercaGruppiAssociabili(UserContext userContext, Tipo_ammortamentoBulk tipo_ammortamento) throws ComponentException{

	SQLBuilder sql = getHome(userContext,Categoria_gruppo_inventBulk.class).createSQLBuilder();
	
	sql.addTableToHeader("ASS_TIPO_AMM_CAT_GRUP_INV");
	sql.addTableToHeader("ASS_TIPO_AMM_CAT_GRUP_INV_APG");
	sql.addSQLJoin("CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO","ASS_TIPO_AMM_CAT_GRUP_INV.CD_CATEGORIA_GRUPPO(+)");// Questa OUT Join permette di visualizzare solo i 
	sql.addSQLJoin("CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO","ASS_TIPO_AMM_CAT_GRUP_INV_APG.CD_CATEGORIA_GRUPPO(+)"); // gruppi NON ancora associati a nessun TIPO_AMMORTAMENTO e che non siano 
	sql.addSQLClause("AND", "ASS_TIPO_AMM_CAT_GRUP_INV_APG.LOCAL_TRANSACTION_ID(+)", sql.EQUALS, tipo_ammortamento.getLocal_transactionID());  // stati scelti durante la transazione attuale
	sql.addSQLClause("AND", "CATEGORIA_GRUPPO_INVENT.FL_GESTIONE_INVENTARIO", sql.EQUALS, "Y");	
	sql.addSQLClause("AND", "CATEGORIA_GRUPPO_INVENT.FL_AMMORTAMENTO", sql.EQUALS, "Y"); // Non scaricati totalmente
	sql.addSQLClause("AND", "CATEGORIA_GRUPPO_INVENT.LIVELLO", sql.GREATER, "0"); // Cerca solo i GRUPPI, (Livello>0)

	sql.addSQLClause("AND", "ASS_TIPO_AMM_CAT_GRUP_INV.CD_CATEGORIA_GRUPPO", sql.ISNULL, null);	
	sql.addSQLClause("AND", "ASS_TIPO_AMM_CAT_GRUP_INV_APG.CD_CATEGORIA_GRUPPO", sql.ISNULL, null);		
	sql.addSQLClause("AND", "ASS_TIPO_AMM_CAT_GRUP_INV.DT_CANCELLAZIONE(+)", sql.ISNULL, null);
	sql.addSQLClause("AND", "ASS_TIPO_AMM_CAT_GRUP_INV_APG.DT_CANCELLAZIONE(+)", sql.ISNULL, null);

	
	return iterator(userContext,sql,Categoria_gruppo_inventBulk.class,null);	
}
 /** 
  *  Cerca tutte le Categorie Gruppo disponibili poer modifica
  *    PreCondition:
  *      E' stata generata la richiesta di cercare tutte le Categorie Gruppo che rispondono alle caratteristiche 
  *		per essere associati per la modifica di Tipo Ammortamento.
  *		I gruppi disponibli sono rispondono alle seguenti caratteristiche:
  *		  	- siano soggetti a gestione Inventario, (FL_GESTIONE_INVENTARIO = 'Y');
  *			- siano soggetti ad ammortamento, (FL_AMMORTAMENTO = 'Y');
  *			- siano di livello maggiore di 0, (ossia siano Gruppi e non Categorie);
  *			- NON siano già associati ad altri Tipi Ammortamento;
  *			- NON siano stati cacellati logicamente.
  *    PostCondition:
  *     Viene costruito e restituito l'Iteratore sui gruppi disponibili.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
  * @param tipo_ammortamento <code>Tipo_ammortamentoBulk</code> il Tipo Ammortamento.
  *
  * @return l'Iteratore <code>RemoteIterator</code> sui gruppi trovati.
**/
public it.cnr.jada.util.RemoteIterator cercaGruppiAssociabiliPerModifica(UserContext userContext, Tipo_ammortamentoBulk tipo_ammortamento) throws ComponentException{

	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	String sub_query = "(" + 	
			"SELECT A.CD_CATEGORIA_GRUPPO " + 
				"FROM " + schema + "ASS_TIPO_AMM_CAT_GRUP_INV A " + 
				"WHERE  " + "A.CD_TIPO_AMMORTAMENTO != '" + tipo_ammortamento.getCd_tipo_ammortamento()  + "'" +   
				"AND  A.DT_CANCELLAZIONE IS NULL " + 
				"AND A.ESERCIZIO_COMPETENZA = " + tipo_ammortamento.getEsercizio_competenza() +   
		"UNION ALL " + 
			"SELECT A.CD_CATEGORIA_GRUPPO " + 
				"FROM " + schema + "ASS_TIPO_AMM_CAT_GRUP_INV A " + 
				"WHERE EXISTS (SELECT 1 " +
					  		 	"FROM " + schema + "ASS_TIPO_AMM_CAT_GRUP_INV_APG " +
					  		 	"WHERE CD_TIPO_AMMORTAMENTO = A.CD_TIPO_AMMORTAMENTO " +
								"AND TI_AMMORTAMENTO	   = A.TI_AMMORTAMENTO " +
								"AND CD_CATEGORIA_GRUPPO  = A.CD_CATEGORIA_GRUPPO " +
								"AND ESERCIZIO_COMPETENZA = A.ESERCIZIO_COMPETENZA " +
								"AND DT_CANCELLAZIONE IS NULL) " +				
				"AND A.CD_TIPO_AMMORTAMENTO = '" + tipo_ammortamento.getCd_tipo_ammortamento()  + "'" +
				"AND  A.DT_CANCELLAZIONE IS NULL " +  
				"AND  A.ESERCIZIO_COMPETENZA = " + tipo_ammortamento.getEsercizio_competenza() +
		"UNION ALL " +
		    "SELECT APG.CD_CATEGORIA_GRUPPO " + 
		    	"FROM " + schema + "ASS_TIPO_AMM_CAT_GRUP_INV_APG APG " +
		    	"WHERE  APG.LOCAL_TRANSACTION_ID = '" + tipo_ammortamento.getLocal_transactionID() + "'" + 
		    	"AND APG.CD_TIPO_AMMORTAMENTO = '" + tipo_ammortamento.getCd_tipo_ammortamento()  + "'" +
		    	"AND APG.DT_CANCELLAZIONE IS NULL " + 
			") SCARTO";
					
	
	SQLBuilder sql = getHome(userContext,Categoria_gruppo_inventBulk.class).createSQLBuilder();
	

	sql.addToHeader(sub_query);
	sql.addSQLClause("AND", "CATEGORIA_GRUPPO_INVENT.FL_GESTIONE_INVENTARIO", sql.EQUALS, "Y");	// Soggetti ad Inventario
	sql.addSQLClause("AND", "CATEGORIA_GRUPPO_INVENT.FL_AMMORTAMENTO", sql.EQUALS, "Y"); // Non scaricati totalmente
	sql.addSQLClause("AND", "CATEGORIA_GRUPPO_INVENT.LIVELLO", sql.GREATER, "0"); // Cerca solo i GRUPPI, (Livello>0)
	
	sql.addSQLJoin("CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO", "SCARTO.CD_CATEGORIA_GRUPPO (+)");
	sql.addSQLClause("AND", "SCARTO.CD_CATEGORIA_GRUPPO", sql.ISNULL, null);
	
	return iterator(userContext,sql,Categoria_gruppo_inventBulk.class,null);	
}
/** 
  * Invocato in fase di modifica del Tipo_Ammortamento, se l'utente decide di aggiungere
  *	 un gruppo al Tipo Ammortamento che sta modificando.
  *	 Il metodo controlla che il gruppo indicato non sia stato già associato e poi
  *  cancellato logicamente (ASS_TIPO_AMM_CAT_GRUP_INV_APG.DT_CANCELLAZIONE!=NULL): in
  *	 tal caso, il gruppo NON viene inserito, bensì viene fatta una operazione di UPDATE
  *	 sul record esistente, settando il campo ASS_TIPO_AMM_CAT_GRUP_INV_APG.DT_CANCELLAZIONE = a NULL.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
  * @param tipo_ammortamento <code>Tipo_ammortamentoBulk</code> il Tipo Ammortamento.
  * @param gruppo il <code>Categoria_gruppo_inventBulk</code> gruppo indicato.
  *
  * @return l'Iteratore <code>RemoteIterator</code> sui gruppi trovati.
**/ 
private boolean checkGroupAlreadyExistsInApgForModify(UserContext userContext, Tipo_ammortamentoBulk tipo_ammortamento, Categoria_gruppo_inventBulk gruppo) throws ComponentException{

	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	
	LoggableStatement ps = null;
	java.sql.ResultSet rs = null;
	
	boolean alreadyExist = false;
	
	String query_controllo = "SELECT * FROM " + schema + "ASS_TIPO_AMM_CAT_GRUP_INV_APG " + 
			"WHERE LOCAL_TRANSACTION_ID = ? " +
			"AND CD_CATEGORIA_GRUPPO = ? " ;

	String query_update = "UPDATE " + schema + "ASS_TIPO_AMM_CAT_GRUP_INV_APG " + 
			"SET DT_CANCELLAZIONE = NULL " + 
			"WHERE LOCAL_TRANSACTION_ID = ? " + 
			"AND CD_CATEGORIA_GRUPPO = ? ";
	
	try {
		ps = new LoggableStatement(getConnection(userContext),query_controllo,true,this.getClass());
		
		ps.setString(1,tipo_ammortamento.getLocal_transactionID());   // LOCAL_TRANSACTION_ID
		ps.setString(2,gruppo.getCd_categoria_gruppo()); //CD_CATEGORIA_GRUPPO
		
		// Controlla che il gruppo non sia già presente in ASS_TIPO_AMM_CAT_GRUP_INV_APG
		rs = ps.executeQuery();
		if (rs.next()){
			alreadyExist = true;
		}
		try{ps.close();}catch( java.sql.SQLException e ){};
		try{rs.close();}catch( java.sql.SQLException e ){};

		// Se il gruppo già esiste, allora effettua l'UPDATE sul campo DT_CANCELLAZIONE
		if(alreadyExist){	
			ps = new LoggableStatement(getConnection(userContext),query_update,true,this.getClass());	
			ps.setString(1,tipo_ammortamento.getLocal_transactionID());   // LOCAL_TRANSACTION_ID
			ps.setString(2,gruppo.getCd_categoria_gruppo()); //CD_CATEGORIA_GRUPPO

			ps.execute();
		}
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	} finally {
			if (ps != null)
				try{ps.close();}catch( java.sql.SQLException e ){};
	}
	return alreadyExist;
}
		
		
/** 
  *  Validazione del Tipo Ammortamento
  *    PreCondition:
  *      validaTipo_Ammortamento non superato.
  *    PostCondition:
  *      Un messaggio di errore viene mostrato all'utente. Non  viene consentita la registrazione del Tipo Ammortamento.
  *
  *  Validazione del codice del Tipo Ammortamento
  *    PreCondition:
  *      Il codice specificato per il Tipo Ammortamento è già utilizzato.
  *    PostCondition:
  *      Un messaggio di errore viene mostrato all'utente. Non  viene consentita la registrazione del Tipo Ammortamento.
  * 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata richiesta la creazione di un nuovo Tipo Ammortamento.
  *    PostCondition:
  *      Viene consentito il salvataggio del Tipo Ammortamento. Vengono salvate le associazioni 
  *		che l'utente ha specificato tra il Tipo Ammortamento e le Categorie Gruppo Inventario.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Bulk da creare
  *
  * @return l'oggetto <code>OggettoBulk</code> creato
**/
public OggettoBulk creaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	try{
		Tipo_ammortamentoBulk tipo_ammortamento = (Tipo_ammortamentoBulk)bulk;

		// Valida il Tipo Ammortamento
		validaTipo_Ammortamento(aUC, tipo_ammortamento);
			
		validaCDTipo_Ammortamento(aUC, tipo_ammortamento);
		//super.creaConBulk(aUC,ti_ammortamento);
		
		/*Tipo_ammortamentoHome ti_ammortHome = (Tipo_ammortamentoHome)getHome(aUC,Tipo_ammortamentoBulk.class);
		ti_ammortHome.makePersistentAssTiAmm_CatBeni(aUC,ti_ammortamento);*/

		tipo_ammortamento = makePersistentTipoAmmortamento(aUC, tipo_ammortamento);
		
		makePersistentAssociazioni(aUC, tipo_ammortamento);

		return tipo_ammortamento;
		
	}catch (Throwable e){
		throw handleException(bulk, e);
	}
}
/** 
  *  Elimina un Tipo Ammortamento
  *    PreCondition:
  *      E' stata generata la richiesta di cancellare un Tipo Ammortamento.
  *    PostCondition:
  *      Vengono eliminati fisicamente i gruppi associati al Tipo Ammortamento specificato, ( eliminaGruppiConBulk(UserContext, Tipo_ammortamentoBulk) ).
  *		Viene cancellato logicamente il Tipo Ammortamento.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.  
  * @param bulk <code>OggettoBulk</code> il Tipo Ammortamento da eliminare.
**/ 
public void eliminaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	
	Tipo_ammortamentoBulk tipo_ammortamento = (Tipo_ammortamentoBulk)bulk;	

	try{
		// Elimina le associazioni esistenti
		eliminaGruppiConBulk(userContext, tipo_ammortamento);

		// Rende persistenti le modifiche fatte
		makePersistentAssociazioni(userContext, tipo_ammortamento);
		
		String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	
		LoggableStatement ps = null;
		
		String query_elimina_ti_ammort = "UPDATE " + schema + "TIPO_AMMORTAMENTO " + 
				"SET DT_CANCELLAZIONE = SYSDATE " + 
				"WHERE CD_TIPO_AMMORTAMENTO = ? ";			

		try {		
			ps = new LoggableStatement(getConnection(userContext),query_elimina_ti_ammort,
					true,this.getClass());
			ps.setString(1, tipo_ammortamento.getCd_tipo_ammortamento()); // CD_TIPO_AMMORTAMENTO
			
			ps.execute();
			try{ps.close();}catch( java.sql.SQLException e ){};
		} catch(java.sql.SQLException e) {
			throw handleException(e);
		} finally {
				if (ps != null)
					try{ps.close();}catch( java.sql.SQLException e ){};
		}
	}catch (Throwable e){
		throw handleException(bulk, e);
	}
}
/** 
  *  Elimina TUTTI i Gruppi associati al Tipo Ammortamento.
  *    PreCondition:
  *      E' stata generata la richiesta di cancellare tutte le associazioni fatte durante 
  *		una sessione di lavoro.
  *    PostCondition:
  *      Vengono cancellate dalla tabella d'appoggio ASS_TIPO_AMM_CAT_GRUP_INV_APG, i gruppi associati
  *		al Tipo Ammortamento.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> tipo ammortamento.
**/ 
public void eliminaGruppiConBulk(UserContext userContext, Tipo_ammortamentoBulk tipo_ammortamento) throws ComponentException{
	
	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	String query;
	
	// SE E' IN FASE DI MODIFICA
	if (!tipo_ammortamento.isToBeCreated()){
		query = "UPDATE " + schema + "ASS_TIPO_AMM_CAT_GRUP_INV_APG SET DT_CANCELLAZIONE = SYSDATE WHERE LOCAL_TRANSACTION_ID = ?";
	}
	else{
		query = "DELETE FROM " + schema + "ASS_TIPO_AMM_CAT_GRUP_INV_APG WHERE LOCAL_TRANSACTION_ID = ?";

	}
	
	LoggableStatement ps = null;
	try {
				
		ps = new LoggableStatement(getConnection(userContext),query,
				true,this.getClass());
		ps.setString(1,tipo_ammortamento.getLocal_transactionID());   // LOCAL_TRANSACTION_ID
		
		ps.execute();		
		//try{ps.close();}catch( java.sql.SQLException e ){};
		
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	} finally {
			if (ps != null)
				try{ps.close();}catch( java.sql.SQLException e ){};
	}
}
/** 
  *  Elimina dei Gruppi associati al Tipo Ammortamento.
  *    PreCondition:
  *      E' stata generata la richiesta di cancellare alcune associazioni fatte durante 
  *		la sessione di lavoro.
  *    PostCondition:
  *      Vengono cancellate dalla tabella d'appoggio ASS_TIPO_AMM_CAT_GRUP_INV_APG, i gruppi 
  *		specificati.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> tipo ammortamento.
  * @param gruppi <code>OggettoBulk[]</code> i gruppi da eliminare.
**/ 
public void eliminaGruppiConBulk(UserContext userContext, Tipo_ammortamentoBulk tipo_ammortamento, OggettoBulk[] gruppi) throws ComponentException{

	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	
	LoggableStatement ps = null;
	
	String query;
	
	// SE E' IN FASE DI MODIFICA
	if (!tipo_ammortamento.isToBeCreated()){
		query = "UPDATE " + schema + "ASS_TIPO_AMM_CAT_GRUP_INV_APG " + 
				"SET DT_CANCELLAZIONE = SYSDATE " + 
				"WHERE LOCAL_TRANSACTION_ID = ? " + 
				"AND CD_CATEGORIA_GRUPPO = ? ";
	}
	else{
		query = "DELETE FROM " + schema + "ASS_TIPO_AMM_CAT_GRUP_INV_APG " + 
				"WHERE LOCAL_TRANSACTION_ID = ? " +
				"AND CD_CATEGORIA_GRUPPO = ? " ;
	}
	
	try {
		for (int i = 0;i < gruppi.length;i++) {
			Categoria_gruppo_inventBulk gruppo = (Categoria_gruppo_inventBulk)gruppi[i];	

			ps = new LoggableStatement(getConnection(userContext),query,
					true,this.getClass());
			
			ps.setString(1,tipo_ammortamento.getLocal_transactionID());   // LOCAL_TRANSACTION_ID
			ps.setString(2,gruppo.getCd_categoria_gruppo()); //CD_CATEGORIA_GRUPPO

			ps.execute();
			ps.clearParameters();
		}	
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	} finally {
			if (ps != null)
				try{ps.close();}catch( java.sql.SQLException e ){};
	}
}
		
		
/** 
  * Cerca tutte le Categorie Gruppo Inventario associate ad un Tipo Ammortamento.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
  * @param ti_ammortamento il <code>Tipo_ammortamentoBulk</code> Tipo Ammortamento.
  *
  * @return gruppi la <code>List</code> lista di gruppi associati al Tipo Ammortamento.
**/
private java.util.List findCat_BeniFor(UserContext aUC,Tipo_ammortamentoBulk ti_ammortamento)
	throws ComponentException,
	it.cnr.jada.persistency.PersistencyException,
	it.cnr.jada.persistency.IntrospectionException 
{
	
	if (ti_ammortamento == null) 
		return null; 
	
	it.cnr.jada.bulk.BulkHome home = getHome(aUC, Categoria_gruppo_inventBulk.class);
	it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
	sql.addTableToHeader("ASS_TIPO_AMM_CAT_GRUP_INV");
	sql.addSQLJoin("CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO","ASS_TIPO_AMM_CAT_GRUP_INV.CD_CATEGORIA_GRUPPO");	
	sql.addSQLClause("AND","ASS_TIPO_AMM_CAT_GRUP_INV.CD_TIPO_AMMORTAMENTO",sql.EQUALS,ti_ammortamento.getCd_tipo_ammortamento());
	
	
	return home.fetchAll(sql);
}
/** 
  *  Cerca i gruppi associati ad un Tipo Ammortamento per modifica.
  *    PreCondition:
  *      E' stata generata la richiesta di modificare un Tipo Ammortamento.
  *    PostCondition:
  *      Vengono cercati i gruppi associati al Tipo Ammortamento specificato. Le associazioni 
  *		trovate vengono inserite nella tabella d'appoggio ASS_TIPO_AMM_CAT_GRUP_INV_APG.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> Tipo Ammortamento.
**/
private void findGruppiAssociati(UserContext aUC,Tipo_ammortamentoBulk tipo_ammortamento)
	throws ComponentException,
	it.cnr.jada.persistency.PersistencyException,
	it.cnr.jada.persistency.IntrospectionException 
{
	
	
	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	LoggableStatement ps = null;
	
	String query = "INSERT INTO ASS_TIPO_AMM_CAT_GRUP_INV_APG (" +
					"CD_TIPO_AMMORTAMENTO, " +
					"TI_AMMORTAMENTO, " +
					"CD_CATEGORIA_GRUPPO, " +
					"ESERCIZIO_COMPETENZA, " +
					"DT_CANCELLAZIONE, " +
					"LOCAL_TRANSACTION_ID) " +
					"SELECT CD_TIPO_AMMORTAMENTO, " +
					"'O'," +
					"CD_CATEGORIA_GRUPPO, " +
					"ESERCIZIO_COMPETENZA, " +
					"DT_CANCELLAZIONE, " +
					"? " +
					"FROM " + schema + "ASS_TIPO_AMM_CAT_GRUP_INV "  +
					"WHERE CD_TIPO_AMMORTAMENTO = ? " +
					" AND ESERCIZIO_COMPETENZA = "+it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC)+
					" AND DT_CANCELLAZIONE IS NULL " + 
					"GROUP BY CD_TIPO_AMMORTAMENTO, " +
					"CD_CATEGORIA_GRUPPO, " +
					"ESERCIZIO_COMPETENZA, " +
					"DT_CANCELLAZIONE";
		
	try {	

		ps = new LoggableStatement(getConnection(aUC),query,
				true,this.getClass());		
		ps.setString(1, tipo_ammortamento.getLocal_transactionID()); // LOCAL_TRANSACTION_ID
		ps.setString(2, tipo_ammortamento.getCd_tipo_ammortamento()); // CD_TIPO_AMMORTAMENTO

		ps.execute();
		try{ps.close();}catch( java.sql.SQLException e ){};
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	} finally {
			if (ps != null)
				try{ps.close();}catch( java.sql.SQLException e ){};
	}
}


/** 
  * Cerca un Tipo Ammortamento per modifica.
  *    PreCondition:
  *      E' stata generata la richiesta di modificare un Tipo Ammortamento.
  *    PostCondition:
  *      Interroga la tabella TIPO_AMMORTAMENTO per impostare tutte le caratteristiche del
  *		Tipo Ammortamento di Riferimento. Viene, poi, cercato l'esercizio di competenza.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> Tipo Ammortamento.
  *
  * @return tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> Tipo Ammortamento correttamente impostato.
**/
private Tipo_ammortamentoBulk findTipoAmmortamentoPerModifica(UserContext aUC,Tipo_ammortamentoBulk tipo_ammortamento)
	throws ComponentException,
	it.cnr.jada.persistency.PersistencyException,
	it.cnr.jada.persistency.IntrospectionException 
{

	
	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	LoggableStatement ps = null;
	java.sql.ResultSet rs = null;
	
	String query = "SELECT * FROM " + schema + "TIPO_AMMORTAMENTO WHERE CD_TIPO_AMMORTAMENTO = ?";
		
	try {	

		ps = new LoggableStatement(getConnection(aUC),query,
				true,this.getClass());
		ps.setString(1, tipo_ammortamento.getCd_tipo_ammortamento()); // CD_TIPO_AMMORTAMENTO
		
		rs = ps.executeQuery();
		while (rs.next()){
			String tipo = rs.getString("TI_AMMORTAMENTO");
			java.math.BigDecimal perc_primo_anno = rs.getBigDecimal("PERC_PRIMO_ANNO");
			java.math.BigDecimal perc_successivi = rs.getBigDecimal("PERC_SUCCESSIVI");

			if (tipo.equalsIgnoreCase(Tipo_ammortamentoBulk.TIPO_ORDINARIO)){
				tipo_ammortamento.setFl_ordinario(Boolean.TRUE);
				tipo_ammortamento.setPerc_primo_anno_ord(perc_primo_anno);
				tipo_ammortamento.setPerc_successivi_ord(perc_successivi);
			}

			else if (tipo.equalsIgnoreCase(Tipo_ammortamentoBulk.TIPO_ACCELERATO)){
				tipo_ammortamento.setFl_anticipato(Boolean.TRUE);
				tipo_ammortamento.setPerc_primo_anno_ant(perc_primo_anno);
				tipo_ammortamento.setPerc_successivi_ant(perc_successivi);
			}

			else if (tipo.equalsIgnoreCase(Tipo_ammortamentoBulk.TIPO_ALTRO)){
				tipo_ammortamento.setFl_altro(Boolean.TRUE);
				tipo_ammortamento.setPerc_primo_anno_altro(perc_primo_anno);
				tipo_ammortamento.setPerc_successivi_altro(perc_successivi);
			}
		}
		try{rs.close();}catch( java.sql.SQLException e ){};
		try{ps.close();}catch( java.sql.SQLException e ){};
		

		// Cerca l'ESERCIZIO DI COMPETENZA
		String query_esercizio = "SELECT DISTINCT(ESERCIZIO_COMPETENZA) FROM ASS_TIPO_AMM_CAT_GRUP_INV WHERE CD_TIPO_AMMORTAMENTO = ?";
		Integer esercizio_competenza = new Integer(0);
		
		ps =new LoggableStatement(getConnection(aUC),query_esercizio,
				true,this.getClass());
		ps.setString(1, tipo_ammortamento.getCd_tipo_ammortamento()); // CD_TIPO_AMMORTAMENTO
		
		rs = ps.executeQuery();
		while (rs.next()){
			esercizio_competenza = new Integer(rs.getInt("ESERCIZIO_COMPETENZA"));
		}

		if (esercizio_competenza.compareTo(new Integer(0))==0)
			esercizio_competenza = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC);		
		tipo_ammortamento.setEsercizio_competenza(esercizio_competenza);
		
		try{rs.close();}catch( java.sql.SQLException e ){};
		try{ps.close();}catch( java.sql.SQLException e ){};
		
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	} finally {
			if (rs != null)
				try{rs.close();}catch( java.sql.SQLException e ){};
				
			if (ps != null)
				try{ps.close();}catch( java.sql.SQLException e ){};
	}

	return tipo_ammortamento;
}


/** 
  * Cerca un Tipo Ammortamento per riassocia.
  *    PreCondition:
  *      E' stata generata la richiesta di riassociare i Gruppi di un Tipo Ammortamento ad 
  *		un altro Tipo Ammortamento.
  *    PostCondition:
  *      Viene restituito un Iteratore sui tipi ammortamento disponibili per l'operazione 
  *		di riassocia; i tipi ammortamento disponibili rispondono alle seguenti caratteristiche:
  *			- non sono stati cancellati logicamente.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> Tipo Ammortamento.
  * @param bulkClass la <code>Class</code> modello per il tipo ammortamento.
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
  *
  * @return iterator <code>RemoteIterator</code> l'Iteratore sulla selezione.
**/
public it.cnr.jada.util.RemoteIterator getAmmortamentoRemoteIteratorPerRiassocia(
	UserContext userContext,
	Tipo_ammortamentoBulk tipo_ammortamento,
	Class bulkClass, 
	it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws ComponentException{

	
    String logicOperator= null;
    int operator= 0;
    String value= null;

    if (clauses == null) 
            clauses = tipo_ammortamento.buildFindClauses(null);
    else {
        CompoundFindClause newClauses= null;

        for (java.util.Iterator i= clauses.iterator(); i.hasNext();) {
            SimpleFindClause clause= (SimpleFindClause) i.next();
            if (clause.getPropertyName().equals("fl_ordinario")) {
                logicOperator= clause.getLogicalOperator();
                operator= clause.getOperator();
                //value= ((Tipo_documento_ammBulk) clause.getValue()).getCd_tipo_documento_amm();
            } else
                newClauses= CompoundFindClause.and(newClauses, clause);
        }

        clauses= newClauses;
    }
	
	SQLBuilder sql = getHome(userContext, Tipo_ammortamentoBulk.class).createSQLBuilder();
	sql.addClause(clauses);	
	sql.addSQLClause("AND","CD_TIPO_AMMORTAMENTO",sql.NOT_EQUALS,tipo_ammortamento.getCd_tipo_ammortamento());
	sql.addSQLClause("AND","DT_CANCELLAZIONE",sql.ISNULL,null);
	

	it.cnr.jada.util.RemoteIterator ri = iterator(userContext,sql,bulkClass,null);
	
	return ri;

}
/** 
  *  Richiede l'ID univoco di Transazione.
  *    PreCondition:
  *      E' stato richiesto di recuperare/generare l'identificativo di transazione.
  *    PostCondition:
  *      Viene richiesto l'ID e, se questo non esiste, verrà generato, se richiesto.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
  * @param force <code>boolean</code> il flag che indica se forzare la generazione dell'ID.
  *
  * @return local_transaction_id <code>String</code> l'ID di transazione richiesto.
**/
public String getLocalTransactionID(UserContext aUC, boolean force) 
	throws ComponentException,
	it.cnr.jada.persistency.PersistencyException,
	it.cnr.jada.persistency.IntrospectionException{

	String localTransactionID = null;
	LoggableStatement cs = null;
	try
	{
		cs = new LoggableStatement(getConnection( aUC ),"{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()+"IBMUTL001.getLocalTransactionID(" + 
			(force?"TRUE":"FALSE") + ")}",false,this.getClass());
		cs.registerOutParameter( 1, java.sql.Types.VARCHAR );
		cs.executeQuery();
		localTransactionID = cs.getString(1);
	} catch (Throwable e){
		throw handleException(e);
	} finally {
		try {
			if (cs != null)
				cs.close();
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		}
	}
	return localTransactionID;
	
}
/**  
  *  Inizializzazione di una istanza di Tipo_ammortamentoBulk
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Tipo_ammortamentoBulk.
  *    PostCondition:
  *      Viene impostato l'esercizio prendendo come riferimento quello di scrivania.
  *  
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il bene che deve essere istanziato
  *
  * @return <code>OggettoBulk</code> il Tipo Ammortamento inizializzato
**/
public OggettoBulk inizializzaBulkPerInserimento (UserContext userContext,OggettoBulk bulk) throws ComponentException
{	
	
	Tipo_ammortamentoBulk ti_ammort = (Tipo_ammortamentoBulk)bulk;

	//ti_ammort.setCatBeni(new it.cnr.jada.bulk.SimpleBulkList());
	//ti_ammort.setCatBeniDisponibili(new it.cnr.jada.bulk.SimpleBulkList());
	//ti_ammort.setTi_ammortamento(Tipo_ammortamentoBulk.TIPO_ORDINARIO);
	
	ti_ammort.setEsercizio_competenza(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	
	ti_ammort.setPerc_primo_anno_ord(new java.math.BigDecimal(0));
	ti_ammort.setPerc_successivi_ord(new java.math.BigDecimal(0));
	
	ti_ammort.setPerc_primo_anno_ant(new java.math.BigDecimal(0));
	ti_ammort.setPerc_successivi_ant(new java.math.BigDecimal(0));
	
	ti_ammort.setPerc_primo_anno_altro(new java.math.BigDecimal(0));
	ti_ammort.setPerc_successivi_altro(new java.math.BigDecimal(0));

	try{
		ti_ammort.setLocal_transactionID(getLocalTransactionID(userContext, true));
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(bulk,ex);
	}catch(it.cnr.jada.persistency.IntrospectionException ex){
		throw handleException(bulk,ex);
	}
	
	return ti_ammort;
}
/** 
  *  Inizializzazione di una istanza di Tipo_ammortamentoBulk per modifica
  *    PreCondition:
  *      E' stata richiesta l'inizializzazione di una istanza di Tipo_ammortamentoBulk per modifica.
  *    PostCondition:
  *		 Vengono impostate le caratteristiche del Tipo Ammortamento specificato, (metodo findTipoAmmortamentoPerModifica) e
  *		vengono caricati i gruppi associati, (metodo findGruppiAssociati).
  *      Viene restituito il Tipo Ammortamento inizializzato per la modifica.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il bene che deve essere istanziato
  *
  * @return <code>OggettoBulk</code> il Tipo Ammortamento inizializzato
**/
public OggettoBulk inizializzaBulkPerModifica (UserContext aUC,OggettoBulk bulk) throws ComponentException
{

	Tipo_ammortamentoBulk ti_ammortamento = (Tipo_ammortamentoBulk)super.inizializzaBulkPerModifica(aUC,bulk);
	
	try{
		// Recupera il LOCAL_TRANSACTION_ID
		ti_ammortamento.setLocal_transactionID(getLocalTransactionID(aUC, true));
		
		// Cerca tutti i tipi ammortamento relativi al CD_TIP_AMMORTAMENTO
		findTipoAmmortamentoPerModifica(aUC, ti_ammortamento);
		
		/* Cerca tutti i gruppi associati a questo Tipo Ammortamento e li scrive sulla tabella 
		 * d'appoggio per poterci lavorare
		*/
		findGruppiAssociati(aUC,ti_ammortamento);
		
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
	} catch (it.cnr.jada.persistency.IntrospectionException ie){
		throw new ComponentException(ie);
	}
	
	return ti_ammortamento;
}
/** 
  *  Inizializzazione di una istanza di Tipo_ammortamentoBulk per ricerca
  *    PreCondition:
  *      E' stata richiesta l'inizializzazione di una istanza di Tipo_ammortamentoBulk per ricerca.
  *    PostCondition:
  *      Trasmette il Tipo Ammortamento con tutti gli oggetti collegati e preparato per una operazione di ricerca.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il bulk che deve essere inizializzato.
  *
  * @return <code>OggettoBulk</code> il Tipo Ammortamento inizializzato
**/
public OggettoBulk inizializzaBulkPerRicerca (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	Tipo_ammortamentoBulk ti_ammortamento = (Tipo_ammortamentoBulk)bulk;
	super.inizializzaBulkPerRicerca(aUC,ti_ammortamento);
	
	return ti_ammortamento;
}
/** 
  *  Inizializza sessione di lavoro.
  *    PreCondition:
  *      Viene richiesta una possibile operazione di creazione di un Tipo Ammortamento.
  *    PostCondition:
  *      Viene impostato un SavePoint sulla tabella ASS_TIPO_AMM_CAT_GRUP_INV_APG. 
  *		In caso di chiusura della sessione da parte dell'utente, tutte le operazione fatte 
  *		sul DB saranno annullate a partire da questo punto.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
**/
public void inizializzaGruppi(UserContext userContext) throws it.cnr.jada.comp.ComponentException{
	try {
		setSavepoint(userContext,"ASS_TIPO_AMM_CAT_GRUP_INV_APG");
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	}
}
/** 
  *  Inizializza sessione di lavoro per modifica.
  *    PreCondition:
  *      Viene richiesta una possibile operazione di modifica di un Tipo Ammortamento.
  *    PostCondition:
  *      Viene impostato un SavePoint sulla tabella ASS_TIPO_AMM_CAT_GRUP_INV_APG. 
  *		In caso di chiusura della sessione da parte dell'utente, tutte le operazione fatte 
  *		sul DB saranno annullate a partire da questo punto.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
**/
public void inizializzaGruppiPerModifica(UserContext userContext) throws it.cnr.jada.comp.ComponentException{
	try {
		setSavepoint(userContext,"ASS_TIPO_AMM_CAT_GRUP_INV_APG");
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	}
}
/** 
  *  Blocca i record selezionati
  *    PreCondition:
  *      Viene richiesta una operazione lock sui gruppi selezionati per una operazione di
  *		associazione ad un Tipo Ammortamento.
  *    PostCondition:
  *      Viene impostato il lock sui record rispondenti alle caratteristiche richieste.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> Tipo Ammortamento.
**/
private void lockGruppiSelezionati(UserContext userContext, Tipo_ammortamentoBulk tipo_ammortamento) throws ComponentException{

	LoggableStatement ps = null;
	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	String query = "SELECT CATEGORIA_GRUPPO_INVENT.* " +
					  " FROM " + schema + "CATEGORIA_GRUPPO_INVENT, " + 
					  schema + "ASS_TIPO_AMM_CAT_GRUP_INV, " +
					  schema + "ASS_TIPO_AMM_CAT_GRUP_INV_APG WHERE " +
					  "( CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO = ASS_TIPO_AMM_CAT_GRUP_INV.CD_CATEGORIA_GRUPPO(+) ) AND " + 
					  "( CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO = ASS_TIPO_AMM_CAT_GRUP_INV_APG.CD_CATEGORIA_GRUPPO(+) ) AND " + 
					  "( ASS_TIPO_AMM_CAT_GRUP_INV_APG.LOCAL_TRANSACTION_ID(+) = '" + tipo_ammortamento.getLocal_transactionID() +"' ) AND " + 
					  "( ASS_TIPO_AMM_CAT_GRUP_INV.CD_CATEGORIA_GRUPPO IS NULL ) AND " + 
					  "( ASS_TIPO_AMM_CAT_GRUP_INV_APG.CD_CATEGORIA_GRUPPO IS NULL ) AND " + 
					  "( CATEGORIA_GRUPPO_INVENT.FL_GESTIONE_INVENTARIO = 'Y' ) AND " + 
					  "( CATEGORIA_GRUPPO_INVENT.FL_AMMORTAMENTO = 'Y' ) " + 
					  "FOR UPDATE NOWAIT";

/***
Bisognerebbe aggiungere anche questo?
AND ( cat.LIVELLO > '0' ) AND
	( ass.DT_CANCELLAZIONE(+) IS NULL ) AND
	( ass_apg.DT_CANCELLAZIONE(+) IS NULL )	    
***/ 
	try {	

			ps = new LoggableStatement(getConnection(userContext),query,
					true,this.getClass());
			//ps.setString(1, tipo_ammortamento.getLocal_transactionID()); // LOCAL_TRANSACTION_ID
			ps.execute();
			try{ps.close();}catch( java.sql.SQLException e ){};
		} catch(java.sql.SQLException e) {
			throw handleException(e);
		} finally {
				if (ps != null)
					try{ps.close();}catch( java.sql.SQLException e ){};
		}
}
/**    
  *  Rende persistente le modifiche al Tipo Ammortamento.
  *    PreCondition:
  *      E' stata generata la richiesta di creazione, modifica o eliminazione logica di un Tipo
  *		Ammortamento.
  *    PostCondition:
  *      Viene richiamata la procedura che scriverà sulle tabelle del Tipo Ammortamento
  *		le operazione fatte dall'utente.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> Tipo Ammortamento.
**/
private void makePersistentAssociazioni(UserContext aUC, Tipo_ammortamentoBulk tipo_ammortamento) 
	throws ComponentException, it.cnr.jada.comp.ApplicationException 
{

	String localTransactionID = tipo_ammortamento.getLocal_transactionID();
	String cd_tipo_ammortamento = tipo_ammortamento.getCd_tipo_ammortamento();		
	String is_ordinario = (tipo_ammortamento.isOrdinario())?"Y":"N";
	String is_anticipato = (tipo_ammortamento.isAnticipato())?"Y":"N";
	String is_altro = (tipo_ammortamento.isAltro())?"Y":"N";
	/**** dati per l'eventuale Tipo Ammortamento Associato ***/
	String is_riassociato = (tipo_ammortamento.isPerRiassocia())?"Y":"N";	
	String cd_tipo_riassociato = null;
	String is_ordinario_riassociato = "N";
	String is_anticipato_riassociato = "N";
	String is_altro_riassociato = "N";
	if (tipo_ammortamento.isPerRiassocia()){
		cd_tipo_riassociato = tipo_ammortamento.getAmmortamento_associato().getCd_tipo_ammortamento();
		is_ordinario_riassociato = (tipo_ammortamento.getAmmortamento_associato().isOrdinario())?"Y":"N";
		is_anticipato_riassociato = (tipo_ammortamento.getAmmortamento_associato().isAnticipato())?"Y":"N";
		is_altro_riassociato = (tipo_ammortamento.getAmmortamento_associato().isAltro())?"Y":"N";
	}
	/********************************************************/
	Integer esercizio_competenza = tipo_ammortamento.getEsercizio_competenza();
	String user = tipo_ammortamento.getUser();
	
	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	LoggableStatement  cs = null;
	try
	{
		cs = new LoggableStatement(getConnection( aUC ), "{call " + schema 
				+"CNRCTB400.updAssTipoAmmCatGruppo(?,?,?,?,?,?,?,?,?,?,?,?)}",false,this.getClass());
		cs.registerOutParameter( 1, java.sql.Types.VARCHAR );
		cs.setString(1, localTransactionID); // local_trans_id
		cs.setString(2, cd_tipo_ammortamento); // cd_tipo_ammortamento
		cs.setString(3, is_ordinario); // is_ordinario
		cs.setString(4, is_anticipato); // is_anticipato
		cs.setString(5, is_altro); // is_altro
		
		cs.setString(6, is_riassociato); // is_riassociato
		cs.setString(7, cd_tipo_riassociato); // cd_tipo_riassociato
		cs.setString(8, is_ordinario_riassociato); // is_ordinario_riassociato
		cs.setString(9, is_anticipato_riassociato); // is_anticipato_riassociato
		cs.setString(10, is_altro_riassociato); // is_altro_riassociato
		
		
		cs.setInt(11, esercizio_competenza.intValue()); // esercizio_competenza
		cs.setString(12, user); // utente
		
		try{
			cs.executeQuery();
		} catch (Throwable e) {
			throw handleException(tipo_ammortamento,e);
		} finally {
		    cs.close();
		}
	} catch (java.sql.SQLException e) {
		// Gestisce eccezioni SQL specifiche (errori di lock,...)
		throw handleSQLException(e);
	}
}
/**    
  *  Rende persistente le modifiche al Tipo Ammortamento per riassocia.
  *    PreCondition:
  *      E' stata generata la richiesta di modifica di un Tipo Ammortamento per riassocia.
  *    PostCondition:
  *      Viene richiamata la procedura che scriverà sulle tabelle del Tipo Ammortamento
  *		le operazione fatte dall'utente.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> Tipo Ammortamento.
**/
private void makePersistentRiassocia(UserContext aUC, Tipo_ammortamentoBulk tipo_ammortamento) 
	throws ComponentException, it.cnr.jada.comp.ApplicationException 
{

	String localTransactionID = tipo_ammortamento.getLocal_transactionID();
	String cd_tipo_ammortamento = tipo_ammortamento.getAmmortamento_associato().getCd_tipo_ammortamento();		
	String is_ordinario = (tipo_ammortamento.isOrdinario())?"Y":"N";
	String is_anticipato = (tipo_ammortamento.isAnticipato())?"Y":"N";
	String is_altro = (tipo_ammortamento.isAltro())?"Y":"N";
	Integer esercizio_competenza = tipo_ammortamento.getEsercizio_competenza();
	String user = tipo_ammortamento.getUser();
	
	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	LoggableStatement  cs = null;
	try
	{
		cs = new LoggableStatement(getConnection( aUC ), "{call " + schema 
				+"CNRCTB400.updAssTipoAmmCatGruppo(?,?,?,?,?,?,?)}",false,this.getClass());
		cs.registerOutParameter( 1, java.sql.Types.VARCHAR );
		cs.setString(1, localTransactionID); // local_trans_id
		cs.setString(2, cd_tipo_ammortamento); // cd_tipo_ammortamento destinatario
		cs.setString(3, is_ordinario); // is_ordinario
		cs.setString(4, is_anticipato); // is_anticipato
		cs.setString(5, is_altro); // is_altro
		cs.setInt(6, esercizio_competenza.intValue()); // esercizio_competenza
		cs.setString(7, user); // utente
		
		try{
			cs.executeQuery();
		} catch (Throwable e) {
			throw handleException(tipo_ammortamento,e);
		} finally {
		    cs.close();
		}
	} catch (java.sql.SQLException e) {
		// Gestisce eccezioni SQL specifiche (errori di lock,...)
		throw handleSQLException(e);
	}
}
/**    
  *  Rende persistente la creazione di un Tipo Ammortamento.
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di un Tipo Ammortamento.
  *    PostCondition:
  *      Viene richiamata la procedura che scriverà sulle tabelle del Tipo Ammortamento
  *		i dati inseriti dall'utente.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> Tipo Ammortamento.
**/
private Tipo_ammortamentoBulk makePersistentTipoAmmortamento(UserContext aUC, Tipo_ammortamentoBulk tipo_ammortamento) 
	throws ComponentException, it.cnr.jada.comp.ApplicationException 
{

	Tipo_ammortamentoBulk new_ammortamento = new Tipo_ammortamentoBulk();
	// Crea il Tipo Ammortamento ORDINARIO
	if (tipo_ammortamento.isOrdinario()){
		new_ammortamento = (Tipo_ammortamentoBulk)tipo_ammortamento.clone();
		new_ammortamento.setTi_ammortamento(Tipo_ammortamentoBulk.TIPO_ORDINARIO);
		new_ammortamento.setPerc_primo_anno(tipo_ammortamento.getPerc_primo_anno_ord());
		new_ammortamento.setPerc_successivi(tipo_ammortamento.getPerc_successivi_ord());		
			
		try{
			makeBulkPersistent(aUC,new_ammortamento);
		}catch (Exception pe){
			throw handleException(tipo_ammortamento,pe);
		}
	}

	// Crea il Tipo Ammortamento ANTICIPATO
	if (tipo_ammortamento.isAnticipato()){
		new_ammortamento = (Tipo_ammortamentoBulk)tipo_ammortamento.clone();
		new_ammortamento.setTi_ammortamento(Tipo_ammortamentoBulk.TIPO_ACCELERATO);
		new_ammortamento.setPerc_primo_anno(tipo_ammortamento.getPerc_primo_anno_ant());
		new_ammortamento.setPerc_successivi(tipo_ammortamento.getPerc_successivi_ant());		
			
		try{
			makeBulkPersistent(aUC,new_ammortamento);
		}catch (Exception pe){
			throw handleException(tipo_ammortamento,pe);
		}
	}

	// Crea il Tipo Ammortamento ALTRO
	if (tipo_ammortamento.isAltro()){
		new_ammortamento = (Tipo_ammortamentoBulk)tipo_ammortamento.clone();
		new_ammortamento.setTi_ammortamento(Tipo_ammortamentoBulk.TIPO_ALTRO);
		new_ammortamento.setPerc_primo_anno(tipo_ammortamento.getPerc_primo_anno_altro());
		new_ammortamento.setPerc_successivi(tipo_ammortamento.getPerc_successivi_altro());		
			
		try{
			makeBulkPersistent(aUC,new_ammortamento);
		}catch (Exception pe){
			throw handleException(tipo_ammortamento,pe);
		}
	}

	return new_ammortamento;
}
/** 
  *  Modifica non valida - Valida Tipo Ammortamento non superato.
  *    PreCondition:
  *      I controlli sulle modifiche apportate al tipo ammortamento non sono valide.
  *    PostCondition:
  *      Viene visualizzato un messaggio con la spiegazione dell'errore.
  *  
  *  Modifica Tipo Ammortamento
  *    PreCondition:
  *      E' stata generata la richiesta di modificare un Tipo Ammortamento.
  *    PostCondition:
  *      Viene consentito il salvataggio.
  *  
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Bulk da modificare
  *
  * @return l'oggetto <code>OggettoBulk</code> modificato
**/
public OggettoBulk modificaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException
{
	Tipo_ammortamentoBulk tipo_ammortamento = (Tipo_ammortamentoBulk)bulk;	

	try{


		// Valida il Tipo Ammortamento
		validaTipo_Ammortamento(aUC, tipo_ammortamento);		
		
		updatePercentualiTipoAmmortamento(aUC, tipo_ammortamento);
		if (tipo_ammortamento.isPerRiassocia()){
			tipo_ammortamento.setAmmortamento_associato(findTipoAmmortamentoPerModifica(aUC, tipo_ammortamento.getAmmortamento_associato()));
			updateGruppiPerRiassocia(aUC, tipo_ammortamento);
		}
		makePersistentAssociazioni(aUC, tipo_ammortamento);
		
		//return super.modificaConBulk(aUC,tipo_ammortamento);
		return tipo_ammortamento;
			
	}catch (Throwable e){
		throw handleException(bulk, e);
	}
}
/** 
  *  Inserisce i grupppi temporanei.
  *    PreCondition:
  *      E' stata generata la richiesta di riportare i gruppi selezionati dall'utente nella tabella 
  *		temporanea ASS_TIPO_AMM_CAT_GRUP_INV_APG. 
  *    PostCondition:
  *      Vengono riportati sulla tabella ASS_TIPO_AMM_CAT_GRUP_INV_APG i dati relativi ai 
  *		gruppi selezionati dall'utente.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> Tipo Ammortamento.
  * @param gruppi <code>OggettoBulk[]</code> i gruppi selezionati dall'utente.
  * @param old_ass la <code>BitSet</code> selezione precedente.
  * @param ass la <code>BitSet</code> selezione attuale.
**/
public void modificaGruppi(UserContext userContext,Tipo_ammortamentoBulk tipo_ammortamento, OggettoBulk[] gruppi,java.util.BitSet old_ass,java.util.BitSet ass) throws ComponentException{

	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	LoggableStatement ps = null;
	boolean exist = false;
	try {
		for (int i = 0;i < gruppi.length;i++) {
			Categoria_gruppo_inventBulk gruppo = (Categoria_gruppo_inventBulk)gruppi[i];
			if (old_ass.get(i) != ass.get(i)) {
				
				if (!tipo_ammortamento.isToBeCreated()){
					exist = checkGroupAlreadyExistsInApgForModify(userContext, tipo_ammortamento, gruppo);
				}
				if (!exist){
					
					if (ass.get(i)) {
						exist = false;
						try{
							lockBulk(userContext, gruppo);
						} catch (it.cnr.jada.bulk.OutdatedResourceException e){
							throw handleException(e);
						} catch (it.cnr.jada.bulk.BusyResourceException e){
							throw handleException(e);
						} catch (it.cnr.jada.persistency.PersistencyException e){
							throw handleException(e);
						}
						ps = new LoggableStatement(getConnection(userContext),"INSERT INTO " + 
						schema + 
						"ASS_TIPO_AMM_CAT_GRUP_INV_APG ( CD_TIPO_AMMORTAMENTO, TI_AMMORTAMENTO, CD_CATEGORIA_GRUPPO, ESERCIZIO_COMPETENZA, LOCAL_TRANSACTION_ID ) " +
						"VALUES ( ?, 'O', ?,?,?)",true,this.getClass());	
						ps.setString(1, tipo_ammortamento.getCd_tipo_ammortamento()); // CD_TIPO_AMMORTAMENTO
						ps.setString(2,gruppo.getCd_categoria_gruppo());    // CD_CATEGORIA_GRUPPO
						ps.setInt(3,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue());// ESERCIZIO_COMPETENZA					
						ps.setString(4, tipo_ammortamento.getLocal_transactionID()); // LOCAL_TRANSACTION_ID
						
						ps.execute();
						try{ps.close();}catch( java.sql.SQLException e ){};
					} else {
						ps = new LoggableStatement(getConnection(userContext),"DELETE FROM " + 
						schema + 
						"ASS_TIPO_AMM_CAT_GRUP_INV_APG WHERE " + 
						"CD_CATEGORIA_GRUPPO = ? " + 
						"AND LOCAL_TRANSACTION_ID = ?",true,this.getClass());
						
						ps.setString(1,gruppo.getCd_categoria_gruppo());    // CD_CATEGORIA_GRUPPO
						ps.setString(2,tipo_ammortamento.getLocal_transactionID());    // LOCAL_TRANSACTION_ID 

						ps.execute();					
						try{ps.close();}catch( java.sql.SQLException e ){};
					}
				}
			}
		}
		
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	}  finally {
			if (ps != null)
				try{ps.close();}catch( java.sql.SQLException e ){};
	}
}
/**
  *  Ricerca di un Tipo Ammortamento
  *    PreCondition:
  *      E' stato richiesto di cercare un Tipo Ammortamento.
  *    PostCondition:
  *		 E' stato creato il SQLBuilder con le clausole implicite (presenti nell'istanza di Tipo_ammortamentoBulk);
  *		i tipi ammortamento, inoltre, non devono essere stati cancellati logicamente, (DT_CANCELLAZIONE = NULL).
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
  * @param bulk <code>OggettoBulk</code> il Tipo Ammortamento modello.
  *
  * @return sql <code>SQLBuilder</code> Risultato della selezione.
**/
protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {

	SQLBuilder sql = (SQLBuilder)super.select(userContext,clauses,bulk);
	sql.addSQLClause("AND", "DT_CANCELLAZIONE", sql.ISNULL, null);
	sql.addOrderBy("CD_TIPO_AMMORTAMENTO");

	return sql;
}
/** 
  *  Seleziona gruppi associati
  *    PreCondition:
  *      E' stata generata la richiesta di cercare i gruppi associati al Tipo Ammortamento.
  *    PostCondition:
  *      Viene restituito un Iteratore sui gruppi presenti sulla tabella ASS_TIPO_AMM_CAT_GRUP_INV_APG.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> Tipo Ammortamento.
  * @param bulkClass la <code>Class</code> modello per la categoria gruppo.
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
  *
  * @return iterator <code>RemoteIterator</code> l'Iteratore sulla selezione.
**/
public it.cnr.jada.util.RemoteIterator selectGruppiByClause(
	UserContext userContext,
	Tipo_ammortamentoBulk tipo_ammortamento,
	Class bulkClass, 
	it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws ComponentException{

	if (tipo_ammortamento == null || tipo_ammortamento.getLocal_transactionID() == null)
		return new it.cnr.jada.util.EmptyRemoteIterator();
	
	SQLBuilder sql = getHome(userContext, Categoria_gruppo_inventBulk.class).createSQLBuilder();
	
	sql.addTableToHeader("ASS_TIPO_AMM_CAT_GRUP_INV_APG");
	sql.addSQLJoin("CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO","ASS_TIPO_AMM_CAT_GRUP_INV_APG.CD_CATEGORIA_GRUPPO");
	sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,tipo_ammortamento.getLocal_transactionID());
	sql.addSQLClause("AND","ASS_TIPO_AMM_CAT_GRUP_INV_APG.DT_CANCELLAZIONE",sql.ISNULL, null);
	
	it.cnr.jada.util.RemoteIterator ri = iterator(userContext,sql,bulkClass,null);
	
	return ri;
}
/** 
  * Aggiorna i gruppi per riassocia
  *    PreCondition:
  *      E' stata generata la richiesta di aggiornare i gruppi associati al Tipo Ammortamento 
  *		per una operazione di riassocia ad un altro Tipo Ammortamento.
  *    PostCondition:
  *		 I gruppi associati al Tipo Ammortamento vengono marcati come cancellati. L'associazione, 
  *		in questo modo, cessa di esistere.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> Tipo Ammortamento.
**/
private void updateGruppiPerRiassocia(UserContext userContext, Tipo_ammortamentoBulk tipo_ammortamento) throws ComponentException{

	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	
	LoggableStatement ps = null;
	
	String query = "UPDATE " + schema + "ASS_TIPO_AMM_CAT_GRUP_INV_APG " + 
			"SET DT_CANCELLAZIONE = SYSDATE " + 
			"WHERE LOCAL_TRANSACTION_ID = ? ";

	try {
		ps = new LoggableStatement(getConnection(userContext),query,true,this.getClass());
		ps.setString(1, tipo_ammortamento.getLocal_transactionID()); // LOCAL_TRANSACTION_ID
		
		ps.execute();
		try{ps.close();}catch( java.sql.SQLException e ){};

	} catch(java.sql.SQLException e) {
		throw handleException(e);
	} finally {
			if (ps != null)
				try{ps.close();}catch( java.sql.SQLException e ){};
	}
}
		
		
/** 
  * Aggiorna il Tipo Ammortamento per modifica
  *    PreCondition:
  *      E' stata generata la richiesta di aggiornare il Tipo Ammortamento per modifica.
  *    PostCondition:
  *		 Vengono registrate sul DB le modifiche fatte dall'utente alle percentuali di ammortamento del Tipo Ammortamento.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> Tipo Ammortamento.
**/
private void updatePercentualiTipoAmmortamento(UserContext aUC, Tipo_ammortamentoBulk tipo_ammortamento) 
	throws ComponentException, it.cnr.jada.comp.ApplicationException 
{

	Tipo_ammortamentoBulk new_ammortamento = new Tipo_ammortamentoBulk();
	// Modifica il Tipo Ammortamento ORDINARIO
	if (tipo_ammortamento.isOrdinario()){
		new_ammortamento = (Tipo_ammortamentoBulk)tipo_ammortamento.clone();		
		new_ammortamento.setTi_ammortamento(Tipo_ammortamentoBulk.TIPO_ORDINARIO);
		new_ammortamento.setPerc_primo_anno(tipo_ammortamento.getPerc_primo_anno_ord());
		new_ammortamento.setPerc_successivi(tipo_ammortamento.getPerc_successivi_ord());		
	
		try{
			updateBulk(aUC,new_ammortamento);
		}catch (Exception pe){
			throw handleException(tipo_ammortamento,pe);
		}
	}

	// Modifica il Tipo Ammortamento ANTICIPATO
	if (tipo_ammortamento.isAnticipato()){		
		new_ammortamento = (Tipo_ammortamentoBulk)tipo_ammortamento.clone();		
		new_ammortamento.setTi_ammortamento(Tipo_ammortamentoBulk.TIPO_ACCELERATO);
		new_ammortamento.setPerc_primo_anno(tipo_ammortamento.getPerc_primo_anno_ant());
		new_ammortamento.setPerc_successivi(tipo_ammortamento.getPerc_successivi_ant());		
			
		try{
			updateBulk(aUC,new_ammortamento);
		}catch (Exception pe){
			throw handleException(tipo_ammortamento,pe);
		}
	}

	// Modifica il Tipo Ammortamento ALTRO
	if (tipo_ammortamento.isAltro()){
		new_ammortamento = (Tipo_ammortamentoBulk)tipo_ammortamento.clone();		
		new_ammortamento.setTi_ammortamento(Tipo_ammortamentoBulk.TIPO_ALTRO);
		new_ammortamento.setPerc_primo_anno(tipo_ammortamento.getPerc_primo_anno_altro());
		new_ammortamento.setPerc_successivi(tipo_ammortamento.getPerc_successivi_altro());		

		try{
			updateBulk(aUC,new_ammortamento);
		}catch (Exception pe){
			throw handleException(tipo_ammortamento,pe);
		}
	}
}
/** 
  *  Valida codice Tipo Ammortamento - codice duplicato
  *    PreCondition:
  *      Si sta tentando di creare un nuovo Tipo Ammortamento il cui codice è già utilizzato 
  *		da un altro Tipo Ammortamento, presente sul DB.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare la necessità di specificare un altro codice.
  *
  *  
  *  Valida codice Tipo Ammortamento - Tutti i controlli superati.
  *    PreCondition:
  *      E' stata richiesta una operazione di creazione di un Tipo Ammortamento. Tutti i controlli superati.
  *    PostCondition:
  *      Consente di creare il Tipo Ammortamento.
  * 
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_ammortamento il <code>Tipo_ammortamentoBulk</code> Tipo Ammortamento.
**/
private void validaCDTipo_Ammortamento(UserContext userContext, Tipo_ammortamentoBulk ti_ammort)
	throws ComponentException {

	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();		
				  	 
	String query = "SELECT * FROM " + schema + "TIPO_AMMORTAMENTO " + 					
					"WHERE CD_TIPO_AMMORTAMENTO = ? ";
	
	LoggableStatement ps = null;
	java.sql.ResultSet rs = null;
	
	try {	
		ps = new LoggableStatement(getConnection(userContext),query,true,this.getClass());
		ps.setString(1, ti_ammort.getCd_tipo_ammortamento()); // CD_TIPO_AMMORTAMENTO
		rs = ps.executeQuery();
		if (rs.next()){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: il Codice indicato è già utilizzato. Indicare un codice diverso");
		}
		
		try{rs.close();}catch( java.sql.SQLException e ){};
		try{ps.close();}catch( java.sql.SQLException e ){};	
		
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	} finally {
			if (ps != null)
				try{ps.close();}catch( java.sql.SQLException e ){};
			if (rs != null)
				try{rs.close();}catch( java.sql.SQLException e ){};
	}
}
/** 
  *  Valida Tipo Ammortamento - codice non specificato
  *    PreCondition:
  *      Si sta tentando di salvare un Tipo Ammortamento di cui non si è specificato il codice.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente.
  *  
  *  Valida Tipo Ammortamento - descrizione mancante
  *    PreCondition:
  *      Non è stata specificata una descrizione per il Tipo Ammortamento.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente indicando la necessità di specificare la descrizione.
  *  
  *  Valida Tipo Ammortamento - esercizio di competenza non specificato
  *    PreCondition:
  *		 Si sta tentando di salvare un Tipo Ammortamento di cui non è stato specificato 
  *		l'esercizio di competenza.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente.
  *  
  *  Valida Tipo Ammortamento - nessun tipo specificato
  *    PreCondition:
  *		 Si sta tentando di salvare un Tipo Ammortamento di cui non è stato specificato il tipo.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente.
  *
  *  Valida Tipo Ammortamento - perc. primo anno ORDINARIO non specificata
  *    PreCondition:
  *		 Si sta tentando di salvare un Tipo Ammortamento ORDINARIO di cui non è stata specificata
  *		la percentuale del primo anno.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente con l'indicazione di indicare 
  *		una percentuale per il primo anno.
  *
  *  Valida Tipo Ammortamento - tot percentuali tipo ORDINARIO non valide
  *    PreCondition:
  *		 La somma delle percentuali specificate per il tipo ORDINARIO non è 100.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente.
  *
  *  Valida Tipo Ammortamento - perc. primo anno ANTICIPATO non specificata
  *    PreCondition:
  *		 Si sta tentando di salvare un Tipo Ammortamento ANTICIPATO di cui non è stata specificata
  *		la percentuale del primo anno.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente con l'indicazione di indicare 
  *		una percentuale per il primo anno.
  *
  *  Valida Tipo Ammortamento - tot percentuali tipo ANTICIPATO non valide
  *    PreCondition:
  *		 La somma delle percentuali specificate per il tipo ANTICIPATO non è 100.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente.  
  *
  *  Valida Tipo Ammortamento - perc. primo anno ALTRO non specificata
  *    PreCondition:
  *		 Si sta tentando di salvare un Tipo Ammortamento ALTRO di cui non è stata specificata
  *		la percentuale del primo anno.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente con l'indicazione di indicare 
  *		una percentuale per il primo anno.
  *
  *  Valida Tipo Ammortamento - tot percentuali tipo ALTRO non valide
  *    PreCondition:
  *		 La somma delle percentuali specificate per il tipo ALTRO non è 100.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente.
  *
  *  Valida Tipo Ammortamento - Tipo Ammortamento per riassocia non specificato
  *    PreCondition:
  *		 Si sta tentando di salvare un Tipo Ammortamento i cui gruppi sono stati riassociati 
  *		ad un altro Tipo Ammortamento, ma non è stato specificato il Tipo Ammortamento di destinazione.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente.
  *  
  *  Valida Tipo Ammortamento - Tutti i controlli superati.
  *    PreCondition:
  *      E' stata richiesta una operazione di salvataggio di un Tipo Ammortamento. Tutti i controlli superati.
  *    PostCondition:
  *      Consente di salvare il Tipo Ammortamento.
  * 
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param ti_ammort il <code>Tipo_ammortamentoBulk</code> Tipo Ammortamento.
**/
private void validaTipo_Ammortamento(UserContext userContext, Tipo_ammortamentoBulk ti_ammort)
	throws ComponentException {
		
	Categoria_gruppo_inventBulk catBene = new Categoria_gruppo_inventBulk();
	java.math.BigDecimal zero = new java.math.BigDecimal(0);
	java.math.BigDecimal cento = new java.math.BigDecimal(100);
	
	try{
		// CONTROLLA CHE SIA STATO SPECIFICATO UN CODICE PER IL TIPO AMMORTAMENTO
		if (ti_ammort.getCd_tipo_ammortamento()==null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare il Codice del Tipo Ammortamento");

		// CONTROLLA CHE SIA STATA INDICATA UNA DESCRIZIONE PER IL TIPO AMMORTAMENTO			
		if (ti_ammort.getDs_tipo_ammortamento()==null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare una Descrizione per il Tipo Ammortamento");
		
		// CONTROLLA CHE SIA STATO INDICATO UN ESERCIZIO DI COMPETENZA
		if (ti_ammort.getEsercizio_competenza()==null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare un Esercizio di Competenza per il Tipo Ammortamento");
			
			
		/*************** CONTROLLO SULLE PERCENTUALI INDICATE PER I DIVERSI TIPI **********/

		if (!ti_ammort.isOrdinario() && !ti_ammort.isAnticipato() && !ti_ammort.isAltro()){
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare almeno un tipo di Ammortamento");
		}
		
		// Se è stato selezionato il TIPO ORDINARIO
		if (ti_ammort.isOrdinario()){
			if (ti_ammort.getPerc_primo_anno_ord()==null || ti_ammort.getPerc_primo_anno_ord().compareTo(zero)==0 || ti_ammort.getPerc_primo_anno_ord().compareTo(cento)>0){
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: la percentuale primo anno per il Tipo Ordinario non è valida");
			}

			if ((ti_ammort.getPerc_successivi_ord()!= null) && (ti_ammort.getPerc_primo_anno_ord().add(ti_ammort.getPerc_successivi_ord())).compareTo(cento)>0){
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: le percentuali indicate per il Tipo Ordinario non sono valide");
			}

			if (ti_ammort.getPerc_successivi_ord() == null)
				ti_ammort.setPerc_successivi_ord(zero);
		}
		
		// Se è stato selezionato il TIPO ANTICIPATO
		if (ti_ammort.isAnticipato()){
			if (ti_ammort.getPerc_primo_anno_ant()==null || ti_ammort.getPerc_primo_anno_ant().compareTo(zero)==0){
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: la percentuale primo anno per il Tipo Anticipato non è valida");
			}
			
			if ((ti_ammort.getPerc_successivi_ant()!= null) && (ti_ammort.getPerc_primo_anno_ant().add(ti_ammort.getPerc_successivi_ant())).compareTo(cento)>0){
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: le percentuali indicate per il Tipo Anticipato non sono valide");
			}

			if (ti_ammort.getPerc_successivi_ant() == null)
				ti_ammort.setPerc_successivi_ant(zero);
		}
		
		// Se è stato selezionato il TIPO ALTRO
		if (ti_ammort.isAltro()){
			if (ti_ammort.getPerc_primo_anno_altro()==null || ti_ammort.getPerc_primo_anno_altro().compareTo(zero)==0){
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: la percentuale primo anno per il Tipo Altro non è valida");
			}

			if ((ti_ammort.getPerc_successivi_altro()!= null) && (ti_ammort.getPerc_primo_anno_altro().add(ti_ammort.getPerc_successivi_altro())).compareTo(cento)>0){
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: le percentuali indicate per il Tipo Altro non sono valide");
			}

			
			if (ti_ammort.getPerc_successivi_altro() == null)
				ti_ammort.setPerc_successivi_altro(zero);
		}
		/*********************************************************************************/

		// CONTROLLA, NEL CASO CHE SIA STATA RICHIESTA UNA OPERAZIONE DI RIASSOCIA, CHE SIA STATO SPECIFICATO UN TIPO AMMORTAMENTO
		if (ti_ammort.isPerRiassocia() && ti_ammort.getAmmortamento_associato() == null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: è necessario specificare un Tipo Ammortamento per l'operazione di Riassocia");
		
	}catch(Throwable t){
		throw handleException(ti_ammort, t);		
	}
}
}
