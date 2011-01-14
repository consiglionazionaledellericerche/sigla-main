package it.cnr.contab.docamm00.comp;

import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

import java.io.Serializable;

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
	Categoria_gruppo_inventBulk categoriaGruppo = beneServizio.getCategoria_gruppo();
	//it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk ev = new it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk(
									//categoriaGruppo.getAss_voce_f().getCd_voce(),
									//it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),
									//categoriaGruppo.getAss_voce_f().getTi_appartenenza(),
									//categoriaGruppo.getAss_voce_f().getTi_gestione());
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk ev = new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk(
									categoriaGruppo.getAss_voce_f().getCd_voce(),
									it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),
									categoriaGruppo.getAss_voce_f().getTi_appartenenza(),
									categoriaGruppo.getAss_voce_f().getTi_gestione());
	
	try {
		categoriaGruppo.setVoce_f((it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk)getHome(userContext, ev).findByPrimaryKey(ev));
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(beneServizio, e);
	}
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
    //cgi.getAss_voce_f().setCd_voce(cgi.getVoce_f().getCd_voce());
    cgi.getAss_voce_f().setVoce_f(cgi.getVoce_f());
    cgi.getAss_voce_f().setCd_voce(cgi.getVoce_f().getCd_elemento_voce());
    cgi.getAss_voce_f().setCategoria_gruppo(cgi);
    cgi.getAss_voce_f().setCd_categoria_gruppo(cgi.getCd_categoria_gruppo());
    cgi.getAss_voce_f().setTi_appartenenza("D");
    cgi.getAss_voce_f().setTi_gestione("S");
    cgi.getAss_voce_f().setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));

    cgi= (Categoria_gruppo_inventBulk) super.creaConBulk(userContext, bulk);
    cgi.getAss_voce_f().setUser(bulk.getUser());
    cgi.getAss_voce_f().setUtcr(bulk.getUtcr());
    cgi.getAss_voce_f().setUtuv(bulk.getUtuv());

    try {
        insertBulk(userContext, cgi.getAss_voce_f());
    } catch (it.cnr.jada.persistency.PersistencyException ex) {
        throw new it.cnr.jada.comp.ApplicationException("Impossibile creare l'associazione con la voce del piano");
    }


    return cgi;
}
private Categoria_gruppo_voceBulk findAssVoceF(UserContext aUC,Categoria_gruppo_inventBulk cgi) throws ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {

	//if (cgi == null || cgi.getVoce_f()==null) return null;
	it.cnr.jada.bulk.BulkHome home = getHome(aUC, Categoria_gruppo_voceBulk.class);
	it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();

	if (cgi.getNodoPadre()!=null){
		sql.addClause("AND", "cd_categoria_gruppo", sql.EQUALS, cgi.getNodoPadre().getCd_categoria_gruppo());
		sql.addClause("AND", "ti_appartenenza", sql.EQUALS,"D");
		sql.addClause("AND", "ti_gestione", sql.EQUALS,"S");
		sql.addClause("AND", "esercizio", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));	
	}else{
		sql.addClause("AND", "cd_categoria_gruppo", sql.EQUALS, cgi.getCd_categoria_gruppo());
		sql.addClause("AND", "ti_appartenenza", sql.EQUALS,"D");
		sql.addClause("AND", "ti_gestione", sql.EQUALS,"S");
		sql.addClause("AND", "esercizio", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));
	}
	it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
        if (!broker.next())
            return null;
 
	Categoria_gruppo_voceBulk cgv=(Categoria_gruppo_voceBulk) broker.fetch(Categoria_gruppo_voceBulk.class);
	broker.close();

	return cgv;
	
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

    //if (cgi == null) return null;
    //if (cgi.getVoce_f()== null ) return null;
    //Elemento_voceBulk ev = new Elemento_voceBulk(
    //cgi.getVoce_f().getCd_elemento_voce(),
    //cgi.getAss_voce_f().getEsercizio(),
    //cgi.getAss_voce_f().getTi_appartenenza(),
    //cgi.getAss_voce_f().getTi_gestione());
    //try{
    //ev=(Elemento_voceBulk)getHome(userContext, ev).findByPrimaryKey(ev);
    //} catch (it.cnr.jada.persistency.PersistencyException e) {
    //throw handleException(cgi, e);
    //}
    //return ev;

    try {
        //cgi.setAss_voce_f(findAssVoceF(aUC, cgi));
        
        //cgi.getAss_voce_f().setTi_appartenenza("D");
        //cgi.getAss_voce_f().setTi_gestione("S");
        //cgi.getAss_voce_f().setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));
		cgi.setAss_voce_f(findAssVoceF(aUC, cgi));
		if(cgi.getAss_voce_f()==null )
		throw new ApplicationException("Non esiste l'associazione categoria/gruppo/voce per l'anno di scrivania");
        it.cnr.jada.bulk.BulkHome homeVoce = getHome(aUC, Elemento_voceBulk.class);
        it.cnr.jada.persistency.sql.SQLBuilder sqlVoce = homeVoce.createSQLBuilder();

        sqlVoce.addClause("AND", "cd_elemento_voce", sqlVoce.EQUALS, cgi.getVoce_f().getCd_elemento_voce());
        sqlVoce.addClause("AND", "ti_appartenenza", sqlVoce.EQUALS, cgi.getVoce_f().getTi_appartenenza());
        sqlVoce.addClause("AND", "ti_gestione", sqlVoce.EQUALS, cgi.getVoce_f().getTi_gestione());
        sqlVoce.addClause("AND", "esercizio", sqlVoce.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));

        it.cnr.jada.persistency.Broker brokerVoce = homeVoce.createBroker(sqlVoce);
        if (!brokerVoce.next())
            return null;

        Elemento_voceBulk voce = (Elemento_voceBulk) brokerVoce.fetch(Elemento_voceBulk.class);
        brokerVoce.close();

        return voce;        
    } catch (it.cnr.jada.persistency.PersistencyException e){
	    throw handleException(cgi,e);
    } catch (it.cnr.jada.persistency.IntrospectionException e){
	    throw handleException(cgi,e);
    }
    
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

	//if (cgi == null || cgi.getVoce_f()==null) return null;

	cgi.setAss_voce_f(findAssVoceF(aUC,cgi));
	cgi.getAss_voce_f().setTi_appartenenza("D");
    cgi.getAss_voce_f().setTi_gestione("S");
    cgi.getAss_voce_f().setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));
	
	it.cnr.jada.bulk.BulkHome homeVoce = getHome(aUC, Voce_fBulk.class);
	it.cnr.jada.persistency.sql.SQLBuilder sqlVoce = homeVoce.createSQLBuilder();

	sqlVoce.addClause("AND", "cd_voce", sqlVoce.EQUALS, cgi.getAss_voce_f().getVoce_f().getCd_elemento_voce());
	sqlVoce.addClause("AND", "ti_appartenenza", sqlVoce.EQUALS, cgi.getAss_voce_f().getVoce_f().getTi_appartenenza());
	sqlVoce.addClause("AND", "ti_gestione", sqlVoce.EQUALS, cgi.getAss_voce_f().getVoce_f().getTi_gestione());
	sqlVoce.addClause("AND", "esercizio", sqlVoce.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));	

	it.cnr.jada.persistency.Broker brokerVoce = homeVoce.createBroker(sqlVoce);
        if (!brokerVoce.next())
            return null;
 
	Voce_fBulk voce=(Voce_fBulk) brokerVoce.fetch(Voce_fBulk.class);
	brokerVoce.close();


	return voce;
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

	cgi.setVoce_f(new Elemento_voceBulk());
	//appartenenza CDS
	cgi.setAss_voce_f(new Categoria_gruppo_voceBulk());
	cgi.getAss_voce_f().setVoce_f(new Elemento_voceBulk());
	cgi.getAss_voce_f().setTi_appartenenza("D");
	cgi.getAss_voce_f().setTi_gestione("S");
	cgi.getAss_voce_f().setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	
	
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
    //cgi.setVoce_f(new Voce_fBulk());
    ////appartenenza CDS
    //cgi.setAss_voce_f(new Categoria_gruppo_voceBulk());
    //cgi.getAss_voce_f().setVoce_f(cgi.getVoce_f());
    //try {

        
        cgi.setVoce_f(findElementoVoce(userContext,cgi));

    //} catch (it.cnr.jada.persistency.PersistencyException e) {
        //throw handleException(cgi, e);
    //} catch (it.cnr.jada.persistency.IntrospectionException e) {
        //throw handleException(cgi, e);
    //}

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
    cgi.getAss_voce_f().setUser(bulk.getUser());
    cgi.getAss_voce_f().setUtcr(bulk.getUtcr());
    cgi.getAss_voce_f().setUtuv(bulk.getUtuv());

    try {
        updateBulk(userContext, cgi.getAss_voce_f());
    } catch (it.cnr.jada.persistency.PersistencyException ex) {
        throw new it.cnr.jada.comp.ApplicationException("Impossibile creare l'associazione con la voce del piano");
    }


    return cgi;
}
public it.cnr.jada.persistency.sql.SQLBuilder selectNodo_padreByClause(UserContext aUC,Categoria_gruppo_inventBulk cgi, Categoria_gruppo_inventBulk padre, it.cnr.jada.persistency.sql.CompoundFindClause clauses) 
	throws ComponentException {
	it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC,padre).createSQLBuilder();


	sql.addTableToHeader("CATEGORIA_GRUPPO_VOCE");
    sql.addSQLJoin("CATEGORIA_GRUPPO_VOCE.CD_CATEGORIA_GRUPPO", "CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO");
	sql.addClause("AND", "CATEGORIA_GRUPPO_VOCE.ti_appartenenza", sql.EQUALS, cgi.getAss_voce_f().getTi_appartenenza());
	sql.addClause("AND", "CATEGORIA_GRUPPO_VOCE.ti_gestione", sql.EQUALS, cgi.getAss_voce_f().getTi_gestione());
	sql.addClause("AND", "CATEGORIA_GRUPPO_VOCE.esercizio", sql.EQUALS, cgi.getAss_voce_f().getEsercizio());	
	sql.addClause("AND", "CATEGORIA_GRUPPO_VOCE.cd_voce_f", sql.EQUALS, cgi.getAss_voce_f().getCd_voce());	
	
	sql.addClause(clauses);
	return sql;
}
public it.cnr.jada.persistency.sql.SQLBuilder selectVoce_fByClause(UserContext aUC,Categoria_gruppo_inventBulk cgi, Elemento_voceBulk voce, it.cnr.jada.persistency.sql.CompoundFindClause clauses) 
	throws ComponentException {
	it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC,voce).createSQLBuilder();


	sql.addClause("AND", "ti_appartenenza", sql.EQUALS, cgi.getAss_voce_f().getTi_appartenenza());
	sql.addClause("AND", "ti_gestione", sql.EQUALS, cgi.getAss_voce_f().getTi_gestione());
	sql.addClause("AND", "esercizio", sql.EQUALS, cgi.getAss_voce_f().getEsercizio());	
	
	sql.addClause(clauses);
	return sql;
}
public it.cnr.jada.persistency.sql.SQLBuilder selectVoce_fByClause(UserContext aUC,Categoria_gruppo_inventBulk cgi, Voce_fBulk voce, it.cnr.jada.persistency.sql.CompoundFindClause clauses) 
	throws ComponentException {
	it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC,voce).createSQLBuilder();


	sql.addClause("AND", "ti_appartenenza", sql.EQUALS, cgi.getAss_voce_f().getTi_appartenenza());
	sql.addClause("AND", "ti_gestione", sql.EQUALS, cgi.getAss_voce_f().getTi_gestione());
	sql.addClause("AND", "esercizio", sql.EQUALS, cgi.getAss_voce_f().getEsercizio());	
	
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
}
