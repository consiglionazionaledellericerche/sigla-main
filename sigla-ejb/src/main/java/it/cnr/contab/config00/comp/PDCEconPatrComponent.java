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

package it.cnr.contab.config00.comp;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.ejb.Lunghezza_chiaviComponentSession;
import it.cnr.contab.config00.esercizio.bulk.*;

import java.util.List;
import java.io.Serializable;

import it.cnr.contab.config00.pdcep.bulk.*;
import it.cnr.contab.config00.pdcep.cla.bulk.V_classificazione_voci_epBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * Classe che ridefinisce alcune operazioni di CRUD su ContoBulk e CapocontoBulk
 */


public class PDCEconPatrComponent  extends it.cnr.jada.comp.CRUDComponent implements IPDCEconPatrMgr, Cloneable,Serializable
{


//@@>> setComponentContext

//@@<< CONSTRUCTORCST
    public  PDCEconPatrComponent()
    {
//>>

//<< CONSTRUCTORCSTL
        /*Default constructor*/
//>>

//<< CONSTRUCTORCSTT

    }
/**
 * Esegue una operazione di ricerca di un ContoBulk o di un CapocontoBulk. 
 *
 * Pre-post-conditions:
 *
 * Nome: Ricerca di Capoconto/Conto
 * Pre:  La richiesta di ricerca di un Capoconto/Contoè stata generata
 * Post: La lista di Conti/Capoconti che soddisfano i criteri di ricerca sono stati recuperati
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	clausole eventuali clausole di ricerca specificate dall'utente
 * @param	bulk il ContoBulk o CapocontoBulk che deve essere ricercato
 * @return	la lista di ContoBulk o CapocontoBulk risultante dopo l'operazione di ricerca.
 */	

public it.cnr.jada.util.RemoteIterator cerca(UserContext userContext,it.cnr.jada.persistency.sql.CompoundFindClause clausole,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	try
	{
		Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(userContext, CNRUserContext.getEsercizio(userContext));
		if (!parCnr.getFl_nuovo_pdg().booleanValue()){
			Voce_epBulk voceBulk = (Voce_epBulk) bulk ;
		 			if (voceBulk.getTi_voce_ep().equals("P"))
						voceBulk.setCd_proprio_voce_ep( getLunghezza_chiavi().formatCapocontoKey( userContext,voceBulk.getCd_proprio_voce_ep(), voceBulk.getEsercizio() ));
					else
					{
						ContoBulk conto = (ContoBulk) voceBulk;
						conto.setCd_proprio_voce_ep( getLunghezza_chiavi().formatContoKey( userContext,conto.getCd_proprio_voce_ep(), conto.getEsercizio() ));
						if ( conto.getVoce_ep_padre() != null && conto.getVoce_ep_padre().getCd_voce_ep() != null )
							conto.getVoce_ep_padre().setCd_voce_ep( conto.getVoce_ep_padre().getCd_voce_ep().toUpperCase());
					}
		} 
		return super.cerca(userContext,clausole,bulk);
	} catch (Exception e)
		{
			throw handleException(e);
		}
}
/**
 * Esegue una operazione di ricerca di un attributo di un ContoBulk o di un CapocontoBulk. 
 *
 * Pre-post-conditions:
 *
 * Nome: Ricerca di attributo diverso da 'riapre_a_conto'
 * Pre:  La richiesta di ricerca di un attributo ricerca di un Capoconto/Conto è stata generata
 * Post: La lista dei valori possibili per quell'attributo e' stata recuperata
 *
 * Nome: Ricerca di attributo uguale a 'riapre_a_conto' con codice = codice conto
 * Pre:  La richiesta di ricerca dell'attributo 'riapre_a_conto' di un Conto è stata generata e il codice selezionato
 *       dall'utente e' il codice del Conto stesso
 * Post: L'istanza di Conto viene ritornata senza effettuare la ricerca
 *
 * Nome: Ricerca di attributo uguale a 'riapre_a_conto' con codice diverso da codice conto
 * Pre:  La richiesta di ricerca dell'attributo 'riapre_a_conto' di un Conto è stata generata e il codice selezionato
 *       dall'utente e' diverso dal codice del Conto stesso
 * Post: La lista di Conti che soddisfano le condizioni imposte dall'utente viene ritornata 
 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	clausole eventuali clausole di ricerca specificate dall'utente
 * @param	bulk l'attributo che deve essere ricercato
 * @param	contesto il ContoBulk o CapocontoBulk da utilizzare come contesto della ricerca
 * @param	attributo il nome dell'attributo del ContoBulk o del CapocontoBulk che deve essere ricercato 
 * @return	la lista di oggetti risultanti dopo l'operazione di ricerca.
 */	

public it.cnr.jada.util.RemoteIterator cerca(UserContext userContext,it.cnr.jada.persistency.sql.CompoundFindClause clausole,OggettoBulk bulk,OggettoBulk contesto,String attributo) throws it.cnr.jada.comp.ComponentException 
{
	if ( attributo.equals( "riapre_a_conto") && ((ContoBulk)bulk).getCd_voce_ep()!= null && 
		((ContoBulk)bulk).getCd_voce_ep().equals( ((ContoBulk)contesto).getCd_voce_ep()) )
	{
		return new it.cnr.jada.util.ArrayRemoteIterator(new ContoBulk[] { (ContoBulk)contesto });
	}
	return super.cerca(userContext,clausole,bulk,contesto,attributo);
}
/**
 * Esegue una operazione di creazione di un CapocontoBulk.
 * In particolare, se il codice proprio del Capoconto non viene specificato
 * dall'utente, il sistema ne genera uno automaticamente; altrimenti, se
 * il codice e' stato inserito, viene formattato.
 *
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	voce_epBulk il CapocontoBulk che deve essere creato
 * @return	OggettoBulk il CapocontoBulk risultante dopo l'operazione di creazione.
 */
 
