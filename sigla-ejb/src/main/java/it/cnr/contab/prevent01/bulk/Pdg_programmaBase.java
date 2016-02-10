/*
* Creted by Generator 1.0
* Date 18/02/2005
*/
package it.cnr.contab.prevent01.bulk;

import it.cnr.jada.persistency.Keyed;

public class Pdg_programmaBase extends Pdg_programmaKey implements Keyed {
	private static final long serialVersionUID = 1L;

//    DS_PROGRAMMA VARCHAR(300) NOT NULL
	private java.lang.String ds_programma;
 
	public Pdg_programmaBase() {
		super();
	}
	
	public Pdg_programmaBase(java.lang.String cd_programma) {
		super(cd_programma);
	}
	
	public java.lang.String getDs_programma () {
		return ds_programma;
	}
	
	public void setDs_programma(java.lang.String ds_programma)  {
		this.ds_programma=ds_programma;
	}
}