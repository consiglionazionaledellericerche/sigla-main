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

package it.cnr.contab.utente00.nav.comp;

import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.bulk.Parametri_enteHome;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.messaggio00.bulk.Messaggio_lettoBulk;
import it.cnr.contab.messaggio00.bulk.Messaggio_notificatoBulk;
import it.cnr.contab.messaggio00.bulk.Messaggio_notificatoKey;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.ldap.Person;
import it.cnr.contab.spring.service.LDAPService;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.PropertyNames;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.IDToken;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.io.Serializable;
import java.security.Principal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class GestioneLoginComponent
        extends it.cnr.jada.comp.GenericComponent
        implements ILoginMgr, Cloneable, Serializable, it.cnr.jada.comp.Component {

    public static final int VALIDA_FASE_INIZIALE = 0;
    public static final int VALIDA_NUOVO_UTENTE_LDAP = 1;
    public static final int VALIDA_NUOVO_UTENTE_LDAP_ANNULLA = 2;
    public static final int VALIDA_FASE_INIZIALE_UTENTE_MULTIPLO = 3;

    public GestioneLoginComponent() {
    }

    /**
     * normale
     * PreCondition:
     * Una richiesta viene fatto per la modifica della password. L'utente ha chiesto di scrivere la password vecchia e due volte la password nuova.
     * <p>
     * Dato un'oggetto UtenteBulk che contiene le informazione dell'utente e la password vecchia (criptata) che ha digitato l'utente, e la password nuova (criptata) che ha digitata l'utente,
     * PostCondition:
     * La password vecchia viene confrontata con la password in base dati.
     * Se questo controllo ha un esito positivo, opzionalmente si fa un controllo della password nuova nel confronto degli standard di password (troppo corta, troppa semplice, ecc.)
     * Se questo controllo ha un esito positivo, si modifica UtenteBulk scrivendo la password nuova in base dati.
     * Viene restituito l'oggetto UtenteBulk modificato.
     */
    //^^@@
    public UtenteBulk cambiaPassword(UserContext userContext, UtenteBulk utente, String nuovoPassword) throws it.cnr.jada.comp.ComponentException {
        try {
            UtenteHome home = (UtenteHome) getHome(userContext, utente);
            UtenteBulk utenteReale = (UtenteBulk) home.findAndLock(utente);
            if (utenteReale.getPassword() != null &&
                    !utenteReale.getPassword().equals(utente.getPassword()))
                return null;
            if (utenteReale.isUtenteComune()) {
                utenteReale = (UtenteBulk) getHome(userContext, UtenteComuneBulk.class).findAndLock(utente);
                UtenteComuneBulk utente_comune = (UtenteComuneBulk) utenteReale;
                utente_comune.setCdr((CdrBulk) getHome(userContext, CdrBulk.class).findByPrimaryKey(utente_comune.getCdr()));
            }
            utenteReale.setPasswordInChiaro(nuovoPassword);
            utenteReale.setDt_ultima_var_password(home.getServerTimestamp());
            utenteReale.setUser(utente.getUser());
            utenteReale.setDt_ultimo_accesso(home.getServerTimestamp());
            utenteReale.setToBeUpdated();
            makeBulkPersistent(userContext, utenteReale);
            return utenteReale;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public boolean controllaAccesso(UserContext userContext, String cd_accesso) throws it.cnr.jada.comp.ComponentException {
        try {
            final Optional<String> cd_unita_organizzativaOptional = Optional.ofNullable(CNRUserContext.getCd_unita_organizzativa(userContext));
            Integer esercizio = cd_unita_organizzativaOptional
                    .map(s -> CNRUserContext.getEsercizio(userContext))
                    .orElse(Integer.valueOf(0));
            String cd_unita_organizzativa = cd_unita_organizzativaOptional
                    .orElse("*");
            final BulkHome v_utente_accessoHome = getHome(userContext, Utente_unita_accessoBulk.class, "V_UTENTE_ACCESSO");
            SQLBuilder sqlBuilder = v_utente_accessoHome.createSQLBuilder();
            sqlBuilder.addClause(FindClause.AND, "cd_utente", SQLBuilder.EQUALS, CNRUserContext.getUser(userContext));
            sqlBuilder.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, cd_unita_organizzativa);
            sqlBuilder.addClause(FindClause.AND, "cd_accesso", SQLBuilder.EQUALS, cd_accesso);
            sqlBuilder.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, esercizio);
            return sqlBuilder.executeExistsQuery(getConnection(userContext));
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }
    }
    //^^@@

    /**
     * normale
     * PreCondition:
     * Dato un'istanza di UTENTE specificata dall'parametro UtenteBulk un'istanza di ALBERO_MAIN specificata dall'attributo CD_NODO, e un numero di livelli specificato dal parametro num_livelli
     * PostCondition:
     * sarà restituito un'oggetto ALBERO_MAINBULK che contiene le informazioni per l'istanza ALBERO_MAIN che corrisponde al CD_NODO, più la gerarchia dell'albero di funzioni a cui l'utente ha accesso per numero di livelli NUM_LIVELLI e cominciando dal nodo CD_NODO
     */
    //^^@@
    public Albero_mainBulk generaAlberoPerUtente(UserContext userContext, UtenteBulk utente, String cd_unita_organizzativa, String cd_nodo, short num_livelli) throws it.cnr.jada.comp.ComponentException {
        try {
            Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext);
            if (cd_unita_organizzativa == null) {
                // Se l'utente non ha selezionato l'unità organizzativa la lista
                // dei nodi è indipendente anche dall'esercizio;
                cd_unita_organizzativa = "*";
                esercizio = new Integer(0);
            }
            if (cd_nodo == null)
                cd_nodo = "0";

            Albero_mainHome home = (Albero_mainHome) getHomeCache(userContext).getHome(Albero_mainBulk.class, "V_ALBERO_MAIN_UNITA_UTENTE");
            it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
            sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, cd_unita_organizzativa);
            sql.addSQLClause("AND", "CD_NODO_PADRE", SQLBuilder.EQUALS, cd_nodo);
            sql.addSQLClause("AND", "CD_UTENTE", SQLBuilder.EQUALS, utente.getCd_utente());
            sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, esercizio);
            sql.addOrderBy("PG_ORDINAMENTO");
            java.util.Collection nodi = home.fetchAll(sql);
            getHomeCache(userContext).fetchAll(userContext);
            Albero_mainBulk nodoRadice = null;
            for (java.util.Iterator i = nodi.iterator(); i.hasNext(); ) {
                Albero_mainBulk nodo = (Albero_mainBulk) i.next();
                if (nodo.getFl_terminale() != null &&
                        !nodo.getFl_terminale().booleanValue()) {
                    sql = home.createSQLBuilder();
                    sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, cd_unita_organizzativa);
                    sql.addSQLClause("AND", "CD_UTENTE", SQLBuilder.EQUALS, utente.getCd_utente());
                    sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, esercizio);
                    sql.addClause("AND", "cd_nodo", SQLBuilder.STARTSWITH, nodo.getCd_nodo());
                    sql.addClause("AND", "fl_terminale", SQLBuilder.EQUALS, Boolean.TRUE);
                    int count = sql.executeCountQuery(getHomeCache(userContext).getConnection());
                    if (nodo.getNodo_padre() != null && count > 0)
                        (nodoRadice = nodo.getNodo_padre()).addToNodi_figli(nodo);
                } else if (nodo.getNodo_padre() != null)
                    (nodoRadice = nodo.getNodo_padre()).addToNodi_figli(nodo);
            }
            return nodoRadice;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * leggiMessaggi method comment.
     */
    public void leggiMessaggi(it.cnr.jada.UserContext userContext, it.cnr.contab.messaggio00.bulk.MessaggioBulk[] messaggi) throws it.cnr.jada.comp.ComponentException {
        try {
            String user = it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext);
            for (int i = 0; i < messaggi.length; i++)
                try {
                    Messaggio_lettoBulk messaggio_letto = new Messaggio_lettoBulk(user, messaggi[i].getPg_messaggio());
                    messaggio_letto.setUser(user);
                    insertBulk(userContext, messaggio_letto);
                } catch (it.cnr.jada.persistency.sql.DuplicateKeyException e) {
                }
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }
    //^^@@

    /**
     * utente amministratore o superutente
     * PreCondition:
     * Dato un'istanza di UTENTE amminstratore o superutente
     * specificata dal parametro UtenteBulk
     * PostCondition:
     * sarà restituito un array di tutti gli esercizi disponibili
     * normale
     * PreCondition:
     * Dato un'istanza di UTENTE specificata dal parametro UtenteBulk
     * PostCondition:
     * sarà restituito un array degli esercizi su cui l'utente
     * possiede almeno un accesso su qualche unita organizzativa
     * (possiede direttamente o tramite i ruoli o l'utente template)
     */
    //^^@@
    public java.lang.Integer[] listaEserciziPerUtente(it.cnr.jada.UserContext userContext, it.cnr.contab.utenze00.bulk.UtenteBulk utente) throws it.cnr.jada.comp.ComponentException {
        try {
            java.util.ArrayList esercizi = new java.util.ArrayList();
            it.cnr.jada.persistency.sql.SQLBuilder sql = new it.cnr.jada.persistency.sql.SQLBuilder();
            sql.setHeader("SELECT DISTINCT ESERCIZIO");

            if (utente.isUtenteComune()) {
                sql.addTableToHeader("V_UTENTE_UNITA_ORGANIZZATIVA");
                sql.addSQLClause("AND", "CD_UTENTE", SQLBuilder.EQUALS, utente.getCd_utente());
            } else {
                sql.addTableToHeader("ESERCIZIO");
                sql.addSQLClause("AND", "ESERCIZIO >= ( SELECT IM01 FROM CONFIGURAZIONE_CNR WHERE CD_CHIAVE_PRIMARIA = 'ESERCIZIO_SPECIALE' AND CD_CHIAVE_SECONDARIA = 'ESERCIZIO_PARTENZA' )");
            }

            sql.addOrderBy("ESERCIZIO");

            LoggableStatement stm = sql.prepareStatement(getConnection(userContext));
            try {
                java.sql.ResultSet rs = stm.executeQuery();
                while (rs.next())
                    esercizi.add(new Integer(rs.getInt(1)));
            } finally {
                try {
                    stm.close();
                } catch (java.sql.SQLException e) {
                }
			}

            Integer[] array = new Integer[esercizi.size()];
            return (Integer[]) esercizi.toArray(array);
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public RemoteIterator listaMessaggi(UserContext userContext, String server_url) throws it.cnr.jada.comp.ComponentException {
        String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
        it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(userContext, it.cnr.contab.messaggio00.bulk.MessaggioBulk.class, "V_MESSAGGI_UTENTE").createSQLBuilder();
        sql.addSQLClause("AND", "CD_UTENTE", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));
        sql.addSQLClause("AND", "LETTO", SQLBuilder.EQUALS, "N");
        sql.openParenthesis("AND");
        sql.addSQLClause("AND", "SERVER_URL", SQLBuilder.ISNULL, null);
        if (server_url != null)
            sql.addSQLClause("OR", "SERVER_URL", SQLBuilder.EQUALS, server_url);
        sql.closeParenthesis();
        return iterator(userContext, sql, it.cnr.contab.messaggio00.bulk.MessaggioBulk.class, null);
    }
    //^^@@

    /**
     * normale
     * PreCondition:
     * Dato un'istanza di UTENTE COMUNE specificata dal parametro UtenteBulk
     * e un esercizio
     * PostCondition:
     * sarà restituito un iteratore sulla collezione di unità organizzative
     * per cui l'utente possiede almeno un accesso per l'esercizio specificato
     * (possiede direttamente o tramite i ruoli o l'utente template). Per ogni
     * unita organizzativa CDS presente nella collezione devono essere presenti
     * tutte le unita organizzative del CDS a cui fa capo.
     */
    //^^@@
    public RemoteIterator listaUOPerUtente(UserContext userContext, UtenteBulk utente, Integer esercizio) throws it.cnr.jada.comp.ComponentException {
        try {
            Unita_organizzativaHome home = (Unita_organizzativaHome) getHome(userContext, Unita_organizzativaBulk.class, "V_UTENTE_UNITA_ORGANIZZATIVA");
            it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilderEsteso();
            sql.addSQLClause("AND", "CD_UTENTE", SQLBuilder.EQUALS, utente.getCd_utente());
            sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, esercizio);
            sql.addOrderBy("CD_UNITA_ORGANIZZATIVA");
            return iterator(
                    userContext,
                    sql,
                    Unita_organizzativaBulk.class,
                    getFetchPolicyName("listaUOPerUtente"));
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public RemoteIterator listaCdrPerUtente(UserContext userContext, UtenteBulk utente, Integer esercizio) throws it.cnr.jada.comp.ComponentException {
        try {
            String cd_unita_organizzativa = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
            CdrHome home = (CdrHome) getHome(userContext, CdrBulk.class);
            it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilderEsteso();
            sql.addSQLClause("AND", "ESERCIZIO_INIZIO", SQLBuilder.LESS_EQUALS, esercizio);
            sql.addSQLClause("AND", "ESERCIZIO_FINE", SQLBuilder.GREATER, esercizio);
            sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, cd_unita_organizzativa);
            sql.addOrderBy("CD_CENTRO_RESPONSABILITA");
            return iterator(
                    userContext,
                    sql,
                    CdrBulk.class,
                    getFetchPolicyName("listaCdrPerUtente"));
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public CdrBulk cdrDaUo(UserContext userContext, Unita_organizzativaBulk uo) throws it.cnr.jada.comp.ComponentException {
        try {
            Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext);
            CdrHome home = (CdrHome) getHome(userContext, CdrBulk.class);
            it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilderEsteso();
            sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, uo.getCd_unita_organizzativa());
            sql.addSQLClause("AND", "ESERCIZIO_INIZIO", SQLBuilder.LESS_EQUALS, esercizio);
            sql.addSQLClause("AND", "ESERCIZIO_FINE", SQLBuilder.GREATER, esercizio);
            sql.setOrderBy("livello", it.cnr.jada.util.OrderConstants.ORDER_ASC);
            sql.setOrderBy("cd_proprio_cdr", it.cnr.jada.util.OrderConstants.ORDER_ASC);
            List result = home.fetchAll(sql);
            if (!result.isEmpty())
                return (CdrBulk) result.get(0);
            else
                return null;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public void notificaMessaggi(UserContext userContext, String server_url) throws it.cnr.jada.comp.ComponentException {
        String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
        String sql = "SELECT MAX(PG_MESSAGGIO) FROM " + schema + "MESSAGGIO WHERE ( CD_UTENTE IS NULL OR CD_UTENTE = ? ) AND ( SERVER_URL IS NULL OR SERVER_URL = ? ) AND ( PG_MESSAGGIO > ( SELECT PG_MESSAGGIO FROM " + schema + "MESSAGGIO_NOTIFICATO WHERE MESSAGGIO_NOTIFICATO.CD_UTENTE = ? ) OR NOT EXISTS ( SELECT 1 FROM " + schema + "MESSAGGIO_NOTIFICATO WHERE MESSAGGIO_NOTIFICATO.CD_UTENTE = ? ) ) AND NOT EXISTS ( SELECT 1 FROM " + schema + "MESSAGGIO_LETTO WHERE MESSAGGIO_LETTO.PG_MESSAGGIO = MESSAGGIO.PG_MESSAGGIO AND MESSAGGIO_LETTO.CD_UTENTE = ? )";
        try {
            LoggableStatement stm = new LoggableStatement(getConnection(userContext), sql,
                    true, this.getClass());
            try {
                stm.setString(1, it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));
                stm.setString(2, server_url);
                stm.setString(3, it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));
                stm.setString(4, it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));
                stm.setString(5, it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));
                java.sql.ResultSet rs = stm.executeQuery();
                long pg_messaggio = -1;
                try {
                    if (!rs.next())
                        return;
                    pg_messaggio = rs.getInt(1);
                    if (rs.wasNull())
                        return;
                } finally {
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                    }
				}
                Messaggio_notificatoBulk messaggio_notificato = (Messaggio_notificatoBulk) getHome(userContext, Messaggio_notificatoBulk.class).findByPrimaryKey(new Messaggio_notificatoKey(it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext)));
                if (messaggio_notificato != null) {
                    messaggio_notificato.setPg_messaggio(new Long(pg_messaggio));
                    updateBulk(userContext, messaggio_notificato);
                } else {
                    messaggio_notificato = new Messaggio_notificatoBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));
                    messaggio_notificato.setPg_messaggio(new Long(pg_messaggio));
                    messaggio_notificato.setUser(it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));
                    insertBulk(userContext, messaggio_notificato);
                }

            } finally {
                try {
                    stm.close();
                } catch (java.sql.SQLException e) {
                }
			}
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<SessionTraceBulk> sessionList(UserContext userContext, String cdUtente) throws it.cnr.jada.comp.ComponentException {
        SessionTraceHome home = (SessionTraceHome) getHomeCache(userContext).getHome(SessionTraceBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause(FindClause.AND, "cd_utente", SQLBuilder.EQUALS, cdUtente);
        sql.addClause(FindClause.AND, "id_sessione", SQLBuilder.NOT_EQUALS, userContext.getSessionId());
        try {
            return home.fetchAll(sql);
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    public void registerUser(UserContext userContext, String id_clone) throws it.cnr.jada.comp.ComponentException {
        unregisterUser(userContext);
        try {
            java.sql.CallableStatement stm = getConnection(userContext).prepareCall(PropertyNames.getProperty("package.cnrctb850.register"));
            stm.setString(1, CNRUserContext.getCd_cds(userContext));
            stm.setInt(2, CNRUserContext.getEsercizio(userContext).intValue());
            stm.setString(3, CNRUserContext.getUser(userContext));
            stm.setString(4, userContext.getSessionId());
            stm.setString(5, id_clone);
            try {
                stm.execute();
            } finally {
                try {
                    stm.close();
                } catch (java.sql.SQLException e) {
                }
			}
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public void unregisterUser(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            java.sql.CallableStatement stm = getConnection(userContext).prepareCall(PropertyNames.getProperty("package.cnrctb850.unregister"));
            stm.setString(1, userContext.getSessionId());
            try {
                stm.execute();
            } finally {
                try {
                    stm.close();
                } catch (java.sql.SQLException e) {
                }
			}
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public void unregisterUsers(String id_clone) throws it.cnr.jada.comp.ComponentException {
        try {
            Connection conn = it.cnr.jada.util.ejb.EJBCommonServices.getConnection();
            java.sql.CallableStatement stm = conn.prepareCall(PropertyNames.getProperty("package.cnrctb850.unregisterall"));
            stm.setString(1, id_clone);
            try {
                stm.execute();
            } finally {
                try {
                    stm.close();
                    conn.close();
                } catch (java.sql.SQLException e) {
                }
			}
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public boolean isUserAccessoAllowed(UserContext userContext, String... accessi) throws it.cnr.jada.comp.ComponentException {
        try {
            SQLBuilder sql = new SQLBuilder();
            sql.setHeader("SELECT CD_ACCESSO, TI_FUNZIONE");
            sql.addTableToHeader("V_ASS_BP_ACCESSO_UNITA_UTENTE");
            sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
            sql.addSQLClause("AND", "CD_UTENTE", SQLBuilder.EQUALS, CNRUserContext.getUser(userContext));
            sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
            for (String accesso : accessi) {
                sql.addSQLClause("AND", "CD_ACCESSO", SQLBuilder.EQUALS, accesso);
            }
            LoggableStatement stm = sql.prepareStatement(getConnection(userContext));
            try {
                java.sql.ResultSet rs = stm.executeQuery();
                return rs.next();
            } finally {
                stm.close();
            }
        } catch (IntrospectionException | SQLException e) {
            throw handleException(e);
        }
    }
    //^^@@

    /**
     * normale
     * PreCondition:
     * Dato un'istanza di UTENTE COMUNE specificata dal parametro UtenteBulk,
     * un esercizio, il codice di una unita organizzativa e il nome di un
     * BusinessProcess
     * PostCondition:
     * sarà restituita la modalità di visualizzazione per il BusinessProcess
     * in base agli accessi posseduti dall'utente. La modalità di visualizzazione
     * è determinata dai nodi di ALBERO_MAIN disponibili all'utente
     * in base ai suoi accessi che posseggono il BusinessProcess specificato.
     * In caso di più nodi disponibili viene restituita la modalità di
     * visualizzazione meno restrittiva
     */
    //^^@@
    public String validaBPPerUtente(UserContext userContext, UtenteBulk utente, String cd_unita_organizzativa, String bp) throws it.cnr.jada.comp.ComponentException {
        try {
            Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext);
            if (cd_unita_organizzativa == null) {
                cd_unita_organizzativa = "*";
                esercizio = new Integer(0);
            }

            it.cnr.jada.persistency.sql.SQLBuilder sql = new it.cnr.jada.persistency.sql.SQLBuilder();
            sql.setHeader("SELECT CD_ACCESSO, TI_FUNZIONE");
            sql.addTableToHeader("V_ASS_BP_ACCESSO_UNITA_UTENTE");
            sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, cd_unita_organizzativa);
            sql.addSQLClause("AND", "BUSINESS_PROCESS", SQLBuilder.EQUALS, bp);
            sql.addSQLClause("AND", "CD_UTENTE", SQLBuilder.EQUALS, utente.getCd_utente());
            sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, esercizio);
            LoggableStatement stm = sql.prepareStatement(getConnection(userContext));
            try {
                java.sql.ResultSet rs = stm.executeQuery();
                String mode = null, accesso = null;
                while (rs.next()) {
                    mode = rs.getString("TI_FUNZIONE");
                    accesso = rs.getString("CD_ACCESSO");
                    if (utente.getFl_attiva_blocco() && accesso != null) {
                        PersistentHome ruoloBloccoHome = getHomeCache(userContext).getHome(Ruolo_bloccoBulk.class);
                        SQLBuilder sqlRuoloBlocco = ruoloBloccoHome.createSQLBuilder();
                        sqlRuoloBlocco.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, esercizio);
                        sqlRuoloBlocco.addClause(FindClause.AND, "fl_attivo", SQLBuilder.EQUALS, Boolean.TRUE);
                        sqlRuoloBlocco.addTableToHeader("RUOLO_ACCESSO");
                        sqlRuoloBlocco.addSQLJoin("RUOLO_BLOCCO.CD_RUOLO", "RUOLO_ACCESSO.CD_RUOLO");
                        sqlRuoloBlocco.addSQLClause(FindClause.AND, "RUOLO_ACCESSO.CD_ACCESSO", SQLBuilder.EQUALS, accesso);
                        java.util.List listBlocco = ruoloBloccoHome.fetchAll(sqlRuoloBlocco);
                        if (!listBlocco.isEmpty())
                            return null;
                    }
                    if (mode == null) {
                        try {
                            rs.close();
                        } catch (java.sql.SQLException e) {
                        }
						return "V";
                    }
                    if ("M".equals(mode)) {
                        try {
                            rs.close();
                        } catch (java.sql.SQLException e) {
                        }
						return mode;
                    }
                }
                return mode;
            } finally {
                try {
                    stm.close();
                } catch (java.sql.SQLException e) {
                }
			}
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * normale
     * PreCondition:
     * Dato un'istanza di UTENTE COMUNE specificata dal parametro UtenteBulk,
     * un esercizio, il codice di una unita organizzativa e il nome di un
     * BusinessProcess
     * PostCondition:
     * sarà restituita la modalità di visualizzazione per il BusinessProcess
     * in base agli accessi posseduti dall'utente. La modalità di visualizzazione
     * è determinata dai nodi di ALBERO_MAIN disponibili all'utente
     * in base ai suoi accessi che posseggono il BusinessProcess specificato.
     * In caso di più nodi disponibili viene restituita la modalità di
     * visualizzazione meno restrittiva
     */
    public boolean isBPEnableForUser(UserContext userContext, UtenteBulk utente, String cd_unita_organizzativa, String bp) throws it.cnr.jada.comp.ComponentException {
        try {
            Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext);
            SQLBuilder sql = new SQLBuilder();
            sql.setHeader("SELECT CD_ACCESSO, CD_UNITA_ORGANIZZATIVA");
            sql.addTableToHeader("V_ASS_BP_ACCESSO_UNITA_UTENTE");
            if (cd_unita_organizzativa != null)
                sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, cd_unita_organizzativa);
            sql.addSQLClause("AND", "BUSINESS_PROCESS", SQLBuilder.EQUALS, bp);
            sql.addSQLClause("AND", "CD_UTENTE", SQLBuilder.EQUALS, utente.getCd_utente());
            if (esercizio != null)
                sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, esercizio);
            LoggableStatement stm = sql.prepareStatement(getConnection(userContext));
            try {
                java.sql.ResultSet rs = stm.executeQuery();
                String accesso = null, uo = null;
                while (rs.next()) {
                    accesso = rs.getString("CD_ACCESSO");
                    uo = rs.getString("CD_UNITA_ORGANIZZATIVA");
                    if (utente.getFl_attiva_blocco() && accesso != null) {
                        PersistentHome ruoloBloccoHome = getHomeCache(userContext).getHome(Ruolo_bloccoBulk.class);
                        SQLBuilder sqlRuoloBlocco = ruoloBloccoHome.createSQLBuilder();
                        sqlRuoloBlocco.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, esercizio);
                        sqlRuoloBlocco.addClause(FindClause.AND, "fl_attivo", SQLBuilder.EQUALS, Boolean.TRUE);
                        sqlRuoloBlocco.addTableToHeader("RUOLO_ACCESSO");
                        sqlRuoloBlocco.addSQLJoin("RUOLO_BLOCCO.CD_RUOLO", "RUOLO_ACCESSO.CD_RUOLO");
                        sqlRuoloBlocco.addSQLClause(FindClause.AND, "RUOLO_ACCESSO.CD_ACCESSO", SQLBuilder.EQUALS, accesso);
                        if (!ruoloBloccoHome.fetchAll(sqlRuoloBlocco).isEmpty())
                            return false;
                    }
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                    }
					Unita_organizzativa_enteHome home = (Unita_organizzativa_enteHome) getHome(userContext, Unita_organizzativa_enteBulk.class);
                    Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) home.fetchAll(home.createSQLBuilder()).get(0);
					return cd_unita_organizzativa != null || uo.equals(uoEnte.getCd_unita_organizzativa());
				}
                return false;
            } finally {
                try {
                    stm.close();
                } catch (java.sql.SQLException e) {
                }
			}
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * normale
     * PreCondition:
     * Dato in input un'oggetto UtenteBulk che rappresenta l'utente,
     * un CD_NODO che rappresenta il nodo a cui l'utente ha richiesto accesso e
     * un CD_UNITA_ORGANIZZATIVA
     * PostCondition:
     * Se un controllo dei template, ruoli, e accessi abilitati per
     * l'utente dimostra l'abilitazione al nodo richiesto,
     * il metodo restituisce l'istanza di Albero_mainBulk che contiene
     * il nodo richiesto
     */
    public Albero_mainBulk validaNodoPerUtente(UserContext userContext, UtenteBulk utente, String cd_unita_organizzativa, String cd_nodo) throws it.cnr.jada.comp.ComponentException {
        try {
            Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext);
            if (cd_unita_organizzativa == null) {
                cd_unita_organizzativa = "*";
                esercizio = new Integer(0);
            }
            Albero_mainHome home = (Albero_mainHome) getHomeCache(userContext).getHome(Albero_mainBulk.class, "V_ALBERO_MAIN_UNITA_UTENTE");
            it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
            sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, cd_unita_organizzativa);
            sql.addSQLClause("AND", "CD_NODO", SQLBuilder.EQUALS, cd_nodo);
            sql.addSQLClause("AND", "CD_UTENTE", SQLBuilder.EQUALS, utente.getCd_utente());
            sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, esercizio);
            it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
            if (!broker.next())
                return null;
            Albero_mainBulk nodo = (Albero_mainBulk) broker.fetch(Albero_mainBulk.class);
            if (nodo.getFl_terminale() == null || !nodo.getFl_terminale().booleanValue())
                return null;
            if (nodo.getBusiness_process() == null)
                return null;
            if (utente.getFl_attiva_blocco() && nodo.getCd_accesso() != null) {
                PersistentHome ruoloBloccoHome = getHomeCache(userContext).getHome(Ruolo_bloccoBulk.class);
                SQLBuilder sqlRuoloBlocco = ruoloBloccoHome.createSQLBuilder();
                sqlRuoloBlocco.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, esercizio);
                sqlRuoloBlocco.addClause(FindClause.AND, "fl_attivo", SQLBuilder.EQUALS, Boolean.TRUE);
                sqlRuoloBlocco.addTableToHeader("RUOLO_ACCESSO");
                sqlRuoloBlocco.addSQLJoin("RUOLO_BLOCCO.CD_RUOLO", "RUOLO_ACCESSO.CD_RUOLO");
                sqlRuoloBlocco.addSQLClause(FindClause.AND, "RUOLO_ACCESSO.CD_ACCESSO", SQLBuilder.EQUALS, nodo.getCd_accesso());
                java.util.List listBlocco = ruoloBloccoHome.fetchAll(sqlRuoloBlocco);
                if (!listBlocco.isEmpty())
                    throw new ApplicationException("Accesso non consentito");
            }
            return nodo;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public UtenteBulk validaUtente(UserContext userContext, UtenteBulk utente) throws it.cnr.jada.comp.ComponentException {
        return validaUtente(userContext, utente, VALIDA_FASE_INIZIALE);
    }
    //^^@@

    /**
     * normale
     * PreCondition:
     * Dato un oggetto che contiene il CD_UTENTE e la password digitati dall'utente che richiede accesso all'applicazione
     * PostCondition:
     * Se l'utente esiste in base dati, se la password corrisponde a quella in base dati, sarà restituito l'oggetto UtenteBulk.
     * Altrimenti si fa un Throw di un'eccezione.
     */
    //^^@@
    public UtenteBulk validaUtente(UserContext userContext, UtenteBulk utente, int faseValidazione) throws it.cnr.jada.comp.ComponentException {
        try {
            /* se faseValidazione==VALIDA_NUOVO_UTENTE_LDAP significa che proveniamo
             * da una situazione di UtenteLdapNuovo
             * cioè l'utente sta inserendo la vecchia utenza sigla e la nuova ldap
             * e va aggiornato il record utente con il nuovo userid ldap
             *
             * se faseValidazione==VALIDA_FASE_INIZIALE si presume che l'utente abbia
             * inserito il codice cd_utente_uid invece del cd_utente
             *
             * se faseValidazione==VALIDA_NUOVO_UTENTE_LDAP_ANNULLA si controlla
             * che l'utente possa ancora usare il login sigla e il controllo della
             * validità della password è stato fatto al primo passaggio
             *
             * se faseValidazione==VALIDA_FASE_INIZIALE_UTENTE_MULTIPLO significa
             * che è stato inserito l'utente di login ldap ed è stato scelto uno degli
             * N utenti sigla collegati a qusto utente ldap */
            if (utente.getCd_utente() == null)
                return null;

            if (faseValidazione != VALIDA_NUOVO_UTENTE_LDAP)
                utente.setCd_utente_uid(utente.getCd_utente().toLowerCase());

            // verifichiamo che tipo di autenticazione è ora attiva sui parametri ente
            Parametri_enteHome enteHome = (Parametri_enteHome) getHome(userContext, Parametri_enteBulk.class);
            SQLBuilder sqlEnte = enteHome.createSQLBuilder();
            sqlEnte.addClause("AND", "attivo", SQLBuilder.EQUALS, new Boolean(true));
            Parametri_enteBulk ente = (Parametri_enteBulk) getHome(userContext, Parametri_enteBulk.class).fetchAll(sqlEnte).get(0);
            UtenteHome home = null;
            UtenteBulk utenteReale = null;
            // in caso sia su ldap...
            if (ente.isAutenticazioneLdap()) {
                String userid = null;
                if (utente.getCd_utente_uid() != null)
                    userid = utente.getCd_utente_uid().toLowerCase();
                else
                    userid = utente.getCd_utente_uid();

                // in tal caso presumiamo che l'utente digitato sia il cd_utente_uid
                // per cui cerchiamo su db l'utente corrispondente
                home = (UtenteHome) getHome(userContext, utente);
                SQLBuilder sqlUtente = home.createSQLBuilder();
                sqlUtente.addClause("AND", "fl_autenticazione_ldap", SQLBuilder.EQUALS, Boolean.TRUE);
                sqlUtente.addSQLClause("AND", "lower(cd_utente_uid)", SQLBuilder.EQUALS, userid);
                //sqlUtente.addSQLClause("AND","dt_fine_validita",sqlUtente.GREATER,EJBCommonServices.getServerTimestamp());

                if (faseValidazione == VALIDA_FASE_INIZIALE_UTENTE_MULTIPLO)
                    sqlUtente.addSQLClause("AND", "cd_utente", SQLBuilder.EQUALS, utente.getUtente_multiplo());

                List result = getHome(userContext, UtenteBulk.class).fetchAll(sqlUtente);

                // contiamo prima quanti sono gli utenti validi
                int numValidi = 0;
                UtenteBulk uteValido = null;
                for (Iterator it = result.iterator(); it.hasNext(); ) {
                    UtenteBulk ute = (UtenteBulk) it.next();
                    if ((ute.getDt_fine_validita() == null || (ute.getDt_fine_validita() != null && ute.getDt_fine_validita().after(EJBCommonServices.getServerTimestamp()))) &&
                            ute.getDt_inizio_validita() != null && ute.getDt_inizio_validita().before(EJBCommonServices.getServerTimestamp())) {
                        uteValido = ute;
                        numValidi++;
                    }
                }
                if (numValidi > 1 && faseValidazione != VALIDA_NUOVO_UTENTE_LDAP) {
                    throw new UtenteMultiploException();
                }

                if (!result.isEmpty() && faseValidazione != VALIDA_NUOVO_UTENTE_LDAP) {
                    // se ne esiste uno valido lo impostiamo a quello altrimenti al primo disponibile
                    if (uteValido != null)
                        utenteReale = uteValido;
                    else
                        utenteReale = (UtenteBulk) result.get(0);
                    // in tal caso va aggiornato "utente"
                    utente.setCd_utente(utenteReale.getCd_utente());
                    utenteReale = (UtenteBulk) home.findByPrimaryKey(utente, false);
                } else { // se non viene trovato si vede se l'utente digitato sia il cd_utente
                    home = (UtenteHome) getHome(userContext, utente);
                    utenteReale = (UtenteBulk) home.findByPrimaryKey(utente, false);
                    // se è stato trovato l'utente con login digitato = cd_utente
                    // controlliamo se è obbligato ad autenticarsi con ldap
                    if (utenteReale != null) {
                        if (utenteReale.isAutenticazioneLdap()) {
                            // se siamo nel primo controllo di login controlliamo
                            // la password SIGLA e poi solleviamo una eccezione
                            // UtenteLdapNuovoException in modo che si immetta il login LDAP
                            if (faseValidazione == VALIDA_FASE_INIZIALE) {
                                UtenteBulk utenteTemp = (UtenteBulk) getHome(userContext, UtenteComuneBulk.class).findByPrimaryKey(utente);
                                // se esiste la nuova user id va usata quella
                                if (utenteTemp.getCd_utente_uid() != null)
                                    throw new UtenteLdapException();
                                // controlliamo la password di sigla in modo che al passaggio
                                // successivo venga controllata quella su ldap
                                // a meno che non sia un utente nuovo creato in sigla (dt_ultima_var_password nulla)
                                if (utenteTemp.getDt_ultima_var_password() != null) {
                                    if (!utenteTemp.getPassword().equals(utente.getPassword()))
                                        return null;
                                }
                                throw new UtenteLdapNuovoException();
                            }
                        } else {
                            // verifichiamo se è un utente nuovo creato in sigla (dt_ultima_var_password nulla)
                            if (utenteReale.getDt_ultima_var_password() == null)
                                return utenteReale;
                        }
                    }
                }
            } else {
                home = (UtenteHome) getHome(userContext, utente);
                utenteReale = (UtenteBulk) home.findByPrimaryKey(utente, false);
            }

            if (utenteReale == null || utenteReale.isUtenteTemplate())
                return null;
            if (utenteReale.isUtenteComune()) {
                utenteReale = (UtenteBulk) getHome(userContext, UtenteComuneBulk.class).findByPrimaryKey(utente);
                UtenteComuneBulk utente_comune = (UtenteComuneBulk) utenteReale;

                CdrHome persistentHome = (CdrHome) getHome(userContext, CdrBulk.class, null, getFetchPolicyName("findUO"));
                CdrBulk cdr = (CdrBulk) persistentHome.findByPrimaryKey(utente_comune.getCdr());
                getHomeCache(userContext).fetchAll(userContext, persistentHome);
                utente_comune.setCdr(cdr);
            }
            java.sql.Timestamp currDate = home.getServerTimestamp();
            if ((utenteReale.getDt_inizio_validita() != null && utenteReale.getDt_inizio_validita().after(currDate)) ||
                    (utenteReale.getDt_fine_validita() != null && utenteReale.getDt_fine_validita().before(currDate)))
                throw new UtenteNonValidoException();
            // controlliamo che l'utente sia stato utilizzato negli ultimi 6 mesi
            java.util.Calendar cal2 = java.util.Calendar.getInstance();
            cal2.setTime(currDate);
            cal2.add(Calendar.MONTH, -6);
            if (!utenteReale.isAltraProc() &&
                    utenteReale.getDt_ultimo_accesso() != null && cal2.getTime().after(utenteReale.getDt_ultimo_accesso()))
                throw new UtenteInDisusoException();
            // questo controllo lo deve fare solo se non va autenticato su ldap,
            // in quanto il controllo della data nulla non ha senso perchè viene
            // valorizzata sul server ldap stesso
            if (utenteReale.getDt_ultima_var_password() == null)
                if (!ente.isAutenticazioneLdap() || !utenteReale.isAutenticazioneLdap())
                    return utenteReale;

            // l'utente va identificato su ldap
            if (ente.isAutenticazioneLdap() && utenteReale.isAutenticazioneLdap()) {
                /* ora sono possibili due situazioni, nella prima l'utente ha inserito
                 * il nuovo login e la nuova password, nella seconda ha annullato l'inserimento
                 * cosa possibile in determinati casi da verificare, in tal caso la validazione
                 * è fatta solo sul db SIGLA e non su LDAP
                 */
                if (faseValidazione == VALIDA_NUOVO_UTENTE_LDAP_ANNULLA) {
                    // verifichiamo che possa rimandare la validazione su LDAP
                    if (ente.getDt_ldap_migrazione() == null || currDate.after(ente.getDt_ldap_migrazione()))
                        return null;
                }
                if (!isUtenteAbilitatoLdap(userContext, utenteReale.getCd_utente_uid(), true)) {
                    throw new UtenteLdapNonUtenteSiglaException();
                }
            } else {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(currDate);
                cal.add(Calendar.MONTH, -6);
                if (utenteReale.getFl_password_change() != null &&
                        utenteReale.getFl_password_change().booleanValue() &&
                        cal.getTime().after(utenteReale.getDt_ultima_var_password()) &&
                        !utenteReale.isAltraProc())
                    throw new PasswordScadutaException();
            }
            if (utenteReale.getCd_utente_uid() != null) {
                java.util.List list = utentiMultipli(userContext, utenteReale);
                for (Iterator it = list.iterator(); it.hasNext(); ) {
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.setTime(currDate);
                    cal.add(Calendar.SECOND, -30);
                    UtenteBulk ute = (UtenteBulk) it.next();
                    if (ute.getCd_utente().compareTo(utenteReale.getCd_utente()) != 0) {
                        ute = (UtenteBulk) home.findByPrimaryKey(ute, false);
                        ute.setDt_ultimo_accesso(new Timestamp(cal.getTime().getTime()));
                        ute.setUser("LOGIN");
                        updateBulk(userContext, ute);
                    }
                }
            }
            // passata la validazione aggiorniamo la data ultimo accesso
            utenteReale.setDt_ultimo_accesso(currDate);
            utenteReale.setUser("LOGIN");
            updateBulk(userContext, utenteReale);

            return utenteReale;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public boolean isUtenteAbilitatoLdap(UserContext userContext, String uid, boolean masterLdap) throws it.cnr.jada.comp.ComponentException {
        try {
            LDAPService ldapService = SpringUtil.getBean(LDAPService.class);
            return ldapService
                    .findPersonById(uid)
                    .flatMap(person -> Optional.ofNullable(person.getCnrapp1()))
                    .map(s -> s.equalsIgnoreCase("si"))
                    .orElse(Boolean.FALSE);
        } catch (NoSuchBeanDefinitionException _ex){
            return true;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    /**
     * Se abilita = true, viene abilitato l'utente userID in LDAP per accedere in SIGLA
     * se abilita = false, viene disabilitato l'utente userID in LDAP per accedere in SIGLA
     *
     * @param userContext
     * @param uid
     * @param abilita
     * @throws it.cnr.jada.comp.ComponentException
     */
    public void cambiaAbilitazioneUtente(UserContext userContext, String uid, boolean abilita) throws it.cnr.jada.comp.ComponentException {
        try {
            LDAPService ldapService = SpringUtil.getBean(LDAPService.class);
            final Optional<Person> personById = ldapService.findPersonById(uid);
            if (personById.isPresent()) {
                personById.get().setCnrapp1(abilita ? "si" : "no");
                ldapService.save(personById.get());
            }
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public List utentiMultipli(UserContext userContext, UtenteBulk utente) throws it.cnr.jada.comp.ComponentException {
        try {
            UtenteHome home = (UtenteHome) getHome(userContext, UtenteBulk.class);
            SQLBuilder sqlUtente = home.createSQLBuilder();
            sqlUtente.addClause("AND", "fl_autenticazione_ldap", SQLBuilder.EQUALS, Boolean.TRUE);
            if (utente.getCd_utente_uid() != null)
                sqlUtente.addSQLClause("AND", "lower(cd_utente_uid)", SQLBuilder.EQUALS, utente.getCd_utente_uid().toLowerCase());
            else
                sqlUtente.addSQLClause("AND", "lower(cd_utente_uid)", SQLBuilder.EQUALS, utente.getCd_utente().toLowerCase());
            sqlUtente.openParenthesis("AND");
            sqlUtente.addSQLClause("AND", "dt_fine_validita", SQLBuilder.GREATER, EJBCommonServices.getServerTimestamp());
            sqlUtente.addSQLClause("OR", "dt_fine_validita is NULL");
            sqlUtente.closeParenthesis();
            sqlUtente.addOrderBy("dt_ultimo_accesso desc");
            List result = getHome(userContext, UtenteBulk.class).fetchAll(sqlUtente);
            return result;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<PreferitiBulk> preferitiList(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        PreferitiHome home = (PreferitiHome) getHomeCache(userContext).getHome(PreferitiBulk.class);
        try {
            SQLBuilder sql = home.selectByClause(userContext, null);
            return home.fetchAll(sql);
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    public List getRuoli(UserContext userContext, UtenteBulk utente) throws it.cnr.jada.comp.ComponentException {
        try {
            Utente_unita_ruoloHome home = (Utente_unita_ruoloHome) getHome(userContext, Utente_unita_ruoloBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            sql.addSQLClause("AND", "CD_UTENTE", SQLBuilder.EQUALS, utente.getCd_utente());
            List result = getHome(userContext, Utente_unita_ruoloBulk.class).fetchAll(sql);
            return result;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public List getUnitaRuolo(UserContext userContext, UtenteBulk utente) throws it.cnr.jada.comp.ComponentException {
        try {
            Utente_unita_ruoloHome home = (Utente_unita_ruoloHome) getHome(userContext, Utente_unita_ruoloBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            sql.addClause("AND", "CD_UTENTE", SQLBuilder.EQUALS, utente.getCd_utente());
            List result = getHome(userContext, UtenteBulk.class).fetchAll(sql);
            return result;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public boolean isUserAccessoAllowed(Principal principal, Integer esercizio, String cds, String uo, String... accessi) throws ComponentException {
        CNRUserContext context = new CNRUserContext();
        context.setEsercizio(esercizio);
        context.setCd_cds(cds);
        context.setCd_unita_organizzativa(uo);
        UtenteBulk utenteBulk = new UtenteBulk();
        final Optional<IDToken> idToken = Optional.ofNullable(principal)
                .filter(KeycloakPrincipal.class::isInstance)
                .map(KeycloakPrincipal.class::cast)
                .map(KeycloakPrincipal::getKeycloakSecurityContext)
                .map(keycloakSecurityContext -> {
                    return Optional.ofNullable(keycloakSecurityContext.getIdToken())
                            .orElse(keycloakSecurityContext.getToken());
                });
        if (idToken.isPresent()) {
            utenteBulk.setCd_utente(idToken.get().getPreferredUsername());
            utenteBulk.setCd_utente_uid(idToken.get().getPreferredUsername());
            return Optional.ofNullable(validaUtente(context, utenteBulk))
                    .map(utenteBulk1 -> {
                        context.setUser(utenteBulk.getCd_utente());
                        try {
                            return isUserAccessoAllowed(context, accessi);
                        } catch (ComponentException e) {
                            return Boolean.FALSE;
                        }
                    }).orElse(Boolean.FALSE);
        }
        return Boolean.FALSE;
    }
}