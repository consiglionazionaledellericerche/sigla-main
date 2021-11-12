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

package it.cnr.contab.utente00.comp;

import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.contab.utenze00.service.UtenteHDService;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDDuplicateKeyException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


/**
 * Classe che ridefinisce alcune operazioni di CRUD su UtenteBulk
 */


public class UtenteComponent extends it.cnr.jada.comp.CRUDComponent implements ICRUDMgr, it.cnr.contab.utenze00.comp.IUtenteMgr, Cloneable,Serializable
{



	public  UtenteComponent()
	{
	}
	/**
	 * Esegue la ricerca degli attributi di un UtenteBulk. In particolare per la ricerca dell'attributo CDS 
	 * di un Utente Amministratore di Utenze viene gestita la creazione di un CDS fittizio in corrispondenza del valore "*".
	 *
	 * Pre-post-conditions:
	 *
	 * Nome: Richiesta di ricerca attributo diverso da CDS
	 * Pre:  E' stata generata la richiesta di ricerca di un attributo di un UtenteBulk
	 * Post: Viene restituito il RemoteIterator con la collezione di OggettoBulk che soddisfano i criteri di ricerca
	 *
	 * Nome: Richiesta di ricerca attributo CDS
	 * Pre:  E' stata generata la richiesta di ricerca dell'attributo CDS per un UtenteAmministratoreBulk
	 * Post: Viene restituito il RemoteIterator con la collezione dei CdsBulk che soddisfano i criteri di ricerca oppure
	 *		 una istanza fittizia di CDS per gestire il codice '*' ad indicare 'Tutti i CDS' 	
	 *
	 * @param userContext lo userContext che ha generato la rcihiesta
	 * @param clausole eventuali clausole gia' specificate dall'utente nella ricerca
	 * @param bulk istanza di OggettoBulk da ricercare
	 * @param contesto istanza di OggettoBulk contesto della ricerca
	 * @param attributo attributo del contesto su cui si vuole effettuare la ricerca
	 * @return il RemoteIterator sulla colleazione di OggettoBulk risultato della ricerca
	 */

	public it.cnr.jada.util.RemoteIterator cerca(UserContext userContext,it.cnr.jada.persistency.sql.CompoundFindClause clausole,OggettoBulk bulk,OggettoBulk contesto,String attributo) throws it.cnr.jada.comp.ComponentException {
		if ( attributo.equals("cds") )
		{
			if ( ((CdsBulk) bulk).getCd_unita_organizzativa() != null && ((CdsBulk) bulk).getCd_unita_organizzativa().equals("*"))
			{	
				CdsBulk cds = getCdsStar();
				return new it.cnr.jada.util.ArrayRemoteIterator(new CdsBulk[] { cds });
			}
		}	
		return super.cerca(userContext,clausole,bulk,contesto,attributo);
	}
	/**
	 * Esegue la ricerca di tutti gli accessi già definiti in precedenza e di tutti gli accessi ancora disponibili 
	 * per un utente  e per una unità organizzativa.
	 *
	 * Pre-post-conditions:
	 *
	 * Nome: Richiesta di ricerca accessi
	 * Pre:  E' stata generata la richiesta di ricerca degli Utente_unita_accessoBulk definiti per un 
 		 UtenteComuneBulk o per un UtenteTemplateBulk
	 * Post: Viene restituita l'istanza di UtenteBulk con impostati gli accessi gia' assegnati (Utente_unita_accessoBulk)
	 *		 e gli accessi (AccessoBulk) ancora dipsonibili per l'utente e l'unita' organizzativa specificata
	 *
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param user istanza di UtenteComuneBulk o di UtenteTemplateBulk per cui ricercare gli accessi
	 * @param uo istanza di Unita_organizzativaBulk per cui ricercare gli accessi
	 * @return l'utente ricercato con gli accessi impostati
	 */

	public UtenteBulk cercaAccessi (UserContext userContext,UtenteBulk user, Unita_organizzativaBulk uo , CompoundFindClause compoundfindclause) throws it.cnr.jada.comp.ComponentException
	{
		try
		{
			if ( user.isUtenteComune() )
			{
				UtenteTemplateBulk utente = (UtenteTemplateBulk) user;
				UtenteHome utenteHome = (UtenteHome) getHome(userContext, utente.getClass() );
				//Carica tutti gli accessi
				List<AccessoBulk> accessiDisponibili =utenteHome.findAccessi_disponibili(utente, compoundfindclause);
				// carica utente_unita_accesso
				Collection result = utenteHome.findUtente_unita_accessi(utente);
				for (java.util.Iterator i = result.iterator();i.hasNext();) 
				{
					Utente_unita_accessoBulk uua = (Utente_unita_accessoBulk)i.next();
					if ( accessiDisponibili.contains(uua.getAccesso())){
						uua.setAccesso( accessiDisponibili.get(accessiDisponibili.indexOf(uua.getAccesso())));
						accessiDisponibili.remove(uua.getAccesso());
					}
					utente.addToUtente_unita_accessi( uua );
				}
				// carica accessi disponibili
				utente.setAccessi_disponibili( accessiDisponibili );
				//utente.setAccessi_disponibili( utenteHome.findAccessi_disponibili(utente, compoundfindclause));
			}
		}
		catch (Exception e )
		{
			throw handleException( e );
		}	
		return user;

	}
	/**
	 * Esegue la ricerca di tutti i ruoli già definiti in precedenza e di tutti i ruoli ancora disponibili 
	 * per un utente  e per una unità organizzativa.
	 *
	 * Pre-post-conditions:
	 *
	 * Nome: Richiesta di ricerca ruoli da parte di gestore con cds = '*'
	 * Pre:  E' stata generata la richiesta di ricerca degli Utente_unita_ruoloBulk definiti per un 
 		 Utente Comune o per un Utente Template da parte di un gestore abilitato a tutti i cds
	 * Post: Viene restituita l'istanza di UtenteBulk con impostati i ruoli gia' assegnati (Utente_unita_ruoloBulk)
	 *       all'utente per l'unita' organizzativa specificata e 
	 *		 tutti i ruoli (ruoloBulk) ancora disponibili per l'utente
	 *
	 * Nome: Richiesta di ricerca ruoli da parte di gestore con cds diverso da '*'
	 * Pre:  E' stata generata la richiesta di ricerca degli Utente_unita_ruoloBulk definiti per un 
 		 Utente Comune o per un Utente Template da parte di un gestore abilitato ad un solo cds
	 * Post: Viene restituita l'istanza di UtenteBulk con impostati i ruoli gia' assegnati (Utente_unita_ruoloBulk)
	 *       all'utente per l'unita' organizzativa specificata e i ruoli ancora assegnabili considerando sia i
	 *		 ruoli definiti per il cds a cui il gestore e' abilitato che i ruoli
	 *       non assegnati ad alcun cds
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param user istanza di UtenteComuneBulk o di UtenteTemplateBulk per cui ricercare i ruoli
	 * @param uo istanza di Unita_organizzativaBulk per cui ricercare i ruoli
	 * @return l'utente ricercato con i ruoli impostati
	 */