	private OggettoBulk creaCapocontoConBulk (UserContext userContext,Voce_epBulk voce_epBulk) throws it.cnr.jada.comp.ComponentException
		{
			try
			{
				Voce_epHome voce_epHome = (Voce_epHome) getHomeCache(userContext).getHome(voce_epBulk.getClass());

				if (voce_epBulk.getCd_proprio_voce_ep() == null || voce_epBulk.getCd_proprio_voce_ep().equals("") )
				{
					String codice = voce_epHome.creaNuovoCodiceCapoconto( voce_epBulk.getEsercizio(), voce_epBulk.getCd_voce_ep_padre());
					voce_epBulk.setCd_proprio_voce_ep( getLunghezza_chiavi().formatCapocontoKey( userContext,codice, voce_epBulk.getEsercizio() ));
				}
				else
					voce_epBulk.setCd_proprio_voce_ep( getLunghezza_chiavi().formatCapocontoKey( userContext,voce_epBulk.getCd_proprio_voce_ep(), voce_epBulk.getEsercizio() ));				
					

				voce_epBulk.setCd_voce_ep(voce_epBulk.getCd_voce_ep_padre().concat(".").concat(voce_epBulk.getCd_proprio_voce_ep()));
				voce_epBulk.setLivello(new Integer(2));
				voce_epBulk.setFl_mastrino(new Boolean (false));
				insertBulk(userContext, voce_epBulk);
				return voce_epBulk;
			}
			catch (it.cnr.jada.persistency.sql.DuplicateKeyException e) 
			{
				if (e.getPersistent() != voce_epBulk)
					throw handleException(voce_epBulk ,e);
				try 
				{
					throw handleException(new CRUDDuplicateKeyException("Errore di chiave duplicata",e,voce_epBulk,(OggettoBulk)getHome(userContext,voce_epBulk).findByPrimaryKey(voce_epBulk)));
				} catch(Throwable ex) 
				{
					throw handleException(voce_epBulk,ex);
				}
			}			
			catch (Exception e)
			{
				throw handleException(e);
			}
		}
/**
 * Esegue una operazione di creazione di un ContoBulk o di un CapocontoBulk. 
 *
 * Pre-post-conditions:
 *
 * Nome: Creazione di Capoconto senza codice proprio
 * Pre:  La richiesta di creazione di un Capoconto senza aver specificato un codice proprio è stata generata
 * Post: Un Capoconto e' stato creato con i dati inseriti dall'utente e il suo codice e' stato generato 
 *       automaticamente.
 *
 * Nome: Creazione di Capoconto con codice proprio
 * Pre:  La richiesta di creazione di un Capoconto con un codice proprio specificato dall'utente
 *       è stata generata
 * Post: Un Capoconto e' stato creato con i dati inseriti dall'utente e il suo codice e' stato formattato
 *
 * Nome: Errore di chiave duplicata per Capoconto
 * Pre:  Esiste già un Capoconto persistente che possiede la stessa chiave
 * 		 primaria di quello specificato.
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Creazione di Conto senza codice proprio
 * Pre:  La richiesta di creazione di un Conto senza aver specificato un codice proprio è stata generata
 * Post: Un Conto e' stato creato con i dati inseriti dall'utente e il suo codice e' stato generato 
 *       automaticamente.
 *
 * Nome: Creazione di Conto con codice proprio
 * Pre:  La richiesta di creazione di un Conto con un codice proprio specificato dall'utente
 *       è stata generata
 * Post: Un Conto e' stato creato con i dati inseriti dall'utente e il suo codice e' stato formattato
 *
 * Nome: Creazione di Conto senza codice 'riapre su conto'
 * Pre:  La richiesta di creazione di un Conto senza aver specificato il codice del conto su cui riapre
 *       è stata generata. Il conto riepiloga a stato patrimoniale (SPA) (Richiesta CNR n.35)
 * Post: Un Conto e' stato creato e il codice del conto su cui riapre e' il codice del conto creato
 *
 * Nome: Errore di chiave duplicata per Conto
 * Pre:  Esiste già un Conto persistente che possiede la stessa chiave primaria di quello specificato.
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Errore di Capoconto inesistente
 * Pre:  Il capoconto specificato dall'utente non esiste
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk il ContoBulk o CapocontoBulk che deve essere creato
 * @return	il ContoBulk o CapocontoBulk risultante dopo l'operazione di creazione.
 */	

public OggettoBulk creaConBulk (UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException
{
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(userContext))
		throw new ApplicationException("Non è possibile creare nuove voci ad esercizio chiuso.");
	
	try
	{
				Voce_epBulk voce_epBulk = (Voce_epBulk) bulk;
				if ( voce_epBulk.getTi_voce_ep().equals("P"))
					return creaCapocontoConBulk(userContext,voce_epBulk);
				else
					return creaContoConBulk(userContext,voce_epBulk);				
			}
			catch (Exception e)
			{
				throw handleException(bulk,e);
			}
		}
/**
 * Esegue una operazione di creazione di un ContoBulk.
 * In particolare, se il codice proprio del Conto non viene specificato
 * dall'utente, il sistema ne genera uno automaticamente; altrimenti, se
 * il codice e' stato inserito, viene formattato.
 * Inoltre, se il codice 'riapre su conto' non viene specificato dall'
 * utente, il codice del conto su cui riapre equivale al codice del Conto
 * generato nel caso il conto riepiloghi a stato Patrimoniale (SPA).
 *
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	voce_epBulk il CapocontoBulk che deve essere creato
 * @return	OggettoBulk il CapocontoBulk risultante dopo l'operazione di creazione.
 */

