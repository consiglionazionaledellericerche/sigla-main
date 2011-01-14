/*
* Created by Generator 1.0
* Date 30/05/2005
*/
package it.cnr.contab.pdg00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Pdg_residuoBase extends Pdg_residuoKey implements Keyed {
//    STATO CHAR(1) NOT NULL
	private java.lang.String stato;
 
//    IM_MASSA_SPENDIBILE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_massa_spendibile;
 
	public Pdg_residuoBase() {
		super();
	}
	public Pdg_residuoBase(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita) {
		super(esercizio, cd_centro_responsabilita);
	}
	public java.lang.String getStato () {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	public java.math.BigDecimal getIm_massa_spendibile () {
		return im_massa_spendibile;
	}
	public void setIm_massa_spendibile(java.math.BigDecimal im_massa_spendibile)  {
		this.im_massa_spendibile=im_massa_spendibile;
	}
}