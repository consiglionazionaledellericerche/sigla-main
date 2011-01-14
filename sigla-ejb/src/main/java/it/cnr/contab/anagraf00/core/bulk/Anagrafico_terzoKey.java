package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Anagrafico_terzoKey extends OggettoBulk implements KeyedPersistent {
	// CD_ANAG DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer cd_anag;

	// CD_TERZO DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer cd_terzo;

	// TI_LEGAME VARCHAR(5) NOT NULL (PK)
	private java.lang.String ti_legame;

	public Anagrafico_terzoKey() {
		super();
	}

	public Anagrafico_terzoKey(java.lang.Integer cd_anag, java.lang.Integer cd_terzo, java.lang.String ti_legame) {
		this.cd_anag = cd_anag;
		this.cd_terzo = cd_terzo;
		this.ti_legame = ti_legame;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Anagrafico_terzoKey)) return false;
		Anagrafico_terzoKey k = (Anagrafico_terzoKey)o;
		if(!compareKey(getCd_anag(),k.getCd_anag())) return false;
		if(!compareKey(getCd_terzo(),k.getCd_terzo())) return false;
		if(!compareKey(getTi_legame(),k.getTi_legame())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getCd_anag())+
			calculateKeyHashCode(getCd_terzo())+
			calculateKeyHashCode(getTi_legame());
	}
	/* 
	 * Getter dell'attributo cd_anag
	 */
	public java.lang.Integer getCd_anag() {
		return cd_anag;
	}
	/* 
	 * Getter dell'attributo cd_terzo
	 */
	public java.lang.Integer getCd_terzo() {
		return cd_terzo;
	}
	/* 
	 * Getter dell'attributo ti_legame
	 */
	public java.lang.String getTi_legame() {
		return ti_legame;
	}
	/* 
	 * Setter dell'attributo cd_anag
	 */
	public void setCd_anag(java.lang.Integer cd_anag) {
		this.cd_anag = cd_anag;
	}
	/* 
	 * Setter dell'attributo cd_terzo
	 */
	public void setCd_terzo(java.lang.Integer cd_terzo) {
		this.cd_terzo = cd_terzo;
	}
	/* 
	 * Setter dell'attributo ti_legame
	 */
	public void setTi_legame(java.lang.String ti_legame) {
		this.ti_legame = ti_legame;
	}
}
