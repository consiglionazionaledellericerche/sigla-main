/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/02/2010
 */
package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.persistency.Keyed;

public class Modalita_incassoBase extends Modalita_incassoKey implements Keyed {
//    DS_MODALITA_INCASSO VARCHAR(100) NOT NULL
	private java.lang.String ds_modalita_incasso;
 
	public Modalita_incassoBase() {
		super();
	}
	public Modalita_incassoBase(java.lang.Integer esercizio, java.lang.String cd_modalita_incasso) {
		super(esercizio, cd_modalita_incasso);
	}
	public java.lang.String getDs_modalita_incasso() {
		return ds_modalita_incasso;
	}
	public void setDs_modalita_incasso(java.lang.String ds_modalita_incasso)  {
		this.ds_modalita_incasso=ds_modalita_incasso;
	}
}