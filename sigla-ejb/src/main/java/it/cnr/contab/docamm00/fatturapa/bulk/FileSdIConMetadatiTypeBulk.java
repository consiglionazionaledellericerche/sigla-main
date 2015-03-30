package it.cnr.contab.docamm00.fatturapa.bulk;

import it.cnr.jada.bulk.OggettoBulk;

import java.io.File;
import java.math.BigInteger;

public class FileSdIConMetadatiTypeBulk extends OggettoBulk {
	private static final long serialVersionUID = 1L;

	private BigInteger identificativoSdI;
	private String nomeFile;
	private File file;
	private String nomeFileMetadati;
	private File metadati;

	public FileSdIConMetadatiTypeBulk() {
		super();
	}

	public BigInteger getIdentificativoSdI() {
		return identificativoSdI;
	}

	public void setIdentificativoSdI(BigInteger identificativoSdI) {
		this.identificativoSdI = identificativoSdI;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getNomeFileMetadati() {
		return nomeFileMetadati;
	}

	public void setNomeFileMetadati(String nomeFileMetadati) {
		this.nomeFileMetadati = nomeFileMetadati;
	}

	public File getMetadati() {
		return metadati;
	}

	public void setMetadati(File metadati) {
		this.metadati = metadati;
	}
}
