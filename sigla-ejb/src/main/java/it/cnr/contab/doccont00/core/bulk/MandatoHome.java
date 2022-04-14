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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk;
import it.cnr.contab.doccont00.tabrif.bulk.CupBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import javax.ejb.EJBException;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class MandatoHome extends BulkHome {
    public MandatoHome(Class clazz, java.sql.Connection conn) {
        super(clazz, conn);
    }

    public MandatoHome(Class clazz, java.sql.Connection conn, PersistentCache persistentCache) {
        super(clazz, conn, persistentCache);
    }

    /**
     * <!-- @TODO: da completare -->
     * Costruisce un MandatoHome
     *
     * @param conn La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     */
    public MandatoHome(java.sql.Connection conn) {
        super(MandatoBulk.class, conn);
    }

    /**
     * <!-- @TODO: da completare -->
     * Costruisce un MandatoHome
     *
     * @param conn            La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     * @param persistentCache La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
     */
    public MandatoHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(MandatoBulk.class, conn, persistentCache);
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param mandato
     * @return
     * @throws PersistencyException
     */
    public Timestamp findDataUltimoMandatoPerCds(MandatoBulk mandato) throws PersistencyException {
        try {
            LoggableStatement ps = new LoggableStatement(getConnection(),
                    "SELECT TRUNC(MAX(DT_EMISSIONE)) " +
                            "FROM " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "MANDATO WHERE " +
                            "ESERCIZIO = ? AND CD_CDS = ?", true, this.getClass());
            try {
                ps.setObject(1, mandato.getEsercizio());
                ps.setString(2, mandato.getCds().getCd_unita_organizzativa());

                ResultSet rs = ps.executeQuery();
                try {
                    if (rs.next())
                        return rs.getTimestamp(1);
                    else
                        return null;
                } catch (SQLException e) {
                    throw new PersistencyException(e);
                } finally {
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                    }
                    ;
                }
            } catch (SQLException e) {
                throw new PersistencyException(e);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
                ;
            }
        } catch (SQLException e) {
            throw new PersistencyException(e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param mandato
     * @return
     * @throws PersistencyException
     * @throws IntrospectionException
     */
    public abstract Collection findMandato_riga(it.cnr.jada.UserContext userContext, MandatoBulk mandato) throws PersistencyException, IntrospectionException;

    /**
     * <!-- @TODO: da completare -->
     *
     * @param mandato
     * @return
     * @throws PersistencyException
     * @throws IntrospectionException
     */
    public abstract Mandato_terzoBulk findMandato_terzo(it.cnr.jada.UserContext userContext, MandatoBulk mandato) throws PersistencyException, IntrospectionException;

    /**
     * Metodo per cercare i sospesi associati al mandato.
     *
     * @param mandato <code>MandatoBulk</code> il mandato
     * @return result i sospesi associati al mandato
     */
    public Collection findSospeso_det_usc(it.cnr.jada.UserContext userContext, MandatoBulk mandato) throws PersistencyException, IntrospectionException {
        PersistentHome home = getHomeCache().getHome(Sospeso_det_uscBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "esercizio", sql.EQUALS, mandato.getEsercizio());
        sql.addClause("AND", "cd_cds_mandato", sql.EQUALS, mandato.getCd_cds());
        sql.addClause("AND", "pg_mandato", sql.EQUALS, mandato.getPg_mandato());
        sql.addClause("AND", "ti_sospeso_riscontro", sql.EQUALS, SospesoBulk.TI_SOSPESO);
//	sql.addClause( "AND", "stato", sql.EQUALS, Sospeso_det_uscBulk.STATO_DEFAULT);	
        Collection result = home.fetchAll(sql);
        getHomeCache().fetchAll(userContext);
        return result;
    }

    /**
     * Imposta il pg_mandato di un oggetto <code>MandatoBulk</code>.
     *
     * @param mandato <code>OggettoBulkBulk</code>
     * @throws PersistencyException
     */

    public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws PersistencyException, ComponentException {
        try {
            MandatoBulk mandato = (MandatoBulk) bulk;
            Long pg;
            Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache().getHome(Numerazione_doc_contBulk.class);
            if (Utility.createParametriCnrComponentSession().getParametriCnr(userContext, mandato.getEsercizio()).getFl_tesoreria_unica().booleanValue()) {
                Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) (getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0));
                pg = numHome.getNextPg(userContext, mandato.getEsercizio(), uoEnte.getCd_cds(), Numerazione_doc_contBulk.TIPO_MAN, mandato.getUser());
            } else {
                pg = numHome.getNextPg(userContext, mandato.getEsercizio(), mandato.getCd_cds(), Numerazione_doc_contBulk.TIPO_MAN, mandato.getUser());
            }
            mandato.setPg_mandato(pg);
        } catch (IntrospectionException e) {
            throw new PersistencyException(e);
        } catch (ApplicationException e) {
            throw new ComponentException(e);
        } catch (RemoteException e) {
            throw new ComponentException(e);
        } catch (EJBException e) {
            throw new ComponentException(e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param bulk
     * @return
     * @throws PersistencyException
     */
    public java.util.Hashtable loadTipoDocumentoKeys(MandatoBulk bulk) throws PersistencyException {
        SQLBuilder sql = getHomeCache().getHome(Tipo_documento_ammBulk.class).createSQLBuilder();
        sql.addClause("AND", "ti_entrata_spesa", sql.EQUALS, "S");
        List result = getHomeCache().getHome(Tipo_documento_ammBulk.class).fetchAll(sql);
        Hashtable ht = new Hashtable();
        Tipo_documento_ammBulk tipo;
        for (Iterator i = result.iterator(); i.hasNext(); ) {
            tipo = (Tipo_documento_ammBulk) i.next();
            ht.put(tipo.getCd_tipo_documento_amm(), tipo.getDs_tipo_documento_amm());
        }
        return ht;
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param bulk
     * @return
     * @throws PersistencyException
     */
    public java.util.Hashtable loadTipoDocumentoPerRicercaKeys(MandatoBulk bulk) throws PersistencyException {
        SQLBuilder sql = getHomeCache().getHome(Tipo_documento_ammBulk.class).createSQLBuilder();
//	sql.addClause( "AND", "ti_entrata_spesa", sql.EQUALS, "S" );
        sql.openParenthesis("AND");
        sql.addSQLClause("AND", "fl_manrev_utente", sql.EQUALS, "M");
        sql.addSQLClause("OR", "fl_manrev_utente", sql.EQUALS, "E");
        sql.closeParenthesis();
        List result = getHomeCache().getHome(Tipo_documento_ammBulk.class).fetchAll(sql);
        Hashtable ht = new Hashtable();
        Tipo_documento_ammBulk tipo;
        for (Iterator i = result.iterator(); i.hasNext(); ) {
            tipo = (Tipo_documento_ammBulk) i.next();
            ht.put(tipo.getCd_tipo_documento_amm(), tipo.getDs_tipo_documento_amm());
        }
        return ht;
    }

    /**
     *
     * @param mandato
     * @return
     * @throws IntrospectionException
     * @throws PersistencyException
     */
    public java.util.List findMandato(MandatoBulk mandato) throws IntrospectionException, PersistencyException {
        final SQLBuilder sql = createSQLBuilder();
        Optional.ofNullable(mandato)
                .ifPresent(mandatoBulk -> sql.addClause(mandatoBulk.buildFindClauses(null)));
        return fetchAll(sql);
    }

    /**
     * Recupera tutti i CUP collegati al Mandato.
     *
     * @param mandatoBulk Mandato in uso.
     * @return java.util.Collection Collezione di oggetti <code>CUP</code>
     */
    public java.util.Collection<CupBulk> findCodiciSiopeCupCollegati(UserContext usercontext, MandatoBulk mandatoBulk) throws PersistencyException {
        PersistentHome mandatoSiopeCupHome = getHomeCache().getHome(MandatoSiopeCupIBulk.class);
        SQLBuilder sql = mandatoSiopeCupHome.createSQLBuilder();
        sql.setAutoJoins(true);
        sql.generateJoin("mandato_siopeI", "MANDATO_SIOPE");

        sql.addSQLClause(FindClause.AND, "MANDATO_SIOPE.CD_CDS", SQLBuilder.EQUALS, mandatoBulk.getCd_cds());
        sql.addSQLClause(FindClause.AND, "MANDATO_SIOPE.ESERCIZIO", SQLBuilder.EQUALS, mandatoBulk.getEsercizio());
        sql.addSQLClause(FindClause.AND, "MANDATO_SIOPE.PG_MANDATO", SQLBuilder.EQUALS, mandatoBulk.getPg_mandato());
        final Stream<MandatoSiopeCupBulk> stream = mandatoSiopeCupHome.fetchAll(sql)
                .stream()
                .filter(MandatoSiopeCupBulk.class::isInstance)
                .map(MandatoSiopeCupBulk.class::cast);
        getHomeCache().fetchAll(usercontext);
        return stream.filter(mandatoSiopeCupBulk -> Optional.ofNullable(mandatoSiopeCupBulk.getCdCup()).isPresent())
                .map(MandatoSiopeCupBulk::getCup).collect(Collectors.toList());
    }

    /**
     * Recupera tutti i Codici CUP collegati al Mandato.
     *
     * @param mandatoBulk Mandato in uso.
     * @return java.util.Collection Collezione di oggetti <code>CUP</code>
     */
    public java.util.Collection<String> findCodiciCupCollegati(UserContext usercontext, MandatoBulk mandatoBulk) throws PersistencyException {
        PersistentHome mandatoCupHome = getHomeCache().getHome(MandatoCupIBulk.class);
        SQLBuilder sql = mandatoCupHome.createSQLBuilder();
        sql.setAutoJoins(true);
        sql.generateJoin("mandato_rigaI", "MANDATO_RIGA");

        sql.addSQLClause(FindClause.AND, "MANDATO_RIGA.CD_CDS", SQLBuilder.EQUALS, mandatoBulk.getCd_cds());
        sql.addSQLClause(FindClause.AND, "MANDATO_RIGA.ESERCIZIO", SQLBuilder.EQUALS, mandatoBulk.getEsercizio());
        sql.addSQLClause(FindClause.AND, "MANDATO_RIGA.PG_MANDATO", SQLBuilder.EQUALS, mandatoBulk.getPg_mandato());
        final Stream<MandatoCupBulk> stream = mandatoCupHome.fetchAll(sql)
                .stream()
                .filter(MandatoCupBulk.class::isInstance)
                .map(MandatoCupBulk.class::cast);
        return stream.map(t -> t.getCdCup())
                .distinct()
                .collect(Collectors.toList());
    }
    public MandatoBulk findAndLockMandatoAnnullato(it.cnr.jada.UserContext userContext,java.lang.String cdCds, java.lang.Integer esercizio, java.lang.Long pgMandato) throws PersistencyException, OutdatedResourceException, BusyResourceException {

        return findAndLockMandato(cdCds, esercizio, pgMandato, true);
    }

    private MandatoBulk findAndLockMandato(String cdCds, Integer esercizio, Long pgMandato, Boolean annullato) throws PersistencyException, OutdatedResourceException, BusyResourceException {
        SQLBuilder sql = createSQLBuilder();
        sql.addClause("AND", "cd_cds", sql.EQUALS, cdCds);
        sql.addClause("AND", "esercizio", sql.EQUALS, esercizio);
        sql.addClause("AND", "pg_mandato", sql.EQUALS, pgMandato);
        sql.addClause("AND", "stato", annullato ? sql.EQUALS : sql.NOT_EQUALS, MandatoBulk.STATO_MANDATO_ANNULLATO);
        List mandati = fetchAll(sql);
        if (mandati == null || mandati.size() == 0) {
            return null;
        } else if (mandati.size() == 1) {
            MandatoBulk man = (MandatoBulk) mandati.get(0);
            lock(man);
            return man;
        } else {
            throw new PersistencyException("Errore nel recupero del Mandato " + esercizio + "-" + pgMandato);
        }
    }

    public MandatoBulk findAndLockMandatoNonAnnullato(it.cnr.jada.UserContext userContext,java.lang.String cdCds, java.lang.Integer esercizio, java.lang.Long pgMandato) throws PersistencyException, OutdatedResourceException, BusyResourceException {
        return findAndLockMandato(cdCds, esercizio, pgMandato, false);
    }
}
