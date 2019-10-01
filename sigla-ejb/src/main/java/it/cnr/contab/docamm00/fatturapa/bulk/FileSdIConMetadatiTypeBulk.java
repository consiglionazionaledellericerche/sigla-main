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
	private File fileFattureRicevute;
	private String identificativoSDI;
	private Integer daysBefore;
	
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

	public File getFileFattureRicevute() {
		return fileFattureRicevute;
	}

	public void setFileFattureRicevute(File fileFattureRicevute) {
		this.fileFattureRicevute = fileFattureRicevute;
	}

	public Integer getDaysBefore() {
		return daysBefore;
	}

	public void setDaysBefore(Integer daysBefore) {
		this.daysBefore = daysBefore;
	}

	public String getIdentificativoSDI() {
		return identificativoSDI;
	}

	public void setIdentificativoSDI(String identificativoSDI) {
		this.identificativoSDI = identificativoSDI;
	}
}
