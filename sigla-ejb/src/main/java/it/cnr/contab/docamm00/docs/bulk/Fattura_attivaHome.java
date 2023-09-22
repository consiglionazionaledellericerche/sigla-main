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

package it.cnr.contab.docamm00.docs.bulk;

/**
 * Insert the type's description here.
 * Creation date: (9/5/2001 5:02:18 PM)
 *
 * @author: Ardire Alfonso
 */

import it.cnr.contab.docamm00.service.DocumentiCollegatiDocAmmService;
import it.cnr.contab.docamm00.storage.StorageFolderFatturaAttiva;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.si.spring.storage.StorageObject;

import java.util.List;

public class Fattura_attivaHome extends BulkHome {
    protected Fattura_attivaHome(Class clazz, java.sql.Connection connection) {
        super(clazz, connection);
    }

    protected Fattura_attivaHome(Class clazz, java.sql.Connection connection, PersistentCache persistentCache) {
        super(clazz, connection, persistentCache);
    }

    public Fattura_attivaHome(java.sql.Connection conn) {
        super(Fattura_attivaBulk.class, conn);
    }

    public Fattura_attivaHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(Fattura_attivaBulk.class, conn, persistentCache);
    }

    /**
     * Inizializza la chiave primaria di un OggettoBulk per un
     * inserimento. Da usare principalmente per riempire i progressivi
     * automatici.
     * @param fattura l'OggettoBulk da inizializzare
     */
    public java.sql.Timestamp findForMaxDataRegistrazione(it.cnr.jada.UserContext userContext, Fattura_attivaBulk fattura) throws PersistencyException, it.cnr.jada.comp.ComponentException {

        if (fattura == null) return null;
        try {
            java.sql.Connection contact = getConnection();
            String query = "SELECT MAX(DT_REGISTRAZIONE) FROM " +
                    it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + "FATTURA_ATTIVA " +
                    "WHERE CD_UO_ORIGINE= '" + fattura.getCd_uo_origine() + "'";

            if (fattura.getSezionale() != null && fattura.getSezionale().getCd_tipo_sezionale() != null)
                query = query + "AND CD_TIPO_SEZIONALE= '" + fattura.getSezionale().getCd_tipo_sezionale() + "' ";

            java.sql.ResultSet rs = contact.createStatement().executeQuery(query);

            if (rs.next())
                return rs.getTimestamp(1);
            else
                return null;
        } catch (java.sql.SQLException sqle) {
            throw new PersistencyException(sqle);
        }
    }

    public List<Fattura_attivaBulk> find(UserContext userContext, Fattura_attiva_IBulk fattura_attiva_iBulk) throws PersistencyException {
        SQLBuilder sql = createSQLBuilder();
        sql.addClause(fattura_attiva_iBulk.buildFindClauses(true));
        return fetchAll(sql);
    }

    public List<Fattura_attivaBulk> findFattureInviateSenzaNomeFile(UserContext userContext, Fattura_attiva_IBulk fattura_attiva_iBulk) throws PersistencyException {
        SQLBuilder sql = createSQLBuilder();
        sql.addClause(fattura_attiva_iBulk.buildFindClauses(true));
        sql.addSQLClause(FindClause.AND, "STATO_INVIO_SDI", SQLBuilder.EQUALS, VDocammElettroniciAttiviBulk.FATT_ELETT_INVIATA_SDI);
        sql.addSQLClause(FindClause.AND, "NOME_FILE_INVIO_SDI", SQLBuilder.ISNULL, null);
        return fetchAll(sql);
    }

    @Override
    public void update(Persistent persistent, UserContext userContext) throws PersistencyException {
        super.update(persistent, userContext);
        Fattura_attivaBulk fattura = (Fattura_attivaBulk) persistent;
        aggiornaMetadatiFattura(fattura);
    }

    public void aggiornaMetadatiFattura(Fattura_attivaBulk fattura) {
        DocumentiCollegatiDocAmmService documentiCollegatiDocAmmService = SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class);
        StorageObject so = documentiCollegatiDocAmmService.recuperoFolderFatturaByPath(fattura);
        if (so != null){
            documentiCollegatiDocAmmService.updateMetadataFromBulk(so, new StorageFolderFatturaAttiva(fattura));
        }
    }
}
