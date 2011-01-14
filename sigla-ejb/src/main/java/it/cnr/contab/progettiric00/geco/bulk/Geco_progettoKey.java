/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 27/11/2006
 */
package it.cnr.contab.progettiric00.geco.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Geco_progettoKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Long id_prog;
	private java.lang.Long esercizio;
	private java.lang.String fase;
	public Geco_progettoKey() {
		super();
	}
	public Geco_progettoKey(java.lang.Long id_prog, java.lang.Long esercizio, java.lang.String fase) {
		super();
		this.id_prog=id_prog;
		this.esercizio=esercizio;
		this.fase=fase;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Geco_progettoKey)) return false;
		Geco_progettoKey k = (Geco_progettoKey) o;
		if (!compareKey(getId_prog(), k.getId_prog())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getFase(), k.getFase())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getId_prog());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getFase());
		return i;
	}
	public void setId_prog(java.lang.Long id_prog)  {
		this.id_prog=id_prog;
	}
	public java.lang.Long getId_prog() {
		return id_prog;
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