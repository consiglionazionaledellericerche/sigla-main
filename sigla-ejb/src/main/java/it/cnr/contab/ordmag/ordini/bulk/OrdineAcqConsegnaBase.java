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
public class OrdineAcqConsegnaBase extends OrdineAcqConsegnaKey implements Keyed {
//    STATO VARCHAR(3) NOT NULL
	private java.lang.String stato;
 
//    TIPO_CONSEGNA VARCHAR(3) NOT NULL
	private java.lang.String tipoConsegna;
 
//    DT_PREV_CONSEGNA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtPrevConsegna;
 
//    QUANTITA DECIMAL(17,5) NOT NULL
	private java.math.BigDecimal quantita;
 
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

//    IM_TOTALE_CONSEGNA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imTotaleConsegna;
 
//    CD_CDS_MAG VARCHAR(30)
	private java.lang.String cdCdsMag;
 
//  CD_CDS_LUOGO VARCHAR(30)
	private java.lang.String cdCdsLuogo;

//  CD_LUOGO_CONSEGNA VARCHAR(10)
	private java.lang.String cdLuogoConsegna;

//    CD_MAGAZZINO VARCHAR(10)
	private java.lang.String cdMagazzino;

	public String getCdVoceEp() {
		return cdVoceEp;
	}

	public void setCdVoceEp(String cdVoceEp) {
		this.cdVoceEp = cdVoceEp;
	}

	public Integer getEsercizioEp() {
		return esercizioEp;
	}

	public void setEsercizioEp(Integer esercizioEp) {
		this.esercizioEp = esercizioEp;
	}

	//    CD_UOP_DEST VARCHAR(30)
	private java.lang.String cdUopDest;
 
//    CD_CDS_OBBL VARCHAR(30)
	private java.lang.String cdCdsObbl;

	private java.lang.String cdVoceEp;

	private java.lang.Integer esercizioEp;

	private java.lang.Integer esercizioObbl;
 
//    ESERCIZIO_ORIG_OBBL DECIMAL(4,0)
	private java.lang.Integer esercizioOrigObbl;
 
//    PG_OBBLIGAZIONE DECIMAL(10,0)
	private java.lang.Long pgObbligazione;
 
//    PG_OBBLIGAZIONE_SCAD DECIMAL(10,0)
	private java.lang.Long pgObbligazioneScad;
 
//  ARR_ALI_IVA DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal arrAliIva;

//  STATO_FATT VARCHAR(3) NOT NULL
	private java.lang.String statoFatt;

//  VECCHIA_CONSEGNA DECIMAL(4,0)
	private java.lang.Integer vecchiaConsegna;

//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ORDINE_ACQ_CONSEGNA
	 **/
	public OrdineAcqConsegnaBase() {
		super();
	}
	public OrdineAcqConsegnaBase(java.lang.String cdCds, java.lang.String cdUnitaOperativa, java.lang.Integer esercizio, java.lang.String cdNumeratore, java.lang.Integer numero, java.lang.Integer riga, java.lang.Integer consegna) {
		super(cdCds, cdUnitaOperativa, esercizio, cdNumeratore, numero, riga, consegna);
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
	 * Restituisce il valore di: [tipoConsegna]
	 **/
	public java.lang.String getTipoConsegna() {
		return tipoConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoConsegna]
	 **/
	public void setTipoConsegna(java.lang.String tipoConsegna)  {
		this.tipoConsegna=tipoConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtPrevConsegna]
	 **/
	public java.sql.Timestamp getDtPrevConsegna() {
		return dtPrevConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtPrevConsegna]
	 **/
	public void setDtPrevConsegna(java.sql.Timestamp dtPrevConsegna)  {
		this.dtPrevConsegna=dtPrevConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [quantita]
	 **/
	public java.math.BigDecimal getQuantita() {
		return quantita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [quantita]
	 **/
	public void setQuantita(java.math.BigDecimal quantita)  {
		this.quantita=quantita;
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
	 * Restituisce il valore di: [cdCdsMag]
	 **/
	public java.lang.String getCdCdsMag() {
		return cdCdsMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsMag]
	 **/
	public void setCdCdsMag(java.lang.String cdCdsMag)  {
		this.cdCdsMag=cdCdsMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzino]
	 **/
	public java.lang.String getCdMagazzino() {
		return cdMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzino]
	 **/
	public void setCdMagazzino(java.lang.String cdMagazzino)  {
		this.cdMagazzino=cdMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUopDest]
	 **/
	public java.lang.String getCdUopDest() {
		return cdUopDest;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUopDest]
	 **/
	public void setCdUopDest(java.lang.String cdUopDest)  {
		this.cdUopDest=cdUopDest;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsObbl]
	 **/
	public java.lang.String getCdCdsObbl() {
		return cdCdsObbl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsObbl]
	 **/
	public void setCdCdsObbl(java.lang.String cdCdsObbl)  {
		this.cdCdsObbl=cdCdsObbl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioObbl]
	 **/
	public java.lang.Integer getEsercizioObbl() {
		return esercizioObbl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioObbl]
	 **/
	public void setEsercizioObbl(java.lang.Integer esercizioObbl)  {
		this.esercizioObbl=esercizioObbl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioOrigObbl]
	 **/
	public java.lang.Integer getEsercizioOrigObbl() {
		return esercizioOrigObbl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioOrigObbl]
	 **/
	public void setEsercizioOrigObbl(java.lang.Integer esercizioOrigObbl)  {
		this.esercizioOrigObbl=esercizioOrigObbl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgObbligazione]
	 **/
	public java.lang.Long getPgObbligazione() {
		return pgObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgObbligazione]
	 **/
	public void setPgObbligazione(java.lang.Long pgObbligazione)  {
		this.pgObbligazione=pgObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgObbligazioneScad]
	 **/
	public java.lang.Long getPgObbligazioneScad() {
		return pgObbligazioneScad;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgObbligazioneScad]
	 **/
	public void setPgObbligazioneScad(java.lang.Long pgObbligazioneScad)  {
		this.pgObbligazioneScad=pgObbligazioneScad;
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
	public java.lang.String getCdCdsLuogo() {
		return cdCdsLuogo;
	}
	public void setCdCdsLuogo(java.lang.String cdCdsLuogo) {
		this.cdCdsLuogo = cdCdsLuogo;
	}
	public java.lang.String getCdLuogoConsegna() {
		return cdLuogoConsegna;
	}
	public void setCdLuogoConsegna(java.lang.String cdLuogoConsegna) {
		this.cdLuogoConsegna = cdLuogoConsegna;
	}
	public java.math.BigDecimal getArrAliIva() {
		return arrAliIva;
	}
	public void setArrAliIva(java.math.BigDecimal arrAliIva) {
		this.arrAliIva = arrAliIva;
	}
	public java.lang.String getStatoFatt() {
		return statoFatt;
	}
	public void setStatoFatt(java.lang.String statoFatt) {
		this.statoFatt = statoFatt;
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
	public java.lang.Integer getVecchiaConsegna() {
		return vecchiaConsegna;
	}
	public void setVecchiaConsegna(java.lang.Integer vecchiaConsegna) {
		this.vecchiaConsegna = vecchiaConsegna;
	}
}