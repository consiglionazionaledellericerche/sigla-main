package it.cnr.contab.bollo00.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Atto_bolloKey extends OggettoBulk implements KeyedPersistent {
	private static final long serialVersionUID = 1L;

	private java.lang.Integer id;

	public Atto_bolloKey() {
		super();
	}
	
	public Atto_bolloKey(java.lang.Integer id) {
		super();
		this.id = id;
	}

	public boolean equals(Object o) {
		if (this == o) 
			return true;
		if (!(o instanceof Atto_bolloKey)) 
			return false;
		Atto_bolloKey k = (Atto_bolloKey)o;
		if(!compareKey(getId(),k.getId())) 
			return false;
		return true;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) 
			return true;
		if (!(o instanceof Atto_bolloKey)) 
			return false;
		Atto_bolloKey k = (Atto_bolloKey)o;
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
