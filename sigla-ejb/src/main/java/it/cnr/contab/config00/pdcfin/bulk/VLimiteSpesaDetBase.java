/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/02/2011
 */
package it.cnr.contab.config00.pdcfin.bulk;
import it.cnr.jada.persistency.Keyed;
public class VLimiteSpesaDetBase extends VLimiteSpesaDetKey implements Keyed {
 
//    DS_ELEMENTO_VOCE VARCHAR(100)
	private java.lang.String ds_elemento_voce;
 
//    IMPORTO_LIMITE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_limite;
 
//    IMPEGNI_ASSUNTI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal impegni_assunti;
 
//    IMPORTO_DISPONIBILE DECIMAL(22,0)
	private java.lang.Long importo_disponibile;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_LIMITE_SPESA_DET
	 **/
	public VLimiteSpesaDetBase() {
		super();
	}
	public VLimiteSpesaDetBase(java.lang.Integer esercizio, java.lang.String cdCds, java.lang.String tiAppartenenza, java.lang.String tiGestione, java.lang.String cdElementoVoce, java.lang.String fonte) {
		super(esercizio, cdCds, tiAppartenenza, tiGestione, cdElementoVoce, fonte);
	}
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ds_elemento_voce]
	 **/
	public java.lang.String getDs_elemento_voce() {
		return ds_elemento_voce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ds_elemento_voce]
	 **/
	public void setDs_elemento_voce(java.lang.String ds_elemento_voce)  {
		this.ds_elemento_voce=ds_elemento_voce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importo_limite]
	 **/
	public java.math.BigDecimal getImporto_limite() {
		return importo_limite;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importo_limite]
	 **/
	public void setImporto_limite(java.math.BigDecimal importo_limite)  {
		this.importo_limite=importo_limite;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [impegni_assunti]
	 **/
	public java.math.BigDecimal getImpegni_assunti() {
		return impegni_assunti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [impegni_assunti]
	 **/
	public void setImpegni_assunti(java.math.BigDecimal impegni_assunti)  {
		this.impegni_assunti=impegni_assunti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [importo_disponibile]
	 **/
	public java.lang.Long getImporto_disponibile() {
		return importo_disponibile;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [importo_disponibile]
	 **/
	public void setImporto_disponibile(java.lang.Long importo_disponibile)  {
		this.importo_disponibile=importo_disponibile;
	}
}