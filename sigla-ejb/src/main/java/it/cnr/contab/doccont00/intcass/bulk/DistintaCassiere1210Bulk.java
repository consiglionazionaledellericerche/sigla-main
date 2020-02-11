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

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 24/12/2015
 */
package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.annotation.StorageType;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.jada.bulk.OggettoBulk;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@StorageType(name="D:doccont:document")
public class DistintaCassiere1210Bulk extends DistintaCassiere1210Base {
	private static final long serialVersionUID = 1L;

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DISTINTA_CASSIERE_1210
	 **/
	public DistintaCassiere1210Bulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DISTINTA_CASSIERE_1210
	 **/
	public DistintaCassiere1210Bulk(java.lang.Integer esercizio, java.lang.Long pgDistinta) {
		super(esercizio, pgDistinta);
	}
	public String getCMISFolderName() {
		String suffix = "Distinta 1210 n.";
		suffix = suffix.concat(String.valueOf(getPgDistinta()));
		return suffix;
	}

	@StorageProperty(name="doccont:tipo")
	public String getTipo() {
		return "DIST1210";
	}

	public String getStorePath() {
		return Arrays.asList(
				SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
				"Distinte 1210",
				Optional.ofNullable(getEsercizio())
						.map(esercizio -> String.valueOf(esercizio))
						.orElse("0"),
				getCMISFolderName()
		).stream().collect(
				Collectors.joining(StorageDriver.SUFFIX)
		);
	}

	public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		setEsercizio( it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context) );
		return this;
	}
}