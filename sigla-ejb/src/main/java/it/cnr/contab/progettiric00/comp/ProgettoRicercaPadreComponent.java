package it.cnr.contab.progettiric00.comp;

import java.sql.CallableStatement;
import java.sql.SQLException;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.progettiric00.core.bulk.*;
import it.cnr.contab.progettiric00.geco.bulk.*;
import it.cnr.contab.progettiric00.tabrif.bulk.*;
import it.cnr.contab.progettiric00.bp.*;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.bulk.*;
import it.cnr.contab.config00.blob.bulk.*;
import it.cnr.contab.utenze00.bp.*;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;
/**
 * @author Marco Spasiano
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ProgettoRicercaPadreComponent extends it.cnr.jada.comp.CRUDComponent implements IProgettoRicercaMgr {
	public static final String TIPO_PROGETTO = "P";
	/**
	 * Constructor for ProgettoRicercaPadreComponent.
	 */
	public ProgettoRicercaPadreComponent() {
		super();
	}
private ProgettoBulk intBulk(UserContext userContext, ProgettoBulk bulk) throws ComponentException {
				
	            if (bulk.getDipartimento() == null)
		           throw new it.cnr.jada.comp.ApplicationException("Attenzione: Per salvare il progetto è necessario inserire il Dipartimento!");
				
				if (bulk.getDt_inizio() == null)
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: Per salvare il progetto è necessario inserire la data di inizio!");
				
				//se data di fine esiste deve essere minore di data inizio
				if(bulk.getDt_fine() != null && bulk.getDt_inizio().after( bulk.getDt_fine() ))
						throw new it.cnr.jada.comp.ApplicationException("Data di fine deve essere maggiore della data di inizio!");

				//se data di fine non esiste non deve esistere data di proroga
				if(bulk.getDt_fine() == null && bulk.getDt_proroga() != null)
						throw new it.cnr.jada.comp.ApplicationException("Non può esistere una data di proroga se non si indica una data di fine!");

				//se data di proroga esiste deve essere minore di data fine
				if(bulk.getDt_proroga() != null && bulk.getDt_fine().after( bulk.getDt_proroga() ))
						throw new it.cnr.jada.comp.ApplicationException("Data di proroga deve essere maggiore della data di fine!");

				if (bulk.getUnita_organizzativa() == null)
						throw new it.cnr.jada.comp.ApplicationException("L'unità organizzativa è obbligatoria.");
				  if (bulk.getImporto_progetto() == null)
					 throw new it.cnr.jada.comp.ApplicationException("Entità delle risorse necessarie è obbligatorio.");
				  //se non vengono specificati dettagli Finanziatori
				  if(((ProgettoBulk)bulk).getDettagliFinanziatori().isEmpty() )
					 throw new it.cnr.jada.comp.ApplicationException("Attenzione: Per salvare il progetto è necessario inserire almeno un Finanziatore!");
				  //calcolo la discrepanza tra importo progetto e importo dettagli finanziatori
				  if(!((ProgettoBulk)bulk).getDettagliFinanziatori().isEmpty() ) {
					java.math.BigDecimal sum = new java.math.BigDecimal(0);
					java.math.BigDecimal residuo = new java.math.BigDecimal(0);
					for(int i = 0; bulk.getDettagliFinanziatori().size() > i; i++) {
						residuo = ((Progetto_finanziatoreBulk) bulk.getDettagliFinanziatori().get(i) ).getImporto_finanziato();
						if (residuo == null)
						throw new it.cnr.jada.comp.ApplicationException("L'importo dei finanziatori non può essere nullo!");
						sum = sum.add(residuo);
					}
					//se i dettagli hanno un importo maggiore del progetto
					if(sum.compareTo(bulk.getImporto_progetto())> 0) {
						throw new it.cnr.jada.comp.ApplicationException("La somma degli importi dei finanziatori è superiore all'importo del progetto");
					}else if(sum.compareTo(bulk.getImporto_progetto())< 0) {
						throw new it.cnr.jada.comp.ApplicationException("La somma degli importi dei finanziatori è inferiore all'importo del progetto");	
					}
				  }                
				if ((ProgettoBulk)bulk.getProgettopadre() == null)
				  ((ProgettoBulk)bulk).setLivello(new Integer(1));

				//se nei dettagli non è presente la UO cordinatrice viene creata                
				if( cercaUocordinatrice(bulk) ) {
				   Progetto_uoBulk dett = new Progetto_uoBulk(
					 bulk.getPg_progetto(),
					 bulk.getUnita_organizzativa()
				   );
				   dett.setCrudStatus( dett.TO_BE_CREATED );
				   dett.setUser( bulk.getUser() );
				   bulk.addToDettagli(dett);
				}
		  return bulk;
		}
