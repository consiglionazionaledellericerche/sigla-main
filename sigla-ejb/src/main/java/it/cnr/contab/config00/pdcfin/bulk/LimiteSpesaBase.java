/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 15/12/2010
 */
package it.cnr.contab.config00.pdcfin.bulk;
import it.cnr.jada.persistency.Keyed;
public class LimiteSpesaBase extends LimiteSpesaKey implements Keyed {
//    IMPORTO_LIMITE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_limite;
 
//    IMPORTO_ASSEGNATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_assegnato;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: LIMITE_SPESA
	 **/
	public LimiteSpesaBase() {
		super();
	}
	public LimiteSpesaBase(java.lang.Integer esercizio, java.lang.String tiAppartenenza, java.lang.String tiGestione, java.lang.String cdElementoVoce, java.lang.String fonte) {
		super(esercizio, tiAppartenenza, tiGestione, cdElementoVoce, fonte);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoLimite]
	 **/
	public java.math.BigDecimal getImporto_limite() {
		return importo_limite;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoLimite]
	 **/
	public void setImporto_limite(java.math.BigDecimal importoLimite)  {
		this.importo_limite=importoLimite;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importoAssegnato]
	 **/
	public java.math.BigDecimal getImporto_assegnato() {
		return importo_assegnato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importoAssegnato]
	 **/
	public void setImporto_assegnato(java.math.BigDecimal importoAssegnato)  {
		this.importo_assegnato=importoAssegnato;
	}
}