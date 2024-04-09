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

package it.cnr.contab.fondecon00.comp;

import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.EnteBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.comp.DateServices;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.fondecon00.core.bulk.*;
import it.cnr.contab.fondecon00.views.bulk.Vsx_reintegro_fondoBulk;
import it.cnr.contab.fondecon00.views.bulk.Vsx_reintegro_fondoHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;

import javax.ejb.EJBException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Optional;

public class FondoEconomaleComponent extends it.cnr.jada.comp.CRUDComponent implements IFondoEconomaleMgr, it.cnr.jada.comp.IPrintMgr {
    /**
     * FondoEconomaleComponent costruttore standard.
     */
    public FondoEconomaleComponent() {
        super();
    }

//^^@@

    /**
     * Normale.
     * PreCondition:
     * Richiesta di associazione di tutte le spese del fondo
     * PostCondition:
     * Collega l'obbligazione scadenza scelta alle spese sel fondo
     */
//^^@@
    public void associaTutteSpese(
            UserContext context,
            Fondo_economaleBulk testata,
            Obbligazione_scadenzarioBulk obbScad)
            throws it.cnr.jada.comp.ComponentException {

        LoggableStatement ps = null;
        try {
            lockBulk(context, testata);
            Fondo_spesaHome spesaHome = (Fondo_spesaHome) getHome(context, Fondo_spesaBulk.class);

            SQLBuilder sqlSpese = spesaHome.cercaSpeseAssociabili(context, testata, obbScad);
            sqlSpese.setForUpdate(true);
            ps = sqlSpese.prepareStatement(getConnection(context));
            ps.execute();
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
            ;

            SQLBuilder sql = spesaHome.ass_all_speSQL(context, testata, obbScad);
            if (sql != null) {
                ps = sql.prepareStatement(getConnection(context));
                ps.execute();
            }

        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        } catch (it.cnr.jada.bulk.OutdatedResourceException e) {
            throw handleException(e);
        } catch (it.cnr.jada.bulk.BusyResourceException e) {
            throw handleException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        } finally {
            if (ps != null) try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
            ;
        }
    }

    /**
     * Associazione.
     * <p>
     * Nome: Associare a Obbligazione_scadenzarioBulk;
     * Pre:  Associare tutte le spese selezionate all'Obbligazione_scadenzarioBulk;
     * Post: Tutte le spese selezionate vengono associare all'Obbligazione_scadenzarioBulk.
     *
     * @param fondo   testata delle spese in elenco.
     * @param obbscad Obbligazione scadenzario da associare.
     */

    public void associazione(UserContext context, Fondo_economaleBulk testata, Obbligazione_scadenzarioBulk obbscad) throws it.cnr.jada.comp.ComponentException {

        LoggableStatement ps = null;
        try {
            lockBulk(context, testata);

            lockBulk(context, obbscad);
            ObbligazioneBulk obbRicaricata = (ObbligazioneBulk) getHome(context, ObbligazioneBulk.class).findAndLock(obbscad.getObbligazione());
            if ("Y".equalsIgnoreCase(obbRicaricata.getRiportato()))
                throw new ApplicationException("L'impegno \"" + obbRicaricata.getEsercizio_originale() + "/" + obbRicaricata.getPg_obbligazione() + "\" è stato riportato da altri ad esercizio successivo durante la fase di associazione! Operazione interrotta.");

            Fondo_spesaHome spesaHome = (Fondo_spesaHome) getHome(context, Fondo_spesaBulk.class);

            ps = new LoggableStatement(getConnection(context),
                    spesaHome.ass_spe_obbSQL(context, testata, obbscad), true, this.getClass());
            ps.execute();
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
            ;

            //Aggiornamento dell'importo associato a documenti amministrativi
            //della scadenza associata alle spese del fondo
            updateScadenzaWith(context, testata, obbscad);

        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        } catch (it.cnr.jada.bulk.OutdatedResourceException e) {
            throw handleException(e);
        } catch (it.cnr.jada.bulk.BusyResourceException e) {
            throw handleException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        } finally {
            if (ps != null) try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
            ;
        }
    }

//^^@@

