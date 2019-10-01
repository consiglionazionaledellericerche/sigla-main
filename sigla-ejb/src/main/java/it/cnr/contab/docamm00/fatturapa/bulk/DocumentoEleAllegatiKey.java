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
 * Date 23/02/2015
 */
package it.cnr.contab.docamm00.fatturapa.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class DocumentoEleAllegatiKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String idPaese;
	private java.lang.String idCodice;
	private java.lang.Long identificativoSdi;
	private java.lang.Long progressivo;	
	private java.lang.String nomeAttachment;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DOCUMENTO_ELE_ALLEGATI
	 **/
	public DocumentoEleAllegatiKey() {
		super();
	}
	public DocumentoEleAllegatiKey(java.lang.String idPaese, java.lang.String idCodice, java.lang.Long identificativoSdi, java.lang.Long progressivo, java.lang.String nomeAttachment) {
		super();
		this.idPaese=idPaese;
		this.idCodice=idCodice;
		this.identificativoSdi=identificativoSdi;
		this.progressivo = progressivo;		
		this.nomeAttachment=nomeAttachment;		
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof DocumentoEleAllegatiKey)) return false;
		DocumentoEleAllegatiKey k = (DocumentoEleAllegatiKey) o;
		if (!compareKey(getIdPaese(), k.getIdPaese())) return false;
		if (!compareKey(getIdCodice(), k.getIdCodice())) return false;
		if (!compareKey(getIdentificativoSdi(), k.getIdentificativoSdi())) return false;
		if (!compareKey(getProgressivo(), k.getProgressivo())) return false;		
		if (!compareKey(getNomeAttachment(), k.getNomeAttachment())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getIdPaese());
		i = i + calculateKeyHashCode(getIdCodice());
		i = i + calculateKeyHashCode(getIdentificativoSdi());
		i = i + calculateKeyHashCode(getProgressivo());
		i = i + calculateKeyHashCode(getNomeAttachment());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [idPaese]
	 **/
	public void setIdPaese(java.lang.String idPaese)  {
		this.idPaese=idPaese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [idPaese]
	 **/
	public java.lang.String getIdPaese() {
		return idPaese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [idCodice]
	 **/
	public void setIdCodice(java.lang.String idCodice)  {
		this.idCodice=idCodice;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [idCodice]
	 **/
	public java.lang.String getIdCodice() {
		return idCodice;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [identificativoSdi]
	 **/
	public void setIdentificativoSdi(java.lang.Long identificativoSdi)  {
		this.identificativoSdi=identificativoSdi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [identificativoSdi]
	 **/
	public java.lang.Long getIdentificativoSdi() {
		return identificativoSdi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [progressivo]
	 **/
	public void setProgressivo(java.lang.Long progressivo)  {
		this.progressivo=progressivo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [progressivo]
	 **/
	public java.lang.Long getProgressivo() {
		return progressivo;
	}		
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nomeAttachment]
	 **/
	public void setNomeAttachment(java.lang.String nomeAttachment)  {
		this.nomeAttachment=nomeAttachment;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nomeAttachment]
	 **/
	public java.lang.String getNomeAttachment() {
		return nomeAttachment;
	}
}