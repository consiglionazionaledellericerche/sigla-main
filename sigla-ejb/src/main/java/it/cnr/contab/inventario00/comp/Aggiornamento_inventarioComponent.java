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

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.ejb.EJBException;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.incarichi00.service.ContrattiService;
import it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniHome;
import it.cnr.contab.inventario00.docs.bulk.Inventario_utilizzatori_laBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_utilizzatori_laHome;
import it.cnr.contab.inventario00.docs.bulk.Utilizzatore_CdrVBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioHome;
import it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkCollections;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.bulk.StorageFile;

public class Aggiornamento_inventarioComponent 
		extends it.cnr.jada.comp.CRUDDetailComponent 
			implements  Serializable, Cloneable {
/**
 * Inventario_beniComponent constructor comment.
 */
public Aggiornamento_inventarioComponent() {
	super();
}

/**
  *	Controllo se l'esercizio COEP di scrivania e' aperto
  *
  * Nome: Controllo chiusura esercizio COEP
  * Pre:  E' stata richiesta la modifica di un bene in inventario
  * Post: Viene chiamata una stored procedure che restituisce 
  *		  -		'Y' se il campo stato della tabella CHIUSURA_COEP vale C o P
  *		  -		'N' altrimenti
  *		  Se l'esercizio e' chiuso e' impossibile proseguire
  *
  * @param  userContext <code>UserContext</code>
  
  * @return boolean : TRUE se stato = C
  *					  FALSE altrimenti
  */
protected boolean isEsercizioCOEPChiuso(UserContext userContext) throws ComponentException {
	
	LoggableStatement cs = null;	
	String status = null;

	
	try
	{
		// Controlla lo Stato 'C'
		cs = new LoggableStatement(getConnection(userContext),"{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +	"CNRCTB200.isChiusuraCoepDef(?,?)}",false,this.getClass());		

		cs.registerOutParameter( 1, java.sql.Types.VARCHAR);
		cs.setObject( 2, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext)	);		
		cs.setObject( 3, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext)		);		
		
		cs.executeQuery();

		status = new String(cs.getString(1));

	    if(status.compareTo("Y")==0){
	    	return true;
	    }

	    // Resetta i parametri
	    cs.clearParameters();

	    // Controlla lo Stato 'P'
		cs = new LoggableStatement(getConnection(userContext),"{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +	"CNRCTB200.isChiusuraCoepProva(?,?)}",false,this.getClass());		

		cs.registerOutParameter( 1, java.sql.Types.VARCHAR);
		cs.setObject( 2, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext)	);		
		cs.setObject( 3, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext)		);		
		
		cs.executeQuery();

		status = new String(cs.getString(1));

	    if(status.compareTo("Y")==0){
	    	return true;
	    }
	    	
	} catch (java.sql.SQLException ex) {
		throw handleException(ex);
	}
	
    return false;		    	
}
public Id_inventarioBulk caricaInventario(UserContext aUC)
		throws ComponentException,
		it.cnr.jada.persistency.PersistencyException,
		it.cnr.jada.persistency.IntrospectionException 
{
	
	Id_inventarioHome inventarioHome = (Id_inventarioHome) getHome(aUC, Id_inventarioBulk.class);
	Id_inventarioBulk inventario = inventarioHome.findInventarioFor(aUC,false);

	// NON ESISTE UN INVENTARIO ASSOCIATO ALLA UO DI SCRIVANIA
	if (inventario == null)
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: non esiste alcun inventario associato alla UO");
		
	// L'INVENTARIO NON E' APERTO
	if (!inventarioHome.isAperto(inventario,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC)))
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: l'inventario non è in stato 'APERTO'");

	return inventario;
}


