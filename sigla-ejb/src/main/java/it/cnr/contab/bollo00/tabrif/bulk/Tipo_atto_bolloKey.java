package it.cnr.contab.bollo00.tabrif.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Tipo_atto_bolloKey extends OggettoBulk implements KeyedPersistent {
	private static final long serialVersionUID = 1L;

	private java.lang.Integer id;

	public Tipo_atto_bolloKey() {
		super();
	}
	
	public Tipo_atto_bolloKey(java.lang.Integer id) {
		super();
		this.id = id;
	}

	public boolean equals(Object o) {
		if (this == o) 
			return true;
		if (!(o instanceof Tipo_atto_bolloKey)) 
			return false;
		Tipo_atto_bolloKey k = (Tipo_atto_bolloKey)o;
		if(!compareKey(getId(),k.getId())) 
			return false;
		return true;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) 
			return true;
		if (!(o instanceof Tipo_atto_bolloKey)) 
			return false;
		Tipo_atto_bolloKey k = (Tipo_atto_bolloKey)o;
		if(!compareKey(getId(),k.getId())) 
			return false;
		return true;
	}

	public java.lang.Integer getId() {
		return id;
	}
	
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public int hashCode() {
		return
			calculateKeyHashCode(getId());
	}

	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getId());
	}
}
