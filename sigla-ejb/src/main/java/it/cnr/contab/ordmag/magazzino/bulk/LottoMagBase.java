/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import it.cnr.jada.persistency.Keyed;
public class LottoMagBase extends LottoMagKey implements Keyed {
//    CD_BENE_SERVIZIO VARCHAR(15) NOT NULL
	private String cdBeneServizio;
 
//    CD_CDS_MAG VARCHAR(30) NOT NULL
	private String cdCdsMag;
 
//    CD_MAGAZZINO_MAG VARCHAR(10) NOT NULL
	private String cdMagazzinoMag;
 
//    DT_SCADENZA TIMESTAMP(7)
	private java.sql.Timestamp dtScadenza;
 
//    GIACENZA DECIMAL(12,5)
	private java.math.BigDecimal giacenza;
 
//    QUANTITA_VALORE DECIMAL(12,5)
	private java.math.BigDecimal quantitaValore;
 
//    LOTTO_FORNITORE VARCHAR(30)
	private String lottoFornitore;
 
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
	private String cdCdsOrdine;
 
//    CD_UNITA_OPERATIVA VARCHAR(30)
	private String cdUnitaOperativa;
 
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
 
//    STATO VARCHAR(3)
	private String stato;
 
//    CD_TERZO DECIMAL(10,0)
	private Integer cdTerzo;
 
//    CD_DIVISA VARCHAR(10) NOT NULL
	private String cdDivisa;
 
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
	public LottoMagBase(String cdCds, String cdMagazzino, Integer esercizio, String cdNumeratoreMag, Integer pgLotto) {
		super(cdCds, cdMagazzino, esercizio, cdNumeratoreMag, pgLotto);
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
	 * Restituisce il valore di: [cdMagazzinoMag]
	 **/
	public String getCdMagazzinoMag() {
		return cdMagazzinoMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzinoMag]
	 **/
	public void setCdMagazzinoMag(String cdMagazzinoMag)  {
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
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public String getCdUnitaOperativa() {
		return cdUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(String cdUnitaOperativa)  {
		this.cdUnitaOperativa=cdUnitaOperativa;
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