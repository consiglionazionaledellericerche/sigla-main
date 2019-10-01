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

package it.cnr.contab.progettiric00.comp;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.pdg00.bulk.Pdg_preventivo_etr_detBulk;
import it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk;
import it.cnr.contab.progettiric00.core.bulk.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class ProgettoRicercaModuloComponent extends it.cnr.jada.comp.CRUDComponent implements IProgettoRicercaMgr {
    public static final String TIPO_PROGETTO = "M";

    /**
     * ProgettoRicercaComponent constructor comment.
     */
    public ProgettoRicercaModuloComponent() {
        super();
    }

    /**
     * Pre:  Controllo Dt_inizio > Dt_fine
     * Post: Segnalazione "Data di fine deve essere maggiore della data di inizio!"
     * <p>
     * Pre:  Controllo se Dt_fine = null e Dt_proroga != null
     * Post: Segnalazione "Non può esistere una data di proroga se non si indica una data di fine!"
     * <p>
     * Pre:  Controllo Dt_fine > Dt_proroga
     * Post: Segnalazione "Data di proroga deve essere maggiore della data di fine!"
     * <p>
     * Pre:  Controllo se la lista dei dettagli è vuota
     * Post: Se vuota viene creato un unico dettaglio che ha:
     * UO = l'UO coordinatrice del Progetto
     * Responsabile = Responsabile del Progetto
     * Importo = Importo del Progetto
     * <p>
     * Pre:  Controllo somma importo dettagli != da importo del Progetto
     * Post: Segnalazione "La somma degli importi degli assegnatari è diversa dall'importo del Progetto"
     */
    public OggettoBulk creaConBulk(UserContext uc, OggettoBulk bulk) throws ComponentException {
        intBulk(uc, (ProgettoBulk) bulk);

        //Parametri_cdsBulk param = parametriCds(uc, (ProgettoBulk)bulk);
        // inserimento automatico del codice
        if (((ProgettoBulk) bulk).getParametriCds().getFl_progetto_numeratore().booleanValue())
            ((ProgettoBulk) bulk).setCd_progetto(creaCodiceProgetto(uc, (ProgettoBulk) bulk));

        java.math.BigDecimal sq_progetto;
        sq_progetto = getSequence(uc);
        ((ProgettoBulk) bulk).setPg_progetto(sq_progetto);
        ((Progetto_uoBulk) ((ProgettoBulk) bulk).getDettagli().get(0)).setPg_progetto(new Integer(sq_progetto.intValue()));
        for (int i = 0; ((ProgettoBulk) bulk).getDettagliFinanziatori().size() > i; i++) {
            ((Progetto_finanziatoreBulk) ((ProgettoBulk) bulk).getDettagliFinanziatori().get(i)).setPg_progetto(new Integer(sq_progetto.intValue()));
        }
        for (int i = 0; ((ProgettoBulk) bulk).getDettagliPartner_esterni().size() > i; i++) {
            ((Progetto_partner_esternoBulk) ((ProgettoBulk) bulk).getDettagliPartner_esterni().get(i)).setPg_progetto(new Integer(sq_progetto.intValue()));
        }
        for (int i = 0; ((ProgettoBulk) bulk).getDettagli().size() > i; i++) {
            ((Progetto_uoBulk) ((ProgettoBulk) bulk).getDettagli().get(i)).setPg_progetto(new Integer(sq_progetto.intValue()));
        }
        return super.creaConBulk(uc, bulk);
    }

    public void eliminaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
          
		  /*Se sto cancellando il progetto cancello anche tutti i dettagli */
        if (bulk instanceof ProgettoBulk) {
            for (int i = 0; ((ProgettoBulk) bulk).getDettagli().size() > i; i++) {
                ((Progetto_uoBulk) ((ProgettoBulk) bulk).getDettagli().get(i)).setCrudStatus(bulk.TO_BE_DELETED);
            }
            for (int i = 0; ((ProgettoBulk) bulk).getDettagliFinanziatori().size() > i; i++) {
                ((Progetto_finanziatoreBulk) ((ProgettoBulk) bulk).getDettagliFinanziatori().get(i)).setCrudStatus(bulk.TO_BE_DELETED);
            }
            for (int i = 0; ((ProgettoBulk) bulk).getDettagliPartner_esterni().size() > i; i++) {
                ((Progetto_partner_esternoBulk) ((ProgettoBulk) bulk).getDettagliPartner_esterni().get(i)).setCrudStatus(bulk.TO_BE_DELETED);
            }
        }
        super.eliminaConBulk(aUC, bulk);
    }

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        // inizializzazione per gestire la codifica automatica
        ((ProgettoBulk) bulk).setParametriCds(parametriCds(aUC, (ProgettoBulk) bulk));
        return super.inizializzaBulkPerInserimento(aUC, bulk);
    }

    /**
     * Pre:  Preparare l'oggetto alle modifiche;
     * Post: carica la lista di dettagli associati a un Progetto
     */
    public OggettoBulk inizializzaBulkPerModifica(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            ProgettoBulk testata = (ProgettoBulk) super.inizializzaBulkPerModifica(userContext, bulk);
            ProgettoHome testataHome = (ProgettoHome) getHome(userContext, ProgettoBulk.class);
            testata.setDettagli(new it.cnr.jada.bulk.BulkList(testataHome.findDettagli(testata)));
            testata.setDettagliFinanziatori(new it.cnr.jada.bulk.BulkList(testataHome.findDettagliFinanziatori(testata)));
            testata.setDettagliPartner_esterni(new it.cnr.jada.bulk.BulkList(testataHome.findDettagliPartner_esterni(testata)));
            testata.setSpeseEsercizio(new it.cnr.jada.bulk.BulkList(testataHome.findDettagliSpese(userContext, testata)));

            getHomeCache(userContext).fetchAll(userContext);

            // controllo per evitare che il progetto padre sia modificabile nel caso
            // in cui tale progetto sia stato inserito nel piano di gestione preventivo
            if (!isCommessaModificabile(userContext, testata))
                testata.getProgettopadre().setOperabile(false);

            return testata;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    private boolean isCommessaModificabile(it.cnr.jada.UserContext userContext, ProgettoBulk testata) throws it.cnr.jada.comp.ComponentException {
        try {
            if (testata.getProgettopadre().getPg_progetto() == null)
                return true;
            SQLBuilder sql_exists = getHome(userContext, Pdg_preventivo_etr_detBulk.class).createSQLBuilder();
            sql_exists.addTableToHeader("LINEA_ATTIVITA");
            sql_exists.addSQLJoin("PDG_PREVENTIVO_ETR_DET.CD_CENTRO_RESPONSABILITA", "LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA");
            sql_exists.addSQLJoin("PDG_PREVENTIVO_ETR_DET.CD_LINEA_ATTIVITA", "LINEA_ATTIVITA.CD_LINEA_ATTIVITA");
            sql_exists.addSQLClause("AND", "LINEA_ATTIVITA.PG_PROGETTO", sql_exists.EQUALS, testata.getPg_progetto());
            if (sql_exists.executeExistsQuery(getConnection(userContext)))
                return false;
            sql_exists = getHome(userContext, Pdg_preventivo_spe_detBulk.class).createSQLBuilder();
            sql_exists.addTableToHeader("LINEA_ATTIVITA");
            sql_exists.addSQLJoin("PDG_PREVENTIVO_SPE_DET.CD_CENTRO_RESPONSABILITA", "LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA");
            sql_exists.addSQLJoin("PDG_PREVENTIVO_SPE_DET.CD_LINEA_ATTIVITA", "LINEA_ATTIVITA.CD_LINEA_ATTIVITA");
            sql_exists.addSQLClause("AND", "LINEA_ATTIVITA.PG_PROGETTO", sql_exists.EQUALS, testata.getPg_progetto());
            if (sql_exists.executeExistsQuery(getConnection(userContext)))
                return false;
            return true;
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }
    }

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        return super.inizializzaBulkPerRicercaLibera(aUC, bulk);
    }

    protected boolean cercaUocordinatrice(ProgettoBulk progetto) {
        for (int i = 0; progetto.getDettagli().size() > i; i++) {
            if (((Progetto_uoBulk) (progetto.getDettagli().get(i))).getCd_unita_organizzativa().equals(progetto.getCd_unita_organizzativa())) {
                return false;
            }
        }
        return true;
    }

    private ProgettoBulk intBulk(UserContext userContext, ProgettoBulk bulk) throws ComponentException {

        if (bulk.getTipo() == null)
            throw new it.cnr.jada.comp.ApplicationException("Attenzione: Per salvare il modulo di attività è necessario inserire il Tipo!");

        //se data di fine esiste deve essere minore di data inizio
        if (bulk.getDt_fine() != null && bulk.getDt_inizio().after(bulk.getDt_fine()))
            throw new it.cnr.jada.comp.ApplicationException("Data di fine deve essere maggiore della data di inizio!");

        //se data di fine non esiste non deve esistere data di proroga
        if (bulk.getDt_fine() == null && bulk.getDt_proroga() != null)
            throw new it.cnr.jada.comp.ApplicationException("Non può esistere una data di proroga se non si indica una data di fine!");

        //se data di proroga esiste deve essere minore di data fine
        if (bulk.getDt_proroga() != null && bulk.getDt_fine().after(bulk.getDt_proroga()))
            throw new it.cnr.jada.comp.ApplicationException("Data di proroga deve essere maggiore della data di fine!");

        if (bulk.getProgettopadre() == null)
            throw new it.cnr.jada.comp.ApplicationException("Attenzione: Per salvare il progetto è necessario inserire il progetto padre!");

        if (bulk.getUnita_organizzativa() == null)
            throw new it.cnr.jada.comp.ApplicationException("L'unità organizzativa è obbligatoria.");

        if ((ProgettoBulk) bulk.getProgettopadre() == null)
            ((ProgettoBulk) bulk).setLivello(new Integer(1));

        if (bulk.getImporto_progetto() == null)
            bulk.setImporto_progetto(new BigDecimal(0));

        //se nei dettagli non è presente la UO cordinatrice viene creata
        if (cercaUocordinatrice(bulk)) {
            Progetto_uoBulk dett = new Progetto_uoBulk(
                    bulk.getPg_progetto(),
                    bulk.getUnita_organizzativa()
            );
            dett.setCrudStatus(dett.TO_BE_CREATED);
            dett.setUser(bulk.getUser());
            bulk.addToDettagli(dett);
        }


        return bulk;
    }

    protected java.math.BigDecimal getSequence(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException {

        //ricavo il progressivo unico pg_progetto
        java.math.BigDecimal pg_progetto = new java.math.BigDecimal(0);
        try {
            LoggableStatement ps = new LoggableStatement(getConnection(userContext),
                    "select CNRSEQ00_PG_PROGETTO.nextval from dual", true, this.getClass());
            try {
                java.sql.ResultSet rs = ps.executeQuery();
                try {
                    if (rs.next())
                        pg_progetto = rs.getBigDecimal(1);
                } finally {
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                    }
                    ;
                }
            } catch (java.sql.SQLException e) {
                throw handleException(e);
            } finally {
                try {
                    ps.close();
                } catch (java.sql.SQLException e) {
                }
                ;
            }
        } catch (java.sql.SQLException e) {
            throw handleException(e);
        }
        return pg_progetto;
    }

    /**
     * Pre:  Ricerca progetti disponibili
     * Post: Limitazione ai progetti della UO in scrivania tranne per l'ente.
     */

    public Query select(UserContext userContext, CompoundFindClause clauses, OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        ProgettoBulk progetto = (ProgettoBulk) bulk;
        ProgettoHome progettohome = (ProgettoHome) getHome(userContext, ProgettoBulk.class, "V_PROGETTO_PADRE");
        SQLBuilder sql = progettohome.createSQLBuilder();
        sql.addClause(clauses);
        sql.addClause(bulk.buildFindClauses(new Boolean(true)));
        sql.addSQLClause("AND", "V_PROGETTO_PADRE.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
        sql.addSQLClause("AND", "V_PROGETTO_PADRE.PG_PROGETTO_PADRE", sql.ISNOTNULL, null);
        sql.addSQLClause("AND", "V_PROGETTO_PADRE.LIVELLO", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_TERZO);
        sql.addClause("AND", "tipo_fase", SQLBuilder.NOT_EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);
        // Se uo 999.000 in scrivania: visualizza tutti i progetti
        Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
        if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals(ente.getCd_unita_organizzativa())) {
            sql.addSQLExistsClause("AND", progettohome.abilitazioniModuli(userContext));
        }
        return sql;
    }

    /**
     * Pre:  Controllo Dt_inizio > Dt_fine
     * Post: Segnalazione "Data di fine deve essere maggiore della data di inizio!"
     * <p>
     * Pre:  Controllo se Dt_fine = null e Dt_proroga != null
     * Post: Segnalazione "Non può esistere una data di proroga se non si indica una data di fine!"
     * <p>
     * Pre:  Controllo Dt_fine > Dt_proroga
     * Post: Segnalazione "Data di proroga deve essere maggiore della data di fine!"
     * <p>
     * Pre:  Controllo se la lista dei dettagli è vuota
     * Post: Se vuota viene creato un unico dettaglio che ha:
     * UO = l'UO coordinatrice del progetto
     * Responsabile = Responsabile del progetto
     * Importo = Importo del progetto
     * <p>
     * Pre:  Controllo somma importo dettagli != da importo del progetto
     * Post: Segnalazione "La somma degli importi degli assegnatari è diversa dall'importo del progetto"
     * <p>
     * Pre:  Se la nuova commessa assegnata non appartiene allo stesso progetto della commessa precedente
     * controllo che il vecchio progetto non sia stato utilizzato sul PDG
     * Post: Segnalazione "Non è possibile associare una commessa appartenente ad un progetto diverso dal precedente"
     */
    public OggettoBulk modificaConBulk(UserContext uc, OggettoBulk bulk) throws ComponentException {
        bulk.setCrudStatus(OggettoBulk.NORMAL);
        return super.modificaConBulk(uc, bulk);
    }

    /*
   * Pre:  Se la nuova commessa assegnata non appartiene allo stesso progetto della commessa precedente
   * 		 controllo che il vecchio progetto non sia stato utilizzato sul PDG
   * Post: Segnalazione "Non è possibile associare una commessa appartenente ad un progetto diverso dal precedente"
   */
    private void verificaCambioProgetto(UserContext userContext, ProgettoBulk progettoOld, ProgettoBulk progettoNew) throws ComponentException {
        if (!progettoOld.getProgettopadre().getProgettopadre().equalsByPrimaryKey(progettoNew.getProgettopadre().getProgettopadre())) {
            try {
                SQLBuilder sqletr = getHome(userContext, it.cnr.contab.config00.latt.bulk.WorkpackageBulk.class, "V_LINEA_ATT_NOT_IN_PDG_ETR").createSQLBuilder();
                sqletr.addSQLClause("AND", "PG_PROGETTO", sqletr.EQUALS, progettoOld.getPg_progetto());
                sqletr.addSQLClause("AND", "CD_ELEMENTO_VOCE", sqletr.ISNOTNULL, null);

                if (sqletr.executeCountQuery(getConnection(userContext)) > 0)
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: operazione non possibile!\nNon è possibile associare una commessa appartenente ad un progetto diverso dal precedente (" + progettoOld.getProgettopadre().getProgettopadre().getCd_progetto() + ") essendo il modulo associato a linee di attività utilizzate nel Piano di Gestione.");

                SQLBuilder sqlspe = getHome(userContext, it.cnr.contab.config00.latt.bulk.WorkpackageBulk.class, "V_LINEA_ATT_NOT_IN_PDG").createSQLBuilder();
                sqlspe.addSQLClause("AND", "PG_PROGETTO", sqlspe.EQUALS, progettoOld.getPg_progetto());
                sqlspe.addSQLClause("AND", "CD_ELEMENTO_VOCE", sqlspe.ISNOTNULL, null);

                if (sqlspe.executeCountQuery(getConnection(userContext)) > 0)
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: operazione non possibile!\nNon è possibile associare una commessa appartenente ad un progetto diverso dal precedente (" + progettoOld.getProgettopadre().getProgettopadre().getCd_progetto() + ") essendo il modulo associato a linee di attività utilizzate nel Piano di Gestione.");

            } catch (Throwable t) {
                throw new ComponentException(t);
            }
        }
    }

    /**
     * Pre:  Ricerca progettopadre
     * Post: Limitazione ai progetti diversi da quello in oggetto.
     */
    public SQLBuilder selectProgettopadreByClause(UserContext userContext,
                                                  OggettoBulk bulk,
                                                  ProgettoBulk progettopadre,
                                                  CompoundFindClause clause)
            throws ComponentException, PersistencyException {
        if (clause == null)
            clause = progettopadre.buildFindClauses(null);
        SQLBuilder sql = getHome(userContext, progettopadre, "V_PROGETTO_PADRE").createSQLBuilder();
        sql.addSQLClause("AND", "PG_PROGETTO", sql.NOT_EQUALS, ((ProgettoBulk) bulk).getPg_progetto());
        if (((ProgettoBulk) bulk).getLivello() != null)
            sql.addSQLClause("AND", "LIVELLO", sql.EQUALS, new Integer(((ProgettoBulk) bulk).getLivello().intValue() - 1));
        if (clause != null)
            sql.addClause(clause);
        return sql;
    }

    /**
     * Pre:  Ricerca terzo ente
     * Post: Limitazione ai terzi ancora validi.
     */
    public SQLBuilder selectEnteByClause(UserContext userContext,
                                         OggettoBulk bulk,
                                         TerzoBulk ente,
                                         CompoundFindClause clause)
            throws ComponentException, PersistencyException {
        if (clause == null) clause = ente.buildFindClauses(null);

        SQLBuilder sql = getHome(userContext, ente).createSQLBuilder();
        sql.addClause(
                it.cnr.jada.persistency.sql.CompoundFindClause.or(
                        new it.cnr.jada.persistency.sql.SimpleFindClause("AND", "dt_fine_rapporto", sql.ISNULL, null),
                        new it.cnr.jada.persistency.sql.SimpleFindClause("AND", "dt_fine_rapporto", sql.GREATER, getHome(userContext, ente).getServerTimestamp())
                )
        );

        if (clause != null) sql.addClause(clause);

        return sql;
    }

    /**
     * Pre:  Ricerca terzo responsabile
     * Post: Limitazione ai terzi ancora validi.
     */
    public SQLBuilder selectResponsabileByClause(UserContext userContext,
                                                 OggettoBulk bulk,
                                                 TerzoBulk responsabile,
                                                 CompoundFindClause clause)
            throws ComponentException, PersistencyException {
        if (clause == null) clause = responsabile.buildFindClauses(null);

        SQLBuilder sql = getHome(userContext, responsabile, "V_TERZO_CF_PI").createSQLBuilder();
        sql.addClause(
                it.cnr.jada.persistency.sql.CompoundFindClause.or(
                        new it.cnr.jada.persistency.sql.SimpleFindClause("AND", "dt_fine_rapporto", sql.ISNULL, null),
                        new it.cnr.jada.persistency.sql.SimpleFindClause("AND", "dt_fine_rapporto", sql.GREATER, getHome(userContext, responsabile).getServerTimestamp())
                )
        );

        if (clause != null) sql.addClause(clause);

        return sql;
    }

    /**
     * Pre:  Ricerca UO
     * Post: Limitazione alle UO valide.
     */
    public SQLBuilder selectUnita_organizzativaByClause(UserContext userContext,
                                                        OggettoBulk bulk,
                                                        Unita_organizzativaBulk uo,
                                                        CompoundFindClause clause)
            throws ComponentException, PersistencyException {
        if (clause == null) clause = uo.buildFindClauses(null);

        SQLBuilder sql = getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA").createSQLBuilder();
        sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));

        if (clause != null) sql.addClause(clause);

        return sql;
    }

    private void inizializzaBulkPerStampa(UserContext usercontext, Stampa_progettiVBulk stampa_progettivbulk)
            throws ComponentException {
        stampa_progettivbulk.setCd_cds(CNRUserContext.getCd_cds(usercontext));
        stampa_progettivbulk.setEsercizio(CNRUserContext.getEsercizio(usercontext));
    }

    private void inizializzaBulkPerStampa(UserContext usercontext, Stampa_anag_progettiVBulk stampa_progettivbulk)
            throws ComponentException {
        String cd_uo = CNRUserContext.getCd_unita_organizzativa(usercontext);
        try {
            stampa_progettivbulk.setLivello(stampa_progettivbulk.LIVELLO_PROGETTO_ALL);
            Unita_organizzativaHome uoHome = (Unita_organizzativaHome) getHome(usercontext, Unita_organizzativaBulk.class);
            Unita_organizzativaBulk uo = (Unita_organizzativaBulk) uoHome.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo));

            Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(usercontext, Unita_organizzativa_enteBulk.class).findAll().get(0);
            if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals(ente.getCd_unita_organizzativa())) {
                stampa_progettivbulk.setUnita_organizzativaForPrint(uo);
                stampa_progettivbulk.setIsUOForPrintEnabled(false);
            } else {
                stampa_progettivbulk.setUnita_organizzativaForPrint(new Unita_organizzativaBulk());
                stampa_progettivbulk.setIsUOForPrintEnabled(true);
            }
        } catch (it.cnr.jada.persistency.PersistencyException pe) {
            throw new ComponentException(pe);
        }
    }

    public OggettoBulk inizializzaBulkPerStampa(UserContext usercontext, OggettoBulk oggettobulk)
            throws ComponentException {
        if (oggettobulk instanceof Stampa_progettiVBulk)
            inizializzaBulkPerStampa(usercontext, (Stampa_progettiVBulk) oggettobulk);
        if (oggettobulk instanceof Stampa_anag_progettiVBulk)
            inizializzaBulkPerStampa(usercontext, (Stampa_anag_progettiVBulk) oggettobulk);
        return oggettobulk;
    }

    /**
     * stampaConBulk method comment.
     */
    public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        return bulk;
    }

    /**
     * Tutti i controlli superati.
     * PreCondition:
     * E' stata generata la richiesta di creazione di un Iteratore su tutti i nodi figli
     * di un Progetto.
     * PostCondition:
     * Viene restituito il RemoteIterator con l'elenco degli eventuali nodi figli del progetto di riferimento.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> il progetto di riferimento.
     * @return remoteIterator <code>RemoteIterator</code> l'Iterator creato.
     **/
    public RemoteIterator getChildren(UserContext userContext, OggettoBulk bulk) throws ComponentException {

        ProgettoBulk ubi = (ProgettoBulk) bulk;
        ProgettoHome ubiHome = (ProgettoHome) getHome(userContext, ProgettoBulk.class, "V_PROGETTO_PADRE");
        return iterator(
                userContext,
                ubiHome.selectChildrenFor(userContext, ubi),
                ProgettoBulk.class,
                null);
    }

    /**
     * Tutti i controlli superati.
     * PreCondition:
     * E' stata generata la richiesta di creazione di un Iteratore su tutti i nodi figli
     * di un Progetto.
     * PostCondition:
     * Viene restituito il RemoteIterator con l'elenco degli eventuali nodi figli del progetto di riferimento.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> il progetto di riferimento.
     * @return remoteIterator <code>RemoteIterator</code> l'Iterator creato.
     **/
    public RemoteIterator getChildrenWorkpackage(UserContext userContext, OggettoBulk bulk) throws ComponentException {

        ProgettoBulk ubi = (ProgettoBulk) bulk;
        ProgettoHome ubiHome = (ProgettoHome) getHome(userContext, ProgettoBulk.class, "V_PROGETTO_PADRE");
        return iterator(
                userContext,
                ubiHome.selectChildrenForWorkpackage(userContext, ubi),
                ProgettoBulk.class,
                null);
    }

    /**
     * Tutti i controlli superati.
     * PreCondition:
     * E' stata generata la richiesta di ricerca del Progetto padre del Progetto specificato negli argomenti.
     * PostCondition:
     * Viene restituito l'oggetto ProgettoBulk che è il Progetto padre cercato.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> il Progetto di riferimento.
     * @return bulk <code>OggettoBulk</code> il Progetto cercato.
     **/
    public OggettoBulk getParent(UserContext userContext, OggettoBulk bulk) throws ComponentException {

        try {
            ProgettoBulk ubi = (ProgettoBulk) bulk;
            ProgettoHome ubiHome = (ProgettoHome) getHome(userContext, ProgettoBulk.class, "V_PROGETTO_PADRE");
            return ubiHome.getParent(ubi);

        } catch (it.cnr.jada.persistency.PersistencyException ex) {
            throw handleException(bulk, ex);
        } catch (it.cnr.jada.persistency.IntrospectionException ex) {
            throw handleException(bulk, ex);
        }
    }

    /**
     * Controlla che il progetto sia una foglia.
     * PreCondition:
     * E' stata generata la richiesta di controllare se il Progetto specificato è una foglia,
     * ossia se il suo livello è l'ultimo, (3). Questo implicherebbe che il Progetto in
     * questione non ha dei Progetti figli.
     * PostCondition:
     * Viene restituito un valore booleano:
     * - true: il Progetto è una foglia;
     * - false: il Progetto non è una foglia.
     *
     * @param userContext lo <code>UserContext</code> che ha generato la richiesta
     * @param bulk        <code>OggettoBulk</code> il Progetto di riferimento.
     * @return il risultato <code>boolean</code> del controllo.
     **/
    public boolean isLeaf(UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            ProgettoBulk ubi = (ProgettoBulk) bulk;
            ProgettoHome ubiHome = (ProgettoHome) getHome(userContext, ubi, "V_PROGETTO_PADRE");
            return (!ubiHome.selectChildrenFor(userContext, ubi).executeExistsQuery(getConnection(userContext)));
        } catch (Throwable e) {
            throw handleException(e);
        }
    }

    public ProgettoBulk cercaWorkpackages(UserContext userContext, ProgettoBulk modulo) throws it.cnr.jada.comp.ComponentException {

        try {
            ProgettoHome moduloHome = (ProgettoHome) getHome(userContext, modulo.getClass());

            Collection result1 = moduloHome.findWorkpackage_collegati(userContext, modulo);
            for (java.util.Iterator i = result1.iterator(); i.hasNext(); ) {
                modulo.addToWorkpackage_collegati((WorkpackageBulk) i.next());
            }

            Collection result2 = moduloHome.findWorkpackage_disponibili(userContext, modulo);
            for (java.util.Iterator i = result2.iterator(); i.hasNext(); ) {
                WorkpackageBulk wp = (WorkpackageBulk) i.next();
                if (wp.getProgetto() != null) {
                    ProgettoBulk pgkey = new ProgettoBulk(((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio(), wp.getProgetto().getPg_progetto(), ProgettoBulk.TIPO_FASE_PREVISIONE);
                    ProgettoBulk pg = (ProgettoBulk) getHome(userContext, ProgettoBulk.class).findByPrimaryKey(
                            pgkey);
                    wp.setProgetto(pg);
                }
                modulo.addToWorkpackage_disponibili(wp);
            }
        } catch (Exception e) {
            throw handleException(e);
        }
        return modulo;

    }

    public String creaCodiceProgetto(UserContext aUC, ProgettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            LoggableStatement cs = new LoggableStatement(getConnection(aUC),
                    "{ ? = call " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "creaCodiceProgetto(?, ?, ?)}", false, this.getClass());
            try {
                cs.registerOutParameter(1, java.sql.Types.CHAR);
                cs.setString(2, bulk.getProgettopadre().getCd_progetto());
                cs.setString(3, TIPO_PROGETTO);
                cs.setObject(4, bulk.getParametriCds().getProgetto_numeratore_cifre());
                cs.executeQuery();

                String result = cs.getString(1);
                return result;
            } catch (SQLException e) {
                throw handleException(e);
            } finally {
                cs.close();
            }
        } catch (SQLException e) {
            throw handleException(e);
        }
    }

    public Parametri_cdsBulk parametriCds(UserContext aUC, ProgettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        Parametri_cdsBulk param;
        try {
            param = (Parametri_cdsBulk) getHome(aUC, Parametri_cdsBulk.class).findByPrimaryKey(
                    new Parametri_cdsBulk(
                            ((CNRUserContext) aUC).getCd_cds(),
                            ((CNRUserContext) aUC).getEsercizio()));
        } catch (PersistencyException ex) {
            throw handleException(ex);
        } catch (ComponentException ex) {
            throw handleException(ex);
        }
        if (param == null) {
            //throw new ApplicationException("Parametri CDS non trovati per il CDS "+((CNRUserContext) aUC).getCd_cds());
            // se si vuole gestire un default
            param = new Parametri_cdsBulk();
            param.setFl_progetto_numeratore(Boolean.FALSE);
        }
        return param;
    }

    public RemoteIterator cercaModuliForWorkpackage(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException {
        try {
            return iterator(usercontext, selectModuliForWorkpackage(usercontext, compoundfindclause, oggettobulk), oggettobulk.getClass(), getFetchPolicyName("find"));
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    private Query selectModuliForWorkpackage(UserContext userContext, CompoundFindClause clauses, OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
        ProgettoBulk progetto = (ProgettoBulk) bulk;
        ProgettoHome progettohome = (ProgettoHome) getHome(userContext, ProgettoBulk.class, "V_PROGETTO_PADRE");
        SQLBuilder sql = progettohome.createSQLBuilder();
        sql.addClause(clauses);
        progetto.setTipo_fase(null);
        sql.addClause(bulk.buildFindClauses(new Boolean(true)));
        sql.addSQLClause("AND", "V_PROGETTO_PADRE.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
        sql.addSQLClause("AND", "V_PROGETTO_PADRE.PG_PROGETTO_PADRE", sql.ISNOTNULL, null);
        sql.addSQLClause("AND", "V_PROGETTO_PADRE.LIVELLO", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_TERZO);
        sql.addClause("AND", "tipo_fase", SQLBuilder.EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);
        // Se uo 999.000 in scrivania: visualizza tutti i progetti
        Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
        if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals(ente.getCd_unita_organizzativa())) {
            sql.addSQLExistsClause("AND", progettohome.abilitazioniModuli(userContext));
        }
        return sql;
    }

    public DipartimentoBulk getDipartimentoModulo(UserContext userContext, ProgettoBulk modulo) throws it.cnr.jada.comp.ComponentException {
        try {
            DipartimentoHome dipartimentohome = (DipartimentoHome) getHome(userContext, DipartimentoBulk.class);
            SQLBuilder sql = dipartimentohome.createSQLBuilder();

            ProgettoHome progettohome = (ProgettoHome) getHome(userContext, ProgettoBulk.class);
            SQLBuilder sqlProgetto = progettohome.createSQLBuilder();

            if (ProgettoBulk.LIVELLO_PROGETTO_SECONDO.equals(modulo.getLivello())) {
                sqlProgetto.addClause("AND", "esercizio", SQLBuilder.EQUALS, modulo.getEsercizio());
                sqlProgetto.addClause("AND", "pg_progetto", SQLBuilder.EQUALS, modulo.getPg_progetto());
                sqlProgetto.addClause("AND", "tipo_fase", SQLBuilder.EQUALS, modulo.getTipo_fase());
                sqlProgetto.addClause("AND", "livello", SQLBuilder.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_SECONDO);

                //Aggiungo in Join le Aree Progettuali
                sqlProgetto.addTableToHeader("PROGETTO A");
                sqlProgetto.addSQLJoin("PROGETTO.ESERCIZIO_PROGETTO_PADRE", "A.ESERCIZIO");
                sqlProgetto.addSQLJoin("PROGETTO.PG_PROGETTO_PADRE", "A.PG_PROGETTO");
                sqlProgetto.addSQLJoin("PROGETTO.TIPO_FASE_PROGETTO_PADRE", "A.TIPO_FASE");
                sqlProgetto.addSQLJoin("A.CD_DIPARTIMENTO", "DIPARTIMENTO.CD_DIPARTIMENTO");
            } else {
                sqlProgetto.addClause("AND", "esercizio", SQLBuilder.EQUALS, modulo.getEsercizio());
                sqlProgetto.addClause("AND", "pg_progetto", SQLBuilder.EQUALS, modulo.getPg_progetto());
                sqlProgetto.addClause("AND", "tipo_fase", SQLBuilder.EQUALS, modulo.getTipo_fase());
                sqlProgetto.addClause("AND", "livello", SQLBuilder.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_TERZO);

                //Aggiungo in Join le commesse
                sqlProgetto.addTableToHeader("PROGETTO A");
                sqlProgetto.addSQLJoin("PROGETTO.ESERCIZIO_PROGETTO_PADRE", "A.ESERCIZIO");
                sqlProgetto.addSQLJoin("PROGETTO.PG_PROGETTO_PADRE", "A.PG_PROGETTO");
                sqlProgetto.addSQLJoin("PROGETTO.TIPO_FASE_PROGETTO_PADRE", "A.TIPO_FASE");

                //Aggiungo in Join i progetti
                sqlProgetto.addTableToHeader("PROGETTO B");
                sqlProgetto.addSQLJoin("A.ESERCIZIO_PROGETTO_PADRE", "B.ESERCIZIO");
                sqlProgetto.addSQLJoin("A.PG_PROGETTO_PADRE", "B.PG_PROGETTO");
                sqlProgetto.addSQLJoin("A.TIPO_FASE_PROGETTO_PADRE", "B.TIPO_FASE");
                sqlProgetto.addSQLJoin("B.CD_DIPARTIMENTO", "DIPARTIMENTO.CD_DIPARTIMENTO");
            }

            sql.addSQLExistsClause("AND", sqlProgetto);

            List result = dipartimentohome.fetchAll(sql);
            if (result.isEmpty())
                return null;
            return (DipartimentoBulk) result.get(0);
        } catch (PersistencyException e) {
            throw new ComponentException(e);
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }
}