	public UtenteBulk cercaRuoli (UserContext userContext,UtenteBulk user, Unita_organizzativaBulk uo ) throws it.cnr.jada.comp.ComponentException
	{
		try
		{
			if ( user.isUtenteComune() )
			{
				UtenteTemplateBulk utente = (UtenteTemplateBulk) user;
				UtenteHome utenteHome = (UtenteHome) getHome(userContext, utente.getClass() );
				List<RuoloBulk> ruoliDisponibili = utenteHome.findRuoli_disponibili(utente);
				// carica utente_unita_ruolo

				Collection result = utenteHome.findUtente_unita_ruoli(utente);
				for (java.util.Iterator i = result.iterator();i.hasNext();) 
				{
					Utente_unita_ruoloBulk uur = (Utente_unita_ruoloBulk)i.next();
					if ( ruoliDisponibili.contains(uur.getRuolo())){
						uur.setRuolo( ruoliDisponibili.get(ruoliDisponibili.indexOf(uur.getRuolo())));
						ruoliDisponibili.remove(uur.getRuolo());
					}
					utente.addToUtente_unita_ruoli( uur );
				}
				// carica ruoli disponibili
				utente.setRuoli_disponibili( ruoliDisponibili );
				//utente.setRuoli_disponibili( utenteHome.findRuoli_disponibili(utente));
			}
		}
		catch (Exception e )
		{
			throw handleException( e );
		}	
		return user;

	}
	/**
	 * Esegue la ricerca dei codici di tutte le UO su cui l'utente ha almeno un accesso proprio.
	 *
	 * Pre-post-conditions:
	 *
	 * Nome: Richiesta di ricerca codici UO
	 * Pre:  E' stata generata la richiesta di ricerca dei codici di UO su cui l'utente ha accessi propri
	 * Post: Viene un collezione di stringhe contenente i codici delle UO
	 *
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param user istanza di UtenteComuneBulk o di UtenteTemplateBulk per cui ricercare i codici di UO
	 * @return collezione di codici di UO
	 */

	public Collection cercaUOAccessiPropri (UserContext userContext,UtenteBulk user) throws it.cnr.jada.comp.ComponentException
	{
		try
		{
			UtenteTemplateHome utenteHome = (UtenteTemplateHome)getHome(userContext, UtenteTemplateBulk.class);
			return utenteHome.findUO_accessi_propri((UtenteTemplateBulk)user);
		}
		catch (Exception e )
		{
			throw handleException( e );
		}	
	}
	/**
	 * Esegue la ricerca i codici di tutte le UO su cui l'utente ha almeno un ruolo proprio.
	 *
	 * Pre-post-conditions:
	 *
	 * Nome: Richiesta di ricerca codici UO
	 * Pre:  E' stata generata la richiesta di ricerca dei codici di UO su cui l'utente ha ruoli propri
	 * Post: Viene un collezione di stringhe contenente i codici delle UO
	 *
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param user istanza di UtenteComuneBulk o di UtenteTemplateBulk per cui ricercare i codici di UO
	 * @return collezione di codici di UO
	 */

