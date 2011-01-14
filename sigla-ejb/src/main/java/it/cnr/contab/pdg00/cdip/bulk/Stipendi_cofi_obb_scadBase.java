/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 28/09/2006
 */
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.persistency.Keyed;
public class Stipendi_cofi_obb_scadBase extends Stipendi_cofi_obb_scadKey implements Keyed {
//    IM_TOTALE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_totale;
 
	public Stipendi_cofi_obb_scadBase() {
		super();
	}
	public Stipendi_cofi_obb_scadBase(java.lang.Integer esercizio, java.lang.Integer mese, java.lang.String cd_cds_obbligazione, java.lang.Integer esercizio_obbligazione, java.lang.Integer esercizio_ori_obbligazione, java.lang.Long pg_obbligazione) {
		super(esercizio, mese, cd_cds_obbligazione, esercizio_obbligazione, esercizio_ori_obbligazione, pg_obbligazione);
	}
	public java.math.BigDecimal getIm_totale() {
		return im_totale;
	}
	public void setIm_totale(java.math.BigDecimal im_totale)  {
		this.im_totale=im_totale;
	}
}