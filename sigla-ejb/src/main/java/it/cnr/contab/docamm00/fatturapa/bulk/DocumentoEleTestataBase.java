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
 * Date 25/02/2015
 */
package it.cnr.contab.docamm00.fatturapa.bulk;
import it.cnr.jada.persistency.Keyed;
public class DocumentoEleTestataBase extends DocumentoEleTestataKey implements Keyed {
//    TIPO_DOCUMENTO VARCHAR(256)
	private java.lang.String tipoDocumento;
 
//    DIVISA VARCHAR(3)
	private java.lang.String divisa;
 
//    DATA_DOCUMENTO TIMESTAMP(7)
	private java.sql.Timestamp dataDocumento;
 
//    NUMERO_DOCUMENTO VARCHAR(20)
	private java.lang.String numeroDocumento;
 
//    BOLLO_VIRTUALE VARCHAR(2)
	private java.lang.String bolloVirtuale;
 
//    IMPORTO_BOLLO DECIMAL(17,2)
	private java.math.BigDecimal importoBollo;
 
//    TIPO_BOLLO VARCHAR(256)
	private java.lang.String tipoBollo;
 
//    IMPORTO_DOCUMENTO DECIMAL(17,2)
	private java.math.BigDecimal importoDocumento;
 
//    ARROTONDAMENTO DECIMAL(17,2)
	private java.math.BigDecimal arrotondamento;
 
//    CAUSALE VARCHAR(1)
	private java.lang.String causale;
 
//    ART73 VARCHAR(2)
	private java.lang.String art73;
 
//    VETTORE_PAESE VARCHAR(256)
	private java.lang.String vettorePaese;
 
//    VETTORE_CODICE VARCHAR(256)
	private java.lang.String vettoreCodice;
 
//    VETTORE_CODICEFISCALE VARCHAR(256)
	private java.lang.String vettoreCodicefiscale;
 
//    VETTORE_DENOMINAZIONE VARCHAR(256)
	private java.lang.String vettoreDenominazione;
 
//    VETTORE_NOME VARCHAR(256)
	private java.lang.String vettoreNome;
 
//    VETTORE_COGNOME VARCHAR(256)
	private java.lang.String vettoreCognome;
 
//    VETTORE_TITOLO VARCHAR(256)
	private java.lang.String vettoreTitolo;
 
//    VETTORE_CODEORI VARCHAR(256)
	private java.lang.String vettoreCodeori;
 
//    VETTORE_NUMEROLICENZA VARCHAR(256)
	private java.lang.String vettoreNumerolicenza;
 
//    MEZZO_TRASPORTO VARCHAR(256)
	private java.lang.String mezzoTrasporto;
 
//    CAUSALE_TRASPORTO VARCHAR(256)
	private java.lang.String causaleTrasporto;
 
//    NUMERO_COLLI NUMBER
	private java.lang.Integer numeroColli;
 
//    DESCRIZIONE_TRASPORTO VARCHAR(256)
	private java.lang.String descrizioneTrasporto;
 
//    UNITA_MISURAPESO VARCHAR(256)
	private java.lang.String unitaMisurapeso;
 
//    PESO_LORDO DECIMAL(17,2)
	private java.math.BigDecimal pesoLordo;
 
//    PESO_NETTO DECIMAL(17,2)
	private java.math.BigDecimal pesoNetto;
 
//    DATAORA_RITIRO TIMESTAMP(7)
	private java.sql.Timestamp dataoraRitiro;
 
//    DATAINIZIO_TRASPORTO TIMESTAMP(7)
	private java.sql.Timestamp datainizioTrasporto;
 
//    TIPO_RESA VARCHAR(256)
	private java.lang.String tipoResa;
 
//    RESA_INDIRIZZO VARCHAR(256)
	private java.lang.String resaIndirizzo;
 
//    RESA_NUMEROCIVICO VARCHAR(256)
	private java.lang.String resaNumerocivico;
 
//    RESA_CAP VARCHAR(256)
	private java.lang.String resaCap;
 
//    RESA_COMUNE VARCHAR(256)
	private java.lang.String resaComune;
 
//    RESA_PROVINCIA VARCHAR(256)
	private java.lang.String resaProvincia;
 
//    RESA_NAZIONE VARCHAR(256)
	private java.lang.String resaNazione;
 
//    DATAORA_CONSEGNA TIMESTAMP(7)
	private java.sql.Timestamp dataoraConsegna;
 
//    NUMERO_FATTURAPRINCIPALE VARCHAR(256)
	private java.lang.String numeroFatturaprincipale;
 
//    DATA_FATTURAPRINCIPALE TIMESTAMP(7)
	private java.sql.Timestamp dataFatturaprincipale;
 
//    DATA_IMMATRICOLAZIONEVEICOLO TIMESTAMP(7)
	private java.sql.Timestamp dataImmatricolazioneveicolo;
 
//    TOTALE_PERCORSOVEICOLO VARCHAR(256)
	private java.lang.String totalePercorsoveicolo;
 
//    BENEFICIARIO_PAGAMENTO VARCHAR(256)
	private java.lang.String beneficiarioPagamento;
 
//    BENEFICIARIO_MOD_PAG VARCHAR(4)
	private java.lang.String beneficiarioModPag;
 
//    DATATERMINI_PAGAMENTO TIMESTAMP(7)
	private java.sql.Timestamp dataterminiPagamento;
 
//    GIORNITERMINI_PAGAMENTO DECIMAL(3,0)
	private java.lang.Integer giorniterminiPagamento;
 
//    DATASCADENZA_PAGAMENTO TIMESTAMP(7)
	private java.sql.Timestamp datascadenzaPagamento;
 
//    IMPORTO_PAGAMENTO DECIMAL(17,2)
	private java.math.BigDecimal importoPagamento;
 
//    CODUFFICIOPOSTALE VARCHAR(256)
	private java.lang.String codufficiopostale;
 
//    COGNOME_QUIETANZANTE VARCHAR(256)
	private java.lang.String cognomeQuietanzante;
 
//    NOME_QUIETANZANTE VARCHAR(256)
	private java.lang.String nomeQuietanzante;
 
//    ISTITUTO_FINANZIARIO VARCHAR(256)
	private java.lang.String istitutoFinanziario;
 
//    IBAN VARCHAR(256)
	private java.lang.String iban;
 
//    ABI VARCHAR(256)
	private java.lang.String abi;
 
//    CAB VARCHAR(256)
	private java.lang.String cab;
 
//    BIC VARCHAR(256)
	private java.lang.String bic;
 
//    PAGAMENTO_CD_TERZO DECIMAL(8,0)
	private java.lang.Integer pagamentoCdTerzo;
 
//    PAGAMENTO_CD_MODALITA_PAG VARCHAR(10)
	private java.lang.String pagamentoCdModalitaPag;
 
//    SCONTO_PAGAMENTO_ANT DECIMAL(17,2)
	private java.math.BigDecimal scontoPagamentoAnt;
 
//    DATALIMITE_PAGAMENTO_ANT TIMESTAMP(7)
	private java.sql.Timestamp datalimitePagamentoAnt;
 
//    PENALITA_PAG_RITARDATI DECIMAL(17,2)
	private java.math.BigDecimal penalitaPagRitardati;
 
//    DATA_RICORRENZAPENALE TIMESTAMP(7)
	private java.sql.Timestamp dataRicorrenzapenale;
 
//    CODICE_PAGAMENTO VARCHAR(60)
	private java.lang.String codicePagamento;
 
//    STATO_DOCUMENTO VARCHAR(60)
	private java.lang.String statoDocumento;
	
//  FL_DECORRENZA_TERMINI VARCHAR(1)
	private java.lang.String flDecorrenzaTermini;
 
//  STATO_NOTIFICA_ESITO VARCHAR(3)
	private java.lang.String statoNotificaEsito;
	 
//    MOTIVO_RIFIUTO VARCHAR(2000)
	private java.lang.String motivoRifiuto;
 
//  DATA_RICEVIMENTO_MAIL_RIFIUTO TIMESTAMP(7)
	private java.sql.Timestamp dataRicevimentoMailRifiuto;

//    ANOMALIE VARCHAR(2000)
	private java.lang.String anomalie;
	//  CD_UNITA_COMPETENZA VARCHAR(30)
	private java.lang.String cdUnitaCompetenza;
	
