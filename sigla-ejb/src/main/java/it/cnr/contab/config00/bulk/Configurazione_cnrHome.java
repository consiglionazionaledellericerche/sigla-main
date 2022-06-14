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

package it.cnr.contab.config00.bulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Configurazione_cnrHome extends BulkHome {
    public static final Integer ANNI_ALL = 0;
    public static final String ASTERISCO = "*";

    /**
     * <!-- @TODO: da completare -->
     * Costruisce un Configurazione_cnrHome
     *
     * @param conn La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     */
    public Configurazione_cnrHome(java.sql.Connection conn) {
        super(Configurazione_cnrBulk.class, conn);
    }

    /**
     * <!-- @TODO: da completare -->
     * Costruisce un Configurazione_cnrHome
     *
     * @param conn            La java.sql.Connection su cui vengono effettuate le operazione di persistenza
     * @param persistentCache La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
     */
    public Configurazione_cnrHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(Configurazione_cnrBulk.class, conn, persistentCache);
    }

    public java.util.List findTipoVariazioniPdg() throws PersistencyException {

        SQLBuilder sql = createSQLBuilder();

        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, new Integer(0));
        sql.addClause("AND", "cd_unita_funzionale", SQLBuilder.EQUALS, Configurazione_cnrBulk.PK_PDG_VARIAZIONE);
        sql.addClause("AND", "cd_chiave_primaria", SQLBuilder.EQUALS, Configurazione_cnrBulk.SK_TIPO_VAR_APPROVA_CDS);

        return fetchAll(sql);
    }

    public java.util.List findTipoVariazioniStanz_res() throws PersistencyException {

        SQLBuilder sql = createSQLBuilder();

        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, new Integer(0));
        sql.addClause("AND", "cd_unita_funzionale", SQLBuilder.EQUALS, Configurazione_cnrBulk.PK_VAR_STANZ_RES);
        sql.addClause("AND", "cd_chiave_primaria", SQLBuilder.EQUALS, Configurazione_cnrBulk.SK_TIPO_VAR_APPROVA_CDS);

        return fetchAll(sql);
    }

    public java.util.List findTipoVariazioniEnteStanz_res() throws PersistencyException {

        SQLBuilder sql = createSQLBuilder();

        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, new Integer(0));
        sql.addClause("AND", "cd_unita_funzionale", SQLBuilder.EQUALS, Configurazione_cnrBulk.PK_VAR_STANZ_RES);
        sql.addClause("AND", "cd_chiave_primaria", SQLBuilder.EQUALS, Configurazione_cnrBulk.SK_TIPO_VAR_APPROVA_CNR);

        return fetchAll(sql);
    }

    /**
     * Indica se la uo indicata è proprio quella speciale tutta sac
     *
     * @param esercizio l'esercizio di ricerca - Lasciare vuoto per ricercare il parametro generale (esercizio=0).
     * @param cdUnitaOrganizzativa l'unità organizzativa di cui si chiede se si tratta della Uo Speciale Tutta SAC
     * @return boolean
     * @throws PersistencyException
     */
    public boolean isUOSpecialeDistintaTuttaSAC(Integer esercizio, String cdUnitaOrganizzativa) throws PersistencyException {
        return Optional.ofNullable(this.getUoDistintaTuttaSac(esercizio))
                .filter(uoDistintaTuttaSac -> uoDistintaTuttaSac.equals(cdUnitaOrganizzativa))
                .isPresent();
    }

    /**
     * Ritorna il record puntuale richiesto sulla base dei parametri indicati.
     * <p>ATTENZIONE: in questo metodo viene ricercato puntualmente il record con l'esercizio indicato</p>
     * <p>Se si desidera avere, in caso di assenza, il record con esercizio=0 utilizzare il metodo {@link #getConfigurazione(UserContext,String,String,String)} o il
     * metodo {@link #getConfigurazione(Integer,String,String,String)}</p>
     *
     * @param esercizio l'esercizio di ricerca - Lasciare vuoto per ricercare il parametro generale (esercizio=0).
     * @param unita_funzionale Unità funzionale - Lasciare vuoto per ricercare il parametro generale (unita_funzionale='*').
     * @param chiave_primaria Chiave Primaria - Campo Obbligatorio
     * @param chiave_secondaria Chiave Secondaria - Lasciare vuoto per ricercare il parametro generale (chiave_secondaria='*')
     * @return il record ConfigurazioneCNR richiesto sulla base dei parametri indicati
     * @throws PersistencyException
     */
    public Configurazione_cnrBulk getConfigurazioneCnrBulk(Integer esercizio, String unita_funzionale, String chiave_primaria, String chiave_secondaria) throws PersistencyException {
        if (esercizio == null) esercizio = new Integer(0);
        if (unita_funzionale == null) unita_funzionale = ASTERISCO;
        if (chiave_secondaria == null) chiave_secondaria = ASTERISCO;
        return (Configurazione_cnrBulk) getHomeCache().getHome(Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, chiave_secondaria, unita_funzionale, esercizio));
    }

    /**
     * Ritorna il record richiesto sulla base dei parametri indicati.
     * <p>ATTENZIONE: in questo metodo viene ricercato prima il record con esercizio di scrivania prelevato dallo Usercontext e,
     * in caso di assenza, viene ricercato il record con anno=0</p>
     * <p>Se si desidera avere il record puntuale per i parametri impostati utilizzare il metodo {@link #getConfigurazioneCnrBulk(Integer,String,String,String)}</p>
     *
     * @param userContext lo UserContext da cui recupera l'esercizio di scrivania
     * @param unita_funzionale Unità funzionale - Lasciare vuoto per ricercare il parametro generale (unita_funzionale='*').
     * @param chiave_primaria Chiave Primaria - Campo Obbligatorio
     * @param chiave_secondaria Chiave Secondaria - Lasciare vuoto per ricercare il parametro generale (chiave_secondaria='*')
     * @return il record ConfigurazioneCNR richiesto sulla base dei parametri indicati
     * @throws PersistencyException
     */
    public Configurazione_cnrBulk getConfigurazione(UserContext userContext, String unita_funzionale, String chiave_primaria, String chiave_secondaria) throws PersistencyException {
        return getConfigurazione(CNRUserContext.getEsercizio(userContext),unita_funzionale,chiave_primaria, chiave_secondaria);
    }

    /**
     * Ritorna il record richiesto sulla base dei parametri indicati.
     * <p>ATTENZIONE: in questo metodo viene ricercato sempre il record con esercizio =0</p>
     * <p>Se si desidera avere il record puntuale per i parametri impostati utilizzare il metodo {@link #getConfigurazioneCnrBulk(Integer,String,String,String)}</p>
     *
     * @param unita_funzionale Unità funzionale - Lasciare vuoto per ricercare il parametro generale (unita_funzionale='*').
     * @param chiave_primaria Chiave Primaria - Campo Obbligatorio
     * @param chiave_secondaria Chiave Secondaria - Lasciare vuoto per ricercare il parametro generale (chiave_secondaria='*')
     * @return il record ConfigurazioneCNR richiesto sulla base dei parametri indicati
     * @throws PersistencyException
     */
    public Configurazione_cnrBulk getConfigurazione(String unita_funzionale, String chiave_primaria, String chiave_secondaria) throws PersistencyException {
        return getConfigurazione(ANNI_ALL,unita_funzionale,chiave_primaria, chiave_secondaria);
    }

    /**
     * Ritorna il record richiesto sulla base dei parametri indicati.
     * <p>ATTENZIONE: in questo metodo viene ricercato prima il record con l'esercizio indicato e, in caso di assenza, viene ricercato il record con esercizio=0</p>
     * <p>Se si desidera avere il record puntuale per i parametri impostati utilizzare il metodo {@link #getConfigurazioneCnrBulk(Integer,String,String,String)}</p>
     *
     * @param esercizio l'esercizio di ricerca - se non esistono configurazioni per l'esercizio indicato viene cercata la configurazione con esercizio=0
     * @param unita_funzionale Unità funzionale - Lasciare vuoto per ricercare il parametro generale (unita_funzionale='*').
     * @param chiave_primaria Chiave Primaria - Campo Obbligatorio
     * @param chiave_secondaria Chiave Secondaria - Lasciare vuoto per ricercare il parametro generale (chiave_secondaria='*')
     * @return il record ConfigurazioneCNR richiesto sulla base dei parametri indicati
     * @throws PersistencyException
     */
    public Configurazione_cnrBulk getConfigurazione(Integer esercizio, String unita_funzionale, String chiave_primaria, String chiave_secondaria) throws PersistencyException {
        Configurazione_cnrBulk config = null;
        if (Optional.ofNullable(esercizio).isPresent())
            config = getConfigurazioneCnrBulk(esercizio,unita_funzionale,chiave_primaria,chiave_secondaria);
        if (!Optional.ofNullable(config).isPresent() && Optional.ofNullable(esercizio).filter(ese->!ese.equals(ANNI_ALL)).isPresent())
            config = getConfigurazioneCnrBulk(ANNI_ALL,unita_funzionale,chiave_primaria,chiave_secondaria);
        return config;
    }

    /**
     * Ritorna il codice uo della Ragioneria
     * <p><b>chiave_primaria: UO_SPECIALE</b>
     * <p><b>chiave_secondaria: UO_RAGIONERIA</b>
     *
     * @param esercizio l'esercizio di ricerca - se non esistono configurazioni per l'esercizio indicato viene cercata la configurazione con esercizio=0
     * @return String - il codice uo della Ragioneria
     * @throws PersistencyException
     */
    public String getUoRagioneria(Integer esercizio) throws PersistencyException {
        return Optional.ofNullable(
                        this.getConfigurazione(esercizio,null,Configurazione_cnrBulk.PK_UO_SPECIALE, Configurazione_cnrBulk.SK_UO_RAGIONERIA))
                .map(Configurazione_cnrBulk::getVal01)
                .orElse(null);
    }

    public String getContoCorrenteEnte(Integer esercizio) throws PersistencyException {
        return Optional.ofNullable(
                this.getConfigurazione(esercizio,null,Configurazione_cnrBulk.PK_CONTO_CORRENTE_SPECIALE, Configurazione_cnrBulk.SK_ENTE))
                .map(Configurazione_cnrBulk::getVal01)
                .orElse(null);
    }

    /**
     * Ritorna il codice cdr del personale
     * <p><b>chiave_primaria: CDR_SPECIALE</b>
     * <p><b>chiave_secondaria: CDR_PERSONALE</b>
     *
     * @param esercizio l'esercizio di ricerca - se non esistono configurazioni per l'esercizio indicato viene cercata la configurazione con esercizio=0
     * @return String - il codice cdr del personale
     * @throws PersistencyException
     */
    public String getCdrPersonale(Integer esercizio) throws PersistencyException {
        return Optional.ofNullable(
                        this.getConfigurazione(esercizio,null,Configurazione_cnrBulk.PK_CDR_SPECIALE, Configurazione_cnrBulk.SK_CDR_PERSONALE))
                .map(Configurazione_cnrBulk::getVal01)
                .orElse(null);
    }

    /**
     * Ritorna il codice cdr servizio ente
     * <p><b>chiave_primaria: CDR_SPECIALE</b>
     * <p><b>chiave_secondaria: CDR_SERVIZIO_ENTE</b>
     *
     * @param esercizio l'esercizio di ricerca - se non esistono configurazioni per l'esercizio indicato viene cercata la configurazione con esercizio=0
     * @return String - il codice cdr servizio ente
     * @throws PersistencyException
     */
    public String getCdrServizioEnte(Integer esercizio) throws PersistencyException {
        return Optional.ofNullable(
                        this.getConfigurazione(esercizio,null,Configurazione_cnrBulk.PK_CDR_SPECIALE, Configurazione_cnrBulk.SK_CDR_SERVIZIO_ENTE))
                .map(Configurazione_cnrBulk::getVal01)
                .orElse(null);
    }

    /**
     * Ritorna il codice uo di Accreditamento Sac
     * <p><b>chiave_primaria: UO_SPECIALE</b>
     * <p><b>chiave_secondaria: UO_RAGIONERIA</b>
     *
     * @param esercizio l'esercizio di ricerca - se non esistono configurazioni per l'esercizio indicato viene cercata la configurazione con esercizio=0
     * @return String - il codice uo di Accreditamento Sac
     * @throws PersistencyException
     */
    public String getUoAccreditamentoSac(Integer esercizio) throws PersistencyException {
        return Optional.ofNullable(
                        this.getConfigurazione(esercizio,null,Configurazione_cnrBulk.PK_UO_SPECIALE, Configurazione_cnrBulk.SK_UO_ACCREDITAMENTO_SAC))
                .map(Configurazione_cnrBulk::getVal01)
                .orElse(null);
    }

    /**
     * Ritorna il codice uo distinta tutta sac
     * <p><b>chiave_primaria: UO_SPECIALE</b>
     * <p><b>chiave_secondaria: UO_DISTINTA_TUTTA_SAC</b>
     *
     * @param esercizio l'esercizio di ricerca - se non esistono configurazioni per l'esercizio indicato viene cercata la configurazione con esercizio=0
     * @return String - il codice codice uo distinta tutta sac
     * @throws PersistencyException
     */
    public String getUoDistintaTuttaSac(Integer esercizio) throws PersistencyException {
        return Optional.ofNullable(
                        this.getConfigurazione(esercizio,null,Configurazione_cnrBulk.PK_UO_SPECIALE, Configurazione_cnrBulk.SK_UO_DISTINTA_TUTTA_SAC))
                .map(Configurazione_cnrBulk::getVal01)
                .orElse(null);
    }
    /**
     * Ritorna il codice cds della SAC
     * <p><b>chiave_primaria: CDS_SPECIALE</b>
     * <p><b>chiave_secondaria: CDS_SAC</b>
     *
     * @param esercizio l'esercizio di ricerca - se non esistono configurazioni per l'esercizio indicato viene cercata la configurazione con esercizio=0
     * @throws PersistencyException
     */
    public String getCdsSAC(Integer esercizio) throws PersistencyException {
        return Optional.ofNullable(
                        this.getConfigurazione(esercizio,null,Configurazione_cnrBulk.PK_CDS_SPECIALE, Configurazione_cnrBulk.SK_CDS_SAC))
                .map(Configurazione_cnrBulk::getVal01)
                .orElse(null);
    }
    /**
     * Ritorna il codice cds della SAC
     * <p><b>chiave_primaria: CDS_SPECIALE</b>
     * <p><b>chiave_secondaria: CDS_SAC</b>
     *
     * @throws PersistencyException
     */
    public String getBeneServScontoAbbuono() throws PersistencyException {
        return Optional.ofNullable(
                        this.getConfigurazione(null,Configurazione_cnrBulk.PK_BENE_SERVIZIO_SPECIALE, Configurazione_cnrBulk.SK_SCONTO_ABBUONO))
                .map(Configurazione_cnrBulk::getVal01)
                .orElse(null);
    }

    /**
     *
     * @param userContext
     * @return É attiva la gestione dell'economico patrimononale (parallela o pura)
     * @throws PersistencyException
     */
    public boolean isAttivaEconomica(UserContext userContext) throws PersistencyException {
        return Optional.ofNullable(
                        this.getConfigurazione(CNRUserContext.getEsercizio(userContext), null,
                                Configurazione_cnrBulk.PK_ECONOMICO_PATRIMONIALE,
                                Configurazione_cnrBulk.SK_TIPO_ECONOMICO_PATRIMONIALE)
                )
                .map(Configurazione_cnrBulk::getVal01)
                .map(s -> !Boolean.valueOf(s.equalsIgnoreCase("N")))
                .orElse(Boolean.FALSE);
    }

    /**
     *
     * @param userContext
     * @return É attiva la gestione dell'economico patrimononale parallela
     * @throws PersistencyException
     */
    public boolean isAttivaEconomicaParallela(UserContext userContext) throws PersistencyException {
        return Optional.ofNullable(
                        this.getConfigurazione(CNRUserContext.getEsercizio(userContext), null,
                                Configurazione_cnrBulk.PK_ECONOMICO_PATRIMONIALE,
                                Configurazione_cnrBulk.SK_TIPO_ECONOMICO_PATRIMONIALE)
                )
                .map(Configurazione_cnrBulk::getVal01)
                .map(s -> Boolean.valueOf(s.equalsIgnoreCase("PARALLELA")))
                .orElse(Boolean.FALSE);
    }

    /**
     *
     * @param userContext
     * @return É attiva la gestione dell'economico patrimononale pura
     * @throws PersistencyException
     */
    public boolean isAttivaEconomicaPura(UserContext userContext) throws PersistencyException {
        return Optional.ofNullable(
                        this.getConfigurazione(CNRUserContext.getEsercizio(userContext), null,
                                Configurazione_cnrBulk.PK_ECONOMICO_PATRIMONIALE,
                                Configurazione_cnrBulk.SK_TIPO_ECONOMICO_PATRIMONIALE)
                )
                .map(Configurazione_cnrBulk::getVal01)
                .map(s -> Boolean.valueOf(s.equalsIgnoreCase("PURA")))
                .orElse(Boolean.FALSE);
    }

    /**
     *
     * @param userContext
     * @return É attivo il blocco delle scritture di economica
     * @throws PersistencyException
     */
    public boolean isBloccoScrittureProposte(UserContext userContext) throws PersistencyException{
        return Optional.ofNullable(
                        this.getConfigurazione(CNRUserContext.getEsercizio(userContext), null,
                                Configurazione_cnrBulk.PK_ECONOMICO_PATRIMONIALE,
                                Configurazione_cnrBulk.SK_TIPO_ECONOMICO_PATRIMONIALE)
                )
                .map(Configurazione_cnrBulk::getVal02)
                .map(s -> Boolean.valueOf(s.equalsIgnoreCase("Y")))
                .orElse(Boolean.TRUE);
    }

    /**
     *
     * @param userContext
     * @return É attivo il blocco delle scritture di economica
     * @throws PersistencyException
     */
    public boolean isVariazioneAutomaticaSpesa(UserContext userContext) throws PersistencyException{
        return Optional.ofNullable(
                        this.getConfigurazione(CNRUserContext.getEsercizio(userContext), null,
                                Configurazione_cnrBulk.PK_VARIAZIONE_AUTOMATICA,
                                Configurazione_cnrBulk.SK_SPESA)
                )
                .map(Configurazione_cnrBulk::getVal01)
                .map(s -> Boolean.valueOf(s.equalsIgnoreCase("Y")))
                .orElse(Boolean.FALSE);
    }

    /**
     * Ritorna il codice terzo da utilizzare come DIVERSI_STIPENDI in fase di emissione mandati stipendi
     * <p><b>chiave_primaria: TERZO_SPECIALE</b>
     * <p><b>chiave_secondaria: DIVERSI_STIPENDI</b>
     *
     * @throws PersistencyException
     */
    public Integer getCdTerzoDiversiStipendi() throws PersistencyException {
        return Optional.ofNullable(
                        this.getConfigurazione(null,Configurazione_cnrBulk.PK_TERZO_SPECIALE, Configurazione_cnrBulk.SK_DIVERSI_STIPENDI))
                .map(Configurazione_cnrBulk::getIm01)
                .map(BigDecimal::intValue)
                .orElse(null);
    }


    /**
     * Ritorna il codice bollo da utilizzare per la genersazione dei madati stipendi
     * <p><b>chiave_primaria: STIPENDI</b>
     * <p><b>chiave_secondaria: CODICE_BOLLO</b>
     *
     * @throws PersistencyException
     */
    public String getCodiceBolloStipendi() throws PersistencyException {
        return Optional.ofNullable(
                        this.getConfigurazione(null,Configurazione_cnrBulk.PK_STIPENDI, Configurazione_cnrBulk.SK_CODICE_BOLLO))
                .map(Configurazione_cnrBulk::getVal01)
                .orElse(null);
    }

    public Timestamp getDataFineValiditaCaricoFamiliare(String tiPersona) throws PersistencyException {
        return Optional.ofNullable(
                this.getConfigurazione(null,Configurazione_cnrBulk.PK_BLOCCO_DETRAZIONI, tiPersona))
                .map(Configurazione_cnrBulk::getDt01)
                .orElse(null);
    }

}
