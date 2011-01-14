/*
* Created by Generator 1.0
* Date 23/06/2006
*/
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class Obbligazione_mod_voceBase extends Obbligazione_mod_voceKey implements Keyed {
//    IM_MODIFICA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_modifica;
 
	public Obbligazione_mod_voceBase() {
		super();
	}
	public Obbligazione_mod_voceBase(java.lang.String cd_cds, java.lang.Integer esercizio, java.lang.Long pg_modifica, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_voce, java.lang.String cd_centro_responsabilita, java.lang.String cd_linea_attivita) {
		super(cd_cds, esercizio, pg_modifica, ti_appartenenza, ti_gestione, cd_voce, cd_centro_responsabilita, cd_linea_attivita);
	}
	public java.math.BigDecimal getIm_modifica () {
		return im_modifica;
	}
	public void setIm_modifica(java.math.BigDecimal im_modifica)  {
		this.im_modifica=im_modifica;
	}
}