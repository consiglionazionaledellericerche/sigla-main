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

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * Insert the type's description here.
 * Creation date: (3/22/2002 1:10:24 PM)
 *
 * @author: Roberto Peli
 */
public class Documento_amministrativo_attivoHome extends Fattura_attivaHome {
    public Documento_amministrativo_attivoHome(java.sql.Connection conn) {
        super(Documento_amministrativo_attivoBulk.class, conn);
    }

    public Documento_amministrativo_attivoHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
        super(Documento_amministrativo_attivoBulk.class, conn, persistentCache);
    }

    public SQLBuilder selectByClauseForFattureAttiveDaFirmare(UserContext context, Documento_amministrativo_attivoBulk oggettobulk, CompoundFindClause compoundfindclause) {
        SQLBuilder sqlBuilder = super.createSQLBuilder();
        sqlBuilder.addClause("AND", "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(context));
        sqlBuilder.addClause("AND", "cd_cds_origine", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(context));
        sqlBuilder.addClause("AND", "cd_uo_origine", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(context));
        sqlBuilder.addClause("AND", "flFatturaElettronica", SQLBuilder.EQUALS, Boolean.TRUE);
        sqlBuilder.addClause("AND", "statoInvioSdi", SQLBuilder.EQUALS, Fattura_attivaBulk.FATT_ELETT_PREDISPOSTA_FIRMA);
        sqlBuilder.addClause("AND", "stato_cofi", SQLBuilder.NOT_EQUALS, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk.STATO_ANNULLATO);
        return sqlBuilder;
    }
}
