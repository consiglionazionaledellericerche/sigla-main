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

package it.cnr.contab.compensi00.comp;

import it.cnr.contab.anagraf00.tabter.bulk.*;

import java.io.Serializable;
import java.rmi.RemoteException;

import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;

public class ScaglioneComponent extends it.cnr.jada.comp.CRUDComponent implements IScaglioneMgr, Cloneable, Serializable {
    /**
     * TipoContributoRitenutaComponent constructor comment.
     */
    public ScaglioneComponent() {
        super();
    }

    /**
     * Completamento dello Scaglione
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: Completamento Scaglione
     * Pre: Viene richiesto il completamento dello Scaglione
     * Post: Viene caricato il Tipo CO/RI relativo allo scaglione e
     * tutti gli scaglioni aventi lo stesso Tipo CO/RI
     *
     * @param    userContext Lo UserContext che ha generato la richiesta
     * @param    testata        Lo scaglione da completare
     **/
    private void completaScaglione(UserContext userContext, ScaglioneBulk testata) throws ComponentException {

        // carico il Tipo Contributo Ritenuta
        loadTipoContributoRitenuta(userContext, testata);

        // carico gli Scaglioni associati al Tipo Contributo Ritenuta selezionato
        loadScaglioni(userContext, testata);
    }

    /**
     * Esegue una operazione di creazione di un OggettoBulk.
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Nessuno scaglione aggiunto alla lista
     * Pre:  E' stato richiesto l'inserimento di uno scaglione che NON è stato aggiunto alla lista
     * Post: Viene generata una eccezione con la descrizione dell'errore
     * - "Inserire gli scaglioni" -
     * <p>
     * Nome: Validazione scaglione superata
     * Pre:  E' stato richiesto l'inserimento di uno scaglione che supera la validazione
     * Post: Viene completato lo scaglione recuperando i dati di testata e viene
     * consentito l'inserimento dello stesso
     * <p>
     * Nome: Validazione NON superata
     * Pre:  E' stato richiesto l'inserimento di uno scaglione che NON supera la validazione
     * Post: Viene generata una eccezione con la descrizione dell'errore
     *
     * @param userContext lo UserContext che ha generato la richiesta
     * @param bulk        OggettoBulk il compenso che deve essere creato
     * @return l'OggettoBulk risultante dopo l'operazione di creazione.
     * <p>
     * Metodo di validzione privato:
     * validaScaglione(userContext, scaglione)
     **/
    public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {

        try {
            ScaglioneBulk testata = (ScaglioneBulk) bulk;

            if (testata.getScaglioni().isEmpty())
                throw new it.cnr.jada.comp.ApplicationException("Inserire gli scaglioni");

            for (java.util.Iterator i = testata.getScaglioni().iterator(); i.hasNext(); ) {
                ScaglioneBulk scaglione = (ScaglioneBulk) i.next();
                scaglione.setRegione(testata.getRegione());
                scaglione.setProvincia(testata.getProvincia());
                scaglione.setComune(testata.getComune());
                scaglione.setDt_inizio_validita(testata.getDt_inizio_validita());
                scaglione.setDt_fine_validita(testata.getDt_fine_validita());
                scaglione.setTi_anagrafico(testata.getTi_anagrafico());
                scaglione.setTi_ente_percipiente(testata.getTi_ente_percipiente());
                validaScaglione(userContext, scaglione);
                insertBulk(userContext, scaglione);
            }

            return (ScaglioneBulk) testata.getScaglioni().get(0);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(bulk, ex);
        }
    }

    /**
     * Cancellazione Scaglione
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: Cancellazione FISICA dello scaglione
     * Pre: Viene richiesta la cancellazione di uno scaglione con Data INIZIO Validita
     * superiore alla data odierna
     * Post: Cancello fisicamente il record in questione
     * <p>
     * Nome: Cancellazione LOGICA dello scaglione
     * Pre: Viene richiesta la cancellazione di uno scaglione con Data INIZIO Validita
     * inferiore alla data odierna e Data FINE Validita superiore alla data odierna
     * Post: Cancello logicamente lo scaglione selezionato impostanto come data Fine Validita
     * la data odierna
     **/
    public void eliminaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            ScaglioneBulk testata = (ScaglioneBulk) bulk;

            java.sql.Timestamp dataOdierna = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();

