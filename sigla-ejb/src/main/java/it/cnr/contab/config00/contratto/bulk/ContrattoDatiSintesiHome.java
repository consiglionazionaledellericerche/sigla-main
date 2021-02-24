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

package it.cnr.contab.config00.contratto.bulk;


import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class ContrattoDatiSintesiHome extends BulkHome {
    public ContrattoDatiSintesiHome(Class clazz, java.sql.Connection conn) {
        super(clazz, conn);
    }

    public ContrattoDatiSintesiHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
        super(clazz, conn, persistentCache);
    }

    public ContrattoDatiSintesiHome(java.sql.Connection conn) {
        super(ContrattoDatiSintesiBulk.class, conn);
    }

    public ContrattoDatiSintesiHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
        super(ContrattoDatiSintesiBulk.class, conn, persistentCache);
    }

    public List<ContrattoDatiSintesiBulk> recuperoDati(UserContext userContext, ContrattoDatiSintesiBulk contrattoDatiSintesiBulk, String natura, Integer cdTerzo, String uo) throws ComponentException, PersistencyException {
        SQLBuilder sql = createSQLBuilder();
        Optional.ofNullable(contrattoDatiSintesiBulk)
                .ifPresent(contrattoDatiSintesiBulk1 -> sql.addClause(contrattoDatiSintesiBulk1.buildFindClauses(true)));

        if (ContrattoBulk.NATURA_CONTABILE_PASSIVO.equals(natura)){
            sql.openParenthesis("AND");
            sql.addSQLClause("AND","NATURA_CONTABILE",SQLBuilder.EQUALS, ContrattoBulk.NATURA_CONTABILE_PASSIVO);
            sql.addSQLClause("OR","NATURA_CONTABILE",SQLBuilder.EQUALS, ContrattoBulk.NATURA_CONTABILE_ATTIVO_E_PASSIVO);
            sql.closeParenthesis();
        } if (ContrattoBulk.NATURA_CONTABILE_ATTIVO.equals(natura)){
            sql.openParenthesis("AND");
            sql.addSQLClause("AND","NATURA_CONTABILE",SQLBuilder.EQUALS, ContrattoBulk.NATURA_CONTABILE_ATTIVO);
            sql.addSQLClause("OR","NATURA_CONTABILE",SQLBuilder.EQUALS, ContrattoBulk.NATURA_CONTABILE_ATTIVO_E_PASSIVO);
            sql.closeParenthesis();
        }
        sql.addSQLClause("AND", "STATO", sql.EQUALS, ContrattoBulk.STATO_DEFINITIVO);

        sql.openParenthesis("AND");
        sql.addSQLClause("AND","CONTRATTO.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,uo);
        SQLBuilder sqlAssUo = getHomeCache().getHome(Ass_contratto_uoBulk.class).createSQLBuilder();
        sqlAssUo.addSQLJoin("CONTRATTO.ESERCIZIO","ASS_CONTRATTO_UO.ESERCIZIO");
        sqlAssUo.addSQLJoin("CONTRATTO.PG_CONTRATTO","ASS_CONTRATTO_UO.PG_CONTRATTO");
        sqlAssUo.addSQLClause("AND","ASS_CONTRATTO_UO.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,uo);
        sql.addSQLExistsClause("OR",sqlAssUo);
        sql.closeParenthesis();
        sql.addTableToHeader("TERZO");
        sql.addSQLJoin("CONTRATTO.FIG_GIUR_EST", SQLBuilder.EQUALS,"TERZO.CD_TERZO");
        sql.addSQLClause("AND","TERZO.DT_FINE_RAPPORTO",SQLBuilder.ISNULL,null);

        if (cdTerzo != null ){
            sql.openParenthesis("AND");
            sql.openParenthesis("AND");
            sql.addSQLClause(FindClause.AND, "FIG_GIUR_EST",SQLBuilder.EQUALS,cdTerzo);

            TerzoHome terzoHome = (TerzoHome) getHomeCache().getHome(TerzoBulk.class);
            TerzoBulk terzoBulk = new TerzoBulk(cdTerzo);
            terzoBulk = (TerzoBulk)terzoHome.findByPrimaryKey(terzoBulk);
            if (terzoBulk == null){
                throw new ApplicationException("Codice Terzo "+ cdTerzo+" non esistente");
            }
            AnagraficoHome anagraficoHome = (AnagraficoHome) getHomeCache().getHome(AnagraficoBulk.class);

            sql.closeParenthesis();
            try {
                for (Iterator<Anagrafico_terzoBulk> i = anagraficoHome.findAssociatiStudio(terzoBulk.getCd_anag()).iterator(); i.hasNext();) {
                    sql.openParenthesis("OR");
                    Anagrafico_terzoBulk associato = i.next();
                    sql.addSQLClause("OR", "CONTRATTO.FIG_GIUR_EST",SQLBuilder.EQUALS, associato.getCd_terzo());
                    sql.closeParenthesis();
                }
            } catch (IntrospectionException e) {
                throw new ApplicationException(e);
            }
            sql.closeParenthesis();
        }
        List<ContrattoDatiSintesiBulk> lista = fetchAll(sql);
        return  lista.stream().sorted(Comparator.comparingLong(ContrattoDatiSintesiBulk::getEsercizio)).sorted(Comparator.comparingLong(ContrattoDatiSintesiBulk::getPg_contratto)).collect(Collectors.toList());
    }
}
