package it.cnr.contab.docamm00.tabrif.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Voce_ivaKey extends OggettoBulk implements KeyedPersistent {
	// CD_VOCE_IVA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_voce_iva;

	/* 
	 * Getter dell'attributo cd_voce_iva
	 */
	public java.lang.String getCd_voce_iva() {
		return cd_voce_iva;
	}
	
	/* 
	 * Setter dell'attributo cd_voce_iva
	 */
	public void setCd_voce_iva(java.lang.String cd_voce_iva) {
		this.cd_voce_iva = cd_voce_iva;
	}
	
	public Voce_ivaKey() {
		super();
	}
	
	
	public Voce_ivaKey(java.lang.String cd_voce_iva) {
		this.cd_voce_iva = cd_voce_iva;
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Voce_ivaKey)) return false;
		Voce_ivaKey k = (Voce_ivaKey)o;
		if(!compareKey(getCd_voce_iva(),k.getCd_voce_iva())) return false;
		return true;
	}
	
	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getCd_voce_iva());
	}
}