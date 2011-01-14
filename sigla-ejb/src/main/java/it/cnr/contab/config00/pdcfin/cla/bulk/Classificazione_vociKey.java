/*
* Created by Generator 1.0
* Date 29/08/2005
*/
package it.cnr.contab.config00.pdcfin.cla.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class Classificazione_vociKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer id_classificazione;
	public Classificazione_vociKey() {
		super();
	}
	public Classificazione_vociKey(java.lang.Integer id_classificazione) {
		super();
		this.id_classificazione=id_classificazione;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof Classificazione_vociKey)) return false;
		Classificazione_vociKey k = (Classificazione_vociKey) o;
		if (!compareKey(getId_classificazione(), k.getId_classificazione())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getId_classificazione());
		return i;
	}
	public void setId_classificazione(java.lang.Integer id_classificazione)  {
		this.id_classificazione=id_classificazione;
	}
	public void setId_classificazione(java.math.BigDecimal id_classificazione) {
		this.id_classificazione = new java.lang.Integer(id_classificazione.intValue());
	}
	public java.lang.Integer getId_classificazione () {
		return id_classificazione;
	}
}