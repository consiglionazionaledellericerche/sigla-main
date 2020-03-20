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


import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.sto.bulk.EnteBulk;
import it.cnr.contab.consultazioni.bulk.ConsultazioniRestHome;
import it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;

import java.util.Collection;
import java.util.List;

public class MandatoComunicaDatiHome extends MandatoHome {
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

    @Override
    public Collection findMandato_riga(UserContext userContext, MandatoBulk mandato) throws PersistencyException, IntrospectionException {
        return null;
    }

    @Override
    public Mandato_terzoBulk findMandato_terzo(UserContext userContext, MandatoBulk mandato) throws PersistencyException, IntrospectionException {
        return null;
    }

    public List recuperoDati(UserContext userContext) throws ComponentException, PersistencyException {
        SQLBuilder sql = getHomeCache().getHome(MandatoComunicaDatiBulk.class, "COMUNICA_DATI_MANDATO").createSQLBuilder();
        sql.resetColumns();
        sql.addColumn("mandato.ESERCIZIO");
        sql.addColumn("mandato.pg_mandato");
        sql.addColumn("decode(to_char(dt_pagamento,'yyyy'),mandato.esercizio,dt_pagamento,to_date('31/12/'||mandato.esercizio,'dd/mm/yyyy'))", "dt_pagamento");
        sql.addColumn("terzo.denominazione_sede","denominazioneSede");
        sql.addColumn("mandato.im_mandato");
        sql.addColumn("mandato.im_mandato-mandato.im_ritenute","IM_RITENUTE");
        sql.addColumn("NVL(SUM(v_mandato_reversale_voce.IM_CAPITOLO_PESATO),0)","importoCapitolo");
        sql.addColumn("'U.'||substr(cd_liv4,2)","cdLiv4");
        sql.addColumn("ds_liv4","dsLiv4");
        sql.addSQLGroupBy("mandato.ESERCIZIO");
        sql.addSQLGroupBy("mandato.pg_mandato");
        sql.addSQLGroupBy("decode(to_char(dt_pagamento,'yyyy'),mandato.esercizio,dt_pagamento,to_date('31/12/'||mandato.esercizio,'dd/mm/yyyy'))");
        sql.addSQLGroupBy("terzo.denominazione_sede");
        sql.addSQLGroupBy("mandato.im_mandato");
        sql.addSQLGroupBy("mandato.im_mandato-mandato.im_ritenute");
        sql.addSQLGroupBy("'U.'||substr(cd_liv4,2)");
        sql.addSQLGroupBy("ds_liv4");

        sql.addTableToHeader("mandato_terzo");
        sql.addSQLJoin("mandato.cd_cds","mandato_terzo.cd_cds");
        sql.addSQLJoin("mandato.esercizio","mandato_terzo.esercizio");
        sql.addSQLJoin("mandato.pg_mandato","mandato_terzo.pg_mandato");
        sql.addTableToHeader("terzo");
        sql.addSQLJoin("terzo.cd_terzo","mandato_terzo.cd_terzo");
        sql.addTableToHeader("v_mandato_reversale_voce");
        sql.addSQLJoin("mandato.cd_cds","v_mandato_reversale_voce.cd_cds");
        sql.addSQLJoin("mandato.esercizio","v_mandato_reversale_voce.esercizio");
        sql.addSQLJoin("mandato.pg_mandato","v_mandato_reversale_voce.pg_documento");
        sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL");
        sql.addSQLJoin("v_mandato_reversale_voce.esercizio","V_CLASSIFICAZIONE_VOCI_ALL.esercizio");
        sql.addSQLJoin("v_mandato_reversale_voce.ti_gestione","V_CLASSIFICAZIONE_VOCI_ALL.ti_gestione");
        sql.addSQLJoin("v_mandato_reversale_voce.cd_voce","V_CLASSIFICAZIONE_VOCI_ALL.cd_livello6");
/*
        and ((dt_pagamento >= to_date ('01102016','ddmmyyyy') and dt_pagamento <= to_date('31122016','ddmmyyyy'))  or

                (dt_pagamento >= to_date ('31122016','ddmmyyyy') and to_char(dt_pagamento,'yyyy') >mandato.esercizio) )
        and mandato.stato ='P'

        AND (mandato.cd_cds = mandato_terzo.cd_cds)

        AND (mandato.esercizio = mandato_terzo.esercizio)

        AND (mandato.pg_mandato = mandato_terzo.pg_mandato)

        AND (v_mandato_reversale_voce.cd_cds = mandato.cd_cds)

        AND (v_mandato_reversale_voce.esercizio = mandato.esercizio)

        AND (v_mandato_reversale_voce.pg_documento = mandato.pg_mandato)

        AND (terzo.cd_terzo = mandato_terzo.cd_terzo)

       ) and v_mandato_reversale_voce.esercizio >=2016   AND TI_DOCUMENTO = 'M' and ti_mandato in('P','S')

        and

                ((cd_livello6 like  '10%'  and cd_livello6 not in('10001','10002','10003','10004','10005','10014')) or

                (cd_livello6 like  '11%'  and cd_livello6  not in('11023','11024','11025','11027')) or

                (cd_livello6  like  '13%'  and cd_livello6 not in('13027','13028','13029','13030','13031','13032','13033','13034','13035','13036','13039',

                '13108','13109','13110','13111','13118','13121'))  or

                (cd_livello6 in('14005','14023' )) or

                (cd_livello6  like  '22%'))

        and not exists (select 1   FROM  pcir009.mandato_riga,pcir009.compenso

                WHERE

                mandato.cd_cds = mandato_riga.cd_cds

                AND mandato.esercizio = mandato_riga.esercizio

                AND mandato.pg_mandato = mandato_riga.pg_mandato

                and mandato_riga.cd_tipo_documento_amm ='GEN_STIP_S')

        and not exists (select 1   FROM  pcir009.mandato_riga,pcir009.compenso

                WHERE

                mandato.cd_cds = mandato_riga.cd_cds

                AND mandato.esercizio = mandato_riga.esercizio

                AND mandato.pg_mandato = mandato_riga.pg_mandato

                and mandato_riga.cd_tipo_documento_amm ='COMPENSO'

                AND compenso.cd_cds = mandato_riga.cd_cds_doc_amm

                AND compenso.cd_unita_organizzativa = mandato_riga.cd_uo_doc_amm

                AND compenso.esercizio = mandato_riga.esercizio_doc_amm

                AND compenso.pg_compenso = mandato_riga.pg_doc_amm

                and compenso.ti_anagrafico='D'

        )

        and not exists (select 1   FROM  pcir009.mandato_riga,pcir009.missione

                WHERE

                mandato.cd_cds = mandato_riga.cd_cds

                AND mandato.esercizio = mandato_riga.esercizio

                AND mandato.pg_mandato = mandato_riga.pg_mandato

                and mandato_riga.cd_tipo_documento_amm ='MISSIONE'

                AND missione.cd_cds = mandato_riga.cd_cds_doc_amm

                AND missione.cd_unita_organizzativa = mandato_riga.cd_uo_doc_amm

                AND missione.esercizio = mandato_riga.esercizio_doc_amm

                AND missione.pg_missione = mandato_riga.pg_doc_amm

                and missione.ti_anagrafico='D'

        )

        and not exists (select 1   FROM  pcir009.mandato_riga,pcir009.anticipo

                WHERE

                mandato.cd_cds = mandato_riga.cd_cds

                AND mandato.esercizio = mandato_riga.esercizio

                AND mandato.pg_mandato = mandato_riga.pg_mandato

                and mandato_riga.cd_tipo_documento_amm ='ANTICIPO'

                AND anticipo.cd_cds = mandato_riga.cd_cds_doc_amm

                AND anticipo.cd_unita_organizzativa = mandato_riga.cd_uo_doc_amm

                AND anticipo.esercizio = mandato_riga.esercizio_doc_amm

                AND anticipo.pg_anticipo = mandato_riga.pg_doc_amm

                and anticipo.ti_anagrafico='D'

        ) and mandato.esercizio =2016*/
// TODO Da togliere
        sql.addSQLClause("AND","mandato.ESERCIZIO",SQLBuilder.EQUALS,new Integer("2016"));
        sql.addSQLClause("AND","mandato.pg_mandato",SQLBuilder.LESS_EQUALS,new Long("10"));
        sql.addSQLClause("AND","mandato.CD_CDS",SQLBuilder.EQUALS,"099");
// TODO Fine Da togliere

        return fetchAll(sql);
    }

}
