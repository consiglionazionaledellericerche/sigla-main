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

import java.util.*;

import it.cnr.contab.utenze00.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.*;

import java.io.Serializable;


/**
 * Classe che ridefinisce alcune operazioni di CRUD su RuoloBulk
 */

public class TipoRuoloComponent extends CRUDComponent implements ICRUDMgr, it.cnr.contab.utenze00.comp.IRuoloMgr, Cloneable,Serializable

{


public  TipoRuoloComponent()
{

}

/**
 * Esegue l'inizializzazione di una nuova istanza di Tipo_ruoloBulk impostando l'elenco di privilegi disponibili per
 * l'utente corrente
 *
 * Pre-post-conditions:
 *
 * Nome: Inizializzazione bulk 
 * Pre:  L'inizializzazione di un Tipo_ruoloBulk per eventuale inserimento e' stata generata
 * Post: Il Tipo_ruoloBulk viene aggiornato con l'elenco delle istanze di PrivilegioBulk disponibili per l'utente corrente
 *
 * Nome: Gestore non trovato
 * Pre:  L'utente che ha generato la richiesta non esiste
 * Post: Viene generata una ComponentException con detail l'ApplicationException con il messaggio 
 *       da visualizzare all'utente
 *
 * @param userContext lo userContext che ha generato la richiesta
 * @param bulk l'istanza di Tipo_ruoloBulk che deve essere inizializzata
 * @return il Tipo_ruoloBulk inizializzato 
 */



public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,OggettoBulk bulk) throws ComponentException
{
	try
	{
		Tipo_ruoloBulk tipo_ruolo = (Tipo_ruoloBulk) bulk;
		Tipo_ruoloHome tipo_ruoloHome = (Tipo_ruoloHome) getHomeCache(userContext).getHome(Tipo_ruoloBulk.class);

		UtenteBulk gestore = (UtenteBulk) getHomeCache(userContext).getHome( UtenteBulk.class ).findByPrimaryKey( new UtenteKey( bulk.getUser()));
		if ( gestore == null )
			throw  new ApplicationException( "Utente Gestore non definito" );
		tipo_ruolo.setGestore( gestore );		

		// carica privilegi disponibili
		tipo_ruolo.setPrivilegi_disponibili(tipo_ruoloHome.findPrivilegi_disponibili( gestore ));

		return tipo_ruolo;
			
	}
	catch (Exception e )
	{
		throw handleException( e );
	}
}
/**
 * Esegue l'inizializzazione di una nuova istanza di Tipo_ruoloBulk impostando l'elenco di Privilegi già assegnati 
 * e l'elenco di Privilegi ancora disponibili
 *
 * Pre-post-conditions:
 *
 * Nome: Inizializzazione bulk 
 * Pre:  L'inizializzazione di un Tipo_ruoloBulk per eventuale modifica e' stata generata
 * Post: Il Tipo_ruoloBulk viene aggiornato con l'elenco delle istanze di PrivilegioBulk ancora disponibili e con
 *		 l'elenco di istanze di Ass_tipo_ruolo_privilegioBulk gia' assegnate al tipo ruolo
 * 
 * Nome: Gestore non trovato
 * Pre:  L'utente che ha generato la richiesta non esiste
 * Post: Viene generata una ComponentException con detail l'ApplicationException con il messaggio 
 *       da visualizzare all'utente
 *
 * @param userContext lo userContext che ha generato la richiesta
 * @param bulk l'istanza di Tipo_ruoloBulk che deve essere inizializzata
 * @return il Tipo_ruoloBulk inizializzato
 */



public OggettoBulk inizializzaBulkPerModifica (UserContext userContext,OggettoBulk bulk) throws ComponentException
{
	try
	{
		Tipo_ruoloBulk tipo_ruolo = (Tipo_ruoloBulk) super.inizializzaBulkPerModifica(userContext, bulk );
		Tipo_ruoloHome tipo_ruoloHome = (Tipo_ruoloHome) getHomeCache(userContext).getHome(Tipo_ruoloBulk.class);
		
		UtenteBulk gestore = (UtenteBulk) getHomeCache(userContext).getHome( UtenteBulk.class ).findByPrimaryKey( new UtenteKey( bulk.getUser()));
		if ( gestore == null )
			throw  new ApplicationException( "Utente Gestore non definito" );
		tipo_ruolo.setGestore( gestore );		
    //		carica TIPO_RUOLO_PRIVILEGI
		Collection result = tipo_ruoloHome.findTipo_ruolo_privilegi(tipo_ruolo);
		for (Iterator i = result.iterator(); i.hasNext();)
		{
			Ass_tipo_ruolo_privilegioBulk ra = (Ass_tipo_ruolo_privilegioBulk)i.next();
			tipo_ruolo.addToTipo_ruolo_privilegi( ra );
		}

		// carica privilegi disponibili
		tipo_ruolo.setPrivilegi_disponibili(tipo_ruoloHome.findPrivilegi_disponibili( gestore ) );
		
		return tipo_ruolo;		
			
	}
	catch (Exception e )
	{
		throw handleException( e );
	}	

	
}
}