/**
 * Pre:  Ricerca progetti disponibili
 * Post: Limitazione ai progetti della UO in scrivania tranne per l'ente.
 */
        
		public Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
		{
		   ProgettoBulk progetto = (ProgettoBulk)bulk;
		   ProgettoHome progettohome = (ProgettoHome)getHome(userContext, ProgettoBulk.class,"V_PROGETTO_PADRE");
		   SQLBuilder sql = progettohome.createSQLBuilder();
		   sql.addClause(clauses);
		   sql.addClause(bulk.buildFindClauses(new Boolean(true)));
		   sql.addSQLClause("AND", "V_PROGETTO_PADRE.ESERCIZIO", SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
		   sql.addSQLClause("AND", "PG_PROGETTO_PADRE", sql.ISNULL,null);
		   sql.addClause("AND","tipo_fase",SQLBuilder.NOT_EQUALS,ProgettoBulk.TIPO_FASE_NON_DEFINITA);
		   // Se uo 999.000 in scrivania: visualizza tutti i progetti
		   Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		   if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
			  sql.addSQLExistsClause("AND",progettohome.abilitazioniProgetti(userContext));
		   }
		   return sql;
		}     
/**
 * Pre:  Controllo Dt_inizio > Dt_fine
 * Post: Segnalazione "Data di fine deve essere maggiore della data di inizio!"
 *
 * Pre:  Controllo se Dt_fine = null e Dt_proroga != null
 * Post: Segnalazione "Non può esistere una data di proroga se non si indica una data di fine!"
 *
 * Pre:  Controllo Dt_fine > Dt_proroga
 * Post: Segnalazione "Data di proroga deve essere maggiore della data di fine!"
 *
 * Pre:  Controllo se la lista dei dettagli è vuota
 * Post: Se vuota viene creato un unico dettaglio che ha:
 *                      UO = l'UO coordinatrice del progetto
 *                      Responsabile = Responsabile del progetto
 *                      Importo = Importo del progetto
 *
 * Pre:  Controllo somma importo dettagli != da importo del progetto
 * Post: Segnalazione "La somma degli importi degli assegnatari è diversa dall'importo del progetto"
 *
 */
		public OggettoBulk modificaConBulk(UserContext uc, OggettoBulk bulk) throws ComponentException {

			intBulk(uc, (ProgettoBulk)bulk );
			
			/*Valorizzazione id PostIt*/	
			for(int i = 0; ((ProgettoBulk)bulk).getDettagliPostIt().size() > i; i++) {
			 /* Solo per i dettagli senza id */
			 if (((PostItBulk) ((ProgettoBulk)bulk).getDettagliPostIt().get(i)).getId()== null )
			 {					
			  Integer idPostit = new Integer (0);
			  PostItHome PostIt_home = (PostItHome) getHome(uc,PostItBulk.class);
			   try{		
				 idPostit = PostIt_home.getMaxId();
			   }catch (it.cnr.jada.persistency.IntrospectionException ie){
				throw handleException(ie);
			   }catch (PersistencyException pe){
				throw handleException(pe);
			   }
			  ((PostItBulk) ((ProgettoBulk)bulk).getDettagliPostIt().get(i)).setId(idPostit);
			 }
			 /* Fine if*/
			/*Fine valorizzazione id PostIt*/
			}
			return super.modificaConBulk(uc, bulk);
		}  
