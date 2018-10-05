package it.cnr.contab.config00.comp;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.EnteBulk;
import it.cnr.contab.config00.sto.bulk.EnteHome;
import it.cnr.contab.doccont00.core.bulk.V_disp_cassa_cdsBulk;
import it.cnr.contab.doccont00.core.bulk.V_disp_cassa_cnrBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * Classe che ridefinisce alcune operazioni di CRUD su EsercizioBulk
 */

public class EsercizioComponent extends it.cnr.jada.comp.CRUDComponent implements IEsercizioMgr, Cloneable, Serializable {


    //@@<< CONSTRUCTORCST
    public EsercizioComponent() {
//>>

//<< CONSTRUCTORCSTL
        /*Default constructor*/
//>>

//<< CONSTRUCTORCSTT

    }

    /**
     * Aggiorna la cassa iniziale dell'esercizio successivo a quello che si sta chiudendo
     * <p>
     * Nome: aggiorna cassa iniziale
     * Pre: l'utente ha richiesto la chiusura di un esercizio contabile per un cds
     * il sistema ha verificato che l'esercizio è chiudibile (metodo 'verificaChiudibilitaEsercizio')
     * Post: il sistema imposta l'importo della cassa iniziale dell'esercizio successivo a quello che viene chiuso
     * <p>
     * 09/02/2004 - NON PIU' UTILIZZATO
     * L'aggiornamento della cassa iniziale del CDS in esercizio successivo (Ente o no) viene fatta nel verificaChiudibilitaEsercizio
     *
     * @param userContext contesto
     * @param esercizio   esercizio da chiudere
     * @return void
     */

    protected void aggiornaIm_cassa_iniziale(UserContext userContext, EsercizioBulk esercizio) throws it.cnr.jada.comp.ComponentException {
        try {

            EsercizioHome home = (EsercizioHome) getHome(userContext, EsercizioBulk.class);
            BigDecimal imDispCassa = findIm_disp_cassa(userContext, esercizio);
            EsercizioBulk esSuccessivo = (EsercizioBulk) home.findAndLock(new EsercizioBulk(esercizio.getCd_cds(), new Integer(esercizio.getEsercizio().intValue() + 1)));
            if (esSuccessivo == null)
                throw new ApplicationException("Esercizio contabile " + esSuccessivo.getEsercizio() + " per Cds " + esSuccessivo.getCd_cds() + " non trovato");

            esSuccessivo.setIm_cassa_iniziale(imDispCassa);
            esSuccessivo.setUser(userContext.getUser());
            updateBulk(userContext, esSuccessivo);

        } catch (Throwable e) {
            throw handleException(esercizio, e);
        }

    }

    /**
     * Richiama la stored procedure che apre i piani di gestione per l'esercizio selezionata
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Apertura PdG da parte dell'ENTE
     * Pre:  La richiesta di aprire i piani di gestione è stata fatta dal CDS ENTE e l'esercizio è in stato STATO_INIZIALE = 'I'
     * Post: Viene richiamata la stored procedure che aggiorna a STATO_PDG_APERTO = 'G' lo stato del PDG ENTE
     * <p>
     * Nome: Apertura PDG da parte di CDS non ENTE da stato = a STATO_INIZIALE
     * Pre:	 La richiesta di aprire i propri piani di gestione è stata fatta da un CDS non ENTE per un esercizio in STATO_INIZIALE = 'I'
     * Pre:  Lo stato dell'esercizio per l'ENTE è in STATO_PDG_APERTO = 'G'
     * Post: Viene richiamata la stored procedure che apre tutti i piani di gestione dei cdr appartenenti al CDS e viene aggiornato
     * lo stato dell'esercizio selezionato dal valore STATO_INIZIALE al valore STATO_PDG_APERTO per il CDS in processo
     * <p>
     * Nome: Apertura PDG da parte di CDS non ENTE da stato DIVERSO da STATO_INIZIALE
     * Pre:	 La richiesta di aprire i propri piani di gestione è stata fatta da un CDS non ENTE per un esercizio in stato DIVERSO da STATO_INIZIALE
     * Pre:  Lo stato dell'esercizio per l'ENTE è in STATO_PDG_APERTO = 'G'
     * Post: Viene richiamata la stored procedure che apre tutti i piani di gestione dei cdr appartenenti al CDS
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    esercizio l' EsercizioBulk per il quale e' necessario aprire i pianio di gestione
     * @return L'EsercizioBulk risultante dopo l'operazione di modifica stato
     */

