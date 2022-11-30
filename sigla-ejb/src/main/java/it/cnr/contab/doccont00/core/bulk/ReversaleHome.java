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
import it.cnr.contab.doccont00.dto.EnumSiopeBilancioGestione;
import it.cnr.contab.doccont00.dto.SiopeBilancioDTO;
import it.cnr.contab.doccont00.dto.SiopeBilancioKeyDto;
import it.cnr.contab.doccont00.tabrif.bulk.CupBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import javax.ejb.EJBException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public abstract Collection findReversale_riga(it.cnr.jada.UserContext userContext, ReversaleBulk reversale) throws PersistencyException;

    public abstract Collection findReversale_riga(it.cnr.jada.UserContext userContext, ReversaleBulk reversale, boolean fetchAll) throws PersistencyException;

    /**
     * <!-- @TODO: da completare -->
     *
     * @param reversale
     * @return
     * @throws PersistencyException
     * @throws IntrospectionException
     */
    public abstract Reversale_terzoBulk findReversale_terzo(it.cnr.jada.UserContext userContext, ReversaleBulk reversale) throws PersistencyException;

    public abstract Reversale_terzoBulk findReversale_terzo(it.cnr.jada.UserContext userContext, ReversaleBulk reversale, boolean fetchAll) throws PersistencyException;

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


    /**
     * Recupera tutti i CUP collegati alla Reversale.
     *
     * @param reversaleBulk Reversale in uso.
     * @return java.util.Collection Collezione di oggetti <code>CUP</code>
     */
    public java.util.Collection<CupBulk> findCodiciSiopeCupCollegati(UserContext usercontext, ReversaleBulk reversaleBulk) throws PersistencyException {
        PersistentHome reversaleSiopeCupHome = getHomeCache().getHome(ReversaleSiopeCupIBulk.class);
        SQLBuilder sql = reversaleSiopeCupHome.createSQLBuilder();
        sql.setAutoJoins(true);
        sql.generateJoin("reversale_siopeI", "REVERSALE_SIOPE");

        sql.addSQLClause(FindClause.AND, "REVERSALE_SIOPE.CD_CDS", SQLBuilder.EQUALS, reversaleBulk.getCd_cds());
        sql.addSQLClause(FindClause.AND, "REVERSALE_SIOPE.ESERCIZIO", SQLBuilder.EQUALS, reversaleBulk.getEsercizio());
        sql.addSQLClause(FindClause.AND, "REVERSALE_SIOPE.PG_REVERSALE", SQLBuilder.EQUALS, reversaleBulk.getPg_reversale());
        final Stream<ReversaleSiopeCupBulk> stream = reversaleSiopeCupHome.fetchAll(sql)
                .stream()
                .filter(ReversaleSiopeCupBulk.class::isInstance)
                .map(ReversaleSiopeCupBulk.class::cast);
        getHomeCache().fetchAll(usercontext);
        return stream.filter(reversaleSiopeCupBulk -> Optional.ofNullable(reversaleSiopeCupBulk.getCdCup()).isPresent())
                .map(ReversaleSiopeCupBulk::getCup).collect(Collectors.toList());
    }

    /**
     * Recupera tutti i Codici CUP collegati alla Reversale.
     *
     * @param reversaleBulk Reversale in uso.
     * @return java.util.Collection Collezione di oggetti <code>CUP</code>
     */
    public java.util.Collection<String> findCodiciCupCollegati(UserContext usercontext, ReversaleBulk reversaleBulk) throws PersistencyException {
        PersistentHome reversaleCupHome = getHomeCache().getHome(ReversaleCupIBulk.class);
        SQLBuilder sql = reversaleCupHome.createSQLBuilder();
        sql.setAutoJoins(true);
        sql.generateJoin("reversale_rigaI", "REVERSALE_RIGA");

        sql.addSQLClause(FindClause.AND, "REVERSALE_RIGA.CD_CDS", SQLBuilder.EQUALS, reversaleBulk.getCd_cds());
        sql.addSQLClause(FindClause.AND, "REVERSALE_RIGA.ESERCIZIO", SQLBuilder.EQUALS, reversaleBulk.getEsercizio());
        sql.addSQLClause(FindClause.AND, "REVERSALE_RIGA.PG_MANDATO", SQLBuilder.EQUALS, reversaleBulk.getPg_reversale());
        final Stream<ReversaleCupBulk> stream = reversaleCupHome.fetchAll(sql)
                .stream()
                .filter(ReversaleCupBulk.class::isInstance)
                .map(ReversaleCupBulk.class::cast);
        return stream.map(t -> t.getCdCup())
                .distinct()
                .collect(Collectors.toList());
    }
    public ReversaleBulk findAndLockReversaleNonAnnullata(it.cnr.jada.UserContext userContext,java.lang.String cdCds, java.lang.Integer esercizio, java.lang.Long pgReversale) throws PersistencyException, OutdatedResourceException, BusyResourceException {

        SQLBuilder sql = createSQLBuilder();
        sql.addClause("AND", "cd_cds",       sql.EQUALS, cdCds);
        sql.addClause("AND", "esercizio",    sql.EQUALS, esercizio);
        sql.addClause("AND", "pg_reversale", sql.EQUALS, pgReversale);
        sql.addClause("AND","stato", sql.NOT_EQUALS, ReversaleBulk.STATO_REVERSALE_ANNULLATO);
        List reversali = fetchAll(sql);
        if (reversali == null || reversali.size() == 0){
            return null;
        } else if (reversali.size() == 1){
            ReversaleBulk rev = (ReversaleBulk) reversali.get(0);
            lock(rev);
            return rev;
        } else  {
            throw new PersistencyException("Errore nel recupero della Reversale "+esercizio+"-"+pgReversale);
        }
    }

    public List<SiopeBilancioDTO> getSiopeBilancio(UserContext userContext, ReversaleBulk reversale ){
        List<SiopeBilancioDTO> bilancio = new ArrayList<SiopeBilancioDTO>();

        Optional.ofNullable(reversale).ifPresent(s -> {
            s.getReversale_rigaColl().stream().forEach(m -> {
                EnumSiopeBilancioGestione gestione = EnumSiopeBilancioGestione.COMPETENZA;
                Integer annoResiduo = null;
                if ( m.getEsercizio_accertamento().compareTo(m.getEsercizio_ori_accertamento())!=0) {
                    gestione = EnumSiopeBilancioGestione.RESIDUO;
                    annoResiduo=m.getEsercizio_ori_accertamento();
                }
                final SiopeBilancioKeyDto keyBilancio = new SiopeBilancioKeyDto(m.elemento_voce.getCd_voce(),
                        gestione,
                        annoResiduo) ;
                Optional<SiopeBilancioDTO> el=bilancio.stream().
                        filter(b->b.equals(keyBilancio)).
                        findFirst();
                if ( el.isPresent()){
                    SiopeBilancioDTO voceBilancio = el.get();
                    voceBilancio.setImporto(voceBilancio.getImporto().add( m.getIm_reversale_riga()));
                }else{
                    SiopeBilancioDTO voceBilancio = new SiopeBilancioDTO(keyBilancio);
                    voceBilancio.setDescrzioneVoceBilancio( m.elemento_voce.getDs_elemento_voce());
                    voceBilancio.setImporto(m.getIm_reversale_riga());
                    bilancio.add(voceBilancio);
                }
            });

        });
        return bilancio;
    }
}
