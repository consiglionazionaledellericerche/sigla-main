/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/02/2010
 */
package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.persistency.Keyed;

public class Modalita_erogazioneBase extends Modalita_erogazioneKey implements Keyed {
//    DS_MODALITA_EROGAZIONE VARCHAR(100) NOT NULL
	private java.lang.String ds_modalita_erogazione;
 
	public Modalita_erogazioneBase() {
		super();
	}
	public Modalita_erogazioneBase(java.lang.Integer esercizio, java.lang.String cd_modalita_erogazione) {
		super(esercizio, cd_modalita_erogazione);
	}
	public java.lang.String getDs_modalita_erogazione() {
		return ds_modalita_erogazione;
	}
	public void setDs_modalita_erogazione(java.lang.String ds_modalita_erogazione)  {
		this.ds_modalita_erogazione=ds_modalita_erogazione;
	}
}