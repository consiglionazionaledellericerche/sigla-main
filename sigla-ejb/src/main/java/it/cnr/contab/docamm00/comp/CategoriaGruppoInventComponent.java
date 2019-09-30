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

package it.cnr.contab.docamm00.comp;


import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Insert the type's description here.
 * Creation date: (12/12/2001 1:14:21 PM)
 * @author: Roberto Peli
 */
public class CategoriaGruppoInventComponent  extends it.cnr.jada.comp.CRUDComponent 
	implements ICategoriaGruppoInventMgr,ICRUDMgr,Cloneable,Serializable {

/**
 * BeneServizioComponent constructor comment.
 */
public CategoriaGruppoInventComponent() {
	super();
}
/**
 * Esegue una operazione di modifica di un OggettoBulk.
 *
 * Pre-post-conditions:
 *
 * Nome: Non passa validazione applicativa
 * Pre: l'OggettoBulk non passa i criteri di validità applicativi per l'operazione
 *		di modifica
 * Post: Viene generata CRUDValidationException che descrive l'errore di validazione.
 *
 * Nome: Non passa validazione per violazione di vincoli della base di dati
 * Pre: l'OggettoBulk contiene qualche attributo nullo in corrispondenza di campi NOT_NULLABLE o qualche
 *			attributo stringa troppo lungo per i corrispondenti campi fisici.
 * Post: Viene generata una it.cnr.jada.comp.CRUDNotNullConstraintException o una 
 *	 		CRUDTooLargeConstraintException con la descrizione dell'errore
 *
 * Nome: Oggetto non trovato
 * Pre: l'OggettoBulk specificato non esiste.
 * Post: Viene generata una CRUDException con la descrizione dell'errore
 *
 * Nome: Oggetto scaduto
 * Pre: l'OggettoBulk specificato è stato modificato da altri utenti dopo la lettura
 * Post: Viene generata una CRUDException con la descrizione dell'errore
 *
 * Nome: Oggetto occupato
 * Pre: l'OggettoBulk specificato è bloccato da qualche altro utente.
 * Post: Viene generata una CRUDException con la descrizione dell'errore
 *
 * Nome: Tutti i controlli superati
 * Pre: Tutti i controlli precedenti superati
 * Post: l'OggettoBulk viene modificato fisicamente nella base dati e viene chiusa la transazione
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk che deve essere modificato
 * @return	l'OggettoBulk risultante dopo l'operazione di modifica.
 */	
public Bene_servizioBulk completaElementoVoceOf(UserContext userContext, Bene_servizioBulk beneServizio) throws it.cnr.jada.comp.ComponentException {

	if (beneServizio == null) return null;
		
	return beneServizio;
}
/** 
  *  Creazione di una Categoria/Gruppo inventariale
  *    PreCondition:
  *      Nessun errore rilevato
  *    PostCondition:
  *      La nuova categoria gruppo viene resa persistente e la sua associazione a voce finanziaria viene salvata
 */
public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

    Categoria_gruppo_inventBulk cgi= (Categoria_gruppo_inventBulk) bulk;
    cgi= (Categoria_gruppo_inventBulk) validaCgi(userContext, cgi);
    if(!cgi.getFl_gestione_inventario().booleanValue())
		cgi.setFl_ammortamento(Boolean.FALSE);
    super.creaConBulk(userContext, cgi);
    return cgi;
}

/**
 * Recupera la voce del piano finanziario corrispondente alla categoria/gruppo in processo (cgi)
 *
 * Pre-post-conditions:
 *
 * Nome: Tutti i controlli superati
 * Pre: Nessun errore
 * Post: viene ritornato l'elemento voce corrispondente alla categoria/gruppo cgi
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	cgi	la categoria gruppo in processo
 * @return	un'istanza di Elemento_voceBulk
 */
public Elemento_voceBulk findElementoVoce(UserContext aUC, Categoria_gruppo_inventBulk cgi)
		throws it.cnr.jada.comp.ComponentException {
	return null;
}
/**
 * Recupera il capitolo/articolo finanziario associato alla categoria/gruppo in processo
 *
 * Pre-post-conditions:
 *
 * Nome: Tutti i controlli superati
 * Pre: Nessun errore
 * Post: viene ritornata un'istanza di Voce_fBulk di appartenenza CDS gestione Spese codice specificato in associazione
 *       tra categoria inventariale e voceF
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	cgi	la categoria/gruppo
 * @return	il capitolo/articolo
 */
public Voce_fBulk findVoce_f(UserContext aUC,Categoria_gruppo_inventBulk cgi) throws ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
	return null;
}
/**
 * Recupera i figli della categoria gruppo corrente
 *
 * Pre-post-conditions:
 *
 * Nome: Tutti i controlli superati
 * Pre: Nessun errore
 * Post: viene ritornato un RemoteIteretor sulla collezione dei figli del bulk specificato
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk di cui determinare il parent
 * @return	il RemoteIterator sui figli
 */
