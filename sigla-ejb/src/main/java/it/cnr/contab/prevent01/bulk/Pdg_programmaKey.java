/*
* Creted by Generator 1.0
* Date 18/02/2005
*/
package it.cnr.contab.prevent01.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Pdg_programmaKey extends OggettoBulk implements KeyedPersistent {
	private static final long serialVersionUID = 1L;

	private java.lang.String cd_programma;
	
	public Pdg_programmaKey() {
		super();
	}
	
	public Pdg_programmaKey(java.lang.String cd_programma) {
		super();
		this.cd_programma=cd_programma;
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Pdg_programmaKey)) return false;
		Pdg_programmaKey k = (Pdg_programmaKey) o;
		if (!compareKey(getCd_programma(), k.getCd_programma())) return false;
		return true;
	}
	
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_programma());
		return i;
	}
	
	public void setCd_programma(java.lang.String cd_programma)  {
		this.cd_programma=cd_programma;
	}
	
	public java.lang.String getCd_programma () {
		return cd_programma;
	}
}