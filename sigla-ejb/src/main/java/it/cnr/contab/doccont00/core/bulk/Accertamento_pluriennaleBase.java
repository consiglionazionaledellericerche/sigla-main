/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2021
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class Accertamento_pluriennaleBase extends Accertamento_pluriennaleKey implements Keyed {
//    IMPORTO DECIMAL(15,2)
	private java.math.BigDecimal importo;
 
//    CD_CDS_RIF VARCHAR(30)
	private String cdCdsRif;
 
//    ESERCIZIO_RIF DECIMAL(38,0)
	private Integer esercizioRif;
 
//    ESERCIZIO_ORIGINALE_RIF DECIMAL(38,0)
	private Integer esercizioOriginaleRif;
 
//    PG_ACCERTAMENTO_RIF DECIMAL(38,0)
	private Long pgAccertamentoRif;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ACCERTAMENTO_PLURIENNALE
	 **/
	public Accertamento_pluriennaleBase() {
		super();
	}
	public Accertamento_pluriennaleBase(String cdCds, Integer esercizio, Integer esercizioOriginale, Long pgAccertamento, Integer anno) {
		super(cdCds, esercizio, esercizioOriginale, pgAccertamento, anno);
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
	 * Restituisce il valore di: [Cds dell'accertamento di riferimento]
	 **/
	public String getCdCdsRif() {
		return cdCdsRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Cds dell'accertamento di riferimento]
	 **/
	public void setCdCdsRif(String cdCdsRif)  {
		this.cdCdsRif=cdCdsRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio dell'accertamento di riferimento]
	 **/
	public Integer getEsercizioRif() {
		return esercizioRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio dell'accertamento di riferimento]
	 **/
	public void setEsercizioRif(Integer esercizioRif)  {
		this.esercizioRif=esercizioRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Esercizio Originale dell'accertamento di riferimento]
	 **/
	public Integer getEsercizioOriginaleRif() {
		return esercizioOriginaleRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Esercizio Originale dell'accertamento di riferimento]
	 **/
	public void setEsercizioOriginaleRif(Integer esercizioOriginaleRif)  {
		this.esercizioOriginaleRif=esercizioOriginaleRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Numero dell'accertamento di riferimento]
	 **/
	public Long getPgAccertamentoRif() {
		return pgAccertamentoRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Numero dell'accertamento di riferimento]
	 **/
	public void setPgAccertamentoRif(Long pgAccertamentoRif)  {
		this.pgAccertamentoRif=pgAccertamentoRif;
	}
}