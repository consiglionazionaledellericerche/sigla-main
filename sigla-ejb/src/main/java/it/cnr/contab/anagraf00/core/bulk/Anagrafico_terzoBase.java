package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.persistency.*;

public class Anagrafico_terzoBase extends Anagrafico_terzoKey implements Keyed {
	// DT_CANC TIMESTAMP
	private java.sql.Timestamp dt_canc;

	public Anagrafico_terzoBase() {
		super();
	}

	public Anagrafico_terzoBase(java.lang.Integer cd_anag, java.lang.Integer cd_terzo, java.lang.String ti_legame) {
		super(cd_anag, cd_terzo, ti_legame);
	}
	/* 
	 * Getter dell'attributo dt_canc
	 */
	public java.sql.Timestamp getDt_canc() {
		return dt_canc;
	}
	/* 
	 * Setter dell'attributo dt_canc
	 */
	public void setDt_canc(java.sql.Timestamp dt_canc) {
		this.dt_canc = dt_canc;
	}
}
