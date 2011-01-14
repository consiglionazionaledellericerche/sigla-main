/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 16/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ass_tipo_cori_voce_epBase extends Ass_tipo_cori_voce_epKey implements Keyed {
//    CD_VOCE_EP VARCHAR(45) NOT NULL
	private java.lang.String cd_voce_ep;
 
//    CD_VOCE_EP_CONTR VARCHAR(45) NOT NULL
	private java.lang.String cd_voce_ep_contr;
 
	public Ass_tipo_cori_voce_epBase() {
		super();
	}
	public Ass_tipo_cori_voce_epBase(java.lang.Integer esercizio, java.lang.String cd_contributo_ritenuta, java.lang.String ti_ente_percepiente, java.lang.String sezione) {
		super(esercizio, cd_contributo_ritenuta, ti_ente_percepiente, sezione);
	}
	public java.lang.String getCd_voce_ep() {
		return cd_voce_ep;
	}
	public void setCd_voce_ep(java.lang.String cd_voce_ep)  {
		this.cd_voce_ep=cd_voce_ep;
	}
	public java.lang.String getCd_voce_ep_contr() {
		return cd_voce_ep_contr;
	}
	public void setCd_voce_ep_contr(java.lang.String cd_voce_ep_contr)  {
		this.cd_voce_ep_contr=cd_voce_ep_contr;
	}
}