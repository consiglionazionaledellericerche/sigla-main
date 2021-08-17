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

package it.cnr.contab.incarichi00.storage;

public enum StorageContrattiAttachment {
	
	SIGLA_CONTRATTI_ATTACHMENT_BANDO("D:sigla_contratti_attachment:bando"),
	SIGLA_CONTRATTI_ATTACHMENT_DECISIONE_A_CONTRATTARE("D:sigla_contratti_attachment:decisione_a_contrattare"),
	SIGLA_CONTRATTI_ATTACHMENT_CONTRATTO("D:sigla_contratti_attachment:contratto"),
	SIGLA_CONTRATTI_ATTACHMENT_VARIAZIONE_CONTRATTO("D:sigla_contratti_attachment:variazione_contratto"),
	SIGLA_CONTRATTI_ATTACHMENT_CURRICULUM_VINCITORE("D:sigla_contratti_attachment:curriculum_vincitore"),
	SIGLA_CONTRATTI_ATTACHMENT_DECRETO_NOMINA("D:sigla_contratti_attachment:decreto_nomina"),
	SIGLA_CONTRATTI_ATTACHMENT_ATTO_ESITO_CONTROLLO("D:sigla_contratti_attachment:atto_esito_controllo"),
	SIGLA_CONTRATTI_ATTACHMENT_PROGETTO("D:sigla_contratti_attachment:progetto"),
	SIGLA_CONTRATTI_ATTACHMENT_ALLEGATO_GENERICO("D:sigla_contratti_attachment:allegato_generico"),
	SIGLA_CONTRATTI_ATTACHMENT_CAPITOLATO("D:sigla_contratti_attachment:capitolato"),
	SIGLA_CONTRATTI_ATTACHMENT_DICHIARAZIONE_ALTRI_RAPPORTI("D:sigla_contratti_attachment:dichiarazione_altri_rapporti"),
	SIGLA_CONTRATTI_ATTACHMENT_CONFLITTO_INTERESSI("D:sigla_contratti_attachment:dichiarazione_insussistenza_conflitto_interessi"),
	SIGLA_CONTRATTI_ATTACHMENT_COMUNICAZIONE_PERLAPA("D:sigla_contratti_attachment:comunicazione_perlapa"),
	SIGLA_CONTRATTI_ATTACHMENT_ATTESTAZIONE_DIRETTORE("D:sigla_contratti_attachment:attestazione_direttore");

	private final String value;

	private StorageContrattiAttachment(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static StorageContrattiAttachment fromValue(String v) {
		for (StorageContrattiAttachment c : StorageContrattiAttachment.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}	
}