	public Collection cercaUORuoliPropri (UserContext userContext,UtenteBulk user) throws it.cnr.jada.comp.ComponentException
	{
		try
		{
			UtenteTemplateHome utenteHome = (UtenteTemplateHome)getHome(userContext, UtenteTemplateBulk.class);
			return utenteHome.findUO_ruoli_propri((UtenteTemplateBulk)user);
		}
		catch (Exception e )
		{
			throw handleException( e );
		}	
	}
	/**
	 * Esegue una operazione di creazione di un UtenteBulk. Nel caso di utenti Amministratori, crea anche gli accessi previsti per
	 * tale tipologia di utenti.
	 *
	 * Pre-post-conditions:
	 *
	 * Nome: Creazione di Utente Comune o Template
	 * Pre:  La richiesta di creazione di una utenza e' stata generata
	 * Post: Un utente e' stato creato
	 *
	 * Nome: Creazione di Utente Amministratore
	 * Pre:  La richiesta di creazione di una utenza Amministratore di Utenze e' stata generata
	 * Post: Un utente Amministratore e' stato creato e le istanze Utente_unita_accessoBulk per tutti gli AccessiBulk
	 *		 con tipologia = UTENTE_AMMINISTRATORE sono state generate
	 * 
	 * Nome: Errore di chiave duplicata
	 * Pre:  Esiste già un UtenteBulk persistente che possiede la stessa chiave
	 * 		 primaria di quello specificato.
	 * Post: Viene segnalato all'utente l'impossibilità di inserimento
	 *
	 * @param	userContext	lo UserContext che ha generato la richiesta
	 * @param	bulk	l'UtenteBulk che deve essere creato
	 * @return	l'UtenteBulk risultante dopo l'operazione di creazione.
	 */	
	public OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		try 
		{	
			UtenteBulk ute = (UtenteBulk) bulk;
			if (!Optional.ofNullable(ute)
					.filter(UtenteTemplateBulk.class::isInstance).isPresent() &&
					Optional.ofNullable(ute)
						.filter(utente -> utente.getFl_autenticazione_ldap() && !Optional.ofNullable(utente.getCd_utente_uid()).isPresent()).isPresent())
				throw new ApplicationException("Il campo Codice Utente Ufficiale CNR è obbligatorio.");

			if (ute!=null && ute.getFl_autenticazione_ldap() && ute.getCd_utente_uid()!=null) {
				// controlliamo se l'utente esiste su ldap e abilitiamolo a sigla
				((GestioneLoginComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession",GestioneLoginComponentSession.class)).cambiaAbilitazioneUtente(userContext, ute.getCd_utente_uid(), true);
			}
			if ( bulk instanceof UtenteAmministratoreBulk )
			{
				UtenteAmministratoreBulk utente = (UtenteAmministratoreBulk) bulk ;
				UtenteAmministratoreHome utenteHome = (UtenteAmministratoreHome) getHome( userContext, utente.getClass());

				insertBulk( userContext, bulk );

				List l = utenteHome.findAccessi();

				AccessoBulk accesso;
				Utente_unita_accessoBulk uua;
				for ( Iterator i = l.iterator(); i.hasNext();)
				{
					accesso = (AccessoBulk) i.next();
					uua = new Utente_unita_accessoBulk();
					uua.setAccesso( accesso );
					uua.setCd_utente( utente.getCd_utente() );
					uua.setCd_unita_organizzativa( "*");
					uua.setUser( userContext.getUser());
					insertBulk( userContext, uua );
				}	

			}
			else{
				super.creaConBulk(userContext,bulk);
				
				if (ute!=null && ute.getFl_autenticazione_ldap() && ute.getCd_utente_uid()!=null) {
					UtenteHDService.loadProperties();
					if (!UtenteHDService.getTargetEndpoint().contains("$")){
						ExternalUser eu=UtenteHDService.getUser( ute.getCd_utente_uid(),"HDEsterno");
						if(eu==null){
							ExternalUser xu = new ExternalUser();
							xu.setFirstName(ute.getNome());
							xu.setFamilyName(ute.getCognome());
							xu.setEmail("");
						    xu.setLogin(ute.getCd_utente_uid());
						    xu.setProfile(1);//utente semplice
						    xu.setTelefono("");
						    if(ute.getCd_cdr()!=null)
						    	xu.setStruttura((ute.getCd_cdr().substring(0, 7)));
						    //xu.setStruttura("2");
						    xu.setEnabled("y");
						    xu.setMailStop("y");// mettendo il flag a y è possibile non specificare email
						    try{
						    	UtenteHDService.newUser(xu, "HDEsterno");
						    }
						    catch (Exception e){
						    	throw new ApplicationException("Inserimento non effettuato! Creazione automatica Utenza Helpdesk non riuscita!");
						    }
						    	
						}
					}
				}
			}
			return bulk;
		} catch (CRUDDuplicateKeyException e) 
		{
			throw handleException(new ApplicationException("Inserimento impossibile: Chiave duplicata, utilizzare un codice differente da "+((UtenteBulk)bulk).getCd_utente()));
		}

		catch (Throwable e) 
		{
			throw handleException(bulk,e);
		}
	}
	public OggettoBulk modificaConBulk(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		try 
		{	
			UtenteBulk ute = (UtenteBulk) bulk;
			if (ute!=null && ute.getFl_autenticazione_ldap() && ute.getCd_utente_uid()!=null) {
				// controlliamo se l'utente esiste su ldap
				((GestioneLoginComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession",GestioneLoginComponentSession.class)).isUtenteAbilitatoLdap(userContext, ute.getCd_utente_uid(), false);
			}
		}
		catch (Throwable e) 
		{
			throw handleException(bulk,e);
		}
		return super.modificaConBulk(userContext, bulk);
	}	
	/**
	 * Esegue una operazione di eliminazione logica di UtenteBulk modificando la data di fine validità 
	 * dell'utenza alla data odierna.
	 *
	 * Pre-post-conditions:
	 *
	 * Nome: Cancellazione logica di un Utente
	 * Pre:  La richiesta di cancellazione di una utenza e' stata generata
	 * Post: La data di fine validità dell'utente e' stata aggiornata alla data odierna
	 * 
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param bulk l'istanza di UtenteBulk che deve essere cancellata logicamente
	 */

	public void eliminaConBulk (UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException
	{
		try
		{
			UtenteBulk utente = (UtenteBulk) bulk;
			utente.setDt_fine_validita( ((UtenteHome)getHome(userContext,UtenteBulk.class)).getServerTimestamp());
			updateBulk(userContext, utente );
		}
		catch ( Exception e )
		{
			throw handleException(bulk, e)	;
		}	
	}
	/*
	 * Ritorna un CDR fittizio non persistente che rappresenta tutti i CDR (codice *)
	 */
	private CdsBulk getCdsStar() 
	{
		CdsBulk cds = new CdsBulk();
		cds.setCd_unita_organizzativa("*");
		cds.setCrudStatus(cds.NORMAL);
		cds.setDs_unita_organizzativa("Tutti i cds");
		return cds;
	}
	/**
	 * Esegue l'inizializzazione di una nuova istanza di UtenteBulk impostandone la password e il gestore
	 *
	 * Pre-post-conditions:
	 *
	 * Nome: Inizializzazione bulk 
	 * Pre:  L'inizializzazione di un Utentebulk per eventuale inserimentoo e' stata generata
	 * Post: L'UtenteBulk viene aggiornato con una password di default e con codice gestore uguale al codice dell'utente
	 *       che ha generato la richiesta
	 *
	 * Nome: Gestore non trovato
	 * Pre:  L'utente che ha generato la richiesta non esiste
	 * Post: Viene generata una ComponentException con detail l'ApplicationException con il messaggio 
	 *       da visualizzare all'utente
	 *
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param bulk l'istanza di UtenteBulk che deve essere inizializzata
	 * @return l' UtenteBulk inizializzato 
	 */

	public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException
	{
		UtenteBulk utenteBulk =  (UtenteBulk)bulk;
		try
		{
			String cd_gestore = utenteBulk.getUser();

			Utente_gestoreHome home = (Utente_gestoreHome) getHome(userContext, Utente_gestoreBulk.class);
			SQLBuilder sqlGest = home.createSQLBuilder();
			sqlGest.addClause("AND","cd_gestore",sqlGest.EQUALS,utenteBulk.getUser());
			List ris = home.fetchAll(sqlGest);
			Utente_gestoreBulk utenteGest=null; 
			if (ris.size()==1) {
				utenteGest = (Utente_gestoreBulk) ris.get(0);
				cd_gestore = utenteGest.getCd_utente();
			}

			UtenteBulk gestore = (UtenteBulk) getHomeCache(userContext).getHome( UtenteBulk.class ).findByPrimaryKey( new UtenteKey( cd_gestore ));
			if ( gestore == null )
				throw  new it.cnr.jada.comp.ApplicationException( "Utente Gestore non definito" );		
			utenteBulk.setGestore( gestore );
			utenteBulk.setCd_gestore( gestore.getCd_utente());

			//		if ( utenteBulk instanceof UtenteComuneBulk )
			//			((UtenteComuneBulk) utenteBulk).getTemplate().setCd_gestore( gestore.getCd_utente() );
			return utenteBulk;		
		}
		catch ( Exception e )
		{
			handleException( e );
			return null;
		}
	}
	/**
	 * Esegue l'inizializzazione di una istanza di UtenteBulk per una possibile operazione di modifica. 
	 * In particolare viene impostato l'utente corrente come gestore dell'utente da modificare.
	 * Inoltre per un utente di tipo Amministratore vengono caricati i dati relativi al CDS da amministrare;
	 * per un utente comune vengono caricati i dati relativi al CDR da cui dipende
	 * Pre-post-conditions:
	 *
	 * Nome: Inizializzazione UtenteTemplateBulk 
	 * Pre:  L'inizializzazione di un UtenteTemplateBulk per eventuale modifica e' stata generata
	 * Post: L'UtenteTemplateBulk viene aggiornato con codice gestore uguale al codice dell'utente
	 *       che ha generato la richiesta
	 *
	 * Nome: Inizializzazione UtenteAmministratoreBulk 
	 * Pre:  L'inizializzazione di un UtenteAmministratoreBulk per eventuale modifica e' stata generata
	 * Post: L'UtenteAmministratoreBulk viene aggiornato con codice gestore uguale al codice dell'utente
	 *       che ha generato la richiesta e con i dati relativi al CDS da lui amministrato
	 *
	 * Nome: Inizializzazione UtenteComuneBulk 
	 * Pre:  L'inizializzazione di un UtenteComuneBulk per eventuale modifica e' stata generata
	 * Post: L'UtenteComuneBulk viene aggiornato con codice gestore uguale al codice dell'utente
	 *       che ha generato la richiesta e con i dati relativi al CDR a cui appartiene
	 *
	 * Nome: Gestore non trovato
	 * Pre:  L'utente che ha generato la richiesta non esiste
	 * Post: Viene generata una ComponentException con detail l'ApplicationException con il messaggio 
	 *       da visualizzare all'utente
	 *
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param bulk l'istanza di UtenteBulk che deve essere inizializzata
	 * @return l' UtenteBulk inizializzato 
	 */


	public OggettoBulk inizializzaBulkPerModifica (UserContext userContext,OggettoBulk bulk)  throws it.cnr.jada.comp.ComponentException 
	{

		bulk = super.inizializzaBulkPerModifica(userContext, bulk );

		try
		{
			UtenteBulk utente = (UtenteBulk) bulk;
			UtenteBulk gestore = (UtenteBulk) getHome(userContext, UtenteBulk.class ).findByPrimaryKey( new UtenteKey( utente.getCd_gestore()));
			if ( gestore == null )
				throw  new it.cnr.jada.comp.ApplicationException( "Utente Gestore non definito" );		
			utente.setGestore( gestore );
			utente.setUtente_indirizzi_mail(new it.cnr.jada.bulk.BulkList(((UtenteHome)getHome(userContext, UtenteBulk.class )).findUtente_indirizzi_email(utente)));
			utente.setUtente_abil_ordine(new it.cnr.jada.bulk.BulkList(((UtenteHome)getHome(userContext, UtenteBulk.class )).findUtente_abil_ordine(utente)));
			getHomeCache(userContext).fetchAll(userContext);
			if ( utente instanceof UtenteAmministratoreBulk )
			{ // carico il cds
				UtenteAmministratoreBulk admin = (UtenteAmministratoreBulk) utente;

				if ( admin.getCd_cds_configuratore().equals("*"))
					admin.setCds( getCdsStar());
				else
					admin.setCds( (CdsBulk) getHome(userContext,CdsBulk.class).findByPrimaryKey( new CdsKey( admin.getCd_cds_configuratore())));				

			}
			if ( utente instanceof UtenteComuneBulk )
			{ // carico il cdr
				UtenteComuneBulk utComune = (UtenteComuneBulk) utente;
				utComune.setCdr( (CdrBulk) getHome(userContext,CdrBulk.class).findByPrimaryKey( new CdrBulk(utComune.getCd_cdr() )));
			}
		}
		catch( Exception e )
		{
			handleException( e );

		}
		return bulk;
	}
	/**
	 * Esegue l'inizializzazione di una istanza di UtenteBulk per una possibile operazione di ricerca. 
	 * In particolare viene impostato l'utente corrente come gestore dell'utente da ricercare.
	 *
	 * Pre-post-conditions:
	 *
	 * Nome: Inizializzazione UtenteBulk 
	 * Pre:  L'inizializzazione di un UtenteBulk per eventuale ricerca e' stata generata
	 * Post: L'UtenteBulk viene aggiornato con codice gestore uguale al codice dell'utente
	 *       che ha generato la richiesta
	 *
	 * Nome: Gestore non trovato
	 * Pre:  L'utente che ha generato la richiesta non esiste
	 * Post: Viene generata una ComponentException con detail l'ApplicationException con il messaggio 
	 *       da visualizzare all'utente
	 *
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param bulk l'istanza di UtenteBulk che deve essere inizializzata
	 * @return l' UtenteBulk inizializzato 
	 */

	public OggettoBulk inizializzaBulkPerRicerca(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException
	{
		UtenteBulk utenteBulk =  (UtenteBulk)bulk;

		try
		{
			UtenteBulk gestore = (UtenteBulk) getHomeCache(userContext).getHome( UtenteBulk.class ).findByPrimaryKey( new UtenteKey( utenteBulk.getUser() ));
			if ( gestore == null )
				throw  new it.cnr.jada.comp.ApplicationException( "Utente Gestore non definito" );		
			utenteBulk.setGestore( gestore );
			utenteBulk.setCd_gestore( gestore.getCd_utente());
			return utenteBulk;
		}
		catch ( Exception e )
		{
			handleException( e );
			return null;
		}
	}
	/**
	 * Esegue l'inizializzazione di una istanza di UtenteBulk per una possibile operazione di ricerca libera. 
	 * In particolare viene impostato l'utente corrente come gestore dell'utente da ricercare.
	 *
	 * Pre-post-conditions:
	 *
	 * Nome: Inizializzazione UtenteBulk 
	 * Pre:  L'inizializzazione di un UtenteBulk per eventuale ricerca libera e' stata generata
	 * Post: L'UtenteBulk viene aggiornato con codice gestore uguale al codice dell'utente
	 *       che ha generato la richiesta
	 *
	 * Nome: Gestore non trovato
	 * Pre:  L'utente che ha generato la richiesta non esiste
	 * Post: Viene generata una ComponentException con detail l'ApplicationException con il messaggio 
	 *       da visualizzare all'utente
	 *
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param bulk l'istanza di UtenteBulk che deve essere inizializzata
	 * @return l' UtenteBulk inizializzato 
	 */

	public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException
	{
		UtenteBulk utenteBulk =  (UtenteBulk)bulk;

		try
		{
			UtenteBulk gestore = (UtenteBulk) getHomeCache(userContext).getHome( UtenteBulk.class ).findByPrimaryKey( new UtenteKey( utenteBulk.getUser() ));
			if ( gestore == null )
				throw  new it.cnr.jada.comp.ApplicationException( "Utente Gestore non definito" );		
			utenteBulk.setGestore( gestore );
			utenteBulk.setCd_gestore( gestore.getCd_utente());
			return utenteBulk;
		}
		catch ( Exception e )
		{
			handleException( e );
			return null;
		}
	}
	/**
	 * Esegue una operazione di reset della password dell'utente
	 *
	 * Pre-post-conditions:
	 *
	 * Nome: Cancellazione pswd di un Utente
	 * Pre:  La richiesta di cancellazione della password di una utenza e' stata generata
	 * Post: La data di ultima variazione della password e' stata impostata al valore null
	 * 
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param bulk l'istanza di UtenteBulk che deve essere cancellata logicamente
	 */

	public UtenteBulk resetPassword(UserContext userContext,UtenteBulk utente) throws it.cnr.jada.comp.ComponentException {
		try 
		{
			utente.setDt_ultima_var_password(null);
			utente.setPassword(null);
			utente.setToBeUpdated();
			makeBulkPersistent( userContext, utente );
			return utente;
		} catch (Throwable e) 
		{
			throw handleException(utente,e);
		}
	}
	/**
	 * Aggiunge una clausola a tutte le operazioni di ricerca eseguite su UtenteBulk, specificando 
	 * che il gestore dell'utente da ricercare sia l'utente attualmente collegato.
	 *	
	 * Pre-post-conditions:
	 *
	 * Nome: Richiesta di ricerca UtenteBulk
	 * Pre:  E' stata generata la richiesta di ricerca di un UtenteBulk
	 * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, la
	 *       clausola che il gestore dell'utente da ricercare sia l'utente attualmente collegato
	 * 
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param clauses clausole di ricerca gia' specificate dall'utente
	 * @param utente istanza di UtenteBulk che deve essere utilizzata per la ricerca
	 * @return il SQLBuilder con la clausola aggiuntiva sul gestore
	 */


	protected Query select(UserContext userContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk utente) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		it.cnr.jada.persistency.sql.SQLBuilder sql;

		if ( utente instanceof UtenteAmministratoreBulk )
		{
			//sql = super.select(userContext,clauses, utente );
			UtenteAmministratoreHome home = (UtenteAmministratoreHome)getHomeCache(userContext).getHome(UtenteAmministratoreBulk.class);
			sql = home.createSQLBuilder();
			it.cnr.jada.persistency.sql.CompoundFindClause bulkClauses = utente.buildFindClauses(null);
			sql.addClause( bulkClauses );
			//sql.addClause( "AND", "cd_gestore", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, ((UtenteBulk)utente).getGestore().getCd_utente()  );		
			sql.addSQLClause( "AND", "(cd_gestore = '"+((UtenteBulk)utente).getGestore().getCd_utente()
					+"' or '"+((UtenteBulk)utente).getGestore().getCd_utente()+"' in (select cd_gestore from utente_gestore where utente_gestore.cd_utente=utente.cd_gestore))");		
			sql.addClause(clauses);

		}
		else if ( utente instanceof UtenteComuneBulk )
		{
			UtenteComuneHome home = (UtenteComuneHome)getHomeCache(userContext).getHome(UtenteComuneBulk.class);
			sql = home.createSQLBuilder();
			it.cnr.jada.persistency.sql.CompoundFindClause bulkClauses = utente.buildFindClauses(null);
			sql.addClause( bulkClauses );
			//sql.addClause( "AND", "cd_gestore", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, ((UtenteBulk)utente).getGestore().getCd_utente()  );		
			sql.addSQLClause( "AND", "(cd_gestore = '"+((UtenteBulk)utente).getGestore().getCd_utente()
					+"' or '"+((UtenteBulk)utente).getGestore().getCd_utente()+"' in (select cd_gestore from utente_gestore where utente_gestore.cd_utente=utente.cd_gestore))");		
			sql.addClause(clauses);
		}
		else
		{
			sql = (SQLBuilder)super.select(userContext,clauses, utente );
			//sql.addClause( "AND", "cd_gestore", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, ((UtenteBulk)utente).getGestore().getCd_utente()  );
			sql.addSQLClause( "AND", "(cd_gestore = '"+((UtenteBulk)utente).getGestore().getCd_utente()
					+"' or '"+((UtenteBulk)utente).getGestore().getCd_utente()+"' in (select cd_gestore from utente_gestore where utente_gestore.cd_utente=utente.cd_gestore))");		
		}	
		return sql;
	}
	/**
	 * Aggiunge una clausola a tutte le operazioni di ricerca eseguite su un CdrBulk da assegnare ad un Utente
	 *	
	 * Pre-post-conditions:
	 *
	 * Nome: Ricerca Cdr da parte di gestore con CDS = '*'
	 * Pre:  E' stata generata la richiesta di ricerca di un Cdr da parte di un gestore abilitato a tutti
	 *       i Cds ('*')
	 * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, la
	 *       clausola che il Cdr sia valido per l'esercizio di scrivania 
	 *
	 * Nome: Ricerca Cdr da parte di gestore con CDS diverso da '*'
	 * Pre:  E' stata generata la richiesta di ricerca di un Cdr da parte di un gestore abilitato ad un solo Cds 
	 * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, la
	 *       clausola che il Cdr sia valido per l'esercizio di scrivania e il Cdr appartenga ad una delle unità organizzative
	 *       che dipendono dal Cds a cui e' abilitato il gestore
	 *
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param uUB istanza di UtenteBulk 
	 * @param aCDR istanza di CdrBulk che deve essere utilizzata per la ricerca 
	 * @param clauses clausole di ricerca gia' specificate dall'utente
	 * @return il SQLBuilder con la clausola aggiuntiva sul gestore
	 */
	public it.cnr.jada.persistency.sql.SQLBuilder selectCdrByClause(UserContext userContext, UtenteBulk aUB, CdrBulk aCDR, it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException {
		CdsBulk cds;
		UtenteAmministratoreBulk utente = (UtenteAmministratoreBulk)getHome(userContext,UtenteAmministratoreBulk.class).findByPrimaryKey(new UtenteAmministratoreBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext)));
		if ( !utente.getCd_cds_configuratore().equals("*"))
			cds = (CdsBulk)getHome(userContext,CdsBulk.class).findByPrimaryKey(new CdsBulk(utente.getCd_cds_configuratore()));
		else
			cds = getCdsStar(); 

		SQLBuilder sql = ((CdrHome)getHome(userContext,aCDR, "V_CDR_VALIDO",getFetchPolicyName("findCdr"))).createSQLBuilderEsteso();
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.addClause(clauses);
		if ( !cds.getCd_unita_organizzativa().equals("*") )
			sql.addClause( "AND", "cd_centro_responsabilita", sql.LIKE, cds.getCd_unita_organizzativa() + "%" );
		return sql;
	}
	public SQLBuilder selectCdsByClause(UserContext userContext, SelezionaCdsBulk scds, CdsBulk cds, CompoundFindClause clauses) throws ComponentException {

		Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext);
		CdsHome home = (CdsHome)getHome(userContext, cds);
		SQLBuilder sql = home.createSQLBuilderIncludeEnte();

		sql.addClause("AND","esercizio_inizio",SQLBuilder.LESS_EQUALS,esercizio);
		sql.addClause("AND","esercizio_fine",SQLBuilder.GREATER_EQUALS,esercizio);
		sql.addClause(clauses);
		sql.addOrderBy("cd_unita_organizzativa");
		return sql;
	}
	public SQLBuilder selectUoByClause(UserContext userContext, SelezionaCdsBulk scds, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {

		Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext);
		SQLBuilder sql;
		if (uo instanceof Unita_organizzativa_enteBulk) {
			Unita_organizzativa_enteHome home;
			home = (Unita_organizzativa_enteHome)getHome(userContext, uo);
			sql = home.createSQLBuilder();
		}
		else {
			Unita_organizzativaHome home;
			home = (Unita_organizzativaHome)getHome(userContext, uo);
			sql = home.createSQLBuilderEsteso();
		}

		if (scds.getCds()!=null && scds.getCds().getCd_unita_organizzativa()!=null)
			sql.addClause("AND","cd_unita_padre",sql.EQUALS,scds.getCds().getCd_unita_organizzativa());
		sql.addClause("AND","esercizio_inizio",SQLBuilder.LESS_EQUALS,esercizio);
		sql.addClause("AND","esercizio_fine",SQLBuilder.GREATER,esercizio);
		sql.addClause(clauses);
		sql.addOrderBy("cd_unita_organizzativa");
		return sql;
	}
	public SQLBuilder selectCdrByClause(UserContext userContext, SelezionaCdsBulk scds, CdrBulk cdr, CompoundFindClause clauses) throws ComponentException {

		Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext);
		CdrHome home = (CdrHome)getHome(userContext, cdr);
		SQLBuilder sql = home.createSQLBuilderEsteso();
		sql.addClause("AND","esercizio_inizio",SQLBuilder.LESS_EQUALS,esercizio);
		sql.addClause("AND","esercizio_fine",SQLBuilder.GREATER,esercizio);
		sql.addClause(clauses);
		sql.addOrderBy("cd_centro_responsabilita");
		return sql;
	}
	/**
	 * Aggiunge una clausola a tutte le operazioni di ricerca eseguite su CdsBulk 
	 *	
	 * Pre-post-conditions:
	 *
	 * Nome: Default
	 * Pre:  E' stata generata la richiesta di ricerca di Cds
	 * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, la
	 *       clausola che il Cds sia valido per l'esercizio di scrivania 
	 * 
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param utente istanza di UtenteBulk 
	 * @param cds istanza di CDsBulk che deve essere utilizzata per la ricerca 
	 * @param clauses clausole di ricerca gia' specificate dall'utente
	 * @return il SQLBuilder con la clausola aggiuntiva sull'esercizio
	 */


	public it.cnr.jada.persistency.sql.SQLBuilder selectCdsByClause(UserContext userContext, UtenteBulk utente, CdsBulk cds, it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = getHome(userContext, cds.getClass(), "V_CDS_VALIDO").createSQLBuilder();
		sql.addClause( clauses );
		sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		return sql;

	}
	/**
	 * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sull'Unità organizzativa da utilizzare per la
	 * definizione degli accessi
	 *	
	 * Pre-post-conditions:
	 *
	 * Nome: Ricerca UO per accesso per gestore con CDS = '*'
	 * Pre:  E' stata generata la richiesta di ricerca di un'Unità organizzativa da parte di un gestore abilitato a tutti
	 *       i Cds ('*')
	 * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, la
	 *       clausola che l'UO sia valida per l'esercizio di scrivania 
	 *
	 * Nome: Ricerca UO per accesso per gestore con CDS diverso da '*'
	 * Pre:  E' stata generata la richiesta di ricerca di un'Unità organizzativa da parte di un gestore abilitato ad un solo
	 *       Cds 
	 * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, la
	 *       clausola che l'UO sia valida per l'esercizio di scrivania e che l'UO appartenga al Cds al quale il gestore e'
	 *       abilitato
	 *
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param utente istanza di UtenteBulk 
	 * @param uo istanza di Unita_organizzativaBulk che deve essere utilizzata per la ricerca 
	 * @param clauses clausole di ricerca gia' specificate dall'utente
	 * @return il SQLBuilder con le clausole aggiuntive
	 */

	public SQLBuilder selectUnita_org_per_accessoByClause(UserContext userContext, UtenteTemplateBulk utente, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = ((it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome)getHome(userContext, uo.getClass(), "V_UNITA_ORGANIZZATIVA_VALIDA")).createSQLBuilderEsteso();
		sql.addClause( clauses );
		sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		if ( !utente.getGestore().getCd_cds_configuratore().equals("*") )
			sql.addClause("AND", "cd_unita_padre", SQLBuilder.EQUALS, utente.getGestore().getCd_cds_configuratore() );
		return sql;
	}
	/**
	 * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sull'Unità organizzativa da utilizzare per la
	 * definizione dei ruoli
	 *	
	 * Pre-post-conditions:
	 *
	 * Nome: Ricerca UO per ruolo per gestore con CDS = '*'
	 * Pre:  E' stata generata la richiesta di ricerca di un'Unità organizzativa da parte di un gestore abilitato a tutti
	 *       i Cds ('*')
	 * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, la
	 *       clausola che l'UO sia valida per l'esercizio di scrivania 
	 *
	 * Nome: Ricerca UO per ruolo per gestore con CDS diverso da '*'
	 * Pre:  E' stata generata la richiesta di ricerca di un'Unità organizzativa da parte di un gestore abilitato ad un solo
	 *       Cds 
	 * Post: Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, la
	 *       clausola che l'UO sia valida per l'esercizio di scrivania e che l'UO appartenga al Cds al quale il gestore e'
	 *       abilitato
	 *
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param utente istanza di UtenteBulk 
	 * @param uo istanza di Unita_organizzativaBulk che deve essere utilizzata per la ricerca 
	 * @param clauses clausole di ricerca gia' specificate dall'utente
	 * @return il SQLBuilder con le clausole aggiuntive
	 */

	public SQLBuilder selectUnita_org_per_ruoloByClause(UserContext userContext, UtenteTemplateBulk utente, Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = ((it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome)getHome(userContext, uo.getClass(), "V_UNITA_ORGANIZZATIVA_VALIDA")).createSQLBuilderEsteso();
		sql.addClause( clauses );
		sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		if ( !utente.getGestore().getCd_cds_configuratore().equals("*") )
			sql.addClause("AND", "cd_unita_padre", SQLBuilder.EQUALS, utente.getGestore().getCd_cds_configuratore() );
		return sql;
	}
	public boolean isCdrConfiguratoreAll(UserContext userContext)throws ComponentException{
		try {
			UtenteBulk utenteScrivania = (UtenteBulk)getHome(userContext,UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext)));
			if (utenteScrivania.getCd_cds_configuratore().equalsIgnoreCase("*"))
				return true;
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		return false;
	}
	public SelezionaCdsBulk findCds(UserContext userContext,SelezionaCdsBulk scds) throws it.cnr.jada.comp.ComponentException {
		try 
		{
			Unita_organizzativaHome home = (Unita_organizzativaHome) getHome(userContext,Unita_organizzativaBulk.class);
			SQLBuilder sql = home.createSQLBuilder();

			CdrHome home2 = (CdrHome) getHome(userContext,CdrBulk.class);
			SQLBuilder sql2 = home2.createSQLBuilderEsteso();

			if (scds.getCds()!=null && scds.getCds().getCd_unita_organizzativa()!=null) {
				if (!scds.getCds().getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_SAC)) {
					sql.addClause("AND","cd_unita_padre",sql.EQUALS,scds.getCds().getCd_unita_organizzativa());
					sql.addClause("AND","fl_uo_cds",sql.EQUALS,Boolean.TRUE);
				}
				List lista = home.fetchAll(sql);
				if (lista.size()==1)
					scds.setUo((Unita_organizzativaBulk)lista.get(0));
				else {
					if (scds.getUo()!=null && !scds.getUo().getUnita_padre().equalsByPrimaryKey(scds.getCds()))
						scds.setUo(null);
				}

				if (scds.getCds().getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)) {
					Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0);
					scds.setUo(uoEnte);
				}

