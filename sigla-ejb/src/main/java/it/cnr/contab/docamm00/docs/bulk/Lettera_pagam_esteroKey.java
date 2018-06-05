package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Lettera_pagam_esteroKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	@StoragePolicy(name="P:strorg:cds", property=@StorageProperty(name="strorgcds:codice"))
	private java.lang.String cd_cds;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	@StoragePolicy(name="P:strorg:uo", property=@StorageProperty(name="strorguo:codice"))
	private java.lang.String cd_unita_organizzativa;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	@StorageProperty(name="doccont:esercizioDoc")
	private java.lang.Integer esercizio;

	// PG_LETTERA DECIMAL(10,0) NOT NULL (PK)
	@StorageProperty(name="doccont:numDoc")
	private java.lang.Long pg_lettera;

	public Lettera_pagam_esteroKey() {
		super();
	}
	public Lettera_pagam_esteroKey(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_lettera) {
		super();
		this.cd_cds = cd_cds;
		this.cd_unita_organizzativa = cd_unita_organizzativa;
		this.esercizio = esercizio;
		this.pg_lettera = pg_lettera;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Lettera_pagam_esteroKey)) return false;
		Lettera_pagam_esteroKey k = (Lettera_pagam_esteroKey)o;
		if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
		if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		if(!compareKey(getPg_lettera(),k.getPg_lettera())) return false;
		return true;
	}
	/* 
	 * Getter dell'attributo cd_cds
	 */
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	/* 
	 * Getter dell'attributo cd_unita_organizzativa
	 */
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}
	/* 
	 * Getter dell'attributo esercizio
	 */
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	/* 
	 * Getter dell'attributo pg_lettera
	 */
	public java.lang.Long getPg_lettera() {
		return pg_lettera;
	}
	public int primaryKeyHashCode() {
		return
				calculateKeyHashCode(getCd_cds())+
				calculateKeyHashCode(getCd_unita_organizzativa())+
				calculateKeyHashCode(getEsercizio())+
				calculateKeyHashCode(getPg_lettera());
	}
	/* 
	 * Setter dell'attributo cd_cds
	 */
	public void setCd_cds(java.lang.String cd_cds) {
		this.cd_cds = cd_cds;
	}
	/* 
	 * Setter dell'attributo cd_unita_organizzativa
	 */
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.cd_unita_organizzativa = cd_unita_organizzativa;
	}
	/* 
	 * Setter dell'attributo esercizio
	 */
	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}
	/* 
	 * Setter dell'attributo pg_lettera
	 */
	public void setPg_lettera(java.lang.Long pg_lettera) {
		this.pg_lettera = pg_lettera;
	}
}
