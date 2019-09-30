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

public class UbicazioneBeneComponent
	extends it.cnr.jada.comp.CRUDComponent
	implements ICRUDMgr,IUbicazioneBeneMgr,Cloneable,Serializable
{



    public  UbicazioneBeneComponent()
    {

        /*Default constructor*/


    }
/**
  *  Codice proprio non valido
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di una Ubicazione associata alla UO di scrivania.
  *		E' stato specificato un codice per l'Ubicazione, (codice_proprio), che contiene caratteri
  *		non validi.
  *    PostCondition:
  *      Viene lanciata una eccezione cche presenta un messaggio informativo all'utente.
  *
  *  Ubicazione di default già esistente.
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di una Ubicazione associata alla UO di scrivania.
  *		L'ubicazione è stata indicata come quella di default, (FL_UBICAZIONE_DEFAULT = 'Y'), ma
  *		esiste già, per la UO di scrivania, una ubicazione indicata come default.
  *    PostCondition:
  *      Viene lanciata una eccezione cche presenta un messaggio informativo all'utente.
  *
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di una Ubicazione associata alla UO di scrivania.
  *    PostCondition:
  *      Viene consentito il salvataggio.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Bulk da creare
  *
  * @return bulk l'oggetto <code>OggettoBulk</code> creato
**/ 
public OggettoBulk creaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException{

	Ubicazione_beneBulk ubi = (Ubicazione_beneBulk)bulk;
	int livello = 0;
	String str = ubi.getCd_ubicazione_propria();

	if (str!=null){
		try{
			Integer.parseInt(str);
		} catch(NumberFormatException e){
			throw new it.cnr.jada.comp.ComponentException(new it.cnr.jada.comp.ApplicationException("Attenzione: il codice proprio non può contenere caratteri alfanumerici."));
		}
	}
	
	if (ubi.getNodoPadre()!= null && ubi.getNodoPadre().getCd_ubicazione() != null && ubi.getNodoPadre().getLivello() != null){
		str = ubi.getNodoPadre().getCd_ubicazione()+"."+str;
		livello = ubi.getNodoPadre().getLivello().intValue()+1;
	}

	try{
		// Si sta creando una Ubicazione indicata come quella di Default x la UO di scrivania,
		//	ma in base dati esiste già una ubicazione con questa caratteristica, x la setssa UO.
		if (ubi.getFl_ubicazione_default().booleanValue() &&
			findUbicazioneFittiziaFor(aUC) != null){
			
			throw new it.cnr.jada.comp.ComponentException(new it.cnr.jada.comp.ApplicationException("Attenzione: esiste già una ubicazione fittizia per questa Unita Organizzativa."));
		}
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw handleException(pe);
	}

	ubi.setCd_ubicazione(str);
	ubi.setLivello(new Integer(livello));

	return super.creaConBulk(aUC, ubi);

}
//^^@@
/** 
  *  L'Ubicazione ha delle Ubicazioni figlie
  *    PreCondition:
  *      Si sta tentando di cancellare una Ubicazione che ha sotto di sè dei nodi figli.
  *    PostCondition:
  *      Viene visualizzato un messaggio all'utente con che spiega la necessità di cancellare 
  *		tutti i nodi figli prima di cancellare il nodo padre.
  *
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di cancellazione di una Ubicazione associata alla UO di scrivania.
  *    PostCondition:
  *      Viene consentito l'eliminazione.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.  
  * @param bulk <code>OggettoBulk</code> il Bulk da eliminare.
**/ 
public void eliminaConBulk (UserContext userContext,OggettoBulk bulk) throws ComponentException{

	try{
		Ubicazione_beneBulk ubi = (Ubicazione_beneBulk)bulk;
		Ubicazione_beneHome ubiHome = (Ubicazione_beneHome)getHome(userContext,ubi);

		// CNRADM (09/10/2002 17:03:25)
		// Modificato perchè usa RemoteIterator in una componente.
		//if(getChildren(userContext,ubi).hasMoreElements())
		if (ubiHome.selectChildrenFor(userContext,ubi).executeExistsQuery(getConnection(userContext)))
			throw new it.cnr.jada.comp.CRUDValidationException("Attenzione: l'Ubicazione selezionata ha dei nodi figli. Eliminare prima tutti i figli.");
	
		super.eliminaConBulk(userContext, ubi);
	}catch(Throwable ex){
		throw handleException(bulk,ex);
	}
}
/** 
  *  Ricerca l'Ubicazione Fittizia per il CdS, UO di scrivania.
  *    PreCondition:
  *      E' stata generata la richiesta di cercare una Ubicazione indicata come quella fittizia.
  *    PostCondition:
  *		E' stato creato il SQLBuilder con le clausole che l'Ubicazione sia associata alla UO di scrivania
  *		e che abbia il FL_UBICAZIONE_FITTIZIA = 'Y'.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
  *
  * @return ubicazione_fittizia l'<code>Ubicazione_beneBulk</code> risultato della selezione.
**/
private Ubicazione_beneBulk findUbicazioneFittiziaFor(UserContext userContext) throws ComponentException, it.cnr.jada.persistency.PersistencyException {

	Ubicazione_beneHome home = (Ubicazione_beneHome)getHome(userContext, Ubicazione_beneBulk.class);
	SQLBuilder sql = home.createSQLBuilder();

	sql.addSQLClause("AND","CD_CDS",sql.EQUALS,	it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
	sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,	it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
	sql.addSQLClause("AND","FL_UBICAZIONE_DEFAULT",sql.EQUALS,	"Y");	

	it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
	
    if (!broker.next())
	    return null;	

	Ubicazione_beneBulk ubicazione_fittizia = (Ubicazione_beneBulk)broker.fetch(Ubicazione_beneBulk.class);
	return ubicazione_fittizia;
}
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di un Iteratore su tutti i nodi figli 
  *		di una Ubicazione.
  *    PostCondition:
  *		 Viene restituito il RemoteIterator con l'elenco degli eventuali nodi figli dell'Ubicazione di riferimento.
  *      
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'Ubicazione di riferimento.
  *
  * @return remoteIterator <code>RemoteIterator</code> l'Iterator creato.
**/ 
public RemoteIterator getChildren(UserContext userContext, OggettoBulk bulk) throws ComponentException{

	Ubicazione_beneBulk ubi = (Ubicazione_beneBulk)bulk;
	Ubicazione_beneHome ubiHome = (Ubicazione_beneHome)getHome(userContext,Ubicazione_beneBulk.class);

	return iterator(
		userContext,
		ubiHome.selectChildrenFor(userContext,ubi),
		Ubicazione_beneBulk.class,
		null);
}
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca dell'Ubicazione padre dell'Ubicazione specificata negli argomenti.
  *    PostCondition:
  *		 Viene restituito l'oggetto UbicazioneBulk che è l'Ubicazione padre cercata.
  *      
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'Ubicazione di riferimento.
  *
  * @return bulk <code>OggettoBulk</code> l'Ubicazione cercata.
**/ 
public OggettoBulk getParent(UserContext userContext, OggettoBulk bulk) throws ComponentException{

	try{
		Ubicazione_beneBulk ubi = (Ubicazione_beneBulk)bulk;
		Ubicazione_beneHome ubiHome = (Ubicazione_beneHome)getHome(userContext,Ubicazione_beneBulk.class);
		
		return ubiHome.getParent(ubi);
		
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(bulk,ex);
	}catch(it.cnr.jada.persistency.IntrospectionException ex){
		throw handleException(bulk, ex);
	}
}
/** 
  *  Inizializzazione di una istanza di Ubicazione_beneBulk
  *    PreCondition:
  *      E' stata generata la richiesta di inizializzare una istanza di Ubicazione_beneBulk.
  *    PostCondition:
  *      Vengono inizializzate le proprietà dell'Ubicazione_beneBulk e, l'oggetto risultante, viene restituito.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
  * @param bulk <code>OggettoBulk</code> il bulk che deve essere inizializzato.
  *
  * @return bulk <code>OggettoBulk</code> il bulk inizializzato.
**/
public OggettoBulk inizializzaBulkPerInserimento (
	UserContext aUC,
	OggettoBulk bulk) 
		throws ComponentException
{

	super.inizializzaBulkPerInserimento(aUC,bulk);	
		
	Ubicazione_beneBulk ubicazione = (Ubicazione_beneBulk) bulk;

	ubicazione.setNodoPadre(new Ubicazione_beneBulk());
		
	return ubicazione;
}



                  
/** 
  *  Controlla che l'ubicazione sia una foglia.
  *    PreCondition:
  *      E' stata generata la richiesta di controllare se l'Ubicazione specificata è una foglia,
  *		ossia se il suo livello è l'ultimo, (3). Questo implicherebbe che l'Ubicazione in 
  *		questione non può avere delle Ubicazioni figlie.
  *    PostCondition:
  *		 Viene restituito un valore booleano:
  *			- true: l'Ubicazione è una foglia;
  *			- false: l'Ubicazione non è una foglia.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> l'Ubicazione di riferimento.
  *
  * @return il risultato <code>boolean</code> del controllo.
**/ 
public boolean isLeaf(UserContext userContext, OggettoBulk bulk) throws ComponentException{
	try {
		Ubicazione_beneBulk ubi = (Ubicazione_beneBulk)bulk;
		Ubicazione_beneHome ubiHome = (Ubicazione_beneHome)getHome(userContext,ubi);

		// CNRADM (09/10/2002 17:03:25)
		// Modificato perchè usa RemoteIterator in una componente.
		//return ( (((Ubicazione_beneBulk)bulk).getLivello().intValue()==2) || 
					//(getChildren(userContext, bulk).countElements() == 0) );
		return ( ubi.getLivello().intValue()==2 ||
				 !ubiHome.selectChildrenFor(userContext,ubi).executeExistsQuery(getConnection(userContext)));
	} catch(Throwable e) {
		throw handleException(e);
	}
}
/**
  *  Ubicazione di default già esistente.
  *    PreCondition:
  *      E' stata generata la richiesta di modifica di una Ubicazione associata alla UO di scrivania.
  *		L'ubicazione è stata indicata come quella di default, (FL_UBICAZIONE_DEFAULT = 'Y'), ma
  *		esiste già, per la UO di scrivania, una ubicazione indicata come default.
  *    PostCondition:
  *      Viene lanciata una eccezione cche presenta un messaggio informativo all'utente.
  *
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di modifica di una Ubicazione associata alla UO di scrivania.
  *    PostCondition:
  *      Viene consentito il salvataggio.
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Bulk da creare
  *
  * @return bulk l'oggetto <code>OggettoBulk</code> creato
**/ 
public OggettoBulk modificaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException {

	Ubicazione_beneBulk ubi = (Ubicazione_beneBulk)bulk;
	
	try{
		// Si sta modificando una Ubicazione indicata come quella di Default x la UO di scrivania,
		//	ma in base dati esiste già una ubicazione con questa caratteristica, x la setssa UO.
		if (ubi.getFl_ubicazione_default().booleanValue() &&
			findUbicazioneFittiziaFor(aUC) != null){
			
			throw new it.cnr.jada.comp.ComponentException(new it.cnr.jada.comp.ApplicationException("Attenzione: esiste già una ubicazione fittizia per questa Unita Organizzativa."));
		}
	} catch (it.cnr.jada.persistency.PersistencyException pe){
		throw handleException(pe);
	}

	
	return super.modificaConBulk(aUC,bulk);

}
/** 
  *  Ricerca di una Ubicazione
  *    PreCondition:
  *      E' stata generata la richiesta di ricercare una Ubicazione.
  *    PostCondition:
  *		E' stato creato il SQLBuilder con le clausole implicite (presenti nell'istanza di Ubicazione_beneBulk),
  *		ed è stata aggiunta la clausola che l'Ubicazione sia associata alla UO di scrivania.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
  * @param bulk <code>OggettoBulk</code> l'OggettoBulk l'Ubicazione di riferimento.
  *
  * @return sql <code>SQLBuilder</code> Risultato della selezione.
**/
protected it.cnr.jada.persistency.sql.Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {

	SQLBuilder sql = (SQLBuilder)super.select(userContext,clauses,bulk);

	sql.addSQLClause("AND","CD_CDS",sql.EQUALS,	it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
	sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,	it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
	sql.addOrderBy("CD_UBICAZIONE");

	return sql;
}
/**
  *  Ricerca di una Ubicazione padre
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca di una Ubicazione che sia nodo padre della
  *		Ubicazione di riferimento.
  *    PostCondition:
  *		 Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
  *		clausole che le Ubicazioni siano associate alla UO di scrivania e che il livello di 
  *		appartenenza sia minore di 3, (Ubicazioni di livello 3, sono considerate "foglie" e 
  *		non possono avere delle Ubicazioni figlie).
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param ubicazione <code>Ubicazione_beneBulk</code> l'Ubicazione di riferimento.
  * @param ubicazione_padre <code>Ubicazione_beneBulk</code> l'Ubicazione modello
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione
  *
  * @return sql <code>SQLBuilder</code> Risultato della selezione.
**/
public SQLBuilder selectNodoPadreByClause(
		UserContext userContext,
		Ubicazione_beneBulk ubicazione, 
		Ubicazione_beneBulk ubicazione_padre, 
		CompoundFindClause clauses) 
	throws ComponentException, it.cnr.jada.persistency.PersistencyException {

	it.cnr.jada.persistency.sql.SQLBuilder sql = (SQLBuilder)super.select(userContext,clauses,ubicazione);

	sql.addSQLClause("AND","CD_CDS",sql.EQUALS,	it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
	sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,	it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
	sql.addSQLClause("AND","LIVELLO",sql.LESS,"3");
	sql.addOrderBy("CD_UBICAZIONE");

	return sql;
}
}
