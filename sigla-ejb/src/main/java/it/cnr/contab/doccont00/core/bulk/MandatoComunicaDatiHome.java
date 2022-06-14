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


import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.ordmag.anag00.TipoOperazioneOrdBulk;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoRigaMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<MandatoComunicaDatiBulk> recuperoDati(UserContext userContext, MandatoComunicaDatiBulk mandatoComunicaDatiBulk, Timestamp daData, Timestamp aData) throws ComponentException, PersistencyException {
        SQLBuilder sql = createSQLBuilder();
        Optional.ofNullable(mandatoComunicaDatiBulk)
                .ifPresent(mandatoComunicaDatiBulk1 -> sql.addClause(mandatoComunicaDatiBulk1.buildFindClauses(true)));

        sql.resetColumns();
        sql.addColumn("mandato.cd_cds");
        sql.addColumn("mandato.ESERCIZIO");
        sql.addColumn("mandato.pg_mandato");
        sql.addColumn("dt_pagamento");
        sql.addColumn("v_mandato_reversale_voce.IM_CAPITOLO_PESATO","IMPORTO");
        sql.addColumn("terzo.denominazione_sede","denominazioneSede");
        sql.addColumn("cd_liv4","cdLiv4");
        sql.addColumn("ds_liv4","dsLiv4");

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

        sql.addTableToHeader("ELEMENTO_VOCE");
        sql.addSQLJoin("v_mandato_reversale_voce.esercizio","ELEMENTO_VOCE.ESERCIZIO");
        sql.addSQLJoin("v_mandato_reversale_voce.ti_gestione","ELEMENTO_VOCE.ti_gestione");
        sql.addSQLJoin("v_mandato_reversale_voce.TI_APPARTENENZA","ELEMENTO_VOCE.TI_APPARTENENZA");
        sql.addSQLJoin("v_mandato_reversale_voce.CD_VOCE","ELEMENTO_VOCE.CD_ELEMENTO_VOCE");

        sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL");
        sql.addSQLJoin("v_mandato_reversale_voce.esercizio","V_CLASSIFICAZIONE_VOCI_ALL.esercizio");
        sql.addSQLJoin("v_mandato_reversale_voce.ti_gestione","V_CLASSIFICAZIONE_VOCI_ALL.ti_gestione");
        sql.addSQLJoin("v_mandato_reversale_voce.cd_voce","V_CLASSIFICAZIONE_VOCI_ALL.cd_livello6");

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(aData.getTime());
        int anno = cal.get(Calendar.YEAR);

        sql.openParenthesis(FindClause.AND);
        sql.openParenthesis(FindClause.AND);
        sql.addSQLClause("AND", "dt_pagamento", SQLBuilder.GREATER_EQUALS, daData );
        sql.addSQLClause("AND", "dt_pagamento", SQLBuilder.LESS_EQUALS, aData );
        sql.closeParenthesis();
        sql.openParenthesis(FindClause.OR);
        sql.addSQLClause("AND", "dt_pagamento", SQLBuilder.GREATER, aData );
        sql.addSQLClause("AND", "ti_mandato", SQLBuilder.EQUALS, MandatoBulk.TIPO_REGOLAM_SOSPESO );
        sql.addSQLClause("AND", "mandato.esercizio", SQLBuilder.EQUALS, anno);
        sql.closeParenthesis();
        sql.closeParenthesis();

        sql.addSQLClause("AND", "mandato.stato", SQLBuilder.EQUALS, MandatoBulk.STATO_MANDATO_PAGATO );
        sql.addSQLClause("AND", "mandato.esercizio", SQLBuilder.GREATER_EQUALS, MandatoComunicaDatiBulk.ANNO_INIZIO_PUBBLICAZIONE );
        sql.addSQLClause("AND", "ti_documento", SQLBuilder.EQUALS, "M" );
        sql.addSQLClause("AND", "FL_COMUNICA_PAGAMENTI", SQLBuilder.EQUALS, "Y" );

        PersistentHome rigaHome = getHomeCache().getHome(Mandato_rigaBulk.class);
        SQLBuilder sqlCompensoExist = rigaHome.createSQLBuilder();

        sqlCompensoExist.addTableToHeader("COMPENSO");
        sqlCompensoExist.addSQLJoin("mandato.CD_CDS", "mandato_riga.CD_CDS");
        sqlCompensoExist.addSQLJoin("mandato.ESERCIZIO", "mandato_riga.ESERCIZIO");
        sqlCompensoExist.addSQLJoin("mandato.pg_mandato", "mandato_riga.pg_mandato");
        sqlCompensoExist.addSQLJoin("compenso.CD_CDS", "mandato_riga.CD_CDS_DOC_AMM");
        sqlCompensoExist.addSQLJoin("compenso.CD_UNITA_ORGANIZZATIVA", "mandato_riga.CD_UO_DOC_AMM");
        sqlCompensoExist.addSQLJoin("compenso.ESERCIZIO", "mandato_riga.ESERCIZIO_DOC_AMM");
        sqlCompensoExist.addSQLJoin("compenso.pg_COMPENSO", "mandato_riga.pg_DOC_AMM");
        sqlCompensoExist.addSQLClause("AND", "COMPENSO.TI_ANAGRAFICO", SQLBuilder.EQUALS, MissioneBulk.ANAG_DIPENDENTE);
        sqlCompensoExist.addSQLClause("AND", "mandato_riga.cd_tipo_documento_amm", SQLBuilder.EQUALS, Numerazione_doc_ammBulk.TIPO_COMPENSO);

        sql.addSQLNotExistsClause(FindClause.AND, sqlCompensoExist);

        SQLBuilder sqlStipendiExist = rigaHome.createSQLBuilder();

        sqlStipendiExist.addSQLJoin("mandato.CD_CDS", "mandato_riga.CD_CDS");
        sqlStipendiExist.addSQLJoin("mandato.ESERCIZIO", "mandato_riga.ESERCIZIO");
        sqlStipendiExist.addSQLJoin("mandato.pg_mandato", "mandato_riga.pg_mandato");
        sqlStipendiExist.addSQLClause("AND", "mandato_riga.cd_tipo_documento_amm", SQLBuilder.EQUALS, "GEN_STIP_S");

        sql.addSQLNotExistsClause(FindClause.AND, sqlStipendiExist);

        SQLBuilder sqlMissioneExist = rigaHome.createSQLBuilder();

        sqlMissioneExist.addTableToHeader("MISSIONE");
        sqlMissioneExist.addSQLJoin("mandato.CD_CDS", "mandato_riga.CD_CDS");
        sqlMissioneExist.addSQLJoin("mandato.ESERCIZIO", "mandato_riga.ESERCIZIO");
        sqlMissioneExist.addSQLJoin("mandato.pg_mandato", "mandato_riga.pg_mandato");
        sqlMissioneExist.addSQLJoin("missione.CD_CDS", "mandato_riga.CD_CDS_DOC_AMM");
        sqlMissioneExist.addSQLJoin("missione.CD_UNITA_ORGANIZZATIVA", "mandato_riga.CD_UO_DOC_AMM");
        sqlMissioneExist.addSQLJoin("missione.ESERCIZIO", "mandato_riga.ESERCIZIO_DOC_AMM");
        sqlMissioneExist.addSQLJoin("missione.pg_missione", "mandato_riga.pg_DOC_AMM");
        sqlMissioneExist.addSQLClause("AND", "missione.TI_ANAGRAFICO", SQLBuilder.EQUALS, MissioneBulk.ANAG_DIPENDENTE);
        sqlMissioneExist.addSQLClause("AND", "mandato_riga.cd_tipo_documento_amm", SQLBuilder.EQUALS, Numerazione_doc_ammBulk.TIPO_MISSIONE);

        sql.addSQLNotExistsClause(FindClause.AND, sqlMissioneExist);

        SQLBuilder sqlAnticipoExist = rigaHome.createSQLBuilder();

        sqlAnticipoExist.addTableToHeader("ANTICIPO");
        sqlAnticipoExist.addSQLJoin("mandato.CD_CDS", "mandato_riga.CD_CDS");
        sqlAnticipoExist.addSQLJoin("mandato.ESERCIZIO", "mandato_riga.ESERCIZIO");
        sqlAnticipoExist.addSQLJoin("mandato.pg_mandato", "mandato_riga.pg_mandato");
        sqlAnticipoExist.addSQLJoin("ANTICIPO.CD_CDS", "mandato_riga.CD_CDS_DOC_AMM");
        sqlAnticipoExist.addSQLJoin("ANTICIPO.CD_UNITA_ORGANIZZATIVA", "mandato_riga.CD_UO_DOC_AMM");
        sqlAnticipoExist.addSQLJoin("ANTICIPO.ESERCIZIO", "mandato_riga.ESERCIZIO_DOC_AMM");
        sqlAnticipoExist.addSQLJoin("ANTICIPO.pg_ANTICIPO", "mandato_riga.pg_DOC_AMM");
        sqlAnticipoExist.addSQLClause("AND", "ANTICIPO.TI_ANAGRAFICO", SQLBuilder.EQUALS, MissioneBulk.ANAG_DIPENDENTE);
        sqlAnticipoExist.addSQLClause("AND", "mandato_riga.cd_tipo_documento_amm", SQLBuilder.EQUALS, Numerazione_doc_ammBulk.TIPO_ANTICIPO);

        sql.addSQLNotExistsClause(FindClause.AND, sqlAnticipoExist);
        List<MandatoComunicaDatiBulk> lista = fetchAll(sql);
        Map<MandatoComunicaDatiBulk, List<MandatoComunicaDatiBulk>> map = lista.stream().collect(
                Collectors.groupingBy(
                        list -> new MandatoComunicaDatiBulk(list.getCd_cds(), list.getEsercizio(), list.getPg_mandato(),
                                                            getYear(list.getDt_pagamento()).compareTo(list.getEsercizio())== 0 ? list.getDt_pagamento() : lastDateOfTheYear(list.getEsercizio()),
                                                            "U."+list.getCdLiv4().substring(1), list.getDsLiv4(),
                                                            list.getDenominazioneSede())));
        map.keySet().stream().forEach(comunica -> comunica.setImporto(map.get(comunica).stream().map(el->Optional.ofNullable(el.getImporto()).orElse(BigDecimal.ZERO)).reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO)));
        List<MandatoComunicaDatiBulk> listaComunicaDati =  map.keySet().stream().sorted(Comparator.comparingLong(MandatoComunicaDatiBulk::getEsercizio)).sorted(Comparator.comparingLong(MandatoComunicaDatiBulk::getPg_mandato)).collect(Collectors.toList());
        return listaComunicaDati;
    }

    private Integer getYear(Timestamp data){
        long timestamp = data.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return cal.get(Calendar.YEAR);

    }

    private Timestamp lastDateOfTheYear(Integer year) {
        GregorianCalendar data = (GregorianCalendar)GregorianCalendar.getInstance();
        data.setTime((new GregorianCalendar(year, 11, 31)).getTime());
        return new Timestamp(data.getTimeInMillis());
    }
}
