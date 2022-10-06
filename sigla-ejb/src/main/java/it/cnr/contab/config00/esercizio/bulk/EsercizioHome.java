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

package it.cnr.contab.config00.esercizio.bulk;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SQLExceptionHandler;
import it.cnr.jada.util.PropertyNames;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Home che gestisce l'esercizio contabile.
 */
public class EsercizioHome extends BulkHome {
    public final static java.sql.Timestamp DATA_INFINITO;

    static {
        // (29/10/2002 17:29:52) CNRADM
        // Imposto la data infinito usando Calendar perchè così sono sicuro
        // che sia relativa al timezone corrente
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(cal.YEAR, 2200);
        cal.set(cal.MONTH, cal.DECEMBER);
        cal.set(cal.DAY_OF_MONTH, 31);
        cal.set(cal.HOUR, 1);
        cal.set(cal.MINUTE, 0);
        cal.set(cal.SECOND, 0);
        cal.set(cal.MILLISECOND, 0);
        cal.set(cal.AM_PM, cal.AM);
        DATA_INFINITO = new Timestamp(cal.getTime().getTime());
    }

    /**
     * Costruttore ESERCIZIO HOME
     *
     * @param conn connessione db
     */
    public EsercizioHome(java.sql.Connection conn) {
        super(EsercizioBulk.class, conn);
    }