    public EsercizioBulk apriPianoDiGestione(UserContext userContext, EsercizioBulk esercizio) throws it.cnr.jada.comp.ComponentException {
        try {
            EsercizioBulk esercizioEnte = getEsercizioEnte(userContext, esercizio);

            if (!esercizio.getCd_cds().equals(esercizioEnte.getCd_cds()) && esercizioEnte.getSt_apertura_chiusura().equals(esercizio.STATO_INIZIALE))
                throw handleException(new ApplicationException("Il piano di gestione non è stato ancora aperto dall'Ente"));

            EsercizioHome home = (EsercizioHome) getHome(userContext, EsercizioBulk.class);
            home.callApriPdGProcedure(esercizio);

            if (esercizio.getSt_apertura_chiusura().equals(esercizio.STATO_INIZIALE)) {
                esercizio.setSt_apertura_chiusura(esercizio.STATO_PDG_APERTO);
                updateBulk(userContext, esercizio);
            }
            return esercizio;
        } catch (Throwable e) {
            throw handleException(esercizio, e);
        }

    }

    /**
     * Esegue una operazione di modifica dello stato di un EsercizioBulk.
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Modifica stato Esercizio iniziale
     * Pre:  La richiesta di modifica dello stato di un Esercizio con stato 'iniziale' è stata generata
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente ("Non e' possibile cambiare lo stato iniziale");
     * <p>
     * Nome: Modifica stato Esercizio a aperto - OK
     * Pre:  La richiesta di modifica dello stato di un Esercizio da 'Piano di gestione aperto' a 'aperto' è stata generata
     * e lo stato dell'esercizio precedente per il cds corrente e' chiuso ( provvisoriamente o definitivamente) e
     * non esistono cds con (esercizio - 2) in stato diverso da chiuso (provvisoriamente o definitivamente)
     * Post: Lo stato dell'Esercizio viene aggiornato ad 'aperto'
     * <p>
     * Nome: Modifica stato Esercizio a aperto - Errore 1
     * Pre:  La richiesta di modifica dello stato di un Esercizio da 'iniziale' a 'aperto' è stata generata e lo stato
     * dell'esercizio precedente per il cds corrente non e' chiuso ( provvisoriamente o definitivamente)
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente ("L'esercizio precedente non è stato chiuso");
     * <p>
     * Nome: Modifica stato Esercizio a aperto - Errore 2
     * Pre:  La richiesta di modifica dello stato di un Esercizio da 'iniziale' a 'aperto' è stata generata e esiste almeno
     * un cds per il quale l'esercizio del secondo anno precedente (esercizio -2) e' in stato non chiuso
     * provvisoriamente o definitivamente
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente ("Esistono esercizi non chiusi per l'anno XXXX");
     * <p>
     * Nome: Modifica stato Esercizio a aperto - Esercizio economico CdS in stato chiuso
     * Pre:  E' stato riochiesto di portare lo stato dell'esercizio ad 'Aperto', ma l'esercizio
     * economico del CdS risulta chiuso.
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente ("L'esercizio economico del CdS risulta in stato chiuso.");
     * <p>
     * <p>
     * Nome: Modifica stato Esercizio a aperto - Esercizio Ente in stato chiuso
     * Pre:  E' stato riochiesto di portare lo stato dell'esercizio ad 'Aperto', ma l'esercizio
     * dell'Ente risulta chiuso.
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente ("L'esercizio dell'Ente risulta in stato chiuso.");
     * <p>
     * Nome: Modifica stato Esercizio chiuso
     * Pre:  La richiesta di modifica dello stato di un Esercizio con stato 'chiuso' è stata generata
     * la procedura che verifica la chiudibilità dell'esercizio ritorna che l'esercizio non è chiudibile
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente ("Non e' possibile cambiare lo stato");
     * <p>
     * Nome: Modifica stato Esercizio a chiuso
     * Pre:  La richiesta di modifica dello stato di un Esercizio a 'chiuso'
     * è stata generata
     * la procedura che verifica la chiudibilità dell'esercizio ritorna che l'esercizio è chiudibile
     * Post: Lo stato dell'Esercizio e' stato aggiornato e l'importo della cassa iniziale dell'esercizio successivo
     * è stato impostato dalla stessa procedura che verifica lachiudibilità dell'esercizio
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    esercizio l' EsercizioBulk il cui stato deve essere modificato
     * @return L'EsercizioBulk risultante dopo l'operazione di modifica stato
     */

