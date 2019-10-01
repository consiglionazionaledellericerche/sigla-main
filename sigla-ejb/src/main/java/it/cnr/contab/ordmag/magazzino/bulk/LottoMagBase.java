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
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import it.cnr.jada.persistency.Keyed;
public class LottoMagBase extends LottoMagKey implements Keyed {
//    CD_BENE_SERVIZIO VARCHAR(15) NOT NULL
	private java.lang.String cdBeneServizio;
 
//    CD_CDS_MAG VARCHAR(30) NOT NULL
	private java.lang.String cdCdsMag;
 
//    CD_MAGAZZINO_MAG VARCHAR(10) NOT NULL
	private java.lang.String cdMagazzinoMag;
 
//    DT_SCADENZA TIMESTAMP(7)
	private java.sql.Timestamp dtScadenza;
 
//    GIACENZA DECIMAL(12,5)
	private java.math.BigDecimal giacenza;
 
//    QUANTITA_VALORE DECIMAL(12,5)
	private java.math.BigDecimal quantitaValore;
 
//    LOTTO_FORNITORE VARCHAR(30)
	private java.lang.String lottoFornitore;
 
//    DT_CARICO TIMESTAMP(7)
	private java.sql.Timestamp dtCarico;
 
//    VALORE_UNITARIO DECIMAL(21,6)
	private java.math.BigDecimal valoreUnitario;
 
//    COSTO_UNITARIO DECIMAL(21,6)
	private java.math.BigDecimal costoUnitario;
 
//    QUANTITA_CARICO DECIMAL(12,5)
	private java.math.BigDecimal quantitaCarico;
 
//    QUANTITA_INIZIO_ANNO DECIMAL(17,5)
	private java.math.BigDecimal quantitaInizioAnno;
 
//    CD_CDS_ORDINE VARCHAR(30)
	private java.lang.String cdCdsOrdine;
 
//    CD_UNITA_OPERATIVA VARCHAR(30)
	private java.lang.String cdUnitaOperativa;
 
//    ESERCIZIO_ORDINE DECIMAL(4,0)
	private java.lang.Integer esercizioOrdine;
 
//    CD_NUMERATORE_ORDINE VARCHAR(3)
	private java.lang.String cdNumeratoreOrdine;
 
//    NUMERO_ORDINE DECIMAL(6,0)
	private java.lang.Integer numeroOrdine;
 
//    RIGA_ORDINE DECIMAL(6,0)
	private java.lang.Integer rigaOrdine;
 
//    CONSEGNA DECIMAL(6,0)
	private java.lang.Integer consegna;
 
//    STATO VARCHAR(3)
	private java.lang.String stato;
 
//    CD_TERZO DECIMAL(10,0)
	private java.lang.Integer cdTerzo;
 
//    CD_DIVISA VARCHAR(10) NOT NULL
	private java.lang.String cdDivisa;
 
//    CAMBIO DECIMAL(15,4) NOT NULL
	private java.math.BigDecimal cambio;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: LOTTO_MAG
	 **/
	public LottoMagBase() {
		super();
	}
	public LottoMagBase(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.Integer esercizio, java.lang.String cdNumeratoreMag, java.lang.Integer pgLotto) {
		super(cdCds, cdMagazzino, esercizio, cdNumeratoreMag, pgLotto);
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
	 * Restituisce il valore di: [cdMagazzinoMag]
	 **/
	public java.lang.String getCdMagazzinoMag() {
		return cdMagazzinoMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzinoMag]
	 **/
	public void setCdMagazzinoMag(java.lang.String cdMagazzinoMag)  {
		this.cdMagazzinoMag=cdMagazzinoMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtScadenza]
	 **/
	public java.sql.Timestamp getDtScadenza() {
		return dtScadenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtScadenza]
	 **/
	public void setDtScadenza(java.sql.Timestamp dtScadenza)  {
		this.dtScadenza=dtScadenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [giacenza]
	 **/
	public java.math.BigDecimal getGiacenza() {
		return giacenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [giacenza]
	 **/
	public void setGiacenza(java.math.BigDecimal giacenza)  {
		this.giacenza=giacenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [quantitaValore]
	 **/
	public java.math.BigDecimal getQuantitaValore() {
		return quantitaValore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [quantitaValore]
	 **/
	public void setQuantitaValore(java.math.BigDecimal quantitaValore)  {
		this.quantitaValore=quantitaValore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [lottoFornitore]
	 **/
	public java.lang.String getLottoFornitore() {
		return lottoFornitore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [lottoFornitore]
	 **/
	public void setLottoFornitore(java.lang.String lottoFornitore)  {
		this.lottoFornitore=lottoFornitore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtCarico]
	 **/
	public java.sql.Timestamp getDtCarico() {
		return dtCarico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtCarico]
	 **/
	public void setDtCarico(java.sql.Timestamp dtCarico)  {
		this.dtCarico=dtCarico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [valoreUnitario]
	 **/
	public java.math.BigDecimal getValoreUnitario() {
		return valoreUnitario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [valoreUnitario]
	 **/
	public void setValoreUnitario(java.math.BigDecimal valoreUnitario)  {
		this.valoreUnitario=valoreUnitario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [costoUnitario]
	 **/
	public java.math.BigDecimal getCostoUnitario() {
		return costoUnitario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [costoUnitario]
	 **/
	public void setCostoUnitario(java.math.BigDecimal costoUnitario)  {
		this.costoUnitario=costoUnitario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [quantitaCarico]
	 **/
	public java.math.BigDecimal getQuantitaCarico() {
		return quantitaCarico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [quantitaCarico]
	 **/
	public void setQuantitaCarico(java.math.BigDecimal quantitaCarico)  {
		this.quantitaCarico=quantitaCarico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [quantitaInizioAnno]
	 **/
	public java.math.BigDecimal getQuantitaInizioAnno() {
		return quantitaInizioAnno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [quantitaInizioAnno]
	 **/
	public void setQuantitaInizioAnno(java.math.BigDecimal quantitaInizioAnno)  {
		this.quantitaInizioAnno=quantitaInizioAnno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsOrdine]
	 **/
	public java.lang.String getCdCdsOrdine() {
		return cdCdsOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsOrdine]
	 **/
	public void setCdCdsOrdine(java.lang.String cdCdsOrdine)  {
		this.cdCdsOrdine=cdCdsOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public java.lang.String getCdUnitaOperativa() {
		return cdUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.cdUnitaOperativa=cdUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioOrdine]
	 **/
	public java.lang.Integer getEsercizioOrdine() {
		return esercizioOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioOrdine]
	 **/
	public void setEsercizioOrdine(java.lang.Integer esercizioOrdine)  {
		this.esercizioOrdine=esercizioOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreOrdine]
	 **/
	public java.lang.String getCdNumeratoreOrdine() {
		return cdNumeratoreOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreOrdine]
	 **/
	public void setCdNumeratoreOrdine(java.lang.String cdNumeratoreOrdine)  {
		this.cdNumeratoreOrdine=cdNumeratoreOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroOrdine]
	 **/
	public java.lang.Integer getNumeroOrdine() {
		return numeroOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroOrdine]
	 **/
	public void setNumeroOrdine(java.lang.Integer numeroOrdine)  {
		this.numeroOrdine=numeroOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rigaOrdine]
	 **/
	public java.lang.Integer getRigaOrdine() {
		return rigaOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rigaOrdine]
	 **/
	public void setRigaOrdine(java.lang.Integer rigaOrdine)  {
		this.rigaOrdine=rigaOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [consegna]
	 **/
	public java.lang.Integer getConsegna() {
		return consegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [consegna]
	 **/
	public void setConsegna(java.lang.Integer consegna)  {
		this.consegna=consegna;
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
	 * Restituisce il valore di: [cdTerzo]
	 **/
	public java.lang.Integer getCdTerzo() {
		return cdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerzo]
	 **/
	public void setCdTerzo(java.lang.Integer cdTerzo)  {
		this.cdTerzo=cdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdDivisa]
	 **/
	public java.lang.String getCdDivisa() {
		return cdDivisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdDivisa]
	 **/
	public void setCdDivisa(java.lang.String cdDivisa)  {
		this.cdDivisa=cdDivisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cambio]
	 **/
	public java.math.BigDecimal getCambio() {
		return cambio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cambio]
	 **/
	public void setCambio(java.math.BigDecimal cambio)  {
		this.cambio=cambio;
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
}