/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 19/07/2013
 */
package it.cnr.contab.doccont00.intcass.bulk;
import java.math.BigDecimal;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class VDocumentiFlussoBase extends OggettoBulk implements Persistent {
//    CD_CDS VARCHAR(30)
	private java.lang.String cdCds;
 
//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    PG_DOCUMENTO DECIMAL(10,0)
	private java.lang.Long pgDocumento;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cdUnitaOrganizzativa;
 
//    CD_CDS_ORIGINE VARCHAR(30)
	private java.lang.String cdCdsOrigine;
 
//    CD_UO_ORIGINE VARCHAR(30)
	private java.lang.String cdUoOrigine;
 
//    CD_TIPO_DOCUMENTO_CONT VARCHAR(10)
	private java.lang.String cdTipoDocumentoCont;
 
//    TI_DOCUMENTO CHAR(1)
	private java.lang.String tiDocumento;
 
//    TI_COMPETENZA_RESIDUO CHAR(1)
	private java.lang.String tiCompetenzaResiduo;
 
//    DS_DOCUMENTO VARCHAR(1200)
	private java.lang.String dsDocumento;
 
//    STATO CHAR(1)
	private java.lang.String stato;
 
//    DT_EMISSIONE TIMESTAMP(7)
	private java.sql.Timestamp dtEmissione;
 
//    DT_TRASMISSIONE TIMESTAMP(7)
	private java.sql.Timestamp dtTrasmissione;
 
//    DT_PAGAMENTO TIMESTAMP(7)
	private java.sql.Timestamp dtPagamento;
 
//    DT_ANNULLAMENTO TIMESTAMP(7)
	private java.sql.Timestamp dtAnnullamento;
 
//    IM_DOCUMENTO DECIMAL(15,2)
	private java.math.BigDecimal imDocumento;
 
//    IM_PAGATO DECIMAL(15,2)
	private java.math.BigDecimal imPagato;
 
//    STATO_TRASMISSIONE CHAR(1)
	private java.lang.String statoTrasmissione;
 
//    DT_RITRASMISSIONE TIMESTAMP(7)
	private java.sql.Timestamp dtRitrasmissione;
 
//    CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cdTerzo;
 
//    CD_ANAG DECIMAL(8,0)
	private java.lang.Integer cdAnag;
 
//    DENOMINAZIONE_SEDE VARCHAR(200)
	private java.lang.String denominazioneSede;
 
//    CD_SIOPE VARCHAR(45)
	private java.lang.String cdSiope;
 
//    CD_CUP VARCHAR(15)
	private java.lang.String cdCup;
 
//    DT_REGISTRAZIONE_SOSP TIMESTAMP(7)
	private java.sql.Timestamp dtRegistrazioneSosp;
 
//    TI_ENTRATA_SPESA CHAR(1)
	private java.lang.String tiEntrataSpesa;
 
//    CD_SOSPESO VARCHAR(24)
	private java.lang.String cdSospeso;
 
//    IM_SOSPESO DECIMAL(15,2)
	private java.math.BigDecimal imSospeso;
 
//    VIA_SEDE VARCHAR(100)
	private java.lang.String viaSede;
 
//    CAP_COMUNE_SEDE VARCHAR(20)
	private java.lang.String capComuneSede;
 
//    DS_COMUNE VARCHAR(100)
	private java.lang.String dsComune;
 
//    CD_PROVINCIA VARCHAR(10)
	private java.lang.String cdProvincia;

	//    PARTITA_IVA VARCHAR(20)
	private java.lang.String partitaIva;

//    CODICE_FISCALE VARCHAR(20)
	private java.lang.String codiceFiscale;
 
//    NUMERO_CONTO VARCHAR(30)
	private java.lang.String numeroConto;
	
//  IBAN VARCHAR(34)
	private java.lang.String codiceIban;
	
//  ABI VARCHAR(5)
	private java.lang.String abi;
	
//  CAB VARCHAR(5)	
	private java.lang.String cab;
	
//  cin VARCHAR(1)	
	private java.lang.String cin;
 
//    CD_ISO VARCHAR(10)
	private java.lang.String cdIso;
 
//    IM_ASSOCIATO DECIMAL(22,0)
	private java.math.BigDecimal imAssociato;
 
//    IMPORTO_CGE DECIMAL(22,0)
	private java.math.BigDecimal importoCge;
 
//    IMPORTO_CUP DECIMAL(22,0)
	private java.math.BigDecimal importoCup;
	
	private java.lang.String assoggettamentoBollo;
	
	private java.lang.String causaleBollo;
	
	private java.lang.String bic;
	
	private java.lang.String cdTipoDocumentoAmm;
	
	private java.lang.Long pgDocAmm;
	
	private java.lang.String modalitaPagamento;

	private java.sql.Timestamp dtPagamentoRichiesta;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_DOCUMENTI_FLUSSO
	 **/
	
	public VDocumentiFlussoBase() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		return cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.cdCds=cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgDocumento]
	 **/
	public java.lang.Long getPgDocumento() {
		return pgDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgDocumento]
	 **/
	public void setPgDocumento(java.lang.Long pgDocumento)  {
		this.pgDocumento=pgDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOrganizzativa]
	 **/
	public java.lang.String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOrganizzativa]
	 **/
	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa)  {
		this.cdUnitaOrganizzativa=cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsOrigine]
	 **/
	public java.lang.String getCdCdsOrigine() {
		return cdCdsOrigine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsOrigine]
	 **/
	public void setCdCdsOrigine(java.lang.String cdCdsOrigine)  {
		this.cdCdsOrigine=cdCdsOrigine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUoOrigine]
	 **/
	public java.lang.String getCdUoOrigine() {
		return cdUoOrigine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUoOrigine]
	 **/
	public void setCdUoOrigine(java.lang.String cdUoOrigine)  {
		this.cdUoOrigine=cdUoOrigine;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoDocumentoCont]
	 **/
	public java.lang.String getCdTipoDocumentoCont() {
		return cdTipoDocumentoCont;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoDocumentoCont]
	 **/
	public void setCdTipoDocumentoCont(java.lang.String cdTipoDocumentoCont)  {
		this.cdTipoDocumentoCont=cdTipoDocumentoCont;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiDocumento]
	 **/
	public java.lang.String getTiDocumento() {
		return tiDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiDocumento]
	 **/
	public void setTiDocumento(java.lang.String tiDocumento)  {
		this.tiDocumento=tiDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiCompetenzaResiduo]
	 **/
	public java.lang.String getTiCompetenzaResiduo() {
		return tiCompetenzaResiduo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiCompetenzaResiduo]
	 **/
	public void setTiCompetenzaResiduo(java.lang.String tiCompetenzaResiduo)  {
		this.tiCompetenzaResiduo=tiCompetenzaResiduo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsDocumento]
	 **/
	public java.lang.String getDsDocumento() {
		return dsDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsDocumento]
	 **/
	public void setDsDocumento(java.lang.String dsDocumento)  {
		this.dsDocumento=dsDocumento;
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
	 * Restituisce il valore di: [dtEmissione]
	 **/
	public java.sql.Timestamp getDtEmissione() {
		return dtEmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtEmissione]
	 **/
	public void setDtEmissione(java.sql.Timestamp dtEmissione)  {
		this.dtEmissione=dtEmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtTrasmissione]
	 **/
	public java.sql.Timestamp getDtTrasmissione() {
		return dtTrasmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtTrasmissione]
	 **/
	public void setDtTrasmissione(java.sql.Timestamp dtTrasmissione)  {
		this.dtTrasmissione=dtTrasmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtPagamento]
	 **/
	public java.sql.Timestamp getDtPagamento() {
		return dtPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtPagamento]
	 **/
	public void setDtPagamento(java.sql.Timestamp dtPagamento)  {
		this.dtPagamento=dtPagamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtAnnullamento]
	 **/
	public java.sql.Timestamp getDtAnnullamento() {
		return dtAnnullamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtAnnullamento]
	 **/
	public void setDtAnnullamento(java.sql.Timestamp dtAnnullamento)  {
		this.dtAnnullamento=dtAnnullamento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imDocumento]
	 **/
	public java.math.BigDecimal getImDocumento() {
		return imDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imDocumento]
	 **/
	public void setImDocumento(java.math.BigDecimal imDocumento)  {
		this.imDocumento=imDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imPagato]
	 **/
	public java.math.BigDecimal getImPagato() {
		return imPagato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imPagato]
	 **/
	public void setImPagato(java.math.BigDecimal imPagato)  {
		this.imPagato=imPagato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [statoTrasmissione]
	 **/
	public java.lang.String getStatoTrasmissione() {
		return statoTrasmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [statoTrasmissione]
	 **/
	public void setStatoTrasmissione(java.lang.String statoTrasmissione)  {
		this.statoTrasmissione=statoTrasmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtRitrasmissione]
	 **/
	public java.sql.Timestamp getDtRitrasmissione() {
		return dtRitrasmissione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtRitrasmissione]
	 **/
	public void setDtRitrasmissione(java.sql.Timestamp dtRitrasmissione)  {
		this.dtRitrasmissione=dtRitrasmissione;
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
	 * Restituisce il valore di: [cdAnag]
	 **/
	public java.lang.Integer getCdAnag() {
		return cdAnag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdAnag]
	 **/
	public void setCdAnag(java.lang.Integer cdAnag)  {
		this.cdAnag=cdAnag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [denominazioneSede]
	 **/
	public java.lang.String getDenominazioneSede() {
		return denominazioneSede;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [denominazioneSede]
	 **/
	public void setDenominazioneSede(java.lang.String denominazioneSede)  {
		this.denominazioneSede=denominazioneSede;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdSiope]
	 **/
	public java.lang.String getCdSiope() {
		return cdSiope;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdSiope]
	 **/
	public void setCdSiope(java.lang.String cdSiope)  {
		this.cdSiope=cdSiope;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCup]
	 **/
	public java.lang.String getCdCup() {
		return cdCup;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCup]
	 **/
	public void setCdCup(java.lang.String cdCup)  {
		this.cdCup=cdCup;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtRegistrazioneSosp]
	 **/
	public java.sql.Timestamp getDtRegistrazioneSosp() {
		return dtRegistrazioneSosp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtRegistrazioneSosp]
	 **/
	public void setDtRegistrazioneSosp(java.sql.Timestamp dtRegistrazioneSosp)  {
		this.dtRegistrazioneSosp=dtRegistrazioneSosp;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiEntrataSpesa]
	 **/
	public java.lang.String getTiEntrataSpesa() {
		return tiEntrataSpesa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiEntrataSpesa]
	 **/
	public void setTiEntrataSpesa(java.lang.String tiEntrataSpesa)  {
		this.tiEntrataSpesa=tiEntrataSpesa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdSospeso]
	 **/
	public java.lang.String getCdSospeso() {
		return cdSospeso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdSospeso]
	 **/
	public void setCdSospeso(java.lang.String cdSospeso)  {
		this.cdSospeso=cdSospeso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imSospeso]
	 **/
	public java.math.BigDecimal getImSospeso() {
		return imSospeso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imSospeso]
	 **/
	public void setImSospeso(java.math.BigDecimal imSospeso)  {
		this.imSospeso=imSospeso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [viaSede]
	 **/
	public java.lang.String getViaSede() {
		return viaSede;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [viaSede]
	 **/
	public void setViaSede(java.lang.String viaSede)  {
		this.viaSede=viaSede;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [capComuneSede]
	 **/
	public java.lang.String getCapComuneSede() {
		return capComuneSede;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [capComuneSede]
	 **/
	public void setCapComuneSede(java.lang.String capComuneSede)  {
		this.capComuneSede=capComuneSede;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsComune]
	 **/
	public java.lang.String getDsComune() {
		return dsComune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsComune]
	 **/
	public void setDsComune(java.lang.String dsComune)  {
		this.dsComune=dsComune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdProvincia]
	 **/
	public java.lang.String getCdProvincia() {
		return cdProvincia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdProvincia]
	 **/
	public void setCdProvincia(java.lang.String cdProvincia)  {
		this.cdProvincia=cdProvincia;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codiceFiscale]
	 **/
	public java.lang.String getCodiceFiscale() {
		return codiceFiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codiceFiscale]
	 **/
	public void setCodiceFiscale(java.lang.String codiceFiscale)  {
		this.codiceFiscale=codiceFiscale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroConto]
	 **/
	public java.lang.String getNumeroConto() {
		return numeroConto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroConto]
	 **/
	public void setNumeroConto(java.lang.String numeroConto)  {
		this.numeroConto=numeroConto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdIso]
	 **/
	public java.lang.String getCdIso() {
		return cdIso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdIso]
	 **/
	public void setCdIso(java.lang.String cdIso)  {
		this.cdIso=cdIso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imAssociato]
	 **/
	public java.math.BigDecimal getImAssociato() {
		return imAssociato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imAssociato]
	 **/
	public void setImAssociato(java.math.BigDecimal imAssociato)  {
		this.imAssociato=imAssociato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoCge]
	 **/
	public BigDecimal getImportoCge() {
		return importoCge;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoCge]
	 **/
	public void setImportoCge(BigDecimal importoCge)  {
		this.importoCge=importoCge;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoCup]
	 **/
	public BigDecimal getImportoCup() {
		return importoCup;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoCup]
	 **/
	public void setImportoCup(BigDecimal importoCup)  {
		this.importoCup=importoCup;
	}
	public java.lang.String getAssoggettamentoBollo() {
		return assoggettamentoBollo;
	}
	public void setAssoggettamentoBollo(java.lang.String assoggettamentoBollo) {
		this.assoggettamentoBollo = assoggettamentoBollo;
	}
	public java.lang.String getCausaleBollo() {
		return causaleBollo;
	}
	public void setCausaleBollo(java.lang.String causaleBollo) {
		this.causaleBollo = causaleBollo;
	}
	public java.lang.String getBic() {
		return bic;
	}
	public void setBic(java.lang.String bic) {
		this.bic = bic;
	}
	public java.lang.String getCdTipoDocumentoAmm() {
		return cdTipoDocumentoAmm;
	}
	public void setCdTipoDocumentoAmm(java.lang.String cdTipoDocumentoAmm) {
		this.cdTipoDocumentoAmm = cdTipoDocumentoAmm;
	}
	public java.lang.Long getPgDocAmm() {
		return pgDocAmm;
	}
	public void setPgDocAmm(java.lang.Long pgDocAmm) {
		this.pgDocAmm = pgDocAmm;
	}
	public java.lang.String getModalitaPagamento() {
		return modalitaPagamento;
	}
	public void setModalitaPagamento(java.lang.String modalitaPagamento) {
		this.modalitaPagamento = modalitaPagamento;
	}
	public java.lang.String getCodiceIban() {
		return codiceIban;
	}
	public void setCodiceIban(java.lang.String codiceIban) {
		this.codiceIban = codiceIban;
	}
	public java.lang.String getAbi() {
		return abi;
	}
	public void setAbi(java.lang.String abi) {
		this.abi = abi;
	}
	public java.lang.String getCab() {
		return cab;
	}
	public void setCab(java.lang.String cab) {
		this.cab = cab;
	}
	public java.lang.String getCin() {
		return cin;
	}
	public void setCin(java.lang.String cin) {
		this.cin = cin;
	}
	public java.sql.Timestamp getDtPagamentoRichiesta() {
		return dtPagamentoRichiesta;
	}
	public void setDtPagamentoRichiesta(java.sql.Timestamp dtPagamentoRichiesta) {
		this.dtPagamentoRichiesta = dtPagamentoRichiesta;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}
}