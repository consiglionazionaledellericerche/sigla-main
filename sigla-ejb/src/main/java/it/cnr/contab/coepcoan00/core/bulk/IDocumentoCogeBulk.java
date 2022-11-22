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

import it.cnr.contab.docamm00.docs.bulk.TipoDocumentoEnum;
import it.cnr.jada.persistency.KeyedPersistent;

import java.sql.Timestamp;

/**
 * Questa classe è utilizzata per gestire tutti i documenti che generano scritture prima nota.
 * Sia i documenti Amministrativi che Contabili valorizzano la chiave cd_tipo_doc, cd_cds, cd_uo, esercizio, pg_doc
 * Il documento di tipo Liquidazione (cd_tipo_doc=LIQUID_IVA) valorizza cd_tipo_doc, cd_cds, cd_uo, esercizio, dtInizioLiquid, dtFineLiquid, tipoLiquid, reportIdLiquid
 * Creation date: (4/17/2002 5:49:25 PM)
 * @author: Roberto Peli
 */
public interface IDocumentoCogeBulk extends KeyedPersistent{
    String getCd_tipo_doc();

    String getCd_cds();

    String getCd_uo();

    Integer getEsercizio();

    Long getPg_doc();

    Timestamp getDtInizioLiquid();

    Timestamp getDtFineLiquid();

    String getTipoLiquid();

    Long getReportIdLiquid();

    TipoDocumentoEnum getTipoDocumentoEnum();

    Scrittura_partita_doppiaBulk getScrittura_partita_doppia();

    void setScrittura_partita_doppia(Scrittura_partita_doppiaBulk scrittura_partita_doppia);

    java.sql.Timestamp getDt_contabilizzazione();

    void setStato_coge(java.lang.String stato_coge);

    java.lang.String getStato_coge();

    int primaryKeyHashCode();
}