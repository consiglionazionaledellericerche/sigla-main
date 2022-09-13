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
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import it.cnr.jada.persistency.Keyed;

import java.math.BigDecimal;

public class FatturaOrdineBase extends FatturaOrdineKey implements Keyed {
//    CD_VOCE_IVA_RETT VARCHAR(10)
	private java.lang.String cdVoceIvaRett;
 
//    PREZZO_UNITARIO_RETT DECIMAL(20,6)
	private java.math.BigDecimal prezzoUnitarioRett;
 
//    SCONTO1_RETT DECIMAL(5,2)
	private java.math.BigDecimal sconto1Rett;
 
//    SCONTO2_RETT DECIMAL(5,2)
	private java.math.BigDecimal sconto2Rett;
 
//    SCONTO3_RETT DECIMAL(5,2)
	private java.math.BigDecimal sconto3Rett;
 
//    IM_IMPONIBILE_DIVISA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imImponibileDivisa;
 
//    IM_IVA_DIVISA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imIvaDivisa;
 
//    IM_IMPONIBILE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imImponibile;

	private java.math.BigDecimal imponibileErrato;
//    IM_IVA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imIva;
 
//    IM_IVA_D DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imIvaD;

	public String getCdCdsObblNc() {
		return cdCdsObblNc;
	}

	public void setCdCdsObblNc(String cdCdsObblNc) {
		this.cdCdsObblNc = cdCdsObblNc;
	}

	public Integer getEsercizioObblNc() {
		return esercizioObblNc;
	}

	public void setEsercizioObblNc(Integer esercizioObblNc) {
		this.esercizioObblNc = esercizioObblNc;
	}

	public Integer getEsercizioOrigObblNc() {
		return esercizioOrigObblNc;
	}

	public void setEsercizioOrigObblNc(Integer esercizioOrigObblNc) {
		this.esercizioOrigObblNc = esercizioOrigObblNc;
	}

	public Long getPgObbligazioneNc() {
		return pgObbligazioneNc;
	}

	public void setPgObbligazioneNc(Long pgObbligazioneNc) {
		this.pgObbligazioneNc = pgObbligazioneNc;
	}

	public Long getPgObbligazioneScadNc() {
		return pgObbligazioneScadNc;
	}

	public void setPgObbligazioneScadNc(Long pgObbligazioneScadNc) {
		this.pgObbligazioneScadNc = pgObbligazioneScadNc;
	}

	//    IM_IVA_ND DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imIvaNd;

	private java.lang.String cdCdsObblNc;

	private java.lang.Integer esercizioObblNc;

	private java.lang.Integer esercizioOrigObblNc;

	private java.lang.Long pgObbligazioneNc;

	//    PG_OBBLIGAZIONE_SCAD DECIMAL(10,0)
	private java.lang.Long pgObbligazioneScadNc;

	public BigDecimal getImponibileErrato() {
		return imponibileErrato;
	}

	public void setImponibileErrato(BigDecimal imponibileErrato) {
		this.imponibileErrato = imponibileErrato;
	}

	//    IM_TOTALE_CONSEGNA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imTotaleConsegna;
 
//    STATO_ASS VARCHAR(3) NOT NULL
	private java.lang.String statoAss;

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: FATTURA_ORDINE
	 **/
	public FatturaOrdineBase() {
		super();
	}
	public FatturaOrdineBase(java.lang.String cdCds, java.lang.String cdUnitaOrganizzativa, java.lang.Integer esercizio, java.lang.Long pgFatturaPassiva, java.lang.Long progressivoRiga, java.lang.String cdCdsOrdine, java.lang.String cdUnitaOperativa, java.lang.Integer esercizioOrdine, java.lang.String cdNumeratore, java.lang.Integer numero, java.lang.Integer riga, java.lang.Integer consegna) {
		super(cdCds, cdUnitaOrganizzativa, esercizio, pgFatturaPassiva, progressivoRiga, cdCdsOrdine, cdUnitaOperativa, esercizioOrdine, cdNumeratore, numero, riga, consegna);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdVoceIvaRett]
	 **/
	public java.lang.String getCdVoceIvaRett() {
		return cdVoceIvaRett;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdVoceIvaRett]
	 **/
	public void setCdVoceIvaRett(java.lang.String cdVoceIvaRett)  {
		this.cdVoceIvaRett=cdVoceIvaRett;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prezzoUnitarioRett]
	 **/
	public java.math.BigDecimal getPrezzoUnitarioRett() {
		return prezzoUnitarioRett;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prezzoUnitarioRett]
	 **/
	public void setPrezzoUnitarioRett(java.math.BigDecimal prezzoUnitarioRett)  {
		this.prezzoUnitarioRett=prezzoUnitarioRett;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [sconto1Rett]
	 **/
	public java.math.BigDecimal getSconto1Rett() {
		return sconto1Rett;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [sconto1Rett]
	 **/
	public void setSconto1Rett(java.math.BigDecimal sconto1Rett)  {
		this.sconto1Rett=sconto1Rett;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [sconto2Rett]
	 **/
	public java.math.BigDecimal getSconto2Rett() {
		return sconto2Rett;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [sconto2Rett]
	 **/
	public void setSconto2Rett(java.math.BigDecimal sconto2Rett)  {
		this.sconto2Rett=sconto2Rett;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [sconto3Rett]
	 **/
	public java.math.BigDecimal getSconto3Rett() {
		return sconto3Rett;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [sconto3Rett]
	 **/
	public void setSconto3Rett(java.math.BigDecimal sconto3Rett)  {
		this.sconto3Rett=sconto3Rett;
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
	 * Restituisce il valore di: [imIvaD]
	 **/
	public java.math.BigDecimal getImIvaD() {
		return imIvaD;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imIvaD]
	 **/
	public void setImIvaD(java.math.BigDecimal imIvaD)  {
		this.imIvaD=imIvaD;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imIvaNd]
	 **/
	public java.math.BigDecimal getImIvaNd() {
		return imIvaNd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imIvaNd]
	 **/
	public void setImIvaNd(java.math.BigDecimal imIvaNd)  {
		this.imIvaNd=imIvaNd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imTotaleConsegna]
	 **/
	public java.math.BigDecimal getImTotaleConsegna() {
		return imTotaleConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imTotaleConsegna]
	 **/
	public void setImTotaleConsegna(java.math.BigDecimal imTotaleConsegna)  {
		this.imTotaleConsegna=imTotaleConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [statoAss]
	 **/
	public java.lang.String getStatoAss() {
		return statoAss;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [statoAss]
	 **/
	public void setStatoAss(java.lang.String statoAss)  {
		this.statoAss=statoAss;
	}

}