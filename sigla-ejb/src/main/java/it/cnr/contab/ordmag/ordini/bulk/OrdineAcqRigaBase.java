/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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
 * Date 28/06/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import it.cnr.jada.persistency.Keyed;
public class OrdineAcqRigaBase extends OrdineAcqRigaKey implements Keyed {
//    CD_BENE_SERVIZIO VARCHAR(15) NOT NULL
	private java.lang.String cdBeneServizio;
 
//    DS_BENE_SERVIZIO VARCHAR(300) NOT NULL
	private java.lang.String dsBeneServizio;
 
//    NOTA_RIGA VARCHAR(2000)
	private java.lang.String notaRiga;
 
//    CD_VOCE_IVA VARCHAR(10) NOT NULL
	private java.lang.String cdVoceIva;
 
//    STATO VARCHAR(3) NOT NULL
	private java.lang.String stato;
 
//    PREZZO_UNITARIO DECIMAL(20,6) NOT NULL
	private java.math.BigDecimal prezzoUnitario;
 
//    CD_UNITA_MISURA VARCHAR(10) NOT NULL
	private java.lang.String cdUnitaMisura;
 
//    COEF_CONV DECIMAL(12,5) NOT NULL
	private java.math.BigDecimal coefConv;
 
//    SCONTO1 DECIMAL(5,2)
	private java.math.BigDecimal sconto1;
 
//    SCONTO2 DECIMAL(5,2)
	private java.math.BigDecimal sconto2;
 
//    SCONTO3 DECIMAL(5,2)
	private java.math.BigDecimal sconto3;
 
//    IM_IMPONIBILE_DIVISA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imImponibileDivisa;
 
//    IM_IVA_DIVISA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imIvaDivisa;
 
//    IM_IMPONIBILE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imImponibile;
 
//    IM_IVA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imIva;
 
//  IM_IVA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imIvaD;

//  IM_IVA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imIvaNd;

//    IM_TOTALE_RIGA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imTotaleRiga;

	private Long idDettaglioContratto;
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;

	public Long getIdDettaglioContratto() {
		return idDettaglioContratto;
	}

	public void setIdDettaglioContratto(Long idDettaglioContratto) {
		this.idDettaglioContratto = idDettaglioContratto;
	}

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ORDINE_ACQ_RIGA
	 **/
	public OrdineAcqRigaBase() {
		super();
	}
	public OrdineAcqRigaBase(java.lang.String cdCds, java.lang.String cdUnitaOperativa, java.lang.Integer esercizio, java.lang.String cdNumeratore, java.lang.Integer numero, java.lang.Integer riga) {
		super(cdCds, cdUnitaOperativa, esercizio, cdNumeratore, numero, riga);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdBeneServizio]
	 **/
	public java.lang.String getCdBeneServizio() {
		return cdBeneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdBeneServizio]
	 **/
	public void setCdBeneServizio(java.lang.String cdBeneServizio)  {
		this.cdBeneServizio=cdBeneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsBeneServizio]
	 **/
	public java.lang.String getDsBeneServizio() {
		return dsBeneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsBeneServizio]
	 **/
	public void setDsBeneServizio(java.lang.String dsBeneServizio)  {
		this.dsBeneServizio=dsBeneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [notaRiga]
	 **/
	public java.lang.String getNotaRiga() {
		return notaRiga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [notaRiga]
	 **/
	public void setNotaRiga(java.lang.String notaRiga)  {
		this.notaRiga=notaRiga;
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
	 * Restituisce il valore di: [stato]
	 **/
	public java.lang.String getStato() {
		return stato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stato]
	 **/
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prezzoUnitario]
	 **/
	public java.math.BigDecimal getPrezzoUnitario() {
		return prezzoUnitario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prezzoUnitario]
	 **/
	public void setPrezzoUnitario(java.math.BigDecimal prezzoUnitario)  {
		this.prezzoUnitario=prezzoUnitario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaMisura]
	 **/
	public java.lang.String getCdUnitaMisura() {
		return cdUnitaMisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaMisura]
	 **/
	public void setCdUnitaMisura(java.lang.String cdUnitaMisura)  {
		this.cdUnitaMisura=cdUnitaMisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [coefConv]
	 **/
	public java.math.BigDecimal getCoefConv() {
		return coefConv;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [coefConv]
	 **/
	public void setCoefConv(java.math.BigDecimal coefConv)  {
		this.coefConv=coefConv;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [sconto1]
	 **/
	public java.math.BigDecimal getSconto1() {
		return sconto1;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [sconto1]
	 **/
	public void setSconto1(java.math.BigDecimal sconto1)  {
		this.sconto1=sconto1;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [sconto2]
	 **/
	public java.math.BigDecimal getSconto2() {
		return sconto2;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [sconto2]
	 **/
	public void setSconto2(java.math.BigDecimal sconto2)  {
		this.sconto2=sconto2;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [sconto3]
	 **/
	public java.math.BigDecimal getSconto3() {
		return sconto3;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [sconto3]
	 **/
	public void setSconto3(java.math.BigDecimal sconto3)  {
		this.sconto3=sconto3;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imImponibileDivisa]
	 **/
	public java.math.BigDecimal getImImponibileDivisa() {
		return imImponibileDivisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imImponibileDivisa]
	 **/
	public void setImImponibileDivisa(java.math.BigDecimal imImponibileDivisa)  {
		this.imImponibileDivisa=imImponibileDivisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imIvaDivisa]
	 **/
	public java.math.BigDecimal getImIvaDivisa() {
		return imIvaDivisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imIvaDivisa]
	 **/
	public void setImIvaDivisa(java.math.BigDecimal imIvaDivisa)  {
		this.imIvaDivisa=imIvaDivisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imImponibile]
	 **/
	public java.math.BigDecimal getImImponibile() {
		return imImponibile;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imImponibile]
	 **/
	public void setImImponibile(java.math.BigDecimal imImponibile)  {
		this.imImponibile=imImponibile;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imIva]
	 **/
	public java.math.BigDecimal getImIva() {
		return imIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imIva]
	 **/
	public void setImIva(java.math.BigDecimal imIva)  {
		this.imIva=imIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imTotaleRiga]
	 **/
	public java.math.BigDecimal getImTotaleRiga() {
		return imTotaleRiga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imTotaleRiga]
	 **/
	public void setImTotaleRiga(java.math.BigDecimal imTotaleRiga)  {
		this.imTotaleRiga=imTotaleRiga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtCancellazione]
	 **/
	public java.sql.Timestamp getDtCancellazione() {
		return dtCancellazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtCancellazione]
	 **/
	public void setDtCancellazione(java.sql.Timestamp dtCancellazione)  {
		this.dtCancellazione=dtCancellazione;
	}
	public java.math.BigDecimal getImIvaD() {
		return imIvaD;
	}
	public void setImIvaD(java.math.BigDecimal imIvaD) {
		this.imIvaD = imIvaD;
	}
	public java.math.BigDecimal getImIvaNd() {
		return imIvaNd;
	}
	public void setImIvaNd(java.math.BigDecimal imIvaNd) {
		this.imIvaNd = imIvaNd;
	}
}