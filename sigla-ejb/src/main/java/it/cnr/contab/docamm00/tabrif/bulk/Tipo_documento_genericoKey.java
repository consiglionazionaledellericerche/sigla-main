package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Tipo_documento_genericoKey extends OggettoBulk implements KeyedPersistent {
	private static final long serialVersionUID = 1L;

	private java.lang.Integer id;

	public Tipo_documento_genericoKey() {
		super();
	}
	
	public Tipo_documento_genericoKey(java.lang.Integer id) {
		super();
		this.id = id;
	}

	public boolean equals(Object o) {
		if (this == o) 
			return true;
		if (!(o instanceof Tipo_documento_genericoKey)) 
			return false;
		Tipo_documento_genericoKey k = (Tipo_documento_genericoKey)o;
		if(!compareKey(getId(),k.getId())) 
			return false;
		return true;
	}

	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) 
			return true;
		if (!(o instanceof Tipo_documento_genericoKey)) 
			return false;
		Tipo_documento_genericoKey k = (Tipo_documento_genericoKey)o;
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