/**
 * Pre:  Controllo Dt_inizio > Dt_fine
 * Post: Segnalazione "Data di fine deve essere maggiore della data di inizio!"
 *
 * Pre:  Controllo se Dt_fine = null e Dt_proroga != null
 * Post: Segnalazione "Non può esistere una data di proroga se non si indica una data di fine!"
 *
 * Pre:  Controllo Dt_fine > Dt_proroga
 * Post: Segnalazione "Data di proroga deve essere maggiore della data di fine!"
 *
 * Pre:  Controllo se la lista dei dettagli è vuota
 * Post: Se vuota viene creato un unico dettaglio che ha:
 *                      UO = l'UO coordinatrice del Progetto
 *                      Responsabile = Responsabile del Progetto
 *                      Importo = Importo del Progetto
 *
 * Pre:  Controllo somma importo dettagli != da importo del Progetto
 * Post: Segnalazione "La somma degli importi degli assegnatari è diversa dall'importo del Progetto"
 *
 */        
		public OggettoBulk creaConBulk(UserContext uc, OggettoBulk bulk) throws ComponentException {
				intBulk(uc, (ProgettoBulk)bulk );

				//Parametri_cdsBulk param = parametriCds(uc, (ProgettoBulk)bulk);
				// inserimento automatico del codice
				if (((ProgettoBulk)bulk).getParametriCds().getFl_progetto_numeratore().booleanValue())
					((ProgettoBulk)bulk).setCd_progetto(creaCodiceProgetto(uc, (ProgettoBulk)bulk) );

				java.math.BigDecimal sq_progetto;
				sq_progetto = getSequence(uc);
				((ProgettoBulk)bulk).setPg_progetto(sq_progetto);
				((Progetto_uoBulk) ((ProgettoBulk)bulk).getDettagli().get(0)).setPg_progetto(new Integer(sq_progetto.intValue()));
				for(int i = 0; ((ProgettoBulk)bulk).getDettagliFinanziatori().size() > i; i++) {
				  ((Progetto_finanziatoreBulk) ((ProgettoBulk)bulk).getDettagliFinanziatori().get(i)).setPg_progetto(new Integer(sq_progetto.intValue()));
				}	
				for(int i = 0; ((ProgettoBulk)bulk).getDettagliPartner_esterni().size() > i; i++) {
				  ((Progetto_partner_esternoBulk) ((ProgettoBulk)bulk).getDettagliPartner_esterni().get(i)).setPg_progetto(new Integer(sq_progetto.intValue()));
				}	                
				for(int i = 0; ((ProgettoBulk)bulk).getDettagli().size() > i; i++) {
				 ((Progetto_uoBulk) ((ProgettoBulk)bulk).getDettagli().get(i)).setPg_progetto(new Integer(sq_progetto.intValue()));
				}	  
				for(int i = 0; ((ProgettoBulk)bulk).getDettagliPostIt().size() > i; i++) {
				/*Valorizzazione id PostIt*/
				if (((PostItBulk) ((ProgettoBulk)bulk).getDettagliPostIt().get(i)).getId()== null )
				{					
				 Integer idPostit = new Integer (0);
				 PostItHome PostIt_home = (PostItHome) getHome(uc,PostItBulk.class);
				  try{		
					 idPostit = PostIt_home.getMaxId();
				  }catch (it.cnr.jada.persistency.IntrospectionException ie){
				   throw handleException(ie);
				  }catch (PersistencyException pe){
				   throw handleException(pe);
				  }
				 ((PostItBulk) ((ProgettoBulk)bulk).getDettagliPostIt().get(i)).setId(idPostit);				 
				 
				}
				/*Fine valorizzazione id PostIt*/	  
				  ((PostItBulk) ((ProgettoBulk)bulk).getDettagliPostIt().get(i)).setPg_progetto(new Integer(sq_progetto.intValue()));
				  
				  
				}                              
				return super.creaConBulk(uc, bulk);
		}                 

	  protected boolean cercaUocordinatrice(ProgettoBulk progetto)
		{
		  for(int i = 0; progetto.getDettagli().size() > i; i++) {
			if (((Progetto_uoBulk)(progetto.getDettagli().get(i))).getCd_unita_organizzativa().equals(progetto.getCd_unita_organizzativa())){
			   return false;	
			}  
		  }        	
		  return true;
		}
	 protected java.math.BigDecimal getSequence(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException {

	  //ricavo il progressivo unico pg_progetto
	  java.math.BigDecimal pg_progetto= new java.math.BigDecimal(0);
	  try {
		  LoggableStatement ps= new LoggableStatement(getConnection(userContext),
				  "select CNRSEQ00_PG_PROGETTO.nextval from dual",true,this.getClass());
		try {
			java.sql.ResultSet rs= ps.executeQuery();
			try {
				if (rs.next())
					pg_progetto= rs.getBigDecimal(1);
			} finally {
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		} finally {
			try{ps.close();}catch( java.sql.SQLException e ){};
		}
	  } catch (java.sql.SQLException e) {
		  throw handleException(e);
		}
	  return pg_progetto;
	}        
    
		public void eliminaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
          
		  /*Se sto cancellando il progetto cancello anche tutti i dettagli */
		  if (bulk instanceof ProgettoBulk){
			for(int i = 0; ((ProgettoBulk)bulk).getDettagli().size() > i; i++) {
			  ((Progetto_uoBulk) ((ProgettoBulk)bulk).getDettagli().get(i)).setCrudStatus(bulk.TO_BE_DELETED);
			}
			for(int i = 0; ((ProgettoBulk)bulk).getDettagliFinanziatori().size() > i; i++) {
			  ((Progetto_finanziatoreBulk) ((ProgettoBulk)bulk).getDettagliFinanziatori().get(i)).setCrudStatus(bulk.TO_BE_DELETED);
			}
			for(int i = 0; ((ProgettoBulk)bulk).getDettagliPartner_esterni().size() > i; i++) {
			  ((Progetto_partner_esternoBulk) ((ProgettoBulk)bulk).getDettagliPartner_esterni().get(i)).setCrudStatus(bulk.TO_BE_DELETED);
			}
			for(int i = 0; ((ProgettoBulk)bulk).getDettagliPostIt().size() > i; i++) {
			  ((PostItBulk) ((ProgettoBulk)bulk).getDettagliPostIt().get(i)).setCrudStatus(bulk.TO_BE_DELETED);
			}            
		  }              
		  super.eliminaConBulk(aUC, bulk);
		}
		public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
			// inizializzazione per gestire la codifica automatica
			((ProgettoBulk)bulk).setParametriCds(parametriCds(aUC, (ProgettoBulk) bulk));
			return super.inizializzaBulkPerInserimento(aUC, bulk);
		}

