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

import it.cnr.jada.util.OrderedHashtable;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.TipoCassaType;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.TipoRitenutaType;

public class DocumentoEleTributiBulk extends DocumentoEleTributiBase {
	
	public static final java.util.Dictionary<String, String> tiTipoTributoKeys = new OrderedHashtable();
	static {
		tiTipoTributoKeys.put(TipoRitenutaType.RT_01.value(), "Ritenuta di acconto persone fisiche");
		tiTipoTributoKeys.put(TipoRitenutaType.RT_02.value(), "Ritenuta di acconto persone giuridiche");
		tiTipoTributoKeys.put(TipoRitenutaType.RT_03.value(), "Contributo INPS");
		tiTipoTributoKeys.put(TipoRitenutaType.RT_04.value(), "Contributo ENASARCO");
		tiTipoTributoKeys.put(TipoRitenutaType.RT_05.value(), "Contributo ENPAM");
		tiTipoTributoKeys.put(TipoRitenutaType.RT_06.value(), "Altro contributo previdenziale");
		tiTipoTributoKeys.put(TipoCassaType.TC_01.value(), "Cassa nazionale previdenza e assistenza avvocati e procuratori legali");
		tiTipoTributoKeys.put(TipoCassaType.TC_02.value(), "Cassa previdenza dottori commercialisti");
		tiTipoTributoKeys.put(TipoCassaType.TC_03.value(), "Cassa previdenza e assistenza geometri");
		tiTipoTributoKeys.put(TipoCassaType.TC_04.value(), "Cassa nazionale previdenza e assistenza ingegneri e architetti liberi professionisti");
		tiTipoTributoKeys.put(TipoCassaType.TC_05.value(), "Cassa nazionale del notariato");
		tiTipoTributoKeys.put(TipoCassaType.TC_06.value(), "Cassa nazionale previdenza e assistenza ragionieri e periti commerciali");
		tiTipoTributoKeys.put(TipoCassaType.TC_07.value(), "Ente nazionale assistenza agenti e rappresentanti di commercio (ENASARCO)");
		tiTipoTributoKeys.put(TipoCassaType.TC_08.value(), "Ente nazionale previdenza e assistenza consulenti del lavoro (ENPACL)");
		tiTipoTributoKeys.put(TipoCassaType.TC_09.value(), "Ente nazionale previdenza e assistenza medici (ENPAM)");
		tiTipoTributoKeys.put(TipoCassaType.TC_10.value(), "Ente nazionale previdenza e assistenza farmacisti (ENPAF)");
		tiTipoTributoKeys.put(TipoCassaType.TC_11.value(), "Ente nazionale previdenza e assistenza veterinari (ENPAV)");
		tiTipoTributoKeys.put(TipoCassaType.TC_12.value(), "Ente nazionale previdenza e assistenza impiegati dell'agricoltura (ENPAIA)");
		tiTipoTributoKeys.put(TipoCassaType.TC_13.value(), "Fondo previdenza impiegati imprese di spedizione e agenzie marittime");
		tiTipoTributoKeys.put(TipoCassaType.TC_14.value(), "Istituto nazionale previdenza giornalisti italiani (INPGI)");
		tiTipoTributoKeys.put(TipoCassaType.TC_15.value(), "Opera nazionale assistenza orfani sanitari italiani (ONAOSI)");
		tiTipoTributoKeys.put(TipoCassaType.TC_16.value(), "Cassa autonoma assistenza integrativa giornalisti italiani (CASAGIT)");
		tiTipoTributoKeys.put(TipoCassaType.TC_17.value(), "Ente previdenza periti industriali e periti industriali laureati (EPPI)");
		tiTipoTributoKeys.put(TipoCassaType.TC_18.value(), "Ente previdenza e assistenza pluricategoriale (EPAP)");
		tiTipoTributoKeys.put(TipoCassaType.TC_19.value(), "Ente nazionale previdenza e assistenza biologi (ENPAB)");
		tiTipoTributoKeys.put(TipoCassaType.TC_20.value(), "Ente nazionale previdenza e assistenza professione infermieristica (ENPAPI)");
		tiTipoTributoKeys.put(TipoCassaType.TC_21.value(), "Ente nazionale previdenza e assistenza psicologi (ENPAP)");
		tiTipoTributoKeys.put(TipoCassaType.TC_22.value(), "INPS");
	}
	
	public final static String TIPO_RIGA_R = "RIT";
	public final static String TIPO_RIGA_C = "CAS";
	
	/**
	 * [DOCUMENTO_ELE_TESTATA Documento elettronico di trasmissione testata]
	 **/
	private DocumentoEleTestataBulk documentoEleTestata =  new DocumentoEleTestataBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DOCUMENTO_ELE_TRIBUTI
	 **/
	public DocumentoEleTributiBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DOCUMENTO_ELE_TRIBUTI
	 **/
	public DocumentoEleTributiBulk(java.lang.String idPaese, java.lang.String idCodice, java.lang.Long identificativoSdi, java.lang.Long progressivo, java.lang.Long progressivoTributo) {
		super(idPaese, idCodice, identificativoSdi,progressivo,progressivoTributo);
		setDocumentoEleTestata( new DocumentoEleTestataBulk(idPaese,idCodice,identificativoSdi,progressivo) );
	}
	public DocumentoEleTributiBulk(DocumentoEleTestataBulk documentoEleTestata) {
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