	private OggettoBulk creaContoConBulk (UserContext userContext,Voce_epBulk voce_epBulk) throws it.cnr.jada.comp.ComponentException
	{
			ContoBulk contoBulk = (ContoBulk) voce_epBulk;							
			try
			{
				Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(userContext, CNRUserContext.getEsercizio(userContext)); 
				
				
				ContoHome contoHome = (ContoHome) getHomeCache(userContext).getHome(contoBulk.getClass());
				if (!parCnr.getFl_nuovo_pdg().booleanValue()){

					if (contoBulk.getCd_proprio_voce_ep() == null || contoBulk.getCd_proprio_voce_ep().equals("") )
					{
						String codice = contoHome.creaNuovoCodiceConto( contoBulk.getEsercizio(), contoBulk.getVoce_ep_padre().getCd_voce_ep());
						contoBulk.setCd_proprio_voce_ep( getLunghezza_chiavi().formatContoKey( userContext,codice, contoBulk.getEsercizio() ) );
					}
					else
						contoBulk.setCd_proprio_voce_ep( getLunghezza_chiavi().formatContoKey( userContext,contoBulk.getCd_proprio_voce_ep(), contoBulk.getEsercizio() ));
	
					contoBulk.setCd_voce_ep(contoBulk.getVoce_ep_padre().getCd_voce_ep().concat(".").concat(contoBulk.getCd_proprio_voce_ep()));
	
					if ( 
					       (contoBulk.getRiapre_a_conto() != null && contoBulk.getRiapre_a_conto().getCd_voce_ep() == null )
		                && (contoBulk.getRiepiloga_a() != null && contoBulk.getRiepiloga_a().equals("SPA"))
					)				
					{				
						contoBulk.getRiapre_a_conto().setCd_voce_ep(contoBulk.getCd_voce_ep());
						contoBulk.getRiapre_a_conto().setDs_voce_ep( contoBulk.getDs_voce_ep());
					}	
					
					if ( !contoBulk.isFl_gruppoNaturaNonCongruiConfermati() && !contoBulk.getDs_gruppo().equals("X") &&
						 !contoBulk.getDs_gruppo().equals((String)contoBulk.getAssociazioni_natura_gruppo().get(contoBulk.getNatura_voce())))
					{
						throw new GruppoNaturaNonCongrui();
					}	
					
					contoBulk.setLivello(new Integer(3));
					contoBulk.setFl_mastrino(new Boolean ( true ));
					((CapocontoHome)getHomeCache(userContext).getHome( CapocontoBulk.class )).lock( contoBulk.getVoce_ep_padre() );
				}
				else{
					if (contoBulk.getRiepiloga_a() == null)
						throw new ApplicationException("Il campo Riepiloga a è obbligatorio.");
					contoBulk.setCd_voce_ep(contoBulk.getCd_proprio_voce_ep());
					contoBulk.setLivello(new Integer(1));
					contoBulk.setFl_mastrino(new Boolean ( true ));
				}			
				insertBulk( userContext, contoBulk);
				return contoBulk;
			}
			catch( it.cnr.jada.persistency.FindException e )
			{
				throw handleException( new ApplicationException( "Il Capoconto " + contoBulk.getVoce_ep_padre().getCd_voce_ep() + " è inesistente" ));
			}
			catch (it.cnr.jada.persistency.sql.DuplicateKeyException e) 
			{
				if (e.getPersistent() != voce_epBulk)
					throw handleException(voce_epBulk,e);
				try 
				{
					throw handleException(new CRUDDuplicateKeyException("Errore di chiave duplicata",e,voce_epBulk,(OggettoBulk)getHome(userContext,voce_epBulk).findByPrimaryKey(voce_epBulk)));
				} catch(Throwable ex) 
				{
					throw handleException(voce_epBulk,ex);
				}
			}			
			catch (Exception e)
			{
				throw handleException(e);
			}
	}
/**
 * Esegue una operazione di eliminazione di ContoBulk o di un CapocontoBulk
 *
 * Pre-post-conditions:
 *
 * Nome: Cancellazione di un CapocontoBulk senza Conti da lui dipendenti
 * Pre:  La richiesta di cancellazione di un Capoconto che non ha Conti dipendenti e' stata generata
 * Post: Il Capoconto e' stato cancellato
 *
 * Nome: Cancellazione di un CapocontoBulk con Conti da lui dipendenti
 * Pre:  La richiesta di cancellazione di un Capoconto che ha Conti dipendenti e' stata generata
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Cancellazione di un ContoBulk
 * Pre:  La richiesta di cancellazione di un Contoe' stata generata
 * Post: Il Capoconto e' stato cancellato
 * 
 * @param userContext lo userContext che ha generato la richiesta
 * @param bulk l'istanza di CapocontoBulk o ContoBulk che deve essere cancellata 
 */


public void eliminaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException 
{
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(userContext))
		throw new ApplicationException("Non è possibile eliminare voci ad esercizio chiuso.");
	