    public EsercizioBulk cambiaStatoConBulk(UserContext userContext, EsercizioBulk esercizio) throws it.cnr.jada.comp.ComponentException {
        try {
            EsercizioHome esercizioHome = (EsercizioHome) getHome(userContext, esercizio);
/*
		if ( esercizio.getSt_apertura_chiusura().equals(esercizio.STATO_CHIUSO_DEF) ||
			 esercizio.getSt_apertura_chiusura().equals(esercizio.STATO_INIZIALE))
			throw new it.cnr.jada.comp.ApplicationException( "Non è possibile cambiare lo stato");
*/
            if (esercizio.getSt_apertura_chiusura().equals(esercizio.STATO_INIZIALE))
                throw new it.cnr.jada.comp.ApplicationException("Non è possibile cambiare lo stato");

            String next = (String) EsercizioBulk.getProssimoStato().get(esercizio.getSt_apertura_chiusura());

            // pdg aperto --> aperto
            if (esercizio.STATO_PDG_APERTO.equals(esercizio.getSt_apertura_chiusura()) &&
                    next.equals(esercizio.STATO_APERTO)) {
//			EsercizioBulk esercizioPrecedente = esercizioHome.findEsercizioPrecedente( esercizio );
//			if ( esercizioPrecedente != null && !esercizioPrecedente.isChiuso() )
//				throw new it.cnr.jada.comp.ApplicationException( "L'esercizio precedente non è stato chiuso." );
                if (!esercizioHome.verificaEsercizi2AnniPrecedenti(esercizio))
                    throw new it.cnr.jada.comp.ApplicationException("Esistono esercizi non chiusi per l'anno " + new Integer(esercizio.getEsercizio().intValue() - 2));
            }


            // chiuso --> aperto - Esercizio ENTE chiuso
            if (esercizio.STATO_CHIUSO_DEF.equals(esercizio.getSt_apertura_chiusura()) &&
                    esercizio.STATO_CHIUSO_DEF.equals(getEsercizioEnte(userContext, esercizio).getSt_apertura_chiusura())) {

                throw new it.cnr.jada.comp.ApplicationException("Attenzione: l'esercizio dell'Ente risulta in stato chiuso.");
            }

            // chiuso --> aperto
            if (esercizio.STATO_CHIUSO_DEF.equals(esercizio.getSt_apertura_chiusura()) &&
                    next.equals(esercizio.STATO_APERTO)) {
                EsercizioBulk esercizioSuccessivo = esercizioHome.findEsercizioSuccessivo(esercizio);
                if (esercizioSuccessivo != null && esercizioSuccessivo.isChiuso())
                    throw new it.cnr.jada.comp.ApplicationException("E' possibile mettere in stato aperto solo l'ultimo esercizio chiuso.");
            }

            // chiuso --> aperto - Esercizio economico del CdS chiuso
            if (esercizio.STATO_CHIUSO_DEF.equals(esercizio.getSt_apertura_chiusura()) &&
                    isEsercizioCdSChiuso(userContext))
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: l'esercizio economico del CdS risulta in stato chiuso.");


            // aperto --> chiuso
            if (esercizio.STATO_APERTO.equals(esercizio.getSt_apertura_chiusura()) &&
                    next.equals(esercizio.STATO_CHIUSO_DEF)) {
                EsercizioBulk esercizioPrecedente = esercizioHome.findEsercizioPrecedente(esercizio);
                if (esercizioPrecedente != null && !esercizioPrecedente.isChiuso())
                    throw new it.cnr.jada.comp.ApplicationException("E' impossibile mettere in stato chiuso perchè l'esercizio precedente non è ancora chiuso.");
                verificaChiudibilitaEsercizio(userContext, esercizio);
//			aggiornaIm_cassa_iniziale( userContext, esercizio );
            }

            esercizio.setSt_apertura_chiusura(next);

            updateBulk(userContext, esercizio);

            return esercizio;

        } catch (Throwable e) {
            throw handleException(esercizio, e);
        }
    }