/**
 * Pre:  Preparare l'oggetto alle modifiche;
 * Post: carica la lista di dettagli associati a un Progetto
 */
		public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
				try {
						ProgettoBulk testata = (ProgettoBulk)super.inizializzaBulkPerModifica(userContext,bulk);
						ProgettoHome testataHome = (ProgettoHome)getHome(userContext, ProgettoBulk.class);
						testata.setDettagli(new it.cnr.jada.bulk.BulkList(testataHome.findDettagli(testata)));
						testata.setDettagliFinanziatori(new it.cnr.jada.bulk.BulkList(testataHome.findDettagliFinanziatori(testata)));
						testata.setDettagliPartner_esterni(new it.cnr.jada.bulk.BulkList(testataHome.findDettagliPartner_esterni(testata)));                	
						testata.setDettagliPostIt(new it.cnr.jada.bulk.BulkList(testataHome.findDettagliPostIt(testata)));                	
                        
						getHomeCache(userContext).fetchAll(userContext);
						return testata;
				} catch(Exception e) {
						throw handleException(e);
				}
		}

		public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
				return super.inizializzaBulkPerRicercaLibera(aUC, bulk);
		}    
/**
 * Pre:  Ricerca progettopadre
 * Post: Limitazione ai progetti diversi da quello in oggetto.
 */
		public SQLBuilder selectProgettopadreByClause (UserContext userContext,
											  OggettoBulk bulk,
											  ProgettoBulk progettopadre,
											  CompoundFindClause clause)
		throws ComponentException, PersistencyException
		{
				if (clause == null) 
				  clause = progettopadre.buildFindClauses(null);
				SQLBuilder sql = getHome(userContext, progettopadre,"V_PROGETTO_PADRE").createSQLBuilder();
				sql.addSQLClause("AND", "PG_PROGETTO", sql.NOT_EQUALS, ((ProgettoBulk)bulk).getPg_progetto());
			    if (((ProgettoBulk)bulk).getLivello() != null)
				   sql.addSQLClause("AND", "LIVELLO", sql.EQUALS, new Integer(((ProgettoBulk)bulk).getLivello().intValue()-1));
				if (clause != null) 
				  sql.addClause(clause);
				return sql;
		}
