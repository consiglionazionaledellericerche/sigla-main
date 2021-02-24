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
public class DocumentoEleTributiBase extends DocumentoEleTributiKey implements Keyed {
//    TIPO_RIGA VARCHAR(3)
	private java.lang.String tipoRiga;
 
//    TIPO_TRIBUTO VARCHAR(4)
	private java.lang.String tipoTributo;
 
//    IMPORTO DECIMAL(17,2)
	private java.math.BigDecimal importo;
 
//    ALIQUOTA DECIMAL(17,2)
	private java.math.BigDecimal aliquota;
 
//    IMPONIBILE_CASSA DECIMAL(17,2)
	private java.math.BigDecimal imponibileCassa;
 
//    CAUSALE_PAGAMENTO VARCHAR(1)
	private java.lang.String causalePagamento;
 
//    RITENUTA_CASSA VARCHAR(2)
	private java.lang.String ritenutaCassa;
 
//    ALIQUOTA_IVA DECIMAL(17,2)
	private java.math.BigDecimal aliquotaIva;
 
//    squad VARCHAR(2)
	private java.lang.String natura;
 
//    RIFERIMENTO_AMMINISTRAZIONE VARCHAR(20)
	private java.lang.String riferimentoAmministrazione;
 
//    ANOMALIE VARCHAR(2000)
	private java.lang.String anomalie;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DOCUMENTO_ELE_TRIBUTI
	 **/
	public DocumentoEleTributiBase() {
		super();
	}
	public DocumentoEleTributiBase(java.lang.String idPaese, java.lang.String idCodice, java.lang.Long identificativoSdi, java.lang.Long progressivo, java.lang.Long progressivoTributo) {
		super(idPaese, idCodice, identificativoSdi, progressivo, progressivoTributo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoRiga]
	 **/
	public java.lang.String getTipoRiga() {
		return tipoRiga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoRiga]
	 **/
	public void setTipoRiga(java.lang.String tipoRiga)  {
		this.tipoRiga=tipoRiga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoTributo]
	 **/
	public java.lang.String getTipoTributo() {
		return tipoTributo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoTributo]
	 **/
	public void setTipoTributo(java.lang.String tipoTributo)  {
		this.tipoTributo=tipoTributo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importo]
	 **/
	public java.math.BigDecimal getImporto() {
		return importo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importo]
	 **/
	public void setImporto(java.math.BigDecimal importo)  {
		this.importo=importo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [aliquota]
	 **/
	public java.math.BigDecimal getAliquota() {
		return aliquota;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [aliquota]
	 **/
	public void setAliquota(java.math.BigDecimal aliquota)  {
		this.aliquota=aliquota;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imponibileCassa]
	 **/
	public java.math.BigDecimal getImponibileCassa() {
		return imponibileCassa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imponibileCassa]
	 **/
	public void setImponibileCassa(java.math.BigDecimal imponibileCassa)  {
		this.imponibileCassa=imponibileCassa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [causalePagamento]
	 **/
	public java.lang.String getCausalePagamento() {
		return causalePagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [causalePagamento]
	 **/
	public void setCausalePagamento(java.lang.String causalePagamento)  {
		this.causalePagamento=causalePagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ritenutaCassa]
	 **/
	public java.lang.String getRitenutaCassa() {
		return ritenutaCassa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ritenutaCassa]
	 **/
	public void setRitenutaCassa(java.lang.String ritenutaCassa)  {
		this.ritenutaCassa=ritenutaCassa;
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
	 * Restituisce il valore di: [riferimentoAmministrazione]
	 **/
	public java.lang.String getRiferimentoAmministrazione() {
		return riferimentoAmministrazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [riferimentoAmministrazione]
	 **/
	public void setRiferimentoAmministrazione(java.lang.String riferimentoAmministrazione)  {
		this.riferimentoAmministrazione=riferimentoAmministrazione;
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