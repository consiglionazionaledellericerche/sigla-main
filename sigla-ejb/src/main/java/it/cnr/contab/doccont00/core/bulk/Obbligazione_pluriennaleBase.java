/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 20/09/2021
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class Obbligazione_pluriennaleBase extends Obbligazione_pluriennaleKey implements Keyed {
//    IMPORTO DECIMAL(15,2)
	private java.math.BigDecimal importo;
 
//    CD_CDS_RIF VARCHAR(30)
	private String cdCdsRif;
 
//    ESERCIZIO_RIF DECIMAL(38,0)
	private Integer esercizioRif;
 
//    ESERCIZIO_ORIGINALE_RIF DECIMAL(38,0)
	private Integer esercizioOriginaleRif;
 
//    PG_OBBLIGAZIONE_RIF DECIMAL(38,0)
	private Long pgObbligazioneRif;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: OBBLIGAZIONE_PLURIENNALE
	 **/
	public Obbligazione_pluriennaleBase() {
		super();
	}
	public Obbligazione_pluriennaleBase(String cdCds, Integer esercizio, Integer esercizioOriginale, Long pgObbligazione, Integer anno) {
		super(cdCds, esercizio, esercizioOriginale, pgObbligazione, anno);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Importo totale]
	 **/
	public java.math.BigDecimal getImporto() {
		return importo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Importo totale]
	 **/
	public void setImporto(java.math.BigDecimal importo)  {
		this.importo=importo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Cds dell'obbligazione di riferimento]
	 **/
	public String getCdCdsRif() {
		return cdCdsRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Cds dell'obbligazione di riferimento]
	 **/
	public void setCdCdsRif(String cdCdsRif)  {
		this.cdCdsRif=cdCdsRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio dell'obbligazione di riferimento]
	 **/
	public Integer getEsercizioRif() {
		return esercizioRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio dell'obbligazione di riferimento]
	 **/
	public void setEsercizioRif(Integer esercizioRif)  {
		this.esercizioRif=esercizioRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio Originale dell'obbligazione di riferimento]
	 **/
	public Integer getEsercizioOriginaleRif() {
		return esercizioOriginaleRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio Originale dell'obbligazione di riferimento]
	 **/
	public void setEsercizioOriginaleRif(Integer esercizioOriginaleRif)  {
		this.esercizioOriginaleRif=esercizioOriginaleRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Numero dell'obbligazione di riferimento]
	 **/
	public Long getPgObbligazioneRif() {
		return pgObbligazioneRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Numero dell'obbligazione di riferimento]
	 **/
	public void setPgObbligazioneRif(Long pgObbligazioneRif)  {
		this.pgObbligazioneRif=pgObbligazioneRif;
	}
}