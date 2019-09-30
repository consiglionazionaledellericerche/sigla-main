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

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.math.BigDecimal;
import java.util.List;

public class Configurazione_cnrHome extends BulkHome {
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

    public boolean isUOSpecialeTuttaSAC(String cdUnitaOrganizzativa) throws PersistencyException {
        SQLBuilder sql = createSQLBuilder();
        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, new Integer(0));
        sql.addClause("AND", "cd_unita_funzionale", SQLBuilder.EQUALS, "*");
        sql.addClause("AND", "cd_chiave_primaria", SQLBuilder.EQUALS, Configurazione_cnrBulk.PK_UO_SPECIALE);
        sql.addClause("AND", "cd_chiave_secondaria", SQLBuilder.EQUALS, "UO_DISTINTA_TUTTA_SAC");
        final List<Configurazione_cnrBulk> list = fetchAll(sql);
        return list.stream()
                    .filter(configurazione_cnrBulk -> configurazione_cnrBulk.getVal01().equals(cdUnitaOrganizzativa))
                    .findAny().isPresent();
    }
    
    public Configurazione_cnrBulk getConfigurazioneCnrBulk(Integer esercizio, String unita_funzionale, String chiave_primaria, String chiave_secondaria) throws PersistencyException {
        if (esercizio == null) esercizio = new Integer(0);
        if (unita_funzionale == null) unita_funzionale = ASTERISCO;
        if (chiave_secondaria == null) chiave_secondaria = ASTERISCO;
        return (Configurazione_cnrBulk) getHomeCache().getHome(Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria, chiave_secondaria, unita_funzionale, esercizio));
    }    
}
