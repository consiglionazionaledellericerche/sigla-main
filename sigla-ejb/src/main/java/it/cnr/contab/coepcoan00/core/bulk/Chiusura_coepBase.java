/*
* Created by Generator 1.0
* Date 17/05/2005
*/
package it.cnr.contab.coepcoan00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class Chiusura_coepBase extends Chiusura_coepKey implements Keyed {
//    STATO CHAR(1) NOT NULL
	private java.lang.String stato;
 
	public Chiusura_coepBase() {
		super();
	}
	public Chiusura_coepBase(java.lang.String cd_cds, java.lang.Integer esercizio) {
		super(cd_cds, esercizio);
	}
	public java.lang.String getStato () {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
}