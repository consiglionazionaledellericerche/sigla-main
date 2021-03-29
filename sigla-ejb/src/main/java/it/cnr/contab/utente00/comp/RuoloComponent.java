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

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDDuplicateKeyException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;


/**
 * Classe che ridefinisce alcune operazioni di CRUD su RuoloBulk
 */

public class RuoloComponent extends it.cnr.jada.comp.CRUDComponent implements ICRUDMgr, it.cnr.contab.utenze00.comp.IRuoloMgr, Cloneable, Serializable {


    public RuoloComponent() {

    }

    /**
     * Esegue una operazione di creazione di un RuoloBulk.
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Creazione di Ruolo non esistente
     * Pre:  La richiesta di creazione di un Ruolo non ancora definito è stata generata
     * Post: Il Ruolo e' stato inserito nel database
     * <p>
     * Nome: Creazione di Ruolo già esistente
     * Pre:  La richiesta di creazione di un Ruolo con chiave già definita è stata generata
     * Post: Viene generata una ComponentException per segnalare all'utente l'impossibilità di effettuare la creazione
     * del ruolo
     *
     * @param    userContext    lo UserContext che ha generato la richiesta
     * @param    bulk il RuoloBulk che deve essere creato
     * @return il RuoloBulk risultante dopo l'operazione di creazione.
     */

