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

package it.cnr.contab.pdg00.comp;
/**
 * Insert the type's description here.
 * Creation date: (08/10/2001 15:39:09)
 *
 * @author: CNRADM
 */

import it.cnr.contab.logs.bulk.Batch_log_rigaBulk;
import it.cnr.contab.pdg00.cdip.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * @author Tilde
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ElaboraFileStipendiComponent extends it.cnr.jada.comp.CRUDComponent {

    public V_stipendi_cofi_dettBulk cercaFileStipendi(UserContext userContext, V_stipendi_cofi_dettBulk dett) throws it.cnr.jada.comp.ComponentException {

        try {
            V_stipendi_cofi_dettHome dettHome = (V_stipendi_cofi_dettHome) getHome(userContext, dett.getClass());

            Collection col = dettHome.findDett(userContext, dett);
            for (java.util.Iterator i = col.iterator(); i.hasNext(); ) {
                V_stipendi_cofi_dettBulk v = (V_stipendi_cofi_dettBulk) i.next();
                if (v.getEntrata_spesa() != null) {
                    dett.getV_stipendi_cofi_dett().add(v);
                }
            }
            Stipendi_cofi_logsHome logHome = (Stipendi_cofi_logsHome) getHome(userContext, Stipendi_cofi_logsBulk.class);
            dett.setPg_exec(logHome.findProg(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext), dett.getMese()).intValue());
            cercaBatch(userContext, dett);
        } catch (Exception e) {
            throw handleException(e);
        }
        return dett;
    }

    public V_stipendi_cofi_dettBulk elaboraFile(
            UserContext userContext,
            V_stipendi_cofi_dettBulk dett)
            throws it.cnr.jada.comp.ComponentException {

        LoggableStatement cs = null;
        try {
            try {
                cs = new LoggableStatement(getConnection(userContext),
                        "{call " +
                                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                                "CNRCTB680.elaboraStipDett(?,?,?) }", false, this.getClass());

                cs.setInt(1, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext)); // ESERCIZIO
                cs.setInt(2, dett.getMese()); // DA ESERCIZIO PREC.
                cs.setString(3, userContext.getUser()); // USER
                cs.executeQuery();

            } catch (java.sql.SQLException e) {
                throw handleException(e);
            } finally {
                if (cs != null)
                    cs.close();
            }
        } catch (java.sql.SQLException ex) {
            throw handleException(ex);
        }
        return dett;
    }

    public V_stipendi_cofi_dettBulk annullaElaborazione(
            UserContext userContext,
            V_stipendi_cofi_dettBulk dett)
            throws it.cnr.jada.comp.ComponentException {

        LoggableStatement cs = null;
        try {
            try {
                cs = new LoggableStatement(getConnection(userContext),
                        "{call " +
                                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                                "CNRCTB680.annullaElabStipDett(?,?,?) }", false, this.getClass());

                cs.setInt(1, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext)); // ESERCIZIO
                cs.setInt(2, dett.getMese()); // DA ESERCIZIO PREC.
                cs.setString(3, userContext.getUser()); // USER
                cs.executeQuery();

            } catch (java.sql.SQLException e) {
                throw handleException(e);
            } finally {
                if (cs != null)
                    cs.close();
            }
        } catch (java.sql.SQLException ex) {
            throw handleException(ex);
        }
        return dett;
    }

    public OggettoBulk cercaBatch(
            UserContext userContext,
            V_stipendi_cofi_dettBulk dett)
            throws it.cnr.jada.comp.ComponentException {
        Batch_log_rigaBulk batch_log_riga = new Batch_log_rigaBulk();
        BulkHome home = getHome(userContext, Batch_log_rigaBulk.class);
        SQLBuilder sql2 = home.createSQLBuilder();

        sql2.addTableToHeader("BATCH_LOG_TSTA");
        sql2.addSQLJoin("BATCH_LOG_RIGA.PG_ESECUZIONE", "BATCH_LOG_TSTA.PG_ESECUZIONE");
        sql2.addSQLClause("AND", "BATCH_LOG_RIGA.pg_esecuzione", SQLBuilder.EQUALS, dett.getPg_exec());
        sql2.addSQLClause("AND", "BATCH_LOG_TSTA.cd_log_tipo", SQLBuilder.EQUALS, "ELAB_STIP00");
        try {
            dett.setBatch_log_riga(new BulkList(home.fetchAll(sql2)));
            getHomeCache(userContext).fetchAll(userContext);
        } catch (PersistencyException e) {
            throw new it.cnr.jada.comp.ComponentException(e.getMessage(), e);
        }
        return dett;
    }

    public SQLBuilder selectDettagliFileStipendiByClause(UserContext userContext, V_stipendi_cofi_dettBulk dett, OggettoBulk bulkClass, CompoundFindClause clauses) throws ComponentException {
        if (dett == null)
            return null;
        SQLBuilder sql;
        if (bulkClass instanceof Stipendi_cofi_cori_dettBulk) {
            sql = getHome(userContext, Stipendi_cofi_cori_dettBulk.class).createSQLBuilder();
        } else {
            if (bulkClass instanceof Stipendi_cofi_obb_scad_dettBulk) {
                sql = getHome(userContext, Stipendi_cofi_obb_scad_dettBulk.class).createSQLBuilder();
            } else
                sql = null;
        }

        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, dett.getEsercizio());
        sql.addSQLClause("AND", "MESE", SQLBuilder.EQUALS, dett.getMese());
        sql.addSQLClause("AND", "TIPO_FLUSSO", SQLBuilder.EQUALS, dett.getTipo_flusso());
        return sql;
    }

    public SQLBuilder selectFlussoStipendiEntrataByClause(UserContext userContext, V_stipendi_cofi_dettBulk dett, Stipendi_cofi_coriBulk bulkClass, CompoundFindClause clauses) throws ComponentException {
        if (dett == null)
            return null;
        SQLBuilder sql;
        sql = getHome(userContext, Stipendi_cofi_coriBulk.class, "V_STIPENDI_COFI_CORI_CONTRIBUTO_RITENUTA").createSQLBuilder();
        sql.addTableToHeader("TIPO_CONTRIBUTO_RITENUTA");
        sql.addSQLClause("AND", "STIPENDI_COFI_CORI.ESERCIZIO", SQLBuilder.EQUALS, dett.getEsercizio());
        sql.addSQLClause("AND", "STIPENDI_COFI_CORI.MESE", SQLBuilder.EQUALS, dett.getMese());
        sql.addSQLJoin("STIPENDI_COFI_CORI.CD_CONTRIBUTO_RITENUTA", "TIPO_CONTRIBUTO_RITENUTA.CD_CONTRIBUTO_RITENUTA");
        sql.addSQLJoin(
                "TIPO_CONTRIBUTO_RITENUTA.DT_INI_VALIDITA",
                "(SELECT MAX(DT_INI_VALIDITA) FROM TIPO_CONTRIBUTO_RITENUTA DETT WHERE DETT.CD_CONTRIBUTO_RITENUTA = STIPENDI_COFI_CORI.CD_CONTRIBUTO_RITENUTA)"
        );
        return sql;
    }

    public SQLBuilder selectFlussoStipendiSpesaByClause(UserContext userContext, V_stipendi_cofi_dettBulk dett, Stipendi_cofi_obb_scadBulk bulkClass, CompoundFindClause clauses) throws ComponentException {
        if (dett == null)
            return null;
        SQLBuilder sql;
        sql = getHome(userContext, Stipendi_cofi_obb_scadBulk.class).createSQLBuilder();

        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, dett.getEsercizio());
        sql.addSQLClause("AND", "MESE", SQLBuilder.EQUALS, dett.getMese());
        return sql;
    }

    public V_cnr_estrazione_coriBulk cercaCnrEstrazioneCori(UserContext userContext, V_cnr_estrazione_coriBulk dett) throws it.cnr.jada.comp.ComponentException {

        try {
            V_cnr_estrazione_coriHome estrazione = (V_cnr_estrazione_coriHome) getHome(userContext, dett.getClass());

            Collection col = estrazione.findDistinct(userContext);

            for (java.util.Iterator i = col.iterator(); i.hasNext(); ) {
                V_cnr_estrazione_coriBulk v = (V_cnr_estrazione_coriBulk) i.next();
                if (v.getMese() != null) {
                    dett.getV_cnr_estrazione_cori().add(v);
                }
            }
        } catch (Exception e) {
            throw handleException(e);
        }
        return dett;
    }

    public V_cnr_estrazione_coriBulk elaboraStralcioMensile(
            UserContext userContext,
            V_cnr_estrazione_coriBulk dett)
            throws it.cnr.jada.comp.ComponentException {

        LoggableStatement cs = null;
        try {
            try {
                cs = new LoggableStatement(getConnection(userContext),
                        "{call " +
                                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                                "CNRCTB577.stralcioMensile(?,?,?) }", false, this.getClass());

                cs.setInt(1, dett.getEsercizio()); // ESERCIZIO
                cs.setInt(2, dett.getMese()); // MESE SELEZIONATO
                cs.setString(3, userContext.getUser()); // USER
                cs.executeQuery();

            } catch (java.sql.SQLException e) {
                throw handleException(e);
            } finally {
                if (cs != null)
                    cs.close();
            }
        } catch (java.sql.SQLException ex) {
            throw handleException(ex);
        }
        return dett;
    }

    public boolean esisteStralcioNegativo(UserContext userContext, V_cnr_estrazione_coriBulk dett) throws ComponentException {
        try {
            if (dett == null)
                return false;
            SQLBuilder sql;
            sql = getHome(userContext, Cnr_estrazione_coriBulk.class).createSQLBuilder();

            sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, dett.getEsercizio());
            sql.addSQLClause("AND", "MESE", SQLBuilder.EQUALS, dett.getMese());
            sql.openParenthesis("AND");
            sql.addSQLClause("AND", "IMPONIBILE", SQLBuilder.LESS, new BigDecimal(0));
            sql.addSQLClause("OR", "IM_RITENUTA", SQLBuilder.LESS, new BigDecimal(0));
            sql.closeParenthesis();
			return sql.executeCountQuery(getConnection(userContext)) > 0;

        } catch (Throwable e) {
            throw handleException(e);
        }
    }
}