    /**
     * Normale.
     * PreCondition:
     * Richiesta di calcolare il totale delle spese del fondo economale associate alla scadenza obbligazione
     * PostCondition:
     * Restituisce il valore del calcolo effettuato
     */
//^^@@
    public java.math.BigDecimal calcolaTotaleSpese(
            UserContext userContext,
            Fondo_economaleBulk fondo,
            Obbligazione_scadenzarioBulk scadenza)
            throws it.cnr.jada.comp.ComponentException {

        java.math.BigDecimal tot = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        Fondo_spesaHome home = (Fondo_spesaHome) getHome(userContext, Fondo_spesaBulk.class, "V_ASS_OBBSCAD_FONDO_SPESA");
        SQLBuilder sql = home.getTotaleSpese(fondo, scadenza);
        try {
            java.sql.ResultSet rs = null;
            LoggableStatement ps = null;
            try {
                ps = sql.prepareStatement(getConnection(userContext));
                try {
                    rs = ps.executeQuery();
                    if (rs.next())
                        tot = rs.getBigDecimal(1);
                } catch (java.sql.SQLException e) {
                    throw handleSQLException(e);
                } finally {
                    if (rs != null) try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                    }
                    ;
                }
            } finally {
                if (ps != null) try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
                ;
            }
        } catch (java.sql.SQLException ex) {
            throw handleException(ex);
        }
        if (tot == null)
            tot = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);

        return tot;
    }

    /**
     * Reintegra le spese in elenco sul fondo economale
     */

    private void callChiudiFondo(
            UserContext userContext,
            Fondo_economaleBulk fondo)
            throws it.cnr.jada.comp.ComponentException {

        LoggableStatement cs = null;
        try {
            cs = new LoggableStatement(getConnection(userContext),
                    "{ call " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "CNRCTB130.chiudiFondo(?, ?, ?, ?, ?) }", false, this.getClass());
            cs.setString(1, fondo.getCd_cds());
            cs.setInt(2, fondo.getEsercizio().intValue());
            cs.setString(3, fondo.getCd_unita_organizzativa());
            cs.setString(4, fondo.getCd_codice_fondo());
            cs.setString(5, userContext.getUser());
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
     * Reintegra le spese in elenco sul fondo economale
     */

    private void callChiudiSpese(
            UserContext userContext,
            Fondo_economaleBulk fondo)
            throws it.cnr.jada.comp.ComponentException {

        LoggableStatement cs = null;
        try {
            cs = new LoggableStatement(getConnection(userContext),
                    "{ call " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "CNRCTB130.chiudiSpese(?, ?, ?, ?, ?) }", false, this.getClass());
            cs.setString(1, fondo.getCd_cds());
            cs.setInt(2, fondo.getEsercizio().intValue());
            cs.setString(3, fondo.getCd_unita_organizzativa());
            cs.setString(4, fondo.getCd_codice_fondo());
            cs.setString(5, userContext.getUser());
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
     * Reintegra le spese in elenco sul fondo economale
     */

    private Long callGetPgPerReintegroSpese(
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
                //cs.setString(1, cd_cds);
                cs.executeQuery();
                pg = new Long(cs.getLong(1));
            } catch (Throwable e) {
                throw handleException(e);
            } finally {
                if (cs != null) cs.close();
            }
        } catch (java.sql.SQLException ex) {
            throw handleException(ex);
        }
        if (pg == null)
            throw new it.cnr.jada.comp.ApplicationException("Impossibile ottenere un progressivo valido per la vista di rintegrazione spese!");
        return pg;
    }

    /**
     * Reintegra le spese in elenco sul fondo economale
     */

    private void callReintegroSpese(
            UserContext userContext,
            Long progressivo)
            throws it.cnr.jada.comp.ComponentException {

        LoggableStatement cs = null;
        try {
            cs = new LoggableStatement(getConnection(userContext),
                    "{ call " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "CNRCTB130.vsx_reintegraSpeseFondo(?) }", false, this.getClass());
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

//^^@@

    /**
     * Normale.
     * PreCondition:
     * Richiesta di ricerca dei fondi economali creati
     * PostCondition:
     * Restituisce l'elenco delle corrispondenze
     */
//^^@@
    public RemoteIterator cercaFondi(it.cnr.jada.UserContext context) throws it.cnr.jada.comp.ComponentException {

        SQLBuilder sql = getHome(context, Fondo_economaleBulk.class).createSQLBuilder();

        sql.addClause("AND", "esercizio", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context));
        sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(context));

        return iterator(
                context,
                sql,
                Fondo_economaleBulk.class,
                "default");
    }

//^^@@

    /**
     * Normale.
     * PreCondition:
     * Richiesta di estrazione di tutti i mandati validi per integrare il fondo
     * PostCondition:
     * Restituisce l'elenco delle corrispondenze valide
     * Mandati di integrazione.
     * PreCondition:
     * Una delle corrispondenze è già collegata ad un fondo
     * PostCondition:
     * La corrispondenza non viene aggiunta all'elenco
     * Mandati di apertura.
     * PreCondition:
     * Una delle corrispondenze è mandato di apertura fondo economale
     * PostCondition:
     * La corrispondenza non viene aggiunta all'elenco
     */
//^^@@
    public RemoteIterator cercaMandatiPerIntegrazioni(
            UserContext context,
            Fondo_economaleBulk fondo)
            throws it.cnr.jada.comp.ComponentException {

        return iterator(
                context,
                selectMandatiPerIntegrazione(context, fondo),
                MandatoIBulk.class,
                "default");
    }

//^^@@

    /**
     * Tutti i controlli  superati.
     * PreCondition:
     * Ricerca la lista delle scadenze di obbligazioni congruenti con la spesa passiva che si sta creando/modificando.
     * PostCondition:
     * Le scadenze vengono aggiunte alla lista delle scadenze congruenti.
     * Validazione lista delle obbligazioni per le fatture passive
     * PreCondition:
     * Si è verificato un errore nel caricamento delle scadenze delle obbligazioni.
     * PostCondition:
     * Viene inviato il messaggio corrispondente all'errore segnalato.
     * Obbligazione definitiva
     * PreCondition:
     * La scadenza non appartiene ad un'obbligazione definitiva
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Obbligazioni non cancellate
     * PreCondition:
     * La scadenza appartiene ad un'obbligazione cancellata
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Obbligazioni associate ad altri documenti amministrativi
     * PreCondition:
     * La scadenza appartiene ad un'obbligazione associata ad altri documenti amministrativi
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Obbligazioni della stessa UO
     * PreCondition:
     * La scadenza dell'obbligazione non appartiene alla stessa UO di generazione spesa passiva
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Abilitazione filtro di selezione sulla data di scadenza
     * PreCondition:
     * La scadenza dell'obbligazione ha una data scadenza precedente alla data di filtro
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Abilitazione filtro importo scadenza
     * PreCondition:
     * La scadenza dell'obbligazione ha un importo di scadenza inferiore a quella di filtro
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     * Abilitazione filtro sul progressivo dell'obbligazione o scadenza
     * PreCondition:
     * La scadenza dell'obbligazione non ha progressivo specificato
     * PostCondition:
     * La scadenza non viene aggiunta alla lista delle scadenze congruenti.
     */
//^^@@
    public RemoteIterator cercaObb_scad(it.cnr.jada.UserContext context, it.cnr.contab.fondecon00.core.bulk.Filtro_ricerca_obbligazioniVBulk filtro) throws it.cnr.jada.comp.ComponentException {

        Fondo_economaleHome fondoHome = (Fondo_economaleHome) getHome(context, Fondo_economaleBulk.class);

        return iterator(
                context,
                fondoHome.cercaObb_scad(context, filtro),
                Obbligazione_scadenzarioBulk.class,
                "default");
    }

    public it.cnr.jada.persistency.sql.SQLBuilder selectSospeso_di_chiusura_cd_sospeso_padreByClause(UserContext userContext, Fondo_economaleBulk fondo, SospesoBulk sospeso, CompoundFindClause findClause) throws it.cnr.jada.comp.ComponentException {
        it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(userContext, SospesoBulk.class).createSQLBuilder();

        Optional.ofNullable(findClause)
                .ifPresent(findClause1 -> sql.addClause(findClause1));
        sql.openParenthesis("AND");
        sql.addSQLClause("OR", "IM_ASS_MOD_1210", sql.EQUALS, new java.math.BigDecimal(0));
        sql.addSQLClause("OR", "IM_ASS_MOD_1210", sql.ISNULL, null);
        sql.closeParenthesis();

        //Errore segnalato. Il controllo sull'importo è rimanadato
        //alla reversale di chiusura 03/12/2003
        sql.addSQLClause("AND", "(IM_SOSPESO - IM_ASSOCIATO)", sql.GREATER_EQUALS, fondo.getIm_residuo_fondo().subtract(fondo.getIm_totale_netto_spese()));
        // r.p. 12/02/2013 la condizione precedente era commentata

        try {
            EnteBulk ente = (EnteBulk) getHome(userContext, EnteBulk.class)
                    .findAll().get(0);

            if (!Utility.createParametriCnrComponentSession().getParametriCnr(userContext, fondo.getEsercizio()).getFl_tesoreria_unica().booleanValue()) {
                sql.addClause("AND", "stato_sospeso", sql.EQUALS, SospesoBulk.STATO_SOSP_ASS_A_CDS);
                sql.addClause("AND", "fl_stornato", sql.EQUALS, Boolean.FALSE);

                sql.addClause("AND", "cd_cds", sql.EQUALS, fondo.getCd_cds());

                sql.addClause("AND", "cd_cds_origine", sql.EQUALS, fondo.getCd_cds());
            } else {
                sql.addClause("AND", "cd_cds", sql.EQUALS, ente.getCd_unita_organizzativa());
                sql.openParenthesis("AND");
                sql.openParenthesis("AND");
                sql.addClause("AND", "stato_sospeso", sql.EQUALS, SospesoBulk.STATO_SOSP_ASS_A_CDS);
                sql.addClause("AND", "cd_cds_origine", sql.EQUALS, fondo.getCd_cds());
                sql.closeParenthesis();
                sql.openParenthesis("OR");
                sql.addClause("OR", "stato_sospeso", sql.EQUALS, SospesoBulk.STATO_SOSP_IN_SOSPESO);
                sql.addClause("AND", "cd_cds_origine", sql.ISNULL, null);
                sql.closeParenthesis();
                sql.closeParenthesis();
            }

            sql.addClause("AND", "ti_entrata_spesa", sql.EQUALS, SospesoBulk.TIPO_ENTRATA);
            sql.addClause("AND", "ti_sospeso_riscontro", sql.EQUALS, SospesoBulk.TI_SOSPESO);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(fondo, e);
        } catch (RemoteException e) {
            throw handleException(fondo, e);
        } catch (EJBException e) {
            throw handleException(fondo, e);
        }
        try {
            int annoSolare = Fondo_spesaBulk.getDateCalendar(getHome(userContext, fondo).getServerDate()).get(Calendar.YEAR);
            int esScrivania = CNRUserContext.getEsercizio(userContext).intValue();
            if (annoSolare != esScrivania) {
                sql.openParenthesis("AND");
                sql.addClause("AND", "esercizio", sql.EQUALS, fondo.getEsercizio());
                sql.addClause("OR", "esercizio", sql.EQUALS, new Integer(annoSolare));
                sql.closeParenthesis();
            } else
                sql.addClause("AND", "esercizio", sql.EQUALS, fondo.getEsercizio());
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(fondo, e);
        }
        return sql;
    }

    public RemoteIterator cercaSospesiDiChiusuraFondo(
            UserContext userContext,
            Fondo_economaleBulk fondo)
            throws it.cnr.jada.comp.ComponentException {
        return iterator(
                userContext,
                selectSospeso_di_chiusura_cd_sospeso_padreByClause(userContext, fondo, null, null),
                SospesoBulk.class,
                "default");
    }

    /**
     * Ricerca spese del fondo.
     * <p>
     * Nome: Ricerca spese;
     * Pre:  Ricerca delle spese con filtro;
     * Post: Viene creato un elenco delle spese che corrispondono ai parametri impostati nel filtro.
     *
     * @param filtro su cui va impostata la ricerca.
     * @return l'elenco di spese selezionate.
     */

    public RemoteIterator cercaSpese(UserContext context, Filtro_ricerca_speseVBulk filtro) throws it.cnr.jada.comp.ComponentException {

        Fondo_spesaHome spesaHome = (Fondo_spesaHome) getHome(context, Fondo_spesaBulk.class);

        return iterator(
                context,
                spesaHome.cercaSpese(context, filtro),
                Fondo_spesaBulk.class,
                "default");
    }

//^^@@

    /**
     * Tutti i controlli  superati.
     * PreCondition:
     * Ricerca la lista delle spese associabili alla scadenza di obbligazioni
     * PostCondition:
     * Le spese vengono aggiunte alla lista delle spese congruenti.
     * Validazione lista delle obbligazioni per le fatture passive
     * PreCondition:
     * Si è verificato un errore nel caricamento delle scadenze delle spese.
     * PostCondition:
     * Viene inviato il messaggio corrispondente all'errore segnalato.
     * Spesa reintegrata
     * PreCondition:
     * La spesa è reintegrata
     * PostCondition:
     * La spesa non viene aggiunta alla lista delle spese congruenti.
     * Spesa documentata
     * PreCondition:
     * La spesa è documentata
     * PostCondition:
     * La spesa non viene aggiunta alla lista delle spese congruenti.
     * Spesa di altro fondo
     * PreCondition:
     * La spesa appartiene ad un altro fondo economale
     * PostCondition:
     * La spesa non viene aggiunta alla lista delle spese congruenti.
     * Spesa già associata
     * PreCondition:
     * La spesa è già associata alla scadenza obbligazione passata
     * PostCondition:
     * La spesa viene aggiunta alla lista delle spese congruenti per permetterne la disassociazione.
     */
//^^@@
    public RemoteIterator cercaSpeseAssociabili(
            UserContext context,
            Fondo_economaleBulk testata,
            Obbligazione_scadenzarioBulk scadenzaSelezionata)
            throws it.cnr.jada.comp.ComponentException {

        Fondo_spesaHome spesaHome = (Fondo_spesaHome) getHome(context, Fondo_spesaBulk.class);

        return iterator(
                context,
                spesaHome.cercaSpeseAssociabili(context, testata, scadenzaSelezionata),
                Fondo_spesaBulk.class,
                "default");
    }

//^^@@

    /**
     * Tutti i controlli  superati.
     * PreCondition:
     * Ricerca la lista delle spese appartenenti al fondo economale
     * PostCondition:
     * Le spese vengono aggiunte alla lista delle spese congruenti.
     * Validazione lista delle spese
     * PreCondition:
     * Si è verificato un errore nel caricamento delle spese.
     * PostCondition:
     * Viene inviato il messaggio corrispondente all'errore segnalato.
     * Spesa di altro fondo
     * PreCondition:
     * La spesa appartiene ad un altro fondo economale
     * PostCondition:
     * La spesa non viene aggiunta alla lista delle spese congruenti.
     * Spesa già reintegrata
     * PreCondition:
     * La spesa è già reintegrata
     * PostCondition:
     * La spesa viene aggiunta alla lista delle spese congruenti se il  filtro di
     * ricerca per le spese reintegrate era abilitato
     * Spesa documentata
     * PreCondition:
     * La spesa è documentata
     * PostCondition:
     * La spesa viene aggiunta alla lista delle spese congruenti se il  filtro di
     * ricerca per le spese docuemtnate era abilitato
     */
//^^@@
    public RemoteIterator cercaSpeseDelFondo(
            UserContext userContext,
            Fondo_economaleBulk fondo,
            Obbligazione_scadenzarioBulk scadenza)
            throws it.cnr.jada.comp.ComponentException {

        Fondo_spesaHome home = (Fondo_spesaHome) getHome(userContext, Fondo_spesaBulk.class, "V_ASS_OBBSCAD_FONDO_SPESA");

        return iterator(userContext,
                home.cercaSpeseDelFondo(fondo, scadenza),
                Fondo_spesaBulk.class,
                "default");
    }

//^^@@

    /**
     * Tutti i controlli  superati.
     * PreCondition:
     * Ricerca la lista delle spese reintegrabili appartenenti al fondo economale
     * PostCondition:
     * Le spese vengono aggiunte alla lista delle spese congruenti.
     * Validazione lista delle spese
     * PreCondition:
     * Si è verificato un errore nel caricamento delle spese.
     * PostCondition:
     * Viene inviato il messaggio corrispondente all'errore segnalato.
     * Spesa di altro fondo
     * PreCondition:
     * La spesa appartiene ad un altro fondo economale
     * PostCondition:
     * La spesa non viene aggiunta alla lista delle spese congruenti.
     * Spesa già reintegrata
     * PreCondition:
     * La spesa è già reintegrata
     * PostCondition:
     * La spesa non viene aggiunta alla lista delle spese congruenti
     */
//^^@@
    public RemoteIterator cercaSpeseReintegrabili(UserContext context, Filtro_ricerca_speseVBulk filtro) throws it.cnr.jada.comp.ComponentException {

        Fondo_spesaHome spesaHome = (Fondo_spesaHome) getHome(context, Fondo_spesaBulk.class);

        return iterator(
                context,
                spesaHome.cercaSpeseReintegrabili(context, filtro),
                Fondo_spesaBulk.class,
                "default");
    }

//^^@@

    /**
     * Tutti i controlli  superati.
     * PreCondition:
     * Richiesta la chiusura del fondo economale
     * PostCondition:
     * Il fondo economale viene chiuso
     * Validazione lista del fondo
     * PreCondition:
     * Si è verificato un errore
     * PostCondition:
     * Viene inviato il messaggio corrispondente all'errore segnalato.
     * Fondo già chiuso
     * PreCondition:
     * Il fondo economale è già stato chiuso
     * PostCondition:
     * L'operazione viene annullata
     * Le spese del fondo economale non sono ancora state reintegrate
     * PreCondition:
     * Esiste almeno una spesa non reintegrata
     * PostCondition:
     * L'operazione viene annullata
     */
//^^@@
    public Fondo_economaleBulk chiudeFondo(
            UserContext userContext,
            Fondo_economaleBulk fondo)
            throws ComponentException {

        SospesoBulk sosp = fondo.getSospeso_di_chiusura();
        if (sosp != null)
            try {
                getHome(userContext, sosp).lock(sosp);
            } catch (Throwable t) {
                throw handleException(sosp, t);
            }

        fondo = (Fondo_economaleBulk) modificaConBulk(userContext, fondo);

        callChiudiFondo(userContext, fondo);

        return fondo;
    }
//^^@@

    /**
     * Tutti i controlli  superati.
     * PreCondition:
     * Richiesta la chiusura delle spese non reintegrate
     * PostCondition:
     * Le spese non reintegrate vengono chiuse con mandato di regolarizzazione
     * Validazione lista del fondo
     * PreCondition:
     * Si è verificato un errore
     * PostCondition:
     * Viene inviato il messaggio corrispondente all'errore segnalato.
     * Spese già tutte reintegrate
     * PreCondition:
     * Tutte le spese esistenti sono già state reintegrate
     * PostCondition:
     * Il fondo viene solamente impostato a chiuso e l'utente avvisato
     * Non esistono spese da reintegrare
     * PreCondition:
     * Non esiste alcuna spesa da reintegrare
     * PostCondition:
     * Il fondo viene solamente impostato a chiuso e l'utente avvisato
     */
//^^@@
    public Fondo_economaleBulk chiudeSpese(
            UserContext userContext,
            Fondo_economaleBulk fondo)
            throws ComponentException {

        fondo = (Fondo_economaleBulk) modificaConBulk(userContext, fondo);

        callChiudiSpese(userContext, fondo);

        return fondo;
    }

    /**
     * Set dei parametri di default in creazione di fondi e spese.
     * <p>
     * Creazione Fondo_economaleBulk:
     * importo totale spese = 0,
     * importo residuo fondo = importo ammontare fondo,
     * se importo ammontare iniziale è nullo importo ammontare iniziale = importo ammontare fondo.
     * <p>
     * Creazione Fondo_spesaBulk:
     * inizializzazione della spesa; vedi initSpesa.
     */
    public OggettoBulk creaConBulk(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

        Fondo_economaleBulk fondo = (Fondo_economaleBulk) bulk;
        fondo.setIm_totale_spese(new java.math.BigDecimal(0));
        fondo.setIm_totale_netto_spese(new java.math.BigDecimal(0));
        fondo.setIm_residuo_fondo(fondo.getIm_ammontare_fondo());
        if (fondo.getIm_ammontare_iniziale() == null)
            fondo.setIm_ammontare_iniziale(fondo.getIm_ammontare_fondo());

        fondo = (Fondo_economaleBulk) super.creaConBulk(userContext, fondo);
        if (!verificaStatoEsercizio(
                userContext,
                new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(
                        fondo.getCd_cds(),
                        ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio())))
            throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare un fondo per un esercizio non aperto!");

        return fondo;
    }

//^^@@

    /**
     * Normale.
     * PreCondition:
     * Richiesta di disassociazione di tutte le spese del fondo
     * PostCondition:
     * Scollega le spese del fondo dall'obbligazione scadenza scelta
     */
//^^@@
    public void dissociaTutteSpese(
            UserContext context,
            Fondo_economaleBulk testata,
            Obbligazione_scadenzarioBulk obbScad)
            throws it.cnr.jada.comp.ComponentException {

        LoggableStatement ps = null;
        try {
            lockBulk(context, testata);
            Fondo_spesaHome spesaHome = (Fondo_spesaHome) getHome(context, Fondo_spesaBulk.class);

            SQLBuilder sqlSpese = spesaHome.cercaSpeseAssociabili(context, testata, obbScad);
            sqlSpese.setForUpdate(true);
            ps = sqlSpese.prepareStatement(getConnection(context));
            ps.execute();
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
            ;

            SQLBuilder sql = spesaHome.rem_all_ass_speSQL(context, testata, obbScad);
            if (sql != null) {
                ps = sql.prepareStatement(getConnection(context));
                ps.execute();
            }
//			try{ps.close();}catch( java.sql.SQLException e ){};

        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        } catch (it.cnr.jada.bulk.OutdatedResourceException e) {
            throw handleException(e);
        } catch (it.cnr.jada.bulk.BusyResourceException e) {
            throw handleException(e);
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        } finally {
            if (ps != null) try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
            ;
        }
    }
//^^@@

    /**
     * tutti i controlli superati.
     * PreCondition:
     * Nessun errore rilevato.
     * PostCondition:
     * Permette la cancellazione della fattura.
     * validazione eliminazione fattura.
     * PreCondition:
     * E' stata eliminata una fattura in stato B or C
     * PostCondition:
     * Viene inviato un messaggio:"Attenzione non si può eliminare una fattura in stato IVA B o C"
     */
//^^@@
    public void eliminaConBulk(UserContext aUC, OggettoBulk bulk) throws ComponentException {

        Fondo_economaleBulk fondo = (Fondo_economaleBulk) bulk;

        if (fondo.isChiuso())
            throw new it.cnr.jada.comp.ApplicationException("Non è possibile eliminare un fondo economale chiuso!");

        if (fondo.isOnlyForClose())
            throw new it.cnr.jada.comp.ApplicationException("Non è possibile eliminare un fondo economale di un altro esercizio!");

        try {
            if (hasSpese(aUC, fondo))
                throw new it.cnr.jada.comp.ApplicationException("Non è possibile eliminare un fondo economale per il quale sono state registrate delle spese!");
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw handleException(fondo, e);
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(fondo, e);
        }

        super.eliminaConBulk(aUC, fondo);

        if (!verificaStatoEsercizio(
                aUC,
                new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(
                        fondo.getCd_cds(),
                        ((it.cnr.contab.utenze00.bp.CNRUserContext) aUC).getEsercizio())))
            throw new it.cnr.jada.comp.ApplicationException("Impossibile eliminare un fondo per un esercizio non aperto!");
    }
//^^@@

    /**
     * Normale.
     * PreCondition:
     * Richiesta di caricamento dettagli di una fattura passiva, nota di credito, nota di debito
     * PostCondition:
     * Restituisce la lista dei dettagli
     */
//^^@@
    public java.util.List findDettagli(UserContext aUC, Fondo_economaleBulk fondo)
            throws ComponentException,
            it.cnr.jada.persistency.PersistencyException,
            it.cnr.jada.persistency.IntrospectionException {

        if (fondo == null) return null;

        it.cnr.jada.bulk.BulkHome home = getHome(aUC, Ass_fondo_eco_mandatoBulk.class);

        it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
        sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, fondo.getCd_unita_organizzativa());
        sql.addClause("AND", "cd_cds", sql.EQUALS, fondo.getCd_cds());
        sql.addClause("AND", "esercizio", sql.EQUALS, fondo.getEsercizio());
        sql.addClause("AND", "cd_codice_fondo", sql.EQUALS, fondo.getCd_codice_fondo());
        return home.fetchAll(sql);
    }
//^^@@

    /**
     * Normale.
     * PreCondition:
     * Nessun errore segnalato.
     * PostCondition:
     * Viene restituita la lista delle banche dell'economo.
     */
//^^@@
    public java.util.Collection findListabanche(UserContext aUC, Fondo_economaleBulk fondo) throws ComponentException, it.cnr.jada.persistency.PersistencyException {

        if (fondo.getEconomo() == null) return null;

        return getHome(aUC, BancaBulk.class).fetchAll(selectBancaByClause(aUC, fondo, null, null));
    }
//^^@@

    /**
     * Tutti i controlli  superati.
     * PreCondition:
     * Richiesta ricerca delle modalità di pagamento dell'economo
     * PostCondition:
     * Restituisce la collezione di modalità di pagamento dell'economo
     * Validazione dell'economo
     * PreCondition:
     * Si è verificato un errore nel caricamento delle modalità di pagamento dell'economo.
     * PostCondition:
     * Viene inviato il messaggio corrispondente all'errore segnalato.
     */
//^^@@
    public java.util.Collection findModalita(UserContext aUC, Fondo_economaleBulk fondo) throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
        if (fondo.getEconomo() == null) return null;
        return ((TerzoHome) getHome(aUC, TerzoBulk.class)).findRif_modalita_pagamento(fondo.getEconomo());
    }

    /**
     * Validazione dell'oggetto in fase di stampa
     */
    private java.sql.Timestamp getDataOdierna(it.cnr.jada.UserContext userContext) throws ComponentException {

        try {
            Fondo_economaleHome home = (Fondo_economaleHome) getHome(userContext, Fondo_economaleBulk.class);
            return home.getServerDate();
        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(ex);
        }
    }

    private java.sql.Timestamp getFirstDayOfYear(int year) {

        java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
        calendar.set(java.util.Calendar.MONTH, 0);
        calendar.set(java.util.Calendar.YEAR, year);
        calendar.set(java.util.Calendar.HOUR, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        calendar.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
        return new java.sql.Timestamp(calendar.getTime().getTime());
    }

    private java.sql.Timestamp getLastDayOfYear(int year) {

        java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 31);
        calendar.set(java.util.Calendar.MONTH, 11);
        calendar.set(java.util.Calendar.YEAR, year);
        calendar.set(java.util.Calendar.HOUR, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        calendar.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
        return new java.sql.Timestamp(calendar.getTime().getTime());
    }
//^^@@

    /**
     * Normale.
     * PreCondition:
     * Richiesta di caricamento dettagli di una fattura passiva, nota di credito, nota di debito
     * PostCondition:
     * Restituisce la lista dei dettagli
     */
//^^@@
    public boolean hasSpese(UserContext aUC, Fondo_economaleBulk fondo)
            throws ComponentException,
            it.cnr.jada.persistency.PersistencyException,
            it.cnr.jada.persistency.IntrospectionException {

        if (fondo == null) return false;

        return calcolaTotaleSpese(aUC, fondo, null).compareTo(new java.math.BigDecimal(0)) != 0;
    }

    /**
     * Inizializza l'unità organizzativa del Fondo_economaleBulk all'unità organizzativa corrente.
     */
    public void initializeKeysAndOptionsInto(UserContext aUC, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        super.initializeKeysAndOptionsInto(aUC, bulk);

        if (bulk instanceof Fondo_economaleBulk) {
            try {
                ((Fondo_economaleBulk) bulk).setUnita_organizzativa(
                        (Unita_organizzativaBulk) getHome(
                                aUC,
                                Unita_organizzativaBulk.class
                        ).findByPrimaryKey(
                                new Unita_organizzativaBulk(
                                        CNRUserContext.getCd_unita_organizzativa(aUC)
                                )
                        )
                );
                ((Fondo_economaleBulk) bulk).setCds(
                        (CdsBulk) getHome(
                                aUC,
                                CdsBulk.class
                        ).findByPrimaryKey(
                                new CdsBulk(
                                        CNRUserContext.getCd_cds(aUC)
                                )
                        )
                );

                ((Fondo_economaleBulk) bulk).setEsercizio(
                        CNRUserContext.getEsercizio(aUC)
                );

            } catch (it.cnr.jada.persistency.PersistencyException e) {
                throw new ComponentException(e);
            }
        }
    }

    /**
     * Inizializza il fleg Aperto del Fondo_economaleBulk a vero.
     */
    public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        bulk = super.inizializzaBulkPerInserimento(userContext, bulk);

        Fondo_economaleBulk fondo = (Fondo_economaleBulk) bulk;
        try {
            Fondo_economaleHome fHome = (Fondo_economaleHome) getHome(userContext, bulk);
            if (!fHome.verificaStatoEsercizio(fondo))
                throw new it.cnr.jada.comp.ApplicationException("Impossibile inserire una fattura passiva per un esercizio non aperto!");
            java.sql.Timestamp date = fHome.getServerDate();
            int annoSolare = Fondo_spesaBulk.getDateCalendar(date).get(java.util.Calendar.YEAR);
            if (annoSolare != it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue())
                throw new it.cnr.jada.comp.ApplicationException("Non è possibile inserire un fondo economale in esercizi non corrispondenti all'anno solare!");
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(fondo, e);
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw handleException(fondo, e);
        }


        fondo.setFl_aperto(Boolean.TRUE);
        fondo.setFl_rev_da_emettere(Boolean.TRUE);

        TerzoBulk tb = new TerzoBulk();
        tb.setAnagrafico(new AnagraficoBulk());
        fondo.setEconomo(tb);

        MandatoIBulk mandato = new MandatoIBulk();
        mandato.setIm_mandato(null);
        mandato.setIm_pagato(null);
        mandato.setIm_ritenute(null);
        fondo.setMandato(mandato);

        fondo.resetImporti();

        fondo.setIm_max_gg_spesa_doc(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        fondo.setIm_max_gg_spesa_non_doc(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        fondo.setIm_max_mm_spesa_doc(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        fondo.setIm_max_mm_spesa_non_doc(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));

        return bulk;
    }

//^^@@

    /**
     * Oggetto non esistente
     * PreCondition:
     * L'OggettoBulk specificato non esiste.
     * PostCondition:
     * Viene generata una CRUDException con la descrizione dell'errore.
     */
//^^@@
    public OggettoBulk inizializzaBulkPerModifica(UserContext aUC, OggettoBulk bulk) throws ComponentException {

        if (bulk == null)
            throw new ComponentException("Attenzione: non esiste alcun fondo corrispondente ai criteri di ricerca!");

        Fondo_economaleBulk fondo = (Fondo_economaleBulk) bulk;
        if (fondo.getEsercizio() == null)
            throw new it.cnr.jada.comp.ApplicationException("L'esercizio del fondo economale non è valorizzato! Impossibile proseguire.");

        if (fondo.getEsercizio().intValue() !=
                it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC).intValue())
            throw new it.cnr.jada.comp.ApplicationException("Il fondo economale non appartiene all'esercizio di scrivania! Operazione annullata!");

        fondo = (Fondo_economaleBulk) super.inizializzaBulkPerModifica(aUC, fondo);

        try {
            BulkList dettagli = new BulkList(findDettagli(aUC, fondo));
            fondo.setAssociazioni_mandati(dettagli);
            getHomeCache(aUC).fetchAll(aUC);
            fondo.setOnlyForClose(
                    ((CNRUserContext.getEsercizio(aUC).intValue() !=
                            fondo.getEsercizio().intValue()) ||
                            (Fondo_spesaBulk.getDateCalendar(null).get(Calendar.YEAR) !=
                                    fondo.getEsercizio().intValue())));
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(fondo, e);
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            throw handleException(fondo, e);
        }

        return fondo;
    }

    /**
     * Inizializza il fleg Aperto del Fondo_economaleBulk a vero.
     */
    public OggettoBulk inizializzaBulkPerRicerca(UserContext aUC, OggettoBulk bulk) throws ComponentException {

        Fondo_economaleBulk fondo = (Fondo_economaleBulk) super.inizializzaBulkPerRicerca(aUC, bulk);

        fondo.setFl_aperto(Boolean.TRUE);
        TerzoBulk tb = new TerzoBulk();
        tb.setAnagrafico(new AnagraficoBulk());
        fondo.setEconomo(tb);
        fondo.setMandato(new MandatoIBulk());

        return fondo;
    }

//^^@@

    /**
     * Oggetto non esistente
     * PreCondition:
     * L'OggettoBulk specificato non esiste.
     * PostCondition:
     * Viene generata una CRUDException con la descrizione dell'errore.
     */
//^^@@
    public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext userContext, OggettoBulk bulk) throws ComponentException {

        if (bulk == null)
            throw new ComponentException("Attenzione: non esiste alcun fondo economale corrispondente ai criteri di ricerca!");

        Fondo_economaleBulk fondo = (Fondo_economaleBulk) super.inizializzaBulkPerRicercaLibera(userContext, bulk);

        TerzoBulk economo = new TerzoBulk();
        economo.setAnagrafico(new AnagraficoBulk());
        fondo.setEconomo(economo);

        fondo.setMandato(new MandatoIBulk());

        //fondo.setUnita_organizzativa(null);
        return fondo;
    }

    /**
     * inizializzaBulkPerStampa method comment.
     */
    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

        Stampa_vpg_fondo_economaleBulk stampa = (Stampa_vpg_fondo_economaleBulk) bulk;

        //stampa.setCd_cds(CNRUserContext.getCd_cds(aUC));
        //stampa.setCd_unita_organizzativa(CNRUserContext.getCd_unita_organizzativa(aUC));

        TerzoBulk economo = new TerzoBulk();
        economo.setAnagrafico(new AnagraficoBulk());
        stampa.setEconomo(economo);
        //stampa.setMandato(new it.cnr.contab.doccont00.core.bulk.MandatoIBulk());

        try {
            stampa.setUnita_organizzativa(
                    (Unita_organizzativaBulk) getHome(
                            aUC,
                            Unita_organizzativaBulk.class
                    ).findByPrimaryKey(
                            new Unita_organizzativaBulk(
                                    CNRUserContext.getCd_unita_organizzativa(aUC)
                            )
                    )
            );

            stampa.setEsercizio(
                    CNRUserContext.getEsercizio(aUC)
            );

        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw new ComponentException(e);
        }

        stampa.setFondoForPrint(new Fondo_economaleBulk());
        stampa.setDataInizio(getFirstDayOfYear(CNRUserContext.getEsercizio(aUC).intValue()));

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(getDataOdierna(aUC));

        //if (CNRUserContext.getEsercizio(aUC).intValue() != cal.get(java.util.Calendar.YEAR)){
        //stampa.setDataFine(getLastDayOfYear(CNRUserContext.getEsercizio(aUC).intValue()));
        //} else {
        //stampa.setDataFine(getDataOdierna(aUC));
        //}

        stampa.setDataFine(getDataOdierna(aUC));

        return stampa;
    }

    /**
     * Set dei parametri di default in modifica di fondi e spese.
     * <p>
     * Creazione Fondo_economaleBulk:
     * importo residuo fondo = importo ammontare fondo - importo totale spese.
     * <p>
     * Creazione Fondo_spesaBulk:
     * inizializzazione della spesa; vedi initSpesa.
     */
    public OggettoBulk modificaConBulk(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

        Fondo_economaleBulk fondo = (Fondo_economaleBulk) bulk;
        if (fondo.getIm_ammontare_fondo() != null)
            fondo.setIm_residuo_fondo(fondo.getIm_ammontare_fondo().subtract(fondo.getIm_totale_spese()));

        fondo = (Fondo_economaleBulk) super.modificaConBulk(userContext, fondo);
        if (!verificaStatoEsercizio(
                userContext,
                new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk(
                        fondo.getCd_cds(),
                        ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio())))
            throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare un fondo per un esercizio non aperto!");

        return fondo;
    }

    /**
     * Modifica associazione spese.
     * <p>
     * Nome: Allineamento con selezione utente;
     * Pre:  Allinea la situazione reale dei record con selezione utente;
     * Post: Alza o abbassa il buleano che indica l'associazione o meno all'obbligazione.
     *
     * @param spese     Elenco delle spese prese in esame.
     * @param associati Elenco delle selezioni operate dall'utente.
     */

    public Fondo_spesaBulk[] modificaSpe_associate(UserContext userContext, Fondo_spesaBulk[] spese, boolean[] associati) throws ComponentException {
        try {
            for (int i = 0; i < spese.length; i++) {
                Fondo_spesaBulk spesa = spese[i];
                boolean associato = associati[i];
                if (spesa.getFl_obbligazione() != null && spesa.getFl_obbligazione().booleanValue() != associato) {
                    spesa.setFl_obbligazione(new Boolean(associato));
                    if (!associato)
                        spesa.setObb_scad(null);
                    updateBulk(userContext, spesa);
                }
            }
            return spese;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }
    }