/**
  *  Cerca l'Inventario associato alla Uo di scrivania
  *    PreCondition:
  *      Non c'è un Inventario associato alla Uo di scrivania, oppure l'Inventario non è in stato "Aperto"
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
  *
  *  Ricerca di un Bene
  *    PreCondition:
  *      E' stato richiesto di cercare un Bene
  *    PostCondition:
  *		E' stato creato il SQLBuilder con le clausole implicite (presenti nell'istanza di Inventario_beniBulk):
  *		i beni devono altresì appartenere all'Inventario associato alla Uo di scrivania e
  *		non devono essere totalmente scaricati.
  *		I beni utilizzabili, inoltre, devono avere ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.  
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione
  * @param bulk <code>OggettoBulk</code> il Bene modello
  *
  * @return sql <code>SQLBuilder</code> Risultato della selezione.
**/
protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) 
	throws ComponentException, it.cnr.jada.persistency.PersistencyException {

	// Effettuo la ricerca dei beni	
	Id_inventarioBulk inventario = null;
	
	try{
		inventario = caricaInventario(userContext);
	} catch (it.cnr.jada.persistency.IntrospectionException e){
		throw new it.cnr.jada.comp.ComponentException(e);
	}
	
	SQLBuilder sql = (SQLBuilder)super.select(userContext,clauses, bulk);	
	Inventario_beniBulk bene = (Inventario_beniBulk)bulk;
	
	sql.addSQLClause("AND", "PG_INVENTARIO", sql.EQUALS, inventario.getPg_inventario());
	sql.addSQLClause("AND", "FL_TOTALMENTE_SCARICATO", sql.EQUALS, Inventario_beniBulk.ISNOTTOTALMENTESCARICATO);
	// Aggiunta clausola che visualizzi solo i beni che abbiano 
	//	ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.
	sql.addClause("AND", "esercizio_carico_bene", sql.LESS_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	return sql;
	
}

public SQLBuilder selectCdrByClause(UserContext userContext, Utilizzatore_CdrVBulk vutilizzatore, CdrBulk cdr, CompoundFindClause clauses) 
		throws ComponentException
{
		
		SQLBuilder sql = getHome(userContext, CdrBulk.class).createSQLBuilder();
		sql.addClause( clauses );
		sql.addSQLClause("AND", "ESERCIZIO_INIZIO", sql.LESS_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND", "ESERCIZIO_FINE", sql.GREATER_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		return sql;		
}

/**
  *  Ricerca di una Linea di Attività per il CdR Utilizzatore
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca di una Linea di Attività per il
  *		CdR Utilizzatore
  *    PostCondition:
  *		Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
  *		clausole che la Linea di Attività appartenga al CdR indicato.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param utilizzatori_la il <code>Inventario_utilizzatori_laBulk</code> CdR di riferimento
  * @param l_att la <code>Linea_attivitaBulk</code> Linea di Attività modello  
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione
  *
  * @return sql <code>SQLBuilder</code> Risultato della selezione.
**/
public SQLBuilder selectLinea_attivitaByClause(UserContext userContext, Inventario_utilizzatori_laBulk utilizzatori_la, it.cnr.contab.config00.latt.bulk.WorkpackageBulk l_att, CompoundFindClause clauses) 
		throws ComponentException
{		
	SQLBuilder sql = getHome(userContext, it.cnr.contab.config00.latt.bulk.WorkpackageBulk.class,"V_LINEA_ATTIVITA_VALIDA").createSQLBuilder();
	sql.addClause( clauses );
	sql.addTableToHeader("FUNZIONE,NATURA,PARAMETRI_CNR,UNITA_ORGANIZZATIVA,CDR");
	
	sql.openParenthesis(FindClause.AND);
	sql.addSQLClause(FindClause.OR,"V_LINEA_ATTIVITA_VALIDA.TI_GESTIONE",SQLBuilder.EQUALS,WorkpackageBulk.TI_GESTIONE_SPESE);
	sql.addSQLClause(FindClause.OR,"V_LINEA_ATTIVITA_VALIDA.TI_GESTIONE",SQLBuilder.EQUALS,WorkpackageBulk.TI_GESTIONE_ENTRAMBE);
	sql.closeParenthesis();
	
	sql.addSQLClause("AND","V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA",sql.EQUALS, utilizzatori_la.getCdr().getCd_centro_responsabilita());
	sql.addSQLClause("AND","V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	sql.addSQLClause("AND","UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
	sql.addSQLJoin("FUNZIONE.CD_FUNZIONE",sql.EQUALS,"V_LINEA_ATTIVITA_VALIDA.CD_FUNZIONE");
	sql.addSQLJoin("CDR.CD_CENTRO_RESPONSABILITA",sql.EQUALS,"V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA");
	sql.addSQLJoin("FUNZIONE.CD_FUNZIONE",sql.EQUALS,"V_LINEA_ATTIVITA_VALIDA.CD_FUNZIONE");
	sql.addSQLClause("AND","PARAMETRI_CNR.ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));	
	sql.addSQLClause("AND","FUNZIONE.FL_UTILIZZABILE",sql.EQUALS,"Y");
	sql.openParenthesis("AND");
	sql.addSQLClause("AND","NATURA.FL_SPESA",sql.EQUALS,"Y");
	sql.addSQLClause("OR","PARAMETRI_CNR.FL_REGOLAMENTO_2006", sql.EQUALS, "N");
	sql.addSQLClause("OR","UNITA_ORGANIZZATIVA.CD_TIPO_UNITA", sql.EQUALS,Tipo_unita_organizzativaHome.TIPO_UO_AREA);
	sql.closeParenthesis();
	sql.setDistinctClause(true);
	it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
	try {
		config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_LINEA_ATTIVITA_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_LINEA_COMUNE_VERSAMENTO_IVA);
	} catch (RemoteException e) {
		throw new ComponentException(e);
	} catch (EJBException e) {
		throw new ComponentException(e);
	}
	if (config != null){
		sql.addSQLClause( "AND", "CD_LINEA_ATTIVITA",  sql.NOT_EQUALS, config.getVal01());
	}
	return sql;	
}
/** 
  *  Ricerca di una Ubicazione
  *    PreCondition:
  *      E' stata generata la richiesta di ricercare una Ubicazione.
  *    PostCondition:
  *		E' stato creato il SQLBuilder con le clausole implicite (presenti nell'istanza di Ubicazione_beneBulk),
  *		ed è stata aggiunta la clausola che l'Ubicazione sia associata alla UO di scrivania.
  *  
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param aggiorno il <code>Aggiornamento_inventarioBulk</code> bene di riferimento
  * @param ubicazione la <code>Ubicazione_beneBulk</code> ubicazione di riferimento  
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione
  *
  * @return sql <code>SQLBuilder</code> Risultato della selezione.
**/
public SQLBuilder selectUbicazione_destinazioneByClause(UserContext userContext, Aggiornamento_inventarioBulk aggiorno, Ubicazione_beneBulk ubicazione, CompoundFindClause clauses) 
		throws ComponentException
{

		String cds_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext);
		String uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
		
		SQLBuilder sql = getHome(userContext, Ubicazione_beneBulk.class).createSQLBuilder();
		sql.addClause( clauses );

		sql.openParenthesis("AND");

		// Cerca le ubicazioni relative al CdS/UO discrivania	
		sql.addSQLClause("AND","CD_CDS",sql.EQUALS, cds_scrivania);
		sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS, uo_scrivania);
		
//		public static final String CD_CDS_FITTIZIO = "999";
//		public static final String CD_UO_FITTIZIO = "999.000";
//		// Aggiunge alle Ubicazioni della UO di scrivania, quelle fittizie.
//		sql.addSQLClause("OR","CD_CDS",sql.EQUALS, Ubicazione_beneBulk.CD_CDS_FITTIZIO);
//		sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS, Ubicazione_beneBulk.CD_UO_FITTIZIO);
//		sql.closeParenthesis();

		try {
			Unita_organizzativa_enteBulk uoEnte=(Unita_organizzativa_enteBulk)(Utility.createUnita_organizzativaComponentSession().getUoEnte(userContext));
			sql.addSQLClause("OR","CD_CDS",sql.EQUALS, uoEnte.getCd_unita_padre());
			sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS, uoEnte.getCd_unita_organizzativa());
		} catch (Exception e) {
			throw handleException(e);
		}
		sql.closeParenthesis();
		sql.addOrderBy("LIVELLO");
		sql.addOrderBy("CD_UBICAZIONE");
		
		return sql;		
}