            // cancellazione logica dell'oggetto attivo
            for (java.util.Iterator i = testata.getScaglioni().iterator(); i.hasNext(); ) {
                ScaglioneBulk scag = (ScaglioneBulk) i.next();
                if (scag.getDt_inizio_validita().compareTo(dataOdierna) > 0) {
                    deleteBulk(userContext, scag);
                } else {
                    if (scag.getDt_fine_validita().compareTo(dataOdierna) > 0) {
                        scag.setDt_fine_validita(dataOdierna);
                        updateBulk(userContext, scag);
                    }
                }
            }

        } catch (javax.ejb.EJBException ex) {
            throw handleException(ex);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }

    }

    /**
     * Prepara un OggettoBulk per la presentazione all'utente per una possibile
     * operazione di modifica.
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Oggetto non esistente
     * Pre: L'OggettoBulk specificato non esiste.
     * Post: Viene generata una CRUDException con la descrizione dell'errore.
     * <p>
     * Nome: Tutti i controlli superati
     * Pre: L'OggettoBulk specificato esiste.
     * Post: Viene riletto l'OggettoBulk, inizializzato con tutti gli oggetti collegati e preparato
     * per l'operazione di presentazione e modifica nell'interfaccia visuale.
     * L'operazione di lettura viene effettuata con una FetchPolicy il cui nome è
     * ottenuto concatenando il nome della component con la stringa ".edit"
     *
     * @param    uc    lo UserContext che ha generato la richiesta
     * @param    bulk    l'OggettoBulk da preparare
     * @return l'OggettoBulk preparato
     * <p>
     * Metodo privato chiamato:
     * completaScaglione(userContext, scaglione)
     */
    public OggettoBulk inizializzaBulkPerModifica(UserContext userContext, OggettoBulk bulk) throws ComponentException {

        ScaglioneBulk scaglione = (ScaglioneBulk) super.inizializzaBulkPerModifica(userContext, bulk);
        completaScaglione(userContext, scaglione);

        return scaglione;

    }

    /**
     * Controllo esistenza altri periodi successivi a quello selezionato
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: L'intervallo in processo è l'ultimo intervallo esistente
     * Pre: La data di inizio validità dell'intervallo in processo >= della massima data di inizio di intervalli
     * Post: Viene ritornato TRUE
     * <p>
     * Nome: L'intervallo in processo non è l'ultimo intervallo esistente
     * Pre: La data di inizio validità dell'intervallo in processo < della massima data di inizio di intervalli
     * Post: Viene ritornato FALSE
     */
    public boolean isUltimoIntervallo(UserContext userContext, ScaglioneBulk scaglione) throws ComponentException {

        try {
            ScaglioneHome home = (ScaglioneHome) getHome(userContext, scaglione);

            it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
            java.sql.Timestamp maxData = (java.sql.Timestamp) home.findMax(scaglione, "dt_inizio_validita", null, false);
            if (maxData == null)
                return (true);

            if (!scaglione.getDt_inizio_validita().before(maxData))
                return (true);

            return (false);
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        } catch (it.cnr.jada.bulk.BusyResourceException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Carica tutti gli scaglioni associati al Tipo Contributo Ritenuta selezionato
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: Caricamento Scaglioni
     * Pre: E' stato selezionato un Tipo Contributo Ritenuta Valido
     * Post: Vengono caricati tutti gli scaglioni associati al Tipo CO/RI selezionato
     *
     * @param    userContext    Lo userContext che ha generato la richiesta
     * @param    testata        La testata in cui settare i dettagli
     */
    private void loadScaglioni(UserContext userContext, ScaglioneBulk testata) throws ComponentException {

        try {

            ScaglioneHome home = (ScaglioneHome) getHome(userContext, ScaglioneBulk.class);

            java.util.List l = home.caricaScaglioni(testata);
            for (java.util.Iterator i = l.iterator(); i.hasNext(); ) {
                ScaglioneBulk scaglione = (ScaglioneBulk) i.next();
                scaglione.setContributo_ritenuta(testata.getContributo_ritenuta());
            }

            testata.setScaglioni(l);

        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Caricamento del Tipo CO/RI
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: Tipo CO/RI esistente e valido in data odierna
     * Pre: Viene richiesto il caricamento del Tipo CO/RI
     * Post: Viene caricato il Tipo CO/RI e aggiornato lo Scaglione
     * <p>
     * Nome: Tipo CO/RI esistente ma NON valido in data odierna
     * Pre: Viene richiesto il caricamento del Tipo CO/RI
     * Post: Viene caricato il Tipo CO/RI e aggiornato lo Scaglione
     * <p>
     * Nome: Tipo CO/RI INESISTENTE (perchè cancellato fisicamente)
     * Pre: Viene richiesto il caricamento del Tipo CO/RI
     * Post: Viene generata un'eccezione con la descrizione dell'errore
     * Il Tipo Contributo/Ritenuta \"" + testata.getCd_contributo_ritenuta() +
     * "\" associato allo scaglione selezionato NON esiste
     *
     * @param    userContext Lo UserContext che ha generato la richiesta
     * @param    testata        Lo scaglione da completare
     **/
    private void loadTipoContributoRitenuta(UserContext userContext, ScaglioneBulk testata) throws ComponentException {

        try {
            Tipo_contributo_ritenutaHome home = (Tipo_contributo_ritenutaHome) getHome(userContext, Tipo_contributo_ritenutaBulk.class);
            Tipo_contributo_ritenutaBulk cori = home.findTipoCORIValido(testata.getCd_contributo_ritenuta(), home.getServerDate());

            // se il tipo contributo ritenuta selezionato non è più valido
            // carico il tipo co/ri senza clausola di validita
            if (cori == null)
                cori = home.findTipoCORIValido(testata.getCd_contributo_ritenuta(), null);

            if (cori == null)
                throw new it.cnr.jada.comp.ApplicationException("Il Tipo Contributo/Ritenuta \"" + testata.getCd_contributo_ritenuta() + "\" associato allo scaglione selezionato NON esiste");

            testata.setContributo_ritenuta(cori);

        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Richiesta di modifica di uno Scaglione
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: Modifica di intervallo avente Data INIZIO Validita futura
     * Pre: Viene richiesta una modifica di uno scaglione avente la data inizio validita superiore alla data odierna
     * Post: Viene aggiornato lo scaglione in processo
     * <p>
     * Nome: Modifica di uno scaglione avente Data FINE Validita < alla data odierna
     * Pre: Viene richiesta una modifica di uno scaglione avente la data fine validita precedente alla data odierna
     * Post: Viene sollevata un'eccezione
     * <p>
     * Nome: Modifica di intervallo avente la data INIZIO validita <= alla data odierna
     * Pre: Viene richiesta una modifica di uno scaglione avente la data inizio validita precedente alla data odierna
     * Post: La data di fine validità dello scaglione corrente viene posta = data odierna
     * Viene creato il nuovo scaglione con data di inizio validità = alla data odierna + 1
     **/
    public OggettoBulk modificaConBulk(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            super.validaModificaConBulk(userContext, bulk);

            ScaglioneBulk scaglione = (ScaglioneBulk) bulk;
            ScaglioneHome home = (ScaglioneHome) getHome(userContext, scaglione);

            java.sql.Timestamp dataOdierna = home.getServerDate();

            // Intervallo futuro
            if (scaglione.getDt_inizio_validita().after(dataOdierna)) {
                updateBulk(userContext, scaglione);
            } else {

                ScaglioneBulk current = (ScaglioneBulk) home.findByPrimaryKey(scaglione, true);
                current.setDt_fine_validita(dataOdierna);
                updateBulk(userContext, current);
                scaglione.setDt_inizio_validita(it.cnr.contab.compensi00.docs.bulk.CompensoBulk.incrementaData(dataOdierna));
                insertBulk(userContext, scaglione);
            }

            return scaglione;

        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
     * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sul Comune
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: Richiesta di ricerca di un Comune
     * Pre: E' stata generata la richiesta di ricerca di un Comune
     * Post: Viene restituito l'SQLBuilder per filtrare i Comuni
     * appartenenti ad una determinata provincia
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param scaglione   l'OggettoBulk che rappresenta il contesto della ricerca.
     * @param comune      l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
     *                    costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
     * @param clauses     L'albero logico delle clausole da applicare alla ricerca
     * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri della query
     **/
    public SQLBuilder selectComuneByClause(UserContext userContext, ScaglioneBulk scaglione, ComuneBulk comune, CompoundFindClause clauses) throws ComponentException {

        ComuneHome comuneHome = (ComuneHome) getHome(userContext, ComuneBulk.class);
        SQLBuilder sql = comuneHome.createSQLBuilder();
        sql.openParenthesis("AND");
        sql.addSQLClause("AND", "DT_CANC", sql.ISNULL, null);
        sql.addSQLClause("OR", "DT_CANC", sql.GREATER, it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
        sql.closeParenthesis();
        sql.addSQLClause("AND", "TI_ITALIANO_ESTERO", sql.EQUALS, NazioneBulk.ITALIA);
        sql.addClause(clauses);
        return sql;
    }

    /**
     * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
     * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sul Contributo/Ritenuta
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: Richiesta di ricerca di un Contributo/Ritenuta
     * Pre: E' stata generata la richiesta di ricerca di un Contributo/Ritenuta
     * Post: Viene restituito l'SQLBuilder per filtrare i Tipi Contributo/Ritenuta validi in data odierna
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param scaglione   l'OggettoBulk che rappresenta il contesto della ricerca.
     * @param cori        l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
     *                    costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
     * @param clauses     L'albero logico delle clausole da applicare alla ricerca
     * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri della query
     **/
    public SQLBuilder selectContributo_ritenutaByClause(UserContext userContext, ScaglioneBulk scaglione, Tipo_contributo_ritenutaBulk cori, CompoundFindClause clauses) throws ComponentException {
        try {
            Tipo_contributo_ritenutaHome coriHome = (Tipo_contributo_ritenutaHome) getHome(userContext, Tipo_contributo_ritenutaBulk.class);
            SQLBuilder sql = coriHome.createSQLBuilder();
            coriHome.addClauseValidita(sql, coriHome.getServerDate());
            sql.addClause(clauses);

            return sql;
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
     * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Provincia
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: Richiesta di ricerca di una Provincia
     * Pre: E' stata generata la richiesta di ricerca di una Provincia
     * Post: Viene restituito l'SQLBuilder per filtrare le Provincie
     * appartenenti ad una determinata regione
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param scaglione   l'OggettoBulk che rappresenta il contesto della ricerca.
     * @param provincia   l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
     *                    costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
     * @param clauses     L'albero logico delle clausole da applicare alla ricerca
     * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri della query
     **/
    public SQLBuilder selectProvinciaByClause(UserContext userContext, ScaglioneBulk scaglione, ProvinciaBulk provincia, CompoundFindClause clauses) throws ComponentException {

        ProvinciaHome provHome = (ProvinciaHome) getHome(userContext, ProvinciaBulk.class);

        SQLBuilder sql = provHome.createSQLBuilder();
        CompoundFindClause clause = CompoundFindClause.or(
                new SimpleFindClause("cd_provincia", SQLBuilder.EQUALS, "*"),
                new SimpleFindClause("cd_regione", SQLBuilder.EQUALS, scaglione.getCd_regione()));
        sql.addClause(clause);
        sql.addClause(clauses);

        return sql;
    }

    /**
     * Validazione dello scaglione
     * <p>
     * Pre-post-conditions
     * <p>
     * Nome: Controlli di validazione del periodo di inizio/fine validita'
     * del nuovo record superati
     * Pre: Validazione periodo inizio/fine superata (data inizio validita del nuovo
     * record deve essere maggiore del record piu' recente presente in tabella)
     * Post: Consente l'inserimento del record e l'aggiornamento della data di fine
     * validita del penultimo record
     * <p>
     * Nome: Validazioni non superate
     * Pre: Controlli su periodo NON OK
     * Post: Non inserisco l'oggetto
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param bulk        l'OggettoBulk (scaglione) da validare
     **/
    public void validaScaglione(UserContext userContext, OggettoBulk bulk) throws ComponentException {

        try {

            ScaglioneHome home = (ScaglioneHome) getHome(userContext, bulk);
            ScaglioneBulk scaglione = (ScaglioneBulk) bulk;
            java.sql.Timestamp dataOdierna = CompensoBulk.getDataOdierna(home.getServerTimestamp());

            SQLBuilder sql = home.createSQLBuilder();
            sql.addClause("AND", "cd_contributo_ritenuta", sql.EQUALS, scaglione.getCd_contributo_ritenuta());
            sql.addClause("AND", "cd_regione", sql.EQUALS, scaglione.getCd_regione());
            sql.addClause("AND", "cd_provincia", sql.EQUALS, scaglione.getCd_provincia());
            sql.addClause("AND", "pg_comune", sql.EQUALS, scaglione.getPg_comune());
            sql.addClause("AND", "ti_anagrafico", sql.EQUALS, scaglione.getTi_anagrafico());
            sql.addClause("AND", "ti_ente_percipiente", sql.EQUALS, scaglione.getTi_ente_percipiente());
            sql.addClause("AND", "dt_inizio_validita", sql.LESS_EQUALS, scaglione.getDt_inizio_validita());
            sql.addClause("AND", "dt_fine_validita", sql.GREATER_EQUALS, scaglione.getDt_inizio_validita());
            sql.addBetweenClause("AND", "im_inferiore", scaglione.getIm_inferiore(), scaglione.getIm_superiore());
//	   	sql.addClause("AND","im_superiore",sql.GREATER_EQUALS,scaglione.getIm_inferiore());   	   	

            java.util.List l = home.fetchAll(sql);
            if (!l.isEmpty()) {
                throw new it.cnr.jada.comp.ApplicationException("Esistono scaglioni attivi nell'intervallo selezionato.");
            }

        } catch (Throwable e) {
            throw handleException(e);
        }
    }

	@Override
	protected Query select(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {

		if (compoundfindclause == null) {
			if (oggettobulk != null)
				compoundfindclause = oggettobulk.buildFindClauses(null);
		} else {
			compoundfindclause = CompoundFindClause.and(compoundfindclause, oggettobulk.buildFindClauses(Boolean.FALSE));
		}
		ScaglioneHome home = (ScaglioneHome) getHome(usercontext, oggettobulk, "COMUNE");
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(compoundfindclause);
		sql.generateJoin("comune", "COMUNE");
		return sql;
	}
}
