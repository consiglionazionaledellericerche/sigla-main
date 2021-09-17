/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 15/09/2021
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class Obbligazione_pluriennaleBase extends Obbligazione_pluriennaleKey implements Keyed {
//    ANNO DECIMAL(38,0) NOT NULL
	private Integer anno;
 
//    CD_CDS VARCHAR(30) NOT NULL
	private String cdCds;
 
//    ESERCIZIO DECIMAL(38,0) NOT NULL
	private Integer esercizio;
 
//    ESERCIZIO_ORIGINALE DECIMAL(38,0) NOT NULL
	private Integer esercizioOriginale;
 
//    PG_OBBLIGAZIONE DECIMAL(38,0) NOT NULL
	private Long pgObbligazione;
 
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
	public Obbligazione_pluriennaleBase(Long idObbligazionePlur) {
		super(idObbligazionePlur);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Anno Obbligazione Pluriennale]
	 **/
	public Integer getAnno() {
		return anno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Anno Obbligazione Pluriennale]
	 **/
	public void setAnno(Integer anno)  {
		this.anno=anno;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Cds dell'obbligazione]
	 **/
	public String getCdCds() {
		return cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Cds dell'obbligazione]
	 **/
	public void setCdCds(String cdCds)  {
		this.cdCds=cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio dell'obbligazione]
	 **/
	public Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio dell'obbligazione]
	 **/
	public void setEsercizio(Integer esercizio)  {
		this.esercizio=esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio Originale dell'obbligazione]
	 **/
	public Integer getEsercizioOriginale() {
		return esercizioOriginale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio Originale dell'obbligazione]
	 **/
	public void setEsercizioOriginale(Integer esercizioOriginale)  {
		this.esercizioOriginale=esercizioOriginale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Numero dell'obbligazione]
	 **/
	public Long getPgObbligazione() {
		return pgObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Numero dell'obbligazione]
	 **/
	public void setPgObbligazione(Long pgObbligazione)  {
		this.pgObbligazione=pgObbligazione;
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