/** 
  *  validaUtilizzatori - CdR Utilizzatore non valido
  *    PreCondition:
  *      Si sta tentando di salvare un CdR Utilizzatore di cui non si è specificato il codice.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare la necessità di 
  *		specificare il codice del CdR.
  *
  *  validaUtilizzatori - CdR Utilizzatore non valido
  *    PreCondition:
  *      Il CdR Utilizzatore è stato indicato già in precedenza.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare l'impossibilità 
  *		utilizzare più volte lo stesso CdR
  *
  *  validaUtilizzatori - CdR Utilizzatore non valido
  *    PreCondition:
  *      Si sta tentando di salvare un CdR Utilizzatore di cui non si è specificata la percentuale di utilizzo.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare la necessità di 
  *		specificare la percentuale di utilizzo.
  *
  *  validaUtilizzatori - CdR Utilizzatore non valido
  *    PreCondition:
  *      Si sta tentando di salvare un CdR Utilizzatore di cui non si è specificata alcuna Linea di Attività
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare la necessità di 
  *		specificare per ogni CdR almeno una Linea di Attività.
  *  
  *  validaUtilizzatori - Percentuale utilizzo dei CdR non valida
  *    PreCondition:
  *      Il totale delle percentuali di utilizzo indicate per ogni CdR, non è 100.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente per segnalare che il totale deve essere 100.
  *    
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param aggiorno il <code>Aggiornamento_inventarioBulk</code> bene di cui controllare gli Utilizzatori
**/
public void validaUtilizzatori (UserContext aUC,Aggiornamento_inventarioBulk aggiorno) 
	throws ComponentException
{
	
	SimpleBulkList cdr_utilizzatori = aggiorno.getV_utilizzatoriColl();
	java.math.BigDecimal zero = new java.math.BigDecimal(0);
	java.math.BigDecimal cento = new java.math.BigDecimal(100);
	java.math.BigDecimal percentuale_utilizzo_CdR = new java.math.BigDecimal("0");	
	java.util.Vector cdr = new java.util.Vector();
	int riga = 0;
		
	if (cdr_utilizzatori.size()>0){
	
		Iterator i = cdr_utilizzatori.iterator();		
		while (i.hasNext()){
			riga++;
			
			Utilizzatore_CdrVBulk utilizzatore = (Utilizzatore_CdrVBulk)i.next();

			// validazione delle singole Linee di Attività
			
			SimpleBulkList utilizzatori_LA = utilizzatore.getBuono_cs_utilizzatoriColl();
			java.math.BigDecimal percentuale_utilizzo_LA = new java.math.BigDecimal("0");
			Vector cdr_la = new Vector();
					
	
			if (utilizzatori_LA.size()>0){
				Iterator a = utilizzatori_LA.iterator();
				while (a.hasNext()){
					Inventario_utilizzatori_laBulk utilizzatore_LA = (Inventario_utilizzatori_laBulk)a.next();

					// Controlla che sia stata specificata la line di Attività
					if (utilizzatore_LA.getLinea_attivita()==null || utilizzatore_LA.getLinea_attivita().getCd_linea_attivita() == null){
							throw new it.cnr.jada.comp.ApplicationException ("GAE non valido. Indicare sia il codice del GAE, sia la sua percentuale di utilizzo");								
					}
			
					// Controlla che non vi siano Linee di Attività DUPLICATE
					if (BulkCollections.containsByPrimaryKey(cdr_la,utilizzatore_LA.getLinea_attivita())){
							throw new it.cnr.jada.comp.ApplicationException ("GAE duplicato. Non è possibile indicare più volte uno stesso GAE");
					}
					else {
						cdr_la.add(utilizzatore_LA.getLinea_attivita());
					}
			
					// Controlla che sia stata indicata una PERCENTUALE DI UTILIZZO VALIDA per Linea di Attività
					if (utilizzatore_LA.getPercentuale_utilizzo_la()!=null){
						percentuale_utilizzo_LA = percentuale_utilizzo_LA.add(utilizzatore_LA.getPercentuale_utilizzo_la());
					}
					else{
						throw new it.cnr.jada.comp.ApplicationException ("La percentuale di utilizzo per i GAE non può essere nulla");
					}
				}

				// Controlla che il totale delle percentuali di utilizzo delle Linee di Attività sia 100
				if (percentuale_utilizzo_LA.compareTo(cento)!=0)
					throw new it.cnr.jada.comp.ApplicationException ("La percentuale di utilizzo per i GAE non è valida");
				}

				// Controlla che sia stato specificato il CdR
				if (utilizzatore.getCdr()==null || utilizzatore.getCdCdr()==null){
					throw new it.cnr.jada.comp.ApplicationException ("Attenzione: è necessario indicare il codice del CdR Utilizzatore.\n " +
					"Il CdR alla riga " + riga + " non è valido");
				}
			
				// Controlla che non vi siano CdR DUPLICATI
				if (it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(cdr,utilizzatore.getCdr())){
				throw new it.cnr.jada.comp.ApplicationException ("Attenzione: non è possibile indicare più volte uno stesso CdR come Utilizzatore\n " +
					"Il CdR " + utilizzatore.getCdCdr() +" è duplicato.");
			}
			else {
				cdr.add(utilizzatore.getCdr());
			}
			
			// Controlla che sia stata indicata una PERCENTUALE DI UTILIZZO VALIDA per il CdR
			if (utilizzatore.getPercentuale_utilizzo_cdr()!=null && ((utilizzatore.getPercentuale_utilizzo_cdr().compareTo(zero))>0)){
				percentuale_utilizzo_CdR = percentuale_utilizzo_CdR.add(utilizzatore.getPercentuale_utilizzo_cdr());				 
			}
			else if (utilizzatore.getPercentuale_utilizzo_cdr()==null){
				throw new it.cnr.jada.comp.ApplicationException ("Attenzione: la percentuale di utilizzo per i CdR Utilizzatori non può essere nulla\n " +
					"Specificare la perc. di utilizzo per il CdR " + utilizzatore.getCdCdr());
			}
			
			// Controlla che per ogni CdR specificato siano state indicate anche delle Linee di Attività
			if (utilizzatore.getBuono_cs_utilizzatoriColl()==null || (utilizzatore.getBuono_cs_utilizzatoriColl().size()==0)){
				throw new it.cnr.jada.comp.ApplicationException ("Attenzione: è necessario specificare i GAE per ogni Utilizzatore\n "+
					"Indicare le Linee di Attività per il CdR " + utilizzatore.getCdCdr());
			}
		}

		// Controlla che il totale delle percentuali di utilizzo dei CdR sia 100
		if (percentuale_utilizzo_CdR.compareTo(cento)!=0)
			throw new it.cnr.jada.comp.ApplicationException ("Attenzione: la percentuale totale di utilizzo dei CdR deve essere 100.\n "+
				"Verificare le percentuali di utilizzo degli Utilizzatori ");
	
	}
}
/** 
  *  Cerca l'Inventario associato alla Uo di scrivania
  *    PreCondition:
  *      Non c'è un Inventario associato alla Uo di scrivania, oppure l'Inventario non è in stato "Aperto"
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con la spiegazione dell'errore
  *
  *  Inizializzazione di una istanza di Inventario_beniBulk
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Inventario_beniBulk
  *    PostCondition:
  *      Vengono caricate le Condizioni_bene valide.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param oggettoBulk <code>OggettoBulk</code> il bene che deve essere istanziato
  *
  * @return <code>OggettoBulk</code> il bene inizializzato
**/

