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

package it.cnr.contab.coepcoan00.comp;

import it.cnr.contab.anagraf00.tabrif.bulk.*;
import java.sql.*;
import it.cnr.contab.config00.pdcep.bulk.*;
import it.cnr.contab.coepcoan00.tabrif.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;

import java.io.Serializable;

public class AssAnagVoceEpComponent extends it.cnr.jada.comp.CRUDComponent implements IAssAnagVoceEpMgr,ICRUDMgr, Cloneable, Serializable
{
/**
 * ScritturaPartitaDoppiaComponent constructor comment.
 */
public AssAnagVoceEpComponent() {
	super();
}
/**
 * Nome: Ricerca dell'attributo relativo alla classificazione anagrafica con codice '*'
 * Pre:  E' stata richiesta la ricerca di una classificazione anagrafica e l'utente ha specificato come codice
 *       il valore '*' ad indicare tutte le classificazioni anagrafiche
 * Post: Viene restituito un RemoteIterator contenente solamente l'oggetto fittizio ( con codice '*' ) che rappresenta
 *       tutte le classificazioni anagrafiche
 *
 * Nome: Ricerca dell'attributo relativo alla classificazione anagrafica con codice diverso  da '*'
 * Pre:  E' stata richiesta la ricerca di una classificazione anagrafica e l'utente ha specificato come codice
 *       un valore diverso da '*' o l'utente non ha specificato alcun codice
 * Post: Viene restituito un RemoteIterator contenente la lista di oggetti di tipo Classificazione_anagBulk
 *       risultante dall'esecuzione della query sul database
 *
 * Nome: Ricerca di un attributo diverso dalla classificazione anagrafica
 * Pre:  E' stata richiesta la ricerca di un attributo dell'associazione anag/contoEP diverso dalla classificazione 
 *       anagrafica
 * Post: Viene restituito un RemoteIterator contenente la lista degli oggettiBulk 
 *       risultante dall'esecuzione della query sul database
 * 
 *
 * @param userContext <code>UserContext</code> 
 * @param clausole <code>CompoundFindClause</code>  clausole specificate dall'utente
 * @param bulk <code>OggettoBulk</code>  oggettoBulk da ricercare
 * @param contesto <code>Ass_anag_voce_epBulk</code>  contesto della ricerca
 * @param attributo nome dell'attributo del contesto che deve essere ricercato
 * @return <code>RemoteIterator</code>  elenco di oggetti trovati
 *
 */

public it.cnr.jada.util.RemoteIterator cerca(UserContext userContext,it.cnr.jada.persistency.sql.CompoundFindClause clausole,OggettoBulk bulk,OggettoBulk contesto,String attributo) throws it.cnr.jada.comp.ComponentException {
	if ( attributo.equals("classanag") )
	{
		if ( ((Classificazione_anagBulk) bulk).getCd_classific_anag() != null && ((Classificazione_anagBulk) bulk).getCd_classific_anag().equals("*"))
		{	
			Classificazione_anagBulk classif = getClassificazione_anagStar();
			return new it.cnr.jada.util.ArrayRemoteIterator(new Classificazione_anagBulk[] { classif });
		}
	}	
	return super.cerca(userContext,clausole,bulk,contesto,attributo);
}
/* restitusce un'istanza fittizia di Classificazione_anagBulk con codice = '*' a rappresentare
   tutte le classificazioni anagrafiche */
private Classificazione_anagBulk getClassificazione_anagStar() 
{
	Classificazione_anagBulk classif = new Classificazione_anagBulk();
	classif.setCd_classific_anag("*");
	classif.setDs_classific_anag("Tutte le classificazioni anagrafiche");
	classif.setCrudStatus( classif.NORMAL);
	return classif;
}
/**
 *
 * Nome: Inizializzazione di un'associazione Anagrafico/Conto EP
 * Pre:  E' stata richiesta l'inizializzazione per modifica di un'associazione Anagrafico/Conto EP
 * Post: L'associazione viene restituita con inizializzata la classificazione anagrafica ( se il codice della classificazione
 *       anagrafica e' diverso da '*', la classificazione viene letta dal database, altrimenti la classificazione viene
 *       valorizzata con un oggetto fittizio che rappresenta tutte le classificazioni anagrafiche) 
 *
 * @param userContext <code>UserContext</code> 
 * @param bulk <code>Ass_anag_voce_epBulk</code>  che deve essere inizializzata per modifica
 * @return <code>Ass_anag_voce_epBulk</code>  inizializzati per modifica
 *
 */

public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws ComponentException {
	try 
	{
		Ass_anag_voce_epBulk ass = (Ass_anag_voce_epBulk) super.inizializzaBulkPerModifica( userContext, bulk );
		if ( "*".equals( ass.getCd_classific_anag()) )
			ass.setClassanag( getClassificazione_anagStar());
		else
			ass.setClassanag( (Classificazione_anagBulk)getHome( userContext, Classificazione_anagBulk.class).findByPrimaryKey( new Classificazione_anagBulk( ass.getCd_classific_anag())));
		return ass;
	} catch(Exception e) {
		throw handleException(e);
	}
}
}