    /**
     * Esegue una operazione di creazione di un EsercizioBulk.
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Creazione dell'esercizio per l'ente
     * Pre:  La richiesta di creazione dell'esercizio per l'ente è stata generata
     * Post: L'esercizio dell'ente e' stato creato con stato 'iniziale'
     * <p>
     * Nome: Creazione di un esercizio consecutivo ad un esercizio già creato per cds ente
     * Pre:  La richiesta di creazione di un esercizio consecutivo ad un esercizio già creato in precedenza
     * è stata generata
     * Post: Il nuovo Esercizio e' stato creato con stato 'iniziale'
     * <p>
     * Nome: Creazione di un esercizio consecutivo ad un esercizio già creato per cds diverso da ente e lo stesso esercizio esiste per l'ente
     * Pre:  La richiesta di creazione di un esercizio consecutivo ad un esercizio già creato per cds diverso da ente e lo stesso esercizio esiste per l'ente
     * è stata generata
     * Post: Il nuovo Esercizio e' stato creato con stato 'iniziale'
     * <p>
     * Nome: Creazione di un esercizio consecutivo ad un esercizio già creato per cds diverso da ente e lo stesso esercizio non esiste per l'ente
     * Pre:  La richiesta di creazione di un esercizio consecutivo ad un esercizio già creato per cds diverso da ente e lo stesso esercizio non esiste per l'ente
     * è stata generata
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     * <p>
     * Nome: Creazione di un esercizio non consecutivo agli esercizi già creati
     * Pre:  La richiesta di creazione di un esercizio non consecutivo agli esercizi già creat in precedenza
     * è stata generata
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk l' EsercizioBulk che deve essere creato
     * @return L'EsercizioBulk risultante dopo l'operazione di creazione.
     */


