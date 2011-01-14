/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 11/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ass_tipo_cori_evBase extends Ass_tipo_cori_evKey implements Keyed {
//    CD_ELEMENTO_VOCE VARCHAR(45) NOT NULL
	private java.lang.String cd_elemento_voce;
 
	public Ass_tipo_cori_evBase() {
		super();
	}
	public Ass_tipo_cori_evBase(java.lang.Integer esercizio, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String ti_ente_percepiente, java.lang.String cd_contributo_ritenuta) {
		super(esercizio, ti_appartenenza, ti_gestione, ti_ente_percepiente, cd_contributo_ritenuta);
	}
	public java.lang.String getCd_elemento_voce() {
		return cd_elemento_voce;
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce)  {
		this.cd_elemento_voce=cd_elemento_voce;
	}
}