/**
 * Pre:  Ricerca terzo ente
 * Post: Limitazione ai terzi ancora validi.
 */
		public SQLBuilder selectEnteByClause (UserContext userContext,
											  OggettoBulk bulk,
											  TerzoBulk ente,
											  CompoundFindClause clause)
		throws ComponentException, PersistencyException
		{
				if (clause == null) clause = ente.buildFindClauses(null);

				SQLBuilder sql = getHome(userContext, ente).createSQLBuilder();
				sql.addClause(
						it.cnr.jada.persistency.sql.CompoundFindClause.or(
								new it.cnr.jada.persistency.sql.SimpleFindClause("AND", "dt_fine_rapporto", sql.ISNULL, null),
								new it.cnr.jada.persistency.sql.SimpleFindClause("AND", "dt_fine_rapporto", sql.GREATER, getHome(userContext, ente).getServerTimestamp())
						)
				);

				if (clause != null) sql.addClause(clause);

				return sql;
		}

/**
 * Pre:  Ricerca terzo responsabile
 * Post: Limitazione ai terzi ancora validi.
 */
		public SQLBuilder selectResponsabileByClause(UserContext userContext,
													 OggettoBulk bulk,
													 TerzoBulk responsabile,
													 CompoundFindClause clause)
		throws ComponentException, PersistencyException
		{
				if (clause == null) clause = responsabile.buildFindClauses(null);

				SQLBuilder sql = getHome(userContext, responsabile,"V_TERZO_CF_PI").createSQLBuilder();
				sql.addClause(
						it.cnr.jada.persistency.sql.CompoundFindClause.or(
								new it.cnr.jada.persistency.sql.SimpleFindClause("AND", "dt_fine_rapporto", sql.ISNULL, null),
								new it.cnr.jada.persistency.sql.SimpleFindClause("AND", "dt_fine_rapporto", sql.GREATER, getHome(userContext, responsabile).getServerTimestamp())
						)
				);

				if (clause != null) sql.addClause(clause);

				return sql;
		}
/**
 * Pre:  Ricerca UO
 * Post: Limitazione alle UO valide.
 */
		public SQLBuilder selectUnita_organizzativaByClause (UserContext userContext,
															  OggettoBulk bulk,
															  Unita_organizzativaBulk uo,
															  CompoundFindClause clause)
		throws ComponentException, PersistencyException
		{
				if (clause == null) clause = uo.buildFindClauses(null);

				SQLBuilder sql = getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA").createSQLBuilder();
				sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );

				if (clause != null) sql.addClause(clause);

				return sql;
		}
	private void inizializzaBulkPerStampa(UserContext usercontext, Stampa_progettiVBulk stampa_progettivbulk)
		throws ComponentException
	{
		stampa_progettivbulk.setCd_cds(CNRUserContext.getCd_cds(usercontext));
		stampa_progettivbulk.setEsercizio(CNRUserContext.getEsercizio(usercontext));
	}

	public OggettoBulk inizializzaBulkPerStampa(UserContext usercontext, OggettoBulk oggettobulk)
		throws ComponentException
	{
		if(oggettobulk instanceof Stampa_progettiVBulk)
			inizializzaBulkPerStampa(usercontext, (Stampa_progettiVBulk)oggettobulk);
		return oggettobulk;
	}

/**
 * stampaConBulk method comment.
 */
public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	return bulk;
}        
        
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di un Iteratore su tutti i nodi figli 
  *		di un Progetto.
  *    PostCondition:
  *		 Viene restituito il RemoteIterator con l'elenco degli eventuali nodi figli del progetto di riferimento.
  *      
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il progetto di riferimento.
  *
  * @return remoteIterator <code>RemoteIterator</code> l'Iterator creato.
**/ 
public RemoteIterator getChildren(UserContext userContext, OggettoBulk bulk) throws ComponentException{

	ProgettoBulk ubi = (ProgettoBulk)bulk;
	ProgettoHome ubiHome = (ProgettoHome)getHome(userContext,ProgettoBulk.class,"V_PROGETTO_PADRE");
	return iterator(
		userContext,
		ubiHome.selectChildrenFor(userContext,ubi),
		ProgettoBulk.class,
		null);
}
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca del Progetto padre del Progetto specificato negli argomenti.
  *    PostCondition:
  *		 Viene restituito l'oggetto ProgettoBulk che è il Progetto padre cercato.
  *      
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Progetto di riferimento.
  *
  * @return bulk <code>OggettoBulk</code> il Progetto cercato.
