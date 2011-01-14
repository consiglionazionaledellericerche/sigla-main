/*
* Created by Generator 1.0
* Date 25/05/2005
*/
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ass_pdg_variazione_cdrBase extends Ass_pdg_variazione_cdrKey implements Keyed {
//    IM_ENTRATA DECIMAL(15,2)
	private java.math.BigDecimal im_entrata;
 
//    IM_SPESA DECIMAL(15,2)
	private java.math.BigDecimal im_spesa;
 
	public Ass_pdg_variazione_cdrBase() {
		super();
	}
	public Ass_pdg_variazione_cdrBase(java.lang.Integer esercizio, java.lang.Long pg_variazione_pdg, java.lang.String cd_centro_responsabilita) {
		super(esercizio, pg_variazione_pdg, cd_centro_responsabilita);
	}
	public java.math.BigDecimal getIm_entrata () {
		return im_entrata;
	}
	public void setIm_entrata(java.math.BigDecimal im_entrata)  {
		this.im_entrata=im_entrata;
	}
	public java.math.BigDecimal getIm_spesa () {
		return im_spesa;
	}
	public void setIm_spesa(java.math.BigDecimal im_spesa)  {
		this.im_spesa=im_spesa;
	}
}