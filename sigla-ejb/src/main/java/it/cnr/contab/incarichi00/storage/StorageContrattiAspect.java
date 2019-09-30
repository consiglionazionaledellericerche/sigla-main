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

public enum StorageContrattiAspect {
	
	SIGLA_CONTRATTI_PROCEDURA("P:sigla_contratti_aspect:procedura"),
	SIGLA_CONTRATTI_INCARICHI("P:sigla_contratti_aspect:incarichi"),
	SIGLA_CONTRATTI_BORSE_STUDIO("P:sigla_contratti_aspect:borse_studio"),
	SIGLA_CONTRATTI_ASSEGNI_RICERCA("P:sigla_contratti_aspect:assegni_ricerca"),
	SIGLA_CONTRATTI_APPALTI("P:sigla_contratti_aspect:appalti"),
	SIGLA_CONTRATTI_TIPO_NORMA("P:sigla_contratti_aspect:tipo_norma"),
	SIGLA_CONTRATTI_STATO_ANNULLATO("P:sigla_contratti_aspect:stato_annullato"),
	SIGLA_CONTRATTI_STATO_DEFINITIVO("P:sigla_contratti_aspect:stato_definitivo"),
	SIGLA_CONTRATTI_PUBBLICATO("P:sigla_contratti_aspect:pubblicato"),
	SIGLA_CONTRATTI_LINK("P:sigla_contratti_aspect_link");
	
	private final String value;

	private StorageContrattiAspect(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static StorageContrattiAspect fromValue(String v) {
		for (StorageContrattiAspect c : StorageContrattiAspect.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}	
}
