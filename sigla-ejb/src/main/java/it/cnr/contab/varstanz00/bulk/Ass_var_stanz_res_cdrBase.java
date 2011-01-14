/*
* Created by Generator 1.0
* Date 15/02/2006
*/
package it.cnr.contab.varstanz00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ass_var_stanz_res_cdrBase extends Ass_var_stanz_res_cdrKey implements Keyed {
//    IM_SPESA DECIMAL(15,2)
	private java.math.BigDecimal im_spesa;
 
	public Ass_var_stanz_res_cdrBase() {
		super();
	}
	public Ass_var_stanz_res_cdrBase(java.lang.Integer esercizio, java.lang.Long pg_variazione, java.lang.String cd_centro_responsabilita) {
		super(esercizio, pg_variazione, cd_centro_responsabilita);
	}
	public java.math.BigDecimal getIm_spesa () {
		return im_spesa;
	}
	public void setIm_spesa(java.math.BigDecimal im_spesa)  {
		this.im_spesa=im_spesa;
	}
}