public OggettoBulk inizializzaBulkPerInserimento (UserContext aUC, OggettoBulk oggettoBulk) 
		throws ComponentException
{
	    Aggiornamento_inventarioBulk aggiorno = (Aggiornamento_inventarioBulk)super.inizializzaBulkPerInserimento(aUC,oggettoBulk);
	
		// CARICO L'INVENTARIO ASSOCIATO
		Id_inventarioBulk inv;
		try {
			inv = caricaInventario(aUC);
			aggiorno.setInventario(inv);
			aggiorno.setBuono_carico_scarico_dettColl(new SimpleBulkList());
			
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		}

	if (isEsercizioCOEPChiuso(aUC))
    	return asRO( aggiorno, "Funzione disponibile solo in lettura. L'esercizio COEP per il CdS " + it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(aUC) + " risulta in stato 'C' o 'P'.");
		
	return aggiorno;
}

    

/** 
  *  Valida  - 
  *    PreCondition:
  *      Non sono stati selezionati beni per l'operazione di trasferimento.
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente.
  *
  *    PostCondition:
  *      Un messaggio di errore viene visualizzato all'utente.
  *  
  *  Valida  - Tutti i controlli superati.
  *    PreCondition:
  *      E' stata richiesta una operazione di Aggiornamento di Inventario. Tutti i controlli superati.
  *    PostCondition:
  *      Consente di proseguire con le operazioni di salvataggio.
  * 
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
  * @param aggiorno il <code>Aggiornamento_inventarioBulk</code> Aggiornamento inventario.
**/
private void valida (UserContext userContext, Aggiornamento_inventarioBulk aggiorno) throws ComponentException {
	if (aggiorno.getDettagli().size() ==0 )
	  throw new it.cnr.jada.comp.ApplicationException ("Attenzione: è necessario specificare almeno un bene da aggiornare!");
	if ((aggiorno.getassegnatario() == null)&&(aggiorno.getUbicazione_destinazione()==null)&&(aggiorno.getV_utilizzatoriColl().size() ==0 ))
		  throw new it.cnr.jada.comp.ApplicationException ("Attenzione: è necessario specificare almeno un dato da aggiornare!");
}
/** 
  *     PreCondition:
  *     E' stata generata la richiesta di riportare i beni selezionati dall'utente nella tabella 
  *		L'utente, in questa fase, si trova a selezionare dei 
  *		beni già presenti sul DB, per una operazione di aggiornamento.
  *  
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param  aggiornamento il <code> Aggiornamento_inventarioBulk </code>.
  * @param beni <code>OggettoBulk[]</code> i beni selezionati dall'utente.
  * @param old_ass la <code>BitSet</code> selezione precedente.
  * @param ass la <code>BitSet</code> selezione attuale.
**/
public void modificaBeniAggiornati(UserContext userContext,Aggiornamento_inventarioBulk aggiornamento, OggettoBulk[] beni,java.util.BitSet old_ass,java.util.BitSet ass) throws ComponentException {
		java.sql.PreparedStatement ps = null;
		
			for (int i = 0;i < beni.length;i++) {
				Inventario_beniBulk bene = (Inventario_beniBulk)beni[i];
				if (old_ass.get(i) != ass.get(i)) {
						
					if (ass.get(i)) {		
						// Locko il bene che è stato selezionato per essere aggiornato.
						try{
							lockBulk(userContext, bene)	;
						} catch (OutdatedResourceException oe){
							throw handleException(oe);
						} catch (BusyResourceException bre){
							throw new ApplicationException("Risorsa occupata.\nIl bene " + bene.getNumeroBeneCompleto() + " è bloccato da un altro utente.");
						} catch (it.cnr.jada.persistency.PersistencyException pe){
							throw handleException(pe);
						} 
						finally {
									   if (ps != null)
										   try{ps.close();}catch( java.sql.SQLException e ){};
							   }
			  	}
			}	
		}
}