public it.cnr.jada.util.RemoteIterator getChildren(UserContext context, OggettoBulk bulk) throws ComponentException{

	Categoria_gruppo_inventBulk cgi = (Categoria_gruppo_inventBulk)bulk;
	Categoria_gruppo_inventHome cgiHome = (Categoria_gruppo_inventHome)getHome(context,Categoria_gruppo_inventBulk.class);

	return iterator(
		context,
		cgiHome.selectChildrenFor(context,cgi),
		Categoria_gruppo_inventBulk.class,
		null);
}
/**
 * Recupera il parent della categoria gruppo corrente
 *
 * Pre-post-conditions:
 *
 * Nome: Tutti i controlli superati
 * Pre: Nessun errore
 * Post: l'OggettoBulk relativo al parent del bulk specificato viene ritornato
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk di cui determinare il parent
 * @return	l'OggettoBulk parent
 */
public OggettoBulk getParent(UserContext context, OggettoBulk bulk) throws ComponentException{

	try{
		Categoria_gruppo_inventBulk cgi = (Categoria_gruppo_inventBulk)bulk;
		Categoria_gruppo_inventHome cgiHome = (Categoria_gruppo_inventHome)getHome(context,Categoria_gruppo_inventBulk.class);

		return cgiHome.getParent(cgi);
		
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(bulk,ex);
	}catch(it.cnr.jada.persistency.IntrospectionException ex){
		throw handleException(bulk, ex);
	}
}
/**
 * Recupera i figli della categoria gruppo corrente
 *
 * Pre-post-conditions:
 *
 * Nome: Tutti i controlli superati
 * Pre: Nessun errore
 * Post: viene ritornato un RemoteIteretor sulla collezione dei figli del bulk specificato
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk di cui determinare il parent
 * @return	il RemoteIterator sui figli
 */
private boolean hasChildren(UserContext context, OggettoBulk bulk) throws ComponentException{

	Categoria_gruppo_inventBulk cgi = (Categoria_gruppo_inventBulk)bulk;
	Categoria_gruppo_inventHome cgiHome = (Categoria_gruppo_inventHome)getHome(context,Categoria_gruppo_inventBulk.class);

	try {
		return cgiHome.selectChildrenFor(context,cgi).executeExistsQuery(getConnection(context));
	} catch (java.sql.SQLException e) {
		throw handleException(cgi, e);
	}
}
/**
 * Prepara un OggettoBulk per la presentazione all'utente per una possibile
 * operazione di creazione.
 *
 * Pre-post-conditions:
 *
 * Nome: Tutti i controlli superati
 * Pre: 
 * Post: l'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato
 *			per una operazione di creazione
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk da preparare
 * @return	l'OggettoBulk preparato
 */	
public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	Categoria_gruppo_inventBulk cgi = (Categoria_gruppo_inventBulk) super.inizializzaBulkPerInserimento(userContext, bulk);
	return cgi;
}
/**
 * Prepara un OggettoBulk per la presentazione all'utente per una possibile
 * operazione di creazione.
 *
 * Pre-post-conditions:
 *
 * Nome: Tutti i controlli superati
 * Pre: 
 * Post: l'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato
 *			per una operazione di creazione
 * 
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk	l'OggettoBulk da preparare
 * @return	l'OggettoBulk preparato
 */
