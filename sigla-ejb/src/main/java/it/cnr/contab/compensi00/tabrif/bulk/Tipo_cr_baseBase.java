/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 17/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Tipo_cr_baseBase extends Tipo_cr_baseKey implements Keyed {
//    CD_GRUPPO_CR VARCHAR(10)
	private java.lang.String cd_gruppo_cr;
 
	public Tipo_cr_baseBase() {
		super();
	}
	public Tipo_cr_baseBase(java.lang.Integer esercizio, java.lang.String cd_contributo_ritenuta) {
		super(esercizio, cd_contributo_ritenuta);
	}
	public java.lang.String getCd_gruppo_cr() {
		return cd_gruppo_cr;
	}
	public void setCd_gruppo_cr(java.lang.String cd_gruppo_cr)  {
		this.cd_gruppo_cr=cd_gruppo_cr;
	}
}