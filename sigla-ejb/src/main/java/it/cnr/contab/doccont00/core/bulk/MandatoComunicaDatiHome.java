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

package it.cnr.contab.doccont00.core.bulk;


import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class MandatoComunicaDatiHome extends BulkHome {
    public MandatoComunicaDatiHome(Class clazz, java.sql.Connection conn) {
        super(clazz, conn);
    }

    public MandatoComunicaDatiHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
        super(clazz, conn, persistentCache);
    }

    public MandatoComunicaDatiHome(java.sql.Connection conn) {
        super(MandatoComunicaDatiBulk.class, conn);
    }

    public MandatoComunicaDatiHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
        super(MandatoComunicaDatiBulk.class, conn, persistentCache);
    }

    public List<MandatoComunicaDatiBulk> recuperoDati(UserContext userContext, MandatoComunicaDatiBulk mandatoComunicaDatiBulk) throws ComponentException, PersistencyException {
        SQLBuilder sql = createSQLBuilder();
        Optional.ofNullable(mandatoComunicaDatiBulk)
                .ifPresent(mandatoComunicaDatiBulk1 -> sql.addClause(mandatoComunicaDatiBulk1.buildFindClauses(true)));

        sql.addTableToHeader("MANDATO_TERZO");
        sql.addSQLJoin("mandato.cd_cds","mandato_terzo.cd_cds");
        sql.addSQLJoin("mandato.esercizio","mandato_terzo.esercizio");
        sql.addSQLJoin("mandato.pg_mandato","mandato_terzo.pg_mandato");

        sql.addTableToHeader("TERZO");
        sql.addSQLJoin("terzo.cd_terzo","mandato_terzo.cd_terzo");

        sql.addTableToHeader("V_MANDATO_REVERSALE_VOCE");
        sql.addSQLJoin("mandato.cd_cds","v_mandato_reversale_voce.cd_cds");
        sql.addSQLJoin("mandato.esercizio","v_mandato_reversale_voce.esercizio");
        sql.addSQLJoin("mandato.pg_mandato","v_mandato_reversale_voce.pg_documento");

        sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL");
        sql.addSQLJoin("v_mandato_reversale_voce.esercizio","V_CLASSIFICAZIONE_VOCI_ALL.esercizio");
        sql.addSQLJoin("v_mandato_reversale_voce.ti_gestione","V_CLASSIFICAZIONE_VOCI_ALL.ti_gestione");
        sql.addSQLJoin("v_mandato_reversale_voce.cd_voce","V_CLASSIFICAZIONE_VOCI_ALL.cd_livello6");

        return  fetchAll(sql);
    }

}
