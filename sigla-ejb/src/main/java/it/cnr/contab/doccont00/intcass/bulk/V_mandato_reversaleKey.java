package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class V_mandato_reversaleKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30)
	@StoragePolicy(name="P:strorg:cds", property=@StorageProperty(name="strorgcds:codice"))
	private java.lang.String cd_cds;

	// CD_TIPO_DOCUMENTO_CONT VARCHAR(10)
	@StorageProperty(name="doccont:tipo")
	private java.lang.String cd_tipo_documento_cont;

	// ESERCIZIO DECIMAL(4,0)
	@StorageProperty(name="doccont:esercizioDoc")
	private java.lang.Integer esercizio;

	// PG_DOCUMENTO_CONT DECIMAL(10,0)
	@StorageProperty(name="doccont:numDoc")
	private java.lang.Long pg_documento_cont;

	public V_mandato_reversaleKey() {
		super();
	}
	public V_mandato_reversaleKey( Integer esercizio, String cd_tipo_documento_cont, String cd_cds, Long pg_documento_cont) {
		this.esercizio = esercizio;
		this.cd_tipo_documento_cont = cd_tipo_documento_cont;
		this.cd_cds = cd_cds ;
		this.pg_documento_cont = pg_documento_cont;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (29/04/2002 15.51.30)
	 * @return java.lang.String
	 */
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (29/04/2002 15.51.30)
	 * @return java.lang.String
	 */
	public java.lang.String getCd_tipo_documento_cont() {
		return cd_tipo_documento_cont;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (29/04/2002 15.51.30)
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (29/04/2002 15.51.30)
	 * @return java.lang.Long
	 */
	public java.lang.Long getPg_documento_cont() {
		return pg_documento_cont;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (29/04/2002 15.51.30)
	 * @param newCd_cds java.lang.String
	 */
	public void setCd_cds(java.lang.String newCd_cds) {
		cd_cds = newCd_cds;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (29/04/2002 15.51.30)
	 * @param newCd_tipo_documento_cont java.lang.String
	 */
	public void setCd_tipo_documento_cont(java.lang.String newCd_tipo_documento_cont) {
		cd_tipo_documento_cont = newCd_tipo_documento_cont;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (29/04/2002 15.51.30)
	 * @param newEsercizio java.lang.Integer
	 */
	public void setEsercizio(java.lang.Integer newEsercizio) {
		esercizio = newEsercizio;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (29/04/2002 15.51.30)
	 * @param newPg_documento_cont java.lang.Long
	 */
	public void setPg_documento_cont(java.lang.Long newPg_documento_cont) {
		pg_documento_cont = newPg_documento_cont;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof V_mandato_reversaleKey)) return false;
		V_mandato_reversaleKey k = (V_mandato_reversaleKey)o;
		if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		if(!compareKey(getCd_tipo_documento_cont(),k.getCd_tipo_documento_cont())) return false;
		if(!compareKey(getPg_documento_cont(),k.getPg_documento_cont())) return false;
		return true;
	}

	public int primaryKeyHashCode() {
		return
				calculateKeyHashCode(getCd_cds())+
						calculateKeyHashCode(getEsercizio())+
						calculateKeyHashCode(getCd_tipo_documento_cont())+
						calculateKeyHashCode(getPg_documento_cont());
	}
	@Override
	public boolean equals(Object obj) {
		return equalsByPrimaryKey(obj);
	}
}