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
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import it.cnr.jada.persistency.Keyed;
public class MovimentiMagBase extends MovimentiMagKey implements Keyed {
	private Long pgMovimentoRif;
	private Long pgMovimentoAnn;
//    DT_MOVIMENTO TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtMovimento;
 
//    CD_CDS_TIPO_MOVIMENTO VARCHAR(30) NOT NULL
	private String cdCdsTipoMovimento;
 
//    CD_TIPO_MOVIMENTO VARCHAR(3) NOT NULL
	private String cdTipoMovimento;
 
//    DATA_BOLLA TIMESTAMP(7)
	private java.sql.Timestamp dataBolla;
 
//    NUMERO_BOLLA VARCHAR(30)
	private String numeroBolla;
 
//    DT_RIFERIMENTO TIMESTAMP(7)
	private java.sql.Timestamp dtRiferimento;
 
//    CD_CDS_ORDINE VARCHAR(30)
	private String cdCdsOrdine;
 
//    CD_UNITA_OPERATIVA_ORDINE VARCHAR(30)
	private String cdUnitaOperativaOrdine;
 
//    ESERCIZIO_ORDINE DECIMAL(4,0)
	private Integer esercizioOrdine;
 
//    CD_NUMERATORE_ORDINE VARCHAR(3)
	private String cdNumeratoreOrdine;
 
//    NUMERO_ORDINE DECIMAL(6,0)
	private Integer numeroOrdine;
 
//    RIGA_ORDINE DECIMAL(6,0)
	private Integer rigaOrdine;
 
//    CONSEGNA DECIMAL(6,0)
	private Integer consegna;
 
//    CD_TERZO DECIMAL(10,0)
	private Integer cdTerzo;
 
//    CD_UNITA_MISURA VARCHAR(3)
	private String cdUnitaMisura;
 
//    QUANTITA DECIMAL(12,5)
	private java.math.BigDecimal quantita;
 
//    COEFF_CONV DECIMAL(12,5)
	private java.math.BigDecimal coeffConv;
 
//    CD_UOP VARCHAR(30)
	private String cdUop;
 
//    DT_SCADENZA TIMESTAMP(7)
	private java.sql.Timestamp dtScadenza;
 
//    LOTTO_FORNITORE VARCHAR(30)
	private String lottoFornitore;
 
//    CD_CDS_LOTTO VARCHAR(30) NOT NULL
	private String cdCdsLotto;
 
//    CD_MAGAZZINO_LOTTO VARCHAR(10) NOT NULL
	private String cdMagazzinoLotto;
 
//    ESERCIZIO_LOTTO DECIMAL(4,0) NOT NULL
	private Integer esercizioLotto;
 
//    CD_NUMERATORE_LOTTO VARCHAR(3) NOT NULL
	private String cdNumeratoreLotto;
 
//    PG_LOTTO DECIMAL(6,0) NOT NULL
	private Integer pgLotto;
 
//    CD_CDS_MAG VARCHAR(30) NOT NULL
	private String cdCdsMag;
 
//    CD_MAGAZZINO VARCHAR(10) NOT NULL
	private String cdMagazzino;
 
//    CD_BENE_SERVIZIO VARCHAR(15) NOT NULL
	private String cdBeneServizio;
 
//    CD_CDS_TIPO_MOVIMENTO_RIF VARCHAR(30)
	private String cdCdsTipoMovimentoRif;
 
//    CD_TIPO_MOVIMENTO_RIF VARCHAR(3)
	private String cdTipoMovimentoRif;
 
//    CD_VOCE_IVA VARCHAR(10)
	private String cdVoceIva;
 
//    SCONTO1 DECIMAL(5,2)
	private java.math.BigDecimal sconto1;
 
//    SCONTO2 DECIMAL(5,2)
	private java.math.BigDecimal sconto2;
 
//    SCONTO3 DECIMAL(5,2)
	private java.math.BigDecimal sconto3;
 
//    CD_CDS_BOLLA_SCA VARCHAR(30)
	private String cdCdsBollaSca;
 
//    CD_MAGAZZINO_BOLLA_SCA VARCHAR(10)
	private String cdMagazzinoBollaSca;
 
//    ESERCIZIO_BOLLA_SCA DECIMAL(22,0)
	private Integer esercizioBollaSca;
 
//    CD_NUMERATORE_BOLLA_SCA VARCHAR(3)
	private String cdNumeratoreBollaSca;
 
//    PG_BOLLA_SCA DECIMAL(6,0)
	private Integer pgBollaSca;
 
//    IMPORTO DECIMAL(21,6)
	private java.math.BigDecimal importo;
 
//    IMPORTO_CEFF DECIMAL(19,2)
	private java.math.BigDecimal importoCeff;
 
//    IMPORTO_CMP DECIMAL(19,2)
	private java.math.BigDecimal importoCmp;
 
//    IMPORTO_FIFO DECIMAL(19,2)
	private java.math.BigDecimal importoFifo;
 
//    IMPORTO_LIFO DECIMAL(19,2)
	private java.math.BigDecimal importoLifo;
 
//    IMPORTO_CMPP DECIMAL(19,2)
	private java.math.BigDecimal importoCmpp;
 
//    IMPORTO_CMPIST DECIMAL(21,6)
	private java.math.BigDecimal importoCmpist;
 
//    CD_DIVISA VARCHAR(10) NOT NULL
	private String cdDivisa;
 
//    CAMBIO DECIMAL(15,4) NOT NULL
	private java.math.BigDecimal cambio;
 
//    PREZZO_UNITARIO DECIMAL(21,6)
	private java.math.BigDecimal prezzoUnitario;
 
//    STATO VARCHAR(3) NOT NULL
	private String stato;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MOVIMENTI_MAG
	 **/
	public MovimentiMagBase() {
		super();
	}
	public MovimentiMagBase(Long pgMovimento) {
		super(pgMovimento);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtMovimento]
	 **/
	public java.sql.Timestamp getDtMovimento() {
		return dtMovimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtMovimento]
	 **/
	public void setDtMovimento(java.sql.Timestamp dtMovimento)  {
		this.dtMovimento=dtMovimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsTipoMovimento]
	 **/
	public String getCdCdsTipoMovimento() {
		return cdCdsTipoMovimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsTipoMovimento]
	 **/
	public void setCdCdsTipoMovimento(String cdCdsTipoMovimento)  {
		this.cdCdsTipoMovimento=cdCdsTipoMovimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimento]
	 **/
	public String getCdTipoMovimento() {
		return cdTipoMovimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimento]
	 **/
	public void setCdTipoMovimento(String cdTipoMovimento)  {
		this.cdTipoMovimento=cdTipoMovimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataBolla]
	 **/
	public java.sql.Timestamp getDataBolla() {
		return dataBolla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataBolla]
	 **/
	public void setDataBolla(java.sql.Timestamp dataBolla)  {
		this.dataBolla=dataBolla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroBolla]
	 **/
	public String getNumeroBolla() {
		return numeroBolla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroBolla]
	 **/
	public void setNumeroBolla(String numeroBolla)  {
		this.numeroBolla=numeroBolla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtRiferimento]
	 **/
	public java.sql.Timestamp getDtRiferimento() {
		return dtRiferimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtRiferimento]
	 **/
	public void setDtRiferimento(java.sql.Timestamp dtRiferimento)  {
		this.dtRiferimento=dtRiferimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsOrdine]
	 **/
	public String getCdCdsOrdine() {
		return cdCdsOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsOrdine]
	 **/
	public void setCdCdsOrdine(String cdCdsOrdine)  {
		this.cdCdsOrdine=cdCdsOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativaOrdine]
	 **/
	public String getCdUnitaOperativaOrdine() {
		return cdUnitaOperativaOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativaOrdine]
	 **/
	public void setCdUnitaOperativaOrdine(String cdUnitaOperativaOrdine)  {
		this.cdUnitaOperativaOrdine=cdUnitaOperativaOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioOrdine]
	 **/
	public Integer getEsercizioOrdine() {
		return esercizioOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioOrdine]
	 **/
	public void setEsercizioOrdine(Integer esercizioOrdine)  {
		this.esercizioOrdine=esercizioOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreOrdine]
	 **/
	public String getCdNumeratoreOrdine() {
		return cdNumeratoreOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreOrdine]
	 **/
	public void setCdNumeratoreOrdine(String cdNumeratoreOrdine)  {
		this.cdNumeratoreOrdine=cdNumeratoreOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroOrdine]
	 **/
	public Integer getNumeroOrdine() {
		return numeroOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroOrdine]
	 **/
	public void setNumeroOrdine(Integer numeroOrdine)  {
		this.numeroOrdine=numeroOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rigaOrdine]
	 **/
	public Integer getRigaOrdine() {
		return rigaOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rigaOrdine]
	 **/
	public void setRigaOrdine(Integer rigaOrdine)  {
		this.rigaOrdine=rigaOrdine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [consegna]
	 **/
	public Integer getConsegna() {
		return consegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [consegna]
	 **/
	public void setConsegna(Integer consegna)  {
		this.consegna=consegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerzo]
	 **/
	public Integer getCdTerzo() {
		return cdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerzo]
	 **/
	public void setCdTerzo(Integer cdTerzo)  {
		this.cdTerzo=cdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaMisura]
	 **/
	public String getCdUnitaMisura() {
		return cdUnitaMisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaMisura]
	 **/
	public void setCdUnitaMisura(String cdUnitaMisura)  {
		this.cdUnitaMisura=cdUnitaMisura;
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
	 * Restituisce il valore di: [coeffConv]
	 **/
	public java.math.BigDecimal getCoeffConv() {
		return coeffConv;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [coeffConv]
	 **/
	public void setCoeffConv(java.math.BigDecimal coeffConv)  {
		this.coeffConv=coeffConv;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUop]
	 **/
	public String getCdUop() {
		return cdUop;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUop]
	 **/
	public void setCdUop(String cdUop)  {
		this.cdUop=cdUop;
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
	 * Restituisce il valore di: [lottoFornitore]
	 **/
	public String getLottoFornitore() {
		return lottoFornitore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [lottoFornitore]
	 **/
	public void setLottoFornitore(String lottoFornitore)  {
		this.lottoFornitore=lottoFornitore;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsLotto]
	 **/
	public String getCdCdsLotto() {
		return cdCdsLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsLotto]
	 **/
	public void setCdCdsLotto(String cdCdsLotto)  {
		this.cdCdsLotto=cdCdsLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzinoLotto]
	 **/
	public String getCdMagazzinoLotto() {
		return cdMagazzinoLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzinoLotto]
	 **/
	public void setCdMagazzinoLotto(String cdMagazzinoLotto)  {
		this.cdMagazzinoLotto=cdMagazzinoLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioLotto]
	 **/
	public Integer getEsercizioLotto() {
		return esercizioLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioLotto]
	 **/
	public void setEsercizioLotto(Integer esercizioLotto)  {
		this.esercizioLotto=esercizioLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreLotto]
	 **/
	public String getCdNumeratoreLotto() {
		return cdNumeratoreLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreLotto]
	 **/
	public void setCdNumeratoreLotto(String cdNumeratoreLotto)  {
		this.cdNumeratoreLotto=cdNumeratoreLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgLotto]
	 **/
	public Integer getPgLotto() {
		return pgLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgLotto]
	 **/
	public void setPgLotto(Integer pgLotto)  {
		this.pgLotto=pgLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsMag]
	 **/
	public String getCdCdsMag() {
		return cdCdsMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsMag]
	 **/
	public void setCdCdsMag(String cdCdsMag)  {
		this.cdCdsMag=cdCdsMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzino]
	 **/
	public String getCdMagazzino() {
		return cdMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzino]
	 **/
	public void setCdMagazzino(String cdMagazzino)  {
		this.cdMagazzino=cdMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdBeneServizio]
	 **/
	public String getCdBeneServizio() {
		return cdBeneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdBeneServizio]
	 **/
	public void setCdBeneServizio(String cdBeneServizio)  {
		this.cdBeneServizio=cdBeneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsTipoMovimentoRif]
	 **/
	public String getCdCdsTipoMovimentoRif() {
		return cdCdsTipoMovimentoRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsTipoMovimentoRif]
	 **/
	public void setCdCdsTipoMovimentoRif(String cdCdsTipoMovimentoRif)  {
		this.cdCdsTipoMovimentoRif=cdCdsTipoMovimentoRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoRif]
	 **/
	public String getCdTipoMovimentoRif() {
		return cdTipoMovimentoRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoRif]
	 **/
	public void setCdTipoMovimentoRif(String cdTipoMovimentoRif)  {
		this.cdTipoMovimentoRif=cdTipoMovimentoRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdVoceIva]
	 **/
	public String getCdVoceIva() {
		return cdVoceIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdVoceIva]
	 **/
	public void setCdVoceIva(String cdVoceIva)  {
		this.cdVoceIva=cdVoceIva;
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
	 * Restituisce il valore di: [cdCdsBollaSca]
	 **/
	public String getCdCdsBollaSca() {
		return cdCdsBollaSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsBollaSca]
	 **/
	public void setCdCdsBollaSca(String cdCdsBollaSca)  {
		this.cdCdsBollaSca=cdCdsBollaSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzinoBollaSca]
	 **/
	public String getCdMagazzinoBollaSca() {
		return cdMagazzinoBollaSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzinoBollaSca]
	 **/
	public void setCdMagazzinoBollaSca(String cdMagazzinoBollaSca)  {
		this.cdMagazzinoBollaSca=cdMagazzinoBollaSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioBollaSca]
	 **/
	public Integer getEsercizioBollaSca() {
		return esercizioBollaSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioBollaSca]
	 **/
	public void setEsercizioBollaSca(Integer esercizioBollaSca)  {
		this.esercizioBollaSca=esercizioBollaSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreBollaSca]
	 **/
	public String getCdNumeratoreBollaSca() {
		return cdNumeratoreBollaSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreBollaSca]
	 **/
	public void setCdNumeratoreBollaSca(String cdNumeratoreBollaSca)  {
		this.cdNumeratoreBollaSca=cdNumeratoreBollaSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgBollaSca]
	 **/
	public Integer getPgBollaSca() {
		return pgBollaSca;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgBollaSca]
	 **/
	public void setPgBollaSca(Integer pgBollaSca)  {
		this.pgBollaSca=pgBollaSca;
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
	 * Restituisce il valore di: [importoCeff]
	 **/
	public java.math.BigDecimal getImportoCeff() {
		return importoCeff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoCeff]
	 **/
	public void setImportoCeff(java.math.BigDecimal importoCeff)  {
		this.importoCeff=importoCeff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoCmp]
	 **/
	public java.math.BigDecimal getImportoCmp() {
		return importoCmp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoCmp]
	 **/
	public void setImportoCmp(java.math.BigDecimal importoCmp)  {
		this.importoCmp=importoCmp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoFifo]
	 **/
	public java.math.BigDecimal getImportoFifo() {
		return importoFifo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoFifo]
	 **/
	public void setImportoFifo(java.math.BigDecimal importoFifo)  {
		this.importoFifo=importoFifo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoLifo]
	 **/
	public java.math.BigDecimal getImportoLifo() {
		return importoLifo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoLifo]
	 **/
	public void setImportoLifo(java.math.BigDecimal importoLifo)  {
		this.importoLifo=importoLifo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoCmpp]
	 **/
	public java.math.BigDecimal getImportoCmpp() {
		return importoCmpp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoCmpp]
	 **/
	public void setImportoCmpp(java.math.BigDecimal importoCmpp)  {
		this.importoCmpp=importoCmpp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoCmpist]
	 **/
	public java.math.BigDecimal getImportoCmpist() {
		return importoCmpist;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoCmpist]
	 **/
	public void setImportoCmpist(java.math.BigDecimal importoCmpist)  {
		this.importoCmpist=importoCmpist;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdDivisa]
	 **/
	public String getCdDivisa() {
		return cdDivisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdDivisa]
	 **/
	public void setCdDivisa(String cdDivisa)  {
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
	 * Restituisce il valore di: [stato]
	 **/
	public String getStato() {
		return stato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stato]
	 **/
	public void setStato(String stato)  {
		this.stato=stato;
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
	public Long getPgMovimentoRif() {
		return pgMovimentoRif;
	}
	public void setPgMovimentoRif(Long pgMovimentoRif) {
		this.pgMovimentoRif = pgMovimentoRif;
	}
	public Long getPgMovimentoAnn() {
		return pgMovimentoAnn;
	}
	public void setPgMovimentoAnn(Long pgMovimentoAnn) {
		this.pgMovimentoAnn = pgMovimentoAnn;
	}
}