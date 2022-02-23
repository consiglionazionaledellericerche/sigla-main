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

package it.cnr.contab.cori00.comp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.cori00.docs.bulk.*;
import it.cnr.contab.cori00.views.bulk.*;
import it.cnr.contab.logs.bulk.Batch_log_rigaBulk;
import it.cnr.contab.logs.bulk.Batch_log_tstaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;

import javax.ejb.EJBException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class Liquid_coriComponent extends it.cnr.jada.comp.CRUDDetailComponent
        implements ICRUDMgr, ILiquid_coriMgr, Cloneable, Serializable, it.cnr.jada.comp.ICRUDDetailMgr {
    /**
     * Liquid_coriComponent constructor comment.
     */
    public Liquid_coriComponent() {
        super();
    }

    /**
     * Le operazioni di <code>Calcola Liquidazione</code>
     * o <code>Liquida CORI</code>, vanno a popolare le tabelle LIQUID_CORI, LIQUID_GRUPPO_CORI e
     * LIQUID_GRUPPO_CORI_DET, e questo metodo permette di allineare l'oggetto Liquid_coriBulk
     * con gli aggiornamenti delle tabelle.
     * <p>
     * Aggiorna l'oggetto Liquid_coriBulk
     * PreCondition:
     * E' stata generata la richiesta di calcolo o esecuzione della liquidazione. Nessun errore rilevato.
     * PostCondition:
     * Viene aggiornato l'oggetto Liquid_coriBulk con le modifiche apportate sulle tabelle
     * relative alla Liquidazione CORI. Restituisce l'oggetto Liquid_coriBulk aggiornato.
     *
     * @param userContext       lo <code>UserContext</code> che ha generato la richiesta.
     * @param liquidazione_cori <code>Liquid_coriBulk</code> l'oggetto da aggiornare.
     * @return liquidazione_cori <code>Liquid_coriBulk</code> l'oggetto da aggiornato.
     **/
    private Liquid_coriBulk aggiornaLiquidCori(it.cnr.jada.UserContext userContext, Liquid_coriBulk liquidazione_cori) throws it.cnr.jada.comp.ComponentException {

        SimpleBulkList gruppo_cori = new SimpleBulkList();
        SimpleBulkList newGruppi = new SimpleBulkList();

        try {
            // Riempie la collezione coi gruppi calcolati
            gruppo_cori = new SimpleBulkList(findGruppoCori(userContext, liquidazione_cori));

            // Per ogni gruppo, riempie la collezione dei Capitoli
            for (Iterator i = gruppo_cori.iterator(); i.hasNext(); ) {
                Liquid_gruppo_coriIBulk gruppo = (Liquid_gruppo_coriIBulk) i.next();
                SimpleBulkList capitoli = new SimpleBulkList(findCapitoliFor(userContext, liquidazione_cori, gruppo));
                gruppo.setCapitoliColl(capitoli);
                newGruppi.add(gruppo);
            }

            // Aggiorna l'oggetto Liquid_coriBulk principale
            liquidazione_cori = (Liquid_coriBulk) getHome(userContext, Liquid_coriBulk.class).findByPrimaryKey(liquidazione_cori);
            getHomeCache(userContext).fetchAll(userContext);


        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw new it.cnr.jada.comp.ComponentException(e);
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw new it.cnr.jada.comp.ComponentException(e);
        }

        liquidazione_cori.setCoriColl(newGruppi);
        return liquidazione_cori;
    }

    /**
     * Calcola liquidazione
     * PreCondition:
     * E' stata generata la richiesta di calcolo dei CORI che devono essere liquidati.
     * Nessun errore rilevato.
     * PostCondition:
     * Viene richiamata la procedura di calcolo della Liquidazione, (metodo callCalcolaLiquidazione).
     * Restituisce l'oggetto Liquid_coriBulk aggiornato, (metodo aggiornaLiquidCori).
     *
     * @param userContext       lo <code>UserContext</code> che ha generato la richiesta.
     * @param liquidazione_cori <code>Liquid_coriBulk</code> l'oggetto che contiene le
     *                          informazioni relative alla liquidazione.
     * @return <code>Liquid_coriBulk</code> l'oggetto aggiornato.
     **/
    public Liquid_coriBulk calcolaLiquidazione(it.cnr.jada.UserContext userContext, Liquid_coriBulk liquidazione_cori) throws it.cnr.jada.comp.ComponentException {

        callCalcolaLiquidazione(userContext, liquidazione_cori);

        return aggiornaLiquidCori(userContext, liquidazione_cori);
    }

    /**
     * Richiama la procedura di calcola liquidazione
     * PreCondition:
     * E' stata generata la richiesta di calcolo dei CORI che devono essere liquidati.
     * PostCondition:
     * Viene richiamata la procedura di calcolo della Liquidazione.
     *
     * @param userContext       lo <code>UserContext</code> che ha generato la richiesta.
     * @param liquidazione_cori <code>Liquid_coriBulk</code> l'oggetto che contiene le
     *                          informazioni relative alla liquidazione.
     **/
    private void callCalcolaLiquidazione(it.cnr.jada.UserContext userContext, Liquid_coriBulk liquidazione_cori) throws it.cnr.jada.comp.ComponentException {

        LoggableStatement cs = null;
        try {
            try {
                cs = new LoggableStatement(getConnection(userContext),
                        "{call " +
                                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                                "CNRCTB570.calcolaLiquidazione(?,?,?,?,?,?,?,?) }", false, this.getClass());

                String da_es_prec = "N";
                if (liquidazione_cori.getDa_esercizio_precedente() != null) {
                    da_es_prec = liquidazione_cori.getDa_esercizio_precedente().booleanValue() ? "Y" : "N";
                }

                cs.setString(1, liquidazione_cori.getCd_cds()); // CD_CDS
                cs.setInt(2, liquidazione_cori.getEsercizio().intValue()); // ESERCIZIO

                /*****>>>>>>>>> Questo parametro dovrà essere Y o N  >>>>>>******/
                cs.setString(3, da_es_prec); // DA ESERCIZIO PREC.
                /*****>>>>>>>>> ************************************ >>>>>>******/

                cs.setString(4, liquidazione_cori.getCd_unita_organizzativa()); // CD_UO
                cs.setInt(5, liquidazione_cori.getPg_liquidazione().intValue()); // PG LIQUIDAZIONE
                cs.setTimestamp(6, liquidazione_cori.getDt_da()); // PERIODO DA
                cs.setTimestamp(7, liquidazione_cori.getDt_a()); // PERIODO A
                cs.setString(8, liquidazione_cori.getUser()); // USER

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
    }

    public Liquid_gruppo_centroBulk cambiaStato(it.cnr.jada.UserContext userContext, Liquid_gruppo_centroBulk liquid) throws ComponentException {
        if (liquid.getStato().equals(liquid.STATO_INIZIALE))
            liquid.setStato(liquid.STATO_SOSPESO);
        else
            liquid.setStato(liquid.STATO_INIZIALE);

        liquid.setToBeUpdated();
        //liquid = (Liquid_gruppo_centroBulk)super.modificaConBulk(userContext, liquid);
        try {
            updateBulk(userContext, liquid);
        } catch (PersistencyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ComponentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return liquid;
    }

    /**
     * Richiede il numeratore di protocollo vsx.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @return il N. liquidazione <code>Long</code> richiesto.
     **/

    private Long callGetPgPerLiquidazioneCORI(
            UserContext userContext)
            throws it.cnr.jada.comp.ComponentException {

        LoggableStatement cs = null;
        Long pg = null;
        try {
            try {
                cs = new LoggableStatement(getConnection(userContext),
                        "{ ? = call " +
                                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                                "IBMUTL020.vsx_get_pg_call() }", false, this.getClass());
                cs.registerOutParameter(1, java.sql.Types.NUMERIC);
                cs.executeQuery();
                pg = new Long(cs.getLong(1));

            } catch (java.sql.SQLException e) {
                throw handleException(e);
            } finally {
                if (cs != null) cs.close();
            }
        } catch (java.sql.SQLException ex) {
            throw handleException(ex);
        }
        if (pg == null)
            throw new it.cnr.jada.comp.ApplicationException("Impossibile ottenere un progressivo valido per la vista di Liquidazione CORI!");
        return pg;
    }

    /**
     * Richiama la procedura di liquidazione dei Contributi/Ritenute
     * PreCondition:
     * E' stata generata la richiesta di liquidazione dei CORI selezionati dall'utente.
     * PostCondition:
     * Viene richiamata la procedura di Liquidazione CORI.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
     * @param progressivo il <code>Long</code> progressivo della chiamata.
     **/
    private void callLiquidaCori(
            UserContext userContext,
            Long progressivo)
            throws it.cnr.jada.comp.ComponentException {

        LoggableStatement cs = null;
        try {
            cs = new LoggableStatement(getConnection(userContext),
                    "{ call " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "CNRCTB570.vsx_liquida_cori(?) }", false, this.getClass());
            cs.setLong(1, progressivo.longValue());
            cs.executeQuery();
        } catch (Throwable e) {
            throw handleException(e);
        } finally {
            try {
                if (cs != null) cs.close();
            } catch (java.sql.SQLException e) {
                throw handleException(e);
            }
        }
    }

    /**
     * Liquida i contributi selezionati.
     * PreCondition:
     * E' stata generata la richiesta di liquidare i CORI.
     * PostCondition:
     * Vengono inseriti nella vista VSX_LIQUIDAZIONE_CORI i dati relativi ai CORI selezionati
     * dall'utente e che devono essere liquidati.
     * Viene richiamata la procedura di Liquidazione CORI, (metodo callLiquidaCori).
     * Restituisce l'oggetto Liquid_coriBulk aggiornato, (metodo aggiornaLiquidCori).
     *
     * @param userContext        lo <code>UserContext</code> che ha generato la richiesta.
     * @param liquid_cori        <code>Liquid_coriBulk</code> l'oggetto che contiene le
     *                           informazioni relative alla liquidazione.
     * @param gruppi_selezionati la <code>List</code> lista di gruppi CORI selezionati.
     * @return <code>Liquid_coriBulk</code> l'oggetto aggiornato.
     **/
    public Liquid_coriBulk eseguiLiquidazione(it.cnr.jada.UserContext userContext, Liquid_coriBulk liquid_cori, java.util.List gruppi_selezionati) throws it.cnr.jada.comp.ComponentException {

        java.util.Vector visteAggiunte = new java.util.Vector();
        Vsx_liquidazione_coriHome home = (Vsx_liquidazione_coriHome) getHome(userContext, Vsx_liquidazione_coriBulk.class);

        try {
            try {
                int count = 0;
                Long pg_call = callGetPgPerLiquidazioneCORI(userContext);
                for (Iterator i = gruppi_selezionati.iterator(); i.hasNext(); ) {
                    Liquid_gruppo_coriIBulk gruppo = (Liquid_gruppo_coriIBulk) i.next();
                    gruppo.setUser(userContext.getUser());

                    Vsx_liquidazione_coriBulk vsx_liqid = new Vsx_liquidazione_coriBulk();
                    vsx_liqid.setPg_call(pg_call);
                    vsx_liqid.completeFrom(gruppo);
                    vsx_liqid.setToBeCreated();
                    vsx_liqid.setPar_num(new Integer(count++));
                    vsx_liqid.setToBeCreated();
                    home.insert(vsx_liqid, userContext);
                    //insertBulk(userContext, vsx_liqid);
                    visteAggiunte.add(vsx_liqid);
                }

                callLiquidaCori(userContext, pg_call);

            } catch (it.cnr.jada.persistency.PersistencyException e) {
                throw e;
            } finally {
                for (java.util.Iterator i = visteAggiunte.iterator(); i.hasNext(); ) {
                    Vsx_liquidazione_coriBulk vistaInserita = (Vsx_liquidazione_coriBulk) i.next();
                    vistaInserita.setToBeDeleted();
                    home.delete(vistaInserita, userContext);
                }
            }

            liquid_cori = aggiornaLiquidCori(userContext, liquid_cori);

        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(liquid_cori, e);
        }
        return liquid_cori;
    }

    /**
     * Cerca i Capitoli per un gruppo CORI.
     * PreCondition:
     * E' stata generata la richiesta di cercare i capitoli relativi ad un gruppo CORI .
     * PostCondition:
     * Viene costruito il SQLBuilder con l'elenco delle clausole implicite (presenti
     * nell'istanza di V_liquid_capitoli_coriBulk), e, in aggiunta, le
     * clausole che i Capitoli appartengano all'Esercizio corrente e siano relativi al gruppo CORI specificato.
     *
     * @param userContext       lo <code>UserContext</code> che ha generato la richiesta.
     * @param liquidazione_cori <code>Liquid_coriBulk</code> l'oggetto che contiene le
     *                          informazioni relative alla liquidazione.
     * @param gruppo            il <code>Liquid_gruppo_coriIBulk</code> gruppo CORI.
     * @return capitoli la <code>List</code> collezione di Capitoli trovati.
     **/
    private java.util.List findCapitoliFor(it.cnr.jada.UserContext userContext, Liquid_coriBulk liquidazione_cori, Liquid_gruppo_coriIBulk gruppo)
            throws ComponentException,
            it.cnr.jada.persistency.PersistencyException,
            it.cnr.jada.persistency.IntrospectionException {

        if (liquidazione_cori == null)
            return null;

        BulkHome home = getHome(userContext, V_liquid_capitoli_coriBulk.class);
        SQLBuilder sql = home.createSQLBuilder();

        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, liquidazione_cori.getEsercizio());
        sql.addSQLClause("AND", "CD_GRUPPO_CR", sql.EQUALS, gruppo.getCd_gruppo_cr());

        return home.fetchAll(sql);
    }

    /**
     * Cerca i gruppi CORI - liquidazione non specificata.
     * PreCondition:
     * Non è stata specificata alcuna Liquidazione.
     * PostCondition:
     * Restituisce un messaggio d'errore che indica che non è stata specificata la Liquidazione di riferimento.
     * <p>
     * Cerca i gruppi CORI.
     * PreCondition:
     * E' stata generata la richiesta di cercare i gruppi relativi alla Liquidazione indicata.
     * PostCondition:
     * Viene costruito il SQLBuilder con l'elenco delle clausole implicite (presenti
     * nell'istanza di Liquid_gruppo_coriIBulk), e, in aggiunta, le
     * clausole che i Gruppi appartengano all'Esercizio corrente e siano relativi alla
     * Unità Organizzativa di scrivania.
     *
     * @param userContext       lo <code>UserContext</code> che ha generato la richiesta.
     * @param liquidazione_cori la <code>Liquid_coriBulk</code> liquidazione di riferimento.
     * @return gruppi la <code>List</code> collezione di gruppi CORI trovati.
     **/
    private java.util.List findGruppoCori(it.cnr.jada.UserContext userContext, Liquid_coriBulk liquidazione_cori)
            throws ComponentException,
            it.cnr.jada.persistency.PersistencyException,
            it.cnr.jada.persistency.IntrospectionException {

        if (liquidazione_cori == null)
            throw new it.cnr.jada.comp.ComponentException("Non è stato specificata alcuna Liquidazione");

        BulkHome home = getHome(userContext, Liquid_gruppo_coriIBulk.class, "V_LIQUID_GRUPPO_CORI");
        SQLBuilder sql = home.createSQLBuilder();

        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, liquidazione_cori.getEsercizio());
        sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, liquidazione_cori.getCd_cds());
        sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, liquidazione_cori.getCd_unita_organizzativa());
        sql.addSQLClause("AND", "PG_LIQUIDAZIONE", sql.EQUALS, liquidazione_cori.getPg_liquidazione());

        return home.fetchAll(sql);
    }

    /**
     * Richiede il Progressivo per i Documenti Contabili. Questo sarà, in pratica, il N. Liquidazione.
     *
     * @param userContext       lo <code>UserContext</code> che ha generato la richiesta
     * @param liquidazione_cori l'oggetto <code>Liquid_coriBulk</code> che effettura la liquidazione
     * @return il N. liquidazione <code>Integer</code> richiesto.
     **/
    private Integer getNumLiquidazione(
            UserContext userContext,
            Liquid_coriBulk liquidazione_cori)
            throws it.cnr.jada.comp.ComponentException {

        LoggableStatement cs = null;
        Integer pg = null;
        try {
            try {
                cs = new LoggableStatement(getConnection(userContext),
                        "{ ? = call " +
                                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                                "CNRCTB575.getNextNumLiquid(?,?,?) }", false, this.getClass());
                cs.registerOutParameter(1, java.sql.Types.NUMERIC);
                cs.setString(2, liquidazione_cori.getCd_cds());
                cs.setInt(3, liquidazione_cori.getEsercizio().intValue());
                cs.setString(4, liquidazione_cori.getCd_unita_organizzativa());
                cs.executeQuery();
                pg = new Integer(cs.getInt(1));
            } catch (Throwable e) {
                throw handleException(e);
            } finally {
                if (cs != null) cs.close();
            }
        } catch (java.sql.SQLException ex) {
            throw handleException(ex);
        }
        if (pg == null)
            throw new it.cnr.jada.comp.ApplicationException("Impossibile ottenere un progressivo valido per la liquidazione CORI!");

        if (pg.compareTo(new Integer(0)) == 0)
            pg = new Integer(1);
        return pg;
    }

    /**
     * Imposta le date di inizio e fine periodo della Liquidazione. Vengono proposte di default
     * l'inizio e la fine del mese attuale.
     *
     * @param liquidazione_cori l'oggetto <code>Liquid_coriBulk</code> che effettura la liquidazione
     **/
    private void impostaDate(Liquid_coriBulk liquidazione_cori) throws ComponentException {

        try {
            GregorianCalendar cal_da = (GregorianCalendar) GregorianCalendar.getInstance();
            cal_da.setTime(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
            cal_da.set(Calendar.DAY_OF_MONTH, 1);
            cal_da.set(Calendar.MONTH, cal_da.get(Calendar.MONTH) - 1);

            GregorianCalendar cal_a = (GregorianCalendar) GregorianCalendar.getInstance();
            cal_a.setTime(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
            cal_a.set(Calendar.DAY_OF_MONTH, 1);
            cal_a.set(Calendar.DAY_OF_YEAR, cal_a.get(Calendar.DAY_OF_YEAR) - 1);

            liquidazione_cori.setDt_da(new Timestamp(cal_da.getTime().getTime()));
            liquidazione_cori.setDt_a(new Timestamp(cal_a.getTime().getTime()));
        } catch (javax.ejb.EJBException e) {
            throw handleException(e);
        }

    }

    /**
     * Inizializzazione di una istanza di Liquid_coriBulk
     * PreCondition:
     * E' stata generata la richiesta di inizializzare una istanza di Liquid_coriBulk.
     * PostCondition:
     * Vengono impostate le proprità relative al CdS alla UO ed alle date di inizio e fine
     * periodo di Liquidazione, (metodo impostaDate).
     * Viene cercato il progressivo da assegnare alla Liquidazione, (metodo getNumLiquidazione);
     * viene restituito l'oggetto Liquid_coriBulk inizializzato.
     *
     * @param aUC  lo <code>UserContext</code> che ha generato la richiesta.
     * @param bulk <code>OggettoBulk</code> l'oggetto che deve essere istanziato.
     * @return <code>OggettoBulk</code> l'oggetto inizializzato.
     **/
    public OggettoBulk inizializzaBulkPerInserimento(UserContext aUC, OggettoBulk bulk) throws ComponentException {
        bulk = super.inizializzaBulkPerInserimento(aUC, bulk);

        Liquid_coriBulk liquidazione_cori = (Liquid_coriBulk) bulk;

        // INIZIALIZZA CDS, UO ED ESERCIZIO LEGGENDOLI DA SCRIVANIA
        liquidazione_cori.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));
        String cd_cds = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(aUC);
        String cd_uo = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(aUC);

        // Imposta la dt_da e la dt_a come il primo e l'ultimo giorno del mese precedente a quello attuale
        impostaDate(liquidazione_cori);

        CdsBulk cds = null;
        Unita_organizzativaBulk uo;
        try {
            cds = (CdsBulk) getHome(aUC, CdsBulk.class).findByPrimaryKey(new CdsBulk(cd_cds));
            uo = (Unita_organizzativaBulk) getHome(aUC, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cd_uo));
            liquidazione_cori.setCds(cds);
            liquidazione_cori.setUnita_organizzativa(uo);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw new it.cnr.jada.comp.ComponentException(e.getMessage(), e);
        }

        //IMPOSTA N. LIQUIDAZIONE
        liquidazione_cori.setPg_liquidazione(getNumLiquidazione(aUC, liquidazione_cori));


        return liquidazione_cori;
    }

    /**
     * Inizializzazione di una istanza di Liquid_coriBulk per modifica
     * PreCondition:
     * E' stata richiesta l'inizializzazione di una istanza di Liquid_coriBulk per modifica
     * PostCondition:
     * Vengono caricati i gruppi CORI legati alla Liquidazione indicata, (metodo findGruppoCori).
     *
     * @param aUC  lo <code>UserContext</code> che ha generato la richiesta.
     * @param bulk <code>OggettoBulk</code> l'oggetto che deve essere istanziato.
     * @return <code>OggettoBulk</code> l'oggetto inizializzato.
     **/
    public OggettoBulk inizializzaBulkPerModifica(UserContext aUC, OggettoBulk bulk) throws ComponentException {
        bulk = super.inizializzaBulkPerInserimento(aUC, bulk);

        Liquid_coriBulk liquidazione_cori = (Liquid_coriBulk) super.inizializzaBulkPerModifica(aUC, bulk);
        SimpleBulkList newGruppi = new SimpleBulkList();

        try {
            SimpleBulkList coll = new SimpleBulkList(findGruppoCori(aUC, liquidazione_cori));

            // Per ogni gruppo, riempie la collezione dei Capitoli
            for (Iterator i = coll.iterator(); i.hasNext(); ) {
                Liquid_gruppo_coriIBulk gruppo = (Liquid_gruppo_coriIBulk) i.next();
                SimpleBulkList capitoli = new SimpleBulkList(findCapitoliFor(aUC, liquidazione_cori, gruppo));
                gruppo.setCapitoliColl(capitoli);
                newGruppi.add(gruppo);
            }

            liquidazione_cori.setCoriColl(newGruppi);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw new it.cnr.jada.comp.ComponentException(e.getMessage(), e);
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw new it.cnr.jada.comp.ComponentException(e.getMessage(), e);
        }


        return liquidazione_cori;
    }

    /**
     * Ricerca liquidazioni
     * PreCondition:
     * E' stato richiesto di cercare una Liquidazione
     * PostCondition:
     * E' stato creato il SQLBuilder con le clausole implicite (presenti nell'istanza di Liquid_coriBulk);
     * inoltre, le liquidazioni devono appartenere alla Uo di scrivania e devono essere dell'esercizio corrente.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param clauses     <code>CompoundFindClause</code> le clausole della selezione
     * @param bulk        <code>OggettoBulk</code> il Liquid_cori modello
     * @return sql <code>SQLBuilder</code> Risultato della selezione.
     **/
    protected Query select(UserContext userContext, CompoundFindClause clauses, OggettoBulk bulk)
            throws ComponentException, it.cnr.jada.persistency.PersistencyException {


        SQLBuilder sql = (SQLBuilder) super.select(userContext, clauses, bulk);

        sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
        sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));

        return sql;

    }

    /**
     * Seleziona dettagli di liquid_gruppo_cori
     * PreCondition:
     * E' stata generata la richiesta di cercare i dettagli associati alla riga di
     * liquid_gruppo_cori indicata.
     * PostCondition:
     * E' stato creato il SQLBuilder con le clausole implicite (presenti nell'istanza di Liquid_gruppo_cori_detIBulk);
     * inoltre, i dettagli devono essere relativi al Liquid_gruppo_cori indicato.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param gruppo_cori il <code>Liquid_gruppo_coriIBulk</code> gruppo CORI di riferimento.
     * @param bulkClass   <code>Class</code> il Liquid_gruppo_cori_det modello.
     * @param clauses     <code>CompoundFindClause</code> le clausole della selezione
     * @return sql <code>SQLBuilder</code> Risultato della selezione.
     **/
    public SQLBuilder selectGruppiDetByClause(
            UserContext userContext,
            Liquid_gruppo_coriIBulk gruppo_cori,
            Class bulkClass,
            CompoundFindClause clauses) throws ComponentException {

        if (gruppo_cori == null)
            return null;

        SQLBuilder sql = getHome(userContext, Liquid_gruppo_cori_detIBulk.class, "V_LIQUID_GRUPPO_CORI_DET").createSQLBuilder();

        sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, gruppo_cori.getCd_cds());
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, gruppo_cori.getEsercizio());
        sql.addSQLClause("AND", "PG_LIQUIDAZIONE", sql.EQUALS, gruppo_cori.getPg_liquidazione());
        sql.addSQLClause("AND", "CD_GRUPPO_CR", sql.EQUALS, gruppo_cori.getCd_gruppo_cr());

        sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, gruppo_cori.getCd_unita_organizzativa());
        sql.addSQLClause("AND", "CD_CDS_ORIGINE", sql.EQUALS, gruppo_cori.getCd_cds_origine());
        sql.addSQLClause("AND", "CD_UO_ORIGINE", sql.EQUALS, gruppo_cori.getCd_uo_origine());
        sql.addSQLClause("AND", "PG_LIQUIDAZIONE_ORIGINE", sql.EQUALS, gruppo_cori.getPg_liquidazione_origine());
        sql.addSQLClause("AND", "CD_REGIONE", sql.EQUALS, gruppo_cori.getCd_regione());
        sql.addSQLClause("AND", "PG_COMUNE", sql.EQUALS, gruppo_cori.getPg_comune());


        //if (gruppo_cori == null)
        //return null;

        //SQLBuilder sql = getHome(userContext, Liquid_gruppo_cori_detIBulk.class,"V_LIQUID_GRUPPO_CORI_DET").createSQLBuilder();

        //sql.addSQLClause("AND","CD_CDS",sql.EQUALS,gruppo_cori.getCd_cds());
        //sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,gruppo_cori.getEsercizio());
        //sql.addSQLClause("AND","PG_LIQUIDAZIONE",sql.EQUALS,gruppo_cori.getPg_liquidazione());
        //sql.addSQLClause("AND","CD_GRUPPO_CR",sql.EQUALS,gruppo_cori.getCd_gruppo_cr());
        ////sql.addSQLClause("AND","CD_REGIONE",sql.EQUALS,gruppo_cori.getCd_regione());
        ////sql.addSQLClause("AND","PG_COMUNE",sql.EQUALS,gruppo_cori.getPg_comune());

        return sql;
    }

    /**
     * NON UTILIZZATO (?)
     **/
    public SQLBuilder selectGruppiLocaliByClause(
            UserContext userContext,
            Liquid_gruppo_coriIBulk gruppo_cori,
            Liquid_gruppo_cori_detBulk dett,
            CompoundFindClause clauses) throws ComponentException {

        if (gruppo_cori == null)
            return null;

        //SQLBuilder sql = getHome(userContext, Liquid_gruppo_cori_detIBulk.class,"V_LIQUID_GRUPPO_CORI_DET").createSQLBuilder();
        SQLBuilder sql = getHome(userContext, Liquid_gruppo_cori_detIBulk.class, "V_LIQUID_CENTRO_UO").createSQLBuilder();

        sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, gruppo_cori.getCd_cds());
        sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, gruppo_cori.getCd_unita_organizzativa());
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, gruppo_cori.getEsercizio());
        sql.addSQLClause("AND", "PG_LIQUIDAZIONE", sql.EQUALS, gruppo_cori.getPg_liquidazione());
        sql.addSQLClause("AND", "CD_GRUPPO_CR", sql.EQUALS, gruppo_cori.getCd_gruppo_cr());


        //sql.addSQLClause("AND","CD_CDS_ORIGINE",sql.EQUALS,gruppo_cori.getCd_cds_origine());
        //sql.addSQLClause("AND","CD_UO_ORIGINE",sql.EQUALS,gruppo_cori.getCd_uo_origine());
        //sql.addSQLClause("AND","PG_LIQUIDAZIONE_ORIGINE",sql.EQUALS,gruppo_cori.getPg_liquidazione_origine());
        sql.addSQLClause("AND", "CD_REGIONE", sql.EQUALS, gruppo_cori.getCd_regione());
        sql.addSQLClause("AND", "PG_COMUNE", sql.EQUALS, gruppo_cori.getPg_comune());

        return sql;
    }

    /**
     * Seleziona Gruppi di una Liquidazione per una UO periferica.
     * PreCondition:
     * E' stata generata la richiesta di cercare i Gruppi associati alla riga di
     * liquid_gruppo_cori indicata. Le informazioni verranno tratte dalla vista V_LIQUID_CENTRO_UO.
     * PostCondition:
     * E' stato creato il SQLBuilder con le clausole implicite (presenti nell'istanza di Liquid_gruppo_coriIBulk);
     * inoltre, i gruppi devono essere relativi al Liquid_gruppo_cori indicato.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param gruppo_cori il <code>Liquid_gruppo_coriIBulk</code> gruppo CORI di riferimento.
     * @param dett        <code>Liquid_gruppo_coriBulk</code> il Liquid_gruppo_coriBulk modello.
     * @param clauses     <code>CompoundFindClause</code> le clausole della selezione
     * @return sql <code>SQLBuilder</code> Risultato della selezione.
     * @throws PersistencyException
     **/
    public SQLBuilder selectGruppiLocaliByClause(
            UserContext userContext,
            Liquid_gruppo_coriIBulk gruppo_cori,
            Liquid_gruppo_coriBulk dett,
            CompoundFindClause clauses) throws ComponentException, PersistencyException {

        if (gruppo_cori == null)
            return null;

        Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome(userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);

        SQLBuilder sql = getHome(userContext, Liquid_gruppo_coriIBulk.class, "V_LIQUID_CENTRO_UO").createSQLBuilder();

        sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, gruppo_cori.getCd_cds());
        sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, gruppo_cori.getCd_unita_organizzativa());
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, gruppo_cori.getEsercizio());
        sql.addSQLClause("AND", "PG_LIQUIDAZIONE", sql.EQUALS, gruppo_cori.getPg_liquidazione());

        sql.addSQLClause("AND", "CD_GRUPPO_CR", sql.EQUALS, gruppo_cori.getCd_gruppo_cr());
        sql.addSQLClause("AND", "CD_REGIONE", sql.EQUALS, gruppo_cori.getCd_regione());
        sql.addSQLClause("AND", "PG_COMUNE", sql.EQUALS, gruppo_cori.getPg_comune());
        //aggiunta per la liquidazione da 999
        sql.addSQLClause("AND", "CD_UO_ORIGINE", sql.NOT_EQUALS, uoEnte.getCd_unita_organizzativa());
        return sql;
    }

    public OggettoBulk eseguiLiquidazioneMassaCori(
            UserContext userContext,
            Liquidazione_massa_coriBulk liquidMassa)
            throws it.cnr.jada.comp.ComponentException {

        LoggableStatement cs = null;
        try {
            try {
                cs = new LoggableStatement(getConnection(userContext),
                        "{call " +
                                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                                "CNRCTB573.job_liquid_cori_massa(?,?,?,?,?) }", false, this.getClass());

                String da_es_prec = "N";
                if (liquidMassa.getDa_esercizio_precedente() != null) {
                    da_es_prec = liquidMassa.getDa_esercizio_precedente().booleanValue() ? "Y" : "N";
                }
                cs.setInt(1, liquidMassa.getEsercizio().intValue()); // ESERCIZIO
                cs.setString(2, da_es_prec); // DA ESERCIZIO PREC.
                cs.setTimestamp(3, liquidMassa.getData_da()); // PERIODO DA
                cs.setTimestamp(4, liquidMassa.getData_a()); // PERIODO A
                cs.setString(5, userContext.getUser()); // USER
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
        return liquidMassa;
    }

    public OggettoBulk cercaBatch(
            UserContext userContext,
            Liquidazione_massa_coriBulk liquidMassa)
            throws it.cnr.jada.comp.ComponentException {
        //recupero le note dalla testata del batch
        SQLBuilder sql;
        String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
        LoggableStatement ps = null;
        java.sql.ResultSet rs = null;
        String note = null;
        sql = getHome(userContext, Batch_log_tstaBulk.class).createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("NOTE");
        sql.addSQLClause("AND", "pg_esecuzione", sql.EQUALS, liquidMassa.getPg_exec());
        try {
            ps = sql.prepareStatement(getConnection(userContext));
            rs = ps.executeQuery();
            while (rs.next()) {
                note = new String(rs.getString("NOTE"));
            }
        } catch (SQLException e) {
            throw handleException(e);
        }
        liquidMassa.setNote(note);

        //recupero i dettagli del batch
        Batch_log_rigaBulk batch_log_riga = new Batch_log_rigaBulk();
        BulkHome home = getHome(userContext, Batch_log_rigaBulk.class);
        SQLBuilder sql2 = home.createSQLBuilder();

        sql2.addTableToHeader("BATCH_LOG_TSTA");
        sql2.addSQLJoin("BATCH_LOG_RIGA.PG_ESECUZIONE", "BATCH_LOG_TSTA.PG_ESECUZIONE");
        sql2.addSQLClause("AND", "BATCH_LOG_RIGA.pg_esecuzione", sql2.EQUALS, liquidMassa.getPg_exec());
        sql2.addSQLClause("AND", "BATCH_LOG_TSTA.cd_log_tipo", sql2.EQUALS, new String("LIQUID_CORI_MASS00"));
        try {
            liquidMassa.setBatch_log_riga(new BulkList(home.fetchAll(sql2)));
            getHomeCache(userContext).fetchAll(userContext);
        } catch (PersistencyException e) {
            throw new it.cnr.jada.comp.ComponentException(e.getMessage(), e);
        }

        return liquidMassa;
    }

    public void callRibalta(it.cnr.jada.UserContext userContext, Liquid_gruppo_centroBulk liquid) throws it.cnr.jada.comp.ComponentException {

        LoggableStatement cs = null;
        try {
            try {
                cs = new LoggableStatement(getConnection(userContext),
                        "{call " +
                                it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                                "CNRCTB576.ribalta(?,?,?,?,?,?,?) }", false, this.getClass());

                cs.setInt(1, liquid.getEsercizio().intValue());
                cs.setString(2, liquid.getStato());
                cs.setString(3, liquid.getCd_gruppo_cr());
                cs.setString(4, liquid.getCd_regione());
                cs.setLong(5, liquid.getPg_comune());
                cs.setString(6, liquid.getDa_esercizio_precedente());
                cs.setString(7, liquid.getUser());

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
    }

    public boolean esisteRiga(UserContext userContext, Liquid_gruppo_centroBulk liquid) throws ComponentException {
        try {
            Liquid_gruppo_centroHome liqHome = (Liquid_gruppo_centroHome) getHome(userContext, Liquid_gruppo_centroBulk.class);
            SQLBuilder sql = liqHome.createSQLBuilder();
            sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
            sql.addSQLClause("AND", "CD_GRUPPO_CR", sql.EQUALS, liquid.getCd_gruppo_cr());
            sql.addSQLClause("AND", "CD_REGIONE", sql.EQUALS, liquid.getCd_regione());
            sql.addSQLClause("AND", "PG_COMUNE", sql.EQUALS, liquid.getPg_comune());
            sql.addSQLClause("AND", "STATO", sql.EQUALS, liquid.STATO_INIZIALE);
            sql.addSQLClause("AND", "DA_ESERCIZIO_PRECEDENTE", sql.EQUALS, "Y");
            try {
                return sql.executeExistsQuery(liqHome.getConnection());
            } catch (java.sql.SQLException e) {
                throw handleSQLException(e);
            }
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public List EstraiLista(UserContext userContext, Liquid_coriBulk liquidazione) throws ComponentException {

        F24ep_tempHome home = (F24ep_tempHome) getHome(userContext, F24ep_tempBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "esercizio", sql.EQUALS, liquidazione.getEsercizio());
        sql.addClause("AND", "cd_cds", sql.EQUALS, liquidazione.getCd_cds());
        sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, liquidazione.getCd_unita_organizzativa());
        sql.addClause("AND", "pg_liquidazione", sql.EQUALS, liquidazione.getPg_liquidazione());
        sql.addOrderBy("PROG");
        try {
            return home.fetchAll(sql);
        } catch (PersistencyException e) {
            handleException(e);
        }
        return null;
    }

    public AnagraficoBulk getAnagraficoEnte(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configurazione = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
            AnagraficoBulk ente = (AnagraficoBulk) getHome(userContext, AnagraficoBulk.class).findByPrimaryKey(
                    new AnagraficoBulk(
                            new Integer(
                                    configurazione.getIm01(userContext, new Integer(0), null, "COSTANTI", "CODICE_ANAG_ENTE").toString()
                            )
                    )
            );
            ente.setComune_fiscale((ComuneBulk) getHome(userContext, ComuneBulk.class).findByPrimaryKey(new ComuneBulk(ente.getPg_comune_fiscale())));
            ente.setComune_nascita((ComuneBulk) getHome(userContext, ComuneBulk.class).findByPrimaryKey(new ComuneBulk(ente.getPg_comune_nascita())));
            return ente;
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public String getContoSpecialeEnteF24(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configurazione = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
            return configurazione.getVal01(userContext, new Integer(0), null, "F24_EP", "CONTO_CORRENTE");
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public void Popola_f24(UserContext userContext, Liquid_coriBulk liquidazione) throws ComponentException {
        try {
            // Elimina pendenti con la stessa liquidazione
            F24ep_tempHome home = (F24ep_tempHome) getHome(userContext, F24ep_tempBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
            sql.addSQLClause("AND", "cd_cds", sql.EQUALS, liquidazione.getCd_cds());
            sql.addSQLClause("AND", "cd_unita_organizzativa", sql.EQUALS, liquidazione.getCd_unita_organizzativa());
            sql.addSQLClause("AND", "pg_liquidazione", sql.EQUALS, liquidazione.getPg_liquidazione());
            sql.addOrderBy("PROG");
            List lista = home.fetchAll(sql);
            for (Iterator i = lista.iterator(); i.hasNext(); ) {
                F24ep_tempBulk F24ep_temp = (F24ep_tempBulk) i.next();
                F24ep_temp.setToBeDeleted();
                ((CRUDComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession", it.cnr.jada.ejb.CRUDComponentSession.class)).eliminaConBulk(userContext, F24ep_temp);
            }

            home = (F24ep_tempHome) getHome(userContext, F24ep_tempBulk.class, "V_F24EP", "none");
            sql = home.createSQLBuilder();
            sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
            sql.addSQLClause("AND", "cd_cds", sql.EQUALS, liquidazione.getCd_cds());
            sql.addSQLClause("AND", "cd_unita_organizzativa", sql.EQUALS, liquidazione.getCd_unita_organizzativa());
            sql.addSQLClause("AND", "pg_liquidazione", sql.EQUALS, liquidazione.getPg_liquidazione());
            sql.addOrderBy("PROG");
            lista = home.fetchAll(sql);
            for (Iterator i = lista.iterator(); i.hasNext(); ) {
                F24ep_tempBulk F24ep_temp = (F24ep_tempBulk) i.next();
                F24ep_tempBulk new_F24ep_temp = new F24ep_tempBulk();
                new_F24ep_temp.setEsercizio(F24ep_temp.getEsercizio());
                new_F24ep_temp.setAnno_rif(F24ep_temp.getAnno_rif());
                new_F24ep_temp.setMese_rif(F24ep_temp.getMese_rif());
                new_F24ep_temp.setCd_cds(F24ep_temp.getCd_cds());
                new_F24ep_temp.setCd_unita_organizzativa(F24ep_temp.getCd_unita_organizzativa());
                new_F24ep_temp.setCodice_ente(F24ep_temp.getCodice_ente());
                new_F24ep_temp.setCodice_tributo(F24ep_temp.getCodice_tributo());
                new_F24ep_temp.setImporto_debito(F24ep_temp.getImporto_debito());
                new_F24ep_temp.setPg_liquidazione(F24ep_temp.getPg_liquidazione());
                new_F24ep_temp.setToBeCreated();
                ((CRUDComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession", it.cnr.jada.ejb.CRUDComponentSession.class)).creaConBulk(userContext, new_F24ep_temp);
            }
        } catch (PersistencyException e) {
            handleException(e);
        } catch (RemoteException e) {
            handleException(e);
        } catch (EJBException e) {
            handleException(e);
        }
    }

    public List EstraiListaTot(UserContext userContext, Liquid_coriBulk liquidazione) throws ComponentException {

        F24ep_tempTotHome home = (F24ep_tempTotHome) getHome(userContext, F24ep_tempTotBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "esercizio", sql.EQUALS, liquidazione.getEsercizio());
        sql.addClause("AND", "cd_cds", sql.EQUALS, liquidazione.getCd_cds());
        sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, liquidazione.getCd_unita_organizzativa());
        sql.addClause("AND", "pg_liquidazione", sql.EQUALS, liquidazione.getPg_liquidazione());
        sql.addOrderBy(" tipo_riga_f24,codice_tributo,codice_ente,mese_rif,anno_rif,esercizio,cd_cds,cd_unita_organizzativa,pg_liquidazione,cd_matricola_inps,periodo_da,periodo_a");
        try {
            return home.fetchAll(sql);
        } catch (PersistencyException e) {
            handleException(e);
        }
        return null;
    }

    public void Popola_f24Tot(UserContext userContext, Liquid_coriBulk liquidazione) throws ComponentException {
        try {
            if (liquidazione != null && liquidazione.getPg_liquidazione() != null) {
                F24ep_tempTotHome home = (F24ep_tempTotHome) getHome(userContext, F24ep_tempTotBulk.class);
                SQLBuilder sql = home.createSQLBuilder();
                sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
                sql.addSQLClause("AND", "cd_cds", sql.EQUALS, liquidazione.getCd_cds());
                sql.addSQLClause("AND", "cd_unita_organizzativa", sql.EQUALS, liquidazione.getCd_unita_organizzativa());
                sql.addSQLClause("AND", "pg_liquidazione", sql.EQUALS, liquidazione.getPg_liquidazione());
                sql.addOrderBy("PROG");
                List lista = home.fetchAll(sql);
                for (Iterator i = lista.iterator(); i.hasNext(); ) {
                    F24ep_tempTotBulk F24ep_temp = (F24ep_tempTotBulk) i.next();
                    F24ep_temp.setToBeDeleted();
                	((CRUDComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession",it.cnr.jada.ejb.CRUDComponentSession.class)).eliminaConBulk(userContext,F24ep_temp);
                }
            }

            List lista;
            F24ep_tempTotHome home = (F24ep_tempTotHome) getHome(userContext, F24ep_tempTotBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            home = (F24ep_tempTotHome) getHome(userContext, F24ep_tempTotBulk.class, "V_F24EP", "none");
            sql = home.createSQLBuilder();
            sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
            sql.addSQLClause("AND", "cd_cds", sql.EQUALS, liquidazione.getCd_cds());
            sql.addSQLClause("AND", "cd_unita_organizzativa", sql.EQUALS, liquidazione.getCd_unita_organizzativa());
            sql.addSQLClause("AND", "pg_liquidazione", sql.EQUALS, liquidazione.getPg_liquidazione());
            sql.addOrderBy(" tipo_riga_f24,codice_tributo,codice_ente,mese_rif,anno_rif,esercizio,cd_cds,cd_unita_organizzativa,pg_liquidazione,cd_matricola_inps,periodo_da,periodo_a");

            lista = home.fetchAll(sql);
            for (Iterator i = lista.iterator(); i.hasNext(); ) {
                F24ep_tempTotBulk F24ep_temp = (F24ep_tempTotBulk) i.next();
                F24ep_tempTotBulk new_F24ep_temp = new F24ep_tempTotBulk();
                new_F24ep_temp.setTipo_riga_f24(F24ep_temp.getTipo_riga_f24());
                new_F24ep_temp.setEsercizio(F24ep_temp.getEsercizio());
                new_F24ep_temp.setAnno_rif(F24ep_temp.getAnno_rif());
                new_F24ep_temp.setMese_rif(F24ep_temp.getMese_rif());
                new_F24ep_temp.setCd_cds(F24ep_temp.getCd_cds());
                new_F24ep_temp.setCd_unita_organizzativa(F24ep_temp.getCd_unita_organizzativa());
                new_F24ep_temp.setCodice_ente(F24ep_temp.getCodice_ente());
                new_F24ep_temp.setCodice_tributo(F24ep_temp.getCodice_tributo());
                new_F24ep_temp.setImporto_debito(F24ep_temp.getImporto_debito());
                new_F24ep_temp.setPg_liquidazione(F24ep_temp.getPg_liquidazione());
                new_F24ep_temp.setCd_matricola_inps(F24ep_temp.getCd_matricola_inps());
                new_F24ep_temp.setPeriodo_da(F24ep_temp.getPeriodo_da());
                new_F24ep_temp.setPeriodo_a(F24ep_temp.getPeriodo_a());
                new_F24ep_temp.setToBeCreated();
            	((CRUDComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession",it.cnr.jada.ejb.CRUDComponentSession.class)).creaConBulk(userContext,new_F24ep_temp);	
            	
            }
        } catch (PersistencyException e) {
            handleException(e);
        } catch (EJBException e) {
            handleException(e);
        } catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public String getSedeInpsF24(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configurazione = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
            return configurazione.getVal01(userContext, new Integer(0), null, "F24_EP", "SEDE_INPS");
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public Configurazione_cnrBulk getSedeInailF24(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            return Utility.createConfigurazioneCnrComponentSession().getConfigurazione(userContext, new Integer(0), null, "F24_EP", "SEDE_INAIL");
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public String getSedeInpdapF24(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configurazione = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
            return configurazione.getVal01(userContext, new Integer(0), null, "F24_EP", "SEDE_INPDAP");
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public String getSedeInpgiF24(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
        try {
            it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configurazione = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
            return configurazione.getVal01(userContext, new Integer(0), null, "F24_EP", "SEDE_INPGI");
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public void eliminaPendenti_f24Tot(UserContext userContext) throws ComponentException {
        try {
            // Elimina pendenti con la stessa liquidazione
            F24ep_tempTotHome home = (F24ep_tempTotHome) getHome(userContext, F24ep_tempTotBulk.class);
            SQLBuilder sql = home.createSQLBuilder();
            sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
            sql.addSQLClause("AND", "cd_cds", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
            sql.addSQLClause("AND", "cd_unita_organizzativa", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
            sql.addOrderBy("PROG");
            List lista = home.fetchAll(sql);
            for (Iterator i = lista.iterator(); i.hasNext(); ) {
                F24ep_tempTotBulk F24ep_temp = (F24ep_tempTotBulk) i.next();
                F24ep_temp.setToBeDeleted();
                ((CRUDComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession", it.cnr.jada.ejb.CRUDComponentSession.class)).eliminaConBulk(userContext, F24ep_temp);
            }
        } catch (PersistencyException e) {
            handleException(e);
        } catch (RemoteException e) {
            handleException(e);
        } catch (EJBException e) {
            handleException(e);
        }
    }
    public RemoteIterator ricercaCori(UserContext userContext, ParSelConsLiqCoriBulk parametri) throws ComponentException
    {
        VConsLiqCoriHome coriHome = (VConsLiqCoriHome)getHome(userContext, VConsLiqCoriBulk.class);
        SQLBuilder sql = coriHome.createSQLBuilder();
        sql.addSQLClause("AND","CD_UO_LIQUIDAZIONE",SQLBuilder.GREATER_EQUALS,parametri.getCd_uo_liquidazione());
        sql.addSQLClause("AND","ESERCIZIO_LIQUIDAZIONE",SQLBuilder.GREATER_EQUALS,parametri.getEsercizio_liquidazione());
        sql.addSQLClause("AND","NUMERO_MANDATO",SQLBuilder.GREATER_EQUALS,parametri.getPgInizio());
        if (parametri.getPgInizio() != null ){
            sql.addSQLClause("AND","NUMERO_MANDATO",SQLBuilder.GREATER_EQUALS,parametri.getPgInizio());
        }
        if (parametri.getPgFine() != null ){
            sql.addSQLClause("AND","NUMERO_MANDATO",SQLBuilder.LESS_EQUALS,parametri.getPgFine());
        }
        if (parametri.getDaLiquidazione() != null && parametri.getDaLiquidazione().getPg_liquidazione() != null){
            sql.addSQLClause("AND","PG_LIQUIDAZIONE",SQLBuilder.GREATER_EQUALS,parametri.getDaLiquidazione().getPg_liquidazione());
        }
        if (parametri.getaLiquidazione() != null && parametri.getaLiquidazione().getPg_liquidazione() != null){
            sql.addSQLClause("AND","PG_LIQUIDAZIONE",SQLBuilder.LESS_EQUALS,parametri.getaLiquidazione().getPg_liquidazione());
        }

        return  iterator(userContext,sql,VConsLiqCoriBulk.class,null);
    }

    public ParSelConsLiqCoriBulk initializeConsultazioneCori(UserContext userContext, ParSelConsLiqCoriBulk parSelConsLiqCoriBulk) throws PersistencyException, ComponentException {
        parSelConsLiqCoriBulk.setEsercizio_liquidazione(CNRUserContext.getEsercizio(userContext));
        parSelConsLiqCoriBulk.setCd_cds_doc(CNRUserContext.getCd_cds(userContext));
        parSelConsLiqCoriBulk.setCd_uo_liquidazione(CNRUserContext.getCd_unita_organizzativa(userContext));
        Unita_organizzativaBulk uo = new Unita_organizzativaBulk(parSelConsLiqCoriBulk.getCd_uo_liquidazione());
        uo = (Unita_organizzativaBulk) getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(uo);
        parSelConsLiqCoriBulk.setDs_uo_liquidazione(uo.getDs_unita_organizzativa());
        return parSelConsLiqCoriBulk;
    }
    public SQLBuilder selectDaLiquidazioneByClause (UserContext userContext,
                                                          ParSelConsLiqCoriBulk par, Liquid_coriBulk liquid_coriBulk, CompoundFindClause clause) throws ComponentException, PersistencyException
    {
        Liquid_coriHome liquidHome = (Liquid_coriHome)getHome(userContext, Liquid_coriBulk.class);
        SQLBuilder sql = liquidHome.createSQLBuilder();
        sql.addClause( clause );
        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
        sql.addSQLClause("AND", "stato", SQLBuilder.EQUALS, "L");
        sql.addSQLClause("AND", "PG_LIQUIDAZIONE", SQLBuilder.GREATER_EQUALS,new Integer("0"));
        sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS,((CNRUserContext) userContext).getCd_unita_organizzativa());
        sql.addOrderBy("pg_liquidazione");
        return sql;
    }
    public SQLBuilder selectALiquidazioneByClause (UserContext userContext,
                                                    ParSelConsLiqCoriBulk par, Liquid_coriBulk liquid_coriBulk, CompoundFindClause clause) throws ComponentException, PersistencyException
    {
        Liquid_coriHome liquidHome = (Liquid_coriHome)getHome(userContext, Liquid_coriBulk.class);
        SQLBuilder sql = liquidHome.createSQLBuilder();
        sql.addClause( clause );
        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
        sql.addSQLClause("AND", "stato", SQLBuilder.EQUALS, "L");
        sql.addSQLClause("AND", "PG_LIQUIDAZIONE", SQLBuilder.GREATER_EQUALS,new Integer("0"));
        sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS,((CNRUserContext) userContext).getCd_unita_organizzativa());
        sql.addOrderBy("pg_liquidazione");
        return sql;
    }
}