//^^@@

    /**
     * Tutti i controlli  superati.
     * PreCondition:
     * Richiesta di reintegro delle spese del fondo economale
     * PostCondition:
     * Le spese selezionate dall'utente vengono reintegrate
     * Spesa reintegrata
     * PreCondition:
     * La spesa è reintegrata
     * PostCondition:
     * La spesa non viene modificata.
     * Spesa non documentata
     * PreCondition:
     * La spesa non è stata associata a scadenza obbligazione
     * PostCondition:
     * La spesa non viene modificata.
     * Quadratura
     * PreCondition:
     * La somma delle spese non documentate associate alla stessa scadenza
     * obbligazione non è in quadratura con l'importo scadenza stesso
     * PostCondition:
     * L'operazione viene interrotta con relativo messaggio
     */
//^^@@
    public Fondo_economaleBulk reintegraSpese(
            UserContext userContext,
            Fondo_economaleBulk fondo,
            java.util.List speseSelezionate)
            throws it.cnr.jada.comp.ComponentException {

        if (fondo != null && speseSelezionate != null && !speseSelezionate.isEmpty()) {
            java.util.Vector visteAggiunte = new java.util.Vector();
            Vsx_reintegro_fondoHome home = (Vsx_reintegro_fondoHome) getHome(userContext, Vsx_reintegro_fondoBulk.class);
            try {
                try {
                    int count = 0;
                    Long pg = callGetPgPerReintegroSpese(userContext);
                    for (java.util.Iterator i = speseSelezionate.iterator(); i.hasNext(); ) {
                        Fondo_spesaBulk spesa = (Fondo_spesaBulk) i.next();
                        spesa.setUser(userContext.getUser());
                        Vsx_reintegro_fondoBulk vista = new Vsx_reintegro_fondoBulk();
                        vista.setPg_call(pg);
                        vista.completeFrom(spesa);
                        vista.setPar_num(new Integer(count++));
                        vista.setToBeCreated();
                        home.insert(vista, userContext);
                        visteAggiunte.add(vista);
                    }

                    callReintegroSpese(userContext, pg);

                } catch (it.cnr.jada.persistency.PersistencyException e) {
                    throw e;
                } finally {
                    for (java.util.Iterator i = visteAggiunte.iterator(); i.hasNext(); ) {
                        Vsx_reintegro_fondoBulk vistaInserita = (Vsx_reintegro_fondoBulk) i.next();
                        vistaInserita.setToBeDeleted();
                        home.delete(vistaInserita, userContext);
                    }
                }

                fondo = (Fondo_economaleBulk) getHome(userContext, fondo).findByPrimaryKey(fondo);

            } catch (it.cnr.jada.persistency.PersistencyException e) {
                throw handleException(fondo, e);
            } catch (Throwable e) {
                throw handleException(fondo, e);
            }
        }

        return fondo;
    }

    public it.cnr.jada.persistency.sql.SQLBuilder selectBancaByClause(
            UserContext aUC,
            Fondo_economaleBulk fondo,
            BancaBulk banca,
            CompoundFindClause clauses)
            throws ComponentException {

        BancaHome bancaHome = (BancaHome) getHome(aUC, it.cnr.contab.anagraf00.core.bulk.BancaBulk.class);
        return bancaHome.selectBancaFor(
                fondo.getModalita_pagamento(),
                fondo.getCd_terzo());
    }

    public SQLBuilder selectEconomoByClause(
            UserContext aUC,
            Fondo_economaleBulk fondo,
            TerzoBulk economo,
            CompoundFindClause clauses)
            throws ComponentException {

        SQLBuilder sql = getHome(aUC, economo).createSQLBuilder();
        sql.addSQLClause("AND", "TERZO.CD_TERZO", sql.EQUALS, economo.getCd_terzo());
        sql.addSQLClause("AND", "TERZO.CD_PRECEDENTE", sql.EQUALS, economo.getCd_precedente());
        sql.openParenthesis("AND");
        sql.addSQLClause("OR", "TERZO.DT_FINE_RAPPORTO", sql.ISNULL, null);
        sql.addSQLClause("OR", "TERZO.DT_FINE_RAPPORTO", sql.GREATER_EQUALS, new java.sql.Timestamp(Fondo_spesaBulk.getDateCalendar(null).getTime().getTime()));
        sql.closeParenthesis();

//	Probabilmente non servono
//	sql.addSQLClause("AND","TERZO.TI_TERZO",sql.NOT_EQUALS,TerzoBulk.DEBITORE);
//	sql.addSQLClause("AND","ANAGRAFICO.TI_ENTITA",sql.NOT_EQUALS,"D");
//	sql.addSQLClause("AND","ANAGRAFICO.TI_ITALIANO_ESTERO",sql.EQUALS,fatturaPassiva.getSupplierNationType());

        if (economo != null && economo.getAnagrafico() != null) {
            sql.addTableToHeader("ANAGRAFICO");
            sql.addSQLJoin("TERZO.CD_ANAG", "ANAGRAFICO.CD_ANAG");
            sql.addSQLClause("AND", "ANAGRAFICO.CODICE_FISCALE", sql.CONTAINS, economo.getAnagrafico().getCodice_fiscale());
            sql.addSQLClause("AND", "ANAGRAFICO.PARTITA_IVA", sql.CONTAINS, economo.getAnagrafico().getPartita_iva());
        }

        sql.addClause(clauses);
        return sql;
    }

    /**
     * Ricerca di un Fondo Economale per la Procedura di Stampa.
     * PreCondition:
     * E' stata generata la richiesta di ricerca di un Fondo Economale
     * per la Procedura di Stampa.
     * PostCondition:
     * Viene restituito il SQLBuilder con l'elenco delle clausole selezionate dall'utente e, in aggiunta, le
     * clausole che il Fondo Economale appartenga al CdR/Esercizio di scrivania
     *
     * @param userContext     lo <code>UserContext</code> che ha generato la richiesta
     * @param utilizzatori_la il <code>Inventario_utilizzatori_laBulk</code> CdR di riferimento
     * @param l_att           la <code>Linea_attivitaBulk</code> Linea di Attività modello
     * @param clauses         <code>CompoundFindClause</code> le clausole della selezione
     * @return sql <code>SQLBuilder</code> Risultato della selezione.
     **/
    public SQLBuilder selectFondoForPrintByClause(UserContext userContext, Stampa_vpg_fondo_economaleBulk stampa, Fondo_economaleBulk fondo, CompoundFindClause clauses)
            throws ComponentException {

        SQLBuilder sql = getHome(userContext, Fondo_economaleBulk.class).createSQLBuilder();
        sql.addClause(clauses);

        sql.addSQLClause("AND", "CD_CDS", sql.EQUALS, CNRUserContext.getCd_cds(userContext));
        sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));

        return sql;
    }

    public it.cnr.jada.persistency.sql.SQLBuilder selectFornitoreByClause(
            UserContext aUC,
            Filtro_ricerca_obbligazioniVBulk filtro,
            TerzoBulk fornitore,
            CompoundFindClause clauses)
            throws ComponentException {

        it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC, fornitore, "V_TERZO_CF_PI").createSQLBuilder();
        sql.addTableToHeader("ANAGRAFICO");
        sql.addSQLJoin("ANAGRAFICO.CD_ANAG", "V_TERZO_CF_PI.CD_ANAG");
        sql.addSQLClause("AND", "V_TERZO_CF_PI.CD_TERZO", sql.EQUALS, fornitore.getCd_terzo());
        sql.addSQLClause("AND", "V_TERZO_CF_PI.DENOMINAZIONE_SEDE", sql.LIKE, fornitore.getDenominazione_sede());

        sql.addSQLClause("AND", "((V_TERZO_CF_PI.DT_FINE_RAPPORTO IS NULL) OR (V_TERZO_CF_PI.DT_FINE_RAPPORTO >= ?))");
        sql.addParameter(filtro.getCurrentDate(), java.sql.Types.TIMESTAMP, 0);

        sql.addSQLClause("AND", "V_TERZO_CF_PI.TI_TERZO", sql.NOT_EQUALS, TerzoBulk.DEBITORE);
        sql.addSQLClause("AND", "ANAGRAFICO.TI_ENTITA", sql.NOT_EQUALS, "D");

        sql.addClause(clauses);
        return sql;
    }

    public SQLBuilder selectMandatiPerIntegrazione(
            UserContext aUC,
            Fondo_economaleBulk fondo)
            throws ComponentException {

        if (fondo.getEconomo() == null || fondo.getEconomo().getCrudStatus() != OggettoBulk.NORMAL ||
                fondo.getMandato() == null || fondo.getMandato().getCrudStatus() != OggettoBulk.NORMAL)
            throw new it.cnr.jada.comp.ApplicationException("Prima di cercare il mandato specificare l'economo e il mandato di origine!");

        return selectMandatoByClause(aUC, fondo, null, null);
    }

    public SQLBuilder selectMandatoByClause(
            UserContext aUC,
            Fondo_economaleBulk fondo,
            MandatoIBulk mandato,
            CompoundFindClause clauses)
            throws ComponentException {

        SQLBuilder sql = getHome(aUC, MandatoIBulk.class, "V_ASS_MANDATO_FONDO_ECO").createSQLBuilder();

        if (fondo.getEconomo() != null)
            sql.addSQLClause("AND", "V_ASS_MANDATO_FONDO_ECO.CD_TERZO", sql.EQUALS, fondo.getEconomo().getCd_terzo());

        //il mandato deve appartenere al mio CDS e esercizio
        sql.addSQLClause("AND", "V_ASS_MANDATO_FONDO_ECO.CD_CDS", sql.EQUALS, fondo.getCd_cds());
        sql.addSQLClause("AND", "V_ASS_MANDATO_FONDO_ECO.ESERCIZIO", sql.EQUALS, fondo.getEsercizio());
        //non deve essere annullato
        sql.addSQLClause("AND", "V_ASS_MANDATO_FONDO_ECO.STATO", sql.NOT_EQUALS, MandatoIBulk.STATO_MANDATO_ANNULLATO);
        //non deve essere associato ad altri fondi economali
        sql.addSQLClause("AND", "V_ASS_MANDATO_FONDO_ECO.CD_CODICE_FONDO", sql.ISNULL, null);
        //deve avere un importo pagato diverso da 0
        sql.openParenthesis("AND");
        sql.addSQLClause("OR", "V_ASS_MANDATO_FONDO_ECO.IM_PAGATO", sql.NOT_EQUALS, new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
        sql.addSQLClause("AND", "V_ASS_MANDATO_FONDO_ECO.IM_PAGATO", sql.ISNOTNULL, null);
        sql.addSQLClause("AND", "V_ASS_MANDATO_FONDO_ECO.TI_APERTURA_INCREMENTO", sql.ISNULL, null);
        sql.closeParenthesis();
        //altre clausole
        sql.addClause(clauses);
        return sql;
    }

    public SQLBuilder selectScadenza_ricerca_obbligazione_creditoreByClause(
            UserContext aUC,
            Fondo_economaleBulk fondo,
            TerzoBulk creditore,
            CompoundFindClause clauses)
            throws ComponentException {

        return selectEconomoByClause(
                aUC,
                fondo,
                creditore,
                clauses);
    }

    public SQLBuilder selectScadenza_ricercaByClause(
            UserContext aUC,
            Fondo_economaleBulk fondo,
            Obbligazione_scadenzarioBulk scadenza,
            CompoundFindClause clauses)
            throws ComponentException {

        SQLBuilder sql = getHome(aUC, scadenza).createSQLBuilder();
        sql.addTableToHeader("OBBLIGAZIONE");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.CD_CDS", "OBBLIGAZIONE.CD_CDS");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.ESERCIZIO", "OBBLIGAZIONE.ESERCIZIO");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.ESERCIZIO_ORIGINALE", "OBBLIGAZIONE.ESERCIZIO_ORIGINALE");
        sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE", "OBBLIGAZIONE.PG_OBBLIGAZIONE");

        //Le obbligazioni valide hanno una sola scadenza
        //sql.addSQLClause(
        //"AND",
        //"( SELECT COUNT(*)"
        //+" FROM "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()+"OBBLIGAZIONE_SCADENZARIO OBBSCA1"
        //+" WHERE OBBSCA1.ESERCIZIO = OBBLIGAZIONE_SCADENZARIO.ESERCIZIO"
        //+" AND OBBSCA1.CD_CDS = OBBLIGAZIONE_SCADENZARIO.CD_CDS"
        //+" AND OBBSCA1.ESERCIZIO_ORIGINALE = OBBLIGAZIONE_SCADENZARIO.ESERCIZIO_ORIGINALE"
        //+" AND OBBSCA1.PG_OBBLIGAZIONE = OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE"
        //+" GROUP BY OBBSCA1.ESERCIZIO, OBBSCA1.CD_CDS, OBBSCA1.ESERCIZIO_ORIGINALE, OBBSCA1.PG_OBBLIGAZIONE"
        //+" ) = 1"
        //);

        sql.addSQLClause("AND", "OBBLIGAZIONE.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, fondo.getCd_unita_organizzativa());
        sql.addSQLClause("AND", "OBBLIGAZIONE.ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));
        sql.addSQLClause("AND", "OBBLIGAZIONE.STATO_OBBLIGAZIONE", sql.EQUALS, ObbligazioneBulk.STATO_OBB_DEFINITIVO);
        sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TIPO_DOCUMENTO_CONT", sql.EQUALS, scadenza.getObbligazione().getCd_tipo_documento_cont());
        sql.addSQLClause("AND", "OBBLIGAZIONE.DT_CANCELLAZIONE", sql.ISNULL, null);
        sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_SCADENZA", sql.NOT_EQUALS, new java.math.BigDecimal(0));
        sql.addSQLClause("AND", "OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_AMM <> ? OR OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_AMM IS NOT NULL");
        sql.addParameter(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP), java.sql.Types.DECIMAL, 2);

        if (scadenza.getObbligazione().getCreditore() != null)
            sql.addSQLClause("AND", "OBBLIGAZIONE.CD_TERZO", sql.EQUALS, scadenza.getObbligazione().getCreditore().getCd_terzo());

        sql.addClause(clauses);
        return sql;
    }

    /**
     * stampaConBulk method comment.
     */
    public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {


        validateBulkForPrint(aUC, (Stampa_vpg_fondo_economaleBulk) bulk);

        return bulk;
    }

    private void updateScadenzaWith(UserContext context, Fondo_economaleBulk testata, Obbligazione_scadenzarioBulk obbscad) throws it.cnr.jada.comp.ComponentException {

        try {
            testata.setFl_documentata_for_search(Boolean.FALSE);
            testata.setFl_reintegrata_for_search(Boolean.FALSE);

            java.math.BigDecimal im_ass_spese = calcolaTotaleSpese(
                    context,
                    testata,
                    obbscad);
            Obbligazione_scadenzarioHome home = (Obbligazione_scadenzarioHome) getHome(context, obbscad);
            obbscad.setIm_associato_doc_amm(im_ass_spese);
            home.aggiornaImportoAssociatoADocAmm(context, obbscad);
        } catch (Throwable e) {
            throw handleException(e);
        } finally {
            testata.setFl_documentata_for_search(null);
            testata.setFl_reintegrata_for_search(null);
        }
    }

    /**
     * Validazione dell'oggetto in fase di stampa
     * Controlla i dati inseriti dall'utente relativi alle date di INIZIO e FINE
     * periodo.
     * Innanzitutto, controlla che entrambe le date siano state inserite; poi, verifica che la DATA
     * INIZIO VALIDITA' sia anteriore alla DATA FINE VALIDITA'; infine, controlla che l'esercizio
     * indicato nelle due date sia lo stesso.
     */
    private void validateBulkForPrint(it.cnr.jada.UserContext userContext, Stampa_vpg_fondo_economaleBulk stampa) throws ComponentException {

        try {
            java.sql.Timestamp dataOdierna = getDataOdierna(userContext);
            java.sql.Timestamp lastDayOfYear = DateServices.getLastDayOfYear(stampa.getEsercizio().intValue());

            if (stampa.getFondoForPrint() == null || stampa.getFondoForPrint().getCd_codice_fondo() == null) {
                throw new ValidationException("Attenzione: indicare un Fondo Economale.");
            }

            if (stampa.getDataInizio() == null)
                throw new ValidationException("Attenzione: il campo DATA INIZIO è obbligatorio");
            if (stampa.getDataFine() == null)
                throw new ValidationException("Attenzione: il campo DATA FINE è obbligatorio");

            java.sql.Timestamp firstDayOfYear = getFirstDayOfYear(stampa.getEsercizio().intValue());
            if (stampa.getDataInizio().compareTo(stampa.getDataFine()) > 0)
                throw new ValidationException("Attenzione: la DATA INIZIO non può essere superiore alla DATA FINE");
            if (stampa.getDataInizio().compareTo(firstDayOfYear) < 0) {
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
                throw new ValidationException("Attenzione: la DATA INIZIO non può essere inferiore a " + formatter.format(firstDayOfYear));
            }
            if (stampa.getDataFine().compareTo(lastDayOfYear) > 0) {
                java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
                throw new ValidationException("Attenzione: la DATA di FINE PERIODO non può essere superiore a " + formatter.format(lastDayOfYear));
            }

            //Calendar cal_da = Calendar.getInstance();
            //cal_da.setTime(stampa.getDataInizio());

            //Calendar cal_a = Calendar.getInstance();
            //cal_a.setTime(stampa.getDataFine());

            //if (cal_da.get(Calendar.YEAR) != cal_a.get(Calendar.YEAR)){
            //throw new ValidationException("Attenzione: l'esercizio di DATA INZIZIO e DATA FINE deve essere uguale.");
            //}


        } catch (ValidationException ex) {
            throw new ApplicationException(ex);
        }
    }

    private boolean verificaStatoEsercizio(
            UserContext userContext,
            EsercizioBulk anEsercizio)
            throws ComponentException {

        try {
            it.cnr.contab.config00.esercizio.bulk.EsercizioHome eHome = (it.cnr.contab.config00.esercizio.bulk.EsercizioHome) getHome(userContext, EsercizioBulk.class);
            return !eHome.isEsercizioChiuso(
                    userContext,
                    anEsercizio.getEsercizio(),
                    anEsercizio.getCd_cds());
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            throw handleException(e);
        }

    }
}
