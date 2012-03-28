/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 27/11/2006
 */
package it.cnr.contab.progettiric00.geco.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Geco_modulo_psKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Long id_mod;
	private java.lang.Long esercizio;
	private java.lang.String fase;
	public Geco_modulo_psKey() {
		super();
	}
	public Geco_modulo_psKey(java.lang.Long id_mod, java.lang.Long esercizio, java.lang.String fase) {
		super();
		this.id_mod=id_mod;
		this.esercizio=esercizio;
		this.fase=fase;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Geco_modulo_psKey)) return false;
		Geco_modulo_psKey k = (Geco_modulo_psKey) o;
		if (!compareKey(getId_mod(), k.getId_mod())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getFase(), k.getFase())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getId_mod());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getFase());
		return i;
	}
	public void setId_mod(java.lang.Long id_mod)  {
		this.id_mod=id_mod;
	}
	public java.lang.Long getId_mod() {
		return id_mod;
	}
	public void setEsercizio(java.lang.Long esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Long getEsercizio() {
		return esercizio;
	}
	public void setFase(java.lang.String fase)  {
		this.fase=fase;
	}
	public java.lang.String getFase() {
		return fase;
	}
}