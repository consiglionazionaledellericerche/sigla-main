/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 15/12/2010
 */
package it.cnr.contab.config00.pdcfin.bulk;
import it.cnr.jada.persistency.Keyed;
public class LimiteSpesaDetBase extends LimiteSpesaDetKey implements Keyed {
//    IMPORTO_LIMITE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_limite;
 
//    IMPEGNI_ASSUNTI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal impegni_assunti;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: LIMITE_SPESA_DET
	 **/
	public LimiteSpesaDetBase() {
		super();
	}
	public LimiteSpesaDetBase(java.lang.Integer esercizio, java.lang.String cdCds, java.lang.String tiAppartenenza, java.lang.String tiGestione, java.lang.String cdElementoVoce, java.lang.String fonte) {
		super(esercizio, cdCds, tiAppartenenza, tiGestione, cdElementoVoce, fonte);
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
	 * Restituisce il valore di: [impegniAssunti]
	 **/
	public java.math.BigDecimal getImpegni_assunti() {
		return impegni_assunti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [impegniAssunti]
	 **/
	public void setImpegni_assunti(java.math.BigDecimal impegniAssunti)  {
		this.impegni_assunti=impegniAssunti;
	}
}