				if (scds.getUo()!=null && scds.getUo().getCd_unita_organizzativa()!=null) {
					if (scds.getUo().getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_AREA)) {
						sql2.addClause("AND","cd_unita_organizzativa",sql2.EQUALS,scds.getUo().getCd_unita_organizzativa());
					}
					else {
						if (!scds.getUo().getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_SAC)) {
							sql2.addClause("AND","cd_unita_organizzativa",sql2.EQUALS,scds.getUo().getCd_unita_organizzativa());
							//sql2.addClause("AND","livello",sql2.EQUALS,CdrHome.CDR_PRIMO_LIVELLO);
						}
					}

					sql2.setOrderBy("livello", it.cnr.jada.util.OrderConstants.ORDER_ASC);
					sql2.setOrderBy("cd_unita_organizzativa", it.cnr.jada.util.OrderConstants.ORDER_ASC);

					List lista2 = home2.fetchAll(sql2);
					//if (!scds.getCdr().equalsByPrimaryKey(findCdrEnte(userContext)))
					if (lista2.size()>=1) {
						if (scds.getCdr()==null || scds.getCdr().getCd_centro_responsabilita()==null || lista2.contains(scds.getCdr()))
							scds.setCdr((CdrBulk)lista2.get(0));
					}
					else {
						if (scds.getCdr()!=null && !scds.getCdr().getUnita_padre().equalsByPrimaryKey(scds.getUo()))
							scds.setCdr(null);
					}
				}
			}

			return scds;
		} catch (Throwable e) 
		{
			throw handleException(scds,e);
		}
	}
	public SelezionaCdsBulk findUo(UserContext userContext,SelezionaCdsBulk scds) throws it.cnr.jada.comp.ComponentException {
		try 
		{
			CdrHome home = (CdrHome) getHome(userContext,CdrBulk.class);
			SQLBuilder sql = home.createSQLBuilder();

			if (scds.getUo()!=null && scds.getUo().getCd_unita_organizzativa()!=null /*&& !scds.getUo().getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)*/) {
				scds.setCds(scds.getUo().getUnita_padre());
				//if (!scds.getUo().getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_SAC)) {
				sql.addClause("AND","cd_unita_organizzativa",sql.EQUALS,scds.getUo().getCd_unita_organizzativa());
				sql.setOrderBy("livello", it.cnr.jada.util.OrderConstants.ORDER_ASC);
				sql.setOrderBy("cd_unita_organizzativa", it.cnr.jada.util.OrderConstants.ORDER_ASC);
				sql.setOrderBy("cd_centro_responsabilita", it.cnr.jada.util.OrderConstants.ORDER_ASC);
				//}
				List lista = home.fetchAll(sql);
				if ((scds.getCdr()!=null && !scds.getCdr().equalsByPrimaryKey(findCdrEnte(userContext))) || scds.getCdr()==null)
					if (lista.size()>=1)
						if (scds.getCdr()==null || scds.getCdr().getCd_centro_responsabilita()==null || lista.contains(scds.getCdr())){
							if (!lista.contains(scds.getCdr()))
								scds.setCdr((CdrBulk)lista.get(0));
						}
						else
							if (scds.getCdr()!=null && !scds.getCdr().getUnita_padre().equalsByPrimaryKey(scds.getUo()))
								scds.setCdr(null);
			}

			return scds;
		} catch (Throwable e) 
		{
			throw handleException(scds,e);
		}
	}
	public CdrBulk findCdrEnte(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
		try 
		{
			Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0);

			CdrHome home = (CdrHome) getHome(userContext,CdrBulk.class);
			SQLBuilder sql = home.createSQLBuilderEsteso();

			sql.addClause("AND","cd_unita_organizzativa",sql.EQUALS,uoEnte.getCd_unita_organizzativa());
			sql.addClause("AND","livello",sql.EQUALS,CdrHome.CDR_PRIMO_LIVELLO);
			return (CdrBulk) home.fetchAll(sql).get(0);
		} catch (Throwable e) 
		{
			throw handleException(e);
		}
	}
	public it.cnr.jada.persistency.sql.SQLBuilder selectRuolo_supervisoreByClause(UserContext userContext, UtenteBulk aUB, RuoloBulk ruolo, it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException {
		SQLBuilder sql = ((RuoloHome)getHome(userContext,ruolo)).createSQLBuilder();
		//sql.addTableToHeader("TIPO_RUOLO");
		//sql.addSQLJoin("RUOLO.TIPO", "TIPO_RUOLO.TIPO");
		//sql.addSQLClause("AND", "TIPO_RUOLO.fl_supervisore", sql.EQUALS, "Y");
		sql.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
		sql.addSQLJoin("RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
		sql.addSQLClause("AND","ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO",SQLBuilder.EQUALS,PrivilegioBulk.ABILITA_SUPERVISORE);				

		sql.addClause(clauses);
		return sql;
	}
	public void validaSelezionaCds(UserContext userContext, SelezionaCdsBulk scds, Integer esercizio) throws it.cnr.jada.comp.ComponentException {
		try 
		{
			if (scds.getCds().getCd_unita_organizzativa()!=null && esercizio!=null) {
				EsercizioHome home = (EsercizioHome) getHome(userContext,EsercizioBulk.class);
				EsercizioBulk esercizioBulk = (EsercizioBulk) home.findByPrimaryKey(
						userContext,
						new EsercizioBulk(
								scds.getCds().getCd_unita_organizzativa(),
								esercizio)
						);
				if (esercizioBulk == null)
					throw  new it.cnr.jada.comp.ApplicationException("Il CDS "+scds.getCds().getCd_unita_organizzativa()+ " non è presente nell'esercizio "+esercizio);		
			}

		} catch (Throwable e) 
		{
			throw handleException(scds,e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public UtenteFirmaDettaglioBulk isUtenteAbilitatoFirma(UserContext userContext, AbilitatoFirma codice) throws it.cnr.jada.comp.ComponentException {
  	  	try {
  	  		Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).
  	  				findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext))));
			UtenteFirmaDettaglioHome home = (UtenteFirmaDettaglioHome) getHome(userContext, UtenteFirmaDettaglioBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addClause(FindClause.AND, "cdUtente", SQLBuilder.EQUALS, userContext.getUser());
			sql.addClause(FindClause.AND, "daData", SQLBuilder.LESS_EQUALS, EJBCommonServices.getServerDate());
			sql.addClause(FindClause.AND, "a_data", SQLBuilder.GREATER_EQUALS, EJBCommonServices.getServerDate());			
			List<UtenteFirmaDettaglioBulk> result = home.fetchAll(sql);
			getHomeCache(userContext).fetchAll(userContext);
			if (result.isEmpty())
				return null;
			for (UtenteFirmaDettaglioBulk firmatario : result) {
				if (firmatario.getFunzioniAbilitate() != null && firmatario.getFunzioniAbilitate().contains(codice.name())) {
					if (firmatario.getCdUnitaOrganizzativa().equalsIgnoreCase(CNRUserContext.getCd_unita_organizzativa(userContext)))
						return firmatario;
					if (firmatario.getUnitaOrganizzativa().isUoCds() && 
							firmatario.getUnitaOrganizzativa().getCd_unita_padre().equalsIgnoreCase(uoScrivania.getCd_unita_padre()))
						return firmatario;
				}
			}
			return null;
		} catch (PersistencyException e) {
			throw handleException(e);
		}
  	}
	public boolean isSupervisore(UserContext userContext)throws ComponentException{
		try {
			UtenteBulk utenteScrivania = (UtenteBulk)getHome(userContext,UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext)));
			if (utenteScrivania.isSupervisore())
				return true;
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		return false;
	}
}
