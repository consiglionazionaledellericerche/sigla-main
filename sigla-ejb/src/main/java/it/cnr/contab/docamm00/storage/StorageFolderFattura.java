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