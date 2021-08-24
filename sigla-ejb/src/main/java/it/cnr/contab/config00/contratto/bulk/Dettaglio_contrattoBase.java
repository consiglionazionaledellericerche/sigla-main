/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 19/08/2021
 */
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.jada.persistency.Keyed;
public class Dettaglio_contrattoBase extends Dettaglio_contrattoKey implements Keyed {
//    PG_CONTRATTO DECIMAL(38,0)
	private Long pgContratto;
 
//    ESERCIZIO_CONTRATTO DECIMAL(5,0)
	private Integer esercizioContratto;
 
//    STATO_CONTRATTO CHAR(1)
	private String statoContratto;
 
//    CD_BENE_SERVIZIO VARCHAR(15)
	private String cdBeneServizio;
 
//    CD_UNITA_MISURA VARCHAR(10)
	private String cdUnitaMisura;
 
//    COEF_CONV DECIMAL(12,5)
	private java.math.BigDecimal coefConv;
 
//    QUANTITA_MIN DECIMAL(15,2)
	private java.math.BigDecimal quantitaMin;
 
//    QUANTITA_MAX DECIMAL(15,2)
	private java.math.BigDecimal quantitaMax;
 
//    QUANTITA_ORDINATA DECIMAL(15,2)
	private java.math.BigDecimal quantitaOrdinata;
 
//    PREZZO_UNITARIO DECIMAL(20,6)
	private java.math.BigDecimal prezzoUnitario;
 
//    CD_CATEGORIA_GRUPPO VARCHAR(10)
	private String cdCategoriaGruppo;
 
//    IMPORTO_ORDINATO DECIMAL(21,6)
	private java.math.BigDecimal importoOrdinato;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DETTAGLIO_CONTRATTO
	 **/
	public Dettaglio_contrattoBase() {
		super();
	}
	public Dettaglio_contrattoBase(Long id) {
		super(id);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Progressivo contratto (cronologico all'interno dell'esercizio)]
	 **/
	public Long getPgContratto() {
		return pgContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Progressivo contratto (cronologico all'interno dell'esercizio)]
	 **/
	public void setPgContratto(Long pgContratto)  {
		this.pgContratto=pgContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio del Contratto.]
	 **/
	public Integer getEsercizioContratto() {
		return esercizioContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio del Contratto.]
	 **/
	public void setEsercizioContratto(Integer esercizioContratto)  {
		this.esercizioContratto=esercizioContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Stato del Contratto]
	 **/
	public String getStatoContratto() {
		return statoContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Stato del Contratto]
	 **/
	public void setStatoContratto(String statoContratto)  {
		this.statoContratto=statoContratto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Codice identificativo del bene o servizio]
	 **/
	public String getCdBeneServizio() {
		return cdBeneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Codice identificativo del bene o servizio]
	 **/
	public void setCdBeneServizio(String cdBeneServizio)  {
		this.cdBeneServizio=cdBeneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Codice dell'unità di misura di ordine]
	 **/
	public String getCdUnitaMisura() {
		return cdUnitaMisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Codice dell'unità di misura di ordine]
	 **/
	public void setCdUnitaMisura(String cdUnitaMisura)  {
		this.cdUnitaMisura=cdUnitaMisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Coefficiente di conversione dell'unità di misura]
	 **/
	public java.math.BigDecimal getCoefConv() {
		return coefConv;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Coefficiente di conversione dell'unità di misura]
	 **/
	public void setCoefConv(java.math.BigDecimal coefConv)  {
		this.coefConv=coefConv;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Quantità Max]
	 **/
	public java.math.BigDecimal getQuantitaMin() {
		return quantitaMin;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Quantità Max]
	 **/
	public void setQuantitaMin(java.math.BigDecimal quantitaMin)  {
		this.quantitaMin=quantitaMin;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Quantità Max]
	 **/
	public java.math.BigDecimal getQuantitaMax() {
		return quantitaMax;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Quantità Max]
	 **/
	public void setQuantitaMax(java.math.BigDecimal quantitaMax)  {
		this.quantitaMax=quantitaMax;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Quantità Ordinata]
	 **/
	public java.math.BigDecimal getQuantitaOrdinata() {
		return quantitaOrdinata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Quantità Ordinata]
	 **/
	public void setQuantitaOrdinata(java.math.BigDecimal quantitaOrdinata)  {
		this.quantitaOrdinata=quantitaOrdinata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Prezzo unitario dell'articolo/servizio]
	 **/
	public java.math.BigDecimal getPrezzoUnitario() {
		return prezzoUnitario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Prezzo unitario dell'articolo/servizio]
	 **/
	public void setPrezzoUnitario(java.math.BigDecimal prezzoUnitario)  {
		this.prezzoUnitario=prezzoUnitario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Codice Categoria]
	 **/
	public String getCdCategoriaGruppo() {
		return cdCategoriaGruppo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Codice Categoria]
	 **/
	public void setCdCategoriaGruppo(String cdCategoriaGruppo)  {
		this.cdCategoriaGruppo=cdCategoriaGruppo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Importo Ordinato]
	 **/
	public java.math.BigDecimal getImportoOrdinato() {
		return importoOrdinato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Importo Ordinato]
	 **/
	public void setImportoOrdinato(java.math.BigDecimal importoOrdinato)  {
		this.importoOrdinato=importoOrdinato;
	}
}