    /**
     * Costruttore ESERCIZIO HOME
     *
     * @param conn            connessione db
     * @param persistentCache Cache modelli
     */
    public EsercizioHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(EsercizioBulk.class, conn, persistentCache);
    }

    /**
     * Invocazione delle stored procedure Oracle CNRCTB050.APRIPDG(?,?,?)
     *
     * @param esercizio oggetto bulk in processo
     * @throws PersistencyException
     */
    public void callApriPdGProcedure(EsercizioBulk esercizio) throws PersistencyException {
        try {
            /* CNRCTB050.APRIPDG(?, ?, ?);) */
            LoggableStatement cs = new LoggableStatement(getConnection(),
                    "{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                            + "CNRCTB050.APRIPDG(?,?,?)}", false, this.getClass());
            try {
                cs.setObject(1, esercizio.getEsercizio());
                cs.setString(2, esercizio.getCd_cds());
                cs.setString(3, esercizio.getUser());
                cs.executeQuery();
            } catch (SQLException e) {
                throw new PersistencyException(e);
            } finally {
                cs.close();
            }
        } catch (SQLException e) {
            throw new PersistencyException(e);
        }
    }

    /**
     * Metodo che restituisce il valore dell'esercizio contabile precedente (a
     * quello attuale), nel caso esista.
     *
     * @return esercizioPrecente Variabile di tipo <code>EsercizioBulk</code> che
     * contiene il valore dell'esercizio contabile
     * precedente.
     */
    public EsercizioBulk findEsercizioPrecedente(EsercizioBulk esercizioCorrente) throws IntrospectionException, PersistencyException {
        EsercizioBulk esercizioPrecente = (EsercizioBulk) findByPrimaryKey(new EsercizioBulk(esercizioCorrente.getCd_cds(), new Integer(esercizioCorrente.getEsercizio().intValue() - 1)));
        return esercizioPrecente;

    }

    /**
     * Metodo che restituisce il valore dell'esercizio contabile successivo (a
     * quello attuale), nel caso esista.
     *
     * @return Variabile di tipo <code>EsercizioBulk</code> che
     * contiene il valore dell'esercizio contabile
     * successivo.
     */
    public EsercizioBulk findEsercizioSuccessivo(EsercizioBulk esercizioCorrente) throws IntrospectionException, PersistencyException {
        return (EsercizioBulk) findByPrimaryKey(new EsercizioBulk(esercizioCorrente.getCd_cds(), new Integer(esercizioCorrente.getEsercizio().intValue() + 1)));

    }

    /**
     * Metodo che restituisce il valore degli esercizi contabili (APERTI) successivi (a
     * quello attuale), nel caso esistano.
     *
     * @return Variabile di tipo <code>EsercizioBulk</code> che
     * contiene il valore dell'esercizio contabile
     * successivo.
     */
    public List findEserciziSuccessivi(EsercizioBulk esercizioCorrente) throws IntrospectionException, PersistencyException {
        SQLBuilder sql = createSQLBuilder();
        sql.addClause("AND", "esercizio", sql.GREATER, new Integer(esercizioCorrente.getEsercizio().intValue()));
        sql.addClause("AND", "cd_cds", sql.EQUALS, esercizioCorrente.getCd_cds());
        sql.addClause("AND", "st_apertura_chiusura", sql.EQUALS, EsercizioBulk.STATO_APERTO);
        return fetchAll(sql);
    }

    /**
     * Controllo se l'esercizio di scrivania e' aperto
     * <p>
     * Nome: Controllo chiusura esercizio
     * Pre:  E' stata richiesta la creazione o modifica di una scrittura
     * Post: Viene chiamata una stored procedure che restituisce
     * -		'Y' se il campo stato della tabella CHIUSURA_COEP vale C
     * -		'N' altrimenti
     * Se l'esercizio e' chiuso e' impossibile proseguire
     *
     * @param userContext <code>UserContext</code>
     * @return boolean : TRUE se stato = C
     * FALSE altrimenti
     */
    public boolean isEsercizioChiuso(it.cnr.jada.UserContext userContext) throws PersistencyException {
        return isEsercizioChiuso(
                userContext,
                it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),
                it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
    }

    /**
     * Controllo se l'esercizio di scrivania e' aperto
     * <p>
     * Nome: Controllo chiusura esercizio
     * Pre:  E' stata richiesta la creazione o modifica di una scrittura
     * Post: Viene chiamata una stored procedure che restituisce
     * -		'Y' se il campo stato della tabella CHIUSURA_COEP vale C
     * -		'N' altrimenti
     * Se l'esercizio e' chiuso e' impossibile proseguire
     *
     * @param userContext <code>UserContext</code>
     * @return boolean : TRUE se stato = C
     * FALSE altrimenti
     */
    public boolean isEsercizioChiuso(it.cnr.jada.UserContext userContext, Integer esercizio, it.cnr.contab.config00.sto.bulk.CdrBulk cdr) throws PersistencyException {
        return isEsercizioChiuso(userContext, esercizio, cdr.getUnita_padre().getCd_unita_padre());
    }

    /**
     * Controllo se l'esercizio di scrivania e' aperto
     * <p>
     * Nome: Controllo chiusura esercizio
     * Pre:  E' stata richiesta la creazione o modifica di una scrittura
     * Post: Viene chiamata una stored procedure che restituisce
     * -		'Y' se il campo stato della tabella CHIUSURA_COEP vale C
     * -		'N' altrimenti
     * Se l'esercizio e' chiuso e' impossibile proseguire
     *
     * @param userContext <code>UserContext</code>
     * @return boolean : TRUE se stato = C
     * FALSE altrimenti
     */
    public boolean isEsercizioChiuso(it.cnr.jada.UserContext userContext, Integer esercizio, String cd_cds) throws PersistencyException {
        try {

            if (findByPrimaryKey(new EsercizioBulk(cd_cds, esercizio)) == null)
                return false;

            LoggableStatement cs = new LoggableStatement(getConnection(),
                    PropertyNames.getProperty("package.cnrctb008.isEsercizioChiusoYesNo"), false, this.getClass());

            try {
                cs.registerOutParameter(1, java.sql.Types.CHAR);
                cs.setObject(2, esercizio);
                cs.setObject(3, cd_cds);

                cs.execute();

                return "Y".equals(cs.getString(1));
            } finally {
                cs.close();
            }
        } catch (java.sql.SQLException e) {
            throw SQLExceptionHandler.getInstance().handleSQLException(e);
        }
    }

    /**
     * Controllo se l'esercizio di scrivania e' aperto
     * <p>
     * Nome: Controllo chiusura esercizio
     * Pre:  E' stata richiesta la creazione o modifica di una scrittura
     * Post: Viene chiamata una stored procedure che restituisce
     * -		'Y' se il campo stato della tabella CHIUSURA_COEP vale C
     * -		'N' altrimenti
     * Se l'esercizio e' chiuso e' impossibile proseguire
     *
     * @param userContext <code>UserContext</code>
     * @return boolean : TRUE se stato = C
     * FALSE altrimenti
     */
    public boolean isEsercizioChiusoPerAlmenoUnCds(it.cnr.jada.UserContext userContext) throws PersistencyException {
        return isEsercizioChiusoPerAlmenoUnCds(
                userContext,
                it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
    }

    /**
     * Controllo se l'esercizio di scrivania e' aperto
     * <p>
     * Nome: Controllo chiusura esercizio
     * Pre:  E' stata richiesta la creazione o modifica di una scrittura
     * Post: Viene chiamata una stored procedure che restituisce
     * -		'Y' se il campo stato della tabella CHIUSURA_COEP vale C
     * -		'N' altrimenti
     * Se l'esercizio e' chiuso e' impossibile proseguire
     *
     * @param userContext <code>UserContext</code>
     * @return boolean : TRUE se stato = C
     * FALSE altrimenti
     */
    public boolean isEsercizioChiusoPerAlmenoUnCds(it.cnr.jada.UserContext userContext, Integer esercizio) throws PersistencyException {
        try {
            LoggableStatement cs = new LoggableStatement(getConnection(),
                    "{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                            + "CNRCTB008.isEsChiusoPerAlmenoUnCdsYesNo(?)}", false, this.getClass());

            try {
                cs.registerOutParameter(1, java.sql.Types.CHAR);
                cs.setObject(2, esercizio);

                cs.execute();

                return "Y".equals(cs.getString(1));
            } finally {
                cs.close();
            }
        } catch (java.sql.SQLException e) {
            throw SQLExceptionHandler.getInstance().handleSQLException(e);
        }
    }

    /**
     * Metodo che restituisce il valore dell'esercizio contabile precedente (a
     * quello attuale), nel caso esista.
     *
     * @return esercizioPrecente Variabile di tipo <code>EsercizioBulk</code> che
     * contiene il valore dell'esercizio contabile
     * precedente.
     */
    public boolean verificaEsercizi2AnniPrecedenti(EsercizioBulk esercizioCorrente) throws IntrospectionException, PersistencyException {
        SQLBuilder sql = createSQLBuilder();
        sql.addClause("AND", "esercizio", sql.EQUALS, new Integer(esercizioCorrente.getEsercizio().intValue() - 2));
        sql.addClause("AND", "st_apertura_chiusura", sql.NOT_EQUALS, EsercizioBulk.STATO_CHIUSO_DEF);
        try {
            int result = sql.executeCountQuery(getConnection());
            return (result == 0);
        } catch (SQLException e) {
            throw new PersistencyException(e);
        }
    }

    public boolean isEsercizioAperto(it.cnr.jada.UserContext userContext, Integer esercizio, String cd_cds) throws PersistencyException {
        try {

            if (findByPrimaryKey(new EsercizioBulk(cd_cds, esercizio)) == null)
                return false;

            LoggableStatement cs = new LoggableStatement(getConnection(),
                    "{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
                            + "CNRCTB008.isEsercizioApertoYesNo(?,?)}", false, this.getClass());

            try {
                cs.registerOutParameter(1, java.sql.Types.CHAR);
                cs.setObject(2, esercizio);
                cs.setObject(3, cd_cds);

                cs.execute();

                return "Y".equals(cs.getString(1));
            } finally {
                cs.close();
            }
        } catch (java.sql.SQLException e) {
            throw SQLExceptionHandler.getInstance().handleSQLException(e);
        }
    }

    public List<EsercizioBulk> findEsercizi(UserContext userContext, String cds) throws PersistencyException {
        final SQLBuilder sqlBuilder = createSQLBuilder();
        Integer esercizioPartenza = Optional.ofNullable(
                    ((Configurazione_cnrHome) getHomeCache().getHome(Configurazione_cnrBulk.class))
                            .getConfigurazione(
                                    null,
                                    Configurazione_cnrBulk.PK_ESERCIZIO_SPECIALE,
                                    Configurazione_cnrBulk.SK_ESERCIZIO_PARTENZA
                            )
                )
                .flatMap(configurazione_cnrBulk -> Optional.ofNullable(configurazione_cnrBulk.getIm01()))
                .map(BigDecimal::intValue)
                .orElse(Integer.MIN_VALUE);
        sqlBuilder.addClause(FindClause.AND, "esercizio", SQLBuilder.GREATER_EQUALS, esercizioPartenza);
        sqlBuilder.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, cds);
        return fetchAll(sqlBuilder);
    }
}