	try
	{
		Voce_epBulk voce_epBulk = (Voce_epBulk) bulk;
		if ( voce_epBulk.getTi_voce_ep().equals("P"))
		{
			verificaConti( userContext,(CapocontoBulk) voce_epBulk );
			makeBulkPersistent(userContext,bulk);
		}			
		else
			makeBulkPersistent(userContext,bulk);
	}
	catch (Exception e)
	{
		throw handleException(bulk,e);
	}
}
/**
 * Recupera la Home dell'ejb Lunghezza_chiaviComponent.
 * @return Lunghezza_chiaviComponentSessionHome la Home dell'ejb Lunghezza_chiaviComponent
 */
private it.cnr.contab.config00.ejb.Lunghezza_chiaviComponentSession getLunghezza_chiavi( ) throws it.cnr.jada.comp.ComponentException
{
	try
	{
		return (Lunghezza_chiaviComponentSession) EJBCommonServices.createEJB( "CNRCONFIG00_EJB_Lunghezza_chiaviComponentSession", Lunghezza_chiaviComponentSession.class );
	}
	catch (Exception e )
	{
		throw handleException( e ) ;
	}		
}
		
	
public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	bulk = super.inizializzaBulkPerModifica(userContext,bulk);
	if (isEsercizioChiuso(userContext))
		bulk = asRO(bulk,"Non è possibile modificare voci ad esercizio chiuso.");
	return bulk;
}
protected boolean isEsercizioChiuso(UserContext userContext) throws ComponentException {
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	try {
		EsercizioHome home = (EsercizioHome)getHome(userContext,EsercizioBulk.class);
		return home.isEsercizioChiusoPerAlmenoUnCds(userContext);
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/**
 * Esegue una operazione di modifica di un ContoBulk o di un CapocontoBulk. 
 *
 * Pre-post-conditions:
 *
 * Nome: Modifica di Capoconto 
 * Pre:  La richiesta di modifica di un Capoconto è stata generata
 * Post: Il Capoconto e' stato modificato
 *
 * Nome: Modifica di Conto 
 * Pre:  La richiesta di modifica di un Conto è stata generata
 * Post: Il Conto e' stato modificato
 *
 * Nome: Modifica di Conto senza codice 'riapre su conto' 
 * Pre:  La richiesta di modifica di un Conto senza aver specificato il codice del conto su cui riapre è stata generata
 * Post: Il Conto e' stato modificato e il codice del conto su cui riapre e' il codice del conto stesso
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk il ContoBulk o CapocontoBulk che deve essere modificato
 * @return	il ContoBulk o CapocontoBulk risultante dopo l'operazione di modifica
 */	

public OggettoBulk modificaConBulk (UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	// 05/09/2003
	// Aggiunto controllo sulla chiusura dell'esercizio
	if (isEsercizioChiuso(userContext))
		throw new ApplicationException("Non è possibile modificare voci ad esercizio chiuso.");
	
	try
	{
		if ( bulk instanceof ContoBulk)
		{						
			ContoBulk contoBulk = (ContoBulk) bulk ;
			if (contoBulk.getRiapre_a_conto() != null && contoBulk.getRiapre_a_conto().getCd_voce_ep() == null )
			{				
				contoBulk.getRiapre_a_conto().setCd_voce_ep(contoBulk.getCd_voce_ep());
				contoBulk.getRiapre_a_conto().setDs_voce_ep( contoBulk.getDs_voce_ep());
			}
			if(contoBulk.getDs_gruppo()!=null){
				if ( !contoBulk.isFl_gruppoNaturaNonCongruiConfermati() && !contoBulk.getDs_gruppo().equals("X") &&
				 !contoBulk.getDs_gruppo().equals((String)contoBulk.getAssociazioni_natura_gruppo().get(contoBulk.getNatura_voce())))
				{
					throw new GruppoNaturaNonCongrui();
				}
			}
		}			
		makeBulkPersistent( userContext,bulk );
		return bulk;
	} catch (Throwable e) 
	{
		throw handleException(bulk,e);
	} 
}
/**
 * Esegue una operazione di controllo contestuale a livello di Capoconto.
 * In particolare, non e' possibile cancellare un Capoconto se esistono
 * uno o piu' conti ad esso associati.
 *
 *
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	capoconto il CapocontoBulk in questione
 */
 
	private void verificaConti( UserContext userContext,CapocontoBulk capoconto ) throws it.cnr.jada.comp.ComponentException
	{
		try
		{
			ContoBulk conto = new ContoBulk();
			conto.setEsercizio( capoconto.getEsercizio() );
			conto.setVoce_ep_padre( capoconto );
			List result = getHomeCache(userContext).getHome( conto.getClass()).find( conto, false );
			if ( !result.isEmpty() )
				throw new ApplicationException( "Non è possibile cancellare il capoconto perchè esistono conti ad esso associati");
	
		}
		catch ( Exception e )
		{
			throw handleException( e );
		}			
	}
	public SQLBuilder selectV_classificazione_voci_epByClause(UserContext userContext,
			 ContoBulk conto,
			 V_classificazione_voci_epBulk classificazioneVoci,
			 CompoundFindClause clause) throws ComponentException, PersistencyException 
	{
		SQLBuilder sql = getHome(userContext, classificazioneVoci).createSQLBuilder();
		if (conto!=null && conto.getRiepiloga_a()!=null)
			if(conto.getRiepiloga_a().equals("SPA"))
				sql.addClause("AND", "tipo", sql.EQUALS,"PAT");
			else
				sql.addClause("AND", "tipo", sql.EQUALS,"ECO");
		sql.addClause(clause);
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		
		sql.addClause("AND", "fl_mastrino", sql.EQUALS, Boolean.TRUE);
		if (clause != null) 
		sql.addClause(clause);
		return sql;
	}
}
