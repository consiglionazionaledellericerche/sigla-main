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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.OggettoBulk;

public class StorageFolderFattura extends OggettoBulk {
	private transient final static Logger logger = LoggerFactory.getLogger(StorageFolderFattura.class);
	private static final long serialVersionUID = 4110702628275029148L;

	@StoragePolicy(name="P:sigla_commons_aspect:utente_applicativo_sigla", property=@StorageProperty(name="sigla_commons_aspect:utente_applicativo"))
	public String getUtenteSigla() {
		return "SDI";
	}

	@StoragePolicy(name="P:sigla_commons_aspect:applicativo", property=@StorageProperty(name="sigla_commons_aspect:applicativo_numero_versione"))
	public String getNumeroVersioneApplicativo() {
		String version  = this.getClass().getPackage().getImplementationVersion();
		
		if (StringUtils.isEmpty(version)){
			version  = "01.001.000";	
		}

		return version;
	}

	@StoragePolicy(name="P:sigla_commons_aspect:applicativo", property=@StorageProperty(name="sigla_commons_aspect:applicativo_nome"))
	public String getNomeApplicativo() {
		return Utility.APPLICATION_TITLE;
	}
}