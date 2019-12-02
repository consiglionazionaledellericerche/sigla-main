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

package it.cnr.contab.docamm00.storage;

public enum StorageDocAmmProperty {
	
	SIGLA_FATTURE_ESERCIZIO("sigla_fatture:esercizio"),
	SIGLA_FATTURE_PG_FATTURA("sigla_fatture:pg_fattura"),
	SIGLA_FATTURE_CODICE_CDS("strorgcds:codice"),
	SIGLA_FATTURE_DESCRIZIONE_CDS("strorgcds:descrizione"),
	SIGLA_FATTURE_PROT_IVA("sigla_fatture:prot_iva"),
	SIGLA_FATTURE_PROT_GEN("sigla_fatture:prot_gen"),
	SIGLA_FATTURE_DT_REGISTRAZIONE("sigla_fatture:data_reg"),
	SIGLA_FATTURE_DESCRIZIONE("sigla_fatture:descrizione"),
	SIGLA_FATTURE_IMPONIBILE("sigla_fatture:imponibile"),
	SIGLA_FATTURE_IVA("sigla_fatture:iva"),
	SIGLA_FATTURE_IMPORTO_TOTALE("sigla_fatture:importo_totale"),
	SIGLA_FATTURE_DIVISA("sigla_fatture:divisa"),
	SIGLA_FATTURE_CAMBIO("sigla_fatture:cambio"),
	SIGLA_FATTURE_DT_COMPETENZA_DAL("sigla_fatture:dt_competenza_dal"),
	SIGLA_FATTURE_DT_COMPETENZA_AL("sigla_fatture:dt_competenza_al"),
	SIGLA_FATTURE_DT_EMISSIONE("sigla_fatture:data_emissione"),
	SIGLA_FATTURE_CODICE_IPA("sigla_fatture:codice_ipa"),
	SIGLA_FATTURE_CODICE_INVIO_SDI("sigla_fatture:codice_invio_sdi"),
	SIGLA_FATTURE_STATO_INVIO_SDI("sigla_fatture:stato_invio_sdi"),
	SIGLA_FATTURE_NOTE_INVIO_SDI("sigla_fatture:note_invio_sdi"),
	SIGLA_FATTURE_DATA_CONSEGNA_SDI("sigla_fatture:data_consegna_sdi"),
	SIGLA_FATTURE_MODALITA_INCASSO("sigla_fatture:modalita_incasso"),
	SIGLA_FATTURE_NOTA_CREDITO_AUT_ANNULLO_SDI("sigla_fatture:nc_aut_annullo_sdi"),
	SIGLA_FATTURE_ATTACHMENT_ORIGINAL_NAME("sigla_fatture_attachment:original_name");

	private final String value;

	private StorageDocAmmProperty(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static StorageDocAmmProperty fromValue(String v) {
		for (StorageDocAmmProperty c : StorageDocAmmProperty.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
}
