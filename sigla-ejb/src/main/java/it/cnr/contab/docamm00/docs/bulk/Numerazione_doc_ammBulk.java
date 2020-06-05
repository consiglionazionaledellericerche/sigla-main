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

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;


public class Numerazione_doc_ammBulk extends Numerazione_doc_ammBase {

    public final static String TIPO_FATTURA_PASSIVA = "FATTURA_P";
    public final static String TIPO_AUTOFATTURA = "AUTOFATT";
    public final static String TIPO_LETTERA_ESTERO = "LT_ESTERO";
    public final static String TIPO_FATTURA_ATTIVA = "FATTURA_A";
    public final static String TIPO_UNIVOCO_FATTURA_ATTIVA = "FATT_A_UNI";
    public final static String TIPO_POSIZIONE_DEBITORIA_PAGOPA = "POS_DEB";
    public final static String TIPO_POSIZIONE_CREDITORIA_PAGOPA = "POS_CRED";
    public final static String TIPO_DOC_GENERICO_S = "GENERICO_S";
    public final static String TIPO_DOC_GENERICO_E = "GENERICO_E";
    public final static String TIPO_TRASF_E = "TRASF_E";
    public final static String TIPO_TRASF_S = "TRASF_S";
    public final static String TIPO_REGOLA_E = "REGOLA_E";
    public final static java.lang.String TIPO_COMPENSO = "COMPENSO";
    public final static java.lang.String TIPO_MINICARRIERA = "MINICARR";
    public final static String TIPO_MISSIONE = "MISSIONE";
    public final static String TIPO_ANTICIPO = "ANTICIPO";
    public final static String TIPO_RIMBORSO = "RIMBORSO";
    public final static String TIPO_CONGUAGLIO = "CONGUA";
    public final static String TIPO_GEN_IVA_E = "GEN_IVA_E";
    public final static String TIPO_GEN_CH_FON = "GEN_CH_FON";
    public final static String TIPO_GEN_AP_FON = "GEN_AP_FON";
    public final static String TIPO_ORDINE = "ORDINE";

    public Numerazione_doc_ammBulk() {
        super();
    }

    public Numerazione_doc_ammBulk(it.cnr.contab.compensi00.docs.bulk.CompensoBulk compenso) {
        super();

        setCd_tipo_documento_amm(TIPO_COMPENSO);

        setCd_cds(compenso.getCd_cds());
        setEsercizio(compenso.getEsercizio());
        setCd_unita_organizzativa(compenso.getCd_unita_organizzativa());
    }

    public Numerazione_doc_ammBulk(it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk conguaglio) {
        super();

        setCd_tipo_documento_amm(TIPO_CONGUAGLIO);

        setCd_cds(conguaglio.getCd_cds());
        setEsercizio(conguaglio.getEsercizio());
        setCd_unita_organizzativa(conguaglio.getCd_unita_organizzativa());
    }

    public Numerazione_doc_ammBulk(it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk carriera) {
        super();

        setCd_tipo_documento_amm(TIPO_MINICARRIERA);

        setCd_cds(carriera.getCd_cds());
        setEsercizio(carriera.getEsercizio());
        setCd_unita_organizzativa(carriera.getCd_unita_organizzativa());
    }

    public Numerazione_doc_ammBulk(AutofatturaBulk autofattura) {
        super();

        setCd_tipo_documento_amm(TIPO_AUTOFATTURA);

        setCd_cds(autofattura.getCd_cds());
        setEsercizio(autofattura.getEsercizio());
        setCd_unita_organizzativa(autofattura.getCd_unita_organizzativa());
    }

    public Numerazione_doc_ammBulk(Documento_genericoBulk documento) {
        super();

        setCd_tipo_documento_amm(documento.getCd_tipo_documento_amm());

        setCd_cds(documento.getCd_cds());
        setEsercizio(documento.getEsercizio());
        setCd_unita_organizzativa(documento.getCd_unita_organizzativa());
    }

    public Numerazione_doc_ammBulk(IDocumentoAmministrativoBulk documentoAmministrativoBulk, Unita_organizzativaBulk uoEntePerProgressivoUnivoco) {
        super();
        setEsercizio(documentoAmministrativoBulk.getEsercizio());
        setCd_cds(uoEntePerProgressivoUnivoco.getCd_cds());
        setCd_unita_organizzativa(uoEntePerProgressivoUnivoco.getCd_unita_organizzativa());
    }

    public Numerazione_doc_ammBulk(Fattura_attivaBulk fatturaAttiva) {
        super();

        setCd_tipo_documento_amm(TIPO_FATTURA_ATTIVA);

        setCd_cds(fatturaAttiva.getCd_cds());
        setEsercizio(fatturaAttiva.getEsercizio());
        setCd_unita_organizzativa(fatturaAttiva.getCd_unita_organizzativa());
    }

    public Numerazione_doc_ammBulk(Fattura_passivaBulk fatturaPassiva) {
        super();

        setCd_tipo_documento_amm(TIPO_FATTURA_PASSIVA);

        setCd_cds(fatturaPassiva.getCd_cds());
        setEsercizio(fatturaPassiva.getEsercizio());
        setCd_unita_organizzativa(fatturaPassiva.getCd_unita_organizzativa());
    }

    public Numerazione_doc_ammBulk(Lettera_pagam_esteroBulk lettera) {
        super();

        setCd_tipo_documento_amm(TIPO_LETTERA_ESTERO);

        setCd_cds(lettera.getCd_cds());
        setEsercizio(lettera.getEsercizio());
        setCd_unita_organizzativa(lettera.getCd_unita_organizzativa());
    }

    public Numerazione_doc_ammBulk(it.cnr.contab.missioni00.docs.bulk.AnticipoBulk anticipo) {
        super();

        setCd_tipo_documento_amm(TIPO_ANTICIPO);

        setCd_cds(anticipo.getCd_cds());
        setEsercizio(anticipo.getEsercizio());
        setCd_unita_organizzativa(anticipo.getCd_unita_organizzativa());
    }

    public Numerazione_doc_ammBulk(it.cnr.contab.missioni00.docs.bulk.MissioneBulk missione) {
        super();

        setCd_tipo_documento_amm(TIPO_MISSIONE);

        setCd_cds(missione.getCd_cds());
        setEsercizio(missione.getEsercizio());
        setCd_unita_organizzativa(missione.getCd_unita_organizzativa());
    }

    public Numerazione_doc_ammBulk(java.lang.String cd_cds, java.lang.String cd_tipo_documento_amm, java.lang.String cd_unita_organizzativa, java.lang.Integer esercizio) {
        super(cd_cds, cd_tipo_documento_amm, cd_unita_organizzativa, esercizio);
    }
}