**/ 
public OggettoBulk getParent(UserContext userContext, OggettoBulk bulk) throws ComponentException{

	try{
		ProgettoBulk ubi = (ProgettoBulk)bulk;
		ProgettoHome ubiHome = (ProgettoHome)getHome(userContext,ProgettoBulk.class,"V_PROGETTO_PADRE");
		return ubiHome.getParent(ubi);
		
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(bulk,ex);
	}catch(it.cnr.jada.persistency.IntrospectionException ex){
		throw handleException(bulk, ex);
	}
}        
/** 
  *  Controlla che il progetto sia una foglia.
  *    PreCondition:
  *      E' stata generata la richiesta di controllare se il Progetto specificato è una foglia,
  *		ossia se il suo livello è l'ultimo, (3). Questo implicherebbe che il Progetto in 
  *		questione non ha dei Progetti figli.
  *    PostCondition:
  *		 Viene restituito un valore booleano:
  *			- true: il Progetto è una foglia;
  *			- false: il Progetto non è una foglia.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Progetto di riferimento.
  *
  * @return il risultato <code>boolean</code> del controllo.
**/ 
public boolean isLeaf(UserContext userContext, OggettoBulk bulk) throws ComponentException{
	try {
		ProgettoBulk ubi = (ProgettoBulk)bulk;
		ProgettoHome ubiHome = (ProgettoHome)getHome(userContext,ubi,"V_PROGETTO_PADRE");
		return (!ubiHome.selectChildrenFor(userContext,ubi).executeExistsQuery(getConnection(userContext)));
	} catch(Throwable e) {
		throw handleException(e);
	}
}        
	public String creaCodiceProgetto(UserContext aUC, ProgettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		try
		{
			LoggableStatement cs =new LoggableStatement( getConnection( aUC ), 
				"{ ? = call " +
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +			
				"creaCodiceProgetto(?, ?, ?)}",false,this.getClass());
			try
			{
				cs.registerOutParameter( 1, java.sql.Types.CHAR );		
				cs.setString( 2, bulk.getCd_dipartimento());
				cs.setString( 3, TIPO_PROGETTO );		
				cs.setObject( 4, bulk.getParametriCds().getProgetto_numeratore_cifre());
				cs.executeQuery();
					
				String result = cs.getString( 1 );
				return result;
			}
			catch ( SQLException e )
			{
				throw handleException( e );
			}	
			finally
			{
				cs.close();
			}
		}
		catch ( SQLException e )
		{
			throw handleException( e );
		}	
	
	}
	public Parametri_cdsBulk parametriCds(UserContext aUC, ProgettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		Parametri_cdsBulk param;
		try {
			 param = (Parametri_cdsBulk) getHome( aUC, Parametri_cdsBulk.class ).findByPrimaryKey( 
				new Parametri_cdsBulk(
					((CNRUserContext) aUC).getCd_cds(), 
					((CNRUserContext) aUC).getEsercizio()));
		} catch (PersistencyException ex) {
			throw handleException(ex);
		} catch (ComponentException ex) {
			throw handleException(ex);
		}
		if (param == null) {
			//throw new ApplicationException("Parametri CDS non trovati per il CDS "+((CNRUserContext) aUC).getCd_cds());
			// se si vuole gestire un default
			param = new Parametri_cdsBulk();
			param.setFl_progetto_numeratore(Boolean.FALSE);
		}
		return param;
	}
	public void aggiornaGECO(UserContext userContext) throws ComponentException{
    	ProgettoHome progettoHome = ((ProgettoHome)getHome(userContext,ProgettoBulk.class));
    	progettoHome.aggiornaGeco(userContext,null);
    	DipartimentoHome dipartimentoHome = ((DipartimentoHome)getHome(userContext,DipartimentoBulk.class));
    	dipartimentoHome.aggiornaDipartimenti(userContext, null);
    	if (userContext.getUser().equalsIgnoreCase("GECO"))
    	   cancellaProgettoSIP(userContext);
	}
	private void handleExceptionMail(UserContext userContext, Exception e){
	}
	private void cancellaProgettoSIP(UserContext userContext) {
    	try {
    		Progetto_sipHome moduli_utilizzatiHome = ((Progetto_sipHome)getHome(userContext,Progetto_sipBulk.class,"V_SIP_MODULI_UTILIZZATI"));
			Progetto_sipHome progettoHome = ((Progetto_sipHome)getHome(userContext,Progetto_sipBulk.class));
			SQLBroker broker = progettoHome.createBroker(progettoHome.createSQLBuilderAll());
			while (broker.next()){
				Progetto_sipBulk progetto_sip = (Progetto_sipBulk)progettoHome.fetch(broker);
				if (progetto_sip.isProgetto()){
					if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext,new Geco_progettoBulk(new Long(progetto_sip.getPg_progetto().intValue()),new Long(progetto_sip.getEsercizio().intValue()),progetto_sip.getTipo_fase()))==null){
						if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext,new Geco_progetto_sacBulk(new Long(progetto_sip.getPg_progetto().intValue()),new Long(progetto_sip.getEsercizio().intValue()),progetto_sip.getTipo_fase()))==null){
							if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext,new Geco_progetto_rstlBulk(new Long(progetto_sip.getPg_progetto().intValue()),new Long(progetto_sip.getEsercizio().intValue()),progetto_sip.getTipo_fase()))==null){
								progetto_sip.setToBeDeleted();
								super.eliminaConBulk(userContext, progetto_sip);
							}
						}
					}
				}
				if (progetto_sip.isCommessa()){
					if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext,new Geco_commessaBulk(new Long(progetto_sip.getPg_progetto().intValue()),new Long(progetto_sip.getEsercizio().intValue()),progetto_sip.getTipo_fase()))==null){
						if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext,new Geco_commessa_sacBulk(new Long(progetto_sip.getPg_progetto().intValue()),new Long(progetto_sip.getEsercizio().intValue()),progetto_sip.getTipo_fase()))==null){
							if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext,new Geco_commessa_rstlBulk(new Long(progetto_sip.getPg_progetto().intValue()),new Long(progetto_sip.getEsercizio().intValue()),progetto_sip.getTipo_fase()))==null){
								progetto_sip.setToBeDeleted();
								super.eliminaConBulk(userContext, progetto_sip);
							}
						}
					}
				}
				if (progetto_sip.isModulo()){
					if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext,new Geco_moduloBulk(new Long(progetto_sip.getPg_progetto().intValue()),new Long(progetto_sip.getEsercizio().intValue()),progetto_sip.getTipo_fase()))==null){
						if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext,new Geco_modulo_sacBulk(new Long(progetto_sip.getPg_progetto().intValue()),new Long(progetto_sip.getEsercizio().intValue()),progetto_sip.getTipo_fase()))==null){
							if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext,new Geco_modulo_rstlBulk(new Long(progetto_sip.getPg_progetto().intValue()),new Long(progetto_sip.getEsercizio().intValue()),progetto_sip.getTipo_fase()))==null){
								SQLBuilder sql = moduli_utilizzatiHome.createSQLBuilderAll();
								sql.addSQLClause("AND", "ESERCIZIO",SQLBuilder.EQUALS,progetto_sip.getEsercizio());
								sql.addSQLClause("AND", "PG_PROGETTO",SQLBuilder.EQUALS,progetto_sip.getPg_progetto());
								sql.addSQLClause("AND", "TIPO_FASE",SQLBuilder.EQUALS,progetto_sip.getTipo_fase());								
								SQLBroker brokerUtilizzati = moduli_utilizzatiHome.createBroker(sql);
								if (brokerUtilizzati.next()){
									handleExceptionMail(userContext, new ApplicationException("Si è tentato di cacellare il modulo utilizzato: "+progetto_sip.getEsercizio()+"/"+progetto_sip.getPg_progetto()+"/"+progetto_sip.getTipo_fase()));
								}else{	
									progetto_sip.setToBeDeleted();
									super.eliminaConBulk(userContext, progetto_sip);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			handleExceptionMail(userContext, e);
		}
	}
}
