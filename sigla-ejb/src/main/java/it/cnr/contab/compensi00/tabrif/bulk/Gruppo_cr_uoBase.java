/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 17/07/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Gruppo_cr_uoBase extends Gruppo_cr_uoKey implements Keyed {
//    FL_ACCENTRATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_accentrato;
 
	public Gruppo_cr_uoBase() {
		super();
	}
	public Gruppo_cr_uoBase(java.lang.Integer esercizio, java.lang.String cd_gruppo_cr, java.lang.String cd_unita_organizzativa) {
		super(esercizio, cd_gruppo_cr, cd_unita_organizzativa);
	}
	public java.lang.Boolean getFl_accentrato() {
		return fl_accentrato;
	}
	public void setFl_accentrato(java.lang.Boolean fl_accentrato)  {
		this.fl_accentrato=fl_accentrato;
	}
}