	private java.lang.String flIrregistrabile;

	private java.lang.String idPaeseFatCol;
	private java.lang.String idCodiceFatCol;
	private java.lang.Long identificativoSdiFatCol;
	private java.lang.Long progressivoFatCol;
	//  ESITO_PCC VARCHAR(30)
	private java.lang.String esitoPCC;

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DOCUMENTO_ELE_TESTATA
	 **/
	public DocumentoEleTestataBase() {
		super();
	}
	public DocumentoEleTestataBase(java.lang.String idPaese, java.lang.String idCodice, java.lang.Long identificativoSdi, java.lang.Long progressivo) {
		super(idPaese, idCodice, identificativoSdi, progressivo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoDocumento]
	 **/
	public java.lang.String getTipoDocumento() {
		return tipoDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoDocumento]
	 **/
	public void setTipoDocumento(java.lang.String tipoDocumento)  {
		this.tipoDocumento=tipoDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [divisa]
	 **/
	public java.lang.String getDivisa() {
		return divisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [divisa]
	 **/
	public void setDivisa(java.lang.String divisa)  {
		this.divisa=divisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataDocumento]
	 **/
	public java.sql.Timestamp getDataDocumento() {
		return dataDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataDocumento]
	 **/
	public void setDataDocumento(java.sql.Timestamp dataDocumento)  {
		this.dataDocumento=dataDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroDocumento]
	 **/
	public java.lang.String getNumeroDocumento() {
		return numeroDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroDocumento]
	 **/
	public void setNumeroDocumento(java.lang.String numeroDocumento)  {
		this.numeroDocumento=numeroDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [bolloVirtuale]
	 **/
	public java.lang.String getBolloVirtuale() {
		return bolloVirtuale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [bolloVirtuale]
	 **/
	public void setBolloVirtuale(java.lang.String bolloVirtuale)  {
		this.bolloVirtuale=bolloVirtuale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoBollo]
	 **/
	public java.math.BigDecimal getImportoBollo() {
		return importoBollo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoBollo]
	 **/
	public void setImportoBollo(java.math.BigDecimal importoBollo)  {
		this.importoBollo=importoBollo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoBollo]
	 **/
	public java.lang.String getTipoBollo() {
		return tipoBollo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoBollo]
	 **/
	public void setTipoBollo(java.lang.String tipoBollo)  {
		this.tipoBollo=tipoBollo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoDocumento]
	 **/
	public java.math.BigDecimal getImportoDocumento() {
		return importoDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoDocumento]
	 **/
	public void setImportoDocumento(java.math.BigDecimal importoDocumento)  {	
		this.importoDocumento=importoDocumento;
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
	 * Restituisce il valore di: [causale]
	 **/
	public java.lang.String getCausale() {
		return causale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [causale]
	 **/
	public void setCausale(java.lang.String causale)  {
		this.causale=causale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [art73]
	 **/
	public java.lang.String getArt73() {
		return art73;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [art73]
	 **/
	public void setArt73(java.lang.String art73)  {
		this.art73=art73;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [vettorePaese]
	 **/
	public java.lang.String getVettorePaese() {
		return vettorePaese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [vettorePaese]
	 **/
	public void setVettorePaese(java.lang.String vettorePaese)  {
		this.vettorePaese=vettorePaese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [vettoreCodice]
	 **/
	public java.lang.String getVettoreCodice() {
		return vettoreCodice;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [vettoreCodice]
	 **/
	public void setVettoreCodice(java.lang.String vettoreCodice)  {
		this.vettoreCodice=vettoreCodice;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [vettoreCodicefiscale]
	 **/
	public java.lang.String getVettoreCodicefiscale() {
		return vettoreCodicefiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [vettoreCodicefiscale]
	 **/
	public void setVettoreCodicefiscale(java.lang.String vettoreCodicefiscale)  {
		this.vettoreCodicefiscale=vettoreCodicefiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [vettoreDenominazione]
	 **/
	public java.lang.String getVettoreDenominazione() {
		return vettoreDenominazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [vettoreDenominazione]
	 **/
	public void setVettoreDenominazione(java.lang.String vettoreDenominazione)  {
		this.vettoreDenominazione=vettoreDenominazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [vettoreNome]
	 **/
	public java.lang.String getVettoreNome() {
		return vettoreNome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [vettoreNome]
	 **/
	public void setVettoreNome(java.lang.String vettoreNome)  {
		this.vettoreNome=vettoreNome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [vettoreCognome]
	 **/
	public java.lang.String getVettoreCognome() {
		return vettoreCognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [vettoreCognome]
	 **/
	public void setVettoreCognome(java.lang.String vettoreCognome)  {
		this.vettoreCognome=vettoreCognome;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [vettoreTitolo]
	 **/
	public java.lang.String getVettoreTitolo() {
		return vettoreTitolo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [vettoreTitolo]
	 **/
	public void setVettoreTitolo(java.lang.String vettoreTitolo)  {
		this.vettoreTitolo=vettoreTitolo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [vettoreCodeori]
	 **/
	public java.lang.String getVettoreCodeori() {
		return vettoreCodeori;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [vettoreCodeori]
	 **/
	public void setVettoreCodeori(java.lang.String vettoreCodeori)  {
		this.vettoreCodeori=vettoreCodeori;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [vettoreNumerolicenza]
	 **/
	public java.lang.String getVettoreNumerolicenza() {
		return vettoreNumerolicenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [vettoreNumerolicenza]
	 **/
	public void setVettoreNumerolicenza(java.lang.String vettoreNumerolicenza)  {
		this.vettoreNumerolicenza=vettoreNumerolicenza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [mezzoTrasporto]
	 **/
	public java.lang.String getMezzoTrasporto() {
		return mezzoTrasporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [mezzoTrasporto]
	 **/
	public void setMezzoTrasporto(java.lang.String mezzoTrasporto)  {
		this.mezzoTrasporto=mezzoTrasporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [causaleTrasporto]
	 **/
	public java.lang.String getCausaleTrasporto() {
		return causaleTrasporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [causaleTrasporto]
	 **/
	public void setCausaleTrasporto(java.lang.String causaleTrasporto)  {
		this.causaleTrasporto=causaleTrasporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroColli]
	 **/
	public java.lang.Integer getNumeroColli() {
		return numeroColli;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroColli]
	 **/
	public void setNumeroColli(java.lang.Integer numeroColli)  {
		this.numeroColli=numeroColli;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [descrizioneTrasporto]
	 **/
	public java.lang.String getDescrizioneTrasporto() {
		return descrizioneTrasporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [descrizioneTrasporto]
	 **/
	public void setDescrizioneTrasporto(java.lang.String descrizioneTrasporto)  {
		this.descrizioneTrasporto=descrizioneTrasporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [unitaMisurapeso]
	 **/
	public java.lang.String getUnitaMisurapeso() {
		return unitaMisurapeso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [unitaMisurapeso]
	 **/
	public void setUnitaMisurapeso(java.lang.String unitaMisurapeso)  {
		this.unitaMisurapeso=unitaMisurapeso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pesoLordo]
	 **/
	public java.math.BigDecimal getPesoLordo() {
		return pesoLordo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pesoLordo]
	 **/
	public void setPesoLordo(java.math.BigDecimal pesoLordo)  {
		this.pesoLordo=pesoLordo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pesoNetto]
	 **/
	public java.math.BigDecimal getPesoNetto() {
		return pesoNetto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pesoNetto]
	 **/
	public void setPesoNetto(java.math.BigDecimal pesoNetto)  {
		this.pesoNetto=pesoNetto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataoraRitiro]
	 **/
	public java.sql.Timestamp getDataoraRitiro() {
		return dataoraRitiro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataoraRitiro]
	 **/
	public void setDataoraRitiro(java.sql.Timestamp dataoraRitiro)  {
		this.dataoraRitiro=dataoraRitiro;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [datainizioTrasporto]
	 **/
	public java.sql.Timestamp getDatainizioTrasporto() {
		return datainizioTrasporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [datainizioTrasporto]
	 **/
	public void setDatainizioTrasporto(java.sql.Timestamp datainizioTrasporto)  {
		this.datainizioTrasporto=datainizioTrasporto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoResa]
	 **/
	public java.lang.String getTipoResa() {
		return tipoResa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoResa]
	 **/
	public void setTipoResa(java.lang.String tipoResa)  {
		this.tipoResa=tipoResa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [resaIndirizzo]
	 **/
	public java.lang.String getResaIndirizzo() {
		return resaIndirizzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [resaIndirizzo]
	 **/
	public void setResaIndirizzo(java.lang.String resaIndirizzo)  {
		this.resaIndirizzo=resaIndirizzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreNumerocivico]
	 **/
	public java.lang.String getResaNumerocivico() {
		return resaNumerocivico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreNumerocivico]
	 **/
	public void setResaNumerocivico(java.lang.String resaNumerocivico)  {
		this.resaNumerocivico=resaNumerocivico;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [resaCap]
	 **/
	public java.lang.String getResaCap() {
		return resaCap;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [resaCap]
	 **/
	public void setResaCap(java.lang.String resaCap)  {
		this.resaCap=resaCap;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [resaComune]
	 **/
	public java.lang.String getResaComune() {
		return resaComune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [resaComune]
	 **/
	public void setResaComune(java.lang.String resaComune)  {
		this.resaComune=resaComune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [resaProvincia]
	 **/
	public java.lang.String getResaProvincia() {
		return resaProvincia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [resaProvincia]
	 **/
	public void setResaProvincia(java.lang.String resaProvincia)  {
		this.resaProvincia=resaProvincia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [resaNazione]
	 **/
	public java.lang.String getResaNazione() {
		return resaNazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [resaNazione]
	 **/
	public void setResaNazione(java.lang.String resaNazione)  {
		this.resaNazione=resaNazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataoraConsegna]
	 **/
	public java.sql.Timestamp getDataoraConsegna() {
		return dataoraConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataoraConsegna]
	 **/
	public void setDataoraConsegna(java.sql.Timestamp dataoraConsegna)  {
		this.dataoraConsegna=dataoraConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroFatturaprincipale]
	 **/
	public java.lang.String getNumeroFatturaprincipale() {
		return numeroFatturaprincipale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroFatturaprincipale]
	 **/
	public void setNumeroFatturaprincipale(java.lang.String numeroFatturaprincipale)  {
		this.numeroFatturaprincipale=numeroFatturaprincipale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataFatturaprincipale]
	 **/
	public java.sql.Timestamp getDataFatturaprincipale() {
		return dataFatturaprincipale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataFatturaprincipale]
	 **/
	public void setDataFatturaprincipale(java.sql.Timestamp dataFatturaprincipale)  {
		this.dataFatturaprincipale=dataFatturaprincipale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataImmatricolazioneveicolo]
	 **/
	public java.sql.Timestamp getDataImmatricolazioneveicolo() {
		return dataImmatricolazioneveicolo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataImmatricolazioneveicolo]
	 **/
	public void setDataImmatricolazioneveicolo(java.sql.Timestamp dataImmatricolazioneveicolo)  {
		this.dataImmatricolazioneveicolo=dataImmatricolazioneveicolo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [totalePercorsoveicolo]
	 **/
	public java.lang.String getTotalePercorsoveicolo() {
		return totalePercorsoveicolo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [totalePercorsoveicolo]
	 **/
	public void setTotalePercorsoveicolo(java.lang.String totalePercorsoveicolo)  {
		this.totalePercorsoveicolo=totalePercorsoveicolo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [beneficiarioPagamento]
	 **/
	public java.lang.String getBeneficiarioPagamento() {
		return beneficiarioPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [beneficiarioPagamento]
	 **/
	public void setBeneficiarioPagamento(java.lang.String beneficiarioPagamento)  {
		this.beneficiarioPagamento=beneficiarioPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [beneficiarioModPag]
	 **/
	public java.lang.String getBeneficiarioModPag() {
		return beneficiarioModPag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [beneficiarioModPag]
	 **/
	public void setBeneficiarioModPag(java.lang.String beneficiarioModPag)  {
		this.beneficiarioModPag=beneficiarioModPag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataterminiPagamento]
	 **/
	public java.sql.Timestamp getDataterminiPagamento() {
		return dataterminiPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataterminiPagamento]
	 **/
	public void setDataterminiPagamento(java.sql.Timestamp dataterminiPagamento)  {
		this.dataterminiPagamento=dataterminiPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [giorniterminiPagamento]
	 **/
	public java.lang.Integer getGiorniterminiPagamento() {
		return giorniterminiPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [giorniterminiPagamento]
	 **/
	public void setGiorniterminiPagamento(java.lang.Integer giorniterminiPagamento)  {
		this.giorniterminiPagamento=giorniterminiPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [datascadenzaPagamento]
	 **/
	public java.sql.Timestamp getDatascadenzaPagamento() {
		return datascadenzaPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [datascadenzaPagamento]
	 **/
	public void setDatascadenzaPagamento(java.sql.Timestamp datascadenzaPagamento)  {
		this.datascadenzaPagamento=datascadenzaPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoPagamento]
	 **/
	public java.math.BigDecimal getImportoPagamento() {
		return importoPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoPagamento]
	 **/
	public void setImportoPagamento(java.math.BigDecimal importoPagamento)  {
		this.importoPagamento=importoPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codufficiopostale]
	 **/
	public java.lang.String getCodufficiopostale() {
		return codufficiopostale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codufficiopostale]
	 **/
	public void setCodufficiopostale(java.lang.String codufficiopostale)  {
		this.codufficiopostale=codufficiopostale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cognomeQuietanzante]
	 **/
	public java.lang.String getCognomeQuietanzante() {
		return cognomeQuietanzante;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cognomeQuietanzante]
	 **/
	public void setCognomeQuietanzante(java.lang.String cognomeQuietanzante)  {
		this.cognomeQuietanzante=cognomeQuietanzante;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [nomeQuietanzante]
	 **/
	public java.lang.String getNomeQuietanzante() {
		return nomeQuietanzante;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [nomeQuietanzante]
	 **/
	public void setNomeQuietanzante(java.lang.String nomeQuietanzante)  {
		this.nomeQuietanzante=nomeQuietanzante;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [istitutoFinanziario]
	 **/
	public java.lang.String getIstitutoFinanziario() {
		return istitutoFinanziario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [istitutoFinanziario]
	 **/
	public void setIstitutoFinanziario(java.lang.String istitutoFinanziario)  {
		this.istitutoFinanziario=istitutoFinanziario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [iban]
	 **/
	public java.lang.String getIban() {
		return iban;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [iban]
	 **/
	public void setIban(java.lang.String iban)  {
		this.iban=iban;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [abi]
	 **/
	public java.lang.String getAbi() {
		return abi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [abi]
	 **/
	public void setAbi(java.lang.String abi)  {
		this.abi=abi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cab]
	 **/
	public java.lang.String getCab() {
		return cab;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cab]
	 **/
	public void setCab(java.lang.String cab)  {
		this.cab=cab;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [bic]
	 **/
	public java.lang.String getBic() {
		return bic;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [bic]
	 **/
	public void setBic(java.lang.String bic)  {
		this.bic=bic;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pagamentoCdTerzo]
	 **/
	public java.lang.Integer getPagamentoCdTerzo() {
		return pagamentoCdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pagamentoCdTerzo]
	 **/
	public void setPagamentoCdTerzo(java.lang.Integer pagamentoCdTerzo)  {
		this.pagamentoCdTerzo=pagamentoCdTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pagamentoCdModalitaPag]
	 **/
	public java.lang.String getPagamentoCdModalitaPag() {
		return pagamentoCdModalitaPag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pagamentoCdModalitaPag]
	 **/
	public void setPagamentoCdModalitaPag(java.lang.String pagamentoCdModalitaPag)  {
		this.pagamentoCdModalitaPag=pagamentoCdModalitaPag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [scontoPagamentoAnt]
	 **/
	public java.math.BigDecimal getScontoPagamentoAnt() {
		return scontoPagamentoAnt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [scontoPagamentoAnt]
	 **/
	public void setScontoPagamentoAnt(java.math.BigDecimal scontoPagamentoAnt)  {
		this.scontoPagamentoAnt=scontoPagamentoAnt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [datalimitePagamentoAnt]
	 **/
	public java.sql.Timestamp getDatalimitePagamentoAnt() {
		return datalimitePagamentoAnt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [datalimitePagamentoAnt]
	 **/
	public void setDatalimitePagamentoAnt(java.sql.Timestamp datalimitePagamentoAnt)  {
		this.datalimitePagamentoAnt=datalimitePagamentoAnt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [penalitaPagRitardati]
	 **/
	public java.math.BigDecimal getPenalitaPagRitardati() {
		return penalitaPagRitardati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [penalitaPagRitardati]
	 **/
	public void setPenalitaPagRitardati(java.math.BigDecimal penalitaPagRitardati)  {
		this.penalitaPagRitardati=penalitaPagRitardati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataRicorrenzapenale]
	 **/
	public java.sql.Timestamp getDataRicorrenzapenale() {
		return dataRicorrenzapenale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataRicorrenzapenale]
	 **/
	public void setDataRicorrenzapenale(java.sql.Timestamp dataRicorrenzapenale)  {
		this.dataRicorrenzapenale=dataRicorrenzapenale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codicePagamento]
	 **/
	public java.lang.String getCodicePagamento() {
		return codicePagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codicePagamento]
	 **/
	public void setCodicePagamento(java.lang.String codicePagamento)  {
		this.codicePagamento=codicePagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [statoDocumento]
	 **/
	public java.lang.String getStatoDocumento() {
		return statoDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [statoDocumento]
	 **/
	public void setStatoDocumento(java.lang.String statoDocumento)  {
		this.statoDocumento=statoDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [motivoRifiuto]
	 **/
	public java.lang.String getMotivoRifiuto() {
		return motivoRifiuto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [motivoRifiuto]
	 **/
	public void setMotivoRifiuto(java.lang.String motivoRifiuto)  {
		this.motivoRifiuto=motivoRifiuto;
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
	public java.lang.String getFlDecorrenzaTermini() {
		return flDecorrenzaTermini;
	}
	public void setFlDecorrenzaTermini(java.lang.String flDecorrenzaTermini) {
		this.flDecorrenzaTermini = flDecorrenzaTermini;
	}
	public java.lang.String getStatoNotificaEsito() {
		return statoNotificaEsito;
	}
	public void setStatoNotificaEsito(java.lang.String statoNotificaEsito) {
		this.statoNotificaEsito = statoNotificaEsito;
	}
	public java.sql.Timestamp getDataRicevimentoMailRifiuto() {
		return dataRicevimentoMailRifiuto;
	}
	public void setDataRicevimentoMailRifiuto(
			java.sql.Timestamp dataRicevimentoMailRifiuto) {
		this.dataRicevimentoMailRifiuto = dataRicevimentoMailRifiuto;
	}
	public java.lang.String getFlIrregistrabile() {
		return flIrregistrabile;
	}
	public void setFlIrregistrabile(java.lang.String flIrregistrabile) {
		this.flIrregistrabile = flIrregistrabile;
	}
	public java.lang.String getCdUnitaCompetenza() {
		return cdUnitaCompetenza;
	}
	public void setCdUnitaCompetenza(java.lang.String cdUnitaCompetenza) {
		this.cdUnitaCompetenza = cdUnitaCompetenza;
	}

	public String getIdPaeseFatCol() {
		return idPaeseFatCol;
	}

	public void setIdPaeseFatCol(String idPaeseFatCol) {
		this.idPaeseFatCol = idPaeseFatCol;
	}

	public String getIdCodiceFatCol() {
		return idCodiceFatCol;
	}

	public void setIdCodiceFatCol(String idCodiceFatCol) {
		this.idCodiceFatCol = idCodiceFatCol;
	}

	public Long getIdentificativoSdiFatCol() {
		return identificativoSdiFatCol;
	}

	public void setIdentificativoSdiFatCol(Long identificativoSdiFatCol) {
		this.identificativoSdiFatCol = identificativoSdiFatCol;
	}

	public Long getProgressivoFatCol() {
		return progressivoFatCol;
	}

	public void setProgressivoFatCol(Long progressivoFatCol) {
		this.progressivoFatCol = progressivoFatCol;
	}

	public String getEsitoPCC() {
		return esitoPCC;
	}

	public void setEsitoPCC(String esitoPCC) {
		this.esitoPCC = esitoPCC;
	}
}