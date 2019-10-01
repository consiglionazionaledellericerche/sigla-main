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
import it.cnr.jada.persistency.Keyed;
public class DocumentoEleAllegatiBase extends DocumentoEleAllegatiKey implements Keyed {
//    ALGORITMO_COMPRESSIONE VARCHAR(10)
	private java.lang.String algoritmoCompressione;
 
//    FORMATO_ATTACHMENT VARCHAR(10)
	private java.lang.String formatoAttachment;
 
//    DESCRIZIONE_ATTACHMENT VARCHAR(100)
	private java.lang.String descrizioneAttachment;
 
//    CMIS_NODE_REF VARCHAR(100)
	private java.lang.String cmisNodeRef;
 
//    ANOMALIE VARCHAR(2000)
	private java.lang.String anomalie;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DOCUMENTO_ELE_ALLEGATI
	 **/
	public DocumentoEleAllegatiBase() {
		super();
	}
	public DocumentoEleAllegatiBase(java.lang.String idPaese, java.lang.String idCodice, java.lang.Long identificativoSdi, java.lang.Long progressivo, java.lang.String nomeAttachment) {
		super(idPaese, idCodice, identificativoSdi, progressivo, nomeAttachment);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [algoritmoCompressione]
	 **/
	public java.lang.String getAlgoritmoCompressione() {
		return algoritmoCompressione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [algoritmoCompressione]
	 **/
	public void setAlgoritmoCompressione(java.lang.String algoritmoCompressione)  {
		this.algoritmoCompressione=algoritmoCompressione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [formatoAttachment]
	 **/
	public java.lang.String getFormatoAttachment() {
		return formatoAttachment;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [formatoAttachment]
	 **/
	public void setFormatoAttachment(java.lang.String formatoAttachment)  {
		this.formatoAttachment=formatoAttachment;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [descrizioneAttachment]
	 **/
	public java.lang.String getDescrizioneAttachment() {
		return descrizioneAttachment;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [descrizioneAttachment]
	 **/
	public void setDescrizioneAttachment(java.lang.String descrizioneAttachment)  {
		this.descrizioneAttachment=descrizioneAttachment;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cmisNodeRef]
	 **/
	public java.lang.String getCmisNodeRef() {
		return cmisNodeRef;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cmisNodeRef]
	 **/
	public void setCmisNodeRef(java.lang.String cmisNodeRef)  {
		this.cmisNodeRef=cmisNodeRef;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [anomalie]
	 **/
	public java.lang.String getAnomalie() {
		return anomalie;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [anomalie]
	 **/
	public void setAnomalie(java.lang.String anomalie)  {
		this.anomalie=anomalie;
	}
}