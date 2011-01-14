/*
* Created by Generator 1.0
* Date 19/10/2005
*/
package it.cnr.contab.prevent01.bulk;
import it.cnr.jada.persistency.Keyed;
public class Pdg_esercizioBase extends Pdg_esercizioKey implements Keyed {
//    STATO CHAR(2)
	private java.lang.String stato;
 
	public Pdg_esercizioBase() {
		super();
	}
	public Pdg_esercizioBase(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita) {
		super(esercizio, cd_centro_responsabilita);
	}
	public java.lang.String getStato () {
		return stato;
	}
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
}