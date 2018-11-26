/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 02/10/2018
 */
package it.cnr.contab.doccont00.intcass.giornaliera;
import it.cnr.jada.persistency.Keyed;
public class MovimentoContoEvidenzaBase extends MovimentoContoEvidenzaKey implements Keyed {
//    TIPO_MOVIMENTO VARCHAR(10)
	private java.lang.String tipoMovimento;
 
//    TIPO_DOCUMENTO VARCHAR(20)
	private java.lang.String tipoDocumento;
 
//    TIPO_OPERAZIONE VARCHAR(20)
	private java.lang.String tipoOperazione;
 
//    NUMERO_DOCUMENTO DECIMAL(10,0)
	private java.lang.Long numeroDocumento;
 
//    TI_PAGAMENTO_FUNZ_DELEGATO VARCHAR(20)
	private java.lang.String tiPagamentoFunzDelegato;
 
//    NUM_PAG_FUNZ_DELEGATO VARCHAR(10)
	private java.lang.String numPagFunzDelegato;
 
//    PROGRESSIVO_DOCUMENTO DECIMAL(10,0)
	private java.lang.Long progressivoDocumento;
 
//    IMPORTO DECIMAL(15,2)
	private java.math.BigDecimal importo;
 
//    IMPORTO_RITENUTE DECIMAL(15,2)
	private java.math.BigDecimal importoRitenute;
 
//    NUMERO_BOLLETTA_QUIETANZA VARCHAR(10)
	private java.lang.String numeroBollettaQuietanza;
 
//    NUMERO_BOLLETTA_QUIETANZA_S VARCHAR(10)
	private java.lang.String numeroBollettaQuietanzaS;
 
//    DATA_MOVIMENTO TIMESTAMP(7)
	private java.sql.Timestamp dataMovimento;
 
//    DATA_VALUTA_ENTE TIMESTAMP(7)
	private java.sql.Timestamp dataValutaEnte;
 
//    TIPO_ESECUZIONE VARCHAR(20)
	private java.lang.String tipoEsecuzione;
 
//    COORDINATE VARCHAR(20)
	private java.lang.String coordinate;
 
//    CODICE_RIF_OPERAZIONE VARCHAR(20)
	private java.lang.String codiceRifOperazione;
 
//    CODICE_RIF_INTERNO VARCHAR(20)
	private java.lang.String codiceRifInterno;
 
//    TIPO_CONTABILITA VARCHAR(20)
	private java.lang.String tipoContabilita;
 
//    DESTINAZIONE VARCHAR(20)
	private java.lang.String destinazione;
 
//    ASSOGGETTAMENTO_BOLLO VARCHAR(30)
	private java.lang.String assoggettamentoBollo;
 
//    IMPORTO_BOLLO DECIMAL(15,2)
	private java.math.BigDecimal importoBollo;
 
//    ASSOGGETTAMENTO_SPESE VARCHAR(30)
	private java.lang.String assoggettamentoSpese;
 
//    IMPORTO_SPESE DECIMAL(15,2)
	private java.math.BigDecimal importoSpese;
 
//    ASSOGGETTAMENTO_COMMISSIONI VARCHAR(30)
	private java.lang.String assoggettamentoCommissioni;
 
//    IMPORTO_COMMISSIONI DECIMAL(15,2)
	private java.math.BigDecimal importoCommissioni;
 
//    ANAGRAFICA_CLIENTE VARCHAR(50)
	private java.lang.String anagraficaCliente;
 
//    INDIRIZZO_CLIENTE VARCHAR(50)
	private java.lang.String indirizzoCliente;
 
//    CAP_CLIENTE VARCHAR(16)
	private java.lang.String capCliente;
 
//    LOCALITA_CLIENTE VARCHAR(50)
	private java.lang.String localitaCliente;
 
//    PROVINCIA_CLIENTE VARCHAR(20)
	private java.lang.String provinciaCliente;
 
//    STATO_CLIENTE VARCHAR(2)
	private java.lang.String statoCliente;
 
//    PARTITA_IVA_CLIENTE VARCHAR(35)
	private java.lang.String partitaIvaCliente;
 
//    CODICE_FISCALE_CLIENTE VARCHAR(35)
	private java.lang.String codiceFiscaleCliente;
 
//    ANAGRAFICA_DELEGATO VARCHAR(50)
	private java.lang.String anagraficaDelegato;
 
//    INDIRIZZO_DELEGATO VARCHAR(50)
	private java.lang.String indirizzoDelegato;
 
//    CAP_DELEGATO VARCHAR(16)
	private java.lang.String capDelegato;
 
//    LOCALITA_DELEGATO VARCHAR(50)
	private java.lang.String localitaDelegato;
 
//    PROVINCIA_DELEGATO VARCHAR(20)
	private java.lang.String provinciaDelegato;
 
//    STATO_DELEGATO VARCHAR(2)
	private java.lang.String statoDelegato;
 
//    CODICE_FISCALE_DELEGATO VARCHAR(35)
	private java.lang.String codiceFiscaleDelegato;
 
//    ANAGRAFICA_CREDITORE_EFF VARCHAR(50)
	private java.lang.String anagraficaCreditoreEff;
 
//    INDIRIZZO_CREDITORE_EFF VARCHAR(50)
	private java.lang.String indirizzoCreditoreEff;
 
//    CAP_CREDITORE_EFF VARCHAR(16)
	private java.lang.String capCreditoreEff;
 
//    LOCALITA_CREDITORE_EFF VARCHAR(50)
	private java.lang.String localitaCreditoreEff;
 
//    PROVINCIA_CREDITORE_EFF VARCHAR(20)
	private java.lang.String provinciaCreditoreEff;
 
//    STATO_CREDITORE_EFF VARCHAR(2)
	private java.lang.String statoCreditoreEff;
 
//    PARTITA_IVA_CREDITORE_EFF VARCHAR(35)
	private java.lang.String partitaIvaCreditoreEff;
 
//    CODICE_FISCALE_CREDITORE_EFF VARCHAR(35)
	private java.lang.String codiceFiscaleCreditoreEff;
 
//    CAUSALE VARCHAR(200)
	private java.lang.String causale;
 
//    NUMERO_SOSPESO DECIMAL(10,0)
	private java.lang.Long numeroSospeso;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MOVIMENTI_CONTO_EVIDENZA
	 **/
	public MovimentoContoEvidenzaBase() {
		super();
	}
	public MovimentoContoEvidenzaBase(java.lang.Integer esercizio, java.lang.String identificativoFlusso, java.lang.String contoEvidenza, java.lang.String stato, java.lang.Long progressivo) {
		super(esercizio, identificativoFlusso, contoEvidenza, stato, progressivo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoMovimento]
	 **/
	public java.lang.String getTipoMovimento() {
		return tipoMovimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoMovimento]
	 **/
	public void setTipoMovimento(java.lang.String tipoMovimento)  {
		this.tipoMovimento=tipoMovimento;
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
	 * Restituisce il valore di: [tipoOperazione]
	 **/
	public java.lang.String getTipoOperazione() {
		return tipoOperazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoOperazione]
	 **/
	public void setTipoOperazione(java.lang.String tipoOperazione)  {
		this.tipoOperazione=tipoOperazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroDocumento]
	 **/
	public java.lang.Long getNumeroDocumento() {
		return numeroDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroDocumento]
	 **/
	public void setNumeroDocumento(java.lang.Long numeroDocumento)  {
		this.numeroDocumento=numeroDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiPagamentoFunzDelegato]
	 **/
	public java.lang.String getTiPagamentoFunzDelegato() {
		return tiPagamentoFunzDelegato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiPagamentoFunzDelegato]
	 **/
	public void setTiPagamentoFunzDelegato(java.lang.String tiPagamentoFunzDelegato)  {
		this.tiPagamentoFunzDelegato=tiPagamentoFunzDelegato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numPagFunzDelegato]
	 **/
	public java.lang.String getNumPagFunzDelegato() {
		return numPagFunzDelegato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numPagFunzDelegato]
	 **/
	public void setNumPagFunzDelegato(java.lang.String numPagFunzDelegato)  {
		this.numPagFunzDelegato=numPagFunzDelegato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [progressivoDocumento]
	 **/
	public java.lang.Long getProgressivoDocumento() {
		return progressivoDocumento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [progressivoDocumento]
	 **/
	public void setProgressivoDocumento(java.lang.Long progressivoDocumento)  {
		this.progressivoDocumento=progressivoDocumento;
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
	 * Restituisce il valore di: [importoRitenute]
	 **/
	public java.math.BigDecimal getImportoRitenute() {
		return importoRitenute;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoRitenute]
	 **/
	public void setImportoRitenute(java.math.BigDecimal importoRitenute)  {
		this.importoRitenute=importoRitenute;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroBollettaQuietanza]
	 **/
	public java.lang.String getNumeroBollettaQuietanza() {
		return numeroBollettaQuietanza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroBollettaQuietanza]
	 **/
	public void setNumeroBollettaQuietanza(java.lang.String numeroBollettaQuietanza)  {
		this.numeroBollettaQuietanza=numeroBollettaQuietanza;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numeroBollettaQuietanzaS]
	 **/
	public java.lang.String getNumeroBollettaQuietanzaS() {
		return numeroBollettaQuietanzaS;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroBollettaQuietanzaS]
	 **/
	public void setNumeroBollettaQuietanzaS(java.lang.String numeroBollettaQuietanzaS)  {
		this.numeroBollettaQuietanzaS=numeroBollettaQuietanzaS;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataMovimento]
	 **/
	public java.sql.Timestamp getDataMovimento() {
		return dataMovimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataMovimento]
	 **/
	public void setDataMovimento(java.sql.Timestamp dataMovimento)  {
		this.dataMovimento=dataMovimento;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataValutaEnte]
	 **/
	public java.sql.Timestamp getDataValutaEnte() {
		return dataValutaEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataValutaEnte]
	 **/
	public void setDataValutaEnte(java.sql.Timestamp dataValutaEnte)  {
		this.dataValutaEnte=dataValutaEnte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoEsecuzione]
	 **/
	public java.lang.String getTipoEsecuzione() {
		return tipoEsecuzione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoEsecuzione]
	 **/
	public void setTipoEsecuzione(java.lang.String tipoEsecuzione)  {
		this.tipoEsecuzione=tipoEsecuzione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [coordinate]
	 **/
	public java.lang.String getCoordinate() {
		return coordinate;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [coordinate]
	 **/
	public void setCoordinate(java.lang.String coordinate)  {
		this.coordinate=coordinate;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codiceRifOperazione]
	 **/
	public java.lang.String getCodiceRifOperazione() {
		return codiceRifOperazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codiceRifOperazione]
	 **/
	public void setCodiceRifOperazione(java.lang.String codiceRifOperazione)  {
		this.codiceRifOperazione=codiceRifOperazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codiceRifInterno]
	 **/
	public java.lang.String getCodiceRifInterno() {
		return codiceRifInterno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codiceRifInterno]
	 **/
	public void setCodiceRifInterno(java.lang.String codiceRifInterno)  {
		this.codiceRifInterno=codiceRifInterno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoContabilita]
	 **/
	public java.lang.String getTipoContabilita() {
		return tipoContabilita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoContabilita]
	 **/
	public void setTipoContabilita(java.lang.String tipoContabilita)  {
		this.tipoContabilita=tipoContabilita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [destinazione]
	 **/
	public java.lang.String getDestinazione() {
		return destinazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [destinazione]
	 **/
	public void setDestinazione(java.lang.String destinazione)  {
		this.destinazione=destinazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [assoggettamentoBollo]
	 **/
	public java.lang.String getAssoggettamentoBollo() {
		return assoggettamentoBollo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [assoggettamentoBollo]
	 **/
	public void setAssoggettamentoBollo(java.lang.String assoggettamentoBollo)  {
		this.assoggettamentoBollo=assoggettamentoBollo;
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
	 * Restituisce il valore di: [assoggettamentoSpese]
	 **/
	public java.lang.String getAssoggettamentoSpese() {
		return assoggettamentoSpese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [assoggettamentoSpese]
	 **/
	public void setAssoggettamentoSpese(java.lang.String assoggettamentoSpese)  {
		this.assoggettamentoSpese=assoggettamentoSpese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoSpese]
	 **/
	public java.math.BigDecimal getImportoSpese() {
		return importoSpese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoSpese]
	 **/
	public void setImportoSpese(java.math.BigDecimal importoSpese)  {
		this.importoSpese=importoSpese;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [assoggettamentoCommissioni]
	 **/
	public java.lang.String getAssoggettamentoCommissioni() {
		return assoggettamentoCommissioni;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [assoggettamentoCommissioni]
	 **/
	public void setAssoggettamentoCommissioni(java.lang.String assoggettamentoCommissioni)  {
		this.assoggettamentoCommissioni=assoggettamentoCommissioni;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoCommissioni]
	 **/
	public java.math.BigDecimal getImportoCommissioni() {
		return importoCommissioni;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoCommissioni]
	 **/
	public void setImportoCommissioni(java.math.BigDecimal importoCommissioni)  {
		this.importoCommissioni=importoCommissioni;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [anagraficaCliente]
	 **/
	public java.lang.String getAnagraficaCliente() {
		return anagraficaCliente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [anagraficaCliente]
	 **/
	public void setAnagraficaCliente(java.lang.String anagraficaCliente)  {
		this.anagraficaCliente=anagraficaCliente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [indirizzoCliente]
	 **/
	public java.lang.String getIndirizzoCliente() {
		return indirizzoCliente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [indirizzoCliente]
	 **/
	public void setIndirizzoCliente(java.lang.String indirizzoCliente)  {
		this.indirizzoCliente=indirizzoCliente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [capCliente]
	 **/
	public java.lang.String getCapCliente() {
		return capCliente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [capCliente]
	 **/
	public void setCapCliente(java.lang.String capCliente)  {
		this.capCliente=capCliente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [localitaCliente]
	 **/
	public java.lang.String getLocalitaCliente() {
		return localitaCliente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [localitaCliente]
	 **/
	public void setLocalitaCliente(java.lang.String localitaCliente)  {
		this.localitaCliente=localitaCliente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [provinciaCliente]
	 **/
	public java.lang.String getProvinciaCliente() {
		return provinciaCliente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [provinciaCliente]
	 **/
	public void setProvinciaCliente(java.lang.String provinciaCliente)  {
		this.provinciaCliente=provinciaCliente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [statoCliente]
	 **/
	public java.lang.String getStatoCliente() {
		return statoCliente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [statoCliente]
	 **/
	public void setStatoCliente(java.lang.String statoCliente)  {
		this.statoCliente=statoCliente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [partitaIvaCliente]
	 **/
	public java.lang.String getPartitaIvaCliente() {
		return partitaIvaCliente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [partitaIvaCliente]
	 **/
	public void setPartitaIvaCliente(java.lang.String partitaIvaCliente)  {
		this.partitaIvaCliente=partitaIvaCliente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codiceFiscaleCliente]
	 **/
	public java.lang.String getCodiceFiscaleCliente() {
		return codiceFiscaleCliente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codiceFiscaleCliente]
	 **/
	public void setCodiceFiscaleCliente(java.lang.String codiceFiscaleCliente)  {
		this.codiceFiscaleCliente=codiceFiscaleCliente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [anagraficaDelegato]
	 **/
	public java.lang.String getAnagraficaDelegato() {
		return anagraficaDelegato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [anagraficaDelegato]
	 **/
	public void setAnagraficaDelegato(java.lang.String anagraficaDelegato)  {
		this.anagraficaDelegato=anagraficaDelegato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [indirizzoDelegato]
	 **/
	public java.lang.String getIndirizzoDelegato() {
		return indirizzoDelegato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [indirizzoDelegato]
	 **/
	public void setIndirizzoDelegato(java.lang.String indirizzoDelegato)  {
		this.indirizzoDelegato=indirizzoDelegato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [capDelegato]
	 **/
	public java.lang.String getCapDelegato() {
		return capDelegato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [capDelegato]
	 **/
	public void setCapDelegato(java.lang.String capDelegato)  {
		this.capDelegato=capDelegato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [localitaDelegato]
	 **/
	public java.lang.String getLocalitaDelegato() {
		return localitaDelegato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [localitaDelegato]
	 **/
	public void setLocalitaDelegato(java.lang.String localitaDelegato)  {
		this.localitaDelegato=localitaDelegato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [provinciaDelegato]
	 **/
	public java.lang.String getProvinciaDelegato() {
		return provinciaDelegato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [provinciaDelegato]
	 **/
	public void setProvinciaDelegato(java.lang.String provinciaDelegato)  {
		this.provinciaDelegato=provinciaDelegato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [statoDelegato]
	 **/
	public java.lang.String getStatoDelegato() {
		return statoDelegato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [statoDelegato]
	 **/
	public void setStatoDelegato(java.lang.String statoDelegato)  {
		this.statoDelegato=statoDelegato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codiceFiscaleDelegato]
	 **/
	public java.lang.String getCodiceFiscaleDelegato() {
		return codiceFiscaleDelegato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codiceFiscaleDelegato]
	 **/
	public void setCodiceFiscaleDelegato(java.lang.String codiceFiscaleDelegato)  {
		this.codiceFiscaleDelegato=codiceFiscaleDelegato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [anagraficaCreditoreEff]
	 **/
	public java.lang.String getAnagraficaCreditoreEff() {
		return anagraficaCreditoreEff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [anagraficaCreditoreEff]
	 **/
	public void setAnagraficaCreditoreEff(java.lang.String anagraficaCreditoreEff)  {
		this.anagraficaCreditoreEff=anagraficaCreditoreEff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [indirizzoCreditoreEff]
	 **/
	public java.lang.String getIndirizzoCreditoreEff() {
		return indirizzoCreditoreEff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [indirizzoCreditoreEff]
	 **/
	public void setIndirizzoCreditoreEff(java.lang.String indirizzoCreditoreEff)  {
		this.indirizzoCreditoreEff=indirizzoCreditoreEff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [capCreditoreEff]
	 **/
	public java.lang.String getCapCreditoreEff() {
		return capCreditoreEff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [capCreditoreEff]
	 **/
	public void setCapCreditoreEff(java.lang.String capCreditoreEff)  {
		this.capCreditoreEff=capCreditoreEff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [localitaCreditoreEff]
	 **/
	public java.lang.String getLocalitaCreditoreEff() {
		return localitaCreditoreEff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [localitaCreditoreEff]
	 **/
	public void setLocalitaCreditoreEff(java.lang.String localitaCreditoreEff)  {
		this.localitaCreditoreEff=localitaCreditoreEff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [provinciaCreditoreEff]
	 **/
	public java.lang.String getProvinciaCreditoreEff() {
		return provinciaCreditoreEff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [provinciaCreditoreEff]
	 **/
	public void setProvinciaCreditoreEff(java.lang.String provinciaCreditoreEff)  {
		this.provinciaCreditoreEff=provinciaCreditoreEff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [statoCreditoreEff]
	 **/
	public java.lang.String getStatoCreditoreEff() {
		return statoCreditoreEff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [statoCreditoreEff]
	 **/
	public void setStatoCreditoreEff(java.lang.String statoCreditoreEff)  {
		this.statoCreditoreEff=statoCreditoreEff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [partitaIvaCreditoreEff]
	 **/
	public java.lang.String getPartitaIvaCreditoreEff() {
		return partitaIvaCreditoreEff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [partitaIvaCreditoreEff]
	 **/
	public void setPartitaIvaCreditoreEff(java.lang.String partitaIvaCreditoreEff)  {
		this.partitaIvaCreditoreEff=partitaIvaCreditoreEff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codiceFiscaleCreditoreEff]
	 **/
	public java.lang.String getCodiceFiscaleCreditoreEff() {
		return codiceFiscaleCreditoreEff;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codiceFiscaleCreditoreEff]
	 **/
	public void setCodiceFiscaleCreditoreEff(java.lang.String codiceFiscaleCreditoreEff)  {
		this.codiceFiscaleCreditoreEff=codiceFiscaleCreditoreEff;
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
	 * Restituisce il valore di: [numeroSospeso]
	 **/
	public java.lang.Long getNumeroSospeso() {
		return numeroSospeso;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numeroSospeso]
	 **/
	public void setNumeroSospeso(java.lang.Long numeroSospeso)  {
		this.numeroSospeso=numeroSospeso;
	}
}