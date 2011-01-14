package it.cnr.contab.docamm00.comp;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

import java.io.Serializable;

/**
 * Insert the type's description here.
 * Creation date: (12/12/2001 1:14:21 PM)
 * @author: Roberto Peli
 */
public class BeneServizioComponent  extends it.cnr.jada.comp.CRUDComponent 
	implements IBeneServizioMgr,ICRUDMgr,Cloneable,Serializable {

/**
 * BeneServizioComponent constructor comment.
 */
public BeneServizioComponent() {
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

	//ctrl
    if (beneServizio == null)
        return null;

    Categoria_gruppo_inventBulk categoriaGruppo= beneServizio.getCategoria_gruppo();
    if (categoriaGruppo != null) {
        /*it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk ev = new it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk(
        								categoriaGruppo.getCd_voce(),
        								it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),
        								categoriaGruppo.getTi_appartenenza(),
        								categoriaGruppo.getTi_gestione());*/
        it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk ev= new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk();

        try {
            categoriaGruppo.setVoce_f((it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk) getHome(userContext, ev).findByPrimaryKey(ev));            
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(beneServizio, e);
        }
    }
    return beneServizio;
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

	Bene_servizioBulk beneServizio = (Bene_servizioBulk) super.inizializzaBulkPerInserimento(userContext, bulk);
	
	Categoria_gruppo_inventBulk categoriaGruppo = beneServizio.getCategoria_gruppo();
	if (categoriaGruppo == null)
		categoriaGruppo = new Categoria_gruppo_inventBulk();
	categoriaGruppo.setVoce_f(new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk());
	return beneServizio;
}
public it.cnr.jada.persistency.sql.SQLBuilder selectCategoria_gruppoByClause(UserContext aUC,Bene_servizioBulk bene, Categoria_gruppo_inventBulk cgi, it.cnr.jada.persistency.sql.CompoundFindClause clauses) 
	throws ComponentException {
	it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC,cgi).createSQLBuilder();

	sql.addSQLClause("AND","LIVELLO",sql.EQUALS,"1");

	sql.addClause(clauses);
	return sql;
}
public it.cnr.jada.persistency.sql.SQLBuilder selectVoce_ivaByClause(UserContext aUC,Bene_servizioBulk bene, it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk voce_iva, it.cnr.jada.persistency.sql.CompoundFindClause clauses) 
	throws ComponentException {
	it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC,voce_iva).createSQLBuilder();

	sql.addClause(clauses);
	sql.openParenthesis("AND");
	sql.addSQLClause("AND","TI_APPLICAZIONE",sql.EQUALS,voce_iva.ENTRAMBE);
	sql.addSQLClause("OR","TI_APPLICAZIONE",sql.EQUALS,voce_iva.ACQUISTI);
	sql.closeParenthesis();

	
	return sql;
}
}
