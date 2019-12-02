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
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import javax.ejb.EJBException;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public abstract class ReversaleHome extends BulkHome {
    public ReversaleHome(Class clazz, java.sql.Connection conn) {
        super(clazz, conn);
    }

    public ReversaleHome(Class clazz, java.sql.Connection conn, PersistentCache persistentCache) {
        super(clazz, conn, persistentCache);
    }

    /**
     * <!-- @TODO: da completare -->
     * Costruisce un ReversaleHome
     *
     * @param conn La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     */
    public ReversaleHome(java.sql.Connection conn) {
        super(ReversaleBulk.class, conn);
    }

    /**
     * <!-- @TODO: da completare -->
     * Costruisce un ReversaleHome
     *
     * @param conn            La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     * @param persistentCache La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
     */
    public ReversaleHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(ReversaleBulk.class, conn, persistentCache);
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param reversale
     * @return
     * @throws PersistencyException
     */
    public Timestamp findDataUltimaReversalePerCds(ReversaleBulk reversale) throws PersistencyException {
        try {
            LoggableStatement ps = new LoggableStatement(getConnection(),
                    "SELECT TRUNC(MAX(DT_EMISSIONE)) " +
                            "FROM " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "REVERSALE WHERE " +
                            "ESERCIZIO = ? AND CD_CDS = ?", true, this.getClass());
            try {
                ps.setObject(1, reversale.getEsercizio());
                ps.setString(2, reversale.getCds().getCd_unita_organizzativa());

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
                }
            } catch (SQLException e) {
                throw new PersistencyException(e);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
            }
        } catch (SQLException e) {
            throw new PersistencyException(e);
        }
    }

    /**
     * <!-- @TODO: da completare -->
     *
     * @param reversale
     * @return
     * @throws PersistencyException
     * @throws IntrospectionException
     */
    public abstract Collection findReversale_riga(it.cnr.jada.UserContext userContext, ReversaleBulk reversale) throws PersistencyException, IntrospectionException;

    /**
     * <!-- @TODO: da completare -->
     *
     * @param reversale
     * @return
     * @throws PersistencyException
     * @throws IntrospectionException
     */
    public abstract Reversale_terzoBulk findReversale_terzo(it.cnr.jada.UserContext userContext, ReversaleBulk reversale) throws PersistencyException, IntrospectionException;

    /**
     * Metodo per cercare i sospesi associati alla reversale.
     *
     * @param reversale <code>ReversaleBulk</code> la reversale
     * @return result i sospesi associati alla reversale
     */
    public Collection findSospeso_det_etr(it.cnr.jada.UserContext userContext, ReversaleBulk reversale) throws PersistencyException, IntrospectionException {
        PersistentHome home = getHomeCache().getHome(Sospeso_det_etrBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, reversale.getEsercizio());
        sql.addClause("AND", "cd_cds_reversale", SQLBuilder.EQUALS, reversale.getCd_cds());
        sql.addClause("AND", "pg_reversale", SQLBuilder.EQUALS, reversale.getPg_reversale());
        sql.addClause("AND", "ti_sospeso_riscontro", SQLBuilder.EQUALS, SospesoBulk.TI_SOSPESO);
//	sql.addClause( "AND", "stato", sql.EQUALS, Sospeso_det_etrBulk.STATO_DEFAULT);	
        Collection result = home.fetchAll(sql);
        getHomeCache().fetchAll(userContext);
        return result;
    }

    /**
     * Imposta il pg_reversale di un oggetto <code>ReversaleBulk</code>.
     *
     * @param reversale <code>OggettoBulkBulk</code>
     * @throws PersistencyException
     */

    public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws PersistencyException, ComponentException {
        try {
            ReversaleBulk reversale = (ReversaleBulk) bulk;
            Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache().getHome(Numerazione_doc_contBulk.class);

            Long pg;
            if (Utility.createParametriCnrComponentSession().getParametriCnr(userContext, reversale.getEsercizio()).getFl_tesoreria_unica().booleanValue()) {
                Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) (getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0));
                pg = numHome.getNextPg(userContext, reversale.getEsercizio(), uoEnte.getCd_cds(), reversale.getCd_tipo_documento_cont(), reversale.getUser());
            } else {
                pg = numHome.getNextPg(userContext, reversale.getEsercizio(), reversale.getCd_cds(), reversale.getCd_tipo_documento_cont(), reversale.getUser());
            }
            reversale.setPg_reversale(pg);
        } catch (IntrospectionException e) {
            throw new PersistencyException(e);

        } catch (RemoteException e) {
            throw new ComponentException(e);
        } catch (EJBException e) {
            throw new ComponentException(e);
        } catch (ApplicationException e) {
            throw new ComponentException(e);
        }
    }

    /**
     * Carica la reversale <reversale> con tutti gli oggetti complessi
     *
     * @param reversale
     * @return
     * @throws PersistencyException
     */
    public abstract ReversaleBulk loadReversale(it.cnr.jada.UserContext userContext, String cdCds, Integer esercizio, Long pgReversale) throws PersistencyException, IntrospectionException;

    /**
     * <!-- @TODO: da completare -->
     *
     * @param bulk
     * @return
     * @throws PersistencyException
     */
    public java.util.Hashtable loadTipoDocumentoKeys(ReversaleBulk bulk) throws PersistencyException {
        SQLBuilder sql = getHomeCache().getHome(Tipo_documento_ammBulk.class).createSQLBuilder();
        sql.addClause("AND", "ti_entrata_spesa", SQLBuilder.EQUALS, "E");
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
    public java.util.Hashtable loadTipoDocumentoPerRicercaKeys(ReversaleBulk bulk) throws PersistencyException {
        SQLBuilder sql = getHomeCache().getHome(Tipo_documento_ammBulk.class).createSQLBuilder();
//	sql.addClause( "AND", "ti_entrata_spesa", sql.EQUALS, "E" );
        sql.openParenthesis("AND");
        sql.addSQLClause("AND", "fl_manrev_utente", SQLBuilder.EQUALS, "R");
        sql.addSQLClause("OR", "fl_manrev_utente", SQLBuilder.EQUALS, "E");
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
     * @param reversale
     * @return
     * @throws IntrospectionException
     * @throws PersistencyException
     */
    public java.util.List findReversale(ReversaleBulk reversale) throws IntrospectionException, PersistencyException {
        final SQLBuilder sql = createSQLBuilder();
        Optional.ofNullable(reversale)
                .ifPresent(mandatoBulk -> sql.addClause(mandatoBulk.buildFindClauses(null)));
        return fetchAll(sql);
    }
}
