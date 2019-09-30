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

package it.cnr.contab.util;

public enum SIGLAStoragePropertyNames {

    //Struttura Organizzativa
    STRORGCDS_CODICE("strorgcds:codice"),
    STRORGCDS_DESCRIZIONE("strorgcds:descrizione"),

    STRORGUO_CODICE("strorguo:codice"),
    STRORGUO_DESCRIZIONE("strorguo:descrizione"),

    STRORGCDR_CODICE("strorgcdr:codice"),
    STRORGCDR_DESCRIZIONE("strorgcdr:descrizione"),

    //Variazioni Al Piano di Gestione
    VARPIANOGEST_ESERCIZIO("varpianogest:esercizio"),
    VARPIANOGEST_NUMEROVARIAZIONE("varpianogest:numeroVariazione"),

    //Dati Utente Applicativo SIGLA
    SIGLA_COMMONS_UTENTE_SIGLA("sigla_commons:utente_applicativo"),

    //Dati Terzi
    SIGLA_COMMONS_CD_TERZO("sigla_commons:terzi_cd_terzo"),
    SIGLA_COMMONS_TERZO_COGNOME("sigla_commons:terzi_pf_cognome"),
    SIGLA_COMMONS_TERZO_NOME("sigla_commons:terzi_pf_nome"),
    SIGLA_COMMONS_TERZO_CODFIS("sigla_commons:terzi_pf_codfis"),
    SIGLA_COMMONS_TERZO_DENOMINAZIONE("sigla_commons:terzi_pg_denominazione"),
    SIGLA_COMMONS_TERZO_PARIVA("sigla_commons:terzi_pg_pariva"),

    // Relazioni
    R_VARPIANOGEST_ALLEGATIVARBILANCIO("R:varpianogest:allegatiVarBilancio"),
    R_CNR_SIGNEDDOCUMENT("R:cnr:signedDocumentAss"),

    // Document
    CNR_ENVELOPEDDOCUMENT("D:cnr:envelopedDocument"),
    VARPIANOGEST_DOCUMENT("D:varpianogest:document"),

    //Aspect
    CNR_SIGNEDDOCUMENT("P:cnr:signedDocument");
    private String value;

    SIGLAStoragePropertyNames(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}
