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

public enum StorageDocAmmAspect {
	
	SIGLA_CONTRATTI_PROCEDURA("P:sigla_contratti_aspect:procedura"),
	SIGLA_CONTRATTI_INCARICHI("P:sigla_contratti_aspect:incarichi"),
	SIGLA_CONTRATTI_BORSE_STUDIO("P:sigla_contratti_aspect:borse_studio"),
	SIGLA_CONTRATTI_ASSEGNI_RICERCA("P:sigla_contratti_aspect:assegni_ricerca"),
	SIGLA_CONTRATTI_APPALTI("P:sigla_contratti_aspect:appalti"),
	SIGLA_CONTRATTI_TIPO_NORMA("P:sigla_contratti_aspect:tipo_norma"),
	SIGLA_CONTRATTI_STATO_ANNULLATO("P:sigla_contratti_aspect:stato_annullato"),
	SIGLA_CONTRATTI_STATO_DEFINITIVO("P:sigla_contratti_aspect:stato_definitivo"),
	SIGLA_STRUTTURA_ORGANIZZATIVA_CDR("P:strorg:cdr"),
	SIGLA_CONTRATTI_LINK("P:sigla_contratti_aspect_link"),
	SIGLA_FATTURE_ATTACHMENT_FATTURA_ELETTRONICA_INVIATA("P:sigla_fatture_attachment:fattura_elettronica_inviata"),
	SIGLA_FATTURE_ATTACHMENT_STAMPA_FATTURA_PRIMA_PROTOCOLLO("P:sigla_fatture_attachment:stampa_fattura_prima_protocollo"),
	SIGLA_FATTURE_ATTACHMENT_STAMPA_FATTURA_DOPO_PROTOCOLLO("P:sigla_fatture_attachment:stampa_fattura_dopo_protocollo"),
	SIGLA_FATTURE_ATTACHMENT_FATTURA_ELETTRONICA_XML_ANTE_FIRMA("P:sigla_fatture_attachment:fattura_elettronica_xml_ante_firma"),
	SIGLA_FATTURE_ATTACHMENT_FATTURA_ELETTRONICA_XML_POST_FIRMA("P:sigla_fatture_attachment:fattura_elettronica_xml_post_firma"),
	SIGLA_FATTURE_ATTACHMENT_RICEVUTA_CONSEGNA("P:sigla_fatture_attachment:ricevuta_consegna"),
	SIGLA_FATTURE_ATTACHMENT_MANCATA_CONSEGNA("P:sigla_fatture_attachment:mancata_consegna"),
	SIGLA_FATTURE_ATTACHMENT_SCARTO("P:sigla_fatture_attachment:scarto"),
	SIGLA_FATTURE_ATTACHMENT_ESITO_ACCETTATO("P:sigla_fatture_attachment:esito_accettato"),
	SIGLA_FATTURE_ATTACHMENT_ESITO_RIFIUTATO("P:sigla_fatture_attachment:esito_rifiutato"),
	SIGLA_FATTURE_ATTACHMENT_DECORRENZA_TERMINI("P:sigla_fatture_attachment:decorrenza_termini"),
	SIGLA_FATTURE_ATTACHMENT_ALLEGATI_NON_INVIATI_SDI("P:sigla_fatture_attachment:allegati_non_inviati_sdi"),
	SIGLA_FATTURE_ATTACHMENT_COMUNICAZIONE_NON_REGISTRABILITA("P:sigla_fatture_attachment:comunicazione_non_registrabilita"),
	SIGLA_FATTURE_ATTACHMENT_TRASMISSIONE_FATTURA("P:sigla_fatture_attachment:trasmissione_fattura"),
	SIGLA_FATTURE_PARTITA_IVA("P:sigla_commons_aspect:terzi_pg_pariva"),
	SIGLA_FATTURE_CODICE_FISCALE("P:sigla_commons_aspect:terzi_pf_codfis"),
	SIGLA_FATTURE_DENOMINAZIONE("P:sigla_commons_aspect:terzi_pg_denominazione"),
	SIGLA_FATTURE_COGNOME("P:sigla_commons_aspect:terzi_pf_cognome"),
	SIGLA_FATTURE_NOME("P:sigla_commons_aspect:terzi_pf_nome"),
	SIGLA_FATTURE_CDS_ORIGINE("P:sigla_commons_aspect:cds_origine_codice"),
	SIGLA_FATTURE_DESCR_CDS_ORIGINE("P:sigla_commons_aspect:cds_origine_descrizione"),
	SIGLA_FATTURE_UO_ORIGINE("P:sigla_commons_aspect:uo_origine_codice"),
	SIGLA_FATTURE_DESCR_UO_ORIGINE("P:sigla_commons_aspect:uo_origine_descrizione"),
	SIGLA_OBBLIGAZIONI_DETERMINA("P:sigla_obbligazioni_aspect:determina");
	
	
	private final String value;

	private StorageDocAmmAspect(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static StorageDocAmmAspect fromValue(String v) {
		for (StorageDocAmmAspect c : StorageDocAmmAspect.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}	
}