/** 
  * Aggiorna tutti i beni disponibili.
  *    PreCondition:
  *      E' stata generata la richiesta di aggiornare tutti i beni disponibili.
  *    PostCondition:
  *      Vengono riportati sulla tabella INVENTARIO_BENI tutti i beni disponibili. 
	*
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param aggiorno il <code>Aggiornamento_inventarioBulk</code>.
  * @param clauses le <code>CompoundFindClause</code> clausole aggiunte dall'utente.
**/ 
public Aggiornamento_inventarioBulk aggiornaTuttiBeni(UserContext userContext,Aggiornamento_inventarioBulk aggiorno, CompoundFindClause clauses) throws ComponentException {
	
	try{
		PersistentHome dettHome = getHome(userContext,Inventario_beniBulk.class, null, getFetchPolicyName("findAssegnatarioCondizione"));
		
		SQLBuilder sql = new SQLBuilder(Inventario_beniBulk.class);
		sql.addClause(clauses);
		sql.addSQLClause("AND", "INVENTARIO_BENI.PG_INVENTARIO", sql.EQUALS, aggiorno.getInventario().getPg_inventario());	
		sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", sql.EQUALS, "N"); // Non scaricati totalmente
		sql.addSQLClause("AND", "INVENTARIO_BENI.ESERCIZIO_CARICO_BENE", sql.LESS_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		
		for (java.util.Iterator iterator = dettHome.fetchAll(sql).iterator();iterator.hasNext();){
			Inventario_beniBulk bene = (Inventario_beniBulk)iterator.next();
			aggiorno.addToDettagli(bene);
		}
		getHomeCache(userContext).fetchAll(userContext,dettHome);
		return aggiorno;
	}catch(it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(e);
	} catch (PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  Cerca tutti i beni disponibili per una operazione di Aggiornamento.
  *    PreCondition:
  *      E' stata generata la richiesta di cercare tutti i beni che rispondono alle caratteristiche 
  *		per essere scaricati.
  *		I beni disponibili sono tutti quei beni che rispondono alle seguenti caratteristiche:
  *		  	- appartengono allo stesso Inventario associato alla UO di scrivania;
  *			- NON sono stati scaricati totalmente, (INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO = 'N').
  *		I beni utilizzabili, inoltre, devono avere ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.    
  *    PostCondition:
  *     Viene costruito e restituito l'Iteratore sui beni disponibili.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
  * @param aggiorno <code>Aggiornamento_inventarioBulk</code> il Buono di Scarico.
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
  *
  * @return l'Iteratore <code>RemoteIterator</code> sui beni trovati.
**/
public RemoteIterator cercaBeniAggiornabili(UserContext userContext, Aggiornamento_inventarioBulk aggiorno, CompoundFindClause clauses) throws ComponentException {

	SQLBuilder sql = getHome(userContext,Inventario_beniBulk.class).createSQLBuilder();
	sql.addClause(clauses);
	sql.addSQLClause("AND", "INVENTARIO_BENI.PG_INVENTARIO", sql.EQUALS, aggiorno.getInventario().getPg_inventario());	
	sql.addSQLClause("AND", "INVENTARIO_BENI.FL_TOTALMENTE_SCARICATO", sql.EQUALS, "N"); // Non scaricati totalmente
	// Aggiunta clausola che visualizzi solo i beni che abbiano 
	sql.addSQLClause("AND", "INVENTARIO_BENI.ESERCIZIO_CARICO_BENE", sql.LESS_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	sql.addOrderBy("INVENTARIO_BENI.NR_INVENTARIO, INVENTARIO_BENI.PROGRESSIVO");

	return iterator(userContext,sql,Inventario_beniBulk.class,null);	
}
public Aggiornamento_inventarioBulk aggiornaBeni(UserContext userContext, Aggiornamento_inventarioBulk aggiorno)
	throws	ComponentException {
	
		validaUtilizzatori(userContext,aggiorno);
		
		valida(userContext,aggiorno);
		
	try {	
		
     for (java.util.Iterator i = aggiorno.getDettagli().iterator();i.hasNext();) {
		     	
			it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk bene=(it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk) i.next();
		
			if ((aggiorno.getassegnatario()!= null && aggiorno.getassegnatario().getCd_terzo()!=null )||(aggiorno.getUbicazione_destinazione()!= null && aggiorno.getUbicazione_destinazione().getCd_ubicazione()!=null)){
				
				lockBulk(userContext,bene);
				
				if (aggiorno.getassegnatario()!= null)	
					bene.setAssegnatario(aggiorno.getassegnatario());
					
				if (aggiorno.getUbicazione_destinazione()!= null)
					bene.setUbicazione(aggiorno.getUbicazione_destinazione());
			
				bene.setToBeUpdated();
			
				super.modificaConBulk(userContext,bene);
			}
			if (aggiorno.getV_utilizzatoriColl().size()!=0) 
					aggiorna_utilizzatori(userContext,aggiorno,bene);
   		}
		return aggiorno;	
		
	} catch (PersistencyException e) {
		throw new ComponentException (e);
	} catch (OutdatedResourceException e) {				
		throw new ApplicationException ("Risorsa non pi\371 valida. Ripetere la selezione dei beni.");
	} catch (BusyResourceException e) {				
		throw new ComponentException (e);
	}	     				
}
	public Aggiornamento_inventarioBulk aggiornaStatoBeni(UserContext userContext, Aggiornamento_inventarioBulk aggiorno)
			throws	ComponentException {
		if (aggiorno.getDettagli().isEmpty())
			throw new it.cnr.jada.comp.ApplicationException ("Attenzione: è necessario specificare almeno un bene da aggiornare!");
		if (aggiorno.getStato() == null)
			throw new it.cnr.jada.comp.ApplicationException ("Attenzione: è necessario specificare lo stato da assegnare ai beni!");
		if (aggiorno.getStato().equals(Inventario_beniBulk.STATO_SMARRITO) && aggiorno.getFile()==null)
			throw new it.cnr.jada.comp.ApplicationException ("Attenzione: è necessario allegare un file per lo stato selezionato!");

		ContrattiService contrattiService = SpringUtil.getBean(ContrattiService.class);

		try {
			for (java.util.Iterator i = aggiorno.getDettagli().iterator();i.hasNext();) {
				it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk bene=(it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk) i.next();

				lockBulk(userContext,bene);
				bene.setStato(aggiorno.getStato());
				bene.setToBeUpdated();

				if (aggiorno.getStato().equals(Inventario_beniBulk.STATO_SMARRITO)) {
					String path = SpringUtil.getBean("storeService", StoreService.class)
							.createFolderIfNotPresent(
									Arrays.asList(
													SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
													bene.getCd_unita_organizzativa(),
													"Inventario Beni")
											.stream().collect(
													Collectors.joining(StorageDriver.SUFFIX)
											),
									bene.getEtichetta(),
									null, null, null);

					StorageFile storageFile = new StorageFile(aggiorno.getFile(), bene.getEtichetta() + "-ProvvedimentoDenuncia.pdf");

					contrattiService.restoreSimpleDocument(
							storageFile,
							storageFile.getInputStream(),
							storageFile.getContentType(),
							storageFile.getFileName(),
							path, true);
				}

				super.modificaConBulk(userContext,bene);
			}
			return aggiorno;
		} catch (IOException e) {
			throw new ApplicationException("CMIS - Errore nella registrazione degli allegati (" + e.getMessage() + ")");
		} catch (PersistencyException e) {
			throw new ComponentException (e);
		} catch (OutdatedResourceException e) {
			throw new ApplicationException ("Risorsa non pi\371 valida. Ripetere la selezione dei beni.");
		} catch (BusyResourceException e) {
			throw new ComponentException (e);
		}
	}

public void aggiorna_utilizzatori(UserContext context,Aggiornamento_inventarioBulk aggiorno,Inventario_beniBulk bene)
throws  it.cnr.jada.comp.ComponentException {
	
	Inventario_utilizzatori_laHome home = (Inventario_utilizzatori_laHome)getHome(context,Inventario_utilizzatori_laBulk.class);
	
	Inventario_beniHome bene_home = (Inventario_beniHome)getHome(context,Inventario_beniBulk.class);
	
	try {
		
		bene.setV_utilizzatoriColl(new SimpleBulkList(bene_home.findUtilizzatori(context,bene)));
	
			for(java.util.Iterator i = bene.getV_utilizzatoriColl().iterator(); i.hasNext();) {
				Inventario_utilizzatori_laBulk utilizzatore_la = (Inventario_utilizzatori_laBulk)i.next();
				
				utilizzatore_la.setToBeDeleted();
				super.eliminaConBulk(context,utilizzatore_la);
			}
		
			for(java.util.Iterator i = aggiorno.getV_utilizzatoriColl().iterator(); i.hasNext();) {
						                                   
			   Utilizzatore_CdrVBulk utilizzatore = (Utilizzatore_CdrVBulk)i.next();
			   
			   for(java.util.Iterator a = utilizzatore.getBuono_cs_utilizzatoriColl().iterator(); a.hasNext();) {
			   	    
				    Inventario_utilizzatori_laBulk utilizzatore_la = (Inventario_utilizzatori_laBulk)a.next();
		 	        Inventario_utilizzatori_laBulk new_utilizzatore_la = new Inventario_utilizzatori_laBulk(utilizzatore_la.getCd_linea_attivita(),utilizzatore_la.getCd_utilizzatore_cdr(),bene.getNr_inventario(),bene.getPg_inventario(),bene.getProgressivo());			  			
					new_utilizzatore_la.setPercentuale_utilizzo_cdr(utilizzatore.getPercentuale_utilizzo_cdr());
					new_utilizzatore_la.setPercentuale_utilizzo_la(utilizzatore_la.getPercentuale_utilizzo_la());
					new_utilizzatore_la.setToBeCreated();
					
					super.creaConBulk(context,new_utilizzatore_la);
			   }	
		  }
	} catch (IntrospectionException e) {
			throw new ComponentException (e);
	} catch (PersistencyException e) {
			throw new ComponentException (e);
	}
 }
}