public OggettoBulk inizializzaBulkPerModifica(UserContext userContext, OggettoBulk bulk)
    throws it.cnr.jada.comp.ComponentException {

    Categoria_gruppo_inventBulk cgi = (Categoria_gruppo_inventBulk) super.inizializzaBulkPerModifica(userContext, bulk);
    Categoria_gruppo_inventHome catHome = (Categoria_gruppo_inventHome)getHome(userContext, Categoria_gruppo_inventBulk.class);
	try {
		cgi.setAssociazioneVoci(new  it.cnr.jada.bulk.BulkList(catHome.findAssociazioneVoci(userContext, cgi)));
		
		 for (Iterator dett = cgi.getAssociazioneVoci().iterator();dett.hasNext();){
			 Categoria_gruppo_voceBulk dettaglio = (Categoria_gruppo_voceBulk)dett.next();
			 dettaglio =(Categoria_gruppo_voceBulk)getHome(userContext,Categoria_gruppo_voceBulk.class).findByPrimaryKey(dettaglio);
		 }	    
		 getHomeCache(userContext).fetchAll(userContext,catHome);
	} catch (IntrospectionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (PersistencyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	    return cgi;
}
/**
 * Controllo che il bulk specificato rappresenta una categoria gruppo di ultimo livello.
 *  PreCondition: Il livello della categoria/gruppo = 0 o non ha figli
 *  PpostCondition: Ritorna true
 *
 * @param context it.cnr.jada.UserContext
 * @param bulk categoria/gruppo di cui stabilire se ultimo livello
 * @return boolean true se ultimo livello
 */
public boolean isLeaf(UserContext context, OggettoBulk bulk) throws ComponentException{

	//return false;
		
	return ((((Categoria_gruppo_inventBulk)bulk).getLivello().intValue()==0) ||
				!hasChildren(context, (Categoria_gruppo_inventBulk)bulk) );
//				(getChildren(context, bulk).countElements() == 0) );
}
/** 
  *  Modifica di una Categoria/Gruppo inventariale esistente
  *    PreCondition:
  *      Nessun errore rilevato
  *    PostCondition:
  *      La categoria gruppo viene resa persistente e la sua associazione a voce finanziaria viene aggiornata
 */
public OggettoBulk modificaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	Categoria_gruppo_inventBulk cgi = (Categoria_gruppo_inventBulk) bulk;
	cgi=(Categoria_gruppo_inventBulk) validaCgi(userContext,cgi);
	if(!cgi.getFl_gestione_inventario().booleanValue())
		cgi.setFl_ammortamento(Boolean.FALSE);

	cgi= (Categoria_gruppo_inventBulk) super.modificaConBulk(userContext, bulk);
    return cgi;
}

public it.cnr.jada.persistency.sql.SQLBuilder selectElemento_voceByClause(UserContext aUC,Categoria_gruppo_voceBulk cgi, Elemento_voceBulk voce, it.cnr.jada.persistency.sql.CompoundFindClause clauses) 
	throws ComponentException {
	it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC,voce).createSQLBuilder();
	sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));
	sql.openParenthesis("AND");
	sql.addClause("AND", "ti_appartenenza", sql.EQUALS, cgi.getTi_appartenenza());
	sql.addClause("AND", "ti_gestione", sql.EQUALS, cgi.getTi_gestione());
	sql.addClause("AND","fl_inv_beni_patr",sql.EQUALS,Boolean.TRUE);
	sql.addClause("OR","fl_inv_beni_comp",sql.EQUALS,Boolean.TRUE);
	sql.closeParenthesis();
	sql.addClause(clauses);
	return sql;
}
public it.cnr.jada.persistency.sql.SQLBuilder selectElemento_voceByClause(UserContext aUC,Categoria_gruppo_voceBulk cgi, Voce_fBulk voce, it.cnr.jada.persistency.sql.CompoundFindClause clauses) 
	throws ComponentException {
	it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC,voce).createSQLBuilder();


	sql.addClause("AND", "ti_appartenenza", sql.EQUALS, cgi.getTi_appartenenza());
	sql.addClause("AND", "ti_gestione", sql.EQUALS, cgi.getTi_gestione());
	sql.addClause("AND", "esercizio", sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));
	sql.addClause("AND","fl_inv_beni_patr",sql.EQUALS,Boolean.TRUE);
	sql.addClause("OR","fl_inv_beni_comp",sql.EQUALS,Boolean.TRUE);
	sql.addClause(clauses);
	return sql;
}
private OggettoBulk validaCgi(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	Categoria_gruppo_inventBulk cgi = (Categoria_gruppo_inventBulk) bulk;

	//cgi.setCd_categoria_gruppo(cgi.getCd_proprio());
	//cgi.setCd_voce(cgi.getVoce_f().getCd_voce());
	
	int livello = 0;
	String str = cgi.getCd_proprio();

	if (cgi.getNodoPadre()!= null && cgi.getNodoPadre().getCd_categoria_gruppo() != null && cgi.getNodoPadre().getLivello() != null){
		str = cgi.getNodoPadre().getCd_categoria_gruppo()+"."+str;
		livello = cgi.getNodoPadre().getLivello().intValue()+1;
	}

	cgi.setCd_categoria_gruppo(str);
	cgi.setLivello(new Integer(livello));

	if(!cgi.getFl_gestione_inventario().booleanValue())
		cgi.setFl_ammortamento(Boolean.FALSE);
	//+ per i figli...
	return cgi;
}

public java.util.List findAssVoceFList(UserContext aUC,Categoria_gruppo_inventBulk cgi) throws ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {

	if (cgi == null ) return null;
	it.cnr.jada.bulk.BulkHome home = getHome(aUC, Categoria_gruppo_voceBulk.class);
	it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();

	sql.addClause("AND", "cd_categoria_gruppo", sql.EQUALS, cgi.getCd_categoria_gruppo());
	sql.addClause("AND", "ti_appartenenza", sql.EQUALS,"D");
	sql.addClause("AND", "ti_gestione", sql.EQUALS,"S");
	sql.addClause("AND", "esercizio", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));
	
	return  home.fetchAll(sql);
}
}
