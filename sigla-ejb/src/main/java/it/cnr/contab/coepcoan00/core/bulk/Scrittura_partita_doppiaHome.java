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

package it.cnr.contab.coepcoan00.core.bulk;

import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Scrittura_partita_doppiaHome extends BulkHome {
    public Scrittura_partita_doppiaHome(java.sql.Connection conn) {
        super(Scrittura_partita_doppiaBulk.class, conn);
    }

    public Scrittura_partita_doppiaHome(java.sql.Connection conn, PersistentCache persistentCache) {
        super(Scrittura_partita_doppiaBulk.class, conn, persistentCache);
    }

    public Collection findMovimentiAvereColl(UserContext userContext, Scrittura_partita_doppiaBulk scrittura) throws PersistencyException {
        SQLBuilder sql = getHomeCache().getHome(Movimento_cogeBulk.class).createSQLBuilder();
        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, scrittura.getEsercizio());
        sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, scrittura.getCd_cds());
        sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, scrittura.getCd_unita_organizzativa());
        sql.addClause("AND", "pg_scrittura", SQLBuilder.EQUALS, scrittura.getPg_scrittura());
        sql.addClause("AND", "sezione", SQLBuilder.EQUALS, Movimento_cogeBulk.SEZIONE_AVERE);
        List result = getHomeCache().getHome(Movimento_cogeBulk.class).fetchAll(sql);
        getHomeCache().fetchAll(userContext);
        return result;
    }

    public Collection findMovimentiDareColl(UserContext userContext, Scrittura_partita_doppiaBulk scrittura) throws PersistencyException {
        SQLBuilder sql = getHomeCache().getHome(Movimento_cogeBulk.class).createSQLBuilder();
        sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, scrittura.getEsercizio());
        sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, scrittura.getCd_cds());
        sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, scrittura.getCd_unita_organizzativa());
        sql.addClause("AND", "pg_scrittura", SQLBuilder.EQUALS, scrittura.getPg_scrittura());
        sql.addClause("AND", "sezione", SQLBuilder.EQUALS, Movimento_cogeBulk.SEZIONE_DARE);
        List result = getHomeCache().getHome(Movimento_cogeBulk.class).fetchAll(sql);
        getHomeCache().fetchAll(userContext);
        return result;
    }

    public Optional<Scrittura_partita_doppiaBulk> findByDocumentoAmministrativo(IDocumentoAmministrativoBulk documentoAmministrativoBulk) throws PersistencyException {
		SQLBuilder sql = this.createSQLBuilder();
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, documentoAmministrativoBulk.getEsercizio());
		sql.addClause("AND", "cd_cds_documento", SQLBuilder.EQUALS, documentoAmministrativoBulk.getCd_cds());
		sql.addClause("AND", "cd_uo_documento", SQLBuilder.EQUALS, documentoAmministrativoBulk.getCd_uo());
		sql.addClause("AND", "pg_numero_documento", SQLBuilder.EQUALS, documentoAmministrativoBulk.getPg_doc_amm());
		sql.addClause("AND", "cd_tipo_documento", SQLBuilder.EQUALS, documentoAmministrativoBulk.getCd_tipo_doc_amm());
		return fetchAll(sql)
				.stream()
				.filter(Scrittura_partita_doppiaBulk.class::isInstance)
				.map(Scrittura_partita_doppiaBulk.class::cast)
				.findAny();
	}
    /**
     * Imposta il pg_scrittura di un oggetto <code>Scrittura_partita_doppiaBulk</code>.
     *
     * @param bulk <code>OggettoBulk</code>
     * @throws PersistencyException
     */

    public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws ComponentException {
        try {
            Scrittura_partita_doppiaBulk scrittura = (Scrittura_partita_doppiaBulk) bulk;

            LoggableStatement cs = new LoggableStatement(getConnection(),
                    "{ ? = call " +
                            it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
                            "CNRCTB200.getNextProgressivo(?, ?, ?, ?, ?)}", false, this.getClass());
            try {
                cs.registerOutParameter(1, java.sql.Types.NUMERIC);
                cs.setObject(2, scrittura.getEsercizio());
                cs.setString(3, scrittura.getCd_cds());
                cs.setString(4, scrittura.getCd_unita_organizzativa());
                cs.setString(5, Scrittura_partita_doppiaBulk.TIPO_COGE);
                cs.setString(6, scrittura.getUser());
                cs.executeQuery();

                Long result = new Long(cs.getLong(1));
                scrittura.setPg_scrittura(result);
            } catch (java.lang.Exception e) {
                throw new ComponentException(e);
            } finally {
                cs.close();
            }
        } catch (java.lang.Exception e) {
            throw new ComponentException(e);
        }
    }
}