    public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            super.creaConBulk(userContext, bulk);
            return bulk;
        } catch (CRUDDuplicateKeyException e) {
            throw handleException(new ApplicationException("Inserimento impossibile: Chiave duplicata"));
        } catch (Throwable e) {
            throw handleException(bulk, e);
        }
    }

    /**
     * Esegue l'inizializzazione di una nuova istanza di RuoloBulk impostando l'elenco di accessi disponibili per
     * l'utente corrente
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Inizializzazione bulk
     * Pre:  L'inizializzazione di un RuoloBulk per eventuale inserimento e' stata generata
     * Post: Il RuoloBulk viene aggiornato con l'elenco delle istanze di AccessoBulk disponibili per l'utente corrente
     * <p>
     * Nome: Gestore non trovato
     * Pre:  L'utente che ha generato la richiesta non esiste
     * Post: Viene generata una ComponentException con detail l'ApplicationException con il messaggio
     * da visualizzare all'utente
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param bulk        l'istanza di Ruolobulk che deve essere inizializzata
     * @return il RuoloBulk inizializzato
     */


    public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            RuoloBulk ruolo = (RuoloBulk) bulk;
            RuoloHome ruoloHome = (RuoloHome) getHomeCache(userContext).getHome(RuoloBulk.class);

            UtenteBulk gestore = (UtenteBulk) getHomeCache(userContext).getHome(UtenteBulk.class).findByPrimaryKey(new UtenteKey(bulk.getUser()));

            if (gestore == null)
                throw new it.cnr.jada.comp.ApplicationException("Utente Gestore non definito");

            ruolo.setGestore(gestore);

            if (!"*".equals(gestore.getCd_cds_configuratore())) {
                CdsBulk cds = (CdsBulk) getHome(userContext, CdsBulk.class).findByPrimaryKey(new CdsBulk(gestore.getCd_cds_configuratore()));
                ruolo.setCds(cds);
            }

            // carica accessi disponibili
            ruolo.setAccessi_disponibili(ruoloHome.findAccessi_disponibili(gestore, ruolo,null));

            return ruolo;

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * Esegue l'inizializzazione di una nuova istanza di RuoloBulk impostando l'elenco di Accessi già assegnati
     * e l'elenco di Accessi ancora disponibili
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Inizializzazione bulk
     * Pre:  L'inizializzazione di un RuoloBulk per eventuale modifica e' stata generata
     * Post: Il RuoloBulk viene aggiornato con l'elenco delle istanze di AccessoBulk ancora disponibili e con
     * l'elenco di istanze di Utente_unita_ruoloBulk gia' assegnate al ruolo
     * <p>
     * Nome: Gestore non trovato
     * Pre:  L'utente che ha generato la richiesta non esiste
     * Post: Viene generata una ComponentException con detail l'ApplicationException con il messaggio
     * da visualizzare all'utente
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param bulk        l'istanza di Ruolobulk che deve essere inizializzata
     * @return il RuoloBulk inizializzato
     */


    public OggettoBulk inizializzaBulkPerModifica(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            RuoloBulk ruolo = (RuoloBulk) super.inizializzaBulkPerModifica(userContext, bulk);
            RuoloHome ruoloHome = (RuoloHome) getHomeCache(userContext).getHome(ruolo.getClass());

            UtenteBulk gestore = (UtenteBulk) getHomeCache(userContext).getHome(UtenteBulk.class).findByPrimaryKey(new UtenteKey(bulk.getUser()));
            if (gestore == null)
                throw new it.cnr.jada.comp.ApplicationException("Utente Gestore non definito");
            ruolo.setGestore(gestore);
            //		carica ruolo_accesso
            Collection result = ruoloHome.findRuolo_accessi(ruolo);
            for (java.util.Iterator i = result.iterator(); i.hasNext(); ) {
                Ruolo_accessoBulk ra = (Ruolo_accessoBulk) i.next();
                ra.setAccesso((AccessoBulk)  getHomeCache(userContext).getHome(AccessoBulk.class).findByPrimaryKey(new AccessoKey(ra.getAccesso().getCd_accesso())));
                ruolo.addToRuolo_accessi(ra);
            }

            // carica accessi disponibili
            ruolo.setAccessi_disponibili(ruoloHome.findAccessi_disponibili(gestore, ruolo,null));

            return ruolo;

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public RuoloBulk cercaAccessiDisponibili(UserContext userContext, RuoloBulk ruolo, CompoundFindClause compoundfindclause) throws it.cnr.jada.comp.ComponentException {
        try {
            RuoloHome ruoloHome = (RuoloHome) getHomeCache(userContext).getHome(ruolo.getClass());
            UtenteBulk gestore = (UtenteBulk) getHomeCache(userContext).getHome(UtenteBulk.class).findByPrimaryKey(new UtenteKey(ruolo.getUser()));
            if (gestore == null)
                throw new it.cnr.jada.comp.ApplicationException("Utente Gestore non definito");
            // carica accessi disponibili
            ruolo.setAccessi_disponibili(ruoloHome.findAccessi_disponibili(gestore, ruolo, compoundfindclause));

            return ruolo;
        } catch (Exception e) {
            throw handleException(e);
        }
    }
    /**
     * Aggiunge una clausola a tutte le operazioni di ricerca eseguite su RuoloBulk, per visualizzare solo
     * i ruoli definiti per il Cds del gestore
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Richiesta di ricerca RuoloBulk da parte di un gestore con cds '*'
     * Pre:  E' stata generata la richiesta di ricerca di un RuoloBulk da parte di un gestore abilitato a tutti i cds
     * Post: Viene restituito il SQLBuilder contenente solo l'elenco delle clausole selezionate dall'utente
     * <p>
     * Nome: Richiesta di ricerca RuoloBulk da parte di un gestore con cds diverso da '*'
     * Pre:  E' stata generata la richiesta di ricerca di un RuoloBulk da parte di un gestore abilitato ad un solo cds
     * Post: Viene restituito il SQLBuilder contenente l'elenco delle clausole selezionate dall'utente e la clausola che
     * il ruolo appartenga al cds a cui l'utente e' abilitato o che il ruolo non abbia il CDS valorizzato
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param clauses     clausole di ricerca gia' specificate dall'utente
     * @param ruolo      istanza di RuoloBulk che deve essere utilizzata per la ricerca
     * @return il SQLBuilder con la clausola aggiuntiva sul gestore
     */


    protected Query select(UserContext userContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, OggettoBulk ruolo) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException {

        try {
            UtenteBulk gestore = (UtenteBulk) getHome(userContext, UtenteBulk.class).findByPrimaryKey(
                    new UtenteKey(((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getUser()));

            SQLBuilder sql = getHomeCache(userContext).getHome(RuoloBulk.class).createSQLBuilder();

            it.cnr.jada.persistency.sql.CompoundFindClause bulkClauses = ruolo.buildFindClauses(null);
            sql.addClause(bulkClauses);

            if (!"*".equals(gestore.getCd_cds_configuratore())) {
                sql.addClause("AND", "cd_cds", it.cnr.jada.persistency.sql.SQLBuilder.EQUALS, gestore.getCd_cds_configuratore());
                sql.addClause("OR", "cd_cds", sql.ISNULL, null);
            }
            sql.addClause(clauses);
            return sql;
        } catch (Exception e) {
            throw handleException(e);
        }


    }

    /**
     * Aggiunge una clausola a tutte le operazioni di ricerca eseguite su CdsBulk
     * <p>
     * Pre-post-conditions:
     * <p>
     * Nome: Default
     * Pre:  E' stata generata la richiesta di ricerca di CDS
     * Post: Se il gestore del ruolo ha CDS uguale a '*' vengono restituiti tutti i Cds validi per l'esercizio di scrivania,
     * altrimenti viene resituito solo il Cds dell'utente
     *
     * @param userContext lo userContext che ha generato la richiesta
     * @param clauses     clausole di ricerca gia' specificate dall'utente
     * @param ruolo      istanza di CdsBulk che deve essere utilizzata per la ricerca
     * @return il SQLBuilder con la clausola aggiuntiva sul gestore
     */


    public it.cnr.jada.persistency.sql.SQLBuilder selectCdsByClause(UserContext userContext, RuoloBulk ruolo, CdsBulk cds, it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = ((CdsHome) getHome(userContext, cds.getClass(), "V_CDS_VALIDO")).createSQLBuilderIncludeEnte();
        sql.addClause(clauses);
        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
        if (!"*".equals(ruolo.getGestore().getCd_cds_configuratore()))
            sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, ruolo.getGestore().getCd_cds_configuratore());
        return sql;

    }

    public boolean isAbilitatoApprovazioneBilancio(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            SQLBuilder sql = null;
            SQLBroker broker = null;

            UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));

            if (utente.isSupervisore() && utente.getCd_ruolo_supervisore() != null) {
                RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente.getCd_ruolo_supervisore())));
                if (ruolo != null && ruolo.getTipo() != null) {
                    SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                    sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                    sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                    sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");

                    sql2.openParenthesis("AND");
                    sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_APPROVA_BILANCIO);
                    sql2.addSQLClause("OR", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_FUNZIONI_DIRETTORE);
                    sql2.closeParenthesis();

                    if (sql2.executeExistsQuery(getConnection(userContext)))
                        return true;
                }
            } else {
                sql = getHome(userContext, Utente_unita_ruoloBulk.class).createSQLBuilder();
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UTENTE", SQLBuilder.EQUALS, CNRUserContext.getUser(userContext));
                broker = getHome(userContext, Utente_unita_ruoloBulk.class).createBroker(sql);
                Utente_unita_ruoloBulk utente_unita_ruolo;
                while (broker.next()) {
                    utente_unita_ruolo = (Utente_unita_ruoloBulk) broker.fetch(Utente_unita_ruoloBulk.class);
                    RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente_unita_ruolo.getCd_ruolo())));
                    if (ruolo != null && ruolo.getTipo() != null) {

                        SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                        sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                        //sql2.addSQLClause("AND","TIPO_RUOLO.FL_APPROVA_BIL",SQLBuilder.EQUALS,"Y");
                        sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                        sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");

                        sql2.openParenthesis("AND");
                        sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_APPROVA_BILANCIO);
                        sql2.addSQLClause("OR", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_FUNZIONI_DIRETTORE);
                        sql2.closeParenthesis();

                        if (sql2.executeExistsQuery(getConnection(userContext))) {

                            broker.close();
                            return true;
                        }
                    }
                    //			else
                    //				return false;
                }
                broker.close();
            }
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }


        return (false);
    }

    public boolean isCapoCommessa(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            SQLBuilder sql = null;
            SQLBroker broker = null;

            UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));

            if (utente.isSupervisore() && utente.getCd_ruolo_supervisore() != null) {
                RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente.getCd_ruolo_supervisore())));
                if (ruolo != null && ruolo.getTipo() != null) {
                    SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                    sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                    if (sql2.executeExistsQuery(getConnection(userContext)))
                        return true;
                }
            } else {
                sql = getHome(userContext, Utente_unita_ruoloBulk.class).createSQLBuilder();
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UTENTE", SQLBuilder.EQUALS, CNRUserContext.getUser(userContext));
                broker = getHome(userContext, Utente_unita_ruoloBulk.class).createBroker(sql);
                Utente_unita_ruoloBulk utente_unita_ruolo;
                while (broker.next()) {
                    utente_unita_ruolo = (Utente_unita_ruoloBulk) broker.fetch(Utente_unita_ruoloBulk.class);
                    RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente_unita_ruolo.getCd_ruolo())));
                    if (ruolo != null && ruolo.getTipo() != null) {
                        SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                        sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                        if (sql2.executeExistsQuery(getConnection(userContext))) {

                            broker.close();
                            return true;
                        }
                    }
                    //			else
                    //				return false;
                }
                broker.close();
            }
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }


        return (false);
    }

    public boolean isAmministratoreInventario(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            SQLBuilder sql = null;
            SQLBroker broker = null;

            UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));

            if (utente.isSupervisore() && utente.getCd_ruolo_supervisore() != null) {
                RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente.getCd_ruolo_supervisore())));
                if (ruolo != null && ruolo.getTipo() != null) {
                    SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                    sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                    sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                    sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                    sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_AGGIORNA_INVENTARIO);

                    if (sql2.executeExistsQuery(getConnection(userContext)))
                        return true;
                }
            } else {
                sql = getHome(userContext, Utente_unita_ruoloBulk.class).createSQLBuilder();
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UTENTE", SQLBuilder.EQUALS, CNRUserContext.getUser(userContext));
                broker = getHome(userContext, Utente_unita_ruoloBulk.class).createBroker(sql);
                Utente_unita_ruoloBulk utente_unita_ruolo;
                while (broker.next()) {
                    utente_unita_ruolo = (Utente_unita_ruoloBulk) broker.fetch(Utente_unita_ruoloBulk.class);
                    RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente_unita_ruolo.getCd_ruolo())));
                    if (ruolo != null && ruolo.getTipo() != null) {
                        SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                        sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                        //sql2.addSQLClause("AND","TIPO_RUOLO.FL_AGGIORNA_INV",SQLBuilder.EQUALS,"Y");
                        sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                        sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                        sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_AGGIORNA_INVENTARIO);

                        if (sql2.executeExistsQuery(getConnection(userContext))) {

                            broker.close();
                            return true;
                        }
                    }
                    //			else
                    //				return false;
                }
                broker.close();
            }
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }


        return (false);
    }

    public boolean isInventarioUfficiale(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            SQLBuilder sql = null;
            SQLBroker broker = null;

            UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));

            if (utente.isSupervisore() && utente.getCd_ruolo_supervisore() != null) {
                RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente.getCd_ruolo_supervisore())));
                if (ruolo != null && ruolo.getTipo() != null) {
                    SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                    sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                    sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                    sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                    sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_INVENTARIO_UFFICIALE);

                    if (sql2.executeExistsQuery(getConnection(userContext)))
                        return true;
                }
            } else {
                sql = getHome(userContext, Utente_unita_ruoloBulk.class).createSQLBuilder();
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UTENTE", SQLBuilder.EQUALS, CNRUserContext.getUser(userContext));
                broker = getHome(userContext, Utente_unita_ruoloBulk.class).createBroker(sql);
                Utente_unita_ruoloBulk utente_unita_ruolo;
                while (broker.next()) {
                    utente_unita_ruolo = (Utente_unita_ruoloBulk) broker.fetch(Utente_unita_ruoloBulk.class);
                    RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente_unita_ruolo.getCd_ruolo())));
                    if (ruolo != null && ruolo.getTipo() != null) {
                        SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                        sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                        //sql2.addSQLClause("AND","TIPO_RUOLO.FL_INVENTARIO_UFFICIALE",SQLBuilder.EQUALS,"Y");
                        sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                        sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                        sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_INVENTARIO_UFFICIALE);

                        if (sql2.executeExistsQuery(getConnection(userContext))) {

                            broker.close();
                            return true;
                        }
                    }
                    //		else
                    //			return false;
                }
                broker.close();
            }
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }


        return (false);
    }

    public boolean isAbilitatoCancellazioneMissioneGemis(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        return controlloAbilitazione(userContext, PrivilegioBulk.ABILITA_CANCELLAZIONE_MISSIONE_GEMIS);
    }

    public boolean isAbilitatoFirmaFatturazioneElettronica(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        return controlloAbilitazione(userContext, PrivilegioBulk.ABILITA_FIRMA_FATTURA_ELETTRONICA);
    }

    public boolean isAbilitatoSbloccoImpegni(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        return controlloAbilitazione(userContext, PrivilegioBulk.ABILITA_SBLOCCO_IMPEGNO);
    }

    public boolean controlloAbilitazione(UserContext userContext, String tipoAbilitazione) throws ComponentException {
        try {
            SQLBuilder sql = null;
            SQLBroker broker = null;

            UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));

            if (utente.isSupervisore() && utente.getCd_ruolo_supervisore() != null) {
                RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente.getCd_ruolo_supervisore())));
                if (ruolo != null && ruolo.getTipo() != null) {
                    SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                    sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                    sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                    sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");

                    sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, tipoAbilitazione);

                    if (sql2.executeExistsQuery(getConnection(userContext)))
                        return true;
                }
            } else {
                sql = getHome(userContext, Utente_unita_ruoloBulk.class).createSQLBuilder();
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UTENTE", SQLBuilder.EQUALS, CNRUserContext.getUser(userContext));
                broker = getHome(userContext, Utente_unita_ruoloBulk.class).createBroker(sql);
                Utente_unita_ruoloBulk utente_unita_ruolo;
                while (broker.next()) {
                    utente_unita_ruolo = (Utente_unita_ruoloBulk) broker.fetch(Utente_unita_ruoloBulk.class);
                    RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente_unita_ruolo.getCd_ruolo())));
                    if (ruolo != null && ruolo.getTipo() != null) {

                        SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                        sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                        //sql2.addSQLClause("AND","TIPO_RUOLO.FL_APPROVA_BIL",SQLBuilder.EQUALS,"Y");
                        sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                        sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");

                        sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, tipoAbilitazione);

                        if (sql2.executeExistsQuery(getConnection(userContext))) {

                            broker.close();
                            return true;
                        }
                    }
                    //			else
                    //				return false;
                }
                broker.close();
            }
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }


        return (false);
    }

    public boolean isGestoreIstatSiope(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            SQLBuilder sql = null;
            SQLBroker broker = null;

            UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));

            if (utente.isSupervisore() && utente.getCd_ruolo_supervisore() != null) {
                RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente.getCd_ruolo_supervisore())));
                if (ruolo != null && ruolo.getTipo() != null) {
                    SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                    sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                    sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                    sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                    sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_GESTIONE_ISTAT_SIOPE);

                    if (sql2.executeExistsQuery(getConnection(userContext)))
                        return true;
                }
            } else {
                sql = getHome(userContext, Utente_unita_ruoloBulk.class).createSQLBuilder();
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UTENTE", SQLBuilder.EQUALS, CNRUserContext.getUser(userContext));
                broker = getHome(userContext, Utente_unita_ruoloBulk.class).createBroker(sql);
                Utente_unita_ruoloBulk utente_unita_ruolo;
                while (broker.next()) {
                    utente_unita_ruolo = (Utente_unita_ruoloBulk) broker.fetch(Utente_unita_ruoloBulk.class);
                    RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente_unita_ruolo.getCd_ruolo())));
                    if (ruolo != null && ruolo.getTipo() != null) {
                        SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                        sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                        //sql2.addSQLClause("AND","TIPO_RUOLO.FL_ISTAT_SIOPE",SQLBuilder.EQUALS,"Y");
                        sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                        sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                        sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_GESTIONE_ISTAT_SIOPE);

                        if (sql2.executeExistsQuery(getConnection(userContext))) {

                            broker.close();
                            return true;
                        }
                    }
                    //		else
                    //			return false;
                }
                broker.close();
            }
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }


        return (false);
    }

    public boolean isAbilitatoECF(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            SQLBuilder sql = null;
            SQLBroker broker = null;

            UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));

            if (utente.isSupervisore() && utente.getCd_ruolo_supervisore() != null) {
                RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente.getCd_ruolo_supervisore())));
                if (ruolo != null && ruolo.getTipo() != null) {
                    SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                    sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                    sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                    sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                    sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_ELENCO_CF);

                    if (sql2.executeExistsQuery(getConnection(userContext)))
                        return true;
                }
            } else {
                sql = getHome(userContext, Utente_unita_ruoloBulk.class).createSQLBuilder();
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UTENTE", SQLBuilder.EQUALS, CNRUserContext.getUser(userContext));
                broker = getHome(userContext, Utente_unita_ruoloBulk.class).createBroker(sql);
                Utente_unita_ruoloBulk utente_unita_ruolo;
                while (broker.next()) {
                    utente_unita_ruolo = (Utente_unita_ruoloBulk) broker.fetch(Utente_unita_ruoloBulk.class);
                    RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente_unita_ruolo.getCd_ruolo())));
                    if (ruolo != null && ruolo.getTipo() != null) {
                        SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                        sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                        //sql2.addSQLClause("AND","TIPO_RUOLO.FL_ECF",SQLBuilder.EQUALS,"Y");
                        sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                        sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                        sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_ELENCO_CF);

                        if (sql2.executeExistsQuery(getConnection(userContext))) {

                            broker.close();
                            return true;
                        }
                    }
                }
                broker.close();
            }
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }


        return (false);
    }

    public boolean isAbilitatoF24EP(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            SQLBuilder sql = null;
            SQLBroker broker = null;

            UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));

            if (utente.isSupervisore() && utente.getCd_ruolo_supervisore() != null) {
                RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente.getCd_ruolo_supervisore())));
                if (ruolo != null && ruolo.getTipo() != null) {
                    SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                    sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                    sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                    sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                    sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_F24EP);

                    if (sql2.executeExistsQuery(getConnection(userContext)))
                        return true;
                }
            } else {
                sql = getHome(userContext, Utente_unita_ruoloBulk.class).createSQLBuilder();
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UTENTE", SQLBuilder.EQUALS, CNRUserContext.getUser(userContext));
                broker = getHome(userContext, Utente_unita_ruoloBulk.class).createBroker(sql);
                Utente_unita_ruoloBulk utente_unita_ruolo;
                while (broker.next()) {
                    utente_unita_ruolo = (Utente_unita_ruoloBulk) broker.fetch(Utente_unita_ruoloBulk.class);
                    RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente_unita_ruolo.getCd_ruolo())));
                    if (ruolo != null && ruolo.getTipo() != null) {
                        SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                        sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                        //sql2.addSQLClause("AND","TIPO_RUOLO.FL_F24EP",SQLBuilder.EQUALS,"Y");
                        sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                        sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                        sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_F24EP);

                        if (sql2.executeExistsQuery(getConnection(userContext))) {

                            broker.close();
                            return true;
                        }
                    }
                }
                broker.close();
            }
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }


        return (false);
    }

    public boolean isAbilitatoPubblicazioneSito(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        return isAbilitatoPrivilegio(userContext,PrivilegioBulk.ABILITA_PUBBLICAZIONE_SITO,PrivilegioBulk.ABILITA_FUNZIONI_DIRETTORE);
    }

    public boolean isAbilitatoFunzioniIncarichi(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            SQLBuilder sql = null;
            SQLBroker broker = null;

            UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));

            if (utente.isSupervisore() && utente.getCd_ruolo_supervisore() != null) {
                RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente.getCd_ruolo_supervisore())));
                if (ruolo != null && ruolo.getTipo() != null) {
                    SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                    sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());
                    sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                    sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                    sql2.openParenthesis("AND");
                    sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_FUNZIONI_INCARICHI);
                    sql2.addSQLClause("OR", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_FUNZIONI_DIRETTORE);
                    sql2.closeParenthesis();
                    if (sql2.executeExistsQuery(getConnection(userContext)))
                        return true;
                }
            } else {
                sql = getHome(userContext, Utente_unita_ruoloBulk.class).createSQLBuilder();
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UTENTE", SQLBuilder.EQUALS, CNRUserContext.getUser(userContext));
                broker = getHome(userContext, Utente_unita_ruoloBulk.class).createBroker(sql);
                Utente_unita_ruoloBulk utente_unita_ruolo;
                while (broker.next()) {
                    utente_unita_ruolo = (Utente_unita_ruoloBulk) broker.fetch(Utente_unita_ruoloBulk.class);
                    RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente_unita_ruolo.getCd_ruolo())));
                    if (ruolo != null && ruolo.getTipo() != null) {

                        SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                        sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());
                        sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                        sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                        sql2.openParenthesis("AND");
                        sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_FUNZIONI_INCARICHI);
                        sql2.addSQLClause("OR", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_FUNZIONI_DIRETTORE);
                        sql2.closeParenthesis();
                        if (sql2.executeExistsQuery(getConnection(userContext))) {
                            broker.close();
                            return true;
                        }
                    }
                }
                broker.close();
            }
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }


        return (false);
    }


    public boolean isAbilitatoModificaModPag(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            SQLBuilder sql = null;
            SQLBroker broker = null;

            UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));

            if (utente.isSupervisore() && utente.getCd_ruolo_supervisore() != null) {
                RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente.getCd_ruolo_supervisore())));
                if (ruolo != null && ruolo.getTipo() != null) {
                    SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                    sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                    sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                    sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                    sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_FATTURA_ATTIVA);

                    if (sql2.executeExistsQuery(getConnection(userContext)))
                        return true;
                }
            } else {
                sql = getHome(userContext, Utente_unita_ruoloBulk.class).createSQLBuilder();
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UTENTE", SQLBuilder.EQUALS, CNRUserContext.getUser(userContext));
                broker = getHome(userContext, Utente_unita_ruoloBulk.class).createBroker(sql);
                Utente_unita_ruoloBulk utente_unita_ruolo;
                while (broker.next()) {
                    utente_unita_ruolo = (Utente_unita_ruoloBulk) broker.fetch(Utente_unita_ruoloBulk.class);
                    RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente_unita_ruolo.getCd_ruolo())));
                    if (ruolo != null && ruolo.getTipo() != null) {
                        SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                        sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());
                        //			sql2.addSQLClause("AND","TIPO_RUOLO.FL_BANCA_FAT_ATTIVA",SQLBuilder.EQUALS,"Y");
                        sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                        sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                        sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_FATTURA_ATTIVA);

                        if (sql2.executeExistsQuery(getConnection(userContext))) {

                            broker.close();
                            return true;
                        }
                    }
                }
                broker.close();
            }
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }


        return (false);
    }

    public boolean isSuperUtenteFunzioniIncarichi(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            SQLBuilder sql = null;
            SQLBroker broker = null;

            UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));

            if (utente.isSupervisore() && utente.getCd_ruolo_supervisore() != null) {
                RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente.getCd_ruolo_supervisore())));
                if (ruolo != null && ruolo.getTipo() != null) {
                    SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                    sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());
                    sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                    sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                    sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_FUNZIONI_SUPERUTENTE_INCARICHI);
                    if (sql2.executeExistsQuery(getConnection(userContext)))
                        return true;
                }
            } else {
                sql = getHome(userContext, Utente_unita_ruoloBulk.class).createSQLBuilder();
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UTENTE", SQLBuilder.EQUALS, CNRUserContext.getUser(userContext));
                broker = getHome(userContext, Utente_unita_ruoloBulk.class).createBroker(sql);
                Utente_unita_ruoloBulk utente_unita_ruolo;
                while (broker.next()) {
                    utente_unita_ruolo = (Utente_unita_ruoloBulk) broker.fetch(Utente_unita_ruoloBulk.class);
                    RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente_unita_ruolo.getCd_ruolo())));
                    if (ruolo != null && ruolo.getTipo() != null) {

                        SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                        sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());
                        sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                        sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                        sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_FUNZIONI_SUPERUTENTE_INCARICHI);
                        if (sql2.executeExistsQuery(getConnection(userContext))) {
                            broker.close();
                            return true;
                        }
                    }
                }
                broker.close();
            }
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        } catch (Exception e) {
            return false;
        }
        return (false);
    }

    public boolean isAbilitatoSospensioneCori(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            SQLBuilder sql = null;
            SQLBroker broker = null;

            UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));

            if (utente.isSupervisore() && utente.getCd_ruolo_supervisore() != null) {
                RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente.getCd_ruolo_supervisore())));
                if (ruolo != null && ruolo.getTipo() != null) {
                    SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                    sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                    sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                    sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                    sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_SOSPCORI);

                    if (sql2.executeExistsQuery(getConnection(userContext)))
                        return true;
                }
            } else {
                sql = getHome(userContext, Utente_unita_ruoloBulk.class).createSQLBuilder();
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UTENTE", SQLBuilder.EQUALS, CNRUserContext.getUser(userContext));
                broker = getHome(userContext, Utente_unita_ruoloBulk.class).createBroker(sql);
                Utente_unita_ruoloBulk utente_unita_ruolo;
                while (broker.next()) {
                    utente_unita_ruolo = (Utente_unita_ruoloBulk) broker.fetch(Utente_unita_ruoloBulk.class);
                    RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente_unita_ruolo.getCd_ruolo())));
                    if (ruolo != null && ruolo.getTipo() != null) {
                        SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                        sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                        //sql2.addSQLClause("AND","TIPO_RUOLO.FL_F24EP",SQLBuilder.EQUALS,"Y");
                        sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                        sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                        sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_SOSPCORI);

                        if (sql2.executeExistsQuery(getConnection(userContext))) {

                            broker.close();
                            return true;
                        }
                    }
                }
                broker.close();
            }
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }


        return (false);
    }

    public boolean isAbilitatoModificaDescVariazioni(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            SQLBuilder sql = null;
            SQLBroker broker = null;

            UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));

            if (utente.isSupervisore() && utente.getCd_ruolo_supervisore() != null) {
                RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente.getCd_ruolo_supervisore())));
                if (ruolo != null && ruolo.getTipo() != null) {
                    SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                    sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                    sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                    sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                    sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_VARIAZIONI);

                    if (sql2.executeExistsQuery(getConnection(userContext)))
                        return true;
                }
            }
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }


        return (false);
    }

    public boolean isAbilitatoAllTrattamenti(UserContext userContext) throws it.cnr.jada.comp.ComponentException, IntrospectionException {
        try {
            SQLBuilder sql = null;
            SQLBroker broker = null;

            UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));

            if (utente.isSupervisore() && utente.getCd_ruolo_supervisore() != null) {
                RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente.getCd_ruolo_supervisore())));
                if (ruolo != null && ruolo.getTipo() != null) {
                    SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                    sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                    sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                    sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                    sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_ALL_TRATT);

                    if (sql2.executeExistsQuery(getConnection(userContext)))
                        return true;
                }
            } else {
                String cd_uo_scrivania = CNRUserContext.getCd_unita_organizzativa(userContext);
                Unita_organizzativaBulk cd_uo_cds;

                Unita_organizzativaHome uoHome = (Unita_organizzativaHome) getHome(userContext, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class);
                Unita_organizzativaBulk uo = (Unita_organizzativaBulk) uoHome.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo_scrivania));

                cd_uo_cds = uoHome.findUo_cdsByUo(CNRUserContext.getEsercizio(userContext), cd_uo_scrivania);

                sql = getHome(userContext, Utente_unita_ruoloBulk.class).createSQLBuilder();
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, cd_uo_cds.getCd_unita_organizzativa());
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UTENTE", SQLBuilder.EQUALS, CNRUserContext.getUser(userContext));
                broker = getHome(userContext, Utente_unita_ruoloBulk.class).createBroker(sql);
                Utente_unita_ruoloBulk utente_unita_ruolo;
                while (broker.next()) {
                    utente_unita_ruolo = (Utente_unita_ruoloBulk) broker.fetch(Utente_unita_ruoloBulk.class);
                    RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente_unita_ruolo.getCd_ruolo())));
                    if (ruolo != null && ruolo.getTipo() != null) {
                        SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                        sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                        //sql2.addSQLClause("AND","TIPO_RUOLO.FL_F24EP",SQLBuilder.EQUALS,"Y");
                        sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                        sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                        sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_ALL_TRATT);

                        if (sql2.executeExistsQuery(getConnection(userContext))) {

                            broker.close();
                            return true;
                        }
                    }
                }
                broker.close();
            }
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }


        return (false);
    }

    public boolean isAbilitatoAutorizzareDiaria(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            SQLBuilder sql = null;
            SQLBroker broker = null;

            UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));

            if (utente.isSupervisore() && utente.getCd_ruolo_supervisore() != null) {
                RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente.getCd_ruolo_supervisore())));
                if (ruolo != null && ruolo.getTipo() != null) {
                    SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                    sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                    sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                    sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                    sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_AUTORIZZA_DIARIA);

                    if (sql2.executeExistsQuery(getConnection(userContext)))
                        return true;
                }
                {
                    sql = getHome(userContext, Utente_unita_ruoloBulk.class).createSQLBuilder();
                    sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
                    sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UTENTE", SQLBuilder.EQUALS, CNRUserContext.getUser(userContext));
                    broker = getHome(userContext, Utente_unita_ruoloBulk.class).createBroker(sql);
                    Utente_unita_ruoloBulk utente_unita_ruolo;
                    while (broker.next()) {
                        utente_unita_ruolo = (Utente_unita_ruoloBulk) broker.fetch(Utente_unita_ruoloBulk.class);
                        RuoloBulk ruoloAltro = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente_unita_ruolo.getCd_ruolo())));
                        if (ruoloAltro != null && ruoloAltro.getTipo() != null) {
                            SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                            sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruoloAltro.getTipo());

                            sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                            sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                            sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_AUTORIZZA_DIARIA);

                            if (sql2.executeExistsQuery(getConnection(userContext))) {

                                broker.close();
                                return true;
                            }
                        }
                    }
                    broker.close();
                }
            } else {
                sql = getHome(userContext, Utente_unita_ruoloBulk.class).createSQLBuilder();
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
                sql.addSQLClause("AND", "UTENTE_UNITA_RUOLO.CD_UTENTE", SQLBuilder.EQUALS, CNRUserContext.getUser(userContext));
                broker = getHome(userContext, Utente_unita_ruoloBulk.class).createBroker(sql);
                Utente_unita_ruoloBulk utente_unita_ruolo;
                while (broker.next()) {
                    utente_unita_ruolo = (Utente_unita_ruoloBulk) broker.fetch(Utente_unita_ruoloBulk.class);
                    RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente_unita_ruolo.getCd_ruolo())));
                    if (ruolo != null && ruolo.getTipo() != null) {
                        SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                        sql2.addSQLClause("AND", "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                        sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                        sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                        sql2.addSQLClause("AND", "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, PrivilegioBulk.ABILITA_AUTORIZZA_DIARIA);

                        if (sql2.executeExistsQuery(getConnection(userContext))) {

                            broker.close();
                            return true;
                        }
                    }
                }
                broker.close();
            }
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }


        return (false);
    }

    public boolean isAbilitatoReversaleIncasso(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        return isAbilitatoPrivilegio(userContext,PrivilegioBulk.ABILITA_REVERSALE_INCASSO);
    }

    private boolean isAbilitatoPrivilegio(UserContext userContext, String... privilegi) throws it.cnr.jada.comp.ComponentException {
        try {
            SQLBuilder sql = null;
            SQLBroker broker = null;

            UtenteBulk utente = (UtenteBulk) (getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext))));

            if (utente.isSupervisore() && utente.getCd_ruolo_supervisore() != null) {
                RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente.getCd_ruolo_supervisore())));
                if (ruolo != null && ruolo.getTipo() != null) {
                    SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                    sql2.addSQLClause(FindClause.AND, "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                    sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                    sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                    sql2.openParenthesis(FindClause.AND);
                    Arrays.stream(privilegi).forEach(privilegio->
                        sql2.addSQLClause(FindClause.OR, "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, privilegio)
                    );
                    sql2.closeParenthesis();

                    if (sql2.executeExistsQuery(getConnection(userContext)))
                        return true;
                }
            } else {
                sql = getHome(userContext, Utente_unita_ruoloBulk.class).createSQLBuilder();
                sql.addSQLClause(FindClause.AND, "UTENTE_UNITA_RUOLO.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
                sql.addSQLClause(FindClause.AND, "UTENTE_UNITA_RUOLO.CD_UTENTE", SQLBuilder.EQUALS, CNRUserContext.getUser(userContext));
                broker = getHome(userContext, Utente_unita_ruoloBulk.class).createBroker(sql);
                Utente_unita_ruoloBulk utente_unita_ruolo;
                while (broker.next()) {
                    utente_unita_ruolo = (Utente_unita_ruoloBulk) broker.fetch(Utente_unita_ruoloBulk.class);
                    RuoloBulk ruolo = (RuoloBulk) (getHome(userContext, RuoloBulk.class).findByPrimaryKey(new RuoloBulk(utente_unita_ruolo.getCd_ruolo())));
                    if (ruolo != null && ruolo.getTipo() != null) {

                        SQLBuilder sql2 = getHome(userContext, Tipo_ruoloBulk.class).createSQLBuilder();
                        sql2.addSQLClause(FindClause.AND, "TIPO_RUOLO.TIPO", SQLBuilder.EQUALS, ruolo.getTipo());

                        //sql2.addSQLClause("AND","TIPO_RUOLO.FL_APPROVA_BIL",SQLBuilder.EQUALS,"Y");
                        sql2.addTableToHeader("ASS_TIPO_RUOLO_PRIVILEGIO");
                        sql2.addSQLJoin("TIPO_RUOLO.TIPO", "ASS_TIPO_RUOLO_PRIVILEGIO.TIPO");
                        sql2.openParenthesis(FindClause.AND);
                        Arrays.stream(privilegi).forEach(privilegio->
                            sql2.addSQLClause(FindClause.OR, "ASS_TIPO_RUOLO_PRIVILEGIO.CD_PRIVILEGIO", SQLBuilder.EQUALS, privilegio)
                        );
                        sql2.closeParenthesis();

                        if (sql2.executeExistsQuery(getConnection(userContext))) {
                            broker.close();
                            return true;
                        }
                    }
                }
                broker.close();
            }
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }
        return (false);
    }
}
