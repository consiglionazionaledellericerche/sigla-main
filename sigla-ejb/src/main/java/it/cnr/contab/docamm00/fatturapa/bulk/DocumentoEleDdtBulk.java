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
public class DocumentoEleDdtBulk extends DocumentoEleDdtBase {
	/**
	 * [DOCUMENTO_ELE_TESTATA Documento elettronico di trasmissione testata]
	 **/
	private DocumentoEleTestataBulk documentoEleTestata =  new DocumentoEleTestataBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DOCUMENTO_ELE_DDT
	 **/
	public DocumentoEleDdtBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DOCUMENTO_ELE_DDT
	 **/
	public DocumentoEleDdtBulk(java.lang.String idPaese, java.lang.String idCodice, java.lang.Long progressivoInvio, java.lang.Long progressivo, java.lang.Long progressivoDdt) {
		super(idPaese, idCodice, progressivoInvio,progressivo,progressivoDdt);
		setDocumentoEleTestata( new DocumentoEleTestataBulk(idPaese,idCodice,progressivoInvio,progressivo) );
	}
	public DocumentoEleDdtBulk(DocumentoEleTestataBulk documentoEleTestata) {
		setDocumentoEleTestata(documentoEleTestata);
	}	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Documento elettronico di trasmissione testata]
	 **/
	public DocumentoEleTestataBulk getDocumentoEleTestata() {
		return documentoEleTestata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Documento elettronico di trasmissione testata]
	 **/
	public void setDocumentoEleTestata(DocumentoEleTestataBulk documentoEleTestata)  {
		this.documentoEleTestata=documentoEleTestata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [idPaese]
	 **/
	public java.lang.String getIdPaese() {
		DocumentoEleTestataBulk documentoEleTestata = this.getDocumentoEleTestata();
		if (documentoEleTestata == null)
			return null;
		return getDocumentoEleTestata().getIdPaese();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [idPaese]
	 **/
	public void setIdPaese(java.lang.String idPaese)  {
		this.getDocumentoEleTestata().setIdPaese(idPaese);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [idCodice]
	 **/
	public java.lang.String getIdCodice() {
		DocumentoEleTestataBulk documentoEleTestata = this.getDocumentoEleTestata();
		if (documentoEleTestata == null)
			return null;
		return getDocumentoEleTestata().getIdCodice();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [idCodice]
	 **/
	public void setIdCodice(java.lang.String idCodice)  {
		this.getDocumentoEleTestata().setIdCodice(idCodice);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [identificativoSdi]
	 **/
	public java.lang.Long getIdentificativoSdi() {
		DocumentoEleTestataBulk documentoEleTestata = this.getDocumentoEleTestata();
		if (documentoEleTestata == null)
			return null;
		return getDocumentoEleTestata().getIdentificativoSdi();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [identificativoSdi]
	 **/
	public void setIdentificativoSdi(java.lang.Long identificativoSdi)  {
		this.getDocumentoEleTestata().setIdentificativoSdi(identificativoSdi);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [progressivo]
	 **/
	public java.lang.Long getProgressivo() {
		DocumentoEleTestataBulk documentoEleTestata = this.getDocumentoEleTestata();
		if (documentoEleTestata == null)
			return null;
		return getDocumentoEleTestata().getProgressivo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [progressivo]
	 **/
	public void setProgressivo(java.lang.Long progressivo)  {
		this.getDocumentoEleTestata().setProgressivo(progressivo);
	}
}