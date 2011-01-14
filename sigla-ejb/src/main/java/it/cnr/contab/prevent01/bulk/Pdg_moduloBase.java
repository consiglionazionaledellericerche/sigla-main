/*
* Created by Generator 1.0
* Date 19/09/2005
*/
package it.cnr.contab.prevent01.bulk;
import it.cnr.jada.persistency.Keyed;
public class Pdg_moduloBase extends Pdg_moduloKey implements Keyed {
//    STATO CHAR(1)
	private java.lang.String stato;
 
	public Pdg_moduloBase() {
		super();
	}
	public Pdg_moduloBase(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_progetto) {
		super(esercizio, cd_centro_responsabilita, pg_progetto);
	}
	public java.lang.String getStato () {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
}