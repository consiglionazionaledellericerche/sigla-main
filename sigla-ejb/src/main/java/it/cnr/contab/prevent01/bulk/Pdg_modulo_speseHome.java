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

/*
 * Created by Generator 1.0
 * Date 29/09/2005
 */
package it.cnr.contab.prevent01.bulk;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_spese_gestBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Pdg_modulo_speseHome extends BulkHome {
    public Pdg_modulo_speseHome(java.sql.Connection conn) {
        super(Pdg_modulo_speseBulk.class, conn);
    }

    public Pdg_modulo_speseHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(Pdg_modulo_speseBulk.class, conn, persistentCache);
    }

    public SQLBuilder calcolaPrevisioneAssestataAnnoPrecedente(it.cnr.jada.UserContext userContext, Pdg_modulo_speseBulk pdg_modulo_spese, Integer anno_precedente, String tipo_natura) throws IntrospectionException, PersistencyException {
        SQLBuilder sql = calcolaPrevisioneAssestataAnnoPrecedente(userContext, pdg_modulo_spese, anno_precedente);
        sql.addSQLClause("AND", "NATURA.TIPO", SQLBuilder.EQUALS, tipo_natura);
        return sql;
    }

    public SQLBuilder calcolaPrevisioneAssestataAnnoPrecedente(it.cnr.jada.UserContext userContext, Pdg_modulo_speseBulk pdg_modulo_spese, Integer anno_precedente) throws IntrospectionException, PersistencyException {
        String nomeColonnaLivello = "V_CLASSIFICAZIONE_VOCI_ALL.CD_LIV" + pdg_modulo_spese.getClassificazione().getNr_livello();
        PersistentHome dettHome = getHomeCache().getHome(Voce_f_saldi_cdr_lineaBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("SUM(VOCE_F_SALDI_CDR_LINEA.IM_STANZ_INIZIALE_A1 + VOCE_F_SALDI_CDR_LINEA.VARIAZIONI_PIU - VOCE_F_SALDI_CDR_LINEA.VARIAZIONI_MENO) TOTALE");
        sql.addTableToHeader("LINEA_ATTIVITA");
        sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL");
        sql.addTableToHeader("ELEMENTO_VOCE");
        sql.addTableToHeader("NATURA");
        sql.addTableToHeader("CDR");
        sql.addTableToHeader("UNITA_ORGANIZZATIVA");
        sql.addSQLClause("AND", "LINEA_ATTIVITA.PG_PROGETTO", SQLBuilder.EQUALS, pdg_modulo_spese.getPg_progetto());
        sql.addSQLClause("AND", "VOCE_F_SALDI_CDR_LINEA.ESERCIZIO", SQLBuilder.EQUALS, anno_precedente);
        sql.addSQLJoin("VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA", "CDR.CD_CENTRO_RESPONSABILITA");
        sql.addSQLJoin("CDR.CD_UNITA_ORGANIZZATIVA", "UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
        sql.addSQLClause("AND", "UNITA_ORGANIZZATIVA.CD_UNITA_PADRE", SQLBuilder.EQUALS, pdg_modulo_spese.getPdg_modulo_costi().getPdg_modulo().getCdr().getCd_cds());
        sql.addSQLJoin("LINEA_ATTIVITA.CD_LINEA_ATTIVITA", "VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA");
        sql.addSQLJoin("LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA", "VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA");
        sql.addSQLJoin("LINEA_ATTIVITA.CD_NATURA", "NATURA.CD_NATURA");
        sql.addSQLClause("AND", "VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA", SQLBuilder.EQUALS, Voce_f_saldi_cdr_lineaBulk.TIPO_APPARTENENZA_CDS);
        sql.addSQLClause("AND", "VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
        sql.addSQLClause("AND", nomeColonnaLivello, SQLBuilder.EQUALS, pdg_modulo_spese.getClassificazione().getCd_classificazione());
        sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI_ALL.FL_MASTRINO", SQLBuilder.EQUALS, "Y");
        sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI_ALL.ESERCIZIO", SQLBuilder.EQUALS, anno_precedente);
        sql.addSQLJoin("V_CLASSIFICAZIONE_VOCI_ALL.ID_CLASSIFICAZIONE", "ELEMENTO_VOCE.ID_CLASSIFICAZIONE");
        sql.addSQLJoin("ELEMENTO_VOCE.ESERCIZIO", "VOCE_F_SALDI_CDR_LINEA.ESERCIZIO");
        sql.addSQLJoin("ELEMENTO_VOCE.TI_APPARTENENZA", "VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA");
        sql.addSQLJoin("ELEMENTO_VOCE.TI_GESTIONE", "VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE");
        sql.addSQLJoin("ELEMENTO_VOCE.CD_ELEMENTO_VOCE", "VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE");
        return sql;
    }

    public void initializePrimaryKeyForInsert(UserContext usercontext, OggettoBulk oggettobulk) throws PersistencyException, ComponentException {
        try {
            Pdg_modulo_speseBulk pdg_modulo_spese = (Pdg_modulo_speseBulk) oggettobulk;
            if (pdg_modulo_spese.getCd_cds_area() == null)
                pdg_modulo_spese.setArea(pdg_modulo_spese.getPdg_modulo_costi().getPdg_modulo().getCdr().getUnita_padre().getUnita_padre());
            pdg_modulo_spese.setPg_dettaglio(
                    new Integer(((Integer) findAndLockMax(oggettobulk, "pg_dettaglio", new Integer(0))).intValue() + 1));
            super.initializePrimaryKeyForInsert(usercontext, pdg_modulo_spese);
        } catch (it.cnr.jada.bulk.BusyResourceException e) {
            throw new PersistencyException(e);
        }
    }

    /**
     * Ritorna la lista delle classificazioni Mastrino
     *
     * @param classificazione
     * @return
     * @throws IntrospectionException
     * @throws PersistencyException
     */
    public java.util.List findClassificazioneMastrinoList(V_classificazione_vociBulk classificazione) throws IntrospectionException, PersistencyException {
        String nomeColonnaLivello = "CD_LIV" + classificazione.getNr_livello();
        PersistentHome asHome = getHomeCache().getHome(V_classificazione_vociBulk.class);
        SQLBuilder sql = asHome.createSQLBuilder();
        sql.addClause("AND", nomeColonnaLivello, SQLBuilder.EQUALS, classificazione.getCd_classificazione());
        sql.addClause("AND", "FL_MASTRINO", SQLBuilder.EQUALS, Boolean.TRUE);
        return asHome.fetchAll(sql);
    }

    /**
     * Ritorna la lista degli elemento voce legati alla classificazione
     *
     * @param classificazione
     * @return
     * @throws IntrospectionException
     * @throws PersistencyException
     */
    public java.util.List findElemento_voceList(V_classificazione_vociBulk classificazione) throws IntrospectionException, PersistencyException {
        PersistentHome asHome = getHomeCache().getHome(Elemento_voceBulk.class);
        SQLBuilder sql = asHome.createSQLBuilder();
        sql.addClause("AND", "TI_GESTIONE", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
        sql.addClause("AND", "ID_CLASSIFICAZIONE", SQLBuilder.EQUALS, classificazione.getId_classificazione());
        return asHome.fetchAll(sql);
    }

    /**
     * Ritorna una lista di Voce_f legate all'elemento voce
     *
     * @param elemento_voce
     * @return
     * @throws IntrospectionException
     * @throws PersistencyException
     */
    public java.util.List findVoceByElementoList(Elemento_voceBulk elemento_voce, Integer esercizio) throws IntrospectionException, PersistencyException {
        PersistentHome asHome = getHomeCache().getHome(Voce_fBulk.class);
        SQLBuilder sql = asHome.createSQLBuilder();
        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, esercizio);
        sql.addSQLClause("AND", "CD_ELEMENTO_VOCE", SQLBuilder.EQUALS, elemento_voce.getCd_elemento_voce());
        return asHome.fetchAll(sql);
    }

    /**
     * @param voce
     * @param cd_cds
     * @return
     * @throws IntrospectionException
     * @throws PersistencyException
     */
    public java.util.List findSaldiForVoceList(Voce_fBulk voce, String cd_cds) throws IntrospectionException, PersistencyException {
        PersistentHome asHome = getHomeCache().getHome(Voce_f_saldi_cmpBulk.class);
        SQLBuilder sql = asHome.createSQLBuilder();
        sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, voce.getEsercizio());
        sql.addSQLClause("AND", "CD_CDS", SQLBuilder.EQUALS, cd_cds);
        sql.addSQLClause("AND", "TI_APPARTENENZA", SQLBuilder.EQUALS, voce.getTi_appartenenza());
        sql.addSQLClause("AND", "TI_GESTIONE", SQLBuilder.EQUALS, voce.getTi_gestione());
        sql.addSQLClause("AND", "CD_VOCE", SQLBuilder.EQUALS, voce.getCd_voce());
        return asHome.fetchAll(sql);
    }

    /**
     * Ritorna l'SQLBuilder utile per recuperare tutti i dati nella tabella PDG_MODULO_SPESE_GEST
     * relativi alla testata in uso.
     *
     * @param testata La testata in uso.
     * @return SQLBuilder
     */
    private SQLBuilder sqlBuilderDettagliGestionali(Pdg_modulo_speseBulk testata) throws PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(Pdg_modulo_spese_gestBulk.class);
        SQLBuilder sql = dettHome.createSQLBuilder();
        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, testata.getEsercizio());
        sql.addClause("AND", "cd_centro_responsabilita", SQLBuilder.EQUALS, testata.getCd_centro_responsabilita());
        sql.addClause("AND", "pg_progetto", SQLBuilder.EQUALS, testata.getPg_progetto());
        sql.addClause("AND", "id_classificazione", SQLBuilder.EQUALS, testata.getId_classificazione());
        sql.addClause("AND", "cd_cds_area", SQLBuilder.EQUALS, testata.getCd_cds_area());
        sql.addClause("AND", "pg_dettaglio", SQLBuilder.EQUALS, testata.getPg_dettaglio());
        return sql;
    }

    /**
     * Ritorna l'SQLBuilder utile per recuperare i dati originali nella tabella PDG_MODULO_SPESE_GEST individuati
     * dal fatto che il CDR_Assegnatario appartiene allo stesso CDS del Centro di responsabilità principale
     * relativi alla testata in uso.
     *
     * @param testata La testata in uso.
     * @return SQLBuilder
     */
    private SQLBuilder sqlBuilderDettagliGestionaliOriginali(Pdg_modulo_speseBulk testata) throws PersistencyException {
        SQLBuilder sql = sqlBuilderDettagliGestionali(testata);

        sql.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "A");
        sql.addSQLJoin("PDG_MODULO_SPESE_GEST.ESERCIZIO", "A.ESERCIZIO");
        sql.addSQLJoin("PDG_MODULO_SPESE_GEST.CD_CENTRO_RESPONSABILITA", "A.CD_ROOT");

        sql.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "B");
        sql.addSQLJoin("PDG_MODULO_SPESE_GEST.ESERCIZIO", "B.ESERCIZIO");
        sql.addSQLJoin("PDG_MODULO_SPESE_GEST.CD_CDR_ASSEGNATARIO", "B.CD_ROOT");

        sql.addSQLJoin("A.CD_CDS", "B.CD_CDS");

        return sql;
    }

    /**
     * Recupera tutti i dati nella tabella PDG_MODULO_SPESE_GEST relativi alla testata in uso.
     *
     * @param testata La testata in uso.
     * @return java.util.Collection Collezione di oggetti <code>Pdg_modulo_spese_gestBulk</code>
     */
    public java.util.Collection findDettagliGestionali(Pdg_modulo_speseBulk testata) throws PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(Pdg_modulo_spese_gestBulk.class);
        return dettHome.fetchAll(sqlBuilderDettagliGestionali(testata));
    }

    /**
     * Recupera tutti i dati originali nella tabella PDG_MODULO_SPESE_GEST individuati
     * dal fatto che il CDR_Assegnatario appartiene allo stesso CDS del Centro di responsabilità principale
     * relativi alla testata in uso.
     *
     * @param testata La testata in uso.
     * @return java.util.Collection Collezione di oggetti <code>Pdg_modulo_spese_gestBulk</code>
     */
    public java.util.Collection findDettagliGestionaliOriginali(Pdg_modulo_speseBulk testata) throws PersistencyException {
        PersistentHome dettHome = getHomeCache().getHome(Pdg_modulo_spese_gestBulk.class);
        return dettHome.fetchAll(sqlBuilderDettagliGestionaliOriginali(testata));
    }

    /**
     * Calcola il totale della colonna IM_SPESE_GEST_ACCENTRATA_INT nella tabella PDG_MODULO_SPESE_GEST
     * relativi alla testata in uso.
     *
     * @param testata La testata in uso.
     * @return java.util.Collection Collezione di oggetti <code>Pdg_modulo_spese_gestBulk</code>
     */
    public SQLBuilder calcolaTotaleDettagliGestionaliAccInt(it.cnr.jada.UserContext userContext, Pdg_modulo_speseBulk testata) throws PersistencyException {
        SQLBuilder sql = sqlBuilderDettagliGestionaliOriginali(testata);
        sql.resetColumns();
        sql.addColumn("SUM(PDG_MODULO_SPESE_GEST.IM_SPESE_GEST_ACCENTRATA_INT) TOTALE");
        return sql;
    }

    /**
     * Calcola il totale della colonna IM_SPESE_GEST_ACCENTRATA_EST nella tabella PDG_MODULO_SPESE_GEST
     * relativi alla testata in uso.
     *
     * @param testata La testata in uso.
     * @return java.util.Collection Collezione di oggetti <code>Pdg_modulo_spese_gestBulk</code>
     */
    public SQLBuilder calcolaTotaleDettagliGestionaliAccEst(it.cnr.jada.UserContext userContext, Pdg_modulo_speseBulk testata) throws PersistencyException {
        SQLBuilder sql = sqlBuilderDettagliGestionaliOriginali(testata);
        sql.resetColumns();
        sql.addColumn("SUM(PDG_MODULO_SPESE_GEST.IM_SPESE_GEST_ACCENTRATA_EST) TOTALE");
        return sql;
    }

    /**
     * Calcola il totale della colonna IM_SPESE_GEST_DECENTRATA_INT nella tabella PDG_MODULO_SPESE_GEST
     * relativi alla testata in uso.
     *
     * @param testata La testata in uso.
     * @return java.util.Collection Collezione di oggetti <code>Pdg_modulo_spese_gestBulk</code>
     */
    public SQLBuilder calcolaTotaleDettagliGestionaliDecInt(it.cnr.jada.UserContext userContext, Pdg_modulo_speseBulk testata) throws PersistencyException {
        SQLBuilder sql = sqlBuilderDettagliGestionaliOriginali(testata);
        sql.resetColumns();
        sql.addColumn("SUM(PDG_MODULO_SPESE_GEST.IM_SPESE_GEST_DECENTRATA_INT) TOTALE");
        return sql;
    }

    /**
     * Calcola il totale della colonna IM_SPESE_GEST_DECENTRATA_EST nella tabella PDG_MODULO_SPESE_GEST
     * relativi alla testata in uso.
     *
     * @param testata La testata in uso.
     * @return java.util.Collection Collezione di oggetti <code>Pdg_modulo_spese_gestBulk</code>
     */
    public SQLBuilder calcolaTotaleDettagliGestionaliDecEst(it.cnr.jada.UserContext userContext, Pdg_modulo_speseBulk testata) throws PersistencyException {
        SQLBuilder sql = sqlBuilderDettagliGestionaliOriginali(testata);
        sql.resetColumns();
        sql.addColumn("SUM(PDG_MODULO_SPESE_GEST.IM_SPESE_GEST_DECENTRATA_EST) TOTALE");
        return sql;
    }

    public SQLBuilder selectClassificazioneByClause(Integer esercizio, String cdr, Integer nrLivello) throws ComponentException, PersistencyException {
        SQLBuilder sql = getHomeCache().getHome(V_classificazione_vociBulk.class).createSQLBuilder();
        sql.addTableToHeader("CDR");
        sql.addTableToHeader("UNITA_ORGANIZZATIVA");
        sql.addSQLClause(FindClause.AND, "V_CLASSIFICAZIONE_VOCI.NR_LIVELLO", SQLBuilder.EQUALS, nrLivello);
        sql.addSQLClause(FindClause.AND, "CDR.CD_CENTRO_RESPONSABILITA", SQLBuilder.EQUALS, cdr);
        sql.addSQLJoin("CDR.CD_UNITA_ORGANIZZATIVA", "UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
        sql.openParenthesis(FindClause.AND);
        sql.addSQLClause(FindClause.AND, "V_CLASSIFICAZIONE_VOCI.FL_ACCENTRATO", SQLBuilder.EQUALS, "Y");
        sql.addSQLClause(FindClause.OR, "V_CLASSIFICAZIONE_VOCI.FL_DECENTRATO", SQLBuilder.EQUALS, "Y");
        sql.closeParenthesis();
        sql.openParenthesis(FindClause.AND);
        sql.addSQLClause(FindClause.AND, "UNITA_ORGANIZZATIVA.CD_TIPO_UNITA", SQLBuilder.EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_SAC);
        //sql.addClause("AND", "V_CLASSIFICAZIONE_VOCI.FL_CLASS_SAC", sql.EQUALS, Boolean.TRUE);
        sql.openParenthesis(FindClause.OR);
        sql.addSQLClause(FindClause.AND, "UNITA_ORGANIZZATIVA.CD_TIPO_UNITA", SQLBuilder.NOT_EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_SAC);
        sql.addSQLClause(FindClause.AND, "V_CLASSIFICAZIONE_VOCI.FL_CLASS_SAC", SQLBuilder.EQUALS, "N");
        sql.closeParenthesis();
        sql.closeParenthesis();
        sql.addSQLClause(FindClause.AND, "V_CLASSIFICAZIONE_VOCI.ESERCIZIO", SQLBuilder.EQUALS, esercizio);
        sql.addSQLClause(FindClause.AND, "V_CLASSIFICAZIONE_VOCI.TI_GESTIONE", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
        sql.addSQLClause(FindClause.AND, "V_CLASSIFICAZIONE_VOCI.FL_SOLO_GESTIONE", SQLBuilder.EQUALS, "N");
        return sql;
    }
}