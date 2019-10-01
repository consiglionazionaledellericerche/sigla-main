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
public class DocumentoEleIvaBase extends DocumentoEleIvaKey implements Keyed {
//    ALIQUOTA_IVA DECIMAL(17,2)
	private java.math.BigDecimal aliquotaIva;
 
//    NATURA VARCHAR(2)
	private java.lang.String natura;
 
//    CD_VOCE_IVA VARCHAR(10)
	private java.lang.String cdVoceIva;
 
//    SPESE_ACCESSORIE DECIMAL(17,2)
	private java.math.BigDecimal speseAccessorie;
 
//    ARROTONDAMENTO DECIMAL(17,2)
	private java.math.BigDecimal arrotondamento;
 
//    IMPONIBILE_IMPORTO DECIMAL(17,2)
	private java.math.BigDecimal imponibileImporto;
 
//    IMPOSTA DECIMAL(17,2)
	private java.math.BigDecimal imposta;
 
//    ESIGIBILITA_IVA VARCHAR(1)
	private java.lang.String esigibilitaIva;
 
//    RIFERIMENTO_NORMATIVO VARCHAR(100)
	private java.lang.String riferimentoNormativo;
 
//    ANOMALIE VARCHAR(2000)
	private java.lang.String anomalie;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DOCUMENTO_ELE_IVA
	 **/
	public DocumentoEleIvaBase() {
		super();
	}
	public DocumentoEleIvaBase(java.lang.String idPaese, java.lang.String idCodice, java.lang.Long identificativoSdi, java.lang.Long progressivo, java.lang.Long progressivoIva){
		super(idPaese, idCodice, identificativoSdi,progressivo, progressivoIva);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [aliquotaIva]
	 **/
	public java.math.BigDecimal getAliquotaIva() {
		return aliquotaIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [aliquotaIva]
	 **/
	public void setAliquotaIva(java.math.BigDecimal aliquotaIva)  {
		this.aliquotaIva=aliquotaIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [natura]
	 **/
	public java.lang.String getNatura() {
		return natura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [natura]
	 **/
	public void setNatura(java.lang.String natura)  {
		this.natura=natura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdVoceIva]
	 **/
	public java.lang.String getCdVoceIva() {
		return cdVoceIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdVoceIva]
	 **/
	public void setCdVoceIva(java.lang.String cdVoceIva)  {
		this.cdVoceIva=cdVoceIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [speseAccessorie]
	 **/
	public java.math.BigDecimal getSpeseAccessorie() {
		return speseAccessorie;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [speseAccessorie]
	 **/
	public void setSpeseAccessorie(java.math.BigDecimal speseAccessorie)  {
		this.speseAccessorie=speseAccessorie;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [arrotondamento]
	 **/
	public java.math.BigDecimal getArrotondamento() {
		return arrotondamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [arrotondamento]
	 **/
	public void setArrotondamento(java.math.BigDecimal arrotondamento)  {
		this.arrotondamento=arrotondamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imponibileImporto]
	 **/
	public java.math.BigDecimal getImponibileImporto() {
		return imponibileImporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imponibileImporto]
	 **/
	public void setImponibileImporto(java.math.BigDecimal imponibileImporto)  {
		this.imponibileImporto=imponibileImporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imposta]
	 **/
	public java.math.BigDecimal getImposta() {
		return imposta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imposta]
	 **/
	public void setImposta(java.math.BigDecimal imposta)  {
		this.imposta=imposta;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esigibilitaIva]
	 **/
	public java.lang.String getEsigibilitaIva() {
		return esigibilitaIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esigibilitaIva]
	 **/
	public void setEsigibilitaIva(java.lang.String esigibilitaIva)  {
		this.esigibilitaIva=esigibilitaIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [riferimentoNormativo]
	 **/
	public java.lang.String getRiferimentoNormativo() {
		return riferimentoNormativo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [riferimentoNormativo]
	 **/
	public void setRiferimentoNormativo(java.lang.String riferimentoNormativo)  {
		this.riferimentoNormativo=riferimentoNormativo;
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