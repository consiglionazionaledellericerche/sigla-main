/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 19/02/2009
 */
package it.cnr.contab.compensi00.docs.bulk;
import it.cnr.jada.persistency.Keyed;
public class Bonus_condizioniBase extends Bonus_condizioniKey implements Keyed {
//    DS_CONDIZIONE VARCHAR(100) NOT NULL
	private java.lang.String ds_condizione;
 
//    NUMERO_COMPONENTI DECIMAL(3,0)
	private java.lang.Integer numero_componenti;
 
//    IM_REDDITO_LIMITE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_reddito_limite;
 
//    IM_BONUS DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_bonus;
 
	public Bonus_condizioniBase() {
		super();
	}
	public Bonus_condizioniBase(java.lang.Integer esercizio, java.lang.String cd_condizione) {
		super(esercizio, cd_condizione);
	}
	public java.lang.String getDs_condizione() {
		return ds_condizione;
	}
	public void setDs_condizione(java.lang.String ds_condizione)  {
		this.ds_condizione=ds_condizione;
	}
	public java.lang.Integer getNumero_componenti() {
		return numero_componenti;
	}
	public void setNumero_componenti(java.lang.Integer numero_componenti)  {
		this.numero_componenti=numero_componenti;
	}
	public java.math.BigDecimal getIm_reddito_limite() {
		return im_reddito_limite;
	}
	public void setIm_reddito_limite(java.math.BigDecimal im_reddito_limite)  {
		this.im_reddito_limite=im_reddito_limite;
	}
	public java.math.BigDecimal getIm_bonus() {
		return im_bonus;
	}
	public void setIm_bonus(java.math.BigDecimal im_bonus)  {
		this.im_bonus=im_bonus;
	}
}