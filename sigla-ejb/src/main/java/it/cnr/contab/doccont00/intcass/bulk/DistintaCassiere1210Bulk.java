/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 24/12/2015
 */
package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.si.spring.storage.StorageService;
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
				Collectors.joining(StorageService.SUFFIX)
		);
	}

	public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		setEsercizio( it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context) );
		return this;
	}
}