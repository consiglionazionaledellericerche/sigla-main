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
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.sto.bulk.DipartimentoBulk;
import it.cnr.contab.config00.sto.bulk.DipartimentoHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.progettiric00.core.bulk.*;
import it.cnr.contab.progettiric00.geco.bulk.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.RemoteIterator;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @author Marco Spasiano
 * <p>
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ProgettoRicercaPadreComponent extends it.cnr.jada.comp.CRUDComponent implements IProgettoRicercaMgr {
    public static final String TIPO_PROGETTO = "P";

    /**
     * Constructor for ProgettoRicercaPadreComponent.
     */
    public ProgettoRicercaPadreComponent() {
        super();
    }

    private ProgettoBulk intBulk(UserContext userContext, ProgettoBulk bulk) throws ComponentException {
        try {
            boolean flNuovoPdg = Utility.createParametriCnrComponentSession().getParametriCnr(userContext, CNRUserContext.getEsercizio(userContext)).getFl_nuovo_pdg();
            boolean flInformix = Utility.createParametriEnteComponentSession().getParametriEnte(userContext).getFl_informix();

            if (bulk.getUnita_organizzativa() == null)
                throw new it.cnr.jada.comp.ApplicationException("L'unità organizzativa è obbligatoria.");

            if (bulk.getDipartimento() == null)
                throw new it.cnr.jada.comp.ApplicationException("Attenzione: Per salvare il progetto è necessario inserire il Dipartimento!");

            if (flNuovoPdg) {
                if (bulk.getCd_progetto() == null)
                    throw new it.cnr.jada.comp.ApplicationException("Campo codice obbligatorio.");
                else {
                    ProgettoHome progettohome = (ProgettoHome) getHome(userContext, ProgettoBulk.class, "V_PROGETTO_PADRE");
                    SQLBuilder sql = progettohome.createSQLBuilder();
                    sql.addClause(FindClause.AND, "cd_progetto", SQLBuilder.EQUALS, bulk.getCd_progetto());
                    if (bulk.getPg_progetto() != null)
                        sql.addClause(FindClause.AND, "pg_progetto", SQLBuilder.NOT_EQUALS, bulk.getPg_progetto());
                    List<ProgettoBulk> progettiClone = progettohome.fetchAll(sql);
                    if (!progettiClone.isEmpty())
                        throw new it.cnr.jada.comp.ApplicationException("Esiste già un progetto con il codice indicato (id: " + progettiClone.get(0).getPg_progetto() + ").");
                }

                if (bulk.getDs_progetto() == null)
                    throw new it.cnr.jada.comp.ApplicationException("Campo descrizione obbligatorio.");

                if (!flInformix && (bulk.getPdgProgramma() == null || bulk.getPdgProgramma().getCd_programma() == null))
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: Per salvare il progetto è necessario inserire il Programma!");
            } else {

                if (bulk.getDt_inizio() == null)
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: Per salvare il progetto è necessario inserire la data di inizio!");

                //se data di fine esiste deve essere minore di data inizio
                if (bulk.getDt_fine() != null && bulk.getDt_inizio().after(bulk.getDt_fine()))
                    throw new it.cnr.jada.comp.ApplicationException("Data di fine deve essere maggiore della data di inizio!");

                //se data di fine non esiste non deve esistere data di proroga
                if (bulk.getDt_fine() == null && bulk.getDt_proroga() != null)
                    throw new it.cnr.jada.comp.ApplicationException("Non può esistere una data di proroga se non si indica una data di fine!");

                //se data di proroga esiste deve essere minore di data fine
                if (bulk.getDt_proroga() != null && bulk.getDt_fine().after(bulk.getDt_proroga()))
                    throw new it.cnr.jada.comp.ApplicationException("Data di proroga deve essere maggiore della data di fine!");

                if (bulk.getImporto_progetto() == null)
                    throw new it.cnr.jada.comp.ApplicationException("Entità delle risorse necessarie è obbligatorio.");

                //se non vengono specificati dettagli Finanziatori
                if (((ProgettoBulk) bulk).getDettagliFinanziatori().isEmpty())
                    throw new it.cnr.jada.comp.ApplicationException("Attenzione: Per salvare il progetto è necessario inserire almeno un Finanziatore!");

                //calcolo la discrepanza tra importo progetto e importo dettagli finanziatori
                if (!((ProgettoBulk) bulk).getDettagliFinanziatori().isEmpty()) {
                    java.math.BigDecimal sum = new java.math.BigDecimal(0);
                    java.math.BigDecimal residuo = new java.math.BigDecimal(0);
                    for (int i = 0; bulk.getDettagliFinanziatori().size() > i; i++) {
                        residuo = ((Progetto_finanziatoreBulk) bulk.getDettagliFinanziatori().get(i)).getImporto_finanziato();
                        if (residuo == null)
                            throw new it.cnr.jada.comp.ApplicationException("L'importo dei finanziatori non può essere nullo!");
                        sum = sum.add(residuo);
                    }
                    //se i dettagli hanno un importo maggiore del progetto
                    if (sum.compareTo(bulk.getImporto_progetto()) > 0) {
                        throw new it.cnr.jada.comp.ApplicationException("La somma degli importi dei finanziatori è superiore all'importo del progetto");
                    } else if (sum.compareTo(bulk.getImporto_progetto()) < 0) {
                        throw new it.cnr.jada.comp.ApplicationException("La somma degli importi dei finanziatori è inferiore all'importo del progetto");
                    }
                }
            }

            if ((ProgettoBulk) bulk.getProgettopadre() == null)
                ((ProgettoBulk) bulk).setLivello(new Integer(1));

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
        } catch (Exception e) {
            throw new ComponentException(e);
        }
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
        progetto.setTipo_fase(null);
        sql.addClause(bulk.buildFindClauses(new Boolean(true)));
        sql.addSQLClause("AND", "V_PROGETTO_PADRE.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
        sql.addSQLClause("AND", "PG_PROGETTO_PADRE", sql.ISNULL, null);
        sql.addClause("AND", "tipo_fase", SQLBuilder.EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);

        // Se uo 999.000 in scrivania: visualizza tutti i progetti
        Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome(userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
        if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals(ente.getCd_unita_organizzativa())) {
            try {
                if (Utility.createParametriEnteComponentSession().getParametriEnte(userContext).getFl_informix())
                    sql.addSQLExistsClause("AND", progettohome.abilitazioniProgetti(userContext));
            } catch (Exception e) {
                throw handleException(e);
            }
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
     */
    public OggettoBulk modificaConBulk(UserContext uc, OggettoBulk bulk) throws ComponentException {

        intBulk(uc, (ProgettoBulk) bulk);


        try {
            validateBulkForInsert(uc, bulk);
            ProgettoBulk progettoPrev = (ProgettoBulk) getHome(uc, ProgettoBulk.class).findByPrimaryKey(new ProgettoBulk(((ProgettoBulk) bulk).getEsercizio(), ((ProgettoBulk) bulk).getPg_progetto(), ProgettoBulk.TIPO_FASE_PREVISIONE));
            if (progettoPrev != null)
                getHome(uc, ProgettoBulk.class, "PROGETTO_SIP").delete(progettoPrev, uc);

            ProgettoBulk progettoGest = (ProgettoBulk) getHome(uc, ProgettoBulk.class).findByPrimaryKey(new ProgettoBulk(((ProgettoBulk) bulk).getEsercizio(), ((ProgettoBulk) bulk).getPg_progetto(), ProgettoBulk.TIPO_FASE_GESTIONE));
            if (progettoGest != null)
                getHome(uc, ProgettoBulk.class, "PROGETTO_SIP").delete(progettoGest, uc);

            ((ProgettoBulk) bulk).setTipo_fase(ProgettoBulk.TIPO_FASE_PREVISIONE);
            getHome(uc, bulk, "PROGETTO_SIP").insert((Persistent) bulk, uc);

            ((ProgettoBulk) bulk).setTipo_fase(ProgettoBulk.TIPO_FASE_GESTIONE);
            getHome(uc, bulk, "PROGETTO_SIP").insert((Persistent) bulk, uc);
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
        return bulk;
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

        ((ProgettoBulk) bulk).setFl_piano_triennale(false);

        ((ProgettoBulk) bulk).setStato(ProgettoBulk.TIPO_STATO_APPROVATO);
        try {
            validaCreaConBulk(uc, bulk);
            ((ProgettoBulk) bulk).setTipo_fase(ProgettoBulk.TIPO_FASE_PREVISIONE);
            getHome(uc, bulk, "PROGETTO_SIP").insert((Persistent) bulk, uc);

            ((ProgettoBulk) bulk).setTipo_fase(ProgettoBulk.TIPO_FASE_GESTIONE);
            getHome(uc, bulk, "PROGETTO_SIP").insert((Persistent) bulk, uc);
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
        return bulk;
    }

    protected boolean cercaUocordinatrice(ProgettoBulk progetto) {
        for (int i = 0; progetto.getDettagli().size() > i; i++) {
            if (((Progetto_uoBulk) (progetto.getDettagli().get(i))).getCd_unita_organizzativa().equals(progetto.getCd_unita_organizzativa())) {
                return false;
            }
        }
        return true;
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

    public void eliminaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            /*Se sto cancellando il progetto cancello anche tutti i dettagli */
            if (bulk instanceof ProgettoBulk) {
                ProgettoHome progettohome = (ProgettoHome) getHome(aUC, ProgettoBulk.class, "V_PROGETTO_PADRE");
                SQLBuilder sql = progettohome.createSQLBuilder();
                sql.addSQLClause(FindClause.AND, "P_PG_PROGETTO", SQLBuilder.EQUALS, ((ProgettoBulk) bulk).getPg_progetto());
                List<ProgettoBulk> progettiFigli = progettohome.fetchAll(sql);
                if (!progettiFigli.isEmpty())
                    throw new it.cnr.jada.comp.ApplicationException("Esistono progetti collegati. Eliminazione non possibile.");

                for (int i = 0; ((ProgettoBulk) bulk).getDettagli().size() > i; i++) {
                    ((Progetto_uoBulk) ((ProgettoBulk) bulk).getDettagli().get(i)).setCrudStatus(bulk.TO_BE_DELETED);
                }
                for (int i = 0; ((ProgettoBulk) bulk).getDettagliFinanziatori().size() > i; i++) {
                    ((Progetto_finanziatoreBulk) ((ProgettoBulk) bulk).getDettagliFinanziatori().get(i)).setCrudStatus(bulk.TO_BE_DELETED);
                }
                for (int i = 0; ((ProgettoBulk) bulk).getDettagliPartner_esterni().size() > i; i++) {
                    ((Progetto_partner_esternoBulk) ((ProgettoBulk) bulk).getDettagliPartner_esterni().get(i)).setCrudStatus(bulk.TO_BE_DELETED);
                }


                ProgettoBulk progettoPrev = (ProgettoBulk) getHome(aUC, ProgettoBulk.class).findByPrimaryKey(new ProgettoBulk(((ProgettoBulk) bulk).getEsercizio(), ((ProgettoBulk) bulk).getPg_progetto(), ProgettoBulk.TIPO_FASE_PREVISIONE));
                if (progettoPrev != null)
                    getHome(aUC, ProgettoBulk.class, "PROGETTO_SIP").delete(progettoPrev, aUC);

                ProgettoBulk progettoGest = (ProgettoBulk) getHome(aUC, ProgettoBulk.class).findByPrimaryKey(new ProgettoBulk(((ProgettoBulk) bulk).getEsercizio(), ((ProgettoBulk) bulk).getPg_progetto(), ProgettoBulk.TIPO_FASE_GESTIONE));
                if (progettoGest != null)
                    getHome(aUC, ProgettoBulk.class, "PROGETTO_SIP").delete(progettoGest, aUC);
            }
        } catch (Throwable throwable) {
            throw handleException(throwable);
        }
    }

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            // inizializzazione per gestire la codifica automatica
            ((ProgettoBulk) bulk).setParametriCds(parametriCds(aUC, (ProgettoBulk) bulk));
            ((ProgettoBulk) bulk).setFl_utilizzabile(Boolean.TRUE);
            ((ProgettoBulk) bulk).setEsercizio(CNRUserContext.getEsercizio(aUC));
            if (Utility.createParametriCnrComponentSession().getParametriCnr(aUC, CNRUserContext.getEsercizio(aUC)).getFl_nuovo_pdg()) {
                ((ProgettoBulk) bulk).setLivello(1);
                ((ProgettoBulk) bulk).setResponsabile(((TerzoHome) getHome(aUC, TerzoBulk.class)).findTerzoEnte());
                ((ProgettoBulk) bulk).setDt_inizio(DateUtils.firstDateOfTheYear(CNRUserContext.getEsercizio(aUC)));
                ((ProgettoBulk) bulk).setImporto_progetto(BigDecimal.ZERO);
                ((ProgettoBulk) bulk).setDurata_progetto(ProgettoBulk.DURATA_PROGETTO_PLURIENNALE);
                ((ProgettoBulk) bulk).setCondiviso(Boolean.TRUE);
            }
        } catch (Exception e) {
            throw handleException(e);
        }
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
            getHomeCache(userContext).fetchAll(userContext);
            return testata;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        return super.inizializzaBulkPerRicercaLibera(aUC, bulk);
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

    public OggettoBulk inizializzaBulkPerStampa(UserContext usercontext, OggettoBulk oggettobulk)
            throws ComponentException {
        if (oggettobulk instanceof Stampa_progettiVBulk)
            inizializzaBulkPerStampa(usercontext, (Stampa_progettiVBulk) oggettobulk);
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

    public String creaCodiceProgetto(UserContext aUC, ProgettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
        try {
            LoggableStatement cs = new LoggableStatement(getConnection(aUC),
                    "{ ? = call " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "creaCodiceProgetto(?, ?, ?)}", false, this.getClass());
            try {
                cs.registerOutParameter(1, java.sql.Types.CHAR);
                cs.setString(2, bulk.getCd_dipartimento());
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


    public void aggiornaGECO(UserContext userContext) throws ComponentException {
        try {
            //L'aggiornamento parte solo se attivo informix
            Parametri_enteBulk parEnte = Utility.createParametriEnteComponentSession().getParametriEnte(userContext);
            if (parEnte.getFl_informix()) {
                ProgettoHome progettoHome = ((ProgettoHome) getHome(userContext, ProgettoBulk.class));
                progettoHome.aggiornaGeco(userContext, null);
            }
        } catch (RemoteException e) {
            throw new ComponentException(e);
        }
    }

    private void handleExceptionMail(UserContext userContext, Exception e) {
    }

    public void aggiornaGECODipartimenti(UserContext userContext) throws ComponentException {
        try {
            //L'aggiornamento parte solo se attivo informix
            Parametri_enteBulk parEnte = Utility.createParametriEnteComponentSession().getParametriEnte(userContext);
            if (parEnte.getFl_informix()) {
                DipartimentoHome dipartimentoHome = ((DipartimentoHome) getHome(userContext, DipartimentoBulk.class));
                dipartimentoHome.aggiornaDipartimenti(userContext, null);
            }
        } catch (RemoteException e) {
            throw new ComponentException(e);
        }
    }

    public void cancellaProgettoSIP(UserContext userContext) throws ComponentException {
        try {
            List<Progetto_sipBulk> listModuliNotDeleted = new BulkList<Progetto_sipBulk>(), listCommesseNotDeleted = new BulkList<Progetto_sipBulk>();

            Progetto_sipHome moduli_utilizzatiHome = ((Progetto_sipHome) getHome(userContext, Progetto_sipBulk.class, "V_SIP_MODULI_VALIDI"));
            Progetto_sipHome progettoHome = ((Progetto_sipHome) getHome(userContext, Progetto_sipBulk.class));

            SQLBuilder sqlModuli = progettoHome.createSQLBuilderAll();
            sqlModuli.addClause(FindClause.AND, "livello", SQLBuilder.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_TERZO);
            List<Progetto_sipBulk> listModuli = progettoHome.fetchAll(sqlModuli);

            //Giro prima sui moduli
            for (Progetto_sipBulk moduli : listModuli) {
                if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext, new Geco_moduloBulk(new Long(moduli.getPg_progetto().intValue()), new Long(moduli.getEsercizio().intValue()), moduli.getTipo_fase())) == null) {
                    if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext, new Geco_modulo_sacBulk(new Long(moduli.getPg_progetto().intValue()), new Long(moduli.getEsercizio().intValue()), moduli.getTipo_fase())) == null) {
                        if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext, new Geco_modulo_rstlBulk(new Long(moduli.getPg_progetto().intValue()), new Long(moduli.getEsercizio().intValue()), moduli.getTipo_fase())) == null) {
                            if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext, new Geco_modulo_pbBulk(new Long(moduli.getPg_progetto().intValue()), new Long(moduli.getEsercizio().intValue()), moduli.getTipo_fase())) == null) {
                                if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext, new Geco_attivitaBulk(new Long(moduli.getPg_progetto().intValue()), new Long(moduli.getEsercizio().intValue()), moduli.getTipo_fase())) == null) {
                                    SQLBuilder sql = moduli_utilizzatiHome.createSQLBuilderAll();
                                    sql.addSQLClause(FindClause.AND, "ESERCIZIO", SQLBuilder.EQUALS, moduli.getEsercizio());
                                    sql.addSQLClause(FindClause.AND, "PG_PROGETTO", SQLBuilder.EQUALS, moduli.getPg_progetto());
                                    sql.addSQLClause(FindClause.AND, "TIPO_FASE", SQLBuilder.EQUALS, moduli.getTipo_fase());
                                    sql.addSQLClause(FindClause.AND, "FL_CANCELLABILE", SQLBuilder.EQUALS, String.valueOf("N"));
                                    sql.addSQLClause(FindClause.AND, "FL_TERMINABILE", SQLBuilder.EQUALS, String.valueOf("N"));
                                    SQLBroker brokerUtilizzati = moduli_utilizzatiHome.createBroker(sql);
                                    if (brokerUtilizzati.next()) {
                                        listModuliNotDeleted.add(moduli);
                                        handleExceptionMail(userContext, new ApplicationException("Si è tentato di cancellare il modulo utilizzato: " + moduli.getEsercizio() + "/" + moduli.getPg_progetto() + "/" + moduli.getTipo_fase()));
                                    } else {
                                        moduli.setToBeDeleted();
                                        super.eliminaConBulk(userContext, moduli);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            SQLBuilder sqlCommesse = progettoHome.createSQLBuilderAll();
            sqlCommesse.addClause(FindClause.AND, "livello", SQLBuilder.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
            List<Progetto_sipBulk> listCommesse = progettoHome.fetchAll(sqlCommesse);

            //Poi sulle commesse
            for (Progetto_sipBulk commesse : listCommesse) {
                if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext, new Geco_commessaBulk(new Long(commesse.getPg_progetto().intValue()), new Long(commesse.getEsercizio().intValue()), commesse.getTipo_fase())) == null) {
                    if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext, new Geco_commessa_sacBulk(new Long(commesse.getPg_progetto().intValue()), new Long(commesse.getEsercizio().intValue()), commesse.getTipo_fase())) == null) {
                        if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext, new Geco_commessa_rstlBulk(new Long(commesse.getPg_progetto().intValue()), new Long(commesse.getEsercizio().intValue()), commesse.getTipo_fase())) == null) {
                            if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext, new Geco_commessa_pbBulk(new Long(commesse.getPg_progetto().intValue()), new Long(commesse.getEsercizio().intValue()), commesse.getTipo_fase())) == null) {
                                if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext, new Geco_progetto_operativoBulk(new Long(commesse.getPg_progetto().intValue()), new Long(commesse.getEsercizio().intValue()), commesse.getTipo_fase())) == null) {
                                    SQLBuilder sql = moduli_utilizzatiHome.createSQLBuilderAll();
                                    sql.addSQLClause(FindClause.AND, "ESERCIZIO", SQLBuilder.EQUALS, commesse.getEsercizio());
                                    sql.addSQLClause(FindClause.AND, "PG_PROGETTO", SQLBuilder.EQUALS, commesse.getPg_progetto());
                                    sql.addSQLClause(FindClause.AND, "TIPO_FASE", SQLBuilder.EQUALS, commesse.getTipo_fase());
                                    sql.addSQLClause(FindClause.AND, "FL_CANCELLABILE", SQLBuilder.EQUALS, String.valueOf("N"));
                                    sql.addSQLClause(FindClause.AND, "FL_TERMINABILE", SQLBuilder.EQUALS, String.valueOf("N"));
                                    SQLBroker brokerUtilizzati = moduli_utilizzatiHome.createBroker(sql);
                                    if (brokerUtilizzati.next()) {
                                        listCommesseNotDeleted.add(commesse);
                                        handleExceptionMail(userContext, new ApplicationException("Si è tentato di cancellare il modulo utilizzato: " + commesse.getEsercizio() + "/" + commesse.getPg_progetto() + "/" + commesse.getTipo_fase()));
                                    } else {
                                        boolean commessaIsToBeDeleted = true;
                                        for (Progetto_sipBulk moduloBulk : listModuliNotDeleted) {
                                            if (commesse.getEsercizio().equals(moduloBulk.getEsercizio_progetto_padre()) &&
                                                    commesse.getPg_progetto().equals(moduloBulk.getPg_progetto_padre()) &&
                                                    commesse.getTipo_fase().equals(moduloBulk.getTipo_fase_progetto_padre())) {
                                                commessaIsToBeDeleted = false;
                                                break;
                                            }
                                        }
                                        if (commessaIsToBeDeleted) {
                                            commesse.setToBeDeleted();
                                            super.eliminaConBulk(userContext, commesse);
                                        } else {
                                            listCommesseNotDeleted.add(commesse);
                                            handleExceptionMail(userContext, new ApplicationException("Si è tentato di cancellare il progetto di secondo livello utilizzato: " + commesse.getEsercizio() + "/" + commesse.getPg_progetto() + "/" + commesse.getTipo_fase()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            SQLBuilder sqlProgetti = progettoHome.createSQLBuilderAll();
            sqlProgetti.addClause(FindClause.AND, "livello", SQLBuilder.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_PRIMO);
            List<Progetto_sipBulk> listProgetti = progettoHome.fetchAll(sqlProgetti);

            //Ed infine sui progetti
            for (Progetto_sipBulk progetto : listProgetti) {
                if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext, new Geco_progettoBulk(new Long(progetto.getPg_progetto().intValue()), new Long(progetto.getEsercizio().intValue()), progetto.getTipo_fase())) == null) {
                    if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext, new Geco_progetto_sacBulk(new Long(progetto.getPg_progetto().intValue()), new Long(progetto.getEsercizio().intValue()), progetto.getTipo_fase())) == null) {
                        if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext, new Geco_progetto_rstlBulk(new Long(progetto.getPg_progetto().intValue()), new Long(progetto.getEsercizio().intValue()), progetto.getTipo_fase())) == null) {
                            if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext, new Geco_progetto_pbBulk(new Long(progetto.getPg_progetto().intValue()), new Long(progetto.getEsercizio().intValue()), progetto.getTipo_fase())) == null) {
                                if (Utility.createProgettoGecoComponentSession().findByPrimaryKey(userContext, new Geco_area_progBulk(new Long(progetto.getPg_progetto().intValue()), new Long(progetto.getEsercizio().intValue()), progetto.getTipo_fase())) == null) {
                                    boolean progettoIsToBeDeleted = true;
                                    for (Progetto_sipBulk commesseBulk : listCommesseNotDeleted) {
                                        if (progetto.getEsercizio().equals(commesseBulk.getEsercizio_progetto_padre()) &&
                                                progetto.getPg_progetto().equals(commesseBulk.getPg_progetto_padre()) &&
                                                progetto.getTipo_fase().equals(commesseBulk.getTipo_fase_progetto_padre())) {
                                            progettoIsToBeDeleted = false;
                                            break;
                                        }
                                    }
                                    if (progettoIsToBeDeleted) {
                                        progetto.setToBeDeleted();
                                        super.eliminaConBulk(userContext, progetto);
                                    } else {
                                        handleExceptionMail(userContext, new ApplicationException("Si è tentato di cancellare il progetto utilizzato: " + progetto.getEsercizio() + "/" + progetto.getPg_progetto() + "/" + progetto.getTipo_fase()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            handleExceptionMail(userContext, e);
        }
    }

    @Override
    public Persistent findByPrimaryKey(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        if (Optional.ofNullable(oggettobulk).isPresent()) {
            BulkHome progettoHome = getHome(usercontext, oggettobulk.getClass(), "V_PROGETTO_PADRE");
            try {
                final Persistent byPrimaryKey = progettoHome.findByPrimaryKey(usercontext, oggettobulk);
                getHomeCache(usercontext).fetchAll(usercontext);
                return byPrimaryKey;
            } catch (PersistencyException e) {
                throw handleException(e);
            }
        }
        return super.findByPrimaryKey(usercontext, oggettobulk);
    }
}