    public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {

            EsercizioBulk esercizio = (EsercizioBulk) bulk;
            EsercizioHome esercizioHome = (EsercizioHome) getHome(userContext, esercizio);

            getEsercizioEnte(userContext, esercizio);

            it.cnr.jada.persistency.sql.SQLBuilder sql = esercizioHome.createSQLBuilder();
            //cerco se la chiave e' già presente nel db
            sql.addClause("AND", "cd_cds", sql.EQUALS, esercizio.getCd_cds());
            sql.addClause("AND", "esercizio", sql.EQUALS, esercizio.getEsercizio());
            // se non e' presente la chiave nel db
            if (sql.executeCountQuery(getConnection(userContext)) == 0) {
                sql = esercizioHome.createSQLBuilder();
                sql.addClause("AND", "cd_cds", sql.EQUALS, esercizio.getCd_cds());

                if (sql.executeCountQuery(getConnection(userContext)) != 0) {
                    EsercizioBulk esercizioPrecedente = esercizioHome.findEsercizioPrecedente(esercizio);
                    if (esercizioPrecedente == null)
                        throw new it.cnr.jada.comp.ApplicationException("Non esiste l'esercizio precedente");
                }
            }
            super.creaConBulk(userContext, esercizio);

            creaEsplVociEsercizio(userContext, esercizio);

            return bulk;
        } catch (Throwable e) {
            throw handleException(bulk, e);
        }
    }

    /**
     * Aggiorna il PDC finanziario per inserimento dell'esercizio per un dato CDS
     * <p>
     * Viene invocato il metodo creaEsplVociEsercizio() di CNRCTB001 per l'aggiornamento del PDC finanziario
     * <p>
     * -- Esplosione della struttura organizzativa nel nuovo anno
     * -- (Funzione di aggiornamento di VOCE_F per la parte strutturale legata a CDS/UO e CDR validi nell'esercizio in definizione)
     * -- Funzione NON invocabile da trigger AI su ESERCIZIO perchè utilizza le viste V_XXXX_VALIDX sulla struttura organizzativa
     * --
     * --  aEs -> codice esercizio
     * -- aCdCDS -> codice CDS
     * -- aUser codice utente che effettua la modifica
     * <p>
     * Creation date: (05/04/2001 14:07:09)
     *
     * @param aUC     user CONTEXT
     * @param aEsBulk bulk esercizio contabile in inserimento
     */

    private void creaEsplVociEsercizio(UserContext aUC, EsercizioBulk aEsBulk) throws it.cnr.jada.comp.ComponentException {
        try {
            Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(aUC, aEsBulk.getEsercizio());
            if (!parCnr.getFl_nuovo_pdg()) {
                LoggableStatement cs = new LoggableStatement(getConnection(aUC),
                        "{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                                + "CNRCTB001.creaEsplVociEsercizio(?,?,?)}", false, this.getClass());
                try {
                    cs.setObject(1, aEsBulk.getEsercizio());
                    cs.setString(2, aEsBulk.getCd_cds());
                    cs.setString(3, null); // Viene utilizzato l'utcr dell'esercizio appena inserito
                    cs.executeQuery();
                } catch (Throwable e) {
                    throw handleException(e);
                } finally {
                    cs.close();
                }
            }
        } catch (SQLException e) {
            throw handleException(e);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Impedisce la cancellazione di un EsercizioBulk.
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Cancellazione Esercizio
     * Pre:  La richiesta di cancellazione di un Esercizio è stata generata
     * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
     * visualizzare all'utente ("Non e' possibile cancellare un Esercizio");
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk l' EsercizioBulk
     */


    public void eliminaConBulk(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        throw handleException(new ApplicationException("Non e' possibile cancellare un Esercizio"));
    }

    protected BigDecimal findIm_disp_cassa(UserContext userContext, EsercizioBulk esercizio) throws it.cnr.jada.comp.ComponentException {
        try {
            EnteBulk aEnte = (EnteBulk) getHome(userContext, EnteBulk.class).findAll().get(0);
            BigDecimal imDispCassa;

            // Se l'esercizio in processo è per l'ENTE ritorno con successo
            if (esercizio.getCd_cds().equals(aEnte.getCd_unita_organizzativa())) {
                SQLBuilder sql = getHome(userContext, V_disp_cassa_cnrBulk.class).createSQLBuilder();
                sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, esercizio.getEsercizio());
                List result = getHome(userContext, V_disp_cassa_cnrBulk.class).fetchAll(sql);
                if (result == null || result.size() == 0)
                    throw new ApplicationException(" Disponibilità di cassa dell'Ente per esercizio " + esercizio.getEsercizio() + " non è stata trovata");
                imDispCassa = ((V_disp_cassa_cnrBulk) result.get(0)).getIm_disponibilta_cassa();
            } else //per cds
            {
                SQLBuilder sql = getHome(userContext, V_disp_cassa_cdsBulk.class).createSQLBuilder();
                sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, esercizio.getEsercizio());
                sql.addSQLClause("AND", "cd_cdS", sql.EQUALS, esercizio.getCd_cds());
                List result = getHome(userContext, V_disp_cassa_cdsBulk.class).fetchAll(sql);
                if (result == null || result.size() == 0)
                    throw new ApplicationException(" Disponibilità di cassa del Cds " + esercizio.getCd_cds() + " per esercizio " + esercizio.getEsercizio() + " non è stata trovata");
                imDispCassa = ((V_disp_cassa_cdsBulk) result.get(0)).getIm_disponibilita_cassa();
            }
            return imDispCassa;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Estrae il bulk dell'esercizio dell'ente lockandolo
     * Nel caso non trovi l'ente o l'esercizio dell'ente nel caso esercizio non corrisponda all'ente, ritorna un'eccezione
     *
     * @param userContext contesto
     * @param esercizio   esercizio in processo
     * @return l'esercizio per l'ente corrispondente all'anno specificato in esercizio
     */

    protected EsercizioBulk getEsercizioEnte(UserContext userContext, EsercizioBulk esercizio) throws it.cnr.jada.comp.ComponentException {
        try {

            EsercizioHome home = (EsercizioHome) getHome(userContext, EsercizioBulk.class);

            EnteHome aEnteHome = (EnteHome) getHome(userContext, EnteBulk.class);
            EnteBulk aEnte = new EnteBulk();
            aEnte.setCd_tipo_unita(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE);
            List aL = aEnteHome.find(aEnte);

            if (aL.size() == 0)
                throw handleException(new ApplicationException("CDS ENTE non trovato!"));

            aEnte = (EnteBulk) aL.get(0);

            // Se l'esercizio in processo è per l'ENTE ritorno con successo
            if (esercizio.getCd_cds().equals(aEnte.getCd_unita_organizzativa()))
                return esercizio;

            EsercizioBulk esercizioEnte = new EsercizioBulk();

            esercizioEnte.setEsercizio(esercizio.getEsercizio());
            esercizioEnte.setCd_cds(aEnte.getCd_unita_organizzativa());

            aL = home.find(esercizioEnte);

            if (aL.size() == 0)
                throw handleException(new ApplicationException("Esercizio dell'ente non ancora definito!"));

            esercizioEnte = (EsercizioBulk) aL.get(0);

            lockBulk(userContext, esercizioEnte);

            return esercizioEnte;
        } catch (Throwable e) {
            throw handleException(esercizio, e);
        }

    }

    /**
     * Inizializza un esercizio per l'inserimento
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Inizializzazione per inserimento
     * Pre:  La richiesta di inizializzazione di un EsercizioBulk per inserimento e' stata generata
     * Post: L'esercizio e' stato inizializzato con il Cds padre dell'unita' organizzativa di scrivania
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk l' EsercizioBulk
     */

    public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            EsercizioBulk esercizio = (EsercizioBulk) super.inizializzaBulkPerInserimento(userContext, bulk);
            CdsBulk cds = (CdsBulk) getHome(userContext, CdsBulk.class).findByPrimaryKey(new CdsBulk(esercizio.getCd_cds()));
            esercizio.setCds(cds);
            return esercizio;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Inizializza un esercizio per la modifica
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Inizializzazione per modifica
     * Pre:  La richiesta di inizializzazione di un EsercizioBulk per modifica e' stata generata
     * Post: L'esercizio e' stato inizializzato con il Cds padre dell'unita' organizzativa di scrivania
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk l' EsercizioBulk
     */

    public OggettoBulk inizializzaBulkPerModifica(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            EsercizioBulk esercizio = (EsercizioBulk) super.inizializzaBulkPerModifica(userContext, bulk);
            CdsBulk cds = (CdsBulk) getHome(userContext, CdsBulk.class).findByPrimaryKey(new CdsBulk(esercizio.getCd_cds()));
            esercizio.setCds(cds);
            return esercizio;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Inizializza un esercizio per ricerca
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Inizializzazione per ricerca
     * Pre:  La richiesta di inizializzazione di un EsercizioBulk per ricerca e' stata generata
     * Post: L'esercizio e' stato inizializzato con il Cds padre dell'unita' organizzativa di scrivania
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk l' EsercizioBulk
     */

    public OggettoBulk inizializzaBulkPerRicerca(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            EsercizioBulk esercizio = (EsercizioBulk) super.inizializzaBulkPerRicerca(userContext, bulk);
            CdsBulk cds = (CdsBulk) getHome(userContext, CdsBulk.class).findByPrimaryKey(new CdsBulk(esercizio.getCd_cds()));
            esercizio.setCds(cds);
            return esercizio;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Inizializza un esercizio per ricerca libera
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Inizializzazione per ricerca libera
     * Pre:  La richiesta di inizializzazione di un EsercizioBulk per ricerca libera e' stata generata
     * Post: L'esercizio e' stato inizializzato con il Cds padre dell'unita' organizzativa di scrivania
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk l' EsercizioBulk
     */

    public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            EsercizioBulk esercizio = (EsercizioBulk) super.inizializzaBulkPerRicercaLibera(userContext, bulk);
            CdsBulk cds = (CdsBulk) getHome(userContext, CdsBulk.class).findByPrimaryKey(new CdsBulk(esercizio.getCd_cds()));
            esercizio.setCds(cds);
            return esercizio;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Controlla se l'esercizio economico del CdS di scrivania e' aperto
     * <p>
     * Nome: Controllo chiusura esercizio
     * Pre:  E' stata richiesta la modifica dello stato di un esercizio
     * Post: Viene chiamata una stored procedure che restituisce
     * -		'Y' se il campo stato della tabella CHIUSURA_COEP vale C
     * -		'N' altrimenti
     * Se l'esercizio e' chiuso e' impossibile proseguire
     *
     * @param userContext <code>UserContext</code>
     * @return boolean : TRUE se stato = C
     * FALSE altrimenti
     */
    private boolean isEsercizioCdSChiuso(UserContext userContext) throws ComponentException {
        LoggableStatement cs = null;
        String status = null;

        try {
            cs = new LoggableStatement(getConnection(userContext),
                    "{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                            + "CNRCTB200.isChiusuraCoepDef(?,?)}", false, this.getClass());

            cs.registerOutParameter(1, java.sql.Types.VARCHAR);
            cs.setObject(2, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
            cs.setObject(3, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));

            cs.executeQuery();

            status = new String(cs.getString(1));

            if (status.compareTo("Y") == 0)
                return true;

        } catch (java.sql.SQLException ex) {
            throw handleException(ex);
        }

        return false;
    }
// 09/02/2004 - Aggiunto il parametro USER per aggiornamenti su
// cassa iniziale esercizio successivo

    private void verificaChiudibilitaEsercizio(UserContext userContext, EsercizioBulk esercizio) throws ComponentException {
        try {
            /* CNRCTB009.checkEsercizioChiusura(?, ?);) */
            LoggableStatement cs = new LoggableStatement(getHome(userContext, EsercizioBulk.class).getConnection(),
                    "{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                            + "CNRCTB009.checkEsercizioChiusura(?,?,?)}", false, this.getClass());
            try {
                cs.setObject(1, esercizio.getEsercizio());
                cs.setString(2, esercizio.getCd_cds());
                cs.setString(3, esercizio.getUser());
                cs.executeQuery();
            } catch (SQLException e) {
                throw handleException(e);
            } finally {
                cs.close();
            }
        } catch (SQLException e) {
            throw handleException(e);
        }
    }

    public boolean isEsercizioChiuso(UserContext userContext) throws ComponentException {
        try {
            EsercizioHome home = (EsercizioHome) getHome(userContext, EsercizioBulk.class);
            return home.isEsercizioChiuso(userContext);
        } catch (PersistencyException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Estrae il bulk dell'esercizio dell'ultimo anno aperto del cds a partire dall'esercizio di scrivania
     * Nel caso non trovi nulla restituisce valore vuoto
     *
     * @param userContext contesto
     * @return l'esercizio del centro di spesa corrispondente all'ultimo anno aperto
     */
    public EsercizioBulk getLastEsercizioOpen(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            EsercizioHome home = (EsercizioHome) getHome(userContext, EsercizioBulk.class);
            EsercizioBulk lastEsercizioOpen = null;

            for (Iterator esercizi = ((EsercizioHome) getHome(userContext, EsercizioBulk.class)).findEserciziSuccessivi(new EsercizioBulk(CNRUserContext.getCd_cds(userContext), CNRUserContext.getEsercizio(userContext))).iterator(); esercizi.hasNext(); ) {
                EsercizioBulk esercizio = (EsercizioBulk) esercizi.next();
                if (lastEsercizioOpen == null || esercizio.getEsercizio().compareTo(lastEsercizioOpen.getEsercizio()) == 1)
                    lastEsercizioOpen = esercizio;
            }

            return lastEsercizioOpen;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public boolean isEsercizioAperto(UserContext userContext) throws ComponentException {
        try {
            EsercizioHome home = (EsercizioHome) getHome(userContext, EsercizioBulk.class);
            return home.isEsercizioAperto(userContext, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext), it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
        } catch (PersistencyException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Estrae il bulk dell'esercizio dell'esercizio di scrivania del cds di scrivania
     * Nel caso non trovi nulla restituisce valore vuoto
     *
     * @param userContext contesto
     * @return l'esercizio del centro di spesa corrispondente all'esercizio di scrivania
     */
    public EsercizioBulk getEsercizio(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            EsercizioHome home = (EsercizioHome) getHome(userContext, EsercizioBulk.class);
            final Persistent byPrimaryKey = home.findByPrimaryKey(new EsercizioBulk(CNRUserContext.getCd_cds(userContext), CNRUserContext.getEsercizio(userContext)));
            getHomeCache(userContext).fetchAll(userContext);
            return (EsercizioBulk) byPrimaryKey;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }
}
