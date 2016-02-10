/*
* Creted by Generator 1.0
* Date 18/02/2005
*/
package it.cnr.contab.prevent01.bulk;

import it.cnr.jada.persistency.Keyed;

public class Pdg_missioneBase extends Pdg_missioneKey implements Keyed {
	private static final long serialVersionUID = 1L;

//    DS_MISSIONE VARCHAR(300) NOT NULL
	private java.lang.String ds_missione;
 
	public Pdg_missioneBase() {
		super();
	}
	
	public Pdg_missioneBase(java.lang.String cd_missione) {
		super(cd_missione);
	}
	
	public java.lang.String getDs_missione () {
		return ds_missione;
	}
	
	public void setDs_missione(java.lang.String ds_missione)  {
		this.ds_missione=ds_missione;
	}
}